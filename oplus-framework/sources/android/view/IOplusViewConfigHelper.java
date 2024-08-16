package android.view;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusViewConfigHelper extends IOplusCommonFeature {
    public static final IOplusViewConfigHelper DEFAULT = new IOplusViewConfigHelper() { // from class: android.view.IOplusViewConfigHelper.1
    };

    default IOplusViewConfigHelper getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusViewConfigHelper;
    }

    default int getScaledOverscrollDistance(int dist) {
        return dist;
    }

    default int getScaledOverflingDistance(int dist) {
        return dist;
    }

    default int calcRealOverScrollDist(int dist, int scrollY) {
        return dist;
    }

    default int calcRealOverScrollDist(int dist, int scrollY, int range) {
        return dist;
    }

    default void setOptEnable(boolean enable) {
    }

    default void setForceUsingSpring(boolean enable) {
    }
}
