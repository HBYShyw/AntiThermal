package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusSetNetworkSelectionMode implements Parcelable {
    public static final int ACCESS_MODE_INVALID = 0;
    public static final int ACCESS_NETWORK_UNKNOWN = 0;
    public static final long CAG_ID_INVALID = -1;
    public static final Parcelable.Creator<OplusSetNetworkSelectionMode> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.OplusSetNetworkSelectionMode.1
        @Override // android.os.Parcelable.Creator
        public OplusSetNetworkSelectionMode createFromParcel(Parcel in) {
            return new OplusSetNetworkSelectionMode(in);
        }

        @Override // android.os.Parcelable.Creator
        public OplusSetNetworkSelectionMode[] newArray(int size) {
            return new OplusSetNetworkSelectionMode[size];
        }
    };
    private static final String TAG = "OplusSetNetworkSelectionMode";
    private int mAccessMode;
    private long mCagId;
    private byte[] mNid;
    private String mOperatorNumeric;
    private int mRan;

    public OplusSetNetworkSelectionMode(String operatorNumeric, int ran, int accessMode, long cagId, byte[] nid) {
        this.mRan = 0;
        this.mAccessMode = 0;
        this.mCagId = -1L;
        this.mOperatorNumeric = operatorNumeric;
        this.mRan = ran;
        this.mAccessMode = accessMode;
        this.mCagId = cagId;
        this.mNid = nid;
    }

    public OplusSetNetworkSelectionMode(Parcel in) {
        this.mRan = 0;
        this.mAccessMode = 0;
        this.mCagId = -1L;
        this.mOperatorNumeric = in.readString();
        this.mRan = in.readInt();
        this.mAccessMode = in.readInt();
        this.mCagId = in.readLong();
        int arrayLength = in.readInt();
        if (arrayLength > 0) {
            byte[] bArr = new byte[arrayLength];
            this.mNid = bArr;
            in.readByteArray(bArr);
            return;
        }
        this.mNid = null;
    }

    public String getOperatorNumeric() {
        return this.mOperatorNumeric;
    }

    public int getRan() {
        return this.mRan;
    }

    public int getAccessMode() {
        return this.mAccessMode;
    }

    public long getCagId() {
        return this.mCagId;
    }

    public byte[] getNid() {
        return this.mNid;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mOperatorNumeric);
        dest.writeInt(this.mRan);
        dest.writeInt(this.mAccessMode);
        dest.writeLong(this.mCagId);
        byte[] bArr = this.mNid;
        if (bArr != null && bArr.length > 0) {
            dest.writeInt(bArr.length);
            dest.writeByteArray(this.mNid);
        } else {
            dest.writeInt(0);
        }
    }

    public void readFromParcel(Parcel in) {
        this.mOperatorNumeric = in.readString();
        this.mRan = in.readInt();
        this.mAccessMode = in.readInt();
        this.mCagId = in.readLong();
        int arrayLength = in.readInt();
        if (arrayLength > 0) {
            byte[] bArr = new byte[arrayLength];
            this.mNid = bArr;
            in.readByteArray(bArr);
            return;
        }
        this.mNid = null;
    }

    public String toString() {
        return TAG + this.mOperatorNumeric + "/" + this.mRan + "/" + this.mAccessMode + "/" + this.mCagId + "/" + this.mNid;
    }
}
