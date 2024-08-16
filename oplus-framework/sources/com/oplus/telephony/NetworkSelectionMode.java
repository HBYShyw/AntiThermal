package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class NetworkSelectionMode implements Parcelable {
    public static final int ACCESS_MODE_INVALID = 0;
    public static final Parcelable.Creator<NetworkSelectionMode> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.NetworkSelectionMode.1
        @Override // android.os.Parcelable.Creator
        public NetworkSelectionMode createFromParcel(Parcel in) {
            return new NetworkSelectionMode(in);
        }

        @Override // android.os.Parcelable.Creator
        public NetworkSelectionMode[] newArray(int size) {
            return new NetworkSelectionMode[size];
        }
    };
    private static final String TAG = "NetworkSelectionMode";
    private int mAccessMode;
    private boolean mIsManual;

    public NetworkSelectionMode(int accessMode, boolean isManual) {
        this.mAccessMode = 0;
        this.mIsManual = false;
        this.mAccessMode = accessMode;
        this.mIsManual = isManual;
    }

    public NetworkSelectionMode(Parcel in) {
        this.mAccessMode = 0;
        this.mIsManual = false;
        this.mAccessMode = in.readInt();
        this.mIsManual = in.readBoolean();
    }

    public int getAccessMode() {
        return this.mAccessMode;
    }

    public boolean getIsManual() {
        return this.mIsManual;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mAccessMode);
        dest.writeBoolean(this.mIsManual);
    }

    public void readFromParcel(Parcel in) {
        this.mAccessMode = in.readInt();
        this.mIsManual = in.readBoolean();
    }

    public String toString() {
        return TAG + this.mAccessMode + "/" + this.mIsManual;
    }
}
