package com.oplus.wrapper.graphics;

/* loaded from: classes.dex */
public class ColorUtils {
    private ColorUtils() {
    }

    public static int LABToColor(double l, double a, double b) {
        return com.android.internal.graphics.ColorUtils.LABToColor(l, a, b);
    }

    public static void colorToLAB(int color, double[] outLab) {
        com.android.internal.graphics.ColorUtils.colorToLAB(color, outLab);
    }

    public static int CAMToColor(float hue, float chroma, float lstar) {
        return com.android.internal.graphics.ColorUtils.CAMToColor(hue, chroma, lstar);
    }
}
