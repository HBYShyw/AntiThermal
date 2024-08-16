package com.oplus.thermalcontrol.config.feature;

import android.util.Pair;
import b6.LocalLog;
import com.oplus.uah.info.UAHResourceInfo;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GpuLevelConfig extends BaseLevelConfig {
    public static final String ATTR_MAX_GPU_CORE = "maxGpuCore";
    public static final String ATTR_MAX_GPU_FREQ = "maxGpuFreq";
    public static final String CONFIG_NAME = "thermal_gpulevel";
    public static final String FEATURE_NAME = "level";
    private static final String TAG = "Thermal.GpuLevelConfig";

    /* loaded from: classes2.dex */
    public static class ThermalGpuLevelPolicy {
        public static final int MAX_GPU_CORE_DEFAULT = -2;
        public static final int MAX_GPU_FREQ_DEFAULT = -2;
        public int mLevel;
        public int mMaxGpuCore;
        public int mMaxGpuFreq;

        public ThermalGpuLevelPolicy(int i10) {
            this.mMaxGpuCore = -2;
            this.mMaxGpuFreq = -2;
            this.mLevel = i10;
            if (i10 == -1) {
                this.mMaxGpuCore = -1;
                this.mMaxGpuFreq = -1;
            }
        }

        public void addUahResInfoTo(ArrayList<UAHResourceInfo> arrayList) {
            arrayList.add(new UAHResourceInfo(16908416, String.valueOf(this.mMaxGpuCore)));
            arrayList.add(new UAHResourceInfo(16908384, String.valueOf(this.mMaxGpuFreq)));
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                ThermalGpuLevelPolicy thermalGpuLevelPolicy = (ThermalGpuLevelPolicy) obj;
                if (this.mMaxGpuCore == thermalGpuLevelPolicy.mMaxGpuCore && this.mMaxGpuFreq == thermalGpuLevelPolicy.mMaxGpuFreq) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return super.hashCode();
        }

        public void putIntoJsonObject(JSONObject jSONObject) {
            try {
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(new JSONObject().put("max_gpu_core", this.mMaxGpuCore));
                jSONArray.put(new JSONObject().put("max_gpu_freq", this.mMaxGpuFreq));
                jSONObject.put("Gpu", jSONArray);
            } catch (JSONException e10) {
                LocalLog.b(GpuLevelConfig.TAG, "gpuPolicy putIntoJsonObject e=" + e10);
            }
        }

        public String toString() {
            return "gpuLevel=" + this.mLevel;
        }
    }

    @Override // com.oplus.thermalcontrol.config.feature.BaseLevelConfig
    public String getStaticConfigName() {
        return CONFIG_NAME;
    }

    @Override // com.oplus.thermalcontrol.config.feature.BaseLevelConfig
    public String getStaticFeatureName() {
        return "level";
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x008d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x007d A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ThermalGpuLevelPolicy getThermalGpuLevelPolicy(int i10) {
        boolean z10;
        if (i10 == -1) {
            return new ThermalGpuLevelPolicy(-1);
        }
        Pair<Integer, Map<String, String>> matchedLevelItemProps = getMatchedLevelItemProps(i10, -1);
        int intValue = ((Integer) matchedLevelItemProps.first).intValue();
        Map map = (Map) matchedLevelItemProps.second;
        ThermalGpuLevelPolicy thermalGpuLevelPolicy = new ThermalGpuLevelPolicy(intValue);
        if (map == null) {
            LocalLog.a(TAG, "gpu level " + i10 + " policy is null");
            return thermalGpuLevelPolicy;
        }
        try {
            for (Map.Entry entry : map.entrySet()) {
                String str = (String) entry.getKey();
                int hashCode = str.hashCode();
                if (hashCode != 1414158439) {
                    if (hashCode == 1414250304 && str.equals(ATTR_MAX_GPU_FREQ)) {
                        z10 = true;
                        if (z10) {
                            thermalGpuLevelPolicy.mMaxGpuCore = Integer.parseInt((String) entry.getValue());
                        } else if (z10) {
                            thermalGpuLevelPolicy.mMaxGpuFreq = Integer.parseInt((String) entry.getValue());
                        }
                    }
                    z10 = -1;
                    if (z10) {
                    }
                } else {
                    if (str.equals(ATTR_MAX_GPU_CORE)) {
                        z10 = false;
                        if (z10) {
                        }
                    }
                    z10 = -1;
                    if (z10) {
                    }
                }
            }
        } catch (NumberFormatException e10) {
            LocalLog.b(TAG, "getThermalGpuLevelPolicy failed, e=" + e10);
        }
        return thermalGpuLevelPolicy;
    }
}
