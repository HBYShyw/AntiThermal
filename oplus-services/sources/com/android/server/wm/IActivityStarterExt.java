package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.util.Pair;
import com.android.server.wm.ActivityStarter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityStarterExt {
    default boolean acPreloadAbortBgActivityStart(ActivityRecord activityRecord, WindowProcessController windowProcessController) {
        return false;
    }

    default void activityPreloadHandleStartActivity(ActivityRecord activityRecord) {
    }

    default void addNextAppTransitionRequests(TransitionController transitionController, int i, int i2) {
    }

    default ActivityOptions adjustOptionsForFlexibleTask(Task task, TaskDisplayArea taskDisplayArea, ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return activityOptions;
    }

    default ActivityOptions adjustOptionsForFlexibleWindow(ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityRecord activityRecord3, int i, int i2) {
        return activityOptions;
    }

    default ActivityOptions adjustOptionsForSplitPair(ActivityOptions activityOptions, ActivityRecord activityRecord) {
        return activityOptions;
    }

    default ActivityOptions adjustOptionsForSplitScreen(ActivityOptions activityOptions, ActivityRecord activityRecord) {
        return activityOptions;
    }

    default ActivityOptions adjustOptionsForZoom(ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2, Task task, int i) {
        return activityOptions;
    }

    default void boostLaunchActivity(WindowProcessController windowProcessController, ActivityInfo activityInfo) {
    }

    default boolean canAddingToTaskFragment(Task task, TaskFragment taskFragment, ActivityRecord activityRecord) {
        return true;
    }

    default boolean canClearActivityRecord(ActivityRecord activityRecord) {
        return false;
    }

    default void changeReusedTask(Task task) {
    }

    default Task changeReusedTaskForAppInner(Task task, ActivityRecord activityRecord) {
        return task;
    }

    default boolean checkSendReady(ActivityRecord activityRecord, ActivityRecord activityRecord2, int i) {
        return true;
    }

    default Pair<Intent, ActivityInfo> checkStartActivity(ActivityRecord activityRecord, ActivityInfo activityInfo, Intent intent, int i, int i2, String str, ActivityOptions activityOptions, ProfilerInfo profilerInfo, Task task, boolean z) {
        return null;
    }

    default Pair<Intent, ActivityInfo> checkStartActivityForAppLock(ActivityTaskSupervisor activityTaskSupervisor, ActivityRecord activityRecord, ActivityInfo activityInfo, Intent intent, int i, int i2, ActivityOptions activityOptions, ProfilerInfo profilerInfo, Task task) {
        return null;
    }

    default ActivityOptions createOptionsForZoom(ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2, int i) {
        return activityOptions;
    }

    default boolean executeAfterShutdownCheck(ActivityStarter.Request request) {
        return false;
    }

    default boolean executeBeforeShutdownCheck(ActivityTaskSupervisor activityTaskSupervisor, ActivityStarter.Request request) {
        return false;
    }

    default void executeRequestAfterErrSuccess(IApplicationThread iApplicationThread, WindowProcessController windowProcessController, String str, int i, int i2, Intent intent) {
    }

    default void executeRequestBeforeStartActivity(ActivityInfo activityInfo, int i) {
    }

    default int executeRequestReplaceErrCheck(String str, int i, int i2, int i3, Intent intent, int i4, ActivityInfo activityInfo, SafeActivityOptions safeActivityOptions, String str2, int i5, int i6) {
        return i3;
    }

    default void exitFlexibleEmbeddedTask(ActivityRecord activityRecord, Task task, ActivityOptions activityOptions, int i, boolean z) {
    }

    default void forceCancelTransitionIfNeed(ActivityRecord activityRecord, int i) {
    }

    default Pair<Integer, Pair<Intent, ActivityInfo>> getMultiAppActivityInfo(int i, Intent intent, String str, ActivityInfo activityInfo, int i2, int i3, int i4, SafeActivityOptions safeActivityOptions, String str2, ActivityTaskSupervisor activityTaskSupervisor, RootWindowContainer rootWindowContainer, int i5, int i6) {
        return null;
    }

    default boolean getScenarioTaskOrder(Task task, TaskDisplayArea taskDisplayArea, String str, ActivityOptions activityOptions, ActivityRecord activityRecord, boolean z) {
        return z;
    }

    default boolean getSubDifferentTopTask(Task task, TaskDisplayArea taskDisplayArea, ActivityOptions activityOptions, ActivityRecord activityRecord) {
        return false;
    }

    default void handleNonResizableTask(ActivityTaskSupervisor activityTaskSupervisor, Task task, int i, TaskDisplayArea taskDisplayArea, Task task2) {
    }

    default ActivityOptions handleRemoteTaskIfNeeded(ActivityRecord activityRecord, TaskDisplayArea taskDisplayArea, ActivityRecord activityRecord2, ActivityRecord activityRecord3, ActivityOptions activityOptions, ActivityRecord activityRecord4, boolean z, Task task, int i, int i2, Intent intent) {
        return activityOptions;
    }

    default void handlerIntentForAppDetails(ActivityTaskSupervisor activityTaskSupervisor, String str, int i, int i2, Intent intent, String str2) {
    }

    default boolean hansActivityIfNeeded(int i, String str, ActivityRecord activityRecord) {
        return false;
    }

    default void hookActivityBoost() {
    }

    default void hookAfterCheckBackgroundActivityStart() {
    }

    default ActivityOptions hookOptionsForSplit(ActivityRecord activityRecord, ActivityRecord activityRecord2, Task task, ActivityOptions activityOptions) {
        return activityOptions;
    }

    default void hookPostStartActivityProcessing(int i, Task task, ActivityRecord activityRecord) {
    }

    default void hooksetUxThreadValue(int i, int i2, String str, IBinder iBinder) {
    }

    default boolean interceptActivityForAppShareModeIfNeed(boolean z, boolean z2, Task task, ActivityRecord activityRecord, RootWindowContainer rootWindowContainer, Task task2, ActivityRecord activityRecord2) {
        return false;
    }

    default void interceptInitiatorForAppDetails(String str, Intent intent, int i) {
    }

    default boolean interceptStartActivityFromFlexibleWindow(Task task, Task task2, ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityStarter.Request request, ActivityRecord activityRecord2) {
        return false;
    }

    default boolean interceptStartForActiveSplitScreen(Intent intent, SafeActivityOptions safeActivityOptions, String str) {
        return false;
    }

    default boolean interceptStartForAsyncRotation(ActivityRecord activityRecord, Intent intent) {
        return false;
    }

    default boolean interceptStartForMirageCarMode(Intent intent, ActivityRecord activityRecord, ActivityRecord activityRecord2, Task task, ActivityOptions activityOptions, ActivityStarter activityStarter) {
        return false;
    }

    default boolean interceptStartForSplitScreenMode(Intent intent, ActivityStarter.Request request, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, Task task) {
        return false;
    }

    default boolean interceptWhenAnr(ActivityTaskManagerService activityTaskManagerService, ActivityRecord activityRecord) {
        return false;
    }

    default boolean isAllowPkgEmbed(String str) {
        return false;
    }

    default boolean isAllowedToStartActivityInZoom(ActivityRecord activityRecord, boolean z, Task task) {
        return true;
    }

    default boolean isAllowedToStartIncompactWindowingmode(ActivityRecord activityRecord, Task task) {
        return true;
    }

    default boolean isAppUnlockActivityFromPocketStudio(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default Pair<Boolean, Task> isAppUnlockPasswordActivity(RootWindowContainer rootWindowContainer, ActivityOptions activityOptions, ActivityOptions activityOptions2, boolean z, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return null;
    }

    default ActivityRecord isAppUnlockPasswordActivity(ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityRecord activityRecord3, Task task) {
        return activityRecord;
    }

    default boolean isCompactWindowMode(int i) {
        return false;
    }

    default boolean isInSplitScreenMode() {
        return false;
    }

    default boolean isMirageDisplay(int i) {
        return false;
    }

    default boolean isNeedFullScreenFromSettingTaskFragment(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default boolean isStartZoom(ActivityRecord activityRecord) {
        return false;
    }

    default void launchIntoCompatMode(ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2, Task task) {
    }

    default void markFlexibleSubTaskIfForceStopNeeded(ActivityRecord activityRecord, ActivityRecord activityRecord2, Task task) {
    }

    default int modifyCallingUidWhenRecentTask(Task task, ActivityInfo activityInfo, ActivityTaskManagerService activityTaskManagerService, int i, int i2, Intent intent) {
        return i;
    }

    default ActivityOptions modifyOptionsForCompactModeIfNeed(ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return activityOptions;
    }

    default TaskFragment modifyParentForEmbeddingSettingIfNeed(ActivityRecord activityRecord, Task task, TaskFragment taskFragment) {
        return taskFragment;
    }

    default boolean newTaskFlagDisable(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default boolean notReparentForComapctWindow(Task task, ActivityRecord activityRecord, Task task2) {
        return false;
    }

    default void notifyNoneTransition(boolean z, Transition transition, Task task, TransitionController transitionController) {
    }

    default void notifySysActivityStart(Class cls, ComponentName componentName) {
    }

    default void onStartFromPrimaryScreen(Task task, ActivityRecord activityRecord) {
    }

    default boolean onStartFromPrimaryScreen(Task task, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, TaskDisplayArea taskDisplayArea) {
        return false;
    }

    default boolean onStartFromPrimaryScreen(Task task, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, TaskDisplayArea taskDisplayArea, Intent intent) {
        return false;
    }

    default void parseFlexibleActivityInfo(ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
    }

    default void preloadAppSplash(ActivityStarter.Request request) {
    }

    default boolean pullPuttTaskBack(ActivityStarter activityStarter, ActivityRecord activityRecord, Task task, ActivityOptions activityOptions, ActivityOptions activityOptions2, ActivityRecord activityRecord2) {
        return false;
    }

    default void recycleTask(ActivityOptions activityOptions, Task task) {
    }

    default boolean replaceActivityStartFromLab(ActivityStarter.Request request) {
        return false;
    }

    default boolean replaceNewTaskIfNeed(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default void resetStartRecentFlag(ActivityOptions activityOptions) {
    }

    default void setForceUpdateWindow(Task task, ActivityRecord activityRecord) {
    }

    default void setHandleForcedResizableFlag(ActivityRecord activityRecord, Task task) {
    }

    default void setInitialState(ActivityRecord activityRecord, ActivityOptions activityOptions, Task task, ActivityRecord activityRecord2, ActivityRecord activityRecord3) {
    }

    default boolean shouldClearReusedActivity(Task task, ActivityRecord activityRecord, ActivityOptions activityOptions, ActivityRecord activityRecord2) {
        return false;
    }

    default boolean shouldLaunchInSplitTask(ActivityOptions activityOptions) {
        return false;
    }

    default void shouldShowStartingwidnowWhenMoveToFront(ActivityRecord activityRecord, Task task, ActivityRecord activityRecord2) {
    }

    default boolean startPreloadActivityWhilePreloading(Task task, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, String str, String str2) {
        return false;
    }

    default void triggerMaskFromIntentIfNeed(Intent intent, String str, String str2) {
    }

    default boolean triggerMaskFromIntentIfNeed(ActivityTaskSupervisor activityTaskSupervisor, Intent intent, ActivityRecord activityRecord, ActivityInfo activityInfo, String str, int i) {
        return false;
    }

    default void updateFlexibleWindowTask(Task task, Task task2, ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2, int i) {
    }

    default void updateTaskForZoom(ActivityOptions activityOptions, ActivityRecord activityRecord, ActivityRecord activityRecord2, Task task, int i, String str, Task task2) {
    }
}
