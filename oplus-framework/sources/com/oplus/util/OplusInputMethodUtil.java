package com.oplus.util;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Debug;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.Display;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import java.lang.reflect.Field;
import java.util.Arrays;

/* loaded from: classes.dex */
public class OplusInputMethodUtil {
    private static final int CALLER_DEPTH = 10;
    private static final int CALLER_START = 1;
    private static final String DEBUG_FIELD = "DEBUG";
    private static final String DISPLAY_APP_MIRAGE = "Mirage_appshare_display";
    private static final String DISPLAY_CAR_MIRAGE = "Mirage_car_display";
    private static final String DISPLAY_PC_MIRAGE = "Mirage_pc_display";
    private static final String DISPLAY_PUTT = "OplusPuttDisplay";
    private static final String DUMP_ARG_ALL = "all";
    private static final String DUMP_ARG_ALWAYS = "always";
    private static final String DUMP_ARG_LOG = "log";
    private static final String DUMP_ARG_OFF = "0";
    private static final String DUMP_ARG_ON = "1";
    private static final String DUMP_IMMS = "imms";
    private static final String FEATURE_FOLD = "oplus.hardware.type.fold";
    private static final String FEATURE_REMAP_DISPLAY_DISABLED = "oplus.software.fold_remap_display_disabled";
    public static final int FLAG_CALL_BY_USER_WHEN_START_INPUT_FAIL = 256;
    public static final int HIDE_INITIALIZE = 1048576;
    public static final int HIDE_SECURE_KEYBOARD = 256;
    public static final int HIDE_SECURE_SWITCH = 4096;
    public static final int INPUT_METHOD_WINDOW_MODE_FOLDING_MODE = 16;
    public static final int INPUT_METHOD_WINDOW_MODE_GAME_MODE = 8;
    public static final int INPUT_METHOD_WINDOW_MODE_SIMPLE_MODE = 4;
    public static final int INPUT_METHOD_WINDOW_MODE_SPLIT_SCREEN = 1;
    public static final int INPUT_METHOD_WINDOW_MODE_ZOOM = 2;
    private static final String KEY_IME_LOG = "persist.sys.assert.imelog";
    private static final String KEY_IME_LOG_ALWAYS_ON = "persist.sys.assert.imelog_alwayson";
    public static final String KEY_INPUT_METHOD_SCENES = "com.oplus.im.SCENES";
    private static final String KEY_PANIC = "persist.sys.assert.panic";
    public static final int REASON_HIDE_INITIALIZE = 1003;
    public static final int REASON_HIDE_SECURE_KEYBOARD = 1000;
    public static final int REASON_HIDE_SECURE_SWITCH = 1001;
    private static final int SECOND_DISPLAYID = 1;
    private static final String TAG = "OplusInputMethodUtil";
    private static boolean sAlwaysOn;
    private static boolean sDebug;
    private static boolean sDebugIme;
    private static Boolean sFoldable;
    private static Boolean sIsExpRom;
    private static Boolean sRemapDisplayDisabled;

    static {
        boolean z = SystemProperties.getBoolean(KEY_IME_LOG_ALWAYS_ON, false);
        sAlwaysOn = z;
        boolean z2 = z || SystemProperties.getBoolean(KEY_IME_LOG, false);
        sDebugIme = z2;
        sDebug = z2 || SystemProperties.getBoolean(KEY_PANIC, false);
        Slog.d(TAG, "init sDebug to " + sDebug + ", init sDebugIme to " + sDebugIme + ", init sAlwaysOn to " + sAlwaysOn);
    }

    public static boolean dynamicallyConfigDebugByDumpArgs(String[] args, String key) {
        boolean z;
        Slog.d(TAG, key + " dynamicallyConfigDebugByDumpArgs " + Arrays.toString(args));
        boolean z2 = false;
        if (args == null || TextUtils.isEmpty(key) || args.length <= 2 || !DUMP_ARG_LOG.equals(args[0]) || (!DUMP_ARG_ALL.equals(args[1]) && !DUMP_ARG_ALWAYS.equals(args[1]))) {
            return false;
        }
        if (DUMP_IMMS.equals(key)) {
            Boolean on = null;
            if ("1".equals(args[2])) {
                on = Boolean.TRUE;
            } else if ("0".equals(args[2])) {
                on = Boolean.FALSE;
            }
            if (on != null) {
                if (DUMP_ARG_ALL.equals(args[1])) {
                    SystemProperties.set(KEY_IME_LOG, String.valueOf(on));
                } else if (DUMP_ARG_ALWAYS.equals(args[1])) {
                    SystemProperties.set(KEY_IME_LOG_ALWAYS_ON, String.valueOf(on));
                }
            }
        }
        boolean z3 = SystemProperties.getBoolean(KEY_IME_LOG_ALWAYS_ON, false);
        sAlwaysOn = z3;
        if (!z3 && !SystemProperties.getBoolean(KEY_IME_LOG, false)) {
            z = false;
        } else {
            z = true;
        }
        sDebugIme = z;
        if (z || SystemProperties.getBoolean(KEY_PANIC, false)) {
            z2 = true;
        }
        sDebug = z2;
        Slog.d(TAG, "update sDebug to " + sDebug + ", update sDebugIme to " + sDebugIme + ", update sAlwaysOn to " + sAlwaysOn);
        return true;
    }

    public static void updateDebugToClass(Class clz) {
        updateDebugToClass(clz, DEBUG_FIELD, sDebug);
    }

    public static void updateDebugToClass(Class clz, String fieldName) {
        updateDebugToClass(clz, fieldName, sDebug);
    }

    public static void updateDebugImeToClass(Class clz) {
        updateDebugToClass(clz, DEBUG_FIELD, sDebugIme);
    }

