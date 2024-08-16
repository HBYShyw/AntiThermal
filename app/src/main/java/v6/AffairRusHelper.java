package v6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import b6.LocalLog;
import com.oplus.deepsleep.ControllerCenter;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.thermalcontrol.ThermalControlConfig;
import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import d6.ConfigUpdateUtil;
import g7.OGuardRusHelper;
import ha.StorageConfigManager;
import java.util.ArrayList;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;
import w6.PluginSupporter;
import w6.RegionPluginUtil;
import z8.SuperPowersaveController;

/* compiled from: AffairRusHelper.java */
/* renamed from: v6.c, reason: use source file name */
/* loaded from: classes.dex */
public class AffairRusHelper implements IAffairCallback {

    /* renamed from: h, reason: collision with root package name */
    private static final String f19155h = "c";

    /* renamed from: e, reason: collision with root package name */
    private Context f19156e;

    /* renamed from: f, reason: collision with root package name */
    private ConfigUpdateUtil f19157f;

    /* renamed from: g, reason: collision with root package name */
    private RegionPluginUtil f19158g;

    /* compiled from: AffairRusHelper.java */
    /* renamed from: v6.c$b */
    /* loaded from: classes.dex */
    private static class b {

        /* renamed from: a, reason: collision with root package name */
        private static final AffairRusHelper f19159a = new AffairRusHelper();
    }

    public static AffairRusHelper a() {
        return b.f19159a;
    }

