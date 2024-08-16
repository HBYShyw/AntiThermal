package android.hardware;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusCameraStatisticsManager extends IOplusCommonFeature {
    public static final IOplusCameraStatisticsManager DEFAULT = new IOplusCameraStatisticsManager() { // from class: android.hardware.IOplusCameraStatisticsManager.1
    };
    public static final String NAME = "IOplusCameraStatisticsManager";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCameraStatisticsManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
