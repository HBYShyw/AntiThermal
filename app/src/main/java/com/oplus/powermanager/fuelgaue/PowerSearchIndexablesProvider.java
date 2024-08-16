package com.oplus.powermanager.fuelgaue;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.SearchIndexableResource;
import android.provider.SearchIndexablesContract;
import android.provider.SearchIndexablesProvider;
import android.util.Log;
import com.oplus.battery.R;
import com.oplus.performance.GTModeBroadcastReceiver;
import f6.f;
import y5.AppFeature;

/* loaded from: classes.dex */
public class PowerSearchIndexablesProvider extends SearchIndexablesProvider {

    /* renamed from: f, reason: collision with root package name */
    private static int[][] f10137f = {new int[]{R.string.power_usage_details}, new int[]{R.string.power_usage_details, R.string.battery_ui_optimization_more_settings_new}, new int[]{R.string.power_usage_details, R.string.power_save_mode_title}, new int[]{R.string.power_usage_details, R.string.wireless_reverse_charging_title}, new int[]{R.string.power_usage_details, R.string.wireless_charging_settings}, new int[]{R.string.power_usage_details, R.string.battery_health_title}};

    /* renamed from: g, reason: collision with root package name */
    private static String[] f10138g = {"android.intent.action.MAIN", "com.oplus.battery.IntellPowerSaveScence", "oplus.intent.action.PowerSaveActivity", "android.service.quicksettings.action.QS_TILE_PREFERENCES", "oplus.intent.action.WirelessChargingSettingsActivity", "oplus.intent.action.BATTERY_HEALTH"};

    /* renamed from: h, reason: collision with root package name */
    private static String[] f10139h = {PowerConsumptionActivity.class.getName(), IntellPowerSaveScence.class.getName(), PowerSaveActivity.class.getName(), WirelessReverseChargingActivity.class.getName(), WirelessChargingSettingsActivity.class.getName(), BatteryHealthActivity.class.getName()};

    /* renamed from: i, reason: collision with root package name */
    private static SearchIndexableResource[] f10140i = {new SearchIndexableResource(1, R.xml.pm_consumption_summary, (String) null, R.drawable.ic_launcher_pwrmgr), new SearchIndexableResource(2, R.xml.intell_powe_save_scene, (String) null, R.drawable.ic_launcher_pwrmgr), new SearchIndexableResource(3, R.xml.power_save_preference, (String) null, R.drawable.ic_launcher_pwrmgr), new SearchIndexableResource(4, R.xml.wireless_reverse_scene, (String) null, R.drawable.ic_launcher_pwrmgr), new SearchIndexableResource(5, R.xml.wireless_settings_scene, (String) null, R.drawable.ic_launcher_pwrmgr), new SearchIndexableResource(6, R.xml.battery_health_preference, (String) null, R.drawable.ic_launcher_pwrmgr)};

    /* renamed from: e, reason: collision with root package name */
    private boolean f10141e;

