package b7;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.powermanager.fuelgaue.basic.customized.PowerSaveLevelPicker;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import f6.CommonUtil;
import f6.f;
import java.util.HashMap;
import o8.BatteryStatusCenter;
import q6.BatteryCardUtil;
import r9.SimplePowerMonitorUtils;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;
import x5.UploadDataUtil;
import x6.BMDiaUtil;

/* compiled from: PowerModeUnion.java */
/* renamed from: b7.a, reason: use source file name */
/* loaded from: classes.dex */
public class PowerModeUnion implements IAffairCallback {

    /* renamed from: o, reason: collision with root package name */
    private static final String f4576o = "a";

    /* renamed from: p, reason: collision with root package name */
    private static Context f4577p;

    /* renamed from: q, reason: collision with root package name */
    private static Handler f4578q = new a(Looper.myLooper());

    /* renamed from: e, reason: collision with root package name */
    private PowerManager f4579e;

    /* renamed from: f, reason: collision with root package name */
    private AlarmManager f4580f;

    /* renamed from: g, reason: collision with root package name */
    private UploadDataUtil f4581g;

    /* renamed from: h, reason: collision with root package name */
    private NotifyUtil f4582h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f4583i = false;

    /* renamed from: j, reason: collision with root package name */
    private boolean f4584j = false;

    /* renamed from: k, reason: collision with root package name */
    private boolean f4585k = false;

    /* renamed from: l, reason: collision with root package name */
    private boolean f4586l = false;

    /* renamed from: m, reason: collision with root package name */
    private boolean f4587m = false;

    /* renamed from: n, reason: collision with root package name */
    private int f4588n = 0;

