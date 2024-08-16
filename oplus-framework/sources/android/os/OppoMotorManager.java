package android.os;

import android.content.Context;
import android.util.Slog;
import com.android.internal.app.IOppoMotorManager;

/* loaded from: classes.dex */
public final class OppoMotorManager {
    private static final boolean DEBUG = true;
    public static final int MOTORDOWNED = 0;
    public static final int MOTORDOWNING = -1;
    public static final int MOTORDOWNLOCKED = -5;
    public static final int MOTORERROR = -10;
    public static final int MOTORUPED = 10;
    public static final int MOTORUPING = 1;
    public static final int MOTORUPLOCKED = 5;
    public static final String TAG = "MotorManager";
    private static MotorStateChangedCallback sMotorStateChangedCallback;
    private Context mContext;
    private IOppoMotorManager mService;
    private IBinder mToken = new Binder();

    public OppoMotorManager(Context context, IOppoMotorManager service) {
        this.mContext = context;
        this.mService = service;
        if (service == null) {
            Slog.v("MotorManager", "MotorManagerService was null");
        }
    }

    public static void sendMotorStateChanged(int state) {
        Slog.d("MotorManager", "sendMotorStateChanged");
        if (sMotorStateChangedCallback != null) {
            Slog.d("MotorManager", "callback keyguard StateChanged");
            sMotorStateChangedCallback.onMotorStateChanged(state);
        }
    }

    public void registerMotorStateChangedCallback(MotorStateChangedCallback callback) {
        Slog.d("MotorManager", "registerMotorStateChangedCallback");
        sMotorStateChangedCallback = callback;
    }

    public void unregisterMotorStateChangedCallback() {
        Slog.d("MotorManager", "unregisterMotorStateChangedCallback");
        sMotorStateChangedCallback = null;
    }

    public int getMotorStateBySystemApp() {
        Slog.d("MotorManager", "getMotorStateBySystemApp");
        IOppoMotorManager iOppoMotorManager = this.mService;
        if (iOppoMotorManager != null) {
            try {
                return iOppoMotorManager.getMotorStateBySystemApp();
            } catch (RemoteException e) {
                Slog.w("MotorManager", "Remote exception in motormanager: ", e);
                return -10;
            }
        }
        return -10;
    }

    public int downMotorBySystemApp(String tag) {
        Slog.d("MotorManager", "downMotorBySystemApp");
        if (this.mService != null && tag != null) {
            try {
                Slog.d("MotorManager", "call service downMotorBySystemApp");
                return this.mService.downMotorBySystemApp(tag, this.mToken);
            } catch (RemoteException e) {
                Slog.w("MotorManager", "Remote exception in motormanager: ", e);
                return 0;
            }
        }
        return 0;
    }

    public int upMotorBySystemApp(String tag) {
        Slog.d("MotorManager", "upMotorBySystemApp");
        if (this.mService != null && tag != null) {
            try {
                Slog.d("MotorManager", "call service upMotorBySystemApp");
                return this.mService.upMotorBySystemApp(tag, this.mToken);
            } catch (RemoteException e) {
                Slog.w("MotorManager", "Remote exception in motormanager: ", e);
                return 0;
            }
        }
        return 0;
    }

    public int downMotorByPrivacyApp(String tag, int delayDownTime) {
        Slog.d("MotorManager", "downMotorByPrivacyApp");
        if (this.mService != null && tag != null) {
            try {
                Slog.d("MotorManager", "call service downMotorByPrivacyApp");
                return this.mService.downMotorByPrivacyApp(tag, delayDownTime, this.mToken);
            } catch (RemoteException e) {
                Slog.w("MotorManager", "Remote exception in motormanager: ", e);
                return 0;
            }
        }
        return 0;
    }

    /* loaded from: classes.dex */
    public static abstract class MotorStateChangedCallback {
        public void onMotorStateChanged(int state) {
            Slog.d("MotorManager", "onMotorStateChanged state: " + Integer.toString(state));
        }
    }

    public void breathLedLoopEffect(int effect) {
        Slog.d("MotorManager", "breathLedLoopEffect effect :" + effect);
        if (this.mService != null) {
            try {
                Slog.d("MotorManager", "call service downMotorBybreathLedLoopEffect");
                this.mService.breathLedLoopEffect(effect);
            } catch (RemoteException e) {
                Slog.w("MotorManager", "Remote exception in motormanager: ", e);
            }
        }
    }
}
