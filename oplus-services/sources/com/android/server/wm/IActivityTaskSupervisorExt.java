package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.servertransaction.ClientTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityTaskSupervisorExt {
    public static final int REMOVE_TASK_TYPE_KILL = 3;
    public static final int REMOVE_TASK_TYPE_NONE = 0;
    public static final int REMOVE_TASK_TYPE_NOT_KILL_PKG = 1;
    public static final int REMOVE_TASK_TYPE_NOT_KILL_PROC = 2;

    default void addColorModeOnResume(ClientTransaction clientTransaction, boolean z, String str) {
    }

    default void adjustStartActivityIntentIfNeed(ActivityRecord activityRecord) {
    }

    default void appLaunchTimeout(RootWindowContainer rootWindowContainer, Context context) {
    }

    default void beforeStartActivityFromRecents(Task task, ActivityOptions activityOptions) {
    }

    default void cameraPreopenIfNeed(Context context, ActivityRecord activityRecord) {
    }

    default boolean canStartActivity(ActivityRecord activityRecord) {
        return true;
    }

    default boolean checkIsValidParentForSplitScreen(Task task, Task task2) {
        return true;
    }

    default boolean exitFlexibleEmbeddedTask(Task task, ActivityOptions activityOptions, boolean z) {
        return false;
    }

    default void findTaskToMoveToFront(Context context) {
    }

    default ResolveInfo getMultiAppResolveInfoIfNeed(ResolveInfo resolveInfo, int i, ActivityTaskSupervisor activityTaskSupervisor, Intent intent, String str, int i2, int i3) {
        return null;
    }

    default int getRemoveTaskFilterType(WindowProcessController windowProcessController) {
        return 0;
    }

    default void handleActivityIdle(ActivityRecord activityRecord) {
    }

    default void handleActivityStart(String str, String str2, int i) {
    }

    default void handleActivityStartAfterStartProc(ActivityRecord activityRecord) {
    }

    default void handleActivityStartBeforeStartProc(ActivityRecord activityRecord, boolean z) {
    }

    default boolean handleNonResizableTaskIfNeeded(Task task) {
        return false;
    }

    default void handleRemoveTask(Task task, boolean z, boolean z2, String str) {
    }

    default void handleRemoveTask(boolean z, int i, String str) {
    }

    default void hookAcquireLaunchBoost() {
    }

    default void hookBeforeRemoveTask(Task task, String str) {
    }

    default void hookRealStartActivityLocked(ActivityRecord activityRecord) {
    }

    default void hookRecordAppStartCount(int i, String str, String str2) {
    }

    default void hookStartSpecificActivity(Context context) {
    }

    default boolean intercepTaskStartForFlexibleWindow(Task task, Context context) {
        return false;
    }

    default boolean interceptRecentStartForAsyncRotation(Task task, ActivityOptions activityOptions, boolean z) {
        return false;
    }

    default boolean isActivitySelfNotAnimating(ActivityRecord activityRecord, Task task) {
        return false;
    }

    default boolean isPuttDisplay(int i) {
        return false;
    }

    default boolean isRunningDisallowed(ActivityRecord activityRecord, WindowManagerService windowManagerService) {
        return false;
    }

    default void modifyApplicaitonInfoForMirageCarMode(ActivityRecord activityRecord) {
    }

    default int modifyTransitionType(Task task, int i) {
        return i;
    }

    default void notifyAppSwitch(ActivityRecord activityRecord, ActivityTaskManagerService activityTaskManagerService, boolean z) {
    }

    default void recordTopActivityWhenScreenOff(ActivityTaskManagerService activityTaskManagerService) {
    }

    default void removeAccessControlPassAsUser(String str, int i, boolean z) {
    }

    default void removeContainerTaskForEmbeddedTask(Task task) {
    }

    default void removePuttTask(Task task) {
    }

    default void requestStateInternal(int i) {
    }

    default void resolveActivity(Intent intent) {
    }

    default int resolvedCallingUid(ActivityRecord activityRecord, Intent intent, int i) {
        return i;
    }

    default void sendTheiaEvent(ActivityRecord activityRecord, boolean z, Context context) {
    }

    default void setLaunchTimeStart(ActivityRecord activityRecord) {
    }

    default void setOplusCallingUid(Intent intent) {
    }

    default void setStartRecentsReason(ActivityOptions activityOptions, boolean z) {
    }

    default boolean skipUpdateResumedActivityIfNeeded(Task task, ActivityRecord activityRecord, String str) {
        return false;
    }

    default void startActivityFromRecents(Task task) {
    }

    default boolean startActivityFromRecents(int i, ActivityOptions activityOptions, int i2) {
        return true;
    }

    default boolean startActivityFromRecents(Task task, ActivityOptions activityOptions) {
        return false;
    }

    default void updateFlexibleWindowTask(Task task, ActivityOptions activityOptions, int i) {
    }

    default void updateRecentWindowingModeIfNeeded(Task task) {
    }

    default void updateResumeLostActivity(ActivityRecord activityRecord) {
    }
}
