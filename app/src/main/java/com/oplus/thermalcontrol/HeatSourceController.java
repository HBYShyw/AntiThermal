package com.oplus.thermalcontrol;

import android.content.Context;
import android.text.TextUtils;
import android.util.Range;
import b6.LocalLog;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import com.oplus.thermalcontrol.config.feature.GpuLevelConfig;
import com.oplus.thermalcontrol.config.feature.TsensorConfig;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;
import y5.AppFeature;

/* loaded from: classes2.dex */
public class HeatSourceController {
    private static final String TAG = "Thermal.HeatSourceController";
    private static volatile HeatSourceController sHeatSourceController;
    private final Context mContext;
    private final ThermalControlConfig mThermalControlConfig;
    private final Map<String, Integer> mHeatSourceMap = new ConcurrentHashMap();
    private final Map<String, Integer> mHeatSourceLevels = new ConcurrentHashMap();
    private final Map<String, Integer> mHeatSourceOffsets = new ConcurrentHashMap();
    private final Map<String, Integer> mTsensorSourceMap = new ConcurrentHashMap();
    private final Map<String, TsensorConfig.ThermalTsensorPolicy> mTsensorLevelPolices = new ConcurrentHashMap();
    private int mLastTsensorCpu = -2;

    private HeatSourceController(Context context) {
        this.mContext = context;
        this.mThermalControlConfig = ThermalControlConfig.getInstance(context);
    }

    public static HeatSourceController getInstance(Context context) {
        if (sHeatSourceController == null) {
            synchronized (HeatSourceController.class) {
                if (sHeatSourceController == null) {
                    sHeatSourceController = new HeatSourceController(context);
                }
            }
        }
        return sHeatSourceController;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("\nmHeatSourceMap = " + this.mHeatSourceMap);
        printWriter.println("\nmHeatSourceLevels = " + this.mHeatSourceLevels);
        printWriter.println("\nmHeatSourceOffsets = " + this.mHeatSourceOffsets);
        printWriter.println("\nmTsensorSourceMap = " + this.mTsensorSourceMap);
        printWriter.println("\nmTsensorLevelPolices = " + this.mTsensorLevelPolices);
    }

    public int getBrightnessLevel(int i10) {
        int intValue = this.mHeatSourceOffsets.getOrDefault("display", 0).intValue();
        if (i10 == -2) {
            i10 = 0;
        }
        int i11 = intValue + i10;
        return (i11 < 0 || i11 > 11 || i10 < 0 || i10 > 11) ? i10 : i11;
    }

    public int getCameraBrightness(ThermalPolicy thermalPolicy) {
        int i10 = thermalPolicy.cameraBrightness;
        if (i10 == -2) {
            return 255;
        }
        return i10;
    }

    public int getChargeLevel(ThermalPolicy thermalPolicy) {
        int i10 = thermalPolicy.charge;
        if (i10 == -2) {
            return 0;
        }
        return i10;
    }

    public CpuLevelConfig.ThermalCpuLevelPolicy getCpuPolicy(ThermalPolicy thermalPolicy, String str, String str2) {
        int intValue = this.mHeatSourceOffsets.getOrDefault(ThermalPolicy.KEY_CPU, 0).intValue();
        int i10 = thermalPolicy.cpu;
        Range range = new Range(0, 9);
        if (range.contains((Range) Integer.valueOf(i10))) {
            int i11 = intValue + i10;
            if (range.contains((Range) Integer.valueOf(i11))) {
                i10 = i11;
            }
        }
        int tsensorCpu = getTsensorCpu(str, str2);
        this.mLastTsensorCpu = tsensorCpu;
        int max = Math.max(i10, tsensorCpu);
        if (AppFeature.z() && max > this.mThermalControlConfig.getAgingCpuLevelRestrictVal()) {
            LocalLog.l(TAG, "is Special Aging, cpu level restrict");
            max = this.mThermalControlConfig.getAgingCpuLevelRestrictVal();
        }
        CpuLevelConfig.ThermalCpuLevelPolicy thermalCpuLevelPolicy = this.mThermalControlConfig.getThermalCpuLevelPolicy(max);
        thermalCpuLevelPolicy.mThermalPolicy = thermalPolicy;
        return thermalCpuLevelPolicy;
    }

    public GpuLevelConfig.ThermalGpuLevelPolicy getGpuPolicy(ThermalPolicy thermalPolicy) {
        int intValue = this.mHeatSourceOffsets.getOrDefault(ThermalPolicy.KEY_GPU, 0).intValue();
        int i10 = thermalPolicy.gpu;
        int i11 = (i10 >= -1 || intValue == 0) ? i10 + intValue : intValue - 1;
        if (intValue >= 0 || i11 >= 0) {
            i10 = i11;
        }
        return this.mThermalControlConfig.getThermalGpuLevelPolicy(i10);
    }

    public int getLastTsensorCpu() {
        return this.mLastTsensorCpu;
    }

