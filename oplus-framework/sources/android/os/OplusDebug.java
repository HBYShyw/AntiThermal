package android.os;

/* loaded from: classes.dex */
public class OplusDebug {
    public static final boolean DEBUG_SYSTRACE_TAG;
    private static final boolean IS_PRE_VERSION;
    public static boolean IS_RELEASE_VERSION = false;
    public static final int LAUNCH_DELAY;
    public static final int LAUNCH_DELAY_DEFAULT = 500;
    public static final int LAUNCH_EXCEPTION_DELAY;
    public static final int LAUNCH_SECOND_DELAY;
    public static final int LAUNCH_SECOND_DELAY_DEFAULT = 300;
    public static final int LOOPER_DELAY;
    public static final int LOOPER_DELAY_DEFAULT = 1000;
    public static final int MIN_DELAY = 50;
    private static final String TAG = "OplusDebug";
    public static final int TYPE_LAUNCH_EXCEPTION_DEFAULT = 2000;
    public static final int VIEW_DELAY;
    public static final int VIEW_DELAY_DEFAULT = 100;

    static {
        boolean contains = SystemProperties.get("ro.build.version.ota", "").contains("PRE_");
        IS_PRE_VERSION = contains;
        LOOPER_DELAY = SystemProperties.getInt("debug.oplus.looper_delay", 1000);
        LAUNCH_DELAY = SystemProperties.getInt("persist.sys.launch_delay", 500);
        LAUNCH_SECOND_DELAY = SystemProperties.getInt("persist.sys.launch_second_delay", 300);
        VIEW_DELAY = SystemProperties.getInt("persist.sys.view_delay", 100);
        LAUNCH_EXCEPTION_DELAY = SystemProperties.getInt("persist.sys.launch_exception_delay", 2000);
        boolean z = SystemProperties.getBoolean("ro.build.release_type", false);
        IS_RELEASE_VERSION = z;
        DEBUG_SYSTRACE_TAG = SystemProperties.getBoolean("debug.oplus.systrace_enhance", contains || !z);
    }
}
