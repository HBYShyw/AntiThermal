package y5;

import android.content.Context;
import android.os.SystemProperties;
import b6.LocalLog;
import com.oplus.content.OplusFeatureConfigManager;
import f6.ChargeUtil;
import h6.AppFeatureProviderUtils;
import java.util.ArrayList;
import java.util.List;

/* compiled from: AppFeature.java */
/* renamed from: y5.a, reason: use source file name */
/* loaded from: classes.dex */
public class AppFeature {
    private static boolean A = false;
    private static boolean B = false;
    private static boolean C = false;
    private static boolean D = false;
    private static boolean E = false;
    private static boolean F = false;
    private static boolean G = false;
    private static boolean H = false;
    private static Context I = null;

    /* renamed from: a, reason: collision with root package name */
    private static boolean f19844a = false;

    /* renamed from: b, reason: collision with root package name */
    private static boolean f19845b = false;

    /* renamed from: c, reason: collision with root package name */
    private static boolean f19846c = false;

    /* renamed from: d, reason: collision with root package name */
    private static boolean f19847d = false;

    /* renamed from: e, reason: collision with root package name */
    private static boolean f19848e = false;

    /* renamed from: f, reason: collision with root package name */
    private static boolean f19849f = false;

    /* renamed from: g, reason: collision with root package name */
    private static boolean f19850g = false;

    /* renamed from: h, reason: collision with root package name */
    private static boolean f19851h = false;

    /* renamed from: i, reason: collision with root package name */
    private static boolean f19852i = false;

    /* renamed from: j, reason: collision with root package name */
    private static boolean f19853j = false;

    /* renamed from: k, reason: collision with root package name */
    private static boolean f19854k = false;

    /* renamed from: l, reason: collision with root package name */
    private static boolean f19855l = false;

    /* renamed from: m, reason: collision with root package name */
    private static List<String> f19856m = null;

    /* renamed from: n, reason: collision with root package name */
    private static boolean f19857n = false;

    /* renamed from: o, reason: collision with root package name */
    private static boolean f19858o = false;

    /* renamed from: p, reason: collision with root package name */
    private static boolean f19859p = false;

    /* renamed from: q, reason: collision with root package name */
    private static int f19860q = 0;

    /* renamed from: r, reason: collision with root package name */
    private static boolean f19861r = false;

    /* renamed from: s, reason: collision with root package name */
    private static int f19862s = 0;

    /* renamed from: t, reason: collision with root package name */
    private static boolean f19863t = false;

    /* renamed from: u, reason: collision with root package name */
    private static boolean f19864u = false;

    /* renamed from: v, reason: collision with root package name */
    private static int f19865v = -1;

    /* renamed from: w, reason: collision with root package name */
    private static boolean f19866w = true;

    /* renamed from: x, reason: collision with root package name */
    private static boolean f19867x = false;

    /* renamed from: y, reason: collision with root package name */
    private static boolean f19868y = false;

    /* renamed from: z, reason: collision with root package name */
    private static boolean f19869z = false;

    public static boolean A() {
        return f19847d;
    }

    public static boolean B() {
        return D;
    }

    public static boolean C() {
        return b.D() && G;
    }

    public static boolean D() {
        return f19846c;
    }

    public static boolean E() {
        return b.D() && H;
    }

    public static boolean F() {
        if (n()) {
            return false;
        }
        return B;
    }

    public static boolean G() {
        return E;
    }

    public static boolean H() {
        return f19849f;
    }

    public static List<String> a() {
        ArrayList arrayList;
        List<String> list = f19856m;
        if (list == null) {
            return null;
        }
        synchronized (list) {
            arrayList = new ArrayList(f19856m);
        }
        return arrayList;
    }

