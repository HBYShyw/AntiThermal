package com.oplus.wrapper.app;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public class BluetoothA2dp {
    private final android.bluetooth.BluetoothA2dp mBluetoothA2dp;

    public BluetoothA2dp(android.bluetooth.BluetoothA2dp bluetoothA2dp) {
        this.mBluetoothA2dp = bluetoothA2dp;
    }

    public boolean connect(BluetoothDevice device) {
        return this.mBluetoothA2dp.connect(device);
    }

    public boolean disconnect(BluetoothDevice device) {
        return this.mBluetoothA2dp.disconnect(device);
    }

    public boolean setActiveDevice(BluetoothDevice device) {
        return this.mBluetoothA2dp.setActiveDevice(device);
    }

    public BluetoothDevice getActiveDevice() {
        return this.mBluetoothA2dp.getActiveDevice();
    }

    public int getPriority(BluetoothDevice device) {
        return this.mBluetoothA2dp.getPriority(device);
    }

    public boolean setPriority(BluetoothDevice device, int priority) {
        return this.mBluetoothA2dp.setPriority(device, priority);
    }

    public static String stateToString(int state) {
        return android.bluetooth.BluetoothA2dp.stateToString(state);
    }

    public boolean setConnectionPolicy(BluetoothDevice device, int connectionPolicy) {
        return this.mBluetoothA2dp.setConnectionPolicy(device, connectionPolicy);
    }

    public int getConnectionPolicy(BluetoothDevice device) {
        return this.mBluetoothA2dp.getConnectionPolicy(device);
    }
}
