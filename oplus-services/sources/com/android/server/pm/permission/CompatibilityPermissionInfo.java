package com.android.server.pm.permission;

import android.annotation.NonNull;
import com.android.internal.util.AnnotationValidations;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CompatibilityPermissionInfo {
    public static final CompatibilityPermissionInfo[] COMPAT_PERMS = {new CompatibilityPermissionInfo("android.permission.POST_NOTIFICATIONS", 33), new CompatibilityPermissionInfo("android.permission.WRITE_EXTERNAL_STORAGE", 4), new CompatibilityPermissionInfo("android.permission.READ_PHONE_STATE", 4)};
    private final String mName;
    private final int mSdkVersion;

    @Deprecated
    private void __metadata() {
    }

    public CompatibilityPermissionInfo(String str, int i) {
        this.mName = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        this.mSdkVersion = i;
    }

    public String getName() {
        return this.mName;
    }

    public int getSdkVersion() {
        return this.mSdkVersion;
    }
}
