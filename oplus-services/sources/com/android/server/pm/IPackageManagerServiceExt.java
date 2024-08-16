package com.android.server.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDeleteObserver2;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInfoLite;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.UserInfo;
import android.content.pm.VersionedPackage;
import android.content.pm.parsing.PackageLite;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import com.android.server.art.model.DexoptParams;
import com.android.server.art.model.DexoptResult;
import com.android.server.art.model.OperationProgress;
import com.android.server.pm.ApexManager;
import com.android.server.pm.PackageManagerLocal;
import com.android.server.pm.ParallelPackageParser;
import com.android.server.pm.Settings;
import com.android.server.pm.dex.DexoptOptions;
import com.android.server.pm.parsing.PackageParser2;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.component.ParsedActivity;
import com.android.server.pm.pkg.component.ParsedPermission;
import com.android.server.pm.pkg.component.ParsedService;
import com.android.server.pm.resolution.ComponentResolverApi;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageManagerServiceExt {
    public static final boolean DEBUG_PMS = true;

    default ApplicationInfo adjustAiInComputerEngineGAppIIB(ApplicationInfo applicationInfo, AndroidPackage androidPackage, PackageStateInternal packageStateInternal, String str, long j, int i) {
        return applicationInfo;
    }

    default int adjustDeleteFlagInDeleteExistingPackageAsUser(int i, VersionedPackage versionedPackage) {
        return i;
    }

    default long adjustFlagsInComputerEngineGAIIB(long j) {
        return j;
    }

    default long adjustFlagsInComputerEngineQIAI(long j) {
        return j;
    }

    default long adjustFlagsInGetInstalledPackages(Computer computer, long j, int i) {
        return j;
    }

    default boolean adjustIsUpgradeFlag(boolean z) {
        return z;
    }

    default PackageSetting adjustPackageSettingInDeletePackageLIF(PackageSetting packageSetting, Context context) {
        return packageSetting;
    }

    default int adjustPermissionStateCheckInSetEnabledSetting(Context context, int i) {
        return i;
    }

    default ProviderInfo adjustProviderInfoInRCPI(Computer computer, ProviderInfo providerInfo, ComponentResolverApi componentResolverApi, String str, long j, int i) {
        return providerInfo;
    }

    default PackageInfo adjustResultAtEndInComputerEngineGPIIB(PackageInfo packageInfo, String str) {
        return packageInfo;
    }

    default ServiceInfo adjustResultAtEndInComputerEngineGSIB(ServiceInfo serviceInfo, Computer computer, Settings settings, AndroidPackage androidPackage, ParsedService parsedService, ComponentName componentName, long j, int i, int i2) {
        return serviceInfo;
    }

    default PackageInfo adjustResultForHasPkgInComputerEngineGPIIB(PackageInfo packageInfo, Object obj, PackageStateInternal packageStateInternal, String str, long j, int i, int i2) {
        return packageInfo;
    }

    default List<ResolveInfo> adjustResultForHasPkgNameInComputerEngineQIAIB(List<ResolveInfo> list, Computer computer, ComponentResolverApi componentResolverApi, Intent intent, String str, long j, int i, int i2, PackageStateInternal packageStateInternal) {
        return list;
    }

    default List<ResolveInfo> adjustResultForNoPkgNameInComputerEngineQIAIB(List<ResolveInfo> list, Computer computer, ComponentResolverApi componentResolverApi, Intent intent, String str, long j, int i, int i2) {
        return list;
    }

    default List<ResolveInfo> adjustResultInComputerEngineMAIAI(List<ResolveInfo> list) {
        return list;
    }

    default int adjustScanFlagsForDataDir(int i) {
        return i;
    }

    default void adjustScanResultBeforeRegisterAppIdInAFILI(ScanResult scanResult) {
    }

    default String adjustUriUserIdInRCPI(String str, int i) {
        return str;
    }

    default int adjustUserIdInComputerEngineGAIIB(Computer computer, int i, ParsedActivity parsedActivity, Settings settings, AndroidPackage androidPackage, ComponentName componentName, long j, int i2) {
        return i;
    }

    default int adjustUserIdWithProviderInfoInRCPI(int i) {
        return i;
    }

    default void adjustWritePackageRestrictionsInHandler(boolean z) {
    }

    default void afterApexGetListAndWaitForOpexFinishInConstructor() {
    }

    default void afterCalculateUpgradeFlagInConstructor(Settings.VersionInfo versionInfo) {
    }

    default void afterCheckSystemAppScannedInConstructor() {
    }

    default void afterDeleteInDeletePackageVersionedInternal(PackageStateInternal packageStateInternal, String str, Handler handler) {
    }

    default void afterDeleteSucceededInDeletePackageX(String str, int i, int i2) {
    }

    default void afterDexOptInExecutePostCommitSteps(AndroidPackage androidPackage, String str, boolean z) {
    }

    default void afterDoPostInstallInProcessInstallRequestsAsync(InstallArgs installArgs, Handler handler, String str, int i) {
    }

    default void afterFrameworksPackageScannedInConstructor(int i, int i2, PackageParser2 packageParser2, ExecutorService executorService) {
    }

    default void afterGetSystemConfigInConstructor() {
    }

    default void afterHandlePackagePostInstallInCasePostInstall(InstallRequest installRequest) {
    }

    default void afterInitializeArtManagerLocal(Context context) {
    }

    default void afterInstallPackagesLIForIconPack(Context context) {
    }

    default void afterNotifyUpdateForDexInExecutePostCommitSteps(AndroidPackage androidPackage) {
    }

    default void afterOnBootUseArtService(ExecutorService executorService) {
    }

    default void afterPackageManagerSystemReady(boolean z) {
    }

    default void afterPackageParsedInPreparePackageLI(Context context, ParsedPackage parsedPackage) {
    }

    default void afterPerformDexOptUpgradeInUpdatePackagesIfNeeded() {
    }

    default void afterPermissionManagerSystemReady() {
    }

    default void afterPmsReadyEventInConstructor() {
    }

    default void afterPmsScanEndEventInConstructor() {
    }

    default void afterPmsStartEventInConstructor() {
    }

    default void afterPmsSystemScanStartEventInConstructor() {
    }

    default void afterReadUserSettingsInConstructor() {
    }

    default void afterScanDataDirInConstructor() {
    }

    default void afterSendPackageAddedForAllInHPPI(AndroidPackage androidPackage, String str, Bundle bundle) {
    }

    default void afterSetVerifyFailInCasePackageVerified(VerifyingSession verifyingSession) {
    }

    default void afterUserManagerSystemReady() {
    }

    default boolean allowDuplicatedPermInPreparePackageLI(InstallArgs installArgs, String str) {
        return false;
    }

    default boolean allowPersistentUpdateInPreparePackageLI() {
        return false;
    }

    default boolean allowUninstallDeviceAdminInDeletePackageX(String str, int i) {
        return false;
    }

    default boolean allowUninstallSystemAppsForUser(UserInfo userInfo) {
        return false;
    }

    default boolean allowUnknownWhenScanRequireKnownInAssertPackageIsValid(AndroidPackage androidPackage) {
        return false;
    }

    default void beforeAddInAddPreferredActivityInternal(ComponentName componentName, WatchedIntentFilter watchedIntentFilter, int i) {
    }

    default void beforeAddSharedUsersInConstructor() {
    }

    default void beforeCheckSystemAppScannedInConstructor() {
    }

    default void beforeCreateNewUser(int i) {
    }

    default void beforeCreateSubComponentsInConstructor() {
    }

    default void beforeDeleteApplicationCacheFiles() {
    }

    default void beforeDeleteForSpecificUserInDeletePackageVersionedInternal(DeletePackageHelper deletePackageHelper, String str, long j, int i, int i2) {
    }

    default void beforeDeletePackageX(String str) {
    }

    default void beforeDoPostDeleteLIInHPPI(String str) {
    }

    default void beforeFailReturnInHandleStartCopyOfVerificationParams(int i, PackageInfoLite packageInfoLite, InstallSource installSource, int i2) {
    }

    default void beforeFailReturnInSetInstallerPackageNameOfVerificationPermission(String str) {
    }

    default void beforeInstallPackagesTracedLI() {
    }

    default void beforeInstallSystemStubPackagesInConstructor() {
    }

    default Pair<ExecutorService, Consumer<OperationProgress>> beforeOnBootUseArtService() {
        return null;
    }

    default void beforePerformDexOptUpgradeInUpdatePackagesIfNeeded(PackageManagerService packageManagerService, int i) {
    }

    default void beforePostDeleteInDeletePackageVersionedInternal(VersionedPackage versionedPackage, int i, String str) {
    }

    default void beforePrepareAppDataInInstallExistingPackageAsUser(String str, int i) {
    }

    default void beforePrepareForReplaceInPreparePackageLI(AndroidPackage androidPackage) {
    }

    default void beforeQueryInResolveContentProviderInternal(PackageManagerService packageManagerService, String str, int i) {
    }

    default void beforeRecordScanEndInConstructor() {
    }

    default void beforeReturnInAddForInitLI(ScanResult scanResult) {
    }

    default void beforeScanDataDirInConstructor() {
    }

    default void beforeScanInScanDirLI() {
    }

    default void beforeShowBootMessageInPerformDexOptUpgrade(boolean z, int i, int i2) {
    }

    default void beforeWriteSettingsInConstructor() {
    }

    default void beforeclearApplicationUserData(String str) {
    }

    default int calculateDelayRemoveIndex(int i, int i2) {
        return i;
    }

    default Integer checkPermissionExtAtBegin(String str, String str2, int i) {
        return null;
    }

    default Integer checkUidPermissionExtAtBegin(String str, int i) {
        return null;
    }

    default boolean customAllowInIsCallerAllowedToSilentlyUninstall(Computer computer, int i) {
        return false;
    }

    default void customHandleMsgInPackageHandler(Message message) {
    }

    default void customLogDuplicatedPermDeclared(Context context, String str, ParsedPackage parsedPackage, ParsedPermission parsedPermission) {
    }

    default boolean customPermissionUpgradeNeeded() {
        return false;
    }

    default void customScanDirLI(File file, int i, int i2, long j, PackageParser2 packageParser2, ExecutorService executorService, ApexManager.ActiveApexInfo activeApexInfo) {
    }

    default DexoptResult dexoptInExecutePostCommitStepsLIF(PackageManagerLocal.FilteredSnapshot filteredSnapshot, String str, DexoptParams dexoptParams) {
        return null;
    }

    default int dexoptInPerformDexOptWithArtService(PackageManagerLocal.FilteredSnapshot filteredSnapshot, String str, DexoptParams dexoptParams) {
        return 0;
    }

    default void disablePackagesNoneReboot(String str, String str2) {
    }

    default boolean doDelayedRemoveInFPANL(Computer computer, int i, PreferredIntentResolver preferredIntentResolver, List<PreferredActivity> list, Intent intent, String str) {
        return false;
    }

    default boolean doPreWorkBeforeDexOptInExecutePostCommitSteps(AndroidPackage androidPackage) {
        return false;
    }

    default boolean dumpProfilesExtAtBegin(String str) {
        return false;
    }

    default boolean filterApplicationInfoInGIALI(String str, boolean z) {
        return false;
    }

    default boolean filterPackageInComputerEngineGIPB(String str, boolean z) {
        return false;
    }

    default ArrayList<ApplicationInfo> getInstalledApplicationsAsUserExt(ArrayList<ApplicationInfo> arrayList) {
        return null;
    }

    default ArrayList<PackageInfo> getInstalledPackagesAsUserExt(ArrayList<PackageInfo> arrayList) {
        return null;
    }

    default void getRequiredServicesExtensionPackageError() {
    }

    default void handleExpOfAddForInitInScanDirLI(PackageManagerService packageManagerService, ParallelPackageParser.ParseResult parseResult, File file, int i, int i2, ApexManager.ActiveApexInfo activeApexInfo) {
    }

    default void handleNewUserInONUC(int i) {
    }

    default void handleSuccessAtEndInHPPI(Context context, AndroidPackage androidPackage, String str, String str2, boolean z, int[] iArr) {
    }

    default Boolean hasSystemFeatureExtAtBegin(String str, int i) {
        return null;
    }

    default boolean hookBeforeTargetSdkBlock(InstallRequest installRequest, boolean z, int i) {
        return z;
    }

    default boolean hookOplusOtaPs(PackageSetting packageSetting) {
        return false;
    }

    default boolean ignoreChangeInPackageParserCallback(long j, ApplicationInfo applicationInfo) {
        return false;
    }

    default void initInMain() {
    }

    default void initOplusBinderExtensionInConstructor(Binder binder) {
    }

    default void installRemovablePackagesNoneReboot(List<String> list) {
    }

    default boolean installStageClusterExtAtBegin(InstallingSession installingSession, List<InstallingSession> list) throws PackageManagerException {
        return false;
    }

    default boolean installStageExtAtBegin(InstallingSession installingSession) {
        return false;
    }

    default void installSystemPackagesNoneReboot(List<String> list) {
    }

    default boolean interceptActionInSetEnabledSetting(int i, int i2, String str) {
        return false;
    }

    default boolean interceptAddPreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, int i2, boolean z) {
        return false;
    }

    default boolean interceptDeleteInDeletePackageVersionedInternal(Context context, String str, int i, int i2, Handler handler, IPackageDeleteObserver2 iPackageDeleteObserver2, VersionedPackage versionedPackage) {
        return false;
    }

    default boolean interceptHideInSetApplicationHiddenSettingAsUser(boolean z, String str) {
        return false;
    }

    default boolean interceptOsdkVersionInPreparePackageLI(ParsedPackage parsedPackage) {
        return false;
    }

    default boolean interceptPerformDexOptSecondary(String str, String str2, boolean z) {
        return false;
    }

    default boolean interceptReplacePreferredActivity(IntentFilter intentFilter) {
        return false;
    }

    default Exception interceptSystemAppInPreparePackageLI(boolean z, ParsedPackage parsedPackage) {
        return null;
    }

    default boolean interceptUseParseResultWithoutThrowInScanDirLI(ParallelPackageParser.ParseResult parseResult, int i) {
        return false;
    }

    default boolean interceptUseParseResultWithoutThrowInScanDirLI2(ParallelPackageParser.ParseResult parseResult, int i, int i2, File file) {
        return false;
    }

    default Exception interceptWithSigInPreparePackageLI(ParsedPackage parsedPackage, String str, InstallArgs installArgs, int i) {
        return null;
    }

    default Exception interceptWithoutSigInPreparePackageLI(Context context, InstallRequest installRequest, String str) {
        return null;
    }

    default boolean isDefaultAppPolicyEnabledInFPANL(Intent intent) {
        return false;
    }

    default boolean isFirstNotifyDexLoad(ApplicationInfo applicationInfo, String str, Map<String, String> map) {
        return false;
    }

    default boolean isHoldingLockInFindPreferredActivityNotLocked() {
        return false;
    }

    default boolean isHoldingLockInUpdateDefaultHomeNotLocked() {
        return false;
    }

    default boolean isHoldingLockInUpdateDefaultHomeNotLockedMulti() {
        return false;
    }

    default boolean isResolveForPermissionController(Computer computer, int i) {
        return false;
    }

    default boolean isTranslatorWhitelistApp(String str) {
        return true;
    }

    default boolean judgeIsSystemAppCallInComputerEngineGIPB() {
        return false;
    }

    default boolean judgeIsSystemAppCallInGIALI() {
        return false;
    }

    default void killDex2oatNow() {
    }

    default DexoptOptions modifyDexoptOptionsBeforDo(IInstallArgsExt iInstallArgsExt, DexoptOptions dexoptOptions) {
        return dexoptOptions;
    }

    default InstallArgs modifyInstallArgsInProcessPendingInstall(InstallArgs installArgs, IInstallParamsExt iInstallParamsExt, PackageLite packageLite) {
        return installArgs;
    }

    default int modifyRetInHandleStartCopyOfVerificationParams(int i, InstallSource installSource, PackageInfoLite packageInfoLite, IPackageInstallObserver2 iPackageInstallObserver2) {
        return i;
    }

    default void notifyDexLoad(ApplicationInfo applicationInfo, String str, Map<String, String> map, String str2) {
    }

    default void notifyPackageAddOrUpdateForAbiInfo(String str, PackageSetting packageSetting) {
    }

    default void notifyPackageDeleteForAbiInfo(String str) {
    }

    default void notifyPackageUseLocked(String str, int i) {
    }

    default void onEndLockedWorkInConstructor() {
    }

    default void onHandleForNotMoveInPreparePackageLI(InstallArgs installArgs, int i) {
    }

    default void onMarkPackageUninstalledForUser(PackageSetting packageSetting, int i) {
    }

    default void onNotifyInstallObserver(String str, int i) {
    }

    default void onPackagePrepareFinishedInConstructor() {
    }

    default void onPrepareAppDataFutureEndByDeferDone(int i) {
    }

    default void onPrepareAppDataFutureEndByNoDefer() {
    }

    default void onPrepareSaveIconPack(Context context, Map<Integer, List<PackageInfo>> map) {
    }

    default void onStartInDeletePackageVersionedInternal(String str) {
    }

    default int onStartInPerformDexOptUpgrade(int i) {
        return i;
    }

    default void onStartInRestoreAndPostInstall(InstallRequest installRequest) {
    }

    default void onStartLockedWorkInConstructor() {
    }

    default void onStartSetEnabledSettingForInformation(List<PackageManager.ComponentEnabledSetting> list, int i, String str) {
    }

    default void onSystemAppNotExistCheckedInConstructor(PackageSetting packageSetting) {
    }

    default int preSetRetInOverrideInstallLocation(String str) {
        return 1;
    }

    default List<ResolveInfo> queryIntentActivitiesExtAtBegin(Intent intent, String str, long j, int i) {
        return null;
    }

    default void readAbiInfoAfterScanEnd(Map<String, PackageSetting> map) {
    }

    default void sendMapCommonDcsUpload(String str, String str2, Map map) {
    }

    default void sendMapCommonDcsUploadWithAppID(String str, String str2, String str3, Map map) {
    }

    default void sendPackageChangedBroadcastInSetEnabledSetting(Computer computer, List<PackageManager.ComponentEnabledSetting> list, String str, ArrayList<String> arrayList, int i, String str2, int i2) {
    }

    default boolean setMarketRecommendPause(long j) {
        return false;
    }

    default boolean shouldReconcileAppsDataInConstructor(PackageManagerService packageManagerService) {
        return true;
    }

    default boolean shouldRemoveUpdatedMainlineApk(String str) {
        return false;
    }

    default boolean shouldSetInstallSettingInInstallExistingPackageAsUser(PackageSetting packageSetting, String str, int i) {
        return false;
    }

    default boolean shouldSkipReturnInFPANL(boolean z) {
        return false;
    }

    default boolean shouldUseCustomScanDirLI() {
        return false;
    }

    default boolean shouldUseLiveComputerInSnapshotComputer() {
        return false;
    }

    default void showAppInstallationRecommendPage(String str, InstallSource installSource) {
    }

    default void shutdownExtAtEnd() {
    }

    default boolean skipCheckCrossUserPermission(int i, int i2) {
        return false;
    }

    default boolean skipDeleteDataAppWhenFailedInScanDirLI(PackageManagerService packageManagerService) {
        return false;
    }

    default boolean skipDestroyAppDataInDestroyAppDataLeafLIF2(AndroidPackage androidPackage, int i, int i2, long j) {
        return false;
    }

    default boolean skipDestroyDeDataInReconcileAppsDataLI(String str, String str2, int i) {
        return false;
    }

    default boolean skipInstallInMultiUser(int i, String str) {
        return false;
    }

    default boolean skipMatchCheckInFPANL(boolean z, List<ResolveInfo> list) {
        return false;
    }

    default boolean skipRemoveKeyStoreInRPDLIF(String str, int i) {
        return false;
    }

    default boolean skipSigCheckWhenDataToSystemInAddForInitLI(AndroidPackage androidPackage, boolean z, int i) {
        return false;
    }

    default boolean skipVerifyInSendPackageVerificationRequest(List<String> list) {
        return false;
    }

    default boolean useLongBroadcastDelayInSetEnabledSetting(int i) {
        return false;
    }

    default boolean writeMdmLog(String str, String str2, String str3) {
        return false;
    }

    default Map<String, String> getContainOplusCertificatePackages() {
        return new HashMap();
    }
}
