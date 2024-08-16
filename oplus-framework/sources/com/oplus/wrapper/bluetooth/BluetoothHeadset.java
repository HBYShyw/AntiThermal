package com.oplus.wrapper.bluetooth;

/* loaded from: classes.dex */
public class BluetoothHeadset {
    private final android.bluetooth.BluetoothHeadset mBluetoothHeadset;

    public BluetoothHeadset(android.bluetooth.BluetoothHeadset bluetoothHeadset) {
        this.mBluetoothHeadset = bluetoothHeadset;
    }

    public android.bluetooth.BluetoothDevice getActiveDevice() {
        return this.mBluetoothHeadset.getActiveDevice();
    }

    public boolean connect(android.bluetooth.BluetoothDevice device) {
        return this.mBluetoothHeadset.connect(device);
    }

    public boolean disconnect(android.bluetooth.BluetoothDevice device) {
        return this.mBluetoothHeadset.disconnect(device);
    }

    public int getPriority(android.bluetooth.BluetoothDevice device) {
        return this.mBluetoothHeadset.getPriority(device);
    }
}
