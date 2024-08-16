package com.android.server.wm;

import com.android.server.wm.SystemGesturesPointerEventListener;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISystemGesturesPointerEventListenerSocExt {
    default boolean hookGetScrollFired() {
        return false;
    }

    default void hookOnFling(SystemGesturesPointerEventListener.Callbacks callbacks, float f, float f2, int i) {
    }

    default void hookSetScrollFired(boolean z) {
    }
}
