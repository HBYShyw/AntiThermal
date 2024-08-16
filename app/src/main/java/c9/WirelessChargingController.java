package c9;

import a6.UserSleepTimeModel;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import b6.LocalLog;
import c6.NotifyUtil;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.powermanager.fuelgaue.WirelessChargingReminderActivity;
import com.oplus.thermalcontrol.ThermalControlUtils;
import d6.ConfigUpdateUtil;
import w4.Affair;
import w4.IAffairCallback;
import x1.COUIAlertDialogBuilder;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: WirelessChargingController.java */
/* renamed from: c9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class WirelessChargingController implements IAffairCallback {
    private static volatile WirelessChargingController E;

    /* renamed from: e, reason: collision with root package name */
    private Context f4968e;

    /* renamed from: f, reason: collision with root package name */
    private Handler f4969f;

    /* renamed from: g, reason: collision with root package name */
    private PowerManager.WakeLock f4970g;

    /* renamed from: h, reason: collision with root package name */
    private NotifyUtil f4971h;

    /* renamed from: i, reason: collision with root package name */
    private UploadDataUtil f4972i;

    /* renamed from: j, reason: collision with root package name */
    private UserSleepTimeModel f4973j;

    /* renamed from: y, reason: collision with root package name */
    private AlertDialog f4988y;

    /* renamed from: z, reason: collision with root package name */
    private AlertDialog f4989z;

    /* renamed from: k, reason: collision with root package name */
    private int f4974k = 25;

    /* renamed from: l, reason: collision with root package name */
    private volatile int f4975l = 100;

    /* renamed from: m, reason: collision with root package name */
    private volatile int f4976m = 0;

    /* renamed from: n, reason: collision with root package name */
    private volatile int f4977n = 1;

    /* renamed from: o, reason: collision with root package name */
    private volatile int f4978o = 25;

    /* renamed from: p, reason: collision with root package name */
    private volatile int f4979p = 100;

    /* renamed from: q, reason: collision with root package name */
    private volatile int f4980q = 0;

    /* renamed from: r, reason: collision with root package name */
    private volatile int f4981r = 1;

    /* renamed from: s, reason: collision with root package name */
    private volatile int f4982s = 25;

    /* renamed from: t, reason: collision with root package name */
    private volatile boolean f4983t = false;

    /* renamed from: u, reason: collision with root package name */
    private volatile boolean f4984u = false;

    /* renamed from: v, reason: collision with root package name */
    private volatile boolean f4985v = false;

    /* renamed from: w, reason: collision with root package name */
    private volatile boolean f4986w = false;

    /* renamed from: x, reason: collision with root package name */
    private volatile int f4987x = 0;
    private ContentObserver A = new a(new Handler());
    private ContentObserver B = new b(new Handler());
    private ContentObserver C = new c(new Handler());
    private BroadcastReceiver D = new d();

    /* compiled from: WirelessChargingController.java */
    /* renamed from: c9.a$a */
    /* loaded from: classes2.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            boolean d12 = f6.f.d1(WirelessChargingController.this.f4968e);
            LocalLog.a("WirelessChargingController", "mReverseStateObserver:  state=" + d12);
            if (d12) {
                f6.f.P1(WirelessChargingController.this.f4968e);
                WirelessChargingController.this.f4971h.q();
            } else {
                f6.f.e(WirelessChargingController.this.f4968e);
            }
            f6.f.w3(d12);
        }
    }

    /* compiled from: WirelessChargingController.java */
    /* renamed from: c9.a$b */
    /* loaded from: classes2.dex */
    class b extends ContentObserver {
        b(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            WirelessChargingController.this.f4969f.sendEmptyMessage(6);
        }
    }

    /* compiled from: WirelessChargingController.java */
    /* renamed from: c9.a$c */
    /* loaded from: classes2.dex */
    class c extends ContentObserver {
        c(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            WirelessChargingController.this.f4969f.sendEmptyMessage(7);
        }
    }

    /* compiled from: WirelessChargingController.java */
    /* renamed from: c9.a$d */
    /* loaded from: classes2.dex */
    class d extends BroadcastReceiver {
        d() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("oplus.intent.action.wirelessreverse.timeout".equals(action)) {
                WirelessChargingController.this.f4969f.sendEmptyMessage(2);
                return;
            }
            if ("oplus.intent.action.wirelesslowPower.start".equals(action)) {
                WirelessChargingController.this.f4970g.acquire(2000L);
                WirelessChargingController.this.f4969f.sendEmptyMessage(3);
            } else if ("oplus.intent.action.wirelesslowPower.end".equals(action)) {
                WirelessChargingController.this.f4970g.acquire(2000L);
                WirelessChargingController.this.f4969f.sendEmptyMessage(4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WirelessChargingController.java */
    /* renamed from: c9.a$e */
    /* loaded from: classes2.dex */
    public class e implements DialogInterface.OnClickListener {
        e() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            WirelessChargingController.this.P();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WirelessChargingController.java */
    /* renamed from: c9.a$f */
    /* loaded from: classes2.dex */
    public class f implements DialogInterface.OnClickListener {
        f() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            WirelessChargingController.this.C();
            WirelessChargingController.this.O();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WirelessChargingController.java */
    /* renamed from: c9.a$g */
    /* loaded from: classes2.dex */
    public class g implements DialogInterface.OnClickListener {
        g() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            WirelessChargingController.this.P();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: WirelessChargingController.java */
    /* renamed from: c9.a$h */
    /* loaded from: classes2.dex */
    public class h implements DialogInterface.OnClickListener {
        h() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            WirelessChargingController.this.D();
        }
    }

    /* compiled from: WirelessChargingController.java */
    /* renamed from: c9.a$i */
    /* loaded from: classes2.dex */
    private class i extends Handler {
        public i(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("oplus.intent.action.wirelessreverse.timeout");
                    intentFilter.addAction("oplus.intent.action.wirelesslowPower.start");
                    intentFilter.addAction("oplus.intent.action.wirelesslowPower.end");
                    WirelessChargingController.this.f4968e.registerReceiver(WirelessChargingController.this.D, intentFilter, 2);
                    WirelessChargingController.this.f4968e.getContentResolver().registerContentObserver(Settings.System.getUriFor("wireless_reverse_charging_state"), false, WirelessChargingController.this.A, 0);
                    WirelessChargingController.this.f4968e.getContentResolver().registerContentObserver(Settings.System.getUriFor("slient_mode_type_state"), false, WirelessChargingController.this.B);
                    WirelessChargingController.this.f4968e.getContentResolver().registerContentObserver(Settings.System.getUriFor("low_power_charging_state"), false, WirelessChargingController.this.C);
                    if (UserHandle.myUserId() == 0 || !f6.f.d1(WirelessChargingController.this.f4968e)) {
                        f6.f.y3(WirelessChargingController.this.f4968e, false);
                        f6.f.w3(false);
                    }
                    if (WirelessChargingController.this.f4976m == 4 && WirelessChargingController.this.f4987x > 0 && WirelessChargingController.this.f4987x != 3 && WirelessChargingController.this.f4987x != 4) {
                        WirelessChargingController.this.a();
                    }
                    WirelessChargingController.this.registerAction();
                    if (WirelessChargingController.this.f4980q == 1 || WirelessChargingController.this.f4980q == 2 || WirelessChargingController.this.f4980q == 4) {
                        if (WirelessChargingController.this.f4981r == 2 || WirelessChargingController.this.f4981r == 5) {
                            f6.f.y3(WirelessChargingController.this.f4968e, false);
                            return;
                        }
                        return;
                    }
                    return;
                case 1:
                    WirelessChargingController.this.C();
                    WirelessChargingController.this.D();
                    WirelessChargingController.this.f4968e.unregisterReceiver(WirelessChargingController.this.D);
                    WirelessChargingController.this.f4968e.getContentResolver().unregisterContentObserver(WirelessChargingController.this.A);
                    WirelessChargingController.this.f4968e.getContentResolver().unregisterContentObserver(WirelessChargingController.this.B);
                    WirelessChargingController.this.f4968e.getContentResolver().unregisterContentObserver(WirelessChargingController.this.C);
                    WirelessChargingController.this.R();
                    return;
                case 2:
                    if (WirelessChargingController.this.f4984u) {
                        return;
                    }
                    f6.f.y3(WirelessChargingController.this.f4968e, false);
                    WirelessChargingController.this.f4972i.R0(false, "open_reverse_timeout");
                    WirelessChargingController.this.f4971h.K();
                    return;
                case 3:
                    int v02 = f6.f.v0(WirelessChargingController.this.f4968e);
                    if (v02 != 0 && v02 == 2) {
                        f6.f.O1(WirelessChargingController.this.f4968e, true, System.currentTimeMillis() + f6.f.X(WirelessChargingController.this.f4968e, true));
                    }
                    f6.f.R2(WirelessChargingController.this.f4968e, 1);
                    return;
                case 4:
                    int v03 = f6.f.v0(WirelessChargingController.this.f4968e);
                    long currentTimeMillis = System.currentTimeMillis();
                    if (v03 == 0) {
                        WirelessChargingController.this.f4973j.o(false);
                        long l10 = WirelessChargingController.this.f4973j.l();
                        long k10 = WirelessChargingController.this.f4973j.k();
                        if (l10 > currentTimeMillis && l10 < k10) {
                            f6.f.O1(WirelessChargingController.this.f4968e, true, l10);
                        }
                        if (k10 > 0) {
                            f6.f.O1(WirelessChargingController.this.f4968e, false, k10);
                        }
                    } else if (v03 == 2) {
                        f6.f.O1(WirelessChargingController.this.f4968e, false, currentTimeMillis + f6.f.X(WirelessChargingController.this.f4968e, false));
                    }
                    f6.f.R2(WirelessChargingController.this.f4968e, 0);
                    return;
                case 5:
                    boolean d12 = f6.f.d1(WirelessChargingController.this.f4968e);
                    WirelessChargingController wirelessChargingController = WirelessChargingController.this;
                    wirelessChargingController.f4974k = f6.f.c1(wirelessChargingController.f4968e);
                    int E = ConfigUpdateUtil.n(WirelessChargingController.this.f4968e).E();
                    if (WirelessChargingController.this.f4979p < WirelessChargingController.this.f4974k && d12) {
                        f6.f.y3(WirelessChargingController.this.f4968e, false);
                        WirelessChargingController.this.f4971h.I(WirelessChargingController.this.f4974k);
                        WirelessChargingController.this.f4972i.R0(false, "battLevel_below_threshold");
                    } else if ((WirelessChargingController.this.f4981r == 2 || WirelessChargingController.this.f4981r == 5) && WirelessChargingController.this.f4980q == 4 && d12) {
                        f6.f.y3(WirelessChargingController.this.f4968e, false);
                        WirelessChargingController.this.f4972i.R0(false, "wireless_charging_begin");
                        WirelessChargingController.this.f4971h.L();
                    } else if (WirelessChargingController.this.f4982s >= E && d12) {
                        f6.f.y3(WirelessChargingController.this.f4968e, false);
                        WirelessChargingController.this.f4972i.R0(false, "high_temperature_disable_reverse");
                        WirelessChargingController.this.f4971h.J();
                    } else if ((WirelessChargingController.this.f4980q == 1 || WirelessChargingController.this.f4980q == 2) && ((WirelessChargingController.this.f4981r == 2 || WirelessChargingController.this.f4981r == 5) && !y5.b.J() && d12)) {
                        f6.f.y3(WirelessChargingController.this.f4968e, false);
                    } else if (WirelessChargingController.this.K() == 1 && !y5.b.J() && ((d12 || (WirelessChargingController.this.f4985v && !WirelessChargingController.this.f4986w)) && UserHandle.myUserId() == ActivityManager.getCurrentUser())) {
                        f6.f.y3(WirelessChargingController.this.f4968e, false);
                        Toast.makeText(WirelessChargingController.this.f4968e, WirelessChargingController.this.f4968e.getString(R.string.reverse_charge_forbbiden_by_wired_otg), 0).show();
                    }
                    if ((WirelessChargingController.this.f4981r == 2 || WirelessChargingController.this.f4981r == 5) && WirelessChargingController.this.f4980q != 4 && WirelessChargingController.this.f4976m == 4) {
                        WirelessChargingController.this.f4971h.r();
                    }
                    if (WirelessChargingController.this.f4976m == 4 && WirelessChargingController.this.f4987x > 0 && WirelessChargingController.this.f4987x != 3 && WirelessChargingController.this.f4987x != 4) {
                        if (WirelessChargingController.this.f4981r == 5) {
                            f6.f.R2(WirelessChargingController.this.f4968e, 2);
                        } else {
                            WirelessChargingController.this.a();
                        }
                    }
                    if ((WirelessChargingController.this.f4980q == 1 || WirelessChargingController.this.f4980q == 2) && WirelessChargingController.this.f4976m == 4 && WirelessChargingController.this.f4977n == 2 && WirelessChargingController.this.f4981r == 2) {
                        Toast.makeText(WirelessChargingController.this.f4968e, WirelessChargingController.this.f4968e.getString(R.string.wire_charging_right_now_toast), 0).show();
                    }
                    if (WirelessChargingController.this.f4980q != 4 && WirelessChargingController.this.f4976m == 4 && WirelessChargingController.this.K() == 1 && UserHandle.myUserId() == ActivityManager.getCurrentUser()) {
                        Toast.makeText(WirelessChargingController.this.f4968e, WirelessChargingController.this.f4968e.getString(R.string.wireless_charge_forbbiden_by_wired_otg), 0).show();
                    }
                    if (WirelessChargingController.this.f4979p >= WirelessChargingController.this.f4974k) {
                        WirelessChargingController.this.f4971h.o();
                    }
                    if (WirelessChargingController.this.f4982s < E) {
                        WirelessChargingController.this.f4971h.p();
                    }
                    if (WirelessChargingController.this.f4983t && !WirelessChargingController.this.f4984u) {
                        f6.f.e(WirelessChargingController.this.f4968e);
                        if (f6.f.d1(WirelessChargingController.this.f4968e)) {
                            f6.f.P1(WirelessChargingController.this.f4968e);
                        }
                    }
                    if (WirelessChargingController.this.f4985v && !WirelessChargingController.this.f4986w && f6.f.d1(WirelessChargingController.this.f4968e)) {
                        f6.f.y3(WirelessChargingController.this.f4968e, false);
                        WirelessChargingController.this.f4972i.R0(false, "reverse_func_auto_close");
                    }
                    if (!AppFeature.v() || WirelessChargingController.this.f4976m != 4 || f6.f.R(WirelessChargingController.this.f4968e) || !y5.b.I() || f6.f.a1(WirelessChargingController.this.f4968e) || ThermalControlUtils.getInstance(WirelessChargingController.this.f4968e).isGameAlive() || ThermalControlUtils.getInstance(WirelessChargingController.this.f4968e).isVideoScene() || WirelessChargingController.this.f4968e.getResources().getConfiguration().orientation == 2) {
                        return;
                    }
                    Handler mainThreadHandler = WirelessChargingController.this.f4968e.getMainThreadHandler();
                    final WirelessChargingController wirelessChargingController2 = WirelessChargingController.this;
                    mainThreadHandler.post(new Runnable() { // from class: c9.b
                        @Override // java.lang.Runnable
                        public final void run() {
                            WirelessChargingController.q(WirelessChargingController.this);
                        }
                    });
                    f6.f.r2(WirelessChargingController.this.f4968e, true);
                    return;
                case 6:
                case 7:
                    if (WirelessChargingController.this.f4976m != 4 || WirelessChargingController.this.f4987x <= 0 || WirelessChargingController.this.f4987x == 3 || WirelessChargingController.this.f4987x == 4) {
                        return;
                    }
                    WirelessChargingController.this.a();
                    return;
                default:
                    return;
            }
        }
    }

    private WirelessChargingController(Context context) {
        this.f4972i = null;
        this.f4968e = context;
        HandlerThread handlerThread = new HandlerThread("great_waterfall_iguacu");
        handlerThread.start();
        this.f4969f = new i(handlerThread.getLooper());
        this.f4970g = ((PowerManager) this.f4968e.getSystemService("power")).newWakeLock(1, "wirelessCharging:controller");
        this.f4971h = NotifyUtil.v(this.f4968e);
        this.f4972i = UploadDataUtil.S0(this.f4968e);
        this.f4973j = UserSleepTimeModel.j(this.f4968e);
    }

    public static WirelessChargingController H(Context context) {
        if (E == null) {
            synchronized (WirelessChargingController.class) {
                if (E == null) {
                    E = new WirelessChargingController(context);
                }
            }
        }
        return E;
    }

    private Context J(Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.Theme_Demo);
        COUIThemeOverlay.i().b(contextThemeWrapper);
        return contextThemeWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void N() {
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(J(this.f4968e), f6.f.k(this.f4968e));
        cOUIAlertDialogBuilder.h0(R.string.wireless_charge_guide_notification_one_title);
        cOUIAlertDialogBuilder.e0(R.string.wireless_charge_guide_notification_one_go, new e());
        cOUIAlertDialogBuilder.a0(R.string.wireless_charge_guide_notification_one_cancel, new f());
        cOUIAlertDialogBuilder.d(false);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f4988y = a10;
        Window window = a10.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        attributes.type = 2009;
        window.setAttributes(attributes);
        this.f4988y.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void O() {
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(J(this.f4968e), f6.f.k(this.f4968e));
        cOUIAlertDialogBuilder.h0(R.string.wireless_charge_guide_notification_two_title);
        cOUIAlertDialogBuilder.Y(R.string.wireless_charge_guide_notification_two_content);
        cOUIAlertDialogBuilder.e0(R.string.wireless_charge_guide_notification_two_go, new g());
        cOUIAlertDialogBuilder.a0(R.string.wireless_charge_guide_notification_two_cancel, new h());
        cOUIAlertDialogBuilder.d(false);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f4989z = a10;
        Window window = a10.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        attributes.type = 2009;
        window.setAttributes(attributes);
        this.f4989z.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void P() {
        Intent intent = new Intent(this.f4968e, (Class<?>) WirelessChargingReminderActivity.class);
        intent.setFlags(872415232);
        this.f4968e.startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        boolean W = f6.f.W(this.f4968e);
        int v02 = f6.f.v0(this.f4968e);
        long currentTimeMillis = System.currentTimeMillis();
        if (!W) {
            f6.f.d(this.f4968e, true);
            f6.f.d(this.f4968e, false);
            f6.f.R2(this.f4968e, 0);
            return;
        }
        if (v02 == 0) {
            this.f4973j.o(false);
            long l10 = this.f4973j.l();
            long k10 = this.f4973j.k();
            if (currentTimeMillis >= l10 && currentTimeMillis < k10) {
                f6.f.R2(this.f4968e, 1);
                f6.f.O1(this.f4968e, false, k10);
                return;
            } else {
                if (currentTimeMillis < l10) {
                    f6.f.R2(this.f4968e, 0);
                    f6.f.O1(this.f4968e, true, l10);
                    f6.f.O1(this.f4968e, false, k10);
                    return;
                }
                return;
            }
        }
        if (v02 == 1) {
            f6.f.R2(this.f4968e, 1);
            f6.f.d(this.f4968e, true);
            f6.f.d(this.f4968e, false);
            return;
        }
        if (v02 == 2) {
            long X = f6.f.X(this.f4968e, true);
            long X2 = f6.f.X(this.f4968e, false);
            if (X <= 0 || X2 <= 0) {
                return;
            }
            if (f6.f.f(this.f4968e)) {
                f6.f.R2(this.f4968e, 1);
                f6.f.O1(this.f4968e, true, X + currentTimeMillis);
                f6.f.O1(this.f4968e, false, currentTimeMillis + X2);
            } else {
                f6.f.R2(this.f4968e, 0);
                f6.f.O1(this.f4968e, true, X + currentTimeMillis);
                f6.f.O1(this.f4968e, false, currentTimeMillis + X2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void q(WirelessChargingController wirelessChargingController) {
        wirelessChargingController.N();
    }

    public void C() {
        AlertDialog alertDialog = this.f4988y;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void D() {
        AlertDialog alertDialog = this.f4989z;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public int E() {
        return this.f4979p;
    }

    public int F() {
        return this.f4981r;
    }

    public int G() {
        return this.f4982s;
    }

    public int I() {
        return this.f4980q;
    }

    public int K() {
        Object obj;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            obj = cls.getMethod("getWiredOtgOnline", new Class[0]).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), new Object[0]);
        } catch (Exception e10) {
            LocalLog.l("WirelessChargingController", "getWiredOtgOnline error = " + e10.toString());
            obj = null;
        }
        int parseInt = obj != null ? Integer.parseInt(obj.toString()) : 0;
        LocalLog.a("WirelessChargingController", "wiredOtgOnline = " + parseInt);
        return parseInt;
    }

    public void L(int i10, int i11, int i12, int i13, int i14) {
        this.f4975l = this.f4979p;
        this.f4976m = this.f4980q;
        this.f4977n = this.f4981r;
        this.f4978o = this.f4982s;
        this.f4983t = this.f4984u;
        this.f4985v = this.f4986w;
        this.f4979p = i10;
        this.f4980q = i12;
        this.f4981r = i11;
        this.f4982s = i13;
        if (i14 == 1) {
            this.f4986w = true;
            this.f4984u = true;
        } else if (i14 == 2) {
            this.f4986w = true;
            this.f4984u = false;
        } else {
            this.f4986w = false;
            this.f4984u = false;
        }
        this.f4969f.sendEmptyMessage(5);
    }

    public void M() {
        this.f4969f.sendEmptyMessage(1);
    }

    public void Q() {
        this.f4969f.sendEmptyMessage(0);
    }

    public void R() {
        Affair.f().i(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().i(this, 229);
    }

    public void S() {
        this.f4972i.Q0();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 == 204) {
            L(intent.getIntExtra("level", 0), intent.getIntExtra("status", 0), intent.getIntExtra("plugged", 0), intent.getIntExtra("temperature", 0), intent.getIntExtra("wireless_reverse_chg_type", 3));
            return;
        }
        if (i10 != 229) {
            return;
        }
        this.f4987x = intent.getIntExtra("chargertechnology", 0);
        LocalLog.a("WirelessChargingController", "mChargerTechnology = " + this.f4987x);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
        Affair.f().g(this, 229);
    }
}
