package com.oplus.wrapper.util;

import android.graphics.Point;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public class RotationUtils {
    private RotationUtils() {
    }

    public static void rotatePoint(Point inOutPoint, int rotation, int parentW, int parentH) {
        android.util.RotationUtils.rotatePoint(inOutPoint, rotation, parentW, parentH);
    }

    public static void rotateSurface(SurfaceControl.Transaction t, SurfaceControl sc, int rotation) {
        android.util.RotationUtils.rotateSurface(t, sc, rotation);
    }
}
