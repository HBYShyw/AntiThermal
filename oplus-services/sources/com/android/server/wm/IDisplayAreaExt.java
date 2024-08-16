package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayAreaExt {
    default boolean shouldBlockOrientingWindowDuringFixedRotation(WindowManagerService windowManagerService, DisplayContent displayContent, WindowState windowState, int i) {
        return false;
    }
}