    public int getModemLevel(int i10) {
        int max = Math.max(this.mHeatSourceOffsets.getOrDefault("cellular", 0).intValue(), this.mHeatSourceOffsets.getOrDefault(ThermalPolicy.KEY_WIFI, 0).intValue());
        if (i10 == -2) {
            i10 = 0;
        }
        int i11 = max + i10;
        return (i11 < 4 || i11 > 7 || i10 < 4 || i10 > 7) ? i10 : i11;
    }

    public int getTsensorCpu(String str, String str2) {
        TsensorConfig.ThermalTsensorPolicy thermalTsensorPolicy = this.mTsensorLevelPolices.get(ThermalPolicy.KEY_CPU);
        if (thermalTsensorPolicy == null || this.mThermalControlConfig.isTsensorExceptScene(thermalTsensorPolicy, str, str2)) {
            return -2;
        }
        return thermalTsensorPolicy.mLevel;
    }

    public Map<String, TsensorConfig.ThermalTsensorPolicy> getTsensorLevelPolices() {
        return this.mTsensorLevelPolices;
    }

    public int getWifiSpeed(ThermalPolicy thermalPolicy) {
        int i10 = thermalPolicy.wifi;
        if (i10 == -2) {
            return 0;
        }
        return i10;
    }

    public Map<String, Integer> updateHeatSourceMap(Object obj) {
        HashMap hashMap = new HashMap();
        if (obj != null) {
            try {
            } catch (JSONException e10) {
                LocalLog.b(TAG, "parse heat source to level fail: " + e10);
            }
            if (!TextUtils.isEmpty(obj.toString())) {
                JSONObject jSONObject = new JSONObject(obj.toString());
                for (String str : jSONObject.keySet()) {
                    hashMap.put(str, Integer.valueOf(jSONObject.getJSONObject(str).getInt("keyVal")));
                }
                this.mHeatSourceMap.clear();
                this.mHeatSourceMap.putAll(hashMap);
                return hashMap;
            }
        }
        LocalLog.b(TAG, "heat source string is null or empty");
        return hashMap;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0078  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean updateHeatSourceOffsets(ThermalPolicy thermalPolicy, boolean z10) {
        Map<String, Integer> hashMap;
        if (z10) {
            this.mHeatSourceLevels.clear();
            this.mHeatSourceLevels.putAll(this.mThermalControlConfig.getHeatSourceLevel(this.mHeatSourceMap));
        }
        boolean z11 = false;
        if (thermalPolicy == null) {
            return false;
        }
        int i10 = thermalPolicy.heatoffPolicy;
        if (i10 >= 0) {
            hashMap = this.mThermalControlConfig.getHeatSourceOffset(i10, this.mHeatSourceLevels);
        } else {
            hashMap = new HashMap<>();
        }
        if (hashMap.size() == this.mHeatSourceOffsets.size()) {
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                if (!this.mHeatSourceOffsets.getOrDefault(entry.getKey(), 0).equals(entry.getValue())) {
                }
            }
            this.mHeatSourceOffsets.clear();
            this.mHeatSourceOffsets.putAll(hashMap);
            if (LocalLog.f()) {
                LocalLog.a(TAG, "updateHeatSourceOffsets: sourceMap:" + this.mHeatSourceMap + " levels:" + this.mHeatSourceLevels + " offsets:" + this.mHeatSourceOffsets + " heatoffPolicy:" + thermalPolicy.heatoffPolicy + " changed:" + z11);
            }
            return z11;
        }
        z11 = true;
        this.mHeatSourceOffsets.clear();
        this.mHeatSourceOffsets.putAll(hashMap);
        if (LocalLog.f()) {
        }
        return z11;
    }

    public boolean updateTsensorSourceLevel() {
        Map<String, TsensorConfig.ThermalTsensorPolicy> tsensorPolicy = this.mThermalControlConfig.getTsensorPolicy(this.mTsensorSourceMap);
        boolean z10 = true;
        if (tsensorPolicy.size() == this.mTsensorLevelPolices.size()) {
            Iterator<Map.Entry<String, TsensorConfig.ThermalTsensorPolicy>> it = tsensorPolicy.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    z10 = false;
                    break;
                }
                Map.Entry<String, TsensorConfig.ThermalTsensorPolicy> next = it.next();
                if (!next.getValue().equals(this.mTsensorLevelPolices.get(next.getKey()))) {
                    break;
                }
            }
        }
        this.mTsensorLevelPolices.clear();
        this.mTsensorLevelPolices.putAll(tsensorPolicy);
        if (LocalLog.f()) {
            LocalLog.a(TAG, "updateTsensorSourceLevel: levels:" + this.mTsensorLevelPolices + " changed:" + z10);
        }
        return z10;
    }

    public void updateTsensorSourceMap(int i10) {
        this.mTsensorSourceMap.put(ThermalPolicy.KEY_CPU, Integer.valueOf(i10));
    }
}
