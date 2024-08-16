package com.oplus.app;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class OplusAccessControlInfo implements Parcelable {
    public static final Parcelable.Creator<OplusAccessControlInfo> CREATOR = new Parcelable.Creator<OplusAccessControlInfo>() { // from class: com.oplus.app.OplusAccessControlInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusAccessControlInfo createFromParcel(Parcel source) {
            return new OplusAccessControlInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusAccessControlInfo[] newArray(int size) {
            return new OplusAccessControlInfo[size];
        }
    };
    public boolean isEncrypted;
    public boolean isHideIcon;
    public boolean isHideInRecent;
    public boolean isHideNotice;
    public String mName;
    public int userId;

    public OplusAccessControlInfo() {
    }

    public OplusAccessControlInfo(Parcel in) {
        this.mName = in.readString();
        this.userId = in.readInt();
        this.isEncrypted = in.readByte() != 0;
        this.isHideIcon = in.readByte() != 0;
        this.isHideInRecent = in.readByte() != 0;
        this.isHideNotice = in.readByte() != 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mName);
        parcel.writeInt(this.userId);
        parcel.writeByte(this.isEncrypted ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isHideIcon ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isHideInRecent ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isHideNotice ? (byte) 1 : (byte) 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusAccessControlInfo = { ");
        sb.append(" mName = " + this.mName);
        sb.append(" userId = " + this.userId);
        sb.append(" isEncrypted = " + this.isEncrypted);
        sb.append(" isHideIcon = " + this.isHideIcon);
        sb.append(" isHideInRecent = " + this.isHideInRecent);
        sb.append(" isHideNotice = " + this.isHideNotice);
        sb.append("}");
        return sb.toString();
    }
}
