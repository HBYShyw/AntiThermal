package com.android.server.wm;

import android.content.res.Configuration;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface INonStaticDisplayContentExt {
    default boolean isPuttDisplay() {
        return false;
    }

    default void setPuttDisplay(boolean z) {
    }

    default void updateRequestedOverrideConfiguration(Configuration configuration) {
    }
}
