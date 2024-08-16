package oplus.telecom;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import java.util.IllegalFormatException;
import java.util.Locale;

/* loaded from: classes.dex */
public class OplusTelecomLog {
    private static final boolean FORCELOGGING = false;
    private static final Object SINGLESYNC = new Object();
    private static final String TAG = "TelecomFramework";
    private static boolean sDEBUG;
    private static boolean sERROR;
    private static boolean sINFO;
    private static boolean sOPLUSDEBUG;
    private static boolean sOPLUSPANIC;
    private static boolean sOPLUSPHONELOGSWITCH;

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        sOPLUSPANIC = z;
        sOPLUSPHONELOGSWITCH = false;
        sDEBUG = z;
        sINFO = z;
        sOPLUSDEBUG = false;
        sERROR = isLoggable(6);
    }

    public static void oplusRefreshLogSwitch(Context context) {
        if (context == null) {
            return;
        }
        sOPLUSPANIC = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        if (Settings.System.getInt(context.getContentResolver(), "phone.log.switch", 0) == 1) {
            sOPLUSPHONELOGSWITCH = true;
        } else {
            sOPLUSPHONELOGSWITCH = false;
        }
        Log.i(TAG, buildMessage(TAG, "sOPLUS_PANIC = " + sOPLUSPANIC + ", sOPLUS_PHONE_LOG_SWITCH = " + sOPLUSPHONELOGSWITCH, new Object[0]));
        boolean z = sOPLUSPHONELOGSWITCH;
        boolean z2 = z || sOPLUSPANIC;
        sDEBUG = z2;
        sINFO = z2;
        sOPLUSDEBUG = z;
    }

    private static String buildMessage(String prefix, String format, Object... args) {
        String msg;
        String format2;
        String sessionName = null;
        if (sOPLUSDEBUG) {
            sessionName = android.telecom.Log.getSessionId();
        }
        String sessionPostfix = TextUtils.isEmpty(sessionName) ? "" : ": " + sessionName;
        if (args != null) {
            try {
            } catch (IllegalFormatException ife) {
                e(TAG, ife, "Log: IllegalFormatException: formatString='%s' numArgs=%d", format, Integer.valueOf(args.length));
                msg = format + " (An error occurred while formatting the message.)";
            }
            if (args.length != 0) {
                format2 = String.format(Locale.US, format, args);
                msg = format2;
                return String.format(Locale.US, "%s: %s%s", prefix, msg, sessionPostfix);
            }
        }
        format2 = format;
        msg = format2;
        return String.format(Locale.US, "%s: %s%s", prefix, msg, sessionPostfix);
    }

    public static void e(String prefix, Throwable tr, String format, Object... args) {
        if (sERROR) {
            Log.e(TAG, buildMessage(prefix, format, args), tr);
        }
    }

    public static boolean isLoggable(int level) {
        return Log.isLoggable(TAG, level);
    }
}