    public static void updateDebugImeToClass(Class clz, String fieldName) {
        updateDebugToClass(clz, fieldName, sDebugIme);
    }

    private static void updateDebugToClass(Class clz, String fieldName, boolean debug) {
        if (clz != null && !TextUtils.isEmpty(fieldName)) {
            try {
                Field debugField = clz.getDeclaredField(fieldName);
                debugField.setAccessible(true);
                debugField.set(clz, Boolean.valueOf(debug));
                Slog.d(TAG, "updateDebugToClass " + clz.getSimpleName() + "." + fieldName + " = " + debug);
            } catch (IllegalAccessException e) {
                logException(TAG, "updateDebugToClass fail", e);
            } catch (NoSuchFieldException e2) {
                logException(TAG, "updateDebugToClass fail", e2);
            }
        }
    }

    private static DisplayManager getDisplayManager(Context context) {
        if (context == null) {
            return null;
        }
        return (DisplayManager) context.getSystemService("display");
    }

    public static Display getDisplay(Context context, int displayId) {
        DisplayManager displayManager = getDisplayManager(context);
        if (displayManager == null) {
            return null;
        }
        return displayManager.getDisplay(displayId);
    }

    public static String getDisplayName(Context context, int displayId) {
        Display display = getDisplay(context, displayId);
        if (display == null) {
            return null;
        }
        return display.getName();
    }

    public static boolean existMirageDisplay(Context context) {
        Display[] displays;
        DisplayManager displayManager = getDisplayManager(context);
        if (displayManager != null && (displays = displayManager.getDisplays()) != null) {
            for (Display display : displays) {
                if (display != null && isMirageDisplay(display.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCarMirageDisplay(String displayName) {
        return DISPLAY_CAR_MIRAGE.equals(displayName);
    }

    public static boolean isCarMirageDisplay(Context context, int displayId) {
        if (displayId == -1 || displayId == 0) {
            return false;
        }
        String displayName = getDisplayName(context, displayId);
        return isCarMirageDisplay(displayName);
    }

    public static boolean isAppMirageDisplay(String displayName) {
        return DISPLAY_APP_MIRAGE.equals(displayName);
    }

    public static boolean isAppMirageDisplay(Context context, int displayId) {
        if (displayId == -1 || displayId == 0) {
            return false;
        }
        String displayName = getDisplayName(context, displayId);
        return isAppMirageDisplay(displayName);
    }

    public static boolean isPcMirageDisplay(String displayName) {
        return DISPLAY_PC_MIRAGE.equals(displayName);
    }

    public static boolean isPcMirageDisplay(Context context, int displayId) {
        if (displayId == -1 || displayId == 0) {
            return false;
        }
        String displayName = getDisplayName(context, displayId);
        return isPcMirageDisplay(displayName);
    }

    public static boolean isMirageDisplay(String displayName) {
        return DISPLAY_PC_MIRAGE.equals(displayName) || DISPLAY_CAR_MIRAGE.equals(displayName) || DISPLAY_APP_MIRAGE.equals(displayName);
    }

    public static boolean isMirageDisplay(Context context, int displayId) {
        if (displayId == -1 || displayId == 0) {
            return false;
        }
        String displayName = getDisplayName(context, displayId);
        return isMirageDisplay(displayName);
    }

    public static boolean isPuttDisplay(String displayName) {
        return DISPLAY_PUTT.equals(displayName);
    }

    public static boolean isSecondDisplayId(int displayId) {
        return displayId == 1;
    }

    public static boolean isSecondScreen(Context context, int displayId) {
        if (isSecondDisplayId(displayId)) {
            return true;
        }
        String displayName = getDisplayName(context, displayId);
        return isPuttDisplay(displayName);
    }

    public static void logException(String tag, String message, Exception e) {
        Log.e(tag, message, e);
    }

    public static void log(String tag, String message) {
        Log.d(tag, message);
    }

    public static void logDebug(String tag, String message) {
        if (sDebug) {
            Log.d(tag, message);
        }
    }

    public static void logDebugIme(String tag, String message) {
        if (sDebugIme) {
            Log.d(tag, message);
        }
    }

    public static void logMethodCallers(String tag, String message) {
        if (sDebugIme) {
            Log.d(tag, message + " callers: " + Debug.getCallers(1, 10));
        } else if (sDebug) {
            Log.d(tag, message);
        }
    }

    public static void slogException(String tag, String message, Exception e) {
        Slog.e(tag, message, e);
    }

    public static void slog(String tag, String message) {
        Slog.d(tag, message);
    }

    public static void slogDebug(String tag, String message) {
        if (sDebug) {
            Slog.d(tag, message);
        }
    }

    public static void slogDebugIme(String tag, String message) {
        if (sDebugIme) {
            Slog.d(tag, message);
        }
    }

    public static void slogMethodCallers(String tag, String message) {
        if (sDebugIme) {
            Slog.d(tag, message + " callers: " + Debug.getCallers(1, 10));
        } else if (sDebug) {
            Slog.d(tag, message);
        }
    }

    public static boolean isDebug() {
        return sDebug;
    }

    public static boolean isDebugIme() {
        return sDebugIme;
    }

    public static boolean isFoldDisplay() {
        if (sFoldable == null) {
            sFoldable = Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.type.fold"));
        }
        return sFoldable.booleanValue();
    }

    public static boolean isRemapDisplayDisabled() {
        if (sRemapDisplayDisabled == null) {
            sRemapDisplayDisabled = Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.fold_remap_display_disabled"));
        }
        return sRemapDisplayDisabled.booleanValue();
    }

    public static boolean isExpRom() {
        if (sIsExpRom == null) {
            sIsExpRom = Boolean.valueOf(!OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_INPUTMETHOD_CN));
        }
        return sIsExpRom.booleanValue();
    }
}
