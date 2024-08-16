package com.oplus.nas.cybersense.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class EventConfig implements Parcelable {
    public static final Parcelable.Creator<EventConfig> CREATOR = new Parcelable.Creator<EventConfig>() { // from class: com.oplus.nas.cybersense.sdk.EventConfig.1
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
    private Set<Integer> mEventSet = new HashSet();

    public EventConfig() {
    }

    public EventConfig(HashSet<Integer> events) {
        if (events != null && !events.isEmpty()) {
            this.mEventSet.addAll(events);
        }
    }

    public EventConfig(Parcel in) {
        ClassLoader loader = EventConfig.class.getClassLoader();
        List<Integer> list = new ArrayList<>();
        in.readList(list, loader);
        if (!list.isEmpty()) {
            this.mEventSet.addAll(list);
        }
    }

    public Set<Integer> getAllEvents() {
        HashSet<Integer> set = new HashSet<>();
        Set<Integer> set2 = this.mEventSet;
        if (set2 != null && !set2.isEmpty()) {
            for (Integer event : this.mEventSet) {
                set.add(event);
            }
        }
        return set;
    }

    public void addEvent(Integer event) {
        if (this.mEventSet == null) {
            this.mEventSet = new HashSet();
        }
        this.mEventSet.add(event);
    }

    public void addEvents(Set<Integer> events) {
        if (this.mEventSet == null) {
            this.mEventSet = new HashSet();
        }
        if (events == null) {
            return;
        }
        this.mEventSet.addAll(events);
    }

    public void removeEvent(Integer event) {
        Set<Integer> set = this.mEventSet;
        if (set == null) {
            return;
        }
        set.remove(event);
    }

    public void removeAllEvent() {
        Set<Integer> set = this.mEventSet;
        if (set == null) {
            return;
        }
        set.clear();
    }

    public String toString() {
        Set<Integer> set = this.mEventSet;
        if (set == null || set.isEmpty()) {
            return null;
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
