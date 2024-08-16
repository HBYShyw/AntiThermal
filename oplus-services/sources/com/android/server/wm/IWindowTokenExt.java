package com.android.server.wm;

import android.content.res.Configuration;
import android.view.DisplayInfo;
import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowTokenExt {
    public static final float SCALE_DEFAULT = 1.0f;

    default boolean autoResolutionEnable(WindowToken windowToken) {
        return false;
    }

    default boolean canAssigFingerPrintLayer(WindowToken windowToken, int i) {
        return false;
    }

    default float getCurrScale() {
        return 1.0f;
    }

    default float getScale() {
        return 1.0f;
    }

    default boolean judgeWindowModeZoom() {
        return false;
    }

    default void makeSurface(SurfaceControl.Builder builder, int i) {
    }

    default void onConfigurationChanged(Configuration configuration) {
    }

    default void recoveryFixedRotationConfig(WindowToken windowToken, Configuration configuration) {
    }

    default void resolveScreenConfigInSecondary(WindowToken windowToken, Configuration configuration, DisplayInfo displayInfo) {
    }

    default void setCurrScale(float f) {
    }

    default void setScale(float f) {
    }

    default void updateSurfaceIfNeed() {
    }
}
