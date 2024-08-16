package com.android.server.wm;

import android.content.pm.ApplicationInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ICompatModePackagesExt {
    default float getCompatScale(String str, int i) {
        return 1.0f;
    }

    default void overrideCompatInfoIfNeed(ApplicationInfo applicationInfo) {
    }
}
