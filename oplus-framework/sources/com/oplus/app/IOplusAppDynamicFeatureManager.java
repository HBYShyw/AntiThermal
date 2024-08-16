package com.oplus.app;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.view.View;

/* loaded from: classes.dex */
public interface IOplusAppDynamicFeatureManager extends IOplusCommonFeature {
    public static final IOplusAppDynamicFeatureManager DEFAULT = new IOplusAppDynamicFeatureManager() { // from class: com.oplus.app.IOplusAppDynamicFeatureManager.1
    };

    default IOplusAppDynamicFeatureManager getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusAppDynamicFeatureManager;
    }

    default void parseAppDynamicInfo(String packageName, String activityName, View view) {
    }
}
