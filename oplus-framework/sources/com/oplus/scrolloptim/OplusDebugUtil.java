package com.oplus.scrolloptim;

import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusDebugUtil {
    private static final String TAG = "ScrollOptim";
    public static final String PROPERTY_DEBUG_SWITCH = "debug.choreographer.scroll.scrollopt.debug";
    public static boolean DEBUG_SWITCH = SystemProperties.getBoolean(PROPERTY_DEBUG_SWITCH, false);
    public static final String PROPERTY_DEBUG_ENABLE_OPT = "debug.choreographer.scroll.scrollopt.enable";
    public static boolean DEBUG_ENABLE_OPT = SystemProperties.getBoolean(PROPERTY_DEBUG_ENABLE_OPT, true);

    public static void i(String tag, String msg) {
        Log.i("ScrollOptim [" + tag + "]", msg);
    }

    public static void d(String tag, String msg) {
        Log.d("ScrollOptim [" + tag + "]", msg);
    }

    public static void v(String tag, String msg) {
        Log.v("ScrollOptim [" + tag + "]", msg);
    }

    public static void w(String tag, String msg) {
        Log.w("ScrollOptim [" + tag + "]", msg);
    }

    public static void e(String tag, String msg) {
        Log.e("ScrollOptim [" + tag + "]", msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e("ScrollOptim [" + tag + "]", msg, tr);
    }

    public static void trace(String msg) {
        Trace.traceBegin(8L, msg);
        Trace.traceEnd(8L);
    }
}
