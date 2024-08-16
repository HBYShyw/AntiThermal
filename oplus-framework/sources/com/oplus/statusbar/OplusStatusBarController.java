package com.oplus.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Singleton;
import android.view.OplusWindowManager;
import android.view.View;
import android.view.WindowInsets;
import com.android.internal.graphics.ColorUtils;
import com.oplus.bluetooth.OplusBluetoothClass;
import com.oplus.util.OplusDarkModeUtil;
import com.oplus.util.OplusTypeCastingHelper;

/* loaded from: classes.dex */
public class OplusStatusBarController {
    private static final int ALPHA = 156;
    private static final int GRAY_LEVEL = 150;
    public static final int NAVIGATION_BAR = 1;
    public static final int OPLUS_NAVIGATION_BAR_COLOR = -1;
    public static final int STATUS_BAR = 0;
    private int mNavigationGuardColor;
    private OplusWindowManager mWm;
    private static final String TAG = OplusStatusBarController.class.getSimpleName();
    private static boolean DEBUG_OPLUS_SYSTEMBAR = false;
    private static final Singleton<OplusStatusBarController> sColorStatusBarControllerSingleton = new Singleton<OplusStatusBarController>() { // from class: com.oplus.statusbar.OplusStatusBarController.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public OplusStatusBarController m1003create() {
            return new OplusStatusBarController();
        }
    };

    private OplusStatusBarController() {
        this.mNavigationGuardColor = -1;
        this.mWm = null;
        this.mWm = new OplusWindowManager();
        DEBUG_OPLUS_SYSTEMBAR = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    }

    public static OplusStatusBarController getInstance() {
        return (OplusStatusBarController) sColorStatusBarControllerSingleton.get();
    }

    public int caculateSystemBarColor(Context context, String pkg, String activityName, int themeColor, int bar) {
        if (OplusDarkModeUtil.isNightMode(context)) {
            return themeColor;
        }
        int barColor = getBarColorFromAdaptation(pkg, activityName, bar);
        if (barColor == 0) {
            return themeColor;
        }
        return barColor;
    }

    public int getBottomInset(boolean isImeWindow, int normalBottomInset, WindowInsets insets) {
        if (!isImeWindow) {
            return normalBottomInset;
        }
        int result = insets.getSystemWindowInsetBottom();
        return result;
    }

    public void updateOplusNavigationGuardColor(Context context, int color, int windowColor, View decor) {
        this.mNavigationGuardColor = getImeBgColor(context, color, windowColor);
    }

    public int getNavigationGuardColor() {
        return this.mNavigationGuardColor;
    }

    private int getImeBgColor(Context context, int color, int windowColor) {
        if (DEBUG_OPLUS_SYSTEMBAR) {
            Log.d(TAG, "getImeBgColor: " + color + " windowColor" + windowColor);
        }
        if (windowColor != -16777216 && windowColor != -1) {
            return windowColor;
        }
        if (DEBUG_OPLUS_SYSTEMBAR) {
            Log.d(TAG, "getImeBgColor: " + color);
        }
        return color;
    }

    private int getBarColorFromAdaptation(String pkg, String activityName, int bar) {
        int defaultColor = 0;
        try {
            OplusWindowManager oplusWindowManager = this.mWm;
            if (oplusWindowManager != null) {
                if (bar == 0) {
                    defaultColor = oplusWindowManager.getStatusBarOplusFromAdaptation(pkg, activityName);
                } else if (bar == 1) {
                    defaultColor = oplusWindowManager.getNavBarOplusFromAdaptation(pkg, activityName);
                }
            }
        } catch (RemoteException e) {
            Log.w(TAG, "getNavBarOplusFromAdaptation " + e);
        }
        return defaultColor;
    }

    private boolean isColorLight(int color) {
        if (color == 0) {
            return false;
        }
        int alpha = ((-16777216) & color) >>> 24;
        int red = (16711680 & color) >>> 16;
        int green = (65280 & color) >>> 8;
        int blue = color & 255;
        int grayLevel = (int) ((red * 0.299d) + (green * 0.587d) + (blue * 0.114d));
        if (grayLevel <= 150 || alpha <= 156) {
            return false;
        }
        return true;
    }

    public static int getDefaultNavigationBarColor(Context context) {
        if (OplusDarkModeUtil.isNightMode(context)) {
            return getDarkModeBackgroundColor(context);
        }
        return -1;
    }

    public static int getDarkModeBackgroundColor(Context context) {
        int bgColor = OplusBluetoothClass.Device.UNKNOWN;
        if (context == null) {
            return OplusBluetoothClass.Device.UNKNOWN;
        }
        Configuration configuration = context.getResources().getConfiguration();
        OplusBaseConfiguration OplusBaseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, configuration);
        if (OplusBaseConfiguration != null) {
            float bgL = OplusBaseConfiguration.getOplusExtraConfiguration().mDarkModeBackgroundMaxL;
            bgColor = ColorUtils.LABToColor(bgL, 0.0d, 0.0d);
            if (DEBUG_OPLUS_SYSTEMBAR) {
                Log.d(TAG, "getDarkModeBackgroundColor: bgColor = " + Integer.toHexString(bgColor));
            }
        }
        return bgColor;
    }
}
