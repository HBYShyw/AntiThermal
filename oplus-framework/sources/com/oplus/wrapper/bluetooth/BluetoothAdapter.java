package com.oplus.wrapper.bluetooth;

import android.os.ParcelUuid;
import java.util.List;

/* loaded from: classes.dex */
public class BluetoothAdapter {
    private final android.bluetooth.BluetoothAdapter mBluetoothAdapter;

    public BluetoothAdapter(android.bluetooth.BluetoothAdapter bluetoothAdapter) {
        this.mBluetoothAdapter = bluetoothAdapter;
    }

    public int getConnectionState() {
        return this.mBluetoothAdapter.getConnectionState();
    }

    public ParcelUuid[] getUuids() {
        return this.mBluetoothAdapter.getUuids();
    }

    public int setScanMode(int mode) {
        return this.mBluetoothAdapter.setScanMode(mode);
    }

    public boolean enableNoAutoConnect() {
        return this.mBluetoothAdapter.enableNoAutoConnect();
    }

    public List<Integer> getSupportedProfiles() {
        return this.mBluetoothAdapter.getSupportedProfiles();
    }

    public int getMaxConnectedAudioDevices() {
        return this.mBluetoothAdapter.getMaxConnectedAudioDevices();
    }
}
