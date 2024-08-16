package com.oplus.wrapper.bluetooth;

/* loaded from: classes.dex */
public class BluetoothDevice {
    private final android.bluetooth.BluetoothDevice mBluetoothDevice;

    public BluetoothDevice(android.bluetooth.BluetoothDevice bluetoothDevice) {
        this.mBluetoothDevice = bluetoothDevice;
    }

    public boolean cancelBondProcess() {
        return this.mBluetoothDevice.cancelBondProcess();
    }

    public boolean removeBond() {
        return this.mBluetoothDevice.removeBond();
    }

    public boolean isConnected() {
        return this.mBluetoothDevice.isConnected();
    }

    public int getBatteryLevel() {
        return this.mBluetoothDevice.getBatteryLevel();
    }

    public boolean isBondingInitiatedLocally() {
        return this.mBluetoothDevice.isBondingInitiatedLocally();
    }

    public int getPhonebookAccessPermission() {
        return this.mBluetoothDevice.getPhonebookAccessPermission();
    }

    public boolean setPhonebookAccessPermission(int value) {
        return this.mBluetoothDevice.setPhonebookAccessPermission(value);
    }

    public int getMessageAccessPermission() {
        return this.mBluetoothDevice.getMessageAccessPermission();
    }

    public boolean setMessageAccessPermission(int value) {
        return this.mBluetoothDevice.setMessageAccessPermission(value);
    }

    public boolean createBond() {
        return this.mBluetoothDevice.createBond();
    }

    public boolean createBond(int transport) {
        return this.mBluetoothDevice.createBond(transport);
    }
}
