package com.oplus.hardware.gameopt;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import vendor.oplus.hardware.gameopt.IGameCallback;
import vendor.oplus.hardware.gameopt.IGameOptHalService;

/* loaded from: classes.dex */
public class OplusGameOptManager {
    private static final int GAME_OPT_DIED_TYPE = 101;
    private static final String TAG = "OplusGameOptManager";
    private static volatile OplusGameOptManager sInstance;
    private static String sServiceName = IGameOptHalService.DESCRIPTOR + "/default";
    private volatile IGameOptHalService mGameOptHalService;
    private final Object mLock = new Object();
    private final ArrayList<OplusGameCallbackWrapper> mCallbacks = new ArrayList<>();
    private GameCallback mServerCallback = null;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.hardware.gameopt.OplusGameOptManager.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.i(OplusGameOptManager.TAG, OplusGameOptManager.sServiceName + " binderDied");
            synchronized (OplusGameOptManager.class) {
                OplusGameOptManager.this.mGameOptHalService = null;
                synchronized (OplusGameOptManager.this.mLock) {
                    if (!OplusGameOptManager.this.mCallbacks.isEmpty()) {
                        Iterator it = OplusGameOptManager.this.mCallbacks.iterator();
                        while (it.hasNext()) {
                            OplusGameCallbackWrapper callback = (OplusGameCallbackWrapper) it.next();
                            callback.onCommonCall("gameopt died", 101);
                        }
                    }
                }
            }
        }
    };

    private OplusGameOptManager() {
    }

    public static OplusGameOptManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusGameOptManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusGameOptManager();
                }
            }
        }
        return sInstance;
    }

    private synchronized IGameOptHalService getService() {
        if (this.mGameOptHalService == null) {
            IBinder binder = ServiceManager.getService(sServiceName);
            if (binder == null) {
                Log.w(TAG, "getService fail." + sServiceName);
                return null;
            }
            try {
                binder.linkToDeath(this.mDeathRecipient, 0);
                this.mGameOptHalService = IGameOptHalService.Stub.asInterface(binder);
                if (this.mGameOptHalService == null) {
                    Log.e(TAG, "asInterface fail.");
                }
            } catch (RemoteException e) {
                Log.e(TAG, "linkToDeath fail ", e);
                return null;
            }
        }
        return this.mGameOptHalService;
    }

    public void notifyGameInfo(int type, String jsonData) {
        try {
            IGameOptHalService service = getService();
            if (service != null) {
                service.notifyGameInfo(type, jsonData);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "notifyGameInfo failed.", e);
        }
    }

    public int notifyTGPAfps(int tFps) {
        try {
            IGameOptHalService service = getService();
            if (service != null) {
                return service.notifyTGPAfps(tFps);
            }
            return -1;
        } catch (RemoteException e) {
            Log.e(TAG, "notifyTGPAfps failed.", e);
            return -1;
        }
    }

    public int registerGameCallback(OplusGameCallback callback) {
        if (callback == null) {
            return -1;
        }
        synchronized (this.mLock) {
            int index = findCallbackLocked(callback);
            if (index != -1) {
                Log.d(TAG, "duplicated callback register");
                return -1;
            }
            OplusGameCallbackWrapper wrapper = new OplusGameCallbackWrapper(callback);
            this.mCallbacks.add(wrapper);
            try {
                IGameOptHalService service = getService();
                if (service != null && this.mServerCallback == null) {
                    GameCallback gameCallback = new GameCallback();
                    this.mServerCallback = gameCallback;
                    return service.setCallback(gameCallback);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "setCallback failed.", e);
            }
            return -1;
        }
    }

    private int findCallbackLocked(OplusGameCallback callback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).mOplusGameCallback.equals(callback)) {
                return i;
            }
        }
        return -1;
    }

    public void setKeyThread(String config) {
        try {
            IGameOptHalService service = getService();
            if (service != null) {
                service.setKeyThread(config);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "setKeyThread failed.", e);
        }
    }

    public int unregisterGameCallback(OplusGameCallback callback) {
        boolean callbackEmpty;
        if (callback == null) {
            return -1;
        }
        Log.i(TAG, "unsetCallback callback = " + callback);
        synchronized (this.mLock) {
            int indexToRemove = findCallbackLocked(callback);
            if (indexToRemove != -1) {
                this.mCallbacks.remove(indexToRemove);
            }
            Log.d(TAG, "callback index " + indexToRemove + " callback size: " + this.mCallbacks.size());
            callbackEmpty = this.mCallbacks.isEmpty();
        }
        if (callbackEmpty) {
            return unsetCallback();
        }
        return 0;
    }

    private int unsetCallback() {
        GameCallback gameCallback;
        try {
            try {
                IGameOptHalService service = getService();
                if (service != null && (gameCallback = this.mServerCallback) != null) {
                    if (service.unsetCallback(gameCallback) == 0) {
                        this.mServerCallback = null;
                        synchronized (this.mLock) {
                            this.mCallbacks.clear();
                        }
                        return 0;
                    }
                }
                this.mServerCallback = null;
                synchronized (this.mLock) {
                    this.mCallbacks.clear();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "unsetCallback failed.", e);
                this.mServerCallback = null;
                synchronized (this.mLock) {
                    this.mCallbacks.clear();
                }
            }
            return -1;
        } catch (Throwable th) {
            this.mServerCallback = null;
            synchronized (this.mLock) {
                this.mCallbacks.clear();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class GameCallback extends IGameCallback.Stub {
        private GameCallback() {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onNotify(String info) {
            Log.d(OplusGameOptManager.TAG, " callback invocked info:" + info);
            synchronized (OplusGameOptManager.this.mLock) {
                if (!OplusGameOptManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusGameOptManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusGameCallbackWrapper wrapper = (OplusGameCallbackWrapper) it.next();
                        wrapper.onNotify(info);
                    }
                }
            }
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onReadTop(String info) {
            Log.d(OplusGameOptManager.TAG, "onReadTop callback invocked info:" + info);
            synchronized (OplusGameOptManager.this.mLock) {
                if (!OplusGameOptManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusGameOptManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusGameCallbackWrapper wrapper = (OplusGameCallbackWrapper) it.next();
                        wrapper.onReadTop(info);
                    }
                }
            }
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onFrameLimit(String info) {
            Log.d(OplusGameOptManager.TAG, "onFrameLimit callback invocked info:" + info);
            synchronized (OplusGameOptManager.this.mLock) {
                if (!OplusGameOptManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusGameOptManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusGameCallbackWrapper wrapper = (OplusGameCallbackWrapper) it.next();
                        wrapper.onFrameLimit(info);
                    }
                }
            }
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onBigDataReport(String info) {
            Log.d(OplusGameOptManager.TAG, "onBigDataReport callback invocked info:" + info);
            synchronized (OplusGameOptManager.this.mLock) {
                if (!OplusGameOptManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusGameOptManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusGameCallbackWrapper wrapper = (OplusGameCallbackWrapper) it.next();
                        wrapper.onBigDataReport(info);
                    }
                }
            }
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onNotifyTemp(String info) {
            Log.d(OplusGameOptManager.TAG, "onNotifyTemp callback invocked info:" + info);
            synchronized (OplusGameOptManager.this.mLock) {
                if (!OplusGameOptManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusGameOptManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusGameCallbackWrapper wrapper = (OplusGameCallbackWrapper) it.next();
                        wrapper.onNotifyTemp(info);
                    }
                }
            }
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onFrameProduce(String info) {
            Log.d(OplusGameOptManager.TAG, "onNotifyTemp callback invocked info:" + info);
            synchronized (OplusGameOptManager.this.mLock) {
                if (!OplusGameOptManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusGameOptManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusGameCallbackWrapper wrapper = (OplusGameCallbackWrapper) it.next();
                        wrapper.onFrameProduce(info);
                    }
                }
            }
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onInputCollect(String info) {
            Log.d(OplusGameOptManager.TAG, "onInputCollect callback invocked info:" + info);
            synchronized (OplusGameOptManager.this.mLock) {
                if (!OplusGameOptManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusGameOptManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusGameCallbackWrapper wrapper = (OplusGameCallbackWrapper) it.next();
                        wrapper.onInputCollect(info);
                    }
                }
            }
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onCommonCall(String info, int type) {
            Log.d(OplusGameOptManager.TAG, "onCommonCall callback invocked info:" + info + " type:" + type);
            synchronized (OplusGameOptManager.this.mLock) {
                if (!OplusGameOptManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusGameOptManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusGameCallbackWrapper wrapper = (OplusGameCallbackWrapper) it.next();
                        wrapper.onCommonCall(info, type);
                    }
                }
            }
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public final int getInterfaceVersion() {
            return 1;
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public final String getInterfaceHash() {
            return "48394e9cdbb0aee7539e513a52c9a972cf2f3fc7";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OplusGameCallbackWrapper {
        private final OplusGameCallback mOplusGameCallback;

        public OplusGameCallbackWrapper(OplusGameCallback mOplusGameCallback) {
            this.mOplusGameCallback = mOplusGameCallback;
        }

        void onNotify(String info) {
            this.mOplusGameCallback.onNotify(info);
        }

        void onReadTop(String info) {
            this.mOplusGameCallback.onReadTop(info);
        }

        void onFrameLimit(String info) {
            this.mOplusGameCallback.onFrameLimit(info);
        }

        void onBigDataReport(String info) {
            this.mOplusGameCallback.onBigDataReport(info);
        }

        void onNotifyTemp(String info) {
            this.mOplusGameCallback.onNotifyTemp(info);
        }

        void onFrameProduce(String info) {
            this.mOplusGameCallback.onFrameProduce(info);
        }

        void onInputCollect(String info) {
            this.mOplusGameCallback.onInputCollect(info);
        }

        void onCommonCall(String info, int type) {
            this.mOplusGameCallback.onCommonCall(info, type);
        }
    }
}
