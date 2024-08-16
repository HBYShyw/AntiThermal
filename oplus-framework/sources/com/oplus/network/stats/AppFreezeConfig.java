package com.oplus.network.stats;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class AppFreezeConfig implements Parcelable {
    public static final Parcelable.Creator<AppFreezeConfig> CREATOR = new Parcelable.Creator<AppFreezeConfig>() { // from class: com.oplus.network.stats.AppFreezeConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeConfig createFromParcel(Parcel in) {
            int enable = in.readInt();
            int uid = in.readInt();
            int timeFlag = in.readInt();
            return new AppFreezeConfig(enable, uid, timeFlag);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeConfig[] newArray(int size) {
            return new AppFreezeConfig[size];
        }
    };
    public int mEnable;
    public int mFgUid;
    public int mTimeFlag;

    public AppFreezeConfig(int mEnable, int mFgUid, int mTimeFlag) {
        this.mEnable = mEnable;
        this.mFgUid = mFgUid;
        this.mTimeFlag = mTimeFlag;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mEnable);
        parcel.writeInt(this.mFgUid);
        parcel.writeInt(this.mTimeFlag);
    }

    public String toString() {
        return "AppFreezeConfig{mEnable=" + this.mEnable + ", mFgUid=" + this.mFgUid + ", mTimeFlag=" + this.mTimeFlag + '}';
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof AppFreezeConfig) && this.mEnable == ((AppFreezeConfig) obj).mEnable && this.mFgUid == ((AppFreezeConfig) obj).mFgUid && this.mTimeFlag == ((AppFreezeConfig) obj).mTimeFlag;
    }
}
