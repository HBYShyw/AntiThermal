package com.android.server.utils;

import android.util.Slog;
import android.util.TimingsTraceLog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class TimingsTraceAndSlog extends TimingsTraceLog {
    private static final long BOTTLENECK_DURATION_MS = -1;
    private static final String SYSTEM_SERVER_TIMING_ASYNC_TAG = "SystemServerTimingAsync";
    public static final String SYSTEM_SERVER_TIMING_TAG = "SystemServerTiming";
    private final String mTag;

    public static TimingsTraceAndSlog newAsyncLog() {
        return new TimingsTraceAndSlog(SYSTEM_SERVER_TIMING_ASYNC_TAG, 524288L);
    }

    public TimingsTraceAndSlog() {
        this(SYSTEM_SERVER_TIMING_TAG);
    }

    public TimingsTraceAndSlog(String str) {
        this(str, 524288L);
    }

    public TimingsTraceAndSlog(String str, long j) {
        super(str, j);
        this.mTag = str;
    }

    public TimingsTraceAndSlog(TimingsTraceAndSlog timingsTraceAndSlog) {
        super(timingsTraceAndSlog);
        this.mTag = timingsTraceAndSlog.mTag;
    }

    public void traceBegin(String str) {
        Slog.d(this.mTag, str);
        super.traceBegin(str);
    }

    public void logDuration(String str, long j) {
        super.logDuration(str, j);
    }

    public String toString() {
        return "TimingsTraceAndSlog[" + this.mTag + "]";
    }
}
