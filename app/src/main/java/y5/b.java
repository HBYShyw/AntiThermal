package y5;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.oplusdevicepolicy.OplusDevicepolicyManager;
import android.util.Log;
import b6.LocalLog;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.osense.OsenseResClient;
import h6.AppFeatureProviderUtils;

/* compiled from: FeatureOption.java */
/* loaded from: classes.dex */
public class b {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f19870a = false;

    /* renamed from: b, reason: collision with root package name */
    private static boolean f19871b = false;

    /* renamed from: c, reason: collision with root package name */
    private static boolean f19872c = false;

    /* renamed from: d, reason: collision with root package name */
    private static boolean f19873d = false;

    /* renamed from: e, reason: collision with root package name */
    private static boolean f19874e = false;

    /* renamed from: f, reason: collision with root package name */
    private static boolean f19875f = false;

    /* renamed from: g, reason: collision with root package name */
    private static boolean f19876g = false;

    /* renamed from: h, reason: collision with root package name */
    private static boolean f19877h = false;

    /* renamed from: i, reason: collision with root package name */
    private static boolean f19878i = false;

    /* renamed from: j, reason: collision with root package name */
    private static boolean f19879j = false;

    /* renamed from: k, reason: collision with root package name */
    private static boolean f19880k = false;

    /* renamed from: l, reason: collision with root package name */
    private static boolean f19881l = false;

    /* renamed from: m, reason: collision with root package name */
    private static boolean f19882m = false;

    /* renamed from: n, reason: collision with root package name */
    private static boolean f19883n = false;

    /* renamed from: o, reason: collision with root package name */
    private static boolean f19884o = false;

    /* renamed from: p, reason: collision with root package name */
    private static boolean f19885p = true;

    /* renamed from: q, reason: collision with root package name */
    private static boolean f19886q = false;

    /* renamed from: r, reason: collision with root package name */
    private static boolean f19887r = false;

    /* renamed from: s, reason: collision with root package name */
    private static boolean f19888s = false;

    /* renamed from: t, reason: collision with root package name */
    private static boolean f19889t = false;

    /* renamed from: u, reason: collision with root package name */
    private static boolean f19890u = false;

    /* renamed from: v, reason: collision with root package name */
    private static boolean f19891v = false;

    /* renamed from: w, reason: collision with root package name */
    private static boolean f19892w = false;

    public static boolean A() {
        return f19873d && f19882m;
    }

    public static boolean B() {
        return f19878i && D();
    }

    public static boolean C() {
        return f19871b;
    }

    public static boolean D() {
        return f19873d;
    }

    public static boolean E() {
        return f19873d && f19882m && ActivityManager.getCurrentUser() == 0;
    }

    public static boolean F() {
        return f19873d && f19889t;
    }

    public static boolean G() {
        return f19873d && f19883n;
    }

    public static boolean H() {
        return f19881l;
    }

    public static boolean I() {
        return UserHandle.myUserId() == 0;
    }

    public static boolean J() {
        return f19886q;
    }

    private static void K(Class cls) {
        OsenseResClient osenseResClient = OsenseResClient.get(cls);
        if (osenseResClient == null) {
            LocalLog.b("FeatureOption", "osenseClient is null");
            return;
        }
        int osenseGetModeStatus = osenseResClient.osenseGetModeStatus(3);
        boolean z10 = true;
        if (osenseGetModeStatus != 0 && osenseGetModeStatus != 1) {
            z10 = false;
        }
        if (AppFeature.D() && !OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.benchmark.support")) {
            if (D()) {
                f19878i = z10;
            }
        } else {
            f19878i = z10;
        }
        LocalLog.a("FeatureOption", "BM Mode Osense Status:" + osenseGetModeStatus + " support:" + f19878i);
    }

    private static void L(Class cls) {
        if (OsenseResClient.get(cls) == null) {
            LocalLog.b("FeatureOption", "osenseClient is null");
            return;
        }
        int osenseGetModeStatus = OsenseResClient.get(cls).osenseGetModeStatus(4);
        boolean z10 = true;
        if (osenseGetModeStatus != 0 && osenseGetModeStatus != 1) {
            z10 = false;
        }
        f19880k = z10;
        LocalLog.a("FeatureOption", "Super PowerSave Mode Osense Status:" + osenseGetModeStatus + " support:" + f19880k);
    }

