package v4;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.BatteryUsageStats;
import android.os.Bundle;
import android.os.OplusBatteryManager;
import android.os.UserHandle;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import java.util.List;
import o8.BatteryStatusCenter;
import q6.BatteryCardUtil;
import s8.PowerSaveHelper;
import w4.Affair;
import w4.IAffairCallback;
import y5.AppFeature;

/* compiled from: BatteryStatsManager.java */
/* renamed from: v4.a, reason: use source file name */
/* loaded from: classes.dex */
public class BatteryStatsManager implements IAffairCallback {

    /* renamed from: q, reason: collision with root package name */
    private static BatteryStatsManager f19049q;

    /* renamed from: k, reason: collision with root package name */
    private int f19056k;

    /* renamed from: p, reason: collision with root package name */
    private Context f19061p;

    /* renamed from: e, reason: collision with root package name */
    private boolean f19050e = false;

    /* renamed from: f, reason: collision with root package name */
    private boolean f19051f = false;

    /* renamed from: g, reason: collision with root package name */
    private int f19052g = 0;

    /* renamed from: h, reason: collision with root package name */
    private int f19053h = 60;

    /* renamed from: i, reason: collision with root package name */
    private int f19054i = 30;

    /* renamed from: j, reason: collision with root package name */
    private int f19055j = 60;

    /* renamed from: l, reason: collision with root package name */
    private int f19057l = 0;

    /* renamed from: m, reason: collision with root package name */
    private int f19058m = 0;

    /* renamed from: n, reason: collision with root package name */
    private int f19059n = 0;

    /* renamed from: o, reason: collision with root package name */
    private int f19060o = 1;

    private BatteryStatsManager() {
        this.f19056k = 0;
        this.f19061p = null;
        this.f19061p = GuardElfContext.e().c();
        this.f19056k = g();
        a();
        b();
    }

    private void a() {
        try {
            this.f19058m = ((Integer) f6.d.a().b("android.os.OplusBatteryManager", "getBattPPSChgIng", new Class[0]).invoke(new OplusBatteryManager(), new Object[0])).intValue();
        } catch (Exception e10) {
            LocalLog.b("BatteryStatsManager", "getBattPPSChgIng e " + e10.toString());
        }
        LocalLog.a("BatteryStatsManager", "getBattPPSChgIng = " + this.f19058m);
    }

    private void b() {
        try {
            this.f19059n = ((Integer) f6.d.a().b("android.os.OplusBatteryManager", "getBattPPSChgPower", new Class[0]).invoke(new OplusBatteryManager(), new Object[0])).intValue();
        } catch (Exception e10) {
            LocalLog.b("BatteryStatsManager", "getBattPPSChgPower e " + e10.toString());
        }
        LocalLog.a("BatteryStatsManager", "getBattPPSChgPower = " + this.f19059n);
    }

