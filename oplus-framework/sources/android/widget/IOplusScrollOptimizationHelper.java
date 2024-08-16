package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.view.animation.Interpolator;

/* loaded from: classes.dex */
public interface IOplusScrollOptimizationHelper extends IOplusCommonFeature {
    public static final IOplusScrollOptimizationHelper DEFAULT = new IOplusScrollOptimizationHelper() { // from class: android.widget.IOplusScrollOptimizationHelper.1
    };

    default IOplusScrollOptimizationHelper getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusScrollOptimizationHelper;
    }

    default boolean enable() {
        return false;
    }

    default boolean interpolatorValid() {
        return false;
    }

    default Interpolator getInterpolator() {
        return null;
    }

    default void setFlingParam(long now, float currVelo, int velo, boolean finished) {
    }

    default double getCustomizedDurationCoef(int velo) {
        return 1.0d;
    }

    default double getCustomizedDistanceCoef(int velo) {
        return 1.0d;
    }

    default void resetVeloAccuCount() {
    }

    default void saveCurrVeloAccuCount() {
    }

    default void setAbortTime(long time) {
    }

    default float[] getInterpolatorValues() {
        return null;
    }
}
