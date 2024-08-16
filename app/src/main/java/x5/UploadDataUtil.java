package x5;

import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import b6.LocalLog;
import com.oplus.battery.OplusBatteryApp;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.statistics.gen.root_battery.TrackApi_20089;
import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import f6.CommonUtil;
import f6.f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import o9.HighPowerHelper;
import x7.ChargeProtectionUtils;
import y5.AppFeature;
import z5.GuardElfDataManager;

/* compiled from: UploadDataUtil.java */
/* renamed from: x5.a, reason: use source file name */
/* loaded from: classes.dex */
public class UploadDataUtil {

    /* renamed from: d, reason: collision with root package name */
    private static UploadDataUtil f19542d;

    /* renamed from: a, reason: collision with root package name */
    private boolean f19543a = OplusBatteryApp.f9757h;

    /* renamed from: b, reason: collision with root package name */
    private Context f19544b;

    /* renamed from: c, reason: collision with root package name */
    private GuardElfDataManager f19545c;

    private UploadDataUtil(Context context) {
        this.f19545c = null;
        this.f19544b = context;
        this.f19545c = GuardElfDataManager.d(context);
    }

    public static synchronized UploadDataUtil S0(Context context) {
        UploadDataUtil uploadDataUtil;
        synchronized (UploadDataUtil.class) {
            if (f19542d == null) {
                f19542d = new UploadDataUtil(context);
            }
            uploadDataUtil = f19542d;
        }
        return uploadDataUtil;
    }

    private String T0(String str, String str2) {
        String str3 = "";
        if (str.equals("startinfo")) {
            str3 = "[" + CommonUtil.i(System.currentTimeMillis()) + "] ";
        }
        return str3 + "[" + CommonUtil.o(this.f19544b, CommonUtil.G(str2)) + "] " + str2;
    }

    private void U0(String str, int i10) {
        HashMap hashMap = new HashMap();
        hashMap.put("eventCount", String.valueOf(i10));
        TrackApi_20089.CommonTracker.obtain("KVEvent", str).add(hashMap).commit();
    }

    private void V0(String str, Map<String, String> map) {
        TrackApi_20089.CommonTracker.obtain("KVEvent", str).add(map).commit();
    }

