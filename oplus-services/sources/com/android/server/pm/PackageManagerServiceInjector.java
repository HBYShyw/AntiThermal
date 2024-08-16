package com.android.server.pm;

import android.app.ActivityManagerInternal;
import android.app.backup.IBackupManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.incremental.IncrementalManager;
import android.util.DisplayMetrics;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.SystemConfig;
import com.android.server.compat.PlatformCompat;
import com.android.server.pm.dex.ArtManagerService;
import com.android.server.pm.dex.DexManager;
import com.android.server.pm.dex.DynamicCodeLogger;
import com.android.server.pm.dex.ViewCompiler;
import com.android.server.pm.parsing.PackageParser2;
import com.android.server.pm.permission.LegacyPermissionManagerInternal;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.resolution.ComponentResolver;
import com.android.server.pm.verify.domain.DomainVerificationManagerInternal;
import java.util.List;
import java.util.concurrent.Executor;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageManagerServiceInjector {
    private final PackageAbiHelper mAbiHelper;
    private final Singleton<ApexManager> mApexManagerProducer;
    private final Singleton<AppsFilterImpl> mAppsFilterProducer;
    private final Singleton<ArtManagerService> mArtManagerServiceProducer;
    private final Singleton<BackgroundDexOptService> mBackgroundDexOptService;
    private final Executor mBackgroundExecutor;
    private final Handler mBackgroundHandler;
    private final Singleton<ComponentResolver> mComponentResolverProducer;
    private final Context mContext;
    private final Singleton<CrossProfileIntentFilterHelper> mCrossProfileIntentFilterHelperProducer;
    private final Singleton<DefaultAppProvider> mDefaultAppProviderProducer;
    private final Singleton<DexManager> mDexManagerProducer;
    private final Singleton<DisplayMetrics> mDisplayMetricsProducer;
    private final Singleton<DomainVerificationManagerInternal> mDomainVerificationManagerInternalProducer;
    private final Singleton<DynamicCodeLogger> mDynamicCodeLoggerProducer;
    private final ServiceProducer mGetLocalServiceProducer;
    private final ServiceProducer mGetSystemServiceProducer;
    private final Singleton<Handler> mHandlerProducer;
    private final Singleton<IBackupManager> mIBackupManager;
    private final Singleton<IncrementalManager> mIncrementalManagerProducer;
    private final Object mInstallLock;
    private final Installer mInstaller;
    private final ProducerWithArgument<InstantAppResolverConnection, ComponentName> mInstantAppResolverConnectionProducer;
    private final Singleton<LegacyPermissionManagerInternal> mLegacyPermissionManagerInternalProducer;
    private final PackageManagerTracedLock mLock;
    private final Singleton<ModuleInfoProvider> mModuleInfoProviderProducer;
    private final Singleton<PackageDexOptimizer> mPackageDexOptimizerProducer;
    private final Singleton<PackageInstallerService> mPackageInstallerServiceProducer;
    private PackageManagerService mPackageManager;
    private final Singleton<PermissionManagerServiceInternal> mPermissionManagerServiceProducer;
    private final Singleton<PlatformCompat> mPlatformCompatProducer;
    private final Producer<PackageParser2> mPreparingPackageParserProducer;
    private final Producer<PackageParser2> mScanningCachingPackageParserProducer;
    private final Producer<PackageParser2> mScanningPackageParserProducer;
    private final Singleton<Settings> mSettingsProducer;
    private final Singleton<SharedLibrariesImpl> mSharedLibrariesProducer;
    private final Singleton<SystemConfig> mSystemConfigProducer;
    private final List<ScanPartition> mSystemPartitions;
    private final SystemWrapper mSystemWrapper;
    private final Singleton<UpdateOwnershipHelper> mUpdateOwnershipHelperProducer;
    private final Singleton<UserManagerService> mUserManagerProducer;
    private final Singleton<ViewCompiler> mViewCompilerProducer;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Producer<T> {
        T produce(PackageManagerServiceInjector packageManagerServiceInjector, PackageManagerService packageManagerService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ProducerWithArgument<T, R> {
        T produce(PackageManagerServiceInjector packageManagerServiceInjector, PackageManagerService packageManagerService, R r);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ServiceProducer {
        <T> T produce(Class<T> cls);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface SystemWrapper {
        void disablePackageCaches();

        void enablePackageCaches();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Singleton<T> {
        private volatile T mInstance = null;
        private final Producer<T> mProducer;

        Singleton(Producer<T> producer) {
            this.mProducer = producer;
        }

        T get(PackageManagerServiceInjector packageManagerServiceInjector, PackageManagerService packageManagerService) {
            if (this.mInstance == null) {
                this.mInstance = this.mProducer.produce(packageManagerServiceInjector, packageManagerService);
            }
            return this.mInstance;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageManagerServiceInjector(Context context, PackageManagerTracedLock packageManagerTracedLock, Installer installer, Object obj, PackageAbiHelper packageAbiHelper, Handler handler, List<ScanPartition> list, Producer<ComponentResolver> producer, Producer<PermissionManagerServiceInternal> producer2, Producer<UserManagerService> producer3, Producer<Settings> producer4, Producer<AppsFilterImpl> producer5, Producer<PlatformCompat> producer6, Producer<SystemConfig> producer7, Producer<PackageDexOptimizer> producer8, Producer<DexManager> producer9, Producer<DynamicCodeLogger> producer10, Producer<ArtManagerService> producer11, Producer<ApexManager> producer12, Producer<ViewCompiler> producer13, Producer<IncrementalManager> producer14, Producer<DefaultAppProvider> producer15, Producer<DisplayMetrics> producer16, Producer<PackageParser2> producer17, Producer<PackageParser2> producer18, Producer<PackageParser2> producer19, Producer<PackageInstallerService> producer20, ProducerWithArgument<InstantAppResolverConnection, ComponentName> producerWithArgument, Producer<ModuleInfoProvider> producer21, Producer<LegacyPermissionManagerInternal> producer22, Producer<DomainVerificationManagerInternal> producer23, Producer<Handler> producer24, SystemWrapper systemWrapper, ServiceProducer serviceProducer, ServiceProducer serviceProducer2, Producer<BackgroundDexOptService> producer25, Producer<IBackupManager> producer26, Producer<SharedLibrariesImpl> producer27, Producer<CrossProfileIntentFilterHelper> producer28, Producer<UpdateOwnershipHelper> producer29) {
        this.mContext = context;
        this.mLock = packageManagerTracedLock;
        this.mInstaller = installer;
        this.mAbiHelper = packageAbiHelper;
        this.mInstallLock = obj;
        this.mBackgroundHandler = handler;
        this.mBackgroundExecutor = new HandlerExecutor(handler);
        this.mSystemPartitions = list;
        this.mComponentResolverProducer = new Singleton<>(producer);
        this.mPermissionManagerServiceProducer = new Singleton<>(producer2);
        this.mUserManagerProducer = new Singleton<>(producer3);
        this.mSettingsProducer = new Singleton<>(producer4);
        this.mAppsFilterProducer = new Singleton<>(producer5);
        this.mPlatformCompatProducer = new Singleton<>(producer6);
        this.mSystemConfigProducer = new Singleton<>(producer7);
        this.mPackageDexOptimizerProducer = new Singleton<>(producer8);
        this.mDexManagerProducer = new Singleton<>(producer9);
        this.mDynamicCodeLoggerProducer = new Singleton<>(producer10);
        this.mArtManagerServiceProducer = new Singleton<>(producer11);
        this.mApexManagerProducer = new Singleton<>(producer12);
        this.mViewCompilerProducer = new Singleton<>(producer13);
        this.mIncrementalManagerProducer = new Singleton<>(producer14);
        this.mDefaultAppProviderProducer = new Singleton<>(producer15);
        this.mDisplayMetricsProducer = new Singleton<>(producer16);
        this.mScanningCachingPackageParserProducer = producer17;
        this.mScanningPackageParserProducer = producer18;
        this.mPreparingPackageParserProducer = producer19;
        this.mPackageInstallerServiceProducer = new Singleton<>(producer20);
        this.mInstantAppResolverConnectionProducer = producerWithArgument;
        this.mModuleInfoProviderProducer = new Singleton<>(producer21);
        this.mLegacyPermissionManagerInternalProducer = new Singleton<>(producer22);
        this.mSystemWrapper = systemWrapper;
        this.mGetLocalServiceProducer = serviceProducer;
        this.mGetSystemServiceProducer = serviceProducer2;
        this.mDomainVerificationManagerInternalProducer = new Singleton<>(producer23);
        this.mHandlerProducer = new Singleton<>(producer24);
        this.mBackgroundDexOptService = new Singleton<>(producer25);
        this.mIBackupManager = new Singleton<>(producer26);
        this.mSharedLibrariesProducer = new Singleton<>(producer27);
        this.mCrossProfileIntentFilterHelperProducer = new Singleton<>(producer28);
        this.mUpdateOwnershipHelperProducer = new Singleton<>(producer29);
    }

    public void bootstrap(PackageManagerService packageManagerService) {
        this.mPackageManager = packageManagerService;
    }

    public UserManagerInternal getUserManagerInternal() {
        return getUserManagerService().getInternalForInjectorOnly();
    }

    public PackageAbiHelper getAbiHelper() {
        return this.mAbiHelper;
    }

    public Object getInstallLock() {
        return this.mInstallLock;
    }

    public List<ScanPartition> getSystemPartitions() {
        return this.mSystemPartitions;
    }

    public UserManagerService getUserManagerService() {
        return this.mUserManagerProducer.get(this, this.mPackageManager);
    }

    public PackageManagerTracedLock getLock() {
        return this.mLock;
    }

    public CrossProfileIntentFilterHelper getCrossProfileIntentFilterHelper() {
        return this.mCrossProfileIntentFilterHelperProducer.get(this, this.mPackageManager);
    }

    public Installer getInstaller() {
        return this.mInstaller;
    }

    public ComponentResolver getComponentResolver() {
        return this.mComponentResolverProducer.get(this, this.mPackageManager);
    }

    public PermissionManagerServiceInternal getPermissionManagerServiceInternal() {
        return this.mPermissionManagerServiceProducer.get(this, this.mPackageManager);
    }

    public Context getContext() {
        return this.mContext;
    }

    public Settings getSettings() {
        return this.mSettingsProducer.get(this, this.mPackageManager);
    }

    public AppsFilterImpl getAppsFilter() {
        return this.mAppsFilterProducer.get(this, this.mPackageManager);
    }

    public PlatformCompat getCompatibility() {
        return this.mPlatformCompatProducer.get(this, this.mPackageManager);
    }

    public SystemConfig getSystemConfig() {
        return this.mSystemConfigProducer.get(this, this.mPackageManager);
    }

    public PackageDexOptimizer getPackageDexOptimizer() {
        return this.mPackageDexOptimizerProducer.get(this, this.mPackageManager);
    }

    public DexManager getDexManager() {
        return this.mDexManagerProducer.get(this, this.mPackageManager);
    }

    public DynamicCodeLogger getDynamicCodeLogger() {
        return this.mDynamicCodeLoggerProducer.get(this, this.mPackageManager);
    }

    public ArtManagerService getArtManagerService() {
        return this.mArtManagerServiceProducer.get(this, this.mPackageManager);
    }

    public ApexManager getApexManager() {
        return this.mApexManagerProducer.get(this, this.mPackageManager);
    }

    public ViewCompiler getViewCompiler() {
        return this.mViewCompilerProducer.get(this, this.mPackageManager);
    }

    public Handler getBackgroundHandler() {
        return this.mBackgroundHandler;
    }

    public Executor getBackgroundExecutor() {
        return this.mBackgroundExecutor;
    }

    public DisplayMetrics getDisplayMetrics() {
        return this.mDisplayMetricsProducer.get(this, this.mPackageManager);
    }

    public <T> T getLocalService(Class<T> cls) {
        return (T) this.mGetLocalServiceProducer.produce(cls);
    }

    public <T> T getSystemService(Class<T> cls) {
        return (T) this.mGetSystemServiceProducer.produce(cls);
    }

    public SystemWrapper getSystemWrapper() {
        return this.mSystemWrapper;
    }

    public IncrementalManager getIncrementalManager() {
        return this.mIncrementalManagerProducer.get(this, this.mPackageManager);
    }

    public DefaultAppProvider getDefaultAppProvider() {
        return this.mDefaultAppProviderProducer.get(this, this.mPackageManager);
    }

    public PackageParser2 getScanningCachingPackageParser() {
        return this.mScanningCachingPackageParserProducer.produce(this, this.mPackageManager);
    }

    public PackageParser2 getScanningPackageParser() {
        return this.mScanningPackageParserProducer.produce(this, this.mPackageManager);
    }

    public PackageParser2 getPreparingPackageParser() {
        return this.mPreparingPackageParserProducer.produce(this, this.mPackageManager);
    }

    public PackageInstallerService getPackageInstallerService() {
        return this.mPackageInstallerServiceProducer.get(this, this.mPackageManager);
    }

    public InstantAppResolverConnection getInstantAppResolverConnection(ComponentName componentName) {
        return this.mInstantAppResolverConnectionProducer.produce(this, this.mPackageManager, componentName);
    }

    public ModuleInfoProvider getModuleInfoProvider() {
        return this.mModuleInfoProviderProducer.get(this, this.mPackageManager);
    }

    public LegacyPermissionManagerInternal getLegacyPermissionManagerInternal() {
        return this.mLegacyPermissionManagerInternalProducer.get(this, this.mPackageManager);
    }

    public DomainVerificationManagerInternal getDomainVerificationManagerInternal() {
        return this.mDomainVerificationManagerInternalProducer.get(this, this.mPackageManager);
    }

    public Handler getHandler() {
        return this.mHandlerProducer.get(this, this.mPackageManager);
    }

    public ActivityManagerInternal getActivityManagerInternal() {
        return (ActivityManagerInternal) getLocalService(ActivityManagerInternal.class);
    }

    public BackgroundDexOptService getBackgroundDexOptService() {
        return this.mBackgroundDexOptService.get(this, this.mPackageManager);
    }

    public IBackupManager getIBackupManager() {
        return this.mIBackupManager.get(this, this.mPackageManager);
    }

    public SharedLibrariesImpl getSharedLibrariesImpl() {
        return this.mSharedLibrariesProducer.get(this, this.mPackageManager);
    }

    public UpdateOwnershipHelper getUpdateOwnershipHelper() {
        return this.mUpdateOwnershipHelperProducer.get(this, this.mPackageManager);
    }
}
