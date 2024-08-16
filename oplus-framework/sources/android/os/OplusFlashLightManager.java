package android.os;

import android.os.IOplusService;
import android.util.Log;

/* loaded from: classes.dex */
public final class OplusFlashLightManager {
    private static final boolean DEBUG = true;
    public static final String SERVICE_NAME = "additionald";
    private static final String TAG = "OplusFlashLightManager";
    private static OplusFlashLightManager mInstance = null;
    private static IOplusService sService;

    private OplusFlashLightManager() {
        sService = IOplusService.Stub.asInterface(ServiceManager.getService("additionald"));
        Log.d(TAG, "get service res:" + (sService != null));
    }

    public static OplusFlashLightManager getOplusFlashLightManager() {
        synchronized (OplusFlashLightManager.class) {
            if (mInstance == null) {
                mInstance = new OplusFlashLightManager();
            }
        }
        return mInstance;
    }

    public boolean openFlashLight() {
        try {
            IOplusService iOplusService = sService;
            if (iOplusService != null) {
                return iOplusService.openFlashLight();
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "openFlashLight failed.", e);
            return false;
        }
    }

    public boolean closeFlashLight() {
        try {
            IOplusService iOplusService = sService;
            if (iOplusService != null) {
                return iOplusService.closeFlashLight();
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "closeFlashLight failed.", e);
            return false;
        }
    }

    public String getFlashLightState() {
        try {
            IOplusService iOplusService = sService;
            return iOplusService != null ? iOplusService.getFlashLightState() : "";
        } catch (RemoteException e) {
            Log.e(TAG, "getFlashLightState failed.", e);
            return null;
        }
    }
}
