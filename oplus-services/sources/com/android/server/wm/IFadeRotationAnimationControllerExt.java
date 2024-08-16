package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IFadeRotationAnimationControllerExt {
    default boolean allowFadeRotationAnimation(WindowState windowState) {
        return true;
    }

    default boolean hasSize(WindowState windowState) {
        return true;
    }
}
