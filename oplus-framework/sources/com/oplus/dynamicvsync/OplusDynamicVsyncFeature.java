package com.oplus.dynamicvsync;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.dynamicvsync.IOplusDynamicVsync;

/* loaded from: classes.dex */
public class OplusDynamicVsyncFeature implements IOplusDynamicVsyncFeature {
    private static final String OPLUS_DYNAMICVSYNC_SERVICE_NAME = "oplusdynamicvsync";
    private static final String TAG = "DynamicVsyncClient";
    private IOplusDynamicVsync mOplusDynamicVsyncService;
    private static final boolean mOplusDebug = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.dynamicvsync.debug", false);

    private OplusDynamicVsyncFeature() {
        this.mOplusDynamicVsyncService = null;
        if (DEBUG) {
            Log.d(TAG, "OplusDynamicVsyncFeature create");
        }
    }

    /* loaded from: classes.dex */
    private static class InstanceHolder {
        static final OplusDynamicVsyncFeature INSTANCE = new OplusDynamicVsyncFeature();

        private InstanceHolder() {
        }
    }

    public static OplusDynamicVsyncFeature getInstance() {
        OplusDynamicVsyncFeature instance = InstanceHolder.INSTANCE;
        return instance;
    }

    @Override // com.oplus.dynamicvsync.IOplusDynamicVsyncFeature
    public void doAnimation(int durationInMs, String detail) {
        if (mOplusDebug) {
            Log.d(TAG, "doAnimation " + durationInMs + ", " + detail);
        }
        getService();
        IOplusDynamicVsync iOplusDynamicVsync = this.mOplusDynamicVsyncService;
        if (iOplusDynamicVsync == null) {
            Log.e(TAG, "failed to get dynamicvsync service:binder null");
            return;
        }
        try {
            iOplusDynamicVsync.doAnimation(durationInMs, detail);
        } catch (RemoteException e) {
            Log.e(TAG, "requestRefreshRate failed!", e);
        }
    }

    @Override // com.oplus.dynamicvsync.IOplusDynamicVsyncFeature
    public void flingEvent(String pkgName, int durationInMs) {
        if (mOplusDebug) {
            Log.d(TAG, "flingEvent " + pkgName + ", " + durationInMs);
        }
        getService();
        IOplusDynamicVsync iOplusDynamicVsync = this.mOplusDynamicVsyncService;
        if (iOplusDynamicVsync == null) {
            Log.e(TAG, "failed to get dynamicvsync service:binder null");
            return;
        }
        try {
            iOplusDynamicVsync.flingEvent(pkgName, durationInMs);
        } catch (RemoteException e) {
            Log.e(TAG, "requestRefreshRate failed!", e);
        }
    }

    private void getService() {
        if (this.mOplusDynamicVsyncService == null) {
            IBinder binder = ServiceManager.getService(OPLUS_DYNAMICVSYNC_SERVICE_NAME);
            if (binder == null) {
                Log.e(TAG, "failed to get dynamicvsync service:binder null");
            } else {
                this.mOplusDynamicVsyncService = IOplusDynamicVsync.Stub.asInterface(binder);
            }
        }
    }
}
