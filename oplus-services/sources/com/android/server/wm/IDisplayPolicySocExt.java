package com.android.server.wm;

import android.util.BoostFramework;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayPolicySocExt {
    default String getAppPackageName() {
        return null;
    }

    default void hookOnDown() {
    }

    default void hookOnHorizontalFling(int i) {
    }

    default void hookOnScroll(boolean z) {
    }

    default void hookOnVerticalFling(int i) {
    }

    default boolean isTopAppGame(String str, BoostFramework boostFramework) {
        return false;
    }

    default void loadConfig() {
    }
}
