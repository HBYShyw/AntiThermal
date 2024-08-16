package m9;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import b6.LocalLog;
import com.oplus.simplepowermonitor.alarm.AlarmReceiver;
import java.util.Calendar;
import r9.SimplePowerMonitorUtils;

/* compiled from: AlarmSetter.java */
/* renamed from: m9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class AlarmSetter {

    /* renamed from: a, reason: collision with root package name */
    private Context f15155a;

    /* renamed from: b, reason: collision with root package name */
    private AlarmManager f15156b;

    public AlarmSetter(Context context) {
        this.f15155a = context;
        this.f15156b = (AlarmManager) context.getSystemService("alarm");
    }

    public void a() {
        Intent intent = new Intent(this.f15155a, (Class<?>) AlarmReceiver.class);
        intent.setAction("com.oplus.app.spm.alarm.get.stats");
        if (PendingIntent.getBroadcast(this.f15155a, 0, intent, 603979776) != null) {
            this.f15156b.cancel(PendingIntent.getBroadcast(this.f15155a, 0, intent, 67108864));
        }
        LocalLog.a("AlarmSetter", "cancelAlarmForGetStats");
    }

    public void b() {
        Intent intent = new Intent(this.f15155a, (Class<?>) AlarmReceiver.class);
        intent.setAction("com.oplus.app.spm.alarm.screen.off");
        if (PendingIntent.getBroadcast(this.f15155a, 0, intent, 603979776) != null) {
            this.f15156b.cancel(PendingIntent.getBroadcast(this.f15155a, 0, intent, 67108864));
        }
        LocalLog.a("AlarmSetter", "cancelAlarmForScreenOffDetect");
    }

    public void c() {
        Intent intent = new Intent(this.f15155a, (Class<?>) AlarmReceiver.class);
        intent.setAction("com.oplus.app.spm.alarm.screen.on");
        if (PendingIntent.getBroadcast(this.f15155a, 0, intent, 603979776) != null) {
            this.f15156b.cancel(PendingIntent.getBroadcast(this.f15155a, 0, intent, 67108864));
        }
        LocalLog.a("AlarmSetter", "cancelAlarmForScreenOnDetect");
    }

    public void d() {
        LocalLog.a("AlarmSetter", "setAlarmForGetStats");
        Intent intent = new Intent(this.f15155a, (Class<?>) AlarmReceiver.class);
        intent.setAction("com.oplus.app.spm.alarm.get.stats");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f15155a, 0, intent, 67108864);
        long currentTimeMillis = System.currentTimeMillis();
        long j10 = SimplePowerMonitorUtils.f17664o;
        LocalLog.a("AlarmSetter", "HIGH_POWER_NOTIFICATION_ALARM_TIME: " + SimplePowerMonitorUtils.f17664o);
        this.f15156b.setExact(1, currentTimeMillis + j10, broadcast);
    }

    public void e() {
        LocalLog.a("AlarmSetter", "setAlarmForScreenOffDetect");
        Intent intent = new Intent(this.f15155a, (Class<?>) AlarmReceiver.class);
        intent.setAction("com.oplus.app.spm.alarm.screen.off");
        this.f15156b.setExact(1, System.currentTimeMillis() + 5000 + SimplePowerMonitorUtils.f17662m, PendingIntent.getBroadcast(this.f15155a, 0, intent, 67108864));
    }

    public void f() {
        LocalLog.a("AlarmSetter", "setAlarmForScreenOnDetect");
        Intent intent = new Intent(this.f15155a, (Class<?>) AlarmReceiver.class);
        intent.setAction("com.oplus.app.spm.alarm.screen.on");
        this.f15156b.setExact(1, System.currentTimeMillis() + SimplePowerMonitorUtils.f17661l, PendingIntent.getBroadcast(this.f15155a, 0, intent, 67108864));
    }

    public void g() {
        LocalLog.a("AlarmSetter", "setAlarmForStoreData");
        Intent intent = new Intent(this.f15155a, (Class<?>) AlarmReceiver.class);
        intent.setAction("com.oplus.app.spm.alarm.store.db");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f15155a, 24, intent, 67108864);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(11, 24);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        this.f15156b.setExact(0, calendar.getTimeInMillis(), broadcast);
    }
}
