package android.os;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class OplusTpCompLog {
    private static final String TAG = "OplusTpCompLog";
    private static final boolean TP_COMP_LOG_JAVA_SWITCH_ON = true;
    public static final int TP_COMP_LOG_LEVEL_ANR = 8;
    public static final int TP_COMP_LOG_LEVEL_CRASH = 4;
    public static final int TP_COMP_LOG_LEVEL_ERROR = 2;
    public static final int TP_COMP_LOG_LEVEL_INFO = 1;
    private static final String TP_COMP_LOG_SWITCH_PROP = "oplus.thirdparty.cmpt.logs.switcher";

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface TpCompLogLevel {
    }

    private static native void native_oplusTpCompLog_anr(String str, String str2);

    private static native void native_oplusTpCompLog_crash(String str, String str2);

    private static native void native_oplusTpCompLog_error(String str, String str2, String str3, String str4, int i);

    private static native void native_oplusTpCompLog_info(String str, String str2);

    private static native void native_oplusTpCompLog_initialize();

    private static native boolean native_oplusTpCompLog_isEnableFor(int i);

    private OplusTpCompLog() {
    }

    public static void i(String module, String msg) {
        if (isEnableFor(1)) {
            native_oplusTpCompLog_info(module, msg);
        }
    }

    public static void e(String module, String msg) {
        if (isEnableFor(2)) {
            Exception e = new Exception();
            String method = e.getStackTrace()[1].getMethodName();
            String file = e.getStackTrace()[1].getFileName();
            int line = e.getStackTrace()[1].getLineNumber();
            native_oplusTpCompLog_error(module, msg, file, method, line);
        }
    }

    public static void c(String module, String msg) {
        if (isEnableFor(4)) {
            native_oplusTpCompLog_crash(module, msg);
        }
    }

    public static void a(String module, String msg) {
        if (isEnableFor(8)) {
            native_oplusTpCompLog_anr(module, msg);
        }
    }

    public static boolean isEnableFor(int level) {
        int ll = SystemProperties.getInt(TP_COMP_LOG_SWITCH_PROP, 0);
        return (ll & level) != 0;
    }

    public static void initialize() {
        native_oplusTpCompLog_initialize();
    }
}
