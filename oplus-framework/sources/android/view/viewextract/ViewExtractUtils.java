package android.view.viewextract;

import android.os.Trace;
import android.util.Log;

/* loaded from: classes.dex */
public class ViewExtractUtils {
    public static boolean DEBUG_ALL = false;
    public static boolean DEBUG_NORMAL = true;
    public static boolean DEBUG_TRACE = false;
    public static final String TAG = "ViewExtract";

    public static void d(String tag, String content) {
        if (DEBUG_NORMAL) {
            Log.d(TAG, "[" + tag + "] " + content);
        }
    }

    public static void i(String tag, String content) {
        if (DEBUG_ALL) {
            Log.i(TAG, "[" + tag + "] " + content);
        }
    }

    public static void e(String tag, String content) {
        Log.e(TAG, "[" + tag + "] " + content);
    }

    public static void e(String content) {
        Log.e(TAG, content);
    }

    public static void beginTrace(String tag) {
        if (DEBUG_TRACE) {
            Trace.beginSection(tag);
        }
    }

    public static void endTrace() {
        if (DEBUG_TRACE) {
            Trace.endSection();
        }
    }
}
