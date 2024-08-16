package com.android.server.wm;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IRecentsAnimationControllerExt {
    default void adjustAnimationBounds(Task task, Rect rect) {
    }

    default void adjustTouchableRegion(WindowState windowState, Rect rect) {
    }

    default WallpaperController adjustWallpaperController(WallpaperController wallpaperController, DisplayContent displayContent) {
        return wallpaperController;
    }

    default void finishPutt(int i, int i2, Rect rect, int i3, Bundle bundle) {
    }

    default void hideDisplaySwitchNotification(Task task, boolean z) {
    }

    default void hooksetPosition(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, SurfaceAnimator surfaceAnimator, float f, float f2) {
    }

    default void hooksetWindowCrop(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, SurfaceAnimator surfaceAnimator, Rect rect) {
    }

    default boolean isInSplitRootTask(Task task) {
        return false;
    }

    default boolean isZoomWindowMode(int i) {
        return false;
    }

    default void markTaskNoAnimation(int i) {
    }

    default RemoteAnimationTarget obtainLaunchViewInfoForRecents(Task task, RemoteAnimationTarget remoteAnimationTarget) {
        return remoteAnimationTarget;
    }

    default void resetZoomAnimationFinished(boolean z) {
    }

    default void sendTasksAppeared(RemoteAnimationTarget[] remoteAnimationTargetArr) {
    }
}
