package com.android.server.powerstats;

import android.app.StatsManager;
import android.content.Context;
import android.hardware.power.stats.Channel;
import android.hardware.power.stats.EnergyMeasurement;
import android.hardware.power.stats.PowerEntity;
import android.hardware.power.stats.State;
import android.hardware.power.stats.StateResidency;
import android.hardware.power.stats.StateResidencyResult;
import android.os.SystemClock;
import android.power.PowerStatsInternal;
import android.util.Slog;
import android.util.StatsEvent;
import com.android.internal.util.ConcurrentUtils;
import com.android.internal.util.FrameworkStatsLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class StatsPullAtomCallbackImpl implements StatsManager.StatsPullAtomCallback {
    private static final boolean DEBUG = false;
    private static final int STATS_PULL_TIMEOUT_MILLIS = 2000;
    private static final String TAG = StatsPullAtomCallbackImpl.class.getSimpleName();
    private Context mContext;
    private PowerStatsInternal mPowerStatsInternal;
    private Map<Integer, Channel> mChannels = new HashMap();
    private Map<Integer, String> mEntityNames = new HashMap();
    private Map<Integer, Map<Integer, String>> mStateNames = new HashMap();

    public int onPullAtom(int i, List<StatsEvent> list) {
        if (i == 10005) {
            return pullSubsystemSleepState(i, list);
        }
        if (i == 10038) {
            return pullOnDevicePowerMeasurement(i, list);
        }
        throw new UnsupportedOperationException("Unknown tagId=" + i);
    }

    private boolean initPullOnDevicePowerMeasurement() {
        Channel[] energyMeterInfo = this.mPowerStatsInternal.getEnergyMeterInfo();
        if (energyMeterInfo == null || energyMeterInfo.length == 0) {
            Slog.e(TAG, "Failed to init OnDevicePowerMeasurement puller");
            return false;
        }
        for (Channel channel : energyMeterInfo) {
            this.mChannels.put(Integer.valueOf(channel.id), channel);
        }
        return true;
    }

    private int pullOnDevicePowerMeasurement(int i, List<StatsEvent> list) {
        try {
            EnergyMeasurement[] energyMeasurementArr = (EnergyMeasurement[]) this.mPowerStatsInternal.readEnergyMeterAsync(new int[0]).get(2000L, TimeUnit.MILLISECONDS);
            if (energyMeasurementArr == null) {
                return 1;
            }
            for (EnergyMeasurement energyMeasurement : energyMeasurementArr) {
                if (energyMeasurement.durationMs == energyMeasurement.timestampMs) {
                    list.add(FrameworkStatsLog.buildStatsEvent(i, this.mChannels.get(Integer.valueOf(energyMeasurement.id)).subsystem, this.mChannels.get(Integer.valueOf(energyMeasurement.id)).name, energyMeasurement.durationMs, energyMeasurement.energyUWs));
                }
            }
            return 0;
        } catch (Exception e) {
            Slog.e(TAG, "Failed to readEnergyMeterAsync", e);
            return 1;
        }
    }

    private boolean initSubsystemSleepState() {
        PowerEntity[] powerEntityInfo = this.mPowerStatsInternal.getPowerEntityInfo();
        if (powerEntityInfo == null || powerEntityInfo.length == 0) {
            Slog.e(TAG, "Failed to init SubsystemSleepState puller");
            return false;
        }
        for (PowerEntity powerEntity : powerEntityInfo) {
            HashMap hashMap = new HashMap();
            int i = 0;
            while (true) {
                State[] stateArr = powerEntity.states;
                if (i < stateArr.length) {
                    State state = stateArr[i];
                    hashMap.put(Integer.valueOf(state.id), state.name);
                    i++;
                }
            }
            this.mEntityNames.put(Integer.valueOf(powerEntity.id), powerEntity.name);
            this.mStateNames.put(Integer.valueOf(powerEntity.id), hashMap);
        }
        return true;
    }

    private int pullSubsystemSleepState(int i, List<StatsEvent> list) {
        try {
            int i2 = 0;
            StateResidencyResult[] stateResidencyResultArr = (StateResidencyResult[]) this.mPowerStatsInternal.getStateResidencyAsync(new int[0]).get(2000L, TimeUnit.MILLISECONDS);
            if (stateResidencyResultArr == null) {
                return 1;
            }
            int i3 = 0;
            while (i3 < stateResidencyResultArr.length) {
                StateResidencyResult stateResidencyResult = stateResidencyResultArr[i3];
                int i4 = i2;
                while (true) {
                    StateResidency[] stateResidencyArr = stateResidencyResult.stateResidencyData;
                    if (i4 < stateResidencyArr.length) {
                        StateResidency stateResidency = stateResidencyArr[i4];
                        list.add(FrameworkStatsLog.buildStatsEvent(i, this.mEntityNames.get(Integer.valueOf(stateResidencyResult.id)), this.mStateNames.get(Integer.valueOf(stateResidencyResult.id)).get(Integer.valueOf(stateResidency.id)), stateResidency.totalStateEntryCount, stateResidency.totalTimeInStateMs, SystemClock.elapsedRealtime() - stateResidency.totalTimeInStateMs));
                        i4++;
                        stateResidencyResult = stateResidencyResult;
                    }
                }
                i3++;
                i2 = 0;
            }
            return i2;
        } catch (Exception e) {
            Slog.e(TAG, "Failed to getStateResidencyAsync", e);
            return 1;
        }
    }

    public StatsPullAtomCallbackImpl(Context context, PowerStatsInternal powerStatsInternal) {
        this.mContext = context;
        this.mPowerStatsInternal = powerStatsInternal;
        if (powerStatsInternal == null) {
            Slog.e(TAG, "Failed to start PowerStatsService statsd pullers");
            return;
        }
        StatsManager statsManager = (StatsManager) context.getSystemService(StatsManager.class);
        if (initPullOnDevicePowerMeasurement()) {
            statsManager.setPullAtomCallback(10038, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this);
        }
        if (initSubsystemSleepState()) {
            statsManager.setPullAtomCallback(10005, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this);
        }
    }
}
