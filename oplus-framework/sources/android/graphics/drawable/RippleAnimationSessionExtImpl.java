package android.graphics.drawable;

import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

/* loaded from: classes.dex */
public class RippleAnimationSessionExtImpl implements IRippleAnimationSessionExt {
    private static final int ENTER_ANIM_DURATION = 233;
    private static final int EXIT_ANIM_DURATION = 200;
    private static final float INTERPOLATOR_ENTER_X1 = 0.17f;
    private static final float INTERPOLATOR_ENTER_X2 = 0.67f;
    private static final float INTERPOLATOR_ENTER_Y1 = 0.17f;
    private static final float INTERPOLATOR_ENTER_Y2 = 1.0f;
    private static final float INTERPOLATOR_EXIT_X1 = 0.33f;
    private static final float INTERPOLATOR_EXIT_X2 = 0.83f;
    private static final float INTERPOLATOR_EXIT_Y1 = 0.0f;
    private static final float INTERPOLATOR_EXIT_Y2 = 0.83f;
    private final Interpolator mEnterAnimationInterpolator = new PathInterpolator(0.17f, 0.17f, INTERPOLATOR_ENTER_X2, 1.0f);
    private final Interpolator mExitAnimationInterpolator = new PathInterpolator(INTERPOLATOR_EXIT_X1, 0.0f, 0.83f, 0.83f);

    public RippleAnimationSessionExtImpl(Object base) {
    }

    public int getEnterAnimationDuration() {
        return 233;
    }

    public int getExitAnimationDuration() {
        return 200;
    }

    public Interpolator getEnterAnimationInterpolator() {
        return this.mEnterAnimationInterpolator;
    }

    public Interpolator getExitAnimationInterpolator() {
        return this.mExitAnimationInterpolator;
    }
}
