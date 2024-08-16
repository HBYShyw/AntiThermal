package oplus.util;

import android.os.Build;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;

/* loaded from: classes.dex */
public class OplusLogUtil {
    public static boolean DEBUG;
    public static final boolean DEBUG_PANIC;

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        DEBUG_PANIC = z;
        DEBUG = z || Build.IS_USERDEBUG;
    }

    public static void logD(String tag, String content) {
        logD(true, tag, content, null);
    }

    public static void logD(boolean shouldLog, String tag, String content) {
        logD(shouldLog, tag, content, null);
    }

    public static void logD(String tag, String content, Throwable tr) {
        logD(true, tag, content, tr);
    }

    public static void logD(boolean shouldLog, String tag, String content, Throwable tr) {
        if (!shouldLog) {
            return;
        }
        logDInner(tag, 0, content, tr);
    }

    public static void sysLogD(String tag, String content) {
        sysLogD(true, tag, content, null);
    }

    public static void sysLogD(boolean shouldLog, String tag, String content) {
        sysLogD(shouldLog, tag, content, null);
    }

    public static void sysLogD(String tag, String content, Throwable tr) {
        sysLogD(true, tag, content, tr);
    }

    public static void sysLogD(boolean shouldLog, String tag, String content, Throwable tr) {
        if (!shouldLog) {
            return;
        }
        logDInner(tag, 3, content, tr);
    }

    public static void formatLogD(String tag, String format, Object... args) {
        formatLogD(true, tag, format, null, args);
    }

    public static void formatLogD(String tag, String format, Throwable tr, Object... args) {
        formatLogD(true, tag, format, tr, args);
    }

    public static void formatLogD(boolean shouldLog, String tag, String format, Object... args) {
        formatLogD(shouldLog, tag, format, null, args);
    }

    public static void formatLogD(boolean shouldLog, String tag, String format, Throwable tr, Object... args) {
        formatLogDInner(shouldLog, format, tag, 0, tr, args);
    }

    private static void formatLogDInner(boolean shouldLog, String format, String tag, int logId, Throwable tr, Object... args) {
        if (!shouldLog || format == null) {
            return;
        }
        try {
            logDInner(tag, logId, String.format(format, args), tr);
        } catch (Exception e) {
            Slog.w(tag, "format error.", e);
        }
    }

    public static void formatSysLogD(String tag, String format, Object... args) {
        formatSysLogD(true, tag, format, null, args);
    }

    public static void formatSysLogD(String tag, String format, Throwable tr, Object... args) {
        formatSysLogD(true, tag, format, tr, args);
    }

    public static void formatSysLogD(boolean shouldLog, String tag, String format, Object... args) {
        formatSysLogD(shouldLog, tag, format, null, args);
    }

    public static void formatSysLogD(boolean shouldLog, String tag, String format, Throwable tr, Object... args) {
        formatLogDInner(shouldLog, format, tag, 3, tr, args);
    }

    private static void logDInner(String tag, int logId, String content, Throwable tr) {
        switch (logId) {
            case 0:
                if (tr != null) {
                    Log.d(tag, content, tr);
                    return;
                } else {
                    Log.d(tag, content);
                    return;
                }
            case 3:
                if (tr != null) {
                    Slog.d(tag, content, tr);
                    return;
                } else {
                    Slog.d(tag, content);
                    return;
                }
            default:
                return;
        }
    }
}
