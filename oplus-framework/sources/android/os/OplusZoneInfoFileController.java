package android.os;

import android.os.IOplusService;
import android.util.Log;

/* loaded from: classes.dex */
public final class OplusZoneInfoFileController {
    private static final boolean DEBUG = true;
    public static final String SERVICE_NAME = "additionald";
    private static final String TAG = "OplusZoneInfoFileController";
    private static OplusZoneInfoFileController mInstance = null;
    private static IOplusService sService;

    private OplusZoneInfoFileController() {
        sService = IOplusService.Stub.asInterface(ServiceManager.getService("additionald"));
    }

    public static OplusZoneInfoFileController getOplusZoneInfoFileController() {
        if (mInstance == null) {
            mInstance = new OplusZoneInfoFileController();
        }
        return mInstance;
    }

    public boolean copyFile(String destPath, String srcPath) {
        try {
            IOplusService iOplusService = sService;
            if (iOplusService != null) {
                return iOplusService.copyFile(destPath, srcPath);
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "copyFile failed.", e);
            return false;
        }
    }

    public boolean deleteFile(String path) {
        try {
            IOplusService iOplusService = sService;
            if (iOplusService != null) {
                return iOplusService.deleteFile(path);
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "deleteFile failed.", e);
            return false;
        }
    }
}
