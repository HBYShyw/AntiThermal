package com.android.server.am;

import android.app.job.JobInfo;
import android.content.ComponentName;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOpulsSceneCallback {
    default void bootCompleted() {
    }

    default boolean checkActivityIfRestricted(int i, String str, int i2, String str2, ComponentName componentName) {
        return false;
    }

    default boolean checkAlarmIfRestricted(int i, String str, String str2) {
        return false;
    }

    default boolean checkBumpServiceIfRestricted(int i, String str, String str2) {
        return false;
    }

    default boolean checkJobIfRestricted(int i, String str, JobInfo jobInfo) {
        return false;
    }

    default boolean checkProviderIfRestricted(int i, String str, int i2, String str2, String str3, String str4) {
        return false;
    }

    default void checkReStartServiceIfRestricted(int i, String str) {
    }

    default boolean checkReceiverIfRestricted(BroadcastRecord broadcastRecord, Object obj) {
        return false;
    }

    default boolean checkStartServiceIfRestricted(int i, int i2, String str, int i3, String str2, String str3, ComponentName componentName, String str4, boolean z) {
        return false;
    }

    default boolean checkSyncIfRestricted(int i, String str) {
        return false;
    }

    default void noteIsolatedApp(int i, int i2, String str, boolean z) {
    }

    default void noteSysShutdown() {
    }

    default void noteSysStateChanged(int i, int i2, String str) {
    }

    default void noteWatchdog() {
    }

    default void resumeTopActivityIfNeedLocked(int i, String str) {
    }
}
