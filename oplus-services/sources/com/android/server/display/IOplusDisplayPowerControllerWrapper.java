package com.android.server.display;

import android.hardware.display.DisplayManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusDisplayPowerControllerWrapper {
    default void animateScreenBrightness(float f, float f2, float f3) {
    }

    default DisplayManagerInternal.DisplayPowerCallbacks getCallbacks() {
        return null;
    }

    default int getDisplayId() {
        return 0;
    }

    default LogicalDisplayMapper getLogicalDisplayMapper() {
        return null;
    }

    default void handleCoverModeProximitySensorEvent(long j, boolean z) {
    }

    default boolean isScreenOnUnblockerExist() {
        return false;
    }

    default void sendMsgUnblockScreenOn(boolean z) {
    }

    default void sendUpdatePowerState() {
    }

    default void setAutoBrightnessAdjustment(float f) {
    }

    default void setCurrentScreenBrightnessSetting(int i) {
    }

    default void setDebug(boolean z) {
    }

    default void setLogicalDisplayMapper(LogicalDisplayMapper logicalDisplayMapper) {
    }

    default void setProximitySensorEnabled(boolean z) {
    }

    default void setScreenBrightnessDefault(int i) {
    }

    default void setScreenBrightnessNormalMaximum(int i) {
    }

    default void setScreenBrightnessRangeMaximum(int i) {
    }

    default void setScreenBrightnessRangeMinimum(int i) {
    }

    default void setScreenOffBecauseOfProximity(boolean z) {
    }

    default void setWaitingForNegativeProximity(boolean z) {
    }

    default void updatePowerState() {
    }
}
