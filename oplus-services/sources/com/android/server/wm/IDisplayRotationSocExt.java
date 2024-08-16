package com.android.server.wm;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayRotationSocExt {
    default int hookGetWifiDisplayRotation() {
        return -1;
    }

    default boolean hookIsWifiDisplayConnected() {
        return false;
    }

    default void hookRegisterWifiDisplay(Context context, WindowManagerService windowManagerService) {
    }
}
