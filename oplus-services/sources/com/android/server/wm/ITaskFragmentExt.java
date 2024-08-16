package com.android.server.wm;

import android.app.servertransaction.ClientTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import com.android.server.wm.ActivityRecord;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITaskFragmentExt {
    default void addChild(ActivityRecord activityRecord) {
    }

    default void addColorModeOnResume(ClientTransaction clientTransaction, boolean z, String str) {
    }

    default void addTask(WindowContainer windowContainer, Task task, Intent intent, ActivityInfo activityInfo) {
    }

    default boolean affectVisibilityByWindowMode(int i, WindowContainer windowContainer) {
        return false;
    }

    default boolean canOccludedBySplitRootTask(TaskFragment taskFragment, WindowContainer windowContainer) {
        return true;
    }

    default boolean canResumeWhilePausing(ActivityRecord activityRecord) {
        return false;
    }

    default boolean canSpecifyOrientationInActivityEmbedding() {
        return false;
    }

    default void disableSensorScreenShot(ActivityRecord activityRecord, ActivityRecord activityRecord2, Context context) {
    }

    default void disposeFullTfIfNeeded(Task task) {
    }

    default boolean dontUpdateSmallestWidthInParallelWindow(TaskFragment taskFragment) {
        return false;
    }

    default void executeAppTransitionForEnterZoomWindowIfNeed(TaskFragment taskFragment, ActivityRecord activityRecord) {
    }

    default String getCounterInfo() {
        return null;
    }

    default int getPrevWinMode() {
        return -1;
    }

    default int getTaskVisibilityInMultiSearch(Task task, ActivityRecord activityRecord) {
        return -1;
    }

    default void handleActivityResumed(ActivityRecord activityRecord, Task task) {
    }

    default boolean hasFullyOccludedContainer(TaskFragment taskFragment, TaskFragment taskFragment2) {
        return false;
    }

    default void hookHandleTopActivity(ActivityRecord activityRecord) {
    }

    default boolean hookIsActivityEmbedded(boolean z, TaskFragment taskFragment, ActivityRecord activityRecord) {
        return z;
    }

    default void hookSetBinderUxFlag(boolean z, ActivityRecord activityRecord) {
    }

    default boolean interceptResumeActivity(ActivityRecord activityRecord) {
        return false;
    }

    default boolean interceptStartChangeTransitionIfNeed(Task task, Rect rect, Rect rect2) {
        return false;
    }

    default boolean isActivityEmbedded(TaskFragment taskFragment, ActivityRecord activityRecord) {
        return false;
    }

    default boolean isActivityPreloadDisplay(DisplayContent displayContent) {
        return false;
    }

    default boolean isAllowedToEmbedActivity(ActivityRecord activityRecord, int i) {
        return true;
    }

    default boolean isBracketMode(int i) {
        return false;
    }

    default boolean isCompactMode(int i) {
        return false;
    }

    default boolean isCreateForMagicWindow() {
        return false;
    }

    default boolean isOnMirageStreamMode(int i) {
        return false;
    }

    default boolean isPrimaryTopTaskFragment(TaskFragment taskFragment, WindowContainer windowContainer) {
        return false;
    }

    default boolean isTaskLaunchedFromMultiSearch(Task task) {
        return false;
    }

    default boolean isZoomMode(int i) {
        return false;
    }

    default boolean needMaintainVisibleSate(TaskFragment taskFragment) {
        return false;
    }

    default void notifyActivityResume(ActivityRecord activityRecord) {
    }

    default void notifySysActivityColdLaunch(Class cls, ComponentName componentName) {
    }

    default void onRealActivityStateChanged(ActivityRecord activityRecord, ActivityRecord.State state) {
    }

    default void onTaskFragmentInfoChanged(TaskFragment taskFragment) {
    }

    default void onTaskFragmentPrepareSurface() {
    }

    default void overrideOrientation(Configuration configuration, DisplayContent displayContent, int i) {
    }

    default void overrideOrientationInFoldDevice(Configuration configuration, DisplayContent displayContent) {
    }

    default void pauseInRecentsAnim() {
    }

    default void removeChild(WindowContainer windowContainer) {
    }

    default int reorderIndex(Task task, ActivityRecord activityRecord, int i) {
        return i;
    }

    default void resetPauseStateInRecentsAnim() {
    }

    default void setConfiguration(int i, Configuration configuration) {
    }

    default void setForeAppInfo(ActivityRecord activityRecord) {
    }

    default void setPreloadTaskFocusedApp(DisplayContent displayContent, ActivityRecord activityRecord) {
    }

    default void setPrevWinMode(int i) {
    }

    default boolean shouldDeferResumeUntilRecentsAnimFinished() {
        return false;
    }

    default boolean shouldRealBeReusmed(Task task, ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldRemoveOnLastChildRemoval() {
        return true;
    }

    default boolean shouldShipIntersectWithContainingAppBounds(Task task, int i) {
        return false;
    }

    default boolean shouldSkipTaskVisible(TaskFragment taskFragment, WindowContainer windowContainer) {
        return false;
    }

    default boolean shouldUseParentScreenWidthDp(TaskFragment taskFragment, ActivityRecord activityRecord) {
        return false;
    }

    default boolean startPausingIfNeed(TaskFragment taskFragment, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default void topResumedActivityChanged(ActivityRecord activityRecord) {
    }

    default void triggerAppTransReadyInAdvance(ActivityRecord activityRecord) {
    }

    default void updateWaitActivityToAttachIfNeeded(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
    }
}
