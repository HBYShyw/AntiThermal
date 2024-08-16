package com.oplus.datasync;

import android.app.OplusActivityManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusDataSyncManager {
    public static final String MODULE_INTERCEPT_SCREEN_WINDOW = "module_intercept_screen_window";
    private static final String TAG = "OplusDataSyncManager";
    private static volatile OplusDataSyncManager sInstance;
    private OplusActivityManager mOAms = new OplusActivityManager();

    public static OplusDataSyncManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusDataSyncManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusDataSyncManager();
                }
            }
        }
        return sInstance;
    }

    private OplusDataSyncManager() {
    }

    public boolean updateAppData(String module, Bundle bundle) {
        try {
            return this.mOAms.updateAppData(module, bundle);
        } catch (RemoteException e) {
            Log.e(TAG, "updateAppData remoteException ");
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerSysStateChangeObserver(String module, ISysStateChangeCallback observer) {
        try {
            return this.mOAms.registerSysStateChangeObserver(module, observer);
        } catch (RemoteException e) {
            Log.e(TAG, "registerSysStateChangeObserver remoteException ");
            e.printStackTrace();
            return false;
        }
    }

    public boolean unregisterSysStateChangeObserver(String module, ISysStateChangeCallback observer) {
        try {
            return this.mOAms.unregisterSysStateChangeObserver(module, observer);
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterSysStateChangeObserver remoteException ");
            e.printStackTrace();
            return false;
        }
    }
}
