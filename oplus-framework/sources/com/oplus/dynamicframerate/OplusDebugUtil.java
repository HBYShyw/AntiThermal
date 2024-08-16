package com.oplus.dynamicframerate;

import android.os.Build;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import com.oplus.debug.InputLog;

/* loaded from: classes.dex */
public class OplusDebugUtil {
    public static final boolean DEBUG;
    public static final int DEBUG_FRAME_STABLE_PARAM;
    public static final String DEBUG_HIGH_CAPABILITY_PROPERTY = "debug.os.perf.vrr.adfr.tfc.high.capability.enable";
    public static final int DEBUG_IDLE_STABLE_FRAMES;
    public static final boolean DEBUG_PANIC;
    public static final boolean DEBUG_STABLEFRAMERATE;
    public static final boolean DEBUG_USERACTIONSTATS;
    public static final String STRING_DEBUG_IDLE_STABLE_FRAMES = "debug.dynamicframerate.idlestableframes";
    public static final String STRING_DEBUG_STABLEFRAMERATE = "debug.os.perf.adfr.stable.frame.enable";
    public static final String STRING_FRAME_STABLE_PARAM = "debug.dynamicframerate.framestableparam";
    private static final String TAG = "DynamicFramerate";

    static {
        boolean z = true;
        boolean z2 = SystemProperties.getBoolean("debug.os.perf.vrr.adfr.tfc.debuginfo.enable", false) || SystemProperties.getBoolean("persist.debug.os.perf.vrr.adfr.tfc.debuginfo.enable", false);
        DEBUG_PANIC = z2;
        DEBUG = z2 || Build.IS_USERDEBUG;
        if (!SystemProperties.get(InputLog.OPLUS_IMAGE_ENGINEERING_TYPE, "").contains("preversion") && !SystemProperties.getBoolean("debug.os.perf.adfr.mydebugforUserActionStats", false)) {
            z = false;
        }
        DEBUG_USERACTIONSTATS = z;
        DEBUG_STABLEFRAMERATE = SystemProperties.getBoolean(STRING_DEBUG_STABLEFRAMERATE, false);
        DEBUG_FRAME_STABLE_PARAM = SystemProperties.getInt(STRING_FRAME_STABLE_PARAM, -1);
        DEBUG_IDLE_STABLE_FRAMES = SystemProperties.getInt(STRING_DEBUG_IDLE_STABLE_FRAMES, -1);
    }

    public static void i(String tag, String msg) {
        Log.i("DynamicFramerate [" + tag + "]", msg);
    }

    public static void d(String tag, String msg) {
        Log.d("DynamicFramerate [" + tag + "]", msg);
    }

    public static void v(String tag, String msg) {
        Log.v("DynamicFramerate [" + tag + "]", msg);
    }

    public static void w(String tag, String msg) {
        Log.w("DynamicFramerate [" + tag + "]", msg);
    }

    public static void e(String tag, String msg) {
        Log.e("DynamicFramerate [" + tag + "]", msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e("DynamicFramerate [" + tag + "]", msg, tr);
    }

    public static void trace(String msg) {
        Trace.traceBegin(8L, msg);
        Trace.traceEnd(8L);
    }
}
