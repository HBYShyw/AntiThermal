package x7;

import a6.UserSleepTimeModel;
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
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.Toast;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.battery.R;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import d6.ConfigUpdateUtil;
import f6.ChargeUtil;
import f6.CommonUtil;
import f6.f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: ChargeProtectionController.java */
/* renamed from: x7.c, reason: use source file name */
/* loaded from: classes.dex */
public class ChargeProtectionController implements IAffairCallback {
    private static volatile ChargeProtectionController K;
    private boolean B;
    private long C;
    private long D;
    private boolean F;
    private ContentObserver I;

    /* renamed from: f, reason: collision with root package name */
    private Handler f19583f;

    /* renamed from: g, reason: collision with root package name */
    private PowerManager.WakeLock f19584g;

    /* renamed from: h, reason: collision with root package name */
    private NotifyUtil f19585h;

    /* renamed from: i, reason: collision with root package name */
    private UploadDataUtil f19586i;

    /* renamed from: j, reason: collision with root package name */
    private UserSleepTimeModel f19587j;

    /* renamed from: k, reason: collision with root package name */
    private ConfigUpdateUtil f19588k;

    /* renamed from: v, reason: collision with root package name */
    private long f19599v;

    /* renamed from: w, reason: collision with root package name */
    private long f19600w;

    /* renamed from: x, reason: collision with root package name */
    private long f19601x;

    /* renamed from: y, reason: collision with root package name */
    private long f19602y;

    /* renamed from: z, reason: collision with root package name */
    private long f19603z;

    /* renamed from: l, reason: collision with root package name */
    private int f19589l = 0;

    /* renamed from: m, reason: collision with root package name */
    private int f19590m = 0;

    /* renamed from: n, reason: collision with root package name */
    private int f19591n = 1;

    /* renamed from: o, reason: collision with root package name */
    private boolean f19592o = false;

    /* renamed from: p, reason: collision with root package name */
    private int f19593p = 0;

    /* renamed from: q, reason: collision with root package name */
    private int f19594q = 0;

    /* renamed from: r, reason: collision with root package name */
    private int f19595r = 1;

    /* renamed from: s, reason: collision with root package name */
    private boolean f19596s = false;

    /* renamed from: t, reason: collision with root package name */
    private boolean f19597t = false;

    /* renamed from: u, reason: collision with root package name */
    private long f19598u = -1;
    private long A = 0;
    private long E = -1;
    private boolean G = false;
    private Object H = new Object();
    private BroadcastReceiver J = new b();

    /* renamed from: e, reason: collision with root package name */
    private Context f19582e = GuardElfContext.e().c();

