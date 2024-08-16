package com.oplus.network.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.BitUtils;
import java.util.Arrays;

/* loaded from: classes.dex */
public class AppFreezeHistory implements Parcelable {
    public static final Parcelable.Creator<AppFreezeHistory> CREATOR = new Parcelable.Creator<AppFreezeHistory>() { // from class: com.oplus.network.stats.AppFreezeHistory.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeHistory createFromParcel(Parcel in) {
            long addBlockTime = in.readLong();
            long removeBlockTime = in.readLong();
            long sendResetTime = in.readLong();
            long enterFgTime = in.readLong();
            long enterBgTime = in.readLong();
            long networkType = in.readLong();
            return new AppFreezeHistory(addBlockTime, removeBlockTime, sendResetTime, enterFgTime, enterBgTime, networkType);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeHistory[] newArray(int size) {
            return new AppFreezeHistory[size];
        }
    };
    public long mAddBlockTime;
    public long mEnterBgTime;
    public long mEnterFgTime;
    public long mNetworkType;
    public long mRemoveBlockTime;
    public long mSendResetTime;

    public AppFreezeHistory() {
        this.mAddBlockTime = 0L;
        this.mRemoveBlockTime = 0L;
        this.mSendResetTime = 0L;
        this.mEnterFgTime = 0L;
        this.mEnterBgTime = 0L;
        this.mNetworkType = 0L;
    }

    public AppFreezeHistory(long mAddBlockTime, long mRemoveBlockTime, long mSendResetTime, long mEnterFgTime, long mEnterBgTime, long mNetworkType) {
        this.mAddBlockTime = mAddBlockTime;
        this.mRemoveBlockTime = mRemoveBlockTime;
        this.mSendResetTime = mSendResetTime;
        this.mEnterFgTime = mEnterFgTime;
        this.mEnterBgTime = mEnterBgTime;
        this.mNetworkType = mNetworkType;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mAddBlockTime);
        parcel.writeLong(this.mRemoveBlockTime);
        parcel.writeLong(this.mSendResetTime);
        parcel.writeLong(this.mEnterFgTime);
        parcel.writeLong(this.mEnterBgTime);
        parcel.writeLong(this.mNetworkType);
    }

    public String toString() {
        return "AppFreezeHistory{mAddBlockTime=" + this.mAddBlockTime + ", mRemoveBlockTime=" + this.mRemoveBlockTime + ", mSendResetTime=" + this.mSendResetTime + ", mEnterFgTime=" + this.mEnterFgTime + ", mEnterBgTime=" + this.mEnterBgTime + ", mNetworkType=" + Arrays.toString(BitUtils.unpackBits(this.mNetworkType)) + '}';
    }
}
