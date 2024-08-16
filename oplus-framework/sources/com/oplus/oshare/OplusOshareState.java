package com.oplus.oshare;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public enum OplusOshareState implements Parcelable {
    IDLE,
    READY,
    TRANSIT_WAIT,
    TRANSITING,
    CANCEL,
    TRANSIT_SUCCESS,
    TRANSIT_FAILED,
    TRANSIT_REJECT,
    TRANSIT_TIMEOUT,
    BUSUY,
    BUSY,
    CANCEL_WAIT,
    SPACE_NOT_ENOUGH;

    public static final Parcelable.Creator<OplusOshareState> CREATOR = new Parcelable.Creator<OplusOshareState>() { // from class: com.oplus.oshare.OplusOshareState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusOshareState createFromParcel(Parcel in) {
            return (OplusOshareState) in.readSerializable();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusOshareState[] newArray(int size) {
            return new OplusOshareState[size];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }
}
