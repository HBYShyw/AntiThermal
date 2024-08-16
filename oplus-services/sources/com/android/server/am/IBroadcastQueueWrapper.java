package com.android.server.am;

import android.util.SparseArray;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastQueueWrapper {
    default void enqueueBroadcastLocked(ArrayList<BroadcastRecord> arrayList, boolean z, boolean z2) {
    }

    default SparseArray<BroadcastProcessQueue> getProcessQueues() {
        return null;
    }

    default String getQueueName() {
        return "";
    }

    default void processNextBroadcast(boolean z) {
    }

    default void processNextBroadcastLocked(boolean z, boolean z2) {
    }

    default IBroadcastQueueExt getExtImpl() {
        return new IBroadcastQueueExt() { // from class: com.android.server.am.IBroadcastQueueWrapper.1
        };
    }

    default IBroadcastQueueModernImplExt getModernExtImpl() {
        return new IBroadcastQueueModernImplExt() { // from class: com.android.server.am.IBroadcastQueueWrapper.2
        };
    }
}
