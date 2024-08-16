package com.oplus.network.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class AppFreezeSyncTotal implements Parcelable {
    public static final Parcelable.Creator<AppFreezeSyncTotal> CREATOR = new Parcelable.Creator<AppFreezeSyncTotal>() { // from class: com.oplus.network.stats.AppFreezeSyncTotal.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeSyncTotal createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();
            HashMap<Long, AppFreezeSync> map = new HashMap<>();
            for (String key : bundle.keySet()) {
                map.put(Long.valueOf(Long.parseLong(key)), (AppFreezeSync) bundle.getParcelable(key));
            }
            return new AppFreezeSyncTotal(map);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppFreezeSyncTotal[] newArray(int size) {
            return new AppFreezeSyncTotal[size];
        }
    };
    public HashMap<Long, AppFreezeSync> mAppFreezeSyncMap;

    public AppFreezeSyncTotal(HashMap<Long, AppFreezeSync> mAppFreezeSyncMap) {
        this.mAppFreezeSyncMap = mAppFreezeSyncMap;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        for (Map.Entry<Long, AppFreezeSync> entry : this.mAppFreezeSyncMap.entrySet()) {
            bundle.putParcelable(String.valueOf(entry.getKey()), entry.getValue());
        }
        dest.writeBundle(bundle);
    }

    public String toString() {
        return "AppFreezeSyncTotal{mAppFreezeSyncMap=" + this.mAppFreezeSyncMap + '}';
    }
}
