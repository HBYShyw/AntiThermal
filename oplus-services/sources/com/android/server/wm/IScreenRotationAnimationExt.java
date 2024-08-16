package com.android.server.wm;

import android.content.Context;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import android.window.ScreenCapture;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IScreenRotationAnimationExt {
    default void adjustBlurBackgroundLayer() {
    }

    default void changeRotateAnimation(Animation animation, Animation animation2, Animation animation3, Context context) {
    }

    default float computStartLuma(float f) {
        return f;
    }

    default boolean enterAnimationinitialize(Animation animation, int i, int i2, int i3, int i4) {
        return false;
    }

    default boolean getDeviceFolding() {
        return false;
    }

    default float getLuma(boolean z) {
        return Float.MIN_VALUE;
    }

    default ScreenCapture.ScreenshotHardwareBuffer getScreenshotHardwareBuffer() {
        return null;
    }

    default float getWindowCornerRadius() {
        return 0.0f;
    }

    default boolean hookAdjustScreenshotInitialRotation(LocalAnimationAdapter localAnimationAdapter, SurfaceAnimator surfaceAnimator, int i, int i2, boolean z, DisplayContent displayContent, SurfaceControl surfaceControl, int i3) {
        return false;
    }

    default boolean hookComputStartLumaForDismiss(int i, int i2, DisplayContent displayContent) {
        return false;
    }

    default boolean hookLoadAnimation(int i, int i2, int i3, int i4, int i5) {
        return false;
    }

    default void notifyScreenshotAnimationStart() {
    }

    default void onScreenRotationAnimationEnd() {
    }

    default void setCustomAnim(boolean z) {
    }

    default void setFrozenByUserSwitching(boolean z) {
    }

    default void setRotationLayer(SurfaceControl surfaceControl, DisplayContent displayContent) {
    }

    default boolean startScreenRotateBackColorAnimation(float[] fArr, Animation animation, SurfaceControl surfaceControl, DisplayContent displayContent) {
        return false;
    }

    default void updateAnimationForFolding(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, SurfaceControl surfaceControl2, DisplayContent displayContent, boolean z) {
    }
}
