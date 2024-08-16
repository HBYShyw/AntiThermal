package com.oplus.powermanager.fuelgaue.base;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import com.oplus.battery.R;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class StatusBarUtil {
    private static final String KEY_MODE_NAVIGATIONBAR_GESTURE = "hide_navigationbar_enable";
    private static final int MODE_NAVIGATIONBAR_GESTURE = 2;
    private static final int MODE_NAVIGATIONBAR_GESTURE_EXT = 3;
    public static final int Oplus_1_0 = 1;
    public static final int Oplus_1_2 = 2;
    public static final int Oplus_1_4 = 3;
    public static final int Oplus_2_0 = 4;
    public static final int Oplus_2_1 = 5;
    public static final int Oplus_3_0 = 6;
    public static final int UNKNOWN = 0;

    private static int getRomVersionCode() {
        try {
            Class<?> cls = Class.forName("com.oplus.os.OplusBuild");
            return ((Integer) cls.getDeclaredMethod("getOplusOSVERSION", new Class[0]).invoke(cls, new Object[0])).intValue();
        } catch (Exception e10) {
            Log.e("RomVersionUtil", "getRomVersionCode failed. error = " + e10.getMessage());
            return 0;
        }
    }

    public static boolean isGestureNavigation(Context context) {
        if (context == null) {
            return false;
        }
        int i10 = Settings.Secure.getInt(context.getContentResolver(), KEY_MODE_NAVIGATIONBAR_GESTURE, 0);
        return i10 == 2 || i10 == 3;
    }

    public static void setStatusBarTransparentAndBlackFont(Activity activity) {
        setStatusBarTransparentAndBlackFont(activity, false);
    }

    public static void setStatusBarTransparentAndBlackFont(Activity activity, boolean z10) {
        Window window = activity.getWindow();
        View decorView = activity.getWindow().getDecorView();
        window.setStatusBarColor(activity.getResources().getColor(R.color.oplus_status_bar_color));
        if (isGestureNavigation(activity) && !z10) {
            window.setNavigationBarColor(0);
            decorView.setSystemUiVisibility(1536);
        } else {
            window.setNavigationBarColor(COUIContextUtil.a(activity, R.attr.couiColorBackgroundWithCard));
            decorView.setSystemUiVisibility(1024);
        }
        int systemUiVisibility = decorView.getSystemUiVisibility();
        int romVersionCode = getRomVersionCode();
        boolean z11 = activity.getResources().getBoolean(R.bool.list_status_white_enabled);
        if (romVersionCode >= 6 || romVersionCode == 0) {
            window.addFlags(Integer.MIN_VALUE);
            decorView.setSystemUiVisibility(COUIDarkModeUtil.a(activity) ? systemUiVisibility & (-8193) & (-17) : !z11 ? systemUiVisibility | 8192 : systemUiVisibility | 256);
        }
    }
}
