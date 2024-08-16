package com.android.server.wm;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.DisplayAddress;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayContentExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface IGestureStaticExt {
        default boolean isWinHasGestureExclusionRestrictions(WindowState windowState) {
            return true;
        }
    }

    default boolean HasZoomWindowAboveImeInputTarget(WindowState windowState, InputTarget inputTarget, WindowContainer windowContainer) {
        return false;
    }

    default void addRefreshRateRangeForPackage(String str, float f, float f2) {
    }

    default int adjustConstantSystemGestureExclusionLimitDp(int i, DisplayContent displayContent) {
        return i;
    }

    default void adjustScreenConfigurationForCarLink(DisplayContent displayContent, Configuration configuration, float f) {
    }

    default void applyPreferredMode(IWindowStateExt iWindowStateExt, WindowState windowState, boolean z) {
    }

    default void applyPreferredMode(IWindowStateExt iWindowStateExt, WindowState windowState, boolean z, int i, float f) {
    }

    default void applyRotation(int i, int i2, int i3) {
    }

    default void beginHookUpdateFocusedWindowLocked(DisplayContent displayContent, WindowState windowState, WindowManagerService windowManagerService) {
    }

    default void checkSetFixedRotationLaunchingApp(DisplayContent displayContent, ActivityRecord activityRecord) {
    }

    default void checkWindowRefreshRateChange() {
    }

    default int correctRotationParam(int i) {
        return i;
    }

    default void debugForBootTime(String str, WindowState windowState) {
    }

    default boolean deferChangeTarget(boolean z, WindowState windowState, WindowState windowState2, WindowState windowState3, InsetsControlTarget insetsControlTarget) {
        return false;
    }

    default void disableStatusBarForSystem(WindowManagerService windowManagerService, WindowState windowState) {
    }

    default void dispatchWallpaperVisibility(boolean z) {
    }

    default void displayChangeToOn() {
    }

    default boolean doRotationAnimation(ActivityRecord activityRecord) {
        return true;
    }

    default boolean dontDoFixedRotatinAnimation(DisplayContent displayContent, ActivityRecord activityRecord) {
        return false;
    }

    default void endHookUpdateFocusedWindowLocked(DisplayContent displayContent, WindowManagerService windowManagerService, int i) {
    }

    default int getFixedScreenOrientation(WindowContainer windowContainer, int i) {
        return i;
    }

    default Object getFlexibleActivityImeAnimationState() {
        return null;
    }

    default float getPreferredMaxRefreshRate(float f) {
        return f;
    }

    default float getPreferredMinRefreshRate(float f) {
        return f;
    }

    default int getPreferredModeId(float f, int i) {
        return i;
    }

    default void getScaleBound(Task task, Rect rect) {
    }

    default int getSplitRequestedOrientation() {
        return -2;
    }

    default boolean handleNonOccludesParentActiviy(DisplayContent displayContent, ActivityRecord activityRecord) {
        return false;
    }

    default boolean hasAdjacentTaskFragmentInActivityEmbeddedState(WindowState windowState) {
        return false;
    }

    default boolean hasGestureAnimationController() {
        return false;
    }

    default void hookAdjustForImeIfNeeded(WindowState windowState, boolean z, int i, WindowState windowState2, WindowState windowState3) {
    }

    default void hookForComplexScene(WindowState windowState, boolean z) {
    }

    default void hookOnDisplayChanged(DisplayContent displayContent) {
    }

    default void hookPerformLayout(DisplayContent displayContent, WindowState windowState, WindowState windowState2) {
    }

    default void hookPrepareSurfaces(DisplayContent displayContent, SurfaceControl.Transaction transaction) {
    }

    default boolean imeTargetIsMainWindow(WindowState windowState) {
        return false;
    }

    default void initOplusRefreshRatePolicy(Object[] objArr) {
    }

    default boolean interceptPointerLocationEnable(DisplayContent displayContent) {
        return false;
    }

    default boolean isActivityPreloadDisplay(DisplayContent displayContent) {
        return false;
    }

    default boolean isCommercialVersion() {
        return false;
    }

    default boolean isFlexibleTaskAndAdjustIme(WindowState windowState) {
        return false;
    }

    default boolean isFoldExternalScreen(RootWindowContainer rootWindowContainer, int i) {
        return false;
    }

    default boolean isMirageDisplay() {
        return false;
    }

    default boolean isPuttDisplay() {
        return false;
    }

    default boolean isResolutionAnimating() {
        return false;
    }

    default boolean isReturnNoCutoutForFullScreenDisplay(int i, DisplayContent displayContent) {
        return false;
    }

    default boolean isSecondDisplay(DisplayContent displayContent) {
        return false;
    }

    default boolean isSupportedIMEOnSecondDisplay(DisplayContent displayContent) {
        return false;
    }

    default boolean isTwoScreenShown() {
        return false;
    }

    default boolean isZoomWindowMode(WindowContainer windowContainer) {
        return false;
    }

    default void mayAddFloatingWindow(WindowState windowState) {
    }

    default void notifyIMELayoutChanged(boolean z, int i, int i2) {
    }

    default void notifyInsetsChangedLw() {
    }

    default void onAppTransitionDone() {
    }

    default void onConfigurationChanged(Configuration configuration) {
    }

    default void onDisplayFocusedAppChanged(DisplayContent displayContent, ActivityRecord activityRecord) {
    }

    default void onFindFocusedWindow() {
    }

    default boolean onGoingToSleep(int i) {
        return true;
    }

    default void onPointerEventForTheia(MotionEvent motionEvent) {
    }

    default boolean performLayoutNoTrace(DisplayPolicy displayPolicy, DisplayFrames displayFrames, int i) {
        return false;
    }

    default void physicalDisplayChanged(DisplayContent displayContent) {
    }

    default void physicalDisplayChangedAfterConfig(DisplayContent displayContent) {
    }

    default void physicalDisplayChangedForFlexibleWindow(int i, int i2, int i3) {
    }

    default boolean pointWithinAppWindow(int i, int i2) {
        return false;
    }

    default void positionAnimation(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
    }

    default void removeImeSurfaceImmediately(DisplayContent displayContent, WindowState windowState) {
    }

    default void removeRefreshRateRangeForPackage(String str) {
    }

    default boolean requestSeamlessExplicit() {
        return false;
    }

    default void requestTraversalWhenAsyncRotationFinished() {
    }

    default void savedSurface(WindowManagerService windowManagerService) {
    }

    default void scheduleFoldableDeviceDisplaySwitch(boolean z, ActivityTaskManagerService activityTaskManagerService, WindowManagerService windowManagerService, DisplayAddress displayAddress) {
    }

    default void setAnimationThreadUx(boolean z, boolean z2, int i) {
    }

    default void setFixedRotationForScreenshot(ActivityRecord activityRecord, int i) {
    }

    default void setFlexibleActivityImeAnimationState(Object obj) {
    }

    default boolean setFocusedAppToNormalWindow(ActivityRecord activityRecord, WindowState windowState) {
        return false;
    }

    default void setForcedDisplayInfoForWmSize(int i, int i2, int i3, int i4, WindowManagerService windowManagerService) {
    }

    default void setInputMethodWindow(WindowState windowState, WindowState windowState2) {
    }

    default void setLastImeLayeringTarget(WindowState windowState) {
    }

    default void setPuttDisplay(boolean z) {
    }

    default void setResolutionAnimating(boolean z) {
    }

    default void setRotationChange(DisplayContent displayContent, boolean z) {
    }

    default void setSecondDefaultDisplay(DisplayContent displayContent, WindowManagerService windowManagerService) {
    }

    default void setVendorPreferredRefreshRate(IWindowStateExt iWindowStateExt, WindowState windowState) {
    }

    default boolean shouldBlockUpdateOrientationDuringFixedRotation(WindowContainer windowContainer, DisplayContent displayContent) {
        return false;
    }

    default boolean shouldCancelRecentAnimation(DisplayContent displayContent) {
        return true;
    }

    default boolean shouldDisableRecentsTransition(WindowContainerTransaction windowContainerTransaction, int i, DisplayContent displayContent) {
        return false;
    }

    default boolean shouldDisplayRotated(int i, String str, DisplayContent displayContent) {
        return i == 1 || i == 3;
    }

    default boolean shouldExitFixedRotation(DisplayContent displayContent, ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldFixOrientationForSplashScreen(ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldNotWaitForDisplayOnBoot(DisplayContent displayContent) {
        return false;
    }

    default boolean shouldPuttFixedRotation(DisplayContent displayContent, ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldReviseScreenOrientationForApp(ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldSetFixedRotationForTargetLaunchingApp(ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        return true;
    }

    default boolean shouldSkipUnFreezeCheck(WindowState windowState) {
        return false;
    }

    default boolean skipAppTransitionAnimation() {
        return false;
    }

    default boolean skipDeferOrientationChangeForEnteringPipFromFullScreen() {
        return false;
    }

    default boolean skipTransitionAnimationIfNeed(int i, TransitionRequestInfo.DisplayChange displayChange, TransitionController transitionController, ActivityRecord activityRecord) {
        return false;
    }

    default boolean startKeyguardExitOnNonAppWindows(WindowState windowState, boolean z, boolean z2, boolean z3) {
        return false;
    }

    default boolean suggestUseFixedRotationAnimation(DisplayContent displayContent, ActivityRecord activityRecord) {
        return false;
    }

    default boolean supportDesktopModeOnExternalDisplays(DisplayContent displayContent) {
        return false;
    }

    default void triggerIntoComapct() {
    }

    default boolean updateImeTarget(WindowState windowState) {
        return true;
    }

    default boolean updateOrientation(Configuration configuration, Configuration configuration2, ActivityRecord activityRecord, WindowContainer windowContainer, DisplayRotation displayRotation) {
        return false;
    }

    default void updateRotation(DisplayContent displayContent, boolean z) {
    }

    default boolean updateRotationUnchecked(boolean z) {
        return false;
    }

    default void updateWindowTapExcludeRegion(DisplayContent displayContent, Region region) {
    }

    default boolean waitingPhysicalDisplayChanged(DisplayContent displayContent) {
        return false;
    }

    default DisplayPolicy createDisplayPolicy(WindowManagerService windowManagerService, DisplayContent displayContent) {
        return new DisplayPolicy(windowManagerService, displayContent);
    }

    default boolean isAnimating(WindowToken windowToken, DisplayRotation displayRotation) {
        return windowToken.isAnimating(3);
    }
}
