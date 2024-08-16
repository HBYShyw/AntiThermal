package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class NrUwbIconRefreshTimerType implements Parcelable {
    public static final Parcelable.Creator<NrUwbIconRefreshTimerType> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.NrUwbIconRefreshTimerType.1
        @Override // android.os.Parcelable.Creator
        public NrUwbIconRefreshTimerType createFromParcel(Parcel in) {
            return new NrUwbIconRefreshTimerType(in);
        }

        @Override // android.os.Parcelable.Creator
        public NrUwbIconRefreshTimerType[] newArray(int size) {
            return new NrUwbIconRefreshTimerType[size];
        }
    };
    public static final int IDLE = 2;
    public static final int IDLE_TO_CONNECT = 1;
    public static final int SCG_TO_MCG = 0;
    private static final String TAG = "NrUwbIconRefreshTimerType";
    private int mNrUwbIconRefreshTimerType;

    public NrUwbIconRefreshTimerType(int type) {
        this.mNrUwbIconRefreshTimerType = type;
    }

    public NrUwbIconRefreshTimerType(Parcel in) {
        this.mNrUwbIconRefreshTimerType = in.readInt();
    }

    public int get() {
        return this.mNrUwbIconRefreshTimerType;
    }

    public static boolean isValid(int mode) {
        switch (mode) {
            case 0:
            case 1:
            case 2:
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
        out.writeInt(this.mNrUwbIconRefreshTimerType);
    }

    public void readFromParcel(Parcel in) {
        this.mNrUwbIconRefreshTimerType = in.readInt();
    }

    public String toString() {
        return "NrUwbIconRefreshTimerType: " + get();
    }
}
