package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusTxInfo implements Parcelable {
    public static final Parcelable.Creator<OplusTxInfo> CREATOR = new Parcelable.Creator<OplusTxInfo>() { // from class: com.android.internal.telephony.OplusTxInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusTxInfo createFromParcel(Parcel in) {
            return new OplusTxInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusTxInfo[] newArray(int size) {
            return new OplusTxInfo[size];
        }
    };
    private int mIsInTraffic;
    private int mTxPwr;

    public OplusTxInfo(int mIsInTraffic, int mTxPwr) {
        this.mIsInTraffic = mIsInTraffic;
        this.mTxPwr = mTxPwr;
    }

    protected OplusTxInfo(Parcel in) {
        this.mIsInTraffic = in.readInt();
        this.mTxPwr = in.readInt();
    }

    public int getIsInTraffic() {
        return this.mIsInTraffic;
    }

    public int getTxPwr() {
        return this.mTxPwr;
    }

    public String toString() {
        return "mIsInTraffic=" + this.mIsInTraffic + ", mTxPwr=" + this.mTxPwr;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mIsInTraffic);
        parcel.writeInt(this.mTxPwr);
    }
}
