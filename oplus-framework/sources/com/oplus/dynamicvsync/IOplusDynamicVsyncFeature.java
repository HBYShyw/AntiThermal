package com.oplus.dynamicvsync;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusDynamicVsyncFeature extends IOplusCommonFeature {
    public static final IOplusDynamicVsyncFeature DEFAULT = new IOplusDynamicVsyncFeature() { // from class: com.oplus.dynamicvsync.IOplusDynamicVsyncFeature.1
    };
    public static final String NAME = "IOplusDynamicVsyncFeature";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusDynamicVsyncFeature;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void doAnimation(int durationInMs, String detail) {
    }

    default void flingEvent(String pkgName, int durationInMs) {
    }
}
