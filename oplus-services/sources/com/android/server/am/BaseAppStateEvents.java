package com.android.server.am;

import android.os.SystemClock;
import android.os.UserHandle;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;
import java.util.LinkedList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
abstract class BaseAppStateEvents<E> {
    static final boolean DEBUG_BASE_APP_STATE_EVENTS = false;
    final LinkedList<E>[] mEvents;
    int mExemptReason = -1;
    final MaxTrackingDurationConfig mMaxTrackingDurationConfig;
    final String mPackageName;
    final String mTag;
    final int mUid;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface Factory<T extends BaseAppStateEvents> {
        T createAppStateEvents(int i, String str);

        T createAppStateEvents(T t);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface MaxTrackingDurationConfig {
        long getMaxTrackingDuration();
    }

    abstract LinkedList<E> add(LinkedList<E> linkedList, LinkedList<E> linkedList2);

    abstract int getTotalEventsSince(long j, long j2, int i);

    abstract void trimEvents(long j, int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAppStateEvents(int i, String str, int i2, String str2, MaxTrackingDurationConfig maxTrackingDurationConfig) {
        this.mUid = i;
        this.mPackageName = str;
        this.mTag = str2;
        this.mMaxTrackingDurationConfig = maxTrackingDurationConfig;
        this.mEvents = new LinkedList[i2];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAppStateEvents(BaseAppStateEvents baseAppStateEvents) {
        this.mUid = baseAppStateEvents.mUid;
        this.mPackageName = baseAppStateEvents.mPackageName;
        this.mTag = baseAppStateEvents.mTag;
        this.mMaxTrackingDurationConfig = baseAppStateEvents.mMaxTrackingDurationConfig;
        this.mEvents = new LinkedList[baseAppStateEvents.mEvents.length];
        int i = 0;
        while (true) {
            LinkedList<E>[] linkedListArr = this.mEvents;
            if (i >= linkedListArr.length) {
                return;
            }
            if (baseAppStateEvents.mEvents[i] != null) {
                linkedListArr[i] = new LinkedList<>(baseAppStateEvents.mEvents[i]);
            }
            i++;
        }
    }

    void addEvent(E e, long j, int i) {
        LinkedList<E>[] linkedListArr = this.mEvents;
        if (linkedListArr[i] == null) {
            linkedListArr[i] = new LinkedList<>();
        }
        this.mEvents[i].add(e);
        trimEvents(getEarliest(j), i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trim(long j) {
        for (int i = 0; i < this.mEvents.length; i++) {
            trimEvents(j, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEmpty() {
        int i = 0;
        while (true) {
            LinkedList<E>[] linkedListArr = this.mEvents;
            if (i >= linkedListArr.length) {
                return true;
            }
            LinkedList<E> linkedList = linkedListArr[i];
            if (linkedList != null && !linkedList.isEmpty()) {
                return false;
            }
            i++;
        }
    }

    boolean isEmpty(int i) {
        LinkedList<E> linkedList = this.mEvents[i];
        return linkedList == null || linkedList.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(BaseAppStateEvents baseAppStateEvents) {
        if (this.mEvents.length != baseAppStateEvents.mEvents.length) {
            return;
        }
        int i = 0;
        while (true) {
            LinkedList<E>[] linkedListArr = this.mEvents;
            if (i >= linkedListArr.length) {
                return;
            }
            linkedListArr[i] = add(linkedListArr[i], baseAppStateEvents.mEvents[i]);
            i++;
        }
    }

    @VisibleForTesting
    LinkedList<E> getRawEvents(int i) {
        return this.mEvents[i];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTotalEvents(long j, int i) {
        return getTotalEventsSince(getEarliest(0L), j, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getEarliest(long j) {
        return Math.max(0L, j - this.mMaxTrackingDurationConfig.getMaxTrackingDuration());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, long j) {
        int i = 0;
        while (true) {
            LinkedList<E>[] linkedListArr = this.mEvents;
            if (i >= linkedListArr.length) {
                return;
            }
            if (linkedListArr[i] != null) {
                printWriter.print(str);
                printWriter.print(formatEventTypeLabel(i));
                printWriter.println(formatEventSummary(j, i));
            }
            i++;
        }
    }

    String formatEventSummary(long j, int i) {
        return Integer.toString(getTotalEvents(j, i));
    }

    String formatEventTypeLabel(int i) {
        return Integer.toString(i) + ":";
    }

    public String toString() {
        return this.mPackageName + "/" + UserHandle.formatUid(this.mUid) + " totalEvents[0]=" + formatEventSummary(SystemClock.elapsedRealtime(), 0);
    }
}
