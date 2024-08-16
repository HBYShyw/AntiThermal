package com.oplus.thermalcontrol.config.feature;

import android.text.TextUtils;
import android.util.Pair;
import b6.LocalLog;
import com.oplus.thermalcontrol.config.UahConstants;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import com.oplus.uah.info.UAHResourceInfo;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CpuLevelConfig extends BaseLevelConfig {
    public static final String ATTR_BOOST_CEILING = "boostCeiling";
    public static final String ATTR_BOOST_CPU_CPU_CORE_CEILING = "boostCpuCoreCeiling";
    public static final String ATTR_MAX_CPU_CORE = "maxCpuCore";
    public static final String ATTR_MAX_CPU_FREQ = "maxCpuFreq";
    public static final String ATTR_SKIP_GOPLUS = "skip_goplus";
    public static final String CONFIG_NAME = "thermal_cpulevel";
    public static final String FEATURE_NAME = "level";
    private static final String TAG = "Thermal.CpuLevelConfig";

    /* loaded from: classes2.dex */
    public static class ThermalCpuLevelPolicy {
        public static final String BOOST_CEILING_DEFAULT = "-2,-2,-2,-2";
        public static final String BOOST_CPU_CPU_CORE_CEILING_DEFAULT = "-2,-2,-2,-2";
        public static final int CPU_POWER_DEFAULT = 9999;
        public static final String DEFAULT_VALUE = "-2,-2,-2,-2";
        public static final String MAX_CPU_CORE_DEFAULT = "-2,-2,-2,-2";
        public static final String MAX_CPU_FREQ_DEFAULT = "-2,-2,-2,-2";
        public static final String NO_LIMITED_VALUE = "-1,-1,-1,-1";
        public static final String SKIP_GOPLUS_DEFAULT = "-1";
        public String mBoostCeiling;
        public String mBoostCpuCoreCeiling;
        public int mLevel;
        public String mMaxCpuCore;
        public String mMaxCpuFreq;
        public String mSkipGoplus = SKIP_GOPLUS_DEFAULT;
        public ThermalPolicy mThermalPolicy;

        public ThermalCpuLevelPolicy(int i10) {
            this.mMaxCpuCore = "-2,-2,-2,-2";
            this.mMaxCpuFreq = "-2,-2,-2,-2";
            this.mBoostCpuCoreCeiling = "-2,-2,-2,-2";
            this.mBoostCeiling = "-2,-2,-2,-2";
            this.mLevel = i10;
            if (i10 == -1) {
                this.mMaxCpuCore = NO_LIMITED_VALUE;
                this.mMaxCpuFreq = NO_LIMITED_VALUE;
                this.mBoostCpuCoreCeiling = NO_LIMITED_VALUE;
                this.mBoostCeiling = NO_LIMITED_VALUE;
            }
        }

        private void addUahResToList(int[] iArr, String str, ArrayList<UAHResourceInfo> arrayList) {
            String[] split = str == null ? new String[0] : str.split(",");
            for (int i10 = 0; i10 < iArr.length; i10++) {
                if (i10 >= split.length) {
                    arrayList.add(new UAHResourceInfo(iArr[i10], "-2"));
                } else {
                    arrayList.add(new UAHResourceInfo(iArr[i10], split[i10]));
                }
            }
        }

        private String getIpaControlStateLimit() {
            StringBuilder sb2 = new StringBuilder();
            String str = this.mBoostCeiling;
            String[] split = str == null ? new String[0] : str.split(",");
            for (int i10 = 0; i10 < 6; i10++) {
                if (i10 >= split.length) {
                    sb2.append("0 ");
                } else {
                    sb2.append(split[i10]);
                    sb2.append(" ");
                }
            }
            return sb2.toString();
        }

        public void addUahResInfoTo(ArrayList<UAHResourceInfo> arrayList) {
            addUahResToList(UahConstants.MAX_CPU_CORE_CODES, this.mMaxCpuCore, arrayList);
            addUahResToList(UahConstants.MAX_CPU_FREQ_CODES, this.mMaxCpuFreq, arrayList);
            addUahResToList(UahConstants.BOOST_CPU_CORE_CEILING_CODES, this.mBoostCpuCoreCeiling, arrayList);
            addUahResToList(UahConstants.BOOST_CEILING_CODES, this.mBoostCeiling, arrayList);
            arrayList.add(new UAHResourceInfo(17104992, getIpaControlStateLimit()));
            arrayList.add(new UAHResourceInfo(UahConstants.CPU_SCHED_TASK_OVERLOAD_SKIP_GOPLUS_ENABLE, String.valueOf(this.mSkipGoplus)));
            ThermalPolicy thermalPolicy = this.mThermalPolicy;
            if (thermalPolicy != null) {
                int i10 = thermalPolicy.cpuPower;
                if (i10 > 0 && i10 <= 9999) {
                    arrayList.add(new UAHResourceInfo(17104928, String.valueOf(this.mThermalPolicy.cpuPower)));
                } else {
                    arrayList.add(new UAHResourceInfo(17104928, String.valueOf(CPU_POWER_DEFAULT)));
                }
                arrayList.add(new UAHResourceInfo(17104944, String.valueOf(this.mThermalPolicy.ipaweight)));
                arrayList.add(new UAHResourceInfo(UahConstants.THERMAL_BOOST_ALL_BREAK_LIMIT, String.valueOf(this.mThermalPolicy.boostBreak2)));
                arrayList.add(new UAHResourceInfo(UahConstants.THERMAL_BOOST_BREAK_LEVEL, String.valueOf(this.mThermalPolicy.boostBreak1)));
            }
        }

        public boolean equals(Object obj) {
            ThermalPolicy thermalPolicy;
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                ThermalCpuLevelPolicy thermalCpuLevelPolicy = (ThermalCpuLevelPolicy) obj;
                if (TextUtils.equals(this.mMaxCpuCore, thermalCpuLevelPolicy.mMaxCpuCore) && TextUtils.equals(this.mMaxCpuFreq, thermalCpuLevelPolicy.mMaxCpuFreq) && TextUtils.equals(this.mBoostCpuCoreCeiling, thermalCpuLevelPolicy.mBoostCpuCoreCeiling) && TextUtils.equals(this.mBoostCeiling, thermalCpuLevelPolicy.mBoostCeiling) && TextUtils.equals(this.mSkipGoplus, thermalCpuLevelPolicy.mSkipGoplus)) {
                    ThermalPolicy thermalPolicy2 = this.mThermalPolicy;
                    if (thermalPolicy2 != null && (thermalPolicy = thermalCpuLevelPolicy.mThermalPolicy) != null) {
                        if (TextUtils.equals(thermalPolicy2.ipaweight, thermalPolicy.ipaweight)) {
                            ThermalPolicy thermalPolicy3 = this.mThermalPolicy;
                            int i10 = thermalPolicy3.boostBreak1;
                            ThermalPolicy thermalPolicy4 = thermalCpuLevelPolicy.mThermalPolicy;
                            if (i10 == thermalPolicy4.boostBreak1 && thermalPolicy3.boostBreak2 == thermalPolicy4.boostBreak2) {
                                return true;
                            }
                        }
                        return false;
                    }
                    if (thermalPolicy2 == null && thermalCpuLevelPolicy.mThermalPolicy == null) {
                        return true;
                    }
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
                jSONArray.put(new JSONObject().put("max_cpu_core", this.mMaxCpuCore));
                jSONArray.put(new JSONObject().put("max_cpu_freq", this.mMaxCpuFreq));
                jSONObject.put("Cpu", jSONArray);
                JSONArray jSONArray2 = new JSONArray();
                jSONArray2.put(new JSONObject().put("boost_cpu_core_ceiling", this.mBoostCpuCoreCeiling));
                jSONArray2.put(new JSONObject().put("boost_ceiling", this.mBoostCeiling));
                jSONArray2.put(new JSONObject().put(CpuLevelConfig.ATTR_SKIP_GOPLUS, this.mSkipGoplus));
                if (this.mThermalPolicy != null) {
                    jSONArray2.put(new JSONObject().put(ThermalPolicy.KEY_BOOST_BREAK_FIRST, this.mThermalPolicy.boostBreak1));
                    jSONArray2.put(new JSONObject().put(ThermalPolicy.KEY_BOOST_BREAK_SECOND, this.mThermalPolicy.boostBreak2));
                }
                jSONObject.put("Boost", jSONArray2);
                if (this.mThermalPolicy != null) {
                    JSONArray jSONArray3 = new JSONArray();
                    JSONObject jSONObject2 = new JSONObject();
                    int i10 = this.mThermalPolicy.cpuPower;
                    if (i10 > 0 && i10 <= 9999) {
                        jSONObject2.put("cpu_power", i10);
                    } else {
                        jSONObject2.put("cpu_power", CPU_POWER_DEFAULT);
                    }
                    jSONArray3.put(jSONObject2);
                    jSONArray3.put(new JSONObject().put("ipa_weight", this.mThermalPolicy.ipaweight));
                    jSONObject.put("IPA", jSONArray3);
                }
            } catch (JSONException e10) {
                LocalLog.b(CpuLevelConfig.TAG, "cpuPolicy putIntoJsonObject e=" + e10);
            }
        }

        public String toString() {
            return "cpuLevel=" + this.mLevel;
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

    public ThermalCpuLevelPolicy getThermalCpuLevelPolicy(int i10) {
        char c10;
        if (i10 == -1) {
            return new ThermalCpuLevelPolicy(-1);
        }
        Pair<Integer, Map<String, String>> matchedLevelItemProps = getMatchedLevelItemProps(i10, -1);
        int intValue = ((Integer) matchedLevelItemProps.first).intValue();
        Map map = (Map) matchedLevelItemProps.second;
        ThermalCpuLevelPolicy thermalCpuLevelPolicy = new ThermalCpuLevelPolicy(intValue);
        if (map == null) {
            LocalLog.a(TAG, "cpu level " + i10 + " policy is null");
            return thermalCpuLevelPolicy;
        }
        for (Map.Entry entry : map.entrySet()) {
            String str = (String) entry.getKey();
            str.hashCode();
            switch (str.hashCode()) {
                case -2135856285:
                    if (str.equals(ATTR_MAX_CPU_CORE)) {
                        c10 = 0;
                        break;
                    }
                    break;
                case -2135764420:
                    if (str.equals(ATTR_MAX_CPU_FREQ)) {
                        c10 = 1;
                        break;
                    }
                    break;
                case -1752719038:
                    if (str.equals(ATTR_SKIP_GOPLUS)) {
                        c10 = 2;
                        break;
                    }
                    break;
                case -1537320038:
                    if (str.equals(ATTR_BOOST_CEILING)) {
                        c10 = 3;
                        break;
                    }
                    break;
                case 1731985369:
                    if (str.equals(ATTR_BOOST_CPU_CPU_CORE_CEILING)) {
                        c10 = 4;
                        break;
                    }
                    break;
            }
            c10 = 65535;
            switch (c10) {
                case 0:
                    thermalCpuLevelPolicy.mMaxCpuCore = (String) entry.getValue();
                    break;
                case 1:
                    thermalCpuLevelPolicy.mMaxCpuFreq = (String) entry.getValue();
                    break;
                case 2:
                    thermalCpuLevelPolicy.mSkipGoplus = (String) entry.getValue();
                    break;
                case 3:
                    thermalCpuLevelPolicy.mBoostCeiling = (String) entry.getValue();
                    break;
                case 4:
                    thermalCpuLevelPolicy.mBoostCpuCoreCeiling = (String) entry.getValue();
                    break;
            }
        }
        return thermalCpuLevelPolicy;
    }
}
