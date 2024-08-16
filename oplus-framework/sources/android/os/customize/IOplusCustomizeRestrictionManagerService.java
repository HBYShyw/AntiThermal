package android.os.customize;

import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeRestrictionManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeRestrictionManagerService";

    void addAppInstallPackageBlacklist(int i, List<String> list) throws RemoteException;

    void addAppInstallPackageWhitelist(int i, List<String> list) throws RemoteException;

    boolean addDisallowedClearDataCacheApps(List<String> list) throws RemoteException;

    boolean allowWifiCellularNetwork(ComponentName componentName, String str) throws RemoteException;

    void applyQSRestriction(String str, int i) throws RemoteException;

    void disableQSRestriction(String str, int i) throws RemoteException;

    boolean disableWifiSar() throws RemoteException;

    int getAirplanePolices(ComponentName componentName) throws RemoteException;

    List<String> getAppInstallPackageList(int i) throws RemoteException;

    int getAppInstallRestrictionPolicies() throws RemoteException;

    List<String> getAppUninstallationPackageList(int i) throws RemoteException;

    int getAppUninstallationPolicies() throws RemoteException;

    List<String> getApplicationDisabledInLauncherOrRecentTask(int i) throws RemoteException;

    List<String> getBluetoothDisabledProfiles() throws RemoteException;

    int getCameraPolicies() throws RemoteException;

    boolean getClipboardStatus() throws RemoteException;

    int getDefaultDataCard(ComponentName componentName) throws RemoteException;

    List<String> getDisallowedClearDataCacheApps() throws RemoteException;

    boolean getFileSharedDisabled() throws RemoteException;

    boolean getForbidRecordScreenState() throws RemoteException;

    int getGpsPolicies(ComponentName componentName) throws RemoteException;

    String getLocalBluetoothAddress() throws RemoteException;

    String getLocalBtRandomAddress() throws RemoteException;

    int getMobileDataMode(ComponentName componentName) throws RemoteException;

    int getNfcPolicies(ComponentName componentName) throws RemoteException;

    long getPasswordExpirationTimeout(ComponentName componentName) throws RemoteException;

    int getPasswordNumSequenceMaxLength() throws RemoteException;

    int getPasswordRepeatMaxLength() throws RemoteException;

    boolean getPowerDisable() throws RemoteException;

    boolean getQSRestrictionState(String str, int i) throws RemoteException;

    int getQSRestrictionValue(String str) throws RemoteException;

    long getRequiredStrongAuthTime(ComponentName componentName) throws RemoteException;

    int getSideBarPolicies(ComponentName componentName) throws RemoteException;

    int getSlot1DataConnectivityDisabled(ComponentName componentName) throws RemoteException;

    int getSlot2DataConnectivityDisabled(ComponentName componentName) throws RemoteException;

    boolean getSplitScreenDisable(ComponentName componentName) throws RemoteException;

    int getSystemUpdatePolicies(ComponentName componentName) throws RemoteException;

    int getTorchPolicies() throws RemoteException;

    int getUnlockByFacePolicies(ComponentName componentName) throws RemoteException;

    int getUnlockByFingerprintPolicies(ComponentName componentName) throws RemoteException;

    int getUserPasswordPolicies(ComponentName componentName) throws RemoteException;

    int getWifiAssistantPolicies(ComponentName componentName) throws RemoteException;

    float getWifiSarPwrDbm() throws RemoteException;

    float getWifiSarPwrMw() throws RemoteException;

    List<String> getWlanAllowListWithoutScanLimit(ComponentName componentName) throws RemoteException;

    boolean isAdbDisabled(ComponentName componentName) throws RemoteException;

    boolean isAndroidAnimationDisabled() throws RemoteException;

    boolean isAndroidBeamDisabled(ComponentName componentName) throws RemoteException;

    boolean isAppInCustomVoipRecordList(String str) throws RemoteException;

    boolean isAppLockDisabled() throws RemoteException;

    boolean isBackButtonDisabled() throws RemoteException;

    boolean isBiometricDisabled() throws RemoteException;

    boolean isBluetoothConnectableDisabled() throws RemoteException;

    boolean isBluetoothDataTransferDisabled() throws RemoteException;

    boolean isBluetoothDisabled() throws RemoteException;

    boolean isBluetoothEnabled() throws RemoteException;

    boolean isBluetoothOutGoingCallDisabled() throws RemoteException;

    boolean isBluetoothPairingDisabled() throws RemoteException;

    boolean isBluetoothRandomEnabled() throws RemoteException;

    boolean isBluetoothTetheringDisabled() throws RemoteException;

    boolean isChangePictorialDisabled(ComponentName componentName) throws RemoteException;

    boolean isChangeWallpaperDisabled(ComponentName componentName) throws RemoteException;

    boolean isCustomizeDozeModeDisabled() throws RemoteException;

    boolean isDataRoamingDisabled() throws RemoteException;

    boolean isDataSyncDisabled() throws RemoteException;

    boolean isDiscoverableDisabled() throws RemoteException;

    boolean isEchoPasswordDisabled() throws RemoteException;

    boolean isExternalStorageDisabled() throws RemoteException;

    boolean isFindMyPhoneDisabled() throws RemoteException;

    boolean isFloatTaskDisabled(ComponentName componentName) throws RemoteException;

    boolean isHomeButtonDisabled() throws RemoteException;

    boolean isLanguageChangeDisabled(ComponentName componentName) throws RemoteException;

    boolean isLimitedDiscoverableDisabled() throws RemoteException;

    boolean isLocationBluetoothScanningDisabled() throws RemoteException;

    boolean isLongPressLauncherDisabled() throws RemoteException;

    boolean isLongPressVolumeUpDisabled() throws RemoteException;

    boolean isMmsDisabled() throws RemoteException;

    boolean isMmsSendReceiveDisabled() throws RemoteException;

    boolean isMultiAppSupport() throws RemoteException;

    boolean isNFCDisabled(ComponentName componentName) throws RemoteException;

    boolean isNFCTurnOn(ComponentName componentName) throws RemoteException;

    boolean isNavigationBarDisabled() throws RemoteException;

    boolean isNavigationModeRevertible() throws RemoteException;

    boolean isPowerSavingModeDisabled() throws RemoteException;

    boolean isPrivateSafeDisabled() throws RemoteException;

    boolean isSafeModeDisabled() throws RemoteException;

    boolean isSettingsApplicationDisabled(ComponentName componentName) throws RemoteException;

    boolean isSleepByPowerButtonDisabled(ComponentName componentName) throws RemoteException;

    boolean isSleepStandbyOptimizationDisabled() throws RemoteException;

    boolean isSmsReceiveDisabled() throws RemoteException;

    boolean isSmsSendDisabled() throws RemoteException;

    boolean isSuperPowerSavingModeDisabled() throws RemoteException;

    boolean isSwipeUpUnlockDisabled() throws RemoteException;

    boolean isTaskButtonDisabled() throws RemoteException;

    boolean isUSBDataDisabled() throws RemoteException;

    boolean isUSBFileTransferDisabled() throws RemoteException;

    boolean isUSBOtgDisabled() throws RemoteException;

    boolean isUnknownSourceAppInstallDisabled(ComponentName componentName) throws RemoteException;

    boolean isUnlockByFaceDisabled(ComponentName componentName) throws RemoteException;

    boolean isUnlockByFingerprintDisabled(ComponentName componentName) throws RemoteException;

    boolean isUsbDebugSwitchDisabled(ComponentName componentName) throws RemoteException;

    boolean isUsbTetheringDisabled() throws RemoteException;

    boolean isVoiceDisabled(ComponentName componentName) throws RemoteException;

    boolean isVoiceIncomingDisabled(ComponentName componentName, int i) throws RemoteException;

    boolean isVoiceOutgoingDisabled(ComponentName componentName, int i) throws RemoteException;

    boolean isWifiDisabled(ComponentName componentName) throws RemoteException;

    boolean isWifiOpen(ComponentName componentName) throws RemoteException;

    boolean isWifiRandomMacForceDisable(ComponentName componentName) throws RemoteException;

    void openCloseNFC(ComponentName componentName, boolean z) throws RemoteException;

    boolean removeDisallowedClearDataCacheApps(List<String> list) throws RemoteException;

    void setAdbDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setAirplanePolices(ComponentName componentName, int i) throws RemoteException;

    boolean setAndroidAnimationDisabled(boolean z) throws RemoteException;

    boolean setAndroidBeamDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setAppInstallRestrictionPolicies(int i) throws RemoteException;

    void setAppLockDisabled(boolean z) throws RemoteException;

    void setAppUninstallationPolicies(int i, List<String> list) throws RemoteException;

    void setApplicationDisabledInLauncherOrRecentTask(List<String> list, int i) throws RemoteException;

    void setBackButtonDisabled(boolean z) throws RemoteException;

    void setBiometricDisabled(boolean z) throws RemoteException;

    boolean setBluetoothConnectableDisabled(boolean z) throws RemoteException;

    boolean setBluetoothDataTransferDisable(boolean z) throws RemoteException;

    void setBluetoothDisabled(boolean z) throws RemoteException;

    boolean setBluetoothDisabledProfiles(List<String> list) throws RemoteException;

    void setBluetoothEnabled(boolean z) throws RemoteException;

    boolean setBluetoothOutGoingCallDisable(boolean z) throws RemoteException;

    boolean setBluetoothPairingDisable(boolean z) throws RemoteException;

    boolean setBluetoothRandomEnabled(boolean z) throws RemoteException;

    boolean setBluetoothTetheringDisable(boolean z) throws RemoteException;

    boolean setCameraPolicies(int i) throws RemoteException;

    void setChangePictorialDisable(ComponentName componentName, boolean z) throws RemoteException;

    void setChangeWallpaperDisable(ComponentName componentName, boolean z) throws RemoteException;

    void setClipboardEnabled(boolean z) throws RemoteException;

    boolean setCustomizeDozeModeDisabled(boolean z) throws RemoteException;

    boolean setDataRoamingDisabled(boolean z) throws RemoteException;

    boolean setDataSyncDisabled(boolean z) throws RemoteException;

    Bundle setDefaultDataCard(ComponentName componentName, int i) throws RemoteException;

    boolean setDiscoverableDisabled(boolean z) throws RemoteException;

    boolean setEchoPasswordDisabled(boolean z) throws RemoteException;

    void setExternalStorageDisabled(boolean z) throws RemoteException;

    boolean setFileSharedDisabled(boolean z) throws RemoteException;

    boolean setFindMyPhoneDisabled(boolean z) throws RemoteException;

    boolean setFloatTaskDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setGpsPolicies(ComponentName componentName, int i) throws RemoteException;

    void setHomeButtonDisabled(boolean z) throws RemoteException;

    void setLanguageChangeDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setLimitedDiscoverableDisable(boolean z) throws RemoteException;

    boolean setLocationBluetoothScanningDisabled(boolean z) throws RemoteException;

    void setLongPressLauncherDisabled(boolean z) throws RemoteException;

    void setLongPressVolumeUpDisabled(boolean z) throws RemoteException;

    void setMmsDisabled(boolean z) throws RemoteException;

    void setMobileDataMode(ComponentName componentName, int i) throws RemoteException;

    void setMultiAppSupport(boolean z) throws RemoteException;

    void setNFCDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setNavigationBarDisabled(boolean z) throws RemoteException;

    boolean setNavigationMode(int i, boolean z) throws RemoteException;

    boolean setNfcPolicies(ComponentName componentName, int i) throws RemoteException;

    void setPasswordExpirationTimeout(ComponentName componentName, long j) throws RemoteException;

    boolean setPasswordNumSequenceMaxLength(int i) throws RemoteException;

    boolean setPasswordRepeatMaxLength(int i) throws RemoteException;

    void setPowerDisable(boolean z) throws RemoteException;

    void setPowerSavingModeDisabled(boolean z) throws RemoteException;

    boolean setPrivateSafeDisabled(boolean z) throws RemoteException;

    void setRequiredStrongAuthTime(ComponentName componentName, long j) throws RemoteException;

    void setSafeModeDisabled(boolean z) throws RemoteException;

    boolean setScreenCaptureDisabled(boolean z) throws RemoteException;

    void setSettingsApplicationDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setSideBarPolicies(ComponentName componentName, int i) throws RemoteException;

    boolean setSleepByPowerButtonDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setSleepStandbyOptimizationDisabled(boolean z) throws RemoteException;

    void setSlot1DataConnectivityDisabled(ComponentName componentName, String str) throws RemoteException;

    void setSlot2DataConnectivityDisabled(ComponentName componentName, String str) throws RemoteException;

    boolean setSplitScreenDisable(ComponentName componentName, boolean z) throws RemoteException;

    boolean setSuperPowerSavingModeDisabled(boolean z) throws RemoteException;

    boolean setSwipeUpUnlockDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setSystemUpdatePolicies(ComponentName componentName, int i) throws RemoteException;

    void setTaskButtonDisabled(boolean z) throws RemoteException;

    boolean setTorchPolicies(int i) throws RemoteException;

    void setUSBDataDisabled(boolean z) throws RemoteException;

    void setUSBFileTransferDisabled(boolean z) throws RemoteException;

    void setUSBOtgDisabled(boolean z) throws RemoteException;

    boolean setUnknownSourceAppInstallDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setUnlockByFaceDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setUnlockByFacePolicies(ComponentName componentName, int i) throws RemoteException;

    boolean setUnlockByFingerprintDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setUnlockByFingerprintPolicies(ComponentName componentName, int i) throws RemoteException;

    void setUsbDebugSwitchDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setUsbTetheringDisable(boolean z) throws RemoteException;

    boolean setUserPasswordPolicies(ComponentName componentName, int i) throws RemoteException;

    void setVoiceDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setVoiceIncomingDisable(ComponentName componentName, boolean z) throws RemoteException;

    void setVoiceOutgoingDisable(ComponentName componentName, boolean z) throws RemoteException;

    boolean setWifiAssistantPolicies(ComponentName componentName, int i) throws RemoteException;

    void setWifiDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setWifiInBackground(ComponentName componentName, boolean z) throws RemoteException;

    boolean setWifiRandomMacForceDisable(ComponentName componentName, boolean z) throws RemoteException;

    boolean setWifiSarPwrDbm(float f) throws RemoteException;

    boolean setWifiSarPwrMw(float f) throws RemoteException;

    boolean setWlanAllowListWithoutScanLimit(ComponentName componentName, List<String> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeRestrictionManagerService {
        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setClipboardEnabled(boolean enable) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean getClipboardStatus() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setAppInstallRestrictionPolicies(int mode) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getAppInstallRestrictionPolicies() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public List<String> getAppInstallPackageList(int mode) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void addAppInstallPackageBlacklist(int pattern, List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void addAppInstallPackageWhitelist(int pattern, List<String> packageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setUSBDataDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isUSBDataDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setUSBFileTransferDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isUSBFileTransferDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setUSBOtgDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isUSBOtgDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setBiometricDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBiometricDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setSafeModeDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isSafeModeDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setExternalStorageDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isExternalStorageDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setUsbTetheringDisable(boolean disable) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isUsbTetheringDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setRequiredStrongAuthTime(ComponentName admin, long timeoutMs) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public long getRequiredStrongAuthTime(ComponentName admin) throws RemoteException {
            return 0L;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setScreenCaptureDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean getForbidRecordScreenState() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setAppUninstallationPolicies(int mode, List<String> appPackageNames) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public List<String> getAppUninstallationPackageList(int mode) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getAppUninstallationPolicies() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setSlot1DataConnectivityDisabled(ComponentName admin, String val) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setSlot2DataConnectivityDisabled(ComponentName admin, String val) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setVoiceOutgoingDisable(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setVoiceIncomingDisable(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isVoiceDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setVoiceDisabled(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isVoiceIncomingDisabled(ComponentName admin, int slot) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isVoiceOutgoingDisabled(ComponentName admin, int slot) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setPowerSavingModeDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isPowerSavingModeDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setSuperPowerSavingModeDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isSuperPowerSavingModeDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setDataRoamingDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isDataRoamingDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void applyQSRestriction(String key, int newValue) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void disableQSRestriction(String key, int newValue) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getQSRestrictionValue(String key) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean getQSRestrictionState(String key, int state) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setSleepByPowerButtonDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isSleepByPowerButtonDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setFileSharedDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean getFileSharedDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setNavigationMode(int mode, boolean revertible) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isNavigationModeRevertible() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setBluetoothDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBluetoothDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setBluetoothEnabled(boolean enabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBluetoothEnabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setBluetoothConnectableDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBluetoothConnectableDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setDiscoverableDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isDiscoverableDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setLimitedDiscoverableDisable(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isLimitedDiscoverableDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setBluetoothPairingDisable(boolean isDisable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBluetoothPairingDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setBluetoothOutGoingCallDisable(boolean isDisable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBluetoothOutGoingCallDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setBluetoothDataTransferDisable(boolean isDisable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBluetoothDataTransferDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setBluetoothTetheringDisable(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBluetoothTetheringDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setLocationBluetoothScanningDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isLocationBluetoothScanningDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setBluetoothDisabledProfiles(List<String> disabledProfiles) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public List<String> getBluetoothDisabledProfiles() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setNFCDisabled(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isNFCDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void openCloseNFC(ComponentName admin, boolean enable) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isNFCTurnOn(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setAndroidBeamDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isAndroidBeamDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setNfcPolicies(ComponentName admin, int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getNfcPolicies(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getMobileDataMode(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setMobileDataMode(ComponentName admin, int mode) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean allowWifiCellularNetwork(ComponentName compName, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setCameraPolicies(int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getCameraPolicies() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setTorchPolicies(int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getTorchPolicies() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setChangeWallpaperDisable(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isChangeWallpaperDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setSettingsApplicationDisabled(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isSettingsApplicationDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setApplicationDisabledInLauncherOrRecentTask(List<String> list, int flag) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public List<String> getApplicationDisabledInLauncherOrRecentTask(int flag) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setAirplanePolices(ComponentName admin, int policy) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getAirplanePolices(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setFloatTaskDisabled(ComponentName componentName, boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isFloatTaskDisabled(ComponentName componentName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isMultiAppSupport() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setMultiAppSupport(boolean support) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setSplitScreenDisable(ComponentName admin, boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean getSplitScreenDisable(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setUserPasswordPolicies(ComponentName admin, int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getUserPasswordPolicies(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setUnlockByFingerprintPolicies(ComponentName admin, int policy) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getUnlockByFingerprintPolicies(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setUnlockByFacePolicies(ComponentName admin, int policy) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getUnlockByFacePolicies(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setUnlockByFingerprintDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isUnlockByFingerprintDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setUnlockByFaceDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isUnlockByFaceDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setPasswordRepeatMaxLength(int length) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getPasswordRepeatMaxLength() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setPasswordNumSequenceMaxLength(int length) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getPasswordNumSequenceMaxLength() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setTaskButtonDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isTaskButtonDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setHomeButtonDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isHomeButtonDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setBackButtonDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBackButtonDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setPowerDisable(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean getPowerDisable() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setLongPressVolumeUpDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isLongPressVolumeUpDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setGpsPolicies(ComponentName admin, int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getGpsPolicies(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getDefaultDataCard(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public Bundle setDefaultDataCard(ComponentName admin, int slot) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getSlot1DataConnectivityDisabled(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getSlot2DataConnectivityDisabled(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setLanguageChangeDisabled(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isLanguageChangeDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isWifiOpen(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setWifiInBackground(ComponentName admin, boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isWifiDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setWifiDisabled(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setEchoPasswordDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isEchoPasswordDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setMmsDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isMmsDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isSmsReceiveDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isSmsSendDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isMmsSendReceiveDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setSleepStandbyOptimizationDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isSleepStandbyOptimizationDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setUsbDebugSwitchDisabled(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isUsbDebugSwitchDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setAdbDisabled(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isAdbDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setNavigationBarDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isNavigationBarDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setLongPressLauncherDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isLongPressLauncherDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setSystemUpdatePolicies(ComponentName admin, int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getSystemUpdatePolicies(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setDataSyncDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isDataSyncDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setUnknownSourceAppInstallDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isUnknownSourceAppInstallDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setSideBarPolicies(ComponentName admin, int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getSideBarPolicies(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setChangePictorialDisable(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isChangePictorialDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isAppInCustomVoipRecordList(String pakageName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setPasswordExpirationTimeout(ComponentName admin, long timeoutMs) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public long getPasswordExpirationTimeout(ComponentName admin) throws RemoteException {
            return 0L;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setSwipeUpUnlockDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isSwipeUpUnlockDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isAndroidAnimationDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setAndroidAnimationDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean addDisallowedClearDataCacheApps(List<String> pkgs) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean removeDisallowedClearDataCacheApps(List<String> pkgs) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public List<String> getDisallowedClearDataCacheApps() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public void setAppLockDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isAppLockDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setFindMyPhoneDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isFindMyPhoneDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isPrivateSafeDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setPrivateSafeDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setCustomizeDozeModeDisabled(boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isCustomizeDozeModeDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setBluetoothRandomEnabled(boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isBluetoothRandomEnabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public String getLocalBluetoothAddress() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public String getLocalBtRandomAddress() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setWlanAllowListWithoutScanLimit(ComponentName admin, List<String> allowList) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public List<String> getWlanAllowListWithoutScanLimit(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setWifiAssistantPolicies(ComponentName admin, int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public int getWifiAssistantPolicies(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setWifiRandomMacForceDisable(ComponentName admin, boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean isWifiRandomMacForceDisable(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public float getWifiSarPwrDbm() throws RemoteException {
            return 0.0f;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public float getWifiSarPwrMw() throws RemoteException {
            return 0.0f;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setWifiSarPwrDbm(float dbm) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean setWifiSarPwrMw(float mw) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
        public boolean disableWifiSar() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeRestrictionManagerService {
        static final int TRANSACTION_addAppInstallPackageBlacklist = 6;
        static final int TRANSACTION_addAppInstallPackageWhitelist = 7;
        static final int TRANSACTION_addDisallowedClearDataCacheApps = 174;
        static final int TRANSACTION_allowWifiCellularNetwork = 85;
        static final int TRANSACTION_applyQSRestriction = 43;
        static final int TRANSACTION_disableQSRestriction = 44;
        static final int TRANSACTION_disableWifiSar = 199;
        static final int TRANSACTION_getAirplanePolices = 97;
        static final int TRANSACTION_getAppInstallPackageList = 5;
        static final int TRANSACTION_getAppInstallRestrictionPolicies = 4;
        static final int TRANSACTION_getAppUninstallationPackageList = 27;
        static final int TRANSACTION_getAppUninstallationPolicies = 28;
        static final int TRANSACTION_getApplicationDisabledInLauncherOrRecentTask = 95;
        static final int TRANSACTION_getBluetoothDisabledProfiles = 74;
        static final int TRANSACTION_getCameraPolicies = 87;
        static final int TRANSACTION_getClipboardStatus = 2;
        static final int TRANSACTION_getDefaultDataCard = 130;
        static final int TRANSACTION_getDisallowedClearDataCacheApps = 176;
        static final int TRANSACTION_getFileSharedDisabled = 50;
        static final int TRANSACTION_getForbidRecordScreenState = 25;
        static final int TRANSACTION_getGpsPolicies = 129;
        static final int TRANSACTION_getLocalBluetoothAddress = 187;
        static final int TRANSACTION_getLocalBtRandomAddress = 188;
        static final int TRANSACTION_getMobileDataMode = 83;
        static final int TRANSACTION_getNfcPolicies = 82;
        static final int TRANSACTION_getPasswordExpirationTimeout = 169;
        static final int TRANSACTION_getPasswordNumSequenceMaxLength = 117;
        static final int TRANSACTION_getPasswordRepeatMaxLength = 115;
        static final int TRANSACTION_getPowerDisable = 125;
        static final int TRANSACTION_getQSRestrictionState = 46;
        static final int TRANSACTION_getQSRestrictionValue = 45;
        static final int TRANSACTION_getRequiredStrongAuthTime = 23;
        static final int TRANSACTION_getSideBarPolicies = 164;
        static final int TRANSACTION_getSlot1DataConnectivityDisabled = 132;
        static final int TRANSACTION_getSlot2DataConnectivityDisabled = 133;
        static final int TRANSACTION_getSplitScreenDisable = 103;
        static final int TRANSACTION_getSystemUpdatePolicies = 158;
        static final int TRANSACTION_getTorchPolicies = 89;
        static final int TRANSACTION_getUnlockByFacePolicies = 109;
        static final int TRANSACTION_getUnlockByFingerprintPolicies = 107;
        static final int TRANSACTION_getUserPasswordPolicies = 105;
        static final int TRANSACTION_getWifiAssistantPolicies = 192;
        static final int TRANSACTION_getWifiSarPwrDbm = 195;
        static final int TRANSACTION_getWifiSarPwrMw = 196;
        static final int TRANSACTION_getWlanAllowListWithoutScanLimit = 190;
        static final int TRANSACTION_isAdbDisabled = 152;
        static final int TRANSACTION_isAndroidAnimationDisabled = 172;
        static final int TRANSACTION_isAndroidBeamDisabled = 80;
        static final int TRANSACTION_isAppInCustomVoipRecordList = 167;
        static final int TRANSACTION_isAppLockDisabled = 178;
        static final int TRANSACTION_isBackButtonDisabled = 123;
        static final int TRANSACTION_isBiometricDisabled = 15;
        static final int TRANSACTION_isBluetoothConnectableDisabled = 58;
        static final int TRANSACTION_isBluetoothDataTransferDisabled = 68;
        static final int TRANSACTION_isBluetoothDisabled = 54;
        static final int TRANSACTION_isBluetoothEnabled = 56;
        static final int TRANSACTION_isBluetoothOutGoingCallDisabled = 66;
        static final int TRANSACTION_isBluetoothPairingDisabled = 64;
        static final int TRANSACTION_isBluetoothRandomEnabled = 186;
        static final int TRANSACTION_isBluetoothTetheringDisabled = 70;
        static final int TRANSACTION_isChangePictorialDisabled = 166;
        static final int TRANSACTION_isChangeWallpaperDisabled = 91;
        static final int TRANSACTION_isCustomizeDozeModeDisabled = 184;
        static final int TRANSACTION_isDataRoamingDisabled = 42;
        static final int TRANSACTION_isDataSyncDisabled = 160;
        static final int TRANSACTION_isDiscoverableDisabled = 60;
        static final int TRANSACTION_isEchoPasswordDisabled = 141;
        static final int TRANSACTION_isExternalStorageDisabled = 19;
        static final int TRANSACTION_isFindMyPhoneDisabled = 180;
        static final int TRANSACTION_isFloatTaskDisabled = 99;
        static final int TRANSACTION_isHomeButtonDisabled = 121;
        static final int TRANSACTION_isLanguageChangeDisabled = 135;
        static final int TRANSACTION_isLimitedDiscoverableDisabled = 62;
        static final int TRANSACTION_isLocationBluetoothScanningDisabled = 72;
        static final int TRANSACTION_isLongPressLauncherDisabled = 156;
        static final int TRANSACTION_isLongPressVolumeUpDisabled = 127;
        static final int TRANSACTION_isMmsDisabled = 143;
        static final int TRANSACTION_isMmsSendReceiveDisabled = 146;
        static final int TRANSACTION_isMultiAppSupport = 100;
        static final int TRANSACTION_isNFCDisabled = 76;
        static final int TRANSACTION_isNFCTurnOn = 78;
        static final int TRANSACTION_isNavigationBarDisabled = 154;
        static final int TRANSACTION_isNavigationModeRevertible = 52;
        static final int TRANSACTION_isPowerSavingModeDisabled = 38;
        static final int TRANSACTION_isPrivateSafeDisabled = 181;
        static final int TRANSACTION_isSafeModeDisabled = 17;
        static final int TRANSACTION_isSettingsApplicationDisabled = 93;
        static final int TRANSACTION_isSleepByPowerButtonDisabled = 48;
        static final int TRANSACTION_isSleepStandbyOptimizationDisabled = 148;
        static final int TRANSACTION_isSmsReceiveDisabled = 144;
        static final int TRANSACTION_isSmsSendDisabled = 145;
        static final int TRANSACTION_isSuperPowerSavingModeDisabled = 40;
        static final int TRANSACTION_isSwipeUpUnlockDisabled = 171;
        static final int TRANSACTION_isTaskButtonDisabled = 119;
        static final int TRANSACTION_isUSBDataDisabled = 9;
        static final int TRANSACTION_isUSBFileTransferDisabled = 11;
        static final int TRANSACTION_isUSBOtgDisabled = 13;
        static final int TRANSACTION_isUnknownSourceAppInstallDisabled = 162;
        static final int TRANSACTION_isUnlockByFaceDisabled = 113;
        static final int TRANSACTION_isUnlockByFingerprintDisabled = 111;
        static final int TRANSACTION_isUsbDebugSwitchDisabled = 150;
        static final int TRANSACTION_isUsbTetheringDisabled = 21;
        static final int TRANSACTION_isVoiceDisabled = 33;
        static final int TRANSACTION_isVoiceIncomingDisabled = 35;
        static final int TRANSACTION_isVoiceOutgoingDisabled = 36;
        static final int TRANSACTION_isWifiDisabled = 138;
        static final int TRANSACTION_isWifiOpen = 136;
        static final int TRANSACTION_isWifiRandomMacForceDisable = 194;
        static final int TRANSACTION_openCloseNFC = 77;
        static final int TRANSACTION_removeDisallowedClearDataCacheApps = 175;
        static final int TRANSACTION_setAdbDisabled = 151;
        static final int TRANSACTION_setAirplanePolices = 96;
        static final int TRANSACTION_setAndroidAnimationDisabled = 173;
        static final int TRANSACTION_setAndroidBeamDisabled = 79;
        static final int TRANSACTION_setAppInstallRestrictionPolicies = 3;
        static final int TRANSACTION_setAppLockDisabled = 177;
        static final int TRANSACTION_setAppUninstallationPolicies = 26;
        static final int TRANSACTION_setApplicationDisabledInLauncherOrRecentTask = 94;
        static final int TRANSACTION_setBackButtonDisabled = 122;
        static final int TRANSACTION_setBiometricDisabled = 14;
        static final int TRANSACTION_setBluetoothConnectableDisabled = 57;
        static final int TRANSACTION_setBluetoothDataTransferDisable = 67;
        static final int TRANSACTION_setBluetoothDisabled = 53;
        static final int TRANSACTION_setBluetoothDisabledProfiles = 73;
        static final int TRANSACTION_setBluetoothEnabled = 55;
        static final int TRANSACTION_setBluetoothOutGoingCallDisable = 65;
        static final int TRANSACTION_setBluetoothPairingDisable = 63;
        static final int TRANSACTION_setBluetoothRandomEnabled = 185;
        static final int TRANSACTION_setBluetoothTetheringDisable = 69;
        static final int TRANSACTION_setCameraPolicies = 86;
        static final int TRANSACTION_setChangePictorialDisable = 165;
        static final int TRANSACTION_setChangeWallpaperDisable = 90;
        static final int TRANSACTION_setClipboardEnabled = 1;
        static final int TRANSACTION_setCustomizeDozeModeDisabled = 183;
        static final int TRANSACTION_setDataRoamingDisabled = 41;
        static final int TRANSACTION_setDataSyncDisabled = 159;
        static final int TRANSACTION_setDefaultDataCard = 131;
        static final int TRANSACTION_setDiscoverableDisabled = 59;
        static final int TRANSACTION_setEchoPasswordDisabled = 140;
        static final int TRANSACTION_setExternalStorageDisabled = 18;
        static final int TRANSACTION_setFileSharedDisabled = 49;
        static final int TRANSACTION_setFindMyPhoneDisabled = 179;
        static final int TRANSACTION_setFloatTaskDisabled = 98;
        static final int TRANSACTION_setGpsPolicies = 128;
        static final int TRANSACTION_setHomeButtonDisabled = 120;
        static final int TRANSACTION_setLanguageChangeDisabled = 134;
        static final int TRANSACTION_setLimitedDiscoverableDisable = 61;
        static final int TRANSACTION_setLocationBluetoothScanningDisabled = 71;
        static final int TRANSACTION_setLongPressLauncherDisabled = 155;
        static final int TRANSACTION_setLongPressVolumeUpDisabled = 126;
        static final int TRANSACTION_setMmsDisabled = 142;
        static final int TRANSACTION_setMobileDataMode = 84;
        static final int TRANSACTION_setMultiAppSupport = 101;
        static final int TRANSACTION_setNFCDisabled = 75;
        static final int TRANSACTION_setNavigationBarDisabled = 153;
        static final int TRANSACTION_setNavigationMode = 51;
        static final int TRANSACTION_setNfcPolicies = 81;
        static final int TRANSACTION_setPasswordExpirationTimeout = 168;
        static final int TRANSACTION_setPasswordNumSequenceMaxLength = 116;
        static final int TRANSACTION_setPasswordRepeatMaxLength = 114;
        static final int TRANSACTION_setPowerDisable = 124;
        static final int TRANSACTION_setPowerSavingModeDisabled = 37;
        static final int TRANSACTION_setPrivateSafeDisabled = 182;
        static final int TRANSACTION_setRequiredStrongAuthTime = 22;
        static final int TRANSACTION_setSafeModeDisabled = 16;
        static final int TRANSACTION_setScreenCaptureDisabled = 24;
        static final int TRANSACTION_setSettingsApplicationDisabled = 92;
        static final int TRANSACTION_setSideBarPolicies = 163;
        static final int TRANSACTION_setSleepByPowerButtonDisabled = 47;
        static final int TRANSACTION_setSleepStandbyOptimizationDisabled = 147;
        static final int TRANSACTION_setSlot1DataConnectivityDisabled = 29;
        static final int TRANSACTION_setSlot2DataConnectivityDisabled = 30;
        static final int TRANSACTION_setSplitScreenDisable = 102;
        static final int TRANSACTION_setSuperPowerSavingModeDisabled = 39;
        static final int TRANSACTION_setSwipeUpUnlockDisabled = 170;
        static final int TRANSACTION_setSystemUpdatePolicies = 157;
        static final int TRANSACTION_setTaskButtonDisabled = 118;
        static final int TRANSACTION_setTorchPolicies = 88;
        static final int TRANSACTION_setUSBDataDisabled = 8;
        static final int TRANSACTION_setUSBFileTransferDisabled = 10;
        static final int TRANSACTION_setUSBOtgDisabled = 12;
        static final int TRANSACTION_setUnknownSourceAppInstallDisabled = 161;
        static final int TRANSACTION_setUnlockByFaceDisabled = 112;
        static final int TRANSACTION_setUnlockByFacePolicies = 108;
        static final int TRANSACTION_setUnlockByFingerprintDisabled = 110;
        static final int TRANSACTION_setUnlockByFingerprintPolicies = 106;
        static final int TRANSACTION_setUsbDebugSwitchDisabled = 149;
        static final int TRANSACTION_setUsbTetheringDisable = 20;
        static final int TRANSACTION_setUserPasswordPolicies = 104;
        static final int TRANSACTION_setVoiceDisabled = 34;
        static final int TRANSACTION_setVoiceIncomingDisable = 32;
        static final int TRANSACTION_setVoiceOutgoingDisable = 31;
        static final int TRANSACTION_setWifiAssistantPolicies = 191;
        static final int TRANSACTION_setWifiDisabled = 139;
        static final int TRANSACTION_setWifiInBackground = 137;
        static final int TRANSACTION_setWifiRandomMacForceDisable = 193;
        static final int TRANSACTION_setWifiSarPwrDbm = 197;
        static final int TRANSACTION_setWifiSarPwrMw = 198;
        static final int TRANSACTION_setWlanAllowListWithoutScanLimit = 189;

        public Stub() {
            attachInterface(this, IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeRestrictionManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeRestrictionManagerService)) {
                return (IOplusCustomizeRestrictionManagerService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setClipboardEnabled";
                case 2:
                    return "getClipboardStatus";
                case 3:
                    return "setAppInstallRestrictionPolicies";
                case 4:
                    return "getAppInstallRestrictionPolicies";
                case 5:
                    return "getAppInstallPackageList";
                case 6:
                    return "addAppInstallPackageBlacklist";
                case 7:
                    return "addAppInstallPackageWhitelist";
                case 8:
                    return "setUSBDataDisabled";
                case 9:
                    return "isUSBDataDisabled";
                case 10:
                    return "setUSBFileTransferDisabled";
                case 11:
                    return "isUSBFileTransferDisabled";
                case 12:
                    return "setUSBOtgDisabled";
                case 13:
                    return "isUSBOtgDisabled";
                case 14:
                    return "setBiometricDisabled";
                case 15:
                    return "isBiometricDisabled";
                case 16:
                    return "setSafeModeDisabled";
                case 17:
                    return "isSafeModeDisabled";
                case 18:
                    return "setExternalStorageDisabled";
                case 19:
                    return "isExternalStorageDisabled";
                case 20:
                    return "setUsbTetheringDisable";
                case 21:
                    return "isUsbTetheringDisabled";
                case 22:
                    return "setRequiredStrongAuthTime";
                case 23:
                    return "getRequiredStrongAuthTime";
                case 24:
                    return "setScreenCaptureDisabled";
                case 25:
                    return "getForbidRecordScreenState";
                case 26:
                    return "setAppUninstallationPolicies";
                case 27:
                    return "getAppUninstallationPackageList";
                case 28:
                    return "getAppUninstallationPolicies";
                case 29:
                    return "setSlot1DataConnectivityDisabled";
                case 30:
                    return "setSlot2DataConnectivityDisabled";
                case 31:
                    return "setVoiceOutgoingDisable";
                case 32:
                    return "setVoiceIncomingDisable";
                case 33:
                    return "isVoiceDisabled";
                case 34:
                    return "setVoiceDisabled";
                case 35:
                    return "isVoiceIncomingDisabled";
                case 36:
                    return "isVoiceOutgoingDisabled";
                case 37:
                    return "setPowerSavingModeDisabled";
                case 38:
                    return "isPowerSavingModeDisabled";
                case 39:
                    return "setSuperPowerSavingModeDisabled";
                case 40:
                    return "isSuperPowerSavingModeDisabled";
                case 41:
                    return "setDataRoamingDisabled";
                case 42:
                    return "isDataRoamingDisabled";
                case 43:
                    return "applyQSRestriction";
                case 44:
                    return "disableQSRestriction";
                case 45:
                    return "getQSRestrictionValue";
                case 46:
                    return "getQSRestrictionState";
                case 47:
                    return "setSleepByPowerButtonDisabled";
                case 48:
                    return "isSleepByPowerButtonDisabled";
                case 49:
                    return "setFileSharedDisabled";
                case 50:
                    return "getFileSharedDisabled";
                case 51:
                    return "setNavigationMode";
                case 52:
                    return "isNavigationModeRevertible";
                case 53:
                    return "setBluetoothDisabled";
                case 54:
                    return "isBluetoothDisabled";
                case 55:
                    return "setBluetoothEnabled";
                case 56:
                    return "isBluetoothEnabled";
                case 57:
                    return "setBluetoothConnectableDisabled";
                case 58:
                    return "isBluetoothConnectableDisabled";
                case 59:
                    return "setDiscoverableDisabled";
                case 60:
                    return "isDiscoverableDisabled";
                case 61:
                    return "setLimitedDiscoverableDisable";
                case 62:
                    return "isLimitedDiscoverableDisabled";
                case 63:
                    return "setBluetoothPairingDisable";
                case 64:
                    return "isBluetoothPairingDisabled";
                case 65:
                    return "setBluetoothOutGoingCallDisable";
                case 66:
                    return "isBluetoothOutGoingCallDisabled";
                case 67:
                    return "setBluetoothDataTransferDisable";
                case 68:
                    return "isBluetoothDataTransferDisabled";
                case 69:
                    return "setBluetoothTetheringDisable";
                case 70:
                    return "isBluetoothTetheringDisabled";
                case 71:
                    return "setLocationBluetoothScanningDisabled";
                case 72:
                    return "isLocationBluetoothScanningDisabled";
                case 73:
                    return "setBluetoothDisabledProfiles";
                case 74:
                    return "getBluetoothDisabledProfiles";
                case 75:
                    return "setNFCDisabled";
                case 76:
                    return "isNFCDisabled";
                case 77:
                    return "openCloseNFC";
                case 78:
                    return "isNFCTurnOn";
                case 79:
                    return "setAndroidBeamDisabled";
                case 80:
                    return "isAndroidBeamDisabled";
                case 81:
                    return "setNfcPolicies";
                case 82:
                    return "getNfcPolicies";
                case 83:
                    return "getMobileDataMode";
                case 84:
                    return "setMobileDataMode";
                case 85:
                    return "allowWifiCellularNetwork";
                case 86:
                    return "setCameraPolicies";
                case 87:
                    return "getCameraPolicies";
                case 88:
                    return "setTorchPolicies";
                case 89:
                    return "getTorchPolicies";
                case 90:
                    return "setChangeWallpaperDisable";
                case 91:
                    return "isChangeWallpaperDisabled";
                case 92:
                    return "setSettingsApplicationDisabled";
                case 93:
                    return "isSettingsApplicationDisabled";
                case 94:
                    return "setApplicationDisabledInLauncherOrRecentTask";
                case 95:
                    return "getApplicationDisabledInLauncherOrRecentTask";
                case 96:
                    return "setAirplanePolices";
                case 97:
                    return "getAirplanePolices";
                case 98:
                    return "setFloatTaskDisabled";
                case 99:
                    return "isFloatTaskDisabled";
                case 100:
                    return "isMultiAppSupport";
                case 101:
                    return "setMultiAppSupport";
                case 102:
                    return "setSplitScreenDisable";
                case 103:
                    return "getSplitScreenDisable";
                case 104:
                    return "setUserPasswordPolicies";
                case 105:
                    return "getUserPasswordPolicies";
                case 106:
                    return "setUnlockByFingerprintPolicies";
                case 107:
                    return "getUnlockByFingerprintPolicies";
                case 108:
                    return "setUnlockByFacePolicies";
                case 109:
                    return "getUnlockByFacePolicies";
                case 110:
                    return "setUnlockByFingerprintDisabled";
                case 111:
                    return "isUnlockByFingerprintDisabled";
                case 112:
                    return "setUnlockByFaceDisabled";
                case 113:
                    return "isUnlockByFaceDisabled";
                case 114:
                    return "setPasswordRepeatMaxLength";
                case 115:
                    return "getPasswordRepeatMaxLength";
                case 116:
                    return "setPasswordNumSequenceMaxLength";
                case 117:
                    return "getPasswordNumSequenceMaxLength";
                case 118:
                    return "setTaskButtonDisabled";
                case 119:
                    return "isTaskButtonDisabled";
                case 120:
                    return "setHomeButtonDisabled";
                case 121:
                    return "isHomeButtonDisabled";
                case 122:
                    return "setBackButtonDisabled";
                case 123:
                    return "isBackButtonDisabled";
                case 124:
                    return "setPowerDisable";
                case 125:
                    return "getPowerDisable";
                case 126:
                    return "setLongPressVolumeUpDisabled";
                case 127:
                    return "isLongPressVolumeUpDisabled";
                case 128:
                    return "setGpsPolicies";
                case 129:
                    return "getGpsPolicies";
                case 130:
                    return "getDefaultDataCard";
                case 131:
                    return "setDefaultDataCard";
                case 132:
                    return "getSlot1DataConnectivityDisabled";
                case 133:
                    return "getSlot2DataConnectivityDisabled";
                case 134:
                    return "setLanguageChangeDisabled";
                case 135:
                    return "isLanguageChangeDisabled";
                case 136:
                    return "isWifiOpen";
                case 137:
                    return "setWifiInBackground";
                case 138:
                    return "isWifiDisabled";
                case 139:
                    return "setWifiDisabled";
                case 140:
                    return "setEchoPasswordDisabled";
                case 141:
                    return "isEchoPasswordDisabled";
                case 142:
                    return "setMmsDisabled";
                case 143:
                    return "isMmsDisabled";
                case 144:
                    return "isSmsReceiveDisabled";
                case 145:
                    return "isSmsSendDisabled";
                case 146:
                    return "isMmsSendReceiveDisabled";
                case 147:
                    return "setSleepStandbyOptimizationDisabled";
                case 148:
                    return "isSleepStandbyOptimizationDisabled";
                case 149:
                    return "setUsbDebugSwitchDisabled";
                case 150:
                    return "isUsbDebugSwitchDisabled";
                case 151:
                    return "setAdbDisabled";
                case 152:
                    return "isAdbDisabled";
                case 153:
                    return "setNavigationBarDisabled";
                case 154:
                    return "isNavigationBarDisabled";
                case 155:
                    return "setLongPressLauncherDisabled";
                case 156:
                    return "isLongPressLauncherDisabled";
                case 157:
                    return "setSystemUpdatePolicies";
                case 158:
                    return "getSystemUpdatePolicies";
                case 159:
                    return "setDataSyncDisabled";
                case 160:
                    return "isDataSyncDisabled";
                case 161:
                    return "setUnknownSourceAppInstallDisabled";
                case TRANSACTION_isUnknownSourceAppInstallDisabled /* 162 */:
                    return "isUnknownSourceAppInstallDisabled";
                case 163:
                    return "setSideBarPolicies";
                case 164:
                    return "getSideBarPolicies";
                case 165:
                    return "setChangePictorialDisable";
                case 166:
                    return "isChangePictorialDisabled";
                case 167:
                    return "isAppInCustomVoipRecordList";
                case 168:
                    return "setPasswordExpirationTimeout";
                case 169:
                    return "getPasswordExpirationTimeout";
                case 170:
                    return "setSwipeUpUnlockDisabled";
                case 171:
                    return "isSwipeUpUnlockDisabled";
                case 172:
                    return "isAndroidAnimationDisabled";
                case 173:
                    return "setAndroidAnimationDisabled";
                case 174:
                    return "addDisallowedClearDataCacheApps";
                case 175:
                    return "removeDisallowedClearDataCacheApps";
                case 176:
                    return "getDisallowedClearDataCacheApps";
                case 177:
                    return "setAppLockDisabled";
                case 178:
                    return "isAppLockDisabled";
                case 179:
                    return "setFindMyPhoneDisabled";
                case 180:
                    return "isFindMyPhoneDisabled";
                case 181:
                    return "isPrivateSafeDisabled";
                case 182:
                    return "setPrivateSafeDisabled";
                case 183:
                    return "setCustomizeDozeModeDisabled";
                case 184:
                    return "isCustomizeDozeModeDisabled";
                case 185:
                    return "setBluetoothRandomEnabled";
                case 186:
                    return "isBluetoothRandomEnabled";
                case 187:
                    return "getLocalBluetoothAddress";
                case 188:
                    return "getLocalBtRandomAddress";
                case 189:
                    return "setWlanAllowListWithoutScanLimit";
                case 190:
                    return "getWlanAllowListWithoutScanLimit";
                case 191:
                    return "setWifiAssistantPolicies";
                case 192:
                    return "getWifiAssistantPolicies";
                case 193:
                    return "setWifiRandomMacForceDisable";
                case 194:
                    return "isWifiRandomMacForceDisable";
                case 195:
                    return "getWifiSarPwrDbm";
                case 196:
                    return "getWifiSarPwrMw";
                case 197:
                    return "setWifiSarPwrDbm";
                case 198:
                    return "setWifiSarPwrMw";
                case 199:
                    return "disableWifiSar";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setClipboardEnabled(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            boolean _result = getClipboardStatus();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 3:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            setAppInstallRestrictionPolicies(_arg02);
                            reply.writeNoException();
                            return true;
                        case 4:
                            int _result2 = getAppInstallRestrictionPolicies();
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 5:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result3 = getAppInstallPackageList(_arg03);
                            reply.writeNoException();
                            reply.writeStringList(_result3);
                            return true;
                        case 6:
                            int _arg04 = data.readInt();
                            List<String> _arg1 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addAppInstallPackageBlacklist(_arg04, _arg1);
                            reply.writeNoException();
                            return true;
                        case 7:
                            int _arg05 = data.readInt();
                            List<String> _arg12 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            addAppInstallPackageWhitelist(_arg05, _arg12);
                            reply.writeNoException();
                            return true;
                        case 8:
                            boolean _arg06 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setUSBDataDisabled(_arg06);
                            reply.writeNoException();
                            return true;
                        case 9:
                            boolean _result4 = isUSBDataDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 10:
                            boolean _arg07 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setUSBFileTransferDisabled(_arg07);
                            reply.writeNoException();
                            return true;
                        case 11:
                            boolean _result5 = isUSBFileTransferDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 12:
                            boolean _arg08 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setUSBOtgDisabled(_arg08);
                            reply.writeNoException();
                            return true;
                        case 13:
                            boolean _result6 = isUSBOtgDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 14:
                            boolean _arg09 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setBiometricDisabled(_arg09);
                            reply.writeNoException();
                            return true;
                        case 15:
                            boolean _result7 = isBiometricDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 16:
                            boolean _arg010 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSafeModeDisabled(_arg010);
                            reply.writeNoException();
                            return true;
                        case 17:
                            boolean _result8 = isSafeModeDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        case 18:
                            boolean _arg011 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setExternalStorageDisabled(_arg011);
                            reply.writeNoException();
                            return true;
                        case 19:
                            boolean _result9 = isExternalStorageDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 20:
                            boolean _arg012 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setUsbTetheringDisable(_arg012);
                            reply.writeNoException();
                            return true;
                        case 21:
                            boolean _result10 = isUsbTetheringDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 22:
                            ComponentName _arg013 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            long _arg13 = data.readLong();
                            data.enforceNoDataAvail();
                            setRequiredStrongAuthTime(_arg013, _arg13);
                            reply.writeNoException();
                            return true;
                        case 23:
                            ComponentName _arg014 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            long _result11 = getRequiredStrongAuthTime(_arg014);
                            reply.writeNoException();
                            reply.writeLong(_result11);
                            return true;
                        case 24:
                            boolean _arg015 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result12 = setScreenCaptureDisabled(_arg015);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 25:
                            boolean _result13 = getForbidRecordScreenState();
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 26:
                            int _arg016 = data.readInt();
                            List<String> _arg14 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            setAppUninstallationPolicies(_arg016, _arg14);
                            reply.writeNoException();
                            return true;
                        case 27:
                            int _arg017 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result14 = getAppUninstallationPackageList(_arg017);
                            reply.writeNoException();
                            reply.writeStringList(_result14);
                            return true;
                        case 28:
                            int _result15 = getAppUninstallationPolicies();
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        case 29:
                            ComponentName _arg018 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            String _arg15 = data.readString();
                            data.enforceNoDataAvail();
                            setSlot1DataConnectivityDisabled(_arg018, _arg15);
                            reply.writeNoException();
                            return true;
                        case 30:
                            ComponentName _arg019 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            String _arg16 = data.readString();
                            data.enforceNoDataAvail();
                            setSlot2DataConnectivityDisabled(_arg019, _arg16);
                            reply.writeNoException();
                            return true;
                        case 31:
                            ComponentName _arg020 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg17 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setVoiceOutgoingDisable(_arg020, _arg17);
                            reply.writeNoException();
                            return true;
                        case 32:
                            ComponentName _arg021 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg18 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setVoiceIncomingDisable(_arg021, _arg18);
                            reply.writeNoException();
                            return true;
                        case 33:
                            ComponentName _arg022 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result16 = isVoiceDisabled(_arg022);
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 34:
                            ComponentName _arg023 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg19 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setVoiceDisabled(_arg023, _arg19);
                            reply.writeNoException();
                            return true;
                        case 35:
                            ComponentName _arg024 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg110 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result17 = isVoiceIncomingDisabled(_arg024, _arg110);
                            reply.writeNoException();
                            reply.writeBoolean(_result17);
                            return true;
                        case 36:
                            ComponentName _arg025 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg111 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result18 = isVoiceOutgoingDisabled(_arg025, _arg111);
                            reply.writeNoException();
                            reply.writeBoolean(_result18);
                            return true;
                        case 37:
                            boolean _arg026 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setPowerSavingModeDisabled(_arg026);
                            reply.writeNoException();
                            return true;
                        case 38:
                            boolean _result19 = isPowerSavingModeDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result19);
                            return true;
                        case 39:
                            boolean _arg027 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result20 = setSuperPowerSavingModeDisabled(_arg027);
                            reply.writeNoException();
                            reply.writeBoolean(_result20);
                            return true;
                        case 40:
                            boolean _result21 = isSuperPowerSavingModeDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result21);
                            return true;
                        case 41:
                            boolean _arg028 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result22 = setDataRoamingDisabled(_arg028);
                            reply.writeNoException();
                            reply.writeBoolean(_result22);
                            return true;
                        case 42:
                            boolean _result23 = isDataRoamingDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result23);
                            return true;
                        case 43:
                            String _arg029 = data.readString();
                            int _arg112 = data.readInt();
                            data.enforceNoDataAvail();
                            applyQSRestriction(_arg029, _arg112);
                            reply.writeNoException();
                            return true;
                        case 44:
                            String _arg030 = data.readString();
                            int _arg113 = data.readInt();
                            data.enforceNoDataAvail();
                            disableQSRestriction(_arg030, _arg113);
                            reply.writeNoException();
                            return true;
                        case 45:
                            String _arg031 = data.readString();
                            data.enforceNoDataAvail();
                            int _result24 = getQSRestrictionValue(_arg031);
                            reply.writeNoException();
                            reply.writeInt(_result24);
                            return true;
                        case 46:
                            String _arg032 = data.readString();
                            int _arg114 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result25 = getQSRestrictionState(_arg032, _arg114);
                            reply.writeNoException();
                            reply.writeBoolean(_result25);
                            return true;
                        case 47:
                            ComponentName _arg033 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg115 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result26 = setSleepByPowerButtonDisabled(_arg033, _arg115);
                            reply.writeNoException();
                            reply.writeBoolean(_result26);
                            return true;
                        case 48:
                            ComponentName _arg034 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result27 = isSleepByPowerButtonDisabled(_arg034);
                            reply.writeNoException();
                            reply.writeBoolean(_result27);
                            return true;
                        case 49:
                            boolean _arg035 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result28 = setFileSharedDisabled(_arg035);
                            reply.writeNoException();
                            reply.writeBoolean(_result28);
                            return true;
                        case 50:
                            boolean _result29 = getFileSharedDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result29);
                            return true;
                        case 51:
                            int _arg036 = data.readInt();
                            boolean _arg116 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result30 = setNavigationMode(_arg036, _arg116);
                            reply.writeNoException();
                            reply.writeBoolean(_result30);
                            return true;
                        case 52:
                            boolean _result31 = isNavigationModeRevertible();
                            reply.writeNoException();
                            reply.writeBoolean(_result31);
                            return true;
                        case 53:
                            boolean _arg037 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setBluetoothDisabled(_arg037);
                            reply.writeNoException();
                            return true;
                        case 54:
                            boolean _result32 = isBluetoothDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result32);
                            return true;
                        case 55:
                            boolean _arg038 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setBluetoothEnabled(_arg038);
                            reply.writeNoException();
                            return true;
                        case 56:
                            boolean _result33 = isBluetoothEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result33);
                            return true;
                        case 57:
                            boolean _arg039 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result34 = setBluetoothConnectableDisabled(_arg039);
                            reply.writeNoException();
                            reply.writeBoolean(_result34);
                            return true;
                        case 58:
                            boolean _result35 = isBluetoothConnectableDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result35);
                            return true;
                        case 59:
                            boolean _arg040 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result36 = setDiscoverableDisabled(_arg040);
                            reply.writeNoException();
                            reply.writeBoolean(_result36);
                            return true;
                        case 60:
                            boolean _result37 = isDiscoverableDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result37);
                            return true;
                        case 61:
                            boolean _arg041 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result38 = setLimitedDiscoverableDisable(_arg041);
                            reply.writeNoException();
                            reply.writeBoolean(_result38);
                            return true;
                        case 62:
                            boolean _result39 = isLimitedDiscoverableDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result39);
                            return true;
                        case 63:
                            boolean _arg042 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result40 = setBluetoothPairingDisable(_arg042);
                            reply.writeNoException();
                            reply.writeBoolean(_result40);
                            return true;
                        case 64:
                            boolean _result41 = isBluetoothPairingDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result41);
                            return true;
                        case 65:
                            boolean _arg043 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result42 = setBluetoothOutGoingCallDisable(_arg043);
                            reply.writeNoException();
                            reply.writeBoolean(_result42);
                            return true;
                        case 66:
                            boolean _result43 = isBluetoothOutGoingCallDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result43);
                            return true;
                        case 67:
                            boolean _arg044 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result44 = setBluetoothDataTransferDisable(_arg044);
                            reply.writeNoException();
                            reply.writeBoolean(_result44);
                            return true;
                        case 68:
                            boolean _result45 = isBluetoothDataTransferDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result45);
                            return true;
                        case 69:
                            boolean _arg045 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result46 = setBluetoothTetheringDisable(_arg045);
                            reply.writeNoException();
                            reply.writeBoolean(_result46);
                            return true;
                        case 70:
                            boolean _result47 = isBluetoothTetheringDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result47);
                            return true;
                        case 71:
                            boolean _arg046 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result48 = setLocationBluetoothScanningDisabled(_arg046);
                            reply.writeNoException();
                            reply.writeBoolean(_result48);
                            return true;
                        case 72:
                            boolean _result49 = isLocationBluetoothScanningDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result49);
                            return true;
                        case 73:
                            List<String> _arg047 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result50 = setBluetoothDisabledProfiles(_arg047);
                            reply.writeNoException();
                            reply.writeBoolean(_result50);
                            return true;
                        case 74:
                            List<String> _result51 = getBluetoothDisabledProfiles();
                            reply.writeNoException();
                            reply.writeStringList(_result51);
                            return true;
                        case 75:
                            ComponentName _arg048 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg117 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setNFCDisabled(_arg048, _arg117);
                            reply.writeNoException();
                            return true;
                        case 76:
                            ComponentName _arg049 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result52 = isNFCDisabled(_arg049);
                            reply.writeNoException();
                            reply.writeBoolean(_result52);
                            return true;
                        case 77:
                            ComponentName _arg050 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg118 = data.readBoolean();
                            data.enforceNoDataAvail();
                            openCloseNFC(_arg050, _arg118);
                            reply.writeNoException();
                            return true;
                        case 78:
                            ComponentName _arg051 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result53 = isNFCTurnOn(_arg051);
                            reply.writeNoException();
                            reply.writeBoolean(_result53);
                            return true;
                        case 79:
                            ComponentName _arg052 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg119 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result54 = setAndroidBeamDisabled(_arg052, _arg119);
                            reply.writeNoException();
                            reply.writeBoolean(_result54);
                            return true;
                        case 80:
                            ComponentName _arg053 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result55 = isAndroidBeamDisabled(_arg053);
                            reply.writeNoException();
                            reply.writeBoolean(_result55);
                            return true;
                        case 81:
                            ComponentName _arg054 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg120 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result56 = setNfcPolicies(_arg054, _arg120);
                            reply.writeNoException();
                            reply.writeBoolean(_result56);
                            return true;
                        case 82:
                            ComponentName _arg055 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result57 = getNfcPolicies(_arg055);
                            reply.writeNoException();
                            reply.writeInt(_result57);
                            return true;
                        case 83:
                            ComponentName _arg056 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result58 = getMobileDataMode(_arg056);
                            reply.writeNoException();
                            reply.writeInt(_result58);
                            return true;
                        case 84:
                            ComponentName _arg057 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg121 = data.readInt();
                            data.enforceNoDataAvail();
                            setMobileDataMode(_arg057, _arg121);
                            reply.writeNoException();
                            return true;
                        case 85:
                            ComponentName _arg058 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            String _arg122 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result59 = allowWifiCellularNetwork(_arg058, _arg122);
                            reply.writeNoException();
                            reply.writeBoolean(_result59);
                            return true;
                        case 86:
                            int _arg059 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result60 = setCameraPolicies(_arg059);
                            reply.writeNoException();
                            reply.writeBoolean(_result60);
                            return true;
                        case 87:
                            int _result61 = getCameraPolicies();
                            reply.writeNoException();
                            reply.writeInt(_result61);
                            return true;
                        case 88:
                            int _arg060 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result62 = setTorchPolicies(_arg060);
                            reply.writeNoException();
                            reply.writeBoolean(_result62);
                            return true;
                        case 89:
                            int _result63 = getTorchPolicies();
                            reply.writeNoException();
                            reply.writeInt(_result63);
                            return true;
                        case 90:
                            ComponentName _arg061 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg123 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setChangeWallpaperDisable(_arg061, _arg123);
                            reply.writeNoException();
                            return true;
                        case 91:
                            ComponentName _arg062 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result64 = isChangeWallpaperDisabled(_arg062);
                            reply.writeNoException();
                            reply.writeBoolean(_result64);
                            return true;
                        case 92:
                            ComponentName _arg063 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg124 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSettingsApplicationDisabled(_arg063, _arg124);
                            reply.writeNoException();
                            return true;
                        case 93:
                            ComponentName _arg064 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result65 = isSettingsApplicationDisabled(_arg064);
                            reply.writeNoException();
                            reply.writeBoolean(_result65);
                            return true;
                        case 94:
                            List<String> _arg065 = data.createStringArrayList();
                            int _arg125 = data.readInt();
                            data.enforceNoDataAvail();
                            setApplicationDisabledInLauncherOrRecentTask(_arg065, _arg125);
                            reply.writeNoException();
                            return true;
                        case 95:
                            int _arg066 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result66 = getApplicationDisabledInLauncherOrRecentTask(_arg066);
                            reply.writeNoException();
                            reply.writeStringList(_result66);
                            return true;
                        case 96:
                            ComponentName _arg067 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg126 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result67 = setAirplanePolices(_arg067, _arg126);
                            reply.writeNoException();
                            reply.writeBoolean(_result67);
                            return true;
                        case 97:
                            ComponentName _arg068 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result68 = getAirplanePolices(_arg068);
                            reply.writeNoException();
                            reply.writeInt(_result68);
                            return true;
                        case 98:
                            ComponentName _arg069 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg127 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result69 = setFloatTaskDisabled(_arg069, _arg127);
                            reply.writeNoException();
                            reply.writeBoolean(_result69);
                            return true;
                        case 99:
                            ComponentName _arg070 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result70 = isFloatTaskDisabled(_arg070);
                            reply.writeNoException();
                            reply.writeBoolean(_result70);
                            return true;
                        case 100:
                            boolean _result71 = isMultiAppSupport();
                            reply.writeNoException();
                            reply.writeBoolean(_result71);
                            return true;
                        case 101:
                            boolean _arg071 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setMultiAppSupport(_arg071);
                            reply.writeNoException();
                            return true;
                        case 102:
                            ComponentName _arg072 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg128 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result72 = setSplitScreenDisable(_arg072, _arg128);
                            reply.writeNoException();
                            reply.writeBoolean(_result72);
                            return true;
                        case 103:
                            ComponentName _arg073 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result73 = getSplitScreenDisable(_arg073);
                            reply.writeNoException();
                            reply.writeBoolean(_result73);
                            return true;
                        case 104:
                            ComponentName _arg074 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg129 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result74 = setUserPasswordPolicies(_arg074, _arg129);
                            reply.writeNoException();
                            reply.writeBoolean(_result74);
                            return true;
                        case 105:
                            ComponentName _arg075 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result75 = getUserPasswordPolicies(_arg075);
                            reply.writeNoException();
                            reply.writeInt(_result75);
                            return true;
                        case 106:
                            ComponentName _arg076 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg130 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result76 = setUnlockByFingerprintPolicies(_arg076, _arg130);
                            reply.writeNoException();
                            reply.writeBoolean(_result76);
                            return true;
                        case 107:
                            ComponentName _arg077 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result77 = getUnlockByFingerprintPolicies(_arg077);
                            reply.writeNoException();
                            reply.writeInt(_result77);
                            return true;
                        case 108:
                            ComponentName _arg078 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg131 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result78 = setUnlockByFacePolicies(_arg078, _arg131);
                            reply.writeNoException();
                            reply.writeBoolean(_result78);
                            return true;
                        case 109:
                            ComponentName _arg079 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result79 = getUnlockByFacePolicies(_arg079);
                            reply.writeNoException();
                            reply.writeInt(_result79);
                            return true;
                        case 110:
                            ComponentName _arg080 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg132 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result80 = setUnlockByFingerprintDisabled(_arg080, _arg132);
                            reply.writeNoException();
                            reply.writeBoolean(_result80);
                            return true;
                        case 111:
                            ComponentName _arg081 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result81 = isUnlockByFingerprintDisabled(_arg081);
                            reply.writeNoException();
                            reply.writeBoolean(_result81);
                            return true;
                        case 112:
                            ComponentName _arg082 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg133 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result82 = setUnlockByFaceDisabled(_arg082, _arg133);
                            reply.writeNoException();
                            reply.writeBoolean(_result82);
                            return true;
                        case 113:
                            ComponentName _arg083 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result83 = isUnlockByFaceDisabled(_arg083);
                            reply.writeNoException();
                            reply.writeBoolean(_result83);
                            return true;
                        case 114:
                            int _arg084 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result84 = setPasswordRepeatMaxLength(_arg084);
                            reply.writeNoException();
                            reply.writeBoolean(_result84);
                            return true;
                        case 115:
                            int _result85 = getPasswordRepeatMaxLength();
                            reply.writeNoException();
                            reply.writeInt(_result85);
                            return true;
                        case 116:
                            int _arg085 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result86 = setPasswordNumSequenceMaxLength(_arg085);
                            reply.writeNoException();
                            reply.writeBoolean(_result86);
                            return true;
                        case 117:
                            int _result87 = getPasswordNumSequenceMaxLength();
                            reply.writeNoException();
                            reply.writeInt(_result87);
                            return true;
                        case 118:
                            boolean _arg086 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setTaskButtonDisabled(_arg086);
                            reply.writeNoException();
                            return true;
                        case 119:
                            boolean _result88 = isTaskButtonDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result88);
                            return true;
                        case 120:
                            boolean _arg087 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setHomeButtonDisabled(_arg087);
                            reply.writeNoException();
                            return true;
                        case 121:
                            boolean _result89 = isHomeButtonDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result89);
                            return true;
                        case 122:
                            boolean _arg088 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setBackButtonDisabled(_arg088);
                            reply.writeNoException();
                            return true;
                        case 123:
                            boolean _result90 = isBackButtonDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result90);
                            return true;
                        case 124:
                            boolean _arg089 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setPowerDisable(_arg089);
                            reply.writeNoException();
                            return true;
                        case 125:
                            boolean _result91 = getPowerDisable();
                            reply.writeNoException();
                            reply.writeBoolean(_result91);
                            return true;
                        case 126:
                            boolean _arg090 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setLongPressVolumeUpDisabled(_arg090);
                            reply.writeNoException();
                            return true;
                        case 127:
                            boolean _result92 = isLongPressVolumeUpDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result92);
                            return true;
                        case 128:
                            ComponentName _arg091 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg134 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result93 = setGpsPolicies(_arg091, _arg134);
                            reply.writeNoException();
                            reply.writeBoolean(_result93);
                            return true;
                        case 129:
                            ComponentName _arg092 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result94 = getGpsPolicies(_arg092);
                            reply.writeNoException();
                            reply.writeInt(_result94);
                            return true;
                        case 130:
                            ComponentName _arg093 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result95 = getDefaultDataCard(_arg093);
                            reply.writeNoException();
                            reply.writeInt(_result95);
                            return true;
                        case 131:
                            ComponentName _arg094 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg135 = data.readInt();
                            data.enforceNoDataAvail();
                            Bundle _result96 = setDefaultDataCard(_arg094, _arg135);
                            reply.writeNoException();
                            reply.writeTypedObject(_result96, 1);
                            return true;
                        case 132:
                            ComponentName _arg095 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result97 = getSlot1DataConnectivityDisabled(_arg095);
                            reply.writeNoException();
                            reply.writeInt(_result97);
                            return true;
                        case 133:
                            ComponentName _arg096 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result98 = getSlot2DataConnectivityDisabled(_arg096);
                            reply.writeNoException();
                            reply.writeInt(_result98);
                            return true;
                        case 134:
                            ComponentName _arg097 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg136 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setLanguageChangeDisabled(_arg097, _arg136);
                            reply.writeNoException();
                            return true;
                        case 135:
                            ComponentName _arg098 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result99 = isLanguageChangeDisabled(_arg098);
                            reply.writeNoException();
                            reply.writeBoolean(_result99);
                            return true;
                        case 136:
                            ComponentName _arg099 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result100 = isWifiOpen(_arg099);
                            reply.writeNoException();
                            reply.writeBoolean(_result100);
                            return true;
                        case 137:
                            ComponentName _arg0100 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg137 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result101 = setWifiInBackground(_arg0100, _arg137);
                            reply.writeNoException();
                            reply.writeBoolean(_result101);
                            return true;
                        case 138:
                            ComponentName _arg0101 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result102 = isWifiDisabled(_arg0101);
                            reply.writeNoException();
                            reply.writeBoolean(_result102);
                            return true;
                        case 139:
                            ComponentName _arg0102 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg138 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setWifiDisabled(_arg0102, _arg138);
                            reply.writeNoException();
                            return true;
                        case 140:
                            boolean _arg0103 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result103 = setEchoPasswordDisabled(_arg0103);
                            reply.writeNoException();
                            reply.writeBoolean(_result103);
                            return true;
                        case 141:
                            boolean _result104 = isEchoPasswordDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result104);
                            return true;
                        case 142:
                            boolean _arg0104 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setMmsDisabled(_arg0104);
                            reply.writeNoException();
                            return true;
                        case 143:
                            boolean _result105 = isMmsDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result105);
                            return true;
                        case 144:
                            boolean _result106 = isSmsReceiveDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result106);
                            return true;
                        case 145:
                            boolean _result107 = isSmsSendDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result107);
                            return true;
                        case 146:
                            boolean _result108 = isMmsSendReceiveDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result108);
                            return true;
                        case 147:
                            boolean _arg0105 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result109 = setSleepStandbyOptimizationDisabled(_arg0105);
                            reply.writeNoException();
                            reply.writeBoolean(_result109);
                            return true;
                        case 148:
                            boolean _result110 = isSleepStandbyOptimizationDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result110);
                            return true;
                        case 149:
                            ComponentName _arg0106 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg139 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setUsbDebugSwitchDisabled(_arg0106, _arg139);
                            reply.writeNoException();
                            return true;
                        case 150:
                            ComponentName _arg0107 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result111 = isUsbDebugSwitchDisabled(_arg0107);
                            reply.writeNoException();
                            reply.writeBoolean(_result111);
                            return true;
                        case 151:
                            ComponentName _arg0108 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg140 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAdbDisabled(_arg0108, _arg140);
                            reply.writeNoException();
                            return true;
                        case 152:
                            ComponentName _arg0109 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result112 = isAdbDisabled(_arg0109);
                            reply.writeNoException();
                            reply.writeBoolean(_result112);
                            return true;
                        case 153:
                            boolean _arg0110 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setNavigationBarDisabled(_arg0110);
                            reply.writeNoException();
                            return true;
                        case 154:
                            boolean _result113 = isNavigationBarDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result113);
                            return true;
                        case 155:
                            boolean _arg0111 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setLongPressLauncherDisabled(_arg0111);
                            reply.writeNoException();
                            return true;
                        case 156:
                            boolean _result114 = isLongPressLauncherDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result114);
                            return true;
                        case 157:
                            ComponentName _arg0112 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg141 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result115 = setSystemUpdatePolicies(_arg0112, _arg141);
                            reply.writeNoException();
                            reply.writeBoolean(_result115);
                            return true;
                        case 158:
                            ComponentName _arg0113 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result116 = getSystemUpdatePolicies(_arg0113);
                            reply.writeNoException();
                            reply.writeInt(_result116);
                            return true;
                        case 159:
                            boolean _arg0114 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result117 = setDataSyncDisabled(_arg0114);
                            reply.writeNoException();
                            reply.writeBoolean(_result117);
                            return true;
                        case 160:
                            boolean _result118 = isDataSyncDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result118);
                            return true;
                        case 161:
                            ComponentName _arg0115 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg142 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result119 = setUnknownSourceAppInstallDisabled(_arg0115, _arg142);
                            reply.writeNoException();
                            reply.writeBoolean(_result119);
                            return true;
                        case TRANSACTION_isUnknownSourceAppInstallDisabled /* 162 */:
                            ComponentName _arg0116 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result120 = isUnknownSourceAppInstallDisabled(_arg0116);
                            reply.writeNoException();
                            reply.writeBoolean(_result120);
                            return true;
                        case 163:
                            ComponentName _arg0117 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg143 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result121 = setSideBarPolicies(_arg0117, _arg143);
                            reply.writeNoException();
                            reply.writeBoolean(_result121);
                            return true;
                        case 164:
                            ComponentName _arg0118 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result122 = getSideBarPolicies(_arg0118);
                            reply.writeNoException();
                            reply.writeInt(_result122);
                            return true;
                        case 165:
                            ComponentName _arg0119 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg144 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setChangePictorialDisable(_arg0119, _arg144);
                            reply.writeNoException();
                            return true;
                        case 166:
                            ComponentName _arg0120 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result123 = isChangePictorialDisabled(_arg0120);
                            reply.writeNoException();
                            reply.writeBoolean(_result123);
                            return true;
                        case 167:
                            String _arg0121 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result124 = isAppInCustomVoipRecordList(_arg0121);
                            reply.writeNoException();
                            reply.writeBoolean(_result124);
                            return true;
                        case 168:
                            ComponentName _arg0122 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            long _arg145 = data.readLong();
                            data.enforceNoDataAvail();
                            setPasswordExpirationTimeout(_arg0122, _arg145);
                            reply.writeNoException();
                            return true;
                        case 169:
                            ComponentName _arg0123 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            long _result125 = getPasswordExpirationTimeout(_arg0123);
                            reply.writeNoException();
                            reply.writeLong(_result125);
                            return true;
                        case 170:
                            ComponentName _arg0124 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg146 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result126 = setSwipeUpUnlockDisabled(_arg0124, _arg146);
                            reply.writeNoException();
                            reply.writeBoolean(_result126);
                            return true;
                        case 171:
                            boolean _result127 = isSwipeUpUnlockDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result127);
                            return true;
                        case 172:
                            boolean _result128 = isAndroidAnimationDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result128);
                            return true;
                        case 173:
                            boolean _arg0125 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result129 = setAndroidAnimationDisabled(_arg0125);
                            reply.writeNoException();
                            reply.writeBoolean(_result129);
                            return true;
                        case 174:
                            List<String> _arg0126 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result130 = addDisallowedClearDataCacheApps(_arg0126);
                            reply.writeNoException();
                            reply.writeBoolean(_result130);
                            return true;
                        case 175:
                            List<String> _arg0127 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result131 = removeDisallowedClearDataCacheApps(_arg0127);
                            reply.writeNoException();
                            reply.writeBoolean(_result131);
                            return true;
                        case 176:
                            List<String> _result132 = getDisallowedClearDataCacheApps();
                            reply.writeNoException();
                            reply.writeStringList(_result132);
                            return true;
                        case 177:
                            boolean _arg0128 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAppLockDisabled(_arg0128);
                            reply.writeNoException();
                            return true;
                        case 178:
                            boolean _result133 = isAppLockDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result133);
                            return true;
                        case 179:
                            boolean _arg0129 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result134 = setFindMyPhoneDisabled(_arg0129);
                            reply.writeNoException();
                            reply.writeBoolean(_result134);
                            return true;
                        case 180:
                            boolean _result135 = isFindMyPhoneDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result135);
                            return true;
                        case 181:
                            boolean _result136 = isPrivateSafeDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result136);
                            return true;
                        case 182:
                            boolean _arg0130 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result137 = setPrivateSafeDisabled(_arg0130);
                            reply.writeNoException();
                            reply.writeBoolean(_result137);
                            return true;
                        case 183:
                            boolean _arg0131 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result138 = setCustomizeDozeModeDisabled(_arg0131);
                            reply.writeNoException();
                            reply.writeBoolean(_result138);
                            return true;
                        case 184:
                            boolean _result139 = isCustomizeDozeModeDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result139);
                            return true;
                        case 185:
                            boolean _arg0132 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result140 = setBluetoothRandomEnabled(_arg0132);
                            reply.writeNoException();
                            reply.writeBoolean(_result140);
                            return true;
                        case 186:
                            boolean _result141 = isBluetoothRandomEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result141);
                            return true;
                        case 187:
                            String _result142 = getLocalBluetoothAddress();
                            reply.writeNoException();
                            reply.writeString(_result142);
                            return true;
                        case 188:
                            String _result143 = getLocalBtRandomAddress();
                            reply.writeNoException();
                            reply.writeString(_result143);
                            return true;
                        case 189:
                            ComponentName _arg0133 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            List<String> _arg147 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result144 = setWlanAllowListWithoutScanLimit(_arg0133, _arg147);
                            reply.writeNoException();
                            reply.writeBoolean(_result144);
                            return true;
                        case 190:
                            ComponentName _arg0134 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            List<String> _result145 = getWlanAllowListWithoutScanLimit(_arg0134);
                            reply.writeNoException();
                            reply.writeStringList(_result145);
                            return true;
                        case 191:
                            ComponentName _arg0135 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg148 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result146 = setWifiAssistantPolicies(_arg0135, _arg148);
                            reply.writeNoException();
                            reply.writeBoolean(_result146);
                            return true;
                        case 192:
                            ComponentName _arg0136 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result147 = getWifiAssistantPolicies(_arg0136);
                            reply.writeNoException();
                            reply.writeInt(_result147);
                            return true;
                        case 193:
                            ComponentName _arg0137 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg149 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result148 = setWifiRandomMacForceDisable(_arg0137, _arg149);
                            reply.writeNoException();
                            reply.writeBoolean(_result148);
                            return true;
                        case 194:
                            ComponentName _arg0138 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result149 = isWifiRandomMacForceDisable(_arg0138);
                            reply.writeNoException();
                            reply.writeBoolean(_result149);
                            return true;
                        case 195:
                            float _result150 = getWifiSarPwrDbm();
                            reply.writeNoException();
                            reply.writeFloat(_result150);
                            return true;
                        case 196:
                            float _result151 = getWifiSarPwrMw();
                            reply.writeNoException();
                            reply.writeFloat(_result151);
                            return true;
                        case 197:
                            float _arg0139 = data.readFloat();
                            data.enforceNoDataAvail();
                            boolean _result152 = setWifiSarPwrDbm(_arg0139);
                            reply.writeNoException();
                            reply.writeBoolean(_result152);
                            return true;
                        case 198:
                            float _arg0140 = data.readFloat();
                            data.enforceNoDataAvail();
                            boolean _result153 = setWifiSarPwrMw(_arg0140);
                            reply.writeNoException();
                            reply.writeBoolean(_result153);
                            return true;
                        case 199:
                            boolean _result154 = disableWifiSar();
                            reply.writeNoException();
                            reply.writeBoolean(_result154);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeRestrictionManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeRestrictionManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setClipboardEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean getClipboardStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setAppInstallRestrictionPolicies(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getAppInstallRestrictionPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public List<String> getAppInstallPackageList(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void addAppInstallPackageBlacklist(int pattern, List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void addAppInstallPackageWhitelist(int pattern, List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(pattern);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setUSBDataDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isUSBDataDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setUSBFileTransferDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isUSBFileTransferDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setUSBOtgDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isUSBOtgDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setBiometricDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBiometricDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setSafeModeDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isSafeModeDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setExternalStorageDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isExternalStorageDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setUsbTetheringDisable(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isUsbTetheringDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setRequiredStrongAuthTime(ComponentName admin, long timeoutMs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeLong(timeoutMs);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public long getRequiredStrongAuthTime(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setScreenCaptureDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean getForbidRecordScreenState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setAppUninstallationPolicies(int mode, List<String> appPackageNames) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeStringList(appPackageNames);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public List<String> getAppUninstallationPackageList(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getAppUninstallationPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setSlot1DataConnectivityDisabled(ComponentName admin, String val) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeString(val);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setSlot2DataConnectivityDisabled(ComponentName admin, String val) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeString(val);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setVoiceOutgoingDisable(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setVoiceIncomingDisable(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isVoiceDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setVoiceDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isVoiceIncomingDisabled(ComponentName admin, int slot) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(slot);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isVoiceOutgoingDisabled(ComponentName admin, int slot) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(slot);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setPowerSavingModeDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isPowerSavingModeDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setSuperPowerSavingModeDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isSuperPowerSavingModeDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setDataRoamingDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isDataRoamingDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void applyQSRestriction(String key, int newValue) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(newValue);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void disableQSRestriction(String key, int newValue) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(newValue);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getQSRestrictionValue(String key) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeString(key);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean getQSRestrictionState(String key, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(state);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setSleepByPowerButtonDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isSleepByPowerButtonDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setFileSharedDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean getFileSharedDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setNavigationMode(int mode, boolean revertible) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeBoolean(revertible);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isNavigationModeRevertible() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setBluetoothDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBluetoothDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setBluetoothEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBluetoothEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setBluetoothConnectableDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBluetoothConnectableDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setDiscoverableDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isDiscoverableDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setLimitedDiscoverableDisable(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isLimitedDiscoverableDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setBluetoothPairingDisable(boolean isDisable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(isDisable);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBluetoothPairingDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setBluetoothOutGoingCallDisable(boolean isDisable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(isDisable);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBluetoothOutGoingCallDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setBluetoothDataTransferDisable(boolean isDisable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(isDisable);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBluetoothDataTransferDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setBluetoothTetheringDisable(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBluetoothTetheringDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setLocationBluetoothScanningDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isLocationBluetoothScanningDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setBluetoothDisabledProfiles(List<String> disabledProfiles) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeStringList(disabledProfiles);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public List<String> getBluetoothDisabledProfiles() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setNFCDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isNFCDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void openCloseNFC(ComponentName admin, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isNFCTurnOn(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setAndroidBeamDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isAndroidBeamDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setNfcPolicies(ComponentName admin, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getNfcPolicies(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getMobileDataMode(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setMobileDataMode(ComponentName admin, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean allowWifiCellularNetwork(ComponentName compName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(compName, 0);
                    _data.writeString(packageName);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setCameraPolicies(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getCameraPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setTorchPolicies(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getTorchPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setChangeWallpaperDisable(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isChangeWallpaperDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setSettingsApplicationDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isSettingsApplicationDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setApplicationDisabledInLauncherOrRecentTask(List<String> list, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeStringList(list);
                    _data.writeInt(flag);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public List<String> getApplicationDisabledInLauncherOrRecentTask(int flag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(flag);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setAirplanePolices(ComponentName admin, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(policy);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getAirplanePolices(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setFloatTaskDisabled(ComponentName componentName, boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isFloatTaskDisabled(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isMultiAppSupport() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setMultiAppSupport(boolean support) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(support);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setSplitScreenDisable(ComponentName admin, boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean getSplitScreenDisable(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setUserPasswordPolicies(ComponentName admin, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getUserPasswordPolicies(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setUnlockByFingerprintPolicies(ComponentName admin, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(policy);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getUnlockByFingerprintPolicies(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setUnlockByFacePolicies(ComponentName admin, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(policy);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getUnlockByFacePolicies(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setUnlockByFingerprintDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isUnlockByFingerprintDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setUnlockByFaceDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isUnlockByFaceDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setPasswordRepeatMaxLength(int length) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(length);
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getPasswordRepeatMaxLength() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setPasswordNumSequenceMaxLength(int length) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeInt(length);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getPasswordNumSequenceMaxLength() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setTaskButtonDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isTaskButtonDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setHomeButtonDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isHomeButtonDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setBackButtonDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(122, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBackButtonDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setPowerDisable(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean getPowerDisable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setLongPressVolumeUpDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isLongPressVolumeUpDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setGpsPolicies(ComponentName admin, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getGpsPolicies(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getDefaultDataCard(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public Bundle setDefaultDataCard(ComponentName admin, int slot) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(slot);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getSlot1DataConnectivityDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getSlot2DataConnectivityDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setLanguageChangeDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isLanguageChangeDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isWifiOpen(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setWifiInBackground(ComponentName admin, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isWifiDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setWifiDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setEchoPasswordDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isEchoPasswordDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setMmsDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isMmsDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isSmsReceiveDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isSmsSendDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isMmsSendReceiveDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setSleepStandbyOptimizationDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isSleepStandbyOptimizationDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setUsbDebugSwitchDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isUsbDebugSwitchDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setAdbDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isAdbDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setNavigationBarDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isNavigationBarDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(154, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setLongPressLauncherDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(155, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isLongPressLauncherDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(156, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setSystemUpdatePolicies(ComponentName admin, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(157, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getSystemUpdatePolicies(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(158, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setDataSyncDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(159, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isDataSyncDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(160, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setUnknownSourceAppInstallDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(161, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isUnknownSourceAppInstallDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(Stub.TRANSACTION_isUnknownSourceAppInstallDisabled, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setSideBarPolicies(ComponentName admin, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(163, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getSideBarPolicies(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(164, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setChangePictorialDisable(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(165, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isChangePictorialDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(166, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isAppInCustomVoipRecordList(String pakageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeString(pakageName);
                    this.mRemote.transact(167, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setPasswordExpirationTimeout(ComponentName admin, long timeoutMs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeLong(timeoutMs);
                    this.mRemote.transact(168, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public long getPasswordExpirationTimeout(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(169, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setSwipeUpUnlockDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(170, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isSwipeUpUnlockDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(171, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isAndroidAnimationDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(172, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setAndroidAnimationDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(173, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean addDisallowedClearDataCacheApps(List<String> pkgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeStringList(pkgs);
                    this.mRemote.transact(174, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean removeDisallowedClearDataCacheApps(List<String> pkgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeStringList(pkgs);
                    this.mRemote.transact(175, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public List<String> getDisallowedClearDataCacheApps() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(176, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public void setAppLockDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(177, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isAppLockDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(178, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setFindMyPhoneDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(179, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isFindMyPhoneDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(180, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isPrivateSafeDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(181, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setPrivateSafeDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(182, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setCustomizeDozeModeDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(183, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isCustomizeDozeModeDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(184, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setBluetoothRandomEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(185, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isBluetoothRandomEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(186, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public String getLocalBluetoothAddress() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(187, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public String getLocalBtRandomAddress() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(188, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setWlanAllowListWithoutScanLimit(ComponentName admin, List<String> allowList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeStringList(allowList);
                    this.mRemote.transact(189, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public List<String> getWlanAllowListWithoutScanLimit(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(190, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setWifiAssistantPolicies(ComponentName admin, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(mode);
                    this.mRemote.transact(191, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public int getWifiAssistantPolicies(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(192, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setWifiRandomMacForceDisable(ComponentName admin, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(193, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean isWifiRandomMacForceDisable(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(194, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public float getWifiSarPwrDbm() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(195, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public float getWifiSarPwrMw() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(196, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setWifiSarPwrDbm(float dbm) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeFloat(dbm);
                    this.mRemote.transact(197, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean setWifiSarPwrMw(float mw) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    _data.writeFloat(mw);
                    this.mRemote.transact(198, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeRestrictionManagerService
            public boolean disableWifiSar() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeRestrictionManagerService.DESCRIPTOR);
                    this.mRemote.transact(199, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 198;
        }
    }
}
