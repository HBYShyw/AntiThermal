package com.android.server.permission.access.util;

import android.content.ApexEnvironment;
import android.os.UserHandle;
import java.io.File;
import org.jetbrains.annotations.NotNull;

/* compiled from: PermissionApex.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PermissionApex {

    @NotNull
    public static final PermissionApex INSTANCE = new PermissionApex();

    private PermissionApex() {
    }

    @NotNull
    public final File getSystemDataDirectory() {
        return getApexEnvironment().getDeviceProtectedDataDir();
    }

    @NotNull
    public final File getUserDataDirectory(int i) {
        return getApexEnvironment().getDeviceProtectedDataDirForUser(UserHandle.of(i));
    }

    private final ApexEnvironment getApexEnvironment() {
        return ApexEnvironment.getApexEnvironment("com.android.permission");
    }
}
