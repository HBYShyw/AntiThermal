package com.oplus.screenmode;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.pm.ApplicationInfo;
import android.content.res.CompatibilityInfo;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.DisplayInfo;

/* loaded from: classes.dex */
public interface IOplusAutoResolutionFeature extends IOplusCommonFeature {
    public static final IOplusAutoResolutionFeature DEFAULT = new IOplusAutoResolutionFeature() { // from class: com.oplus.screenmode.IOplusAutoResolutionFeature.1
    };
    public static final String NAME = "IOplusAutoResolutionFeature";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusAutoResolutionFeature;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void overrideDisplayMetricsIfNeed(DisplayMetrics inoutDm) {
    }

    default void applyCompatInfo(CompatibilityInfo compatInfo, DisplayMetrics outMetrics) {
    }

    default void updateCompatDensityIfNeed(int density) {
    }

    default boolean isDisplayCompat(String packageName, int uid) {
        return false;
    }

    default boolean supportDisplayCompat() {
        return false;
    }

    default int displayCompatDensity(int density) {
        return density;
    }

    default void initDisplayCompat(ApplicationInfo appInfo, CompatibilityInfo compatInfo) {
    }

    default void updateCompatRealSize(DisplayInfo displayInfo, Point outSize) {
    }

    default CompatibilityInfo getCompatibilityInfo() {
        return null;
    }
}
