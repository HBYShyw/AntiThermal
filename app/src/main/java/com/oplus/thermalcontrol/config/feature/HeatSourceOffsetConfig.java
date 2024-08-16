package com.oplus.thermalcontrol.config.feature;

import android.util.ArrayMap;
import android.util.SparseArray;
import b6.LocalLog;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class HeatSourceOffsetConfig extends ThermalBaseConfig {
    public static final String CONFIG_NAME = "heatoffset_policy";
    public static final String FEATURE_NAME = "heatsource";
    private static final String TAG = "Thermal.HeatSourceOffsetConfig";
    private final SparseArray<Map<String, SparseArray<Integer>>> mHeatSourceOffsets = new SparseArray<>();

    public Map<String, Integer> getHeatSourceOffset(int i10, Map<String, Integer> map) {
        Integer num;
        HashMap hashMap = new HashMap();
        synchronized (this.mHeatSourceOffsets) {
            Map<String, SparseArray<Integer>> map2 = this.mHeatSourceOffsets.get(i10);
            if (map2 != null && map != null) {
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String key = entry.getKey();
                    SparseArray<Integer> sparseArray = map2.get(key);
                    if (sparseArray != null && entry.getValue() != null && (num = sparseArray.get(entry.getValue().intValue())) != null) {
                        hashMap.put(key, num);
                    }
                }
                return hashMap;
            }
            LocalLog.a(TAG, "index " + i10 + " components:" + map2 + " heatSource:" + map);
            return hashMap;
        }
    }

    @Override // com.oplus.thermalcontrol.config.ThermalBaseConfig
    protected void onItemLoaded(ThermalBaseConfig.Item item) {
    }

    @Override // com.oplus.thermalcontrol.config.ThermalBaseConfig
    protected void onSubItemLoaded(ThermalBaseConfig.Item item, ThermalBaseConfig.SubItem subItem) {
        int i10 = -1;
        try {
            i10 = Integer.parseInt(item.mProperties.get(ThermalBaseConfig.Item.ATTR_INDEX));
            String str = subItem.mProperties.get(ThermalBaseConfig.SubItem.ATTR_COMPONENT);
            int parseInt = Integer.parseInt(subItem.mProperties.get("heatlevel"));
            int parseInt2 = Integer.parseInt(subItem.mProperties.get("offset"));
            synchronized (this.mHeatSourceOffsets) {
                Map<String, SparseArray<Integer>> map = this.mHeatSourceOffsets.get(i10);
                if (map == null) {
                    map = new ArrayMap<>();
                    this.mHeatSourceOffsets.put(i10, map);
                }
                SparseArray<Integer> sparseArray = map.get(str);
                if (sparseArray == null) {
                    sparseArray = new SparseArray<>();
                    map.put(str, sparseArray);
                }
                sparseArray.put(parseInt, Integer.valueOf(parseInt2));
            }
        } catch (NumberFormatException e10) {
            LocalLog.b(TAG, "onConfigSubItemLoaded failed, e=" + e10);
            synchronized (this.mHeatSourceOffsets) {
                this.mHeatSourceOffsets.remove(i10);
            }
        }
    }
}
