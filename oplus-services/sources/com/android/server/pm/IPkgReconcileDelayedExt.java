package com.android.server.pm;

import com.android.server.pm.pkg.PackageStateInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPkgReconcileDelayedExt {
    public static final IPkgReconcileDelayedExt DEFAULT = new IPkgReconcileDelayedExt() { // from class: com.android.server.pm.IPkgReconcileDelayedExt.1
    };

    default void asyncDelayedPrepareWork(PackageManagerService packageManagerService) {
    }

    default boolean canDelayPrepareAppData(PackageStateInternal packageStateInternal) {
        return false;
    }

    default void markDelayPrepareAppData(PackageStateInternal packageStateInternal) {
    }
}
