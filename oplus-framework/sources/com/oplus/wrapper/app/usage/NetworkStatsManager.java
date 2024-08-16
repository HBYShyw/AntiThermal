package com.oplus.wrapper.app.usage;

import android.app.usage.NetworkStats;
import com.oplus.wrapper.net.NetworkTemplate;

/* loaded from: classes.dex */
public class NetworkStatsManager {
    private final android.app.usage.NetworkStatsManager mNetworkStatsManager;

    public NetworkStatsManager(android.app.usage.NetworkStatsManager networkStatsManager) {
        this.mNetworkStatsManager = networkStatsManager;
    }

    public NetworkStats.Bucket querySummaryForDevice(NetworkTemplate template, long startTime, long endTime) {
        if (template.getNetworkTemplate() == null) {
            return null;
        }
        return this.mNetworkStatsManager.querySummaryForDevice(template.getNetworkTemplate(), startTime, endTime);
    }
}
