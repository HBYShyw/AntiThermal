package com.android.server.power.stats;

import com.android.server.power.stats.BatteryStatsImpl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IBatteryStatsImplWrapper {
    default BatteryStatsImpl.BatteryCallback getBatteryCallback() {
        return null;
    }
}
