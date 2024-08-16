package com.android.server.wm;

import android.os.IBinder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityClientControllerExt {
    default void activityDestroyed(ActivityRecord activityRecord) {
    }

    default void activityResumed(IBinder iBinder, int i) {
    }

    default void closeRemoteTask(ActivityTaskManagerService activityTaskManagerService, int i) {
    }

    default String getCallingPackage(String str, IBinder iBinder, WindowManagerGlobalLock windowManagerGlobalLock) {
        return str;
    }

    default void hookActivityFinishEnd(ActivityRecord activityRecord) {
    }

    default void hookActivityFinishIfResumeNotOK(ActivityRecord activityRecord) {
    }

    default void hookActivityResumed(IBinder iBinder) {
    }

    default boolean ignoringOverridePendingTransition(ActivityRecord activityRecord) {
        return false;
    }

    default void moveActivityTaskToBack(Task task, IBinder iBinder, boolean z) {
    }

    default boolean needMoveTaskToBack(IBinder iBinder) {
        return false;
    }

    default void notifyFlexibleWindowTaskVanish(ActivityRecord activityRecord, boolean z, boolean z2) {
    }

    default void onActivityRequestOrientation() {
    }

    default boolean onBackPressed(ActivityRecord activityRecord, IBinder iBinder) {
        return false;
    }

    default boolean setRequestedOrientation(ActivityRecord activityRecord, int i, boolean z) {
        return false;
    }

    default boolean setRequestedOrientationAfter(ActivityRecord activityRecord, int i, boolean z) {
        return false;
    }

    default boolean setRequestedOrientationBefore(ActivityRecord activityRecord, int i, boolean z) {
        return false;
    }

    default boolean skipSetTurnScreenOn(ActivityRecord activityRecord, boolean z) {
        return false;
    }
}
