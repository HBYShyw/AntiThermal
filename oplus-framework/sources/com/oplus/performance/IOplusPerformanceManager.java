package com.oplus.performance;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusPerformanceManager extends IOplusCommonFeature {
    public static final IOplusPerformanceManager DEFAULT = new IOplusPerformanceManager() { // from class: com.oplus.performance.IOplusPerformanceManager.1
    };
    public static final String NAME = "IOplusPerformanceManager";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusPerformanceManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void ormsAction(Class clazz, String scene, String action, int timeout) {
    }
}
