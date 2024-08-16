package com.android.server.alarm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAlarmManagerServiceBroadcastStatsExt {
    default void addNumCanceledWakeup(int i) {
    }

    default void addNumRealWakeupScreeoff(int i) {
    }

    default void addNumWakeupScreenoff(int i) {
    }

    default long getLastTimeWakeup() {
        return 0L;
    }

    default int getNumCanceledWakeup() {
        return 0;
    }

    default int getNumRealWakeupScreeoff() {
        return 0;
    }

    default int getNumRealWakeupWhenReset() {
        return 0;
    }

    default int getNumWakeupScreenoff() {
        return 0;
    }

    default int getNumWakeupWhenReset() {
        return 0;
    }

    default long getTimestampWakupCountReset() {
        return 0L;
    }

    default void setLastTimeWakeup(long j) {
    }

    default void setNumCanceledWakeup(int i) {
    }

    default void setNumRealWakeupScreeoff(int i) {
    }

    default void setNumRealWakeupWhenReset(int i) {
    }

    default void setNumWakeupScreenoff(int i) {
    }

    default void setNumWakeupWhenReset(int i) {
    }

    default void setTimestampWakupCountReset(long j) {
    }
}
