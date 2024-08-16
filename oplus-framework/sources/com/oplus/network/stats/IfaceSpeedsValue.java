package com.oplus.network.stats;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class IfaceSpeedsValue implements Parcelable {
    public static final Parcelable.Creator<IfaceSpeedsValue> CREATOR = new Parcelable.Creator<IfaceSpeedsValue>() { // from class: com.oplus.network.stats.IfaceSpeedsValue.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IfaceSpeedsValue createFromParcel(Parcel in) {
            long rxSpeed = in.readLong();
            long txSpeed = in.readLong();
            return new IfaceSpeedsValue(rxSpeed, txSpeed);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IfaceSpeedsValue[] newArray(int size) {
            return new IfaceSpeedsValue[size];
        }
    };
    public long mRxSpeed;
    public long mTxSpeed;

    public IfaceSpeedsValue(long mRxSpeed, long mTxSpeed) {
        this.mRxSpeed = mRxSpeed;
        this.mTxSpeed = mTxSpeed;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mRxSpeed);
        parcel.writeLong(this.mTxSpeed);
    }

    public String toString() {
        return "IfaceSpeedsValue{mRxSpeed=" + this.mRxSpeed + ", mTxSpeed=" + this.mTxSpeed + '}';
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof IfaceSpeedsValue) && this.mTxSpeed == ((IfaceSpeedsValue) obj).mTxSpeed && this.mRxSpeed == ((IfaceSpeedsValue) obj).mRxSpeed;
    }
}
