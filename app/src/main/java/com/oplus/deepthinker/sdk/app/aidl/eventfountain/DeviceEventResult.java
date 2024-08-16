package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.deepthinker.sdk.app.SDKLog;

/* loaded from: classes.dex */
public class DeviceEventResult implements Parcelable {
    public static final Parcelable.Creator<DeviceEventResult> CREATOR = new Parcelable.Creator<DeviceEventResult>() { // from class: com.oplus.deepthinker.sdk.app.aidl.eventfountain.DeviceEventResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceEventResult createFromParcel(Parcel parcel) {
            return new DeviceEventResult(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceEventResult[] newArray(int i10) {
            return new DeviceEventResult[i10];
        }
    };
    private static final String TAG = "DeviceEventResult";
    private int mEventStateType;
    private int mEventType;
    private Bundle mExtraData;
    private int mPid;
    private String mPkgName;

    public DeviceEventResult(int i10, int i11, int i12, String str, Bundle bundle) {
        this.mEventType = i10;
        this.mEventStateType = i11;
        this.mPid = i12;
        this.mPkgName = str;
        this.mExtraData = bundle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getEventStateType() {
        return this.mEventStateType;
    }

    public int getEventType() {
        return this.mEventType;
    }

    public Bundle getExtraData() {
        if (this.mExtraData == null) {
            SDKLog.d(TAG, "getExtraData: This event is not supported.");
        }
        return this.mExtraData;
    }

    public int getPid() {
        if (this.mPid == -1) {
            SDKLog.d(TAG, "getPid: This event is not supported.");
        }
        return this.mPid;
    }

    public String getPkgName() {
        if (this.mPkgName == null) {
            SDKLog.d(TAG, "getPkgName: This event is not supported.");
        }
        return this.mPkgName;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder("DeviceEventResult :");
        sb2.append("\teventType is :");
        sb2.append(this.mEventType);
        sb2.append("\teventStateType is :");
        sb2.append(this.mEventStateType);
        sb2.append("\tpid is : ");
        sb2.append(this.mPid);
        sb2.append("\t\tpackageName is : ");
        sb2.append(this.mPkgName);
        if (this.mExtraData != null) {
            sb2.append("\tExtraData is : ");
            sb2.append(this.mExtraData.toString());
        }
        return sb2.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeInt(this.mEventType);
        parcel.writeInt(this.mEventStateType);
        parcel.writeInt(this.mPid);
        parcel.writeString(this.mPkgName);
        parcel.writeBundle(this.mExtraData);
    }

    public DeviceEventResult(Parcel parcel) {
        this.mEventType = parcel.readInt();
        this.mEventStateType = parcel.readInt();
        this.mPid = parcel.readInt();
        this.mPkgName = parcel.readString();
        this.mExtraData = parcel.readBundle(getClass().getClassLoader());
    }
}
