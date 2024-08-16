package com.oplus.network.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class StatsValueTotal implements Parcelable {
    public static final Parcelable.Creator<StatsValueTotal> CREATOR = new Parcelable.Creator<StatsValueTotal>() { // from class: com.oplus.network.stats.StatsValueTotal.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StatsValueTotal createFromParcel(Parcel in) {
            Bundle bundle = in.readBundle();
            HashMap<Long, StatsValue> map = new HashMap<>();
            for (String key : bundle.keySet()) {
                map.put(Long.valueOf(Long.parseLong(key)), (StatsValue) bundle.getParcelable(key));
            }
            return new StatsValueTotal(map);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StatsValueTotal[] newArray(int size) {
            return new StatsValueTotal[size];
        }
    };
    public HashMap<Long, StatsValue> mStatsMap;

    public StatsValueTotal(HashMap<Long, StatsValue> mStatsMap) {
        this.mStatsMap = mStatsMap;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        for (Map.Entry<Long, StatsValue> entry : this.mStatsMap.entrySet()) {
            bundle.putParcelable(String.valueOf(entry.getKey()), entry.getValue());
        }
        parcel.writeBundle(bundle);
    }

    public String toString() {
        return "StatsValueTotal{mStatsMap=" + this.mStatsMap + '}';
    }
}
