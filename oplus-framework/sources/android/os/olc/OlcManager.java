package android.os.olc;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.olc.IOlcService;
import android.util.Log;

/* loaded from: classes.dex */
public class OlcManager {
    private static final int ERROR_INVOKE_ERROR = -2;
    private static final int ERROR_SERVICE_NOT_INIT = -1;
    private static final int INIT_TRY_TIMES = 3;
    public static final String OLC_SERVICE_NAME = "olc";
    private static final String TAG = "OlcManager";
    private static IOlcService sOlcService;

    private static boolean init() {
        if (sOlcService != null) {
            return true;
        }
        int tryTimes = 3;
        do {
            IOlcService asInterface = IOlcService.Stub.asInterface(ServiceManager.getService(OLC_SERVICE_NAME));
            sOlcService = asInterface;
            if (asInterface != null) {
                return true;
            }
            tryTimes--;
        } while (tryTimes > 0);
        Log.w(TAG, "failed to init olc service");
        return false;
    }

    public static int raiseException(ExceptionInfo exceptionInfo) {
        if (!init()) {
            Log.w(TAG, "olc service not init");
            return -1;
        }
        try {
            return sOlcService.raiseException(exceptionInfo);
        } catch (RemoteException e) {
            Log.e(TAG, "olc raiseException failed " + e);
            sOlcService = null;
            return -2;
        }
    }
}
