package com.android.server.people.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class AggregateEventHistoryImpl implements EventHistory {
    private final List<EventHistory> mEventHistoryList = new ArrayList();

    @Override // com.android.server.people.data.EventHistory
    public EventIndex getEventIndex(int i) {
        Iterator<EventHistory> it = this.mEventHistoryList.iterator();
        while (it.hasNext()) {
            EventIndex eventIndex = it.next().getEventIndex(i);
            if (!eventIndex.isEmpty()) {
                return eventIndex;
            }
        }
        return EventIndex.EMPTY;
    }

    @Override // com.android.server.people.data.EventHistory
    public EventIndex getEventIndex(Set<Integer> set) {
        Iterator<EventHistory> it = this.mEventHistoryList.iterator();
        EventIndex eventIndex = null;
        while (it.hasNext()) {
            EventIndex eventIndex2 = it.next().getEventIndex(set);
            if (eventIndex == null) {
                eventIndex = eventIndex2;
            } else if (!eventIndex2.isEmpty()) {
                eventIndex = EventIndex.combine(eventIndex, eventIndex2);
            }
        }
        return eventIndex != null ? eventIndex : EventIndex.EMPTY;
    }

    @Override // com.android.server.people.data.EventHistory
    public List<Event> queryEvents(Set<Integer> set, long j, long j2) {
        List<Event> arrayList = new ArrayList<>();
        for (EventHistory eventHistory : this.mEventHistoryList) {
            if (!eventHistory.getEventIndex(set).isEmpty()) {
                arrayList = combineEventLists(arrayList, eventHistory.queryEvents(set, j, j2));
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addEventHistory(EventHistory eventHistory) {
        this.mEventHistoryList.add(eventHistory);
    }

    private List<Event> combineEventLists(List<Event> list, List<Event> list2) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        while (i < list.size() && i2 < list2.size()) {
            if (list.get(i).getTimestamp() < list2.get(i2).getTimestamp()) {
                arrayList.add(list.get(i));
                i++;
            } else {
                arrayList.add(list2.get(i2));
                i2++;
            }
        }
        if (i < list.size()) {
            arrayList.addAll(list.subList(i, list.size()));
        } else if (i2 < list2.size()) {
            arrayList.addAll(list2.subList(i2, list2.size()));
        }
        return arrayList;
    }
}
