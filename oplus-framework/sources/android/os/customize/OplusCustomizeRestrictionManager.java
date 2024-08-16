package android.os.customize;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeRestrictionManagerService;
import android.util.Slog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCustomizeRestrictionManager {
    public static final int AIRPLANE_POLICY_NO_RESTRICTIONS = 2;
    public static final int AIRPLANE_POLICY_OFF = 3;
    public static final int AIRPLANE_POLICY_OFF_FORCE = 0;
    public static final int AIRPLANE_POLICY_ON = 4;
    public static final int AIRPLANE_POLICY_ON_FORCE = 1;
    private static final String SERVICE_NAME = "OplusCustomizeRestrictionManagerService";
    private static final String TAG = "OplusCustomizeRestrictionManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeRestrictionManager sInstance = null;
    private IOplusCustomizeRestrictionManagerService mOplusCustomizeRestrictionManagerService = null;

    private OplusCustomizeRestrictionManager() {
        getOplusCustomizeRestrictionManagerService();
    }

    public static final OplusCustomizeRestrictionManager getInstance(Context context) {
        OplusCustomizeRestrictionManager oplusCustomizeRestrictionManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    Slog.e(TAG, "sInstance is null, start a new sInstance");
                    sInstance = new OplusCustomizeRestrictionManager();
                }
                oplusCustomizeRestrictionManager = sInstance;
            }
            return oplusCustomizeRestrictionManager;
        }
        return sInstance;
    }

    private IOplusCustomizeRestrictionManagerService getOplusCustomizeRestrictionManagerService() {
        IOplusCustomizeRestrictionManagerService iOplusCustomizeRestrictionManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeRestrictionManagerService == null) {
                this.mOplusCustomizeRestrictionManagerService = IOplusCustomizeRestrictionManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            if (this.mOplusCustomizeRestrictionManagerService == null) {
                Slog.e(TAG, "mOplusCustomizeRestrictionManagerService is null");
            }
            iOplusCustomizeRestrictionManagerService = this.mOplusCustomizeRestrictionManagerService;
        }
        return iOplusCustomizeRestrictionManagerService;
    }

    public int getDefaultDataCard(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getDefaultDataCard(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getDefaultDataCard fail!", e);
            return 0;
        }
    }

    public Bundle setDefaultDataCard(ComponentName admin, int slot) {
        try {
            return getOplusCustomizeRestrictionManagerService().setDefaultDataCard(admin, slot);
        } catch (Exception e) {
            Slog.e(TAG, "setDefaultDataCard fail!", e);
            return null;
        }
    }

    public int getSlot1DataConnectivityDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getSlot1DataConnectivityDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getSlot1DataConnectivityDisabled fail!", e);
            return -1;
        }
    }

    public int getSlot2DataConnectivityDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getSlot2DataConnectivityDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getSlot2DataConnectivityDisabled fail!", e);
            return -1;
        }
    }

    public void setClipboardEnabled(boolean enable) {
        try {
            getOplusCustomizeRestrictionManagerService().setClipboardEnabled(enable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setClipboardEnabled Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setClipboardEnabled Error" + e2);
        }
    }

    public boolean getClipboardStatus() {
        try {
            return getOplusCustomizeRestrictionManagerService().getClipboardStatus();
        } catch (RemoteException e) {
            Slog.e(TAG, "getClipboardStatus Error");
            return true;
        } catch (Exception e2) {
            Slog.e(TAG, "getClipboardStatus Error" + e2);
            return true;
        }
    }

    public void setAppInstallRestrictionPolicies(int mode) {
        try {
            getOplusCustomizeRestrictionManagerService().setAppInstallRestrictionPolicies(mode);
        } catch (RemoteException e) {
            Slog.e(TAG, "addInstallPackageBlacklist", e);
        } catch (Exception e2) {
            Slog.e(TAG, "addInstallPackageBlacklist exception: ", e2);
        }
    }

    public int getAppInstallRestrictionPolicies() {
        try {
            int mode = getOplusCustomizeRestrictionManagerService().getAppInstallRestrictionPolicies();
            return mode;
        } catch (RemoteException e) {
            Slog.e(TAG, "addInstallPackageBlacklist", e);
            return 0;
        } catch (Exception e2) {
            Slog.e(TAG, "addInstallPackageBlacklist exception: ", e2);
            return 0;
        }
    }

    public List<String> getAppInstallPackageList(int mode) {
        List<String> list = new ArrayList<>();
        try {
            return getOplusCustomizeRestrictionManagerService().getAppInstallPackageList(mode);
        } catch (RemoteException e) {
            Slog.e(TAG, "getAppInstalledPackageList", e);
            return list;
        } catch (Exception e2) {
            Slog.e(TAG, "getAppInstalledPackageList exception: ", e2);
            return list;
        }
    }

    public void addAppInstallPackageBlacklist(int pattern, List<String> packageNames) {
        try {
            getOplusCustomizeRestrictionManagerService().addAppInstallPackageBlacklist(pattern, packageNames);
        } catch (RemoteException e) {
            Slog.e(TAG, "addAppInstallPackageBlacklist", e);
        } catch (Exception e2) {
            Slog.e(TAG, "addAppInstallPackageBlacklist exception: ", e2);
        }
    }

    public void addAppInstallPackageWhitelist(int pattern, List<String> packageNames) {
        try {
            getOplusCustomizeRestrictionManagerService().addAppInstallPackageWhitelist(pattern, packageNames);
        } catch (RemoteException e) {
            Slog.e(TAG, "addAppInstallPackageWhitelist", e);
        } catch (Exception e2) {
            Slog.e(TAG, "addAppInstallPackageWhitelist exception: ", e2);
        }
    }

    public void setUSBDataDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setUSBDataDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setUSBDataDisabled Error", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setUSBDataDisabled Error exception: ", e2);
        }
    }

    public boolean isUSBDataDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isUSBDataDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isUSBDataDisabled Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isUSBDataDisabled Error exception: ", e2);
            return false;
        }
    }

    public void setUSBFileTransferDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setUSBFileTransferDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setUSBFileTransferDisabled Error", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setUSBFileTransferDisabled Error exception: ", e2);
        }
    }

    public boolean isUSBFileTransferDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isUSBFileTransferDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isUSBFileTransferDisabled Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isUSBFileTransferDisabled Error exception: ", e2);
            return false;
        }
    }

    public void setSafeModeDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setSafeModeDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setSafeModeDisabled error: ", e);
        }
    }

    public boolean isSafeModeDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isSafeModeDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isSafeModeDisabled error: ", e);
            return false;
        }
    }

    public void setUSBOtgDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setUSBOtgDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setUSBOtgDisabled Error", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setUSBOtgDisabled Error exception: ", e2);
        }
    }

    public boolean isUSBOtgDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isUSBOtgDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isUSBOtgDisabled Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isUSBOtgDisabled Error" + e2);
            return false;
        }
    }

    public void setBiometricDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setBiometricDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setBiometricDisabled Error", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setBiometricDisabled Error exception: ", e2);
        }
    }

    public boolean isBiometricDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBiometricDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isBiometricDisabled Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isBiometricDisabled Error" + e2);
            return false;
        }
    }

    public void setExternalStorageDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setExternalStorageDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setExternalStorageDisabled Error", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setExternalStorageDisabled Error exception: ", e2);
        }
    }

    public boolean isExternalStorageDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isExternalStorageDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isExternalStorageDisabled Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isExternalStorageDisabled Error" + e2);
            return false;
        }
    }

    public void setUsbTetheringDisable(boolean disable) {
        try {
            getOplusCustomizeRestrictionManagerService().setUsbTetheringDisable(disable);
        } catch (RemoteException e) {
            Slog.d(TAG, "setUsbTetheringDisable", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setUsbTetheringDisable Error" + e2);
        }
    }

    public boolean isUsbTetheringDisabled() {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().isUsbTetheringDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.d(TAG, "isUsbTetheringDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isUsbTetheringDisabled Error" + e2);
            return false;
        }
    }

    public void setRequiredStrongAuthTime(ComponentName admin, long timeoutMs) {
        try {
            getOplusCustomizeRestrictionManagerService().setRequiredStrongAuthTime(admin, timeoutMs);
        } catch (RemoteException e) {
            Slog.d(TAG, "setRequiredStrongAuthTime", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setRequiredStrongAuthTime Error" + e2);
        }
    }

    public long getRequiredStrongAuthTime(ComponentName admin) {
        try {
            long timeoutMs = getOplusCustomizeRestrictionManagerService().getRequiredStrongAuthTime(admin);
            return timeoutMs;
        } catch (RemoteException e) {
            Slog.d(TAG, "getRequiredStrongAuthTime", e);
            return 0L;
        } catch (Exception e2) {
            Slog.e(TAG, "getRequiredStrongAuthTime Error" + e2);
            return 0L;
        }
    }

    public boolean setScreenCaptureDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setScreenCaptureDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "enableForbidRecordScreen Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "enableForbidRecordScreen Error" + e2);
            return false;
        }
    }

    public boolean getForbidRecordScreenState() {
        try {
            return getOplusCustomizeRestrictionManagerService().getForbidRecordScreenState();
        } catch (RemoteException e) {
            Slog.e(TAG, "getForbidRecordScreenState Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "getForbidRecordScreenState Error" + e2);
            return false;
        }
    }

    public void setAppUninstallationPolicies(int mode, List<String> appPackageNames) {
        try {
            getOplusCustomizeRestrictionManagerService().setAppUninstallationPolicies(mode, appPackageNames);
        } catch (RemoteException e) {
            Slog.d(TAG, "setAppUninstallationPolicies", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setAppUninstallationPolicies Error" + e2);
        }
    }

    public List<String> getAppUninstallationPackageList(int mode) {
        List<String> packagelist = new ArrayList<>();
        try {
            return getOplusCustomizeRestrictionManagerService().getAppUninstallationPackageList(mode);
        } catch (RemoteException e) {
            Slog.d(TAG, "getAppUninstallationPolicies", e);
            return packagelist;
        } catch (Exception e2) {
            Slog.e(TAG, "getAppUninstallationPolicies Error" + e2);
            return packagelist;
        }
    }

    public int getAppUninstallationPolicies() {
        try {
            int mode = getOplusCustomizeRestrictionManagerService().getAppUninstallationPolicies();
            return mode;
        } catch (RemoteException e) {
            Slog.e(TAG, "getAppUninstallationPolicies", e);
            return 0;
        } catch (Exception e2) {
            Slog.e(TAG, "getAppUninstallationPolicies exception: ", e2);
            return 0;
        }
    }

    public void setSlot1DataConnectivityDisabled(ComponentName admin, String val) {
        try {
            getOplusCustomizeRestrictionManagerService().setSlot1DataConnectivityDisabled(admin, val);
        } catch (RemoteException e) {
            Slog.d(TAG, "setSlot1DataConnectivityDisabled", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot1DataConnectivityDisabled Error" + e2);
        }
    }

    public void setSlot2DataConnectivityDisabled(ComponentName admin, String val) {
        try {
            getOplusCustomizeRestrictionManagerService().setSlot2DataConnectivityDisabled(admin, val);
        } catch (RemoteException e) {
            Slog.d(TAG, "setSlot2DataConnectivityDisabled", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot2DataConnectivityDisabled Error" + e2);
        }
    }

    public boolean isVoiceOutgoingDisabled(ComponentName admin, int slot) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().isVoiceOutgoingDisabled(admin, slot);
            return result;
        } catch (RemoteException e) {
            Slog.d(TAG, "isVoiceOutgoingDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isVoiceOutgoingDisabled Error" + e2);
            return false;
        }
    }

    public boolean isVoiceIncomingDisabled(ComponentName admin, int slot) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().isVoiceIncomingDisabled(admin, slot);
            return result;
        } catch (RemoteException e) {
            Slog.d(TAG, "isVoiceIncomingDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isVoiceIncomingDisabled Error" + e2);
            return false;
        }
    }

    public void setVoiceOutgoingDisable(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setVoiceOutgoingDisable(admin, disabled);
        } catch (RemoteException e) {
            Slog.d(TAG, "setVoiceOutgoingDisable", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setVoiceOutgoingDisable Error" + e2);
        }
    }

    public void setVoiceIncomingDisable(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setVoiceIncomingDisable(admin, disabled);
        } catch (RemoteException e) {
            Slog.d(TAG, "setVoiceIncomingDisable", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setVoiceIncomingDisable Error" + e2);
        }
    }

    public boolean isVoiceDisabled(ComponentName admin) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().isVoiceDisabled(admin);
            return result;
        } catch (RemoteException e) {
            Slog.d(TAG, "setVoiceIncomingDisable", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setVoiceIncomingDisable Error" + e2);
            return false;
        }
    }

    public void setVoiceDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setVoiceDisabled(admin, disabled);
        } catch (RemoteException e) {
            Slog.d(TAG, "setVoiceDisabled", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setVoiceDisabled Error" + e2);
        }
    }

    public boolean setFloatTaskDisabled(ComponentName admin, boolean disable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setFloatTaskDisabled(admin, disable);
        } catch (Exception e) {
            Slog.e(TAG, "setTaskButtonDisabled error!", e);
            return false;
        }
    }

    public boolean isFloatTaskDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isFloatTaskDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isTaskButtonDisabled error!", e);
            return false;
        }
    }

    public void setPowerSavingModeDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setPowerSavingModeDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setPowerSavingModeDisabled fail!", e);
        }
    }

    public boolean isPowerSavingModeDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isPowerSavingModeDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isPowerSavingModeDisabled fail!", e);
            return false;
        }
    }

    public boolean setSuperPowerSavingModeDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setSuperPowerSavingModeDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setPowerSavingModeDisabled fail!", e);
            return false;
        }
    }

    public boolean isSuperPowerSavingModeDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isSuperPowerSavingModeDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isPowerSavingModeDisabled fail!", e);
            return false;
        }
    }

    public boolean setDataRoamingDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setDataRoamingDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setDataRoamingDisabled fail!", e);
            return false;
        }
    }

    public boolean isDataRoamingDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isDataRoamingDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isDataRoamingDisabled fail!", e);
            return false;
        }
    }

    public void applyQSRestriction(String key, int newValue) {
        try {
            getOplusCustomizeRestrictionManagerService().applyQSRestriction(key, newValue);
        } catch (Exception e) {
            Slog.e(TAG, "applyQSRestriction Error ", e);
        }
    }

    public void disableQSRestriction(String key, int newValue) {
        try {
            getOplusCustomizeRestrictionManagerService().disableQSRestriction(key, newValue);
        } catch (Exception e) {
            Slog.e(TAG, "applyQSRestriction Error ", e);
        }
    }

    public int getQSRestrictionValue(String key) {
        try {
            return getOplusCustomizeRestrictionManagerService().getQSRestrictionValue(key);
        } catch (Exception e) {
            Slog.e(TAG, "getQSRestrictionValue Error", e);
            return 1;
        }
    }

    public boolean getQSRestrictionState(String key, int state) {
        try {
            return getOplusCustomizeRestrictionManagerService().getQSRestrictionState(key, state);
        } catch (Exception e) {
            Slog.e(TAG, "getQSRestrictionState Error", e);
            return false;
        }
    }

    public boolean setSleepByPowerButtonDisabled(ComponentName admin, boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setSleepByPowerButtonDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setSleepByPowerButtonDisabled fail!", e);
            return false;
        }
    }

    public boolean isSleepByPowerButtonDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isSleepByPowerButtonDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isSleepByPowerButtonDisabled Error" + e);
            return false;
        }
    }

    public boolean setFileSharedDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setFileSharedDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setFileSharedDisabled Error" + e);
            return false;
        }
    }

    public boolean getFileSharedDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().getFileSharedDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "getFileSharedDisabled Error" + e);
            return false;
        }
    }

    public boolean setNavigationMode(int mode, boolean revertible) {
        try {
            return getOplusCustomizeRestrictionManagerService().setNavigationMode(mode, revertible);
        } catch (Exception e) {
            Slog.e(TAG, "setNavigationMode fail!", e);
            return false;
        }
    }

    public boolean isNavigationModeRevertible() {
        try {
            return getOplusCustomizeRestrictionManagerService().isNavigationModeRevertible();
        } catch (Exception e) {
            Slog.e(TAG, "isNavigationModeRevertible Error" + e);
            return true;
        }
    }

    public void setBluetoothDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setBluetoothDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setBluetoothDisabled fail!");
        }
    }

    public boolean isBluetoothDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBluetoothDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isBluetoothDisabled fail!");
            return false;
        }
    }

    public void setBluetoothEnabled(boolean enabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setBluetoothEnabled(enabled);
        } catch (Exception e) {
            Slog.e(TAG, "setBluetoothDisabled fail!");
        }
    }

    public boolean isBluetoothEnabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBluetoothEnabled();
        } catch (Exception e) {
            Slog.e(TAG, "isBluetoothDisabled fail!");
            return false;
        }
    }

    public boolean setBluetoothConnectableDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setBluetoothConnectableDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setBluetoothConnectableDisabled fail!");
            return false;
        }
    }

    public boolean isBluetoothConnectableDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBluetoothConnectableDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isBluetoothConnectableDisabled fail!");
            return false;
        }
    }

    public boolean setDiscoverableDisabled(boolean disable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setDiscoverableDisabled(disable);
        } catch (Exception e) {
            Slog.e(TAG, "setDiscoverableDisabled fail!");
            return false;
        }
    }

    public boolean isDiscoverableDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isDiscoverableDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isDiscoverableDisabled fail!");
            return false;
        }
    }

    public boolean setLimitedDiscoverableDisable(boolean disable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setLimitedDiscoverableDisable(disable);
        } catch (Exception e) {
            Slog.e(TAG, "setLimitedDiscoverableDisable fail!");
            return false;
        }
    }

    public boolean isLimitedDiscoverableDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isLimitedDiscoverableDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isLimitedDiscoverableDisabled fail!");
            return false;
        }
    }

    public boolean setBluetoothPairingDisable(boolean isDisable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setBluetoothPairingDisable(isDisable);
        } catch (Exception e) {
            Slog.e(TAG, "setBluetoothPairingDisable fail!");
            return false;
        }
    }

    public boolean isBluetoothPairingDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBluetoothPairingDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isBluetoothPairingDisabled fail!");
            return false;
        }
    }

    public boolean setBluetoothOutGoingCallDisable(boolean isDisable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setBluetoothOutGoingCallDisable(isDisable);
        } catch (Exception e) {
            Slog.e(TAG, "setBluetoothOutGoingCallDisable fail!");
            return false;
        }
    }

    public boolean isBluetoothOutGoingCallDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBluetoothOutGoingCallDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isBluetoothOutGoingCallDisabled fail!");
            return false;
        }
    }

    public boolean setBluetoothDataTransferDisable(boolean isDisable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setBluetoothDataTransferDisable(isDisable);
        } catch (Exception e) {
            Slog.e(TAG, "setBluetoothDataTransferDisable fail!");
            return false;
        }
    }

    public boolean isBluetoothDataTransferDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBluetoothDataTransferDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isBluetoothDataTransferDisable fail!");
            return false;
        }
    }

    public boolean setBluetoothTetheringDisable(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setBluetoothTetheringDisable(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setBluetoothTetheringDisable fail!");
            return false;
        }
    }

    public boolean isBluetoothTetheringDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBluetoothTetheringDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isBluetoothTetheringDisabled fail!");
            return false;
        }
    }

    public boolean setLocationBluetoothScanningDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setLocationBluetoothScanningDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setLocationBluetoothScanningDisable fail!");
            return false;
        }
    }

    public boolean isLocationBluetoothScanningDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isLocationBluetoothScanningDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isLocationBluetoothScanningDisabled fail!");
            return false;
        }
    }

    public boolean setBluetoothDisabledProfiles(List<String> disabledProfiles) {
        try {
            return getOplusCustomizeRestrictionManagerService().setBluetoothDisabledProfiles(disabledProfiles);
        } catch (Exception e) {
            Slog.e(TAG, "setBluetoothDisabledProfiles fail!");
            return false;
        }
    }

    public List<String> getBluetoothDisabledProfiles() {
        try {
            return getOplusCustomizeRestrictionManagerService().getBluetoothDisabledProfiles();
        } catch (Exception e) {
            Slog.e(TAG, "getBluetoothDisabledProfiles fail!");
            return new ArrayList();
        }
    }

    public void setNFCDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setNFCDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setNFCDisabled fail!", e);
        }
    }

    public boolean isNFCDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isNFCDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isNFCDisabled fail!", e);
            return false;
        }
    }

    public void openCloseNFC(ComponentName admin, boolean enable) {
        try {
            getOplusCustomizeRestrictionManagerService().openCloseNFC(admin, enable);
        } catch (Exception e) {
            Slog.e(TAG, "openCloseNFC fail!", e);
        }
    }

    public boolean isNFCTurnOn(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isNFCTurnOn(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isNFCTurnOn fail!", e);
            return false;
        }
    }

    @Deprecated
    public boolean setAndroidBeamDisabled(ComponentName admin, boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setAndroidBeamDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setAndroidBeamDisabled fail!", e);
            return false;
        }
    }

    public boolean isAndroidBeamDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isAndroidBeamDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isAndroidBeamDisabled fail!", e);
            return false;
        }
    }

    public boolean setNfcPolicies(ComponentName admin, int mode) {
        try {
            return getOplusCustomizeRestrictionManagerService().setNfcPolicies(admin, mode);
        } catch (Exception e) {
            Slog.e(TAG, "setNfcPolicies fail!", e);
            return false;
        }
    }

    public int getNfcPolicies(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getNfcPolicies(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getNfcPolicies fail!", e);
            return 2;
        }
    }

    public void setMobileDataMode(ComponentName componentName, int mode) {
        try {
            getOplusCustomizeRestrictionManagerService().setMobileDataMode(componentName, mode);
        } catch (RemoteException e) {
            Slog.e(TAG, "setMobileDataMode error!");
        }
    }

    public int getMobileDataMode(ComponentName componentName) {
        try {
            int result = getOplusCustomizeRestrictionManagerService().getMobileDataMode(componentName);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getMobileDataMode error!");
            return -1;
        }
    }

    public boolean allowWifiCellularNetwork(ComponentName compName, String packageName) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().allowWifiCellularNetwork(compName, packageName);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "allowWifiCellularNetwork error!");
            return false;
        }
    }

    public boolean setCameraPolicies(int mode) {
        try {
            return getOplusCustomizeRestrictionManagerService().setCameraPolicies(mode);
        } catch (Exception e) {
            Slog.e(TAG, "setCameraPolicies fail!", e);
            return false;
        }
    }

    public int getCameraPolicies() {
        try {
            return getOplusCustomizeRestrictionManagerService().getCameraPolicies();
        } catch (Exception e) {
            Slog.e(TAG, "getCameraPolicies fail!", e);
            return 0;
        }
    }

    public boolean setTorchPolicies(int mode) {
        try {
            return getOplusCustomizeRestrictionManagerService().setTorchPolicies(mode);
        } catch (Exception e) {
            Slog.e(TAG, "setTorchPolicies fail!", e);
            return false;
        }
    }

    public int getTorchPolicies() {
        try {
            return getOplusCustomizeRestrictionManagerService().getTorchPolicies();
        } catch (Exception e) {
            Slog.e(TAG, "getTorchPolicies fail!", e);
            return 0;
        }
    }

    public void setChangeWallpaperDisable(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setChangeWallpaperDisable(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setChangeWallpaperDisable fail!", e);
        }
    }

    public boolean isChangeWallpaperDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isChangeWallpaperDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isChangeWallpaperDisabled fail!", e);
            return false;
        }
    }

    public void setSettingsApplicationDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setSettingsApplicationDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setSettingsApplicationDisabled fail!", e);
        }
    }

    public boolean isSettingsApplicationDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isSettingsApplicationDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isSettingsApplicationDisabled fail!", e);
            return false;
        }
    }

    public void setApplicationDisabledInLauncherOrRecentTask(List<String> list, int flag) {
        try {
            getOplusCustomizeRestrictionManagerService().setApplicationDisabledInLauncherOrRecentTask(list, flag);
        } catch (Exception e) {
            Slog.e(TAG, "setApplicationDisabledInLauncherOrRecentTask fail!", e);
        }
    }

    public List<String> getApplicationDisabledInLauncherOrRecentTask(int flag) {
        try {
            return getOplusCustomizeRestrictionManagerService().getApplicationDisabledInLauncherOrRecentTask(flag);
        } catch (Exception e) {
            Slog.e(TAG, "getApplicationDisabledInLauncherOrRecentTask fail!", e);
            return Collections.emptyList();
        }
    }

    public boolean isMultiAppSupport() {
        try {
            return getOplusCustomizeRestrictionManagerService().isMultiAppSupport();
        } catch (RemoteException e) {
            Slog.e(TAG, "isMultiAppSupport Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isMultiAppSupport Error exception: ", e2);
            return false;
        }
    }

    public void setMultiAppSupport(boolean support) {
        try {
            getOplusCustomizeRestrictionManagerService().setMultiAppSupport(support);
        } catch (RemoteException e) {
            Slog.e(TAG, "setMultiAppSupport Error", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setMultiAppSupport Error exception: ", e2);
        }
    }

    public boolean setAirplanePolices(ComponentName admin, int policy) {
        try {
            return getOplusCustomizeRestrictionManagerService().setAirplanePolices(admin, policy);
        } catch (RemoteException e) {
            Slog.e(TAG, "setAirplanePolices error!", e);
            return false;
        }
    }

    public int getAirplanePolices(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getAirplanePolices(admin);
        } catch (RemoteException e) {
            Slog.e(TAG, "getAirplanePolices error!", e);
            return -1;
        }
    }

    public boolean setSplitScreenDisable(ComponentName admin, boolean disable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setSplitScreenDisable(admin, disable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSplitScreenDisable error!", e);
            return false;
        }
    }

    public boolean getSplitScreenDisable(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getSplitScreenDisable(admin);
        } catch (RemoteException e) {
            Slog.e(TAG, "getSplitScreenDisable error!", e);
            return false;
        }
    }

    public boolean setUserPasswordPolicies(ComponentName admin, int mode) {
        try {
            return getOplusCustomizeRestrictionManagerService().setUserPasswordPolicies(admin, mode);
        } catch (Exception e) {
            Slog.e(TAG, "setUserPasswordPolicies fail!", e);
            return false;
        }
    }

    public int getUserPasswordPolicies(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getUserPasswordPolicies(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getUserPasswordPolicies fail!", e);
            return 0;
        }
    }

    public boolean setUnlockByFingerprintPolicies(ComponentName admin, int policy) {
        try {
            return getOplusCustomizeRestrictionManagerService().setUnlockByFingerprintPolicies(admin, policy);
        } catch (Exception e) {
            Slog.e(TAG, "setUnlockByFingerprintPolicies fail!", e);
            return false;
        }
    }

    public int getUnlockByFingerprintPolicies(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getUnlockByFingerprintPolicies(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getUnlockByFingerprintPolicies fail!", e);
            return 0;
        }
    }

    public boolean setUnlockByFacePolicies(ComponentName admin, int policy) {
        try {
            return getOplusCustomizeRestrictionManagerService().setUnlockByFacePolicies(admin, policy);
        } catch (Exception e) {
            Slog.e(TAG, "setUnlockByFacePolicies fail!", e);
            return false;
        }
    }

    public int getUnlockByFacePolicies(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getUnlockByFacePolicies(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getUnlockByFacePolicies fail!", e);
            return 0;
        }
    }

    public boolean setUnlockByFingerprintDisabled(ComponentName admin, boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setUnlockByFingerprintDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setUnlockByFingerprintDisabled fail!", e);
            return false;
        }
    }

    public boolean isUnlockByFingerprintDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isUnlockByFingerprintDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isUnlockByFingerprintDisabled fail!", e);
            return false;
        }
    }

    public boolean setUnlockByFaceDisabled(ComponentName admin, boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setUnlockByFaceDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setUnlockByFaceDisabled fail!", e);
            return false;
        }
    }

    public boolean isUnlockByFaceDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isUnlockByFaceDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isUnlockByFaceDisabled fail!", e);
            return false;
        }
    }

    public boolean setPasswordRepeatMaxLength(int length) {
        try {
            return getOplusCustomizeRestrictionManagerService().setPasswordRepeatMaxLength(length);
        } catch (Exception e) {
            Slog.e(TAG, "setPasswordRepeatMaxLength fail!", e);
            return false;
        }
    }

    public int getPasswordRepeatMaxLength() {
        try {
            return getOplusCustomizeRestrictionManagerService().getPasswordRepeatMaxLength();
        } catch (Exception e) {
            Slog.e(TAG, "getPasswordRepeatMaxLength fail!", e);
            return -1;
        }
    }

    public boolean setPasswordNumSequenceMaxLength(int length) {
        try {
            return getOplusCustomizeRestrictionManagerService().setPasswordNumSequenceMaxLength(length);
        } catch (Exception e) {
            Slog.e(TAG, "setPasswordNumSequenceMaxLength fail!", e);
            return false;
        }
    }

    public int getPasswordNumSequenceMaxLength() {
        try {
            return getOplusCustomizeRestrictionManagerService().getPasswordNumSequenceMaxLength();
        } catch (Exception e) {
            Slog.e(TAG, "getPasswordNumSequenceMaxLength fail!", e);
            return -1;
        }
    }

    public void setTaskButtonDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setTaskButtonDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setTaskButtonDisabled error: ", e);
        }
    }

    public boolean isTaskButtonDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isTaskButtonDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isTaskButtonDisabled error: ", e);
            return false;
        }
    }

    public void setHomeButtonDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setHomeButtonDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setHomeButtonDisabled error: ", e);
        }
    }

    public boolean isHomeButtonDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isHomeButtonDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isHomeButtonDisabled error: ", e);
            return false;
        }
    }

    public void setBackButtonDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setBackButtonDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setBackButtonDisabled error: ", e);
        }
    }

    public boolean isBackButtonDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBackButtonDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isBackButtonDisabled error: ", e);
            return false;
        }
    }

    public void setPowerDisable(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setPowerDisable(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setPowerDisable error: ", e);
        }
    }

    public boolean getPowerDisable() {
        try {
            return getOplusCustomizeRestrictionManagerService().getPowerDisable();
        } catch (Exception e) {
            Slog.e(TAG, "getPowerDisable error: ", e);
            return false;
        }
    }

    public boolean setGpsPolicies(ComponentName admin, int mode) {
        try {
            return getOplusCustomizeRestrictionManagerService().setGpsPolicies(admin, mode);
        } catch (Exception e) {
            Slog.e(TAG, "setGpsPolicies Error", e);
            return false;
        }
    }

    public int getGpsPolicies(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getGpsPolicies(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getQSRestrictionState Error", e);
            return 0;
        }
    }

    public void setLanguageChangeDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setLanguageChangeDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.i(TAG, "setLanguageChangeDisabled error!");
            e.printStackTrace();
        }
    }

    public boolean isLanguageChangeDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isLanguageChangeDisabled(admin);
        } catch (RemoteException e) {
            Slog.i(TAG, "setLanguageChangeDisabled error!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean isWifiOpen(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isWifiOpen(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isWifiOpen fail!", e);
            return false;
        }
    }

    public boolean setWifiInBackground(ComponentName admin, boolean enable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setWifiInBackground(admin, enable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiInBackground error!");
            return false;
        }
    }

    public boolean isWifiDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isWifiDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isWifiDisabled fail!", e);
            return false;
        }
    }

    public void setWifiDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setWifiDisabled(admin, disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiDisabled error!");
        }
    }

    public boolean isEchoPasswordDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isEchoPasswordDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isEchoPasswordDisabled fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isEchoPasswordDisabled Error" + e2);
            return false;
        }
    }

    public boolean setEchoPasswordDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setEchoPasswordDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setEchoPasswordDisabled fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setEchoPasswordDisabled Error" + e2);
            return false;
        }
    }

    public void setMmsDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setMmsDisabled(disabled);
        } catch (RemoteException e) {
            Slog.d(TAG, "setMmsDisabled", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setMmsDisabled Error" + e2);
        }
    }

    public boolean isMmsDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isMmsDisabled();
        } catch (RemoteException e) {
            Slog.d(TAG, "isMmsDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isMmsDisabled Error" + e2);
            return false;
        }
    }

    public boolean isSmsReceiveDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isSmsReceiveDisabled();
        } catch (RemoteException e) {
            Slog.d(TAG, "isSmsReceiveDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isSmsReceiveDisabled Error" + e2);
            return false;
        }
    }

    public boolean isSmsSendDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isSmsSendDisabled();
        } catch (RemoteException e) {
            Slog.d(TAG, "isSmsSendDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isSmsSendDisabled Error" + e2);
            return false;
        }
    }

    public boolean isMmsSendReceiveDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isMmsSendReceiveDisabled();
        } catch (RemoteException e) {
            Slog.d(TAG, "isMmsSendReceiveDisabled", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isMmsSendReceiveDisabled Error" + e2);
            return false;
        }
    }

    public boolean isSleepStandbyOptimizationDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isSleepStandbyOptimizationDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isSleepStandbyOptimizationDisabled fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isSleepStandbyOptimizationDisabled Error" + e2);
            return false;
        }
    }

    public boolean setSleepStandbyOptimizationDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setSleepStandbyOptimizationDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSleepStandbyOptimizationDisabled fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setSleepStandbyOptimizationDisabled Error" + e2);
            return false;
        }
    }

    public void setUsbDebugSwitchDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setUsbDebugSwitchDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setUsbDebugSwitchDisabled fail!", e);
        }
    }

    public boolean isUsbDebugSwitchDisabled(ComponentName admin) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().isUsbDebugSwitchDisabled(admin);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "isUsbDebugSwitchDisabled fail!", e);
            return false;
        }
    }

    public void setAdbDisabled(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setAdbDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setAdbDisabled fail!", e);
        }
    }

    public boolean isAdbDisabled(ComponentName admin) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().isAdbDisabled(admin);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "isAdbDisabled fail!", e);
            return false;
        }
    }

    public void setNavigationBarDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setNavigationBarDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setNavigationBarDisabled: " + e);
        }
    }

    public boolean isNavigationBarDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isNavigationBarDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isNavigationBarDisabled error: " + e);
            return false;
        }
    }

    public void setLongPressLauncherDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setLongPressLauncherDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setLongPressLauncherDisabled error: ", e);
        }
    }

    public boolean isLongPressLauncherDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isLongPressLauncherDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isLongPressLauncherDisabled error: ", e);
            return false;
        }
    }

    public boolean setSystemUpdatePolicies(ComponentName admin, int mode) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().setSystemUpdatePolicies(admin, mode);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setSystemUpdatePolicies fail!", e);
            return false;
        }
    }

    public int getSystemUpdatePolicies(ComponentName admin) {
        try {
            int result = getOplusCustomizeRestrictionManagerService().getSystemUpdatePolicies(admin);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "getSystemUpdatePolicies fail!", e);
            return -1;
        }
    }

    public boolean setDataSyncDisabled(boolean disabled) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().setDataSyncDisabled(disabled);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setDataSyncDisabled fail!", e);
            return false;
        }
    }

    public boolean isDataSyncDisabled() {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().isDataSyncDisabled();
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "isDataSyncDisabled fail!", e);
            return false;
        }
    }

    public boolean setUnknownSourceAppInstallDisabled(ComponentName admin, boolean disabled) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().setUnknownSourceAppInstallDisabled(admin, disabled);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setUnknownSourceAppInstallDisabled fail!", e);
            return false;
        }
    }

    public boolean isUnknownSourceAppInstallDisabled(ComponentName admin) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().isUnknownSourceAppInstallDisabled(admin);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "isUnknownSourceAppInstallDisabled fail!", e);
            return false;
        }
    }

    public boolean setSideBarPolicies(ComponentName admin, int mode) {
        try {
            return getOplusCustomizeRestrictionManagerService().setSideBarPolicies(admin, mode);
        } catch (Exception e) {
            Slog.e(TAG, "setSideBarPolicies Error", e);
            return false;
        }
    }

    public int getSideBarPolicies(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getSideBarPolicies(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getSideBarPolicies Error", e);
            return 0;
        }
    }

    public void setChangePictorialDisable(ComponentName admin, boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setChangePictorialDisable(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setChangePictorialDisable fail!", e);
        }
    }

    public boolean isChangePictorialDisabled(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isChangePictorialDisabled(admin);
        } catch (Exception e) {
            Slog.e(TAG, "isChangePictorialDisabled fail!", e);
            return false;
        }
    }

    public boolean isAppInCustomVoipRecordList(String pakageName) {
        try {
            return getOplusCustomizeRestrictionManagerService().isAppInCustomVoipRecordList(pakageName);
        } catch (Exception e) {
            Slog.e(TAG, "isAppInCustomVoipRecordList Error" + e);
            return false;
        }
    }

    public void setPasswordExpirationTimeout(ComponentName admin, long timeoutMs) {
        try {
            getOplusCustomizeRestrictionManagerService().setPasswordExpirationTimeout(admin, timeoutMs);
        } catch (Exception e) {
            Slog.e(TAG, "setPasswordExpirationTimeout fail!", e);
        }
    }

    public long getPasswordExpirationTimeout(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getPasswordExpirationTimeout(admin);
        } catch (Exception e) {
            Slog.e(TAG, "getPasswordExpirationTimeout fail!", e);
            return 0L;
        }
    }

    public boolean setSwipeUpUnlockDisabled(ComponentName admin, boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setSwipeUpUnlockDisabled(admin, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setSwipeUpUnlockDisabled fail!", e);
            return false;
        }
    }

    public boolean isSwipeUpUnlockDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isSwipeUpUnlockDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isSwipeUpUnlockDisabled fail!", e);
            return false;
        }
    }

    public boolean isAndroidAnimationDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isAndroidAnimationDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isAndroidAnimationDisabled fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isAndroidAnimationDisabled Error" + e2);
            return false;
        }
    }

    public boolean setAndroidAnimationDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setAndroidAnimationDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setAndroidAnimationDisabled fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setAndroidAnimationDisabled Error" + e2);
            return false;
        }
    }

    public boolean addDisallowedClearDataCacheApps(List<String> pkgs) {
        try {
            return getOplusCustomizeRestrictionManagerService().addDisallowedClearDataCacheApps(pkgs);
        } catch (RemoteException e) {
            Slog.e(TAG, "addDisallowedClearDataCacheApps RemoteException Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "addDisallowedClearDataCacheApps Error" + e2);
            return false;
        }
    }

    public boolean removeDisallowedClearDataCacheApps(List<String> pkgs) {
        try {
            return getOplusCustomizeRestrictionManagerService().removeDisallowedClearDataCacheApps(pkgs);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeDisallowedClearDataCacheApps RemoteException Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "removeDisallowedClearDataCacheApps Error" + e2);
            return false;
        }
    }

    public List<String> getDisallowedClearDataCacheApps() {
        try {
            return getOplusCustomizeRestrictionManagerService().getDisallowedClearDataCacheApps();
        } catch (RemoteException e) {
            Slog.e(TAG, "getDisallowedClearDataCacheApps RemoteException Error", e);
            return new ArrayList();
        } catch (Exception e2) {
            Slog.e(TAG, "getDisallowedClearDataCacheApps Error" + e2);
            return new ArrayList();
        }
    }

    public void setAppLockDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setAppLockDisabled(disabled);
        } catch (RemoteException e) {
            Slog.e(TAG, "setAppLockDisabled RemoteException Error", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setAppLockDisabled error: ", e2);
        }
    }

    public boolean isAppLockDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isAppLockDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isAppLockDisabled RemoteException Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isAppLockDisabled error: ", e2);
            return false;
        }
    }

    public boolean setFindMyPhoneDisabled(boolean disabled) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().setFindMyPhoneDisabled(disabled);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setFindMyPhoneDisabled RemoteException Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setFindMyPhoneDisabled Error exception: ", e2);
            return false;
        }
    }

    public boolean isFindMyPhoneDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isFindMyPhoneDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isFindMyPhoneDisabled RemoteException Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isFindMyPhoneDisabled Error exception: ", e2);
            return false;
        }
    }

    public boolean isPrivateSafeDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isPrivateSafeDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isPrivateSafeDisabled RemoteException Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isPrivateSafeDisabled Error exception: ", e2);
            return false;
        }
    }

    public boolean setPrivateSafeDisabled(boolean disabled) {
        try {
            boolean result = getOplusCustomizeRestrictionManagerService().setPrivateSafeDisabled(disabled);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setPrivateSafeDisabled RemoteException Error", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setPrivateSafeDisabled Error exception: ", e2);
            return false;
        }
    }

    public boolean setCustomizeDozeModeDisabled(boolean disabled) {
        try {
            return getOplusCustomizeRestrictionManagerService().setCustomizeDozeModeDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setCustomizeDozeModeDisabled fail!", e);
            return false;
        }
    }

    public boolean isCustomizeDozeModeDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isCustomizeDozeModeDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isCustomizeDozeModeDisabled fail!", e);
            return false;
        }
    }

    public void setLongPressVolumeUpDisabled(boolean disabled) {
        try {
            getOplusCustomizeRestrictionManagerService().setLongPressVolumeUpDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setLongPressVolumeUpDisabled error: ", e);
        }
    }

    public boolean isLongPressVolumeUpDisabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isLongPressVolumeUpDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isLongPressVolumeUpDisabled error: ", e);
            return false;
        }
    }

    public boolean setBluetoothRandomEnabled(boolean enable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setBluetoothRandomEnabled(enable);
        } catch (RemoteException e) {
            Slog.e(TAG, "isLongPressVolumeUpDisabled error: ", e);
            return false;
        }
    }

    public boolean isBluetoothRandomEnabled() {
        try {
            return getOplusCustomizeRestrictionManagerService().isBluetoothRandomEnabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isLongPressVolumeUpDisabled error: ", e);
            return false;
        }
    }

    public String getLocalBluetoothAddress() {
        try {
            return getOplusCustomizeRestrictionManagerService().getLocalBluetoothAddress();
        } catch (RemoteException e) {
            Slog.e(TAG, "getLocalBluetoothAddress error", e);
            return "00:00:00:00:00:00";
        }
    }

    public String getLocalBtRandomAddress() {
        try {
            return getOplusCustomizeRestrictionManagerService().getLocalBtRandomAddress();
        } catch (RemoteException e) {
            Slog.e(TAG, "getLocalBtRandomAddress error", e);
            return "00:00:00:00:00:00";
        }
    }

    public boolean setWlanAllowListWithoutScanLimit(ComponentName admin, List<String> allowList) {
        try {
            return getOplusCustomizeRestrictionManagerService().setWlanAllowListWithoutScanLimit(admin, allowList);
        } catch (RemoteException e) {
            Slog.e(TAG, "setWlanAllowListWithoutScanLimit fail!", e);
            return false;
        } catch (SecurityException e2) {
            Slog.e(TAG, "setWlanAllowListWithoutScanLimit fail!", e2);
            return false;
        }
    }

    public List<String> getWlanAllowListWithoutScanLimit(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getWlanAllowListWithoutScanLimit(admin);
        } catch (RemoteException e) {
            Slog.e(TAG, "getWlanAllowListWithoutScanLimit fail!", e);
            return new ArrayList();
        } catch (SecurityException e2) {
            Slog.e(TAG, "getWlanAllowListWithoutScanLimit fail!", e2);
            return new ArrayList();
        }
    }

    public boolean setWifiAssistantPolicies(ComponentName admin, int mode) {
        try {
            return getOplusCustomizeRestrictionManagerService().setWifiAssistantPolicies(admin, mode);
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiAssistantPolicies fail!", e);
            return false;
        } catch (SecurityException e2) {
            Slog.e(TAG, "setWifiAssistantPolicies fail!", e2);
            return false;
        }
    }

    public int getWifiAssistantPolicies(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().getWifiAssistantPolicies(admin);
        } catch (RemoteException e) {
            Slog.e(TAG, "getWifiAssistantPolicies fail!", e);
            return -1;
        } catch (SecurityException e2) {
            Slog.e(TAG, "getWifiAssistantPolicies fail!", e2);
            return -1;
        }
    }

    public boolean setWifiRandomMacForceDisable(ComponentName admin, boolean enable) {
        try {
            return getOplusCustomizeRestrictionManagerService().setWifiRandomMacForceDisable(admin, enable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiRandomMacForceDisable fail!", e);
            return false;
        } catch (SecurityException e2) {
            Slog.e(TAG, "setWifiRandomMacForceDisable fail!", e2);
            return false;
        }
    }

    public boolean isWifiRandomMacForceDisable(ComponentName admin) {
        try {
            return getOplusCustomizeRestrictionManagerService().isWifiRandomMacForceDisable(admin);
        } catch (RemoteException e) {
            Slog.e(TAG, "isWifiRandomMacForceDisable fail!", e);
            return false;
        } catch (SecurityException e2) {
            Slog.e(TAG, "isWifiRandomMacForceDisable fail!", e2);
            return false;
        }
    }

    public float getWifiSarPwrDbm() {
        try {
            return getOplusCustomizeRestrictionManagerService().getWifiSarPwrDbm();
        } catch (RemoteException e) {
            Slog.e(TAG, "getWifiSarPwrDbm error: ", e);
            return -1.0f;
        }
    }

    public float getWifiSarPwrMw() {
        try {
            return getOplusCustomizeRestrictionManagerService().getWifiSarPwrMw();
        } catch (RemoteException e) {
            Slog.e(TAG, "getWifiSarPwrMw error: ", e);
            return -1.0f;
        }
    }

    public boolean setWifiSarPwrDbm(float dbm) {
        try {
            return getOplusCustomizeRestrictionManagerService().setWifiSarPwrDbm(dbm);
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiSarPwrDbm error: ", e);
            return false;
        }
    }

    public boolean setWifiSarPwrMw(float mw) {
        try {
            return getOplusCustomizeRestrictionManagerService().setWifiSarPwrMw(mw);
        } catch (RemoteException e) {
            Slog.e(TAG, "setWifiSarPwrMw error: ", e);
            return false;
        }
    }

    public boolean disableWifiSar() {
        try {
            return getOplusCustomizeRestrictionManagerService().disableWifiSar();
        } catch (RemoteException e) {
            Slog.e(TAG, "disableWifiSar error: ", e);
            return false;
        }
    }
}
