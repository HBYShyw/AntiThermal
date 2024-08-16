package com.android.server.audio;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAudioDeviceInventoryWrapper {
    default String getConnectedDevices() {
        return null;
    }

    default boolean isBluetoothScoDeviceConnected() {
        return false;
    }

    default IAudioDeviceInventoryExt getExtImpl() {
        return new IAudioDeviceInventoryExt() { // from class: com.android.server.audio.IAudioDeviceInventoryWrapper.1
        };
    }
}
