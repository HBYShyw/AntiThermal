package com.oplus.network.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class IfaceSpeedsValueTotal implements Parcelable {
    public static final Parcelable.Creator<IfaceSpeedsValueTotal> CREATOR = new Parcelable.Creator<IfaceSpeedsValueTotal>() { // from class: com.oplus.network.stats.IfaceSpeedsValueTotal.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IfaceSpeedsValueTotal createFromParcel(Parcel in) {
            Bundle bundle = in.readBundle();
            HashMap<Pair<Integer, Integer>, IfaceSpeedsValue> map = new HashMap<>();
            for (String key : bundle.keySet()) {
                try {
                    long k = Long.parseLong(key);
                    int ifindex = (int) ((-1) & k);
                    int uid = (int) (k >> 32);
                    map.put(new Pair<>(Integer.valueOf(uid), Integer.valueOf(ifindex)), (IfaceSpeedsValue) bundle.getParcelable(key));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new IfaceSpeedsValueTotal(map);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IfaceSpeedsValueTotal[] newArray(int size) {
            return new IfaceSpeedsValueTotal[size];
        }
    };
    public HashMap<Pair<Integer, Integer>, IfaceSpeedsValue> mSpeedsMap;

    public IfaceSpeedsValueTotal(HashMap<Pair<Integer, Integer>, IfaceSpeedsValue> mSpeedsMap) {
        this.mSpeedsMap = mSpeedsMap;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        for (Map.Entry<Pair<Integer, Integer>, IfaceSpeedsValue> entry : this.mSpeedsMap.entrySet()) {
            long value = (((Integer) entry.getKey().first).intValue() << 32) | ((Integer) entry.getKey().second).intValue();
            bundle.putParcelable(Long.toString(value), entry.getValue());
        }
        parcel.writeBundle(bundle);
    }

    public String toString() {
        return "IfaceSpeedsValueTotal{mSpeedsMap=" + this.mSpeedsMap + '}';
    }
}
