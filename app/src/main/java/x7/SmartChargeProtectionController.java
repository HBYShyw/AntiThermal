package x7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.deepthinker.sdk.app.ClientConnection;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.deepthinker.sdk.app.OplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.feature.LongTermChargingConstants;
import com.oplus.deepthinker.sdk.app.feature.eventassociation.PerformanceOfLongCharging;
import com.oplus.deepthinker.sdk.app.userprofile.labels.LeaveHomeCluster;
import com.oplus.deepthinker.sdk.app.userprofile.labels.LeaveHomeLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.SleepHabitCluster;
import com.oplus.deepthinker.sdk.app.userprofile.labels.SleepHabitLabel;
import com.oplus.powermanager.fuelgaue.BatteryHealthActivity;
import com.oplus.powermanager.fuelgaue.PowerConsumptionActivity;
import com.oplus.powermanager.fuelgaue.base.HighlightPreferenceGroupAdapter;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.statistics.OplusTrack;
import com.oplus.statistics.util.TimeInfoUtil;
import f6.ChargeUtil;
import f6.CommonUtil;
import f6.f;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import w4.Affair;
import w4.IAffairCallback;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: SmartChargeProtectionController.java */
/* renamed from: x7.e, reason: use source file name */
/* loaded from: classes.dex */
public class SmartChargeProtectionController implements IAffairCallback {

    /* renamed from: o0, reason: collision with root package name */
    private static volatile SmartChargeProtectionController f19608o0;

    /* renamed from: a0, reason: collision with root package name */
    private long f19609a0;

    /* renamed from: e, reason: collision with root package name */
    private int f19613e;

    /* renamed from: f, reason: collision with root package name */
    private int f19615f;

    /* renamed from: f0, reason: collision with root package name */
    private Context f19616f0;

    /* renamed from: g0, reason: collision with root package name */
    private Handler f19618g0;

    /* renamed from: h0, reason: collision with root package name */
    private PowerManager.WakeLock f19620h0;

    /* renamed from: i0, reason: collision with root package name */
    private UploadDataUtil f19622i0;

    /* renamed from: j0, reason: collision with root package name */
    private PerformanceOfLongCharging f19624j0;

    /* renamed from: k0, reason: collision with root package name */
    private ClientConnection f19626k0;

    /* renamed from: l0, reason: collision with root package name */
    private ContentObserver f19628l0;

    /* renamed from: m0, reason: collision with root package name */
    private ContentObserver f19630m0;

    /* renamed from: g, reason: collision with root package name */
    private int f19617g = 0;

    /* renamed from: h, reason: collision with root package name */
    private int f19619h = 0;

    /* renamed from: i, reason: collision with root package name */
    private int f19621i = 0;

    /* renamed from: j, reason: collision with root package name */
    private int f19623j = 0;

    /* renamed from: k, reason: collision with root package name */
    private int f19625k = 0;

    /* renamed from: l, reason: collision with root package name */
    private int f19627l = 0;

    /* renamed from: m, reason: collision with root package name */
    private int f19629m = -1;

    /* renamed from: n, reason: collision with root package name */
    private int f19631n = 0;

    /* renamed from: o, reason: collision with root package name */
    private int f19633o = ChargeUtil.f11371l;

    /* renamed from: p, reason: collision with root package name */
    private int f19634p = 0;

    /* renamed from: q, reason: collision with root package name */
    private int f19635q = -1;

    /* renamed from: r, reason: collision with root package name */
    private int f19636r = 0;

    /* renamed from: s, reason: collision with root package name */
    private int f19637s = 0;

    /* renamed from: t, reason: collision with root package name */
    private long f19638t = 0;

    /* renamed from: u, reason: collision with root package name */
    private long f19639u = 0;

    /* renamed from: v, reason: collision with root package name */
    private long f19640v = 0;

    /* renamed from: w, reason: collision with root package name */
    private long f19641w = 0;

    /* renamed from: x, reason: collision with root package name */
    private long f19642x = 0;

    /* renamed from: y, reason: collision with root package name */
    private long f19643y = 0;

    /* renamed from: z, reason: collision with root package name */
    private long f19644z = 0;
    private long A = 0;
    private long B = 0;
    private long C = 0;
    private float D = 0.0f;
    private float E = 0.0f;
    private float F = 0.0f;
    private float G = 0.0f;
    private long H = 0;
    private float I = 0.0f;
    private float J = 0.0f;
    private float K = 0.0f;
    private float L = 0.0f;
    private long M = 0;
    private long N = 0;
    private long O = 0;
    private boolean P = false;
    private boolean Q = false;
    private boolean R = false;
    private boolean S = false;
    private boolean T = false;
    private boolean U = false;
    private boolean V = false;
    private boolean W = false;
    private int X = 0;
    private long Y = -1;
    private long Z = -1;

    /* renamed from: b0, reason: collision with root package name */
    private long f19610b0 = -1;

    /* renamed from: c0, reason: collision with root package name */
    private long f19611c0 = -1;

    /* renamed from: d0, reason: collision with root package name */
    private boolean f19612d0 = false;

    /* renamed from: e0, reason: collision with root package name */
    private boolean f19614e0 = false;

    /* renamed from: n0, reason: collision with root package name */
    private BroadcastReceiver f19632n0 = new c();

