package com.android.server.audio;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothLeAudio;
import android.content.Intent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBtHelperSocExt {
    default boolean isBluetoothScoOn() {
        return true;
    }

    default boolean isLeAudioDevice(Intent intent) {
        return false;
    }

    default boolean isNextBtActiveDeviceAvailableForMusic(BluetoothA2dp bluetoothA2dp, BluetoothLeAudio bluetoothLeAudio) {
        return false;
    }
}
