package com.oplus.powermanager.powercurve;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.net.NetworkStatsHistory;
import android.net.NetworkTemplate;
import android.os.BatteryUsageStats;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import b6.LocalLog;
import c9.WirelessChargingController;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.oguard.data.database.OGuardDataBase;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.statistics.util.TimeInfoUtil;
import com.oplus.thermalcontrol.ThermalControlUtils;
import d6.ConfigUpdateUtil;
import f6.f;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import o8.BatteryStatusCenter;
import o9.HighPowerHelper;
import o9.HighPowerSipper;
import p6.AppStandbyStatistic;
import p9.PowerMonitor;
import q9.AppStats;
import q9.NetWork;
import r8.BatterySipper;
import t8.PowerUsageManager;
import v4.BatteryStatsManager;
import x4.AppSwitchObserverHelper;
import x8.DataBaseUtil;

/* loaded from: classes2.dex */
public class SaveBatteryStatsReceiver extends BroadcastReceiver {

    /* renamed from: i, reason: collision with root package name */
    private static NetWork f10303i;

    /* renamed from: j, reason: collision with root package name */
    private static final Object f10304j = new Object();

    /* renamed from: a, reason: collision with root package name */
    private BatteryUsageStats f10305a;

    /* renamed from: b, reason: collision with root package name */
    private PowerManager.WakeLock f10306b;

    /* renamed from: c, reason: collision with root package name */
    private INetworkStatsSession f10307c;

    /* renamed from: d, reason: collision with root package name */
    private INetworkStatsService f10308d;

    /* renamed from: e, reason: collision with root package name */
    private NetworkTemplate f10309e;

    /* renamed from: f, reason: collision with root package name */
    private BatterySipper f10310f = null;

    /* renamed from: g, reason: collision with root package name */
    private int f10311g = 0;

    /* renamed from: h, reason: collision with root package name */
    private Context f10312h;