    /* compiled from: ChargeProtectionController.java */
    /* renamed from: x7.c$a */
    /* loaded from: classes.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            ChargeProtectionController.this.f19583f.sendMessage(Message.obtain(ChargeProtectionController.this.f19583f, 7));
        }
    }

    /* compiled from: ChargeProtectionController.java */
    /* renamed from: x7.c$b */
    /* loaded from: classes.dex */
    class b extends BroadcastReceiver {
        b() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LocalLog.a("ChargeProtectionController", "onReceiver: " + action);
            ChargeProtectionController.this.f19584g.acquire(2000L);
            if ("oplus.intent.action.chargeprotection.start".equals(action)) {
                ChargeProtectionController.this.f19583f.sendMessage(Message.obtain(ChargeProtectionController.this.f19583f, 2));
                return;
            }
            if ("oplus.intent.action.chargeprotection.end".equals(action)) {
                ChargeProtectionController.this.f19583f.sendMessage(Message.obtain(ChargeProtectionController.this.f19583f, 3));
                return;
            }
            if ("oplus.intent.action.smartchargeprotection.end".equals(action)) {
                ChargeProtectionController.this.f19583f.sendMessage(Message.obtain(ChargeProtectionController.this.f19583f, 6));
                return;
            }
            if ("android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                ChargeProtectionController.this.f19583f.sendMessage(Message.obtain(ChargeProtectionController.this.f19583f, 9));
            } else if ("android.intent.action.TIME_SET".equals(action)) {
                ChargeProtectionController.this.f19583f.sendMessage(Message.obtain(ChargeProtectionController.this.f19583f, 10));
            } else if ("android.intent.action.DATE_CHANGED".equals(action)) {
                ChargeProtectionController.this.f19583f.sendMessage(Message.obtain(ChargeProtectionController.this.f19583f, 11));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ChargeProtectionController.java */
    /* renamed from: x7.c$c */
    /* loaded from: classes.dex */
    public class c implements Comparator<Long> {
        c() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(Long l10, Long l11) {
            return l10.compareTo(l11);
        }
    }

    /* compiled from: ChargeProtectionController.java */
    /* renamed from: x7.c$d */
    /* loaded from: classes.dex */
    private class d extends Handler {
        public d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            synchronized (ChargeProtectionController.this.H) {
                switch (message.what) {
                    case 0:
                        ChargeProtectionController.this.registerAction();
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction("oplus.intent.action.chargeprotection.start");
                        intentFilter.addAction("oplus.intent.action.chargeprotection.end");
                        intentFilter.addAction("oplus.intent.action.smartchargeprotection.end");
                        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
                        intentFilter.addAction("android.intent.action.TIME_SET");
                        intentFilter.addAction("android.intent.action.DATE_CHANGED");
                        ChargeProtectionController.this.f19582e.registerReceiver(ChargeProtectionController.this.J, intentFilter, 2);
                        ChargeProtectionController.this.f19582e.getContentResolver().registerContentObserver(Settings.System.getUriFor((AppFeature.q() && ChargeProtectionController.this.f19582e.getUserId() == 0) ? "smart_long_charge_protection_switch_state" : "charge_protection_switch_state"), false, ChargeProtectionController.this.I, 0);
                        f.c2(ChargeProtectionController.this.f19582e, false);
                        CanlendarCollector.g().i(ChargeProtectionController.this.f19582e);
                        AlarmCollector.e().g(ChargeProtectionController.this.f19582e);
                        ChargeProtectionController.this.F = true;
                        break;
                    case 1:
                        ChargeProtectionController.this.f19582e.unregisterReceiver(ChargeProtectionController.this.J);
                        ChargeProtectionController.this.f19582e.getContentResolver().unregisterContentObserver(ChargeProtectionController.this.I);
                        ChargeProtectionController.this.Y();
                        CanlendarCollector.g().c();
                        AlarmCollector.e().b();
                        break;
                    case 2:
                        LocalLog.a("ChargeProtectionController", "handle broadcast: MSG_HANDLER_CHARGE_PROTECTION_STARToplus.intent.action.chargeprotection.start");
                        boolean z10 = f.I0(ChargeProtectionController.this.f19582e) == 1;
                        ChargeProtectionController.this.T();
                        ChargeProtectionController.this.U();
                        if (z10) {
                            if (ChargeProtectionController.this.f19592o && ChargeProtectionController.this.f19596s && ChargeProtectionController.this.f19593p < 100) {
                                ChargeProtectionController.this.f19597t = true;
                                ChargeProtectionController chargeProtectionController = ChargeProtectionController.this;
                                chargeProtectionController.C = chargeProtectionController.f19599v;
                                ChargeProtectionController chargeProtectionController2 = ChargeProtectionController.this;
                                chargeProtectionController2.D = chargeProtectionController2.f19600w;
                                ChargeProtectionController.this.f19585h.B();
                                f.c3(ChargeProtectionController.this.f19582e, 1);
                                f.b3(ChargeProtectionController.this.f19582e, ChargeProtectionController.this.f19600w);
                                if (ChargeProtectionController.this.f19598u - ChargeProtectionController.this.A > 43200000) {
                                    ChargeProtectionController chargeProtectionController3 = ChargeProtectionController.this;
                                    chargeProtectionController3.A = chargeProtectionController3.f19598u;
                                    ChargeProtectionController.this.f19586i.r("ExecuteChargeProtection with Broadcast", ChargeProtectionController.this.f19598u, ChargeProtectionController.this.f19599v, ChargeProtectionController.this.f19603z);
                                }
                                ChargeProtectionController.this.f19586i.q("ExecuteChargeProtection with Broadcast", ChargeProtectionController.this.f19598u, ChargeProtectionController.this.f19599v, ChargeProtectionController.this.f19603z);
                                if (ChargeProtectionController.this.f19593p >= 80) {
                                    ChargeProtectionUtils.j(ChargeProtectionController.this.f19582e, false, ChargeProtectionController.this.f19603z);
                                    ChargeProtectionController.this.W(true, 10L);
                                    break;
                                }
                            }
                        } else {
                            SmartChargeProtectionController.J(ChargeProtectionController.this.f19582e).k0(System.currentTimeMillis(), ChargeProtectionController.this.f19585h);
                            break;
                        }
                        break;
                    case 3:
                        LocalLog.a("ChargeProtectionController", "handle broadcast: MSG_HANDLER_CHARGE_PROTECTION_ENDoplus.intent.action.chargeprotection.end");
                        ChargeProtectionController.this.W(false, 0L);
                        break;
                    case 5:
                        ChargeProtectionController.this.f19583f.removeMessages(5);
                        ChargeProtectionController.this.U();
                        boolean z11 = f.I0(ChargeProtectionController.this.f19582e) == 1;
                        if (!ChargeProtectionController.this.f19592o && ChargeProtectionController.this.f19596s && !ChargeProtectionController.this.f19597t) {
                            LocalLog.a("ChargeProtectionController", "handle battery broadcast, meet Case 1");
                            if (ChargeProtectionController.this.F && !ChargeProtectionController.this.G) {
                                ChargeProtectionController.this.G = true;
                            }
                            ChargeProtectionController.this.L();
                        }
                        if (!z11) {
                            return;
                        }
                        if (ChargeProtectionController.this.f19592o && ChargeProtectionController.this.f19596s && ChargeProtectionController.this.f19593p == 80 && ChargeProtectionController.this.f19589l == 79 && ChargeProtectionController.this.f19597t) {
                            LocalLog.a("ChargeProtectionController", "handle battery broadcast, meet Case 2");
                            ChargeProtectionController.this.W(true, 10L);
                            ChargeProtectionUtils.j(ChargeProtectionController.this.f19582e, false, ChargeProtectionController.this.f19603z);
                        }
                        if (ChargeProtectionController.this.F && ChargeProtectionController.this.f19596s && !ChargeProtectionController.this.f19597t && !ChargeProtectionController.this.G) {
                            ChargeProtectionController.this.G = true;
                            LocalLog.a("ChargeProtectionController", "handle battery broadcast, meet Case 3");
                            ChargeProtectionController.this.L();
                        }
                        if (ChargeProtectionController.this.f19597t && ChargeProtectionController.this.f19589l == 71 && ChargeProtectionController.this.f19593p == 70) {
                            LocalLog.a("ChargeProtectionController", "handle battery broadcast, meet Case 4");
                            ChargeProtectionController.this.W(false, 10L);
                        }
                        if (!ChargeProtectionController.this.f19596s && ChargeProtectionController.this.f19597t) {
                            LocalLog.a("ChargeProtectionController", "handle battery broadcast, meet Case 5");
                            ChargeProtectionController.this.V();
                            ChargeProtectionController.this.f19586i.s("Unpluging_battery_not_full", ChargeProtectionController.this.C, ChargeProtectionController.this.f19598u, ChargeProtectionController.this.f19599v, ChargeProtectionController.this.f19603z);
                        }
                        if (ChargeProtectionController.this.f19595r == 5 && ChargeProtectionController.this.f19592o && ChargeProtectionController.this.f19596s && ChargeProtectionController.this.f19597t) {
                            LocalLog.a("ChargeProtectionController", "handle battery broadcast, meet Case 6");
                            ChargeProtectionController.this.f19597t = false;
                            ChargeProtectionController.this.f19585h.h();
                            ChargeProtectionUtils.d(ChargeProtectionController.this.f19582e, false);
                            ChargeProtectionUtils.d(ChargeProtectionController.this.f19582e, true);
                            ChargeProtectionController.this.f19586i.s("exit_when_battery_full", ChargeProtectionController.this.C, ChargeProtectionController.this.f19598u, ChargeProtectionController.this.f19599v, ChargeProtectionController.this.f19603z);
                        }
                        if (ChargeProtectionController.this.f19592o && !ChargeProtectionController.this.f19596s && ChargeProtectionController.this.f19591n == 5) {
                            LocalLog.a("ChargeProtectionController", "handle battery broadcast, meet Case 7");
                            ChargeProtectionController.this.f19585h.h();
                            ChargeProtectionController.this.f19586i.s("unplug_when_battery_full", ChargeProtectionController.this.C, ChargeProtectionController.this.f19598u, ChargeProtectionController.this.f19599v, ChargeProtectionController.this.f19603z);
                            break;
                        }
                        break;
                    case 6:
                        long currentTimeMillis = System.currentTimeMillis();
                        int P0 = f.P0(ChargeProtectionController.this.f19582e) + 1;
                        LocalLog.a("ChargeProtectionController", "handle broadcast: MSG_HANDLER_CHARGE_PROTECTION_END_INNOTIFYoplus.intent.action.smartchargeprotection.end, times = " + P0);
                        ChargeProtectionController.this.V();
                        ChargeProtectionUtils.i(ChargeProtectionController.this.f19582e, System.currentTimeMillis());
                        if (ChargeProtectionController.this.f19597t) {
                            Toast.makeText(ChargeProtectionController.this.f19582e, ChargeProtectionController.this.f19582e.getString(R.string.charge_protection_toast), 0).show();
                            ChargeProtectionController.this.f19586i.o(ChargeProtectionController.this.C, currentTimeMillis, ChargeProtectionController.this.f19599v, ChargeProtectionController.this.f19603z);
                            ChargeProtectionController.this.f19586i.s("exit charge protection on notify", ChargeProtectionController.this.C, currentTimeMillis, ChargeProtectionController.this.f19599v, ChargeProtectionController.this.f19603z);
                            f.l3(ChargeProtectionController.this.f19582e, P0);
                            if (P0 % ChargeUtil.f11368i == 0) {
                                NotifyUtil.v(ChargeProtectionController.this.f19582e).O();
                                break;
                            }
                        }
                        break;
                    case 7:
                        ChargeProtectionController.this.R(f.I0(ChargeProtectionController.this.f19582e) == 1);
                        break;
                    case 8:
                        boolean z12 = message.getData().getBoolean("nodeVal");
                        SmartChargeProtectionController.J(ChargeProtectionController.this.f19582e).R(z12);
                        if ((z12 && ChargeProtectionController.this.f19598u < ChargeProtectionController.this.f19603z) || !z12) {
                            f.b2(ChargeProtectionController.this.f19582e, ChargeProtectionController.this.f19594q, z12);
                            break;
                        }
                        break;
                    case 9:
                        LocalLog.a("ChargeProtectionController", "mProtectStartTimeToRecord = " + ChargeProtectionUtils.h(ChargeProtectionController.this.C) + ", mProtectEndTimeToRecord = " + ChargeProtectionUtils.h(ChargeProtectionController.this.D));
                        if (ChargeProtectionController.this.f19596s && ChargeProtectionController.this.f19597t) {
                            LocalLog.a("ChargeProtectionController", "handle MSG_HANDLER_TIMEZONE_CHANGED");
                            ChargeProtectionController.this.V();
                            break;
                        }
                        break;
                    case 10:
                        ChargeProtectionController.this.T();
                        long currentTimeMillis2 = System.currentTimeMillis();
                        LocalLog.a("ChargeProtectionController", "mProtectStartTimeToRecord = " + ChargeProtectionUtils.h(ChargeProtectionController.this.C) + ", mProtectEndTimeToRecord = " + ChargeProtectionUtils.h(ChargeProtectionController.this.D) + ", nowRtc = " + ChargeProtectionUtils.h(currentTimeMillis2));
                        if (ChargeProtectionController.this.f19592o && ChargeProtectionController.this.f19596s && ((ChargeProtectionController.this.C > currentTimeMillis2 || currentTimeMillis2 >= ChargeProtectionController.this.D) && ChargeProtectionController.this.f19597t)) {
                            LocalLog.a("ChargeProtectionController", "handle MSG_HANDLER_TIME_CHANGED");
                            ChargeProtectionController.this.V();
                            break;
                        }
                        break;
                    case 11:
                        LocalLog.a("ChargeProtectionController", "handle message:11");
                        ChargeProtectionController.this.T();
                        ChargeProtectionController.this.U();
                        break;
                }
            }
        }
    }

