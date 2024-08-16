package android.os.customize;

import android.content.Context;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeStateManagerService;
import android.util.Slog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCustomizeStateManager {
    private static final String SERVICE_NAME = "OplusCustomizeStateManagerService";
    private static final String TAG = "OplusCustomizeStateManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeStateManager sInstance;
    private IOplusCustomizeStateManagerService mOplusCustomizeStateManagerService;

    private OplusCustomizeStateManager() {
        getOplusCustomizeStateManagerService();
    }

    public static final OplusCustomizeStateManager getInstance(Context context) {
        OplusCustomizeStateManager oplusCustomizeStateManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeStateManager();
                }
                oplusCustomizeStateManager = sInstance;
            }
            return oplusCustomizeStateManager;
        }
        return sInstance;
    }

    private IOplusCustomizeStateManagerService getOplusCustomizeStateManagerService() {
        IOplusCustomizeStateManagerService iOplusCustomizeStateManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeStateManagerService == null) {
                this.mOplusCustomizeStateManagerService = IOplusCustomizeStateManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            if (this.mOplusCustomizeStateManagerService == null) {
                Slog.e(TAG, "mOplusCustomizeStateManagerService is null");
            }
            iOplusCustomizeStateManagerService = this.mOplusCustomizeStateManagerService;
        }
        return iOplusCustomizeStateManagerService;
    }

    public List<String> getRunningApplication() {
        List<String> runningApplicationList = new ArrayList<>();
        try {
            IOplusCustomizeStateManagerService iOplusCustomizeStateManagerService = getOplusCustomizeStateManagerService();
            if (iOplusCustomizeStateManagerService != null) {
                runningApplicationList = iOplusCustomizeStateManagerService.getRunningApplication();
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "getRunningApplication fail!", e);
        }
        return runningApplicationList == null ? Collections.emptyList() : runningApplicationList;
    }

    public String[] getDeviceState() {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService == null) {
                return null;
            }
            String[] ret = iService.getDeviceState();
            return ret;
        } catch (RemoteException e) {
            Slog.e(TAG, "getDeviceState fail!" + e);
            return null;
        }
    }

    public List<String> getAppRuntimeExceptionInfo() {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService == null) {
                return null;
            }
            List<String> appExceps = iService.getAppRuntimeExceptionInfo();
            return appExceps;
        } catch (RemoteException e) {
            Slog.e(TAG, "getAppRuntimeExceptionInfo error!" + e);
            return null;
        }
    }

    public boolean getSystemIntegrity() {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService == null) {
                return false;
            }
            boolean ret = iService.getSystemIntegrity();
            return ret;
        } catch (RemoteException e) {
            Slog.e(TAG, "getSystemIntegrity error!" + e);
            return false;
        }
    }

    public void setScreenOnStatus(boolean screenOnStatus) {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService != null) {
                iService.setScreenOnStatus(screenOnStatus);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "setScreenOnStatus error!" + e);
        }
    }

    public boolean getScreenOnStatus() {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService == null) {
                return false;
            }
            boolean ret = iService.getScreenOnStatus();
            return ret;
        } catch (RemoteException e) {
            Slog.e(TAG, "getScreenOnStatus error!" + e);
            return false;
        }
    }

    public void setAllowedNotificationListenerAccessList(List<String> packageNames) {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService != null) {
                iService.setAllowedNotificationListenerAccessList(packageNames);
            }
        } catch (RemoteException e) {
            Slog.d(TAG, "setAllowedNotificationListenerAccessList fail: ", e);
        }
    }

    public List<String> getAllowedNotificationListenerAccessList() {
        List<String> list = new ArrayList<>();
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService != null) {
                return iService.getAllowedNotificationListenerAccessList();
            }
            return list;
        } catch (RemoteException e) {
            Slog.d(TAG, "getAllowedNotificationListenerAccessList fail: ", e);
            return list;
        }
    }

    public void setAllowedGetUsageStatusList(List<String> packageNames) {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService != null) {
                iService.setAllowedGetUsageStatusList(packageNames);
            }
        } catch (RemoteException e) {
            Slog.d(TAG, "setAllowedGetUsageStatusList: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setAllowedGetUsageStatusList Error" + e2);
        }
    }

    public List<String> getAllowedGetUsageStatusList() {
        List<String> list = new ArrayList<>();
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService != null) {
                return iService.getAllowedGetUsageStatusList();
            }
            return list;
        } catch (RemoteException e) {
            Slog.d(TAG, "getAllowedGetUsageStatusList: fail", e);
            return list;
        } catch (Exception e2) {
            Slog.e(TAG, "getAllowedGetUsageStatusList Error" + e2);
            return list;
        }
    }

    public void setExtStorageMode(String packageName, String permissionName, int choice, boolean systemFixed) {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService != null) {
                iService.setExtStorageMode(packageName, permissionName, choice, systemFixed);
            }
        } catch (RemoteException e) {
            Slog.d(TAG, "setExtStorageMode: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setExtStorageMode Error" + e2);
        }
    }

    public int getExtStorageMode(String packageName) {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService == null) {
                return -1;
            }
            int mode = iService.getExtStorageMode(packageName);
            return mode;
        } catch (RemoteException e) {
            Slog.d(TAG, "getExtStorageMode: fail", e);
            return -1;
        }
    }

    public void setAllowedAllFilesAccessList(List<String> packageNames) {
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService != null) {
                iService.setAllowedAllFilesAccessList(packageNames);
            }
        } catch (RemoteException e) {
            Slog.d(TAG, "setAllowedAllFilesAccessList: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setAllowedAllFilesAccessList Error" + e2);
        }
    }

    public List<String> getAllowedAllFilesAccessList() {
        List<String> list = new ArrayList<>();
        try {
            IOplusCustomizeStateManagerService iService = getOplusCustomizeStateManagerService();
            if (iService != null) {
                return iService.getAllowedAllFilesAccessList();
            }
            return list;
        } catch (RemoteException e) {
            Slog.d(TAG, "getAllowedAllFilesAccessList: fail", e);
            return list;
        } catch (Exception e2) {
            Slog.e(TAG, "getAllowedAllFilesAccessList Error" + e2);
            return list;
        }
    }
}
