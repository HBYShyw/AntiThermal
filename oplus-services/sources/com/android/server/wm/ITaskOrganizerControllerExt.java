package com.android.server.wm;

import android.app.ActivityManager;
import android.window.StartingWindowInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITaskOrganizerControllerExt {
    default void hookAddStartingWindow(ActivityRecord activityRecord, StartingWindowInfo startingWindowInfo) {
    }

    default void hookSetBinderUxFlag(boolean z) {
    }

    default void onBackPressedOnTaskRoot(Task task, ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onTaskAppeared(Task task, ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onTaskInfoChanged(Task task, ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onTaskVanished(Task task, ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default boolean playShiftUpAnimation(ActivityRecord activityRecord) {
        return true;
    }

    default boolean sameTaskInfoForSplitScreen(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        return true;
    }

    default boolean shouldDispatchTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        return false;
    }

    default boolean shouldDispatchTaskInfoChangedForEmbeddedTask(Task task, boolean z) {
        return false;
    }
}
