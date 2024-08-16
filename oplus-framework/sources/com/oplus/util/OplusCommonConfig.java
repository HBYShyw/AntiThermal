package com.oplus.util;

import android.app.OplusActivityManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusCommonConfig {
    private static final String TAG = "OplusCommonConfig";
    public static final int TO_AMS = 1;
    public static final int TO_PMS = 2;
    private static volatile OplusCommonConfig sConfig = null;
    private OplusActivityManager mOppoAm;

    private OplusCommonConfig() {
        this.mOppoAm = null;
        this.mOppoAm = new OplusActivityManager();
    }

    public static OplusCommonConfig getInstance() {
        if (sConfig == null) {
            synchronized (OplusCommonConfig.class) {
                if (sConfig == null) {
                    sConfig = new OplusCommonConfig();
                }
            }
        }
        return sConfig;
    }

    public boolean putConfigInfo(String configName, Bundle bundle, int flag) {
        if (this.mOppoAm == null) {
            this.mOppoAm = new OplusActivityManager();
        }
        OplusActivityManager oplusActivityManager = this.mOppoAm;
        if (oplusActivityManager != null) {
            try {
                return oplusActivityManager.putConfigInfo(configName, bundle, flag, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.e(TAG, "putConfigInfo " + configName + " failed!");
                return false;
            }
        }
        return false;
    }

    public boolean putConfigInfoAsUser(String configName, Bundle bundle, int flag, int userId) {
        if (this.mOppoAm == null) {
            this.mOppoAm = new OplusActivityManager();
        }
        OplusActivityManager oplusActivityManager = this.mOppoAm;
        if (oplusActivityManager != null) {
            try {
                return oplusActivityManager.putConfigInfo(configName, bundle, flag, userId);
            } catch (RemoteException e) {
                Log.e(TAG, "putConfigInfoAsUser " + configName + " failed!");
                return false;
            }
        }
        return false;
    }

    public Bundle getConfigInfo(String configName, int flag) {
        if (this.mOppoAm == null) {
            this.mOppoAm = new OplusActivityManager();
        }
        OplusActivityManager oplusActivityManager = this.mOppoAm;
        if (oplusActivityManager != null) {
            try {
                return oplusActivityManager.getConfigInfo(configName, flag, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.e(TAG, "getConfigInfo " + configName + " failed!");
                return null;
            }
        }
        return null;
    }

    public Bundle getConfigInfoAsUser(String configName, int flag, int userId) {
        if (this.mOppoAm == null) {
            this.mOppoAm = new OplusActivityManager();
        }
        OplusActivityManager oplusActivityManager = this.mOppoAm;
        if (oplusActivityManager != null) {
            try {
                return oplusActivityManager.getConfigInfo(configName, flag, userId);
            } catch (RemoteException e) {
                Log.e(TAG, "getConfigInfoAsUser " + configName + " failed!");
                return null;
            }
        }
        return null;
    }
}
