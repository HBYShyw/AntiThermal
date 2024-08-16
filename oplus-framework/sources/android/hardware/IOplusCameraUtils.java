package android.hardware;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusCameraUtils extends IOplusCommonFeature {
    public static final IOplusCameraUtils DEFAULT = new IOplusCameraUtils() { // from class: android.hardware.IOplusCameraUtils.1
    };
    public static final String NAME = "IOplusCameraUtils";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCameraUtils;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default String getComponentName() {
        return null;
    }
}
