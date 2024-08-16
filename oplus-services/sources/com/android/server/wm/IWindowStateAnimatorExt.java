package com.android.server.wm;

import android.content.Context;
import android.view.animation.Animation;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowStateAnimatorExt {
    default void addStartingBackColorLayerIfNeed(WindowState windowState) {
    }

    default void adjustMultiSearchAnimation(boolean z, WindowState windowState, Animation animation) {
    }

    default void destoryCompactDimmer(WindowState windowState) {
    }

    default boolean hideForUnFolded(WindowState windowState) {
        return false;
    }

    default boolean prepareSurfaceLocked(WindowState windowState) {
        return false;
    }

    default boolean setStartingWindowExitAnimation(int i, WindowState windowState) {
        return false;
    }

    default boolean shouldSkipOrientation(boolean z, WindowState windowState) {
        return true;
    }

    default void skipWindowAnimation(boolean z, WindowState windowState, Animation animation) {
    }

    default boolean skipWindowAnimationIfNeed(int i, boolean z, WindowState windowState) {
        return false;
    }

    default boolean waitDrawingCompleted(WindowState windowState, Context context) {
        return false;
    }
}
