package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class NrIconType implements Parcelable {
    public static final Parcelable.Creator<NrIconType> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.NrIconType.1
        @Override // android.os.Parcelable.Creator
        public NrIconType createFromParcel(Parcel in) {
            return new NrIconType(in);
        }

        @Override // android.os.Parcelable.Creator
        public NrIconType[] newArray(int size) {
            return new NrIconType[size];
        }
    };
    public static final int INVALID = -1;
    private static final String TAG = "NrIconType";
    public static final int TYPE_5G_BASIC = 1;
    public static final int TYPE_5G_UWB = 2;
    public static final int TYPE_NONE = 0;
    private int mValue;

    public NrIconType(int val) {
        this.mValue = val;
    }

    public NrIconType(Parcel in) {
        this.mValue = in.readInt();
    }

    public int get() {
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
        return "NrIconType: " + get();
    }
}
