package com.android.server.power;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface INotifierWrapper {
    default void finishPendingBroadcastLocked() {
    }

    default void setPendingWakeUpBroadcast(boolean z) {
    }

    default void updatePendingBroadcastLocked() {
    }

    default Object getLock() {
        return new Object();
    }
}
