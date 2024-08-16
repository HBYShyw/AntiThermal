package com.android.server.devicestate;

import android.hardware.devicestate.DeviceStateInfo;
import android.util.SparseArray;
import java.util.Optional;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDeviceStateManagerServiceExt {
    public static final int DEVICE_STATE_DSI_SWITCH_TRANSITORY = 98;

    default boolean canCancelRequestState() {
        return false;
    }

    default boolean canRequestState(Optional<DeviceState> optional, int i) {
        return true;
    }

    default void enableDeviceStateAfterBoot(boolean z) {
    }

    default int[] getSupportedStateIdentifiersLocked(int[] iArr, SparseArray<DeviceState> sparseArray) {
        return iArr;
    }

    default DeviceState[] getSupportedStates(DeviceState[] deviceStateArr, SparseArray<DeviceState> sparseArray) {
        return deviceStateArr;
    }

    default boolean hasFoldRemapDisplayDisableFeature() {
        return false;
    }

    default boolean notifyPolicyImmediately() {
        return false;
    }

    default int overrideBaseState(Optional<DeviceState> optional, int i) {
        return i;
    }

    default void setDeviceStateInfo(DeviceStateInfo deviceStateInfo) {
    }

    default void setRequestState(int i, int i2, int i3, int i4) {
    }

    default void setSwitchingTrackerSensorEventLog() {
    }

    default Optional<DeviceState> shouldInjectTransitoryState(Optional<DeviceState> optional) {
        return Optional.empty();
    }
}
