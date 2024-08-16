package com.oplus.deepthinker.sdk.app.userprofile.labels;

import com.google.gson.GsonBuilder;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class WifiLocationLabel {
    private double mAccuracy;
    private int mClusterPointsNum;
    private double mLatitude;
    private double mLongitude;
    private int mRadius;
    private int mSurvivalTime;
    private Set<String> mBssidSet = new HashSet();
    private Set<String> mSsidSet = new HashSet();
    private Set<String> mConfigName = new HashSet();

    public WifiLocationLabel(double d10, double d11, int i10, int i11, double d12, Set<String> set, Set<String> set2, Set<String> set3) {
        this.mLongitude = d10;
        this.mLatitude = d11;
        this.mRadius = i10;
        this.mClusterPointsNum = i11;
        this.mAccuracy = d12;
        this.mBssidSet.addAll(set);
        this.mSsidSet.addAll(set2);
        this.mConfigName.addAll(set3);
    }

    public double getAccuracy() {
        return this.mAccuracy;
    }

    public Set<String> getBssidSet() {
        return this.mBssidSet;
    }

    public int getClusterPointsNum() {
        return this.mClusterPointsNum;
    }

    public Set<String> getConfigName() {
        return this.mConfigName;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public int getRadius() {
        return this.mRadius;
    }

    public Set<String> getSsidSet() {
        return this.mSsidSet;
    }

    public int getSurvivalTime() {
        return this.mSurvivalTime;
    }

    public void setAccuracy(double d10) {
        this.mAccuracy = d10;
    }

    public void setBssidSet(Set<String> set) {
        this.mBssidSet = set;
    }

    public void setClusterPointsNum(int i10) {
        this.mClusterPointsNum = i10;
    }

    public void setConfigName(Set<String> set) {
        this.mConfigName = set;
    }

    public void setSsidSet(Set<String> set) {
        this.mSsidSet = set;
    }

    public void setSurvivalTime(int i10) {
        this.mSurvivalTime = i10;
    }

    public String toJsonString() {
        return new GsonBuilder().create().toJson(this);
    }

    public String toString() {
        return "WifiLocationLabel{mLongitude=" + this.mLongitude + ", mLatitude=" + this.mLatitude + ", mRadius=" + this.mRadius + ", mClusterPointsNum=" + this.mClusterPointsNum + ", mAccuracy=" + this.mAccuracy + ", mSsidSet=" + this.mSsidSet.toString() + ", mBssidSet=" + this.mBssidSet.toString() + ", mConfigName=" + this.mConfigName.toString() + ", mSurvivalTime=" + this.mSurvivalTime + '}';
    }
}
