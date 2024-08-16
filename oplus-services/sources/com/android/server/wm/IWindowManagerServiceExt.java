package com.android.server.wm;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.view.DisplayInfo;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.WindowManager;
import com.android.server.input.InputManagerService;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.wm.EmbeddedWindowController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowManagerServiceExt {
    public static final String SPECIAL_HANDLING_WIN = "android.view.cts/android.view.cts.HandleConfigurationActivity";

    default void addSplitScreenImmersiveFlagIfNeed(WindowState windowState, Bundle bundle) {
    }

    default void addWindow(WindowState windowState) {
    }

    default int adjustDensityForUser(int i, int i2) {
        return i;
    }

    default void allWindowsDraw() {
    }

    default void beginHookscreenTurningOff() {
    }

    default boolean canAddSubWindow(Context context, WindowManager.LayoutParams layoutParams) {
        return false;
    }

    default boolean canShowInLockDeviceMode(int i) {
        return true;
    }

    default boolean changeFocusForce(WindowState windowState) {
        return false;
    }

    default boolean checkExitingAnimationRationality(WindowState windowState) {
        return true;
    }

    default boolean checkOplusWindowPermission(WindowManagerService windowManagerService) {
        return false;
    }

    default void checkScreenFreezingTimeOut(boolean z) {
    }

    default void clearSavedSurfaceIfNeeded(WindowState windowState, ArrayList<WindowState> arrayList, int i, boolean z) {
    }

    default void clearSkipWaitingForDrawn() {
    }

    default void clearSwitchPhysicalDisplayFlag(WindowContainer windowContainer) {
    }

    default void cpuFrequencyBoostIfNeed(ActivityRecord activityRecord) {
    }

    default boolean currentFoucusWindowModeNotZoomMode(int i) {
        return true;
    }

    default boolean doDump(WindowManagerService windowManagerService, String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i) {
        return false;
    }

    default boolean doDumpWindows(PrintWriter printWriter, String str, String[] strArr, int i, boolean z) {
        return true;
    }

    default boolean dontWaitDrawForCompactWindow(WindowState windowState) {
        return false;
    }

    default boolean dontWaitDrawForFlexibleWindow(WindowState windowState) {
        return false;
    }

    default void dump(PrintWriter printWriter, String[] strArr) {
    }

    default void enableDefaultLogIfNeed(Context context) {
    }

    default void endHookSystemReady() {
    }

    default void endHookperformEnableScreen(WindowManagerService windowManagerService, Context context) {
    }

    default void endHookstopFreezingDisplayLocked(String str) {
    }

    default int extractConfigInfoAndRealFlags(int i, WindowState windowState) {
        return i;
    }

    default int extractSwitchPhysicalDisplayFlag(int i, WindowState windowState) {
        return i;
    }

    default ArrayList<WindowState> getDestroySavedSurface() {
        return null;
    }

    default DisplayInfo getNeedForceSetDensityDisplayInfo(WindowManagerService windowManagerService, int i, int i2) {
        return null;
    }

    default WindowManagerService getOplusWindowManagerService(Context context, InputManagerService inputManagerService, boolean z, WindowManagerPolicy windowManagerPolicy, ActivityTaskManagerService activityTaskManagerService, DisplayWindowSettingsProvider displayWindowSettingsProvider, Supplier<SurfaceControl.Transaction> supplier, Function<SurfaceSession, SurfaceControl.Builder> function) {
        return null;
    }

    default void handleAppVisible(ActivityRecord activityRecord) {
    }

    default void handleCompactWindowTouchFocusChange(WindowState windowState) {
    }

    default void handleKeyguardGoingAway(boolean z) {
    }

    default void handleOplusMessage(Message message) {
    }

    default void handleUiModeChanged() {
    }

    default boolean hideForUnFolded(WindowState windowState) {
        return false;
    }

    default void hookAddWindowBeforeAttach(int i) {
    }

    default void hookDisplayReady(WindowManagerService windowManagerService, Context context) {
    }

    default Configuration hookRegisterWindowContainerListener(DisplayArea<?> displayArea, WindowContextListenerController windowContextListenerController, IBinder iBinder, int i, int i2, Bundle bundle) {
        return null;
    }

    default void hookboost(boolean z) {
    }

    default boolean ignoreFingerprintWindow(Context context, Task task) {
        return false;
    }

    default boolean interceptFloatWindow(WindowManagerService windowManagerService, Context context, WindowState windowState, boolean z, boolean z2) {
        return false;
    }

    default boolean isActivityTypeMultiSearch(Task task) {
        return false;
    }

    default void isFingerPrintDimLayerRequestHorizontalLayout(WindowState windowState, int i, int i2, int i3) {
    }

    default boolean isFocusChangeWithNonFlexible(WindowState windowState) {
        return false;
    }

    default boolean isGestureAnimationTarget(ActivityRecord activityRecord) {
        return false;
    }

    default boolean isGestureAnimationWapaperTarget(WindowState windowState) {
        return false;
    }

    default boolean isHomePageOfSettingsTaskFragment(InputTarget inputTarget, InputTarget inputTarget2) {
        return false;
    }

    default boolean isIMETargetWindowHasFocus(InputTarget inputTarget) {
        return true;
    }

    default boolean isNoCanvasActivity(ActivityRecord activityRecord) {
        return false;
    }

    default boolean isRotationLockForBootAnimation() {
        return false;
    }

    default boolean isSecondaryhomePackageName(EmbeddedWindowController.EmbeddedWindow embeddedWindow) {
        return false;
    }

    default boolean isShouldInterceptEnterPip() {
        return false;
    }

    default boolean isStartingSplitPairFromRecents() {
        return false;
    }

    default boolean isWindowSurfaceSaved(WindowState windowState) {
        return false;
    }

    default void logNoFocusedWindowANRState() {
    }

    default void notifySysWindowRotation(Class cls, ComponentName componentName) {
    }

    default void onPowerKeyDown(boolean z) {
    }

    default void onSetDensityForUser(int i, int i2) {
    }

    default void onStopFreezingDisplayLocked() {
    }

    default void oplusOnInitReady() {
    }

    default void pokeDynamicVsyncAnimation(int i, String str) {
    }

    default void removeWindow(WindowState windowState) {
    }

    default void setAnimThreadUxIfNeed(boolean z, WindowState windowState) {
    }

    default void setFrozenByUserSwitching(boolean z) {
    }

    default boolean setInputMethodWindow(DisplayContent displayContent, WindowState windowState) {
        return true;
    }

    default void setShouldInterceptEnterPip(boolean z) {
    }

    default boolean shouldCancelRelayout(WindowState windowState, int i, int i2) {
        return false;
    }

    default boolean shouldForceStopFreezingScreen() {
        return false;
    }

    default boolean shouldShowPresentation(DisplayContent displayContent, String str) {
        return false;
    }

    default boolean shouldSkipCheckWindowDrawn(WindowState windowState) {
        return false;
    }

    default boolean shouldSkipUnFreezeCheck(WindowState windowState) {
        return false;
    }

    default boolean shouldWindowSurfaceSaved(WindowState windowState, DisplayContent displayContent) {
        return false;
    }

    default boolean shouldwaitingForFolded() {
        return false;
    }

    default void showCustomizeWatermark(boolean z, Context context, DisplayContent displayContent, SurfaceControl.Transaction transaction) {
    }

    default void speedWallpaperShowIfNeeded(DisplayContent displayContent) {
    }

    default void tryAddActivityToAnimationSourceWhenStartExitingAnimation(WindowState windowState) {
    }

    default void updateBracketPanelWindow(WindowState windowState, boolean z) {
    }
}
