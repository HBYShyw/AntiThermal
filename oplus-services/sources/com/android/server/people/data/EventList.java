package com.android.server.people.data;

import com.android.internal.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class EventList {
    private final List<Event> mEvents = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(Event event) {
        int firstIndexOnOrAfter = firstIndexOnOrAfter(event.getTimestamp());
        if (firstIndexOnOrAfter < this.mEvents.size() && this.mEvents.get(firstIndexOnOrAfter).getTimestamp() == event.getTimestamp() && isDuplicate(event, firstIndexOnOrAfter)) {
            return;
        }
        this.mEvents.add(firstIndexOnOrAfter, event);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addAll(List<Event> list) {
        Iterator<Event> it = list.iterator();
        while (it.hasNext()) {
            add(it.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Event> queryEvents(Set<Integer> set, long j, long j2) {
        int firstIndexOnOrAfter = firstIndexOnOrAfter(j);
        if (firstIndexOnOrAfter == this.mEvents.size()) {
            return new ArrayList();
        }
        int firstIndexOnOrAfter2 = firstIndexOnOrAfter(j2);
        if (firstIndexOnOrAfter2 < firstIndexOnOrAfter) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        while (firstIndexOnOrAfter < firstIndexOnOrAfter2) {
            Event event = this.mEvents.get(firstIndexOnOrAfter);
            if (set.contains(Integer.valueOf(event.getType()))) {
                arrayList.add(event);
            }
            firstIndexOnOrAfter++;
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        this.mEvents.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Event> getAllEvents() {
        return CollectionUtils.copyOf(this.mEvents);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeOldEvents(long j) {
        int firstIndexOnOrAfter = firstIndexOnOrAfter(j);
        if (firstIndexOnOrAfter == 0) {
            return;
        }
        int size = this.mEvents.size();
        if (firstIndexOnOrAfter == size) {
            this.mEvents.clear();
            return;
        }
        int i = 0;
        while (firstIndexOnOrAfter < size) {
            List<Event> list = this.mEvents;
            list.set(i, list.get(firstIndexOnOrAfter));
            i++;
            firstIndexOnOrAfter++;
        }
        if (size > i) {
            this.mEvents.subList(i, size).clear();
        }
    }

    private int firstIndexOnOrAfter(long j) {
        int size = this.mEvents.size();
        int size2 = this.mEvents.size() - 1;
        int i = 0;
        while (i <= size2) {
            int i2 = (i + size2) >>> 1;
            if (this.mEvents.get(i2).getTimestamp() >= j) {
                size2 = i2 - 1;
                size = i2;
            } else {
                i = i2 + 1;
            }
        }
        return size;
    }

    private boolean isDuplicate(Event event, int i) {
        int size = this.mEvents.size();
        while (i < size && this.mEvents.get(i).getTimestamp() <= event.getTimestamp()) {
            int i2 = i + 1;
            if (this.mEvents.get(i).getType() == event.getType()) {
                return true;
            }
            i = i2;
        }
        return false;
    }
}
