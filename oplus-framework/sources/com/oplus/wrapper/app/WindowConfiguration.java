package com.oplus.wrapper.app;

import android.graphics.Rect;

/* loaded from: classes.dex */
public class WindowConfiguration {
    private final android.app.WindowConfiguration mWindowConfiguration;
    public static final int WINDOWING_MODE_FULLSCREEN = getWindowingModeFullscreen();

    @Deprecated
    public static final int WINDOWING_MODE_SPLIT_SCREEN_PRIMARY = getWindowingModeSplitScreenPrimary();

    @Deprecated
    public static final int WINDOWING_MODE_SPLIT_SCREEN_SECONDARY = getWindowingModeSplitScreenSecondary();
    public static final int WINDOWING_MODE_UNDEFINED = getWindowingModeUndefined();
    public static final int WINDOWING_MODE_PINNED = getWindowingModePinned();
    public static final int WINDOWING_MODE_FREEFORM = getWindowingModeFreeform();
    public static final int WINDOWING_MODE_MULTI_WINDOW = getWindowingModeMultiWindow();

    public WindowConfiguration() {
        this.mWindowConfiguration = new android.app.WindowConfiguration();
    }

    public WindowConfiguration(android.app.WindowConfiguration windowConfiguration) {
        this.mWindowConfiguration = windowConfiguration;
    }

    private static int getWindowingModeFullscreen() {
        return 1;
    }

    private static int getWindowingModeSplitScreenPrimary() {
        return 0;
    }

    private static int getWindowingModeSplitScreenSecondary() {
        return 0;
    }

    private static int getWindowingModeUndefined() {
        return 0;
    }

    private static int getWindowingModePinned() {
        return 2;
    }

    private static int getWindowingModeFreeform() {
        return 5;
    }

    private static int getWindowingModeMultiWindow() {
        return 6;
    }

    public android.app.WindowConfiguration getmWindowConfiguration() {
        return this.mWindowConfiguration;
    }

    public Rect getMaxBounds() {
        return this.mWindowConfiguration.getMaxBounds();
    }

    public int getActivityType() {
        return this.mWindowConfiguration.getActivityType();
    }

    public Rect getAppBounds() {
        return this.mWindowConfiguration.getAppBounds();
    }

    public Rect getBounds() {
        return this.mWindowConfiguration.getBounds();
    }

    public int getRotation() {
        return this.mWindowConfiguration.getRotation();
    }

    public int getWindowingMode() {
        return this.mWindowConfiguration.getWindowingMode();
    }

    public void setAppBounds(Rect rect) {
        this.mWindowConfiguration.setAppBounds(rect);
    }

    public void setWindowingMode(int windowingMode) {
        this.mWindowConfiguration.setWindowingMode(windowingMode);
    }
}
