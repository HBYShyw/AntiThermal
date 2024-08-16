package y8;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import b6.LocalLog;
import com.oplus.powermanager.smartCharge.SmartChargeAlarmReceiver;
import java.util.Calendar;

/* compiled from: SmartChargeAlarmSetter.java */
/* renamed from: y8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class SmartChargeAlarmSetter {

    /* renamed from: b, reason: collision with root package name */
    private static volatile SmartChargeAlarmSetter f19905b;

    /* renamed from: a, reason: collision with root package name */
    private Context f19906a;

    public SmartChargeAlarmSetter(Context context) {
        this.f19906a = context;
    }

    public static SmartChargeAlarmSetter b(Context context) {
        if (f19905b == null) {
            synchronized (SmartChargeAlarmSetter.class) {
                if (f19905b == null) {
                    f19905b = new SmartChargeAlarmSetter(context);
                }
            }
        }
        return f19905b;
    }

    public void a() {
        AlarmManager alarmManager = (AlarmManager) this.f19906a.getSystemService("alarm");
        Intent intent = new Intent(this.f19906a, (Class<?>) SmartChargeAlarmReceiver.class);
        intent.setAction("com.oplus.app.smart.charge.income.one.day");
        if (PendingIntent.getBroadcast(this.f19906a, 0, intent, 603979776) != null) {
            alarmManager.cancel(PendingIntent.getBroadcast(this.f19906a, 0, intent, 67108864));
        }
        LocalLog.a("SmartChargeAlarmSetter", "cancelAlarmForHandleIncomeData");
    }

    public void c() {
        AlarmManager alarmManager = (AlarmManager) this.f19906a.getSystemService("alarm");
        LocalLog.a("SmartChargeAlarmSetter", "setAlarmForHandleIncomeData");
        Intent intent = new Intent(this.f19906a, (Class<?>) SmartChargeAlarmReceiver.class);
        intent.setAction("com.oplus.app.smart.charge.income.one.day");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f19906a, 24, intent, 67108864);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(11, 24);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        alarmManager.setExact(0, calendar.getTimeInMillis(), broadcast);
    }
}
