package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public class EventConfig implements Parcelable {
    public static final Parcelable.Creator<EventConfig> CREATOR = new Parcelable.Creator<EventConfig>() { // from class: com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EventConfig createFromParcel(Parcel parcel) {
            return new EventConfig(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EventConfig[] newArray(int i10) {
            return new EventConfig[i10];
        }
    };
    private Set<Event> mEventSet;

    public EventConfig(HashSet<Event> hashSet) {
        this.mEventSet = new HashSet();
        if (hashSet == null || hashSet.isEmpty()) {
            return;
        }
        this.mEventSet.addAll(hashSet);
    }

    public void addEvent(Event event) {
        if (this.mEventSet == null) {
            this.mEventSet = new HashSet();
        }
        this.mEventSet.add(event);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Set<Integer> getAllEvents() {
        HashSet hashSet = new HashSet();
        Set<Event> set = this.mEventSet;
        if (set != null && !set.isEmpty()) {
            Iterator<Event> it = this.mEventSet.iterator();
            while (it.hasNext()) {
                hashSet.add(Integer.valueOf(it.next().getEventType()));
            }
        }
        return hashSet;
    }

    public Set<Event> getEventSet() {
        if (this.mEventSet == null) {
            this.mEventSet = new HashSet();
        }
        return this.mEventSet;
    }

    public String toString() {
        Set<Event> set = this.mEventSet;
        if (set == null || set.isEmpty()) {
            return "EventConfig empty.";
        }
        return "EventConfig{mEventSet=" + Arrays.toString(this.mEventSet.toArray()) + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        if (this.mEventSet != null) {
            parcel.writeList(new ArrayList(this.mEventSet));
        }
    }

    public EventConfig(Parcel parcel) {
        ClassLoader classLoader = EventConfig.class.getClassLoader();
        ArrayList arrayList = new ArrayList();
        parcel.readList(arrayList, classLoader);
        this.mEventSet = new HashSet();
        if (arrayList.isEmpty()) {
            return;
        }
        this.mEventSet.addAll(arrayList);
    }
}
