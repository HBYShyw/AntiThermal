package com.aiunit.aon.utils.core;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class InfoResult implements Parcelable {
    public static final Parcelable.Creator<InfoResult> CREATOR = new Parcelable.Creator<InfoResult>() { // from class: com.aiunit.aon.utils.core.InfoResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InfoResult createFromParcel(Parcel parcel) {
            return new InfoResult(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InfoResult[] newArray(int i) {
            return new InfoResult[i];
        }
    };
    private String mInfoResult;

    protected InfoResult(Parcel parcel) {
        readFromParcel(parcel);
    }

    public InfoResult(String str) {
        this.mInfoResult = str;
    }

    public void readFromParcel(Parcel parcel) {
        this.mInfoResult = parcel.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getInfoResult() {
        return this.mInfoResult;
    }

    public void setInfoResult(String str) {
        this.mInfoResult = str;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mInfoResult);
    }
}