    private Intent h(Context context) {
        Intent intent = new Intent("power.intent.action.BATTERY_LOW_TO_FIND_PHONE");
        intent.setPackage(f6.f.h("kge&kgdgzg{&nafleqx`gfm", 8));
        List<ResolveInfo> queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 64);
        if (queryBroadcastReceivers != null && queryBroadcastReceivers.size() > 0) {
            return intent;
        }
        intent.setPackage("com.oplus.findservice");
        List<ResolveInfo> queryBroadcastReceivers2 = context.getPackageManager().queryBroadcastReceivers(intent, 64);
        if (queryBroadcastReceivers2 == null || queryBroadcastReceivers2.size() <= 0) {
            return null;
        }
        return intent;
    }

    public static synchronized BatteryStatsManager i() {
        BatteryStatsManager batteryStatsManager;
        synchronized (BatteryStatsManager.class) {
            if (f19049q == null) {
                f19049q = new BatteryStatsManager();
            }
            batteryStatsManager = f19049q;
        }
        return batteryStatsManager;
    }

    private void m(boolean z10) {
        if (z10) {
            return;
        }
        f6.f.J1(this.f19061p);
    }

    private void p() {
        int i10;
        int i11 = 3;
        if (this.f19058m == 1 && ((i10 = this.f19059n) == 150 || i10 == 125 || i10 == 100)) {
            Settings.System.putIntForUser(this.f19061p.getContentResolver(), "oplus_battery_settings_plugged_type", 3, 0);
            return;
        }
        int i12 = this.f19057l;
        int i13 = 4;
        if (i12 == 0) {
            i13 = -1;
        } else if (i12 == 4) {
            int i14 = this.f19056k;
            if (i14 == 3 || i14 == 4 || i14 < 2) {
                i13 = 0;
            }
        } else {
            int i15 = this.f19056k;
            if (i15 == 1) {
                i11 = 1;
            } else if (i15 == 2) {
                i11 = 2;
            } else if (i15 != 20) {
                if (i15 == 25 || i15 == 30) {
                    i11 = (y5.b.D() && this.f19056k == 25) ? 33 : 5;
                } else {
                    i11 = 0;
                }
            }
            i13 = this.f19060o == 5 ? -2 : i11;
        }
        LocalLog.a("BatteryStatsManager", "charge type " + i13);
        Settings.System.putIntForUser(this.f19061p.getContentResolver(), "oplus_battery_settings_plugged_type", i13, 0);
    }

    public int c() {
        return this.f19053h;
    }

    public int d() {
        return this.f19060o;
    }

    public int e() {
        return this.f19054i;
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        int i11;
        if (i10 == 204) {
            int intExtra = intent.getIntExtra("level", 0);
            int intExtra2 = intent.getIntExtra("status", 0);
            int intExtra3 = intent.getIntExtra("plugged", 0);
            if (this.f19057l == intExtra3 && this.f19060o == intExtra2) {
                if (this.f19055j != intExtra) {
                    LocalLog.a("BatteryStatsManager", "battery level change");
                    this.f19055j = intExtra;
                    BatteryCardUtil.a(this.f19061p).b(intExtra, intExtra2, intExtra3);
                }
            } else {
                LocalLog.a("BatteryStatsManager", "plug type change");
                this.f19060o = intExtra2;
                this.f19057l = intExtra3;
                BatteryCardUtil.a(this.f19061p).b(intExtra, intExtra2, intExtra3);
                p();
            }
            k(intExtra, intExtra2, intExtra3);
            return;
        }
        if (i10 != 229) {
            return;
        }
        int intExtra4 = intent.getIntExtra("chargertechnology", 0);
        int intExtra5 = intent.getIntExtra("chargewattage", -1);
        int intExtra6 = intent.getIntExtra("pps_chg_mode", -1);
        LocalLog.a("BatteryStatsManager", "chargeWattage =" + intExtra5 + ", passState =" + intExtra6);
        this.f19058m = intExtra6;
        this.f19059n = intExtra5;
        if (y5.b.D() && AppFeature.n()) {
            LocalLog.a("BatteryStatsManager", "chargeWattage =" + intExtra5);
            if (intExtra5 >= 100) {
                Settings.System.putIntForUser(this.f19061p.getContentResolver(), "oplus_battery_settings_plugged_type", 6, 0);
                return;
            }
        }
        if (this.f19058m == 1 && ((i11 = this.f19059n) == 150 || i11 == 125 || i11 == 100)) {
            Settings.System.putIntForUser(this.f19061p.getContentResolver(), "oplus_battery_settings_plugged_type", 3, 0);
        } else if (this.f19056k != intExtra4) {
            this.f19056k = intExtra4;
            p();
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    public BatteryUsageStats f() {
        android.os.BatteryStatsManager batteryStatsManager = (android.os.BatteryStatsManager) this.f19061p.getSystemService(android.os.BatteryStatsManager.class);
        if (batteryStatsManager != null) {
            return batteryStatsManager.getBatteryUsageStats();
        }
        LocalLog.b("BatteryStatsManager", "getBatteryUsageStats(): get batteryStatsManager null, build it");
        return new BatteryUsageStats.Builder(new String[0]).build();
    }

    public int g() {
        int i10 = 0;
        try {
            i10 = ((Integer) f6.d.a().b("android.os.OplusBatteryManager", "getChargerTechnology", new Class[0]).invoke(new OplusBatteryManager(), new Object[0])).intValue();
        } catch (Exception e10) {
            LocalLog.b("BatteryStatsManager", "getChargerTechnology " + e10.toString());
        }
        LocalLog.a("BatteryStatsManager", "get chargerTechnology = " + i10);
        return i10;
    }

    public int j() {
        return this.f19057l;
    }

    public void k(int i10, int i11, int i12) {
        Intent h10;
        boolean z10 = i11 == 2 || (i11 == 1 && (i12 & 15) != 0);
        boolean z11 = (i12 & 15) != 0;
        if (z11 != this.f19050e) {
            this.f19050e = z11;
            m(z11);
        }
        if (!z10 && i10 == 15 && this.f19053h > i10 && (h10 = h(this.f19061p)) != null) {
            LocalLog.a("BatteryStatsManager", "send find my phone broadcast");
            this.f19061p.sendBroadcast(h10, "oplus.permission.OPLUS_COMPONENT_SAFE", -1);
        }
        if (!this.f19051f) {
            if (i10 > f6.f.U(this.f19061p)) {
                f6.f.J1(this.f19061p);
            }
            this.f19051f = true;
        }
        if (i10 != this.f19053h) {
            f6.f.u2(this.f19061p, i10);
        }
        this.f19053h = i10;
        BatteryStatusCenter.a(this.f19061p).f(z10);
        if (UserHandle.myUserId() == 0) {
            PowerSaveHelper.m(this.f19061p).B(i10);
        }
    }

    public void l(Context context, int i10, int i11, int i12, int i13) {
        this.f19053h = i12;
        boolean z10 = (i11 & 15) != 0;
        if (z10 != this.f19050e) {
            this.f19050e = z10;
            m(z10);
        }
        this.f19054i = i13;
    }

    public void n() {
        registerAction();
    }

    public void o() {
        this.f19050e = false;
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().g(this, 229);
    }
}
