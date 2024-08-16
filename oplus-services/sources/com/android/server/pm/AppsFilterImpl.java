package com.android.server.pm;

import android.R;
import android.content.pm.PackageManagerInternal;
import android.content.pm.SigningDetails;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseSetArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.FgThread;
import com.android.server.compat.CompatChange;
import com.android.server.job.controllers.JobStatus;
import com.android.server.om.OverlayReferenceMapper;
import com.android.server.pm.AppsFilterImpl;
import com.android.server.pm.AppsFilterUtils;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.SharedUserApi;
import com.android.server.pm.pkg.component.ParsedInstrumentation;
import com.android.server.pm.pkg.component.ParsedPermission;
import com.android.server.pm.pkg.component.ParsedUsesPermission;
import com.android.server.utils.Snappable;
import com.android.server.utils.SnapshotCache;
import com.android.server.utils.Watchable;
import com.android.server.utils.WatchableImpl;
import com.android.server.utils.WatchedArraySet;
import com.android.server.utils.WatchedSparseBooleanMatrix;
import com.android.server.utils.WatchedSparseSetArray;
import com.android.server.utils.Watcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class AppsFilterImpl extends AppsFilterLocked implements Watchable, Snappable {

    @GuardedBy({"mQueryableViaUsesPermissionLock"})
    private final ArrayMap<String, ArraySet<Integer>> mPermissionToUids;
    private final SnapshotCache<AppsFilterSnapshot> mSnapshot;

    @GuardedBy({"mQueryableViaUsesPermissionLock"})
    private final ArrayMap<String, ArraySet<Integer>> mUsesPermissionToUids;
    private final WatchableImpl mWatchable = new WatchableImpl();

    @Override // com.android.server.utils.Watchable
    public void registerObserver(Watcher watcher) {
        this.mWatchable.registerObserver(watcher);
    }

    @Override // com.android.server.utils.Watchable
    public void unregisterObserver(Watcher watcher) {
        this.mWatchable.unregisterObserver(watcher);
    }

    @Override // com.android.server.utils.Watchable
    public boolean isRegisteredObserver(Watcher watcher) {
        return this.mWatchable.isRegisteredObserver(watcher);
    }

    @Override // com.android.server.utils.Watchable
    public void dispatchChange(Watchable watchable) {
        this.mWatchable.dispatchChange(watchable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onChanged() {
        dispatchChange(this);
    }

    private void invalidateCache(String str) {
        if (this.mCacheValid.compareAndSet(true, false)) {
            Slog.i("AppsFilter", "Invalidating cache: " + str);
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    AppsFilterImpl(FeatureConfig featureConfig, String[] strArr, boolean z, OverlayReferenceMapper.Provider provider, Handler handler) {
        this.mFeatureConfig = featureConfig;
        this.mForceQueryableByDevicePackageNames = strArr;
        this.mSystemAppsQueryable = z;
        this.mOverlayReferenceMapper = new OverlayReferenceMapper(true, provider);
        this.mHandler = handler;
        this.mShouldFilterCache = new WatchedSparseBooleanMatrix();
        WatchedSparseBooleanMatrix watchedSparseBooleanMatrix = this.mShouldFilterCache;
        this.mShouldFilterCacheSnapshot = new SnapshotCache.Auto(watchedSparseBooleanMatrix, watchedSparseBooleanMatrix, "AppsFilter.mShouldFilterCache");
        this.mImplicitlyQueryable = new WatchedSparseSetArray<>();
        WatchedSparseSetArray<Integer> watchedSparseSetArray = this.mImplicitlyQueryable;
        this.mImplicitQueryableSnapshot = new SnapshotCache.Auto(watchedSparseSetArray, watchedSparseSetArray, "AppsFilter.mImplicitlyQueryable");
        this.mRetainedImplicitlyQueryable = new WatchedSparseSetArray<>();
        WatchedSparseSetArray<Integer> watchedSparseSetArray2 = this.mRetainedImplicitlyQueryable;
        this.mRetainedImplicitlyQueryableSnapshot = new SnapshotCache.Auto(watchedSparseSetArray2, watchedSparseSetArray2, "AppsFilter.mRetainedImplicitlyQueryable");
        this.mQueriesViaPackage = new WatchedSparseSetArray<>();
        WatchedSparseSetArray<Integer> watchedSparseSetArray3 = this.mQueriesViaPackage;
        this.mQueriesViaPackageSnapshot = new SnapshotCache.Auto(watchedSparseSetArray3, watchedSparseSetArray3, "AppsFilter.mQueriesViaPackage");
        this.mQueriesViaComponent = new WatchedSparseSetArray<>();
        WatchedSparseSetArray<Integer> watchedSparseSetArray4 = this.mQueriesViaComponent;
        this.mQueriesViaComponentSnapshot = new SnapshotCache.Auto(watchedSparseSetArray4, watchedSparseSetArray4, "AppsFilter.mQueriesViaComponent");
        this.mQueryableViaUsesLibrary = new WatchedSparseSetArray<>();
        WatchedSparseSetArray<Integer> watchedSparseSetArray5 = this.mQueryableViaUsesLibrary;
        this.mQueryableViaUsesLibrarySnapshot = new SnapshotCache.Auto(watchedSparseSetArray5, watchedSparseSetArray5, "AppsFilter.mQueryableViaUsesLibrary");
        this.mQueryableViaUsesPermission = new WatchedSparseSetArray<>();
        WatchedSparseSetArray<Integer> watchedSparseSetArray6 = this.mQueryableViaUsesPermission;
        this.mQueryableViaUsesPermissionSnapshot = new SnapshotCache.Auto(watchedSparseSetArray6, watchedSparseSetArray6, "AppsFilter.mQueryableViaUsesPermission");
        this.mForceQueryable = new WatchedArraySet<>();
        WatchedArraySet<Integer> watchedArraySet = this.mForceQueryable;
        this.mForceQueryableSnapshot = new SnapshotCache.Auto(watchedArraySet, watchedArraySet, "AppsFilter.mForceQueryable");
        this.mProtectedBroadcasts = new WatchedArraySet<>();
        WatchedArraySet<String> watchedArraySet2 = this.mProtectedBroadcasts;
        this.mProtectedBroadcastsSnapshot = new SnapshotCache.Auto(watchedArraySet2, watchedArraySet2, "AppsFilter.mProtectedBroadcasts");
        this.mPermissionToUids = new ArrayMap<>();
        this.mUsesPermissionToUids = new ArrayMap<>();
        this.mSnapshot = new SnapshotCache<AppsFilterSnapshot>(this, this) { // from class: com.android.server.pm.AppsFilterImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.server.utils.SnapshotCache
            public AppsFilterSnapshot createSnapshot() {
                return new AppsFilterSnapshotImpl(AppsFilterImpl.this);
            }
        };
        readCacheEnabledSysProp();
        SystemProperties.addChangeCallback(new Runnable() { // from class: com.android.server.pm.AppsFilterImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AppsFilterImpl.this.readCacheEnabledSysProp();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readCacheEnabledSysProp() {
        this.mCacheEnabled = SystemProperties.getBoolean("debug.pm.use_app_filter_cache", true);
    }

    @Override // com.android.server.utils.Snappable
    public AppsFilterSnapshot snapshot() {
        return this.mSnapshot.snapshot();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class FeatureConfigImpl implements FeatureConfig, CompatChange.ChangeListener {
        private static final String FILTERING_ENABLED_NAME = "package_query_filtering_enabled";
        private AppsFilterImpl mAppsFilter;

        @GuardedBy({"mDisabledPackages"})
        private final ArraySet<String> mDisabledPackages;
        private volatile boolean mFeatureEnabled;
        private final PackageManagerServiceInjector mInjector;
        private SparseBooleanArray mLoggingEnabled;
        private final PackageManagerInternal mPmInternal;

        private FeatureConfigImpl(PackageManagerInternal packageManagerInternal, PackageManagerServiceInjector packageManagerServiceInjector) {
            this.mFeatureEnabled = true;
            this.mDisabledPackages = new ArraySet<>();
            this.mLoggingEnabled = null;
            this.mPmInternal = packageManagerInternal;
            this.mInjector = packageManagerServiceInjector;
        }

        FeatureConfigImpl(FeatureConfigImpl featureConfigImpl) {
            this.mFeatureEnabled = true;
            ArraySet<String> arraySet = new ArraySet<>();
            this.mDisabledPackages = arraySet;
            this.mLoggingEnabled = null;
            this.mInjector = null;
            this.mPmInternal = null;
            this.mFeatureEnabled = featureConfigImpl.mFeatureEnabled;
            synchronized (featureConfigImpl.mDisabledPackages) {
                arraySet.addAll((ArraySet<? extends String>) featureConfigImpl.mDisabledPackages);
            }
            this.mLoggingEnabled = featureConfigImpl.mLoggingEnabled;
        }

        public void setAppsFilter(AppsFilterImpl appsFilterImpl) {
            this.mAppsFilter = appsFilterImpl;
        }

        @Override // com.android.server.pm.FeatureConfig
        public void onSystemReady() {
            this.mFeatureEnabled = DeviceConfig.getBoolean("package_manager_service", FILTERING_ENABLED_NAME, true);
            DeviceConfig.addOnPropertiesChangedListener("package_manager_service", FgThread.getExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.pm.AppsFilterImpl$FeatureConfigImpl$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                    AppsFilterImpl.FeatureConfigImpl.this.lambda$onSystemReady$0(properties);
                }
            });
            this.mInjector.getCompatibility().registerListener(135549675L, this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSystemReady$0(DeviceConfig.Properties properties) {
            if (properties.getKeyset().contains(FILTERING_ENABLED_NAME)) {
                synchronized (this) {
                    this.mFeatureEnabled = properties.getBoolean(FILTERING_ENABLED_NAME, true);
                }
            }
        }

        @Override // com.android.server.pm.FeatureConfig
        public boolean isGloballyEnabled() {
            return this.mFeatureEnabled;
        }

        @Override // com.android.server.pm.FeatureConfig
        public boolean packageIsEnabled(AndroidPackage androidPackage) {
            boolean z;
            synchronized (this.mDisabledPackages) {
                z = !this.mDisabledPackages.contains(androidPackage.getPackageName());
            }
            return z;
        }

        @Override // com.android.server.pm.FeatureConfig
        public boolean isLoggingEnabled(int i) {
            SparseBooleanArray sparseBooleanArray = this.mLoggingEnabled;
            return sparseBooleanArray != null && sparseBooleanArray.indexOfKey(i) >= 0;
        }

        @Override // com.android.server.pm.FeatureConfig
        public void enableLogging(int i, boolean z) {
            int indexOfKey;
            if (z) {
                if (this.mLoggingEnabled == null) {
                    this.mLoggingEnabled = new SparseBooleanArray();
                }
                this.mLoggingEnabled.put(i, true);
                return;
            }
            SparseBooleanArray sparseBooleanArray = this.mLoggingEnabled;
            if (sparseBooleanArray == null || (indexOfKey = sparseBooleanArray.indexOfKey(i)) < 0) {
                return;
            }
            this.mLoggingEnabled.removeAt(indexOfKey);
            if (this.mLoggingEnabled.size() == 0) {
                this.mLoggingEnabled = null;
            }
        }

        public void onCompatChange(String str) {
            Computer computer = (Computer) this.mPmInternal.snapshot();
            AndroidPackage androidPackage = computer.getPackage(str);
            if (androidPackage == null) {
                return;
            }
            long currentTimeMicro = SystemClock.currentTimeMicro();
            updateEnabledState(androidPackage);
            this.mAppsFilter.updateShouldFilterCacheForPackage(computer, str);
            this.mAppsFilter.logCacheUpdated(4, SystemClock.currentTimeMicro() - currentTimeMicro, computer.getUserInfos().length, computer.getPackageStates().size(), androidPackage.getUid());
        }

        private void updateEnabledState(AndroidPackage androidPackage) {
            boolean isChangeEnabledInternalNoLogging = this.mInjector.getCompatibility().isChangeEnabledInternalNoLogging(135549675L, AndroidPackageUtils.generateAppInfoWithoutState(androidPackage));
            synchronized (this.mDisabledPackages) {
                if (isChangeEnabledInternalNoLogging) {
                    this.mDisabledPackages.remove(androidPackage.getPackageName());
                } else {
                    this.mDisabledPackages.add(androidPackage.getPackageName());
                }
            }
            AppsFilterImpl appsFilterImpl = this.mAppsFilter;
            if (appsFilterImpl != null) {
                appsFilterImpl.onChanged();
            }
        }

        @Override // com.android.server.pm.FeatureConfig
        public void updatePackageState(PackageStateInternal packageStateInternal, boolean z) {
            enableLogging(packageStateInternal.getAppId(), (packageStateInternal.getPkg() == null || z || (!packageStateInternal.getPkg().isTestOnly() && !packageStateInternal.getPkg().isDebuggable())) ? false : true);
            if (z) {
                synchronized (this.mDisabledPackages) {
                    this.mDisabledPackages.remove(packageStateInternal.getPackageName());
                }
                AppsFilterImpl appsFilterImpl = this.mAppsFilter;
                if (appsFilterImpl != null) {
                    appsFilterImpl.onChanged();
                    return;
                }
                return;
            }
            if (packageStateInternal.getPkg() != null) {
                updateEnabledState(packageStateInternal.getPkg());
            }
        }

        @Override // com.android.server.pm.FeatureConfig
        public FeatureConfig snapshot() {
            return new FeatureConfigImpl(this);
        }
    }

    public static AppsFilterImpl create(PackageManagerServiceInjector packageManagerServiceInjector, PackageManagerInternal packageManagerInternal) {
        String[] strArr;
        boolean z = packageManagerServiceInjector.getContext().getResources().getBoolean(17891700);
        FeatureConfigImpl featureConfigImpl = new FeatureConfigImpl(packageManagerInternal, packageManagerServiceInjector);
        if (z) {
            strArr = new String[0];
        } else {
            String[] stringArray = packageManagerServiceInjector.getContext().getResources().getStringArray(R.array.config_serialPorts);
            for (int i = 0; i < stringArray.length; i++) {
                stringArray[i] = stringArray[i].intern();
            }
            strArr = stringArray;
        }
        AppsFilterImpl appsFilterImpl = new AppsFilterImpl(featureConfigImpl, strArr, z, null, packageManagerServiceInjector.getHandler());
        featureConfigImpl.setAppsFilter(appsFilterImpl);
        return appsFilterImpl;
    }

    public FeatureConfig getFeatureConfig() {
        return this.mFeatureConfig;
    }

    public boolean grantImplicitAccess(int i, int i2, boolean z) {
        boolean add;
        if (i == i2) {
            return false;
        }
        synchronized (this.mImplicitlyQueryableLock) {
            if (z) {
                add = this.mRetainedImplicitlyQueryable.add(i, Integer.valueOf(i2));
            } else {
                add = this.mImplicitlyQueryable.add(i, Integer.valueOf(i2));
            }
        }
        if (this.mCacheReady) {
            synchronized (this.mCacheLock) {
                this.mShouldFilterCache.put(i, i2, false);
            }
        } else if (add) {
            invalidateCache("grantImplicitAccess: " + i + " -> " + i2);
        }
        if (add) {
            onChanged();
        }
        return add;
    }

    public void onSystemReady(PackageManagerInternal packageManagerInternal) {
        this.mOverlayReferenceMapper.rebuildIfDeferred();
        this.mFeatureConfig.onSystemReady();
        updateEntireShouldFilterCacheAsync(packageManagerInternal, 1);
    }

    public void addPackage(Computer computer, PackageStateInternal packageStateInternal, boolean z, boolean z2) {
        int i;
        long currentTimeMicro = SystemClock.currentTimeMicro();
        int i2 = z ? 3 : 1;
        if (z) {
            try {
                removePackageInternal(computer, packageStateInternal, true, z2);
            } finally {
                onChanged();
            }
        }
        ArrayMap<String, ? extends PackageStateInternal> packageStates = computer.getPackageStates();
        UserInfo[] userInfos = computer.getUserInfos();
        ArraySet<String> addPackageInternal = addPackageInternal(packageStateInternal, packageStates);
        if (this.mCacheReady) {
            synchronized (this.mCacheLock) {
                try {
                    try {
                        updateShouldFilterCacheForPackage(computer, null, packageStateInternal, packageStates, userInfos, -1, packageStates.size());
                        if (addPackageInternal != null) {
                            int i3 = 0;
                            while (i3 < addPackageInternal.size()) {
                                PackageStateInternal packageStateInternal2 = packageStates.get(addPackageInternal.valueAt(i3));
                                if (packageStateInternal2 == null) {
                                    i = i3;
                                } else {
                                    i = i3;
                                    updateShouldFilterCacheForPackage(computer, null, packageStateInternal2, packageStates, userInfos, -1, packageStates.size());
                                }
                                i3 = i + 1;
                            }
                        }
                        logCacheUpdated(i2, SystemClock.currentTimeMicro() - currentTimeMicro, userInfos.length, packageStates.size(), packageStateInternal.getAppId());
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        } else {
            invalidateCache("addPackage: " + packageStateInternal.getPackageName());
        }
    }

    private ArraySet<String> addPackageInternal(PackageStateInternal packageStateInternal, ArrayMap<String, ? extends PackageStateInternal> arrayMap) {
        boolean z;
        boolean contains;
        SigningDetails signingDetails;
        boolean z2;
        if (PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageStateInternal.getPackageName())) {
            this.mSystemSigningDetails = packageStateInternal.getSigningDetails();
            for (PackageStateInternal packageStateInternal2 : arrayMap.values()) {
                if (isSystemSigned(this.mSystemSigningDetails, packageStateInternal2)) {
                    synchronized (this.mForceQueryableLock) {
                        this.mForceQueryable.add(Integer.valueOf(packageStateInternal2.getAppId()));
                    }
                }
            }
        }
        AndroidPackageInternal pkg = packageStateInternal.getPkg();
        if (pkg == null) {
            return null;
        }
        List<String> protectedBroadcasts = pkg.getProtectedBroadcasts();
        if (protectedBroadcasts.size() != 0) {
            synchronized (this.mProtectedBroadcastsLock) {
                int size = this.mProtectedBroadcasts.size();
                this.mProtectedBroadcasts.addAll(protectedBroadcasts);
                z2 = this.mProtectedBroadcasts.size() != size;
            }
            if (z2) {
                this.mQueriesViaComponentRequireRecompute.set(true);
            }
        }
        synchronized (this.mForceQueryableLock) {
            if (!this.mForceQueryable.contains(Integer.valueOf(packageStateInternal.getAppId())) && !packageStateInternal.isForceQueryableOverride() && (!packageStateInternal.isSystem() || (!this.mSystemAppsQueryable && !pkg.isForceQueryable() && !ArrayUtils.contains(this.mForceQueryableByDevicePackageNames, pkg.getPackageName())))) {
                z = false;
                if (!z || ((signingDetails = this.mSystemSigningDetails) != null && isSystemSigned(signingDetails, packageStateInternal))) {
                    this.mForceQueryable.add(Integer.valueOf(packageStateInternal.getAppId()));
                }
            }
            z = true;
            if (!z) {
            }
            this.mForceQueryable.add(Integer.valueOf(packageStateInternal.getAppId()));
        }
        if (!pkg.getUsesPermissions().isEmpty()) {
            synchronized (this.mQueryableViaUsesPermissionLock) {
                Iterator<ParsedUsesPermission> it = pkg.getUsesPermissions().iterator();
                while (it.hasNext()) {
                    String name = it.next().getName();
                    if (this.mPermissionToUids.containsKey(name)) {
                        ArraySet<Integer> arraySet = this.mPermissionToUids.get(name);
                        for (int i = 0; i < arraySet.size(); i++) {
                            int intValue = arraySet.valueAt(i).intValue();
                            if (intValue != packageStateInternal.getAppId()) {
                                this.mQueryableViaUsesPermission.add(packageStateInternal.getAppId(), Integer.valueOf(intValue));
                            }
                        }
                    }
                    if (!this.mUsesPermissionToUids.containsKey(name)) {
                        this.mUsesPermissionToUids.put(name, new ArraySet<>());
                    }
                    this.mUsesPermissionToUids.get(name).add(Integer.valueOf(packageStateInternal.getAppId()));
                }
            }
        }
        if (!pkg.getPermissions().isEmpty()) {
            synchronized (this.mQueryableViaUsesPermissionLock) {
                Iterator<ParsedPermission> it2 = pkg.getPermissions().iterator();
                while (it2.hasNext()) {
                    String name2 = it2.next().getName();
                    if (this.mUsesPermissionToUids.containsKey(name2)) {
                        ArraySet<Integer> arraySet2 = this.mUsesPermissionToUids.get(name2);
                        for (int i2 = 0; i2 < arraySet2.size(); i2++) {
                            int intValue2 = arraySet2.valueAt(i2).intValue();
                            if (intValue2 != packageStateInternal.getAppId()) {
                                this.mQueryableViaUsesPermission.add(intValue2, Integer.valueOf(packageStateInternal.getAppId()));
                            }
                        }
                    }
                    if (!this.mPermissionToUids.containsKey(name2)) {
                        this.mPermissionToUids.put(name2, new ArraySet<>());
                    }
                    this.mPermissionToUids.get(name2).add(Integer.valueOf(packageStateInternal.getAppId()));
                }
            }
        }
        for (int size2 = arrayMap.size() - 1; size2 >= 0; size2--) {
            PackageStateInternal valueAt = arrayMap.valueAt(size2);
            if (valueAt.getAppId() != packageStateInternal.getAppId() && valueAt.getPkg() != null) {
                AndroidPackageInternal pkg2 = valueAt.getPkg();
                if (!z) {
                    if (!this.mQueriesViaComponentRequireRecompute.get() && AppsFilterUtils.canQueryViaComponents(pkg2, pkg, this.mProtectedBroadcasts)) {
                        synchronized (this.mQueriesViaComponentLock) {
                            this.mQueriesViaComponent.add(valueAt.getAppId(), Integer.valueOf(packageStateInternal.getAppId()));
                        }
                    }
                    if (AppsFilterUtils.canQueryViaPackage(pkg2, pkg) || AppsFilterUtils.canQueryAsInstaller(valueAt, pkg) || AppsFilterUtils.canQueryAsUpdateOwner(valueAt, pkg)) {
                        synchronized (this.mQueriesViaPackageLock) {
                            this.mQueriesViaPackage.add(valueAt.getAppId(), Integer.valueOf(packageStateInternal.getAppId()));
                        }
                    }
                    if (AppsFilterUtils.canQueryViaUsesLibrary(pkg2, pkg)) {
                        synchronized (this.mQueryableViaUsesLibraryLock) {
                            this.mQueryableViaUsesLibrary.add(valueAt.getAppId(), Integer.valueOf(packageStateInternal.getAppId()));
                        }
                    }
                }
                synchronized (this.mForceQueryableLock) {
                    contains = this.mForceQueryable.contains(Integer.valueOf(valueAt.getAppId()));
                }
                if (!contains) {
                    if (!this.mQueriesViaComponentRequireRecompute.get() && AppsFilterUtils.canQueryViaComponents(pkg, pkg2, this.mProtectedBroadcasts)) {
                        synchronized (this.mQueriesViaComponentLock) {
                            this.mQueriesViaComponent.add(packageStateInternal.getAppId(), Integer.valueOf(valueAt.getAppId()));
                        }
                    }
                    if (AppsFilterUtils.canQueryViaPackage(pkg, pkg2) || AppsFilterUtils.canQueryAsInstaller(packageStateInternal, pkg2) || AppsFilterUtils.canQueryAsUpdateOwner(packageStateInternal, pkg2)) {
                        synchronized (this.mQueriesViaPackageLock) {
                            this.mQueriesViaPackage.add(packageStateInternal.getAppId(), Integer.valueOf(valueAt.getAppId()));
                        }
                    }
                    if (AppsFilterUtils.canQueryViaUsesLibrary(pkg, pkg2)) {
                        synchronized (this.mQueryableViaUsesLibraryLock) {
                            this.mQueryableViaUsesLibrary.add(packageStateInternal.getAppId(), Integer.valueOf(valueAt.getAppId()));
                        }
                    }
                }
                if (packageStateInternal.getPkg() != null && valueAt.getPkg() != null && (pkgInstruments(packageStateInternal.getPkg(), valueAt.getPkg()) || pkgInstruments(valueAt.getPkg(), packageStateInternal.getPkg()))) {
                    synchronized (this.mQueriesViaPackageLock) {
                        this.mQueriesViaPackage.add(packageStateInternal.getAppId(), Integer.valueOf(valueAt.getAppId()));
                        this.mQueriesViaPackage.add(valueAt.getAppId(), Integer.valueOf(packageStateInternal.getAppId()));
                    }
                }
            }
        }
        int size3 = arrayMap.size();
        ArrayMap arrayMap2 = new ArrayMap(size3);
        for (int i3 = 0; i3 < size3; i3++) {
            PackageStateInternal valueAt2 = arrayMap.valueAt(i3);
            if (valueAt2.getPkg() != null) {
                arrayMap2.put(valueAt2.getPackageName(), valueAt2.getPkg());
            }
        }
        ArraySet<String> addPkg = this.mOverlayReferenceMapper.addPkg(packageStateInternal.getPkg(), arrayMap2);
        this.mFeatureConfig.updatePackageState(packageStateInternal, false);
        return addPkg;
    }

    private void removeAppIdFromVisibilityCache(int i) {
        synchronized (this.mCacheLock) {
            int i2 = 0;
            while (i2 < this.mShouldFilterCache.size()) {
                if (UserHandle.getAppId(this.mShouldFilterCache.keyAt(i2)) == i) {
                    this.mShouldFilterCache.removeAt(i2);
                    i2--;
                }
                i2++;
            }
        }
    }

    private void updateEntireShouldFilterCache(Computer computer, int i) {
        ArrayMap<String, ? extends PackageStateInternal> packageStates = computer.getPackageStates();
        UserInfo[] userInfos = computer.getUserInfos();
        int i2 = 0;
        while (true) {
            if (i2 >= userInfos.length) {
                i = -10000;
                break;
            } else if (i == userInfos[i2].id) {
                break;
            } else {
                i2++;
            }
        }
        if (i == -10000) {
            Slog.e("AppsFilter", "We encountered a new user that isn't a member of known users, updating the whole cache");
            i = -1;
        }
        updateEntireShouldFilterCacheInner(computer, packageStates, userInfos, i);
        onChanged();
    }

    private void updateEntireShouldFilterCacheInner(Computer computer, ArrayMap<String, ? extends PackageStateInternal> arrayMap, UserInfo[] userInfoArr, int i) {
        synchronized (this.mCacheLock) {
            if (i == -1) {
                this.mShouldFilterCache.clear();
            }
            this.mShouldFilterCache.setCapacity(userInfoArr.length * arrayMap.size());
            for (int size = arrayMap.size() - 1; size >= 0; size--) {
                updateShouldFilterCacheForPackage(computer, null, arrayMap.valueAt(size), arrayMap, userInfoArr, i, size);
            }
        }
    }

    private void updateEntireShouldFilterCacheAsync(PackageManagerInternal packageManagerInternal, int i) {
        updateEntireShouldFilterCacheAsync(packageManagerInternal, JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY, i);
    }

    private void updateEntireShouldFilterCacheAsync(final PackageManagerInternal packageManagerInternal, final long j, final int i) {
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.pm.AppsFilterImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AppsFilterImpl.this.lambda$updateEntireShouldFilterCacheAsync$0(packageManagerInternal, i, j);
            }
        }, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEntireShouldFilterCacheAsync$0(PackageManagerInternal packageManagerInternal, int i, long j) {
        if (this.mCacheValid.compareAndSet(false, true)) {
            long currentTimeMicro = SystemClock.currentTimeMicro();
            ArrayMap arrayMap = new ArrayMap();
            Computer computer = (Computer) packageManagerInternal.snapshot();
            ArrayMap<String, ? extends PackageStateInternal> packageStates = computer.getPackageStates();
            UserInfo[] userInfos = computer.getUserInfos();
            arrayMap.ensureCapacity(packageStates.size());
            UserInfo[][] userInfoArr = {userInfos};
            int size = packageStates.size();
            for (int i2 = 0; i2 < size; i2++) {
                arrayMap.put(packageStates.keyAt(i2), packageStates.valueAt(i2).getPkg());
            }
            updateEntireShouldFilterCacheInner(computer, packageStates, userInfoArr[0], -1);
            onChanged();
            logCacheRebuilt(i, SystemClock.currentTimeMicro() - currentTimeMicro, userInfos.length, packageStates.size());
            if (!this.mCacheValid.compareAndSet(true, true)) {
                Slog.i("AppsFilter", "Cache invalidated while building, retrying.");
                updateEntireShouldFilterCacheAsync(packageManagerInternal, Math.min(2 * j, JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY), i);
            } else {
                this.mCacheReady = true;
            }
        }
    }

    public void onUserCreated(Computer computer, int i) {
        if (this.mCacheReady) {
            long currentTimeMicro = SystemClock.currentTimeMicro();
            updateEntireShouldFilterCache(computer, i);
            logCacheRebuilt(2, SystemClock.currentTimeMicro() - currentTimeMicro, computer.getUserInfos().length, computer.getPackageStates().size());
        }
    }

    public void onUserDeleted(Computer computer, int i) {
        if (this.mCacheReady) {
            long currentTimeMicro = SystemClock.currentTimeMicro();
            removeShouldFilterCacheForUser(i);
            onChanged();
            logCacheRebuilt(3, SystemClock.currentTimeMicro() - currentTimeMicro, computer.getUserInfos().length, computer.getPackageStates().size());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShouldFilterCacheForPackage(Computer computer, String str) {
        if (this.mCacheReady) {
            ArrayMap<String, ? extends PackageStateInternal> packageStates = computer.getPackageStates();
            UserInfo[] userInfos = computer.getUserInfos();
            synchronized (this.mCacheLock) {
                updateShouldFilterCacheForPackage(computer, null, packageStates.get(str), packageStates, userInfos, -1, packageStates.size());
            }
            onChanged();
        }
    }

    @GuardedBy({"mCacheLock"})
    private void updateShouldFilterCacheForPackage(Computer computer, String str, PackageStateInternal packageStateInternal, ArrayMap<String, ? extends PackageStateInternal> arrayMap, UserInfo[] userInfoArr, int i, int i2) {
        for (int min = Math.min(i2, arrayMap.size() - 1); min >= 0; min--) {
            PackageStateInternal valueAt = arrayMap.valueAt(min);
            if (packageStateInternal.getAppId() != valueAt.getAppId() && packageStateInternal.getPackageName() != str && valueAt.getPackageName() != str) {
                if (i == -1) {
                    for (UserInfo userInfo : userInfoArr) {
                        updateShouldFilterCacheForUser(computer, packageStateInternal, userInfoArr, valueAt, userInfo.id);
                    }
                } else {
                    updateShouldFilterCacheForUser(computer, packageStateInternal, userInfoArr, valueAt, i);
                }
            }
        }
    }

    @GuardedBy({"mCacheLock"})
    private void updateShouldFilterCacheForUser(Computer computer, PackageStateInternal packageStateInternal, UserInfo[] userInfoArr, PackageStateInternal packageStateInternal2, int i) {
        for (UserInfo userInfo : userInfoArr) {
            int i2 = userInfo.id;
            int uid = UserHandle.getUid(i, packageStateInternal.getAppId());
            int uid2 = UserHandle.getUid(i2, packageStateInternal2.getAppId());
            boolean shouldFilterApplicationInternal = shouldFilterApplicationInternal(computer, uid, packageStateInternal, packageStateInternal2, i2);
            boolean shouldFilterApplicationInternal2 = shouldFilterApplicationInternal(computer, uid2, packageStateInternal2, packageStateInternal, i);
            this.mShouldFilterCache.put(uid, uid2, shouldFilterApplicationInternal);
            this.mShouldFilterCache.put(uid2, uid, shouldFilterApplicationInternal2);
        }
    }

    private void removeShouldFilterCacheForUser(int i) {
        synchronized (this.mCacheLock) {
            int[] keys = this.mShouldFilterCache.keys();
            int length = keys.length;
            int binarySearch = Arrays.binarySearch(keys, UserHandle.getUid(i, 0));
            if (binarySearch < 0) {
                binarySearch = ~binarySearch;
            }
            if (binarySearch < length && UserHandle.getUserId(keys[binarySearch]) == i) {
                int binarySearch2 = Arrays.binarySearch(keys, UserHandle.getUid(i + 1, 0) - 1);
                int i2 = binarySearch2 >= 0 ? binarySearch2 + 1 : ~binarySearch2;
                if (binarySearch < i2 && UserHandle.getUserId(keys[i2 - 1]) == i) {
                    this.mShouldFilterCache.removeRange(binarySearch, i2);
                    this.mShouldFilterCache.compact();
                    return;
                }
                Slog.w("AppsFilter", "Failed to remove should filter cache for user " + i + ", fromIndex=" + binarySearch + ", toIndex=" + i2);
                return;
            }
            Slog.w("AppsFilter", "Failed to remove should filter cache for user " + i + ", fromIndex=" + binarySearch);
        }
    }

    private static boolean isSystemSigned(SigningDetails signingDetails, PackageStateInternal packageStateInternal) {
        return packageStateInternal.isSystem() && packageStateInternal.getSigningDetails().signaturesMatchExactly(signingDetails);
    }

    private void collectProtectedBroadcasts(ArrayMap<String, ? extends PackageStateInternal> arrayMap, String str) {
        synchronized (this.mProtectedBroadcastsLock) {
            this.mProtectedBroadcasts.clear();
            for (int size = arrayMap.size() - 1; size >= 0; size--) {
                PackageStateInternal valueAt = arrayMap.valueAt(size);
                if (valueAt.getPkg() != null && !valueAt.getPkg().getPackageName().equals(str)) {
                    List<String> protectedBroadcasts = valueAt.getPkg().getProtectedBroadcasts();
                    if (!protectedBroadcasts.isEmpty()) {
                        this.mProtectedBroadcasts.addAll(protectedBroadcasts);
                    }
                }
            }
        }
    }

    @Override // com.android.server.pm.AppsFilterBase
    protected boolean isQueryableViaComponentWhenRequireRecompute(ArrayMap<String, ? extends PackageStateInternal> arrayMap, PackageStateInternal packageStateInternal, ArraySet<PackageStateInternal> arraySet, AndroidPackage androidPackage, int i, int i2) {
        recomputeComponentVisibility(arrayMap);
        return isQueryableViaComponent(i, i2);
    }

    private void recomputeComponentVisibility(ArrayMap<String, ? extends PackageStateInternal> arrayMap) {
        WatchedArraySet watchedArraySet;
        ArraySet arraySet;
        synchronized (this.mProtectedBroadcastsLock) {
            watchedArraySet = new WatchedArraySet(this.mProtectedBroadcasts);
        }
        synchronized (this.mForceQueryableLock) {
            arraySet = new ArraySet((ArraySet) this.mForceQueryable.untrackedStorage());
        }
        SparseSetArray<Integer> execute = new AppsFilterUtils.ParallelComputeComponentVisibility(arrayMap, arraySet, watchedArraySet).execute();
        synchronized (this.mQueriesViaComponentLock) {
            this.mQueriesViaComponent = new WatchedSparseSetArray<>(execute);
            WatchedSparseSetArray<Integer> watchedSparseSetArray = this.mQueriesViaComponent;
            this.mQueriesViaComponentSnapshot = new SnapshotCache.Auto(watchedSparseSetArray, watchedSparseSetArray, "AppsFilter.mQueriesViaComponent");
        }
        this.mQueriesViaComponentRequireRecompute.set(false);
        onChanged();
    }

    public void addPackage(Computer computer, PackageStateInternal packageStateInternal) {
        addPackage(computer, packageStateInternal, false, false);
    }

    public void removePackage(Computer computer, PackageStateInternal packageStateInternal) {
        long currentTimeMicro = SystemClock.currentTimeMicro();
        removePackageInternal(computer, packageStateInternal, false, false);
        logCacheUpdated(2, SystemClock.currentTimeMicro() - currentTimeMicro, computer.getUserInfos().length, computer.getPackageStates().size(), packageStateInternal.getAppId());
    }

    /* JADX WARN: Code restructure failed: missing block: B:143:0x02ad, code lost:
    
        if (r13 != null) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x02b3, code lost:
    
        if (r12 >= r13.size()) goto L207;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x02b5, code lost:
    
        r4 = r10.get(r13.valueAt(r12));
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x02c2, code lost:
    
        if (r4 != null) goto L132;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x02c5, code lost:
    
        r9 = r19.mCacheLock;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x02c7, code lost:
    
        monitor-enter(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x02ca, code lost:
    
        updateShouldFilterCacheForPackage(r20, null, r4, r10, r11, -1, r10.size());
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x02d7, code lost:
    
        monitor-exit(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x02d8, code lost:
    
        r12 = r12 + 1;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:114:? -> B:111:0x02ab). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void removePackageInternal(Computer computer, PackageStateInternal packageStateInternal, boolean z, boolean z2) {
        boolean z3;
        Object obj;
        int i;
        ArrayMap<String, ? extends PackageStateInternal> packageStates = computer.getPackageStates();
        UserInfo[] userInfos = computer.getUserInfos();
        int i2 = 0;
        if (!z || !z2) {
            synchronized (this.mImplicitlyQueryableLock) {
                for (UserInfo userInfo : userInfos) {
                    int uid = UserHandle.getUid(userInfo.id, packageStateInternal.getAppId());
                    this.mImplicitlyQueryable.remove(uid);
                    for (int size = this.mImplicitlyQueryable.size() - 1; size >= 0; size--) {
                        WatchedSparseSetArray<Integer> watchedSparseSetArray = this.mImplicitlyQueryable;
                        watchedSparseSetArray.remove(watchedSparseSetArray.keyAt(size), Integer.valueOf(uid));
                    }
                    if (!z) {
                        this.mRetainedImplicitlyQueryable.remove(uid);
                        for (int size2 = this.mRetainedImplicitlyQueryable.size() - 1; size2 >= 0; size2--) {
                            WatchedSparseSetArray<Integer> watchedSparseSetArray2 = this.mRetainedImplicitlyQueryable;
                            watchedSparseSetArray2.remove(watchedSparseSetArray2.keyAt(size2), Integer.valueOf(uid));
                        }
                    }
                }
            }
        }
        if (!this.mQueriesViaComponentRequireRecompute.get()) {
            synchronized (this.mQueriesViaComponentLock) {
                this.mQueriesViaComponent.remove(packageStateInternal.getAppId());
                for (int size3 = this.mQueriesViaComponent.size() - 1; size3 >= 0; size3--) {
                    WatchedSparseSetArray<Integer> watchedSparseSetArray3 = this.mQueriesViaComponent;
                    watchedSparseSetArray3.remove(watchedSparseSetArray3.keyAt(size3), Integer.valueOf(packageStateInternal.getAppId()));
                }
            }
        }
        synchronized (this.mQueriesViaPackageLock) {
            this.mQueriesViaPackage.remove(packageStateInternal.getAppId());
            for (int size4 = this.mQueriesViaPackage.size() - 1; size4 >= 0; size4--) {
                WatchedSparseSetArray<Integer> watchedSparseSetArray4 = this.mQueriesViaPackage;
                watchedSparseSetArray4.remove(watchedSparseSetArray4.keyAt(size4), Integer.valueOf(packageStateInternal.getAppId()));
            }
        }
        synchronized (this.mQueryableViaUsesLibraryLock) {
            this.mQueryableViaUsesLibrary.remove(packageStateInternal.getAppId());
            for (int size5 = this.mQueryableViaUsesLibrary.size() - 1; size5 >= 0; size5--) {
                WatchedSparseSetArray<Integer> watchedSparseSetArray5 = this.mQueryableViaUsesLibrary;
                watchedSparseSetArray5.remove(watchedSparseSetArray5.keyAt(size5), Integer.valueOf(packageStateInternal.getAppId()));
            }
        }
        synchronized (this.mQueryableViaUsesPermissionLock) {
            if (packageStateInternal.getPkg() != null && !packageStateInternal.getPkg().getPermissions().isEmpty()) {
                Iterator<ParsedPermission> it = packageStateInternal.getPkg().getPermissions().iterator();
                while (it.hasNext()) {
                    String name = it.next().getName();
                    if (this.mPermissionToUids.containsKey(name)) {
                        this.mPermissionToUids.get(name).remove(Integer.valueOf(packageStateInternal.getAppId()));
                        if (this.mPermissionToUids.get(name).isEmpty()) {
                            this.mPermissionToUids.remove(name);
                        }
                    }
                }
            }
            if (packageStateInternal.getPkg() != null && !packageStateInternal.getPkg().getUsesPermissions().isEmpty()) {
                Iterator<ParsedUsesPermission> it2 = packageStateInternal.getPkg().getUsesPermissions().iterator();
                while (it2.hasNext()) {
                    String name2 = it2.next().getName();
                    if (this.mUsesPermissionToUids.containsKey(name2)) {
                        this.mUsesPermissionToUids.get(name2).remove(Integer.valueOf(packageStateInternal.getAppId()));
                        if (this.mUsesPermissionToUids.get(name2).isEmpty()) {
                            this.mUsesPermissionToUids.remove(name2);
                        }
                    }
                }
            }
            this.mQueryableViaUsesPermission.remove(packageStateInternal.getAppId());
        }
        synchronized (this.mForceQueryableLock) {
            this.mForceQueryable.remove(Integer.valueOf(packageStateInternal.getAppId()));
        }
        synchronized (this.mProtectedBroadcastsLock) {
            if (packageStateInternal.getPkg() != null && !packageStateInternal.getPkg().getProtectedBroadcasts().isEmpty()) {
                String packageName = packageStateInternal.getPkg().getPackageName();
                ArrayList arrayList = new ArrayList(this.mProtectedBroadcasts.untrackedStorage());
                collectProtectedBroadcasts(packageStates, packageName);
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    if (!this.mProtectedBroadcasts.contains(arrayList.get(i3))) {
                        z3 = true;
                        break;
                    }
                }
            }
            z3 = false;
        }
        if (z3) {
            this.mQueriesViaComponentRequireRecompute.set(true);
        }
        ArraySet<String> removePkg = this.mOverlayReferenceMapper.removePkg(packageStateInternal.getPackageName());
        this.mFeatureConfig.updatePackageState(packageStateInternal, true);
        SharedUserApi sharedUser = packageStateInternal.hasSharedUser() ? computer.getSharedUser(packageStateInternal.getSharedUserAppId()) : null;
        if (sharedUser != null) {
            ArraySet<? extends PackageStateInternal> packageStates2 = sharedUser.getPackageStates();
            for (int size6 = packageStates2.size() - 1; size6 >= 0; size6--) {
                if (packageStates2.valueAt(size6) != packageStateInternal) {
                    addPackageInternal(packageStates2.valueAt(size6), packageStates);
                }
            }
        }
        if (this.mCacheReady) {
            removeAppIdFromVisibilityCache(packageStateInternal.getAppId());
            if (sharedUser != null) {
                ArraySet<? extends PackageStateInternal> packageStates3 = sharedUser.getPackageStates();
                int size7 = packageStates3.size() - 1;
                while (size7 >= 0) {
                    PackageStateInternal valueAt = packageStates3.valueAt(size7);
                    if (valueAt == packageStateInternal) {
                        i = size7;
                    } else {
                        Object obj2 = this.mCacheLock;
                        synchronized (obj2) {
                            try {
                                obj = obj2;
                                i = size7;
                                try {
                                    updateShouldFilterCacheForPackage(computer, packageStateInternal.getPackageName(), valueAt, packageStates, userInfos, -1, packageStates.size());
                                } catch (Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                obj = obj2;
                                throw th;
                            }
                        }
                    }
                    size7 = i - 1;
                }
            }
        } else {
            invalidateCache("removePackage: " + packageStateInternal.getPackageName());
        }
        onChanged();
    }

    private static boolean pkgInstruments(AndroidPackage androidPackage, AndroidPackage androidPackage2) {
        String packageName = androidPackage2.getPackageName();
        List<ParsedInstrumentation> instrumentations = androidPackage.getInstrumentations();
        for (int size = ArrayUtils.size(instrumentations) - 1; size >= 0; size--) {
            if (Objects.equals(instrumentations.get(size).getTargetPackage(), packageName)) {
                return true;
            }
        }
        return false;
    }

    private void logCacheRebuilt(int i, long j, int i2, int i3) {
        FrameworkStatsLog.write(545, i, j, i2, i3, this.mShouldFilterCache.size());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logCacheUpdated(int i, long j, int i2, int i3, int i4) {
        if (this.mCacheReady) {
            FrameworkStatsLog.write(546, i, i4, j, i2, i3, this.mShouldFilterCache.size());
        }
    }
}
