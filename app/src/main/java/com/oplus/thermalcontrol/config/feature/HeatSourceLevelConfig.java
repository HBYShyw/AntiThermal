package com.oplus.thermalcontrol.config.feature;

import b6.LocalLog;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes2.dex */
public class HeatSourceLevelConfig extends ThermalBaseConfig {
    public static final String CONFIG_NAME = "heatsource_level";
    public static final String FEATURE_NAME = "heatsource";
    private static final String TAG = "Thermal.HeatSourceLevelConfig";

    public Map<String, Integer> getHeatSourceLevel(Map<String, Integer> map) {
        HashMap hashMap = new HashMap();
        if (map == null) {
            LocalLog.b(TAG, "heatSourceMap is null");
            return hashMap;
        }
        try {
            for (ThermalBaseConfig.Item item : getConfigItems()) {
                if (map.get(item.mName) == null) {
                    LocalLog.a(TAG, "heatSource of " + item.mName + " is null");
                } else {
                    Iterator<ThermalBaseConfig.SubItem> it = item.mSubItemList.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            ThermalBaseConfig.SubItem next = it.next();
                            if (r4.intValue() < Float.parseFloat(next.mProperties.getOrDefault("thresholds", "")) * 1000.0f && r4.intValue() >= Float.parseFloat(next.mProperties.getOrDefault("thresholds_min", "")) * 1000.0f) {
                                hashMap.put(item.mName, Integer.valueOf(Integer.parseInt(next.mProperties.getOrDefault("heatlevel", ""))));
                                break;
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e10) {
            LocalLog.b(TAG, "getHeatSourceLevel, e=" + e10);
        }
        return hashMap;
    }
}
