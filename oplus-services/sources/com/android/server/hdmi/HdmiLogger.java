package com.android.server.hdmi;

import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import java.util.HashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class HdmiLogger {
    private static final long ERROR_LOG_DURATION_MILLIS = 20000;
    private static final String TAG = "HDMI";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final ThreadLocal<HdmiLogger> sLogger = new ThreadLocal<>();
    private final HashMap<String, Pair<Long, Integer>> mWarningTimingCache = new HashMap<>();
    private final HashMap<String, Pair<Long, Integer>> mErrorTimingCache = new HashMap<>();

    private HdmiLogger() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void warning(String str, Object... objArr) {
        getLogger().warningInternal(toLogString(str, objArr));
    }

    private void warningInternal(String str) {
        String updateLog = updateLog(this.mWarningTimingCache, str);
        if (updateLog.isEmpty()) {
            return;
        }
        Slog.w(TAG, updateLog);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void error(String str, Object... objArr) {
        getLogger().errorInternal(toLogString(str, objArr));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void error(String str, Exception exc, Object... objArr) {
        getLogger().errorInternal(toLogString(str + exc, objArr));
    }

    private void errorInternal(String str) {
        String updateLog = updateLog(this.mErrorTimingCache, str);
        if (updateLog.isEmpty()) {
            return;
        }
        Slog.e(TAG, updateLog);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void debug(String str, Object... objArr) {
        getLogger().debugInternal(toLogString(str, objArr));
    }

    private void debugInternal(String str) {
        if (DEBUG) {
            Slog.d(TAG, str);
        }
    }

    private static final String toLogString(String str, Object[] objArr) {
        return objArr.length > 0 ? String.format(str, objArr) : str;
    }

    private static HdmiLogger getLogger() {
        ThreadLocal<HdmiLogger> threadLocal = sLogger;
        HdmiLogger hdmiLogger = threadLocal.get();
        if (hdmiLogger != null) {
            return hdmiLogger;
        }
        HdmiLogger hdmiLogger2 = new HdmiLogger();
        threadLocal.set(hdmiLogger2);
        return hdmiLogger2;
    }

    private static String updateLog(HashMap<String, Pair<Long, Integer>> hashMap, String str) {
        long uptimeMillis = SystemClock.uptimeMillis();
        Pair<Long, Integer> pair = hashMap.get(str);
        if (shouldLogNow(pair, uptimeMillis)) {
            String buildMessage = buildMessage(str, pair);
            hashMap.put(str, new Pair<>(Long.valueOf(uptimeMillis), 1));
            return buildMessage;
        }
        increaseLogCount(hashMap, str);
        return "";
    }

    private static String buildMessage(String str, Pair<Long, Integer> pair) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(pair == null ? 1 : ((Integer) pair.second).intValue());
        sb.append("]:");
        sb.append(str);
        return sb.toString();
    }

    private static void increaseLogCount(HashMap<String, Pair<Long, Integer>> hashMap, String str) {
        Pair<Long, Integer> pair = hashMap.get(str);
        if (pair != null) {
            hashMap.put(str, new Pair<>((Long) pair.first, Integer.valueOf(((Integer) pair.second).intValue() + 1)));
        }
    }

    private static boolean shouldLogNow(Pair<Long, Integer> pair, long j) {
        return pair == null || j - ((Long) pair.first).longValue() > ERROR_LOG_DURATION_MILLIS;
    }
}
