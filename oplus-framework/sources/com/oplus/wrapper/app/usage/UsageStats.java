package com.oplus.wrapper.app.usage;

/* loaded from: classes.dex */
public class UsageStats {
    private final android.app.usage.UsageStats mUsageStats;

    public UsageStats(android.app.usage.UsageStats usageStats) {
        this.mUsageStats = usageStats;
    }

    public int getAppLaunchCount() {
        return this.mUsageStats.getAppLaunchCount();
    }

    public int getLastEvent() {
        return this.mUsageStats.mLastEvent;
    }

    public int getLaunchCount() {
        return this.mUsageStats.mLaunchCount;
    }
}
