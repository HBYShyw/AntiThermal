package com.android.server.pm;

import android.content.ComponentName;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManagerInternal;
import android.content.pm.TestUtilityService;
import android.os.Build;
import android.os.Handler;
import android.os.incremental.IncrementalManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.om.OverlayConfig;
import com.android.server.pm.MovePackageHelper;
import com.android.server.pm.dex.ArtManagerService;
import com.android.server.pm.dex.DexManager;
import com.android.server.pm.dex.DynamicCodeLogger;
import com.android.server.pm.dex.ViewCompiler;
import com.android.server.pm.parsing.PackageParser2;
import com.android.server.pm.permission.LegacyPermissionManagerInternal;
import com.android.server.pm.pkg.AndroidPackage;
import java.io.File;
import java.util.List;
import java.util.Set;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PackageManagerServiceTestParams {
    public DisplayMetrics Metrics;
    public String ambientContextDetectionPackage;
    public ApexManager apexManager;
    public AppDataHelper appDataHelper;
    public File appInstallDir;
    public File appLib32InstallDir;
    public String appPredictionServicePackage;
    public ArtManagerService artManagerService;
    public ArrayMap<String, FeatureInfo> availableFeatures;
    public BackgroundDexOptService backgroundDexOptService;
    public BroadcastHelper broadcastHelper;
    public String configuratorPackage;
    public int defParseFlags;
    public DefaultAppProvider defaultAppProvider;
    public String defaultTextClassifierPackage;
    public DeletePackageHelper deletePackageHelper;
    public DexManager dexManager;
    public DexOptHelper dexOptHelper;
    public List<ScanPartition> dirsToScanAsSystem;
    public DistractingPackageHelper distractingPackageHelper;
    public DynamicCodeLogger dynamicCodeLogger;
    public boolean enableFreeCacheV2;
    public boolean factoryTest;
    public Handler handler;
    public String incidentReportApproverPackage;
    public IncrementalManager incrementalManager;
    public InitAppsHelper initAndSystemPackageHelper;
    public InstallPackageHelper installPackageHelper;
    public PackageInstallerService installerService;
    public InstantAppRegistry instantAppRegistry;
    public InstantAppResolverConnection instantAppResolverConnection;
    public ComponentName instantAppResolverSettingsComponent;
    public boolean isEngBuild;
    public boolean isPreNmr1Upgrade;
    public boolean isPreQupgrade;
    public boolean isUpgrade;
    public boolean isUserDebugBuild;
    public LegacyPermissionManagerInternal legacyPermissionManagerInternal;
    public ModuleInfoProvider moduleInfoProvider;
    public MovePackageHelper.MoveCallbacks moveCallbacks;
    public boolean onlyCore;
    public OverlayConfig overlayConfig;
    public String overlayConfigSignaturePackage;
    public PackageDexOptimizer packageDexOptimizer;
    public PackageParser2.Callback packageParserCallback;
    public ArrayMap<String, AndroidPackage> packages;
    public PendingPackageBroadcasts pendingPackageBroadcasts;
    public PackageManagerInternal pmInternal;
    public PreferredActivityHelper preferredActivityHelper;
    public ProcessLoggingHandler processLoggingHandler;
    public ProtectedPackages protectedPackages;
    public String recentsPackage;
    public RemovePackageHelper removePackageHelper;
    public String requiredInstallerPackage;
    public String requiredPermissionControllerPackage;
    public String requiredSdkSandboxPackage;
    public String requiredUninstallerPackage;
    public String[] requiredVerifierPackages;
    public ComponentName resolveComponentName;
    public ResolveIntentHelper resolveIntentHelper;
    public String retailDemoPackage;
    public int sdkVersion;
    public String[] separateProcesses;
    public String servicesExtensionPackageName;
    public String setupWizardPackage;
    public String sharedSystemSharedLibraryPackageName;
    public boolean shouldStopSystemPackagesByDefault;
    public StorageEventHelper storageEventHelper;
    public String storageManagerPackage;
    public SuspendPackageHelper suspendPackageHelper;
    public String systemTextClassifierPackage;
    public TestUtilityService testUtilityService;
    public ViewCompiler viewCompiler;
    public String wearableSensingPackage;
    public ChangedPackagesTracker changedPackagesTracker = new ChangedPackagesTracker();
    public int sdkInt = Build.VERSION.SDK_INT;
    public final String incrementalVersion = Build.VERSION.INCREMENTAL;
    public Set<String> initialNonStoppedSystemPackages = new ArraySet();
}
