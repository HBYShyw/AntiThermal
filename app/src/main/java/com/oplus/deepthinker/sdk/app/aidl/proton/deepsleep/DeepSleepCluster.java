package com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep;

import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* loaded from: classes.dex */
public class DeepSleepCluster implements Parcelable {
    public static final int ANOMALY_TYPE = -1;
    public static final Parcelable.Creator<DeepSleepCluster> CREATOR = new Parcelable.Creator<DeepSleepCluster>() { // from class: com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepCluster.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeepSleepCluster createFromParcel(Parcel parcel) {
            DeepSleepCluster deepSleepCluster = new DeepSleepCluster(UserProfileInfo.Constant.NA_LAT_LON, UserProfileInfo.Constant.NA_LAT_LON);
            deepSleepCluster.mSleepTimePeriod = parcel.readDouble();
            deepSleepCluster.mWakeTimePeriod = parcel.readDouble();
            deepSleepCluster.mMaxDistance = parcel.readDouble();
            deepSleepCluster.mClusterId = parcel.readInt();
            deepSleepCluster.mClusterNum = parcel.readInt();
            deepSleepCluster.mSleepMinValue = parcel.readDouble();
            deepSleepCluster.mSleepMaxValue = parcel.readDouble();
            deepSleepCluster.mWakeMinValue = parcel.readDouble();
            deepSleepCluster.mWakeMaxValue = parcel.readDouble();
            return deepSleepCluster;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeepSleepCluster[] newArray(int i10) {
            return new DeepSleepCluster[i10];
        }
    };
    private static final double DEFAULT_MAX_DISTANCE = 0.0d;
    private int mClusterId;
    private int mClusterNum;
    private double mMaxDistance;
    private double mSleepMaxValue;
    private double mSleepMinValue;
    private double mSleepTimePeriod;
    private double mWakeMaxValue;
    private double mWakeMinValue;
    private double mWakeTimePeriod;

    public DeepSleepCluster(double d10, double d11) {
        this.mSleepMinValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mSleepMaxValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mWakeMinValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mWakeMaxValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mClusterId = -1;
        this.mClusterNum = 0;
        this.mSleepTimePeriod = d10;
        this.mWakeTimePeriod = d11;
        this.mMaxDistance = UserProfileInfo.Constant.NA_LAT_LON;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
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

    public double getSleepMaxValue() {
        return this.mSleepMaxValue;
    }

    public double getSleepMinValue() {
        return this.mSleepMinValue;
    }

    public double getSleepTimePeriod() {
        return this.mSleepTimePeriod;
    }

    public double getWakeMaxValue() {
        return this.mWakeMaxValue;
    }

    public double getWakeMinValue() {
        return this.mWakeMinValue;
    }

    public double getWakeTimePeriod() {
        return this.mWakeTimePeriod;
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

    public void setSleepTimePeriod(double d10) {
        this.mSleepTimePeriod = d10;
    }

    public void setWakeTimePeriod(double d10) {
        this.mWakeTimePeriod = d10;
    }

    public String toString() {
        return String.format("DeepSleepCluster:clusterId=%d sleep=%.2f wake=%.2f clusterNum=%d maxDistance=%.2f", Integer.valueOf(this.mClusterId), Double.valueOf(this.mSleepTimePeriod), Double.valueOf(this.mWakeTimePeriod), Integer.valueOf(this.mClusterNum), Double.valueOf(this.mMaxDistance));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeDouble(this.mSleepTimePeriod);
        parcel.writeDouble(this.mWakeTimePeriod);
        parcel.writeDouble(this.mMaxDistance);
        parcel.writeInt(this.mClusterId);
        parcel.writeInt(this.mClusterNum);
        parcel.writeDouble(this.mSleepMinValue);
        parcel.writeDouble(this.mSleepMaxValue);
        parcel.writeDouble(this.mWakeMinValue);
        parcel.writeDouble(this.mWakeMaxValue);
    }

    public DeepSleepCluster(double d10, double d11, double d12, double d13, double d14, double d15) {
        this.mClusterId = -1;
        this.mClusterNum = 0;
        this.mSleepTimePeriod = d10;
        this.mWakeTimePeriod = d11;
        this.mMaxDistance = UserProfileInfo.Constant.NA_LAT_LON;
        this.mSleepMinValue = d12;
        this.mSleepMaxValue = d13;
        this.mWakeMinValue = d14;
        this.mWakeMaxValue = d15;
    }

    public DeepSleepCluster(double d10, double d11, double d12) {
        this.mSleepMinValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mSleepMaxValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mWakeMinValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mWakeMaxValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mClusterId = -1;
        this.mClusterNum = 0;
        this.mSleepTimePeriod = d10;
        this.mWakeTimePeriod = d11;
        this.mMaxDistance = d12;
    }

    public DeepSleepCluster(int i10, double d10, double d11, double d12) {
        this.mSleepMinValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mSleepMaxValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mWakeMinValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mWakeMaxValue = UserProfileInfo.Constant.NA_LAT_LON;
        this.mClusterNum = 0;
        this.mSleepTimePeriod = d10;
        this.mWakeTimePeriod = d11;
        this.mMaxDistance = d12;
        this.mClusterId = i10;
    }
}
