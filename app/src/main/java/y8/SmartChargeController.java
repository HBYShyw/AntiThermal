package y8;

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
import android.os.UserHandle;
import android.provider.Settings;
import android.widget.Toast;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.powermanager.fuelgaue.SmartChargeActivity;
import com.oplus.sceneservice.sdk.dataprovider.api.DataAbilityApi;
import com.oplus.sceneservice.sdk.dataprovider.bean.SceneStatusInfo;
import com.oplus.thermalcontrol.ThermalControlUtils;
import f6.ChargeUtil;
import f6.CommonUtil;
import f6.f;
import java.util.HashMap;
import java.util.Map;
import w4.Affair;
import w4.IAffairCallback;
import x5.UploadDataUtil;
import x7.SmartChargeProtectionController;
import y5.AppFeature;

/* compiled from: SmartChargeController.java */
/* renamed from: y8.b, reason: use source file name */
/* loaded from: classes2.dex */
public class SmartChargeController implements IAffairCallback {
    private static volatile SmartChargeController M;
    private Context G;
    private Handler H;
    private PowerManager.WakeLock I;
    private UploadDataUtil J;

    /* renamed from: f, reason: collision with root package name */
    private boolean f19908f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f19909g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f19910h;

    /* renamed from: v, reason: collision with root package name */
    private int f19924v;

    /* renamed from: e, reason: collision with root package name */
    private volatile boolean f19907e = false;

    /* renamed from: i, reason: collision with root package name */
    private boolean f19911i = false;

    /* renamed from: j, reason: collision with root package name */
    private boolean f19912j = false;

    /* renamed from: k, reason: collision with root package name */
    private int f19913k = 100;

    /* renamed from: l, reason: collision with root package name */
    private int f19914l = -1;

    /* renamed from: m, reason: collision with root package name */
    private int f19915m = 0;

    /* renamed from: n, reason: collision with root package name */
    private int f19916n = 0;

    /* renamed from: o, reason: collision with root package name */
    private int f19917o = 0;

    /* renamed from: p, reason: collision with root package name */
    private int f19918p = 0;

    /* renamed from: q, reason: collision with root package name */
    private int f19919q = 0;

    /* renamed from: r, reason: collision with root package name */
    private int f19920r = 0;

    /* renamed from: s, reason: collision with root package name */
    private int f19921s = 100;

    /* renamed from: t, reason: collision with root package name */
    private int f19922t = 0;

    /* renamed from: u, reason: collision with root package name */
    private int f19923u = 0;

    /* renamed from: w, reason: collision with root package name */
    private int f19925w = 0;

    /* renamed from: x, reason: collision with root package name */
    private int f19926x = 0;

    /* renamed from: y, reason: collision with root package name */
    private int f19927y = -1;

    /* renamed from: z, reason: collision with root package name */
    private int f19928z = -1;
    private int A = 0;
    private long B = 0;
    private long C = 0;
    private long D = 0;
    private long E = 0;
    private long F = 0;
    private ContentObserver K = new a(new Handler());
    private BroadcastReceiver L = new b();

