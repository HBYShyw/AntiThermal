package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class OplusAccelerateDecelerateInterpolator extends BaseInterpolator {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "OplusAccelerateDecelerateInterpolator";

    public OplusAccelerateDecelerateInterpolator() {
    }

    public OplusAccelerateDecelerateInterpolator(Context context, AttributeSet attrs) {
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float input) {
        if (input < 0.5f) {
            float interpolation = (float) ((Math.cos((Math.sin((input * 3.141592653589793d) / 2.0d) + 1.0d) * 3.141592653589793d) + 1.0d) / 2.0d);
            return interpolation;
        }
        float interpolation2 = (float) ((Math.cos((Math.sqrt(input) + 1.0d) * 3.141592653589793d) + 1.0d) / 2.0d);
        return interpolation2;
    }
}
