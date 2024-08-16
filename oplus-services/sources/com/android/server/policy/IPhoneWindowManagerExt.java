package com.android.server.policy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import com.android.internal.policy.PhoneWindow;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.wm.DisplayRotation;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPhoneWindowManagerExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Callback {
        String getName();

        default void onInterceptKeyBeforeQueueing(KeyEvent keyEvent, boolean z, int i, int i2) {
        }

        default void updateSettings(Context context) {
        }
    }

    default boolean addCallback(Callback callback) {
        return false;
    }

    default void adjustBrightnessUpOrDownEvent(int i, int i2) {
    }

    default boolean applyKeyguardOcclusionChange(boolean z) {
        return false;
    }

    default boolean checkStartingWindowBackground(Drawable drawable) {
        return drawable != null;
    }

    default void clearHwShutdownDectect() {
    }

    default void extraWorkInCancelPendingPowerKeyAction() {
    }

    default void finishScreenTurningOn(int i) {
    }

    default boolean finishWindowsDrawn(int i) {
        return false;
    }

    default boolean getBlackScreenWindowManagerPowerKeyState() {
        return false;
    }

    default Context getContext() {
        return null;
    }

    default boolean getDisplayEnable(boolean z) {
        return false;
    }

    default IInputExt getInputExtension() {
        return null;
    }

    default int getKeyMode() {
        return 0;
    }

    default int getLaunchMode(Message message) {
        return -1;
    }

    default PhoneWindowManager getPhoneWindowManager() {
        return null;
    }

    default boolean getSpeechLongPressHandle() {
        return false;
    }

    default void getTpInfo(String str, String str2) {
    }

    default void handleStartingWindow(PhoneWindow phoneWindow) {
    }

    default void handleStartingWindowBackground(PhoneWindow phoneWindow) {
    }

    default void hookForInit() {
    }

    default void hookForInputLogV(String str) {
    }

    default long hookScreenshotChordLongPressDelay() {
        return 0L;
    }

    default boolean interceptKeyEventForAppShareModeIfNeed(KeyEvent keyEvent) {
        return false;
    }

    default boolean interceptPowerKeyDown() {
        return false;
    }

    default void interceptPowerKeyUp(boolean z, WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
    }

    default boolean interceptRingerChordGesture() {
        return false;
    }

    default void interceptScreenshotChord() {
    }

    default boolean isCameraGestureEnabled() {
        return false;
    }

    default boolean isCustomize() {
        return false;
    }

    default boolean isDisplaysOnLocked(Display display) {
        return false;
    }

    default boolean isGlobalActionVisible() {
        return false;
    }

    default boolean isMenuLongPressed() {
        return false;
    }

    default boolean isPowerButtonFpSensor() {
        return false;
    }

    default boolean isSecondScreenOn() {
        return false;
    }

    default boolean isSleepByPowerButtonDisabled() {
        return false;
    }

    default boolean isTargetUserUnlocked(int i) {
        return true;
    }

    default void keyEventSpendTimeEventLog(long j) {
    }

    default void notePowerkeyProcessEvent(String str, boolean z, boolean z2) {
    }

    default void notePowerkeyProcessStagePoint(String str) {
    }

    default boolean notifyPowerKeyPressed(String str) {
        return false;
    }

    default void onBackPressedOnTheiaMonitor(KeyEvent keyEvent) {
    }

    default void onPowerKeyPressedOnTheiaMonitor(KeyEvent keyEvent) {
    }

    default void onPwkPressed() {
    }

    default void onPwkReleased() {
    }

    default void onRecentClicked() {
    }

    default void onScreenShotKeyPressedOnTheiaMonitor() {
    }

    default void onSpecialKeyPressedOnTheiaMonitor(KeyEvent keyEvent) {
    }

    default void oplusHandleAssistLaunchMode(int i, Bundle bundle) {
    }

    default boolean oplusInterceptAppSwitchEventBeforeQueueing(KeyEvent keyEvent, boolean z) {
        return false;
    }

    default boolean oplusInterceptLongHomePress() {
        return false;
    }

    default boolean oplusInterceptLongPowerPress() {
        return false;
    }

    default void oplusInterceptPowerKeyDown(KeyEvent keyEvent, boolean z) {
    }

    default void oplusInterceptPowerKeyForAlarm() {
    }

    default boolean oplusInterceptPowerKeyForTelephone(KeyEvent keyEvent, boolean z) {
        return false;
    }

    default void oplusInterceptPowerKeyUp(boolean z) {
    }

    default void oplusPowerPress(long j, boolean z, int i) {
    }

    default int oplusUpdateConfigurationDependentBehaviors(int i) {
        return i;
    }

    default int overrideCheckAddPermission(int i, boolean z, String str, int[] iArr) {
        return 0;
    }

    default void overrideDump(String str, PrintWriter printWriter, String[] strArr) {
    }

    default void overrideEnableScreenAfterBoot() {
    }

    default int overrideGetMaxWindowLayer() {
        return 0;
    }

    default int overrideGetWindowLayerFromTypeLw(int i, boolean z, boolean z2) {
        return 0;
    }

    default void overrideHideBootMessages() {
    }

    default void overrideInit(PhoneWindowManager phoneWindowManager, Context context, WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
    }

    default long overrideInterceptKeyBeforeDispatching(IBinder iBinder, KeyEvent keyEvent, int i) {
        return 0L;
    }

    default int overrideInterceptKeyBeforeQueueing(KeyEvent keyEvent, int i) {
        return 0;
    }

    default boolean overrideIsKeyguardShowingAndNotOccluded() {
        return false;
    }

    default void overrideOnDefaultDisplayFocusChangedLw(WindowManagerPolicy.WindowState windowState) {
    }

    default boolean overridePerformHapticFeedback(int i, String str, int i2, boolean z, String str2) {
        return false;
    }

    default void overrideScreenTurnedOff(int i, boolean z) {
    }

    default void overrideScreenTurningOn(int i, WindowManagerPolicy.ScreenOnListener screenOnListener) {
    }

    default void overrideSetCurrentUserLw(int i) {
    }

    default void overrideShowBootMessage(CharSequence charSequence, boolean z) {
    }

    default void overrideShowGlobalActionsInternal() {
    }

    default void overrideSystemBooted() {
    }

    default void overrideSystemReady() {
    }

    default void overrideUpdateSettings() {
    }

    default boolean removeCallback(Callback callback) {
        return false;
    }

    default void requestKeyguard(String str) {
    }

    default void resetDeviceFolded() {
    }

    default void screenTurnedOff(int i) {
    }

    default void screenTurnedOn(int i, WindowManagerPolicy.ScreenOnListener screenOnListener) {
    }

    default void sendBroadcastByCustomizeKey(String str) {
    }

    default boolean sendBroadcastForCombinationKeyGrabSystrace() {
        return false;
    }

    default void sendSpeechMessage(Long l) {
    }

    default void sendWindowDrawCompleteMsg(int i) {
    }

    default void sendWindowDrawCompleteMsgDelay(int i) {
    }

    default void setDisplayEnable(boolean z, boolean z2) {
    }

    default void setDynamicalLogEnable(boolean z) {
    }

    default void setSecondDefaultDisplay(WindowManagerPolicy.DisplayContentInfo displayContentInfo) {
    }

    default void setSwitchingTrackerKeyguardOndrawnEventLog() {
    }

    default void setSwitchingTrackerScreenTurningOnEventLog(boolean z) {
    }

    default boolean skipKeyguardOccludedCheck() {
        return false;
    }

    default boolean skipVolumeKeyIfNeeded() {
        return false;
    }

    default void startHwShutdownDectect() {
    }

    default void startedGoingToSleep() {
    }

    default void updateOrientationListener(int i) {
    }

    default void updateOrientationListenerAsyncIfNeeded(DisplayRotation displayRotation) {
        if (displayRotation != null) {
            displayRotation.updateOrientationListener();
        }
    }
}
