package com.oplus.network.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class AppFreezeSyncInfo implements Parcelable {
    public static final Parcelable.Creator<AppFreezeSyncInfo> CREATOR = new Parcelable.Creator<AppFreezeSyncInfo>() { // from class: com.oplus.network.stats.AppFreezeSyncInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeSyncInfo createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();
            AppFreezeSync stats = (AppFreezeSync) bundle.getParcelable("mSyn");
            AppFreezeHistory history = (AppFreezeHistory) bundle.getParcelable("mHistory");
            String name = source.readString();
            int count = source.readInt();
            return new AppFreezeSyncInfo(stats, history, name, count);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeSyncInfo[] newArray(int size) {
            return new AppFreezeSyncInfo[size];
        }
    };
    private static final String TAG = "AppFreezeSyncInfo";
    public String mAppName;
    public int mCount;
    public AppFreezeHistory mHistory;
    public AppFreezeSync mSyn;

    public AppFreezeSyncInfo(AppFreezeSync mSyn, AppFreezeHistory mHistory, String mAppName, int mCount) {
        this.mSyn = mSyn;
        this.mHistory = mHistory;
        this.mAppName = mAppName;
        this.mCount = mCount;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("mSyn", this.mSyn);
        bundle.putParcelable("mHistory", this.mHistory);
        dest.writeBundle(bundle);
        dest.writeString(this.mAppName);
        dest.writeInt(this.mCount);
    }

    public String toString() {
        return "AppFreezeSyncInfo{mSyn=" + this.mSyn + ", mHistory=" + this.mHistory + ", mAppName='" + OplusNetworkUtils.getHashPackageName(this.mAppName) + "', mCount=" + this.mCount + '}';
    }
}
