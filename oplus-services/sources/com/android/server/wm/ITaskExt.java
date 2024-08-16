package com.android.server.wm;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.TaskInfo;
import android.app.servertransaction.ClientTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.util.ArraySet;
import android.view.DisplayInfo;
import android.view.SurfaceControl;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITaskExt {
    public static final int SCREEN_ORIENTATION_UNFIXED = -100;

    default void addChild(WindowContainer windowContainer) {
    }

    default void addColorModeOnResume(ClientTransaction clientTransaction, boolean z, String str) {
    }

    default void addEmbeddedChildren(int i) {
    }

    default void addExtraTaskInfo(Task task, TaskInfo taskInfo) {
    }

    default void addStartingBackColorLayerIfNeed(ActivityRecord activityRecord) {
    }

    default void adjustAppBoundsInCompactWindowMode(Task task, Rect rect, Rect rect2, DisplayInfo displayInfo, int i) {
    }

    default boolean adjustMoveDisplayToTopForMirage(int i, boolean z) {
        return z;
    }

    default void adjustTaskConfiguration(Task task, Configuration configuration) {
    }

    default boolean allowTaskDetachFromEmbedded() {
        return false;
    }

    default void applyNewOrientationWhenReuseIfNeed(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
    }

    default boolean canClearActivityRecord(ActivityRecord activityRecord) {
        return false;
    }

    default void cancelTaskChildrenAnimationIfNeed(Task task) {
    }

    default boolean checkVisibleForSplit(boolean z, boolean z2, Task task) {
        return false;
    }

    default boolean cropWindowsToRootTaskBounds(Task task) {
        return true;
    }

    default void disableSensorScreenShot(ActivityRecord activityRecord, ActivityRecord activityRecord2, Context context) {
    }

    default boolean dontPauseAfterQActivityIfNeed(Task task) {
        return true;
    }

    default void dump(PrintWriter printWriter, String str) {
    }

    default boolean enableRotationWhenFolding() {
        return false;
    }

    default void excuteAppTransitionForCompactWindowIfNeed(ActivityRecord activityRecord, Task task) {
    }

    default ActivityRecord findEnterPipOnTaskSwitchCandidateForPs(Task task) {
        return null;
    }

    default boolean forceCreateRemoteAnimationTarget(ActivityRecord activityRecord) {
        return false;
    }

    default boolean getAllowReparent() {
        return false;
    }

    default int getCornerRadius() {
        return 0;
    }

    default int getCurrentCornerRadius() {
        return 0;
    }

    default float getCurrentScale() {
        return 1.0f;
    }

    default int getCurrentShadows() {
        return 0;
    }

    default String getDisableFeatures() {
        return "";
    }

    default int getEmbeddedContainerTaskId() {
        return -1;
    }

    default float getFixedMaxAspectRatio() {
        return -1.0f;
    }

    default float getFixedMinAspectRatio() {
        return -1.0f;
    }

    default int getFixedScreenOrientation() {
        return -100;
    }

    default boolean getFlexibleEmbedding() {
        return false;
    }

    default int getForceAppOrientationConfig() {
        return -1;
    }

    default boolean getForceUpdateSurface() {
        return false;
    }

    default boolean getFullScreenByFullButton() {
        return false;
    }

    default int getHookAppBounds() {
        return -1;
    }

    default Rect getLastBounds() {
        return null;
    }

    default long getLastResumedActivityStamp() {
        return 0L;
    }

    default int getLaunchConfigScenario() {
        return 0;
    }

    default int getLaunchScenario() {
        return 0;
    }

    default int getLaunchSplashTheme() {
        return 0;
    }

    default String getLaunchSplashThemePackage() {
        return null;
    }

    default boolean getLaunchedFromMultiSearch() {
        return false;
    }

    default float getMaxScale() {
        return 1.0f;
    }

    default float getMinScale() {
        return 1.0f;
    }

    default int getOriginTaskIdForZoomWindow() {
        return -1;
    }

    default boolean getRootLockDeviceTask() {
        return false;
    }

    default float getScale() {
        return 1.0f;
    }

    default boolean getScreenOffPlay() {
        return false;
    }

    default int getShadowRadiusFocused() {
        return 0;
    }

    default int getShadowRadiusUnfocused() {
        return 0;
    }

    default int getShouldRelaunchConfig() {
        return -1;
    }

    default int getSimulateDensity() {
        return 0;
    }

    default int getSimulatedWidth() {
        return 0;
    }

    default boolean getSupportAllMode() {
        return false;
    }

    default String getSupportRatios() {
        return null;
    }

    default Object getZoomStateManager() {
        return null;
    }

    default boolean handleActivityReorder(Task task, WindowContainer windowContainer, int i, boolean z) {
        return false;
    }

    default void handleConfigChanged(Configuration configuration, Rect rect, Task task) {
    }

    default void handleRemoveTask(String str, int i) {
    }

    default void handleTaskCreated(Task task) {
    }

    default void hansTopOrSecondActivityIfNeeded(ActivityRecord activityRecord) {
    }

    default boolean hasNoSurfaceShowing(Task task, boolean z, boolean z2) {
        return false;
    }

    default void hideBackgroundSurface(Task task) {
    }

    default void hookHandleTopActivity(ActivityRecord activityRecord) {
    }

    default boolean ignoreResumePuttTask(Task task) {
        return false;
    }

    default boolean interceptResumeActivity(ActivityRecord activityRecord) {
        return false;
    }

    default boolean isAdjustInputMethod() {
        return false;
    }

    default boolean isAlwaysOnTop() {
        return false;
    }

    default boolean isAvoidMoveTaskToFront(ActivityOptions activityOptions) {
        return false;
    }

    default boolean isCompactWindowingMode(int i) {
        return false;
    }

    default boolean isContainerTask() {
        return false;
    }

    default boolean isCreateForSingleSplit() {
        return false;
    }

    default boolean isDragZoomMode() {
        return false;
    }

    default boolean isDragZoomTask(Task task) {
        return false;
    }

    default boolean isFlexibleEmbedded() {
        return false;
    }

    default boolean isFlexibleTaskMaximized() {
        return false;
    }

    default boolean isFlexibleWindowScenario(ActivityOptions activityOptions) {
        return false;
    }

    default boolean isFlexibleWindowScenario(int... iArr) {
        return false;
    }

    default boolean isFocusChangeWithNonFlexible() {
        return false;
    }

    default boolean isForceAllowAllOrientation() {
        return false;
    }

    default boolean isForceAlwaysOnTop(Task task) {
        return false;
    }

    default boolean isForceUpdateWindow() {
        return false;
    }

    default boolean isFromExtraLauncher() {
        return false;
    }

    default boolean isFromSplitToZoom() {
        return false;
    }

    default boolean isHasCaption() {
        return false;
    }

    default boolean isIgnoreSystemBar() {
        return false;
    }

    default boolean isInPendingAnimation(Task task) {
        return false;
    }

    default boolean isInSplitBlackList(String str) {
        return false;
    }

    default boolean isMaintainTaskState() {
        return false;
    }

    default boolean isMiniRootTask(Task task) {
        return false;
    }

    default boolean isNeedDefaultAnimation() {
        return false;
    }

    default boolean isNeedForceStopWhenSubDisplayScenario() {
        return false;
    }

    default boolean isNeedMaintainTaskState() {
        return false;
    }

    default boolean isNeedMask() {
        return false;
    }

    default boolean isNeedReCalcBounds() {
        return false;
    }

    default boolean isNeedSkipCloseTransition() {
        return false;
    }

    default boolean isNoAnimationTask(int i) {
        return false;
    }

    default boolean isParentChanged(Task task, Task task2) {
        return false;
    }

    default boolean isPendingToBottomTask(int i) {
        return false;
    }

    default boolean isPuttTask() {
        return false;
    }

    default boolean isReparentToTaskView() {
        return false;
    }

    default boolean isScreenOffPlay(Task task) {
        return false;
    }

    default boolean isShowRecent() {
        return true;
    }

    default boolean isSkipAnimation() {
        return false;
    }

    default boolean isSkipWaitingForDrawn() {
        return false;
    }

    default boolean isSmartBackend() {
        return false;
    }

    default boolean isStartZoomFormFloatScenario(ActivityOptions activityOptions) {
        return false;
    }

    default boolean isTaskCanvas() {
        return false;
    }

    default boolean isTaskEmbedded() {
        return false;
    }

    default boolean isTaskInreParent() {
        return false;
    }

    default boolean isTaskSplitToPs() {
        return false;
    }

    default boolean isZoomMode(int i) {
        return false;
    }

    default void launchIntoCompactwindowingMode(Task task, boolean z) {
    }

    default void moveTaskToBack(Task task, Task task2) {
    }

    default void moveTaskToFront(ActivityOptions activityOptions, Task task) {
    }

    default boolean noAnimForRelatedActivity(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default void notifyActivityResume(ActivityRecord activityRecord) {
    }

    default void notifyChildActivityRecordAdded(ActivityRecord activityRecord) {
    }

    default void notifyChildActivityRecordRemoved(ActivityRecord activityRecord) {
    }

    default void notifyCompactWindowState(Task task, boolean z) {
    }

    default void notifyFlexibleWindowTaskUpdate() {
    }

    default void notifySysActivityColdLaunch(Class cls, ComponentName componentName) {
    }

    default void notifySysActivityHotLaunch(Class cls, ComponentName componentName) {
    }

    default void notifyZoomModeChanged(int i, int i2) {
    }

    default void onApplyNoAnimationOfTask(Task task) {
    }

    default void onConfigurationChangedOfTask(Configuration configuration, Rect rect, Task task) {
    }

    default void onDescendantOrientationChanged(WindowContainer windowContainer) {
    }

    default void onSetTaskIntent(Task task, ActivityInfo activityInfo, boolean z) {
    }

    default void onTaskOrganizerApplyChanges(WindowContainer windowContainer, int i, int i2, Configuration configuration) {
    }

    default void onTaskParentChanged(ConfigurationContainer configurationContainer, ConfigurationContainer configurationContainer2, Task task) {
    }

    default void onTaskParentChanged(DisplayContent displayContent, DisplayContent displayContent2, ConfigurationContainer configurationContainer, ConfigurationContainer configurationContainer2, Task task) {
    }

    default void onTaskRemoved(Task task) {
    }

    default void onTaskTopActivityCrashed(Task task) {
    }

    default void onTaskWindowFocusChanged(Task task, boolean z) {
    }

    default void onTaskWindowingModeChanged(Task task, int i, int i2) {
    }

    default void onWindowingModeChanged(Task task, int i) {
    }

    default boolean pauseResumeActivity(Task task, ActivityRecord activityRecord) {
        return false;
    }

    default boolean positionChildAtBottom(Task task, Task task2, Task task3) {
        return false;
    }

    default void prepareDimBounds(TaskFragment taskFragment, Rect rect) {
    }

    default void prepareSurfaces(SurfaceControl.Transaction transaction, DisplayContent displayContent, Task task) {
    }

    default void removeChild(WindowContainer windowContainer) {
    }

    default void removeCompactMask(Task task, boolean z) {
    }

    default void removeEmbeddedChildren(int i) {
    }

    default void removedFromRecents(Task task) {
    }

    default void reparentTask(Task task, Task task2) {
    }

    default void resetLaunchParams() {
    }

    default boolean resumeTopActivityInnerInCompactWindow(boolean[] zArr, ActivityRecord activityRecord, ActivityOptions activityOptions, boolean z) {
        return false;
    }

    default void saveAppUsageHistoryRecord(ActivityRecord activityRecord) {
    }

    default void saveFixedRotatedTaskWhenKeyGuardGoingAway(Task task, int i, boolean z) {
    }

    default void sendBroadcastResumedActivity(Handler handler, Context context, ActivityRecord activityRecord) {
    }

    default void setAllowReparent(boolean z) {
    }

    default void setAppTransitionReadyInAdvance(DisplayContent displayContent, ActivityRecord activityRecord) {
    }

    default void setContainerTask(boolean z) {
    }

    default void setCreateForSingleSplit(boolean z) {
    }

    default void setCurrentCornerRadius(int i) {
    }

    default void setCurrentScale(float f) {
    }

    default void setCurrentShadowsRadius(int i) {
    }

    default void setDisableFeatures(String str) {
    }

    default void setEmbeddedContainerTask(int i) {
    }

    default void setEmbeddedTaskFrame(int i, Rect rect) {
    }

    default void setEnableRotationWhenFolding(boolean z) {
    }

    default void setFixedMaxAspectRatio(float f) {
    }

    default void setFixedMinAspectRatio(float f) {
    }

    default void setFixedScreenOrientation(int i) {
    }

    default void setFlexibleEmbedding(boolean z) {
    }

    default void setFlexibleFrame(Task task, Rect rect) {
    }

    default void setFlexibleHandleFrame(Rect rect) {
    }

    default void setFlexibleTaskMaximized(boolean z) {
    }

    default void setForceAllowAllOrientation(boolean z) {
    }

    default void setForceAppOrientationConfig(int i) {
    }

    default void setForceUpdateSurface(boolean z) {
    }

    default void setForceUpdateWindow(boolean z) {
    }

    default void setForeAppInfo(ActivityRecord activityRecord) {
    }

    default void setFullScreenByFullButton(boolean z) {
    }

    default void setHookAppBounds(int i) {
    }

    default void setIsFromSplitToZoom(boolean z) {
    }

    default void setIsReparentToTaskView(boolean z) {
    }

    default void setIsSkipAnimation(boolean z) {
    }

    default void setIsSkipWaitingForDrawn(boolean z) {
    }

    default void setIsTaskEmbedded(boolean z) {
    }

    default void setIsTaskSplitToPs(boolean z) {
    }

    default void setLastBounds(Rect rect) {
    }

    default void setLastResumedActivityStamp(long j) {
    }

    default void setLaunchParams(ActivityOptions activityOptions) {
    }

    default void setLaunchScenario(int i, boolean z) {
    }

    default void setLaunchSplashTheme(int i) {
    }

    default void setLaunchSplashThemePackage(String str) {
    }

    default void setLaunchedFromMultiSearch(boolean z) {
    }

    default void setMaintainTaskState(boolean z) {
    }

    default void setNeedForceStopWhenSubDisplayScenario(boolean z) {
    }

    default void setNeedMask(boolean z) {
    }

    default void setNeedReCalcBounds(boolean z) {
    }

    default void setNeedSkipCloseTransition(boolean z) {
    }

    default void setOriginTaskIdForZoomWindow(int i) {
    }

    default void setPuttTask(boolean z) {
    }

    default void setRequestFixConfigOrientation() {
    }

    default void setRootLockDeviceTask(boolean z) {
    }

    default void setScale(float f) {
    }

    default void setSceenOffPlay(boolean z) {
    }

    default void setShouldRelaunchConfig(int i) {
    }

    default void setShowRecent(boolean z) {
    }

    default void setSupportAllMode(boolean z) {
    }

    default void setTaskCanvas(boolean z) {
    }

    default void setUseOriginOrientationConfig(boolean z) {
    }

    default void setZoomStateManager(Object obj) {
    }

    default boolean sholdUpdateSplitScreenLauncherDim(Task task, DisplayContent displayContent) {
        return false;
    }

    default boolean shouldDoPuttTransition(int i) {
        return false;
    }

    default boolean shouldFixConfigOrientation() {
        return false;
    }

    default boolean shouldInterceptInputEvent() {
        return false;
    }

    default boolean shouldSkipLaunchIntoCompactWindowingMode() {
        return false;
    }

    default boolean shouldSkipRotationForFlexibleWindow() {
        return false;
    }

    default boolean shouldUpdateTransitLocked(ActivityRecord activityRecord, int i, ActivityOptions activityOptions) {
        return false;
    }

    default boolean shouldUseOriginOrientationConfig() {
        return false;
    }

    default boolean shouldUseSelfDimmer() {
        return false;
    }

    default boolean skipGoToSleep() {
        return false;
    }

    default void skipNextLaunchIntoCompactWindowingMode(boolean z) {
    }

    default void startFreezingDisplay(Task task, ActivityTaskManagerService activityTaskManagerService) {
    }

    default boolean supportMultiResume(ActivityRecord activityRecord) {
        return false;
    }

    default boolean supportsSplitScreenByVendorPolicy(Task task, boolean z) {
        return z;
    }

    default void updateAlphaInPinnedMode(Task task, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
    }

    default boolean updateConfigWidthForPageModeSetting(Task task, int i) {
        return false;
    }

    default boolean updateSWDpInCompactWindowingMode(Task task, int i) {
        return false;
    }

    default boolean shouldUseTaskDimmer(Task task, Dimmer dimmer) {
        return !task.isTranslucent(null);
    }

    default Rect getFlexibleFrame() {
        return new Rect();
    }

    default Rect getFlexibleHandleFrame() {
        return new Rect();
    }

    default Rect getEmbeddedTaskFrame(int i) {
        return new Rect();
    }

    default ArraySet<Integer> getEmbeddedChildren() {
        return new ArraySet<>();
    }

    default List<ActivityManager.RecentTaskInfo> getEmbeddedRecentTasks() {
        return new ArrayList();
    }
}
