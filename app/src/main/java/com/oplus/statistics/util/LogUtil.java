package com.oplus.statistics.util;

import android.util.Log;

/* loaded from: classes2.dex */
public class LogUtil {
    public static final String TAG_PREFIX = "OplusTrack-";
    private static boolean isDebug = false;

    public static void d(String str, Supplier<String> supplier) {
        if (isDebug) {
            Log.d(TAG_PREFIX + str, supplier.get());
        }
    }

    public static void e(String str, Supplier<String> supplier) {
        Log.e(TAG_PREFIX + str, supplier.get());
    }

    public static void i(String str, Supplier<String> supplier) {
        if (isDebug) {
            Log.i(TAG_PREFIX + str, supplier.get());
        }
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean z10) {
        isDebug = z10;
    }

    public static void v(String str, Supplier<String> supplier) {
        if (isDebug) {
            Log.v(TAG_PREFIX + str, supplier.get());
        }
    }

    public static void w(String str, Supplier<String> supplier) {
        Log.w(TAG_PREFIX + str, supplier.get());
    }
}
