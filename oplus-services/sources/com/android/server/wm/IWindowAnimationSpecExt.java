package com.android.server.wm;

import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowAnimationSpecExt {
    default void adjustCropRect(Animation animation, Rect rect, Transformation transformation, SurfaceControl.Transaction transaction) {
    }

    default void clipTmpRect(int i, Rect rect, float f, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
    }

    default int getmClipSide() {
        return 0;
    }

    default void setClipSide(int i) {
    }

    default void setUseExtendAnimation(boolean z) {
    }

    default boolean useExtendAnimation() {
        return true;
    }
}
