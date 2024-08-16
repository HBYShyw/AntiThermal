package com.oplus.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class CallForwardInfoExt implements Parcelable {
    public static final Parcelable.Creator<CallForwardInfoExt> CREATOR = new Parcelable.Creator() { // from class: com.oplus.internal.telephony.CallForwardInfoExt.1
        @Override // android.os.Parcelable.Creator
        public CallForwardInfoExt createFromParcel(Parcel in) {
            return new CallForwardInfoExt(in);
        }

        @Override // android.os.Parcelable.Creator
        public CallForwardInfoExt[] newArray(int size) {
            return new CallForwardInfoExt[size];
        }
    };
    private static final String TAG = "CallForwardInfoExt";
    public String number;
    public int reason;
    public int serviceClass;
    public int status;
    public int timeSeconds;
    public long[] timeSlot;
    public int toa;

    public CallForwardInfoExt() {
    }

    public CallForwardInfoExt(Parcel in) {
        this.status = in.readInt();
        this.reason = in.readInt();
        this.serviceClass = in.readInt();
        this.toa = in.readInt();
        this.number = in.readString();
        this.timeSeconds = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.status);
        out.writeInt(this.reason);
        out.writeInt(this.serviceClass);
        out.writeInt(this.toa);
        out.writeString(this.number);
        out.writeInt(this.timeSeconds);
    }

    public void readFromParcel(Parcel in) {
        this.status = in.readInt();
        this.reason = in.readInt();
        this.serviceClass = in.readInt();
        this.toa = in.readInt();
        this.number = in.readString();
        this.timeSeconds = in.readInt();
    }

    public String toString() {
        return "[CallForwardInfoExt: status=" + (this.status == 0 ? " not active " : " active ") + ", reason= " + this.reason + ", serviceClass= " + this.serviceClass + ", timeSec= " + this.timeSeconds + " seconds]";
    }
}
