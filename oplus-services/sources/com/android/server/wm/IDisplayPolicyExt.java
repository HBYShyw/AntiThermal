package com.android.server.wm;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.InsetsFrameProvider;
import android.view.InsetsState;
import android.view.WindowManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayPolicyExt {
    default void addTaskBar(WindowState windowState, boolean z) {
    }

    default int adjustAppHeightForCarDockBar(DisplayContent displayContent, int i, int i2) {
        return i;
    }

    default int adjustCutoutModeForWinIfNeed(WindowState windowState, DisplayContent displayContent, int i) {
        return i;
    }

    default boolean adjustNavigationBarToBottom(DisplayContent displayContent) {
        return false;
    }

    default void adjustOppoWindowFrame(Rect rect, Rect rect2, WindowManager.LayoutParams layoutParams, DisplayFrames displayFrames) {
    }

    default InsetsState adjustTaskBarInsetsState(InsetsState insetsState, WindowState windowState, int i) {
        return insetsState;
    }

    default void adjustWindowParamsLw(WindowState windowState, WindowManager.LayoutParams layoutParams) {
    }

    default int calculateGestureNavInset(int i, int i2) {
        return i;
    }

    default boolean canBeTopFullscreenOpqWin(WindowState windowState) {
        return true;
    }

    default boolean checkWindowForSimulateLayoutDisplay(WindowState windowState) {
        return true;
    }

    default Handler createPolicyHandler(Looper looper, Handler handler) {
        return handler;
    }

    default void finishScreenTurningOn() {
    }

    default int getInvalidNavigationBarHeight(int i) {
        return 0;
    }

    default int getLastNavBarVisibility() {
        return 0;
    }

    default int getNavBarVisibility(boolean z, boolean z2, boolean z3) {
        return 0;
    }

    default Object getOplusDisplayPolicyInner() {
        return null;
    }

    default Handler getOplusUIHandler(Handler handler) {
        return handler;
    }

    default int getSystemUIFlagAfterGesture(int i) {
        return i;
    }

    default WindowState getTaskBar() {
        return null;
    }

    default int getTaskBarHeight() {
        return 0;
    }

    default int getTaskBarPolicy(WindowState windowState, int i) {
        return 0;
    }

    default float getZoomCurrentScale() {
        return 1.0f;
    }

    default boolean hasRotationLock() {
        return false;
    }

    default void initOplusDisplayPolicy(DisplayPolicy displayPolicy) {
    }

    default void initOplusDisplayPolicyEx(WindowManagerService windowManagerService, DisplayPolicy displayPolicy) {
    }

    default boolean intersectInCompactWindow(WindowState windowState, Rect rect, Rect rect2) {
        return false;
    }

    default boolean isDisableExpendNavBar() {
        return false;
    }

    default boolean isDisableExpendStatusBar() {
        return false;
    }

    default boolean isDreamWindow(boolean z) {
        return false;
    }

    default boolean isFlexibleTaskIgnoreSysBar(WindowState windowState) {
        return false;
    }

    default boolean isHideNavBarGestureMode() {
        return false;
    }

    default boolean isInPocketStudio(int i) {
        return false;
    }

    default boolean isKeyboardPositionUp() {
        return false;
    }

    default boolean isMinimized(DisplayContent displayContent) {
        return false;
    }

    default boolean isNavBarHidden() {
        return false;
    }

    default boolean isNavGestureMode() {
        return false;
    }

    default boolean isNeedAdjustDisplayCutoutInsets(DisplayContent displayContent, WindowState windowState, WindowManager.LayoutParams layoutParams) {
        return false;
    }

    default boolean isNeedForceShowSystemBarsWhenSplit() {
        return true;
    }

    default Rect isSettingDialog(WindowState windowState) {
        return null;
    }

    default boolean isSpecialAppWindow(boolean z, WindowManager.LayoutParams layoutParams) {
        return z;
    }

    default boolean isSplitTaskVisible(DisplayContent displayContent) {
        return false;
    }

    default boolean isWaitForExitSplit() {
        return false;
    }

    default boolean judgeWindowModeZoom(WindowState windowState) {
        return false;
    }

    default Rect layoutInFullScreen(WindowState windowState, Rect rect) {
        return rect;
    }

    default void loadGestureBarHeight(Resources resources, int i, int i2, int i3, int i4) {
    }

    default boolean makeStatusBarOpaque(DisplayContent displayContent) {
        return false;
    }

    default int modifyNaviBar(int i) {
        return i;
    }

    default void notifyWindowStateChanged(int i, int i2, int i3) {
    }

    default void onTopFullscreenOpaqueWindowUpdated(DisplayPolicy displayPolicy, WindowState windowState) {
    }

    default boolean opaqueNavBar(WindowState windowState) {
        return false;
    }

    default void pokeDynamicVsyncAnimation(int i, String str) {
    }

    default boolean requestGameDockIfNecessary() {
        return false;
    }

    default boolean restrictFullScreenActivityRectInCompactWindow(WindowState windowState, int i, int i2, int i3) {
        return false;
    }

    default void setLastNavBarVisibility(int i) {
    }

    default void setSystemUiVisibility(int i, WindowState windowState) {
    }

    default void setTaskBarAppBoundsIfNeed(Configuration configuration, Configuration configuration2, int i) {
    }

    default boolean shouldNoFocusWindowUpdateSystemBarAttributes(WindowState windowState) {
        return false;
    }

    default boolean skipSystemUiVisibility(WindowManager.LayoutParams layoutParams) {
        return false;
    }

    default void updateBottomGestureAdditionalInset() {
    }

    default void updateDisplayConfig() {
    }

    default void updateFrameProvider(DisplayFrames displayFrames, WindowState windowState, Rect rect, WindowManager.LayoutParams layoutParams, InsetsFrameProvider insetsFrameProvider) {
    }

    default void updateGestureStatus() {
    }

    default boolean updateImeSourceFrame(WindowState windowState, Rect rect) {
        return false;
    }

    default void updateNavigationBarHideState() {
    }

    default void updateNavigationFrame(int i, int i2, Rect rect, Rect rect2) {
    }

    default boolean updateSpecialSystemBar(WindowManager.LayoutParams layoutParams) {
        return false;
    }

    default void updateTaskBarAppearanceIfNeed(WindowState windowState) {
    }

    default Rect getZoomRectBeforeShowIME() {
        return new Rect();
    }
}
