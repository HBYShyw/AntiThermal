package com.oplus.osense.eventinfo;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/* loaded from: classes.dex */
public class OsenseEventResult implements Parcelable {
    public static final Parcelable.Creator<OsenseEventResult> CREATOR = new Parcelable.Creator<OsenseEventResult>() { // from class: com.oplus.osense.eventinfo.OsenseEventResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseEventResult createFromParcel(Parcel in) {
            return new OsenseEventResult(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsenseEventResult[] newArray(int size) {
            return new OsenseEventResult[size];
        }
    };
    private static final String TAG = "OsenseEventResult";
    private int mEventStateType;
    private int mEventType;
    private Bundle mExtraData;

    public OsenseEventResult(Parcel in) {
        this.mEventType = in.readInt();
        this.mEventStateType = in.readInt();
        this.mExtraData = in.readBundle(getClass().getClassLoader());
    }

    public OsenseEventResult(int eventType, int eventStateType, Bundle bundle) {
        this.mEventType = eventType;
        this.mEventStateType = eventStateType;
        this.mExtraData = bundle;
    }

    public int getEventType() {
        return this.mEventType;
    }

    public int getEventStateType() {
        return this.mEventStateType;
    }

    public Bundle getExtraData() {
        if (this.mExtraData == null) {
            Log.w(TAG, "this event extra data is null");
        }
        return this.mExtraData;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("OsenseEventResult :");
        stringBuilder.append("\teventType is :");
        stringBuilder.append(this.mEventType);
        stringBuilder.append("\teventStateType is :");
        stringBuilder.append(this.mEventStateType);
        if (this.mExtraData != null) {
            stringBuilder.append("\tExtraData is : ");
            stringBuilder.append(this.mExtraData.toString());
        }
        return stringBuilder.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.mEventType);
        dest.writeInt(this.mEventStateType);
        dest.writeBundle(this.mExtraData);
    }
}
