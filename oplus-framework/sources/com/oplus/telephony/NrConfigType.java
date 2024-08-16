package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class NrConfigType implements Parcelable {
    public static final Parcelable.Creator<NrConfigType> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.NrConfigType.1
        @Override // android.os.Parcelable.Creator
        public NrConfigType createFromParcel(Parcel in) {
            return new NrConfigType(in);
        }

        @Override // android.os.Parcelable.Creator
        public NrConfigType[] newArray(int size) {
            return new NrConfigType[size];
        }
    };
    public static final int INVALID = -1;
    public static final int NSA_CONFIGURATION = 0;
    public static final int SA_CONFIGURATION = 1;
    private static final String TAG = "NrConfigType";
    private int mValue;

    public NrConfigType(int val) {
        this.mValue = val;
    }

    public NrConfigType(Parcel in) {
        this.mValue = in.readInt();
    }

    public int getNrConfigType() {
        return this.mValue;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mValue);
    }

    public void readFromParcel(Parcel in) {
        this.mValue = in.readInt();
    }

    public String toString() {
        return "NrConfigType: " + getNrConfigType();
    }
}
