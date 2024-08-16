package com.oplus.network.stats;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class AppFreezeStats implements Parcelable {
    public static final Parcelable.Creator<AppFreezeStats> CREATOR = new Parcelable.Creator<AppFreezeStats>() { // from class: com.oplus.network.stats.AppFreezeStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeStats createFromParcel(Parcel in) {
            int uid = in.readInt();
            int expired = in.readInt();
            long rxBytes = in.readLong();
            long txBytes = in.readLong();
            int rxCount = in.readInt();
            int txCount = in.readInt();
            int occurFlag = in.readInt();
            return new AppFreezeStats(uid, expired, rxBytes, txBytes, rxCount, txCount, occurFlag);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeStats[] newArray(int size) {
            return new AppFreezeStats[size];
        }
    };
    public int mExpired;
    public int mOccurFlag;
    public long mRxBytes;
    public int mRxCount;
    public long mTxBytes;
    public int mTxCount;
    public int mUid;

    public AppFreezeStats(int mUid, int mExpired, long mRxBytes, long mTxBytes, int mRxCount, int mTxCount, int mOccurFlag) {
        this.mUid = mUid;
        this.mExpired = mExpired;
        this.mRxBytes = mRxBytes;
        this.mTxBytes = mTxBytes;
        this.mRxCount = mRxCount;
        this.mTxCount = mTxCount;
        this.mOccurFlag = mOccurFlag;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mUid);
        parcel.writeInt(this.mExpired);
        parcel.writeLong(this.mRxBytes);
        parcel.writeLong(this.mTxBytes);
        parcel.writeInt(this.mRxCount);
        parcel.writeInt(this.mTxCount);
        parcel.writeInt(this.mOccurFlag);
    }

    public String toString() {
        return "AppFreezeStats{mUid=" + this.mUid + ", mExpired=" + this.mExpired + ", mRxBytes=" + this.mRxBytes + ", mTxBytes=" + this.mTxBytes + ", mRxCount=" + this.mRxCount + ", mTxCount=" + this.mTxCount + ", mOccurFlag=" + this.mOccurFlag + '}';
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof AppFreezeStats) && this.mUid == ((AppFreezeStats) obj).mUid && this.mExpired == ((AppFreezeStats) obj).mExpired && this.mRxBytes == ((AppFreezeStats) obj).mRxBytes && this.mTxBytes == ((AppFreezeStats) obj).mTxBytes && this.mRxCount == ((AppFreezeStats) obj).mRxCount && this.mTxCount == ((AppFreezeStats) obj).mTxCount && this.mOccurFlag == ((AppFreezeStats) obj).mOccurFlag;
    }
}
