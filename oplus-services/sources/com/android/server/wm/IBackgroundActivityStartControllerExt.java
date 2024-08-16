package com.android.server.wm;

import android.content.Intent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IBackgroundActivityStartControllerExt {
    public static final int BAL_ALLOW_OPLUS_WHITE_LIST = 99;

    default boolean interceptBackgroundActivityStartBegin(Intent intent, int i, int i2, String str, int i3, int i4) {
        return false;
    }

    default boolean isFromBackgroundWhiteList(ActivityTaskManagerService activityTaskManagerService, int i) {
        return false;
    }

    default void monitorActivityStartInfoIfNeed(String str, boolean z, boolean z2) {
    }

    default boolean startAllowedIfRealCallingUidIsHome(ActivityTaskManagerService activityTaskManagerService, int i) {
        return false;
    }
}