    public static void a(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        PackageManager packageManager = context.getPackageManager();
        f19874e = packageManager.hasSystemFeature("oplus.multibits.dimming.support");
        f19875f = z(context.getClass());
        f19876g = packageManager.hasSystemFeature("oplus.disable.high.performance.mode");
        f19877h = packageManager.hasSystemFeature("oplus.power.onwirelesscharger.support") || AppFeatureProviderUtils.h(context.getContentResolver(), "oplus.power.onwirelesscharger.support");
        f19887r = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.display.aod_support") && !OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.display.aod_ramless_support");
        f19888s = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.display.dynamic_fps_switch");
        f19884o = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.display.colormode_support");
        f19886q = packageManager.hasSystemFeature("oplus.power.wirelesschgwhenwired.support") || AppFeatureProviderUtils.h(context.getContentResolver(), "oplus.power.wirelesschgwhenwired.support");
        f19885p = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.startup_strategy_restrict");
        String str = SystemProperties.get("ro.build.display.id", (String) null);
        if (str != null && str.contains("ROM") && (str.contains("Beta") || str.contains("Alpha"))) {
            f19872c = true;
        }
        f19873d = AppFeatureProviderUtils.h(context.getContentResolver(), "com.android.settings.device_rm");
        boolean hasFeature = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.support.gt.mode");
        f19882m = hasFeature;
        if (f19873d && hasFeature) {
            packageManager.setComponentEnabledSetting(ComponentName.unflattenFromString("com.oplus.battery/com.oplus.performance.GTModeTile"), 1, 1);
        } else {
            packageManager.setComponentEnabledSetting(ComponentName.unflattenFromString("com.oplus.battery/com.oplus.performance.GTModeTile"), 2, 1);
        }
        f19883n = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.display.reduce_brightness_rm");
        K(context.getClass());
        L(context.getClass());
        f19881l = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.display.dynamic_fps_switch");
        f19871b = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.power.disable_power_save");
        f19879j = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.power.disable_power_save");
        f19889t = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.display.game.memc_optimise_power_mode");
        f19890u = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.forwardly_freeze");
        f19891v = packageManager.hasSystemFeature("oplus.misc.lights.support");
        f19892w = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.display.multi_led_support");
        Log.d("FeatureOption", "FEATURE_CONFIG, interactiveVersion=" + f19872c + ", sRLMDevice=" + f19873d + ", highPerformance supported= " + f19875f + ", featureScreenDimming= " + f19874e + ", featureWirelessChargSupport= " + f19877h + ", sFeatureDisableSuperPowersave= " + f19879j + ", sFeatureWirelessChgSupportWhenWired= " + f19886q + ", sFeatureBMModeSupport " + f19878i + ", sys.custom.whitelist=" + SystemProperties.get("sys.custom.whitelist", "") + ", isSupportRefresh=" + f19881l + ", sFeatureColorModeSupport=" + f19884o + ", disablePowerSave=" + f19871b + ", sFeatureCurvedDisplayModeSupport=" + f19887r + ", sFeatureSupportRMBrightness=" + f19883n + ", disableSuperPowerSave=" + f19879j + ", sFeatureScreenRefreshRateSettingsSupport=" + f19888s + ", sFeatureSupportMemcOptimiseRL=" + f19889t + ", sFeatureRmLightSupport=" + f19891v + ", sFeatureRmMultiLightSupport=" + f19892w);
        if (LocalLog.f()) {
            LocalLog.a("FeatureOption", "featureOption done use time:" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
    }

    public static boolean b() {
        return f19873d;
    }

    public static boolean c() {
        return f19885p;
    }

    public static boolean d() {
        return FeatureUtil.b("oplus.software.display.aod_support");
    }

    public static boolean e() {
        return FeatureUtil.b("oplus.software.aon_enable");
    }

    public static boolean f() {
        return f19878i && !D();
    }

    public static boolean g() {
        return f19884o;
    }

    public static boolean h() {
        return f19887r;
    }

    public static boolean i() {
        return f19876g;
    }

    public static boolean j() {
        return f19879j;
    }

    public static boolean k() {
        boolean z10 = OplusDevicepolicyManager.getInstance().getBoolean("persist.sys.disable_doze_mode", 1, false);
        f19870a = z10;
        return z10;
    }

    public static boolean l() {
        return FeatureUtil.b("oplus.software.fold_remap_display_disabled");
    }

    public static boolean m() {
        return f19873d && f19890u;
    }

    public static boolean n() {
        return f19875f;
    }

    public static boolean o() {
        return f19891v && E();
    }

    public static boolean p() {
        return FeatureUtil.b("oplus.software.display.osie_support");
    }

    public static boolean q() {
        return FeatureUtil.b("oplus.software.display.pixelworks_enable");
    }

    public static boolean r() {
        return f19892w && E();
    }

    public static boolean s() {
        return f19874e;
    }

    public static boolean t() {
        return f19888s;
    }

    public static boolean u() {
        return f19880k;
    }

    public static boolean v() {
        return FeatureUtil.b("oplus.software.video.osie_support");
    }

    public static boolean w() {
        return FeatureUtil.b("oplus.software.video.sr_support");
    }

    public static boolean x() {
        return f19877h;
    }

    public static boolean y() {
        return FeatureUtil.b("oplus.software.general.cooling.back.clip.enable") && D();
    }

    private static boolean z(Class cls) {
        OsenseResClient osenseResClient = OsenseResClient.get(cls);
        if (osenseResClient == null) {
            LocalLog.b("FeatureOption", "osenseClient is null ");
            return false;
        }
        int osenseGetModeStatus = osenseResClient.osenseGetModeStatus(2);
        LocalLog.a("FeatureOption", "High Performance Osense Status = " + osenseGetModeStatus);
        return osenseGetModeStatus == 0 || osenseGetModeStatus == 1;
    }
}
