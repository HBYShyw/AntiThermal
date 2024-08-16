package com.android.server.wm;

import android.content.ComponentName;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITaskDisplayAreaExt {
    default void addZoomChildren(Task task) {
    }

    default int adjustMaxPositionForSplitRootTask(Task task, int i) {
        return i;
    }

    default void clearZoomChildren() {
    }

    default ArrayList<WindowContainer> getZoomChildren() {
        return null;
    }

    default boolean isActivityPreloadDisplay(DisplayContent displayContent) {
        return false;
    }

    default boolean isBracketValidWindowingMode(int i) {
        return false;
    }

    default boolean isComactValidWindowingMode(int i) {
        return false;
    }

    default boolean isFlexibleTaskPriorityLower(WindowContainer windowContainer, Task task) {
        return false;
    }

    default boolean isMultiSearchActivityType(int i) {
        return false;
    }

    default boolean isMultiSearchTask(Task task) {
        return false;
    }

    default boolean isValidWindowingMode(int i, ActivityRecord activityRecord, Task task) {
        return false;
    }

    default boolean isZoomMode(int i) {
        return false;
    }

    default boolean moveSplitScreenTasksToFullScreen(TaskDisplayArea taskDisplayArea, Task task) {
        return false;
    }

    default void notifySysActivityStackChange(Class cls, ComponentName componentName) {
    }

    default void onFocusedTaskChanged(Task task, Task task2) {
    }

    default void onRootTaskRemoved(Task task) {
    }

    default boolean pauseResumeActivity(Task task, ActivityRecord activityRecord) {
        return false;
    }

    default Task replaceByMultiSearchIfNeed(Task task) {
        return task;
    }

    default void setMultiSearchTask(Task task) {
    }

    default boolean shouldIgnoreRotationForSplitMini() {
        return false;
    }

    default boolean validateWindowingMode(boolean z, int i, ActivityRecord activityRecord, Task task) {
        return false;
    }
}
