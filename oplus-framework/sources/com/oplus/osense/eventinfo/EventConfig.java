package com.oplus.osense.eventinfo;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class EventConfig implements Parcelable {
    public static final Parcelable.Creator<EventConfig> CREATOR = new Parcelable.Creator<EventConfig>() { // from class: com.oplus.osense.eventinfo.EventConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EventConfig createFromParcel(Parcel in) {
            return new EventConfig(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EventConfig[] newArray(int size) {
            return new EventConfig[size];
        }
    };
    private static final String EMPTYCONFIG = "EventConfig is empty";
    private Set<Integer> mEventSet;

    public EventConfig(Parcel in) {
        ClassLoader loader = EventConfig.class.getClassLoader();
        List<Integer> list = new ArrayList<>();
        in.readList(list, loader);
        this.mEventSet = new HashSet();
        if (!list.isEmpty()) {
            this.mEventSet.addAll(list);
        }
    }

    public EventConfig(HashSet<Integer> events) {
        this.mEventSet = new HashSet();
        if (events != null && !events.isEmpty()) {
            this.mEventSet.addAll(events);
        }
    }

    public Set<Integer> getEventSet() {
        if (this.mEventSet == null) {
            this.mEventSet = new HashSet();
        }
        return this.mEventSet;
    }

    public void addEvent(int eventType) {
        if (this.mEventSet == null) {
            this.mEventSet = new HashSet();
        }
        this.mEventSet.add(Integer.valueOf(eventType));
    }

    public String toString() {
        Set<Integer> set = this.mEventSet;
        if (set == null || set.isEmpty()) {
            return EMPTYCONFIG;
        }
        return "EventConfig{mEventSet=" + Arrays.toString(this.mEventSet.toArray()) + '}';
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int i) {
        if (this.mEventSet != null) {
            dest.writeList(new ArrayList(this.mEventSet));
        }
    }
}
