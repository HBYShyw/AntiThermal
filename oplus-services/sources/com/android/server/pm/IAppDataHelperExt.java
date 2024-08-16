package com.android.server.pm;

import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import java.io.File;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IAppDataHelperExt {
    default void afterCreateAppDataCompleted(Long l, Throwable th, AndroidPackage androidPackage, int i, int i2) {
    }

    default void afterDataPreparedInPrepareAppDataAndMigrate(AndroidPackage androidPackage, int i, int i2) {
    }

    default void afterExecuteBatchInReconcileAppsDataLI(String str, int i, int i2) {
    }

    default void afterExecuteBatchInReconcileAppsDataLI0(IPkgReconcileDelayedExt iPkgReconcileDelayedExt) {
    }

    default void afterReconcileAppsData(String str) {
    }

    default void beforeReconcileAppsData(String str) {
    }

    default void beforeReconcileAppsDataInConstructor() {
    }

    default List<String> customLogicForCeInReconcileAppsDataLI(List<String> list, File file, String str, int i, int i2, boolean z, boolean z2, Computer computer) {
        return list;
    }

    default boolean delayPrepareAppDataInRADL(IPkgReconcileDelayedExt iPkgReconcileDelayedExt, PackageStateInternal packageStateInternal) {
        return false;
    }

    default long fixDataForExceptionInPrepareAppDataLeaf(long j, AndroidPackage androidPackage, int i, int i2, int i3, String str, String str2, String str3) {
        return j;
    }

    default void init(PackageManagerService packageManagerService, AppDataHelper appDataHelper) {
    }

    default void onEndInReconcileAppsData(boolean z, boolean z2) {
    }

    default void onEndInReconcileAppsDataLI(int i) {
    }

    default void onPrepareAppDataFutureEndByDeferDone(int i) {
    }

    default void onPrepareAppDataFutureEndByNoDefer() {
    }

    default boolean shouldReconcileAppsDataInConstructor(PackageManagerService packageManagerService) {
        return true;
    }

    default boolean skipDestroyAppDataInDestroyAppDataLeafLIF(AndroidPackage androidPackage, int i, int i2) {
        return false;
    }

    default boolean skipDestroyCeDataInReconcileAppsDataLI(String str, String str2, int i) {
        return false;
    }

    default boolean skipDestroyDeDataInReconcileAppsDataLI(String str, String str2, int i) {
        return false;
    }

    default boolean skipPrepareAppDataForPkgInRADL(IPkgReconcileSkipExt iPkgReconcileSkipExt, PackageStateInternal packageStateInternal) {
        return false;
    }

    default boolean skipWorkAfterCreateAppData(IBatchExt iBatchExt, PackageSetting packageSetting) {
        return false;
    }

    default boolean useCustomLogicForCeInReconcileAppsDataLI(boolean z, int i, int i2) {
        return false;
    }

    default IPkgReconcileDelayedExt beforePrepareAppDataInRADL(int i, int i2, boolean z, String str, boolean z2) {
        return IPkgReconcileDelayedExt.DEFAULT;
    }

    default IPkgReconcileSkipExt beforePrepareAppDataInRADL2(int i, int i2, String str) {
        return IPkgReconcileSkipExt.DEFAULT;
    }
}
