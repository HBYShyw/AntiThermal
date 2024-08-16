package com.android.server.wm;

import android.content.Intent;
import android.view.MotionEvent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IRecentTasksExt {
    default int adjustPreloadingTaskIndex(int i, int i2, Task task, int i3) {
        return i;
    }

    default boolean getRecentTasksImpl(Intent intent) {
        return false;
    }

    default boolean hasCompatibleActivityTypeAndWindowingMode(int i, int i2) {
        return false;
    }

    default boolean isInVisibleRange(Task task) {
        return false;
    }

    default boolean isLaunchedFromMultiSearch(Task task, Task task2) {
        return false;
    }

    default boolean reCheckUserSetupComplete(Task task) {
        return false;
    }

    default void removeContainerTask(Task task) {
    }

    default void removeForAddTask(Task task, Task task2) {
    }

    default boolean shouldRemoveIndexForAddTask(Task task, Task task2) {
        return false;
    }

    default boolean skipAddPreloadingFakeTask(Task task) {
        return false;
    }

    default boolean skipMovePreloadingTask(Task task) {
        return false;
    }

    default boolean skipMultiSearchTask(Task task) {
        return false;
    }

    default boolean skipPersistMultiSearchTask(Task task) {
        return false;
    }

    default boolean skipPreloadingTaskInRecents(Task task) {
        return false;
    }

    default boolean skipResetFreezeTaskListReordering(MotionEvent motionEvent) {
        return false;
    }

    default boolean skipShowRecentTask(Task task, int i) {
        return false;
    }
}
