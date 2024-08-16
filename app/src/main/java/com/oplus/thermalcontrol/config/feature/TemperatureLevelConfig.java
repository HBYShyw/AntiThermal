package com.oplus.thermalcontrol.config.feature;

import android.util.SparseIntArray;
import b6.LocalLog;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;

/* loaded from: classes2.dex */
public class TemperatureLevelConfig extends ThermalBaseConfig {
    public static final String CONFIG_NAME = "temperature_level";
    public static final String FEATURE_NAME = "level";
    private static final String TAG = "Thermal.TemperatureLevelConfig";
    private final SparseIntArray mSeriousLevelMap = new SparseIntArray();

    public int getTempSeriousLevel(int i10) {
        synchronized (this.mSeriousLevelMap) {
            int i11 = -2;
            while (i10 >= 0) {
                i11 = this.mSeriousLevelMap.get(i10, -2);
                if (i11 != -2) {
                    return i11;
                }
                i10--;
            }
            return i11;
        }
    }

    @Override // com.oplus.thermalcontrol.config.ThermalBaseConfig
    protected void onItemLoaded(ThermalBaseConfig.Item item) {
        if (!CONFIG_NAME.equals(this.mConfigName) || item.mLevel == Integer.MIN_VALUE) {
            return;
        }
        synchronized (this.mSeriousLevelMap) {
            for (String str : item.mProperties.getOrDefault(ThermalBaseConfig.Item.ATTR_VALUE, "").split(",")) {
                try {
                    this.mSeriousLevelMap.put(Integer.parseInt(str), item.mLevel);
                } catch (NumberFormatException unused) {
                    LocalLog.b(TAG, "onItemLoaded, parse int error:" + str);
                }
            }
        }
    }
}
