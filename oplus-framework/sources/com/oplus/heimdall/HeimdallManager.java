package com.oplus.heimdall;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.heimdall.IHeimdallService;

/* loaded from: classes.dex */
public class HeimdallManager {
    public static final String HEIMDALL_SERVICE = "heimdall";
    private static final String TAG = HeimdallManager.class.getSimpleName();
    private static IBinder.DeathRecipient sDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.heimdall.HeimdallManager.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.e(HeimdallManager.TAG, "HeimdallService binderDied!!!");
            HeimdallManager.sService = null;
        }
    };
    private static HeimdallManager sHeimdallManager = new HeimdallManager();
    private static IHeimdallService sService;

    public static HeimdallManager getInstance() {
        return sHeimdallManager;
    }

    private HeimdallManager() {
        IBinder binder = null;
        if (sService == null) {
            binder = ServiceManager.getService(HEIMDALL_SERVICE);
            sService = IHeimdallService.Stub.asInterface(binder);
        }
        if (binder != null) {
            try {
                binder.linkToDeath(sDeathRecipient, 0);
            } catch (RemoteException e) {
                Log.e(TAG, "exception happened when linkToDeathRecipient : " + e.getMessage());
                sService = null;
            }
        }
    }

    public ICrashService getCrashService() {
        try {
            IHeimdallService iHeimdallService = sService;
            if (iHeimdallService == null) {
                return null;
            }
            return iHeimdallService.getCrashService();
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "failed to getCrashService : " + e.getMessage());
            return null;
        }
    }

    public ITraceService getTraceService() {
        try {
            IHeimdallService iHeimdallService = sService;
            if (iHeimdallService == null) {
                return null;
            }
            return iHeimdallService.getTraceService();
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "failed to getTraceService : " + e.getMessage());
            return null;
        }
    }

    public IRootService getRootService() {
        try {
            IHeimdallService iHeimdallService = sService;
            if (iHeimdallService == null) {
                return null;
            }
            return iHeimdallService.getRootService();
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "failed to getRootService : " + e.getMessage());
            return null;
        }
    }
}
