package x7;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import b6.LocalLog;
import d6.ConfigUpdateUtil;
import e6.SmartModeSharepref;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* compiled from: ChargeProtectionUtils.java */
/* renamed from: x7.d, reason: use source file name */
/* loaded from: classes.dex */
public class ChargeProtectionUtils {

    /* compiled from: ChargeProtectionUtils.java */
    /* renamed from: x7.d$a */
    /* loaded from: classes.dex */
    class a implements Comparator<Long> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(Long l10, Long l11) {
            return l10.compareTo(l11);
        }
    }

    /* compiled from: ChargeProtectionUtils.java */
    /* renamed from: x7.d$b */
    /* loaded from: classes.dex */
    class b implements Comparator<Long> {
        b() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(Long l10, Long l11) {
            return l10.compareTo(l11);
        }
    }

    public static void a(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent("oplus.intent.action.longchargeprotection.end.auto");
        intent.setPackage("com.oplus.battery");
        if (PendingIntent.getBroadcast(context, 0, intent, 603979776) != null) {
            alarmManager.cancel(PendingIntent.getBroadcast(context, 0, intent, 67108864));
        }
        LocalLog.a("ChargeProtectionUtils", "cancelAlarmForLongChargeProtectionForgetOneMonth");
    }

    public static void b(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent("oplus.intent.action.longchargeprotection.forget.one.month");
        intent.setPackage("com.oplus.battery");
        if (PendingIntent.getBroadcast(context, 0, intent, 603979776) != null) {
            alarmManager.cancel(PendingIntent.getBroadcast(context, 0, intent, 67108864));
        }
        LocalLog.a("ChargeProtectionUtils", "cancelAlarmForLongChargeProtectionForgetOneMonth");
    }

    public static void c(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent("oplus.intent.action.longchargeprotection.guide.open.switch");
        intent.setPackage("com.oplus.battery");
        if (PendingIntent.getBroadcast(context, 0, intent, 603979776) != null) {
            alarmManager.cancel(PendingIntent.getBroadcast(context, 0, intent, 67108864));
        }
        LocalLog.a("ChargeProtectionUtils", "cancelAlarmForLongChargeProtectionGuideOpenSwitch");
    }

    public static void d(Context context, boolean z10) {
        Intent intent = new Intent(z10 ? "oplus.intent.action.chargeprotection.start" : "oplus.intent.action.chargeprotection.end");
        intent.setPackage("com.oplus.battery");
        ((AlarmManager) context.getSystemService("alarm")).cancel(PendingIntent.getBroadcastAsUser(context, 0, intent, 67108864, UserHandle.SYSTEM));
    }

    public static long e(Context context) {
        return SmartModeSharepref.c(context, "close_charg_protect_time", -1L);
    }

    public static long f(Context context, long j10) {
        AlarmCollector e10 = AlarmCollector.e();
        if (e10 != null) {
            try {
                List<Long> c10 = e10.c();
                if (c10.isEmpty()) {
                    return 0L;
                }
                Collections.sort(c10, new a());
                long longValue = c10.get(0).longValue();
                LocalLog.a("ChargeProtectionUtils", "First Alarm is " + h(longValue) + ", nowRtc  = " + h(j10));
                if (longValue - j10 > ConfigUpdateUtil.n(context).y()) {
                    return longValue;
                }
                LocalLog.b("ChargeProtectionUtils", "First Alarm is " + h(longValue) + ", nowRtc  = " + h(j10) + ", too close , ignore it.");
                return -1L;
            } catch (Exception e11) {
                e11.printStackTrace();
            }
        }
        LocalLog.b("ChargeProtectionUtils", "alarmCollector is null. get first Alarm time fail.");
        return 0L;
    }

    public static long g(Context context, long j10) {
        CanlendarCollector g6 = CanlendarCollector.g();
        if (g6 != null) {
            try {
                List<Long> d10 = g6.d();
                if (d10.isEmpty()) {
                    LocalLog.b("ChargeProtectionUtils", "mCanlendarTmp.isEmpty()!");
                    return 0L;
                }
                Collections.sort(d10, new b());
                long longValue = d10.get(0).longValue();
                LocalLog.a("ChargeProtectionUtils", "First Schedule = " + h(longValue) + ", nowRtc  = " + h(j10));
                if (longValue - j10 > ConfigUpdateUtil.n(context).y()) {
                    return longValue;
                }
                LocalLog.b("ChargeProtectionUtils", "First Schedule = " + h(longValue) + ", nowRtc  = " + h(j10) + ", too close , ignore it.");
                return -1L;
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        }
        LocalLog.b("ChargeProtectionUtils", "CanlendarCollector is null. get first Schedule time fail.");
        return 0L;
    }

    public static String h(long j10) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        return calendar.getTime().toString();
    }

    public static void i(Context context, long j10) {
        SmartModeSharepref.e(context, "close_charg_protect_time", j10);
    }

    public static void j(Context context, boolean z10, long j10) {
        Intent intent = new Intent(z10 ? "oplus.intent.action.chargeprotection.start" : "oplus.intent.action.chargeprotection.end");
        intent.setPackage("com.oplus.battery");
        ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(0, j10, PendingIntent.getBroadcastAsUser(context, 0, intent, 67108864, UserHandle.SYSTEM));
    }

    public static void k() {
        CanlendarCollector g6 = CanlendarCollector.g();
        if (g6 != null) {
            g6.k();
        }
    }

    public static void l(Context context, long j10) {
        LocalLog.a("ChargeProtectionUtils", "setAlarmForLongChargeProtectionAutoQuit");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent("oplus.intent.action.longchargeprotection.end.auto");
        intent.setPackage("com.oplus.battery");
        alarmManager.setExact(1, System.currentTimeMillis() + j10, PendingIntent.getBroadcast(context, 0, intent, 67108864));
    }

    public static void m(Context context) {
        LocalLog.a("ChargeProtectionUtils", "setAlarmForLongChargeProtectionGuideOpenSwitch");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent("oplus.intent.action.longchargeprotection.guide.open.switch");
        intent.setPackage("com.oplus.battery");
        alarmManager.setExact(1, System.currentTimeMillis() + 300000, PendingIntent.getBroadcast(context, 0, intent, 67108864));
    }

    public static void n(Context context) {
        LocalLog.a("ChargeProtectionUtils", "setAlarmForSlowChargeProtectionGetAiRestData");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent("oplus.intent.action.slowchargeprotection.get.ai.data");
        intent.setPackage("com.oplus.battery");
        alarmManager.setExact(1, System.currentTimeMillis() + 43200000, PendingIntent.getBroadcast(context, 0, intent, 67108864));
    }
}