    public static void b(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        I = context;
        f19844a = AppFeatureProviderUtils.h(context.getContentResolver(), "com.oplus.battery.cabc_level_dynamic_enable");
        f19845b = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.enable_hidden_api_collect");
        f19846c = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.one_key_power_save", true);
        f19847d = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.special_hightemp", false);
        f19848e = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.special_aging", false);
        SystemProperties.getInt("ro.product.first_api_level", 0);
        f19849f = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.power_upgrade_product", false);
        if (SystemProperties.get("ro.oplus.image.my_engineering.type", "release").equals("factory")) {
            f19857n = true;
        }
        f19850g = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.support_abnormal");
        f19851h = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.thermal_tolerance", false);
        f19852i = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.thermal_diff", false);
        f19853j = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.thermal_distinguish_env_temp", false);
        f19854k = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.thermal_temp_src_motherboard", false);
        f19855l = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.disable_deep_sleep", false);
        f19856m = AppFeatureProviderUtils.f(context.getContentResolver(), "com.oplus.battery.allow_bg_runable");
        f19859p = AppFeatureProviderUtils.h(context.getContentResolver(), "com.oplus.battery.smart.charge.mode");
        f19860q = AppFeatureProviderUtils.d(context.getContentResolver(), "com.oplus.battery.smart.charge.mode", 0);
        f19858o = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.disable_vowifi", false);
        f19861r = AppFeatureProviderUtils.h(context.getContentResolver(), "com.oplus.battery.life.mode.notificate");
        f19862s = AppFeatureProviderUtils.d(context.getContentResolver(), "com.oplus.battery.life.mode.notificate", 0);
        f19863t = AppFeatureProviderUtils.h(context.getContentResolver(), "os.charge.settings.batterysettings.batteryhealth");
        f19864u = AppFeatureProviderUtils.h(context.getContentResolver(), "com.oplus.battery.wireless.charging.notificate");
        f19865v = AppFeatureProviderUtils.d(context.getContentResolver(), "com.oplus.battery.wireless.charging.notificate", -1);
        f19867x = AppFeatureProviderUtils.h(context.getContentResolver(), "os.charge.settings.wirelesscharge.support");
        f19868y = AppFeatureProviderUtils.h(context.getContentResolver(), "os.charge.settings.smartchargeswitch.open");
        f19869z = AppFeatureProviderUtils.h(context.getContentResolver(), "os.charge.settings.smartchargeswitch.80w");
        A = AppFeatureProviderUtils.h(context.getContentResolver(), "com.android.systemui.support.show_charging_wattage");
        B = AppFeatureProviderUtils.c(context.getContentResolver(), "com.oplus.battery.speed_charge_mode", false);
        D = AppFeatureProviderUtils.h(context.getContentResolver(), "os.charge.settings.support.close_life_mode");
        E = AppFeatureProviderUtils.h(context.getContentResolver(), "os.charge.super.endurance.mode.support");
        G = AppFeatureProviderUtils.h(context.getContentResolver(), "com.oplus.battery.customize_charge_mode");
        if (SystemProperties.getInt("ro.oplus.radio.hide_nr_switch", 0) == 1) {
            C = true;
        }
        F = AppFeatureProviderUtils.h(context.getContentResolver(), "com.android.launcher.TASKBAR_ENABLE");
        H = AppFeatureProviderUtils.h(context.getContentResolver(), "com.oplus.battery.support.smart_refresh");
        LocalLog.l("AppFeature", "CabcLevelDynamicEnable= " + f19844a + ", EnableHiddenApiCollect=" + f19845b + ", FeatureExp= " + f19846c + ", sFeatureHighTemp= " + f19847d + ", sFeatureAging= " + f19848e + ", sFeatureIsUpgradeProduct= " + f19849f + ", FeatureFactory= " + f19857n + ", sFeatureSupportAbnormal= " + f19850g + ", FeatureTolerance=" + f19851h + ", FeatureDiff=" + f19852i + ", sSetTempThresholdForNewStrategy " + f19853j + ", sGetTempFromMotherboard " + f19854k + ", sDeepSleepDisabled " + f19855l + ", ListCustAllowBgRunable " + f19856m + ", sDisableVoWifiDeepSleep " + f19858o + ", sFeatureIsLifeMode " + f19861r + ", sFeatureIsLifeModeValue " + f19862s + ", sWirelessChargeNotificate " + f19864u + ", sWirelessChargeNotificateType " + f19865v + ", sFeatureBatteryHealth " + f19863t + ", sFeatureSmartChargeMode " + f19859p + ", sFeatureIsSmartChargeModeValue " + f19860q + ", sSmartLongChargeProtection" + f19866w + ", sFeatureSupportWirlessCharge" + f19867x + ", sFeatureIsSmartChargeSwitchOpenDefault=" + f19868y + ", sFeatureSmartChargeFor80W=" + f19869z + ", sFeatureDartToVooc " + A + ", sFeatureSupportSpeedCharge " + B + ", sFeatureHideNrSwitch " + C + ", sSupportCloseLifeMode " + D + ", sFeatureSuperEnduranceModeSupport " + E + ", sFeatureTaskbarEnable " + F + ", sSupportCustomizeChargeMode " + G + ", sSupportSmartRefresh " + H);
        if (LocalLog.f()) {
            LocalLog.a("AppFeature", "appfeature done use time:" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
    }

    public static boolean c() {
        return f19844a;
    }

    public static boolean d() {
        return f19863t;
    }

    public static boolean e() {
        return A;
    }

    public static boolean f() {
        return f19855l;
    }

    public static boolean g() {
        LocalLog.l("AppFeature", "isFeatureDisableVoWifiDeepSleep " + f19858o);
        return f19858o;
    }

    public static boolean h() {
        return f19857n;
    }

    public static boolean i() {
        return C;
    }

    public static boolean j() {
        return f19861r;
    }

    public static int k() {
        return f19862s;
    }

    public static boolean l() {
        if (o()) {
            return true;
        }
        return f19868y;
    }

    public static boolean m() {
        return FeatureUtil.b("oplus.hardware.type.tablet");
    }

    public static boolean n() {
        if (b.D() && f19859p && f19860q == 0) {
            return false;
        }
        return (f19859p && f19860q == 2) || ChargeUtil.k(I) >= 100 || f19869z;
    }

    public static boolean o() {
        return f19869z;
    }

    public static boolean p() {
        return n() && !f19869z;
    }

    public static boolean q() {
        f19866w = true;
        return true;
    }

    public static boolean r() {
        return f19867x;
    }

    public static boolean s() {
        return F;
    }

    public static boolean t() {
        return f19852i;
    }

    public static boolean u() {
        return f19851h;
    }

    public static boolean v() {
        if (h()) {
            return false;
        }
        return f19864u;
    }

    public static int w() {
        if (h()) {
            return -1;
        }
        return f19865v;
    }

    public static boolean x() {
        return f19854k;
    }

    public static boolean y() {
        return f19853j;
    }

    public static boolean z() {
        return f19848e;
    }
}
