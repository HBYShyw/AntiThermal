package android.view.autolayout;

import android.os.Debug;
import android.os.Trace;
import android.util.Log;
import android.view.View;

/* loaded from: classes.dex */
public class AutoLayoutDebug {
    public static final int REASON_CONFIG = 1;
    public static final int REASON_LAUNCH = 3;
    public static final int REASON_RESUME = 2;
    private static final String TAG = AutoLayoutDebug.class.getSimpleName();
    private static final Boolean DEBUG = false;
    private static StringBuilder sStringBuilder = new StringBuilder();

    public static void appendLog(String content) {
        if (DEBUG.booleanValue()) {
            String calledFrom = Debug.getCaller();
            sStringBuilder.append("[");
            sStringBuilder.append("caller:" + calledFrom);
            sStringBuilder.append(",");
            sStringBuilder.append(content);
            sStringBuilder.append("]");
        }
    }

    public static void flushLog() {
        if (DEBUG.booleanValue()) {
            Log.d(TAG, sStringBuilder.toString());
            sStringBuilder = new StringBuilder();
        }
    }

    public static void appendExtraInfo(View view, String content) {
        AutoLayoutViewInfo viewInfo = AutoLayoutUtils.getViewInfo(view);
        viewInfo.setExtraInfo(content);
    }

    public static void startTraceSection(String content) {
        if (Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, TAG + "" + content);
        }
    }

    public static void endTraceSection() {
        if (Trace.isTagEnabled(8L)) {
            Trace.traceEnd(8L);
        }
    }

    public static void onCrash(Throwable e) {
    }

    public static boolean needDisable() {
        return false;
    }

    public static void log(String content) {
        if (DEBUG.booleanValue()) {
            Log.d(TAG, content);
        }
    }

    public static void logE(Exception e) {
        Log.e(TAG, e.getMessage());
    }

    public static boolean isDebug() {
        return DEBUG.booleanValue();
    }

    public static String updatePolicyReason(int reason) {
        switch (reason) {
            case 1:
                return "REASON_CONFIG";
            case 2:
                return "REASON_RESUME";
            case 3:
                return "REASON_LAUNCH";
            default:
                return "Unknown Reason";
        }
    }
}
