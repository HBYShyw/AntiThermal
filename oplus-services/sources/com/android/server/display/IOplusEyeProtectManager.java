package com.android.server.display;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import com.android.server.display.color.DisplayTransformManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusEyeProtectManager extends IOplusCommonFeature {
    public static final IOplusEyeProtectManager DEFAULT = new IOplusEyeProtectManager() { // from class: com.android.server.display.IOplusEyeProtectManager.1
    };
    public static final int LEVEL_COLOR_MATRIX_COLOR = 400;

    default boolean needResetAnimationScaleSetting(Context context, int i) {
        return false;
    }

    default void setUp(Context context, int i) {
    }

    default void tearDown() {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusEyeProtectManager;
    }

    default IOplusEyeProtectManager getDefault() {
        return DEFAULT;
    }

    default void setColorMatrix(int i, float[] fArr, DisplayTransformManager displayTransformManager) {
        displayTransformManager.setColorMatrix(i, fArr);
    }
}
