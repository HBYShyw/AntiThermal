package com.android.server.wm;

import android.view.InsetsState;
import android.view.WindowManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IInsetsPolicyExt {
    default WindowState getContainerWindow(WindowState windowState, DisplayContent displayContent, boolean z) {
        return null;
    }

    default InsetsState getInsetsForWindowMetrics(WindowManager.LayoutParams layoutParams, WindowToken windowToken, InsetsState insetsState, DisplayContent displayContent) {
        return insetsState;
    }

    default WindowState getStatusControlTargetInSplit(WindowState windowState) {
        return null;
    }

    default boolean hasFoldRemapDisplayDisableFeature() {
        return false;
    }

    default boolean isFlexibleTaskIgnoreSysBar(WindowState windowState) {
        return false;
    }

    default boolean isWindowingZoomMode(WindowState windowState) {
        return false;
    }

    default void removeSource(InsetsState insetsState, DisplayContent displayContent) {
    }

    default boolean shouldForceShowStatusBar(DisplayContent displayContent) {
        return true;
    }

    default boolean shouldTopFullOpqWinForceCtrlNavBar(WindowState windowState) {
        return false;
    }

    default boolean shouldTopFullOpqWinForceCtrlStatusBar(WindowState windowState) {
        return false;
    }

    default boolean showTransient() {
        return false;
    }
}
