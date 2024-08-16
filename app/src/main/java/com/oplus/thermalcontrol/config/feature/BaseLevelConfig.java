package com.oplus.thermalcontrol.config.feature;

import android.util.Pair;
import android.util.SparseArray;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Map;

/* loaded from: classes2.dex */
public abstract class BaseLevelConfig extends ThermalBaseConfig {
    public final SparseArray<ThermalBaseConfig.Item> mLevelPolicyMap = new SparseArray<>();

    protected Map<String, String> getLevelItemProps(int i10) {
        Map<String, String> map;
        synchronized (this.mLevelPolicyMap) {
            ThermalBaseConfig.Item item = this.mLevelPolicyMap.get(i10);
            map = item != null ? item.mProperties : null;
        }
        return map;
    }

    protected Pair<Integer, ThermalBaseConfig.Item> getMatchedLevelItem(int i10, int i11) {
        ThermalBaseConfig.Item item = null;
        int i12 = i10;
        while (i12 >= i11) {
            synchronized (this.mLevelPolicyMap) {
                item = this.mLevelPolicyMap.get(i10);
            }
            if (item != null) {
                break;
            }
            i12--;
        }
        return new Pair<>(Integer.valueOf(i12), item);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Pair<Integer, Map<String, String>> getMatchedLevelItemProps(int i10, int i11) {
        Map<String, String> map = null;
        while (i10 >= i11) {
            map = getLevelItemProps(i10);
            if (map != null) {
                break;
            }
            i10--;
        }
        return new Pair<>(Integer.valueOf(i10), map);
    }

    public abstract String getStaticConfigName();

    public abstract String getStaticFeatureName();

    @Override // com.oplus.thermalcontrol.config.ThermalBaseConfig
    protected void onItemLoaded(ThermalBaseConfig.Item item) {
        if (!getStaticConfigName().equals(this.mConfigName) || item.mLevel == Integer.MIN_VALUE) {
            return;
        }
        synchronized (this.mLevelPolicyMap) {
            this.mLevelPolicyMap.put(item.mLevel, item);
        }
    }
}
