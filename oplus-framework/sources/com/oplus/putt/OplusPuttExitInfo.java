package com.oplus.putt;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class OplusPuttExitInfo implements Parcelable {
    public static final Parcelable.Creator<OplusPuttExitInfo> CREATOR = new Parcelable.Creator<OplusPuttExitInfo>() { // from class: com.oplus.putt.OplusPuttExitInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPuttExitInfo createFromParcel(Parcel source) {
            return new OplusPuttExitInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPuttExitInfo[] newArray(int size) {
            return new OplusPuttExitInfo[size];
        }
    };
    public int action;
    public String cpnName;
    public Bundle extension;
    public int puttDisplayId;
    public String puttHash;
    public String puttPkg;
    public boolean returnToForeground;
    public int taskId;
    public int type;
    public int userId;

    public OplusPuttExitInfo() {
        this.extension = new Bundle();
    }

    public OplusPuttExitInfo(Parcel in) {
        this.extension = new Bundle();
        this.type = in.readInt();
        this.puttHash = in.readString();
        this.action = in.readInt();
        this.taskId = in.readInt();
        this.userId = in.readInt();
        this.puttDisplayId = in.readInt();
        this.puttPkg = in.readString();
        this.cpnName = in.readString();
        this.returnToForeground = in.readBoolean();
        this.extension = in.readBundle();
    }

    public OplusPuttExitInfo(OplusPuttExitInfo in) {
        this.extension = new Bundle();
        if (in != null) {
            this.type = in.type;
            this.puttHash = in.puttHash;
            this.action = in.action;
            this.taskId = in.taskId;
            this.userId = in.userId;
            this.puttDisplayId = in.puttDisplayId;
            this.puttPkg = in.puttPkg;
            this.cpnName = in.cpnName;
            this.returnToForeground = in.returnToForeground;
            this.extension = in.extension;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.puttHash);
        dest.writeInt(this.action);
        dest.writeInt(this.taskId);
        dest.writeInt(this.userId);
        dest.writeInt(this.puttDisplayId);
        dest.writeString(this.puttPkg);
        dest.writeString(this.cpnName);
        dest.writeBoolean(this.returnToForeground);
        dest.writeBundle(this.extension);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" OplusPuttExitInfo{");
        sb.append(" type = " + this.type);
        sb.append(" puttHash = " + this.puttHash);
        sb.append(" action = " + this.action);
        sb.append(" taskId = " + this.taskId);
        sb.append(" userId = " + this.userId);
        sb.append(" puttDisplayId = " + this.puttDisplayId);
        sb.append(" puttPkg = " + this.puttPkg);
        sb.append(" cpnName = " + this.cpnName);
        sb.append(" returnToForeground = " + this.returnToForeground);
        sb.append(" extension = " + this.extension);
        sb.append("}");
        return sb.toString();
    }
}
