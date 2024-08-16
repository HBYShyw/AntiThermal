package com.android.server.wm;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.InsetsFrameProvider;
import android.view.WindowManager;
import com.android.server.statusbar.StatusBarManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OplusDefaultDisplayPolicyEx implements IOplusDisplayPolicyEx {
    public static final String KEY_NAVIGATIONBAR_MODE = "hide_navigationbar_enable";
    public static final String KEY_NAV_BAR_HIDE_STATE = "manual_hide_navigationbar";
    public static final String KEY_NAV_BAR_IMMERSIVE = "nav_bar_immersive";
    public static final String KEY_SWIPE_SIDE_GESTURE_BAR_TYPE = "gesture_side_hide_bar_prevention_enable";
    protected static final int MODE_NAVIGATIONBAR = 0;
    protected static final int MODE_NAVIGATIONBAR_GESTURE = 2;
    protected static final int MODE_NAVIGATIONBAR_GESTURE_SIDE = 3;
    protected static final int MODE_NAVIGATIONBAR_NONE = -1;
    protected static final int MODE_NAVIGATIONBAR_WITH_HIDE = 1;
    public static final int NAV_BAR_HIDE_STATE_HIDE = 1;
    public static final int NAV_BAR_HIDE_STATE_NONE = -1;
    public static final int NAV_BAR_HIDE_STATE_SHOW = 0;
    public static final int SWIPE_SIDE_GESTURE_BAR_TYPE_HIDE = 1;
    public static final int SWIPE_SIDE_GESTURE_BAR_TYPE_SUSPEND = 0;
    public static final String SYSTEM_BAR_ID = "systembar_id";
    public static final String SYSTEM_BAR_STATE = "systembar_state";
    public static final String SYSTEM_DISPLAY_ID = "system_display_id";
    public static final int SYSTEM_UI_FLAG_FOCUS_TOP_OR_LEFT = 64;
    protected static final Rect mTmpNavigationFrameForGesture = new Rect();
    final DisplayPolicy mDisplayPolicy;
    protected int mLastWindowFocusFlags;
    IOplusDisplayPolicyInner mOplusDpInner;
    protected OplusWindowManagerInternal mOplusWindowManagerInternal;
    protected int mSideGestureHideState = 0;
    protected int mLastSideGestureHideState = -1;
    protected int mNavigationBarMode = 0;
    protected int mLastNavigationBarMode = -1;
    protected int mNavigationBarHideState = 0;
    protected int mLastNavigationBarHideState = -1;
    protected boolean mHideNavigationBar = false;
    protected boolean mIsNavBarImmersive = false;
    protected int[] mNavigationBarHeightForRotationGestrue = new int[4];
    protected int mLastNavigationBarState = 0;
    protected int mLastStatusBarState = 0;

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public int caculateDisplayFrame(int i, Rect rect, int i2) {
        return i2;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public int calculateGestureNavInset(int i, int i2) {
        return i;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public int getNavBarVisibility(boolean z, boolean z2, boolean z3) {
        return 0;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public int getSysUiFlagsForSplitScreen(WindowState windowState, int i) {
        return i;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isForceShowNavbar() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isGameDockWindow(WindowState windowState) {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isHideNavBarGestureMode() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isIncludedNextPage(WindowState windowState) {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isKeyboardPositionUp() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isNavBarHidden() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isNavBarImmersive() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isNavGestureMode() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void layoutGameDockWindowLw(DisplayFrames displayFrames, Rect rect, Rect rect2, Rect rect3, Rect rect4) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void loadGestureBarHeight(Resources resources, int i, int i2, int i3, int i4) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void notifyWindowStateChange(int i) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void notifyWindowStateChanged(int i, int i2, int i3) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void onMayUseInputMethod(boolean z) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void setForceShowNavbar(boolean z) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void setSystemUiVisibility(int i, WindowState windowState) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void setSystemUiVisibility(StatusBarManagerInternal statusBarManagerInternal, int i, int i2, int i3, int i4, int i5, Rect rect, Rect rect2, boolean z, WindowState windowState, int i6) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void systemBarStateChange(int i, boolean z, boolean z2) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateDisplayConfig() {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateFrameProvider(DisplayFrames displayFrames, WindowState windowState, Rect rect, WindowManager.LayoutParams layoutParams, InsetsFrameProvider insetsFrameProvider) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateGestureStatus() {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateKeyboardPosition() {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateKeyboardPosition(boolean z) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateNavigationBarHideState() {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateNavigationFrame(int i, int i2, Rect rect, Rect rect2) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean willSkipSystemUI(String str) {
        return false;
    }

    public OplusDefaultDisplayPolicyEx(DisplayPolicy displayPolicy) {
        this.mDisplayPolicy = displayPolicy;
        this.mOplusDpInner = (IOplusDisplayPolicyInner) displayPolicy.getWrapper().getExtImpl().getOplusDisplayPolicyInner();
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isWindowTitleEquals(WindowState windowState, String str) {
        return windowState != null && windowState.getAttrs().getTitle().equals(str);
    }
}
