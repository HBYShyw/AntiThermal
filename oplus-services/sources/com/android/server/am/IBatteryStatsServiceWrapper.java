package com.android.server.am;

import com.android.server.power.stats.BatteryExternalStatsWorker;
import com.android.server.power.stats.BatteryStatsImpl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBatteryStatsServiceWrapper {
    default void awaitCompletion() {
    }

    default BatteryExternalStatsWorker getWorker() {
        return null;
    }

    default BatteryStatsImpl.UserInfoProvider getUserManagerUserInfoProvider() {
        return new BatteryStatsImpl.UserInfoProvider() { // from class: com.android.server.am.IBatteryStatsServiceWrapper.1
            public int[] getUserIds() {
                return null;
            }
        };
    }
}
