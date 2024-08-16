package com.oplus.touchnode;

import android.app.OplusActivityTaskManager;
import android.os.Binder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.os.IOplusTouchNodeCallback;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OplusTouchNodeManager {
    public static final int BASELINE_RESULT_NODE = 14;
    public static final int BASELINE_TEST_NODE = 3;
    public static final int BLACK_SCREEN_RESULT_NODE = 15;
    public static final int BLACK_SCREEN_TEST_NODE = 13;
    public static final int CALIBRATION_NODE = 4;
    public static final int CHARGE_DETECT_NODE = 10;
    public static final int COORDINATE_NODE = 2;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final int DEBUG_BASELINE_NODE = 18;
    public static final int DEBUG_DELTA_NODE = 17;
    public static final int DEBUG_HEALTH_MONITOR_NODE = 20;
    public static final int DOUBLE_TAP_ENABLE_NODE = 1;
    public static final int DOUBLE_TAP_INDEP_NODE = 21;
    public static final int HEAD_SET_DETECT_NODE = 12;
    public static final int HOVER_SELFDATA_NODE = 19;
    public static final int KERNEL_GRIP_HANDLE_NODE = 8;
    public static final int OPLUS_TP_DIRECTION_NODE = 5;
    public static final int OPLUS_TP_LIMIT_ENABLE_NODE = 7;
    public static final int OPLUS_TP_LIMIT_WHITELIST_NODE = 6;
    public static final int REPORT_RATE_WHITE_LIST_NODE = 9;
    private static final String TAG = "OplusTouchNodeManager";
    public static final int TOUCH_OPTIMIZED_TIME_NODE = 22;
    public static final int TP_AGING_TEST_NODE = 16;
    private static final int TP_DEBUG_DATA_RECORD_NODE = 164;
    private static final int TP_DEBUG_LEVEL_NODE = 160;
    private static final int TP_DEBUG_MAIN_REGISTER_NODE = 161;
    private static final int TP_DEBUG_RESERVE_NODE = 165;
    private static final int TP_DEBUG_SELF_DATA_NODE = 162;
    private static final int TP_DEBUG_SELF_RAW_NODE = 163;
    public static final int WIRELESS_CHARGE_DETECT_NODE = 11;
    private static volatile OplusTouchNodeManager sInstance;
    private final ArrayList<OplusTouchEventCallbackWrapper> mCallbacks = new ArrayList<>();
    private final Object mLock = new Object();
    private OplusActivityTaskManager mOAms = OplusActivityTaskManager.getInstance();
    private OplusTouchNodeServerCallback mServerCallback;

    /* loaded from: classes.dex */
    public interface OplusTouchEventCallback {
        void onTouchNodeChange(int i, long j, int i2, int i3, int i4, String str);

        void onTouchNodeChangeOneWay(int i, long j, int i2, int i3, int i4, String str);
    }

    public static OplusTouchNodeManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusTouchNodeManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusTouchNodeManager();
                }
            }
        }
        return sInstance;
    }

    private OplusTouchNodeManager() {
    }

    public String readNodeFile(int nodeFlag) {
        if (DEBUG) {
            Log.d(TAG, "readNodeFile, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid() + ", nodeFlag: " + nodeFlag);
        }
        try {
            return this.mOAms.readNodeFile(nodeFlag);
        } catch (RemoteException e) {
            Log.e(TAG, "readNodeFile failed, err: " + e);
            return null;
        }
    }

    public String readNodeFileByDevice(int dId, int nodeFlag) {
        if (DEBUG) {
            Log.d(TAG, "readNodeFileByDevice, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid() + ", nodeFlag: " + nodeFlag + ", dId :" + dId);
        }
        try {
            return this.mOAms.readNodeFileByDevice(dId, nodeFlag);
        } catch (RemoteException e) {
            Log.e(TAG, "readNodeFileByDevice failed, err: " + e + ", dId :" + dId);
            return null;
        }
    }

    public boolean writeNodeFile(int nodeFlag, String info) {
        if (DEBUG) {
            Log.d(TAG, "writeNodeFile, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid() + ", nodeFlag: " + nodeFlag + ", info: " + info);
        }
        try {
            return this.mOAms.writeNodeFile(nodeFlag, info);
        } catch (RemoteException e) {
            Log.e(TAG, "writeNodeFile failed, err: " + e);
            return false;
        }
    }

    public boolean writeNodeFileByDevice(int dId, int nodeFlag, String info) {
        if (DEBUG) {
            Log.d(TAG, "writeNodeFileByDevice, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid() + ", nodeFlag: " + nodeFlag + ", info: " + info);
        }
        try {
            return this.mOAms.writeNodeFileByDevice(dId, nodeFlag, info);
        } catch (RemoteException e) {
            Log.e(TAG, "writeNodeFileByDevice failed, err: " + e);
            return false;
        }
    }

    public boolean isTouchNodeSupport(int dId, int nodeFlag) {
        if (DEBUG) {
            Log.d(TAG, "isTouchNodeSupport, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid() + ", nodeFlag: " + nodeFlag + ", dId :" + dId);
        }
        try {
            return this.mOAms.isTouchNodeSupport(dId, nodeFlag);
        } catch (RemoteException e) {
            Log.e(TAG, "isTouchNodeSupport failed, err: " + e + ", dId :" + dId);
            return false;
        }
    }

    public boolean writeNodeFileFromBt(int deviceId, int nodeFlag, String info) {
        if (DEBUG) {
            Log.d(TAG, "writeNodeFileFromBt, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid() + ", nodeFlag: " + nodeFlag + ", info :" + info);
        }
        try {
            return this.mOAms.writeNodeFileFromBt(deviceId, nodeFlag, info);
        } catch (RemoteException e) {
            Log.e(TAG, "writeNodeFileFromBt failed, err: " + e);
            return false;
        }
    }

    public void writeNodeFileOneWay(int deviceId, int nodeFlag, String info) {
        if (DEBUG) {
            Log.d(TAG, "writeNodeFileOneWay, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid() + ", nodeFlag: " + nodeFlag + ", deviceId :" + deviceId + ", info :" + info);
        }
        try {
            this.mOAms.writeNodeFileOneWay(deviceId, nodeFlag, info);
        } catch (RemoteException e) {
            Log.e(TAG, "writeNodeFileOneWay failed, err: " + e);
        }
    }

    public boolean notifyTouchNodeChange(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) {
        if (DEBUG) {
            Log.d(TAG, "notifyTouchNodeChange, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid() + ", time: " + time + ", nodeFlag: " + nodeFlag + ", deviceId :" + deviceId + ", data :" + data + ", info :" + info);
        }
        try {
            return this.mOAms.notifyTouchNodeChange(clientFlag, time, deviceId, nodeFlag, data, info);
        } catch (RemoteException e) {
            Log.e(TAG, "notifyTouchNodeChange failed, err: " + e);
            return false;
        }
    }

    public boolean registerEventCallback(OplusTouchEventCallback callback) {
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "registerEventCallback, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid());
        }
        if (callback == null) {
            if (z) {
                Log.e(TAG, "registerEventCallback callback is null");
            }
            return false;
        }
        synchronized (this.mLock) {
            int index = findCallbackLocked(callback);
            if (index != -1) {
                return false;
            }
            OplusTouchEventCallbackWrapper wrapper = new OplusTouchEventCallbackWrapper(callback);
            this.mCallbacks.add(wrapper);
            if (this.mOAms != null && this.mServerCallback == null) {
                OplusTouchNodeServerCallback oplusTouchNodeServerCallback = new OplusTouchNodeServerCallback();
                this.mServerCallback = oplusTouchNodeServerCallback;
                try {
                    return this.mOAms.registerEventCallback(oplusTouchNodeServerCallback);
                } catch (RemoteException e) {
                    this.mServerCallback = null;
                    synchronized (this.mLock) {
                        this.mCallbacks.clear();
                        Log.d(TAG, "Remote exception in registerEventCallback: ", e);
                    }
                }
            }
            return false;
        }
    }

    public boolean unregisterEventCallback(OplusTouchEventCallback callback) {
        boolean callbackEmpty;
        OplusActivityTaskManager oplusActivityTaskManager;
        OplusTouchNodeServerCallback oplusTouchNodeServerCallback;
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "unregisterEventCallback, uid: " + Binder.getCallingUid() + ", pid: " + Binder.getCallingPid());
        }
        if (callback == null) {
            if (!z) {
                return false;
            }
            Log.e(TAG, "unregisterEventCallback callback is null");
            return false;
        }
        synchronized (this.mLock) {
            int indexToRemove = findCallbackLocked(callback);
            if (indexToRemove != -1) {
                this.mCallbacks.remove(indexToRemove);
            }
            callbackEmpty = this.mCallbacks.isEmpty();
        }
        if (!callbackEmpty || (oplusActivityTaskManager = this.mOAms) == null || (oplusTouchNodeServerCallback = this.mServerCallback) == null) {
            return true;
        }
        try {
            try {
                boolean unregisterEventCallback = oplusActivityTaskManager.unregisterEventCallback(oplusTouchNodeServerCallback);
                this.mServerCallback = null;
                synchronized (this.mLock) {
                    this.mCallbacks.clear();
                }
                return unregisterEventCallback;
            } catch (RemoteException e) {
                Log.d(TAG, "Remote exception in unregisterEventCallback: ", e);
                this.mServerCallback = null;
                synchronized (this.mLock) {
                    this.mCallbacks.clear();
                    return true;
                }
            }
        } catch (Throwable th) {
            this.mServerCallback = null;
            synchronized (this.mLock) {
                this.mCallbacks.clear();
                throw th;
            }
        }
    }

    /* loaded from: classes.dex */
    private final class OplusTouchNodeServerCallback extends IOplusTouchNodeCallback.Stub {
        private OplusTouchNodeServerCallback() {
        }

        @Override // com.oplus.os.IOplusTouchNodeCallback
        public void onTouchNodeChange(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) {
            synchronized (OplusTouchNodeManager.this.mLock) {
                if (!OplusTouchNodeManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusTouchNodeManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusTouchEventCallbackWrapper wrapper = (OplusTouchEventCallbackWrapper) it.next();
                        wrapper.onTouchNodeChange(clientFlag, time, deviceId, nodeFlag, data, info);
                    }
                }
            }
        }

        @Override // com.oplus.os.IOplusTouchNodeCallback
        public void onTouchNodeChangeOneWay(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) {
            synchronized (OplusTouchNodeManager.this.mLock) {
                if (!OplusTouchNodeManager.this.mCallbacks.isEmpty()) {
                    Iterator it = OplusTouchNodeManager.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        OplusTouchEventCallbackWrapper wrapper = (OplusTouchEventCallbackWrapper) it.next();
                        wrapper.onTouchNodeChangeOneWay(clientFlag, time, deviceId, nodeFlag, data, info);
                    }
                }
            }
        }
    }

    private int findCallbackLocked(OplusTouchEventCallback callback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).mOplusTouchEventCallback.equals(callback)) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OplusTouchEventCallbackWrapper {
        private final OplusTouchEventCallback mOplusTouchEventCallback;

        OplusTouchEventCallbackWrapper(OplusTouchEventCallback callback) {
            this.mOplusTouchEventCallback = callback;
        }

        void onTouchNodeChange(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) {
            this.mOplusTouchEventCallback.onTouchNodeChange(clientFlag, time, deviceId, nodeFlag, data, info);
        }

        void onTouchNodeChangeOneWay(int clientFlag, long time, int deviceId, int nodeFlag, int data, String info) {
            this.mOplusTouchEventCallback.onTouchNodeChangeOneWay(clientFlag, time, deviceId, nodeFlag, data, info);
        }
    }
}
