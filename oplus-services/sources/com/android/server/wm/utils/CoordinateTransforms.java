package com.android.server.wm.utils;

import android.graphics.Matrix;
import android.view.DisplayInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class CoordinateTransforms {
    private CoordinateTransforms() {
    }

    public static void transformPhysicalToLogicalCoordinates(int i, int i2, int i3, Matrix matrix) {
        if (i == 0) {
            matrix.reset();
            return;
        }
        if (i == 1) {
            matrix.setRotate(270.0f);
            matrix.postTranslate(0.0f, i2);
            return;
        }
        if (i == 2) {
            matrix.setRotate(180.0f);
            matrix.postTranslate(i2, i3);
        } else if (i == 3) {
            matrix.setRotate(90.0f);
            matrix.postTranslate(i3, 0.0f);
        } else {
            throw new IllegalArgumentException("Unknown rotation: " + i);
        }
    }

    public static void transformLogicalToPhysicalCoordinates(int i, int i2, int i3, Matrix matrix) {
        if (i == 0) {
            matrix.reset();
            return;
        }
        if (i == 1) {
            matrix.setRotate(90.0f);
            matrix.preTranslate(0.0f, -i2);
            return;
        }
        if (i == 2) {
            matrix.setRotate(180.0f);
            matrix.preTranslate(-i2, -i3);
        } else if (i == 3) {
            matrix.setRotate(270.0f);
            matrix.preTranslate(-i3, 0.0f);
        } else {
            throw new IllegalArgumentException("Unknown rotation: " + i);
        }
    }

    public static void transformToRotation(int i, int i2, DisplayInfo displayInfo, Matrix matrix) {
        int i3 = displayInfo.rotation;
        boolean z = true;
        if (i3 != 1 && i3 != 3) {
            z = false;
        }
        int i4 = z ? displayInfo.logicalWidth : displayInfo.logicalHeight;
        int i5 = z ? displayInfo.logicalHeight : displayInfo.logicalWidth;
        Matrix matrix2 = new Matrix();
        transformLogicalToPhysicalCoordinates(i, i5, i4, matrix);
        transformPhysicalToLogicalCoordinates(i2, i5, i4, matrix2);
        matrix.postConcat(matrix2);
    }

    public static void transformToRotation(int i, int i2, int i3, int i4, Matrix matrix) {
        boolean z = true;
        if (i2 != 1 && i2 != 3) {
            z = false;
        }
        int i5 = z ? i3 : i4;
        if (z) {
            i3 = i4;
        }
        Matrix matrix2 = new Matrix();
        transformLogicalToPhysicalCoordinates(i, i3, i5, matrix);
        transformPhysicalToLogicalCoordinates(i2, i3, i5, matrix2);
        matrix.postConcat(matrix2);
    }

    public static void computeRotationMatrix(int i, int i2, int i3, Matrix matrix) {
        if (i == 0) {
            matrix.reset();
            return;
        }
        if (i == 1) {
            matrix.setRotate(90.0f);
            matrix.postTranslate(i3, 0.0f);
        } else if (i == 2) {
            matrix.setRotate(180.0f);
            matrix.postTranslate(i2, i3);
        } else {
            if (i != 3) {
                return;
            }
            matrix.setRotate(270.0f);
            matrix.postTranslate(0.0f, i2);
        }
    }
}
