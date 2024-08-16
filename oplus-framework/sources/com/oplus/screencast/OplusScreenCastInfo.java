package com.oplus.screencast;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class OplusScreenCastInfo implements Parcelable {
    public static final Parcelable.Creator<OplusScreenCastInfo> CREATOR = new Parcelable.Creator<OplusScreenCastInfo>() { // from class: com.oplus.screencast.OplusScreenCastInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusScreenCastInfo createFromParcel(Parcel source) {
            return new OplusScreenCastInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusScreenCastInfo[] newArray(int size) {
            return new OplusScreenCastInfo[size];
        }
    };
    public boolean appDied;
    public int castFlag;
    public int castMode;
    public String castPkg;

    public OplusScreenCastInfo() {
    }

    public OplusScreenCastInfo(Parcel in) {
        this.castMode = in.readInt();
        this.castFlag = in.readInt();
        this.castPkg = in.readString();
        this.appDied = in.readByte() != 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.castMode);
        parcel.writeInt(this.castFlag);
        parcel.writeString(this.castPkg);
        parcel.writeByte(this.appDied ? (byte) 1 : (byte) 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusScreenCastInfo = { ");
        sb.append(" castMode = " + this.castMode);
        sb.append(" castFlag = " + this.castFlag);
        sb.append(" castPkg = " + this.castPkg);
        sb.append(" appDied = " + this.appDied);
        sb.append("}");
        return sb.toString();
    }
}
