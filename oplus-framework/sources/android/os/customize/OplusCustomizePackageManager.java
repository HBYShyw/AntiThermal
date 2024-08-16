package android.os.customize;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizePackageManagerService;
import android.util.Slog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusCustomizePackageManager {
    private static final String SERVICE_NAME = "OplusCustomizePackageManagerService";
    private static final String TAG = "OplusCustomizePackageManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizePackageManager sInstance;
    private IOplusCustomizePackageManagerService mIOplusCustomizePackageManagerService;

    private OplusCustomizePackageManager() {
        getOplusCustomizePackageManagerService();
    }

    public static final OplusCustomizePackageManager getInstance(Context context) {
        OplusCustomizePackageManager oplusCustomizePackageManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizePackageManager();
                }
                oplusCustomizePackageManager = sInstance;
            }
            return oplusCustomizePackageManager;
        }
        return sInstance;
    }

    private IOplusCustomizePackageManagerService getOplusCustomizePackageManagerService() {
        IOplusCustomizePackageManagerService iOplusCustomizePackageManagerService;
        synchronized (mServiceLock) {
            if (this.mIOplusCustomizePackageManagerService == null) {
                this.mIOplusCustomizePackageManagerService = IOplusCustomizePackageManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            if (this.mIOplusCustomizePackageManagerService == null) {
                Slog.e(TAG, "mIOplusCustomizePackageManagerService is null");
            }
            iOplusCustomizePackageManagerService = this.mIOplusCustomizePackageManagerService;
        }
        return iOplusCustomizePackageManagerService;
    }

    public void addDisabledDeactivateMdmPackages(List<String> packageNames) {
        try {
            getOplusCustomizePackageManagerService().addDisabledDeactivateMdmPackages(packageNames);
        } catch (RemoteException e) {
            Slog.e(TAG, "addDisabledDeactivateMdmPackages fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "addDisabledDeactivateMdmPackages Error" + e2);
        }
    }

    public void removeDisabledDeactivateMdmPackages(List<String> packageNames) {
        try {
            getOplusCustomizePackageManagerService().removeDisabledDeactivateMdmPackages(packageNames);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeDisabledDeactivateMdmPackages fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "removeDisabledDeactivateMdmPackages Error" + e2);
        }
    }

    public List<String> getDisabledDeactivateMdmPackages() {
        List<String> disabledDeactivateMdmPackagesList = new ArrayList<>();
        try {
            return getOplusCustomizePackageManagerService().getDisabledDeactivateMdmPackages();
        } catch (RemoteException e) {
            Slog.e(TAG, "getDisabledDeactivateMdmPackages fail!", e);
            return disabledDeactivateMdmPackagesList;
        } catch (Exception e2) {
            Slog.e(TAG, "getDisabledDeactivateMdmPackages Error" + e2);
            return disabledDeactivateMdmPackagesList;
        }
    }

    public void removeAllDisabledDeactivateMdmPackages() {
        try {
            getOplusCustomizePackageManagerService().removeAllDisabledDeactivateMdmPackages();
        } catch (RemoteException e) {
            Slog.e(TAG, "removeAllDisabledDeactivateMdmPackages fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "removeAllDisabledDeactivateMdmPackages Error" + e2);
        }
    }

    public boolean isDisabledDeactivateMdmPackage(String adminPackage) {
        try {
            return getOplusCustomizePackageManagerService().isDisabledDeactivateMdmPackage(adminPackage);
        } catch (RemoteException e) {
            Slog.e(TAG, "isDisabledDeactivateMdmPackage fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isDisabledDeactivateMdmPackage Error" + e2);
            return false;
        }
    }

    public void addDisallowedUninstallPackages(List<String> packageNames) {
        try {
            getOplusCustomizePackageManagerService().addDisallowedUninstallPackages(packageNames);
        } catch (RemoteException e) {
            Slog.d(TAG, "addDisallowedUninstallPackages: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "addDisallowedUninstallPackages Error" + e2);
        }
    }

    public void removeDisallowedUninstallPackages(List<String> packageNames) {
        try {
            getOplusCustomizePackageManagerService().removeDisallowedUninstallPackages(packageNames);
        } catch (RemoteException e) {
            Slog.d(TAG, "removeDisallowedUninstallPackages: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "removeDisallowedUninstallPackages Error" + e2);
        }
    }

    public void removeAllDisallowedUninstallPackages() {
        try {
            getOplusCustomizePackageManagerService().removeAllDisallowedUninstallPackages();
        } catch (RemoteException e) {
            Slog.d(TAG, "removeAllDisallowedUninstallPackages: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "removeAllDisallowedUninstallPackages Error" + e2);
        }
    }

    public List<String> getDisallowUninstallPackageList() {
        try {
            List<String> list = getOplusCustomizePackageManagerService().getDisallowUninstallPackageList();
            return list == null ? Collections.emptyList() : list;
        } catch (RemoteException e) {
            Slog.d(TAG, "getDisallowUninstallPackageList: fail", e);
            return Collections.emptyList();
        } catch (Exception e2) {
            Slog.e(TAG, "getDisallowUninstallPackageList Error" + e2);
            return Collections.emptyList();
        }
    }

    public void clearAppData(String packageName) {
        try {
            getOplusCustomizePackageManagerService().clearAppData(packageName);
        } catch (RemoteException e) {
            Slog.e(TAG, "clearApplicationUserData: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "clearApplicationUserData Error" + e2);
        }
    }

    public List<String> getClearAppName() {
        try {
            List<String> list = getOplusCustomizePackageManagerService().getClearAppName();
            return list;
        } catch (RemoteException e) {
            Slog.e(TAG, "getClearAppName: fail", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getClearAppName Error" + e2);
            return null;
        }
    }

    public boolean setCustomizeDefaultApp(String roleName, String packageName) {
        try {
            return getOplusCustomizePackageManagerService().setCustomizeDefaultApp(roleName, packageName);
        } catch (RemoteException e) {
            Slog.e(TAG, "setCustomizeDefaultApp fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setCustomizeDefaultApp Error" + e2);
            return false;
        }
    }

    public String getCustomizeDefaultApp(String roleName) {
        try {
            return getOplusCustomizePackageManagerService().getCustomizeDefaultApp(roleName);
        } catch (RemoteException e) {
            Slog.e(TAG, "getCustomizeDefaultApp fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getCustomizeDefaultApp Error" + e2);
            return null;
        }
    }

    public void removeCustomizeDefaultApp(String roleName) {
        try {
            getOplusCustomizePackageManagerService().removeCustomizeDefaultApp(roleName);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeCustomizeDefaultApp fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "removeCustomizeDefaultApp Error" + e2);
        }
    }

    public void setAdbInstallUninstallDisabled(boolean disabled) {
        try {
            getOplusCustomizePackageManagerService().setAdbInstallUninstallDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setAdbInstallUninstallDisabled fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setAdbInstallUninstallDisabled Error" + e2);
        }
    }

    public boolean getAdbInstallUninstallDisabled() {
        try {
            return getOplusCustomizePackageManagerService().getAdbInstallUninstallDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "getAdbInstallUninstallDisabled fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "getAdbInstallUninstallDisabled Error" + e2);
            return false;
        }
    }

    public void setInstallSysAppBundle(Bundle bundle) {
        try {
            getOplusCustomizePackageManagerService().setInstallSysAppBundle(bundle);
        } catch (RemoteException e) {
            Slog.e(TAG, "setInstallSysAppBundle: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setInstallSysAppBundle Error" + e2);
        }
    }

    public Bundle getInstallSysAppBundle() {
        try {
            return getOplusCustomizePackageManagerService().getInstallSysAppBundle();
        } catch (RemoteException e) {
            Slog.e(TAG, "getInstallSysAppBundle: fail", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getInstallSysAppBundle Error" + e2);
            return null;
        }
    }

    public List<String> getPrivInstallSysAppList() {
        try {
            return getOplusCustomizePackageManagerService().getPrivInstallSysAppList();
        } catch (RemoteException e) {
            Slog.e(TAG, "getPrivInstallSysAppList: fail", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getPrivInstallSysAppList Error" + e2);
            return null;
        }
    }

    public List<String> getDetachableInstallSysAppList() {
        try {
            return getOplusCustomizePackageManagerService().getDetachableInstallSysAppList();
        } catch (RemoteException e) {
            Slog.e(TAG, "getDetachableInstallSysAppList: fail", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getDetachableInstallSysAppList Error" + e2);
            return null;
        }
    }

    public List<String> getAllInstallSysAppList() {
        try {
            return getOplusCustomizePackageManagerService().getAllInstallSysAppList();
        } catch (RemoteException e) {
            Slog.e(TAG, "getAllInstallSysAppList: fail", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getAllInstallSysAppList Error" + e2);
            return null;
        }
    }

    public Map<String, String> getContainOplusCertificatePackages() {
        try {
            return getOplusCustomizePackageManagerService().getContainOplusCertificatePackages();
        } catch (RemoteException e) {
            Slog.e(TAG, "getContainOplusCertificatePackages: fail", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getContainOplusCertificatePackages Error" + e2);
            return null;
        }
    }

    public boolean setSuperWhiteList(List<String> list) {
        try {
            return getOplusCustomizePackageManagerService().setSuperWhiteList(list);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSuperWhiteList fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setSuperWhiteList Error" + e2);
            return false;
        }
    }

    public List<String> getSuperWhiteList() {
        try {
            return getOplusCustomizePackageManagerService().getSuperWhiteList();
        } catch (RemoteException e) {
            Slog.e(TAG, "getSuperWhiteList: fail", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getSuperWhiteList Error" + e2);
            return null;
        }
    }

    public boolean clearSuperWhiteList(List<String> clearList) {
        try {
            return getOplusCustomizePackageManagerService().clearSuperWhiteList(clearList);
        } catch (RemoteException e) {
            Slog.e(TAG, "clearSuperWhiteList fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "clearSuperWhiteList Error" + e2);
            return false;
        }
    }

    public boolean clearAllSuperWhiteList() {
        try {
            return getOplusCustomizePackageManagerService().clearAllSuperWhiteList();
        } catch (RemoteException e) {
            Slog.e(TAG, "clearAllSuperWhiteList fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "clearAllSuperWhiteList Error" + e2);
            return false;
        }
    }
}
