package com.android.server.display;

import android.util.MathUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BrightnessUtils {
    private static final float A = 0.17883277f;
    private static final float B = 0.28466892f;
    private static final float C = 0.5599107f;
    private static final float R = 0.5f;

    public static final float convertGammaToLinear(float f) {
        float exp;
        if (f <= R) {
            exp = MathUtils.sq(f / R);
        } else {
            exp = MathUtils.exp((f - C) / A) + B;
        }
        return MathUtils.constrain(exp, 0.0f, 12.0f) / 12.0f;
    }

    public static final float convertLinearToGamma(float f) {
        float f2 = f * 12.0f;
        if (f2 <= 1.0f) {
            return MathUtils.sqrt(f2) * R;
        }
        return (MathUtils.log(f2 - B) * A) + C;
    }
}
