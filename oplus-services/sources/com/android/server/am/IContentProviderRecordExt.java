package com.android.server.am;

import android.content.pm.ApplicationInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IContentProviderRecordExt {
    default void hookProviderTimeout(ActivityManagerService activityManagerService, ProcessRecord processRecord, ApplicationInfo applicationInfo) {
    }

    default boolean isNeedRelease(String str) {
        return false;
    }
}
