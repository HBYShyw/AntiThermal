package com.oplus.network.heartbeat;

import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.network.heartbeat.IHeartbeat;

/* loaded from: classes.dex */
public final class HeartbeatManager {
    public static final String HEARTBEATMANAGER_SERVICE = "heartbeat";
    private static final String TAG = "HeartbeatManager";
    private static volatile HeartbeatManager sInstance;
    private static IHeartbeat sService;

    private HeartbeatManager() {
        if (sService == null) {
            try {
                sService = IHeartbeat.Stub.asInterface(ServiceManager.getServiceOrThrow(HEARTBEATMANAGER_SERVICE));
            } catch (ServiceManager.ServiceNotFoundException e) {
                Log.e(TAG, "failed to get service!");
            }
        }
    }

    public static HeartbeatManager getInstance() {
        if (sInstance == null) {
            synchronized (HeartbeatManager.class) {
                if (sInstance == null) {
                    sInstance = new HeartbeatManager();
                }
            }
        }
        return sInstance;
    }

    public boolean isHeartbeatAvailabel() {
        try {
            return sService.isHeartbeatAvailabel();
        } catch (Exception e) {
            Log.e(TAG, "failed to call isHeartbeatAvailabel, e=" + e);
            return false;
        }
    }

    public boolean isHeartbeatDynamicCycleEnabled() {
        try {
            return sService.isHeartbeatDynamicCycleEnabled();
        } catch (Exception e) {
            Log.e(TAG, "failed to call isHeartbeatDynamicCycleEnabled, e=" + e);
            return false;
        }
    }

    public String establishHeartbeat(HeartbeatSettings settings, HeartbeatListener listener) {
        try {
            return sService.establishHeartbeat(settings, listener.mCallback);
        } catch (Exception e) {
            Log.e(TAG, "failed to call establishHeartbeat, e=" + e);
            return HeartbeatStream.PROXY_KEY_NONE;
        }
    }

    public boolean stopHeartbeat(String proxyKey) {
        if (TextUtils.isEmpty(proxyKey)) {
            Log.e(TAG, "failed to call stopHeartbeat, proxy key is null!");
            return false;
        }
        try {
            sService.stopHeartbeat(proxyKey);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "failed to call stopHeartbeat, e=" + e);
            return false;
        }
    }

    public boolean pauseHeartbeat(String proxyKey) {
        if (TextUtils.isEmpty(proxyKey)) {
            Log.e(TAG, "failed to call pauseHeartbeat, proxy key is null!");
            return false;
        }
        try {
            sService.pauseHeartbeat(proxyKey);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "failed to call pauseHeartbeat, e=" + e);
            return false;
        }
    }

    public boolean resumeHeartbeat(String proxyKey) {
        if (TextUtils.isEmpty(proxyKey)) {
            Log.e(TAG, "failed to call resumeHeartbeat, proxy key is null!");
            return false;
        }
        try {
            sService.resumeHeartbeat(proxyKey);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "failed to call resumeHeartbeat, e=" + e);
            return false;
        }
    }
}
