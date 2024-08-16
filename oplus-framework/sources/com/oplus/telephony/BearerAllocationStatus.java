package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class BearerAllocationStatus implements Parcelable {
    public static final int ALLOCATED = 1;
    public static final Parcelable.Creator<BearerAllocationStatus> CREATOR = new Parcelable.Creator() { // from class: com.oplus.telephony.BearerAllocationStatus.1
        @Override // android.os.Parcelable.Creator
        public BearerAllocationStatus createFromParcel(Parcel in) {
            return new BearerAllocationStatus(in);
        }

        @Override // android.os.Parcelable.Creator
        public BearerAllocationStatus[] newArray(int size) {
            return new BearerAllocationStatus[size];
        }
    };
    public static final int INVALID = -1;
    public static final int MMW_ALLOCATED = 2;
    public static final int NOT_ALLOCATED = 0;
    private static final String TAG = "BearerAllocationStatus";
    private int mValue;

    public BearerAllocationStatus(int val) {
        this.mValue = val;
    }

    public BearerAllocationStatus(Parcel in) {
        this.mValue = in.readInt();
    }

    public int getBearerAllocationStatus() {
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
        return "BearerAllocationStatus: " + getBearerAllocationStatus();
    }
}
