package com.android.server.wm;

import android.content.Context;
import android.os.Bundle;
import com.oplus.wallpaper.IWallpaperCallbackExt;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWallpaperControllerExt {
    default float checkWallpaperOffsetX(Context context, WindowState windowState, boolean z, float f) {
        return f;
    }

    default void dispatchWallpaperWindowsForRegister() {
    }

    default void dispatchWallpaperWindowsTarget(WindowState windowState, DisplayContent displayContent, boolean z) {
    }

    default IWallpaperCallbackExt getCallback() {
        return null;
    }

    default void handleWallpaperCreated(DisplayContent displayContent) {
    }

    default void removeWallpaperWindows() {
    }

    default boolean sendWindowWallpaperCommand(WindowState windowState, String str, Bundle bundle, boolean z) {
        return false;
    }

    default boolean shouldWaitForWallpaperToDraw(WindowState windowState) {
        return false;
    }

    default boolean skipHideSecondaryWallpaper(WindowManagerService windowManagerService, DisplayContent displayContent) {
        return false;
    }
}
