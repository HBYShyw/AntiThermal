package com.oplus.lockscreen;

import android.app.OplusActivityManager;
import android.os.RemoteException;
import android.util.Log;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusLockScreenManager {
    private static final String TAG = "OplusLockScreenManager";
    private static volatile OplusLockScreenManager sInstance;
    private OplusActivityManager mOAms = new OplusActivityManager();

    public static OplusLockScreenManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusLockScreenManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusLockScreenManager();
                }
            }
        }
        return sInstance;
    }

    private OplusLockScreenManager() {
    }

    public void setPackagesState(Map<String, Integer> packageMap) {
        try {
            this.mOAms.setPackagesState(packageMap);
        } catch (RemoteException e) {
            Log.e(TAG, "setPackagesState remoteException ");
            e.printStackTrace();
        }
    }

    public boolean registerLockScreenCallback(IOplusLockScreenCallback callback) {
        try {
            return this.mOAms.registerLockScreenCallback(callback);
        } catch (RemoteException e) {
            Log.e(TAG, "registerLockScreenCallback remoteException ");
            e.printStackTrace();
            return false;
        }
    }

    public boolean unregisterLockScreenCallback(IOplusLockScreenCallback callback) {
        try {
            return this.mOAms.unregisterLockScreenCallback(callback);
        } catch (RemoteException e) {
            Log.e(TAG, "unRegisterLockScreenCallback remoteException ");
            e.printStackTrace();
            return false;
        }
    }
}