    private ChargeProtectionController(Context context) {
        this.I = new a(this.f19583f);
        HandlerThread handlerThread = new HandlerThread("charge_protection");
        handlerThread.start();
        this.f19583f = new d(handlerThread.getLooper());
        this.f19584g = ((PowerManager) this.f19582e.getSystemService("power")).newWakeLock(1, "ChargeProtection:controller");
        this.f19585h = NotifyUtil.v(this.f19582e);
        this.f19586i = UploadDataUtil.S0(this.f19582e);
        this.f19588k = ConfigUpdateUtil.n(this.f19582e);
        this.f19587j = UserSleepTimeModel.j(this.f19582e);
    }

    private boolean K(int i10) {
        return (i10 == 4 || i10 == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean L() {
        if (!SmartChargeProtectionController.J(this.f19582e).K()) {
            this.f19585h.h();
        }
        f.c3(this.f19582e, 0);
        f.b3(this.f19582e, 0L);
        ChargeProtectionUtils.d(this.f19582e, false);
        ChargeProtectionUtils.d(this.f19582e, true);
        this.f19597t = false;
        long j10 = this.f19598u;
        long j11 = this.f19603z;
        if (j10 <= j11 && !this.B) {
            long j12 = this.f19601x;
            if (0 < j12 && j12 <= 3600000 && this.f19593p < 100) {
                ChargeProtectionUtils.j(this.f19582e, true, this.f19599v);
                return true;
            }
            if (this.f19599v <= j10 && j10 < j11 && this.f19602y <= 43800000 && this.f19593p < 100) {
                boolean z10 = f.I0(this.f19582e) == 1;
                T();
                if (z10) {
                    this.f19597t = true;
                    this.C = this.f19599v;
                    this.D = this.f19600w;
                    this.f19585h.B();
                    f.c3(this.f19582e, 1);
                    f.b3(this.f19582e, this.f19600w);
                    long j13 = this.f19598u;
                    if (j13 - this.A > 43200000) {
                        this.A = j13;
                        this.f19586i.r("ExecuteChargeProtection", j13, this.f19599v, this.f19603z);
                    }
                    this.f19586i.q("ExecuteChargeProtection with matching condition", this.f19598u, this.f19599v, this.f19603z);
                    ChargeProtectionUtils.j(this.f19582e, false, this.f19603z);
                    int i10 = this.f19593p;
                    if (i10 >= 80 && i10 < 100) {
                        W(true, 10L);
                    }
                    return true;
                }
                SmartChargeProtectionController.J(this.f19582e).k0(System.currentTimeMillis(), this.f19585h);
            }
        }
        return false;
    }

    private void M() {
        int s7 = CommonUtil.s();
        synchronized (this.H) {
            boolean z10 = true;
            if (f.I0(this.f19582e) != 1) {
                z10 = false;
            }
            this.f19586i.p(z10, s7);
        }
    }

    public static ChargeProtectionController N(Context context) {
        if (K == null) {
            synchronized (ChargeProtectionController.class) {
                if (K == null) {
                    K = new ChargeProtectionController(context);
                }
            }
        }
        return K;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void R(boolean z10) {
        if (!z10) {
            V();
            this.f19586i.s("exit_when_UI_Switch_off", this.C, this.f19598u, this.f19599v, this.f19603z);
        } else {
            if (!this.f19596s || this.f19597t) {
                return;
            }
            U();
            L();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void T() {
        ChargeProtectionUtils.k();
        this.f19600w = P();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void U() {
        this.f19598u = System.currentTimeMillis();
        boolean z10 = false;
        this.f19587j.o(false);
        this.f19599v = O();
        long P = P();
        this.f19600w = P;
        long j10 = this.f19599v;
        long j11 = this.f19598u;
        this.f19601x = j10 - j11;
        this.f19602y = P - j11;
        this.f19603z = P - ConfigUpdateUtil.n(this.f19582e).y();
        long e10 = ChargeProtectionUtils.e(this.f19582e);
        if (this.f19599v < e10 && e10 <= this.f19600w) {
            z10 = true;
        }
        this.B = z10;
        if (LocalLog.g()) {
            LocalLog.a("ChargeProtectionController", "refreshSystemCurrentStateInfo nowRtc =  " + ChargeProtectionUtils.h(this.f19598u) + "\n, protectStartTime = " + ChargeProtectionUtils.h(this.f19599v) + ", protectEndTime = " + ChargeProtectionUtils.h(this.f19600w) + ", nowCloseToStartInterval = " + this.f19601x + "\n, nowCloseToEndInterval = " + this.f19602y + ", realStopProtectTime = " + ChargeProtectionUtils.h(this.f19603z) + ", lastCloseProtectOnNotify = " + ChargeProtectionUtils.h(e10) + "\n, isLastCloseProtectInDuration = " + this.B + ", mChargeProtecitonTonight = " + this.f19597t + ", mBatLevelNew =" + this.f19593p + ", mBatLevel =" + this.f19589l + ", mStatusNew =" + this.f19595r + ", mStatus =" + this.f19591n + ", mPlugTypeNew =" + this.f19594q + ", mPlugType =" + this.f19590m);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void W(boolean z10, long j10) {
        Message obtainMessage = this.f19583f.obtainMessage();
        obtainMessage.what = 8;
        Bundle bundle = new Bundle();
        bundle.putBoolean("nodeVal", z10);
        obtainMessage.setData(bundle);
        this.f19583f.sendMessageDelayed(obtainMessage, j10);
        this.E = SystemClock.elapsedRealtime();
    }

    public long O() {
        return this.f19587j.l();
    }

    public long P() {
        long f10 = ChargeProtectionUtils.f(this.f19582e, this.f19598u);
        long g6 = ChargeProtectionUtils.g(this.f19582e, this.f19598u);
        long k10 = this.f19587j.k();
        LocalLog.a("ChargeProtectionController", "mFirstAlarmTime= " + ChargeProtectionUtils.h(f10) + ", mFirstScheduleTime= " + ChargeProtectionUtils.h(g6) + ", mPredictedSleepEndTime=" + ChargeProtectionUtils.h(k10));
        long min = Math.min(Math.min(f10, g6), k10);
        if (min != 0) {
            return min;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(Long.valueOf(f10));
        arrayList.add(Long.valueOf(g6));
        arrayList.add(Long.valueOf(k10));
        Collections.sort(arrayList, new c());
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            long longValue = ((Long) arrayList.get(i10)).longValue();
            if (this.f19598u < longValue) {
                return longValue;
            }
        }
        return min;
    }

    public void Q(int i10, int i11, int i12) {
        synchronized (this.H) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (this.F && elapsedRealtime - this.E > 1000) {
                this.f19589l = this.f19593p;
                this.f19590m = this.f19594q;
                this.f19591n = this.f19595r;
                this.f19593p = i10;
                this.f19594q = i12;
                this.f19595r = i11;
                boolean z10 = true;
                this.f19596s = (i12 & 15) != 0 && K(i12);
                int i13 = this.f19590m;
                if ((i13 & 15) == 0 || !K(i13)) {
                    z10 = false;
                }
                this.f19592o = z10;
                if (f.v(this.f19582e)) {
                    this.f19583f.sendMessage(Message.obtain(this.f19583f, 5));
                } else if (this.f19583f.hasMessages(5)) {
                    this.f19583f.removeMessages(5);
                }
            }
        }
    }

    public void S() {
        this.f19583f.sendMessage(Message.obtain(this.f19583f, 1));
    }

    public void V() {
        this.f19585h.h();
        synchronized (this.H) {
            if (this.F) {
                this.f19597t = false;
                this.C = 0L;
                this.D = 0L;
                ChargeProtectionUtils.d(this.f19582e, false);
                ChargeProtectionUtils.d(this.f19582e, true);
                ChargeProtectionUtils.i(this.f19582e, -1L);
                W(false, 10L);
                f.c3(this.f19582e, 0);
                f.b3(this.f19582e, 0L);
            }
        }
    }

    public void X() {
        this.f19583f.sendMessageDelayed(Message.obtain(this.f19583f, 0), 0L);
    }

    public void Y() {
        Affair.f().i(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().i(this, EventType.SCENE_MODE_READING);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 == 204) {
            Q(intent.getIntExtra("level", 0), intent.getIntExtra("status", 0), intent.getIntExtra("plugged", 0));
        } else {
            if (i10 != 207) {
                return;
            }
            M();
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().g(this, EventType.SCENE_MODE_READING);
    }
}
