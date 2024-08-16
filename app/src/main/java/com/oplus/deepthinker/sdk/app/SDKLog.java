package com.oplus.deepthinker.sdk.app;

import android.util.Log;

/* loaded from: classes.dex */
public class SDKLog {
    private static final String ADDTIONAL = "[APP] ";
    private static final String TAG = "DeepThinkerSDK";
    private static Boolean sDebug = null;
    private static Boolean sInfo = null;
    private static boolean sLogOn = false;

    static {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            sLogOn = ((Boolean) cls.getMethod("getBoolean", String.class, Boolean.TYPE).invoke(cls, "persist.sys.assert.panic", Boolean.FALSE)).booleanValue();
        } catch (Throwable th) {
            e(TAG, "static init", th);
        }
    }

    private SDKLog() {
    }

    public static void d(String str, String str2) {
        if (sLogOn || debug()) {
            Log.d(TAG, ADDTIONAL + str + ": " + str2);
        }
    }

    private static boolean debug() {
        if (sDebug == null) {
            sDebug = Boolean.valueOf(Log.isLoggable(TAG, 3));
        }
        return sDebug.booleanValue();
    }

    public static void e(String str, String str2) {
        Log.e(TAG, ADDTIONAL + str + ": " + str2);
    }

    public static void i(String str, String str2) {
        if (sLogOn || info()) {
            Log.i(TAG, ADDTIONAL + str + ": " + str2);
        }
    }

    private static boolean info() {
        if (sInfo == null) {
            sInfo = Boolean.valueOf(Log.isLoggable(TAG, 4));
        }
        return sInfo.booleanValue();
    }

    public static void v(String str, String str2) {
        if (debug()) {
            Log.v(TAG, ADDTIONAL + str + ": " + str2);
        }
    }

    public static void w(String str, String str2) {
        Log.w(TAG, ADDTIONAL + str + ": " + str2);
    }

    public static void e(String str) {
        Log.e(TAG, ADDTIONAL + str);
    }

    public static void w(String str) {
        Log.w(TAG, ADDTIONAL + str);
    }

    public static void d(String str) {
        if (sLogOn || debug()) {
            Log.d(TAG, ADDTIONAL + str);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        Log.e(TAG, ADDTIONAL + str + ":" + str2 + " , " + th);
    }

    public static void i(String str) {
        if (sLogOn || info()) {
            Log.i(TAG, ADDTIONAL + str);
        }
    }

    public static void v(String str) {
        if (debug()) {
            Log.v(TAG, ADDTIONAL + str);
        }
    }
}