    /* loaded from: classes2.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Context f10313e;

        a(Context context) {
            this.f10313e = context;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (SaveBatteryStatsReceiver.f10304j) {
                try {
                    LocalLog.a("SaveBatteryStats", "mTime = " + SaveBatteryStatsReceiver.this.f10311g + " start wait");
                    SaveBatteryStatsReceiver.f10304j.wait(5000L);
                } catch (InterruptedException e10) {
                    e10.printStackTrace();
                }
                LocalLog.a("SaveBatteryStats", "mTime = " + SaveBatteryStatsReceiver.this.f10311g + " get lock");
            }
            Iterator<Map.Entry<String, HighPowerSipper>> it = HighPowerHelper.f(SaveBatteryStatsReceiver.this.f10312h).e().entrySet().iterator();
            while (it.hasNext()) {
                it.next().getValue().d();
            }
            PowerMonitor h10 = PowerMonitor.h(this.f10313e);
            boolean isScreenOn = ThermalControlUtils.getInstance(SaveBatteryStatsReceiver.this.f10312h).isScreenOn();
            if (!isScreenOn) {
                h10.p();
            } else {
                AppSwitchObserverHelper.d(SaveBatteryStatsReceiver.this.f10312h).g();
            }
            Iterator<Map.Entry<String, AppStats>> it2 = PowerMonitor.j().entrySet().iterator();
            while (it2.hasNext()) {
                it2.next().getValue().a();
            }
            if (!isScreenOn) {
                h10.n(false);
            }
            new AppStandbyStatistic().a(SaveBatteryStatsReceiver.this.f10312h);
            f.I1(SaveBatteryStatsReceiver.this.f10312h);
            if (y5.b.x()) {
                WirelessChargingController.H(SaveBatteryStatsReceiver.this.f10312h).S();
            }
            OGuardDataBase.v(SaveBatteryStatsReceiver.this.f10312h).u().a(System.currentTimeMillis() - TimeInfoUtil.MILLISECOND_OF_A_WEEK);
            SaveBatteryStatsReceiver.q(SaveBatteryStatsReceiver.this.f10312h);
        }
    }

    /* loaded from: classes2.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SaveBatteryStatsReceiver.this.f10306b.acquire(5000L);
            SaveBatteryStatsReceiver saveBatteryStatsReceiver = SaveBatteryStatsReceiver.this;
            saveBatteryStatsReceiver.r(saveBatteryStatsReceiver.f10312h);
            SaveBatteryStatsReceiver.this.f10305a = BatteryStatsManager.i().f();
            SaveBatteryStatsReceiver saveBatteryStatsReceiver2 = SaveBatteryStatsReceiver.this;
            saveBatteryStatsReceiver2.l(saveBatteryStatsReceiver2.f10312h);
            synchronized (SaveBatteryStatsReceiver.f10304j) {
                LocalLog.a("SaveBatteryStats", "mTime = " + SaveBatteryStatsReceiver.this.f10311g + " notify lock");
                SaveBatteryStatsReceiver.f10304j.notifyAll();
            }
            SaveBatteryStatsReceiver.p(SaveBatteryStatsReceiver.this.f10312h);
            if (SaveBatteryStatsReceiver.this.f10306b.isHeld()) {
                SaveBatteryStatsReceiver.this.f10306b.release();
            }
        }
    }

    private void g(Context context) {
    }

    private int h() {
        if (BatteryStatusCenter.a(this.f10312h).b()) {
            return 1;
        }
        return (BatteryStatusCenter.a(this.f10312h).c() || BatteryStatusCenter.a(this.f10312h).d()) ? 2 : 0;
    }

    private long i() {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.set(11, 18);
        calendar.set(12, 20);
        calendar.set(13, 0);
        calendar.set(14, 0);
        if (calendar2.before(calendar)) {
            calendar.add(5, -1);
        }
        calendar.set(12, 0);
        return calendar.getTimeInMillis();
    }

    private String j() {
        DateTimeFormatterBuilder appendPattern = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm");
        Locale locale = Locale.ENGLISH;
        return LocalDateTime.now().format(appendPattern.toFormatter(locale).withDecimalStyle(DecimalStyle.of(locale)));
    }

    private boolean k(String str, BatterySipper batterySipper) {
        if (!"dex2oat".equals(str)) {
            return false;
        }
        BatterySipper batterySipper2 = this.f10310f;
        if (batterySipper2 == null) {
            this.f10310f = batterySipper;
            LocalLog.a("SaveBatteryStats", "hanleDex2oat: find sipperDex2oat. uid=" + batterySipper.b() + String.format(Locale.getDefault(), ", power=%.2f, percent=%.2f", Double.valueOf(batterySipper.f17617i), Double.valueOf(batterySipper.f17620l)));
        } else {
            batterySipper2.f17620l += batterySipper.f17620l;
            LocalLog.a("SaveBatteryStats", "hanleDex2oat: add(sipper); uid=" + batterySipper.b() + String.format(Locale.getDefault(), ",total power=%.2f, total percent=%.2f", Double.valueOf(this.f10310f.f17617i), Double.valueOf(this.f10310f.f17620l)));
        }
        return true;
    }

    private void m(Context context) {
        LocalLog.a("SaveBatteryStats", "saveFakePowerGaugeDate");
        ContentValues contentValues = new ContentValues();
        contentValues.put("drain_type", "Fake");
        contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, "Fake");
        contentValues.put("packageWithHighestDrain", "Fake");
        contentValues.put("usage_time", (Integer) 0);
        contentValues.put("foreground_act_time", (Integer) 0);
        contentValues.put("background_act_time", (Integer) 0);
        contentValues.put("awake_time", (Integer) 0);
        contentValues.put("wlan_tx_bytes", (Integer) 0);
        contentValues.put("wlan_rx_bytes", (Integer) 0);
        contentValues.put("power", (Integer) 0);
        contentValues.put("saved_time", j());
        contentValues.put("sipper_uid", (Integer) 0);
        contentValues.put("battery_level", Integer.valueOf(PowerUsageManager.x(this.f10312h).r()));
        contentValues.put("battery_status", Integer.valueOf(h()));
        contentValues.put("saved_time_millis", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("icon", (byte[]) null);
        try {
            context.getContentResolver().insert(DataBaseUtil.f19650b, contentValues);
        } catch (Exception e10) {
            Log.e("SaveBatteryStats", "Fail to insert e=" + e10);
        }
    }

    private void n(Context context, BatterySipper batterySipper) {
        ContentValues contentValues = new ContentValues();
        BatterySipper.a aVar = batterySipper.f17623o;
        if (aVar == BatterySipper.a.PHONE) {
            contentValues.put("drain_type", "PHONE");
            contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, "PHONE");
            contentValues.put("packageWithHighestDrain", batterySipper.f17622n);
            contentValues.put("usage_time", Long.valueOf(batterySipper.f17613e));
        } else if (aVar == BatterySipper.a.SCREEN) {
            contentValues.put("drain_type", "SCREEN");
            contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, "SCREEN");
            contentValues.put("packageWithHighestDrain", batterySipper.f17622n);
            contentValues.put("usage_time", Long.valueOf(batterySipper.f17613e));
        } else if (aVar == BatterySipper.a.CELL) {
            contentValues.put("drain_type", "CELL");
            contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, "CELL");
            contentValues.put("packageWithHighestDrain", batterySipper.f17622n);
            contentValues.put("usage_time", Long.valueOf(batterySipper.f17613e));
        } else if (aVar == BatterySipper.a.WIFI) {
            contentValues.put("drain_type", "WIFI");
            contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, "WIFI");
            contentValues.put("packageWithHighestDrain", batterySipper.f17622n);
            contentValues.put("usage_time", Long.valueOf(batterySipper.f17614f));
        } else if (aVar == BatterySipper.a.IDLE) {
            contentValues.put("drain_type", "IDLE");
            contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, "IDLE");
            contentValues.put("packageWithHighestDrain", batterySipper.f17622n);
            contentValues.put("usage_time", Long.valueOf(batterySipper.f17613e));
        } else if (aVar == BatterySipper.a.BLUETOOTH) {
            contentValues.put("drain_type", "BLUETOOTH");
            contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, "BLUETOOTH");
            contentValues.put("packageWithHighestDrain", batterySipper.f17622n);
            contentValues.put("usage_time", Long.valueOf(batterySipper.f17615g));
        }
        contentValues.put("foreground_act_time", (Integer) 0);
        contentValues.put("background_act_time", (Integer) 0);
        contentValues.put("awake_time", (Integer) 0);
        contentValues.put("wlan_tx_bytes", (Integer) 0);
        contentValues.put("wlan_rx_bytes", (Integer) 0);
        contentValues.put("saved_time_millis", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("power", Double.valueOf(batterySipper.f17617i));
        contentValues.put("sipper_uid", (Integer) 0);
        contentValues.put("battery_level", Integer.valueOf(PowerUsageManager.x(this.f10312h).r()));
        contentValues.put("battery_status", Integer.valueOf(h()));
        contentValues.put("icon", (byte[]) null);
        contentValues.put("saved_time", j());
        try {
            context.getContentResolver().insert(DataBaseUtil.f19650b, contentValues);
        } catch (Exception e10) {
            Log.e("SaveBatteryStats", "Fail to insert e=" + e10);
        }
    }

    private void o(Context context, BatterySipper batterySipper) {
        NetworkStatsHistory.Entry entry;
        long j10;
        if (batterySipper == null) {
            return;
        }
        if (f.e1(batterySipper)) {
            LocalLog.a("SaveBatteryStats", "savePowerGaugeDate: ignore name:" + batterySipper.f17622n + ", uid=" + batterySipper.b());
            return;
        }
        try {
            entry = f10303i.d(batterySipper.b(), i(), System.currentTimeMillis());
        } catch (RemoteException e10) {
            e10.printStackTrace();
            entry = null;
        }
        long j11 = 0;
        if (entry != null) {
            j10 = entry.rxBytes + 0;
            j11 = 0 + entry.txBytes;
        } else {
            j10 = 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("drain_type", "APP");
        contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, batterySipper.f17622n);
        contentValues.put("packageWithHighestDrain", batterySipper.f17622n);
        contentValues.put("usage_time", Long.valueOf(batterySipper.f17610b));
        contentValues.put("foreground_act_time", Long.valueOf(batterySipper.f17610b));
        contentValues.put("background_act_time", Long.valueOf(batterySipper.f17611c));
        contentValues.put("awake_time", Long.valueOf(batterySipper.f17612d));
        contentValues.put("wlan_tx_bytes", Long.valueOf(j11));
        contentValues.put("wlan_rx_bytes", Long.valueOf(j10));
        if ("com.aiunit.aon".equals(batterySipper.f17622n)) {
            contentValues.put("power", Double.valueOf(batterySipper.f17617i - ((batterySipper.f17616h * 5.0d) / 6.0d)));
        } else {
            contentValues.put("power", Double.valueOf(batterySipper.f17617i));
        }
        contentValues.put("saved_time", j());
        contentValues.put("sipper_uid", Integer.valueOf(batterySipper.b()));
        contentValues.put("battery_level", Integer.valueOf(PowerUsageManager.x(this.f10312h).r()));
        contentValues.put("battery_status", Integer.valueOf(h()));
        contentValues.put("saved_time_millis", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("icon", (byte[]) null);
        try {
            context.getContentResolver().insert(DataBaseUtil.f19650b, contentValues);
        } catch (Exception e11) {
            Log.e("SaveBatteryStats", "Fail to insert e=" + e11);
        }
    }

    public static void p(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(13, 0);
        calendar.set(14, 0);
        if (calendar.get(12) < 30) {
            calendar.set(12, 30);
        } else {
            calendar.add(11, 1);
            calendar.set(12, 0);
        }
        long timeInMillis = calendar.getTimeInMillis();
        Intent intent = new Intent(context, (Class<?>) SaveBatteryStatsReceiver.class);
        intent.setAction("save_battery_stats_action");
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 335544320);
        if (!ConfigUpdateUtil.n(context).d()) {
            alarmManager.setExact(1, timeInMillis, broadcast);
        } else {
            alarmManager.setExact(0, timeInMillis, broadcast);
        }
        LocalLog.a("SaveBatteryStats", "setSaveBatteryStatsAlarm time = " + timeInMillis);
    }

    public static void q(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.set(11, 18);
        calendar.set(12, 5);
        calendar.set(13, 0);
        calendar.set(14, 0);
        if (calendar.before(calendar2)) {
            LocalLog.a("SaveBatteryStats", "setSaveBatteryStatsResetAlarm day + 1 ");
            calendar.add(5, 1);
        }
        long timeInMillis = calendar.getTimeInMillis();
        Intent intent = new Intent(context, (Class<?>) SaveBatteryStatsReceiver.class);
        intent.setAction("reset_battery_stats_action");
        intent.putExtra("time", 18);
        alarmManager.setExact(0, timeInMillis, PendingIntent.getBroadcast(context, 18, intent, 335544320));
        LocalLog.a("SaveBatteryStats", "setSaveBatteryStatsResetAlarm time = 18");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r(Context context) {
        try {
            LocalLog.a("SaveBatteryStats", "delete abnormal data is " + context.getContentResolver().delete(DataBaseUtil.f19650b, "saved_time_millis<? OR saved_time_millis>?", new String[]{String.valueOf(System.currentTimeMillis() - 259200000), String.valueOf(System.currentTimeMillis() + TimeInfoUtil.MILLISECOND_OF_A_DAY)}));
        } catch (Exception e10) {
            Log.e("SaveBatteryStats", "updateDatabase delete abnormal data error : " + e10);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0188  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void l(Context context) {
        String E;
        Iterator<BatterySipper> it;
        double d10;
        boolean z10;
        boolean z11;
        ApplicationInfo applicationInfo;
        LocalLog.l("SaveBatteryStats", "saveBatteryStats");
        List<BatterySipper> y4 = f.y(f.g(this.f10305a.getUidBatteryConsumers()));
        double c10 = f.c(y4, context);
        if (Double.isNaN(c10)) {
            LocalLog.a("SaveBatteryStats", "saveBatteryStats: totalPower isNaN, return!");
            return;
        }
        if (y4.isEmpty()) {
            LocalLog.a("SaveBatteryStats", "usageList isEmpty saveFakePowerGaugeDate");
            m(context);
        }
        double d11 = UserProfileInfo.Constant.NA_LAT_LON;
        if (c10 <= UserProfileInfo.Constant.NA_LAT_LON) {
            LocalLog.a("SaveBatteryStats", "saveBatteryStats: totalPower = " + c10 + ", return!");
            m(context);
            return;
        }
        boolean z12 = false;
        LocalLog.a("SaveBatteryStats", String.format(Locale.getDefault(), "saveBatteryStats: [Refresh-Power 1] totalPower=%.2f", Double.valueOf(c10)));
        Iterator<BatterySipper> it2 = y4.iterator();
        while (it2.hasNext()) {
            BatterySipper next = it2.next();
            double d12 = next.f17617i;
            if (d12 > d11) {
                double d13 = (d12 / c10) * 100.0d;
                if (Double.isNaN(d13)) {
                    LocalLog.a("SaveBatteryStats", "saveBatteryStats: percentOfTotal=" + d13 + ", ignore!");
                } else {
                    next.f17620l = d13;
                    if (next.f17623o != BatterySipper.a.APP) {
                        LocalLog.a("SaveBatteryStats", "saveHardwarePowerDate" + next.f17623o + next.f17617i);
                        n(context, next);
                    } else if (next.b() == 0) {
                        LocalLog.a("SaveBatteryStats", "saveBatteryStats: ignore ROOT_UID");
                    } else {
                        if (next.b() == 1000) {
                            E = next.f17622n;
                        } else {
                            E = f.E(context, next);
                        }
                        if (E == null) {
                            it = it2;
                            d10 = c10;
                            z10 = z12;
                        } else if ("".equals(E)) {
                            it = it2;
                            d10 = c10;
                            z10 = false;
                        } else {
                            next.f17622n = E;
                            try {
                                it = it2;
                            } catch (PackageManager.NameNotFoundException unused) {
                                it = it2;
                            }
                            try {
                                applicationInfo = context.getPackageManager().getApplicationInfo(E, 128);
                                d10 = c10;
                            } catch (PackageManager.NameNotFoundException unused2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("saveBatteryStats: NameNotFoundException.sipper uid=");
                                sb2.append(next.b());
                                sb2.append(", packgeName=");
                                sb2.append(E);
                                d10 = c10;
                                sb2.append(String.format(Locale.getDefault(), ", power=%.2f, percent=%.2f", Double.valueOf(d12), Double.valueOf(d13)));
                                LocalLog.a("SaveBatteryStats", sb2.toString());
                                applicationInfo = null;
                                if (applicationInfo == null) {
                                }
                                if (!f.j1(E, next)) {
                                }
                                z10 = false;
                                z11 = true;
                                z12 = z10;
                                it2 = it;
                                c10 = d10;
                                d11 = UserProfileInfo.Constant.NA_LAT_LON;
                            }
                            if (applicationInfo == null && !applicationInfo.enabled && !f.m1(applicationInfo, context)) {
                                LocalLog.a("SaveBatteryStats", "saveBatteryStats: disabled pkg=" + E);
                                it2 = it;
                                c10 = d10;
                                d11 = UserProfileInfo.Constant.NA_LAT_LON;
                                z12 = false;
                            } else {
                                if (!f.j1(E, next)) {
                                    o(context, next);
                                } else if (!k(E, next)) {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("saveBatteryStats: ignore sipper. uid=");
                                    sb3.append(next.b());
                                    sb3.append(", packgeName=");
                                    sb3.append(E);
                                    Locale locale = Locale.getDefault();
                                    Double valueOf = Double.valueOf(d12);
                                    z10 = false;
                                    z11 = true;
                                    sb3.append(String.format(locale, ", power=%.2f, percent=%.2f", valueOf, Double.valueOf(d13)));
                                    LocalLog.a("SaveBatteryStats", sb3.toString());
                                    z12 = z10;
                                    it2 = it;
                                    c10 = d10;
                                }
                                z10 = false;
                                z11 = true;
                                z12 = z10;
                                it2 = it;
                                c10 = d10;
                            }
                        }
                        z11 = true;
                        if (f.j1(next.f17622n, next)) {
                            o(context, next);
                        } else if (!k(next.f17622n, next)) {
                            LocalLog.a("SaveBatteryStats", "saveBatteryStats: ignore sipper. uid=" + next.b() + ", packgeName=" + E);
                        }
                        z12 = z10;
                        it2 = it;
                        c10 = d10;
                    }
                    d11 = UserProfileInfo.Constant.NA_LAT_LON;
                }
            }
        }
        BatteryStatusCenter.a(this.f10312h).e();
        g(context);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("save_battery_stats_action".equals(intent.getAction()) || "reset_battery_stats_action".equals(intent.getAction())) {
            this.f10312h = context;
            this.f10306b = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "curve:save_stats");
            INetworkStatsService asInterface = INetworkStatsService.Stub.asInterface(ServiceManager.getService("netstats"));
            this.f10308d = asInterface;
            try {
                this.f10307c = asInterface.openSession();
            } catch (RemoteException e10) {
                e10.printStackTrace();
            }
            NetworkTemplate buildTemplateWifiWildcard = NetworkTemplate.buildTemplateWifiWildcard();
            this.f10309e = buildTemplateWifiWildcard;
            f10303i = new NetWork(this.f10307c, NetWork.a(buildTemplateWifiWildcard, 30));
            Bundle extras = intent.getExtras();
            if (extras == null) {
                LocalLog.a("SaveBatteryStats", "onReceive bundle is null!!!");
                this.f10311g = 0;
            } else {
                this.f10311g = extras.getInt("time", 0);
            }
            LocalLog.a("SaveBatteryStats", "onReceive mTime = " + this.f10311g + " this = " + this);
            if (this.f10311g == 18) {
                new Thread(new a(context)).start();
            } else {
                new Thread(new b()).start();
            }
        }
    }
}
