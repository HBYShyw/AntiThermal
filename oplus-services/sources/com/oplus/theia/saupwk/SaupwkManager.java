package com.oplus.theia.saupwk;

import android.os.Handler;
import android.os.SystemProperties;
import android.util.Slog;
import java.util.Calendar;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SaupwkManager {
    static final String TAG = "SAUPWK";
    private static SaupwkManager sInstance;
    Handler mHandler = new Handler();

    public static synchronized SaupwkManager getInstance() {
        SaupwkManager saupwkManager;
        synchronized (SaupwkManager.class) {
            if (sInstance == null) {
                sInstance = new SaupwkManager();
            }
            saupwkManager = sInstance;
        }
        return saupwkManager;
    }

    public void saupwkLogDumpTrigger() {
        if (SystemProperties.get("persist.sys.saupwk_en", "").equals("1")) {
            Calendar calendar = Calendar.getInstance();
            String str = calendar.get(1) + "-" + calendar.get(2) + "-" + calendar.get(5) + " " + calendar.get(11) + ":" + calendar.get(12) + ":" + calendar.get(13);
            SystemProperties.set("sys.bootfinish.timestamp", str);
            Slog.w(TAG, "[SAUPWK]: marking sys.bootfinish.timestamp as " + str);
            this.mHandler.postDelayed(new Runnable() { // from class: com.oplus.theia.saupwk.SaupwkManager.1
                @Override // java.lang.Runnable
                public void run() {
                    String str2 = SystemProperties.get("persist.sys.rbsreason", "na");
                    SystemProperties.set("sys.sr.reboot_reason", str2);
                    SystemProperties.set("persist.sys.rbsreason", "");
                    String str3 = SystemProperties.get("persist.sys.saupwknum.rk", "0");
                    String str4 = SystemProperties.get("persist.sys.saupwknum.ru", "0");
                    String str5 = SystemProperties.get("persist.sys.saupwknum.nk", "0");
                    String str6 = SystemProperties.get("persist.sys.saupwknum.nu", "0");
                    Slog.d(SaupwkManager.TAG, "[SAUPWK]: rk:" + str3 + " ru:" + str4 + " nk:" + str5 + " nu:" + str6 + " rbsreason:" + str2);
                    int parseInt = Integer.parseInt(str3) + Integer.parseInt(str4) + Integer.parseInt(str5) + Integer.parseInt(str6);
                    SystemProperties.set("sys.saupwknum", String.valueOf(parseInt));
                    SystemProperties.set("persist.sys.saupwknum.rk", "0");
                    SystemProperties.set("persist.sys.saupwknum.ru", "0");
                    SystemProperties.set("persist.sys.saupwknum.nk", "0");
                    SystemProperties.set("persist.sys.saupwknum.nu", "0");
                    if (parseInt > 0) {
                        Slog.d(SaupwkManager.TAG, "[SAUPWK]: setting sys.saupwk.logdump to true ...\n");
                        SystemProperties.set("sys.saupwk.logdump", "true");
                    } else {
                        Slog.d(SaupwkManager.TAG, "[SAUPWK]: none power key detected ....\n");
                    }
                }
            }, 10000L);
        }
    }

    public static void saupwkLogDumpTrigger(Handler handler) {
        if (SystemProperties.get("persist.sys.saupwk_en", "").equals("1")) {
            Calendar calendar = Calendar.getInstance();
            String str = calendar.get(1) + "-" + calendar.get(2) + "-" + calendar.get(5) + " " + calendar.get(11) + ":" + calendar.get(12) + ":" + calendar.get(13);
            SystemProperties.set("sys.bootfinish.timestamp", str);
            Slog.w(TAG, "[SAUPWK]: marking sys.bootfinish.timestamp as " + str);
            handler.postDelayed(new Runnable() { // from class: com.oplus.theia.saupwk.SaupwkManager.2
                @Override // java.lang.Runnable
                public void run() {
                    String str2 = SystemProperties.get("persist.sys.rbsreason", "na");
                    SystemProperties.set("sys.sr.reboot_reason", str2);
                    SystemProperties.set("persist.sys.rbsreason", "");
                    String str3 = SystemProperties.get("persist.sys.saupwknum.rk", "0");
                    String str4 = SystemProperties.get("persist.sys.saupwknum.ru", "0");
                    String str5 = SystemProperties.get("persist.sys.saupwknum.nk", "0");
                    String str6 = SystemProperties.get("persist.sys.saupwknum.nu", "0");
                    Slog.d(SaupwkManager.TAG, "[SAUPWK]: rk:" + str3 + " ru:" + str4 + " nk:" + str5 + " nu:" + str6 + " rbsreason:" + str2);
                    int parseInt = Integer.parseInt(str3) + Integer.parseInt(str4) + Integer.parseInt(str5) + Integer.parseInt(str6);
                    SystemProperties.set("sys.saupwknum", String.valueOf(parseInt));
                    SystemProperties.set("persist.sys.saupwknum.rk", "0");
                    SystemProperties.set("persist.sys.saupwknum.ru", "0");
                    SystemProperties.set("persist.sys.saupwknum.nk", "0");
                    SystemProperties.set("persist.sys.saupwknum.nu", "0");
                    if (parseInt > 0) {
                        Slog.d(SaupwkManager.TAG, "[SAUPWK]: setting sys.saupwk.logdump to true ...\n");
                        SystemProperties.set("sys.saupwk.logdump", "true");
                    } else {
                        Slog.d(SaupwkManager.TAG, "[SAUPWK]: none power key detected ....\n");
                    }
                }
            }, 10000L);
        }
    }

    public static void saupwkStaticEnterSR(String str) {
        if (SystemProperties.get("persist.sys.saupwk_en", "").equals("1")) {
            if (str.equals("sau") || str.equals("silence")) {
                Calendar calendar = Calendar.getInstance();
                String str2 = calendar.get(1) + "-" + calendar.get(2) + "-" + calendar.get(5) + " " + calendar.get(11) + ":" + calendar.get(12) + ":" + calendar.get(13);
                SystemProperties.set("persist.sys.sr_start", str2);
                String str3 = SystemProperties.get("ro.build.version.ota", "na");
                SystemProperties.set("persist.sys.sau_from_ver", str3);
                StringBuilder sb = new StringBuilder();
                sb.append("[SAUPWK]: sau START from ");
                sb.append(str3);
                sb.append("@");
                sb.append(str2);
                sb.append(", reason=");
                sb.append(str.equals("silence") ? "slc" : "sau");
                Slog.w(TAG, sb.toString());
                SystemProperties.set("persist.sys.rbsreason", str.equals("silence") ? "slc" : "sau");
                Slog.w(TAG, "[SAUPWK]: persist.sys.rbsreason:" + SystemProperties.get("persist.sys.rbsreason", "na"));
            }
        }
    }

    public void saupwkEnterSR(String str) {
        if (SystemProperties.get("persist.sys.saupwk_en", "").equals("1")) {
            if (str.equals("sau") || str.equals("silence")) {
                Calendar calendar = Calendar.getInstance();
                String str2 = calendar.get(1) + "-" + calendar.get(2) + "-" + calendar.get(5) + " " + calendar.get(11) + ":" + calendar.get(12) + ":" + calendar.get(13);
                SystemProperties.set("persist.sys.sr_start", str2);
                String str3 = SystemProperties.get("ro.build.version.ota", "na");
                SystemProperties.set("persist.sys.sau_from_ver", str3);
                StringBuilder sb = new StringBuilder();
                sb.append("[SAUPWK]: sau START from ");
                sb.append(str3);
                sb.append("@");
                sb.append(str2);
                sb.append(", reason=");
                sb.append(str.equals("silence") ? "slc" : "sau");
                Slog.w(TAG, sb.toString());
                SystemProperties.set("persist.sys.rbsreason", str.equals("silence") ? "slc" : "sau");
                Slog.w(TAG, "[SAUPWK]: persist.sys.rbsreason:" + SystemProperties.get("persist.sys.rbsreason", "na"));
            }
        }
    }

    public void saupwkMarkSlsauEnd() {
        if (SystemProperties.get("persist.sys.saupwk_en", "").equals("1")) {
            Calendar calendar = Calendar.getInstance();
            String str = calendar.get(1) + "-" + calendar.get(2) + "-" + calendar.get(5) + " " + calendar.get(11) + ":" + calendar.get(12) + ":" + calendar.get(13);
            SystemProperties.set("persist.sys.sr_end", str);
            Slog.w(TAG, "[SAUPWK]: marking persist.sys.sr_end as " + str);
            String str2 = SystemProperties.get("ro.build.version.ota", "na");
            SystemProperties.set("persist.sys.sau_to_ver", str2);
            Slog.w(TAG, "[SAUPWK]: sau END with " + str2 + "@ " + str);
            String str3 = SystemProperties.get("sys.slsau_finished", "");
            SystemProperties.set("sys.slsau_finished", "true");
            Slog.d(TAG, "[SAUPWK]: setting property sys.slsau_finished:" + str3 + " to " + SystemProperties.get("sys.slsau_finished", ""));
        }
    }
}
