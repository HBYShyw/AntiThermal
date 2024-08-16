package k8;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.battery.R;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import e8.IBatteryPageModel;
import f6.CommonUtil;
import g8.BatteryPageModel;
import h8.IPage;
import i8.BasePagePresenter;
import j8.EventObserver;
import java.util.ArrayList;
import java.util.HashMap;
import l8.IBatteryPageUpdate;
import t8.BatteryRemainTimeCalculator;
import t8.PowerUsageManager;
import v4.BatteryStatsManager;
import x1.COUISecurityAlertDialogBuilder;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: BatteryPagePresenter.java */
/* renamed from: k8.d, reason: use source file name */
/* loaded from: classes2.dex */
public class BatteryPagePresenter extends BasePagePresenter implements IPage {
    private IBatteryPageUpdate F;
    private IBatteryPageModel G;
    private EventObserver H;
    private NotifyUtil I;
    private UploadDataUtil J;
    private Context K;
    private Context L;
    private boolean M;

    /* renamed from: n, reason: collision with root package name */
    private PowerUsageManager.e f14087n;

    /* renamed from: o, reason: collision with root package name */
    private PowerUsageManager.c f14088o;

    /* renamed from: k, reason: collision with root package name */
    private Handler f14084k = null;

    /* renamed from: l, reason: collision with root package name */
    private PowerUsageManager f14085l = null;

    /* renamed from: m, reason: collision with root package name */
    private boolean f14086m = false;

    /* renamed from: p, reason: collision with root package name */
    private int f14089p = 0;

    /* renamed from: q, reason: collision with root package name */
    private int f14090q = -1;

    /* renamed from: r, reason: collision with root package name */
    private int f14091r = 1;

    /* renamed from: s, reason: collision with root package name */
    private int f14092s = 0;

    /* renamed from: t, reason: collision with root package name */
    private long f14093t = 0;

    /* renamed from: u, reason: collision with root package name */
    private boolean f14094u = false;

    /* renamed from: v, reason: collision with root package name */
    private boolean f14095v = false;

    /* renamed from: w, reason: collision with root package name */
    private boolean f14096w = false;

    /* renamed from: x, reason: collision with root package name */
    private boolean f14097x = true;

    /* renamed from: y, reason: collision with root package name */
    private boolean f14098y = true;

    /* renamed from: z, reason: collision with root package name */
    private boolean f14099z = false;
    private Object A = new Object();
    private AlertDialog B = null;
    private boolean C = false;
    private AlertDialog D = null;
    private boolean E = false;
    ServiceConnection N = new c();

    /* compiled from: BatteryPagePresenter.java */
    /* renamed from: k8.d$a */
    /* loaded from: classes2.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ boolean f14100e;

        a(boolean z10) {
            this.f14100e = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            BatteryPagePresenter.this.F.a0(this.f14100e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BatteryPagePresenter.java */
    /* renamed from: k8.d$b */
    /* loaded from: classes2.dex */
    public class b implements PowerUsageManager.c {
        b() {
        }

        @Override // t8.PowerUsageManager.c
        public void a(Intent intent) {
            int intExtra = intent.getIntExtra("plugged", 0);
            int intExtra2 = intent.getIntExtra("level", 0);
            int intExtra3 = intent.getIntExtra("status", 1);
            BatteryStatsManager.i().l(BatteryPagePresenter.this.K, intExtra3, intExtra, intExtra2, intent.getIntExtra("temperature", 0));
            if (LocalLog.f()) {
                LocalLog.a("BatteryPagePresenter", "battery change");
            }
            if (intExtra != BatteryPagePresenter.this.f14090q || intExtra2 != BatteryPagePresenter.this.f14089p || intExtra3 != BatteryPagePresenter.this.f14091r) {
                BatteryPagePresenter.this.f14089p = intExtra2;
                BatteryPagePresenter.this.f14091r = intExtra3;
                BatteryPagePresenter.this.O(intExtra, intExtra3);
                BatteryPagePresenter.this.F.x();
            }
            if (intExtra != BatteryPagePresenter.this.f14090q) {
                if (BatteryPagePresenter.this.f14090q != -1) {
                    BatteryPagePresenter.this.F.j(intExtra == 0);
                    if (intExtra == 0) {
                        f6.f.J1(BatteryPagePresenter.this.L);
                    }
                }
                BatteryPagePresenter.this.f14090q = intExtra;
            }
        }
    }

