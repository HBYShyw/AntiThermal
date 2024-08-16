package android.os.customize;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeVpnManagerService;
import android.util.Slog;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCustomizeVpnManager {
    private static final String SERVICE_NAME = "OplusCustomizeVpnManagerService";
    private static final String TAG = "OplusCustomizeVpnManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeVpnManager sInstance;
    private IOplusCustomizeVpnManagerService mOplusCustomizeVpnManagerService;

    private OplusCustomizeVpnManager() {
        getOplusCustomizeVpnManagerService();
    }

    public static final OplusCustomizeVpnManager getInstance(Context context) {
        OplusCustomizeVpnManager oplusCustomizeVpnManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeVpnManager();
                }
                oplusCustomizeVpnManager = sInstance;
            }
            return oplusCustomizeVpnManager;
        }
        return sInstance;
    }

    private IOplusCustomizeVpnManagerService getOplusCustomizeVpnManagerService() {
        IOplusCustomizeVpnManagerService iOplusCustomizeVpnManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeVpnManagerService == null) {
                this.mOplusCustomizeVpnManagerService = IOplusCustomizeVpnManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            iOplusCustomizeVpnManagerService = this.mOplusCustomizeVpnManagerService;
        }
        return iOplusCustomizeVpnManagerService;
    }

    public int getVpnServiceState() {
        try {
            int result = getOplusCustomizeVpnManagerService().getVpnServiceState();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getVpnServiceState fail!", e);
            return 0;
        } catch (Exception e2) {
            Slog.e(TAG, "getVpnServiceState Error" + e2);
            return 0;
        }
    }

    public void setVpnDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeVpnManagerService().setVpnDisabled(admin, disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setVpnDisabled Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setVpnDisabled Error" + e2);
        }
    }

    public boolean isVpnDisabled(ComponentName admin) {
        try {
            boolean result = getOplusCustomizeVpnManagerService().isVpnDisabled(admin);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "isVpnDisabled Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isVpnDisabled Error" + e2);
            return false;
        }
    }

    public void setAlwaysOnVpnPackage(ComponentName admin, String vpnPackage, boolean lockdown) {
        try {
            getOplusCustomizeVpnManagerService().setAlwaysOnVpnPackage(admin, vpnPackage, lockdown);
        } catch (RemoteException e) {
            Slog.e(TAG, "setAlwaysOnVpnPackage fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setAlwaysOnVpnPackage Error" + e2);
        }
    }

    public List<String> getVpnList(ComponentName componentName) {
        try {
            List result = getOplusCustomizeVpnManagerService().getVpnList(componentName);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getVpnList fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getVpnList Error" + e2);
            return null;
        }
    }

    public boolean deleteVpnProfile(ComponentName componentName, String key) {
        try {
            boolean result = getOplusCustomizeVpnManagerService().deleteVpnProfile(componentName, key);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "deleteVpnProfile fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "deleteVpnProfile Error" + e2);
            return false;
        }
    }

    public int disestablishVpnConnection(ComponentName admin) {
        try {
            int result = getOplusCustomizeVpnManagerService().disestablishVpnConnection(admin);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "disestablishVpnConnection fail!", e);
            return -1;
        } catch (Exception e2) {
            Slog.e(TAG, "disestablishVpnConnection Error" + e2);
            return -1;
        }
    }

    public String getAlwaysOnVpnPackage(ComponentName admin) {
        try {
            String result = getOplusCustomizeVpnManagerService().getAlwaysOnVpnPackage(admin);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getAlwaysOnVpnPackage fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getAlwaysOnVpnPackage Error" + e2);
            return null;
        }
    }

    public void setVpnAlwaysOnPersis(boolean lockdown) {
        try {
            getOplusCustomizeVpnManagerService().setVpnAlwaysOnPersis(lockdown);
        } catch (RemoteException e) {
            Slog.e(TAG, "setVpnAlwaysOnPersis fail!", e);
        }
    }

    public boolean getVpnAlwaysOnPersis(String defval) {
        try {
            boolean result = getOplusCustomizeVpnManagerService().getVpnAlwaysOnPersis(defval);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getVpnAlwaysOnPersis fail!", e);
            return false;
        }
    }
}
