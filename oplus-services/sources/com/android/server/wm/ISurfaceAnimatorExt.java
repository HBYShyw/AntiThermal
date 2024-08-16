package com.android.server.wm;

import android.graphics.Point;
import android.view.SurfaceControl;
import com.android.server.wm.SurfaceAnimator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISurfaceAnimatorExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface IStaticExt {
        default void adjustAnimationLeashLayerIfNeeded(SurfaceControl.Transaction transaction, SurfaceAnimator.Animatable animatable, SurfaceControl surfaceControl) {
        }
    }

    default void boostLeashLayerIfNeed(SurfaceAnimator.Animatable animatable, int i, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
    }

    default void callOrmsSetSceneActionForRemoteAnimation(boolean z, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i) {
    }

    default boolean cancelAnimThreadUxIfNeed(SurfaceAnimator.Animatable animatable, int i) {
        return true;
    }

    default void customReset(boolean z) {
    }

    default boolean hookReset(SurfaceAnimator surfaceAnimator, SurfaceControl.Transaction transaction) {
        return false;
    }

    default boolean hookResetForTask(SurfaceAnimator surfaceAnimator, boolean z) {
        return false;
    }

    default boolean hookSetLeash(SurfaceAnimator.Animatable animatable, SurfaceControl surfaceControl) {
        return false;
    }

    default boolean isDragSplitToFullLeash(SurfaceControl surfaceControl) {
        return false;
    }

    default boolean isDragZoomToSplitLeash(SurfaceControl surfaceControl) {
        return false;
    }

    default boolean isReuseLeash() {
        return false;
    }

    default void resetIfNeeded(SurfaceAnimator surfaceAnimator) {
    }

    default void setDeferAnimationFinish(SurfaceAnimator.Animatable animatable, boolean z) {
    }

    default void setReuseLeash(SurfaceAnimator surfaceAnimator) {
    }

    default void showTaskIfNeed(SurfaceAnimator.Animatable animatable, SurfaceControl.Transaction transaction) {
    }

    default boolean useGesturePosition(SurfaceAnimator surfaceAnimator, Point point, boolean z) {
        return false;
    }
}
