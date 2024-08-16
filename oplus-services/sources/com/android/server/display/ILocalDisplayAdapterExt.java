package com.android.server.display;

import android.content.Context;
import android.os.Handler;
import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ILocalDisplayAdapterExt {
    public static final float DEFAULT_SCALE = 1.0f;

    default float brightnessToBacklight(long j, float f) {
        return 0.0f;
    }

    default float brightnessToColor(long j, float f) {
        return 0.0f;
    }

    default float brightnessToNits(float f) {
        return 0.0f;
    }

    default float brightnessToNits(long j, float f) {
        return 0.0f;
    }

    default int findDisplayModeIdByPolicy(boolean z, int i, int i2, int i3) {
        return i3;
    }

    default float getAodBrightness() {
        return 0.0f;
    }

    default float getBrightnessFromNit(long j, float f) {
        return 0.0f;
    }

    default float getDefaultDisplayBrightness(long j) {
        return 0.0f;
    }

    default int getEdrType() {
        return 0;
    }

    default float getEnhanceDolbyOriginNit(long j, float f, float f2) {
        return 0.0f;
    }

    default float getEnhanceDolbyScale(float f, float f2, long j) {
        return 1.0f;
    }

    default float getMaxBrightness(long j) {
        return 0.0f;
    }

    default Handler getOPlusRefreshRateHandler(Handler handler) {
        return handler;
    }

    default float getPowerOnRealTimeBrightness(boolean z, float f) {
        return f;
    }

    default int getSecondaryLcdDensity() {
        return 0;
    }

    default float getTotalDisplayBrightness(long j) {
        return 0.0f;
    }

    default boolean hasRemapDisable() {
        return false;
    }

    default void init(Context context) {
    }

    default boolean isAnimating(long j) {
        return false;
    }

    default boolean isLongTakeAodToOn(int i, int i2, long j) {
        return false;
    }

    default boolean needCaculateScale(long j) {
        return false;
    }

    default void notifyBacklightAnimFinished(float f) {
    }

    default void requestDisplayState(boolean z, int i) {
    }

    default void setCurrentEdrEnhanceScale(float f) {
    }

    default void setDisplayInfoDpi(DisplayDeviceInfo displayDeviceInfo, long j) {
    }

    default void setDisplayPowerModeFinished(boolean z, int i) {
    }

    default void setPrimaryPhysicalDisplayId(long j) {
    }

    default void setStaticDisplayDensity(SurfaceControl.StaticDisplayInfo staticDisplayInfo, long j) {
    }

    default void setSwitchingTrackerPowerEventLog(int i, boolean z) {
    }

    default void updateDCLayerState(int i) {
    }

    default void updateDisplayModes(boolean z, long j) {
    }

    default void updateScreenBrightnessProvider(float f, int i, float f2, long j, boolean z, int i2, int i3, int i4) {
    }
}
