package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class OplusDecelerateInterpolator extends BaseInterpolator {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "OplusDecelerateInterpolator";

    public OplusDecelerateInterpolator() {
    }

    public OplusDecelerateInterpolator(Context context, AttributeSet attrs) {
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float input) {
        float interpolation = (float) (1.0199999809265137d - (1.0199999809265137d / ((Math.pow(input, 2.0d) * 50.0d) + 1.0d)));
        return interpolation;
    }
}
