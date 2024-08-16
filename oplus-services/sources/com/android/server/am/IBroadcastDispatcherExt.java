package com.android.server.am;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastDispatcherExt {
    default void adjustQueueIfNecessary(ArrayList<BroadcastRecord> arrayList, BroadcastRecord broadcastRecord) {
    }

    default void enqueueOrderedBroadcastLocked(int i, BroadcastRecord broadcastRecord) {
    }

    default void setAMS(ActivityManagerService activityManagerService) {
    }

    default void setBroadcastDispatcher(BroadcastDispatcher broadcastDispatcher) {
    }

    default ArrayList<BroadcastRecord> getOrderedBroadcasts() {
        return new ArrayList<>();
    }
}