    private void a(MatrixCursor matrixCursor) {
        Context context = getContext();
        Object[] objArr = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr[0] = 2;
        objArr[1] = context.getString(R.string.battery_ui_power_optimization);
        objArr[4] = null;
        objArr[6] = context.getString(R.string.power_usage_details) + ";" + context.getString(R.string.app_power_consumer_title_new);
        objArr[7] = PowerInspectActivity.class.getName();
        Integer valueOf = Integer.valueOf(R.drawable.ic_launcher_pwrmgr);
        objArr[8] = valueOf;
        objArr[9] = "com.oplus.battery.PowerInspectActivity";
        objArr[10] = "com.oplus.battery";
        objArr[11] = PowerInspectActivity.class.getName();
        objArr[12] = "one_key_power_save";
        objArr[13] = -1;
        if (y5.b.I()) {
            matrixCursor.addRow(objArr);
        }
        Object[] objArr2 = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr2[0] = 2;
        objArr2[1] = context.getString(R.string.power_save_setting_switch_backlight);
        objArr2[4] = null;
        objArr2[6] = context.getString(R.string.power_usage_details) + ";" + context.getString(R.string.power_save_mode_title) + ";" + context.getString(R.string.power_save_default_optimization_new);
        objArr2[7] = PowerSaveActivity.class.getName();
        objArr2[7] = PowerSaveSecondActivity.class.getName();
        objArr2[8] = valueOf;
        objArr2[9] = "oplus.intent.action.PowerSaveSecondActivity";
        objArr2[10] = "com.oplus.battery";
        objArr2[11] = PowerSaveSecondActivity.class.getName();
        objArr2[12] = "screen_bright_switch";
        objArr2[13] = -1;
        matrixCursor.addRow(objArr2);
        Object[] objArr3 = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr3[0] = 2;
        objArr3[1] = context.getString(R.string.power_save_setting_switch_screenoff_time);
        objArr3[4] = null;
        objArr3[6] = context.getString(R.string.power_usage_details) + ";" + context.getString(R.string.power_save_mode_title) + ";" + context.getString(R.string.power_save_default_optimization_new);
        objArr3[7] = PowerSaveActivity.class.getName();
        objArr3[7] = PowerSaveSecondActivity.class.getName();
        objArr3[8] = valueOf;
        objArr3[9] = "oplus.intent.action.PowerSaveSecondActivity";
        objArr3[10] = "com.oplus.battery";
        objArr3[11] = PowerSaveSecondActivity.class.getName();
        objArr3[12] = "screen_auto_off_switch";
        objArr3[13] = -1;
        matrixCursor.addRow(objArr3);
        Object[] objArr4 = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr4[0] = 2;
        objArr4[1] = context.getString(R.string.power_save_setting_switch_sync);
        objArr4[4] = null;
        objArr4[6] = context.getString(R.string.power_usage_details) + ";" + context.getString(R.string.power_save_mode_title) + ";" + context.getString(R.string.power_save_default_optimization_new);
        objArr4[7] = PowerSaveActivity.class.getName();
        objArr4[7] = PowerSaveSecondActivity.class.getName();
        objArr4[8] = valueOf;
        objArr4[9] = "oplus.intent.action.PowerSaveSecondActivity";
        objArr4[10] = "com.oplus.battery";
        objArr4[11] = PowerSaveSecondActivity.class.getName();
        objArr4[12] = "back_synchronize_switch";
        objArr4[13] = -1;
        matrixCursor.addRow(objArr4);
        Object[] objArr5 = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr5[0] = 2;
        objArr5[1] = context.getString(R.string.power_save_five_g);
        objArr5[4] = null;
        objArr5[6] = context.getString(R.string.power_usage_details) + ";" + context.getString(R.string.power_save_mode_title) + ";" + context.getString(R.string.power_save_default_optimization_new);
        objArr5[7] = PowerSaveActivity.class.getName();
        objArr5[7] = PowerSaveSecondActivity.class.getName();
        objArr5[8] = valueOf;
        objArr5[9] = "oplus.intent.action.PowerSaveSecondActivity";
        objArr5[10] = "com.oplus.battery";
        objArr5[11] = PowerSaveSecondActivity.class.getName();
        objArr5[12] = "five_g_switch";
        objArr5[13] = -1;
        matrixCursor.addRow(objArr5);
        Object[] objArr6 = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr6[0] = 2;
        if (Build.BRAND.equals("OnePlus")) {
            objArr6[1] = "";
        } else {
            objArr6[1] = context.getString(R.string.charging_base_settings_note1, context.getString(R.string.charging_base_settings_note2));
        }
        objArr6[4] = null;
        objArr6[6] = context.getString(R.string.power_usage_details) + ";" + context.getString(R.string.wireless_charging_settings);
        objArr6[7] = WirelessChargingSettingsActivity.class.getName();
        objArr6[8] = valueOf;
        objArr6[9] = "oplus.intent.action.WirelessChargingSettingsActivity";
        objArr6[10] = "com.oplus.battery";
        objArr6[11] = WirelessChargingSettingsActivity.class.getName();
        objArr6[12] = "wireless_charging_note_pref";
        objArr6[13] = -1;
        matrixCursor.addRow(objArr6);
        Object[] objArr7 = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr7[0] = 2;
        objArr7[1] = context.getString(R.string.power_save_screen_refresh);
        objArr7[4] = null;
        objArr7[6] = context.getString(R.string.power_usage_details) + ";" + context.getString(R.string.power_save_mode_title) + ";" + context.getString(R.string.power_save_default_optimization_new);
        objArr7[7] = PowerSaveSecondActivity.class.getName();
        objArr7[8] = valueOf;
        objArr7[9] = "oplus.intent.action.PowerSaveSecondActivity";
        objArr7[10] = "com.oplus.battery";
        objArr7[11] = PowerSaveSecondActivity.class.getName();
        objArr7[12] = "decrease_screen_refresh";
        objArr7[13] = -1;
        matrixCursor.addRow(objArr7);
        Object[] objArr8 = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr8[0] = 2;
        objArr8[1] = context.getString(R.string.power_save_default_optimization_new);
        objArr8[4] = null;
        objArr8[6] = context.getString(R.string.power_usage_details) + ";" + context.getString(R.string.power_save_mode_title) + ";" + context.getString(R.string.power_save_default_optimization_new);
        objArr8[7] = PowerSaveSecondActivity.class.getName();
        objArr8[8] = valueOf;
        objArr8[9] = "oplus.intent.action.PowerSaveSecondActivity";
        objArr8[10] = "com.oplus.battery";
        objArr8[11] = PowerSaveSecondActivity.class.getName();
        objArr8[12] = "default_optimization_category";
        objArr8[13] = -1;
        matrixCursor.addRow(objArr8);
        Object[] objArr9 = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr9[0] = 2;
        objArr9[1] = context.getString(R.string.power_consumption_optimization_title);
        objArr9[4] = null;
        objArr9[6] = context.getString(R.string.power_usage_details) + ";" + context.getString(R.string.battery_ui_optimization_more_settings_new);
        objArr9[7] = IntellPowerSaveScence.class.getName();
        objArr9[8] = valueOf;
        objArr9[9] = "com.oplus.battery.IntellPowerSaveScence";
        objArr9[10] = "com.oplus.battery";
        objArr9[11] = IntellPowerSaveScence.class.getName();
        objArr9[12] = "power_consumption_optimization";
        objArr9[13] = -1;
        matrixCursor.addRow(objArr9);
        Log.d("PowerSearchIndexablesProvider", "addItem: ");
    }

