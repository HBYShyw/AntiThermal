package com.oplus.nwpower;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.nwpower.IOSysNetControl;

/* loaded from: classes.dex */
public final class OSysNetControlManager {
    public static final String OSYSNETCONTROL_SERVICE = "osysnetcontrol";
    private static final String TAG = "OSysNetControlManager";
    private static volatile OSysNetControlManager sInstance;
    private final IOSysNetControl mService;

    private OSysNetControlManager(IOSysNetControl service) {
        this.mService = service;
    }

    public static OSysNetControlManager getInstance() {
        OSysNetControlManager oSysNetControlManager;
        if (sInstance == null) {
            synchronized (OSysNetControlManager.class) {
                if (sInstance == null) {
                    try {
                        sInstance = new OSysNetControlManager(IOSysNetControl.Stub.asInterface(ServiceManager.getServiceOrThrow(OSYSNETCONTROL_SERVICE)));
                    } catch (ServiceManager.ServiceNotFoundException e) {
                        Log.w(TAG, "ServiceNotFoundException:" + e.toString());
                    }
                }
                oSysNetControlManager = sInstance;
            }
            return oSysNetControlManager;
        }
        return sInstance;
    }

    public void setDataEnabled(boolean enable) {
        try {
            this.mService.setDataEnabled(enable);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not set data enabled = " + enable);
            throw e.rethrowFromSystemServer();
        }
    }

    public void setWifiEnabled(boolean enable) {
        try {
            this.mService.setWifiEnabled(enable);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not set wifi enabled = " + enable);
            throw e.rethrowFromSystemServer();
        }
    }
}