    /* compiled from: BatteryPagePresenter.java */
    /* renamed from: k8.d$c */
    /* loaded from: classes2.dex */
    class c implements ServiceConnection {
        c() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalLog.a("BatteryPagePresenter", "RemoteGuardElf connected!");
            synchronized (BatteryPagePresenter.this.A) {
                BatteryPagePresenter.this.A.notifyAll();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            LocalLog.a("BatteryPagePresenter", "RemoteGuardElf disconnected!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: BatteryPagePresenter.java */
    /* renamed from: k8.d$d */
    /* loaded from: classes2.dex */
    public class d extends Handler {
        public d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i10 = message.what;
            if (i10 == 100) {
                ((PowerManager) BatteryPagePresenter.this.K.getSystemService("power")).setPowerSaveModeEnabled(((Boolean) message.obj).booleanValue());
            } else {
                if (i10 != 101) {
                    return;
                }
                boolean booleanValue = ((Boolean) message.obj).booleanValue();
                HashMap hashMap = new HashMap();
                hashMap.put("switch", String.valueOf(booleanValue));
                BatteryPagePresenter.this.J.J(hashMap);
            }
        }
    }

    public BatteryPagePresenter(Context context, IBatteryPageUpdate iBatteryPageUpdate) {
        this.G = new BatteryPageModel(context);
        this.K = context.getApplicationContext();
        this.L = context;
        this.F = iBatteryPageUpdate;
    }

    private boolean C(Intent intent, int i10) {
        int intExtra = intent.getIntExtra("chargewattage", -1);
        int intExtra2 = intent.getIntExtra("pps_chg_mode", -1);
        if (LocalLog.f()) {
            LocalLog.a("BatteryPagePresenter", "chargeWattage =" + intExtra + ", passState =" + intExtra2 + "chargeType =" + i10);
        }
        Settings.System.putIntForUser(this.L.getContentResolver(), "oplus_battery_settings_charge_wattage", intExtra, 0);
        if (intExtra2 == 1 && intExtra >= 100) {
            Settings.System.putIntForUser(this.L.getContentResolver(), "oplus_battery_settings_plugged_type", 3, 0);
            if (i10 == 0) {
                this.f14092s = 20;
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void H() {
        IBatteryPageUpdate iBatteryPageUpdate = this.F;
        if (iBatteryPageUpdate != null) {
            iBatteryPageUpdate.x();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void I(int i10, boolean z10) {
        f6.f.p3(this.K, z10);
        if (i10 == -1) {
            this.F.I(true);
            f6.f.q3(this.K, true);
            this.E = false;
            this.J.K0(true);
            return;
        }
        if (i10 == -2) {
            this.F.I(false);
            this.E = false;
            this.J.K0(false);
        }
    }

    private void N() {
        this.H.m(this, 200);
        this.H.m(this, EventType.SCENE_MODE_AUDIO_OUT);
        this.H.m(this, EventType.SCENE_MODE_VIDEO);
        this.H.m(this, 101);
        if (this.f14096w) {
            this.H.m(this, EventType.SCENE_MODE_AUDIO_IN);
            this.H.m(this, EventType.SCENE_MODE_CAMERA);
        }
        if (this.f14097x) {
            this.H.m(this, EventType.SCENE_MODE_LOCATION);
        }
        if (this.f14099z) {
            this.H.m(this, EventType.SCENE_MODE_DOWNLOAD);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void O(int i10, int i11) {
        int i12;
        int i13 = 4;
        if (i10 == 0) {
            i13 = -1;
        } else if (i10 == 4) {
            int i14 = this.f14092s;
            if (i14 == 3 || i14 == 4 || i14 < 2) {
                i13 = 0;
            }
        } else {
            int i15 = this.f14092s;
            if (i15 == 1) {
                i12 = 1;
            } else if (i15 == 2) {
                i12 = 2;
            } else if (i15 == 20) {
                i12 = 3;
            } else if (i15 == 25 || i15 == 30) {
                i12 = (y5.b.D() && this.f14092s == 25) ? 33 : 5;
            } else {
                i12 = 0;
            }
            i13 = (this.M || i11 != 5) ? i12 : -2;
        }
        LocalLog.a("BatteryPagePresenter", "charge type " + i13);
        Settings.System.putIntForUser(this.L.getContentResolver(), "oplus_battery_settings_plugged_type", i13, 0);
    }

    private void R() {
        if (this.E) {
            return;
        }
        this.E = true;
        Context context = this.L;
        COUISecurityAlertDialogBuilder cOUISecurityAlertDialogBuilder = new COUISecurityAlertDialogBuilder(context, R.style.Theme_Dialog_Alert, f6.f.u0(context));
        cOUISecurityAlertDialogBuilder.I0(R.string.battery_speed_charge_dialog_open).F0(R.string.battery_speed_charge_dialog_cancel).B0(R.string.battery_speed_charge_dialog_no_remind).E0(true).D0(false).K0(false).H0(new COUISecurityAlertDialogBuilder.g() { // from class: k8.c
            @Override // x1.COUISecurityAlertDialogBuilder.g
            public final void a(int i10, boolean z10) {
                BatteryPagePresenter.this.I(i10, z10);
            }
        }).h0(R.string.battery_speed_charge_dialog_title).Y(R.string.battery_speed_charge_dialog_content);
        AlertDialog w10 = cOUISecurityAlertDialogBuilder.w();
        this.D = w10;
        if (w10 != null) {
            w10.setCancelable(true);
            w10.setCanceledOnTouchOutside(false);
        }
        this.D.show();
    }

    private void S() {
        this.K.unbindService(this.N);
    }

    private void T() {
        PowerUsageManager powerUsageManager = this.f14085l;
        if (powerUsageManager != null) {
            PowerUsageManager.e eVar = this.f14087n;
            if (eVar != null) {
                powerUsageManager.D(eVar);
            }
            PowerUsageManager.c cVar = this.f14088o;
            if (cVar != null) {
                this.f14085l.C(cVar);
                BatteryStatsManager.i().o();
            }
        }
    }

    private void U() {
        String b02;
        String b03;
        if (f6.f.f0(this.K)) {
            b02 = A(1);
            b03 = b0(3);
        } else {
            b02 = b0(1);
            b03 = b0(3);
        }
        this.F.w(b02, b03);
    }

    private void W() {
        this.f14093t = SystemClock.elapsedRealtime();
        U();
        int[] y4 = this.f14085l.y(null, new ArrayList<>());
        int i10 = y4[0];
        LocalLog.a("BatteryPagePresenter", "refresh size = " + y4[1]);
        if (!f6.f.I()) {
            this.F.c(0, i10);
            f6.f.i2(true);
        }
        this.F.x();
    }

    private void x() {
        this.H.c(this, 200);
        this.H.c(this, EventType.SCENE_MODE_AUDIO_OUT);
        this.H.c(this, EventType.SCENE_MODE_VIDEO);
        this.H.c(this, 101);
        if (this.f14096w) {
            this.H.c(this, EventType.SCENE_MODE_AUDIO_IN);
            this.H.c(this, EventType.SCENE_MODE_CAMERA);
        }
        if (this.f14097x) {
            this.H.c(this, EventType.SCENE_MODE_LOCATION);
        }
        if (this.f14099z) {
            this.H.c(this, EventType.SCENE_MODE_DOWNLOAD);
        }
    }

    private void y() {
        f6.f.b(this.N, this.K);
    }

    private void z() {
        if (this.f14086m) {
            return;
        }
        this.f14085l = PowerUsageManager.x(this.K);
        this.f14086m = true;
        b bVar = new b();
        this.f14088o = bVar;
        this.f14085l.p(bVar, true);
    }

    public String A(int i10) {
        return this.K.getString(R.string.battery_ui_optimization_remain_time_new) + " " + BatteryRemainTimeCalculator.d(this.K, this.f14085l.s(this.f14085l.r(), i10));
    }

    public int B() {
        return this.f14085l.r();
    }

    public void D(boolean z10) {
        if (this.f14084k.hasMessages(101)) {
            this.f14084k.removeMessages(101);
        }
        Message obtain = Message.obtain(this.f14084k, 101);
        obtain.obj = Boolean.valueOf(z10);
        this.f14084k.sendMessage(obtain);
    }

    public void F(boolean z10) {
        if (z10) {
            boolean U0 = f6.f.U0(this.L);
            LocalLog.a("BatteryPagePresenter", "handleSpeedChargePrefSwitchChange remindState =" + U0);
            if (U0) {
                f6.f.q3(this.K, true);
                this.J.K0(true);
                return;
            } else {
                R();
                return;
            }
        }
        f6.f.q3(this.K, false);
        this.J.K0(false);
    }

    public void G() {
        HandlerThread handlerThread = new HandlerThread("BatteryPagePresenter");
        handlerThread.start();
        this.f14084k = new d(handlerThread.getLooper());
        this.f14085l = PowerUsageManager.x(this.K);
        this.H = EventObserver.d(this.K);
        this.I = NotifyUtil.v(this.K);
        this.J = UploadDataUtil.S0(this.K);
        this.f14094u = f6.f.f0(this.K);
        this.f14095v = f6.f.i1();
        this.f14096w = y5.b.x();
        this.f14097x = f6.f.s1();
        this.f14099z = AppFeature.p() && this.L.getUserId() == 0;
    }

    public void J() {
        G();
    }

    public void K() {
        z();
        W();
    }

    public void L() {
        x();
    }

    public void P(boolean z10) {
        this.f14092s = -1;
        Settings.System.putIntForUser(this.L.getContentResolver(), "oplus_battery_settings_plugged_type", this.f14092s, 0);
        this.L.getMainThreadHandler().post(new Runnable() { // from class: k8.b
            @Override // java.lang.Runnable
            public final void run() {
                BatteryPagePresenter.this.H();
            }
        });
        this.M = z10;
    }

    public void a() {
    }

    public void b() {
        y();
    }

    public String b0(int i10) {
        long s7 = this.f14085l.s(this.f14085l.r(), i10);
        LocalLog.a("BatteryPagePresenter", "power1 " + i10);
        Context context = this.K;
        return context.getString(R.string.battery_ui_optimization_remain_time_new, BatteryRemainTimeCalculator.d(context, s7));
    }

    @Override // h8.IPage
    public void c(int i10, Bundle bundle) {
        IBatteryPageUpdate iBatteryPageUpdate;
        boolean z10;
        switch (i10) {
            case 200:
                boolean z11 = bundle.getBoolean("boolean_powersave_state");
                this.f14094u = z11;
                if (z11 && (iBatteryPageUpdate = this.F) != null && this.f14095v) {
                    iBatteryPageUpdate.e(true, true, false);
                    CommonUtil.b(this.K);
                }
                IBatteryPageUpdate iBatteryPageUpdate2 = this.F;
                if (iBatteryPageUpdate2 != null) {
                    iBatteryPageUpdate2.g(true, true, this.f14094u);
                    this.F.x();
                    return;
                }
                return;
            case EventType.SCENE_MODE_LOCATION /* 201 */:
                this.F.d(this.f14097x, bundle.getBoolean("boolean_s_powersave_state"));
                return;
            case EventType.SCENE_MODE_AUDIO_OUT /* 202 */:
                boolean z12 = bundle.getBoolean("boolean_highpref_state");
                IBatteryPageUpdate iBatteryPageUpdate3 = this.F;
                if (iBatteryPageUpdate3 != null) {
                    iBatteryPageUpdate3.x();
                }
                if (z12) {
                    IBatteryPageUpdate iBatteryPageUpdate4 = this.F;
                    if (iBatteryPageUpdate4 == null || !this.f14095v) {
                        return;
                    }
                    iBatteryPageUpdate4.e(true, true, true);
                    return;
                }
                IBatteryPageUpdate iBatteryPageUpdate5 = this.F;
                if (iBatteryPageUpdate5 == null || !this.f14095v) {
                    return;
                }
                iBatteryPageUpdate5.e(true, true, false);
                return;
            case EventType.SCENE_MODE_AUDIO_IN /* 203 */:
                if (this.f14096w) {
                    this.f14084k.postDelayed(new a(bundle.getBoolean("boolean_wireless_reverse_state")), 500L);
                    return;
                }
                return;
            case EventType.SCENE_MODE_CAMERA /* 204 */:
                if (this.f14096w) {
                    bundle.getInt("int_reverse_threshold_val");
                    B();
                    return;
                }
                return;
            case EventType.SCENE_MODE_VIDEO /* 205 */:
            default:
                return;
            case EventType.SCENE_MODE_DOWNLOAD /* 206 */:
                boolean z13 = bundle.getBoolean("boolean_smartcharge_state");
                this.f14098y = z13;
                IBatteryPageUpdate iBatteryPageUpdate6 = this.F;
                if (iBatteryPageUpdate6 == null || !(z10 = this.f14099z)) {
                    return;
                }
                iBatteryPageUpdate6.z(true, z10, z13);
                return;
        }
    }

    @Override // h8.IPage
    public void e(Intent intent) {
        String action = intent.getAction();
        if (LocalLog.f()) {
            LocalLog.a("BatteryPagePresenter", "action = " + action);
        }
        if ("android.intent.action.ADDITIONAL_BATTERY_CHANGED".equals(action)) {
            if (this.M && !intent.getBooleanExtra("wattageTest", false)) {
                LocalLog.b("BatteryPagePresenter", "in test mode, not respond to ADDITIONAL_BATTERY_CHANGED");
                return;
            }
            int intExtra = intent.getIntExtra("chargertechnology", 0);
            if (!C(intent, intExtra) && this.f14092s != intExtra) {
                this.f14092s = intExtra;
                O(this.f14090q, this.f14091r);
            }
        }
        this.F.x();
    }

    public void onDetach() {
        T();
        N();
        S();
        this.F = null;
    }
}
