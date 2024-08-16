package com.aiunit.aon.utils.core;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class ErrorResult implements Parcelable {
    public static final Parcelable.Creator<ErrorResult> CREATOR = new Parcelable.Creator<ErrorResult>() { // from class: com.aiunit.aon.utils.core.ErrorResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ErrorResult createFromParcel(Parcel parcel) {
            return new ErrorResult(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ErrorResult[] newArray(int i) {
            return new ErrorResult[i];
        }
    };
    private int mResultCode;

    public ErrorResult(int i) {
        this.mResultCode = i;
    }

    protected ErrorResult(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void readFromParcel(Parcel parcel) {
        this.mResultCode = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getResultCode() {
        return this.mResultCode;
    }

    public void setResultCode(int i) {
        this.mResultCode = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mResultCode);
    }
}
