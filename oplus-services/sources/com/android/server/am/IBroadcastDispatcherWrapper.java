package com.android.server.am;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastDispatcherWrapper {
    default IBroadcastDispatcherExt getExtImpl() {
        return new IBroadcastDispatcherExt() { // from class: com.android.server.am.IBroadcastDispatcherWrapper.1
        };
    }

    default ArrayList<BroadcastRecord> getOrderedBroadcasts() {
        return new ArrayList<>();
    }
}
