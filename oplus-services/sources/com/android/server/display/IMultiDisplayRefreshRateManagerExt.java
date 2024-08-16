package com.android.server.display;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IMultiDisplayRefreshRateManagerExt {
    default void init(Context context) {
    }

    default boolean isSupport() {
        return false;
    }
}
