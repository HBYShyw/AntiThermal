package com.android.server.wm;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.res.Configuration;
import android.view.WindowManager;
import android.window.ClientWindowFrames;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusCarModeManager extends IOplusCommonFeature {
    public static final IOplusCarModeManager DEFAULT = new IOplusCarModeManager() { // from class: com.android.server.wm.IOplusCarModeManager.1
    };
    public static final String NAME = "IOplusCarModeManager";

    default void addWindowLw(WindowState windowState, WindowManager.LayoutParams layoutParams) {
    }

    default void adjustScreenAppConfigurationForCarLink(DisplayContent displayContent, Configuration configuration, float f) {
    }

    default void adjustScreenConfigurationForCarLink(DisplayContent displayContent, Configuration configuration, float f) {
    }

    default void adjustWindowFrameForCarDockBarInsets(DisplayContent displayContent, WindowState windowState, ClientWindowFrames clientWindowFrames) {
    }

    default boolean isCarDockBar(WindowManager.LayoutParams layoutParams) {
        return false;
    }

    default void layoutCarDockBar(DisplayContent displayContent, DisplayFrames displayFrames) {
    }

    default int validateAddingWindowLw(Context context, WindowManager.LayoutParams layoutParams, int i, int i2) {
        return -10;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCarModeManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
