package com.android.server.people.data;

import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface EventHistory {
    EventIndex getEventIndex(int i);

    EventIndex getEventIndex(Set<Integer> set);

    List<Event> queryEvents(Set<Integer> set, long j, long j2);
}
