package com.android.server.pm.permission;

import android.content.pm.PackageInfo;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IDefaultPermissionGrantPolicyWrapper {
    default Object getNoPmCache() {
        return null;
    }

    default void grantRuntimePermissions(Object obj, PackageInfo packageInfo, Set<String> set, boolean z, boolean z2, boolean z3, int i) {
    }
}
