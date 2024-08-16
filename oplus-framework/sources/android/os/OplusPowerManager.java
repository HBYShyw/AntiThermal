package android.os;

import android.os.IOplusPowerManager;
import android.os.IPowerManager;
import android.util.Singleton;
import android.util.Slog;
import com.oplus.os.IOplusScreenStatusListener;

/* loaded from: classes.dex */
public class OplusPowerManager {
    public static final int GO_TO_SLEEP_REASON_FINGERPRINT = 15;
    private static final Singleton<IOplusPowerManager> INSTANCE = new Singleton<IOplusPowerManager>() { // from class: android.os.OplusPowerManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusPowerManager m161create() {
            try {
                IBinder b = ServiceManager.getService("power");
                IPowerManager pm = IPowerManager.Stub.asInterface(b);
                IOplusPowerManager oplusPowerManager = IOplusPowerManager.Stub.asInterface(pm.asBinder().getExtension());
                return oplusPowerManager;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    };
    private static final String TAG = "OplusPowerManager";
    public static final int WAKE_UP_REASON_FINGERPRINT = 98;

    public static IOplusPowerManager getService() {
        return (IOplusPowerManager) INSTANCE.get();
    }

    public void registerScreenStatusListener(IOplusScreenStatusListener listener) {
        try {
            Slog.d(TAG, "registerScreenStatusListener listener=" + listener);
            getService().registerScreenStatusListener(listener);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterScreenStatusListener(IOplusScreenStatusListener listener) {
        try {
            Slog.d(TAG, "unregisterScreenStatusListener listener=" + listener);
            getService().unregisterScreenStatusListener(listener);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setMinScreenOffTimeout(long timeout) {
        try {
            return getService().setMinScreenOffTimeout(timeout);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public long getMinScreenOffTimeout() {
        try {
            return getService().getMinScreenOffTimeout();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isScreenStayAwake() {
        try {
            return getService().isScreenStayAwake();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void disableScreenStayAwakeOfApp(boolean disable, int uid) {
        try {
            getService().disableScreenStayAwakeOfApp(disable, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean getDisplayAodStatus() {
        try {
            Slog.d(TAG, "OplusPowerManager, getDisplayAodStatus");
            return getService().getDisplayAodStatus();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMinBrightness() {
        try {
            Slog.d(TAG, "OplusPowerManager, getMinBrightness");
            return getService().getMinBrightness();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMaxBrightness() {
        try {
            Slog.d(TAG, "OplusPowerManager, getMaxBrightness");
            return getService().getMaxBrightness();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getDefaultBrightness() {
        try {
            Slog.d(TAG, "OplusPowerManager, getDefaultBrightness");
            return getService().getDefaultBrightness();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public float[] getDisplaysBrightnessByNit(float nit) {
        try {
            Slog.d(TAG, "OplusPowerManager, getDisplaysBrightnessByNit, nit=" + nit);
            return getService().getDisplaysBrightnessByNit(nit);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMinimumScreenBrightnessSetting() {
        try {
            Slog.d(TAG, "OplusPowerManager, getMinimumScreenBrightnessSetting");
            return getService().getMinimumScreenBrightnessSetting();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMaximumScreenBrightnessSetting() {
        try {
            Slog.d(TAG, "OplusPowerManager, getMaximumScreenBrightnessSetting");
            return getService().getMaximumScreenBrightnessSetting();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getDefaultScreenBrightnessSetting() {
        try {
            Slog.d(TAG, "OplusPowerManager, getDefaultScreenBrightnessSetting");
            return getService().getDefaultScreenBrightnessSetting();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setFlashing(int type, int color, int onMS, int offMS, int mode) {
        try {
            Slog.d(TAG, "OplusPowerManager, setFlashing");
            getService().setFlashing(type, color, onMS, offMS, mode);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
