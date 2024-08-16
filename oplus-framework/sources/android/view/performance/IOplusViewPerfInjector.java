package android.view.performance;

import android.view.View;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;

/* loaded from: classes.dex */
public interface IOplusViewPerfInjector {
    default void initView() {
    }

    default IOplusAdjustlayerType getOplusAdjustlayerTypeInstance() {
        return IOplusAdjustlayerType.DEFAULT;
    }

    default void checkBoostAnimation(Animation animation) {
    }

    default void checkBoostBuildDrawingCache() {
    }

    default void checkBoostTouchEvent(int action) {
    }

    default void checkBoostOnPerformClick(View.OnClickListener onClickListener) {
    }

    default void checkNeedBoostedPropertyAnimator(ViewPropertyAnimator animator) {
    }

    default void ignoreSpecailViewDescendantInvalidated(ViewParent p) {
    }

    default boolean isIgnoreSpecailViewDescendantInvalidated() {
        return false;
    }
}
