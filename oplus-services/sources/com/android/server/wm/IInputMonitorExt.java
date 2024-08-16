package com.android.server.wm;

import android.graphics.Rect;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IInputMonitorExt {
    default void adjustTouchableRegion(int i, Rect rect) {
    }

    default boolean getInputConsumerEnabled() {
        return true;
    }

    default void setOplusInputConfig(InputWindowHandleWrapper inputWindowHandleWrapper, WindowState windowState) {
    }
}
