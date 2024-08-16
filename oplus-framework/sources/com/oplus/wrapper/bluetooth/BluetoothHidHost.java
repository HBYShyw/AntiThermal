package com.oplus.wrapper.bluetooth;

/* loaded from: classes.dex */
public class BluetoothHidHost {
    private final android.bluetooth.BluetoothHidHost mBluetoothHidHost;

    public BluetoothHidHost(android.bluetooth.BluetoothProfile bluetoothHidHost) {
        this.mBluetoothHidHost = (android.bluetooth.BluetoothHidHost) bluetoothHidHost;
    }

    public boolean connect(android.bluetooth.BluetoothDevice device) {
        return this.mBluetoothHidHost.connect(device);
    }

    public boolean disconnect(android.bluetooth.BluetoothDevice device) {
        return this.mBluetoothHidHost.disconnect(device);
    }

    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        return this.mBluetoothHidHost.getConnectionState(device);
    }
}
