package android.os.customize;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeNetworkManagerService;
import android.util.Slog;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCustomizeNetworkManager {
    private static final String SERVICE_NAME = "OplusCustomizeNetworkManagerService";
    private static final String TAG = "OplusCustomizeNetworkManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeNetworkManager sInstance;
    private IOplusCustomizeNetworkManagerService mOplusCustomizeNetworkManagerService;

    private OplusCustomizeNetworkManager() {
        getOplusCustomizeNetworkManagerService();
    }

    public static final OplusCustomizeNetworkManager getInstance(Context context) {
        OplusCustomizeNetworkManager oplusCustomizeNetworkManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeNetworkManager();
                }
                oplusCustomizeNetworkManager = sInstance;
            }
            return oplusCustomizeNetworkManager;
        }
        return sInstance;
    }

    private IOplusCustomizeNetworkManagerService getOplusCustomizeNetworkManagerService() {
        IOplusCustomizeNetworkManagerService iOplusCustomizeNetworkManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeNetworkManagerService == null) {
                this.mOplusCustomizeNetworkManagerService = IOplusCustomizeNetworkManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            iOplusCustomizeNetworkManagerService = this.mOplusCustomizeNetworkManagerService;
        }
        return iOplusCustomizeNetworkManagerService;
    }

    public void setNetworkRestriction(int pattern) {
        try {
            getOplusCustomizeNetworkManagerService().setNetworkRestriction(pattern);
        } catch (RemoteException e) {
            Slog.e(TAG, "setNetworkRestriction Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setNetworkRestriction Error" + e2);
        }
    }

    public void addNetworkRestriction(int pattern, List<String> list) {
        try {
            getOplusCustomizeNetworkManagerService().addNetworkRestriction(pattern, list);
        } catch (RemoteException e) {
            Slog.e(TAG, "addNetworkRestriction Error");
        } catch (Exception e2) {
            Slog.e(TAG, "addNetworkRestriction Error" + e2);
        }
    }

    public void removeNetworkRestriction(int pattern, List<String> list) {
        try {
            getOplusCustomizeNetworkManagerService().removeNetworkRestriction(pattern, list);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeNetworkRestriction Error");
        } catch (Exception e2) {
            Slog.e(TAG, "removeNetworkRestriction Error" + e2);
        }
    }

    public void removeNetworkRestrictionAll(int pattern) {
        try {
            getOplusCustomizeNetworkManagerService().removeNetworkRestrictionAll(pattern);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeNetworkRestrictionAll Error");
        } catch (Exception e2) {
            Slog.e(TAG, "removeNetworkRestrictionAll Error" + e2);
        }
    }

    public List<String> getNetworkRestrictionList(int pattern) {
        try {
            return getOplusCustomizeNetworkManagerService().getNetworkRestrictionList(pattern);
        } catch (RemoteException e) {
            Slog.e(TAG, "getNetworkRestrictionList Error");
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getNetworkRestrictionList Error" + e2);
            return null;
        }
    }

    public void setUserApnMgrPolicies(int mode) {
        try {
            getOplusCustomizeNetworkManagerService().setUserApnMgrPolicies(mode);
        } catch (RemoteException e) {
            Slog.e(TAG, "setUserApnMgrPolicies Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setUserApnMgrPolicies Error" + e2);
        }
    }

    public int getUserApnMgrPolicies() {
        try {
            return getOplusCustomizeNetworkManagerService().getUserApnMgrPolicies();
        } catch (RemoteException e) {
            Slog.e(TAG, "getUserApnMgrPolicies Error");
            return -1;
        } catch (Exception e2) {
            Slog.e(TAG, "getUserApnMgrPolicies Error" + e2);
            return -1;
        }
    }

    public void addAppMeteredDataBlackList(List<String> pkgs) {
        try {
            getOplusCustomizeNetworkManagerService().addAppMeteredDataBlackList(pkgs);
        } catch (RemoteException e) {
            Slog.e(TAG, "addAppMeteredDataBlackList Error");
        } catch (Exception e2) {
            Slog.e(TAG, "addAppMeteredDataBlackList Error");
        }
    }

    public void addAppWlanDataBlackList(List<String> pkgs) {
        try {
            getOplusCustomizeNetworkManagerService().addAppWlanDataBlackList(pkgs);
        } catch (RemoteException e) {
            Slog.e(TAG, "addAppWlanDataBlackList Error");
        } catch (Exception e2) {
            Slog.e(TAG, "addAppWlanDataBlackList Error");
        }
    }

    public void removeAppMeteredDataBlackList(List<String> pkgs) {
        try {
            getOplusCustomizeNetworkManagerService().removeAppMeteredDataBlackList(pkgs);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeAppMeteredDataBlackList Error");
        } catch (Exception e2) {
            Slog.e(TAG, "removeAppMeteredDataBlackList Error");
        }
    }

    public void removeAppWlanDataBlackList(List<String> pkgs) {
        try {
            getOplusCustomizeNetworkManagerService().removeAppWlanDataBlackList(pkgs);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeAppWlanDataBlackList Error");
        } catch (Exception e2) {
            Slog.e(TAG, "removeAppWlanDataBlackList Error");
        }
    }

    public List<String> getAppMeteredDataBlackList() {
        try {
            return getOplusCustomizeNetworkManagerService().getAppMeteredDataBlackList();
        } catch (RemoteException e) {
            Slog.e(TAG, "getAppMeteredDataBlackList Error");
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getAppMeteredDataBlackList Error");
            return null;
        }
    }

    public List<String> getAppWlanDataBlackList() {
        try {
            return getOplusCustomizeNetworkManagerService().getAppWlanDataBlackList();
        } catch (RemoteException e) {
            Slog.e(TAG, "getAppWlanDataBlackList Error");
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getAppWlanDataBlackList Error");
            return null;
        }
    }

    public void setNetworkControlMode(ComponentName componentName, int mode) {
        try {
            getOplusCustomizeNetworkManagerService().setNetworkControlMode(componentName, mode);
        } catch (RemoteException e) {
            Slog.e(TAG, "setNetworkControlMode error! " + e);
        }
    }

    public String getNetworkControlMode(String packageName) {
        try {
            return getOplusCustomizeNetworkManagerService().getNetworkControlMode(packageName);
        } catch (RemoteException e) {
            Slog.e(TAG, "getNetworkControlMode error! " + e);
            return null;
        }
    }

    public int getNetworkRestrictionMode() {
        try {
            int pattern = getOplusCustomizeNetworkManagerService().getNetworkRestrictionMode();
            return pattern;
        } catch (RemoteException e) {
            Slog.e(TAG, "getNetworkRestrictionMode Error");
            return -1;
        } catch (Exception e2) {
            Slog.e(TAG, "getNetworkRestrictionMode Error" + e2);
            return -1;
        }
    }

    public void setDomainRestrictionMode(int mode) {
        try {
            getOplusCustomizeNetworkManagerService().setDomainRestrictionMode(mode);
        } catch (RemoteException e) {
            Slog.e(TAG, "setDomainRestrictionMode Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setDomainRestrictionMode Error" + e2);
        }
    }

    public int getDomainRestrictionMode() {
        try {
            int mode = getOplusCustomizeNetworkManagerService().getDomainRestrictionMode();
            return mode;
        } catch (RemoteException e) {
            Slog.e(TAG, "getDomainRestrictionMode Error");
            return -1;
        } catch (Exception e2) {
            Slog.e(TAG, "getDomainRestrictionMode Error" + e2);
            return -1;
        }
    }

    public void addDomainRestrictionList(int mode, List<String> list) {
        try {
            if (list != null) {
                getOplusCustomizeNetworkManagerService().addDomainRestrictionList(mode, list);
            } else {
                Slog.e(TAG, "addDomainRestrictionList list is null");
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "addDomainRestrictionList Error");
        } catch (Exception e2) {
            Slog.e(TAG, "addDomainRestrictionList Error" + e2);
        }
    }

    public List<String> getDomainRestrictionList(int mode) {
        try {
            return getOplusCustomizeNetworkManagerService().getDomainRestrictionList(mode);
        } catch (RemoteException e) {
            Slog.e(TAG, "getDomainRestrictionList Error");
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getDomainRestrictionList Error" + e2);
            return null;
        }
    }

    public void removeDomainRestrictionList(int mode, List<String> list) {
        try {
            if (list != null) {
                getOplusCustomizeNetworkManagerService().removeDomainRestrictionList(mode, list);
            } else {
                Slog.e(TAG, "removeDomainRestrictionList list is null");
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "removeDomainRestrictionList Error");
        } catch (Exception e2) {
            Slog.e(TAG, "removeDomainRestrictionList Error" + e2);
        }
    }

    public void removeAllDomainRestrictionList(int mode) {
        try {
            getOplusCustomizeNetworkManagerService().removeAllDomainRestrictionList(mode);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeAllDomainRestrictionList Error");
        } catch (Exception e2) {
            Slog.e(TAG, "removeAllDomainRestrictionList Error" + e2);
        }
    }
}
