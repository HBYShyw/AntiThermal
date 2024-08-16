package com.oplus.epona.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;

/* loaded from: classes.dex */
public class Logger {
    private static final String TAG = "Epona->";
    public static boolean sDEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    /* loaded from: classes.dex */
    private static class LoggerObserver extends ContentObserver {
        @Override // android.database.ContentObserver
        public void onChange(boolean z10, Uri uri) {
            Logger.sDEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        }

        private LoggerObserver(Handler handler) {
            super(null);
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        if (sDEBUG) {
            Log.d(TAG + str, formatMessage(str2, objArr));
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        if (sDEBUG) {
            Log.e(TAG + str, formatMessage(str2, objArr));
        }
    }

    private static String formatMessage(String str, Object[] objArr) {
        return (str == null || objArr == null || objArr.length <= 0) ? "" : String.format(str, objArr);
    }

    public static void i(String str, String str2, Object... objArr) {
        if (sDEBUG) {
            Log.i(TAG + str, formatMessage(str2, objArr));
        }
    }

    public static void init(Context context) {
        if (context == null || context.getContentResolver() == null || !"com.oplus.appplatform".equals(context.getPackageName())) {
            return;
        }
        context.getContentResolver().registerContentObserver(Settings.System.getUriFor("log_switch_type"), false, new LoggerObserver(null));
    }

    public static void w(String str, String str2, Object... objArr) {
        if (sDEBUG) {
            Log.w(TAG + str, formatMessage(str2, objArr));
        }
    }
}
