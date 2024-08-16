package com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class TotalPredictResult implements Parcelable {
    public static final Parcelable.Creator<TotalPredictResult> CREATOR = new Parcelable.Creator<TotalPredictResult>() { // from class: com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.TotalPredictResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TotalPredictResult createFromParcel(Parcel parcel) {
            TotalPredictResult totalPredictResult = new TotalPredictResult(null, null, null, null);
            totalPredictResult.setSleepCluster((DeepSleepCluster) parcel.readParcelable(AnonymousClass1.class.getClassLoader()));
            totalPredictResult.setWakeCluster((DeepSleepCluster) parcel.readParcelable(AnonymousClass1.class.getClassLoader()));
            totalPredictResult.setOptimalSleepConfig((TrainConfig) parcel.readParcelable(AnonymousClass1.class.getClassLoader()));
            totalPredictResult.setOptimalWakeConfig((TrainConfig) parcel.readParcelable(AnonymousClass1.class.getClassLoader()));
            return totalPredictResult;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TotalPredictResult[] newArray(int i10) {
            return new TotalPredictResult[i10];
        }
    };
    private static final String NULL = "null";
    private static final String TAG = "TotalPredictResult";
    private TrainConfig mOptimalSleepConfig;
    private TrainConfig mOptimalWakeConfig;
    private DeepSleepCluster mSleepCluster;
    private DeepSleepCluster mWakeCluster;

    public TotalPredictResult(DeepSleepCluster deepSleepCluster, DeepSleepCluster deepSleepCluster2) {
        this.mOptimalSleepConfig = null;
        this.mOptimalWakeConfig = null;
        this.mSleepCluster = deepSleepCluster;
        this.mWakeCluster = deepSleepCluster2;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public TrainConfig getOptimalSleepConfig() {
        return this.mOptimalSleepConfig;
    }

    public TrainConfig getOptimalWakeConfig() {
        return this.mOptimalWakeConfig;
    }

    public DeepSleepCluster getSleepCluster() {
        return this.mSleepCluster;
    }

    public DeepSleepCluster getWakeCluster() {
        return this.mWakeCluster;
    }

    public void setOptimalSleepConfig(TrainConfig trainConfig) {
        this.mOptimalSleepConfig = trainConfig;
    }

    public void setOptimalWakeConfig(TrainConfig trainConfig) {
        this.mOptimalWakeConfig = trainConfig;
    }

    public void setSleepCluster(DeepSleepCluster deepSleepCluster) {
        this.mSleepCluster = deepSleepCluster;
    }

    public void setWakeCluster(DeepSleepCluster deepSleepCluster) {
        this.mWakeCluster = deepSleepCluster;
    }

    public String toString() {
        DeepSleepCluster deepSleepCluster = this.mSleepCluster;
        String str = NULL;
        String deepSleepCluster2 = deepSleepCluster != null ? deepSleepCluster.toString() : NULL;
        DeepSleepCluster deepSleepCluster3 = this.mWakeCluster;
        String deepSleepCluster4 = deepSleepCluster3 != null ? deepSleepCluster3.toString() : NULL;
        TrainConfig trainConfig = this.mOptimalSleepConfig;
        String trainConfig2 = trainConfig != null ? trainConfig.toString() : NULL;
        TrainConfig trainConfig3 = this.mOptimalWakeConfig;
        if (trainConfig3 != null) {
            str = trainConfig3.toString();
        }
        return String.format("mSleepCluster=%s,mSleepConfig=%s,mWakeCluster=%s,mWakeConfig=%s", deepSleepCluster2, trainConfig2, deepSleepCluster4, str);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeParcelable(this.mSleepCluster, i10);
        parcel.writeParcelable(this.mWakeCluster, i10);
        parcel.writeParcelable(this.mOptimalSleepConfig, i10);
        parcel.writeParcelable(this.mOptimalWakeConfig, i10);
    }

    public TotalPredictResult(DeepSleepCluster deepSleepCluster, DeepSleepCluster deepSleepCluster2, TrainConfig trainConfig, TrainConfig trainConfig2) {
        this.mSleepCluster = deepSleepCluster;
        this.mWakeCluster = deepSleepCluster2;
        this.mOptimalSleepConfig = trainConfig;
        this.mOptimalWakeConfig = trainConfig2;
    }
}
