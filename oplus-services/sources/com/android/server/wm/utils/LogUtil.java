package com.android.server.wm.utils;

import android.os.SystemProperties;
import android.util.Log;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class LogUtil {
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    public static void d(String str, String str2) {
        Log.d(str, str2);
    }

    public static void i(String str, String str2) {
        Log.i(str, str2);
    }

    public static void debugD(String str, String str2) {
        if (DEBUG) {
            Log.d(str, str2);
        }
    }

    public static void debugI(String str, String str2) {
        if (DEBUG) {
            Log.i(str, str2);
        }
    }
}
