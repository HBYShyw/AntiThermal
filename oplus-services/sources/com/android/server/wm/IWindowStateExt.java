package com.android.server.wm;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.view.InsetsState;
import android.view.RemoteAnimationDefinition;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.view.animation.Animation;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowStateExt {
    default boolean adjustPosForComapctWindow(WindowState windowState) {
        return false;
    }

    default void adjustTouchableRegionInActivityEmbedding(WindowState windowState, Rect rect) {
    }

    default void attach(WindowState windowState) {
    }

    default boolean blockSeamlesslyRotateForFingerPrintWindow(WindowState windowState) {
        return false;
    }

    default boolean canBeImeTarget(WindowState windowState) {
        return true;
    }

    default boolean canInitAppOpVisibilityLw(String str, int i, int i2) {
        return true;
    }

    default boolean canOverlayWindows() {
        return false;
    }

    default boolean canSetAppOpVisibilityLw(String str, int i) {
        return true;
    }

    default boolean canShowInLockDeviceMode(int i) {
        return true;
    }

    default void cancelFadeAnimationIfNeed(WindowState windowState) {
    }

    default void cancelFlexibleAppInnerScreenAnimationIfNeed(WindowState windowState) {
    }

    default void cancelSplashScreenAnimation(WindowState windowState) {
    }

    default void changeStartingWindowParentBounds(WindowState windowState, Rect rect) {
    }

    default boolean checkIfHasDrawn(WindowState windowState) {
        return false;
    }

    default boolean checkIfWindowingModeZoom(int i) {
        return false;
    }

    default void createCompactDimmer(WindowState windowState) {
    }

    default void dispatchWallpaperVisibility(boolean z) {
    }

    default boolean excludeDimmerForComponent(ActivityRecord activityRecord) {
        return false;
    }

    default void expandFingerPrintDimLayerSurface(WindowState windowState, boolean z) {
    }

    default boolean finishDrawing(boolean z) {
        return z;
    }

    default boolean finishDrawingApplyPostDraw(WindowState windowState, SurfaceControl.Transaction transaction) {
        return false;
    }

    default boolean forceUpdateWallpaperOffset(WindowState windowState) {
        return false;
    }

    default boolean getAppOpVisibility() {
        return false;
    }

    default float getCompactScaled(WindowState windowState) {
        return -1.0f;
    }

    default InsetsState getCompatInsetsStateForSplit(WindowState windowState, InsetsState insetsState) {
        return insetsState;
    }

    default boolean getDeviceFolding() {
        return false;
    }

    default int getFixedScreenOrientation() {
        return -1;
    }

    default boolean getHideByKeyguardExitAnim() {
        return false;
    }

    default boolean getIsSwitchingPhysicalDisplay() {
        return false;
    }

    default int getLastClientRotation() {
        return -1;
    }

    default int getLastFinishDrawDp() {
        return 0;
    }

    default Object getOplusLaunchViewInfo() {
        return null;
    }

    default RemoteAnimationDefinition getRemoteAnimationDefinitionExt() {
        return null;
    }

    default boolean getWindowRelayoutFlag() {
        return false;
    }

    default boolean hideForUnFolded(WindowState windowState) {
        return false;
    }

    default boolean hookCanReceiveKeys(int i, WindowStateAnimator windowStateAnimator) {
        return false;
    }

    default InsetsState hookGetCompatInsetsState(InsetsState insetsState) {
        return insetsState;
    }

    default InsetsState hookGetInsetsState(InsetsState insetsState, boolean z) {
        return insetsState;
    }

    default float hookOverrideScale(WindowState windowState, WindowState windowState2, float f) {
        return f;
    }

    default boolean hookRequestDrawIfNeeded(WindowState windowState, int i, List<WindowState> list) {
        return false;
    }

    default boolean inRemapViceDisplay(WindowState windowState) {
        return false;
    }

    default void initColorDisplayCompat(String str, WindowState windowState) {
    }

    default boolean isCompactScaledWindowingMode(WindowState windowState) {
        return false;
    }

    default boolean isCompactWindowingMode(int i) {
        return false;
    }

    default boolean isDisplayCompat() {
        return false;
    }

    default boolean isDisplayCompat(String str, int i) {
        return false;
    }

    default boolean isDisplayHideFullscreenButtonNeeded() {
        return false;
    }

    default boolean isFlexibleTaskInTransitionAnimation(WindowState windowState) {
        return false;
    }

    default boolean isIgnoreImeTargetBottomOverlapFlexibleTask(WindowState windowState) {
        return false;
    }

    default boolean isInSkipWaitingForDrawn(WindowState windowState) {
        return false;
    }

    default boolean isLastInputmethodShow() {
        return false;
    }

    default boolean isMinimizedPocketStudio() {
        return false;
    }

    default boolean isMirageDisplay(int i) {
        return false;
    }

    default boolean isNoMoveAnimationOnFlexibleWindow() {
        return false;
    }

    default boolean isNotFullScreenCompactWindow(WindowState windowState) {
        return false;
    }

    default boolean isNotReadyForDisplayDuringFixedRotation(WindowState windowState, DisplayContent displayContent, Rect rect) {
        return false;
    }

    default boolean isOnMirageDisplay(WindowState windowState) {
        return false;
    }

    default boolean isOplusTrustedWindow(WindowManager.LayoutParams layoutParams) {
        return false;
    }

    default boolean isSyncFinished(WindowState windowState, int i, IWindowManagerServiceExt iWindowManagerServiceExt, ArrayList<WindowState> arrayList) {
        return false;
    }

    default boolean isTrustedOverlay() {
        return false;
    }

    default boolean isVisibleLw() {
        return false;
    }

    default boolean isWallpaperAndInTransition() {
        return false;
    }

    default boolean isWallpaperShow(WindowToken windowToken) {
        return false;
    }

    default boolean layoutFullscreenInEmbedding() {
        return false;
    }

    default Rect layoutInFullScreen(WindowState windowState, Rect rect) {
        return rect;
    }

    default boolean letterBoxEnabledForCompactWin(WindowState windowState) {
        return true;
    }

    default boolean needLetterBoxSurface(boolean z, ActivityRecord activityRecord, WindowState windowState) {
        return false;
    }

    default void needResetDrawStateOnResize(WindowState windowState, Rect rect) {
    }

    default void notifyGameFloatWindowVisibility(boolean z, WindowState windowState) {
    }

    default void notifyImeWindowStateChange(boolean z, WindowState windowState) {
    }

    default void notifyWindowStateChange(Bundle bundle) {
    }

    default void onDisplayChanged(DisplayContent displayContent, DisplayContent displayContent2, WindowState windowState) {
    }

    default void onDisplayChangedEnd(InputWindowHandleWrapper inputWindowHandleWrapper) {
    }

    default void onDisplayImeChanged(DisplayContent displayContent, DisplayContent displayContent2, WindowState windowState) {
    }

    default void onNonAppSurfaceVisibilityChanged(boolean z) {
    }

    default void onWindowStateCreated(WindowState windowState) {
    }

    default void onWindowStateHasDrawn(WindowState windowState) {
    }

    default void performShowLocked(WindowState windowState) {
    }

    default boolean prepareSurfaces(WindowState windowState) {
        return false;
    }

    default boolean providesDisplayDecorInsetsForTaskbar(WindowState windowState) {
        return true;
    }

    default void putSnapshotWhenStartingWindowExit(int i, boolean z, WindowState windowState) {
    }

    default void registerRemoteAnimationsExt(RemoteAnimationDefinition remoteAnimationDefinition) {
    }

    default void removeImmediately(WindowState windowState) {
    }

    default void removeStartingBackColorLayerIfNeed(WindowState windowState) {
    }

    default void resizeTouchRegionForCompactWindow(ActivityRecord activityRecord, WindowFrames windowFrames, Region region, WindowState windowState) {
    }

    default void resizeTouchRegionForSpecial(ActivityRecord activityRecord, WindowFrames windowFrames, Region region, WindowState windowState) {
    }

    default void resizeTouchableRegionForBracketMode(Region region, ActivityRecord activityRecord, WindowState windowState) {
    }

    default void resizeTouchableRegionForBracketPanelWindow(Region region, WindowState windowState) {
    }

    default void resizeTouchableRegionInOplusCompatMode(WindowState windowState, Region region) {
    }

    default void setCurrentLaunchCanTurnScreenOn(ActivityRecord activityRecord) {
    }

    default void setDisplayCompat(boolean z) {
    }

    default void setFixedScreenOrientation(int i) {
    }

    default void setHideByKeyguardExitAnim(boolean z) {
    }

    default void setInputmethodShow(boolean z) {
    }

    default void setIsSwitchingPhysicalDisplay(boolean z) {
    }

    default void setLastClientRotation(int i) {
    }

    default void setLastFinishDrawDp(int i) {
    }

    default void setLayoutFullscreenInEmbeddingIfNeed() {
    }

    default void setOplusLaunchViewInfo(Object obj) {
    }

    default void setSimultaneousDisplayState(boolean z) {
    }

    default void setToInputMethodWindow() {
    }

    default void setTrustedOverlay(boolean z) {
    }

    default void setWindowRelayoutFlag(boolean z) {
    }

    default void setmDisplayHideFullscreenButton(boolean z) {
    }

    default boolean shouldAddSettingsWindowToA11y(WindowState windowState) {
        return false;
    }

    default boolean shouldBlockWindowMoveAnimation(WindowState windowState) {
        return false;
    }

    default boolean shouldDeferCallOnFirstWindowDrawn(WindowState windowState) {
        return false;
    }

    default boolean shouldRelativeLayerInSplitScreenMode(WindowState windowState) {
        return true;
    }

    default boolean shouldShowLetterboxUi(WindowState windowState) {
        return false;
    }

    default boolean shouldSkipFreezingWhenFolding(WindowState windowState) {
        return false;
    }

    default boolean shouldSkipResizeStartingWindow(WindowManager.LayoutParams layoutParams) {
        return false;
    }

    default boolean shouldUpdateWinPos(WindowFrames windowFrames) {
        return false;
    }

    default boolean startAnimationWithRoundedCorners(WindowState windowState, Animation animation, Point point, Rect rect) {
        return false;
    }

    default boolean supportTransWindowAnim(WindowState windowState, WindowFrames windowFrames) {
        return false;
    }

    default boolean syncEmbeddedWindowDrawStateIfNeeded(WindowState windowState) {
        return false;
    }

    default boolean toUpdateCompactDimmer(WindowState windowState) {
        return false;
    }

    default boolean translateTouchableRegionInOplusCompatMode(WindowState windowState, Region region) {
        return false;
    }

    default void unregisterRemoteAnimationsExt() {
    }

    default void updateAttrsBeforeCompute(WindowManager.LayoutParams layoutParams) {
    }

    default void updateCompactStartingWindowFrames(WindowState windowState, Rect rect, Rect rect2) {
    }

    default void updateOrientationChangeIfNeeded(WindowState windowState, ActivityRecord activityRecord, WindowManagerService windowManagerService) {
    }

    default void updateSFPosWithNextSync() {
    }

    default void updateWindowState(WindowState windowState, Session session, WindowStateAnimator windowStateAnimator, int i, boolean z) {
    }

    default void wakeupInPrepareWindowToDisplayDuringRelayout(String str) {
    }

    default boolean wallpaperSeamlesslyRotate(WindowState windowState) {
        return false;
    }
}
