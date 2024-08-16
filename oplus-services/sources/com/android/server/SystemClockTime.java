package com.android.server;

import android.os.Build;
import android.os.Environment;
import android.os.SystemProperties;
import android.util.LocalLog;
import android.util.Slog;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SystemClockTime {
    private static final String TAG = "SystemClockTime";
    public static final int TIME_CONFIDENCE_HIGH = 100;
    public static final int TIME_CONFIDENCE_LOW = 0;
    private static final LocalLog sTimeDebugLog = new LocalLog(30, false);
    private static int sTimeConfidence = 0;
    private static final long sNativeData = init();

    @Target({ElementType.TYPE_USE})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface TimeConfidence {
    }

    private static native long init();

    private static native int setTime(long j, long j2);

    private SystemClockTime() {
    }

    public static void initializeIfRequired() {
        long max = Long.max(SystemProperties.getLong("ro.build.date.utc", -1L) * 1000, Long.max(Environment.getRootDirectory().lastModified(), Build.TIME));
        long currentTimeMillis = getCurrentTimeMillis();
        if (currentTimeMillis < max) {
            String str = "Current time only " + currentTimeMillis + ", advancing to build time " + max;
            Slog.i(TAG, str);
            setTimeAndConfidence(max, 0, str);
        }
    }

    public static void setTimeAndConfidence(long j, int i, String str) {
        synchronized (SystemClockTime.class) {
            setTime(sNativeData, j);
            sTimeConfidence = i;
            sTimeDebugLog.log(str);
        }
    }

    public static void setConfidence(int i, String str) {
        synchronized (SystemClockTime.class) {
            sTimeConfidence = i;
            sTimeDebugLog.log(str);
        }
    }

    private static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static int getTimeConfidence() {
        int i;
        synchronized (SystemClockTime.class) {
            i = sTimeConfidence;
        }
        return i;
    }

    public static void addDebugLogEntry(String str) {
        sTimeDebugLog.log(str);
    }

    public static void dump(PrintWriter printWriter) {
        sTimeDebugLog.dump(printWriter);
    }
}