    public void A(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("extreme_fast_charge_mode_battery_level", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void A0(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_forget_mechanism_time", str);
        hashMap.put("smart_long_charge_protect_forget_mechanism_gear", str2);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void B(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("extreme_fast_charge_mode_duration", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void B0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_guide_open_jump_time", str);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void C(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("extreme_fast_charge_mode_income", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void C0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_guide_open_is_open", str);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void D(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("extreme_fast_charge_mode_income_notifity_time", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void D0(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_negative_feedback_time", str);
        hashMap.put("smart_long_charge_protect_negative_feedback_gear", str2);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void E(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("extreme_fast_charge_mode_phone_temp", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void E0(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_notification_end_time", str);
        hashMap.put("smart_long_charge_protect_notification_end_times", str2);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void F(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("extreme_fast_charge_mode_time", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void F0(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_satisfy_scene_satify_times", str);
        hashMap.put("smart_long_charge_protect_satisfy_scene_enter_times", str2);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void G(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("extreme_fast_charge_mode_times", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void G0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_satisfy_switch_status", str);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void H(Map<String, String> map) {
        W0("high_performance_mode_dialog", map, false);
    }

    public void H0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_switch_status", str);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void I(Map<String, String> map) {
        W0("high_performance_mode_notify", map, false);
    }

    public void I0(String str, String str2, String str3, String str4, String str5, String str6) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_time", str);
        hashMap.put("smart_long_charge_protect_unplug_times", str2);
        hashMap.put("smart_long_charge_protect_maxY_ms", str3);
        hashMap.put("smart_long_charge_protect_start_charge_level", str4);
        hashMap.put("smart_long_charge_protect_predict_unplug_time", str5);
        hashMap.put("smart_long_charge_protect_charge_time_ms", str6);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void J(Map<String, String> map) {
        W0("high_performance_mode_switch", map, false);
    }

    public void J0(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_unplug_not_full_time", str);
        hashMap.put("smart_long_charge_protect_unplug_not_full_level", str2);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void K() {
        HashMap<String, String> i10 = HighPowerHelper.f(this.f19544b).i();
        LocalLog.a("UDataUtil", "map.size(): " + i10.size());
        if (i10.size() == 0) {
            return;
        }
        for (Map.Entry<String, String> entry : i10.entrySet()) {
            if (entry != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("high_power_notification_data", entry.getKey() + entry.getValue());
                X0("high_power_notification", hashMap);
            }
        }
    }

    public void K0(boolean z10) {
        HashMap hashMap = new HashMap();
        hashMap.put("speedcharge_state", z10 ? "on" : "off");
        W0("speed_charge_switch_change", hashMap, false);
    }

    public void L() {
        HashMap<String, String> j10 = HighPowerHelper.f(this.f19544b).j();
        LocalLog.a("UDataUtil", "map.size(): " + j10.size());
        if (j10.size() == 0) {
            return;
        }
        for (Map.Entry<String, String> entry : j10.entrySet()) {
            if (entry != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("high_power_notification_times", entry.getKey() + entry.getValue());
                X0("high_power_notification", hashMap);
            }
        }
    }

    public void L0(String str, ArrayList<String> arrayList) {
        if (arrayList == null || str == null || arrayList.isEmpty() || !str.equals("startinfo")) {
            return;
        }
        List<String> e10 = this.f19545c.e("startinfo_white.xml");
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            String str2 = arrayList.get(i10);
            String G = CommonUtil.G(str2);
            String T0 = T0(str, str2);
            HashMap hashMap = new HashMap();
            if (T0.contains("abnormal")) {
                hashMap.put("abnormal", T0);
            } else if (T0.contains("warning")) {
                hashMap.put("warning", T0);
            }
            if (e10.contains(G)) {
                hashMap.put(ThermalPolicy.KEY_RESTRICT, "false");
            } else {
                hashMap.put(ThermalPolicy.KEY_RESTRICT, "true");
            }
            V0("startinfo_abnormal", hashMap);
        }
    }

    public void M(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("high_power_notification_reason", str);
        W0("high_power_notification", hashMap, false);
    }

    public void M0(boolean z10, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("superEndurance", String.valueOf(z10));
        hashMap.put("type", str);
        W0("super_endurance_mode_change", hashMap, false);
    }

    public void N(Map<String, String> map) {
        W0("high_temperature_close_zoom_window", map, false);
    }

    public void N0(boolean z10, int i10) {
        HashMap hashMap = new HashMap();
        hashMap.put("clickDialogButton", z10 ? "Open" : "Cancel");
        hashMap.put("currentBattLevel", String.valueOf(i10));
        W0("super_powersave_click_which_button", hashMap, false);
    }

    public void O(Map<String, String> map) {
        W0("high_temperature_safety", map, false);
    }

    public void O0(boolean z10, int i10, long j10) {
        HashMap hashMap = new HashMap();
        hashMap.put("superPowersave", z10 ? "On" : "Off");
        hashMap.put("currentBattLevel", String.valueOf(i10));
        if (!z10 && j10 > 0) {
            hashMap.put("ModeOnDuration", String.valueOf(j10));
        }
        W0("super_powersave_mode_change", hashMap, false);
    }

    public void P(Map<String, String> map) {
        W0("high_temperature_first_step", map, false);
    }

    public void P0(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("data", str2);
        V0(str, hashMap);
    }

    public void Q(Map<String, String> map) {
        W0("high_temperature_shutdown", map, false);
    }

    public void Q0() {
        boolean W = f.W(this.f19544b);
        int v02 = f.v0(this.f19544b);
        int c12 = f.c1(this.f19544b);
        HashMap hashMap = new HashMap();
        hashMap.put("reverseThreshold", String.valueOf(c12));
        hashMap.put("lowPowerCharging", W ? "on" : "off");
        if (v02 == 0) {
            hashMap.put("lowPowerWhenSleep", "on");
        } else if (v02 == 1) {
            hashMap.put("lowPowerAlwaysOn", "on");
        } else if (v02 == 2) {
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            sb2.append(String.format("%02d", Integer.valueOf(f.s(this.f19544b))));
            sb2.append(":");
            sb2.append(String.format("%02d", Integer.valueOf(f.t(this.f19544b))));
            sb3.append(String.format("%02d", Integer.valueOf(f.J(this.f19544b))));
            sb3.append(":");
            sb3.append(String.format("%02d", Integer.valueOf(f.K(this.f19544b))));
            hashMap.put("lowPowerInCustom", "on");
            hashMap.put("beginTimeInCustom", sb2.toString());
            hashMap.put("EndTimeInCustom", sb3.toString());
        }
        W0("wireless_charging_status_data", hashMap, false);
    }

    public void R(Map<String, String> map) {
        W0("high_temperature_threshold", map, false);
    }

    public void R0(boolean z10, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("reverseState", z10 ? "on" : "off");
        hashMap.put("reason", str);
        W0("reverse_switch_change", hashMap, false);
    }

    public void S() {
        HashMap hashMap = new HashMap();
        hashMap.put("eventCount", String.valueOf(1));
        TrackApi_20089.CommonTracker.obtain("KVEvent", "screenon_low_mem_clear").add(hashMap).commit();
    }

    public void T() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_5g", "off");
        W0("one_key_power_save", hashMap, false);
    }

    public void U() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_aod", "off");
        W0("one_key_power_save", hashMap, false);
    }

    public void V() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_aon", "off");
        W0("one_key_power_save", hashMap, false);
    }

    public void W() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_auto_brightness", "on");
        W0("one_key_power_save", hashMap, false);
    }

    public void W0(String str, Map<String, String> map, boolean z10) {
        TrackApi_20089.CommonTracker.obtain("20089", str).add(map).commit();
    }

    public void X() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_auto_close_screen", "30s");
        W0("one_key_power_save", hashMap, false);
    }

    public void X0(String str, Map<String, String> map) {
        TrackApi_20089.CommonTracker.obtain("20089", str).add(map).commit();
    }

    public void Y() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_curved_display", "off");
        W0("one_key_power_save", hashMap, false);
    }

    public void Y0() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("switch", String.valueOf(System.currentTimeMillis()));
        k(hashMap);
    }

    public void Z() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_darkness", "on");
        W0("one_key_power_save", hashMap, false);
    }

    public void a(String str) {
        if (str == null) {
            return;
        }
        int i10 = 0;
        if ("Settings".equals(str)) {
            i10 = 1;
        } else if ("Dialog".equals(str)) {
            i10 = 2;
        } else if ("StatusBar".equals(str)) {
            i10 = 3;
        } else if ("Notification".equals(str)) {
            i10 = 4;
        }
        if (i10 != 0) {
            U0("battery_enter", i10);
            LocalLog.a("UDataUtil", "ActivityEnter eventCount= " + i10 + ", source= " + str);
        }
    }

    public void a0() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_gps", "off");
        W0("one_key_power_save", hashMap, false);
    }

    public void b(Map<String, String> map) {
        W0("deepsleep_ai_predict_statistics", map, false);
    }

    public void b0() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_highperformance", "off");
        W0("one_key_power_save", hashMap, false);
    }

    public void c(Context context, String str, String str2) {
        if (str == null || str.isEmpty()) {
            return;
        }
        if (str.contains("Input")) {
            str = "Input";
        } else if (str.contains("service")) {
            str = "service";
        } else if (str.contains("Broadcast")) {
            str = "Broadcast";
        } else if (str.contains("ContentProvider")) {
            str = "ContentProvider";
        }
        HashMap hashMap = new HashMap();
        hashMap.put("anrType", str);
        hashMap.put("pkgName", str2);
        int I = CommonUtil.I(context, str2);
        hashMap.put("verCode", String.valueOf(I));
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long j10 = (memoryInfo.totalMem / 1024) / 1024;
        long j11 = (memoryInfo.availMem / 1024) / 1024;
        hashMap.put("totalMem", String.valueOf(j10));
        hashMap.put("availMem", String.valueOf(j11));
        boolean z10 = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        hashMap.put("debugMode", String.valueOf(z10));
        V0("anr_event", hashMap);
        LocalLog.a("UDataUtil", "AnrEvent anrType=" + str + ", pkgName=" + str2 + ", totalMem=" + j10 + ", availMem=" + j11 + ", debugMode=" + z10 + ", verCode=" + I);
    }

    public void c0() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_hotspot", "off");
        W0("one_key_power_save", hashMap, false);
    }

    public void d(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("data", str);
        V0("application_click", hashMap);
        LocalLog.a("UDataUtil", "AppClick  pkg= " + str);
    }

    public void d0() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_notification_bright_screen", "off");
        W0("one_key_power_save", hashMap, false);
    }

    public void e(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("data", str2);
        V0(str, hashMap);
    }

    public void e0() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_screen_refresh_rate", "60hz");
        W0("one_key_power_save", hashMap, false);
    }

