package com.android.server.policy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPermissionPolicyServiceExt {
    default void beforeGrantOrUpgradeDefaultRuntimePermissions(int i) {
    }

    default int getSwitchOp(String str) {
        return -1;
    }

    default boolean shouldCallRemotePermissionController(int i) {
        return false;
    }

    default boolean shouldDelayAppOpsSyncJob(int i, String str) {
        return false;
    }

    default boolean shouldGrantOrUpgradeDefaultRuntimePermissions(int i) {
        return false;
    }

    default boolean shouldSynchronizePermissionsAndAppOpsForUser(int i) {
        return false;
    }

    default boolean skipRunOnInitialized(int i) {
        return false;
    }

    default boolean skipSynchronizePackagePermissionsAndAppOpsAsyncForUser(String str, int i) {
        return false;
    }
}
