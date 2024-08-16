package com.android.server.display.mode;

import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class Vote {
    static final int APP_REQUEST_REFRESH_RATE_RANGE_PRIORITY_CUTOFF = 4;
    static final int INVALID_SIZE = -1;
    static final int MAX_PRIORITY = 14;
    static final int MIN_PRIORITY = 0;
    static final int PRIORITY_APP_REQUEST_BASE_MODE_REFRESH_RATE = 5;
    static final int PRIORITY_APP_REQUEST_RENDER_FRAME_RATE_RANGE = 4;
    static final int PRIORITY_APP_REQUEST_SIZE = 6;
    static final int PRIORITY_AUTH_OPTIMIZER_RENDER_FRAME_RATE = 8;
    static final int PRIORITY_DEFAULT_RENDER_FRAME_RATE = 0;
    static final int PRIORITY_FLICKER_REFRESH_RATE = 1;
    static final int PRIORITY_FLICKER_REFRESH_RATE_SWITCH = 11;
    static final int PRIORITY_HIGH_BRIGHTNESS_MODE = 2;
    static final int PRIORITY_LAYOUT_LIMITED_FRAME_RATE = 9;
    static final int PRIORITY_LOW_POWER_MODE = 10;
    static final int PRIORITY_PROXIMITY = 13;
    static final int PRIORITY_SKIN_TEMPERATURE = 12;
    static final int PRIORITY_UDFPS = 14;
    static final int PRIORITY_USER_SETTING_MIN_RENDER_FRAME_RATE = 3;
    static final int PRIORITY_USER_SETTING_PEAK_RENDER_FRAME_RATE = 7;
    public final float appRequestBaseModeRefreshRate;
    public final boolean disableRefreshRateSwitching;
    public final int height;
    public final SurfaceControl.RefreshRateRanges refreshRateRanges;
    public final int width;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Vote forPhysicalRefreshRates(float f, float f2) {
        return new Vote(-1, -1, f, f2, 0.0f, Float.POSITIVE_INFINITY, f == f2, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Vote forRenderFrameRates(float f, float f2) {
        return new Vote(-1, -1, 0.0f, Float.POSITIVE_INFINITY, f, f2, false, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Vote forSize(int i, int i2) {
        return new Vote(i, i2, 0.0f, Float.POSITIVE_INFINITY, 0.0f, Float.POSITIVE_INFINITY, false, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Vote forDisableRefreshRateSwitching() {
        return new Vote(-1, -1, 0.0f, Float.POSITIVE_INFINITY, 0.0f, Float.POSITIVE_INFINITY, true, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Vote forBaseModeRefreshRate(float f) {
        return new Vote(-1, -1, 0.0f, Float.POSITIVE_INFINITY, 0.0f, Float.POSITIVE_INFINITY, false, f);
    }

    private Vote(int i, int i2, float f, float f2, float f3, float f4, boolean z, float f5) {
        this.width = i;
        this.height = i2;
        this.refreshRateRanges = new SurfaceControl.RefreshRateRanges(new SurfaceControl.RefreshRateRange(f, f2), new SurfaceControl.RefreshRateRange(f3, f4));
        this.disableRefreshRateSwitching = z;
        this.appRequestBaseModeRefreshRate = f5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String priorityToString(int i) {
        switch (i) {
            case 0:
                return "PRIORITY_DEFAULT_REFRESH_RATE";
            case 1:
                return "PRIORITY_FLICKER_REFRESH_RATE";
            case 2:
                return "PRIORITY_HIGH_BRIGHTNESS_MODE";
            case 3:
                return "PRIORITY_USER_SETTING_MIN_RENDER_FRAME_RATE";
            case 4:
                return "PRIORITY_APP_REQUEST_RENDER_FRAME_RATE_RANGE";
            case 5:
                return "PRIORITY_APP_REQUEST_BASE_MODE_REFRESH_RATE";
            case 6:
                return "PRIORITY_APP_REQUEST_SIZE";
            case 7:
                return "PRIORITY_USER_SETTING_PEAK_RENDER_FRAME_RATE";
            case 8:
                return "PRIORITY_AUTH_OPTIMIZER_RENDER_FRAME_RATE";
            case 9:
                return "PRIORITY_LAYOUT_LIMITED_FRAME_RATE";
            case 10:
                return "PRIORITY_LOW_POWER_MODE";
            case 11:
                return "PRIORITY_FLICKER_REFRESH_RATE_SWITCH";
            case 12:
                return "PRIORITY_SKIN_TEMPERATURE";
            case 13:
                return "PRIORITY_PROXIMITY";
            case 14:
                return "PRIORITY_UDFPS";
            default:
                return Integer.toString(i);
        }
    }

    public String toString() {
        return "Vote: {width: " + this.width + ", height: " + this.height + ", refreshRateRanges: " + this.refreshRateRanges + ", disableRefreshRateSwitching: " + this.disableRefreshRateSwitching + ", appRequestBaseModeRefreshRate: " + this.appRequestBaseModeRefreshRate + "}";
    }
}
