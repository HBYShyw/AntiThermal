package z8;

import a9.SuperEnduranceController;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.UserHandle;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.osense.OsenseResClient;
import com.oplus.osense.info.OsenseNotifyRequest;
import f6.f;
import t8.BatteryRemainTimeCalculator;
import w4.Affair;
import w4.IAffairCallback;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: SuperPowersaveController.java */
/* renamed from: z8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class SuperPowersaveController implements IAffairCallback {
    private static volatile SuperPowersaveController E;
    private UploadDataUtil A;

    /* renamed from: u, reason: collision with root package name */
    private boolean f20281u;

    /* renamed from: v, reason: collision with root package name */
    private PowerManager f20282v;

    /* renamed from: w, reason: collision with root package name */
    private Context f20283w;

    /* renamed from: x, reason: collision with root package name */
    private d f20284x;

    /* renamed from: y, reason: collision with root package name */
    private PowerManager.WakeLock f20285y;

    /* renamed from: z, reason: collision with root package name */
    private SuperPowersaveUtils f20286z;

    /* renamed from: e, reason: collision with root package name */
    private SuperpowersaveRUSHelper f20265e = null;

    /* renamed from: f, reason: collision with root package name */
    private boolean f20266f = true;

    /* renamed from: g, reason: collision with root package name */
    private boolean f20267g = true;

    /* renamed from: h, reason: collision with root package name */
    private boolean f20268h = true;

    /* renamed from: i, reason: collision with root package name */
    private boolean f20269i = true;

    /* renamed from: j, reason: collision with root package name */
    private boolean f20270j = true;

    /* renamed from: k, reason: collision with root package name */
    private boolean f20271k = true;

    /* renamed from: l, reason: collision with root package name */
    private boolean f20272l = true;

    /* renamed from: m, reason: collision with root package name */
    private boolean f20273m = true;

    /* renamed from: n, reason: collision with root package name */
    private boolean f20274n = true;

    /* renamed from: o, reason: collision with root package name */
    private boolean f20275o = true;

    /* renamed from: p, reason: collision with root package name */
    private boolean f20276p = true;

    /* renamed from: q, reason: collision with root package name */
    private boolean f20277q = true;

    /* renamed from: r, reason: collision with root package name */
    private boolean f20278r = true;

    /* renamed from: s, reason: collision with root package name */
    private boolean f20279s = true;

    /* renamed from: t, reason: collision with root package name */
    private boolean f20280t = true;
    private ContentObserver B = new a(new Handler());
    private ContentObserver C = new b(new Handler());
    private ContentObserver D = new c(new Handler());

    /* compiled from: SuperPowersaveController.java */
    /* renamed from: z8.a$a */
    /* loaded from: classes2.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (SuperPowersaveController.this.f20286z.z()) {
                SuperPowersaveUtils unused = SuperPowersaveController.this.f20286z;
                SuperPowersaveUtils.q0(SuperPowersaveController.this.f20283w, true);
                LocalLog.a("SuperPowersaveController", "bluetooth status changed by user");
            }
        }
    }

    /* compiled from: SuperPowersaveController.java */
    /* renamed from: z8.a$b */
    /* loaded from: classes2.dex */
    class b extends ContentObserver {
        b(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (SuperPowersaveController.this.f20286z.E() == 3) {
                SuperPowersaveUtils unused = SuperPowersaveController.this.f20286z;
                SuperPowersaveUtils.v0(SuperPowersaveController.this.f20283w, true);
                LocalLog.a("SuperPowersaveController", "GpsStateObserver: status changed by user");
            }
        }
    }

    /* compiled from: SuperPowersaveController.java */
    /* renamed from: z8.a$c */
    /* loaded from: classes2.dex */
    class c extends ContentObserver {
        c(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (AppFeature.D()) {
                SuperPowersaveController.this.f20284x.sendEmptyMessageDelayed(3, 333L);
            } else {
                SuperPowersaveController.this.f20284x.sendEmptyMessage(3);
            }
        }
    }

    /* compiled from: SuperPowersaveController.java */
    /* renamed from: z8.a$d */
    /* loaded from: classes2.dex */
    private class d extends Handler {
        public d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i10 = message.what;
            if (i10 == 0) {
                if (UserHandle.myUserId() != 0) {
                    return;
                }
                boolean a12 = f.a1(SuperPowersaveController.this.f20283w);
                LocalLog.a("SuperPowersaveController", "mSuperPowerSaveObserver: mIsSuperPowersaveOn=" + a12);
                if (!a12) {
                    SuperPowersaveController.this.U();
                    SuperPowersaveController.this.W();
                    SuperPowersaveController.this.V();
                }
                SuperPowersaveController.this.e0(a12);
                return;
            }
            if (i10 == 1) {
                SuperPowersaveController superPowersaveController = SuperPowersaveController.this;
                superPowersaveController.f20265e = SuperpowersaveRUSHelper.c(superPowersaveController.f20283w);
                SuperPowersaveController.this.a0();
                if (!f.Z0(SuperPowersaveController.this.f20283w) || f.a1(SuperPowersaveController.this.f20283w)) {
                    return;
                }
                LocalLog.l("SuperPowersaveController", "super power state not synchronize.");
                SuperPowersaveController.this.f20284x.sendEmptyMessage(4);
                return;
            }
            if (i10 == 2) {
                SuperPowersaveController.this.f20265e.f(SuperPowersaveController.this.f20283w);
                SuperPowersaveController.this.a0();
                return;
            }
            if (i10 != 3) {
                if (i10 != 4) {
                    return;
                }
                f.u3(SuperPowersaveController.this.f20283w, true);
                SuperPowersaveController.this.E();
                SuperPowersaveController.this.D();
                return;
            }
            if (f.Z0(SuperPowersaveController.this.f20283w)) {
                if (AppFeature.D()) {
                    SuperPowersaveController.this.f20284x.sendEmptyMessageDelayed(4, 200L);
                } else {
                    SuperPowersaveController.this.f20284x.sendEmptyMessageDelayed(4, 333L);
                }
            }
        }
    }

    private SuperPowersaveController(Context context) {
        this.f20281u = true;
        this.f20282v = null;
        this.f20283w = context;
        HandlerThread handlerThread = new HandlerThread("super_powersave_thread");
        handlerThread.start();
        d dVar = new d(handlerThread.getLooper());
        this.f20284x = dVar;
        dVar.sendEmptyMessage(1);
        PowerManager powerManager = (PowerManager) this.f20283w.getSystemService("power");
        this.f20282v = powerManager;
        this.f20285y = powerManager.newWakeLock(1, "SuperPowersave:controller");
        this.f20286z = SuperPowersaveUtils.H(this.f20283w);
        this.A = UploadDataUtil.S0(this.f20283w);
        SuperPowersaveUtils superPowersaveUtils = this.f20286z;
        if (superPowersaveUtils != null) {
            this.f20281u = superPowersaveUtils.m();
        }
        if (AppFeature.G()) {
            SuperEnduranceController.h(this.f20283w).k(this.f20284x);
        }
    }

    private void A() {
        boolean N = this.f20286z.N();
        this.f20286z.j0(N);
        LocalLog.a("SuperPowersaveController", "doActionForSwipeSideGestureFunc: isFuncOn=" + N);
        if (N) {
            this.f20286z.B0(false);
        }
    }

    private void B() {
        boolean O = this.f20286z.O();
        LocalLog.a("SuperPowersaveController", "doActionForWifiApFunc,isFuncOn:" + O);
        if (O) {
            this.f20286z.a();
        }
    }

    private void C() {
        boolean P = this.f20286z.P();
        this.f20286z.k0(P);
        LocalLog.a("SuperPowersaveController", "doActionForWifiScanAlwaysFunc,isFuncOn:" + P);
        if (P) {
            this.f20286z.C0(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void D() {
        if (this.f20268h) {
            C();
        }
        if (this.f20269i) {
            B();
        }
        if (this.f20276p) {
            o();
        }
        if (this.f20277q) {
            n();
        }
        if (this.f20279s) {
            q();
        }
        A();
        if (this.f20270j) {
            r();
        }
        if (this.f20271k) {
            u();
        }
        t();
        if (this.f20267g) {
            v();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E() {
        p();
        z();
        if (this.f20280t) {
            y();
        }
        if (this.f20272l) {
            x();
        }
        s();
        if (this.f20275o && y5.b.g()) {
            w();
        }
    }

    private void F() {
        int b10 = this.f20286z.b();
        int d10 = this.f20286z.d();
        LocalLog.a("SuperPowersaveController", "doRecoveryForAutoRotationFunc,rotationMode:" + b10 + ",backupVal:" + d10);
        if (b10 != 0 || d10 == 0) {
            return;
        }
        this.f20286z.l0(d10);
    }

    private void G() {
        boolean c10 = this.f20286z.c();
        boolean e10 = this.f20286z.e();
        LocalLog.a("SuperPowersaveController", "doRecoveryForAutoSyncFunc,autoSyncState:" + c10 + ",backupVal:" + e10);
        if (!e10 || c10) {
            return;
        }
        this.f20286z.m0(true);
    }

    private void H() {
        int x10 = this.f20286z.x();
        int f10 = this.f20286z.f();
        if (f10 != x10) {
            this.f20286z.n0(f10);
        }
    }

    private void I() {
        boolean y4 = this.f20286z.y();
        boolean g6 = this.f20286z.g();
        LocalLog.a("SuperPowersaveController", "doRecoveryForBlackScreenGestureFunc,currentState:" + y4 + ",backupVal:" + g6);
        if (!g6 || y4) {
            return;
        }
        this.f20286z.o0(true);
    }

    private void J() {
        boolean z10 = this.f20286z.z();
        if (!this.f20286z.h() || z10) {
            return;
        }
        this.f20286z.p0(true);
    }

    private void K() {
        if (this.f20273m) {
            int C = this.f20286z.C();
            int j10 = this.f20286z.j();
            LocalLog.a("SuperPowersaveController", "doRecoveryForCurvedDisplayFunc,curvedDisplay:" + C + ",backupVal=" + j10);
            if (C == 0 && j10 != 0) {
                this.f20286z.s0(j10);
            }
        }
        if (this.f20274n) {
            int B = this.f20286z.B();
            int i10 = this.f20286z.i();
            LocalLog.a("SuperPowersaveController", "doRecoveryForCurvedDisplayFunc,callCurvedDisplay:" + B + ",backupValCall=" + i10);
            if (B != 4 || i10 == 4) {
                return;
            }
            this.f20286z.r0(i10);
        }
    }

    private void L() {
        if (this.B != null) {
            this.f20283w.getContentResolver().unregisterContentObserver(this.B);
        }
        if (this.C != null) {
            this.f20283w.getContentResolver().unregisterContentObserver(this.C);
        }
        SuperPowersaveUtils.q0(this.f20283w, false);
        SuperPowersaveUtils.v0(this.f20283w, false);
    }

    private void M() {
        int E2 = this.f20286z.E();
        int k10 = this.f20286z.k();
        if (E2 != 0 || k10 == 0) {
            return;
        }
        this.f20286z.u0(k10);
    }

    private void N() {
        if (y5.b.u()) {
            OsenseNotifyRequest osenseNotifyRequest = new OsenseNotifyRequest(6, 0);
            OsenseResClient osenseResClient = OsenseResClient.get(getClass());
            if (osenseResClient != null) {
                osenseResClient.osenseSetNotification(osenseNotifyRequest);
            } else {
                LocalLog.b("SuperPowersaveController", "close super power save mode: osenseClient is null");
            }
        }
        LocalLog.a("SuperPowersaveController", "doActionForOSENSE: NOTIF_SRC_SUPER_POWER_SAVE_MODE_OFF = 0");
    }

    private void O() {
        if (this.f20281u) {
            int J = this.f20286z.J();
            int l10 = this.f20286z.l();
            LocalLog.a("SuperPowersaveController", "doRecoveryForOplusColorModeFunc,oplusColorMode:" + J + ",backupVal:" + l10);
            if (J != 1 || l10 == 1) {
                return;
            }
            if (l10 != -1) {
                this.f20286z.w0(l10);
            } else {
                LocalLog.a("SuperPowersaveController", "doRecoveryForOplusColorModeFunc COLOR_MODE_INVALID");
                this.f20286z.w0(0);
            }
        }
    }

    private void P() {
        int K = this.f20286z.K();
        int n10 = this.f20286z.n();
        LocalLog.a("SuperPowersaveController", "doRecoveryForOsieVisionEffectFunc,osieVisionEffect:" + K + ",backupVal:" + n10);
        if (K != 0 || n10 == 0) {
            return;
        }
        this.f20286z.y0(n10);
    }

    private void Q() {
        X();
        if (this.f20286z.s()) {
            return;
        }
        this.f20282v.setPowerSaveModeEnabled(false);
    }

    private void R() {
        int u7 = this.f20286z.u();
        LocalLog.a("SuperPowersaveController", "doRecoveryForSceneServiceStart: backupVal=" + u7);
        if (u7 == 1) {
            this.f20286z.A0(1);
        }
        this.f20286z.D0();
    }

    private void S() {
        boolean N = this.f20286z.N();
        boolean v7 = this.f20286z.v();
        LocalLog.a("SuperPowersaveController", "doRecoveryForSwipeSideGestureFunc,currentState:" + N + ",backupVal:" + v7);
        if (!v7 || N) {
            return;
        }
        this.f20286z.B0(true);
    }

    private void T() {
        boolean w10 = this.f20286z.w();
        LocalLog.a("SuperPowersaveController", "doRecoveryForWifiScanAlwaysFunc,backupVal:" + w10);
        this.f20286z.C0(w10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void U() {
        H();
        R();
        if (this.f20280t) {
            Q();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void V() {
        if (this.f20268h) {
            T();
        }
        if (this.f20277q) {
            F();
        }
        if (this.f20279s) {
            I();
        }
        if (this.f20276p) {
            G();
        }
        if (this.f20270j && !SuperPowersaveUtils.A(this.f20283w)) {
            J();
        }
        if (this.f20271k && !SuperPowersaveUtils.F(this.f20283w)) {
            M();
        }
        L();
        S();
        if (this.f20267g) {
            N();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void W() {
        if (this.f20272l) {
            P();
        }
        K();
        if (this.f20275o && y5.b.g()) {
            O();
        }
    }

    private void X() {
        this.f20286z.z0(this.f20283w, false);
        f.F2(this.f20283w, this.f20286z.o() ? 1 : 0);
        f.G2(this.f20283w, this.f20286z.t() ? 1 : 0);
        f.z2(this.f20283w, this.f20286z.p() ? 1 : 0);
        f.E2(this.f20283w, this.f20286z.r() ? 1 : 0);
    }

    private void Y() {
        this.f20286z.z0(this.f20283w, true);
        f.F2(this.f20283w, 1);
        f.G2(this.f20283w, 1);
        f.z2(this.f20283w, 0);
        f.E2(this.f20283w, 1);
    }

    public static SuperPowersaveController Z(Context context) {
        if (E == null) {
            synchronized (SuperPowersaveController.class) {
                if (E == null) {
                    E = new SuperPowersaveController(context);
                }
            }
        }
        return E;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a0() {
        LocalLog.a("SuperPowersaveController", "getPowersavePolicy");
        try {
            this.f20266f = Boolean.valueOf(this.f20265e.d("cpu_policy_enable", "true")).booleanValue();
            this.f20267g = Boolean.valueOf(this.f20265e.d("hypnus_policy_enable", "true")).booleanValue();
            this.f20268h = Boolean.valueOf(this.f20265e.d("wifi_scan_always_disable", "true")).booleanValue();
            this.f20269i = Boolean.valueOf(this.f20265e.d("wifi_ap_state_disable", "true")).booleanValue();
            this.f20270j = Boolean.valueOf(this.f20265e.d("bluetooth_state_disable", "true")).booleanValue();
            this.f20271k = Boolean.valueOf(this.f20265e.d("gps_state_disable", "true")).booleanValue();
            this.f20272l = Boolean.valueOf(this.f20265e.d("osie_vision_effect_disable", "true")).booleanValue();
            this.f20273m = Boolean.valueOf(this.f20265e.d("aod_curved_display_disable", "true")).booleanValue();
            this.f20274n = Boolean.valueOf(this.f20265e.d("call_curved_display_disable", "true")).booleanValue();
            this.f20275o = Boolean.valueOf(this.f20265e.d("oplus_color_mode_disable", "true")).booleanValue();
            this.f20276p = Boolean.valueOf(this.f20265e.d("sync_automatically_disable", "true")).booleanValue();
            this.f20277q = Boolean.valueOf(this.f20265e.d("auto_rotation_disable", "true")).booleanValue();
            this.f20278r = Boolean.valueOf(this.f20265e.d("color_dark_mode_enabled", "true")).booleanValue();
            this.f20279s = Boolean.valueOf(this.f20265e.d("black_screen_gesture_disable", "true")).booleanValue();
            this.f20280t = Boolean.valueOf(this.f20265e.d("power_save_mode_enabled", "true")).booleanValue();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e0(boolean z10) {
        int r10 = f.r(this.f20283w);
        if (z10) {
            this.f20286z.t0(System.currentTimeMillis());
            this.A.O0(true, r10, -1L);
        } else {
            this.A.O0(false, r10, (System.currentTimeMillis() - this.f20286z.D()) / 60000);
            this.f20286z.t0(-1L);
        }
    }

    private void f0() {
        Intent intent = new Intent("oplus.intent.action.superpowersave.ready");
        intent.setPackage("com.android.systemui");
        intent.putExtra("sendFrom", this.f20283w.getPackageName());
        this.f20283w.sendBroadcastAsUser(intent, UserHandle.SYSTEM);
    }

    private void m() {
        this.f20286z.g0(this.f20282v.isPowerSaveMode());
        this.f20286z.e0(f.e0(this.f20283w) == 1);
        this.f20286z.c0(f.j0(this.f20283w) == 1);
        this.f20286z.h0(f.k0(this.f20283w) == 1);
        this.f20286z.d0(f.b0(this.f20283w) != 0);
        this.f20286z.f0(f.i0(this.f20283w) != 0);
    }

    private void n() {
        int b10 = this.f20286z.b();
        this.f20286z.R(b10);
        LocalLog.a("SuperPowersaveController", "doActionForAutoRotationFunc: rotationMode=" + b10);
        if (b10 == 1) {
            this.f20286z.l0(0);
        }
    }

    private void o() {
        boolean c10 = this.f20286z.c();
        this.f20286z.S(c10);
        LocalLog.a("SuperPowersaveController", "doActionForAutoSyncFunc,isFuncOn:" + c10);
        if (c10) {
            this.f20286z.m0(false);
        }
    }

    private void p() {
        int x10 = this.f20286z.x();
        this.f20286z.T(x10);
        if (x10 == 0) {
            this.f20286z.n0(1);
        }
    }

    private void q() {
        boolean y4 = this.f20286z.y();
        this.f20286z.U(y4);
        LocalLog.a("SuperPowersaveController", "doActionForBlackScreenGestureFunc: isFuncOn=" + y4);
        if (y4) {
            this.f20286z.o0(false);
        }
    }

    private void r() {
        boolean z10 = this.f20286z.z();
        this.f20286z.V(z10);
        if (z10) {
            this.f20286z.p0(false);
        }
    }

    private void s() {
        if (this.f20273m) {
            int C = this.f20286z.C();
            this.f20286z.X(C);
            LocalLog.a("SuperPowersaveController", "doActionForCurvedDisplayFunc,curvedDisplay:" + C);
            if (C != 0) {
                this.f20286z.s0(0);
            }
        }
        if (this.f20274n) {
            int B = this.f20286z.B();
            this.f20286z.W(B);
            LocalLog.a("SuperPowersaveController", "doActionForCurvedDisplayFunc,callCurvedDisplay:" + B);
            if (B != 4) {
                this.f20286z.r0(4);
            }
        }
    }

    private void t() {
        this.f20283w.getContentResolver().registerContentObserver(Settings.Global.getUriFor("bluetooth_on"), false, this.B);
        this.f20283w.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("location_providers_allowed"), false, this.C);
    }

    private void u() {
        int E2 = this.f20286z.E();
        this.f20286z.Y(E2);
        if (E2 != 0) {
            this.f20286z.u0(0);
        }
    }

    private void v() {
        if (y5.b.u()) {
            OsenseNotifyRequest osenseNotifyRequest = new OsenseNotifyRequest(6, 1);
            OsenseResClient osenseResClient = OsenseResClient.get(getClass());
            if (osenseResClient != null) {
                osenseResClient.osenseSetNotification(osenseNotifyRequest);
            } else {
                LocalLog.b("SuperPowersaveController", "open super power save mode: osenseClient is null");
            }
        }
        LocalLog.a("SuperPowersaveController", "doActionForOSENSE: NOTIF_SRC_SUPER_POWER_SAVE_MODE_ON = 1");
    }

    private void w() {
        if (this.f20286z.I()) {
            LocalLog.a("SuperPowersaveController", "doActionForOplusColorModeFunc,getIsScreenBlocked");
            this.f20281u = false;
            this.f20286z.a0(false);
            return;
        }
        this.f20281u = true;
        this.f20286z.a0(true);
        int J = this.f20286z.J();
        this.f20286z.Z(J);
        LocalLog.a("SuperPowersaveController", "doActionForOplusColorModeFunc,getOplusColorModeFuncMode:" + J);
        if (J != 1) {
            this.f20286z.w0(1);
        }
    }

    private void x() {
        int K = this.f20286z.K();
        this.f20286z.b0(K);
        LocalLog.a("SuperPowersaveController", "doActionForOsieVisionEffectFunc,osieVisionEffect:" + K);
        if (K != 0) {
            this.f20286z.y0(0);
        }
    }

    private void y() {
        m();
        Y();
        if (!this.f20282v.isPowerSaveMode()) {
            this.f20282v.setPowerSaveModeEnabled(true);
        }
    }

    private void z() {
        int L = this.f20286z.L();
        this.f20286z.i0(L);
        LocalLog.a("SuperPowersaveController", "doActionForSceneServiceStart: startState=" + L);
        if (L == 1) {
            this.f20286z.A0(0);
        }
    }

    public void b0() {
        LocalLog.a("SuperPowersaveController", "notePowersavePolicyChange");
        this.f20284x.removeMessages(2);
        this.f20284x.sendEmptyMessage(2);
    }

    public void c0() {
        g0();
        this.f20283w.getContentResolver().unregisterContentObserver(this.D);
        SuperEnduranceController.h(this.f20283w).m();
    }

    public void d0() {
        registerAction();
        this.f20283w.getContentResolver().registerContentObserver(Settings.System.getUriFor("super_powersave_launcher_enter"), false, this.D);
        BatteryRemainTimeCalculator.e(this.f20283w).a();
        f0();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
        if (i10 != 903) {
            return;
        }
        this.f20284x.sendEmptyMessage(0);
    }

    public void g0() {
        Affair.f().i(this, 903);
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 903);
    }
}
