package com.android.server.display;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IRampAnimatorExt {
    default void animateRun(float f, float f2, float f3, float f4) {
    }

    default boolean animateTo(int i, float f, float f2, float f3, boolean z) {
        return false;
    }

    default float getAmount(int i, float f, float f2, float f3, float f4, float f5, boolean z) {
        return 0.0f;
    }

    default boolean getBrightnessNoAnimation() {
        return false;
    }

    default boolean getEdrHdrBrightnessNoAnimation() {
        return false;
    }

    default boolean getEdrSdrBrightnessNoAnimation() {
        return false;
    }

    default void onAnimationEnd(float f) {
    }

    default void onAnimationStart(float f, float f2) {
    }

    default void setBrightnessNoAnimation(boolean z) {
    }

    default void setDisplayId(int i) {
    }

    default void setLoggingEnabled(boolean z) {
    }

    default void setValue(int i, float f) {
    }

    default boolean updateRealTimeBrightness(int i, boolean z, float f) {
        return false;
    }
}
