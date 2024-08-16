package com.oplus.network.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class IfaceUidStatsTotal implements Parcelable {
    public static final Parcelable.Creator<IfaceUidStatsTotal> CREATOR = new Parcelable.Creator<IfaceUidStatsTotal>() { // from class: com.oplus.network.stats.IfaceUidStatsTotal.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IfaceUidStatsTotal createFromParcel(Parcel in) {
            String[] arr;
            Bundle bundle = in.readBundle();
            HashMap<Pair<String, Integer>, StatsValue> map = new HashMap<>();
            for (String key : bundle.keySet()) {
                try {
                    arr = key.split("\\|");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(IfaceUidStatsTotal.TAG, "parse failed!" + e.getMessage());
                }
                if (arr != null && arr.length == 2) {
                    int uid = Integer.parseInt(arr[1]);
                    map.put(new Pair<>(arr[0], Integer.valueOf(uid)), (StatsValue) bundle.getParcelable(key));
                }
                Log.w(IfaceUidStatsTotal.TAG, "arr invalid!" + arr);
            }
            return new IfaceUidStatsTotal(map);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IfaceUidStatsTotal[] newArray(int size) {
            return new IfaceUidStatsTotal[size];
        }
    };
    private static final String TAG = "IfaceUidStatsTotal";
    public HashMap<Pair<String, Integer>, StatsValue> mStatsMap;

    public IfaceUidStatsTotal(HashMap<Pair<String, Integer>, StatsValue> mStatsMap) {
        this.mStatsMap = mStatsMap;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        for (Map.Entry<Pair<String, Integer>, StatsValue> entry : this.mStatsMap.entrySet()) {
            String key = ((String) entry.getKey().first) + "|" + entry.getKey().second;
            bundle.putParcelable(key, entry.getValue());
        }
        dest.writeBundle(bundle);
    }

    public String toString() {
        return "IfaceUidStatsTotal{mStatsMap=" + this.mStatsMap + '}';
    }
}
