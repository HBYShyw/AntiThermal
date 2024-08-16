package com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep;

import android.util.ArrayMap;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.Map;

/* loaded from: classes.dex */
public class DeepSleepClusterWithPercentile {
    public static final int ANOMALY_TYPE = -1;
    private static final double DEFAULT_MAX_DISTANCE = 0.0d;
    private int mClusterId;
    private int mClusterNum;
    private double mMaxDistance;
    private Map<Integer, Double> mSleepTimePercentiles;
    private Map<Integer, Double> mWakeTimePercentiles;

    public DeepSleepClusterWithPercentile(Map<Integer, Double> map, Map<Integer, Double> map2, double d10, int i10, int i11) {
        this.mSleepTimePercentiles = map;
        this.mWakeTimePercentiles = map2;
        this.mMaxDistance = d10;
        this.mClusterId = i10;
        this.mClusterNum = i11;
    }

    public int getClusterId() {
        return this.mClusterId;
    }

    public int getClusterNum() {
        return this.mClusterNum;
    }

    public double getMaxDistance() {
        return this.mMaxDistance;
    }

    public Map<Integer, Double> getSleepTimePercentiles() {
        return this.mSleepTimePercentiles;
    }

    public Map<Integer, Double> getWakeTimePercentiles() {
        return this.mWakeTimePercentiles;
    }

    public void setClusterId(int i10) {
        this.mClusterId = i10;
    }

    public void setClusterNum(int i10) {
        this.mClusterNum = i10;
    }

    public void setMaxDistance(double d10) {
        this.mMaxDistance = d10;
    }

    public void setSleepTimePercentiles(Map<Integer, Double> map) {
        this.mSleepTimePercentiles = map;
    }

    public void setWakeTimePercentiles(Map<Integer, Double> map) {
        this.mWakeTimePercentiles = map;
    }

    public String toString() {
        return "DeepSleepClusterWithPercentile{mSleepTimePercentiles=" + this.mSleepTimePercentiles + ", mWakeTimePercentiles=" + this.mWakeTimePercentiles + ", mMaxDistance=" + this.mMaxDistance + ", mClusterId=" + this.mClusterId + ", mClusterNum=" + this.mClusterNum + '}';
    }

    public DeepSleepClusterWithPercentile() {
        this(new ArrayMap(), new ArrayMap(), UserProfileInfo.Constant.NA_LAT_LON, -1, 0);
    }
}
