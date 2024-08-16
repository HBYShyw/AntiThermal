package com.android.server.am;

import com.android.server.CircularQueue;
import com.android.server.am.BaseAppStateEvents;
import com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class BaseAppStateTimeEvents<T extends BaseTimeEvent> extends BaseAppStateEvents<T> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAppStateTimeEvents(int i, String str, int i2, String str2, BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
        super(i, str, i2, str2, maxTrackingDurationConfig);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAppStateTimeEvents(BaseAppStateTimeEvents baseAppStateTimeEvents) {
        super(baseAppStateTimeEvents);
    }

    @Override // com.android.server.am.BaseAppStateEvents
    LinkedList<T> add(LinkedList<T> linkedList, LinkedList<T> linkedList2) {
        if (linkedList2 == null || linkedList2.size() == 0) {
            return linkedList;
        }
        if (linkedList == null || linkedList.size() == 0) {
            return (LinkedList) linkedList2.clone();
        }
        Iterator<T> it = linkedList.iterator();
        Iterator<T> it2 = linkedList2.iterator();
        T next = it.next();
        T next2 = it2.next();
        CircularQueue circularQueue = (LinkedList<T>) new LinkedList();
        long timestamp = next.getTimestamp();
        long timestamp2 = next2.getTimestamp();
        while (true) {
            if (timestamp == Long.MAX_VALUE && timestamp2 == Long.MAX_VALUE) {
                return circularQueue;
            }
            if (timestamp == timestamp2) {
                circularQueue.add((BaseTimeEvent) next.clone());
                if (it.hasNext()) {
                    next = it.next();
                    timestamp = next.getTimestamp();
                } else {
                    timestamp = Long.MAX_VALUE;
                }
                if (it2.hasNext()) {
                    next2 = it2.next();
                    timestamp2 = next2.getTimestamp();
                } else {
                    timestamp2 = Long.MAX_VALUE;
                }
            } else if (timestamp < timestamp2) {
                circularQueue.add((BaseTimeEvent) next.clone());
                if (it.hasNext()) {
                    next = it.next();
                    timestamp = next.getTimestamp();
                } else {
                    timestamp = Long.MAX_VALUE;
                }
            } else {
                circularQueue.add((BaseTimeEvent) next2.clone());
                if (it2.hasNext()) {
                    next2 = it2.next();
                    timestamp2 = next2.getTimestamp();
                } else {
                    timestamp2 = Long.MAX_VALUE;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.am.BaseAppStateEvents
    public int getTotalEventsSince(long j, long j2, int i) {
        LinkedList linkedList = this.mEvents[i];
        int i2 = 0;
        if (linkedList != null && linkedList.size() != 0) {
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                if (((BaseTimeEvent) it.next()).getTimestamp() >= j) {
                    i2++;
                }
            }
        }
        return i2;
    }

    @Override // com.android.server.am.BaseAppStateEvents
    void trimEvents(long j, int i) {
        LinkedList linkedList = this.mEvents[i];
        if (linkedList == null) {
            return;
        }
        while (linkedList.size() > 0 && ((BaseTimeEvent) linkedList.peek()).getTimestamp() < j) {
            linkedList.pop();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class BaseTimeEvent implements Cloneable {
        long mTimestamp;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BaseTimeEvent(long j) {
            this.mTimestamp = j;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BaseTimeEvent(BaseTimeEvent baseTimeEvent) {
            this.mTimestamp = baseTimeEvent.mTimestamp;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void trimTo(long j) {
            this.mTimestamp = j;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getTimestamp() {
            return this.mTimestamp;
        }

        public Object clone() {
            return new BaseTimeEvent(this);
        }

        public boolean equals(Object obj) {
            return obj != null && obj.getClass() == BaseTimeEvent.class && ((BaseTimeEvent) obj).mTimestamp == this.mTimestamp;
        }

        public int hashCode() {
            return Long.hashCode(this.mTimestamp);
        }
    }
}
