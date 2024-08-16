package android.view;

import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusScreenShotUtil {
    private static boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String PERSIST_KEY = "sys.oplus.screenshot";
    private static final String TAG = "OplusScreenShotUtil";

    public static void pauseDeliverPointerEvent() {
        SystemProperties.set(PERSIST_KEY, "1");
    }

    public static void resumeDeliverPointerEvent() {
        SystemProperties.set(PERSIST_KEY, "0");
    }

    public static boolean checkPauseDeliverPointer() {
        return SystemProperties.getBoolean(PERSIST_KEY, false);
    }

    public static void dealPausedDeliverPointer(MotionEvent event, View view) {
        if (DEBUG) {
            Log.d(TAG, "dealPausedDeliverPointer ------------------------");
        }
        if (view != null) {
            event.setAction(3);
            view.dispatchPointerEvent(event);
        }
    }
}