    private static String b(Context context, int[] iArr) {
        StringBuilder sb2 = new StringBuilder();
        int length = iArr.length;
        for (int i10 = 0; i10 < length; i10++) {
            sb2.append(context.getString(iArr[i10]));
            if (i10 != length - 1) {
                sb2.append(";");
            }
        }
        return sb2.toString();
    }

    private String c(String str, boolean z10) {
        StringBuilder sb2 = new StringBuilder(str);
        sb2.append(";");
        sb2.append(z10 ? "enable" : "disable");
        return sb2.toString();
    }

    public boolean onCreate() {
        Log.d("PowerSearchIndexablesProvider", "onCreate");
        return true;
    }

    public Cursor queryNonIndexableKeys(String[] strArr) {
        int i10;
        MatrixCursor matrixCursor = new MatrixCursor(SearchIndexablesContract.NON_INDEXABLES_KEYS_COLUMNS);
        Context context = getContext();
        boolean z10 = !((BatteryManager) context.getSystemService("batterymanager")).isCharging();
        boolean isPowerSaveMode = ((PowerManager) context.getSystemService("power")).isPowerSaveMode();
        boolean W = f.W(context);
        boolean x10 = y5.b.x();
        boolean H = y5.b.H();
        if (f.v(context) && !AppFeature.h() && !AppFeature.q()) {
            this.f10141e = true;
        }
        Object[] objArr = new Object[1];
        boolean z11 = (f.m(context) || isPowerSaveMode) ? false : true;
        objArr[0] = c("app_power_save_settings_scene", z11);
        matrixCursor.addRow(objArr);
        Object[] objArr2 = new Object[1];
        boolean B = y5.b.B();
        if (y5.b.A()) {
            B = false;
        }
        boolean z12 = (!y5.b.n() || y5.b.i() || B) ? false : true;
        if (y5.b.A()) {
            z12 = false;
        }
        objArr2[0] = c("high_performance_switch_in_more", z12);
        matrixCursor.addRow(objArr2);
        matrixCursor.addRow(new Object[]{c("screen_save_preference", AppFeature.c())});
        boolean I = y5.b.I();
        Object[] objArr3 = new Object[1];
        Object[] objArr4 = new Object[1];
        Object[] objArr5 = new Object[1];
        if (y5.b.D()) {
            objArr3[0] = c("battery_percentage_setting_switch", false);
        } else {
            objArr3[0] = c("battery_percentage_setting_switch", true);
        }
        objArr4[0] = c("auto_power_protect_switch", I);
        objArr5[0] = c("intelligent_power_saving_scene_pref", false);
        matrixCursor.addRow(objArr3);
        matrixCursor.addRow(objArr4);
        matrixCursor.addRow(objArr5);
        matrixCursor.addRow(new Object[]{c("power_save_preference", !f.n1(context))});
        Object[] objArr6 = {c("one_key_use_time_after_charge", z10)};
        Object[] objArr7 = {c("one_key_screen_time_after_charge", z10)};
        Object[] objArr8 = {c("one_key_use_time_left", z10)};
        matrixCursor.addRow(objArr6);
        matrixCursor.addRow(objArr7);
        matrixCursor.addRow(objArr8);
        Object[] objArr9 = {c("screen_bright_switch", true)};
        Object[] objArr10 = {c("screen_auto_off_switch", true)};
        Object[] objArr11 = {c("back_synchronize_switch", true)};
        matrixCursor.addRow(objArr9);
        matrixCursor.addRow(objArr10);
        matrixCursor.addRow(objArr11);
        matrixCursor.addRow(new Object[]{c("performance_mode_in_more", B)});
        matrixCursor.addRow(new Object[]{c("app_quick_freeze_pref", false)});
        matrixCursor.addRow(new Object[]{c("app_power_save_settings", false)});
        if (!y5.b.m()) {
            matrixCursor.addRow(new Object[]{c("app_freeze_pref", false)});
        }
        Object[] objArr12 = new Object[1];
        Object[] objArr13 = new Object[1];
        objArr12[0] = c("sleep_optimize_pref", W && AppFeature.w() != 1);
        objArr13[0] = c("custom_optimize_pref", W && AppFeature.w() != 1);
        matrixCursor.addRow(objArr12);
        matrixCursor.addRow(objArr13);
        Object[] objArr14 = new Object[1];
        objArr14[0] = c("wireless_charging_note_pref", x10 && I && AppFeature.w() != 1);
        matrixCursor.addRow(objArr14);
        matrixCursor.addRow(new Object[]{c("wireless_reverse_charging_switch", false)});
        matrixCursor.addRow(new Object[]{c("wireless_charging_settings_pref", false)});
        Object[] objArr15 = new Object[1];
        objArr15[0] = c("slient_alwayson_pref", W && AppFeature.w() != 1);
        matrixCursor.addRow(objArr15);
        Object[] objArr16 = new Object[1];
        objArr16[0] = c("super_power_save_switch_pref", !f.r1(context) && I);
        matrixCursor.addRow(objArr16);
        matrixCursor.addRow(new Object[]{c("default_optimization_category", false)});
        matrixCursor.addRow(new Object[]{c("decrease_screen_refresh", H)});
        matrixCursor.addRow(new Object[]{c("power_save_switch", false)});
        matrixCursor.addRow(new Object[]{c("power_consumption_optimization", true)});
        matrixCursor.addRow(new Object[]{c("high_performance_switch", false)});
        matrixCursor.addRow(new Object[]{c("power_control_pref", I)});
        if (y5.b.D()) {
            matrixCursor.addRow(new Object[]{c("intelligent_deep_sleep_mode", false)});
            matrixCursor.addRow(new Object[]{c("intelligent_rm_sleep_mode", I)});
        } else {
            matrixCursor.addRow(new Object[]{c("intelligent_deep_sleep_mode", I)});
            matrixCursor.addRow(new Object[]{c("intelligent_rm_sleep_mode", false)});
        }
        if (y5.b.E() && GTModeBroadcastReceiver.j(getContext())) {
            matrixCursor.addRow(new Object[]{c("rm_gt_mode_switch_pref", true)});
        } else {
            matrixCursor.addRow(new Object[]{c("rm_gt_mode_switch_pref", false)});
        }
        matrixCursor.addRow(new Object[]{c("power_comsumption_ranking_pref", I)});
        if (AppFeature.F()) {
            matrixCursor.addRow(new Object[]{c("speed_charge_switch_pref", true)});
        } else {
            matrixCursor.addRow(new Object[]{c("speed_charge_switch_pref", false)});
        }
        if (f.k1(context)) {
            matrixCursor.addRow(new Object[]{c("five_g_switch", true)});
        } else {
            matrixCursor.addRow(new Object[]{c("five_g_switch", false)});
        }
        if (AppFeature.d()) {
            matrixCursor.addRow(new Object[]{c("battery_health_preference", true)});
        } else {
            matrixCursor.addRow(new Object[]{c("battery_health_preference", false)});
        }
        if (AppFeature.p() && I) {
            matrixCursor.addRow(new Object[]{c("smart_charge_preference", true)});
        } else {
            matrixCursor.addRow(new Object[]{c("smart_charge_preference", false)});
        }
        if (AppFeature.p() && I) {
            matrixCursor.addRow(new Object[]{c("smart_charge_switch", true)});
        } else {
            matrixCursor.addRow(new Object[]{c("smart_charge_switch", false)});
        }
        if (AppFeature.q() && context.getUserId() == 0) {
            matrixCursor.addRow(new Object[]{c("smart_charge_protection_switch_in_more", !AppFeature.d())});
        } else {
            matrixCursor.addRow(new Object[]{c("smart_charge_protection_switch_in_more", false)});
        }
        if (AppFeature.q() && context.getUserId() == 0) {
            matrixCursor.addRow(new Object[]{c("smart_charge_protection_switch_in_health", AppFeature.d())});
        } else {
            matrixCursor.addRow(new Object[]{c("smart_charge_protection_switch_in_health", false)});
        }
        matrixCursor.addRow(new Object[]{c("wireless_charging_guide_pref", AppFeature.v())});
        Log.d("PowerSearchIndexablesProvider", "queryNonIndexableKeys: canSearchCustomPowerSave=" + z11);
        matrixCursor.addRow(new Object[]{c("life", AppFeature.j())});
        matrixCursor.addRow(new Object[]{c("default_power_optimization", f.n1(context) ^ true)});
        Object[] objArr17 = new Object[1];
        objArr17[0] = c("low_power_charging_pref", x10 && AppFeature.w() != 1);
        matrixCursor.addRow(objArr17);
        if (AppFeature.D() && context.getUserId() == 0) {
            i10 = 1;
            matrixCursor.addRow(new Object[]{c("regular_charge_protection_switch_in_more", !AppFeature.d())});
        } else {
            i10 = 1;
            matrixCursor.addRow(new Object[]{c("regular_charge_protection_switch_in_more", false)});
        }
        if (AppFeature.D() && context.getUserId() == 0) {
            Object[] objArr18 = new Object[i10];
            objArr18[0] = c("regular_charge_protection_switch_in_health", AppFeature.d());
            matrixCursor.addRow(objArr18);
        } else {
            Object[] objArr19 = new Object[i10];
            objArr19[0] = c("regular_charge_protection_switch_in_health", false);
            matrixCursor.addRow(objArr19);
        }
        if (AppFeature.C()) {
            Object[] objArr20 = new Object[i10];
            objArr20[0] = c("customize_charge_switch", f.J0(context));
            matrixCursor.addRow(objArr20);
        }
        return matrixCursor;
    }

