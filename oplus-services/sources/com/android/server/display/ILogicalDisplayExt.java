package com.android.server.display;

import android.view.DisplayInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ILogicalDisplayExt {
    default boolean isSecondaryDisplayEnabled() {
        return false;
    }

    default void setDisplayInfoFlags(DisplayDeviceInfo displayDeviceInfo, DisplayInfo displayInfo, int i) {
    }

    default void setSecondaryDisplayEnabled(boolean z) {
    }
}
