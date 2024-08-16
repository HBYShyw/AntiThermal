package com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep;

import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* loaded from: classes.dex */
public class TrainConfig implements Parcelable {
    private static final int CONFIG_DATA_LENGTH = 4;
    public static final Parcelable.Creator<TrainConfig> CREATOR = new Parcelable.Creator<TrainConfig>() { // from class: com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.TrainConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrainConfig createFromParcel(Parcel parcel) {
            TrainConfig trainConfig = new TrainConfig(0, UserProfileInfo.Constant.NA_LAT_LON, 0);
            trainConfig.mClusterMinPoints = parcel.readInt();
            trainConfig.mClusterEps = parcel.readDouble();
            trainConfig.mDayForPredict = parcel.readInt();
            trainConfig.mType = parcel.readInt();
            return trainConfig;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrainConfig[] newArray(int i10) {
            return new TrainConfig[i10];
        }
    };
    private static final int MULTIPLE = 1000;
    private static final int PRIME_NUM = 31;
    private static final double SIGMA = 1.0E-4d;
    private static final String SPLIT = ",";
    private static final String TAG = "TrainConfig";
    private double mClusterEps;
    private int mClusterMinPoints;
    private int mDayForPredict;
    private int mType;

    public TrainConfig(int i10, double d10, int i11) {
        this.mType = -1;
        this.mClusterMinPoints = i10;
        this.mClusterEps = d10;
        this.mDayForPredict = i11;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TrainConfig)) {
            return false;
        }
        TrainConfig trainConfig = (TrainConfig) obj;
        return this.mClusterMinPoints == trainConfig.getClusterMinPoints() && Math.abs(this.mClusterEps - trainConfig.getClusterEps()) >= SIGMA && this.mDayForPredict == trainConfig.getdayForPredict() && this.mType == trainConfig.getType();
    }

    public double getClusterEps() {
        return this.mClusterEps;
    }

    public int getClusterMinPoints() {
        return this.mClusterMinPoints;
    }

    public int getType() {
        return this.mType;
    }

    public int getdayForPredict() {
        return this.mDayForPredict;
    }

    public int hashCode() {
        return (((((this.mClusterMinPoints * 31) + ((int) (this.mClusterEps * 1000.0d))) * 31) + this.mDayForPredict) * 31) + this.mType;
    }

    public void setClusterEps(double d10) {
        this.mClusterEps = d10;
    }

    public void setClusterMinPoints(int i10) {
        this.mClusterMinPoints = i10;
    }

    public void setType(int i10) {
        this.mType = i10;
    }

    public void setdayForPredict(int i10) {
        this.mDayForPredict = i10;
    }

    public String spliceParameter() {
        return this.mClusterMinPoints + "," + this.mClusterEps + "," + this.mDayForPredict + "," + this.mType;
    }

    public String toString() {
        return "TrainConfig{mClusterMinPoints=" + this.mClusterMinPoints + ", mClusterEps=" + this.mClusterEps + ", mDayForPredict=" + this.mDayForPredict + ", mType=" + this.mType + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeInt(this.mClusterMinPoints);
        parcel.writeDouble(this.mClusterEps);
        parcel.writeInt(this.mDayForPredict);
        parcel.writeInt(this.mType);
    }

    public TrainConfig(String str) {
        this.mType = -1;
        String[] split = str.split(",");
        if (split.length == 4) {
            try {
                this.mClusterMinPoints = Integer.parseInt(split[0]);
                this.mClusterEps = Double.parseDouble(split[1]);
                this.mDayForPredict = Integer.parseInt(split[2]);
                this.mType = Integer.parseInt(split[3]);
            } catch (NumberFormatException e10) {
                SDKLog.w(TAG, e10.toString());
            }
        }
    }
}
