package com.oplus.resolver;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.android.internal.graphics.ColorUtils;

/* loaded from: classes.dex */
public class COUIPressFeedbackUtil {
    private static final float BIG_CARD_GUARANTEE_VALUE_THRESHOLD_PERCENTAGE = 0.07f;
    private static final int DEFAULT_FLOATING_BUTTON_HEIGHT = 156;
    private static final float DEFAULT_GUARANTEE_VALUE_THRESHOLD_PERCENTAGE = 0.1f;
    private static final int DEFAULT_PRESS_FEEDBACK_ANIMATION_DURATION = 200;
    private static final float DEFAULT_PRESS_FEEDBACK_BRIGHTNESS_MAX_VALUE = 1.0f;
    private static final float DEFAULT_PRESS_FEEDBACK_BRIGHTNESS_MIN_VALUE = 0.8f;
    private static final float DEFAULT_PRESS_FEEDBACK_SCALE_MAX_VALUE = 1.0f;
    private static final float DEFAULT_PRESS_FEEDBACK_SCALE_MIN_VALUE = 0.9f;
    private static final int DEFAULT_RELEASE_ANIMATION_DURATION = 340;
    private static final int DEFAULT_TARGET_GUARANTEED_VALUE_THRESHOLD_HEIGHT = 600;
    private static final PathInterpolator PRESS_FEEDBACK_INTERPOLATOR = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
    private static final String PROPERTY_BRIGHTNESS = "brightness";
    private static final String PROPERTY_SCALE_X = "scaleX";
    private static final String PROPERTY_SCALE_Y = "scaleY";
    private static final float SMALL_CARD_GUARANTEE_VALUE_THRESHOLD_PERCENTAGE = 0.35f;

    public static ValueAnimator generatePressAnimation(View target, Animator.AnimatorListener listener) {
        return createAnim(target, 1.0f, DEFAULT_PRESS_FEEDBACK_BRIGHTNESS_MIN_VALUE, 1.0f, DEFAULT_PRESS_FEEDBACK_SCALE_MIN_VALUE, 1.0f, DEFAULT_PRESS_FEEDBACK_SCALE_MIN_VALUE, 200L, PRESS_FEEDBACK_INTERPOLATOR, listener);
    }

    public static ValueAnimator generateResumeAnimation(View target, float animationStartValue, Animator.AnimatorListener listener) {
        return createAnim(target, (DEFAULT_PRESS_FEEDBACK_BRIGHTNESS_MIN_VALUE * animationStartValue) / DEFAULT_PRESS_FEEDBACK_SCALE_MIN_VALUE, 1.0f, animationStartValue, 1.0f, animationStartValue, 1.0f, 340L, PRESS_FEEDBACK_INTERPOLATOR, listener);
    }

    private static ValueAnimator createAnim(final View target, float brightnessStartValue, float brightnessEndValue, float scaleXStartValue, float scaleXEndValue, float scaleYStartValue, float scaleYEndValue, long duration, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        if (target == null) {
            throw new IllegalArgumentException("The given view is empty. Please provide a valid view.");
        }
        PropertyValuesHolder brightnessProperty = PropertyValuesHolder.ofFloat(PROPERTY_BRIGHTNESS, brightnessStartValue, brightnessEndValue);
        PropertyValuesHolder scaleXProperty = PropertyValuesHolder.ofFloat(PROPERTY_SCALE_X, scaleXStartValue, scaleXEndValue);
        PropertyValuesHolder scaleYProperty = PropertyValuesHolder.ofFloat(PROPERTY_SCALE_Y, scaleYStartValue, scaleYEndValue);
        ValueAnimator anim = ObjectAnimator.ofPropertyValuesHolder(brightnessProperty, scaleXProperty, scaleYProperty);
        anim.setDuration(duration);
        anim.setInterpolator(interpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.resolver.COUIPressFeedbackUtil.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                float brightnessValue = ((Float) animation.getAnimatedValue(COUIPressFeedbackUtil.PROPERTY_BRIGHTNESS)).floatValue();
                float scaleXValue = ((Float) animation.getAnimatedValue(COUIPressFeedbackUtil.PROPERTY_SCALE_X)).floatValue();
                float scaleYValue = ((Float) animation.getAnimatedValue(COUIPressFeedbackUtil.PROPERTY_SCALE_Y)).floatValue();
                target.setScaleX(scaleXValue);
                target.setScaleY(scaleYValue);
                ColorStateList tintList = target.getBackgroundTintList();
                if (tintList != null) {
                    int color = tintList.getDefaultColor();
                    target.getBackground().setTint(COUIPressFeedbackUtil.getBrightnessColor(color, brightnessValue));
                }
            }
        });
        if (listener != null) {
            anim.addListener(listener);
        }
        return anim;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getBrightnessColor(int color, float brightness) {
        ColorUtils.colorToHSL(color, mColorHsl);
        float[] mColorHsl = {0.0f, 0.0f, mColorHsl[2] * brightness};
        int newColor = ColorUtils.HSLToColor(mColorHsl);
        int r = Math.min(255, Color.red(newColor));
        int g = Math.min(255, Color.green(newColor));
        int b = Math.min(255, Color.blue(newColor));
        int a = Color.alpha(newColor);
        return Color.argb(a, r, g, b);
    }

    public static ValueAnimator generatePressAnimationRecord() {
        ValueAnimator pressAnimationRecord = ValueAnimator.ofFloat(1.0f, DEFAULT_PRESS_FEEDBACK_SCALE_MIN_VALUE);
        pressAnimationRecord.setDuration(200L);
        pressAnimationRecord.setInterpolator(PRESS_FEEDBACK_INTERPOLATOR);
        return pressAnimationRecord;
    }

    public static float getGuaranteedAnimationValue(View target) {
        if (target == null) {
            throw new IllegalArgumentException("The given view is empty. Please provide a valid view.");
        }
        if (target.getHeight() >= 600) {
            return 0.993f;
        }
        if (target.getHeight() >= 156) {
            return 0.965f;
        }
        return 0.99f;
    }

    public static void cancelAnim(ValueAnimator animator) {
        if (animator != null && animator.isRunning()) {
            animator.end();
        }
    }
}
