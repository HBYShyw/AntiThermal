package com.oplus.battery;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.deepsleep.DeepSleepUtils;
import com.oplus.performance.GTModeBroadcastReceiver;
import com.oplus.settingslib.service.RecoveryService;
import d6.ConfigUpdateUtil;
import f6.ChargeUtil;
import f6.CommonUtil;
import f6.f;
import ia.ThermalCustomizeChargeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import r9.SimplePowerMonitorUtils;
import u4.IRemoteGuardElfInterface;
import x7.SmartChargeProtectionController;
import y5.AppFeature;
import y5.b;

/* loaded from: classes.dex */
public class RecoverySettingsService extends RecoveryService {

    /* renamed from: g, reason: collision with root package name */
    Context f9805g;

    /* renamed from: h, reason: collision with root package name */
    ServiceConnection f9806h = new a();

    /* loaded from: classes.dex */
    class a implements ServiceConnection {
        a() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (RecoverySettingsService.this.f9805g == null) {
                LocalLog.a("RecoverySettingsService", "RemoteGuardElf connected! mContext is null!");
                return;
            }
            LocalLog.a("RecoverySettingsService", "RemoteGuardElf connected!");
            IRemoteGuardElfInterface z10 = IRemoteGuardElfInterface.a.z(iBinder);
            f.Q1(RecoverySettingsService.this.f9805g, true);
            f.N1(new ArrayList(), new ArrayList(), RecoverySettingsService.this.f9805g.getApplicationContext());
            f.L1(RecoverySettingsService.this.f9805g, z10);
            if (b.E()) {
                GTModeBroadcastReceiver.c(RecoverySettingsService.this.f9805g, 4);
            }
            RecoverySettingsService recoverySettingsService = RecoverySettingsService.this;
            recoverySettingsService.f9805g.unbindService(recoverySettingsService.f9806h);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            LocalLog.a("RecoverySettingsService", "RemoteGuardElf disconnected!");
        }
    }

    @Override // com.oplus.settingslib.service.RecoveryService
    public boolean a(Context context) {
        LocalLog.a("RecoverySettingsService", "doRecoveryOperation");
        this.f9805g = context;
        ((PowerManager) context.getSystemService("power")).setPowerSaveModeEnabled(false);
        f.E3("auto_close_switch", "1", this.f9805g);
        f.z2(this.f9805g, 1);
        f.E3("open_level_switch", "0", this.f9805g);
        Settings.System.putIntForUser(this.f9805g.getContentResolver(), "power_save_open_level_switch", 0, 0);
        f.E3("power_save_open_level", "15", this.f9805g);
        Settings.System.putIntForUser(this.f9805g.getContentResolver(), "power_save_open_level", 15, 0);
        if (b.D()) {
            if (f.e0(context) == 1) {
                f.B2(context, 0);
            }
            DeepSleepUtils.getInstance(context).setExtremeSleepStatus(0);
            f.L2(context, 1);
        } else if (f.e0(context) == 0) {
            f.B2(context, 1);
        }
        if (f.j0(context) == 0) {
            f.F2(context, 1);
        }
        if (f.k0(context) == 0) {
            f.G2(context, 1);
        }
        if (f.G(context) == 0 && ConfigUpdateUtil.n(this.f9805g).x()) {
            f.h2(context, 1);
        }
        if (f.i0(context) == 0) {
            f.E2(context, 1);
        }
        if (AppFeature.c()) {
            Settings.System.putIntForUser(getContentResolver(), "lcd_cabc_mode", 1, 0);
        }
        f.f2(this.f9805g, true);
        f.b(this.f9806h, this.f9805g);
        if (b.n()) {
            if (b.B()) {
                CommonUtil.e0(this.f9805g);
            } else {
                CommonUtil.b(this.f9805g);
            }
        }
        if (b.x()) {
            LocalLog.a("RecoverySettingsService", "doRecoveryOperation Wireless charging data.");
            f.y3(this.f9805g, false);
            f.w3(false);
            f.w2(this.f9805g, true);
            f.Q2(this.f9805g, 0);
            f.x3(this.f9805g, 25);
            f.W1(this.f9805g, 22);
            f.X1(this.f9805g, 0);
            f.j2(this.f9805g, 7);
            f.k2(this.f9805g, 0);
        }
        SmartChargeProtectionController.J(this.f9805g).Z();
        f.d3(this.f9805g, 0);
        if (AppFeature.D()) {
            SmartChargeProtectionController.J(this.f9805g).W();
            f.J2(this.f9805g, 0);
        }
        LocalLog.a("RecoverySettingsService", "bindRemoteGuardElfService");
        if (b.f()) {
            SharedPreferences.Editor edit = this.f9805g.getSharedPreferences("BenchFlags", 0).edit();
            edit.putBoolean("HighPerDiaFlag", false);
            edit.apply();
        }
        if (AppFeature.F()) {
            f.r3(this.f9805g, false);
            f.p3(this.f9805g, false);
        }
        HashMap<String, Integer> B1 = f.B1(context);
        B1.clear();
        f.C3(B1, context);
        HashMap<String, Integer> x12 = f.x1(context);
        x12.clear();
        f.A3(x12, context);
        if (AppFeature.n()) {
            boolean l10 = AppFeature.l();
            f.e3(context, l10);
            if (b.D() && AppFeature.B() && AppFeature.j() && AppFeature.k() == 1) {
                ChargeUtil.B(!l10);
            }
            if (AppFeature.C()) {
                ThermalCustomizeChargeUtils.h(context).k();
                f.e2(context, false);
            }
            f.E3("smart_charge_switch", "0", context);
            ChargeUtil.t(3, false);
        }
        Iterator<Map.Entry<String, Integer>> it = SimplePowerMonitorUtils.e().entrySet().iterator();
        while (it.hasNext()) {
            f.o2(this.f9805g, it.next().getKey(), false);
        }
        return true;
    }
}