    private void b(ArrayList<String> arrayList) {
        RegionPluginUtil regionPluginUtil;
        ControllerCenter controllerCenter;
        String str = f19155h;
        Log.d(str, "handleConfigUpdateAction");
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        if (arrayList.contains("sys_guardelf_config_list")) {
            LocalLog.a(str, "guardelf config table changed!");
            this.f19157f.U0();
        }
        if (arrayList.contains("sys_guardelf_limitbkg_blacklist")) {
            LocalLog.a(str, "guardelf assistant app black list changed!");
            this.f19157f.Z();
        }
        if (arrayList.contains("sys_pms_adbinstaller_switch")) {
            LocalLog.a(str, "adb installer switch changed!");
            this.f19157f.Q();
        }
        if (arrayList.contains("sys_ams_crashclear_whitelist")) {
            LocalLog.a(str, "crash clear white list changed!");
            this.f19157f.k0();
        }
        if (arrayList.contains("sys_pms_odex_whitelist")) {
            LocalLog.a(str, "third_app_dex_list changed!");
            this.f19157f.F1();
        }
        if (arrayList.contains("sys_wms_intercept_window")) {
            LocalLog.a(str, "sys_wms_intercept_window changed!");
            this.f19157f.E0();
        }
        if (arrayList.contains("sys_ams_skipbroadcast")) {
            LocalLog.a(str, "sys_ams_skipbroadcast changed!");
            this.f19157f.a0();
        }
        if (arrayList.contains("sys_ams_processfilter_list")) {
            LocalLog.a(str, "sys_ams_processfilter_list changed!");
            this.f19157f.a1();
        }
        if (arrayList.contains("sys_startupmanager_monitor_list")) {
            LocalLog.a(str, "sys_startupmanager_monitor_list changed!");
            this.f19157f.j1();
        }
        if (arrayList.contains("sys_secure_keyboard_config")) {
            LocalLog.a(str, "secure keyboard config list changed!");
            n7.a.b(this.f19156e).c();
        }
        if (arrayList.contains("sys_rom_black_list")) {
            LocalLog.a(str, "oplus_rom_black_list changed!");
            this.f19157f.f1();
        }
        if (arrayList.contains("sys_system_config_list")) {
            LocalLog.a(str, "sys_system_config_list changed!");
            this.f19157f.o1();
        }
        if (arrayList.contains("sys_wms_splitapp_list")) {
            LocalLog.a(str, "sys_wms_splitapp_list changed!");
            this.f19157f.q1();
        }
        if (arrayList.contains("sys_display_compat_config")) {
            LocalLog.a(str, "sys_display_compat_config changed!");
            this.f19157f.v0();
        }
        if (arrayList.contains("sys_freeform_config")) {
            LocalLog.a(str, "sys_freeform_config changed!");
            this.f19157f.p1();
        }
        if (arrayList.contains("sys_direct_widget_config_list")) {
            LocalLog.a(str, "sys_direct_widget_config_list changed!");
            this.f19157f.t0();
        }
        if (arrayList.contains("sys_oplus_animation_config")) {
            LocalLog.a(str, "sys_oplus_animation_config changed!");
            this.f19157f.W();
        }
        if (arrayList.contains("formatter_compatibility_config_list")) {
            LocalLog.a(str, "sys_formater_app_compact_config_list changed!");
            this.f19157f.z0();
        }
        if (arrayList.contains("sys_alarm_filterpackages_list") && (controllerCenter = ControllerCenter.getInstance(this.f19156e.getApplicationContext())) != null) {
            controllerCenter.onWhiteListChanged();
        }
        if (arrayList.contains("sys_high_temp_protect")) {
            LocalLog.a(str, "HIGH TEMP PROTECT table changed!");
            this.f19157f.D0();
        }
        if (arrayList.contains("sys_guardelf_google_restriction_list") && (regionPluginUtil = this.f19158g) != null) {
            regionPluginUtil.c();
        }
        if (arrayList.contains("sys_powersave_policy_config_list")) {
            LocalLog.a(str, "powersave config table changed!");
            SuperPowersaveController.Z(this.f19156e).b0();
        }
        if (arrayList.contains("sys_thermal_control_list")) {
            LocalLog.a(str, "thermal control config table changed!");
            ThermalControlConfig.getInstance(this.f19156e).noteThermalControlConfigChange(this.f19156e);
        }
        if (arrayList.contains("sys_thermal_control_list_low_ambient")) {
            LocalLog.a(str, "low ambient thermal control config table changed!");
            ThermalControlConfig.getInstance(this.f19156e).noteAmbientThermalControlConfigChange(this.f19156e, true);
        }
        if (arrayList.contains("sys_thermal_control_list_high_ambient")) {
            LocalLog.a(str, "high ambient thermal control config table changed!");
            ThermalControlConfig.getInstance(this.f19156e).noteAmbientThermalControlConfigChange(this.f19156e, false);
        }
        if (arrayList.contains("sys_thermal_control_list_folding")) {
            LocalLog.a(str, "folding thermal control config table changed!");
            ThermalControlConfig.getInstance(this.f19156e).noteFoldingThermalControlConfigChange(this.f19156e);
        }
        if (arrayList.contains("sys_thermal_control_list_split")) {
            LocalLog.a(str, "split thermal control config table changed!");
            ThermalControlConfig.getInstance(this.f19156e).noteSplitThermalControlConfigChange(this.f19156e);
        }
        if (arrayList.contains(ThermalWindowConfigInfo.ZOOM_WINDOW_RESTRICT_LIST_XML_FILE_ROOT_ITEM)) {
            LocalLog.a(str, "thermal window control config table changed!");
            ThermalControlConfig.getInstance(this.f19156e).noteThermalWindowConfigChange(this.f19156e);
        }
        if (arrayList.contains("sys_pms_defaultpackage_list")) {
            LocalLog.a(str, "sys_pms_defaultpackage_list changed!");
            this.f19157f.s0();
        }
        if (arrayList.contains("sys_extreme_deepsleep_list")) {
            LocalLog.a(str, "sys_forcestop_config changed!");
            this.f19157f.y0();
        }
        if (arrayList.contains("sys_thermal_control_gt_list")) {
            LocalLog.a(str, "GT thermal control config table changed!");
            ThermalControlConfig.getInstance(this.f19156e).noteGTThermalControlConfigChange(this.f19156e);
        }
        if (arrayList.contains("sys_guardelf_cpu_kill_top_list")) {
            LocalLog.a(str, "sys_guardelf_cpu_kill_top_list changed!");
            this.f19157f.e0();
        }
        if (arrayList.contains("power_save_rus_config_list")) {
            LocalLog.a(str, "power_save_config_list changed!");
            this.f19157f.X0();
        }
        if (arrayList.contains("sys_highpower_config_list")) {
            LocalLog.a(str, "sys_highpower_notification_config_list changed!");
            this.f19157f.C0();
        }
        if (arrayList.contains("sys_charge_config_list")) {
            LocalLog.a(str, "sys_highpower_notification_config_list changed!");
            this.f19157f.b0();
        }
        if (arrayList.contains("sys_flash_threshold_config")) {
            LocalLog.a(str, "sys_flash_threshold_config changed!");
            StorageConfigManager.i(this.f19156e, true);
        }
        if (arrayList.contains("sys_oguard_config_list")) {
            LocalLog.a(str, "sys_oguard_config_list changed!");
            OGuardRusHelper.f(this.f19156e).l(false);
        }
    }

    public void c() {
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        ArrayList<String> stringArrayListExtra;
        if (i10 != 219 || (stringArrayListExtra = intent.getStringArrayListExtra("ROM_UPDATE_CONFIG_LIST")) == null || stringArrayListExtra.isEmpty()) {
            return;
        }
        b(stringArrayListExtra);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_VPN);
    }

    private AffairRusHelper() {
        Context c10 = GuardElfContext.e().c();
        this.f19156e = c10;
        this.f19157f = ConfigUpdateUtil.n(c10);
        this.f19158g = PluginSupporter.m().o();
    }
}
