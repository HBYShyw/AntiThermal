package com.oplus.network.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class AppFreezeStatsInfo implements Parcelable {
    public static final Parcelable.Creator<AppFreezeStatsInfo> CREATOR = new Parcelable.Creator<AppFreezeStatsInfo>() { // from class: com.oplus.network.stats.AppFreezeStatsInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeStatsInfo createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();
            AppFreezeStats stats = (AppFreezeStats) bundle.getParcelable("mStats");
            AppFreezeHistory history = (AppFreezeHistory) bundle.getParcelable("mHistory");
            String name = source.readString();
            int count = source.readInt();
            return new AppFreezeStatsInfo(stats, history, name, count);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeStatsInfo[] newArray(int size) {
            return new AppFreezeStatsInfo[size];
        }
    };
    private static final String TAG = "AppFreezeStatsInfo";
    public String mAppName;
    public int mCount;
    public AppFreezeHistory mHistory;
    public AppFreezeStats mStats;

    public AppFreezeStatsInfo(AppFreezeStats mStats, AppFreezeHistory mHistory, String mAppName, int mCount) {
        this.mStats = mStats;
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
        bundle.putParcelable("mStats", this.mStats);
        bundle.putParcelable("mHistory", this.mHistory);
        dest.writeBundle(bundle);
        dest.writeString(this.mAppName);
        dest.writeInt(this.mCount);
    }

    public String toString() {
        return "AppFreezeStatsInfo{mStats=" + this.mStats + ", mHistory=" + this.mHistory + ", mAppName='" + OplusNetworkUtils.getHashPackageName(this.mAppName) + "', mCount=" + this.mCount + '}';
    }
}
