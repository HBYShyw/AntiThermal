package k8;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUISwitchPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.basic.customized.PowerSaveLevelPicker;
import com.oplus.powermanager.powersave.PowerSaveTipsService;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import h8.IPowerSavePresenter;
import i8.BasePagePresenter;
import l8.IPowerSaveUpdate;
import t8.BatteryRemainTimeCalculator;
import t8.PowerUsageManager;
import x1.COUISecurityAlertDialogBuilder;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: PowerSavePresenter.java */
/* renamed from: k8.m, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerSavePresenter extends BasePagePresenter implements IPowerSavePresenter {

    /* renamed from: k, reason: collision with root package name */
    private final int f14185k;

    /* renamed from: l, reason: collision with root package name */
    private final int f14186l;

    /* renamed from: m, reason: collision with root package name */
    private final int f14187m;

    /* renamed from: n, reason: collision with root package name */
    private final String f14188n;

    /* renamed from: o, reason: collision with root package name */
    private final String f14189o;

    /* renamed from: p, reason: collision with root package name */
    private Context f14190p;

    /* renamed from: q, reason: collision with root package name */
    private Context f14191q;

    /* renamed from: r, reason: collision with root package name */
    private Handler f14192r;

    /* renamed from: s, reason: collision with root package name */
    private HandlerThread f14193s;

    /* renamed from: t, reason: collision with root package name */
    private IPowerSaveUpdate f14194t;

    /* renamed from: u, reason: collision with root package name */
    private PowerUsageManager f14195u;

    /* renamed from: v, reason: collision with root package name */
    private AlertDialog f14196v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f14197w;

    /* renamed from: x, reason: collision with root package name */
    private UploadDataUtil f14198x;

    /* renamed from: y, reason: collision with root package name */
    private ContentObserver f14199y;

    /* renamed from: z, reason: collision with root package name */
    private ContentObserver f14200z;

    /* compiled from: PowerSavePresenter.java */
    /* renamed from: k8.m$a */
    /* loaded from: classes2.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            int intForUser = Settings.System.getIntForUser(PowerSavePresenter.this.f14191q.getContentResolver(), "power_save_mode_switch", 0, 0);
            LocalLog.a(PowerSavePresenter.this.f12677j, "mLevelPickerObserver: state=" + intForUser);
            PowerSavePresenter.this.y(intForUser == 1);
        }
    }

    /* compiled from: PowerSavePresenter.java */
    /* renamed from: k8.m$b */
    /* loaded from: classes2.dex */
    class b extends ContentObserver {
        b(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            int i10 = Settings.Global.getInt(PowerSavePresenter.this.f14191q.getContentResolver(), "low_power", 0);
            if (i10 >= 0 && i10 <= 1) {
                boolean z11 = i10 == 1;
                if (PowerSavePresenter.this.f14194t != null) {
                    PowerSavePresenter.this.f14194t.f(Boolean.valueOf(z11));
                    return;
                }
                return;
            }
            LocalLog.b(PowerSavePresenter.this.f12677j, "mPowerSaveObserver:  invalid state=" + i10);
        }
    }

    /* compiled from: PowerSavePresenter.java */
    /* renamed from: k8.m$c */
    /* loaded from: classes2.dex */
    private class c extends Handler {
        public c(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 102) {
                return;
            }
            boolean booleanValue = ((Boolean) message.obj).booleanValue();
            if (PowerSavePresenter.this.f12673f.isPowerSaveMode() != booleanValue) {
                PowerSavePresenter.this.f12673f.setPowerSaveModeEnabled(booleanValue);
            }
            if (booleanValue && f6.f.l0(PowerSavePresenter.this.f14190p) != 2 && f6.f.t1(PowerSavePresenter.this.f14190p) && f6.f.G(PowerSavePresenter.this.f14190p) == 1) {
                PowerSavePresenter.this.f14190p.startService(new Intent(PowerSavePresenter.this.f14190p, (Class<?>) PowerSaveTipsService.class));
                f6.f.H2(PowerSavePresenter.this.f14190p, 2);
            }
        }
    }

    public PowerSavePresenter(Context context, IPowerSaveUpdate iPowerSaveUpdate) {
        super(PowerSavePresenter.class.getSimpleName());
        this.f14185k = 0;
        this.f14186l = 1;
        this.f14187m = 102;
        this.f14188n = CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT;
        this.f14189o = "PowerSaveHandler";
        this.f14190p = null;
        this.f14191q = null;
        this.f14192r = null;
        this.f14193s = null;
        this.f14194t = null;
        this.f14195u = null;
        this.f14196v = null;
        this.f14197w = false;
        this.f14199y = new a(new Handler());
        this.f14200z = new b(new Handler());
        this.f14190p = context;
        Context applicationContext = context.getApplicationContext();
        this.f14191q = applicationContext;
        this.f14194t = iPowerSaveUpdate;
        this.f14195u = PowerUsageManager.x(applicationContext);
        this.f14198x = UploadDataUtil.S0(this.f14191q);
    }

    private void n(boolean z10) {
        if (z10) {
            this.f14194t.L();
        } else {
            this.f14194t.S();
        }
    }

    private void o(boolean z10) {
        f6.f.E3("auto_close_switch", String.valueOf(z10 ? 1 : 0), this.f14191q);
        f6.f.z2(this.f14191q, z10 ? 1 : 0);
    }

    private void q(boolean z10) {
        f6.f.E3("open_level_switch", String.valueOf(z10 ? 1 : 0), this.f14191q);
        n(z10);
        x(z10);
    }

    private void r(boolean z10) {
        w(z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void u(int i10, boolean z10) {
        f6.f.g2(this.f14191q, z10);
        if (i10 != -1) {
            if (i10 == -2) {
                this.f14194t.d(true, false);
                this.f14198x.N0(false, this.f14195u.r());
                this.f14197w = false;
                return;
            }
            return;
        }
        AlertDialog alertDialog = this.f14196v;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (t()) {
            Toast.makeText(this.f14191q, R.string.cancle_lock_task_tip, 1).show();
            this.f14197w = false;
            return;
        }
        this.f14194t.d(true, true);
        f6.f.t3(this.f14191q, true);
        this.f14198x.N0(true, this.f14195u.r());
        this.f14197w = false;
        this.f14194t.F();
    }

    private void v() {
        if (f6.f.F(this.f14191q)) {
            f6.f.t3(this.f14191q, true);
            this.f14194t.F();
            return;
        }
        if (this.f14197w) {
            return;
        }
        this.f14197w = true;
        Context context = this.f14190p;
        COUISecurityAlertDialogBuilder H0 = new COUISecurityAlertDialogBuilder(context, 2131820883, f6.f.u0(context)).I0(R.string.super_powersave_dialog_button_open).F0(R.string.super_powersave_dialog_button_cancel).B0(R.string.super_powersave_dialog_never_remind).E0(true).D0(false).K0(false).H0(new COUISecurityAlertDialogBuilder.g() { // from class: k8.l
            @Override // x1.COUISecurityAlertDialogBuilder.g
            public final void a(int i10, boolean z10) {
                PowerSavePresenter.this.u(i10, z10);
            }
        });
        H0.h0(R.string.super_powersave_dialog_title);
        H0.Y(R.string.super_powersave_dialog_text);
        H0.d(true);
        AlertDialog w10 = H0.w();
        this.f14196v = w10;
        if (w10 != null) {
            w10.setCancelable(true);
            w10.setCanceledOnTouchOutside(false);
        }
        this.f14196v.show();
    }

    private void w(boolean z10) {
        if (this.f14192r.hasMessages(102)) {
            this.f14192r.removeMessages(102);
        }
        Message obtain = Message.obtain(this.f14192r, 102);
        obtain.obj = Boolean.valueOf(z10);
        this.f14192r.sendMessage(obtain);
    }

    private void x(boolean z10) {
        Settings.System.putIntForUser(this.f14191q.getContentResolver(), "power_save_open_level_switch", z10 ? 1 : 0, 0);
        if (z10) {
            int intForUser = Settings.System.getIntForUser(this.f14191q.getContentResolver(), "power_save_open_level", PowerSaveLevelPicker.g(), 0);
            int intProperty = this.f12675h.getIntProperty(4);
            if (intProperty <= intForUser) {
                y(true);
                LocalLog.a(this.f12677j, "Open PowerSaveMode currentLevel = " + intProperty);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y(boolean z10) {
        LocalLog.a(this.f12677j, "setPowerSaveMode enable = " + z10);
        IPowerSaveUpdate iPowerSaveUpdate = this.f14194t;
        if (iPowerSaveUpdate != null) {
            iPowerSaveUpdate.f(Boolean.valueOf(z10));
        }
        w(z10);
    }

    @Override // androidx.preference.Preference.d
    public boolean M(Preference preference) {
        if (!"default_power_optimization".equals(preference.getKey())) {
            return true;
        }
        this.f14194t.k();
        return true;
    }

    @Override // h8.IPowerSavePresenter
    public String b0(int i10) {
        long s7 = this.f14195u.s(this.f14195u.r(), i10);
        LocalLog.a(this.f12677j, "power1 " + i10);
        int i11 = AppFeature.G() ? R.string.battery_super_endurance_time : R.string.battery_ui_optimization_remain_time_new;
        Context context = this.f14191q;
        return context.getString(i11, BatteryRemainTimeCalculator.d(context, s7));
    }

    @Override // h8.IPowerSavePresenter
    public void onCreate(Bundle bundle) {
        HandlerThread handlerThread = new HandlerThread("PowerSaveHandler");
        this.f14193s = handlerThread;
        handlerThread.start();
        this.f14192r = new c(this.f14193s.getLooper());
        int intForUser = Settings.System.getIntForUser(this.f14191q.getContentResolver(), "power_save_open_level", f6.f.D1("power_save_open_level", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0);
        PowerSaveLevelPicker.l(intForUser);
        Settings.System.putIntForUser(this.f14191q.getContentResolver(), "power_save_open_level", intForUser, 0);
    }

    @Override // h8.IPowerSavePresenter
    public void onCreatePreferences(Bundle bundle, String str) {
        try {
            boolean isPowerSaveMode = this.f12673f.isPowerSaveMode();
            this.f14194t.f(Boolean.valueOf(isPowerSaveMode));
            int b02 = f6.f.b0(this.f14191q);
            boolean z10 = true;
            this.f14194t.s(Boolean.valueOf(b02 == 1));
            boolean z11 = Settings.System.getIntForUser(this.f14191q.getContentResolver(), "power_save_open_level_switch", f6.f.D1("open_level_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0) == 1;
            this.f14194t.q(Boolean.valueOf(z11));
            n(z11);
            String str2 = this.f12677j;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("initPowerSaveSwitch: isPowerSaveOn=");
            sb2.append(isPowerSaveMode);
            sb2.append(" isAutoClosed=");
            if (b02 != 1) {
                z10 = false;
            }
            sb2.append(z10);
            sb2.append(" isLevelSwitchOn=");
            sb2.append(z11);
            LocalLog.a(str2, sb2.toString());
            this.f14191q.getContentResolver().registerContentObserver(Settings.Global.getUriFor("low_power"), false, this.f14200z);
            this.f14191q.getContentResolver().registerContentObserver(Settings.System.getUriFor("power_save_mode_switch"), false, this.f14199y, 0);
        } catch (Exception unused) {
            LocalLog.b(this.f12677j, "onCreatePreferences NullPointerException");
        }
    }

    @Override // h8.IPowerSavePresenter
    public void onDestroy() {
        this.f14193s.quitSafely();
        this.f14193s = null;
        this.f14191q.getContentResolver().unregisterContentObserver(this.f14200z);
        this.f14191q.getContentResolver().unregisterContentObserver(this.f14199y);
        this.f14194t = null;
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (!(preference instanceof COUISwitchPreference)) {
            LocalLog.a(this.f12677j, "onPreferenceChange : preference is not expected");
            return false;
        }
        boolean booleanValue = ((Boolean) obj).booleanValue();
        String key = preference.getKey();
        LocalLog.a(this.f12677j, "onPreferenceChange : key = " + key + " checked = " + booleanValue);
        if ("power_save_switch".equals(key)) {
            r(booleanValue);
            return true;
        }
        if ("auto_close_switch".equals(key)) {
            o(booleanValue);
            return true;
        }
        if ("open_level_switch".equals(key)) {
            q(booleanValue);
            return true;
        }
        if (!key.equals("super_power_save_switch_pref")) {
            return true;
        }
        if (t()) {
            Toast.makeText(this.f14191q, R.string.cancle_lock_task_tip, 0).show();
            return false;
        }
        if (!booleanValue) {
            return true;
        }
        s();
        return true;
    }

    public void s() {
        if (f6.f.Z0(this.f14191q)) {
            f6.f.t3(this.f14191q, false);
        }
        if (f6.f.a1(this.f14191q)) {
            f6.f.u3(this.f14191q, false);
        } else {
            v();
        }
    }

    public boolean t() {
        IActivityManager service = ActivityManager.getService();
        if (service == null) {
            return false;
        }
        try {
            return service.isInLockTaskMode();
        } catch (RemoteException e10) {
            e10.printStackTrace();
            return false;
        }
    }
}
