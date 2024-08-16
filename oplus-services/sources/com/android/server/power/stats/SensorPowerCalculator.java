package com.android.server.power.stats;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryStats;
import android.os.BatteryUsageStats;
import android.os.BatteryUsageStatsQuery;
import android.os.UidBatteryConsumer;
import android.util.SparseArray;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SensorPowerCalculator extends PowerCalculator {
    private final SparseArray<Sensor> mSensors;

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int i) {
        return i == 9;
    }

    public SensorPowerCalculator(SensorManager sensorManager) {
        List<Sensor> sensorList = sensorManager.getSensorList(-1);
        this.mSensors = new SparseArray<>(sensorList.size());
        for (int i = 0; i < sensorList.size(); i++) {
            Sensor sensor = sensorList.get(i);
            this.mSensors.put(sensor.getHandle(), sensor);
        }
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(BatteryUsageStats.Builder builder, BatteryStats batteryStats, long j, long j2, BatteryUsageStatsQuery batteryUsageStatsQuery) {
        SparseArray uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        double d = 0.0d;
        for (int size = uidBatteryConsumerBuilders.size() - 1; size >= 0; size--) {
            UidBatteryConsumer.Builder builder2 = (UidBatteryConsumer.Builder) uidBatteryConsumerBuilders.valueAt(size);
            if (!builder2.isVirtualUid()) {
                d += calculateApp(builder2, builder2.getBatteryStatsUid(), j);
            }
        }
        builder.getAggregateBatteryConsumerBuilder(0).setConsumedPower(9, d);
        builder.getAggregateBatteryConsumerBuilder(1).setConsumedPower(9, d);
    }

    private double calculateApp(UidBatteryConsumer.Builder builder, BatteryStats.Uid uid, long j) {
        double calculatePowerMah = calculatePowerMah(uid, j, 0);
        builder.setUsageDurationMillis(9, calculateDuration(uid, j, 0)).setConsumedPower(9, calculatePowerMah);
        return calculatePowerMah;
    }

    private long calculateDuration(BatteryStats.Uid uid, long j, int i) {
        SparseArray sensorStats = uid.getSensorStats();
        int size = sensorStats.size();
        long j2 = 0;
        for (int i2 = 0; i2 < size; i2++) {
            if (sensorStats.keyAt(i2) != -10000) {
                j2 += ((BatteryStats.Uid.Sensor) sensorStats.valueAt(i2)).getSensorTime().getTotalTimeLocked(j, i) / 1000;
            }
        }
        return j2;
    }

    private double calculatePowerMah(BatteryStats.Uid uid, long j, int i) {
        Sensor sensor;
        SparseArray sensorStats = uid.getSensorStats();
        int size = sensorStats.size();
        double d = 0.0d;
        for (int i2 = 0; i2 < size; i2++) {
            int keyAt = sensorStats.keyAt(i2);
            if (keyAt != -10000) {
                if (((BatteryStats.Uid.Sensor) sensorStats.valueAt(i2)).getSensorTime().getTotalTimeLocked(j, i) / 1000 != 0 && (sensor = this.mSensors.get(keyAt)) != null) {
                    d += (((float) r5) * sensor.getPower()) / 3600000.0f;
                }
            }
        }
        return d;
    }
}
