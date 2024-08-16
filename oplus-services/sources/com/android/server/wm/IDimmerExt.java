package com.android.server.wm;

import android.graphics.Rect;
import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDimmerExt {
    default boolean skipDimAnimation(WindowContainer windowContainer) {
        return false;
    }

    default void updateDims(WindowContainer windowContainer, Rect rect, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
    }

    default boolean useSpeceficDurationForDim(WindowContainer windowContainer, WindowContainer windowContainer2, float f) {
        return false;
    }
}
