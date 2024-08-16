package com.android.server.display;

import android.content.Context;
import android.os.SystemProperties;
import android.util.MathUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class OplusPixelworksHelper {
    private final String TAG = "OplusPixelworksHelper";
    private Context mContext;
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String KEY_BRIGHTNESS_SMOOTH_SUPPORT = "ro.oplus.display.brightness.smooth";
    public static boolean sSupportBrightnessSmooth = SystemProperties.getBoolean(KEY_BRIGHTNESS_SMOOTH_SUPPORT, false);
    private static OplusPixelworksHelper sOplusPixelworksHelper = null;
    private static final String KEY_MAX_BRIGHTNESS = "ro.oplus.display.brightness.max_brightness";
    private static final float sMaxBrightness = SystemProperties.getInt(KEY_MAX_BRIGHTNESS, 10238);
    private static final String KEY_NORMAL_MAX_BRIGHTNESS = "ro.oplus.display.brightness.normal_max_brightness";
    private static final float sNormalMaxBrightness = SystemProperties.getInt(KEY_NORMAL_MAX_BRIGHTNESS, IOplusDisplayPowerControllerExt.MAX_BRIGHTNESS);
    private static final String KEY_DEFAULT_BRIGHTNESS = "ro.oplus.display.brightness.default_brightness";
    private static final float sDefaultBrightness = SystemProperties.getInt(KEY_DEFAULT_BRIGHTNESS, 3758);
    private static final String KEY_ENGINEERINGMODE_BRIGHTNESS_20P = "ro.display.brightness.mode.exp.per_20";
    private static final float sEngineeringMode20P = SystemProperties.getInt(KEY_ENGINEERINGMODE_BRIGHTNESS_20P, -1);

    public static float convertOverrideBrightness(float f, int i) {
        return f;
    }

    public static OplusPixelworksHelper getInstance(Context context) {
        OplusPixelworksHelper oplusPixelworksHelper = sOplusPixelworksHelper;
        if (oplusPixelworksHelper == null && oplusPixelworksHelper == null) {
            sOplusPixelworksHelper = new OplusPixelworksHelper(context);
        }
        return sOplusPixelworksHelper;
    }

    private OplusPixelworksHelper(Context context) {
        this.mContext = context;
    }

    public static boolean isSupportBrightnessSmooth() {
        return sSupportBrightnessSmooth;
    }

    public static float getMaxBrightness() {
        return sMaxBrightness;
    }

    public static float getNormalMaxBrightness() {
        return sNormalMaxBrightness;
    }

    public static float getDefaultBrightness() {
        return sDefaultBrightness;
    }

    public static float getEngineeringModeBrightness20P() {
        return sEngineeringMode20P;
    }

    public static float convertGammaToLinearLevel(float f, float f2) {
        return MathUtils.constrain(MathUtils.pow(f / sNormalMaxBrightness, 2.2f), 0.0f, 1.0f) * f2;
    }
}
