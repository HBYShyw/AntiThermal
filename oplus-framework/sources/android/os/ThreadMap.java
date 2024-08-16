package android.os;

import android.os.Parcelable;
import java.util.Collections;
import java.util.Map;

/* loaded from: classes.dex */
public final class ThreadMap implements Parcelable {
    public static final Parcelable.Creator<ThreadMap> CREATOR = new Parcelable.Creator<ThreadMap>() { // from class: android.os.ThreadMap.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ThreadMap createFromParcel(Parcel source) {
            return new ThreadMap(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ThreadMap[] newArray(int size) {
            return new ThreadMap[size];
        }
    };
    private Map mThreadMap;

    private ThreadMap(Parcel source) {
        this.mThreadMap = Collections.emptyMap();
        Map map = source.readHashMap(null);
        if (map == null) {
            return;
        }
        this.mThreadMap = map;
    }

    public void set(Map map) {
        this.mThreadMap = map;
    }

    public Map get() {
        return this.mThreadMap;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(this.mThreadMap);
    }
}
