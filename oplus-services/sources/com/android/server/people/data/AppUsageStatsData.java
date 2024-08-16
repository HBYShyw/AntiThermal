package com.android.server.people.data;

import com.android.internal.annotations.VisibleForTesting;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AppUsageStatsData {
    private int mChosenCount;
    private int mLaunchCount;

    @VisibleForTesting
    public AppUsageStatsData(int i, int i2) {
        this.mChosenCount = i;
        this.mLaunchCount = i2;
    }

    public AppUsageStatsData() {
    }

    public int getLaunchCount() {
        return this.mLaunchCount;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void incrementLaunchCountBy(int i) {
        this.mLaunchCount += i;
    }

    public int getChosenCount() {
        return this.mChosenCount;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void incrementChosenCountBy(int i) {
        this.mChosenCount += i;
    }
}