    public void f(Map<String, String> map) {
        if (AppFeature.D()) {
            return;
        }
        W0("app_standby_statistic", map, false);
        LocalLog.a("UDataUtil", "doUploadAppStandbyStatistic");
    }

    public void f0() {
        HashMap hashMap = new HashMap();
        hashMap.put("one_key_close_sos_earthquake_warning", "off");
        W0("one_key_power_save", hashMap, false);
    }

    public void g(String str, String str2) {
        String str3;
        String[] split = str2.split("\\s+");
        HashMap hashMap = new HashMap();
        String[] strArr = {"caller", "called", "cpnn", "type", "calledAppExist", "callCount"};
        String[] strArr2 = {DeviceDomainManager.ARG_PKG, "cpnn", "isScrnOn", "nowFgPkg", "isInterceptA", "prev5", "gap5", "prev4", "gap4", "prev3", "gap3", "prev2", "gap2", "prev1", "gap1"};
        String[] strArr3 = {"caller", "called", "cpn", "isScrnOn", "top"};
        int i10 = 0;
        String[] strArr4 = new String[0];
        if (str.equals("appcallinfo")) {
            str3 = "startup_app";
        } else if (str.equals("activitycallinfo")) {
            str3 = "activity_call";
            strArr = strArr2;
        } else if (str.equals("activityinterceptinfo")) {
            str3 = "activity_intercept";
            strArr = strArr3;
        } else {
            str3 = "";
            strArr = strArr4;
        }
        while (i10 < split.length) {
            hashMap.put(i10 < strArr.length ? strArr[i10] : "unknown", split[i10]);
            i10++;
        }
        V0(str3, hashMap);
        if (this.f19543a) {
            Log.d("UDataUtil", "U data successfully " + str3 + " " + hashMap.toString());
        }
    }

