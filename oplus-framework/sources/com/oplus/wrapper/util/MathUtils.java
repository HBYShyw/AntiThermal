package com.oplus.wrapper.util;

/* loaded from: classes.dex */
public class MathUtils {
    private MathUtils() {
    }

    public static float constrainedMap(float rangeMin, float rangeMax, float valueMin, float valueMax, float value) {
        return android.util.MathUtils.constrainedMap(rangeMin, rangeMax, valueMin, valueMax, value);
    }

    public static float lerp(int start, int stop, float amount) {
        return android.util.MathUtils.lerp(start, stop, amount);
    }

    public static float log(float a) {
        return android.util.MathUtils.log(a);
    }

    public static float max(float a, float b) {
        return android.util.MathUtils.max(a, b);
    }
}
