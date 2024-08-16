package s8;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.powermanager.powersave.PowerSaveTipsService;
import d6.ConfigUpdateUtil;
import f6.CommonUtil;
import java.util.HashMap;
import java.util.function.BiConsumer;
import t8.PowerUsageManager;
import w4.Affair;
import w4.IAffairCallback;
import x4.AppSwitchManager;
import x4.AppSwitchObserver;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: PowerSaveHelper.java */
/* renamed from: s8.d, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerSaveHelper implements IAffairCallback {

    /* renamed from: v, reason: collision with root package name */
    private static volatile PowerSaveHelper f18163v;

    /* renamed from: s, reason: collision with root package name */
    private Context f18178s;

    /* renamed from: e, reason: collision with root package name */
    private HashMap<String, Integer> f18164e = new HashMap<>();

    /* renamed from: f, reason: collision with root package name */
    private d f18165f = null;

    /* renamed from: g, reason: collision with root package name */
    private f f18166g = null;

    /* renamed from: h, reason: collision with root package name */
    private h f18167h = null;

    /* renamed from: i, reason: collision with root package name */
    private e f18168i = null;

    /* renamed from: j, reason: collision with root package name */
    private AppSwitchManager f18169j = null;

    /* renamed from: k, reason: collision with root package name */
    private boolean f18170k = false;

    /* renamed from: l, reason: collision with root package name */
    private boolean f18171l = false;

    /* renamed from: m, reason: collision with root package name */
    private boolean f18172m = false;

    /* renamed from: n, reason: collision with root package name */
    private boolean f18173n = false;

    /* renamed from: o, reason: collision with root package name */
    private boolean f18174o = false;

    /* renamed from: p, reason: collision with root package name */
    private int f18175p = 0;

    /* renamed from: q, reason: collision with root package name */
    private int f18176q = -1;

    /* renamed from: r, reason: collision with root package name */
    private int f18177r = 0;

    /* renamed from: t, reason: collision with root package name */
    private Handler f18179t = null;

    /* renamed from: u, reason: collision with root package name */
    private AppSwitchObserver f18180u = new c();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerSaveHelper.java */
    /* renamed from: s8.d$a */
    /* loaded from: classes2.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PowerSaveConfig.f(PowerSaveHelper.this.f18178s).m();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerSaveHelper.java */
    /* renamed from: s8.d$b */
    /* loaded from: classes2.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Intent w10 = CommonUtil.w(PowerSaveHelper.this.f18178s, new Intent("oplus.intent.action.REQUEST_APP_CLEAN_RUNNING"));
            if (w10 == null) {
                return;
            }
            w10.setPackage("com.oplus.athena");
            w10.putExtra("caller_package", "com.oplus.battery.powersavemode");
            w10.putExtra("reason", "com.oplus.battery.powersavemode");
            LocalLog.l("PowerSaveHelper", "start Power Save Mode Clear from Battery.-----------------");
            PowerSaveHelper.this.f18178s.startService(w10);
        }
    }

    /* compiled from: PowerSaveHelper.java */
    /* renamed from: s8.d$c */
    /* loaded from: classes2.dex */
    class c implements AppSwitchObserver {
        c() {
        }

        @Override // x4.AppSwitchObserver
        public void a(String str, String str2, String str3) {
            if (PowerSaveHelper.this.f18170k && PowerSaveHelper.this.f18173n) {
                if (COSAServiceImpl.b().a(1) != null ? COSAServiceImpl.b().a(1).contains(str2) : false) {
                    PowerSaveHelper.this.t(false);
                } else {
                    PowerSaveHelper.this.t(true);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PowerSaveHelper.java */
    /* renamed from: s8.d$d */
    /* loaded from: classes2.dex */
    public static class d extends ContentObserver {

        /* renamed from: a, reason: collision with root package name */
        private final Context f18184a;

        public d(Context context, Handler handler) {
            super(handler);
            this.f18184a = context.getApplicationContext();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            LocalLog.a("PowerSaveHelper", "Backlight sub switch observer. state=" + f6.f.e0(this.f18184a));
            Context context = this.f18184a;
            f6.f.A2(context, (f6.f.e0(context) != 1 || PowerUsageManager.x(this.f18184a).r() > 20) ? 0 : 1);
            ((PowerManager) this.f18184a.getSystemService("power")).userActivity(SystemClock.uptimeMillis(), 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PowerSaveHelper.java */
    /* renamed from: s8.d$e */
    /* loaded from: classes2.dex */
    public class e extends ContentObserver {
        public e(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            String z11 = f6.f.z(PowerSaveHelper.this.f18178s);
            LocalLog.l("PowerSaveHelper", "competition mode observer. state=" + z11);
            if (z11 != null) {
                PowerSaveHelper.this.f18171l = "true".equals(z11);
            }
            if (PowerSaveHelper.this.f18171l) {
                if (PowerSaveHelper.this.f18170k) {
                    ((PowerManager) PowerSaveHelper.this.f18178s.getSystemService("power")).setPowerSaveModeEnabled(false);
                    PowerSaveHelper.this.f18172m = true;
                }
                PowerSaveHelper powerSaveHelper = PowerSaveHelper.this;
                powerSaveHelper.f18176q = powerSaveHelper.f18175p;
                return;
            }
            if (PowerSaveHelper.this.f18176q == -1) {
                LocalLog.l("PowerSaveHelper", "close action invalid");
                return;
            }
            boolean z12 = f6.f.d0(PowerSaveHelper.this.f18178s) == 1;
            int c02 = f6.f.c0(PowerSaveHelper.this.f18178s);
            boolean z13 = f6.f.a0(PowerSaveHelper.this.f18178s) == 1;
            if (PowerSaveHelper.this.f18172m) {
                if (PowerSaveHelper.this.f18176q < 90 && z13 && PowerSaveHelper.this.f18175p >= 90) {
                    LocalLog.l("PowerSaveHelper", "auto close not recover");
                    NotifyUtil.v(PowerSaveHelper.this.f18178s).H(90);
                    return;
                }
                ((PowerManager) PowerSaveHelper.this.f18178s.getSystemService("power")).setPowerSaveModeEnabled(true);
            } else if (PowerSaveHelper.this.f18176q > c02 && z12 && PowerSaveHelper.this.f18175p <= c02) {
                LocalLog.l("PowerSaveHelper", "auto open delay because of competition");
                ((PowerManager) PowerSaveHelper.this.f18178s.getSystemService("power")).setPowerSaveModeEnabled(true);
            }
            PowerSaveHelper.this.f18176q = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PowerSaveHelper.java */
    /* renamed from: s8.d$f */
    /* loaded from: classes2.dex */
    public class f extends ContentObserver {
        public f(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            int G = f6.f.G(PowerSaveHelper.this.f18178s);
            LocalLog.a("PowerSaveHelper", "power save FiveG switch observer. state=" + G);
            if (PowerSaveHelper.this.f18170k) {
                if (G == 0) {
                    if (f6.f.L(PowerSaveHelper.this.f18178s) != 0) {
                        f6.f.l2(PowerSaveHelper.this.f18178s, 0);
                    }
                } else {
                    if (G != 1 || f6.f.L(PowerSaveHelper.this.f18178s) == 1) {
                        return;
                    }
                    f6.f.l2(PowerSaveHelper.this.f18178s, 1);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PowerSaveHelper.java */
    /* renamed from: s8.d$g */
    /* loaded from: classes2.dex */
    public class g extends Handler {
        public g(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PowerSaveHelper.java */
    /* renamed from: s8.d$h */
    /* loaded from: classes2.dex */
    public class h extends ContentObserver {
        public h(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            int l02 = f6.f.l0(PowerSaveHelper.this.f18178s);
            LocalLog.a("PowerSaveHelper", "power save tips observer. state=" + l02);
            if (l02 == 1) {
                if (f6.f.t1(PowerSaveHelper.this.f18178s) && f6.f.G(PowerSaveHelper.this.f18178s) == 1) {
                    PowerSaveHelper.this.f18178s.startService(new Intent(PowerSaveHelper.this.f18178s, (Class<?>) PowerSaveTipsService.class));
                    f6.f.H2(PowerSaveHelper.this.f18178s, 2);
                    return;
                }
                f6.f.H2(PowerSaveHelper.this.f18178s, 0);
            }
        }
    }

    private PowerSaveHelper(Context context) {
        this.f18178s = null;
        this.f18178s = context;
        y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void A(String str, Integer num) {
        if (num.intValue() != 0) {
            str.hashCode();
            char c10 = 65535;
            switch (str.hashCode()) {
                case -1987198628:
                    if (str.equals("oplus_power_save_lockscreen_sounds_enabled")) {
                        c10 = 0;
                        break;
                    }
                    break;
                case -885675210:
                    if (str.equals("oplus_power_save_aod_state")) {
                        c10 = 1;
                        break;
                    }
                    break;
                case -787364747:
                    if (str.equals("oplus_power_save_anim_state")) {
                        c10 = 2;
                        break;
                    }
                    break;
                case -725675956:
                    if (str.equals("oplus_power_save_disable_screen_capture_sound")) {
                        c10 = 3;
                        break;
                    }
                    break;
                case -600572992:
                    if (str.equals("oplus_power_save_aon_state")) {
                        c10 = 4;
                        break;
                    }
                    break;
                case -376470184:
                    if (str.equals("oplus_power_save_disable_front_finger_sound")) {
                        c10 = 5;
                        break;
                    }
                    break;
                case 626970849:
                    if (str.equals("oplus_power_save_auto_brightness_state")) {
                        c10 = 6;
                        break;
                    }
                    break;
                case 644654481:
                    if (str.equals("oplus_power_save_keyguard_notice_state")) {
                        c10 = 7;
                        break;
                    }
                    break;
                case 1206297925:
                    if (str.equals("oplus_power_save_global_delete_sound")) {
                        c10 = '\b';
                        break;
                    }
                    break;
                case 1338718844:
                    if (str.equals("oplus_power_save_curved_display_state")) {
                        c10 = '\t';
                        break;
                    }
                    break;
                case 1600040706:
                    if (str.equals("oplus_power_save_sound_effects_enabled")) {
                        c10 = '\n';
                        break;
                    }
                    break;
                case 1721421047:
                    if (str.equals("oplus_power_save_sub_aod_state")) {
                        c10 = 11;
                        break;
                    }
                    break;
                case 1798980900:
                    if (str.equals("oplus_power_save_osie_state")) {
                        c10 = '\f';
                        break;
                    }
                    break;
                case 1881570918:
                    if (str.equals("oplus_power_save_call_ui_curved_display_state")) {
                        c10 = '\r';
                        break;
                    }
                    break;
                case 1985558518:
                    if (str.equals("oplus_power_save_dtmf_tone")) {
                        c10 = 14;
                        break;
                    }
                    break;
            }
            switch (c10) {
                case 0:
                    PowerSaveUtils.F(this.f18178s, "lockscreen_sounds_enabled", true, false);
                    return;
                case 1:
                    PowerSaveUtils.z(this.f18178s);
                    return;
                case 2:
                    PowerSaveUtils.i(this.f18178s, num.intValue());
                    return;
                case 3:
                    PowerSaveUtils.F(this.f18178s, "disable_screen_capture_sound", false, true);
                    return;
                case 4:
                    PowerSaveUtils.A(this.f18178s, num.intValue() & 1, (num.intValue() & 2) >> 1);
                    return;
                case 5:
                    PowerSaveUtils.F(this.f18178s, "disable_front_finger_sound", false, true);
                    return;
                case 6:
                    PowerSaveUtils.h(this.f18178s);
                    return;
                case 7:
                    PowerSaveUtils.D(this.f18178s);
                    return;
                case '\b':
                    PowerSaveUtils.F(this.f18178s, "global_delete_sound", false, false);
                    return;
                case '\t':
                    r(num.intValue(), false);
                    return;
                case '\n':
                    PowerSaveUtils.F(this.f18178s, "sound_effects_enabled", true, false);
                    return;
                case 11:
                    PowerSaveUtils.G(this.f18178s);
                    return;
                case '\f':
                    PowerSaveUtils.E(this.f18178s, num.intValue() & 1, (num.intValue() & 2) >> 1, (num.intValue() & 4) >> 2, (num.intValue() & 8) >> 3);
                    return;
                case '\r':
                    r(num.intValue(), true);
                    return;
                case 14:
                    PowerSaveUtils.F(this.f18178s, "dtmf_tone", true, false);
                    return;
                default:
                    return;
            }
        }
    }

    private void C() {
        this.f18168i = new e(this.f18179t);
        this.f18178s.getContentResolver().registerContentObserver(Settings.System.getUriFor("competition_event_enable"), false, this.f18168i, 0);
    }

    private void D() {
        this.f18166g = new f(this.f18179t);
        this.f18178s.getContentResolver().registerContentObserver(Settings.Global.getUriFor("power_save_disable_five_g_state"), false, this.f18166g, 0);
    }

    private void E() {
        F();
        D();
        G();
        C();
    }

    private void F() {
        this.f18165f = new d(this.f18178s.getApplicationContext(), this.f18179t);
        this.f18178s.getContentResolver().registerContentObserver(Settings.System.getUriFor("power_save_backlight_switch_state"), false, this.f18165f, 0);
    }

    private void G() {
        this.f18167h = new h(this.f18179t);
        this.f18178s.getContentResolver().registerContentObserver(Settings.Global.getUriFor("power_save_disable_five_g_tips"), false, this.f18167h, 0);
    }

    private void H(String str, HashMap<String, Integer> hashMap) {
        if (this.f18172m && hashMap.get(str).intValue() == 0) {
            return;
        }
        str.hashCode();
        char c10 = 65535;
        switch (str.hashCode()) {
            case -1987198628:
                if (str.equals("oplus_power_save_lockscreen_sounds_enabled")) {
                    c10 = 0;
                    break;
                }
                break;
            case -885675210:
                if (str.equals("oplus_power_save_aod_state")) {
                    c10 = 1;
                    break;
                }
                break;
            case -787364747:
                if (str.equals("oplus_power_save_anim_state")) {
                    c10 = 2;
                    break;
                }
                break;
            case -725675956:
                if (str.equals("oplus_power_save_disable_screen_capture_sound")) {
                    c10 = 3;
                    break;
                }
                break;
            case -600572992:
                if (str.equals("oplus_power_save_aon_state")) {
                    c10 = 4;
                    break;
                }
                break;
            case -376470184:
                if (str.equals("oplus_power_save_disable_front_finger_sound")) {
                    c10 = 5;
                    break;
                }
                break;
            case 644654481:
                if (str.equals("oplus_power_save_keyguard_notice_state")) {
                    c10 = 6;
                    break;
                }
                break;
            case 1206297925:
                if (str.equals("oplus_power_save_global_delete_sound")) {
                    c10 = 7;
                    break;
                }
                break;
            case 1338718844:
                if (str.equals("oplus_power_save_curved_display_state")) {
                    c10 = '\b';
                    break;
                }
                break;
            case 1600040706:
                if (str.equals("oplus_power_save_sound_effects_enabled")) {
                    c10 = '\t';
                    break;
                }
                break;
            case 1721421047:
                if (str.equals("oplus_power_save_sub_aod_state")) {
                    c10 = '\n';
                    break;
                }
                break;
            case 1881570918:
                if (str.equals("oplus_power_save_call_ui_curved_display_state")) {
                    c10 = 11;
                    break;
                }
                break;
            case 1985558518:
                if (str.equals("oplus_power_save_dtmf_tone")) {
                    c10 = '\f';
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                PowerSaveUtils.f(this.f18178s, "lockscreen_sounds_enabled", "oplus_power_save_lockscreen_sounds_enabled", true, false);
                return;
            case 1:
                PowerSaveUtils.t(this.f18178s);
                return;
            case 2:
                PowerSaveUtils.o(this.f18178s);
                return;
            case 3:
                PowerSaveUtils.f(this.f18178s, "disable_screen_capture_sound", "oplus_power_save_disable_screen_capture_sound", false, true);
                return;
            case 4:
                if (n("AONRusSwitch")) {
                    PowerSaveUtils.u(this.f18178s);
                    return;
                }
                return;
            case 5:
                PowerSaveUtils.f(this.f18178s, "disable_front_finger_sound", "oplus_power_save_disable_front_finger_sound", false, true);
                return;
            case 6:
                PowerSaveUtils.x(this.f18178s);
                return;
            case 7:
                PowerSaveUtils.f(this.f18178s, "global_delete_sound", "oplus_power_save_global_delete_sound", false, false);
                return;
            case '\b':
                PowerSaveUtils.w(this.f18178s);
                return;
            case '\t':
                PowerSaveUtils.f(this.f18178s, "sound_effects_enabled", "oplus_power_save_sound_effects_enabled", true, false);
                return;
            case '\n':
                PowerSaveUtils.y(this.f18178s);
                return;
            case 11:
                PowerSaveUtils.v(this.f18178s);
                return;
            case '\f':
                PowerSaveUtils.f(this.f18178s, "dtmf_tone", "oplus_power_save_dtmf_tone", true, false);
                return;
            default:
                return;
        }
    }

    private void J() {
        try {
            boolean z10 = true;
            if (Settings.Global.getInt(this.f18178s.getContentResolver(), "low_power") != 1) {
                z10 = false;
            }
            this.f18170k = z10;
            LocalLog.l("PowerSaveHelper", "mPowerSaveState:" + this.f18170k);
        } catch (Settings.SettingNotFoundException e10) {
            e10.printStackTrace();
        }
    }

    public static PowerSaveHelper m(Context context) {
        if (f18163v == null) {
            synchronized (PowerSaveHelper.class) {
                if (f18163v == null) {
                    f18163v = new PowerSaveHelper(context);
                }
            }
        }
        return f18163v;
    }

    private boolean n(String str) {
        return this.f18164e.get(str) != null && this.f18164e.get(str).intValue() == 1;
    }

    private void o() {
        if (this.f18170k && PowerSaveConfig.f(this.f18178s).k()) {
            Context context = this.f18178s;
            PowerSaveUtils.i(context, PowerSaveConfig.f(context).g().get("oplus_power_save_anim_state").intValue());
            PowerSaveConfig.f(this.f18178s).p();
            PowerSaveUtils.o(this.f18178s);
            this.f18179t.postDelayed(new a(), 1000L);
            PowerSaveConfig.f(this.f18178s).n();
        }
    }

    private void p() {
        Context context = this.f18178s;
        f6.f.A2(context, (this.f18173n && f6.f.e0(context) == 1) ? 1 : 0);
        t(true);
        o();
        s();
    }

    private void q() {
        AppSwitchManager appSwitchManager = this.f18169j;
        if (appSwitchManager != null) {
            appSwitchManager.c(this.f18180u);
        }
        PowerSaveConfig.f(this.f18178s).o(true);
        t(false);
        if (ConfigUpdateUtil.n(this.f18178s).x()) {
            f6.f.F1(this.f18178s);
        }
        this.f18177r = 0;
        PowerSaveConfig.f(this.f18178s).g().forEach(new BiConsumer() { // from class: s8.c
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PowerSaveHelper.this.A((String) obj, (Integer) obj2);
            }
        });
        if (this.f18171l) {
            return;
        }
        PowerSaveConfig.f(this.f18178s).h();
    }

    private void r(int i10, boolean z10) {
        String str;
        LocalLog.a("PowerSaveHelper", "handleCurvedDisplay");
        LocalLog.a("PowerSaveHelper", "isCallUi:" + z10);
        if (i10 == 2) {
            str = "red";
        } else if (i10 == 3) {
            str = "gold";
        } else if (i10 != 4) {
            return;
        } else {
            str = "blue";
        }
        if (z10) {
            PowerSaveUtils.B(this.f18178s, str);
        } else {
            PowerSaveUtils.C(this.f18178s, str);
        }
    }

    private void s() {
        if (this.f18170k && f6.f.g0(this.f18178s) == 1 && this.f18173n) {
            f6.f.H1(this.f18178s);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t(boolean z10) {
        boolean z11 = false;
        if (z10) {
            boolean a12 = f6.f.a1(this.f18178s);
            Context context = this.f18178s;
            if (this.f18170k && this.f18173n && !a12) {
                z11 = true;
            }
            PowerSaveUtils.l(context, z11);
            return;
        }
        PowerSaveUtils.l(this.f18178s, false);
    }

    private void u() {
        PowerSaveConfig.f(this.f18178s).l(this.f18170k);
    }

    private void v() {
        HashMap<String, Integer> hashMap = new HashMap<>(PowerSaveConfig.f(this.f18178s).g());
        PowerSaveUtils.j(this.f18178s, ActivityManager.getCurrentUser());
        AppSwitchManager appSwitchManager = this.f18169j;
        if (appSwitchManager != null) {
            appSwitchManager.b(this.f18180u);
        }
        H("oplus_power_save_aon_state", hashMap);
        if (ConfigUpdateUtil.n(this.f18178s).x()) {
            f6.f.G1(this.f18178s);
        }
        H("oplus_power_save_aod_state", hashMap);
        H("oplus_power_save_sub_aod_state", hashMap);
        H("oplus_power_save_curved_display_state", hashMap);
        H("oplus_power_save_call_ui_curved_display_state", hashMap);
        H("oplus_power_save_anim_state", hashMap);
        H("oplus_power_save_keyguard_notice_state", hashMap);
        H("oplus_power_save_dtmf_tone", hashMap);
        H("oplus_power_save_lockscreen_sounds_enabled", hashMap);
        H("oplus_power_save_disable_screen_capture_sound", hashMap);
        H("oplus_power_save_global_delete_sound", hashMap);
        H("oplus_power_save_disable_front_finger_sound", hashMap);
        H("oplus_power_save_sound_effects_enabled", hashMap);
        t(true);
        w(true);
        if (this.f18172m) {
            this.f18172m = false;
        } else {
            PowerSaveConfig.f(this.f18178s).n();
        }
    }

    private void w(boolean z10) {
        if (this.f18173n && this.f18170k) {
            int i10 = this.f18177r;
            boolean z11 = i10 % 5 == 0 && z10;
            if (z10) {
                this.f18177r = i10 + 1;
            } else {
                this.f18177r = 0;
            }
            if (z11) {
                this.f18179t.post(new b());
                return;
            }
            return;
        }
        this.f18177r = 0;
    }

    private boolean x() {
        if (this.f18170k) {
            UploadDataUtil.S0(this.f18178s).k0(PowerUsageManager.x(this.f18178s).r());
            v();
            u();
            return true;
        }
        u();
        q();
        return true;
    }

    private void y() {
        HandlerThread handlerThread = new HandlerThread("PowerSaveHelper");
        handlerThread.start();
        this.f18179t = new g(handlerThread.getLooper());
        this.f18169j = new AppSwitchManager(this.f18178s);
        J();
        z();
        registerAction();
        E();
        I();
    }

    private void z() {
        int i10 = Settings.Global.getInt(this.f18178s.getContentResolver(), "power_save_disable_five_g_state", -1);
        int i11 = 1;
        if (i10 != 1) {
            if (AppFeature.D()) {
                if (i10 == 0) {
                    return;
                } else {
                    i11 = 0;
                }
            }
            Settings.Global.putInt(this.f18178s.getContentResolver(), "power_save_disable_five_g_state", i11);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0041  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void B(int i10) {
        boolean z10;
        int i11 = this.f18175p;
        if (i11 == i10) {
            return;
        }
        if (i11 < i10 && i10 >= 50) {
            f6.f.F1(this.f18178s);
        }
        int i12 = this.f18175p;
        if (i12 == 20 || i12 == 21 || i12 == 0) {
            if (i12 != 0) {
                if (i12 != 20) {
                    if (i12 == 21) {
                        if (i10 == 20) {
                            this.f18173n = true;
                        } else {
                            this.f18173n = false;
                        }
                    }
                    z10 = false;
                } else if (i10 == 21) {
                    this.f18173n = false;
                } else {
                    this.f18173n = true;
                    z10 = false;
                }
                if (z10) {
                    p();
                    Intent intent = new Intent("oplus.intent.action.BATTERY_LOW_POWER_SAVE");
                    intent.putExtra("isBatteryLow", this.f18173n);
                    this.f18178s.sendBroadcast(intent, "oplus.permission.OPLUS_COMPONENT_SAFE", -1);
                }
            } else {
                this.f18173n = i10 <= 20;
            }
            z10 = true;
            if (z10) {
            }
        }
        w(this.f18175p > i10);
        this.f18175p = i10;
    }

    public void I() {
        HashMap<String, Integer> C1 = f6.f.C1(this.f18178s, true);
        this.f18164e = C1;
        if (C1.size() == 0) {
            this.f18164e.put("AONRusSwitch", 1);
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
        if (i10 != 901) {
            return;
        }
        J();
        x();
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 901);
    }
}