    /* compiled from: SmartChargeProtectionController.java */
    /* renamed from: x7.e$a */
    /* loaded from: classes.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            super.onChange(z10);
            SmartChargeProtectionController smartChargeProtectionController = SmartChargeProtectionController.this;
            smartChargeProtectionController.f19613e = f.I0(smartChargeProtectionController.f19616f0);
            SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 108));
            LocalLog.a("SmartChargeProtectionController", "mSmartChargeProtectionSwitchStatus: " + SmartChargeProtectionController.this.f19613e);
        }
    }

    /* compiled from: SmartChargeProtectionController.java */
    /* renamed from: x7.e$b */
    /* loaded from: classes.dex */
    class b extends ContentObserver {
        b(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            super.onChange(z10);
            SmartChargeProtectionController smartChargeProtectionController = SmartChargeProtectionController.this;
            smartChargeProtectionController.f19615f = f.n0(smartChargeProtectionController.f19616f0);
            SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 117));
            LocalLog.a("SmartChargeProtectionController", "mRegularChargeProtectionSwitchStatus: " + SmartChargeProtectionController.this.f19615f);
        }
    }

    /* compiled from: SmartChargeProtectionController.java */
    /* renamed from: x7.e$c */
    /* loaded from: classes.dex */
    class c extends BroadcastReceiver {
        c() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LocalLog.a("SmartChargeProtectionController", "onReceiver: " + action);
            SmartChargeProtectionController.this.f19620h0.acquire(2000L);
            if ("oplus.intent.action.smartchargeprotection.open".equals(action)) {
                SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 109));
                return;
            }
            if ("oplus.intent.action.smartchargeprotection.close".equals(action)) {
                SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 110));
                return;
            }
            if ("oplus.intent.action.smartchargeprotection.end".equals(action)) {
                SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 113));
                return;
            }
            if ("oplus.intent.action.longchargeprotection.end.auto".equals(action)) {
                SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 107));
                return;
            }
            if ("oplus.intent.action.longchargeprotection.forget.one.month".equals(action)) {
                SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 114));
                return;
            }
            if ("oplus.intent.action.smartchargeprotection.notification".equals(action)) {
                SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 115));
            } else if ("oplus.intent.action.longchargeprotection.guide.open.switch".equals(action)) {
                SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 116));
            } else if ("oplus.intent.action.slowchargeprotection.get.ai.data".equals(action)) {
                SmartChargeProtectionController.this.f19618g0.sendMessage(Message.obtain(SmartChargeProtectionController.this.f19618g0, 103));
            }
        }
    }

    /* compiled from: SmartChargeProtectionController.java */
    /* renamed from: x7.e$d */
    /* loaded from: classes.dex */
    private class d extends Handler {
        public d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 100:
                    if (ChargeUtil.f11362c) {
                        SmartChargeProtectionController.this.registerAction();
                    }
                    ChargeProtectionController.N(SmartChargeProtectionController.this.f19616f0).X();
                    SmartChargeProtectionController.this.V = true;
                    return;
                case 101:
                    if (ChargeUtil.f11362c) {
                        SmartChargeProtectionController.this.n0();
                    }
                    ChargeProtectionController.N(SmartChargeProtectionController.this.f19616f0).S();
                    NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).h();
                    NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).g();
                    SmartChargeProtectionController.this.O(false);
                    SmartChargeProtectionController.this.N(0);
                    f.c3(SmartChargeProtectionController.this.f19616f0, 0);
                    f.b3(SmartChargeProtectionController.this.f19616f0, 0L);
                    f.I2(SmartChargeProtectionController.this.f19616f0, 0);
                    SmartChargeProtectionController.this.e0(0);
                    return;
                case 102:
                    SmartChargeProtectionController.this.F();
                    return;
                case 103:
                    SmartChargeProtectionController.this.G(ChargeUtil.f11383x, ChargeUtil.f11384y, false);
                    return;
                case 104:
                    SmartChargeProtectionController.this.E();
                    return;
                case 105:
                case 111:
                case 112:
                default:
                    return;
                case 106:
                    SmartChargeProtectionController.this.O(false);
                    SmartChargeProtectionController.this.N(0);
                    NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).h();
                    ChargeProtectionUtils.a(SmartChargeProtectionController.this.f19616f0);
                    f.c3(SmartChargeProtectionController.this.f19616f0, 0);
                    f.b3(SmartChargeProtectionController.this.f19616f0, 0L);
                    f.l3(SmartChargeProtectionController.this.f19616f0, 0);
                    LocalLog.a("SmartChargeProtectionController", "handle MSG_SMART_CHARGE_PROTECTION_OUT_WITH_UNPLUG---mIsInLongChargeProtectionState: " + SmartChargeProtectionController.this.P);
                    if (SmartChargeProtectionController.this.f19621i != 100) {
                        SmartChargeProtectionController.this.f19622i0.J0(ChargeUtil.y(System.currentTimeMillis()), Integer.toString(SmartChargeProtectionController.this.f19621i));
                    }
                    SmartChargeProtectionController.this.P = false;
                    SmartChargeProtectionController.this.f19612d0 = false;
                    SmartChargeProtectionController.this.Y();
                    return;
                case 107:
                    SmartChargeProtectionController.this.P = false;
                    SmartChargeProtectionController.this.O(false);
                    NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).h();
                    f.l3(SmartChargeProtectionController.this.f19616f0, 0);
                    SmartChargeProtectionController.this.V();
                    LocalLog.a("SmartChargeProtectionController", "handle MSG_LONG_CHARGE_PROTECTION_OUT_WITH_AUTO_FULL---mIsInLongChargeProtectionState: " + SmartChargeProtectionController.this.P);
                    return;
                case 108:
                    NotifyUtil v7 = NotifyUtil.v(SmartChargeProtectionController.this.f19616f0);
                    if (SmartChargeProtectionController.this.f19613e == 0) {
                        v7.s();
                        v7.h();
                        SmartChargeProtectionController.this.P = false;
                        SmartChargeProtectionController.this.O(false);
                        SmartChargeProtectionController.this.N(0);
                        f.l3(SmartChargeProtectionController.this.f19616f0, 0);
                        f.c3(SmartChargeProtectionController.this.f19616f0, 0);
                        f.b3(SmartChargeProtectionController.this.f19616f0, 0L);
                        ChargeProtectionUtils.i(SmartChargeProtectionController.this.f19616f0, -1L);
                        SmartChargeProtectionController.this.Y();
                        return;
                    }
                    v7.t();
                    if (System.currentTimeMillis() - SmartChargeProtectionController.this.A > 300000) {
                        f.p2(SmartChargeProtectionController.this.f19616f0, 0);
                        return;
                    }
                    return;
                case 109:
                    NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).t();
                    f.d3(SmartChargeProtectionController.this.f19616f0, 1);
                    SmartChargeProtectionController.this.A = System.currentTimeMillis();
                    ChargeProtectionUtils.m(SmartChargeProtectionController.this.f19616f0);
                    SmartChargeProtectionController.this.f19622i0.B0(Long.toString(SmartChargeProtectionController.this.A));
                    return;
                case 110:
                    CommonUtil.c(SmartChargeProtectionController.this.f19616f0);
                    Intent intent = new Intent(SmartChargeProtectionController.this.f19616f0, (Class<?>) (AppFeature.d() ? BatteryHealthActivity.class : PowerConsumptionActivity.class));
                    intent.addFlags(268468224);
                    intent.putExtra(HighlightPreferenceGroupAdapter.EXTRA_FRAGMENT_ARG_KEY, AppFeature.d() ? "smart_charge_protection_switch_in_health" : "smart_charge_protection_switch_in_more");
                    SmartChargeProtectionController.this.f19616f0.startActivity(intent);
                    NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).s();
                    SmartChargeProtectionController.this.f19622i0.B0(Long.toString(System.currentTimeMillis()));
                    return;
                case 113:
                    SmartChargeProtectionController.this.O(false);
                    SmartChargeProtectionController.this.N(0);
                    NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).h();
                    f.c3(SmartChargeProtectionController.this.f19616f0, 0);
                    f.b3(SmartChargeProtectionController.this.f19616f0, 0L);
                    int P0 = f.P0(SmartChargeProtectionController.this.f19616f0) + 1;
                    int T0 = f.T0(SmartChargeProtectionController.this.f19616f0);
                    long currentTimeMillis = System.currentTimeMillis();
                    LocalLog.a("SmartChargeProtectionController", "MSG_SNART_CHARGE_PROTECTION_NOTIFICATION_END: times = " + P0 + ", gear = " + T0);
                    if (SmartChargeProtectionController.this.P || SmartChargeProtectionController.this.f19612d0) {
                        f.l3(SmartChargeProtectionController.this.f19616f0, P0);
                        f.k3(SmartChargeProtectionController.this.f19616f0, currentTimeMillis);
                        if (P0 % ChargeUtil.f11368i == 0) {
                            NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).O();
                        }
                        SmartChargeProtectionController.this.f19622i0.E0(ChargeUtil.y(currentTimeMillis), Integer.toString(P0));
                        if (SmartChargeProtectionController.this.P) {
                            SmartChargeProtectionController.this.f19622i0.D0(ChargeUtil.y(currentTimeMillis), Integer.toString(T0 + 1));
                            SmartChargeProtectionController.this.C = currentTimeMillis;
                            if (SmartChargeProtectionController.this.C - SmartChargeProtectionController.this.B >= 300000) {
                                f.g3(SmartChargeProtectionController.this.f19616f0, f.L0(SmartChargeProtectionController.this.f19616f0) + 1);
                            }
                            SmartChargeProtectionController.this.B = 0L;
                        }
                    }
                    ChargeProtectionUtils.i(SmartChargeProtectionController.this.f19616f0, System.currentTimeMillis());
                    SmartChargeProtectionController.this.P = false;
                    SmartChargeProtectionController.this.Q = false;
                    SmartChargeProtectionController.this.f19612d0 = false;
                    return;
                case 114:
                    LocalLog.a("SmartChargeProtectionController", "handle MSG_LONG_CHARGE_PROTECTION_FORGET_ONE_MONTH");
                    f.l3(SmartChargeProtectionController.this.f19616f0, 0);
                    f.k3(SmartChargeProtectionController.this.f19616f0, 0L);
                    int T02 = f.T0(SmartChargeProtectionController.this.f19616f0);
                    long currentTimeMillis2 = System.currentTimeMillis();
                    LocalLog.a("SmartChargeProtectionController", "MSG_LONG_CHARGE_PROTECTION_FORGET_ONE_MONTH: gear = " + T02);
                    if (T02 >= 1) {
                        f.o3(SmartChargeProtectionController.this.f19616f0, T02 - 1);
                    }
                    SmartChargeProtectionController.this.f19622i0.A0(ChargeUtil.y(currentTimeMillis2), Integer.toString(T02));
                    return;
                case 115:
                    LocalLog.b("SmartChargeProtectionController", "MSG_LONG_CHARGE_PROTECTION_CLICK_NOTIFICATION");
                    SmartChargeProtectionController.this.f19622i0.B0(Long.toString(System.currentTimeMillis()));
                    return;
                case 116:
                    SmartChargeProtectionController.this.f19622i0.C0(Boolean.toString(SmartChargeProtectionController.this.f19613e == 1));
                    return;
                case 117:
                    if (SmartChargeProtectionController.this.f19615f != 1) {
                        NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).g();
                        SmartChargeProtectionController.this.O(false);
                        f.I2(SmartChargeProtectionController.this.f19616f0, 0);
                        SmartChargeProtectionController.this.R = false;
                        return;
                    }
                    return;
                case 118:
                    if (SmartChargeProtectionController.this.R || SmartChargeProtectionController.this.f19623j != 2 || SmartChargeProtectionController.this.f19621i < 80) {
                        return;
                    }
                    NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).A();
                    SmartChargeProtectionController.this.O(true);
                    f.I2(SmartChargeProtectionController.this.f19616f0, 1);
                    SmartChargeProtectionController.this.R = true;
                    return;
                case 119:
                    NotifyUtil.v(SmartChargeProtectionController.this.f19616f0).g();
                    SmartChargeProtectionController.this.O(false);
                    f.I2(SmartChargeProtectionController.this.f19616f0, 0);
                    SmartChargeProtectionController.this.R = false;
                    return;
            }
        }
    }

    private SmartChargeProtectionController(Context context) {
        this.f19613e = -1;
        this.f19615f = -1;
        this.f19609a0 = -1L;
        this.f19628l0 = new a(this.f19618g0);
        this.f19630m0 = new b(this.f19618g0);
        this.f19616f0 = context;
        this.f19622i0 = UploadDataUtil.S0(context);
        this.f19613e = f.I0(this.f19616f0);
        this.f19615f = f.n0(this.f19616f0);
        this.f19620h0 = ((PowerManager) this.f19616f0.getSystemService("power")).newWakeLock(1, "ChargeProtection:controller");
        this.f19609a0 = System.currentTimeMillis();
        HandlerThread handlerThread = new HandlerThread("LongChargeProtectionContoller");
        handlerThread.start();
        this.f19618g0 = new d(handlerThread.getLooper());
        this.f19626k0 = new ClientConnection(this.f19616f0, Executors.newFixedThreadPool(3), this.f19618g0);
    }

    private void D() {
        long currentTimeMillis = System.currentTimeMillis();
        int i10 = this.f19625k;
        if (i10 == 0) {
            if (i10 != this.f19619h) {
                if (this.f19638t != 0) {
                    if (LocalLog.f()) {
                        LocalLog.a("SmartChargeProtectionController", "calculateLongChargeData: unplug the adapter -> interrupt LongCharge data -> initial LongChargeDisCharge data");
                    }
                    this.f19639u = currentTimeMillis;
                    this.f19640v = currentTimeMillis - this.f19638t;
                    this.f19641w = currentTimeMillis;
                    return;
                }
                return;
            }
            if (this.f19641w != 0) {
                if (LocalLog.f()) {
                    LocalLog.a("SmartChargeProtectionController", "calculateLongChargeData: unpluging -> calculate LongChargeDisCharge data");
                }
                this.f19642x = currentTimeMillis;
                long j10 = currentTimeMillis - this.f19641w;
                this.f19643y = j10;
                if (j10 >= ChargeUtil.f11365f * 60000) {
                    if (LocalLog.f()) {
                        LocalLog.a("SmartChargeProtectionController", "calculateLongChargeData: unpluging -> exceed LongChargeDisCharge level -> handle current data");
                    }
                    if (this.f19640v >= ChargeUtil.f11364e * 60000) {
                        b0();
                        LocalLog.a("SmartChargeProtectionController", "calculateLongChargeData: unpluging -> meet long charge condition -> send long charge data");
                    }
                    if (LocalLog.f()) {
                        LocalLog.a("SmartChargeProtectionController", "calculateLongChargeData: unpluging -> exceed LongChargeDisCharge level -> reset current data");
                    }
                    this.f19641w = 0L;
                    this.f19642x = 0L;
                    this.f19643y = 0L;
                    this.f19638t = 0L;
                    this.f19639u = 0L;
                    this.f19640v = 0L;
                    this.f19631n = 0;
                    this.f19644z = 0L;
                    this.f19635q = -1;
                    return;
                }
                return;
            }
            return;
        }
        if (i10 != this.f19619h) {
            int i11 = this.f19635q;
            if (i11 == -1) {
                i11 = this.f19621i;
            }
            this.f19635q = i11;
            if (this.f19641w != 0) {
                if (LocalLog.f()) {
                    LocalLog.a("SmartChargeProtectionController", "calculateLongChargeData: pluging -> handle LongChargeDisCharge data");
                }
                this.f19642x = currentTimeMillis;
                long j11 = currentTimeMillis - this.f19641w;
                this.f19643y = j11;
                this.f19641w = 0L;
                this.f19631n++;
                long j12 = this.f19644z;
                if (j11 < j12) {
                    j11 = j12;
                }
                this.f19644z = j11;
            }
        }
        if (this.f19621i >= 80) {
            if (this.f19638t == 0) {
                if (LocalLog.f()) {
                    LocalLog.a("SmartChargeProtectionController", "calculateLongChargeData: pluging -> exceed LongChargelevel -> initial LongCharge data");
                }
                this.f19638t = currentTimeMillis;
                this.f19639u = 0L;
                this.f19640v = 0L;
                return;
            }
            if (LocalLog.f()) {
                LocalLog.a("SmartChargeProtectionController", "calculateLongChargeData: pluging -> exceed LongChargelevel -> calculate LongCharge data");
            }
            this.f19639u = currentTimeMillis;
            this.f19640v = currentTimeMillis - this.f19638t;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E() {
        this.f19609a0 = System.currentTimeMillis();
        if (this.T) {
            this.Y = this.f19610b0;
            this.Z = this.f19611c0;
        }
        if (P()) {
            return;
        }
        LocalLog.a("SmartChargeProtectionController", "determinSlowChargeScene: mIsTestMode = " + this.T + ", mRtcForSlowCharge = " + this.f19609a0 + ", mAiRestStartTime = " + this.Y + ", mAiRestEndTime = " + this.Z + ", mIsInSlowChargeProtectionState = " + this.f19612d0);
        PowerManager powerManager = (PowerManager) this.f19616f0.getSystemService("power");
        if (!this.f19612d0) {
            long j10 = this.f19609a0;
            if (j10 < this.Y || j10 > this.Z - ChargeUtil.f11382w) {
                return;
            }
            NotifyUtil v7 = NotifyUtil.v(this.f19616f0);
            if (this.f19613e == 1) {
                N(1);
                v7.B();
                f.c3(this.f19616f0, 1);
                f.b3(this.f19616f0, this.Z);
                this.f19612d0 = true;
                UploadDataUtil.S0(this.f19616f0).w0(String.valueOf(this.f19609a0), String.valueOf(powerManager.isInteractive()), String.valueOf(this.f19621i), String.valueOf(this.Y), true);
                return;
            }
            k0(this.f19609a0, v7);
            return;
        }
        long j11 = this.f19609a0;
        if (j11 < this.Y || j11 > this.Z - ChargeUtil.f11382w) {
            N(0);
            NotifyUtil.v(this.f19616f0).h();
            f.c3(this.f19616f0, 0);
            f.b3(this.f19616f0, 0L);
            this.f19612d0 = false;
            UploadDataUtil.S0(this.f19616f0).w0(String.valueOf(this.f19609a0), String.valueOf(powerManager.isInteractive()), String.valueOf(this.f19621i), String.valueOf(this.Y), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F() {
        float f10 = ChargeUtil.f11369j;
        int T0 = f.T0(this.f19616f0);
        float f11 = ChargeUtil.i()[T0];
        float f12 = ChargeUtil.j()[T0];
        I();
        LocalLog.a("SmartChargeProtectionController", "determineLongChargeScene:, d1 = " + f10 + ", m1 = " + f11 + ", m2 = " + f12);
        if (P()) {
            return;
        }
        if (this.G >= f10) {
            LocalLog.a("SmartChargeProtectionController", "determineLongChargeScene: Identify long-term crowds at fixed locations");
            M();
        } else {
            float f13 = this.F;
            if (f13 >= ChargeUtil.f11370k) {
                if ((this.D * ChargeUtil.f11366g) + (f13 * ChargeUtil.f11367h) >= f11) {
                    LocalLog.a("SmartChargeProtectionController", "determineLongChargeScene: long charge scene in -> Identify people with highly overlapping front desk apk and long charging behaviors in non-fixed locations");
                    M();
                } else {
                    LocalLog.a("SmartChargeProtectionController", "determineLongChargeScene: long charge scene out -> Identify people with highly overlapping front desk apk and long charging behaviors in non-fixed locations");
                }
            } else if ((this.D * ChargeUtil.f11366g) + (this.E * ChargeUtil.f11367h) > f12) {
                LocalLog.a("SmartChargeProtectionController", "determineLongChargeScene: long charge scene in -> Supplementary identification of long-term crowds in fixed locations");
                M();
            } else {
                LocalLog.a("SmartChargeProtectionController", "determineLongChargeScene: long charge scene out -> Supplementary identification of long-term crowds in fixed locations");
            }
        }
        if (f11 >= 0.5f || f12 >= 0.5f) {
            this.f19622i0.z0(Float.toString(this.D), Float.toString(this.E), Float.toString(this.F), Float.toString(this.G));
        }
    }

    private void I() {
        OplusDeepThinkerManager oplusDeepThinkerManager = new OplusDeepThinkerManager(this.f19616f0);
        oplusDeepThinkerManager.setRemote(this.f19626k0.getDeepThinkerBridge());
        PerformanceOfLongCharging performanceForLongCharging = oplusDeepThinkerManager.getPerformanceForLongCharging(DataLinkConstants.LONG_TERM_CHARGING_LEARN, ChargeUtil.f11377r, ChargeUtil.f11363d);
        this.f19624j0 = performanceForLongCharging;
        this.D = performanceForLongCharging.getProbabilityOfTime();
        this.E = this.f19624j0.getProbabilityOfLocation();
        this.F = this.f19624j0.getProbabilityOfApps();
        this.G = this.f19624j0.getProbabilityOfTotal();
        this.H = this.f19624j0.getDuration();
        if (this.T && this.U) {
            this.D = this.I;
            this.E = this.J;
            this.F = this.K;
            this.G = this.L;
            this.H = this.M;
        }
        LocalLog.a("SmartChargeProtectionController", "getAiData:, mIsTestMode = " + this.T + ", mIsGetAiTestData = " + this.U + ", mCurrentTimeMatch = " + this.D + ", mCurrentLocationMatch = " + this.E + ", mCurrentFgAppMatch = " + this.F + ", mCurrentComprehensiveMatch = " + this.G + ", mCurrentLongChargePredictDuration = " + this.H);
    }

    public static SmartChargeProtectionController J(Context context) {
        if (f19608o0 == null) {
            synchronized (SmartChargeProtectionController.class) {
                if (f19608o0 == null) {
                    f19608o0 = new SmartChargeProtectionController(context);
                }
            }
        }
        return f19608o0;
    }

    private void L(int i10, int i11, int i12) {
        int i13;
        if (i12 == 2) {
            this.f19633o = ChargeUtil.f11371l;
        } else if (i12 == 1) {
            this.f19633o = ChargeUtil.f11372m;
        } else if (i12 == 4) {
            this.f19633o = ChargeUtil.f11373n;
        } else {
            this.f19633o = ChargeUtil.f11371l;
        }
        this.f19621i = i10;
        this.f19623j = i11;
        this.f19625k = i12;
        if (this.Q || this.P || this.R || this.f19612d0) {
            LocalLog.a("SmartChargeProtectionController", "handleBatteryChangeAction:  , mIsInLongChargeProtectionState = " + this.P + ", mIsInNightChargeProtectionState = " + this.Q + ", mIsInSlowChargeProtectionState = " + this.f19612d0 + ", mIsInRegularChargeProtectionState = " + this.R);
            if ((this.P || this.R || this.f19612d0) && (i13 = this.f19625k) == 0 && i13 != this.f19619h) {
                LocalLog.a("SmartChargeProtectionController", "current is in LongChargeProtectionState || RegularChargeProtectionState, but charger is unplug!");
                this.f19618g0.sendMessage(Message.obtain(this.f19618g0, (this.P || this.f19612d0) ? 106 : 119));
            }
        }
        D();
        if (this.f19615f == 1) {
            this.f19618g0.sendMessage(Message.obtain(this.f19618g0, 118));
        } else {
            if (System.currentTimeMillis() - f.R0(this.f19616f0) >= ChargeUtil.f11363d * TimeInfoUtil.MILLISECOND_OF_A_DAY && ChargeUtil.f11362c) {
                j0();
            }
            if (Q() && ChargeUtil.f11381v) {
                this.f19618g0.sendMessage(Message.obtain(this.f19618g0, 104));
            }
        }
        if (i11 == 5 && (this.P || this.f19612d0)) {
            NotifyUtil.v(this.f19616f0).h();
            f.c3(this.f19616f0, 0);
            f.b3(this.f19616f0, 0L);
            this.P = false;
            this.f19612d0 = false;
            O(false);
            N(0);
            Y();
        }
        this.f19617g = i11;
        this.f19619h = i12;
    }

    private void M() {
        long j10 = this.H - (this.f19633o * 60000);
        long currentTimeMillis = System.currentTimeMillis();
        this.B = currentTimeMillis;
        this.C = 0L;
        LocalLog.a("SmartChargeProtectionController", "handle handleLongChargeProtectionIn---judge if enter LongChargeProtection: mCurrentLongChargePredictDuration = " + this.H + ", mReserveTimeForChargeFullMinute = " + this.f19633o + ", autoExitDuartionMills = " + j10 + ", mSmartChargeProtectionSwitchStatus = " + this.f19613e + ", mIsInNightChargeProtectionState = " + this.Q);
        NotifyUtil v7 = NotifyUtil.v(this.f19616f0);
        int i10 = this.f19613e;
        if (i10 != 1 || this.Q) {
            if (i10 != 1) {
                k0(currentTimeMillis, v7);
            }
        } else if (j10 > 0) {
            v7.s();
            v7.t();
            this.P = true;
            O(true);
            ChargeProtectionUtils.l(this.f19616f0, j10);
            long j11 = currentTimeMillis + j10;
            long j12 = this.H + currentTimeMillis;
            v7.B();
            f.c3(this.f19616f0, 1);
            f.b3(this.f19616f0, j12);
            if (this.O != j12) {
                Context context = this.f19616f0;
                f.h3(context, f.M0(context) + 1);
            }
            this.N = j11;
            this.O = j12;
            LocalLog.a("SmartChargeProtectionController", "handle handleLongChargeProtectionIn---enter LongChargeProtection");
        } else {
            LocalLog.a("SmartChargeProtectionController", "handle handleLongChargeProtectionIn---not enter LongChargeProtection");
        }
        this.f19622i0.I0(ChargeUtil.y(currentTimeMillis), Integer.toString(this.f19631n), Long.toString(this.f19644z), Integer.toString(this.f19635q), ChargeUtil.y(currentTimeMillis + this.H), Long.toString(this.f19633o * 60000));
        int K0 = f.K0(this.f19616f0) + 1;
        int L0 = f.L0(this.f19616f0);
        float f10 = K0;
        f.f3(this.f19616f0, K0);
        this.f19622i0.y0(Integer.toString(K0), Integer.toString(L0), Float.toString((f10 - L0) / f10));
        if (j10 > 0) {
            int S0 = f.S0(this.f19616f0);
            this.f19622i0.G0(S0 != 0 ? S0 != 1 ? "" : "on" : "off");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void O(boolean z10) {
        LocalLog.a("SmartChargeProtectionController", "handleStopCharging: isStopCharging = " + z10);
        f.b2(this.f19616f0, this.f19625k, z10);
    }

    private boolean P() {
        long e10 = ChargeProtectionUtils.e(this.f19616f0);
        this.S = (e10 <= this.Z && e10 > this.Y) || (e10 <= this.O && e10 > this.N);
        LocalLog.a("SmartChargeProtectionController", "isInLastCloseProtectInDuration: lastCloseProtectOnNotify = " + e10 + ", mIsLastCloseProtectInDuration = " + this.S);
        return this.S;
    }

    private boolean Q() {
        int i10;
        if (LocalLog.f()) {
            LocalLog.a("SmartChargeProtectionController", "isSupportSmartSlowCharge: mChargeWattage = " + this.f19627l + ", mBatteryPlugType = " + this.f19625k);
        }
        return (this.f19627l < ChargeUtil.f11385z || (i10 = this.f19625k) == 4 || i10 == 0) ? false : true;
    }

    private void T() {
        this.f19616f0.getContentResolver().registerContentObserver(Settings.System.getUriFor("regular_charge_protection_switch_state"), false, this.f19630m0, 0);
    }

    private void U() {
        this.f19616f0.getContentResolver().registerContentObserver(Settings.System.getUriFor("smart_charge_protection_switch_state"), false, this.f19628l0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void V() {
        this.D = 0.0f;
        this.E = 0.0f;
        this.F = 0.0f;
        this.G = 0.0f;
        this.H = 0L;
        this.I = 0.0f;
        this.J = 0.0f;
        this.K = 0.0f;
        this.L = 0.0f;
        this.M = 0L;
    }

    private void X() {
        this.Y = 0L;
        this.Z = 0L;
        this.f19610b0 = 0L;
        this.f19611c0 = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Y() {
        V();
        X();
    }

    private void b0() {
        HashMap hashMap = new HashMap();
        hashMap.put(LongTermChargingConstants.ENTER_TIME, String.valueOf(this.f19638t));
        hashMap.put("duration", String.valueOf((this.f19640v / 1000) / 60));
        OplusTrack.onCommon(this.f19616f0, "long_term_charging", "long_term_charging", hashMap, 2);
        LocalLog.a("SmartChargeProtectionController", "sendAiData: enter_time = " + this.f19638t + ", duration = " + this.f19640v);
        int Q0 = f.Q0(this.f19616f0) + 1;
        int M0 = f.M0(this.f19616f0);
        f.m3(this.f19616f0, Q0);
        UploadDataUtil.S0(this.f19616f0).F0(String.valueOf(Q0), String.valueOf(M0));
    }

    private void j0() {
        LocalLog.a("SmartChargeProtectionController", "triggerGetAiData: mBatteryPlugType = " + this.f19625k + ", mLastBatteryPlugType = " + this.f19619h + ", mBatteryLevel = " + this.f19621i);
        int i10 = this.f19625k;
        if (i10 != 0 && i10 != this.f19619h) {
            if (this.f19621i < 80) {
                LocalLog.a("SmartChargeProtectionController", "triggerGetAiData: plug ->do not exceed LongChargeDisCharge level -> set flag");
                return;
            }
            LocalLog.a("SmartChargeProtectionController", "triggerGetAiData: plug -> exceed LongChargeDisCharge level -> get AI long charge data");
            this.f19618g0.sendMessage(Message.obtain(this.f19618g0, 102));
            return;
        }
        if (i10 == 0 || this.f19621i != 80) {
            return;
        }
        LocalLog.a("SmartChargeProtectionController", "triggerGetAiData: plug -> do not exceed LongChargeDisCharge level -> get AI long charge data");
        this.f19618g0.sendMessage(Message.obtain(this.f19618g0, 102));
    }

    private void l0() {
        if (this.f19630m0 != null) {
            this.f19616f0.getContentResolver().unregisterContentObserver(this.f19630m0);
        }
    }

    private void m0() {
        if (this.f19628l0 != null) {
            this.f19616f0.getContentResolver().unregisterContentObserver(this.f19628l0);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00fc, code lost:
    
        if (r6 >= (r16 - f6.ChargeUtil.f11382w)) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void G(int i10, int i11, boolean z10) {
        SleepHabitLabel sleepHabitResult;
        LocalLog.a("SmartChargeProtectionController", "getAIRestData: sleepPercent = " + i10 + ", wakePercent = " + i11);
        X();
        long currentTimeMillis = System.currentTimeMillis();
        try {
            OplusDeepThinkerManager oplusDeepThinkerManager = new OplusDeepThinkerManager(this.f19616f0);
            oplusDeepThinkerManager.setRemote(this.f19626k0.getDeepThinkerBridge());
            sleepHabitResult = oplusDeepThinkerManager.getSleepHabitResult();
            LocalLog.a("SmartChargeProtectionController", "getAIRestData: sleepHabitLabel is " + sleepHabitResult);
        } catch (NumberFormatException unused) {
            LocalLog.b("SmartChargeProtectionController", "getAIRestData: NumberFormatException.");
        }
        if (sleepHabitResult == null) {
            LocalLog.b("SmartChargeProtectionController", "getAIRestData: sleepHabitLabel is null.");
            return;
        }
        List<SleepHabitCluster> sleepHabitClusters = sleepHabitResult.getSleepHabitClusters();
        if (sleepHabitClusters != null && !sleepHabitClusters.isEmpty()) {
            LocalLog.a("SmartChargeProtectionController", "getAIRestData: listCluster.size=" + sleepHabitClusters.size());
            for (int i12 = 0; i12 < sleepHabitClusters.size(); i12++) {
                SleepHabitCluster sleepHabitCluster = sleepHabitClusters.get(i12);
                Double d10 = sleepHabitCluster.getSleepTimePercentiles().get(Integer.valueOf(i10));
                Double d11 = sleepHabitCluster.getWakeTimePercentiles().get(Integer.valueOf(i11));
                if (d10 != null && d11 != null) {
                    double doubleValue = d10.doubleValue();
                    double doubleValue2 = d11.doubleValue();
                    LocalLog.a("SmartChargeProtectionController", "getAIRestData: startTimeDouble=" + doubleValue + ", endTimeDouble=" + doubleValue2 + ", i=" + i12);
                    long a10 = ChargeUtil.a(doubleValue, currentTimeMillis);
                    long a11 = ChargeUtil.a(doubleValue2, currentTimeMillis);
                    if (a10 >= 0 && a11 >= 0) {
                        if (doubleValue >= doubleValue2) {
                            if (doubleValue > doubleValue2) {
                                a10 -= TimeInfoUtil.MILLISECOND_OF_A_DAY;
                                if (currentTimeMillis >= a11 - ChargeUtil.f11382w) {
                                    a10 += TimeInfoUtil.MILLISECOND_OF_A_DAY;
                                    a11 += TimeInfoUtil.MILLISECOND_OF_A_DAY;
                                }
                            } else {
                                a11 = currentTimeMillis + TimeInfoUtil.MILLISECOND_OF_A_DAY;
                                a10 = currentTimeMillis;
                            }
                            long j10 = this.Y;
                            if (a10 >= j10 && j10 != 0) {
                                a10 = j10;
                            }
                            this.Y = a10;
                            long j11 = this.Z;
                            if (a11 > j11 || j11 == 0) {
                                j11 = a11;
                            }
                            this.Z = j11;
                            if (!z10) {
                                a10 = this.f19610b0;
                            }
                            this.f19610b0 = a10;
                            if (!z10) {
                                j11 = this.f19611c0;
                            }
                            this.f19611c0 = j11;
                        }
                    }
                    LocalLog.a("SmartChargeProtectionController", "getAIRestData: time error. startTimeDouble=" + doubleValue + ", endTimeDouble=" + doubleValue2);
                }
                LocalLog.b("SmartChargeProtectionController", "getAIRestData error startTime=" + d10 + ", endTime=" + d11);
            }
            LocalLog.a("SmartChargeProtectionController", "getAIRestData: mAiRestStartTime = " + this.Y + ", mAiRestEndTime = " + this.Z);
            return;
        }
        LocalLog.a("SmartChargeProtectionController", "getAIRestData: no listCluster.");
    }

    public long H(int i10) {
        LeaveHomeLabel leaveHomeHabitResult;
        long currentTimeMillis = System.currentTimeMillis();
        int d10 = ChargeUtil.d();
        LocalLog.a("SmartChargeProtectionController", "getAiLeaveHomeData: leaveHomePercent = " + i10 + ", dayOfWeek = " + d10);
        long j10 = 0;
        try {
            OplusDeepThinkerManager oplusDeepThinkerManager = new OplusDeepThinkerManager(this.f19616f0);
            oplusDeepThinkerManager.setRemote(this.f19626k0.getDeepThinkerBridge());
            leaveHomeHabitResult = oplusDeepThinkerManager.getLeaveHomeHabitResult();
            LocalLog.a("SmartChargeProtectionController", "getAiLeaveHomeData: leaveHomeLabel is " + leaveHomeHabitResult);
        } catch (NumberFormatException unused) {
            LocalLog.b("SmartChargeProtectionController", "getAiLeaveHomeData: NumberFormatException.");
        }
        if (leaveHomeHabitResult == null) {
            LocalLog.b("SmartChargeProtectionController", "getAiLeaveHomeData: leaveHomeLabel is null.");
            return 0L;
        }
        List<LeaveHomeCluster> leaveHomeClusters = leaveHomeHabitResult.getLeaveHomeClusters();
        if (leaveHomeClusters != null && !leaveHomeClusters.isEmpty()) {
            LocalLog.a("SmartChargeProtectionController", "getAiLeaveHomeData: listCluster.size=" + leaveHomeClusters.size());
            long j11 = Long.MAX_VALUE;
            for (int i11 = 0; i11 < leaveHomeClusters.size(); i11++) {
                LeaveHomeCluster leaveHomeCluster = leaveHomeClusters.get(i11);
                double doubleValue = leaveHomeCluster.getLeaveHomeTimePercentiles().get(Integer.valueOf(i10)).doubleValue();
                int intValue = leaveHomeCluster.getClusterDayDistribution().containsKey(Integer.valueOf(d10)) ? leaveHomeCluster.getClusterDayDistribution().get(Integer.valueOf(d10)).intValue() : 0;
                LocalLog.a("SmartChargeProtectionController", "getAiLeaveHomeData: leaveHomeTimeDouble=" + doubleValue + ", i=" + i11 + ", value = " + intValue);
                if (intValue < 2) {
                    doubleValue = UserProfileInfo.Constant.NA_LAT_LON;
                }
                long a10 = ChargeUtil.a(doubleValue, currentTimeMillis);
                long abs = Math.abs(a10 - currentTimeMillis);
                if (abs <= j11) {
                    j10 = a10;
                    j11 = abs;
                }
                LocalLog.a("SmartChargeProtectionController", "getAiLeaveHomeData: leaveHomeTimeLong=" + a10);
            }
            return j10;
        }
        LocalLog.b("SmartChargeProtectionController", "getAiLeaveHomeData: no listCluster.");
        return 0L;
    }

    public boolean K() {
        return this.f19612d0;
    }

    public void N(int i10) {
        LocalLog.a("SmartChargeProtectionController", "handleSlowCharge: status = " + i10);
        ChargeUtil.v(this.f19616f0, ChargeUtil.A, ChargeUtil.B, i10);
    }

    public void R(boolean z10) {
        LocalLog.a("SmartChargeProtectionController", "notifyNightChargeProtectionState: isInNightChargeProtection = " + z10);
        this.Q = z10;
        if (z10) {
            NotifyUtil v7 = NotifyUtil.v(this.f19616f0);
            v7.s();
            v7.t();
        }
    }

    public void S() {
        this.f19618g0.sendEmptyMessage(101);
    }

    public void W() {
        if (this.V) {
            NotifyUtil.v(this.f19616f0).g();
            O(false);
            f.I2(this.f19616f0, 0);
        }
    }

    public void Z() {
        if (this.V) {
            NotifyUtil v7 = NotifyUtil.v(this.f19616f0);
            v7.h();
            v7.s();
            v7.t();
            ChargeProtectionUtils.b(this.f19616f0);
            ChargeProtectionUtils.c(this.f19616f0);
            ChargeProtectionUtils.c(this.f19616f0);
            O(false);
            N(0);
            f.l3(this.f19616f0, 0);
            f.k3(this.f19616f0, 0L);
            f.o3(this.f19616f0, 0);
            f.j3(this.f19616f0, 0);
            f.i3(this.f19616f0, 0L);
            f.p2(this.f19616f0, 1);
            f.f3(this.f19616f0, 0);
            f.g3(this.f19616f0, 0);
            f.c3(this.f19616f0, 0);
            f.b3(this.f19616f0, 0L);
            ChargeProtectionUtils.i(this.f19616f0, -1L);
            ChargeProtectionController.N(this.f19616f0).V();
            this.f19612d0 = false;
            this.Q = false;
            this.P = false;
            Y();
            a0();
        }
    }

    public void a0() {
        LocalLog.a("SmartChargeProtectionController", "resetTestMode");
        this.T = false;
        this.U = false;
        this.f19634p = 0;
        this.I = 0.0f;
        this.J = 0.0f;
        this.K = 0.0f;
        this.L = 0.0f;
        this.M = 0L;
        this.f19631n = 0;
        this.f19644z = 0L;
        this.B = 0L;
        this.C = 0L;
        this.X = 0;
        f.l3(this.f19616f0, 0);
        f.k3(this.f19616f0, 0L);
        f.o3(this.f19616f0, 0);
        f.n3(this.f19616f0, System.currentTimeMillis());
        f.j3(this.f19616f0, 0);
        f.i3(this.f19616f0, 0L);
        f.p2(this.f19616f0, 1);
        f.f3(this.f19616f0, 0);
        f.g3(this.f19616f0, 0);
        this.f19610b0 = 0L;
        this.f19611c0 = 0L;
        this.X = 0;
        ChargeUtil.r(-99L, this.f19616f0);
    }

    public void c0(float f10, float f11, float f12, float f13, long j10) {
        LocalLog.a("SmartChargeProtectionController", "setAiDataWhileTestMode:  timeMatch = " + f10 + "; locationMatch = " + f11 + "; fgAppMatch = " + f12 + "; comprehensiveMatch = " + f13 + "; predictDuration = " + j10);
        this.U = true;
        this.I = f10;
        this.J = f11;
        this.K = f12;
        this.L = f13;
        this.M = j10;
    }

    public void d0(float f10, float f11) {
        long e10 = ChargeUtil.e();
        LocalLog.a("SmartChargeProtectionController", "setAiRestDataHourWhileTestMode:  startTime = " + f10 + "; endTime = " + f11 + "; timeStamp = " + e10);
        this.f19610b0 = ((((long) ((int) (f10 * 10.0f))) * 3600000) / 10) + e10;
        this.f19611c0 = e10 + ((((int) (10.0f * f11)) * 3600000) / 10) + ((f10 >= f11 ? 1 : 0) * TimeInfoUtil.MILLISECOND_OF_A_DAY);
    }

    public void e0(int i10) {
        Settings.System.putIntForUser(this.f19616f0.getContentResolver(), "oplus_battery_settings_bms_heat_status", i10, 0);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        int intExtra;
        if (i10 == 204) {
            if (!this.T || (intExtra = this.f19634p) == 0) {
                intExtra = intent.getIntExtra("level", 0);
            }
            int intExtra2 = intent.getIntExtra("status", 0);
            int intExtra3 = intent.getIntExtra("plugged", 0);
            if (this.f19636r != this.f19637s) {
                NotifyUtil v7 = NotifyUtil.v(this.f19616f0);
                e0(this.f19636r);
                if (this.f19636r == 1) {
                    v7.z();
                } else {
                    v7.f();
                }
            }
            this.f19637s = this.f19636r;
            L(intExtra, intExtra2, intExtra3);
            return;
        }
        if (i10 != 207) {
            if (i10 != 229) {
                return;
            }
            intent.getIntExtra("chargertechnology", 0);
            int intExtra4 = intent.getIntExtra("chargewattage", 0);
            this.f19636r = intent.getIntExtra("bms_heating_status", 0);
            if (this.T) {
                intExtra4 = this.X;
            }
            this.f19627l = intExtra4;
            if (intExtra4 >= 65) {
                this.f19633o = ChargeUtil.f11374o;
                return;
            }
            return;
        }
        int f10 = ChargeUtil.f();
        this.f19629m = f10;
        if (f10 == 0) {
            int I0 = f.I0(this.f19616f0);
            int n02 = f.n0(this.f19616f0);
            this.f19622i0.H0(String.valueOf(I0));
            this.f19622i0.r0(String.valueOf(n02));
            ChargeProtectionUtils.n(this.f19616f0);
        }
        LocalLog.a("SmartChargeProtectionController", "mCurrentHourTime: " + this.f19629m);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    public void f0(int i10) {
        LocalLog.a("SmartChargeProtectionController", "setChargeWattageWhileTestMode:  chargeWattage = " + i10);
        this.X = i10;
        this.f19627l = i10;
    }

    public void g0(int i10) {
        LocalLog.a("SmartChargeProtectionController", "setLevelWhileTestMode:  level = " + i10);
        this.f19634p = i10;
    }

    public void h0() {
        LocalLog.a("SmartChargeProtectionController", "setTestMode");
        this.T = true;
    }

    public void i0() {
        this.f19618g0.sendEmptyMessage(100);
    }

    public void k0(long j10, NotifyUtil notifyUtil) {
        int O0 = f.O0(this.f19616f0);
        int P = f.P(this.f19616f0);
        long N0 = j10 - f.N0(this.f19616f0);
        LocalLog.a("SmartChargeProtectionController", "notifyLongChargeProtectionGuideOpen: guideOpenTimes = " + O0 + ", guideOpenPopUp = " + P + ", internalTime = " + N0);
        if (P != 1 || O0 >= 3 || N0 <= 172800000) {
            return;
        }
        notifyUtil.P();
        f.i3(this.f19616f0, j10);
        f.j3(this.f19616f0, O0 + 1);
    }

    public void n0() {
        Affair.f().i(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().i(this, 229);
        Affair.f().i(this, EventType.SCENE_MODE_READING);
        this.f19616f0.unregisterReceiver(this.f19632n0);
        m0();
        l0();
        this.W = false;
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().g(this, 229);
        Affair.f().g(this, EventType.SCENE_MODE_READING);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("oplus.intent.action.smartchargeprotection.open");
        intentFilter.addAction("oplus.intent.action.smartchargeprotection.close");
        intentFilter.addAction("oplus.intent.action.smartchargeprotection.end");
        intentFilter.addAction("oplus.intent.action.longchargeprotection.end.auto");
        intentFilter.addAction("oplus.intent.action.longchargeprotection.forget.one.month");
        intentFilter.addAction("oplus.intent.action.smartchargeprotection.notification");
        intentFilter.addAction("oplus.intent.action.longchargeprotection.guide.open.switch");
        intentFilter.addAction("oplus.intent.action.slowchargeprotection.get.ai.data");
        this.f19616f0.registerReceiver(this.f19632n0, intentFilter, "oplus.permission.OPLUS_COMPONENT_SAFE", null, 2);
        U();
        T();
        if (f.R0(this.f19616f0) == 0) {
            f.n3(this.f19616f0, System.currentTimeMillis());
        }
        this.W = true;
    }
}
