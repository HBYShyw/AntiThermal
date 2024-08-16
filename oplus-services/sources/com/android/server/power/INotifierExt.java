package com.android.server.power;

import com.android.server.input.InputManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface INotifierExt {
    default void finishPendingBroadcastLocked() {
    }

    default boolean handleEarlyInteractiveChangeInActive() {
        return false;
    }

    default void handleLateInteractiveChangeInActive() {
    }

    default boolean handleLateInteractiveChangeUnActive() {
        return false;
    }

    default boolean isNeedActiveInput() {
        return true;
    }

    default boolean isSkipGotoSleepBroadcast() {
        return false;
    }

    default boolean isSkipWakeupBroadcast() {
        return false;
    }

    default void noteSysStateChanged(int i, int i2) {
    }

    default void notifyOnWakefulnessChangeFinishedEnter(InputManagerInternal inputManagerInternal, boolean z, boolean z2) {
    }

    default void notifyOnWakefulnessChangeStartedEnter(boolean z, int i) {
    }

    default void notifyScreenOnOff(boolean z) {
    }

    default void onWakefulnessChanged(int i) {
    }

    default boolean playChargingStartedFeedback() {
        return false;
    }

    default void updatePendingBroadcastLocked() {
    }
}