    /* compiled from: PowerModeUnion.java */
    /* renamed from: b7.a$a */
    /* loaded from: classes.dex */
    class a extends Handler {
        a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            NotifyUtil v7 = NotifyUtil.v(PowerModeUnion.f4577p);
            int i10 = message.what;
            if (i10 == 1) {
                int intForUser = Settings.System.getIntForUser(PowerModeUnion.f4577p.getContentResolver(), "high_performance_mode_on", 0, 0);
                v7.j();
                if (intForUser == 1) {
                    v7.C();
                    return;
                }
                return;
            }
            if (i10 == 2) {
                if (Settings.System.getIntForUser(PowerModeUnion.f4577p.getContentResolver(), "high_performance_mode_on", 0, 0) == 1) {
                    CommonUtil.b(PowerModeUnion.f4577p);
                    CommonUtil.h0(PowerModeUnion.f4577p, 1);
                    return;
                }
                return;
            }
            if (i10 != 3) {
                return;
            }
            int B = CommonUtil.B(PowerModeUnion.f4577p);
            if (LocalLog.f()) {
                LocalLog.a(PowerModeUnion.f4576o, "CHECK_HIGH_PERFORMANCE_LAST_SHUTDOWN: " + B);
            }
            if (B == 1) {
                PowerModeUnion.i();
            }
        }
    }

    /* compiled from: PowerModeUnion.java */
    /* renamed from: b7.a$b */
    /* loaded from: classes.dex */
    private static class b {

        /* renamed from: a, reason: collision with root package name */
        private static final PowerModeUnion f4589a = new PowerModeUnion();
    }

    private void d(boolean z10) {
        if (z10) {
            this.f4582h.j();
            if (CommonUtil.C(f4577p)) {
                CommonUtil.b(f4577p);
            }
        }
    }

    public static PowerModeUnion e() {
        f4577p = GuardElfContext.e().c();
        return b.f4589a;
    }

    private void f() {
        BMDiaUtil.f(f4577p).c();
        if (y5.b.n()) {
            int intForUser = Settings.System.getIntForUser(f4577p.getContentResolver(), "high_performance_mode_on", 0, 0);
            this.f4582h.j();
            if (intForUser == 1) {
                this.f4582h.C();
            }
        }
    }

    private void h(int i10) {
        int intForUser;
        if (this.f4588n == i10) {
            return;
        }
        if ("true".equals(f.z(f4577p))) {
            LocalLog.l(f4576o, "in competition mode, lowPowerModeHandle invalid!");
            return;
        }
        if ((Settings.System.getIntForUser(f4577p.getContentResolver(), "power_save_open_level_switch", f.D1("open_level_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0) == 1) && i10 == (intForUser = Settings.System.getIntForUser(f4577p.getContentResolver(), "power_save_open_level", PowerSaveLevelPicker.g(), 0)) && this.f4588n > intForUser && !this.f4579e.isPowerSaveMode()) {
            this.f4579e.setPowerSaveModeEnabled(true);
            LocalLog.a(f4576o, "Battery level = " + intForUser + " open PowerSaveMode!");
        }
        int intForUser2 = Settings.System.getIntForUser(f4577p.getContentResolver(), "power_save_auto_close_switch", f.b0(f4577p), 0);
        if (this.f4587m && intForUser2 == 1 && i10 >= 90 && this.f4588n < 90) {
            this.f4579e.setPowerSaveModeEnabled(false);
            this.f4582h.H(i10);
            LocalLog.a(f4576o, "Battery level = " + i10 + " auto close PowerSaveMode");
        }
        this.f4588n = i10;
        this.f4587m = this.f4579e.isPowerSaveMode();
        LocalLog.a(f4576o, "lowPowerModeHandle: level=" + i10 + ", levelLast=" + this.f4588n + ", LowPowerModeLast=" + this.f4587m);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void i() {
        Context c10 = GuardElfContext.e().c();
        ((PowerManager) c10.getSystemService("power")).setPowerSaveModeEnabled(false);
        if (!y5.b.D()) {
            CommonUtil.b0(c10);
        } else {
            CommonUtil.j0(c10, 2);
        }
    }

    private void j() {
        if (y5.b.n()) {
            long uptimeMillis = SystemClock.uptimeMillis();
            String str = f4576o;
            LocalLog.a(str, "uptimeMillis " + uptimeMillis);
            if (uptimeMillis > 60000) {
                Message obtainMessage = f4578q.obtainMessage();
                obtainMessage.what = 1;
                f4578q.sendMessageDelayed(obtainMessage, 2000L);
                LocalLog.a(str, "send high per msg");
            } else if (uptimeMillis <= 60000) {
                Message obtainMessage2 = f4578q.obtainMessage();
                obtainMessage2.what = 2;
                f4578q.sendMessageDelayed(obtainMessage2, 2000L);
                LocalLog.a(str, "send high per check");
            }
            Message obtainMessage3 = f4578q.obtainMessage();
            obtainMessage3.what = 3;
            f4578q.sendMessageDelayed(obtainMessage3, 3000L);
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 == 204) {
            int intExtra = intent.getIntExtra("level", 0);
            h(intExtra);
            f.U1(f4577p, intExtra);
            return;
        }
        if (i10 == 218) {
            f();
            LocalLog.a(f4576o, "AFFAIR_USER_SWITCHED_BROADCAST: " + intent.getIntExtra("android.intent.extra.user_handle", 0));
            SimplePowerMonitorUtils.H(intent.getIntExtra("android.intent.extra.user_handle", 0));
            return;
        }
        if (i10 == 1001) {
            HashMap hashMap = new HashMap();
            hashMap.put("notify", "close");
            this.f4581g.I(hashMap);
            CommonUtil.b(f4577p);
            return;
        }
        if (i10 != 1404) {
            if (i10 == 225) {
                this.f4582h.X(false);
                return;
            } else {
                if (i10 != 226) {
                    return;
                }
                this.f4582h.X(true);
                return;
            }
        }
        boolean booleanExtra = intent.getBooleanExtra("enduranceScene_enter", false);
        boolean C = CommonUtil.C(f4577p);
        if (C) {
            CommonUtil.i0(GuardElfContext.e().c(), !booleanExtra);
        }
        LocalLog.l(f4576o, "enduranceScene:" + booleanExtra + " highPerf:" + C);
    }

    public void g() {
        this.f4579e = GuardElfContext.e().h();
        this.f4580f = GuardElfContext.e().b();
        this.f4581g = UploadDataUtil.S0(f4577p);
        this.f4582h = NotifyUtil.v(f4577p);
        this.f4583i = CommonUtil.C(f4577p);
        this.f4584j = f.a1(f4577p);
        registerAction();
        this.f4588n = f.r(f4577p);
        this.f4587m = this.f4579e.isPowerSaveMode();
        j();
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 901);
        Affair.f().g(this, 902);
        Affair.f().g(this, 903);
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().g(this, DataTypeConstants.USER_ACTION);
        Affair.f().g(this, EventType.SCENE_MODE_NAVIGATION);
        Affair.f().g(this, 225);
        Affair.f().g(this, 226);
        Affair.f().g(this, 1404);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
        switch (i10) {
            case 901:
                boolean z10 = bundle.getBoolean("powersave_state");
                d(z10);
                PowerSaveMode.d(f4577p).h(z10);
                this.f4587m = z10;
                BatteryStatusCenter.a(f4577p).g(z10);
                BatteryCardUtil.a(f4577p).b(this.f4588n, -1, -1);
                LocalLog.a(f4576o, "powersave_state:" + z10);
                return;
            case 902:
                this.f4583i = bundle.getBoolean("highpref_state");
                return;
            case 903:
                this.f4584j = bundle.getBoolean("s_powersave_state");
                BatteryStatusCenter.a(f4577p).h(this.f4584j);
                return;
            default:
                return;
        }
    }
}
