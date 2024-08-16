package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;

/* loaded from: classes.dex */
public interface IOplusOverScrollerHelper extends IOplusCommonFeature {
    public static final IOplusOverScrollerHelper DEFAULT = new IOplusOverScrollerHelper() { // from class: android.widget.IOplusOverScrollerHelper.1
    };

    default IOplusOverScrollerHelper getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusOverScrollerHelper;
    }

    default int getFinalX(int x) {
        return x;
    }

    default int getFinalY(int y) {
        return y;
    }

    default boolean setFriction(float friction) {
        return false;
    }

    default boolean isFinished(boolean finished) {
        return finished;
    }

    default int getCurrX(int x) {
        return x;
    }

    default int getCurrY(int y) {
        return y;
    }

    default void setOptEnable(boolean enable) {
    }

    default boolean isForceUsingSpring(Context context, boolean optHelperEnable) {
        return false;
    }

    default boolean isForSpringOverScroller() {
        return false;
    }
}
