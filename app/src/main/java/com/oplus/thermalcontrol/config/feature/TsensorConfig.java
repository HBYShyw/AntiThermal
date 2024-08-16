package com.oplus.thermalcontrol.config.feature;

import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes2.dex */
public class TsensorConfig extends ThermalBaseConfig {
    public static final String CONFIG_NAME = "tsensor";
    public static final String FEATURE_NAME = "tsensor_feature";
    private static final String TAG = "Thermal.TsensorConfig";

    /* loaded from: classes2.dex */
    public static class ThermalTsensorPolicy {
        public static final int TSENSOR_LEVEL_DEFAULT = -2;
        public String mExceptScenes;
        public int mLevel;
        public String mName;

        public ThermalTsensorPolicy(String str, int i10, String str2) {
            this.mName = str;
            this.mLevel = i10;
            this.mExceptScenes = str2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ThermalTsensorPolicy thermalTsensorPolicy = (ThermalTsensorPolicy) obj;
            return TextUtils.equals(this.mName, thermalTsensorPolicy.mName) && TextUtils.equals(this.mExceptScenes, thermalTsensorPolicy.mExceptScenes) && this.mLevel == thermalTsensorPolicy.mLevel;
        }

        public int hashCode() {
            return super.hashCode();
        }

        public String toString() {
            return "ThermalTsensorPolicy{mName='" + this.mName + "', mLevel=" + this.mLevel + ", mExceptScenes='" + this.mExceptScenes + "'}";
        }
    }

    public Map<String, ThermalTsensorPolicy> getTsensorPolicy(Map<String, Integer> map) {
        HashMap hashMap = new HashMap();
        if (map == null) {
            LocalLog.a(TAG, "tsensorSource is null");
            return hashMap;
        }
        try {
            for (ThermalBaseConfig.Item item : getConfigItems()) {
                if (map.get(item.mName) == null) {
                    LocalLog.a(TAG, "tsensorSource of " + item.mName + " is null");
                } else {
                    Iterator<ThermalBaseConfig.SubItem> it = item.mSubItemList.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            ThermalBaseConfig.SubItem next = it.next();
                            if (r4.intValue() < Float.parseFloat(next.mProperties.getOrDefault("thresholds", "")) * 1000.0f && r4.intValue() >= Float.parseFloat(next.mProperties.getOrDefault("thresholds_min", "")) * 1000.0f) {
                                hashMap.put(item.mName, new ThermalTsensorPolicy(item.mName, Integer.parseInt(next.mProperties.getOrDefault("cpulevel", "")), item.mProperties.getOrDefault("except_scene", "")));
                                break;
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e10) {
            LocalLog.b(TAG, "getTsensorPolicy, e=" + e10);
        }
        return hashMap;
    }
}
