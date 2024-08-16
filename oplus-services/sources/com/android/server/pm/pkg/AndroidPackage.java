package com.android.server.pm.pkg;

import android.annotation.SystemApi;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureGroupInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.pm.SigningDetails;
import android.os.Bundle;
import android.processor.immutability.Immutable;
import android.util.ArraySet;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.server.pm.pkg.component.ParsedActivity;
import com.android.server.pm.pkg.component.ParsedApexSystemService;
import com.android.server.pm.pkg.component.ParsedAttribution;
import com.android.server.pm.pkg.component.ParsedInstrumentation;
import com.android.server.pm.pkg.component.ParsedIntentInfo;
import com.android.server.pm.pkg.component.ParsedPermission;
import com.android.server.pm.pkg.component.ParsedPermissionGroup;
import com.android.server.pm.pkg.component.ParsedProcess;
import com.android.server.pm.pkg.component.ParsedProvider;
import com.android.server.pm.pkg.component.ParsedService;
import com.android.server.pm.pkg.component.ParsedUsesPermission;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Immutable
@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface AndroidPackage {
    @Immutable.Ignore
    List<ParsedActivity> getActivities();

    List<String> getAdoptPermissions();

    @Immutable.Ignore
    List<ParsedApexSystemService> getApexSystemServices();

    String getAppComponentFactory();

    String getApplicationClassName();

    @Immutable.Ignore
    List<ParsedAttribution> getAttributions();

    int getAutoRevokePermissions();

    String getBackupAgentName();

    int getBannerResourceId();

    @Deprecated
    String getBaseApkPath();

    int getBaseRevisionCode();

    int getCategory();

    String getClassLoaderName();

    int getCompatibleWidthLimitDp();

    int getCompileSdkVersion();

    String getCompileSdkVersionCodeName();

    @Immutable.Ignore
    List<ConfigurationInfo> getConfigPreferences();

    int getDataExtractionRulesResourceId();

    int getDescriptionResourceId();

    @Immutable.Ignore
    List<FeatureGroupInfo> getFeatureGroups();

    int getFullBackupContentResourceId();

    int getGwpAsanMode();

    int getIconResourceId();

    List<String> getImplicitPermissions();

    int getInstallLocation();

    @Immutable.Ignore
    List<ParsedInstrumentation> getInstrumentations();

    @Immutable.Ignore
    Map<String, ArraySet<PublicKey>> getKeySetMapping();

    Set<String> getKnownActivityEmbeddingCerts();

    int getLabelResourceId();

    int getLargestWidthLimitDp();

    List<String> getLibraryNames();

    int getLocaleConfigResourceId();

    int getLogoResourceId();

    long getLongVersionCode();

    String getManageSpaceActivityName();

    String getManifestPackageName();

    float getMaxAspectRatio();

    int getMaxSdkVersion();

    int getMemtagMode();

    @Immutable.Ignore
    Bundle getMetaData();

    Set<String> getMimeGroups();

    float getMinAspectRatio();

    @Immutable.Ignore
    SparseIntArray getMinExtensionVersions();

    int getMinSdkVersion();

    int getNativeHeapZeroInitialized();

    String getNativeLibraryDir();

    String getNativeLibraryRootDir();

    int getNetworkSecurityConfigResourceId();

    CharSequence getNonLocalizedLabel();

    List<String> getOriginalPackages();

    String getOverlayCategory();

    int getOverlayPriority();

    String getOverlayTarget();

    String getOverlayTargetOverlayableName();

    Map<String, String> getOverlayables();

    String getPackageName();

    String getPath();

    String getPermission();

    @Immutable.Ignore
    List<ParsedPermissionGroup> getPermissionGroups();

    @Immutable.Ignore
    List<ParsedPermission> getPermissions();

    @Immutable.Ignore
    List<Pair<String, ParsedIntentInfo>> getPreferredActivityFilters();

    String getProcessName();

    @Immutable.Ignore
    Map<String, ParsedProcess> getProcesses();

    @Immutable.Ignore
    Map<String, PackageManager.Property> getProperties();

    List<String> getProtectedBroadcasts();

    @Immutable.Ignore
    List<ParsedProvider> getProviders();

    @Immutable.Ignore
    List<Intent> getQueriesIntents();

    List<String> getQueriesPackages();

    Set<String> getQueriesProviders();

    @Immutable.Ignore
    List<ParsedActivity> getReceivers();

    @Immutable.Ignore
    List<FeatureInfo> getRequestedFeatures();

    List<String> getRequestedPermissions();

    String getRequiredAccountType();

    int getRequiresSmallestWidthDp();

    Boolean getResizeableActivity();

    @Immutable.Ignore
    byte[] getRestrictUpdateHash();

    String getRestrictedAccountType();

    int getRoundIconResourceId();

    int getSdkLibVersionMajor();

    String getSdkLibraryName();

    String getSecondaryNativeLibraryDir();

    @Immutable.Ignore
    List<ParsedService> getServices();

    String getSharedUserId();

    int getSharedUserLabelResourceId();

    @Immutable.Ignore
    SigningDetails getSigningDetails();

    @Immutable.Ignore
    String[] getSplitClassLoaderNames();

    @Immutable.Ignore
    String[] getSplitCodePaths();

    @Immutable.Ignore
    SparseArray<int[]> getSplitDependencies();

    @Immutable.Ignore
    int[] getSplitFlags();

    @Immutable.Ignore
    String[] getSplitNames();

    @Immutable.Ignore
    int[] getSplitRevisionCodes();

    List<AndroidPackageSplit> getSplits();

    String getStaticSharedLibraryName();

    long getStaticSharedLibraryVersion();

    UUID getStorageUuid();

    int getTargetSandboxVersion();

    int getTargetSdkVersion();

    String getTaskAffinity();

    int getThemeResourceId();

    int getUiOptions();

    @Deprecated
    int getUid();

    Set<String> getUpgradeKeySets();

    List<String> getUsesLibraries();

    List<String> getUsesNativeLibraries();

    List<String> getUsesOptionalLibraries();

    List<String> getUsesOptionalNativeLibraries();

    @Immutable.Ignore
    List<ParsedUsesPermission> getUsesPermissions();

    List<String> getUsesSdkLibraries();

    @Immutable.Ignore
    String[][] getUsesSdkLibrariesCertDigests();

    @Immutable.Ignore
    long[] getUsesSdkLibrariesVersionsMajor();

    List<String> getUsesStaticLibraries();

    @Immutable.Ignore
    String[][] getUsesStaticLibrariesCertDigests();

    @Immutable.Ignore
    long[] getUsesStaticLibrariesVersions();

    String getVersionName();

    String getVolumeUuid();

    String getZygotePreloadName();

    boolean hasPreserveLegacyExternalStorage();

    boolean hasRequestForegroundServiceExemption();

    Boolean hasRequestRawExternalStorageAccess();

    boolean is32BitAbiPreferred();

    boolean isAllowAudioPlaybackCapture();

    boolean isAllowNativeHeapPointerTagging();

    boolean isAnyDensity();

    boolean isApex();

    boolean isAttributionsUserVisible();

    boolean isBackupAllowed();

    boolean isBackupInForeground();

    boolean isClearUserDataAllowed();

    boolean isClearUserDataOnFailedRestoreAllowed();

    boolean isCleartextTrafficAllowed();

    boolean isCoreApp();

    boolean isCrossProfile();

    boolean isDebuggable();

    boolean isDeclaredHavingCode();

    boolean isDefaultToDeviceProtectedStorage();

    boolean isDirectBootAware();

    boolean isEnabled();

    boolean isExternalStorage();

    boolean isExtraLargeScreensSupported();

    boolean isExtractNativeLibrariesRequested();

    boolean isFactoryTest();

    boolean isForceQueryable();

    boolean isFullBackupOnly();

    @Deprecated
    boolean isGame();

    boolean isHardwareAccelerated();

    boolean isHasDomainUrls();

    boolean isIsolatedSplitLoading();

    boolean isKillAfterRestoreAllowed();

    boolean isLargeHeap();

    boolean isLargeScreensSupported();

    boolean isLeavingSharedUser();

    boolean isMultiArch();

    boolean isNativeLibraryRootRequiresIsa();

    boolean isNonSdkApiRequested();

    boolean isNormalScreensSupported();

    boolean isOnBackInvokedCallbackEnabled();

    boolean isOverlayIsStatic();

    boolean isPartiallyDirectBootAware();

    boolean isPersistent();

    boolean isProfileable();

    boolean isProfileableByShell();

    boolean isRequestLegacyExternalStorage();

    boolean isRequiredForAllUsers();

    boolean isResetEnabledSettingsOnAppDataCleared();

    boolean isResizeable();

    boolean isResizeableActivityViaSdkVersion();

    boolean isResourceOverlay();

    boolean isRestoreAnyVersion();

    boolean isRtlSupported();

    boolean isSaveStateDisallowed();

    boolean isSdkLibrary();

    boolean isSignedWithPlatformKey();

    boolean isSmallScreensSupported();

    boolean isStaticSharedLibrary();

    boolean isStub();

    boolean isTaskReparentingAllowed();

    boolean isTestOnly();

    boolean isUseEmbeddedDex();

    boolean isUserDataFragile();

    boolean isVisibleToInstantApps();

    boolean isVmSafeMode();
}
