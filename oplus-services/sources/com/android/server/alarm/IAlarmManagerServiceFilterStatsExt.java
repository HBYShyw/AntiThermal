package com.android.server.alarm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAlarmManagerServiceFilterStatsExt {
    default int getNumWakeupWhenReset() {
        return 0;
    }

    default int getNumWakeupWhenScreenoff() {
        return 0;
    }

    default void setNumWakeupWhenReset(int i) {
    }

    default void setNumWakeupWhenScreenoff(int i) {
    }
}
