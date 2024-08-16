package com.oplus.network.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class AppFreezeStatsTotal implements Parcelable {
    public static final Parcelable.Creator<AppFreezeStatsTotal> CREATOR = new Parcelable.Creator<AppFreezeStatsTotal>() { // from class: com.oplus.network.stats.AppFreezeStatsTotal.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeStatsTotal createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();
            HashMap<Long, AppFreezeStats> map = new HashMap<>();
            for (String key : bundle.keySet()) {
                map.put(Long.valueOf(Long.parseLong(key)), (AppFreezeStats) bundle.getParcelable(key));
            }
            return new AppFreezeStatsTotal(map);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeStatsTotal[] newArray(int size) {
            return new AppFreezeStatsTotal[size];
        }
    };
    public HashMap<Long, AppFreezeStats> mAppFreezeStatsMap;

    public AppFreezeStatsTotal(HashMap<Long, AppFreezeStats> mAppFreezeStatsMap) {
        this.mAppFreezeStatsMap = mAppFreezeStatsMap;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        for (Map.Entry<Long, AppFreezeStats> entry : this.mAppFreezeStatsMap.entrySet()) {
            bundle.putParcelable(String.valueOf(entry.getKey()), entry.getValue());
        }
        dest.writeBundle(bundle);
    }

    public String toString() {
        return "AppFreezeStatsTotal{mAppFreezeStatsMap=" + this.mAppFreezeStatsMap + '}';
    }
}
