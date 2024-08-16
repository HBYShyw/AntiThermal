package com.oplus.putt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class OplusPuttEnterInfo implements Parcelable {
    public static final Parcelable.Creator<OplusPuttEnterInfo> CREATOR = new Parcelable.Creator<OplusPuttEnterInfo>() { // from class: com.oplus.putt.OplusPuttEnterInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPuttEnterInfo createFromParcel(Parcel source) {
            return new OplusPuttEnterInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPuttEnterInfo[] newArray(int size) {
            return new OplusPuttEnterInfo[size];
        }
    };
    public String cpnName;
    public Bundle extension;
    public Intent intent;
    public long launchTime;
    public int mEnterAction;
    public int puttDisplayId;
    public String puttHash;
    public String puttPkg;
    public int taskId;
    public int type;
    public int userId;

    public OplusPuttEnterInfo() {
        this.extension = new Bundle();
    }

    public OplusPuttEnterInfo(Parcel in) {
        this.extension = new Bundle();
        this.type = in.readInt();
        this.puttHash = in.readString();
        this.mEnterAction = in.readInt();
        this.taskId = in.readInt();
        if (in.readInt() == 1) {
            this.intent = (Intent) Intent.CREATOR.createFromParcel(in);
        }
        this.userId = in.readInt();
        this.puttDisplayId = in.readInt();
        this.puttPkg = in.readString();
        this.cpnName = in.readString();
        this.launchTime = in.readLong();
        this.extension = in.readBundle();
    }

    public OplusPuttEnterInfo(OplusPuttEnterInfo in) {
        this.extension = new Bundle();
        if (in != null) {
            this.type = in.type;
            this.puttHash = in.puttHash;
            this.mEnterAction = in.mEnterAction;
            this.taskId = in.taskId;
            this.intent = in.intent;
            this.userId = in.userId;
            this.puttDisplayId = in.puttDisplayId;
            this.puttPkg = in.puttPkg;
            this.cpnName = in.cpnName;
            this.launchTime = in.launchTime;
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
        dest.writeInt(this.mEnterAction);
        dest.writeInt(this.taskId);
        if (this.intent == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            this.intent.writeToParcel(dest, flags);
        }
        dest.writeInt(this.userId);
        dest.writeInt(this.puttDisplayId);
        dest.writeString(this.puttPkg);
        dest.writeString(this.cpnName);
        dest.writeLong(this.launchTime);
        dest.writeBundle(this.extension);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OplusPuttEnterInfo = { ");
        sb.append(" type = " + this.type);
        sb.append(" puttHash = " + this.puttHash);
        sb.append(" action = " + this.mEnterAction);
        sb.append(" taskId = " + this.taskId);
        sb.append(" userId = " + this.userId);
        sb.append(" puttDisplayId = " + this.puttDisplayId);
        sb.append(" puttPkg = " + this.puttPkg);
        sb.append(" cpnName = " + this.cpnName);
        sb.append(" extension = " + this.extension);
        sb.append("}");
        return sb.toString();
    }
}
