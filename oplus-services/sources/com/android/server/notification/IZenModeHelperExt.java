package com.android.server.notification;

import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.service.notification.ZenModeConfig;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IZenModeHelperExt {
    default ZenModeConfig adjustZenModeConfig(ZenModeConfig zenModeConfig) {
        return zenModeConfig;
    }

    default void applyRestrictions(String[] strArr, boolean z, int i) {
    }

    default void applyRestrictions(String[] strArr, boolean z, int i, int i2) {
    }

    default boolean applyRestrictions(int i, long j, NotificationManager.Policy policy, String[] strArr) {
        return false;
    }

    default void init(ZenModeHelper zenModeHelper, Context context, AppOpsManager appOpsManager) {
    }

    default boolean interceptOnUserUnlocked(int i) {
        return false;
    }

    default void setPriorityOnlyDndExemptPackages(String[] strArr) {
    }

    default void setZenModeExtInfoStr(Context context, String str) {
    }
}
