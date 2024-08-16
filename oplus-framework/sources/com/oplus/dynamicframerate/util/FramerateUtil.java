package com.oplus.dynamicframerate.util;

import android.os.Trace;

/* loaded from: classes.dex */
public class FramerateUtil {
    public static final int FRAMERATE_120 = 120;
    public static final int FRAMERATE_144 = 144;
    public static final int FRAMERATE_30 = 30;
    public static final int FRAMERATE_40 = 40;
    public static final int FRAMERATE_60 = 60;
    public static final int FRAMERATE_72 = 72;
    public static final int FRAMERATE_90 = 90;
    public static final int[] FRAMERATE_LIST;
    public static final long[] FRAME_INTERVALS;
    public static final int FRAME_INTERVAL_COMPARE_GAP = 500000;
    public static final int IDLE_STABLE_FRAME_COUNT = 3;
    public static final long NANOS_PER_SECOND = 1000000000;
    public static final int SCREEN_IDLE_REFRESH_THRESHOLD_MS = 100;
    public static final int STABLE_FRAME_COUNT = 7;
    private static long sFrameIntervalNanos;
    private static int sFrameRateThreshold;

    static {
        int[] iArr = {120, 90, 60};
        FRAMERATE_LIST = iArr;
        int i = iArr[0];
        FRAME_INTERVALS = new long[]{NANOS_PER_SECOND / i, NANOS_PER_SECOND / iArr[1], NANOS_PER_SECOND / iArr[2]};
        sFrameRateThreshold = i;
    }

    public static int getFramerateFromInterval(long frameIntervalNanos) {
        int i = 0;
        while (true) {
            long[] jArr = FRAME_INTERVALS;
            if (i >= jArr.length) {
                return FRAMERATE_LIST[0];
            }
            if (Math.abs(jArr[i] - frameIntervalNanos) >= 500000) {
                i++;
            } else {
                return FRAMERATE_LIST[i];
            }
        }
    }

    public static long getFrameIntervalFromFramerate(int framerate) {
        if (framerate <= 0) {
            return FRAME_INTERVALS[0];
        }
        return NANOS_PER_SECOND / framerate;
    }

    public static boolean isInSameVsync(long nanos0, long nanos1) {
        return Math.abs(nanos0 - nanos1) < 500000;
    }

    public static boolean isSameFrameInterval(long interval1, long interval2) {
        return Math.abs(interval1 - interval2) < 500000;
    }

    public static void updateFramerateThreshold(int threshold) {
        Trace.traceCounter(8L, "FrameRateThreshold", threshold);
        sFrameRateThreshold = threshold;
    }

    public static int getFramerateThreshold() {
        return sFrameRateThreshold;
    }

    public static void updateFrameIntervalNanos(long frameIntervalNanos) {
        sFrameIntervalNanos = frameIntervalNanos;
    }

    public static long getFrameIntervalNanos() {
        return sFrameIntervalNanos;
    }

    public static long get120HzIntervalNanos() {
        return FRAME_INTERVALS[0];
    }

    public static long get60HzIntervalNanos() {
        return FRAME_INTERVALS[r0.length - 1];
    }
}
