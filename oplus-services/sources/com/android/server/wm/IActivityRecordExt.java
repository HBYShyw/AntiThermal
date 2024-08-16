package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.servertransaction.ClientTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.util.MergedConfiguration;
import android.view.RemoteAnimationDefinition;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.window.SizeConfigurationBuckets;
import android.window.TaskSnapshot;
import com.android.internal.policy.AttributeCache;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.StartingSurfaceController;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityRecordExt {
    default void activityPreloadAbort(ActivityRecord activityRecord, String str) {
    }

    default void activityPreloadHandleDisplayChanged(Configuration configuration, int i) {
    }

    default boolean activityResumedLocked(ActivityRecord activityRecord, boolean z) {
        return false;
    }

    default void addColorModeOnResume(ClientTransaction clientTransaction, boolean z, String str) {
    }

    default void addFlagsOfIntentFromSettingTaskFragment(ActivityRecord activityRecord, Intent intent, String str, AttributeCache.Entry entry, int i) {
    }

    default void addVisibleWindow(int i, String str, ApplicationInfo applicationInfo) {
    }

    default boolean adjustActivityWidth(ActivityRecord activityRecord, boolean z) {
        return z;
    }

    default void adjustAppCutoutInCompactWindow(ActivityRecord activityRecord, Rect rect, Configuration configuration) {
    }

    default void adjustBracketMode(Configuration configuration, Rect rect, Rect rect2, Rect rect3, ActivityRecord activityRecord) {
    }

    default void adjustLastReportedConfiguration(Configuration configuration, Configuration configuration2, int i, ActivityRecord activityRecord, MergedConfiguration mergedConfiguration) {
    }

    default boolean adjustOccludesParent(boolean z) {
        return z;
    }

    default boolean adjustPreserveWindowWhenRelaunch(boolean z, int i, Configuration configuration) {
        return z;
    }

    default int adujstLayerIfneeded(int i, ActivityRecord activityRecord) {
        return i;
    }

    default boolean allowUseSnapshot(ActivityRecord activityRecord, boolean z, boolean z2, boolean z3, boolean z4) {
        return false;
    }

    default void applyOplusCompatAspectRatioIfNeed(Configuration configuration, Configuration configuration2) {
    }

    default boolean assignLayersIfNeed(ActivityRecord activityRecord) {
        return true;
    }

    default boolean autoResolutionEnable() {
        return false;
    }

    default boolean blockActivityRecordRequestOrientation(ActivityRecord activityRecord, int i) {
        return false;
    }

    default void calculateFlexibleOffset(int[] iArr) {
    }

    default Rect calculateInsetsForAnimationTarget(ActivityRecord activityRecord, Rect rect) {
        return rect;
    }

    default int calculateNewChanges(int i, Configuration configuration, SizeConfigurationBuckets sizeConfigurationBuckets) {
        return -1;
    }

    default void calculateOplusCompatBoundsOffset(int[] iArr, ActivityRecord activityRecord, Configuration configuration, ComponentName componentName, Rect rect, Rect rect2, int i) {
    }

    default ActivityRecord.State changeStartActiveStateIfNeed(ActivityRecord.State state) {
        return state;
    }

    default void clearAccessControlPassPackages(Task task, String str, int i) {
    }

    default void clearLastParentBeforeSplitScreen() {
    }

    default boolean clearStartingWindowWhenSnapshotDiffOrientation(ActivityRecord activityRecord) {
        return false;
    }

    default void collectAppRequestFinishAr(ActivityRecord activityRecord, String str) {
    }

    default boolean dontAllowsetOccludesParent(ActivityRecord activityRecord) {
        return false;
    }

    default boolean dontApplyAspectRatio(ActivityRecord activityRecord) {
        return false;
    }

    default void finishActivity(ActivityRecord activityRecord, String str, boolean z) {
    }

    default void finishIfPossible(ActivityRecord activityRecord, String str, boolean z) {
    }

    default boolean forceCreateRemoteAnimationTarget(ActivityRecord activityRecord) {
        return false;
    }

    default boolean forceRelaunchByNavBarHide() {
        return false;
    }

    default boolean forceRelaunchWhenActivityIdle(Configuration configuration) {
        return false;
    }

    default Configuration getActivitySpecificConfig() {
        return null;
    }

    default int getAdjustSizeType() {
        return -1;
    }

    default int getAnimationLayer() {
        return 0;
    }

    default boolean getApplyNewOrientationWhenReuse() {
        return false;
    }

    default int getCameraDisplayMode() {
        return -1;
    }

    default boolean getCompactReparenting() {
        return false;
    }

    default float getCompatScaleInOplusCompatMode() {
        return 1.0f;
    }

    default String getDisableFeatures() {
        return "";
    }

    default String getFinishReason() {
        return "";
    }

    default float getFixedAspectRatioForActivity(ActivityRecord activityRecord, boolean z) {
        return -1.0f;
    }

    default float getFixedMaxAspectRatio() {
        return -1.0f;
    }

    default float getFixedMinAspectRatio() {
        return -1.0f;
    }

    default int getFixedScreenOrientation() {
        return -1;
    }

    default Object getFlexibleActivityInfo() {
        return null;
    }

    default int getFoldDeviceFixRotationFromTask(ActivityRecord activityRecord, int i) {
        return i;
    }

    default int getForceLetterBox() {
        return -1;
    }

    default int getHookAppBounds() {
        return -1;
    }

    default Intent getLastIntentReceived() {
        return null;
    }

    default Task getLastParentBeforeSplitScreen() {
        return null;
    }

    default DisplayContent getLastReportedDisplay() {
        return null;
    }

    default int getLaunchDisplayId() {
        return 0;
    }

    default boolean getLaunchedFromMultiSearch() {
        return false;
    }

    default boolean getMaxBoundsForZoomWindow() {
        return false;
    }

    default boolean getNotifyHotStart() {
        return false;
    }

    default int getRelaunchConfig() {
        return -1;
    }

    default RemoteAnimationDefinition getRemoteAnimationDefinition(RemoteAnimationDefinition remoteAnimationDefinition) {
        return remoteAnimationDefinition;
    }

    default boolean getRootLockActivity() {
        return false;
    }

    default String getRootLockPkgName() {
        return null;
    }

    default boolean getShouldFixConfigOrientation() {
        return false;
    }

    default boolean getShouldUseOriginOrientationConfig() {
        return false;
    }

    default Rect getSizeCompatBoundsInOplusCompatMode() {
        return null;
    }

    default int getStartingWindowType(int i, int i2, int i3) {
        return -1;
    }

    default boolean getSupportAllMode() {
        return false;
    }

    default boolean getTransitionForceAHead() {
        return false;
    }

    default boolean getWindowAnimationTag() {
        return false;
    }

    default void handleActivityReparent(ActivityRecord activityRecord, Task task) {
    }

    default boolean handleDestroySurfaces(String str, int i) {
        return false;
    }

    default int handleStartingWindowForCompactWindow(ActivityRecord activityRecord, TaskSnapshot taskSnapshot, int i) {
        return i;
    }

    default boolean hasOtherTopActivityOccludesKeyguard(ActivityRecord activityRecord) {
        return false;
    }

    default boolean hasSizeCompatBoundsInOplusCompatMode() {
        return false;
    }

    default boolean hasSplashWindowFlag() {
        return false;
    }

    default boolean hasSurfaceView() {
        return false;
    }

    default void hookLifecyclePause(String str, String str2, String str3) {
    }

    default void hookPrepareSurfaces(boolean z) {
    }

    default int hookRotationForPIPIfNeeded(int i, DisplayContent displayContent, ActivityRecord activityRecord) {
        return i;
    }

    default void hookSetBinderUxFlag(boolean z, ActivityRecord activityRecord) {
    }

    default boolean hookShouldRelaunchLocked(int i, int i2, Configuration configuration) {
        return ((~i2) & i) != 0;
    }

    default boolean ignoreChangePlayingTransition(boolean z) {
        return false;
    }

    default boolean ignoreOplusAppPlayingTransition(ActivityRecord activityRecord) {
        return false;
    }

    default boolean ignoreOrientationRespectedWithInsets(ActivityRecord activityRecord, boolean z) {
        return z;
    }

    default boolean ignoreSnapShotRotation(int i, Task task) {
        return false;
    }

    default boolean ignoreTimeOut(ActivityRecord activityRecord, String str) {
        return false;
    }

    default boolean ignoreTimeOutForNonFinishing(ActivityRecord activityRecord, String str) {
        return false;
    }

    default boolean inOplusActivityCompatMode() {
        return false;
    }

    default boolean inOplusCompactWindowMode(boolean z) {
        return false;
    }

    default boolean inOplusCompatEnabled() {
        return false;
    }

    default boolean inOplusCompatMode() {
        return false;
    }

    default void interceptActivityOnSecondary(ActivityRecord activityRecord, KeyguardController keyguardController) {
    }

    default boolean interceptRemoveStartingWindow(ActivityRecord activityRecord, Handler handler, StartingSurfaceController.StartingSurface startingSurface, boolean z) {
        return false;
    }

    default boolean isActivityConfigOverrideDisable(ActivityRecord activityRecord, WindowProcessController windowProcessController) {
        return false;
    }

    default boolean isActivityPreloadDisplay(int i, DisplayContent displayContent) {
        return false;
    }

    default boolean isAnimationTarget() {
        return false;
    }

    default boolean isBackgroundPuttTask(ActivityRecord activityRecord) {
        return false;
    }

    default boolean isCompactRoot(ActivityRecord activityRecord) {
        return false;
    }

    default boolean isCompactWindowingMode(int i) {
        return false;
    }

    default boolean isDisableshowWhenLockByRecents() {
        return false;
    }

    default boolean isFlexibleSuitable() {
        return false;
    }

    default boolean isFontPageKilled(Task task, ActivityRecord activityRecord) {
        return false;
    }

    default boolean isForceHidden() {
        return false;
    }

    default boolean isInRestoring(ActivityRecord activityRecord) {
        return false;
    }

    default boolean isMirageWindowDisplayId(int i) {
        return false;
    }

    default boolean isNotTransferForEmbeded(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return false;
    }

    default boolean isParentChanged() {
        return false;
    }

    default boolean isResizeableForMultiSearch(Task task) {
        return false;
    }

    default boolean isSettingTaskFragment(TaskFragment taskFragment) {
        return false;
    }

    default boolean isSnapshotStarting() {
        return false;
    }

    default boolean isSupprotBracketMode(ActivityRecord activityRecord) {
        return false;
    }

    default boolean isUpdateFromNavbarHide(Configuration configuration, Configuration configuration2, int i, String str) {
        return false;
    }

    default boolean isWindowSurfaceSaved(WindowState windowState) {
        return false;
    }

    default boolean isZoomMode(int i) {
        return false;
    }

    default boolean isZoomSplashExceptionList(String str) {
        return false;
    }

    default void makeActiveIfNeeded(int i, String str, ApplicationInfo applicationInfo) {
    }

    default int needChangeDiff(int i, int i2, int i3) {
        return i;
    }

    default boolean notIgnoreWindowDisableStarting(ActivityRecord activityRecord) {
        return false;
    }

    default void notifyActivityMultiWindowAllowanceChanged() {
    }

    default void notifyActivityPaused(Task task, ActivityRecord activityRecord) {
    }

    default void notifyActivityRecordVisible(ActivityRecord activityRecord, boolean z) {
    }

    default void notifyLaunchTime(ApplicationInfo applicationInfo, String str, long j) {
    }

    default void notifySysActivityHotLaunch(Class cls, ComponentName componentName) {
    }

    default RemoteAnimationTarget obtainLaunchViewInfoIfNeeded(ActivityRecord activityRecord, RemoteAnimationTarget remoteAnimationTarget) {
        return remoteAnimationTarget;
    }

    default void onActivityDestroyed(ActivityRecord activityRecord) {
    }

    default void onActivityFinish(ActivityRecord activityRecord, String str) {
    }

    default void onActivityRecordCreated(ActivityRecord activityRecord) {
    }

    default int onActivityRecordOrientationInit(ActivityRecord activityRecord, int i) {
        return i;
    }

    default void onActivityRecordParentChanged(ConfigurationContainer configurationContainer, ConfigurationContainer configurationContainer2, ActivityRecord activityRecord) {
    }

    default void onActivityRecordParentChangedAfter(ConfigurationContainer configurationContainer, ConfigurationContainer configurationContainer2, ActivityRecord activityRecord) {
    }

    default void onAnimationFinished(ActivityRecord activityRecord) {
    }

    default void onCompactWindowAnimationFinished(ActivityRecord activityRecord) {
    }

    default void onLeashAnimationStarting(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
    }

    default void onPreActivityRecordConfigurationChanged(Configuration configuration) {
    }

    default void onShowAllWindowsOfActivity(Task task) {
    }

    default void onWindowsDrawn(ActivityRecord activityRecord) {
    }

    default void onWindowsVisible(ActivityRecord activityRecord) {
    }

    default void pauseCompactResumedActivity(Task task, ActivityRecord activityRecord) {
    }

    default boolean performClearTaskLocked(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return true;
    }

    default void registerActivityMultiWindowAllowanceObserver(IBinder iBinder) {
    }

    default void removeImmediately() {
    }

    default void removeUnVisibleWindow(int i, String str) {
    }

    default void resetLeashCropIfNeed(ActivityRecord activityRecord, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
    }

    default void resetUseTransferredAnimIfRequired(boolean z, boolean z2) {
    }

    default void resolveAppOrientationIfNeed(ActivityRecord activityRecord, Configuration configuration, int i, Configuration configuration2) {
    }

    default void resolveFlexibleActivityConfig(Configuration configuration, Rect rect) {
    }

    default void resolveScreenOnFlag(ActivityRecord activityRecord, boolean z) {
    }

    default void reviseMergedOverrideConfiguration(Configuration configuration, int i) {
    }

    default void reviseWindowFlagsForStarting(ActivityRecord activityRecord, boolean z, boolean z2, boolean z3, boolean z4, ActivityRecord.State state) {
    }

    default void setAdjustSizeType(int i) {
    }

    default void setAnimationLayer(int i) {
    }

    default void setAppBoundsIfNeed(ActivityRecord activityRecord, Configuration configuration) {
    }

    default void setApplyNewOrientationWhenReuse(boolean z) {
    }

    default void setBlockOrientationDuringFixedRotation(boolean z) {
    }

    default void setCameraDisplayMode(int i) {
    }

    default void setCompactReparenting(boolean z) {
    }

    default void setDisableFeatures(String str) {
    }

    default void setDisableshowWhenLockByRecents(boolean z) {
    }

    default void setFixedMaxAspectRatio(float f) {
    }

    default void setFixedMinAspectRatio(float f) {
    }

    default void setFixedScreenOrientation(int i) {
    }

    default void setFlexibleActivityInfo(Object obj) {
    }

    default void setForceLetterBox(int i) {
    }

    default void setForceRelaunchByNavBarHide(boolean z) {
    }

    default void setHookAppBounds(int i) {
    }

    default void setIsAnimationTarget(boolean z) {
    }

    default void setLastIntentReceived(Intent intent) {
    }

    default void setLastParentBeforeSplitScreen() {
    }

    default void setLastReportedDisplay(DisplayContent displayContent) {
    }

    default void setLaunchDisplayId(int i) {
    }

    default void setLaunchedFromMultiSearch(boolean z) {
    }

    default void setMaxBoundsForZoomWindow(boolean z) {
    }

    default void setNotifyHotStart(boolean z) {
    }

    default boolean setOrientation(ActivityRecord activityRecord, int i, int i2) {
        return false;
    }

    default void setParentChanged(boolean z) {
    }

    default void setRelaunchConfig(int i) {
    }

    default void setRootLockActivity(boolean z) {
    }

    default void setShouldBeVisibleInSecondaryKeyguard(boolean z) {
    }

    default void setShouldFixConfigOrientation(boolean z) {
    }

    default void setShouldUseOriginOrientationConfig(boolean z) {
    }

    default boolean setSimultaneousDisplayState(boolean z) {
        return false;
    }

    default void setSkipAppTransitionWhenStarting(boolean z) {
    }

    default void setSnapshotStarting(boolean z) {
    }

    default void setSourceRecordHint(ActivityRecord activityRecord) {
    }

    default void setStateForVisible(ActivityRecord.State state, ActivityRecord.State state2, int i, String str, ApplicationInfo applicationInfo, String str2) {
    }

    default void setSupportAllMode(boolean z) {
    }

    default void setSupprotBracketMode(ActivityRecord activityRecord, boolean z) {
    }

    default void setTransitionForceAHead(boolean z) {
    }

    default void setVisibleRequested(boolean z, TransitionController transitionController) {
    }

    default void setWindowAnimationTag(boolean z) {
    }

    default void setupAppFrameForCompatMode(Rect rect, Rect rect2, ActivityRecord activityRecord) {
    }

    default boolean shouldApplyAnimation(ActivityRecord activityRecord, boolean z) {
        return true;
    }

    default boolean shouldAssociateStartingDataWithTask() {
        return false;
    }

    default boolean shouldBeVisible(boolean z, int i) {
        return false;
    }

    default boolean shouldBeVisibleInSecondaryKeyguard() {
        return false;
    }

    default boolean shouldBlockOrientationDuringFixedRotation() {
        return false;
    }

    default boolean shouldBlockPrepareActivityHideTransitionAnimation(ActivityRecord activityRecord, boolean z) {
        return false;
    }

    default boolean shouldBlockTransferAnimation(ActivityRecord activityRecord, AnimatingActivityRegistry animatingActivityRegistry) {
        return false;
    }

    default boolean shouldClearCompat(ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldClearSizeCompatMode(Configuration configuration) {
        return false;
    }

    default boolean shouldClearStartingPolicyVisibility(ActivityRecord activityRecord) {
        return true;
    }

    default boolean shouldCreateCompatDisplayInsetsForMirageWindow(int i) {
        return false;
    }

    default boolean shouldCreateCompatDisplayInsetsForSquare(ActivityRecord activityRecord) {
        return true;
    }

    default boolean shouldDeferTaskAppear(Task task) {
        return true;
    }

    default boolean shouldDelayRemovalInCompleteFinishing(ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldExitFixedRotation(DisplayContent displayContent, ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldForceRelaunch(int i, MergedConfiguration mergedConfiguration, Configuration configuration, Configuration configuration2, boolean z) {
        return false;
    }

    default boolean shouldIgnoreOrientationRequests(ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldMakeHomeActivityVisibleOnSecondary(ActivityRecord activityRecord, KeyguardController keyguardController) {
        return false;
    }

    default boolean shouldReviseScreenOrientationForApp(ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldSizeCompatVerticalCenter() {
        return true;
    }

    default boolean shouldSkipAppTransition(ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldSkipAppTransitionWhenStarting() {
        return false;
    }

    default boolean shouldSkipRemoveStartingWindow(WindowState windowState, ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldSkipTransition(String str) {
        return false;
    }

    default boolean shouldSpeedUnLock(ActivityRecord activityRecord, AppTransition appTransition) {
        return false;
    }

    default boolean shouldSplashDislay(boolean z, boolean z2, boolean z3, StartingData startingData) {
        return false;
    }

    default boolean shouldUseAppThemeSnapshot(WindowState windowState, boolean z) {
        return false;
    }

    default boolean shouldWindowSurfaceSaved(WindowState windowState, DisplayContent displayContent) {
        return false;
    }

    default void showInTransition(SurfaceControl.Transaction transaction, WindowManagerService windowManagerService, SurfaceControl surfaceControl) {
    }

    default boolean skipCheckKeyguardVisibility() {
        return false;
    }

    default boolean skipPrepareAppTransitionForMirageIfNeed(Task task, int i, String str) {
        return false;
    }

    default void startCompactMask(Task task) {
    }

    default boolean supportsSplitScreenByVendorPolicy(ActivityRecord activityRecord, boolean z) {
        return z;
    }

    default boolean syncFinishedForOptimizeStartup(ActivityRecord activityRecord) {
        return false;
    }

    default int toMultiSearchActivityTypeIfNeed(ActivityInfo activityInfo, IPackageManager iPackageManager, int i) {
        return i;
    }

    default void topResumedActivityChanged(ActivityRecord activityRecord, boolean z, WindowProcessController windowProcessController) {
    }

    default void transferPreloadedInfoIfNeed(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
    }

    default void unregisterActivityMultiWindowAllowanceObserver(IBinder iBinder) {
    }

    default void updateActivitySpecificConfig(Configuration configuration) {
    }

    default void updateActivityStateChanged(ActivityRecord activityRecord, TaskFragment taskFragment, ActivityRecord.State state, String str) {
    }

    default void updateActvityResumeTimeStamp(ActivityRecord activityRecord) {
    }

    default boolean updateActvityState(ActivityRecord activityRecord) {
        return false;
    }

    default void updateAllDrawnActivity(ActivityRecord activityRecord) {
    }

    default void updateAllTopApps() {
    }

    default void updateCompactFullScreenWindow(ActivityRecord activityRecord, AttributeCache.Entry entry, int i) {
    }

    default int updateOrSaveResolvedThemeIfNeeded(Task task, ActivityRecord activityRecord, boolean z, boolean z2, ActivityRecord activityRecord2, ActivityOptions activityOptions, boolean z3, boolean z4, int i, int i2, int i3) {
        return i3;
    }

    default void updateRecordSurfaceViewState(boolean z) {
    }

    default void updateStartingRecords(ActivityRecord activityRecord, boolean z) {
    }

    default float getMaxAspectRatio(ActivityInfo activityInfo, Rect rect) {
        return activityInfo.getMaxAspectRatio();
    }
}
