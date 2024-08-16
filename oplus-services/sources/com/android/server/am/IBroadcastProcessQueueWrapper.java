package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastProcessQueueWrapper {
    default void enqueueBroadcast(BroadcastRecord broadcastRecord, int i, boolean z, boolean z2) {
    }

    default int getRunnableAtReasonWithoutRefresh() {
        return 0;
    }

    default long getRunnableAtWithoutRefresh() {
        return 0L;
    }

    default IBroadcastProcessQueueExt getExtImpl() {
        return new IBroadcastProcessQueueExt() { // from class: com.android.server.am.IBroadcastProcessQueueWrapper.1
        };
    }
}
