package com.android.server.wm;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.InsetsFrameProvider;
import android.view.WindowManager;
import com.android.server.statusbar.StatusBarManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusDisplayPolicyEx extends IOplusCommonFeature {
    public static final IOplusDisplayPolicyEx DEFAULT = new IOplusDisplayPolicyEx() { // from class: com.android.server.wm.IOplusDisplayPolicyEx.1
    };
    public static final String NAME = "IOplusDisplayPolicyEx";

    default int caculateDisplayFrame(int i, Rect rect, int i2) {
        return i2;
    }

    default int calculateGestureNavInset(int i, int i2) {
        return i;
    }

    default int getNavBarVisibility(boolean z, boolean z2, boolean z3) {
        return 0;
    }

    default int getSysUiFlagsForSplitScreen(WindowState windowState, int i) {
        return i;
    }

    default boolean isDisableExpendNavBar() {
        return false;
    }

    default boolean isDisableExpendStatusBar() {
        return false;
    }

    default boolean isForceShowNavbar() {
        return false;
    }

    default boolean isGameDockWindow(WindowState windowState) {
        return false;
    }

    default boolean isHideNavBarGestureMode() {
        return false;
    }

    default boolean isIncludedNextPage(WindowState windowState) {
        return false;
    }

    default boolean isKeyboardPositionUp() {
        return false;
    }

    default boolean isNavBarHidden() {
        return false;
    }

    default boolean isNavBarImmersive() {
        return false;
    }

    default boolean isNavGestureMode() {
        return false;
    }

    default void layoutGameDockWindowLw(DisplayFrames displayFrames, Rect rect, Rect rect2, Rect rect3, Rect rect4) {
    }

    default void loadGestureBarHeight(Resources resources, int i, int i2, int i3, int i4) {
    }

    default void notifyWindowStateChange(int i) {
    }

    default void notifyWindowStateChanged(int i, int i2, int i3) {
    }

    default void onMayUseInputMethod(boolean z) {
    }

    default void setForceShowNavbar(boolean z) {
    }

    default void setSystemUiVisibility(int i, WindowState windowState) {
    }

    default void setSystemUiVisibility(StatusBarManagerInternal statusBarManagerInternal, int i, int i2, int i3, int i4, int i5, Rect rect, Rect rect2, boolean z, WindowState windowState, int i6) {
    }

    default void systemBarStateChange(int i, boolean z, boolean z2) {
    }

    default void updateDisplayConfig() {
    }

    default void updateFrameProvider(DisplayFrames displayFrames, WindowState windowState, Rect rect, WindowManager.LayoutParams layoutParams, InsetsFrameProvider insetsFrameProvider) {
    }

    default void updateGestureStatus() {
    }

    default void updateKeyboardPosition() {
    }

    default void updateKeyboardPosition(boolean z) {
    }

    default void updateNavigationBarHideState() {
    }

    default void updateNavigationFrame(int i, int i2, Rect rect, Rect rect2) {
    }

    default boolean willSkipSystemUI(String str) {
        return false;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusDisplayPolicyEx;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean isWindowTitleEquals(WindowState windowState, String str) {
        return windowState != null && windowState.getAttrs().getTitle().equals(str);
    }
}
