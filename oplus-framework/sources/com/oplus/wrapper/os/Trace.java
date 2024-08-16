package com.oplus.wrapper.os;

import android.net.wifi.OplusWifiManager;

/* loaded from: classes.dex */
public class Trace {
    public static final long TRACE_TAG_GRAPHICS = getTraceTagGraphics();
    public static final long TRACE_TAG_HAL = getTraceTagHal();

    private static long getTraceTagGraphics() {
        return 2L;
    }

    private static final long getTraceTagHal() {
        return OplusWifiManager.OPLUS_WIFI_FEATURE_Passpoint;
    }

    public static void traceBegin(long traceTag, String methodName) {
        android.os.Trace.traceBegin(traceTag, methodName);
    }

    public static void traceEnd(long traceTag) {
        android.os.Trace.traceEnd(traceTag);
    }

    public static void asyncTraceBegin(long traceTag, String methodName, int cookie) {
        android.os.Trace.asyncTraceBegin(traceTag, methodName, cookie);
    }

    public static void asyncTraceEnd(long traceTag, String methodName, int cookie) {
        android.os.Trace.asyncTraceEnd(traceTag, methodName, cookie);
    }

    public static void traceCounter(long traceTag, String counterName, int counterValue) {
        android.os.Trace.traceCounter(traceTag, counterName, counterValue);
    }
}
