package com.oplus.wrapper.bluetooth;

/* loaded from: classes.dex */
public class BluetoothGatt {
    private final android.bluetooth.BluetoothGatt mBluetoothGatt;

    public BluetoothGatt(android.bluetooth.BluetoothGatt bluetoothGatt) {
        this.mBluetoothGatt = bluetoothGatt;
    }

    public boolean refresh() {
        return this.mBluetoothGatt.refresh();
    }
}