    public void g0() {
        if (AppFeature.D()) {
            return;
        }
        W0("power_save_click_entry_button", new HashMap(), false);
        LocalLog.a("UDataUtil", "doUploadOneKeyPowerSaveClickEntryButton");
    }

    public void h(Map<String, String> map) {
        W0("bench_mark_dialog_click", map, false);
    }

    public void h0(Map<String, String> map) {
        if (AppFeature.D()) {
            return;
        }
        W0("power_save_click_work_button", map, false);
        LocalLog.a("UDataUtil", "doUploadOneKeyPowerSaveClickWorkButton");
    }

    public void i(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("battery_health_enter_times_daily", str);
        W0("battery_health", hashMap, false);
    }

    public void i0(Map<String, String> map) {
    }

    public void j(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("battery_health_soh_value", str);
        W0("battery_health", hashMap, false);
    }

    public void j0(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("pkgName", str);
        hashMap.put(ThermalWindowConfigInfo.ATTR_POLICY, str2);
        W0("power_protect_policy", hashMap, false);
    }

    public void k(HashMap<String, String> hashMap) {
        W0("battery_percentage_setting_switch", hashMap, false);
    }

    public void k0(int i10) {
        HashMap hashMap = new HashMap();
        hashMap.put("power_save_level", String.valueOf(i10));
        W0("power_save_level", hashMap, false);
        LocalLog.a("UDataUtil", "powerSaveLevel = " + i10);
    }

