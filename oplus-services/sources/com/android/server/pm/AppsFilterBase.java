package com.android.server.pm;

import android.content.pm.SigningDetails;
import android.os.Binder;
import android.os.Handler;
import android.os.Process;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.function.QuadFunction;
import com.android.server.om.OverlayReferenceMapper;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.SharedUserApi;
import com.android.server.pm.snapshot.PackageDataSnapshot;
import com.android.server.utils.SnapshotCache;
import com.android.server.utils.Watched;
import com.android.server.utils.WatchedArrayMap;
import com.android.server.utils.WatchedArraySet;
import com.android.server.utils.WatchedSparseBooleanMatrix;
import com.android.server.utils.WatchedSparseSetArray;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class AppsFilterBase implements AppsFilterSnapshot {
    protected static final boolean CACHE_INVALID = false;
    protected static final int CACHE_REBUILD_DELAY_MAX_MS = 10000;
    protected static final int CACHE_REBUILD_DELAY_MIN_MS = 10000;
    protected static final boolean CACHE_VALID = true;
    protected static final boolean DEBUG_ALLOW_ALL = false;
    protected static final boolean DEBUG_LOGGING = false;
    public static final boolean DEBUG_TRACING = false;
    protected static final String TAG = "AppsFilter";
    protected FeatureConfig mFeatureConfig;

    @Watched
    protected WatchedArraySet<Integer> mForceQueryable;
    protected String[] mForceQueryableByDevicePackageNames;
    protected SnapshotCache<WatchedArraySet<Integer>> mForceQueryableSnapshot;
    protected Handler mHandler;
    protected SnapshotCache<WatchedSparseSetArray<Integer>> mImplicitQueryableSnapshot;

    @Watched
    protected WatchedSparseSetArray<Integer> mImplicitlyQueryable;
    protected OverlayReferenceMapper mOverlayReferenceMapper;

    @Watched
    protected WatchedArraySet<String> mProtectedBroadcasts;
    protected SnapshotCache<WatchedArraySet<String>> mProtectedBroadcastsSnapshot;

    @Watched
    protected WatchedSparseSetArray<Integer> mQueriesViaComponent;
    protected SnapshotCache<WatchedSparseSetArray<Integer>> mQueriesViaComponentSnapshot;

    @Watched
    protected WatchedSparseSetArray<Integer> mQueriesViaPackage;
    protected SnapshotCache<WatchedSparseSetArray<Integer>> mQueriesViaPackageSnapshot;

    @Watched
    protected WatchedSparseSetArray<Integer> mQueryableViaUsesLibrary;
    protected SnapshotCache<WatchedSparseSetArray<Integer>> mQueryableViaUsesLibrarySnapshot;

    @Watched
    protected WatchedSparseSetArray<Integer> mQueryableViaUsesPermission;
    protected SnapshotCache<WatchedSparseSetArray<Integer>> mQueryableViaUsesPermissionSnapshot;

    @Watched
    protected WatchedSparseSetArray<Integer> mRetainedImplicitlyQueryable;
    protected SnapshotCache<WatchedSparseSetArray<Integer>> mRetainedImplicitlyQueryableSnapshot;

    @Watched
    protected WatchedSparseBooleanMatrix mShouldFilterCache;
    protected SnapshotCache<WatchedSparseBooleanMatrix> mShouldFilterCacheSnapshot;
    protected boolean mSystemAppsQueryable;
    protected SigningDetails mSystemSigningDetails;
    protected AtomicBoolean mQueriesViaComponentRequireRecompute = new AtomicBoolean(false);
    protected volatile boolean mCacheReady = false;
    protected volatile boolean mCacheEnabled = true;
    protected AtomicBoolean mCacheValid = new AtomicBoolean(false);

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ToString<T> {
        String toString(T t);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isForceQueryable(int i) {
        return this.mForceQueryable.contains(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isQueryableViaPackage(int i, int i2) {
        return this.mQueriesViaPackage.contains(i, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isQueryableViaComponent(int i, int i2) {
        return this.mQueriesViaComponent.contains(i, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isImplicitlyQueryable(int i, int i2) {
        return this.mImplicitlyQueryable.contains(i, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isRetainedImplicitlyQueryable(int i, int i2) {
        return this.mRetainedImplicitlyQueryable.contains(i, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isQueryableViaUsesLibrary(int i, int i2) {
        return this.mQueryableViaUsesLibrary.contains(i, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isQueryableViaUsesPermission(int i, int i2) {
        return this.mQueryableViaUsesPermission.contains(i, Integer.valueOf(i2));
    }

    protected boolean isQueryableViaComponentWhenRequireRecompute(ArrayMap<String, ? extends PackageStateInternal> arrayMap, PackageStateInternal packageStateInternal, ArraySet<PackageStateInternal> arraySet, AndroidPackage androidPackage, int i, int i2) {
        if (packageStateInternal != null) {
            return packageStateInternal.getPkg() != null && AppsFilterUtils.canQueryViaComponents(packageStateInternal.getPkg(), androidPackage, this.mProtectedBroadcasts);
        }
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            AndroidPackageInternal pkg = arraySet.valueAt(size).getPkg();
            if (pkg != null && AppsFilterUtils.canQueryViaComponents(pkg, androidPackage, this.mProtectedBroadcasts)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.pm.AppsFilterSnapshot
    public SparseArray<int[]> getVisibilityAllowList(PackageDataSnapshot packageDataSnapshot, PackageStateInternal packageStateInternal, int[] iArr, ArrayMap<String, ? extends PackageStateInternal> arrayMap) {
        int binarySearch;
        int i;
        int i2;
        int[] iArr2 = null;
        if (isForceQueryable(packageStateInternal.getAppId())) {
            return null;
        }
        SparseArray<int[]> sparseArray = new SparseArray<>(iArr.length);
        int i3 = 0;
        int i4 = 0;
        while (i4 < iArr.length) {
            int i5 = iArr[i4];
            int size = arrayMap.size();
            int[] iArr3 = new int[size];
            int size2 = arrayMap.size() - 1;
            int[] iArr4 = iArr2;
            int i6 = i3;
            while (size2 >= 0) {
                PackageStateInternal valueAt = arrayMap.valueAt(size2);
                int appId = valueAt.getAppId();
                if (appId >= 10000 && (binarySearch = Arrays.binarySearch(iArr3, i3, i6, appId)) < 0) {
                    i = i6;
                    if (shouldFilterApplication(packageDataSnapshot, UserHandle.getUid(i5, appId), valueAt, packageStateInternal, i5)) {
                        i2 = 0;
                    } else {
                        int[] iArr5 = iArr4 == null ? new int[size] : iArr4;
                        int i7 = ~binarySearch;
                        int i8 = i - i7;
                        i2 = 0;
                        System.arraycopy(iArr3, i7, iArr5, 0, i8);
                        iArr3[i7] = appId;
                        System.arraycopy(iArr5, 0, iArr3, i7 + 1, i8);
                        i6 = i + 1;
                        iArr4 = iArr5;
                        size2--;
                        i3 = i2;
                    }
                } else {
                    i2 = i3;
                    i = i6;
                }
                i6 = i;
                size2--;
                i3 = i2;
            }
            sparseArray.put(i5, Arrays.copyOf(iArr3, i6));
            i4++;
            i3 = i3;
            iArr2 = null;
        }
        return sparseArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public SparseArray<int[]> getVisibilityAllowList(PackageDataSnapshot packageDataSnapshot, PackageStateInternal packageStateInternal, int[] iArr, WatchedArrayMap<String, ? extends PackageStateInternal> watchedArrayMap) {
        return getVisibilityAllowList(packageDataSnapshot, packageStateInternal, iArr, watchedArrayMap.untrackedStorage());
    }

    @Override // com.android.server.pm.AppsFilterSnapshot
    public boolean shouldFilterApplication(PackageDataSnapshot packageDataSnapshot, int i, Object obj, PackageStateInternal packageStateInternal, int i2) {
        int appId = UserHandle.getAppId(i);
        if (appId < 10000 || packageStateInternal.getAppId() < 10000 || appId == packageStateInternal.getAppId()) {
            return false;
        }
        if (Process.isSdkSandboxUid(appId)) {
            return (isForceQueryable(packageStateInternal.getAppId()) || isImplicitlyQueryable(i, UserHandle.getUid(i2, packageStateInternal.getAppId()))) ? false : true;
        }
        if (this.mCacheReady && this.mCacheEnabled) {
            if (!shouldFilterApplicationUsingCache(i, packageStateInternal.getAppId(), i2)) {
                return false;
            }
        } else if (!shouldFilterApplicationInternal((Computer) packageDataSnapshot, i, obj, packageStateInternal, i2)) {
            return false;
        }
        if (this.mFeatureConfig.isLoggingEnabled(appId)) {
            log(obj, packageStateInternal, "BLOCKED");
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean shouldFilterApplicationUsingCache(int i, int i2, int i3) {
        int indexOfKey = this.mShouldFilterCache.indexOfKey(i);
        if (indexOfKey < 0) {
            Slog.wtf(TAG, "Encountered calling uid with no cached rules: " + i);
            return true;
        }
        int uid = UserHandle.getUid(i3, i2);
        int indexOfKey2 = this.mShouldFilterCache.indexOfKey(uid);
        if (indexOfKey2 < 0) {
            Slog.w(TAG, "Encountered calling -> target with no cached rules: " + i + " -> " + uid);
            return true;
        }
        return this.mShouldFilterCache.valueAt(indexOfKey, indexOfKey2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean shouldFilterApplicationInternal(Computer computer, int i, Object obj, PackageStateInternal packageStateInternal, int i2) {
        if (!this.mFeatureConfig.isGloballyEnabled()) {
            return false;
        }
        if (obj == null) {
            Slog.wtf(TAG, "No setting found for non system uid " + i);
            return true;
        }
        int appId = UserHandle.getAppId(i);
        int appId2 = packageStateInternal.getAppId();
        if (appId == appId2 || appId < 10000 || appId2 < 10000) {
            return false;
        }
        ArraySet<PackageStateInternal> arraySet = new ArraySet<>();
        PackageStateInternal packageStateInternal2 = null;
        if (obj instanceof PackageStateInternal) {
            PackageStateInternal packageStateInternal3 = (PackageStateInternal) obj;
            if (packageStateInternal3.hasSharedUser()) {
                SharedUserApi sharedUser = computer.getSharedUser(packageStateInternal3.getSharedUserAppId());
                if (sharedUser != null) {
                    arraySet.addAll(sharedUser.getPackageStates());
                }
            } else {
                packageStateInternal2 = packageStateInternal3;
            }
        } else {
            arraySet.addAll(((SharedUserSetting) obj).getPackageStates());
        }
        PackageStateInternal packageStateInternal4 = packageStateInternal2;
        if (packageStateInternal4 != null) {
            if (packageStateInternal4.getPkg() != null && !this.mFeatureConfig.packageIsEnabled(packageStateInternal4.getPkg())) {
                return false;
            }
        } else {
            for (int size = arraySet.size() - 1; size >= 0; size--) {
                AndroidPackageInternal pkg = arraySet.valueAt(size).getPkg();
                if (pkg != null && !this.mFeatureConfig.packageIsEnabled(pkg)) {
                    return false;
                }
            }
        }
        if (packageStateInternal4 != null) {
            if (packageStateInternal4.getPkg() != null && AppsFilterUtils.requestsQueryAllPackages(packageStateInternal4.getPkg())) {
                return false;
            }
        } else {
            for (int size2 = arraySet.size() - 1; size2 >= 0; size2--) {
                AndroidPackageInternal pkg2 = arraySet.valueAt(size2).getPkg();
                if (pkg2 != null && AppsFilterUtils.requestsQueryAllPackages(pkg2)) {
                    return false;
                }
            }
        }
        AndroidPackageInternal pkg3 = packageStateInternal.getPkg();
        if (pkg3 == null) {
            return true;
        }
        if (pkg3.isStaticSharedLibrary() || isForceQueryable(appId2) || isQueryableViaPackage(appId, appId2)) {
            return false;
        }
        if (!this.mQueriesViaComponentRequireRecompute.get()) {
            if (isQueryableViaComponent(appId, appId2)) {
                return false;
            }
        } else if (isQueryableViaComponentWhenRequireRecompute(computer.getPackageStates(), packageStateInternal4, arraySet, pkg3, appId, appId2)) {
            return false;
        }
        if (isImplicitlyQueryable(i, UserHandle.getUid(i2, appId2)) || isRetainedImplicitlyQueryable(i, UserHandle.getUid(i2, appId2))) {
            return false;
        }
        String packageName = pkg3.getPackageName();
        if (!arraySet.isEmpty()) {
            int size3 = arraySet.size();
            for (int i3 = 0; i3 < size3; i3++) {
                if (this.mOverlayReferenceMapper.isValidActor(packageName, arraySet.valueAt(i3).getPackageName())) {
                    return false;
                }
            }
        } else if (this.mOverlayReferenceMapper.isValidActor(packageName, packageStateInternal4.getPackageName())) {
            return false;
        }
        return (isQueryableViaUsesLibrary(appId, appId2) || isQueryableViaUsesPermission(appId, appId2)) ? false : true;
    }

    @Override // com.android.server.pm.AppsFilterSnapshot
    public boolean canQueryPackage(AndroidPackage androidPackage, String str) {
        if (UserHandle.getAppId(androidPackage.getUid()) >= 10000 && this.mFeatureConfig.packageIsEnabled(androidPackage) && !AppsFilterUtils.requestsQueryAllPackages(androidPackage)) {
            return !androidPackage.getQueriesPackages().isEmpty() && androidPackage.getQueriesPackages().contains(str);
        }
        return true;
    }

    private static void log(Object obj, PackageStateInternal packageStateInternal, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("interaction: ");
        if (obj == null) {
            obj = "system";
        }
        sb.append(obj);
        sb.append(" -> ");
        sb.append(packageStateInternal);
        sb.append(" ");
        sb.append(str);
        Slog.i(TAG, sb.toString());
    }

    @Override // com.android.server.pm.AppsFilterSnapshot
    public void dumpQueries(PrintWriter printWriter, Integer num, DumpState dumpState, final int[] iArr, final QuadFunction<Integer, Integer, Integer, Boolean, String[]> quadFunction) {
        final SparseArray sparseArray = new SparseArray();
        ToString<Integer> toString = new ToString() { // from class: com.android.server.pm.AppsFilterBase$$ExternalSyntheticLambda0
            @Override // com.android.server.pm.AppsFilterBase.ToString
            public final String toString(Object obj) {
                String lambda$dumpQueries$0;
                lambda$dumpQueries$0 = AppsFilterBase.lambda$dumpQueries$0(sparseArray, iArr, quadFunction, (Integer) obj);
                return lambda$dumpQueries$0;
            }
        };
        printWriter.println();
        printWriter.println("Queries:");
        dumpState.onTitlePrinted();
        if (!this.mFeatureConfig.isGloballyEnabled()) {
            printWriter.println("  DISABLED");
            return;
        }
        printWriter.println("  system apps queryable: " + this.mSystemAppsQueryable);
        dumpForceQueryable(printWriter, num, toString);
        dumpQueriesViaPackage(printWriter, num, toString);
        dumpQueriesViaComponent(printWriter, num, toString);
        dumpQueriesViaImplicitlyQueryable(printWriter, num, iArr, toString);
        dumpQueriesViaUsesLibrary(printWriter, num, toString);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$dumpQueries$0(SparseArray sparseArray, int[] iArr, QuadFunction quadFunction, Integer num) {
        String str;
        String str2 = (String) sparseArray.get(num.intValue());
        if (str2 == null) {
            int callingUid = Binder.getCallingUid();
            int appId = UserHandle.getAppId(num.intValue());
            int length = iArr.length;
            String[] strArr = null;
            for (int i = 0; strArr == null && i < length; i++) {
                strArr = (String[]) quadFunction.apply(Integer.valueOf(callingUid), Integer.valueOf(iArr[i]), Integer.valueOf(appId), Boolean.FALSE);
            }
            if (strArr == null) {
                str = "[app id " + num + " not installed]";
            } else if (strArr.length == 1) {
                str = strArr[0];
            } else {
                str = "[" + TextUtils.join(",", strArr) + "]";
            }
            str2 = str;
            sparseArray.put(num.intValue(), str2);
        }
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpForceQueryable(PrintWriter printWriter, Integer num, ToString<Integer> toString) {
        printWriter.println("  queries via forceQueryable:");
        dumpPackageSet(printWriter, num, this.mForceQueryable.untrackedStorage(), "forceQueryable", "  ", toString);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpQueriesViaPackage(PrintWriter printWriter, Integer num, ToString<Integer> toString) {
        printWriter.println("  queries via package name:");
        dumpQueriesMap(printWriter, num, this.mQueriesViaPackage, "    ", toString);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpQueriesViaComponent(PrintWriter printWriter, Integer num, ToString<Integer> toString) {
        printWriter.println("  queries via component:");
        dumpQueriesMap(printWriter, num, this.mQueriesViaComponent, "    ", toString);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpQueriesViaImplicitlyQueryable(PrintWriter printWriter, Integer num, int[] iArr, ToString<Integer> toString) {
        printWriter.println("  queryable via interaction:");
        for (int i : iArr) {
            printWriter.append("    User ").append((CharSequence) Integer.toString(i)).println(":");
            Integer num2 = null;
            dumpQueriesMap(printWriter, num == null ? null : Integer.valueOf(UserHandle.getUid(i, num.intValue())), this.mImplicitlyQueryable, "      ", toString);
            if (num != null) {
                num2 = Integer.valueOf(UserHandle.getUid(i, num.intValue()));
            }
            dumpQueriesMap(printWriter, num2, this.mRetainedImplicitlyQueryable, "      ", toString);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpQueriesViaUsesLibrary(PrintWriter printWriter, Integer num, ToString<Integer> toString) {
        printWriter.println("  queryable via uses-library:");
        dumpQueriesMap(printWriter, num, this.mQueryableViaUsesLibrary, "    ", toString);
    }

    protected void dumpQueriesViaUsesPermission(PrintWriter printWriter, Integer num, ToString<Integer> toString) {
        printWriter.println("  queryable via uses-permission:");
        dumpQueriesMap(printWriter, num, this.mQueryableViaUsesPermission, "    ", toString);
    }

    private static void dumpQueriesMap(PrintWriter printWriter, Integer num, WatchedSparseSetArray<Integer> watchedSparseSetArray, String str, ToString<Integer> toString) {
        String toString2;
        String toString3;
        for (int i = 0; i < watchedSparseSetArray.size(); i++) {
            Integer valueOf = Integer.valueOf(watchedSparseSetArray.keyAt(i));
            if (Objects.equals(valueOf, num)) {
                ArraySet<Integer> arraySet = watchedSparseSetArray.get(valueOf.intValue());
                if (toString == null) {
                    toString3 = valueOf.toString();
                } else {
                    toString3 = toString.toString(valueOf);
                }
                dumpPackageSet(printWriter, null, arraySet, toString3, str, toString);
            } else {
                ArraySet<Integer> arraySet2 = watchedSparseSetArray.get(valueOf.intValue());
                if (toString == null) {
                    toString2 = valueOf.toString();
                } else {
                    toString2 = toString.toString(valueOf);
                }
                dumpPackageSet(printWriter, num, arraySet2, toString2, str, toString);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> void dumpPackageSet(PrintWriter printWriter, T t, ArraySet<T> arraySet, String str, String str2, ToString<T> toString) {
        if (arraySet == null || arraySet.size() <= 0) {
            return;
        }
        if (t == null || arraySet.contains(t)) {
            printWriter.append((CharSequence) str2).append((CharSequence) str).println(":");
            Iterator<T> it = arraySet.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (t == null || t.equals(next)) {
                    PrintWriter append = printWriter.append((CharSequence) str2).append("  ");
                    if (toString != 0) {
                        next = toString.toString(next);
                    }
                    append.println(next);
                }
            }
        }
    }
}
