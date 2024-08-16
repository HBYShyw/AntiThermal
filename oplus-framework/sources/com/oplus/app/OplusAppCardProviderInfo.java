package com.oplus.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusAppCardProviderInfo implements Parcelable {
    public static final Parcelable.Creator<OplusAppCardProviderInfo> CREATOR = new Parcelable.Creator<OplusAppCardProviderInfo>() { // from class: com.oplus.app.OplusAppCardProviderInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusAppCardProviderInfo createFromParcel(Parcel in) {
            return new OplusAppCardProviderInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusAppCardProviderInfo[] newArray(int size) {
            return new OplusAppCardProviderInfo[size];
        }
    };
    public static final int REQUEST_ID_ADD = 1;
    public static final int REQUEST_ID_UPDATE = 2;
    public static final int UPDATE_TYPE_ACTIVE = 1;
    public static final int UPDATE_TYPE_PASSIVE = 2;
    private Bundle mExtension;
    private String mPackageName;
    private int mUid;
    private int mUpdateType;

    public OplusAppCardProviderInfo(int uid, String packageName, int updateType) {
        this.mUid = uid;
        this.mPackageName = packageName;
        this.mUpdateType = updateType;
        this.mExtension = new Bundle();
    }

    public OplusAppCardProviderInfo(OplusAppCardProviderInfo other) {
        this.mUid = other.getUid();
        this.mPackageName = other.getPackageName();
        this.mUpdateType = other.getUpdateType();
        this.mExtension = new Bundle(other.getExtension());
    }

    public OplusAppCardProviderInfo(Parcel in) {
        this.mUid = in.readInt();
        this.mPackageName = in.readString();
        this.mUpdateType = in.readInt();
        this.mExtension = in.readBundle();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mUid);
        dest.writeString(this.mPackageName);
        dest.writeInt(this.mUpdateType);
        dest.writeBundle(this.mExtension);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusAppCardProviderInfo{");
        sb.append("uid=").append(this.mUid);
        sb.append(", packageName=").append(this.mPackageName);
        sb.append(", updateType=").append(this.mUpdateType);
        sb.append(", extension=").append(this.mExtension);
        sb.append("}");
        return sb.toString();
    }

    public int getUid() {
        return this.mUid;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public int getUpdateType() {
        return this.mUpdateType;
    }

    public void setUpdateType(int updateType) {
        this.mUpdateType = updateType;
    }

    public Bundle getExtension() {
        return this.mExtension;
    }

    public void setExtension(Bundle extension) {
        this.mExtension = extension;
    }
}
