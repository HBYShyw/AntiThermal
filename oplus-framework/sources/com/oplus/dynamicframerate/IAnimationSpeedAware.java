package com.oplus.dynamicframerate;

import android.os.Handler;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public interface IAnimationSpeedAware {
    public static final int STATE_FLING_ANIMATION = 3;
    public static final int STATE_IDLE = 0;
    public static final int STATE_INPUT_IDLE = 1;
    public static final int STATE_SCROLL_ANIMATION = 2;
    public static final int STATE_SCROLL_BAR_FADE_ANIMATION = 4;
    public static final int STATE_WINDOW_ANIMATION = 5;

    default void onEventHandled(Object vri, MotionEvent ev) {
    }

    default void onFlingStart(int duration, int velocity, int position) {
    }

    default void onFlingPositionUpdate(int velocity, int position) {
    }

    default void onFlingStateUpdate(int state) {
    }

    default void onFlingFinish() {
    }

    default void onScrollBarFadeStart(int duration) {
    }

    default void onScrollBarFadeEnd() {
    }

    default void onScrollChanged(int l, int t, int oldl, int oldt) {
    }

    default void onSetMatrix(SurfaceControl sc, float[] float9) {
    }

    default void onDeliverInputEvent(InputEvent event) {
    }

    default void syncInfoTogether() {
    }

    default void setHandler(Handler handler) {
    }

    default void handleCancelState(int state) {
    }

    default void updateScreenSize(int width, int height) {
    }

    default void resetAnimationInfo() {
    }
}
