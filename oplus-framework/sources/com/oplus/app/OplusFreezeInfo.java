package com.oplus.app;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusFreezeInfo implements Parcelable {
    public static final Parcelable.Creator<OplusFreezeInfo> CREATOR = new Parcelable.Creator<OplusFreezeInfo>() { // from class: com.oplus.app.OplusFreezeInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusFreezeInfo createFromParcel(Parcel in) {
            return new OplusFreezeInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusFreezeInfo[] newArray(int size) {
            return new OplusFreezeInfo[size];
        }
    };
    public static final int FREEZE_EVENT_ID = 1;
    public static final int IMPORTANT_EVENT_ID = 3;
    public static final int UNFREEZE_EVENT_ID = 2;
    public int mEventID;
    public String mPackageName;
    public String mReason;
    public int mTargetUid;
    public String mThawInfo;
    public long mTime;

    public OplusFreezeInfo(int eventID, long time, String packageName, int targetUid, String reason, String thawInfo) {
        this.mEventID = eventID;
        this.mTime = time;
        this.mPackageName = packageName;
        this.mTargetUid = targetUid;
        this.mReason = reason;
        this.mThawInfo = thawInfo;
    }

    public OplusFreezeInfo(Parcel in) {
        this.mEventID = in.readInt();
        this.mTime = in.readLong();
        this.mPackageName = in.readString();
        this.mTargetUid = in.readInt();
        this.mReason = in.readString();
        this.mThawInfo = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(this.mEventID);
        out.writeLong(this.mTime);
        out.writeString(this.mPackageName);
        out.writeInt(this.mTargetUid);
        out.writeString(this.mReason);
        out.writeString(this.mThawInfo);
    }

    public String toString() {
        return "OplusFreezeInfo{mEventID=" + this.mEventID + "mTime=" + this.mTime + ", mPackageName=" + this.mPackageName + ", mTargetUid=" + this.mTargetUid + ", mReason=" + this.mReason + ", mThawInfo=" + this.mThawInfo + '}';
    }
}
