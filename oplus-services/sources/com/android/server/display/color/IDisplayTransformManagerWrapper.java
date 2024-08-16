package com.android.server.display.color;

import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDisplayTransformManagerWrapper {
    default void applyColorMatrix(float[] fArr) {
    }

    default void applyColorMatrix32(float[] fArr) {
    }

    default SparseArray<float[]> getColorMatrixs() {
        return new SparseArray<>();
    }
}
