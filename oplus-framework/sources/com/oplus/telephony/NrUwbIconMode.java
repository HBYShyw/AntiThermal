package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class NrUwbIconMode implements Parcelable {
    public static final int CONNECTED = 1;
    public static final int CONNECTED_AND_IDLE = 3;
    public static final Parcelable.Creator<NrUwbIconMode> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.NrUwbIconMode.1
        @Override // android.os.Parcelable.Creator
        public NrUwbIconMode createFromParcel(Parcel in) {
            return new NrUwbIconMode(in);
        }

        @Override // android.os.Parcelable.Creator
        public NrUwbIconMode[] newArray(int size) {
            return new NrUwbIconMode[size];
        }
    };
    public static final int IDLE = 2;
    public static final int NONE = 0;
    private static final String TAG = "NrUwbIconMode";
    private int mNrUwbIconMode;

    public NrUwbIconMode(int mode) {
        this.mNrUwbIconMode = mode;
    }

    public NrUwbIconMode(Parcel in) {
        this.mNrUwbIconMode = in.readInt();
    }

    public int get() {
        return this.mNrUwbIconMode;
    }

    public static boolean isValid(int mode) {
        switch (mode) {
            case 0:
            case 1:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mNrUwbIconMode);
    }

    public void readFromParcel(Parcel in) {
        this.mNrUwbIconMode = in.readInt();
    }

    public String toString() {
        return "NrUwbIconMode: " + get();
    }
}
