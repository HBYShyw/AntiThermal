package com.android.server.display;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusFeatureBrightnessBarController extends IOplusCommonFeature {
    public static final float BAD_VALUE = -1.0f;
    public static final IOplusFeatureBrightnessBarController DEFAULT = new IOplusFeatureBrightnessBarController() { // from class: com.android.server.display.IOplusFeatureBrightnessBarController.1
    };
    public static final String NAME = "IOplusFeatureBrightnessBarController";

    default float getBrightnessBarScale() {
        return 1.0f;
    }

    default String getFeatureBrightnessBarControllerState(float f) {
        return "";
    }

    default int getOriginalBrightnessState(int i, boolean z) {
        return i;
    }

    default float getUpdateBrightnessState(float f) {
        return f;
    }

    default boolean isCustomDarkLevel(float f, float f2) {
        return false;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusFeatureBrightnessBarController;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void setLux(float f) {
        Slog.w(NAME, "subclass not yet constructed");
    }

    default void setReason(int i) {
        Slog.w(NAME, "subclass not yet constructed");
    }

    default void onScreenEvent(boolean z) {
        Slog.w(NAME, "subclass not yet constructed");
    }

    default void brightnessModeOnChange(int i) {
        Slog.w(NAME, "subclass not yet constructed");
    }

    default void setAnimating(boolean z, boolean z2) {
        Slog.w(NAME, "subclass not yet constructed");
    }

    default void setCameraMode(boolean z) {
        Slog.w(NAME, "subclass not yet constructed");
    }

    default void setFeatureOn(boolean z) {
        Slog.w(NAME, "subclass not yet constructed");
    }
}
