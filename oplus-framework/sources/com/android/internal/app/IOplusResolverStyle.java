package com.android.internal.app;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusResolverStyle extends IOplusCommonFeature {
    public static final IOplusResolverStyle DEFAULT = new IOplusResolverStyle() { // from class: com.android.internal.app.IOplusResolverStyle.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusResolverStyle;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default int getActivityStartAnimationRes() {
        return 0;
    }

    default int getActivityEndAnimationRes() {
        return 0;
    }
}
