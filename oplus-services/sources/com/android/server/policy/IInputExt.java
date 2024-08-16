package com.android.server.policy;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.IWindowManager;
import android.view.KeyEvent;
import com.android.server.policy.WindowManagerPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IInputExt {
    public static final int ASSIST_MANAGER_LAUNCH_MODE_DEFAULT = 1;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_LENS = 2;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_PERSONAL_UPDATES = 5;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_UNKNOWN = 0;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_WALKIE_TALKIE_START = 3;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_WALKIE_TALKIE_STOP = 4;
    public static final int DEFAULT_LONG_PRESS_POWERON_DISPLAY_TIME = 2500;
    public static final int KEY_OFFSET_VALUE = 800;

    default void extraWorkInCancelPendingPowerKeyAction() {
    }

    default int getKeyMode() {
        return -1;
    }

    default boolean getSpeechLongPressHandle() {
        return false;
    }

    default void handleAssistLaunchMode(int i, Bundle bundle) {
    }

    default void handlePowerKeyUpForWallet(boolean z) {
    }

    default long hookScreenshotChordLongPressDelay() {
        return 0L;
    }

    default void init(PhoneWindowManager phoneWindowManager, Context context, IWindowManager iWindowManager, WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
    }

    default boolean interceptAppSwitchEventBeforeQueueing(KeyEvent keyEvent, boolean z) {
        return z;
    }

    default boolean interceptLongHomePress() {
        return false;
    }

    default boolean interceptLongPowerPress() {
        return false;
    }

    default void interceptPowerKeyDown(KeyEvent keyEvent, boolean z) {
    }

    default void interceptPowerKeyForAlarm() {
    }

    default boolean interceptPowerKeyForTelephone(KeyEvent keyEvent, boolean z) {
        return false;
    }

    default void interceptPowerKeyUp(boolean z) {
    }

    default boolean interceptRingerChordGesture() {
        return false;
    }

    default boolean isCameraGestureEnabled() {
        return false;
    }

    default boolean isMenuLongPressed() {
        return false;
    }

    default void launchAssistGoogleSpeechAssistantAction(Message message) {
    }

    default void powerPress(long j, boolean z, int i) {
    }

    default void setLaunchModeInBundleWithDefault(Message message) {
    }

    default int updateConfigurationDependentBehaviors(int i) {
        return i;
    }
}
