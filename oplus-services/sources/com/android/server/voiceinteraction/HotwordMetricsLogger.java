package com.android.server.voiceinteraction;

import android.content.Context;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.LatencyTracker;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HotwordMetricsLogger {
    private static final int AUDIO_EGRESS_DSP_DETECTOR = 1;
    private static final int AUDIO_EGRESS_NORMAL_DETECTOR = 0;
    private static final int AUDIO_EGRESS_SOFTWARE_DETECTOR = 2;
    private static final int METRICS_INIT_DETECTOR_DSP = 1;
    private static final int METRICS_INIT_DETECTOR_SOFTWARE = 2;
    private static final int METRICS_INIT_NORMAL_DETECTOR = 0;

    private static int getAudioEgressDetectorType(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return 0;
            }
        }
        return i2;
    }

    private static int getCreateMetricsDetectorType(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return 0;
            }
        }
        return i2;
    }

    private static int getDetectorMetricsDetectorType(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return 0;
            }
        }
        return i2;
    }

    private static int getInitMetricsDetectorType(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return 0;
            }
        }
        return i2;
    }

    private static int getKeyphraseMetricsDetectorType(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return 0;
            }
        }
        return i2;
    }

    private static int getRestartMetricsDetectorType(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return 0;
            }
        }
        return i2;
    }

    private HotwordMetricsLogger() {
    }

    public static void writeDetectorCreateEvent(int i, boolean z, int i2) {
        FrameworkStatsLog.write(430, getCreateMetricsDetectorType(i), z, i2);
    }

    public static void writeServiceInitResultEvent(int i, int i2, int i3) {
        FrameworkStatsLog.write(431, getInitMetricsDetectorType(i), i2, i3);
    }

    public static void writeServiceRestartEvent(int i, int i2, int i3) {
        FrameworkStatsLog.write(432, getRestartMetricsDetectorType(i), i2, i3);
    }

    public static void writeKeyphraseTriggerEvent(int i, int i2, int i3) {
        FrameworkStatsLog.write(433, getKeyphraseMetricsDetectorType(i), i2, i3);
    }

    public static void writeDetectorEvent(int i, int i2, int i3) {
        FrameworkStatsLog.write(434, getDetectorMetricsDetectorType(i), i2, i3);
    }

    public static void writeAudioEgressEvent(int i, int i2, int i3, int i4, int i5, int i6) {
        FrameworkStatsLog.write(578, getAudioEgressDetectorType(i), i2, i3, i4, i5, i6);
    }

    public static void startHotwordTriggerToUiLatencySession(Context context, String str) {
        LatencyTracker.getInstance(context).onActionStart(19, str);
    }

    public static void stopHotwordTriggerToUiLatencySession(Context context) {
        LatencyTracker.getInstance(context).onActionEnd(19);
    }

    public static void cancelHotwordTriggerToUiLatencySession(Context context) {
        LatencyTracker.getInstance(context).onActionCancel(19);
    }
}
