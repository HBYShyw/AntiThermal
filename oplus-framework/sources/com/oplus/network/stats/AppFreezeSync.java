package com.oplus.network.stats;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class AppFreezeSync implements Parcelable {
    public static final Parcelable.Creator<AppFreezeSync> CREATOR = new Parcelable.Creator<AppFreezeSync>() { // from class: com.oplus.network.stats.AppFreezeSync.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeSync createFromParcel(Parcel in) {
            int uid = in.readInt();
            int expired = in.readInt();
            int occurFlag = in.readInt();
            int ackFlag = in.readInt();
            return new AppFreezeSync(uid, expired, occurFlag, ackFlag);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeSync[] newArray(int size) {
            return new AppFreezeSync[size];
        }
    };
    public int mExpired;
    public int mOccurFlag;
    public int mSynAckFlag;
    public int mUid;

    public AppFreezeSync(int mUid, int mExpired, int mOccurFlag, int mSynAckFlag) {
        this.mUid = mUid;
        this.mExpired = mExpired;
        this.mOccurFlag = mOccurFlag;
        this.mSynAckFlag = mSynAckFlag;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mUid);
        parcel.writeInt(this.mExpired);
        parcel.writeInt(this.mOccurFlag);
        parcel.writeInt(this.mSynAckFlag);
    }

    public String toString() {
        return "AppFreezeSync{mUid=" + this.mUid + ", mExpired=" + this.mExpired + ", mOccurFlag=" + this.mOccurFlag + ", mSynAckFlag=" + this.mSynAckFlag + '}';
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof AppFreezeSync) && this.mUid == ((AppFreezeSync) obj).mUid && this.mExpired == ((AppFreezeSync) obj).mExpired && this.mOccurFlag == ((AppFreezeSync) obj).mOccurFlag && this.mSynAckFlag == ((AppFreezeSync) obj).mSynAckFlag;
    }
}
