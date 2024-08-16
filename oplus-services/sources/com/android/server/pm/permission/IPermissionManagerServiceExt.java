package com.android.server.pm.permission;

import android.content.Context;
import android.util.ArraySet;
import com.android.server.pm.pkg.AndroidPackage;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPermissionManagerServiceExt {
    default void adjustGetAppOpPermissionPackagesInternal(ArraySet<String> arraySet) {
    }

    default void afterOnPackageUninstalled() {
    }

    default void beforeOnPackageUninstalled() {
    }

    default boolean hookCheckUidPermissionImpl(Context context, String str, int i) {
        return false;
    }

    default boolean hookIsPermissionsReviewRequiredInternal() {
        return false;
    }

    default void hookNotifyMultiAppPermissionChanged(String str) {
    }

    default void hookPermissionManagerService(Context context, PermissionManagerService permissionManagerService) {
    }

    default void hookShouldDowngradePrivPermViolation(ArraySet<String> arraySet) {
    }

    default boolean hookShouldGrantNormalPermission(String str) {
        return false;
    }

    default boolean hookShouldGrantPermissionBySignature(AndroidPackage androidPackage, String str, boolean z, String str2) {
        return false;
    }
}
