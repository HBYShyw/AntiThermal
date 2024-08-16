package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.IApplicationThread;
import android.content.Intent;
import android.view.Surface;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IRemoteTaskHandlerManagerExt {
    default ActivityOptions activateRemoteTaskIfNeeded(RootWindowContainer rootWindowContainer, IApplicationThread iApplicationThread, int i, int i2, int i3, int i4, Task task, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions) {
        return null;
    }

    default boolean anyTaskExist(int i) {
        return false;
    }

    default boolean closeRemoteTask(int i, int i2) {
        return false;
    }

    default ActivityRecord findTaskForReuseIfNeeded(ActivityRecord activityRecord, ActivityOptions activityOptions, TaskDisplayArea taskDisplayArea, int i) {
        return null;
    }

    default boolean findTaskOnlyForLaunch(Intent intent, String str, int i) {
        return false;
    }

    default TaskDisplayArea getFinalPreferredTaskDisplayArea() {
        return null;
    }

    default void handleFinishActivity(Task task, ActivityRecord activityRecord) {
    }

    default void handleInterceptSessionIfNeeded() {
    }

    default void handleProcessDied(WindowProcessController windowProcessController) {
    }

    default void handleRemoveTask(Task task, String str) {
    }

    default void handleReuseTaskIfNeeded(IApplicationThread iApplicationThread, Task task, Intent intent, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions) {
    }

    default boolean inAnyInterceptSession() {
        return false;
    }

    default boolean interceptFromRecents(Task task, Intent intent) {
        return false;
    }

    default boolean isDeliverToCurrentTop(TaskDisplayArea taskDisplayArea, ActivityRecord activityRecord, ActivityRecord activityRecord2, int i, int i2) {
        return false;
    }

    default boolean isDisplaySwitchDetected() {
        return false;
    }

    default boolean isFromBackgroundWhiteList(String str) {
        return false;
    }

    default TaskDisplayArea queryPreferredDisplayArea(Task task, TaskDisplayArea taskDisplayArea, Intent intent, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions) {
        return null;
    }

    default void resetSession() {
    }

    default void setRootWindowContainer(RootWindowContainer rootWindowContainer) {
    }

    default boolean shouldIgnoreRelaunch(boolean z, int i, int i2, int i3) {
        return false;
    }

    default void updateRemoteTaskIfNeeded(Task task, ActivityOptions activityOptions) {
    }

    default void updateSurface(String str, Surface surface) {
    }
}
