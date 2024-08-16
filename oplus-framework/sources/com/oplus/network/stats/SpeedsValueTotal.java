package com.oplus.network.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class SpeedsValueTotal implements Parcelable {
    public static final Parcelable.Creator<SpeedsValueTotal> CREATOR = new Parcelable.Creator<SpeedsValueTotal>() { // from class: com.oplus.network.stats.SpeedsValueTotal.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SpeedsValueTotal createFromParcel(Parcel in) {
            Bundle bundle = in.readBundle();
            HashMap<Long, SpeedsValue> map = new HashMap<>();
            for (String key : bundle.keySet()) {
                map.put(Long.valueOf(Long.parseLong(key)), (SpeedsValue) bundle.getParcelable(key));
            }
            return new SpeedsValueTotal(map);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SpeedsValueTotal[] newArray(int size) {
            return new SpeedsValueTotal[size];
        }
    };
    public HashMap<Long, SpeedsValue> mSpeedsMap;

    public SpeedsValueTotal(HashMap<Long, SpeedsValue> mSpeedsMap) {
        this.mSpeedsMap = mSpeedsMap;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        for (Map.Entry<Long, SpeedsValue> entry : this.mSpeedsMap.entrySet()) {
            bundle.putParcelable(String.valueOf(entry.getKey()), entry.getValue());
        }
        parcel.writeBundle(bundle);
    }

    public String toString() {
        return "SpeedsValueTotal{mSpeedsMap=" + this.mSpeedsMap + '}';
    }
}
