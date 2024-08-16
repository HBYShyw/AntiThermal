package com.android.server.pm.permission;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IDefaultPermissionGrantPolicyExt {
    default void hookGetDefaultPermissionFiles(ArrayList<File> arrayList, File file) {
    }

    default Set<String> hookGrantDefaultSystemHandlerPermissions(Set<String> set) {
        return set;
    }
}
