package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class SmsResult implements Parcelable {
    public static final Parcelable.Creator<SmsResult> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.SmsResult.1
        @Override // android.os.Parcelable.Creator
        public SmsResult createFromParcel(Parcel in) {
            return new SmsResult(in);
        }

        @Override // android.os.Parcelable.Creator
        public SmsResult[] newArray(int size) {
            return new SmsResult[size];
        }
    };
    private static final String TAG = "SmsResult";
    private String mAckPDU;
    private int mErrorCode;
    private int mMessageRef;

    public SmsResult(int messageRef, String ackPDU, int errorCode) {
        this.mMessageRef = messageRef;
        this.mAckPDU = ackPDU;
        this.mErrorCode = errorCode;
    }

    public SmsResult(Parcel in) {
        this.mMessageRef = in.readInt();
        this.mAckPDU = in.readString();
        this.mErrorCode = in.readInt();
    }

    public int getMessageRef() {
        return this.mMessageRef;
    }

    public String getAckPDU() {
        return this.mAckPDU;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mMessageRef);
        out.writeString(this.mAckPDU);
        out.writeInt(this.mErrorCode);
    }

    public void readFromParcel(Parcel in) {
        this.mMessageRef = in.readInt();
        this.mAckPDU = in.readString();
        this.mErrorCode = in.readInt();
    }

    public String toString() {
        return "SmsResult{mMessageRef=" + getMessageRef() + ", mErrorCode=" + getErrorCode() + ", mAckPDU='" + getAckPDU() + "'}";
    }
}