    public void l(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("create_count", str);
        W0("event_batterycard_create_minus", hashMap, false);
    }

    public void l0() {
        boolean z10 = f.i0(this.f19544b) == 1;
        boolean z11 = f.G(this.f19544b) == 1;
        HashMap hashMap = new HashMap();
        boolean f02 = f.f0(this.f19544b);
        if (f02) {
            hashMap.put("power_save", "on");
        } else {
            hashMap.put("power_save", "off");
        }
        hashMap.put("screen_refresh", z10 ? "on" : "off");
        hashMap.put("disable_five_g", z11 ? "on" : "off");
        hashMap.put("version", "2.0");
        W0("power_save_on", hashMap, false);
        LocalLog.a("UDataUtil", "isPowerSaveOn = " + f02);
    }

    public void m(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("create_count", str);
        W0("event_batterycard_create_desktop", hashMap, false);
    }

    public void m0() {
        HashMap hashMap = new HashMap();
        hashMap.put("power_save_recover", "recover");
        W0("power_save_recover", hashMap, false);
        LocalLog.a("UDataUtil", "powerSaveRecover");
    }

    public void n(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("jump_count", str);
        W0("event_batterycard_enter", hashMap, false);
    }

    public void n0(int i10, boolean z10) {
        HashMap hashMap = new HashMap();
        hashMap.put("eventCount", String.valueOf(i10));
        if (z10) {
            hashMap.put("isHighPowerConsumption", "is");
        } else {
            hashMap.put("isHighPowerConsumption", "not");
        }
        W0("power_usage_graph_detail_self_select", hashMap, false);
        LocalLog.a("UDataUtil", "doUploadPowerUsageSelfSelectCount: " + i10 + " isHighPowerConsumption=" + z10);
    }

    public void o(long j10, long j11, long j12, long j13) {
        HashMap hashMap = new HashMap();
        hashMap.put("chargeProtEnterTime", ChargeProtectionUtils.h(j10));
        hashMap.put("canNotifyTime", ChargeProtectionUtils.h(j11));
        hashMap.put("predictStartTime", ChargeProtectionUtils.h(j12));
        hashMap.put("predictEndTime", ChargeProtectionUtils.h(j13));
        W0("charge_protection_notify_cancle", hashMap, false);
    }

    public void o0() {
        HashMap hashMap = new HashMap();
        hashMap.put("power_usage_graph_by_power", "1");
        W0("power_usage_graph_detail", hashMap, false);
        LocalLog.a("UDataUtil", "doUploadPowerUsageTypeByPower: power_usage_graph_by_power");
    }

    public void p(boolean z10, int i10) {
        HashMap hashMap = new HashMap();
        hashMap.put("chargeProtState", z10 ? "on" : "off");
        hashMap.put("currentBattCapacity", String.valueOf(i10));
        W0("charge_protection_capacity", hashMap, false);
    }

    public void p0() {
        HashMap hashMap = new HashMap();
        hashMap.put("power_usage_graph_by_time", "1");
        W0("power_usage_graph_detail", hashMap, false);
        LocalLog.a("UDataUtil", "doUploadPowerUsageTypeByTime: power_usage_graph_by_time");
    }

