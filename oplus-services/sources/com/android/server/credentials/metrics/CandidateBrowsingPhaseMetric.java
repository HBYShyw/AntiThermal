package com.android.server.credentials.metrics;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CandidateBrowsingPhaseMetric {
    private int mEntryEnum = EntryEnum.UNKNOWN.getMetricCode();
    private int mProviderUid = -1;

    public void setEntryEnum(int i) {
        this.mEntryEnum = i;
    }

    public int getEntryEnum() {
        return this.mEntryEnum;
    }

    public void setProviderUid(int i) {
        this.mProviderUid = i;
    }

    public int getProviderUid() {
        return this.mProviderUid;
    }
}
