package android.hardware.camera2;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusCamera2StatisticsManager extends IOplusCommonFeature {
    public static final IOplusCamera2StatisticsManager DEFAULT = new IOplusCamera2StatisticsManager() { // from class: android.hardware.camera2.IOplusCamera2StatisticsManager.1
    };
    public static final String NAME = "IOplusCamera2StatisticsManager";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCamera2StatisticsManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
