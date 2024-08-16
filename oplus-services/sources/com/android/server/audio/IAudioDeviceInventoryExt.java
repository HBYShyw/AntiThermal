package com.android.server.audio;

import android.bluetooth.BluetoothDevice;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAudioDeviceInventoryExt {
    default int getFinalA2dpVolume(int i) {
        return 0;
    }

    default int getFinalBleVolume(int i) {
        return 0;
    }

    default void init(AudioDeviceBroker audioDeviceBroker) {
    }

    default boolean isDhpResetting() {
        return false;
    }

    default boolean isMetaAudioSupport() {
        return false;
    }

    default boolean isSpeakerA2dpDevice() {
        return false;
    }

    default void postAbsoluteA2dpVolume(int i) {
    }

    default void postAbsoluteBleVolume(int i) {
    }

    default void setActiveA2dpDeviceClass(BluetoothDevice bluetoothDevice, int i) {
    }

    default void setAudioDeviceDisconnect(int i) {
    }
}