    /* compiled from: SmartChargeController.java */
    /* renamed from: y8.b$a */
    /* loaded from: classes2.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            SmartChargeController smartChargeController = SmartChargeController.this;
            smartChargeController.f19908f = f.J0(smartChargeController.G);
            int i10 = SmartChargeController.this.f19908f ? 104 : 105;
            SmartChargeController.this.H.sendEmptyMessage(i10);
            LocalLog.a("SmartChargeController", "mIsSmartChargeModeOn = " + SmartChargeController.this.f19908f + "; selfChange" + z10 + "; msg: " + i10);
            if (AppFeature.o()) {
                return;
            }
            ChargeUtil.t(3, SmartChargeController.this.f19908f);
        }
    }

    /* compiled from: SmartChargeController.java */
    /* renamed from: y8.b$b */
    /* loaded from: classes2.dex */
    class b extends BroadcastReceiver {
        b() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LocalLog.a("SmartChargeController", "onReceiver: " + action);
            SmartChargeController.this.I.acquire(2000L);
            if ("oplus.intent.action.smartcharge.berunning.nomore".equals(action)) {
                SmartChargeController.this.H.sendMessage(Message.obtain(SmartChargeController.this.H, 110));
            } else if ("oplus.intent.action.smartcharge.berunning.knowmore".equals(action)) {
                SmartChargeController.this.H.sendMessage(Message.obtain(SmartChargeController.this.H, 111));
            }
        }
    }

    /* compiled from: SmartChargeController.java */
    /* renamed from: y8.b$c */
    /* loaded from: classes2.dex */
    private class c extends Handler {
        public c(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 100:
                    SmartChargeController.this.registerAction();
                    if (Settings.System.getIntForUser(SmartChargeController.this.G.getContentResolver(), "smart_charge_switch_state", -1, 0) == -1) {
                        LocalLog.l("SmartChargeController", "set SmartChargeSwitch default value");
                        f.e3(SmartChargeController.this.G, AppFeature.l());
                    }
                    if (!AppFeature.o()) {
                        ChargeUtil.t(3, SmartChargeController.this.f19908f);
                    }
                    if (SmartChargeController.this.f19908f) {
                        SmartChargeAlarmSetter.b(SmartChargeController.this.G).c();
                        return;
                    }
                    return;
                case 101:
                    SmartChargeAlarmSetter.b(SmartChargeController.this.G).a();
                    SmartChargeController.this.O();
                    return;
                case 102:
                    if (LocalLog.f()) {
                        LocalLog.a("SmartChargeController", "MSG_SCREEN_ON");
                    }
                    SmartChargeController.this.f19909g = true;
                    if (SmartChargeController.this.x()) {
                        SmartChargeController.this.s();
                        SmartChargeController.this.M();
                        return;
                    }
                    return;
                case 103:
                    if (LocalLog.f()) {
                        LocalLog.a("SmartChargeController", "MSG_SCREEN_OFF");
                    }
                    SmartChargeController.this.f19909g = false;
                    SmartChargeController.this.A();
                    SmartChargeController.this.f19925w = 1;
                    return;
                case 104:
                    SmartChargeController.this.f19908f = true;
                    if (SmartChargeController.this.f19909g) {
                        SmartChargeController.this.f19925w = 0;
                        SmartChargeController.this.s();
                        SmartChargeController.this.M();
                    }
                    SmartChargeAlarmSetter.b(SmartChargeController.this.G).c();
                    return;
                case 105:
                    SmartChargeController.this.f19908f = false;
                    SmartChargeController.this.f19925w = 0;
                    SmartChargeController.this.H.sendEmptyMessage(107);
                    SmartChargeController.this.C();
                    SmartChargeAlarmSetter.b(SmartChargeController.this.G).a();
                    if (LocalLog.f()) {
                        LocalLog.a("SmartChargeController", "exit SMART_CHARGEF---reason: SMART_CHARGE_SWITCH_OFF");
                        return;
                    }
                    return;
                case 106:
                    SmartChargeController.this.w(3, true);
                    if (LocalLog.f()) {
                        LocalLog.a("SmartChargeController", "handle MSG_SMART_CHARGE_MODE_ENTER---mSmartChargeMode:" + SmartChargeController.this.f19924v);
                        return;
                    }
                    return;
                case 107:
                    SmartChargeController.this.w(3, false);
                    if (LocalLog.f()) {
                        LocalLog.a("SmartChargeController", "handle MSG_SMART_CHARGE_MODE_EXIT---mSmartChargeMode: " + SmartChargeController.this.f19924v);
                        return;
                    }
                    return;
                case 108:
                    SmartChargeController.this.w(3, false);
                    if (LocalLog.f()) {
                        LocalLog.a("SmartChargeController", "handle MSG_SMART_CHARGE_HIGH_LOAD_ENTER---mSmartChargeMode: " + SmartChargeController.this.f19924v);
                        return;
                    }
                    return;
                case 109:
                    if (SmartChargeController.this.f19921s < 80) {
                        SmartChargeController.this.w(3, true);
                        SmartChargeController smartChargeController = SmartChargeController.this;
                        smartChargeController.f19925w = smartChargeController.f19926x;
                        SmartChargeController.this.f19926x = 0;
                    }
                    LocalLog.a("SmartChargeController", "handle MSG_SMART_CHARGE_HIGH_LOAD_EXIT---mSmartChargeMode: " + SmartChargeController.this.f19924v);
                    return;
                case 110:
                    NotifyUtil.v(SmartChargeController.this.G).n();
                    f.W2(SmartChargeController.this.G, ChargeUtil.f11361b);
                    return;
                case 111:
                    NotifyUtil.v(SmartChargeController.this.G).n();
                    CommonUtil.c(SmartChargeController.this.G);
                    Intent intent = new Intent(SmartChargeController.this.G, (Class<?>) SmartChargeActivity.class);
                    intent.addFlags(335544320);
                    SmartChargeController.this.G.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    }

    private SmartChargeController(Context context) {
        this.f19908f = true;
        this.f19909g = false;
        this.f19910h = false;
        this.f19924v = 0;
        this.G = context;
        this.f19909g = ((PowerManager) context.getSystemService("power")).isScreenOn();
        this.f19908f = f.J0(this.G);
        if (y5.b.D() && AppFeature.B() && AppFeature.j() && AppFeature.k() == 1) {
            LocalLog.a("SmartChargeController", "SmartChargeController updateLifeModeTrigger +" + this.f19908f);
            ChargeUtil.B(this.f19908f ^ true);
        }
        this.f19924v = 0;
        this.J = UploadDataUtil.S0(this.G);
        this.f19910h = AppFeature.D();
        this.I = ((PowerManager) this.G.getSystemService("power")).newWakeLock(1, "ChargeProtection:controller");
        HandlerThread handlerThread = new HandlerThread("SmartChargeController");
        handlerThread.start();
        this.H = new c(handlerThread.getLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void A() {
        this.f19925w = 0;
        this.E = 0L;
        if (this.f19924v == 3) {
            this.H.sendEmptyMessage(107);
        }
        if (LocalLog.f()) {
            LocalLog.a("SmartChargeController", "quitSmartCharge: mSmartChargeMode = " + this.f19924v);
        }
    }

    private void B() {
        this.G.getContentResolver().registerContentObserver(Settings.System.getUriFor("smart_charge_switch_state"), false, this.K, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void C() {
        this.f19917o = 0;
        this.f19918p = 0;
        this.f19920r = 0;
        this.f19921s = 0;
        this.f19922t = 0;
        this.f19923u = 0;
        this.f19924v = 0;
        this.f19925w = 0;
        this.A = 0;
        this.B = 0L;
        this.C = 0L;
        this.D = 0L;
        this.E = 0L;
        this.F = 0L;
    }

    private void D() {
        f.Z2(this.G, 0);
        f.Y2(this.G, 0);
        f.a3(this.G, 0);
        f.X2(this.G, 0);
        f.W2(this.G, 0);
        f.V2(this.G, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M() {
        if (LocalLog.f()) {
            LocalLog.a("SmartChargeController", "triggerSmartCharge: mChargeWattage: " + this.f19927y + "; mIsSmartChargeSwitchOn: " + this.f19908f + "; mSmartChargeScene: " + this.f19925w + "; mSmartChargeMode: " + this.f19924v);
        }
        if (x() && this.f19908f) {
            int i10 = this.f19925w;
            if (i10 == 0) {
                if (this.f19909g || this.f19924v == 0) {
                    return;
                }
                this.H.sendEmptyMessage(107);
                LocalLog.a("SmartChargeController", "sendMessage(MSG_SMART_CHARGE_MODE_EXIT): SCENE_NOT_URGENTLY_NEED_POWER");
                return;
            }
            if (i10 == 2) {
                this.H.sendEmptyMessage(106);
                LocalLog.a("SmartChargeController", "sendMessage(MSG_SMART_CHARGE_MODE_ENTER): SCENE_URGENTLY_NEED_POWER_LOW_POWER");
                return;
            }
            if (i10 == 3) {
                this.H.sendEmptyMessage(106);
                LocalLog.a("SmartChargeController", "sendMessage(MSG_SMART_CHARGE_MODE_ENTER): SCENE_URGENTLY_NEED_POWER_OUT_MORING");
                return;
            }
            if (i10 == 4) {
                this.H.sendEmptyMessage(106);
                LocalLog.a("SmartChargeController", "sendMessage(MSG_SMART_CHARGE_MODE_ENTER): SCENE_URGENTLY_NEED_POWER_JOURNEY");
            } else if (i10 == 5) {
                this.H.sendEmptyMessage(108);
                LocalLog.a("SmartChargeController", "sendMessage(MSG_SMART_CHARGE_HIGH_LOAD_ENTER): MSG_SMART_CHARGE_HIGH_LOAD_ENTER");
            } else {
                if (i10 != 6) {
                    return;
                }
                this.H.sendEmptyMessage(109);
                LocalLog.a("SmartChargeController", "sendMessage(MSG_SMART_CHARGE_HIGH_LOAD_EXIT): MSG_SMART_CHARGE_HIGH_LOAD_EXIT");
            }
        }
    }

    private void N() {
        if (this.K != null) {
            this.G.getContentResolver().unregisterContentObserver(this.K);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        if (LocalLog.f()) {
            LocalLog.a("SmartChargeController", "determineSmartChargeScene : mIsTestMode: " + this.f19911i + "; mBatteryLevel: " + this.f19921s + "; mTimeWhileTestMode: " + this.f19914l + "; mJourneyValueWhileTestMode: " + this.f19916n);
        }
        int i10 = this.f19921s;
        if (i10 <= 20) {
            this.f19925w = 2;
            return;
        }
        if (i10 > 40) {
            if (i10 >= 80) {
                int i11 = this.f19925w;
                if (i11 != 2 && i11 != 3 && i11 != 4) {
                    if (this.f19924v == 3) {
                        LocalLog.b("SmartChargeController", "Something is wrong: SmartChargeScene does not match SmartChargeMode---mSmartChargeScene: " + this.f19925w + "---mSmartChargeMode: " + this.f19924v);
                        A();
                        return;
                    }
                    return;
                }
                this.f19925w = 0;
                if (this.f19924v == 3) {
                    A();
                    return;
                }
                return;
            }
            return;
        }
        if (!this.f19910h && this.f19925w == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            int f10 = this.f19911i ? this.f19914l : ChargeUtil.f();
            long j10 = this.E;
            if (j10 == 0) {
                j10 = SmartChargeProtectionController.J(this.G).H(ChargeUtil.D);
            }
            this.E = j10;
            if (j10 == 0 || !ChargeUtil.C) {
                if (f10 >= 6 && f10 < 9) {
                    this.f19925w = 3;
                    return;
                } else {
                    y();
                    return;
                }
            }
            if (currentTimeMillis >= j10 - 5400000 && currentTimeMillis <= j10 + 5400000) {
                this.f19925w = 3;
            } else {
                y();
            }
        }
    }

    public static SmartChargeController t(Context context) {
        if (M == null) {
            synchronized (SmartChargeController.class) {
                if (M == null) {
                    M = new SmartChargeController(context);
                }
            }
        }
        return M;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void v() {
        int i10;
        int i11;
        int i12;
        int i13 = this.f19925w;
        String m10 = ChargeUtil.m();
        if (m10 == null || !m10.contains("+") || m10.equals("0+0")) {
            i10 = 0;
            i11 = 0;
        } else {
            try {
                String str = m10.split("\\+")[0];
                String str2 = m10.split("\\+")[1];
                i11 = !str.contains("-") ? Integer.parseInt(str) : 0;
                try {
                    i10 = !str2.contains("-") ? Integer.parseInt(str2) : 0;
                    try {
                        LocalLog.a("SmartChargeController", "smartChargeIncomeValue: " + i11 + "; smartChargeIncomePercent: " + i10);
                    } catch (NumberFormatException e10) {
                        i12 = i10;
                        e = e10;
                        e.printStackTrace();
                        i10 = i12;
                        int max = Math.max(f.z0(this.G), 0) + i11;
                        f.U2(this.G, max);
                        LocalLog.a("SmartChargeController", "totalSmartChargeIncomeTime: " + max);
                        if (this.f19911i) {
                        }
                        LocalLog.a("SmartChargeController", "judge if notifySmartChargeIncome: currentScene: " + i13 + ", income: " + i10);
                        if (AppFeature.p()) {
                        }
                    }
                } catch (NumberFormatException e11) {
                    e = e11;
                    i12 = 0;
                }
            } catch (NumberFormatException e12) {
                e = e12;
                i12 = 0;
                i11 = 0;
            }
        }
        int max2 = Math.max(f.z0(this.G), 0) + i11;
        f.U2(this.G, max2);
        LocalLog.a("SmartChargeController", "totalSmartChargeIncomeTime: " + max2);
        if (this.f19911i) {
            i10 = this.f19915m;
        }
        LocalLog.a("SmartChargeController", "judge if notifySmartChargeIncome: currentScene: " + i13 + ", income: " + i10);
        if (AppFeature.p()) {
            if (LocalLog.f()) {
                LocalLog.a("SmartChargeController", "no need to notifySmartChargeIncome");
            }
            this.f19912j = true;
            return;
        }
        if (i13 == 1) {
            int E0 = f.E0(this.G);
            if (i10 >= 10 && E0 < 1) {
                NotifyUtil.v(this.G).N();
                f.Z2(this.G, E0 + 1);
            }
        } else if (i13 == 2) {
            int D0 = f.D0(this.G);
            if (i10 >= 10 && D0 < 1) {
                NotifyUtil.v(this.G).N();
                f.Y2(this.G, D0 + 1);
            }
        } else if (i13 == 3) {
            int F0 = f.F0(this.G);
            if (i10 >= 10 && F0 < 1) {
                NotifyUtil.v(this.G).N();
                f.a3(this.G, F0 + 1);
            }
        } else if (i13 == 4) {
            int C0 = f.C0(this.G);
            if (i10 >= 10 && C0 < 1) {
                NotifyUtil.v(this.G).N();
                f.X2(this.G, C0 + 1);
            }
        }
        this.f19912j = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w(int i10, boolean z10) {
        String str;
        LocalLog.a("SmartChargeController", "handleSmartChargeMode: mode = " + i10 + "; enable = " + z10);
        if (this.f19924v != (z10 ? 3 : 0)) {
            this.f19924v = z10 ? 3 : 0;
            ThermalControlUtils.getInstance(this.G).sendChargeModeChanged(this.f19924v);
            long currentTimeMillis = System.currentTimeMillis();
            if (this.f19924v == 3) {
                this.B = currentTimeMillis;
                this.C = 0L;
                this.D = 0L;
                this.A++;
                this.J.F(ChargeUtil.g());
                this.J.E(Integer.toString(this.f19920r));
                this.J.A(Integer.toString(this.f19921s));
                this.J.z(Long.toString(this.E));
            } else {
                this.C = currentTimeMillis;
                long j10 = currentTimeMillis - this.B;
                if (j10 <= 0) {
                    j10 = 0;
                }
                this.D = j10;
                this.B = 0L;
                this.J.y(ChargeUtil.g());
                this.J.B(Long.toString(this.D));
                this.J.x(Integer.toString(this.f19921s));
                this.J.C(ChargeUtil.m());
            }
            if (this.f19911i) {
                String str2 = ": ";
                int i11 = this.f19925w;
                if (i11 == 2) {
                    str2 = ": 低电量场景";
                } else if (i11 == 3) {
                    str2 = ": 早晨出行场景";
                } else if (i11 == 4) {
                    str2 = ": 高铁飞机行程场景";
                }
                if (z10) {
                    str = "进入极速模式充电！" + str2;
                } else {
                    str = "退出极速模式充电！";
                }
                f.d2(this.G, z10);
                Toast.makeText(this.G, str, 1).show();
                LocalLog.a("SmartChargeController", "Toast has been pushed! The content is: " + str);
            }
        }
        LocalLog.a("SmartChargeController", "handleSmartChargeMode: mSmartChargeMode = " + this.f19924v);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean x() {
        return AppFeature.o() ? this.f19927y >= 80 : this.f19927y >= 100;
    }

    private void y() {
        int i10;
        if (this.f19911i) {
            i10 = this.f19916n;
        } else {
            DataAbilityApi dataAbilityApi = DataAbilityApi.INSTANCE;
            SceneStatusInfo querySceneStatusByName = dataAbilityApi.querySceneStatusByName(SceneStatusInfo.SceneConstant.SCENE_NAME_FLIGHT, this.G);
            SceneStatusInfo querySceneStatusByName2 = dataAbilityApi.querySceneStatusByName(SceneStatusInfo.SceneConstant.SCENE_NAME_TRAIN, this.G);
            if (querySceneStatusByName == null && querySceneStatusByName2 == null) {
                i10 = -1;
            } else {
                if (querySceneStatusByName == null) {
                    querySceneStatusByName = querySceneStatusByName2;
                }
                LocalLog.a("SmartChargeController", "enterUrgentNeedPowerScene---statusJourney.mSceneStatus: " + querySceneStatusByName.mSceneStatus);
                i10 = querySceneStatusByName.mSceneStatus;
            }
        }
        LocalLog.a("SmartChargeController", "enterUrgentNeedPowerScene---statusJourney value: " + i10);
        if (i10 == 2900 || i10 == 3000 || i10 == 4000 || i10 == 5000) {
            this.f19925w = 4;
        }
    }

    public void E() {
        LocalLog.a("SmartChargeController", "resetTestMode");
        this.f19911i = false;
        this.f19913k = 100;
        this.f19914l = -1;
        this.f19915m = 0;
        this.f19916n = 0;
        this.F = 0L;
        D();
        s();
        A();
    }

    public void F(float f10) {
        LocalLog.a("SmartChargeController", "setAiLeaveHomeTimeWhileTestMode:  aiLeaveHomeTime = " + f10);
        this.E = ChargeUtil.a((double) f10, System.currentTimeMillis());
    }

    public void G(int i10) {
        LocalLog.a("SmartChargeController", "setIncomePercentWhileTestMode:  incomePercent = " + i10);
        this.f19915m = i10;
    }

    public void H(int i10) {
        LocalLog.a("SmartChargeController", "setJourneyValueWhileTestMode:  journeyValue = " + i10);
        this.f19916n = i10;
    }

    public void I(int i10) {
        LocalLog.a("SmartChargeController", "setLevelWhileTestMode:  level = " + i10);
        this.f19913k = i10;
    }

    public void J() {
        LocalLog.a("SmartChargeController", "setTestMode");
        this.f19911i = true;
    }

    public void K(int i10) {
        LocalLog.a("SmartChargeController", "setTimeWhileTestMode:  time = " + i10);
        this.f19914l = i10;
    }

    public void L() {
        this.H.sendEmptyMessage(100);
    }

    public void O() {
        Affair.f().i(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().i(this, 229);
        Affair.f().i(this, EventType.SCENE_MODE_AUDIO_OUT);
        Affair.f().i(this, EventType.SCENE_MODE_LOCATION);
        Affair.f().i(this, 1209);
        if (this.f19907e) {
            this.G.unregisterReceiver(this.L);
            this.f19907e = false;
        }
        N();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (!this.f19908f) {
            LocalLog.a("SmartChargeController", "do not handle broadcast: mIsSmartChargeSwitchOn = " + this.f19908f);
            return;
        }
        if (i10 == 201) {
            this.f19909g = false;
            this.H.sendEmptyMessage(103);
            return;
        }
        if (i10 == 202) {
            this.f19909g = true;
            this.H.sendEmptyMessage(102);
            return;
        }
        if (i10 == 204) {
            int intExtra = intent.getIntExtra("level", 0);
            int intExtra2 = intent.getIntExtra("status", 0);
            int intExtra3 = intent.getIntExtra("plugged", 0);
            this.f19920r = intent.getIntExtra("temperature", 0);
            if (this.f19911i) {
                intExtra = this.f19913k;
            }
            this.f19921s = intExtra;
            this.f19922t = intExtra2;
            this.f19923u = intExtra3;
            if (intExtra3 == 0 && intExtra3 != this.f19918p) {
                NotifyUtil.v(this.G).n();
                v();
            }
            if (x() && this.f19909g) {
                s();
                M();
            } else if (this.f19912j || AppFeature.o()) {
                A();
            }
            int i11 = this.f19923u;
            if (i11 != 0 && i11 != this.f19918p) {
                this.f19912j = false;
            }
            this.f19917o = intExtra2;
            this.f19918p = intExtra3;
            return;
        }
        if (i10 != 229) {
            if (i10 != 1209) {
                return;
            }
            this.f19928z = ChargeUtil.f();
            LocalLog.a("SmartChargeController", "mCurrentHourTime: " + this.f19928z);
            return;
        }
        intent.getIntExtra("chargertechnology", 0);
        int intExtra4 = intent.getIntExtra("chargewattage", 0);
        this.f19927y = intExtra4;
        LocalLog.a("SmartChargeController", "handle ADDITIONAL_BATTERY_CHANGED broadcast: mChargeWattage: " + this.f19927y + " mIsSmartChargeSwitchOn: " + this.f19908f + " mIsScreenOn: " + this.f19909g);
        if (x() && this.f19909g) {
            s();
            M();
        } else {
            if (this.f19912j || AppFeature.o()) {
                A();
            }
            LocalLog.a("SmartChargeController", "quitSmartCharge---reason");
        }
        if (x() && this.f19927y != this.f19919q && UserHandle.myUserId() == 0 && AppFeature.p()) {
            int B0 = f.B0(this.G);
            long currentTimeMillis = System.currentTimeMillis();
            long A0 = f.A0(this.G);
            LocalLog.a("SmartChargeController", "notifySmartChargeBeRunning: times = " + B0 + "; SMART_CHARGE_NOTIFICATION_TIMES_BE_RUNNING = " + ChargeUtil.f11361b + "; time = " + currentTimeMillis + "; lastTime = " + A0);
            if (B0 < ChargeUtil.f11361b && (A0 == 0 || currentTimeMillis - A0 >= 259200000)) {
                LocalLog.a("SmartChargeController", "notifySmartChargeBeRunning: success!");
                NotifyUtil.v(this.G).M();
                f.W2(this.G, B0 + 1);
                f.V2(this.G, currentTimeMillis);
            }
        }
        this.f19919q = intExtra4;
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().g(this, 229);
        Affair.f().g(this, EventType.SCENE_MODE_AUDIO_OUT);
        Affair.f().g(this, EventType.SCENE_MODE_LOCATION);
        Affair.f().g(this, 1209);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("oplus.intent.action.smartcharge.berunning.nomore");
        intentFilter.addAction("oplus.intent.action.smartcharge.berunning.knowmore");
        this.G.registerReceiver(this.L, intentFilter, 2);
        B();
        this.f19907e = true;
    }

    public void u() {
        int i10;
        Map<Integer, Integer> map;
        this.J.u0(f.J0(this.G) ? "on" : "off");
        this.J.G(Integer.toString(this.A));
        int i11 = 0;
        this.A = 0;
        int x02 = f.x0(this.G) + 1;
        int z02 = f.z0(this.G);
        if (x02 == 1) {
            map = new HashMap<>();
            map.put(Integer.valueOf(x02), Integer.valueOf(z02));
            i10 = 0;
        } else {
            Map<Integer, Integer> y02 = f.y0(this.G);
            if (y02 != null) {
                int i12 = 0;
                for (Map.Entry<Integer, Integer> entry : y02.entrySet()) {
                    if (entry.getValue() != null) {
                        i12 += entry.getValue().intValue();
                        LocalLog.a("SmartChargeController", "handleIncomeData: incomeday =  " + entry.getKey() + ", incomeValue = " + entry.getValue());
                    }
                }
                i10 = z02 > i12 ? z02 - i12 : 0;
                if (x02 <= 7) {
                    y02.put(Integer.valueOf(x02), Integer.valueOf(i10));
                } else {
                    if (y02.get(1) != null) {
                        z02 -= y02.get(1).intValue();
                    }
                    while (i11 < 6) {
                        int i13 = i11 + 1;
                        y02.put(Integer.valueOf(i13), y02.get(Integer.valueOf(i11 + 2)));
                        i11 = i13;
                    }
                    y02.put(7, Integer.valueOf(i10));
                }
                map = y02;
                i11 = i12;
            } else {
                i10 = 0;
                map = y02;
            }
        }
        f.T2(map, this.G);
        f.U2(this.G, z02);
        f.S2(this.G, x02);
        LocalLog.a("SmartChargeController", "handleIncomeData: day =  " + x02 + ", pastTotalSmartChargeIncomeTime = " + i11 + ", curTotalSmartChargeIncomeTime = " + i10 + ", totalSmartChargeIncomeTime = " + z02);
    }

    public void z() {
        this.H.sendEmptyMessage(101);
    }
}
