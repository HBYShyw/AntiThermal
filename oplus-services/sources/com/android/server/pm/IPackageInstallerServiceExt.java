package com.android.server.pm;

import android.util.SparseArray;
import com.android.server.pm.StagingManager;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageInstallerServiceExt {
    public static final String NONE = "none";

    default void afterRestoreSession(List<StagingManager.StagedSession> list, SparseArray<PackageInstallerSession> sparseArray, PackageManagerService packageManagerService) {
    }

    default boolean canForceAbandonMainlineSession(PackageInstallerSession packageInstallerSession) {
        return false;
    }

    default int changeUserIdIfNeed(int i, int i2, Computer computer) {
        return i;
    }

    default String getSotaAppState() {
        return "none";
    }

    default boolean isSotaAppSession(StagingManager.StagedSession stagedSession) {
        return false;
    }

    default boolean skipRemoveInstallAllUsersFlag(int i) {
        return false;
    }

    default void triggerPostBootApexSessionEvent() {
    }
}
