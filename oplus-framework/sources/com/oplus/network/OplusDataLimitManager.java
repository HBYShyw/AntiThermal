package com.oplus.network;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.network.IOplusDataLimit;

/* loaded from: classes.dex */
public class OplusDataLimitManager {
    public static final int DATA_LIMIT_EVENT_OPT_DONE = 3;
    public static final int DATA_LIMIT_EVENT_START = 1;
    public static final int DATA_LIMIT_EVENT_STOP = 2;
    public static final int DATA_LIMIT_STATE_IDLE = 0;
    public static final int DATA_LIMIT_STATE_RUNNING = 1;
    public static final int DATA_LIMIT_STATE_WAITING = 2;
    public static final boolean DBG = true;
    public static final String SRV_NAME = "oplusdatalimit";
    public static final String TAG = "OplusDataLimitManager";
    private static OplusDataLimitManager sInstance;
    private Context mContext;
    private IOplusDataLimit mOplusDataLimitService;

    private IOplusDataLimit getLimitService() {
        IOplusDataLimit iOplusDataLimit;
        synchronized (OplusDataLimitManager.class) {
            if (this.mOplusDataLimitService == null) {
                IOplusDataLimit asInterface = IOplusDataLimit.Stub.asInterface(ServiceManager.getService(SRV_NAME));
                this.mOplusDataLimitService = asInterface;
                if (asInterface == null) {
                    Log.e(TAG, "mOplusDataLimitService init failed!");
                }
            }
            iOplusDataLimit = this.mOplusDataLimitService;
        }
        return iOplusDataLimit;
    }

    private OplusDataLimitManager() {
        getLimitService();
    }

    public static OplusDataLimitManager getInstance() {
        OplusDataLimitManager oplusDataLimitManager;
        synchronized (OplusDataLimitManager.class) {
            if (sInstance == null) {
                sInstance = new OplusDataLimitManager();
            }
            oplusDataLimitManager = sInstance;
        }
        return oplusDataLimitManager;
    }

    public int getDataLimitState(int netId) {
        try {
            return getLimitService().getDataLimitState(netId);
        } catch (Exception e) {
            Log.e(TAG, "getDataLimitState failed!", e);
            return -1;
        }
    }

    public void registerDataLimitEvent(IDataLimitEventCb cb) {
        try {
            getLimitService().registerDataLimitEvent(cb);
        } catch (Exception e) {
            Log.e(TAG, "getDataLimitState failed!", e);
        }
    }

    public void unregisterDataLimitEvent(IDataLimitEventCb cb) {
        try {
            getLimitService().unregisterDataLimitEvent(cb);
        } catch (Exception e) {
            Log.e(TAG, "getDataLimitState failed!", e);
        }
    }

    public boolean setThermalDataLimit(int netId, long rxSpeed, long txSpeed) {
        try {
            return getLimitService().setThermalDataLimit(netId, rxSpeed, txSpeed);
        } catch (Exception e) {
            Log.e(TAG, "setThermalDataLimit failed!", e);
            return false;
        }
    }

    public boolean clearThermalDataLimit(int netId) {
        try {
            return getLimitService().clearThermalDataLimit(netId);
        } catch (Exception e) {
            Log.e(TAG, "clearThermalDataLimit failed!", e);
            return false;
        }
    }

    public boolean setGlobalDataLimit(int netId, long rxSpeed, long txSpeed) {
        try {
            return getLimitService().setGlobalDataLimit(netId, rxSpeed, txSpeed);
        } catch (Exception e) {
            Log.e(TAG, "setGlobalDataLimit failed!", e);
            return false;
        }
    }

    public boolean clearGlobalDataLimit(int netId) {
        try {
            return getLimitService().clearGlobalDataLimit(netId);
        } catch (Exception e) {
            Log.e(TAG, "clearGlobalDataLimit failed!", e);
            return false;
        }
    }

    public boolean setGlobalDataLimitWithModule(int netId, long rxSpeed, long txSpeed, String moduleName) {
        try {
            return getLimitService().setGlobalDataLimitWithModule(netId, rxSpeed, txSpeed, moduleName);
        } catch (Exception e) {
            Log.e(TAG, "setGlobalDataLimit failed!", e);
            return false;
        }
    }

    public boolean clearGlobalDataLimitWithModule(int netId, String moduleName) {
        try {
            return getLimitService().clearGlobalDataLimitWithModule(netId, moduleName);
        } catch (Exception e) {
            Log.e(TAG, "clearGlobalDataLimit failed!", e);
            return false;
        }
    }
}
