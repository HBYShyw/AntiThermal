package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusTxRxInfo implements Parcelable {
    public static final Parcelable.Creator<OplusTxRxInfo> CREATOR = new Parcelable.Creator<OplusTxRxInfo>() { // from class: com.android.internal.telephony.OplusTxRxInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusTxRxInfo createFromParcel(Parcel in) {
            return new OplusTxRxInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusTxRxInfo[] newArray(int size) {
            return new OplusTxRxInfo[size];
        }
    };
    private OplusRxChainInfo mRxChain0;
    private int mRxChain0Valid;
    private OplusRxChainInfo mRxChain1;
    private int mRxChain1Valid;
    private OplusRxChainInfo mRxChain2;
    private int mRxChain2Valid;
    private OplusRxChainInfo mRxChain3;
    private int mRxChain3Valid;
    private OplusTxInfo mTx;
    private int mTxValid;

    public OplusTxRxInfo(int mRxChain0Valid, OplusRxChainInfo mRxChain0, int mRxChain1Valid, OplusRxChainInfo mRxChain1, int mRxChain2Valid, OplusRxChainInfo mRxChain2, int mRxChain3Valid, OplusRxChainInfo mRxChain3, int mTxValid, OplusTxInfo mTx) {
        this.mRxChain0Valid = mRxChain0Valid;
        this.mRxChain0 = mRxChain0;
        this.mRxChain1Valid = mRxChain1Valid;
        this.mRxChain1 = mRxChain1;
        this.mRxChain2Valid = mRxChain2Valid;
        this.mRxChain2 = mRxChain2;
        this.mRxChain3Valid = mRxChain3Valid;
        this.mRxChain3 = mRxChain3;
        this.mTxValid = mTxValid;
        this.mTx = mTx;
    }

    protected OplusTxRxInfo(Parcel in) {
        this.mRxChain0Valid = in.readInt();
        this.mRxChain0 = (OplusRxChainInfo) in.readParcelable(OplusRxChainInfo.class.getClassLoader());
        this.mRxChain1Valid = in.readInt();
        this.mRxChain1 = (OplusRxChainInfo) in.readParcelable(OplusRxChainInfo.class.getClassLoader());
        this.mRxChain2Valid = in.readInt();
        this.mRxChain2 = (OplusRxChainInfo) in.readParcelable(OplusRxChainInfo.class.getClassLoader());
        this.mRxChain3Valid = in.readInt();
        this.mRxChain3 = (OplusRxChainInfo) in.readParcelable(OplusRxChainInfo.class.getClassLoader());
        this.mTxValid = in.readInt();
        this.mTx = (OplusTxInfo) in.readParcelable(OplusTxInfo.class.getClassLoader());
    }

    public int getRxChain0Valid() {
        return this.mRxChain0Valid;
    }

    public int getRxChain1Valid() {
        return this.mRxChain1Valid;
    }

    public int getRxChain2Valid() {
        return this.mRxChain2Valid;
    }

    public int getRxChain3Valid() {
        return this.mRxChain3Valid;
    }

    public int getTxValid() {
        return this.mTxValid;
    }

    public OplusRxChainInfo getRxChain0() {
        return this.mRxChain0;
    }

    public OplusRxChainInfo getRxChain1() {
        return this.mRxChain1;
    }

    public OplusRxChainInfo getRxChain2() {
        return this.mRxChain2;
    }

    public OplusRxChainInfo getRxChain3() {
        return this.mRxChain3;
    }

    public OplusTxInfo getTx() {
        return this.mTx;
    }

    public String toString() {
        return "mRxChain0Valid=" + this.mRxChain0Valid + ", mRxChain0=(" + this.mRxChain0.toString() + "), mRxChain1Valid=" + this.mRxChain1Valid + ", mRxChain1=(" + this.mRxChain1.toString() + ")," + this.mRxChain2Valid + ", mRxChain2=(" + this.mRxChain2.toString() + "), mRxChain3Valid=" + this.mRxChain3Valid + ", mRxChain3=(" + this.mRxChain3.toString() + "),mTxValid=" + this.mTxValid + ", mTx=(" + this.mTx.toString() + ")";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mRxChain0Valid);
        parcel.writeParcelable(this.mRxChain0, i);
        parcel.writeInt(this.mRxChain1Valid);
        parcel.writeParcelable(this.mRxChain1, i);
        parcel.writeInt(this.mRxChain2Valid);
        parcel.writeParcelable(this.mRxChain2, i);
        parcel.writeInt(this.mRxChain3Valid);
        parcel.writeParcelable(this.mRxChain3, i);
        parcel.writeInt(this.mTxValid);
        parcel.writeParcelable(this.mTx, i);
    }
}