    public Cursor queryRawData(String[] strArr) {
        MatrixCursor matrixCursor = new MatrixCursor(SearchIndexablesContract.INDEXABLES_RAW_COLUMNS);
        Log.d("PowerSearchIndexablesProvider", "queryRawData");
        a(matrixCursor);
        return matrixCursor;
    }

    public Cursor queryXmlResources(String[] strArr) {
        MatrixCursor matrixCursor = new MatrixCursor(SearchIndexablesContract.INDEXABLES_XML_RES_COLUMNS);
        boolean x10 = y5.b.x();
        boolean I = y5.b.I();
        boolean d10 = AppFeature.d();
        Context context = getContext();
        int length = f10140i.length;
        for (int i10 = 0; i10 < length; i10++) {
            if (((i10 != 3 && i10 != 4) || x10) && ((i10 != 4 || I) && (i10 != 5 || d10))) {
                Object[] objArr = new Object[SearchIndexablesContract.INDEXABLES_XML_RES_COLUMNS.length];
                objArr[0] = Integer.valueOf(f10140i[i10].rank);
                objArr[1] = Integer.valueOf(f10140i[i10].xmlResId);
                objArr[2] = b(context, f10137f[i10]);
                objArr[3] = Integer.valueOf(f10140i[i10].iconResId);
                objArr[4] = f10138g[i10];
                objArr[5] = "com.oplus.battery";
                objArr[6] = f10139h[i10];
                matrixCursor.addRow(objArr);
            }
        }
        return matrixCursor;
    }
}
