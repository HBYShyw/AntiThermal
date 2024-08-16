package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusRxChainInfo implements Parcelable {
    public static final Parcelable.Creator<OplusRxChainInfo> CREATOR = new Parcelable.Creator<OplusRxChainInfo>() { // from class: com.android.internal.telephony.OplusRxChainInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusRxChainInfo createFromParcel(Parcel in) {
            return new OplusRxChainInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusRxChainInfo[] newArray(int size) {
            return new OplusRxChainInfo[size];
        }
    };
    private int mEcio;
    private int mIsRadioTurned;
    private int mPhase;
    private int mRscp;
    private int mRsrp;
    private int mRxPwr;

    public OplusRxChainInfo(int mIsRadioTurned, int mRxPwr, int mEcio, int mRscp, int mRsrp, int mPhase) {
        this.mIsRadioTurned = mIsRadioTurned;
        this.mRxPwr = mRxPwr;
        this.mEcio = mEcio;
        this.mRscp = mRscp;
        this.mRsrp = mRsrp;
        this.mPhase = mPhase;
    }

    protected OplusRxChainInfo(Parcel in) {
        this.mIsRadioTurned = in.readInt();
        this.mRxPwr = in.readInt();
        this.mEcio = in.readInt();
        this.mRscp = in.readInt();
        this.mRsrp = in.readInt();
        this.mPhase = in.readInt();
    }

    public int getIsRadioTurned() {
        return this.mIsRadioTurned;
    }

    public int getRxPwr() {
        return this.mRxPwr;
    }

    public int getEcio() {
        return this.mEcio;
    }

    public int getRscp() {
        return this.mRscp;
    }

    public int getRsrp() {
        return this.mRsrp;
    }

    public int getPhase() {
        return this.mPhase;
    }

    public String toString() {
        return "mIsRadioTurned=" + this.mIsRadioTurned + ", mRxPwr=" + this.mRxPwr + ", mEcio=" + this.mEcio + ", mRscp=" + this.mRscp + ", mRsrp=" + this.mRsrp + ", mPhase=" + this.mPhase;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mIsRadioTurned);
        parcel.writeInt(this.mRxPwr);
        parcel.writeInt(this.mEcio);
        parcel.writeInt(this.mRscp);
        parcel.writeInt(this.mRsrp);
        parcel.writeInt(this.mPhase);
    }
}
