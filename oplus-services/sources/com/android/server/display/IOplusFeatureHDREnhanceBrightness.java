package com.android.server.display;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusFeatureHDREnhanceBrightness extends IOplusCommonFeature {
    public static final IOplusFeatureHDREnhanceBrightness DEFAULT = new IOplusFeatureHDREnhanceBrightness() { // from class: com.android.server.display.IOplusFeatureHDREnhanceBrightness.1
    };
    public static final String NAME = "IOplusFeatureHDREnhanceBrightness";

    default boolean enhanceBrightness(int i, int i2, int i3) {
        return false;
    }

    default int getBrightness(int i) {
        return 0;
    }

    default int getRate(int i) {
        return 0;
    }

    default void registerByNewImpl() {
    }

    default void unregisterByNewImpl() {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusFeatureHDREnhanceBrightness;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
