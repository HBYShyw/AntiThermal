package android.bluetooth;

import android.content.AttributionSource;
import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.bluetooth.OplusBluetoothClass;
import java.io.IOException;
import java.util.UUID;

/* loaded from: classes.dex */
public final class OplusBluetoothDevice {
    public static final int GATT_CONNECT_MODE_FAST = 1;
    public static final int GATT_CONNECT_MODE_NORMAL = 2;
    public static final int GATT_CONNECT_MODE_SLOW = 3;
    public static final int RFCOMM_ENHANCE_MODE_SENSELESS_NO_SECURE = 1;
    public static final String TAG = "OplusBluetoothDevice";
    private AttributionSource mAttributionSource = AttributionSource.myAttributionSource();
    private BluetoothDevice mBluetoothDevice;

    public OplusBluetoothDevice(BluetoothDevice device) {
        this.mBluetoothDevice = null;
        this.mBluetoothDevice = device;
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int connectMode) {
        return connectGatt(context, autoConnect, callback, 2, false, 1, null, connectMode);
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, boolean fastConnect) {
        return connectGatt(context, autoConnect, callback, 0, fastConnect);
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport, boolean fastConnect) {
        return connectGatt(context, autoConnect, callback, transport, 1, fastConnect);
    }

    private BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport, int phy, boolean fastConnect) {
        return connectGatt(context, autoConnect, callback, transport, phy, null, fastConnect);
    }

    private BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport, int phy, Handler handler, boolean fastConnect) {
        return connectGatt(context, autoConnect, callback, transport, false, phy, handler, fastConnect ? 1 : 2);
    }

    private BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport, boolean opportunistic, int phy, Handler handler, int connectMode) {
        if (callback == null) {
            throw new NullPointerException("callback is null");
        }
        BluetoothManager systemBluetootService = (BluetoothManager) context.getSystemService("bluetooth");
        if (systemBluetootService == null) {
            Log.e(TAG, "cannot get bluetooth service");
            return null;
        }
        BluetoothAdapter adapter = systemBluetootService.getAdapter();
        if (adapter == null) {
            Log.e(TAG, "no bluetoot adapter available!");
            return null;
        }
        IBluetoothManager managerService = adapter.getBluetoothManager();
        try {
            IBluetoothGatt iGatt = managerService.getBluetoothGatt();
            if (iGatt == null || this.mBluetoothDevice == null) {
                return null;
            }
            BluetoothGatt gatt = new BluetoothGatt(iGatt, this.mBluetoothDevice, transport, opportunistic, phy, context.getAttributionSource());
            if (!autoConnect) {
                boolean result = OplusBluetoothGatt.oplusClientSetFastConnectMode(iGatt, this.mBluetoothDevice.getAddress(), connectMode);
                Log.d(TAG, "setConnectMode " + connectMode + " ret " + result);
            }
            try {
                gatt.connect(Boolean.valueOf(autoConnect), callback, handler);
                return gatt;
            } catch (RemoteException e) {
                e = e;
                Log.e(TAG, "", e);
                return null;
            }
        } catch (RemoteException e2) {
            e = e2;
        }
    }

    public BluetoothSocket createEnhanceInsecureRfcommSocketToServiceRecord(UUID uuid, int enhanceMode) throws IOException {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {
            Log.e(TAG, "Bluetooth is not enabled");
            throw new IOException();
        }
        BluetoothAdapterExtImpl bluetoothAdapterExtImpl = BluetoothAdapterExtImpl.getInstance();
        bluetoothAdapterExtImpl.setInsecureRfcommEnhanceMode(this.mBluetoothDevice.getAddress(), uuid.toString(), enhanceMode);
        return this.mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
    }

    public boolean createBondOutOfBand(int transport, OplusBluetoothOobData remoteP192Data, OplusBluetoothOobData remoteP256Data) {
        if (remoteP192Data == null && remoteP256Data == null) {
            throw new IllegalArgumentException("One or both arguments for the OOB data types are required to not be null.  Please use createBond() instead if you do not have OOB data to pass.");
        }
        BluetoothAdapterExtImpl bluetoothAdapterExtImpl = BluetoothAdapterExtImpl.getInstance();
        return bluetoothAdapterExtImpl.createBondOutOfBand(transport, this.mBluetoothDevice, remoteP192Data, remoteP256Data, this.mAttributionSource);
    }

    public boolean isSupportAbsoluteVolume() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImpl = BluetoothAdapterExtImpl.getInstance();
        return bluetoothAdapterExtImpl.isSupportAbsoluteVolume(this.mBluetoothDevice);
    }

    public boolean isAbsoluteVolumeOn() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImpl = BluetoothAdapterExtImpl.getInstance();
        return bluetoothAdapterExtImpl.isAbsoluteVolumeOn(this.mBluetoothDevice);
    }

    public boolean setAbsoluteVolumeOn(boolean on) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImpl = BluetoothAdapterExtImpl.getInstance();
        return bluetoothAdapterExtImpl.setAbsoluteVolumeOn(this.mBluetoothDevice, on);
    }

    public int getOplusBluetoothClass() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImpl = BluetoothAdapterExtImpl.getInstance();
        int classInt = bluetoothAdapterExtImpl.getRemoteClass(this.mBluetoothDevice);
        if (classInt == -16777216) {
            return OplusBluetoothClass.Device.UNKNOWN;
        }
        return classInt;
    }
}
