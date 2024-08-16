package com.oplus.wrapper.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ChangedPackages;
import android.content.pm.IDexModuleRegisterCallback;
import android.content.pm.IOnChecksumsReadyListener;
import android.content.pm.IPackageDeleteObserver2;
import android.content.pm.IPackageInstaller;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageMoveObserver;
import android.content.pm.InstallSourceInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.KeySet;
import android.content.pm.ModuleInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SuspendDialogInfo;
import android.content.pm.VerifierDeviceIdentity;
import android.content.pm.VersionedPackage;
import android.content.pm.dex.IArtManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface IPackageManager {
    android.content.pm.ActivityInfo getActivityInfo(ComponentName componentName, long j, int i) throws RemoteException;

    int getApplicationEnabledSetting(String str, int i) throws RemoteException;

    ComponentName getHomeActivities(List<android.content.pm.ResolveInfo> list) throws RemoteException;

    int installExistingPackageAsUser(String str, int i, int i2, int i3, List<String> list) throws RemoteException;

    boolean isStorageLow() throws RemoteException;

    android.content.pm.ResolveInfo resolveIntent(Intent intent, String str, long j, int i) throws RemoteException;

    void setApplicationEnabledSetting(String str, int i, int i2, int i3, String str2) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IPackageManager {
        private final android.content.pm.IPackageManager mTarget = new IPackageManager.Stub() { // from class: com.oplus.wrapper.content.pm.IPackageManager.Stub.1
            public void checkPackageStartable(String s, int i) throws RemoteException {
            }

            public boolean isPackageAvailable(String s, int i) throws RemoteException {
                return false;
            }

            public android.content.pm.PackageInfo getPackageInfo(String s, long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage, long l, int i) throws RemoteException {
                return null;
            }

            public int getPackageUid(String s, long l, int i) throws RemoteException {
                return 0;
            }

            public int[] getPackageGids(String s, long l, int i) throws RemoteException {
                return new int[0];
            }

            public String[] currentToCanonicalPackageNames(String[] strings) throws RemoteException {
                return new String[0];
            }

            public String[] canonicalToCurrentPackageNames(String[] strings) throws RemoteException {
                return new String[0];
            }

            public android.content.pm.ApplicationInfo getApplicationInfo(String s, long l, int i) throws RemoteException {
                return null;
            }

            public int getTargetSdkVersion(String s) throws RemoteException {
                return 0;
            }

            public android.content.pm.ActivityInfo getActivityInfo(ComponentName componentName, long l, int i) throws RemoteException {
                return Stub.this.getActivityInfo(componentName, l, i);
            }

            public boolean activitySupportsIntentAsUser(ComponentName componentName, Intent intent, String s, int userId) throws RemoteException {
                return false;
            }

            public android.content.pm.ActivityInfo getReceiverInfo(ComponentName componentName, long l, int i) throws RemoteException {
                return null;
            }

            public ServiceInfo getServiceInfo(ComponentName componentName, long l, int i) throws RemoteException {
                return null;
            }

            public ProviderInfo getProviderInfo(ComponentName componentName, long l, int i) throws RemoteException {
                return null;
            }

            public boolean isProtectedBroadcast(String s) throws RemoteException {
                return false;
            }

            public int checkSignatures(String s, String s1, int userId) throws RemoteException {
                return 0;
            }

            public int checkUidSignatures(int i, int i1) throws RemoteException {
                return 0;
            }

            public List<String> getAllPackages() throws RemoteException {
                return null;
            }

            public String[] getPackagesForUid(int i) throws RemoteException {
                return new String[0];
            }

            public String getNameForUid(int i) throws RemoteException {
                return null;
            }

            public String[] getNamesForUids(int[] ints) throws RemoteException {
                return new String[0];
            }

            public int getUidForSharedUser(String s) throws RemoteException {
                return 0;
            }

            public int getFlagsForUid(int i) throws RemoteException {
                return 0;
            }

            public int getPrivateFlagsForUid(int i) throws RemoteException {
                return 0;
            }

            public boolean isUidPrivileged(int i) throws RemoteException {
                return false;
            }

            public android.content.pm.ResolveInfo resolveIntent(Intent intent, String s, long l, int i) throws RemoteException {
                return Stub.this.resolveIntent(intent, s, l, i);
            }

            public android.content.pm.ResolveInfo findPersistentPreferredActivity(Intent intent, int i) throws RemoteException {
                return null;
            }

            public boolean canForwardTo(Intent intent, String s, int i, int i1) throws RemoteException {
                return false;
            }

            public android.content.pm.ParceledListSlice queryIntentActivities(Intent intent, String s, long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice queryIntentActivityOptions(ComponentName componentName, Intent[] intents, String[] strings, Intent intent, String s, long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice queryIntentReceivers(Intent intent, String s, long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ResolveInfo resolveService(Intent intent, String s, long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice queryIntentServices(Intent intent, String s, long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice queryIntentContentProviders(Intent intent, String s, long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getInstalledPackages(long l, int i) throws RemoteException {
                return null;
            }

            public ParcelFileDescriptor getAppMetadataFd(String packageName, int userId) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getPackagesHoldingPermissions(String[] strings, long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getInstalledApplications(long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getPersistentApplications(int i) throws RemoteException {
                return null;
            }

            public ProviderInfo resolveContentProvider(String s, long l, int i) throws RemoteException {
                return null;
            }

            public void querySyncProviders(List<String> list, List<ProviderInfo> list1) throws RemoteException {
            }

            public android.content.pm.ParceledListSlice queryContentProviders(String s, int i, long l, String s1) throws RemoteException {
                return null;
            }

            public InstrumentationInfo getInstrumentationInfoAsUser(ComponentName componentName, int i, int userId) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice queryInstrumentationAsUser(String s, int i, int userId) throws RemoteException {
                return null;
            }

            public void finishPackageInstall(int i, boolean b) throws RemoteException {
            }

            public void setInstallerPackageName(String s, String s1) throws RemoteException {
            }

            public void relinquishUpdateOwnership(String targetPackage) throws RemoteException {
            }

            public void setApplicationCategoryHint(String s, int i, String s1) throws RemoteException {
            }

            public void deletePackageAsUser(String s, int i, android.content.pm.IPackageDeleteObserver iPackageDeleteObserver, int i1, int i2) throws RemoteException {
            }

            public void deletePackageVersioned(VersionedPackage versionedPackage, IPackageDeleteObserver2 iPackageDeleteObserver2, int i, int i1) throws RemoteException {
            }

            public void deleteExistingPackageAsUser(VersionedPackage versionedPackage, IPackageDeleteObserver2 iPackageDeleteObserver2, int i) throws RemoteException {
            }

            public String getInstallerPackageName(String s) throws RemoteException {
                return null;
            }

            public InstallSourceInfo getInstallSourceInfo(String s, int userId) throws RemoteException {
                return null;
            }

            public void resetApplicationPreferences(int i) throws RemoteException {
            }

            public android.content.pm.ResolveInfo getLastChosenActivity(Intent intent, String s, int i) throws RemoteException {
                return null;
            }

            public void setLastChosenActivity(Intent intent, String s, int i, IntentFilter intentFilter, int i1, ComponentName componentName) throws RemoteException {
            }

            public void addPreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNames, ComponentName componentName, int i1, boolean b) throws RemoteException {
            }

            public void replacePreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNames, ComponentName componentName, int i1) throws RemoteException {
            }

            public void clearPackagePreferredActivities(String s) throws RemoteException {
            }

            public int getPreferredActivities(List<IntentFilter> list, List<ComponentName> list1, String s) throws RemoteException {
                return 0;
            }

            public void addPersistentPreferredActivity(IntentFilter intentFilter, ComponentName componentName, int i) throws RemoteException {
            }

            public void clearPackagePersistentPreferredActivities(String s, int i) throws RemoteException {
            }

            public void clearPersistentPreferredActivity(IntentFilter filter, int userId) throws RemoteException {
            }

            public void addCrossProfileIntentFilter(IntentFilter intentFilter, String s, int i, int i1, int i2) throws RemoteException {
            }

            public boolean removeCrossProfileIntentFilter(IntentFilter intentFilter, String ownerPackage, int sourceUserId, int targetUserId, int flags) throws RemoteException {
                return false;
            }

            public void clearCrossProfileIntentFilters(int i, String s) throws RemoteException {
            }

            public String[] setDistractingPackageRestrictionsAsUser(String[] strings, int i, int i1) throws RemoteException {
                return new String[0];
            }

            public String[] setPackagesSuspendedAsUser(String[] strings, boolean b, PersistableBundle persistableBundle, PersistableBundle persistableBundle1, SuspendDialogInfo suspendDialogInfo, String s, int i) throws RemoteException {
                return new String[0];
            }

            public String[] getUnsuspendablePackagesForUser(String[] strings, int i) throws RemoteException {
                return new String[0];
            }

            public boolean isPackageSuspendedForUser(String s, int i) throws RemoteException {
                return false;
            }

            public Bundle getSuspendedPackageAppExtras(String s, int i) throws RemoteException {
                return null;
            }

            public byte[] getPreferredActivityBackup(int i) throws RemoteException {
                return new byte[0];
            }

            public void restorePreferredActivities(byte[] bytes, int i) throws RemoteException {
            }

            public byte[] getDefaultAppsBackup(int i) throws RemoteException {
                return new byte[0];
            }

            public void restoreDefaultApps(byte[] bytes, int i) throws RemoteException {
            }

            public byte[] getDomainVerificationBackup(int i) throws RemoteException {
                return new byte[0];
            }

            public void restoreDomainVerification(byte[] bytes, int i) throws RemoteException {
            }

            public ComponentName getHomeActivities(List<android.content.pm.ResolveInfo> list) throws RemoteException {
                return Stub.this.getHomeActivities(list);
            }

            public void setHomeActivity(ComponentName componentName, int i) throws RemoteException {
            }

            public void overrideLabelAndIcon(ComponentName componentName, String s, int i, int i1) throws RemoteException {
            }

            public void restoreLabelAndIcon(ComponentName componentName, int i) throws RemoteException {
            }

            public void setComponentEnabledSetting(ComponentName componentName, int i, int i1, int i2, String s1) throws RemoteException {
            }

            public void setComponentEnabledSettings(List<PackageManager.ComponentEnabledSetting> list, int i, String s1) throws RemoteException {
            }

            public int getComponentEnabledSetting(ComponentName componentName, int i) throws RemoteException {
                return 0;
            }

            public void setApplicationEnabledSetting(String s, int i, int i1, int i2, String s1) throws RemoteException {
                Stub.this.setApplicationEnabledSetting(s, i, i1, i2, s1);
            }

            public int getApplicationEnabledSetting(String s, int i) throws RemoteException {
                return Stub.this.getApplicationEnabledSetting(s, i);
            }

            public void logAppProcessStartIfNeeded(String s, String s1, int i, String s2, String s3, int i1) throws RemoteException {
            }

            public void flushPackageRestrictionsAsUser(int i) throws RemoteException {
            }

            public void setPackageStoppedState(String s, boolean b, int i) throws RemoteException {
            }

            public void freeStorageAndNotify(String s, long l, int i, android.content.pm.IPackageDataObserver iPackageDataObserver) throws RemoteException {
            }

            public void freeStorage(String s, long l, int i, IntentSender intentSender) throws RemoteException {
            }

            public void deleteApplicationCacheFiles(String s, android.content.pm.IPackageDataObserver iPackageDataObserver) throws RemoteException {
            }

            public void deleteApplicationCacheFilesAsUser(String s, int i, android.content.pm.IPackageDataObserver iPackageDataObserver) throws RemoteException {
            }

            public void clearApplicationUserData(String s, android.content.pm.IPackageDataObserver iPackageDataObserver, int i) throws RemoteException {
            }

            public void clearApplicationProfileData(String s) throws RemoteException {
            }

            public void getPackageSizeInfo(String s, int i, android.content.pm.IPackageStatsObserver iPackageStatsObserver) throws RemoteException {
            }

            public String[] getSystemSharedLibraryNames() throws RemoteException {
                return new String[0];
            }

            public android.content.pm.ParceledListSlice getSystemAvailableFeatures() throws RemoteException {
                return null;
            }

            public boolean hasSystemFeature(String s, int i) throws RemoteException {
                return false;
            }

            public List<String> getInitialNonStoppedSystemPackages() throws RemoteException {
                return null;
            }

            public void enterSafeMode() throws RemoteException {
            }

            public boolean isSafeMode() throws RemoteException {
                return false;
            }

            public boolean hasSystemUidErrors() throws RemoteException {
                return false;
            }

            public void notifyPackageUse(String s, int i) throws RemoteException {
            }

            public void notifyDexLoad(String s, Map<String, String> map, String s1) throws RemoteException {
            }

            public void registerDexModule(String s, String s1, boolean b, IDexModuleRegisterCallback iDexModuleRegisterCallback) throws RemoteException {
            }

            public boolean performDexOptMode(String s, boolean b, String s1, boolean b1, boolean b2, String s2) throws RemoteException {
                return false;
            }

            public boolean performDexOptSecondary(String s, String s1, boolean b) throws RemoteException {
                return false;
            }

            public int getMoveStatus(int i) throws RemoteException {
                return 0;
            }

            public void registerMoveCallback(IPackageMoveObserver iPackageMoveObserver) throws RemoteException {
            }

            public void unregisterMoveCallback(IPackageMoveObserver iPackageMoveObserver) throws RemoteException {
            }

            public int movePackage(String s, String s1) throws RemoteException {
                return 0;
            }

            public int movePrimaryStorage(String s) throws RemoteException {
                return 0;
            }

            public boolean setInstallLocation(int i) throws RemoteException {
                return false;
            }

            public int getInstallLocation() throws RemoteException {
                return 0;
            }

            public int installExistingPackageAsUser(String packageName, int userId, int installFlags, int installReason, List<String> whiteListedPermissions) throws RemoteException {
                return Stub.this.installExistingPackageAsUser(packageName, userId, installFlags, installReason, whiteListedPermissions);
            }

            public void verifyPendingInstall(int i, int i1) throws RemoteException {
            }

            public void extendVerificationTimeout(int i, int i1, long l) throws RemoteException {
            }

            public void verifyIntentFilter(int i, int i1, List<String> list) throws RemoteException {
            }

            public int getIntentVerificationStatus(String s, int i) throws RemoteException {
                return 0;
            }

            public boolean updateIntentVerificationStatus(String s, int i, int i1) throws RemoteException {
                return false;
            }

            public android.content.pm.ParceledListSlice getIntentFilterVerifications(String s) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getAllIntentFilters(String s) throws RemoteException {
                return null;
            }

            public VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException {
                return null;
            }

            public boolean isFirstBoot() throws RemoteException {
                return false;
            }

            public boolean isDeviceUpgrading() throws RemoteException {
                return false;
            }

            public boolean isStorageLow() throws RemoteException {
                return Stub.this.isStorageLow();
            }

            public boolean setApplicationHiddenSettingAsUser(String s, boolean b, int i) throws RemoteException {
                return false;
            }

            public boolean getApplicationHiddenSettingAsUser(String s, int i) throws RemoteException {
                return false;
            }

            public void setSystemAppHiddenUntilInstalled(String s, boolean b) throws RemoteException {
            }

            public boolean setSystemAppInstallState(String s, boolean b, int i) throws RemoteException {
                return false;
            }

            public IPackageInstaller getPackageInstaller() throws RemoteException {
                return null;
            }

            public boolean setBlockUninstallForUser(String s, boolean b, int i) throws RemoteException {
                return false;
            }

            public boolean getBlockUninstallForUser(String s, int i) throws RemoteException {
                return false;
            }

            public KeySet getKeySetByAlias(String s, String s1) throws RemoteException {
                return null;
            }

            public KeySet getSigningKeySet(String s) throws RemoteException {
                return null;
            }

            public boolean isPackageSignedByKeySet(String s, KeySet keySet) throws RemoteException {
                return false;
            }

            public boolean isPackageSignedByKeySetExactly(String s, KeySet keySet) throws RemoteException {
                return false;
            }

            public String getPermissionControllerPackageName() throws RemoteException {
                return null;
            }

            public String getSdkSandboxPackageName() throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getInstantApps(int i) throws RemoteException {
                return null;
            }

            public byte[] getInstantAppCookie(String s, int i) throws RemoteException {
                return new byte[0];
            }

            public boolean setInstantAppCookie(String s, byte[] bytes, int i) throws RemoteException {
                return false;
            }

            public Bitmap getInstantAppIcon(String s, int i) throws RemoteException {
                return null;
            }

            public boolean isInstantApp(String s, int i) throws RemoteException {
                return false;
            }

            public boolean setRequiredForSystemUser(String s, boolean b) throws RemoteException {
                return false;
            }

            public void setUpdateAvailable(String s, boolean b) throws RemoteException {
            }

            public String getServicesSystemSharedLibraryPackageName() throws RemoteException {
                return null;
            }

            public String getSharedSystemSharedLibraryPackageName() throws RemoteException {
                return null;
            }

            public ChangedPackages getChangedPackages(int i, int i1) throws RemoteException {
                return null;
            }

            public boolean isPackageDeviceAdminOnAnyUser(String s) throws RemoteException {
                return false;
            }

            public int getInstallReason(String s, int i) throws RemoteException {
                return 0;
            }

            public android.content.pm.ParceledListSlice getSharedLibraries(String s, long l, int i) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice getDeclaredSharedLibraries(String s, long l, int i) throws RemoteException {
                return null;
            }

            public boolean canRequestPackageInstalls(String s, int i) throws RemoteException {
                return false;
            }

            public void deletePreloadsFileCache() throws RemoteException {
            }

            public ComponentName getInstantAppResolverComponent() throws RemoteException {
                return null;
            }

            public ComponentName getInstantAppResolverSettingsComponent() throws RemoteException {
                return null;
            }

            public ComponentName getInstantAppInstallerComponent() throws RemoteException {
                return null;
            }

            public String getInstantAppAndroidId(String s, int i) throws RemoteException {
                return null;
            }

            public IArtManager getArtManager() throws RemoteException {
                return null;
            }

            public void setHarmfulAppWarning(String s, CharSequence charSequence, int i) throws RemoteException {
            }

            public CharSequence getHarmfulAppWarning(String s, int i) throws RemoteException {
                return null;
            }

            public boolean hasSigningCertificate(String s, byte[] bytes, int i) throws RemoteException {
                return false;
            }

            public boolean hasUidSigningCertificate(int i, byte[] bytes, int i1) throws RemoteException {
                return false;
            }

            public String getDefaultTextClassifierPackageName() throws RemoteException {
                return null;
            }

            public String getSystemTextClassifierPackageName() throws RemoteException {
                return null;
            }

            public String getAttentionServicePackageName() throws RemoteException {
                return null;
            }

            public String getRotationResolverPackageName() throws RemoteException {
                return null;
            }

            public String getWellbeingPackageName() throws RemoteException {
                return null;
            }

            public String getAppPredictionServicePackageName() throws RemoteException {
                return null;
            }

            public String getSystemCaptionsServicePackageName() throws RemoteException {
                return null;
            }

            public String getSetupWizardPackageName() throws RemoteException {
                return null;
            }

            public String getIncidentReportApproverPackageName() throws RemoteException {
                return null;
            }

            public boolean isPackageStateProtected(String s, int i) throws RemoteException {
                return false;
            }

            public void sendDeviceCustomizationReadyBroadcast() throws RemoteException {
            }

            public List<ModuleInfo> getInstalledModules(int i) throws RemoteException {
                return null;
            }

            public ModuleInfo getModuleInfo(String s, int i) throws RemoteException {
                return null;
            }

            public int getRuntimePermissionsVersion(int i) throws RemoteException {
                return 0;
            }

            public void setRuntimePermissionsVersion(int i, int i1) throws RemoteException {
            }

            public void notifyPackagesReplacedReceived(String[] strings) throws RemoteException {
            }

            public void requestPackageChecksums(String s, boolean b, int i, int i1, List list, IOnChecksumsReadyListener iOnChecksumsReadyListener, int i2) throws RemoteException {
            }

            public IntentSender getLaunchIntentSenderForPackage(String s, String s1, String s2, int i) throws RemoteException {
                return null;
            }

            public String[] getAppOpPermissionPackages(String s, int userId) throws RemoteException {
                return new String[0];
            }

            public PermissionGroupInfo getPermissionGroupInfo(String s, int i) throws RemoteException {
                return null;
            }

            public boolean addPermission(PermissionInfo permissionInfo) throws RemoteException {
                return false;
            }

            public boolean addPermissionAsync(PermissionInfo permissionInfo) throws RemoteException {
                return false;
            }

            public void removePermission(String s) throws RemoteException {
            }

            public int checkPermission(String s, String s1, int i) throws RemoteException {
                return 0;
            }

            public void grantRuntimePermission(String s, String s1, int i) throws RemoteException {
            }

            public int checkUidPermission(String s, int i) throws RemoteException {
                return 0;
            }

            public void setMimeGroup(String s, String s1, List<String> list) throws RemoteException {
            }

            public String getSplashScreenTheme(String s, int i) throws RemoteException {
                return null;
            }

            public void setSplashScreenTheme(String s, String s1, int i) throws RemoteException {
            }

            public List<String> getMimeGroup(String s, String s1) throws RemoteException {
                return null;
            }

            public boolean isAutoRevokeWhitelisted(String s) throws RemoteException {
                return false;
            }

            public void makeProviderVisible(int i, String s) throws RemoteException {
            }

            public void makeUidVisible(int i, int i1) throws RemoteException {
            }

            public IBinder getHoldLockToken() throws RemoteException {
                return null;
            }

            public void holdLock(IBinder iBinder, int i) throws RemoteException {
            }

            public PackageManager.Property getPropertyAsUser(String s, String s1, String s2, int userId) throws RemoteException {
                return null;
            }

            public android.content.pm.ParceledListSlice queryProperty(String s, int i) throws RemoteException {
                return null;
            }

            public void setKeepUninstalledPackages(List<String> list) throws RemoteException {
            }

            public boolean[] canPackageQuery(String s, String[] s1, int i) throws RemoteException {
                return new boolean[0];
            }

            public boolean waitForHandler(long timeoutMillis, boolean forBackgroundHandler) throws RemoteException {
                return false;
            }
        };

        public static IPackageManager asInterface(IBinder obj) {
            return new Proxy(IPackageManager.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IPackageManager {
            private final android.content.pm.IPackageManager mTarget;

            Proxy(android.content.pm.IPackageManager target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.content.pm.IPackageManager
            public boolean isStorageLow() throws RemoteException {
                return this.mTarget.isStorageLow();
            }

            @Override // com.oplus.wrapper.content.pm.IPackageManager
            public int installExistingPackageAsUser(String packageName, int userId, int installFlags, int installReason, List<String> whiteListedPermissions) throws RemoteException {
                return this.mTarget.installExistingPackageAsUser(packageName, userId, installFlags, installReason, whiteListedPermissions);
            }

            @Override // com.oplus.wrapper.content.pm.IPackageManager
            public android.content.pm.ActivityInfo getActivityInfo(ComponentName className, long flags, int userId) throws RemoteException {
                return this.mTarget.getActivityInfo(className, flags, userId);
            }

            @Override // com.oplus.wrapper.content.pm.IPackageManager
            public ComponentName getHomeActivities(List<android.content.pm.ResolveInfo> outHomeCandidates) throws RemoteException {
                return this.mTarget.getHomeActivities(outHomeCandidates);
            }

            @Override // com.oplus.wrapper.content.pm.IPackageManager
            public int getApplicationEnabledSetting(String packageName, int userId) throws RemoteException {
                return this.mTarget.getApplicationEnabledSetting(packageName, userId);
            }

            @Override // com.oplus.wrapper.content.pm.IPackageManager
            public android.content.pm.ResolveInfo resolveIntent(Intent intent, String resolvedType, long flags, int userId) throws RemoteException {
                return this.mTarget.resolveIntent(intent, resolvedType, flags, userId);
            }

            @Override // com.oplus.wrapper.content.pm.IPackageManager
            public void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage) throws RemoteException {
                this.mTarget.setApplicationEnabledSetting(packageName, newState, flags, userId, callingPackage);
            }
        }
    }
}
