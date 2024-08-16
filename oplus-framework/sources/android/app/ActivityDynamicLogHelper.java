package android.app;

import android.app.servertransaction.TransactionExecutor;
import android.os.SystemProperties;

/* loaded from: classes.dex */
class ActivityDynamicLogHelper {
    ActivityDynamicLogHelper() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void enableDynamicalLogIfNeed() {
        boolean on = "eng".equals(SystemProperties.get("ro.build.type"));
        String open = SystemProperties.get("sys.activity.thread.log");
        if (open != null) {
            on |= open.equals("true");
        }
        setDynamicalLogEnable(on);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setDynamicalLogEnable(boolean on) {
        ActivityThread.localLOGV = on;
        ActivityThread.DEBUG_BROADCAST = on;
        ActivityThread.DEBUG_SERVICE = on;
        ActivityThread.DEBUG_MESSAGES = on;
        ActivityThread.DEBUG_MEMORY_TRIM = on;
        ActivityThread.DEBUG_BROADCAST_LIGHT = on;
        ActivityThread.DEBUG_CONFIGURATION = on;
        ActivityThread.DEBUG_PROVIDER = on;
        TransactionExecutor.setDebugResolver(on);
    }
}