    public void q(String str, long j10, long j11, long j12) {
        HashMap hashMap = new HashMap();
        hashMap.put("execPerReason", str);
        hashMap.put("execPerTime", ChargeProtectionUtils.h(j10));
        hashMap.put("predictStartTime", ChargeProtectionUtils.h(j11));
        hashMap.put("predictEndTime", ChargeProtectionUtils.h(j12));
        W0("charge_protection_execute_pertime", hashMap, false);
    }

    public void q0(Map<String, String> map) {
        W0("abnormal_push_beat_data", map, false);
    }

    public void r(String str, long j10, long j11, long j12) {
        HashMap hashMap = new HashMap();
        hashMap.put("execReason", str);
        hashMap.put("execTime", ChargeProtectionUtils.h(j10));
        hashMap.put("predictStartTime", ChargeProtectionUtils.h(j11));
        hashMap.put("predictEndTime", ChargeProtectionUtils.h(j12));
        W0("charge_protection_execute_time", hashMap, false);
    }

    public void r0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("regular_charge_protect_switch_status", str);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void s(String str, long j10, long j11, long j12, long j13) {
        HashMap hashMap = new HashMap();
        hashMap.put("exitPerReason", str);
        hashMap.put("enterTimeRecord", ChargeProtectionUtils.h(j10));
        hashMap.put("exitPerTime", ChargeProtectionUtils.h(j11));
        hashMap.put("predictStartTime", ChargeProtectionUtils.h(j12));
        hashMap.put("predictEndTime", ChargeProtectionUtils.h(j13));
        W0("charge_protection_exit_pertime", hashMap, false);
    }

    public void s0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_charge_mode_switch_battery_level", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void t(HashMap<String, String> hashMap) {
        if (hashMap == null) {
            return;
        }
        V0("cpu_monitor_event", hashMap);
    }

    public void t0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_charge_mode_switch_phone_temp", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void u(Map<String, String> map) {
        W0("deepsleep_network_changing_status", map, false);
    }

    public void u0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_charge_switch_status", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void v(Map<String, String> map) {
        W0("deepsleep_detail_statistics", map, false);
    }

    public void v0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_charge_mode_switch_time", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void w(boolean z10) {
        HashMap hashMap = new HashMap();
        hashMap.put("state", z10 ? "on" : "off");
        W0("deepsleep_switch_change", hashMap, false);
    }

    public void w0(String str, String str2, String str3, String str4, boolean z10) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_charge_protect_info_status", z10 ? "start" : "end");
        hashMap.put("smart_charge_protect_info_time", str);
        hashMap.put("smart_charge_protect_info_screen", str2);
        hashMap.put("smart_charge_protect_info_level", str3);
        hashMap.put("smart_charge_protect_info_ai_data", str4);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void x(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("exit_extreme_fast_charge_mode_level", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void x0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_charge_protect_switch_status", str);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void y(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("exit_extreme_fast_charge_mode_time", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void y0(String str, String str2, String str3) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_deepthinker_total_times", str);
        hashMap.put("smart_long_charge_protect_deepthinker_wrong_times", str2);
        hashMap.put("smart_long_charge_protect_deepthinker_accuracy", str3);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }

    public void z(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("extreme_fast_charge_mode_ai_leave_home_data", str);
        W0("smart_charge_mode", hashMap, false);
    }

    public void z0(String str, String str2, String str3, String str4) {
        HashMap hashMap = new HashMap();
        hashMap.put("smart_long_charge_protect_deepthinker_time_match", str);
        hashMap.put("smart_long_charge_protect_deepthinker_location_match", str2);
        hashMap.put("smart_long_charge_protect_deepthinker_fg_app_match", str3);
        hashMap.put("smart_long_charge_protect_deepthinker_comprehensive_match", str4);
        W0("smart_long_charge_protect_mode", hashMap, false);
    }
}
