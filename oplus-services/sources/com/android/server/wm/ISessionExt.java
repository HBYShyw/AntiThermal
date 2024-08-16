package com.android.server.wm;

import android.util.MergedConfiguration;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISessionExt {
    default boolean hasOplusSafeWindowPermission() {
        return false;
    }

    default void hookrelayout(MergedConfiguration mergedConfiguration, String str) {
    }

    default void setOplusSafeWindowPermission(WindowManagerService windowManagerService) {
    }
}
