package com.android.server.wm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import com.android.server.wm.RootWindowContainer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IRootWindowContainerExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface IFindTaskResultExt {
        default boolean handleIncomingUser(int i, int i2) {
            return false;
        }
    }

    default void checkAnimationReady() {
    }

    default Task checkRootHomeTask(Task task, Task task2) {
        return null;
    }

    default boolean findTaskOnlyForLaunch(ActivityTaskManagerService activityTaskManagerService, RootWindowContainer.FindTaskResult findTaskResult, String str, Intent intent) {
        return false;
    }

    default ActivityRecord getStartingActivity(WindowProcessController windowProcessController) {
        return null;
    }

    default void handleResizingWindows() {
    }

    default void hookAcquireLaunchBoost() {
    }

    default void hookHandleNotObscuredLocked(WindowState windowState, boolean z, boolean z2, float f) {
    }

    default void hookPerformSurfacePlacementNoTraceInit() {
    }

    default void hooksetUxThreadValue(int i, int i2, String str) {
    }

    default boolean isNotLargeFoldDevice() {
        return true;
    }

    default boolean isTaskOnPuttDisplay(Task task) {
        return false;
    }

    default void moveActivityToPinnedRootTask(Task task, ActivityRecord activityRecord) {
    }

    default void onDisplayAdded(DisplayContent displayContent) {
    }

    default void onDisplayRemoved(DisplayContent displayContent) {
    }

    default void positionSurface(int i, int i2) {
    }

    default void putExtraIfNeededForDisplayingNewFeatures(String str, Intent intent, int i) {
    }

    default void putTasksToSleep(Task task) {
    }

    default void removeSleepToken(String str, DisplayContent displayContent) {
    }

    default boolean resumeFocusedSkipped(DisplayContent displayContent, Task task, ActivityRecord activityRecord) {
        return false;
    }

    default boolean resumeSecondHomeIfNeed(DisplayContent displayContent, Task task, Task task2) {
        return false;
    }

    default void setProcRaiseAdjList(Object obj) {
    }

    default boolean shouldIgnoreKeyguardOccluedTransition(DisplayContent displayContent) {
        return false;
    }

    default boolean shouldObscureApplicationContentOnSecondaryDisplay() {
        return true;
    }

    default boolean shouldSkipUnFreezeCheck(WindowState windowState) {
        return false;
    }

    default boolean shouldWindowSurfaceSaved(WindowState windowState, DisplayContent displayContent) {
        return false;
    }

    default boolean skipResolveRootTaskIfNeed(Task task) {
        return false;
    }

    default boolean skipSleepTransition(DisplayContent displayContent) {
        return false;
    }

    default ActivityInfo switchDefaultLauncherForBootAware(Context context, ActivityInfo activityInfo, int i, Intent intent) {
        return null;
    }

    default void updatePendingScreenBrightnessOverrideMap() {
    }
}
