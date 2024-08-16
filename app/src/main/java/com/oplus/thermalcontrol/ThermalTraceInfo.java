package com.oplus.thermalcontrol;

import com.oplus.statistics.util.TimeInfoUtil;
import com.oplus.thermalcontrol.ThermalControlUtils;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class ThermalTraceInfo {
    public final int ambient_state;
    public final int ambient_temperature;
    public final List<ThermalControlUtils.WindowInfo> appFloatingWindowsInfo;
    public final boolean app_switch_safety_mode;
    public final ThermalPolicy currPolicy;
    public final int curr_env_temp_type;
    public final boolean curr_plug_in;
    public final boolean curr_screen_on;
    public final String fg_application_states;
    public final int fg_application_type;
    public final String fg_package_name;
    public final int folding_mode;
    public final boolean is_speed_charge;
    public final boolean is_split;
    public final List<ThermalControlUtils.WindowInfo> pipWindowsInfo;
    public final String safety_test_judge;
    public final boolean safety_test_on;
    public final int trigger_level;
    public final int trigger_skin_temp;
    public final long trigger_time;
    public final int user_mode;
    public final List<ThermalControlUtils.WindowInfo> zoomWindowsInfo;

    public ThermalTraceInfo(long j10, int i10, int i11, int i12, boolean z10, boolean z11, boolean z12, String str, String str2, int i13, String str3, int i14, int i15, boolean z13, boolean z14, boolean z15, int i16, int i17, ThermalPolicy thermalPolicy, List<ThermalControlUtils.WindowInfo> list, List<ThermalControlUtils.WindowInfo> list2, List<ThermalControlUtils.WindowInfo> list3) {
        this.trigger_time = j10;
        this.trigger_level = i10;
        this.trigger_skin_temp = i11;
        this.curr_env_temp_type = i12;
        this.curr_screen_on = z10;
        this.curr_plug_in = z11;
        this.safety_test_on = z12;
        this.safety_test_judge = str;
        this.fg_package_name = str2;
        this.fg_application_type = i13;
        this.fg_application_states = str3;
        this.user_mode = i14;
        this.folding_mode = i15;
        this.is_split = z13;
        this.is_speed_charge = z14;
        this.app_switch_safety_mode = z15;
        this.ambient_temperature = i16;
        this.ambient_state = i17;
        this.currPolicy = thermalPolicy;
        this.zoomWindowsInfo = list;
        this.pipWindowsInfo = list2;
        this.appFloatingWindowsInfo = list3;
    }

    private String printTimeInMillis(long j10) {
        return new SimpleDateFormat(TimeInfoUtil.TIME_PATTERN_01).format(new Date(j10));
    }

    public HashMap<String, String> currentStateTransformToMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("trigger_time", printTimeInMillis(this.trigger_time));
        hashMap.put("trigger_level", String.valueOf(this.trigger_level));
        hashMap.put("trigger_skin_temp", String.valueOf(this.trigger_skin_temp));
        hashMap.put("curr_env_temp_type", String.valueOf(this.curr_env_temp_type));
        hashMap.put("curr_screen_on", String.valueOf(this.curr_screen_on));
        hashMap.put("curr_plug_in", String.valueOf(this.curr_plug_in));
        hashMap.put("safety_test_on", String.valueOf(this.safety_test_on));
        hashMap.put("safety_test_judge", this.safety_test_judge);
        hashMap.put("fg_package_name", this.fg_package_name);
        hashMap.put("fg_application_type", String.valueOf(this.fg_application_type));
        hashMap.put("fg_application_states", this.fg_application_states);
        hashMap.put("user_mode", String.valueOf(this.user_mode));
        hashMap.put("folding_mode", String.valueOf(this.folding_mode));
        hashMap.put("split_mode_on", String.valueOf(this.is_split));
        hashMap.put("is_speed_charge", String.valueOf(this.is_speed_charge));
        hashMap.put("app_switch_safety_mode", String.valueOf(this.app_switch_safety_mode));
        hashMap.put("ambient_temperature", String.valueOf(this.ambient_temperature));
        hashMap.put("ambient_state", String.valueOf(this.ambient_state));
        ThermalPolicy thermalPolicy = this.currPolicy;
        if (thermalPolicy != null) {
            hashMap.put("trigger_category", thermalPolicy.categoryName);
            hashMap.put("restrict_level", String.valueOf(this.currPolicy.restrict));
        }
        if (!this.zoomWindowsInfo.isEmpty()) {
            hashMap.put("zoom_windows_info", this.zoomWindowsInfo.toString());
        }
        if (!this.pipWindowsInfo.isEmpty()) {
            hashMap.put("pip_windows_info", this.pipWindowsInfo.toString());
        }
        if (!this.appFloatingWindowsInfo.isEmpty()) {
            hashMap.put("app_floating_windows_info", this.appFloatingWindowsInfo.toString());
        }
        return hashMap;
    }
}
