package com.android.server.power.stats;

import android.os.BatteryStats;
import android.os.BatteryUsageStats;
import android.os.BatteryUsageStatsQuery;
import android.os.UidBatteryConsumer;
import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class PowerCalculator {
    protected static final boolean DEBUG = false;
    protected static final double MILLIAMPHOUR_PER_MICROCOULOMB = 2.777777777777778E-7d;

    protected static int getPowerModel(long j) {
        return j != -1 ? 2 : 1;
    }

    public static double uCtoMah(long j) {
        return j * MILLIAMPHOUR_PER_MICROCOULOMB;
    }

    protected void calculateApp(UidBatteryConsumer.Builder builder, BatteryStats.Uid uid, long j, long j2, BatteryUsageStatsQuery batteryUsageStatsQuery) {
    }

    public abstract boolean isPowerComponentSupported(int i);

    public void reset() {
    }

    public void calculate(BatteryUsageStats.Builder builder, BatteryStats batteryStats, long j, long j2, BatteryUsageStatsQuery batteryUsageStatsQuery) {
        SparseArray uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        for (int size = uidBatteryConsumerBuilders.size() - 1; size >= 0; size--) {
            UidBatteryConsumer.Builder builder2 = (UidBatteryConsumer.Builder) uidBatteryConsumerBuilders.valueAt(size);
            calculateApp(builder2, builder2.getBatteryStatsUid(), j, j2, batteryUsageStatsQuery);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getPowerModel(long j, BatteryUsageStatsQuery batteryUsageStatsQuery) {
        return (j == -1 || batteryUsageStatsQuery.shouldForceUsePowerProfileModel()) ? 1 : 2;
    }
}
