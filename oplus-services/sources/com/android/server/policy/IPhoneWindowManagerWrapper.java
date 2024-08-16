package com.android.server.policy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPhoneWindowManagerWrapper {
    default void cancelGlobalActionsAction() {
    }

    default void cancelPendingRingerToggleChordAction() {
    }

    default void cancelPreloadRecentApps() {
    }

    default void finishPowerKeyPress() {
    }

    default KeyCombinationManager getKeyCombinationManager() {
        return null;
    }

    default SingleKeyGestureDetector getSingleKeyGestureDetector() {
        return null;
    }

    default void interceptRingerToggleChord() {
    }

    default void launchAssistAction(String str, int i, long j, int i2, int i3) {
    }

    default boolean performHapticFeedback(int i, boolean z, String str) {
        return false;
    }

    default void powerPress(long j, int i, boolean z) {
    }

    default void setDebugInput(boolean z) {
    }

    default void setDebugKeyguard(boolean z) {
    }

    default void setDebugWakeup(boolean z) {
    }

    default void setlocalLOGV(boolean z) {
    }

    default void wakeUpFromPowerKey(long j) {
    }

    default IPhoneWindowManagerExt getExtImpl() {
        return new IPhoneWindowManagerExt() { // from class: com.android.server.policy.IPhoneWindowManagerWrapper.1
        };
    }

    default Object getLock() {
        return new Object();
    }
}
