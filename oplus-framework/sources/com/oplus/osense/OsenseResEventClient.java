package com.oplus.osense;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.osense.IOsenseResManager;
import com.oplus.osense.eventinfo.EventConfig;
import com.oplus.osense.eventinfo.IOsenseEventCallback;
import com.oplus.osense.eventinfo.OsenseEventCallback;

/* loaded from: classes.dex */
public class OsenseResEventClient {
    private static final String SERVICENAME = "osensemanager";
    private static final String TAG = OsenseResEventClient.class.getSimpleName();
    private static volatile OsenseResEventClient sInstance;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.osense.OsenseResEventClient.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.i(OsenseResEventClient.TAG, "osensemanager binderDied");
            synchronized (OsenseResEventClient.class) {
                OsenseResEventClient.this.mService = null;
            }
        }
    };
    private volatile IOsenseResManager mService;

    private OsenseResEventClient() {
    }

    public static OsenseResEventClient getInstance() {
        if (sInstance == null) {
            synchronized (OsenseResEventClient.class) {
                if (sInstance == null) {
                    sInstance = new OsenseResEventClient();
                }
            }
        }
        return sInstance;
    }

    private synchronized IOsenseResManager getService() {
        if (this.mService == null) {
            IBinder binder = ServiceManager.getService(SERVICENAME);
            if (binder == null) {
                Log.w(TAG, "get osensemanager service failed");
                return null;
            }
            try {
                binder.linkToDeath(this.mDeathRecipient, 0);
                this.mService = IOsenseResManager.Stub.asInterface(binder);
                if (this.mService == null) {
                    Log.e(TAG, "osensemanager asInterface fail");
                }
            } catch (RemoteException e) {
                Log.e(TAG, "linkToDeath fail ", e);
                return null;
            }
        }
        return this.mService;
    }

    public int registerEventCallback(OsenseEventCallback callback, EventConfig eventConfig) {
        try {
            if (callback == null) {
                Log.e(TAG, "registerEventCallback failed because callback is null");
                return -1;
            }
            IOsenseEventCallback ioeCallback = IOsenseEventCallback.Stub.asInterface(callback.asBinder());
            IOsenseResManager service = getService();
            if (service == null) {
                return -1;
            }
            int ret = service.registerEventCallback(ioeCallback, eventConfig);
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, "registerEventCallback failed " + e + ", ret: -1");
            return -1;
        }
    }

    public int unregisterEventCallbackWithConfig(OsenseEventCallback callback, EventConfig eventConfig) {
        try {
            if (callback == null) {
                Log.e(TAG, "unregisterEventCallbackWithConfig failed because callback is null");
                return -1;
            }
            IOsenseEventCallback ioeCallback = IOsenseEventCallback.Stub.asInterface(callback.asBinder());
            IOsenseResManager service = getService();
            if (service == null) {
                return -1;
            }
            int ret = service.unregisterEventCallbackWithConfig(ioeCallback, eventConfig);
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterEventCallbackWithConfig for the event failed " + e + ", ret: -1");
            return -1;
        }
    }

    public int unregisterEventCallback(OsenseEventCallback callback) {
        try {
            if (callback == null) {
                Log.e(TAG, "unregisterEventCallback failed because callback is null");
                return -1;
            }
            IOsenseEventCallback ioeCallback = IOsenseEventCallback.Stub.asInterface(callback.asBinder());
            IOsenseResManager service = getService();
            if (service == null) {
                return -1;
            }
            int ret = service.unregisterEventCallback(ioeCallback);
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterEventCallback for all event failed " + e);
            return -1;
        }
    }

    public void notifyProcessTerminate(int[] pids, String reason) {
        try {
            IOsenseResManager service = getService();
            if (service != null) {
                service.notifyProcessTerminate(pids, reason);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "notifyProcessTerminate for athena failed " + e);
        }
    }

    public void notifyProcessTerminateFinish(OsenseEventCallback callback) {
        try {
            if (callback == null) {
                Log.e(TAG, "notifyProcessTerminateFinish failed because callback is null");
                return;
            }
            IOsenseEventCallback ioeCallback = IOsenseEventCallback.Stub.asInterface(callback.asBinder());
            IOsenseResManager service = getService();
            if (service != null) {
                service.notifyProcessTerminateFinish(ioeCallback);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "notifyProcessTerminateFinish for app failed " + e);
        }
    }

    public void requestSceneAction(Bundle bundle) {
        try {
            IOsenseResManager service = getService();
            if (service != null) {
                service.requestSceneAction(bundle);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "externalReqSceneAction for external caller failed " + e);
        }
    }
}
