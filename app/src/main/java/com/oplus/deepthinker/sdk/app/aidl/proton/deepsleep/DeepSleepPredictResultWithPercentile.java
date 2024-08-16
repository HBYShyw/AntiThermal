package com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class DeepSleepPredictResultWithPercentile {
    private List<DeepSleepClusterWithPercentile> mDeepSleepClusterWithPercentiles;

    public DeepSleepPredictResultWithPercentile(List<DeepSleepClusterWithPercentile> list) {
        this.mDeepSleepClusterWithPercentiles = list;
    }

    public List<DeepSleepClusterWithPercentile> getDeepSleepClusterWithPercentiles() {
        return this.mDeepSleepClusterWithPercentiles;
    }

    public void setDeepSleepClusterWithPercentiles(List<DeepSleepClusterWithPercentile> list) {
        this.mDeepSleepClusterWithPercentiles = list;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder("DeepSleepPredictResultWithPercentile:");
        List<DeepSleepClusterWithPercentile> list = this.mDeepSleepClusterWithPercentiles;
        if (list != null && list.size() > 0) {
            for (DeepSleepClusterWithPercentile deepSleepClusterWithPercentile : this.mDeepSleepClusterWithPercentiles) {
                if (deepSleepClusterWithPercentile != null) {
                    sb2.append(deepSleepClusterWithPercentile);
                }
            }
        }
        return sb2.toString();
    }

    public DeepSleepPredictResultWithPercentile() {
        this(new ArrayList());
    }
}
