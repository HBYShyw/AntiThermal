package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastRecordWrapper {
    default void setDeliveryState(int i, long j, int i2) {
    }

    default IBroadcastRecordExt getExtImpl() {
        return new IBroadcastRecordExt() { // from class: com.android.server.am.IBroadcastRecordWrapper.1
        };
    }
}
