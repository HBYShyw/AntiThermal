package com.android.server.performance;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusPerformanceService extends IOplusCommonFeature {
    public static final IOplusPerformanceService DEFAULT = new IOplusPerformanceService() { // from class: com.android.server.performance.IOplusPerformanceService.1
    };
    public static final String NAME = "IOplusPerformanceService";

    default void disableSensorScreenShot(Context context) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusPerformanceService;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
