package com.oplus.util;

import android.content.Context;
import android.opengl.Matrix;
import android.provider.Settings;

/* loaded from: classes.dex */
public class OplusReduceSaturationUtil {
    private static final String DISPLAY_ADJUST_URI = "color_dispaly_adjust";
    private static final float[] DEFAULT_MATRIX_GRAYSCALE = {0.2126f, 0.2126f, 0.2126f, 0.0f, 0.7152f, 0.7152f, 0.7152f, 0.0f, 0.0722f, 0.0722f, 0.0722f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private static final float[] MATRIX_INVERT_COLOR = {-0.08247f, -0.08247f, -0.08247f, 0.0f, -0.08247f, -0.08247f, -0.08247f, 0.0f, -0.08247f, -0.08247f, -0.08247f, 0.0f, 0.31615f, 0.31615f, 0.31615f, 1.0f};

    private static float[] multiply(float[] matrix, float[] other) {
        if (matrix == null) {
            return other;
        }
        float[] result = new float[16];
        Matrix.multiplyMM(result, 0, matrix, 0, other, 0);
        return result;
    }

    public static void setReduceOn(Context context, Boolean checked) {
        String values;
        double[] matrix = new double[16];
        if (checked.booleanValue()) {
            int i = 0;
            while (true) {
                if (i >= DEFAULT_MATRIX_GRAYSCALE.length) {
                    break;
                }
                matrix[i] = r3[i];
                i++;
            }
            values = Double.toString(matrix[0]) + ",";
            for (int i2 = 1; i2 < matrix.length; i2++) {
                if (i2 != matrix.length - 1) {
                    values = values + Double.toString(matrix[i2]) + ",";
                } else {
                    values = values + Double.toString(matrix[i2]);
                }
            }
        } else {
            values = null;
        }
        Settings.System.putString(context.getContentResolver(), DISPLAY_ADJUST_URI, values);
    }

    public static void setInverseOn(Context context, Boolean checked) {
        String values;
        double[] matrix = new double[16];
        float[] matrix1 = multiply(DEFAULT_MATRIX_GRAYSCALE, MATRIX_INVERT_COLOR);
        if (checked.booleanValue()) {
            for (int i = 0; i < matrix1.length; i++) {
                matrix[i] = matrix1[i];
            }
            values = Double.toString(matrix[0]) + ",";
            for (int i2 = 1; i2 < matrix.length; i2++) {
                if (i2 != matrix.length - 1) {
                    values = values + Double.toString(matrix[i2]) + ",";
                } else {
                    values = values + Double.toString(matrix[i2]);
                }
            }
        } else {
            values = null;
        }
        Settings.System.putString(context.getContentResolver(), DISPLAY_ADJUST_URI, values);
    }
}
