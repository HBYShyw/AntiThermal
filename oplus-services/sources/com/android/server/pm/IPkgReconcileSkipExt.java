package com.android.server.pm;

import com.android.server.pm.pkg.PackageStateInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPkgReconcileSkipExt {
    public static final IPkgReconcileSkipExt DEFAULT = new IPkgReconcileSkipExt() { // from class: com.android.server.pm.IPkgReconcileSkipExt.1
    };

    default boolean skipPrepareAppData(PackageStateInternal packageStateInternal) {
        return false;
    }
}
