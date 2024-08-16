package com.android.server.display.marvels.module;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.os.Handler;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IORBrightnessMarvelsDataCollector extends IOplusCommonFeature {
    public static final float BAD_VALUE = -1.0f;
    public static final IORBrightnessMarvelsDataCollector DEFAULT = new IORBrightnessMarvelsDataCollector() { // from class: com.android.server.display.marvels.module.IORBrightnessMarvelsDataCollector.1
    };
    public static final String NAME = "IORBrightnessMarvelsDataCollector";

    default void keepTheCoreProgramRelease(Object obj) {
    }

    default void release(Object obj) {
    }

    default void setBrightnessMode(int i) {
    }

    default void setCurrentBrightness(int i) {
    }

    default void setDarkestBaseBrightness(float f) {
    }

    default void setDrag(boolean z) {
    }

    default void setEyesProtect(int i) {
    }

    default void setLowPower(int i) {
    }

    default void setLux(float f) {
    }

    default void setMarvelsHandler(Object obj, Handler handler) {
    }

    default void setMotion(String str) {
    }

    default void setReason(int i) {
    }

    default void setStableLux(float f) {
    }

    default void setTargetBrightness(int i) {
    }

    default void setTimerTask(String str) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IORBrightnessMarvelsDataCollector;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
