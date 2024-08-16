package com.oplus.keylayout;

import android.app.OplusActivityTaskManager;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusKeyLayoutManager {
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String TAG = "OplusKeyLayoutManager";
    private static volatile OplusKeyLayoutManager sInstance;
    private OplusActivityTaskManager mOAms = OplusActivityTaskManager.getInstance();

    public static OplusKeyLayoutManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusKeyLayoutManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusKeyLayoutManager();
                }
            }
        }
        return sInstance;
    }

    private OplusKeyLayoutManager() {
    }

    public void setGimbalLaunchPkg(String pkgName) {
        if (DEBUG) {
            Log.i(TAG, "setGimbalLaunchPkg, pkgName: " + pkgName);
        }
        try {
            OplusActivityTaskManager oplusActivityTaskManager = this.mOAms;
            if (oplusActivityTaskManager != null) {
                oplusActivityTaskManager.setGimbalLaunchPkg(pkgName);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "setGimbalLaunchPkg remoteException " + e);
            e.printStackTrace();
        }
    }
}
