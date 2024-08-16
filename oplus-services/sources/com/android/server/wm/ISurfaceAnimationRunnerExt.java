package com.android.server.wm;

import android.view.Choreographer;
import android.view.SurfaceControl;
import com.android.server.wm.LocalAnimationAdapter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISurfaceAnimationRunnerExt {
    default void computeAnimHashForstartAnimationLocked(LocalAnimationAdapter.AnimationSpec animationSpec) {
    }

    default boolean hookonAnimationEndRemove(boolean z, SurfaceControl surfaceControl) {
        return false;
    }

    default void onAnimationEnd(LocalAnimationAdapter.AnimationSpec animationSpec, Choreographer choreographer) {
    }

    default void onAnimationStart(LocalAnimationAdapter.AnimationSpec animationSpec, long j, Choreographer choreographer) {
    }

    default void onWindowAnimationEnded(int i) {
    }

    default void recordCurrentAnimationPoints(long j) {
    }

    default void tryClearAnimPointsWhenCancelled(LocalAnimationAdapter.AnimationSpec animationSpec, int i) {
    }

    default void trySaveAnimationLeashHashAndReinitializeAnimParams(LocalAnimationAdapter.AnimationSpec animationSpec, int i) {
    }
}
