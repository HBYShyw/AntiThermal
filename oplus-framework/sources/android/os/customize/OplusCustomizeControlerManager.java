package android.os.customize;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeControlerManagerService;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCustomizeControlerManager {
    private static final String SERVICE_NAME = "OplusCustomizeControlerManagerService";
    private static final String TAG = "OplusCustomizeControlerManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeControlerManager sInstance;
    private IOplusCustomizeControlerManagerService mOplusCustomizeControlerManagerService;

    private OplusCustomizeControlerManager() {
        getOplusCustomizeControlerManagerService();
    }

    public static final OplusCustomizeControlerManager getInstance(Context context) {
        OplusCustomizeControlerManager oplusCustomizeControlerManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeControlerManager();
                }
                oplusCustomizeControlerManager = sInstance;
            }
            return oplusCustomizeControlerManager;
        }
        return sInstance;
    }

    private IOplusCustomizeControlerManagerService getOplusCustomizeControlerManagerService() {
        IOplusCustomizeControlerManagerService iOplusCustomizeControlerManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeControlerManagerService == null) {
                this.mOplusCustomizeControlerManagerService = IOplusCustomizeControlerManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            iOplusCustomizeControlerManagerService = this.mOplusCustomizeControlerManagerService;
        }
        return iOplusCustomizeControlerManagerService;
    }

    public void shutdownDevice() {
        try {
            getOplusCustomizeControlerManagerService().shutdownDevice();
        } catch (RemoteException e) {
            Slog.e(TAG, "shutdownDevice Error");
        } catch (Exception e2) {
            Slog.e(TAG, "shutdownDevice Error exception: ", e2);
        }
    }

    public void rebootDevice() {
        try {
            getOplusCustomizeControlerManagerService().rebootDevice();
        } catch (RemoteException e) {
            Slog.e(TAG, "rebootDevice Error");
        } catch (Exception e2) {
            Slog.e(TAG, "rebootDevice Error exception", e2);
        }
    }

    public void wipeDeviceData() {
        try {
            getOplusCustomizeControlerManagerService().wipeDeviceData();
        } catch (RemoteException e) {
            Slog.e(TAG, "wipeDeviceData Error");
        } catch (Exception e2) {
            Slog.e(TAG, "wipeDeviceData Error exception", e2);
        }
    }

    public void installSystemUpdate(String updatePath) {
        try {
            getOplusCustomizeControlerManagerService().installSystemUpdate(updatePath);
        } catch (RemoteException e) {
            Slog.e(TAG, "installSystemUpdate fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "installSystemUpdate Error" + e2);
        }
    }

    public boolean formatSDCard(String diskId) {
        try {
            boolean result = getOplusCustomizeControlerManagerService().formatSDCard(diskId);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "formatSDCard Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "formatSDCard Error exception: ", e2);
            return false;
        }
    }

    public void enableAccessibilityService(ComponentName componentName) {
        try {
            getOplusCustomizeControlerManagerService().enableAccessibilityService(componentName);
        } catch (RemoteException e) {
            Slog.e(TAG, "enableAccessibilityService fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "enableAccessibilityService Error" + e2);
        }
    }

    public void disableAccessibilityService(ComponentName componentName) {
        try {
            getOplusCustomizeControlerManagerService().disableAccessibilityService(componentName);
        } catch (RemoteException e) {
            Slog.e(TAG, "disableAccessibilityService fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "disableAccessibilityService Error" + e2);
        }
    }

    public List<ComponentName> getAccessibilityService() {
        List<ComponentName> enabledServicesList = new ArrayList<>();
        try {
            return getOplusCustomizeControlerManagerService().getAccessibilityService();
        } catch (RemoteException e) {
            Slog.e(TAG, "getAccessibilityService fail!", e);
            return enabledServicesList;
        } catch (Exception e2) {
            Slog.e(TAG, "getAccessibilityService Error" + e2);
            return enabledServicesList;
        }
    }

    public boolean isAccessibilityServiceEnabled() {
        try {
            boolean result = getOplusCustomizeControlerManagerService().isAccessibilityServiceEnabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "isAccessibilityServiceEnabled fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isAccessibilityServiceEnabled Error" + e2);
            return false;
        }
    }

    public String getEnabledAccessibilityServicesName() {
        try {
            String result = getOplusCustomizeControlerManagerService().getEnabledAccessibilityServicesName();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getEnabledAccessibilityServicesName fail!", e);
            return "";
        } catch (Exception e2) {
            Slog.e(TAG, "getEnabledAccessibilityServicesName Error" + e2);
            return "";
        }
    }

    public void addAccessibilityServiceToWhiteList(List<String> pkgList) {
        try {
            getOplusCustomizeControlerManagerService().addAccessibilityServiceToWhiteList(pkgList);
        } catch (RemoteException e) {
            Slog.e(TAG, "addAccessibilityServiceToWhiteList fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "addAccessibilityServiceToWhiteList Error" + e2);
        }
    }

    public void removeAccessibilityServiceFromWhiteList(List<String> pkgList) {
        try {
            getOplusCustomizeControlerManagerService().removeAccessibilityServiceFromWhiteList(pkgList);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeAccessibilityServiceFromWhiteList fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "removeAccessibilityServiceFromWhiteList Error" + e2);
        }
    }

    public List<String> getAccessibilityServiceWhiteList() {
        try {
            return getOplusCustomizeControlerManagerService().getAccessibilityServiceWhiteList();
        } catch (RemoteException e) {
            Slog.e(TAG, "getAccessibilityServiceWhiteList fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getAccessibilityServiceWhiteList Error" + e2);
            return null;
        }
    }

    public void deleteAccessibilityServiceWhiteList() {
        try {
            getOplusCustomizeControlerManagerService().deleteAccessibilityServiceWhiteList();
        } catch (RemoteException e) {
            Slog.e(TAG, "deleteAccessibilityServiceWhiteList fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "deleteAccessibilityServiceWhiteList Error" + e2);
        }
    }

    public boolean setDisableKeyguardForgetPassword(boolean disable) {
        try {
            IOplusCustomizeControlerManagerService manager = getOplusCustomizeControlerManagerService();
            Slog.d(TAG, "setDisableKeyguardForgetPassword: succeeded");
            return manager.setDisableKeyguardForgetPassword(disable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setDisableKeyguardForgetPassword fail!", e);
            return false;
        }
    }

    public boolean isDisableKeyguardForgetPassword() {
        try {
            IOplusCustomizeControlerManagerService manager = getOplusCustomizeControlerManagerService();
            Slog.d(TAG, "isDisableKeyguardForgetPassword: succeeded");
            return manager.isDisableKeyguardForgetPassword();
        } catch (RemoteException e) {
            Slog.e(TAG, "isDisableKeyguardForgetPassword fail!", e);
            return false;
        }
    }

    public void setDisabledKeyguardPolicy(boolean disable, String key) {
        try {
            IOplusCustomizeControlerManagerService manager = getOplusCustomizeControlerManagerService();
            Slog.d(TAG, "setDisabledKeyguardPolicy: succeeded");
            manager.setDisabledKeyguardPolicy(disable, key);
        } catch (RemoteException e) {
            Slog.e(TAG, "setDisabledKeyguardPolicy fail!", e);
        }
    }

    public boolean isDisabledKeyguardPolicy(String key) {
        try {
            IOplusCustomizeControlerManagerService manager = getOplusCustomizeControlerManagerService();
            Slog.d(TAG, "isDisabledKeyguardPolicy: succeeded");
            return manager.isDisabledKeyguardPolicy(key);
        } catch (RemoteException e) {
            Slog.e(TAG, "isDisabledKeyguardPolicy fail!", e);
            return false;
        }
    }

    public void setAirplaneMode(ComponentName admin, boolean on) {
        try {
            getOplusCustomizeControlerManagerService().setAirplaneMode(admin, on);
        } catch (RemoteException e) {
            Slog.e(TAG, "setAirplaneMode error! " + e);
        }
    }

    public boolean getAirplaneMode(ComponentName admin) {
        try {
            boolean result = getOplusCustomizeControlerManagerService().getAirplaneMode(admin);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getAirplaneMode error! e=" + e);
            return false;
        }
    }

    public boolean setSysTime(ComponentName admin, long mills) {
        try {
            boolean result = getOplusCustomizeControlerManagerService().setSysTime(admin, mills);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setSysTime fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setSysTime Error" + e2);
            return false;
        }
    }

    public void setCustomSettingsMenu(ComponentName admin, List<String> deleteMenus) {
        try {
            getOplusCustomizeControlerManagerService().setCustomSettingsMenu(admin, deleteMenus);
        } catch (RemoteException e) {
            Slog.e(TAG, "setCustomSettingsMenu fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setCustomSettingsMenu Error" + e2);
        }
    }

    public void setMaxDelayTimeForCustomizeRebootanim(int timeout) {
        try {
            getOplusCustomizeControlerManagerService().setMaxDelayTimeForCustomizeRebootanim(timeout);
        } catch (RemoteException e) {
            Slog.e(TAG, "setMaxDelayTimeForCustomizeRebootanim fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setMaxDelayTimeForCustomizeRebootanim Error" + e2);
        }
    }

    public void setCustomAnimationPath(String customAnimPath) {
        try {
            getOplusCustomizeControlerManagerService().setCustomAnimationPath(customAnimPath);
        } catch (RemoteException e) {
            Slog.e(TAG, "setCustomAnimationPath fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setCustomAnimationPath Error" + e2);
        }
    }

    public String getCustomAnimationPath() {
        try {
            String result = getOplusCustomizeControlerManagerService().getCustomAnimationPath();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getCustomAnimationPath fail!", e);
            return "";
        } catch (Exception e2) {
            Slog.e(TAG, "getCustomAnimationPath Error" + e2);
            return "";
        }
    }

    public void setCustomSoundPath(String customSoundPath) {
        try {
            getOplusCustomizeControlerManagerService().setCustomSoundPath(customSoundPath);
        } catch (RemoteException e) {
            Slog.e(TAG, "setCustomSoundPath fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setCustomSoundPath Error" + e2);
        }
    }

    public String getCustomSoundPath() {
        try {
            String result = getOplusCustomizeControlerManagerService().getCustomSoundPath();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getCustomSoundPath fail!", e);
            return "";
        } catch (Exception e2) {
            Slog.e(TAG, "getCustomSoundPath Error" + e2);
            return "";
        }
    }
}
