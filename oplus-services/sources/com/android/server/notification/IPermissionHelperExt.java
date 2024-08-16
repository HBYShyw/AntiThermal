package com.android.server.notification;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPermissionHelperExt {
    default boolean canModifyNotificationPermissionForPackage(String str, int i) {
        return true;
    }

    default boolean isLoggable() {
        return false;
    }
}
