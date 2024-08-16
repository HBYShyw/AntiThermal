package com.oplus.app;

import android.app.OplusActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.oplus.app.IOplusFreezeInfoListener;
import com.oplus.app.IOplusHansFreezeManager;
import com.oplus.app.IOplusHansListener;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusHansFreezeManager {
    private static final String BUNDLE_KEY_LIST = "list";
    private static final String BUNDLE_KEY_TYPE = "type";
    public static final int CONFIG_INVALID = 4;
    public static final int FREEZE_INFO_SUPPORT_VERSION = 1;
    public static final String FREEZE_LEVEL = "level";
    public static final int FREEZE_LEVEL_FOUR = 4;
    public static final int FREEZE_LEVEL_ONE = 1;
    public static final int FREEZE_LEVEL_THREE = 3;
    public static final int FREEZE_LEVEL_TWO = 2;
    public static final String FREEZE_TYPE_ADD = "add";
    public static final String FREEZE_TYPE_RM = "rm";
    public static final int INVALID_TIME = -1;
    public static final int NOT_SUPPORT = 2;
    public static final String PACKAGE = "pkg";
    public static final int PARAMETER_INVALID = 3;
    public static final int PERMISSION_DENY = 1;
    private static final int REQUEST_ID_APP_CARD_1 = 1;
    private static final int REQUEST_ID_APP_CARD_2 = 2;
    private static final String SERVICE_NAME = "oplus_freeze";
    private static final String TAG = "OplusHansFreezeManager";
    public static final String UID = "uid";
    private static volatile OplusHansFreezeManager sInstance;
    private final HansAppHandler mHandler;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final long UPDATE_APP_CARD_THRESHOLD_MS = SystemProperties.getLong("persist.sys.hans.appcard.update_threshold", 5000);
    private final ArrayMap<OplusHansCallBack, ColorHansListenerDelegate> mColorHansCallBackMap = new ArrayMap<>();
    private final ArrayMap<FreezeInfoCallBack, OplusFreezeInfoListenerDelegate> mFreezeInfoCallBackMap = new ArrayMap<>();
    private final Object mAppCardLock = new Object();
    private final SparseArray<OplusAppCardProviderInfo> mVisibleAppCardUpdateMap = new SparseArray<>();
    private final SparseArray<OplusAppCardProviderInfo> mVisibleAppCardAddMap = new SparseArray<>();
    private long mUpdateAppCardTime = 0;
    private final OplusActivityManager mOAms = new OplusActivityManager();
    private final IOplusHansFreezeManager mService = IOplusHansFreezeManager.Stub.asInterface(ServiceManager.getService(SERVICE_NAME));

    /* loaded from: classes.dex */
    public interface FreezeInfoCallBack {
        void notifyFreezeInfo(List<OplusFreezeInfo> list);
    }

    /* loaded from: classes.dex */
    public interface OplusHansCallBack {
        void notifyRecordData(Bundle bundle, String str);
    }

    private OplusHansFreezeManager() {
        HandlerThread hansThread = new HandlerThread("HansAppHandler");
        hansThread.start();
        this.mHandler = new HansAppHandler(hansThread.getLooper());
    }

    public static OplusHansFreezeManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusHansFreezeManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusHansFreezeManager();
                }
            }
        }
        return sInstance;
    }

    public boolean registerHansListener(Context context, OplusHansCallBack callBack) {
        OplusActivityManager oplusActivityManager;
        if (callBack == null || context == null) {
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "registerHansListener callBack = " + callBack);
        }
        synchronized (this.mColorHansCallBackMap) {
            if (this.mColorHansCallBackMap.get(callBack) != null) {
                Log.e(TAG, "already register before");
                return false;
            }
            ColorHansListenerDelegate delegate = new ColorHansListenerDelegate(callBack);
            try {
                oplusActivityManager = this.mOAms;
            } catch (RemoteException e) {
                Log.e(TAG, "registerHansListener remoteException " + e);
            }
            if (oplusActivityManager != null) {
                boolean result = oplusActivityManager.registerHansListener(context.getPackageName(), delegate);
                if (result) {
                    this.mColorHansCallBackMap.put(callBack, delegate);
                }
                return result;
            }
            return true;
        }
    }

    public boolean unregisterHansListener(Context context, OplusHansCallBack callBack) {
        if (context == null || callBack == null) {
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "unregisterHansListener callBack = " + callBack);
        }
        synchronized (this.mColorHansCallBackMap) {
            ColorHansListenerDelegate delegate = this.mColorHansCallBackMap.get(callBack);
            if (delegate != null) {
                try {
                    if (this.mOAms != null) {
                        this.mColorHansCallBackMap.remove(delegate);
                        return this.mOAms.unregisterHansListener(context.getPackageName(), delegate);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterHansListener remoteException " + e);
                }
            }
            return true;
        }
    }

    public boolean setAppFreeze(Context context, ArrayList<Bundle> list, String type) {
        if (context == null || list == null || type == null) {
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "setAppFreeze list = " + list + " " + type);
        }
        try {
            if (this.mOAms != null) {
                Bundle data = new Bundle();
                data.putString("type", type);
                data.putParcelableArrayList(BUNDLE_KEY_LIST, list);
                return this.mOAms.setAppFreeze(context.getPackageName(), data);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "registerHansListener remoteException " + e);
        }
        return false;
    }

    /* loaded from: classes.dex */
    private class ColorHansListenerDelegate extends IOplusHansListener.Stub {
        private final OplusHansCallBack mCallBack;

        public ColorHansListenerDelegate(OplusHansCallBack callBack) {
            this.mCallBack = callBack;
        }

        @Override // com.oplus.app.IOplusHansListener
        public void notifyRecordData(Bundle data, String configName) {
            OplusHansCallBack oplusHansCallBack = this.mCallBack;
            if (oplusHansCallBack != null) {
                oplusHansCallBack.notifyRecordData(data, configName);
            }
        }
    }

    /* loaded from: classes.dex */
    private class OplusFreezeInfoListenerDelegate extends IOplusFreezeInfoListener.Stub {
        private final FreezeInfoCallBack mFreezeInfoCallBack;

        public OplusFreezeInfoListenerDelegate(FreezeInfoCallBack freezeInfoCallBack) {
            this.mFreezeInfoCallBack = freezeInfoCallBack;
        }

        @Override // com.oplus.app.IOplusFreezeInfoListener
        public void notifyFreezeInfo(List<OplusFreezeInfo> freeInfoList) {
            FreezeInfoCallBack freezeInfoCallBack = this.mFreezeInfoCallBack;
            if (freezeInfoCallBack != null) {
                freezeInfoCallBack.notifyFreezeInfo(freeInfoList);
            }
        }
    }

    public void enterFastFreezer(Context context, int[] uids, long timeout, String reason) {
        if (context == null) {
            Log.w(TAG, "enterFastFreezer context is null");
            return;
        }
        if (DEBUG) {
            Log.i(TAG, "enterFastFreezer");
        }
        try {
            OplusActivityManager oplusActivityManager = this.mOAms;
            if (oplusActivityManager != null) {
                oplusActivityManager.enterFastFreezer(context.getPackageName(), uids, timeout, reason);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "enterFastFreezer remoteException " + e);
        }
    }

    public void exitFastFreezer(Context context, String reason) {
        if (context == null) {
            Log.w(TAG, "exitFastFreezer context is null");
            return;
        }
        if (DEBUG) {
            Log.i(TAG, "exitFastFreezer");
        }
        try {
            OplusActivityManager oplusActivityManager = this.mOAms;
            if (oplusActivityManager != null) {
                oplusActivityManager.exitFastFreezer(context.getPackageName(), reason);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "exitFastFreezer remoteException " + e);
        }
    }

    public boolean isFrozenByHans(String packageName, int uid) {
        if (DEBUG) {
            Log.i(TAG, "isFrozenByHans");
        }
        try {
            OplusActivityManager oplusActivityManager = this.mOAms;
            if (oplusActivityManager != null) {
                return oplusActivityManager.isFrozenByHans(packageName, uid);
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "isFrozenByHans remoteException " + e);
            return false;
        }
    }

    public SparseArray<Long> getTrafficBytesList(ArrayList<Integer> uids) {
        if (DEBUG) {
            Log.i(TAG, "getTrafficBytesList");
        }
        try {
            OplusActivityManager oplusActivityManager = this.mOAms;
            if (oplusActivityManager != null) {
                return oplusActivityManager.getTrafficBytesList(uids);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getTrafficBytesList remoteException " + e);
        }
        return new SparseArray<>();
    }

    public SparseArray<Long> getTrafficPacketList(ArrayList<Integer> uids) {
        if (DEBUG) {
            Log.i(TAG, "getTrafficPacketList");
        }
        try {
            OplusActivityManager oplusActivityManager = this.mOAms;
            if (oplusActivityManager != null) {
                return oplusActivityManager.getTrafficPacketList(uids);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getTrafficPacketList remoteException " + e);
        }
        return new SparseArray<>();
    }

    public ArrayList<String> getFrozenPackageList() {
        List<String> list;
        if (DEBUG) {
            Log.i(TAG, "getFrozenPackageList");
        }
        try {
            IOplusHansFreezeManager iOplusHansFreezeManager = this.mService;
            if (iOplusHansFreezeManager != null && (list = iOplusHansFreezeManager.getFrozenPackageList()) != null) {
                return new ArrayList<>(list);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getFrozenPackageList remoteException " + e);
        }
        return new ArrayList<>();
    }

    public boolean registerFreezeInfoListener(Context context, FreezeInfoCallBack callBack) {
        IOplusHansFreezeManager iOplusHansFreezeManager;
        if (callBack == null || context == null) {
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "registerFreezeInfoListener caller package name = " + context.getPackageName());
        }
        synchronized (this.mFreezeInfoCallBackMap) {
            if (this.mFreezeInfoCallBackMap.get(callBack) != null) {
                Log.e(TAG, "already register before");
                return false;
            }
            OplusFreezeInfoListenerDelegate delegate = new OplusFreezeInfoListenerDelegate(callBack);
            try {
                iOplusHansFreezeManager = this.mService;
            } catch (RemoteException e) {
                Log.e(TAG, "registerHansListener remoteException " + e);
            }
            if (iOplusHansFreezeManager != null) {
                boolean result = iOplusHansFreezeManager.registerFreezeInfoListener(context.getPackageName(), delegate);
                if (result) {
                    this.mFreezeInfoCallBackMap.put(callBack, delegate);
                }
                return result;
            }
            return true;
        }
    }

    public boolean unregisterFreezeInfoListener(Context context, FreezeInfoCallBack callBack) {
        if (context == null || callBack == null) {
            return false;
        }
        if (DEBUG) {
            Log.i(TAG, "unregisterHansListener callBack = " + callBack);
        }
        synchronized (this.mFreezeInfoCallBackMap) {
            OplusFreezeInfoListenerDelegate delegate = this.mFreezeInfoCallBackMap.get(callBack);
            if (delegate != null) {
                try {
                    IOplusHansFreezeManager iOplusHansFreezeManager = this.mService;
                    if (iOplusHansFreezeManager != null) {
                        boolean result = iOplusHansFreezeManager.unregisterFreezeInfoListener(context.getPackageName(), delegate);
                        if (result) {
                            this.mFreezeInfoCallBackMap.remove(callBack);
                        }
                        return result;
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterHansListener remoteException " + e);
                }
            }
            return false;
        }
    }

    public ArrayList<OplusFreezeInfo> getFreezeInfo(Context context) {
        List<OplusFreezeInfo> list;
        if (context == null) {
            return new ArrayList<>();
        }
        if (DEBUG) {
            Log.i(TAG, "getFreezeInfo" + context.getPackageName());
        }
        try {
            IOplusHansFreezeManager iOplusHansFreezeManager = this.mService;
            if (iOplusHansFreezeManager != null && (list = iOplusHansFreezeManager.getFreezeInfo(context.getPackageName())) != null) {
                return new ArrayList<>(list);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getFreezeInfo remoteException " + e);
        }
        return new ArrayList<>();
    }

    public int getFreezeInfoSupportVersion() {
        return 1;
    }

    public void requestFrozenDelay(Context context, long timeout, String reason, IOplusProtectConnection protectConnection) {
        if (context == null || protectConnection == null) {
            Log.w(TAG, "requestFrozenDelay failed, context or protectConnection is null, please offer the valid info");
            return;
        }
        if (DEBUG) {
            Log.i(TAG, context.getPackageName() + "requestFrozenDelay ");
        }
        try {
            IOplusHansFreezeManager iOplusHansFreezeManager = this.mService;
            if (iOplusHansFreezeManager != null) {
                iOplusHansFreezeManager.requestFrozenDelay(context.getApplicationInfo().uid, context.getPackageName(), timeout, reason, protectConnection);
            }
        } catch (RemoteException e) {
            Log.e(TAG, context.getPackageName() + " requestFrozenDelay failed " + e);
        }
    }

    public void cancelFrozenDelay(Context context) {
        if (context == null) {
            Log.w(TAG, "cancelFrozenDelay failed, context is null, please offer the valid context");
            return;
        }
        if (DEBUG) {
            Log.i(TAG, context.getPackageName() + "cancelFrozenDelay ");
        }
        try {
            IOplusHansFreezeManager iOplusHansFreezeManager = this.mService;
            if (iOplusHansFreezeManager != null) {
                iOplusHansFreezeManager.cancelFrozenDelay(context.getApplicationInfo().uid);
            }
        } catch (RemoteException e) {
            Log.e(TAG, context.getPackageName() + " getFrozenDelayTime failed " + e);
        }
    }

    public long getFrozenDelayTime(Context context) {
        if (context == null) {
            Log.w(TAG, "getFrozenDelayTime failed, context is null, please offer the valid context");
            return -1L;
        }
        try {
            IOplusHansFreezeManager iOplusHansFreezeManager = this.mService;
            if (iOplusHansFreezeManager != null) {
                return iOplusHansFreezeManager.getFrozenDelayTime(context.getApplicationInfo().uid);
            }
        } catch (RemoteException e) {
            Log.e(TAG, context.getPackageName() + " getFrozenDelayTime failed " + e);
        }
        return -1L;
    }

    public void keepBackgroundRunning(Context context, String reason, boolean enable, IOplusProtectConnection protectConnection) {
        if (enable) {
            requestFrozenDelay(context, 0L, reason, protectConnection);
        } else {
            cancelFrozenDelay(context);
        }
    }

    public boolean updateAppCardProvider(Context context, SparseArray<OplusAppCardProviderInfo> visibleAppCardMap, int requestId) {
        if (context == null) {
            Log.w(TAG, "updateAppCardProvider failed, context is null, please offer the valid context");
            return false;
        }
        if (requestId == 2) {
            return handleRequestUpdateAppCard(context, visibleAppCardMap, requestId);
        }
        if (requestId != 1) {
            return false;
        }
        return handleRequestAddAppCard(context, visibleAppCardMap, requestId);
    }

    private void deepCopyAppCardMap(SparseArray<OplusAppCardProviderInfo> copyFromMap, SparseArray<OplusAppCardProviderInfo> copyToMap) {
        if (copyToMap == null || copyFromMap == null) {
            return;
        }
        copyToMap.clear();
        for (int i = 0; i < copyFromMap.size(); i++) {
            int uid = copyFromMap.keyAt(i);
            OplusAppCardProviderInfo info = copyFromMap.valueAt(i);
            OplusAppCardProviderInfo infoCopy = new OplusAppCardProviderInfo(info);
            copyToMap.put(uid, infoCopy);
        }
    }

    private boolean handleRequestUpdateAppCard(Context context, SparseArray<OplusAppCardProviderInfo> visibleAppCardMap, int requestId) {
        boolean z;
        long updateAppCardTime = SystemClock.elapsedRealtime();
        long j = this.mUpdateAppCardTime;
        long j2 = UPDATE_APP_CARD_THRESHOLD_MS;
        if (updateAppCardTime > j + j2) {
            if (DEBUG) {
                Log.d(TAG, "updateAppCardProvider request_update now.");
            }
            this.mHandler.removeMessages(1);
            SparseArray<OplusAppCardProviderInfo> tmpMap = new SparseArray<>();
            deepCopyAppCardMap(visibleAppCardMap, tmpMap);
            ArrayList<OplusAppCardProviderInfo> cardList = getAppCardListFromSaprseArray(tmpMap);
            return updateAppCardProviderInternal(context.getPackageName(), cardList, requestId, true);
        }
        synchronized (this.mAppCardLock) {
            deepCopyAppCardMap(visibleAppCardMap, this.mVisibleAppCardUpdateMap);
            z = DEBUG;
            if (z) {
                Log.d(TAG, "updateAppCardProvider request_update delayed.");
            }
        }
        if (!this.mHandler.hasMessages(1)) {
            Message msg = Message.obtain(this.mHandler, 1, requestId, 0, context.getPackageName());
            this.mHandler.sendMessageDelayed(msg, (this.mUpdateAppCardTime + j2) - updateAppCardTime);
        } else {
            synchronized (this.mAppCardLock) {
                this.mVisibleAppCardAddMap.clear();
                if (z) {
                    Log.d(TAG, "updateAppCardProvider request_update clear add map.");
                }
            }
        }
        return false;
    }

    private boolean handleRequestAddAppCard(Context context, SparseArray<OplusAppCardProviderInfo> visibleAppCardMap, int requestId) {
        SparseArray<OplusAppCardProviderInfo> tmpMap = new SparseArray<>();
        deepCopyAppCardMap(visibleAppCardMap, tmpMap);
        if (this.mHandler.hasMessages(1)) {
            synchronized (this.mAppCardLock) {
                for (int i = 0; i < tmpMap.size(); i++) {
                    int uid = tmpMap.keyAt(i);
                    OplusAppCardProviderInfo info = tmpMap.valueAt(i);
                    info.setUpdateType(1);
                    this.mVisibleAppCardAddMap.put(uid, info);
                }
                if (DEBUG) {
                    Log.d(TAG, "updateAppCardProvider request_add append to update list.");
                }
            }
        }
        ArrayList<OplusAppCardProviderInfo> cardList = getAppCardListFromSaprseArray(tmpMap);
        return updateAppCardProviderInternal(context.getPackageName(), cardList, requestId, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<OplusAppCardProviderInfo> getAppCardListFromSaprseArray(SparseArray<OplusAppCardProviderInfo> visibleAppCardMap) {
        if (visibleAppCardMap == null) {
            return new ArrayList<>();
        }
        ArrayList<OplusAppCardProviderInfo> cardList = new ArrayList<>(visibleAppCardMap.size());
        for (int i = 0; i < visibleAppCardMap.size(); i++) {
            cardList.add(visibleAppCardMap.valueAt(i));
        }
        return cardList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateAppCardProviderInternal(String packageName, ArrayList<OplusAppCardProviderInfo> cardList, int requestId, boolean updateTime) {
        try {
            IOplusHansFreezeManager iOplusHansFreezeManager = this.mService;
            boolean result = iOplusHansFreezeManager != null ? iOplusHansFreezeManager.updateAppCardProvider(packageName, cardList, requestId) : false;
            if (result && updateTime) {
                this.mUpdateAppCardTime = SystemClock.elapsedRealtime();
            }
            if (DEBUG) {
                Log.d(TAG, packageName + " updateAppCardProvider is called, result: " + result + ", requestId: " + requestId + " , list: " + cardList);
            }
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, packageName + " updateAppCardProvider failed " + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class HansAppHandler extends Handler {
        private static final int MSG_UPDATE_APP_CARD_VISIBLE = 1;

        public HansAppHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            ArrayList<OplusAppCardProviderInfo> cardList;
            if (msg == null) {
                return;
            }
            switch (msg.what) {
                case 1:
                    String packageName = (String) msg.obj;
                    int updateType = msg.arg1;
                    synchronized (OplusHansFreezeManager.this.mAppCardLock) {
                        for (int i = 0; i < OplusHansFreezeManager.this.mVisibleAppCardAddMap.size(); i++) {
                            int uid = OplusHansFreezeManager.this.mVisibleAppCardAddMap.keyAt(i);
                            OplusAppCardProviderInfo info = (OplusAppCardProviderInfo) OplusHansFreezeManager.this.mVisibleAppCardAddMap.valueAt(i);
                            OplusHansFreezeManager.this.mVisibleAppCardUpdateMap.put(uid, info);
                        }
                        OplusHansFreezeManager oplusHansFreezeManager = OplusHansFreezeManager.this;
                        cardList = oplusHansFreezeManager.getAppCardListFromSaprseArray(oplusHansFreezeManager.mVisibleAppCardUpdateMap);
                        OplusHansFreezeManager.this.mVisibleAppCardAddMap.clear();
                        OplusHansFreezeManager.this.mVisibleAppCardUpdateMap.clear();
                    }
                    OplusHansFreezeManager.this.updateAppCardProviderInternal(packageName, cardList, updateType, true);
                    return;
                default:
                    return;
            }
        }
    }
}
