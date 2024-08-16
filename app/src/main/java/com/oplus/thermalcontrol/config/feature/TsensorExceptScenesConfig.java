package com.oplus.thermalcontrol.config.feature;

import android.text.TextUtils;
import android.util.ArrayMap;
import b6.LocalLog;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.oplus.thermalcontrol.config.feature.TsensorConfig;

/* loaded from: classes2.dex */
public class TsensorExceptScenesConfig extends ThermalBaseConfig {
    public static final String CONFIG_NAME = "except_scenes";
    public static final String FEATURE_NAME = "tsensor_feature";
    private static final String TAG = "Thermal.TsensorExceptScenesConfig";
    private final ArrayMap<String, String> mExceptScenes = new ArrayMap<>();

    public boolean isExceptScene(TsensorConfig.ThermalTsensorPolicy thermalTsensorPolicy, String str, String str2) {
        synchronized (this.mExceptScenes) {
            if (TextUtils.isEmpty(thermalTsensorPolicy.mExceptScenes)) {
                return false;
            }
            for (String str3 : thermalTsensorPolicy.mExceptScenes.split("-")) {
                String str4 = this.mExceptScenes.get(str3);
                if (str4 != null && (str4.contains(str) || str4.contains(str2))) {
                    LocalLog.a(TAG, "isExceptScene: " + thermalTsensorPolicy.mName + " exceptScenes: " + str + ", " + str2);
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.oplus.thermalcontrol.config.ThermalBaseConfig
    protected void onItemLoaded(ThermalBaseConfig.Item item) {
        synchronized (this.mExceptScenes) {
            this.mExceptScenes.put(item.mProperties.get("thermal_scene"), item.mProperties.get("black_list"));
        }
    }
}
