package com.oplus.wrapper.app.usage;

import java.util.Map;

/* loaded from: classes.dex */
public class UsageStatsManager {
    private final android.app.usage.UsageStatsManager mUsageStatsManager;
    public static final int STANDBY_BUCKET_EXEMPTED = getStandbyBucketExempted();
    public static final int STANDBY_BUCKET_ACTIVE = getStandbyBucketActive();
    public static final int STANDBY_BUCKET_WORKING_SET = getStandbyBucketWorkingSet();
    public static final int STANDBY_BUCKET_FREQUENT = getStandbyBucketFrequent();
    public static final int STANDBY_BUCKET_RARE = getStandbyBucketRare();
    public static final int STANDBY_BUCKET_RESTRICTED = getStandbyBucketRestricted();
    public static final int STANDBY_BUCKET_NEVER = getStandbyBucketNever();

    public UsageStatsManager(android.app.usage.UsageStatsManager usageStatsManager) {
        this.mUsageStatsManager = usageStatsManager;
    }

    private static int getStandbyBucketNever() {
        return 50;
    }

    private static int getStandbyBucketRestricted() {
        return 45;
    }

    private static int getStandbyBucketExempted() {
        return 5;
    }

    private static int getStandbyBucketActive() {
        return 10;
    }

    private static int getStandbyBucketWorkingSet() {
        return 20;
    }

    private static int getStandbyBucketFrequent() {
        return 30;
    }

    private static int getStandbyBucketRare() {
        return 40;
    }

    public void setAppStandbyBuckets(Map<String, Integer> appBuckets) {
        this.mUsageStatsManager.setAppStandbyBuckets(appBuckets);
    }
}
