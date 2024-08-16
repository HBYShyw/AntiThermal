package android.os.customize;

import android.content.Context;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeApplicationManagerService;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCustomizeApplicationManager {
    private static final String SERVICE_NAME = "OplusCustomizeApplicationManagerService";
    private static final String TAG = "OplusCustomizeApplicationManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeApplicationManager sInstance;
    private IOplusCustomizeApplicationManagerService mOplusCustomizeApplicationManagerService;

    private OplusCustomizeApplicationManager() {
        getOplusCustomizeApplicationManagerService();
    }

    public static final OplusCustomizeApplicationManager getInstance(Context context) {
        OplusCustomizeApplicationManager oplusCustomizeApplicationManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeApplicationManager();
                }
                oplusCustomizeApplicationManager = sInstance;
            }
            return oplusCustomizeApplicationManager;
        }
        return sInstance;
    }

    private IOplusCustomizeApplicationManagerService getOplusCustomizeApplicationManagerService() {
        IOplusCustomizeApplicationManagerService iOplusCustomizeApplicationManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeApplicationManagerService == null) {
                this.mOplusCustomizeApplicationManagerService = IOplusCustomizeApplicationManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            if (this.mOplusCustomizeApplicationManagerService == null) {
                Slog.e(TAG, "mOplusCustomizeApplicationManagerService is null");
            }
            iOplusCustomizeApplicationManagerService = this.mOplusCustomizeApplicationManagerService;
        }
        return iOplusCustomizeApplicationManagerService;
    }

    public boolean forceStopPackage(List<String> pkgs) {
        if (pkgs == null) {
            return false;
        }
        boolean ret = false;
        try {
            ret = getOplusCustomizeApplicationManagerService().forceStopPackage(pkgs);
            Slog.d(TAG, "forceStopPackage: succeeded");
            return ret;
        } catch (RemoteException e) {
            Slog.e(TAG, "forceStopPackage fail!", e);
            return ret;
        } catch (Exception e2) {
            Slog.e(TAG, "forceStopPackage Error" + e2);
            return ret;
        }
    }

    public String getTopAppPackageName() {
        try {
            return getOplusCustomizeApplicationManagerService().getTopAppPackageName();
        } catch (RemoteException e) {
            Slog.e(TAG, "getTopAppPackageName fail!", e);
            return "";
        } catch (Exception e2) {
            Slog.e(TAG, "getTopAppPackageName Error" + e2);
            return "";
        }
    }

    public void killApplicationProcess(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return;
        }
        try {
            getOplusCustomizeApplicationManagerService().killApplicationProcess(packageName);
        } catch (RemoteException e) {
            Slog.e(TAG, "killApplicationProcess fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "killApplicationProcess Error" + e2);
        }
    }

    public void cleanBackgroundProcess() {
        try {
            getOplusCustomizeApplicationManagerService().cleanBackgroundProcess();
        } catch (RemoteException e) {
            Slog.e(TAG, "cleanBackgroundProcess fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "cleanBackgroundProcess Error" + e2);
        }
    }

    public void addDisallowedRunningApp(List<String> packageNames) {
        try {
            getOplusCustomizeApplicationManagerService().addDisallowedRunningApp(packageNames);
        } catch (RemoteException e) {
            Slog.e(TAG, "addDisallowedRunningApp fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "addDisallowedRunningApp Error" + e2);
        }
    }

    public void removeDisallowedRunningApp(List<String> packageNames) {
        try {
            getOplusCustomizeApplicationManagerService().removeDisallowedRunningApp(packageNames);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeDisallowedRunningApp fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "removeDisallowedRunningApp Error" + e2);
        }
    }

    public List<String> getDisallowedRunningApp() {
        try {
            return getOplusCustomizeApplicationManagerService().getDisallowedRunningApp();
        } catch (RemoteException e) {
            Slog.e(TAG, "getDisallowedRunningApp fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getDisallowedRunningApp Error" + e2);
            return null;
        }
    }

    public void addTrustedAppStore(String appStorePkgName) {
        try {
            getOplusCustomizeApplicationManagerService().addTrustedAppStore(appStorePkgName);
        } catch (RemoteException e) {
            Slog.e(TAG, "addTrustedAppStore fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "addTrustedAppStore Error" + e2);
        }
    }

    public void deleteTrustedAppStore(String appStorePkgName) {
        try {
            getOplusCustomizeApplicationManagerService().deleteTrustedAppStore(appStorePkgName);
        } catch (RemoteException e) {
            Slog.e(TAG, "deleteTrustedAppStore fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "deleteTrustedAppStore Error" + e2);
        }
    }

    public void enableTrustedAppStore(boolean enable) {
        try {
            getOplusCustomizeApplicationManagerService().enableTrustedAppStore(enable);
        } catch (RemoteException e) {
            Slog.e(TAG, "enableTrustedAppStore fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "enableTrustedAppStore Error" + e2);
        }
    }

    public boolean isTrustedAppStoreEnabled() {
        try {
            return getOplusCustomizeApplicationManagerService().isTrustedAppStoreEnabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isTrustedAppStoreEnabled Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isTrustedAppStoreEnabled Error" + e2);
            return false;
        }
    }

    public List<String> getTrustedAppStore() {
        try {
            return getOplusCustomizeApplicationManagerService().getTrustedAppStore();
        } catch (RemoteException e) {
            Slog.e(TAG, "getTrustedAppStore fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getTrustedAppStore Error" + e2);
            return null;
        }
    }

    public void setAllowTrustedAppStore(boolean enable) {
        try {
            getOplusCustomizeApplicationManagerService().setAllowTrustedAppStore(enable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setAllowTrustedAppStore: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setAllowTrustedAppStore Error" + e2);
        }
    }

    public boolean isAllowTrustedAppStore() {
        try {
            return getOplusCustomizeApplicationManagerService().isAllowTrustedAppStore();
        } catch (RemoteException e) {
            Slog.e(TAG, "isAllowTrustedAppStore: fail", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isAllowTrustedAppStore Error" + e2);
            return false;
        }
    }

    public void addTrustedAppStoreList(List<String> packageNames) {
        try {
            getOplusCustomizeApplicationManagerService().addTrustedAppStoreList(packageNames);
        } catch (RemoteException e) {
            Slog.e(TAG, "addTrustedAppStoreList fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "addTrustedAppStoreList Error" + e2);
        }
    }

    public void clearAllTrustedAppStore() {
        try {
            getOplusCustomizeApplicationManagerService().clearAllTrustedAppStore();
        } catch (RemoteException e) {
            Slog.e(TAG, "clearAllTrustedAppStore fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "clearAllTrustedAppStore Error" + e2);
        }
    }

    public void addAppAlarmWhiteList(List<String> packageNames) {
        try {
            getOplusCustomizeApplicationManagerService().addAppAlarmWhiteList(packageNames);
        } catch (Exception e) {
            Slog.e(TAG, "addAppAlarmWhiteList exception " + e);
        }
    }

    public List<String> getAppAlarmWhiteList() {
        try {
            return getOplusCustomizeApplicationManagerService().getAppAlarmWhiteList();
        } catch (Exception e) {
            Slog.e(TAG, "getAppAlarmWhiteList exception " + e);
            return null;
        }
    }

    public boolean removeAppAlarmWhiteList(List<String> packageNames) {
        try {
            return getOplusCustomizeApplicationManagerService().removeAppAlarmWhiteList(packageNames);
        } catch (Exception e) {
            Slog.e(TAG, "removeAppAlarmWhiteList exception " + e);
            return false;
        }
    }

    public boolean removeAllAppAlarmWhiteList() {
        try {
            return getOplusCustomizeApplicationManagerService().removeAllAppAlarmWhiteList();
        } catch (Exception e) {
            Slog.e(TAG, "removeAllAppAlarmWhiteList exception " + e);
            return false;
        }
    }

    public void setAllowControlAppRun(boolean enable) {
        try {
            getOplusCustomizeApplicationManagerService().setAllowControlAppRun(enable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setAllowControlAppRun: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setAllowControlAppRun Error" + e2);
        }
    }

    public boolean isAllowControlAppRun() {
        try {
            return getOplusCustomizeApplicationManagerService().isAllowControlAppRun();
        } catch (RemoteException e) {
            Slog.e(TAG, "isAllowControlAppRun: fail", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isAllowControlAppRun Error" + e2);
            return false;
        }
    }

    public void addPersistentApp(List<String> packageNames, String label) {
        try {
            getOplusCustomizeApplicationManagerService().addPersistentApp(packageNames, label);
        } catch (Exception e) {
            Slog.e(TAG, "addPersistentApp exception " + e);
        }
    }

    public void removePersistentApp(List<String> packageNames) {
        try {
            getOplusCustomizeApplicationManagerService().removePersistentApp(packageNames);
        } catch (Exception e) {
            Slog.e(TAG, "removePersistentApp exception " + e);
        }
    }

    public List<String> getPersistentApp() {
        List<String> tempList = new ArrayList<>();
        try {
            List<String> tempList2 = getOplusCustomizeApplicationManagerService().getPersistentApp();
            return tempList2;
        } catch (Exception e) {
            Slog.e(TAG, "getPersistentApp exception " + e);
            return tempList;
        }
    }

    public List<String> getSpecificCutoutModeAppList(int cutoutMode) {
        List<String> tempList = new ArrayList<>();
        try {
            List<String> tempList2 = getOplusCustomizeApplicationManagerService().getSpecificCutoutModeAppList(cutoutMode);
            return tempList2;
        } catch (Exception e) {
            Slog.e(TAG, "getSpecificCutoutModeAppList exception " + e);
            return tempList;
        }
    }

    public void setSpecificCutoutModeAppList(List<String> pkgList, int cutoutMode) {
        if (pkgList != null) {
            try {
                if (pkgList.size() != 0) {
                    getOplusCustomizeApplicationManagerService().setSpecificCutoutModeAppList(pkgList, cutoutMode);
                }
            } catch (Exception e) {
                Slog.e(TAG, "setSpecificCutoutModeAppList exception " + e);
            }
        }
    }

    public void interceptStopLockTask(boolean intercept) {
        try {
            getOplusCustomizeApplicationManagerService().interceptStopLockTask(intercept);
        } catch (Exception e) {
            Slog.e(TAG, "interceptStopLockTask exception " + e);
        }
    }

    public boolean getStopLockTaskAvailability() {
        try {
            return getOplusCustomizeApplicationManagerService().getStopLockTaskAvailability();
        } catch (Exception e) {
            Slog.e(TAG, "getStopLockTaskAvailability exception " + e);
            return false;
        }
    }

    public void setAERDeviceOwnerApp(List<String> packageNames) {
        if (packageNames == null || packageNames.size() == 0) {
            return;
        }
        try {
            getOplusCustomizeApplicationManagerService().setAERDeviceOwnerApp(packageNames);
        } catch (RemoteException e) {
            Slog.e(TAG, "setAERDeviceOwnerApp fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setAERDeviceOwnerApp Error" + e2);
        }
    }

    public List<String> getAERDeviceOwnerApp() {
        List<String> packageNames = new ArrayList<>();
        try {
            return getOplusCustomizeApplicationManagerService().getAERDeviceOwnerApp();
        } catch (RemoteException e) {
            Slog.e(TAG, "getAERDeviceOwnerApp fail!", e);
            return packageNames;
        } catch (Exception e2) {
            Slog.e(TAG, "getAERDeviceOwnerApp Error" + e2);
            return packageNames;
        }
    }
}
