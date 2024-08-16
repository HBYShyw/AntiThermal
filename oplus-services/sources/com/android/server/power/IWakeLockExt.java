package com.android.server.power;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IWakeLockExt {
    default long getActiveSince() {
        return 0L;
    }

    default boolean getDisabledByHans() {
        return false;
    }

    default long getTotalTime() {
        return 0L;
    }

    default void setActiveSince(long j) {
    }

    default void setDisableByHans(boolean z) {
    }

    default void setTotalTime(long j) {
    }
}
