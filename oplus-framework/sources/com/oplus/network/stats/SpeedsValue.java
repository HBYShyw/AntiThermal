package com.oplus.network.stats;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class SpeedsValue implements Parcelable {
    public static final Parcelable.Creator<SpeedsValue> CREATOR = new Parcelable.Creator<SpeedsValue>() { // from class: com.oplus.network.stats.SpeedsValue.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SpeedsValue createFromParcel(Parcel in) {
            int uid = in.readInt();
            int ifindex = in.readInt();
            long rxSpeed = in.readLong();
            long txSpeed = in.readLong();
            return new SpeedsValue(uid, ifindex, rxSpeed, txSpeed);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SpeedsValue[] newArray(int size) {
            return new SpeedsValue[size];
        }
    };
    public int mIfindex;
    public long mRxSpeed;
    public long mTxSpeed;
    public int mUid;

    public SpeedsValue(int mUid, int mIfindex, long mRxSpeed, long mTxSpeed) {
        this.mUid = mUid;
        this.mIfindex = mIfindex;
        this.mRxSpeed = mRxSpeed;
        this.mTxSpeed = mTxSpeed;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mUid);
        parcel.writeInt(this.mIfindex);
        parcel.writeLong(this.mRxSpeed);
        parcel.writeLong(this.mTxSpeed);
    }

    public String toString() {
        return "SpeedsValue{mUid=" + this.mUid + ", mIfindex=" + this.mIfindex + ", mRxSpeed=" + this.mRxSpeed + ", mTxSpeed=" + this.mTxSpeed + '}';
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof SpeedsValue) && this.mUid == ((SpeedsValue) obj).mUid && this.mIfindex == ((SpeedsValue) obj).mIfindex && this.mRxSpeed == ((SpeedsValue) obj).mRxSpeed && this.mTxSpeed == ((SpeedsValue) obj).mTxSpeed;
    }
}
