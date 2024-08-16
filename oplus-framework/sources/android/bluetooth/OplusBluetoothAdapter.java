package android.bluetooth;

import android.bluetooth.IOplusBluetoothOobDataCallback;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.AttributionSource;
import android.util.Log;
import com.oplus.bluetooth.OplusBluetoothQoSData;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public final class OplusBluetoothAdapter {
    public static final String ACTION_BLUETOOTH_CHANNEL_STATE = "com.oplus.bluetooth.a2dp.path";
    public static final long FEATURE_CALL_SWITCH_DURING_CALL = 1;
    public static final int PATH_EAR = 0;
    public static final int PATH_MQBT = 1;
    public static final String TAG = "OplusBluetoothAdapter";
    public static final int TYPE_BLUEOOTH_RECORD_DUAL_EAR_DUAL_CHANNEL = 1;
    public static final int TYPE_BLUEOOTH_RECORD_DUAL_EAR_FOUR_CHANNEL = 2;
    public static final int TYPE_BLUEOOTH_RECORD_NOT_CONNECTED = 0;
    private static OplusBluetoothAdapter sAdapter;

    public static synchronized OplusBluetoothAdapter getOplusBluetoothAdapter() {
        OplusBluetoothAdapter oplusBluetoothAdapter;
        synchronized (OplusBluetoothAdapter.class) {
            if (sAdapter == null) {
                sAdapter = new OplusBluetoothAdapter();
            }
            oplusBluetoothAdapter = sAdapter;
        }
        return oplusBluetoothAdapter;
    }

    public void oplusEnableAutoConnectPolicy(BluetoothDevice device) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            bluetoothAdapterExtImp.enableAutoConnectPolicy(device);
        }
    }

    public void oplusDisableAutoConnectPolicy(BluetoothDevice device) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            bluetoothAdapterExtImp.disableAutoConnectPolicy(device);
        }
    }

    public boolean registerOplusBluetoothRssiDetectCallback(OplusBluetoothRssiDetectCallback callback) {
        if (callback == null) {
            Log.w(TAG, "callback null!");
            return false;
        }
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp == null) {
            return false;
        }
        return bluetoothAdapterExtImp.registerBluetoothRssiDetectCallback(callback);
    }

    public boolean unregisterOplusBluetoothRssiDetectCallback(OplusBluetoothRssiDetectCallback callback) {
        if (callback == null) {
            Log.w(TAG, "callback null!");
            return false;
        }
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            return bluetoothAdapterExtImp.unregisterBluetoothRssiDetectCallback(callback);
        }
        Log.w(TAG, "bluetoothAdapterExtImp null!");
        return false;
    }

    public int getBluetoothConnectionCount() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp == null) {
            return 0;
        }
        return bluetoothAdapterExtImp.getBluetoothConnectionCount();
    }

    public int[] getBluetoothConnectedAppPID() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            return bluetoothAdapterExtImp.getBluetoothConnectedAppPID();
        }
        return new int[0];
    }

    public int getBluetoothRecordConnectedType() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            return bluetoothAdapterExtImp.getBluetoothRecordConnectedType();
        }
        return 0;
    }

    public boolean isBluetoothRecordConnected() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            return bluetoothAdapterExtImp.isBluetoothRecordConnected();
        }
        return false;
    }

    public void startSenselessConnectionLeAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseCallback appCallback) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            bluetoothAdapterExtImp.senselessConnectionStartLeAdvertising(settings, advertiseData, scanResponse, appCallback);
        } else if (appCallback != null) {
            appCallback.onStartFailure(4);
        }
    }

    public void startSenselessConnectionLeScan(List<ScanFilter> filters, ScanSettings settings, ScanCallback callback) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            bluetoothAdapterExtImp.senselessConnectionStartLeScan(filters, settings, callback);
        } else if (callback != null) {
            callback.onScanFailed(3);
        }
    }

    public boolean isHDTSupported(BluetoothDevice device) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            return bluetoothAdapterExtImp.isHDTSupported(device);
        }
        Log.w(TAG, "bluetoothAdapterExtImp null in isHDTSupported!");
        return false;
    }

    public int getA2dpTransmissionPath() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            return bluetoothAdapterExtImp.getA2dpTransmissionPath();
        }
        Log.w(TAG, "bluetoothAdapterExtImp null in getA2dpTransmissionPath!");
        return 1;
    }

    public void setHDTEnable(boolean enable) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            bluetoothAdapterExtImp.setHDTEnable(enable);
        } else {
            Log.w(TAG, "bluetoothAdapterExtImp null in setHDTEnable!");
        }
    }

    /* loaded from: classes.dex */
    private class WrappedOobDataCallback extends IOplusBluetoothOobDataCallback.Stub {
        private final OplusOobDataCallback mCallback;
        private final Executor mExecutor;

        WrappedOobDataCallback(OplusOobDataCallback callback, Executor executor) {
            this.mCallback = callback;
            this.mExecutor = executor;
        }

        @Override // android.bluetooth.IOplusBluetoothOobDataCallback
        public void onOobData(final int transport, final OplusBluetoothOobData oobData) {
            this.mExecutor.execute(new Runnable() { // from class: android.bluetooth.OplusBluetoothAdapter.WrappedOobDataCallback.1
                @Override // java.lang.Runnable
                public void run() {
                    WrappedOobDataCallback.this.mCallback.onOobData(transport, oobData);
                }
            });
        }

        @Override // android.bluetooth.IOplusBluetoothOobDataCallback
        public void onError(final int errorCode) {
            this.mExecutor.execute(new Runnable() { // from class: android.bluetooth.OplusBluetoothAdapter.WrappedOobDataCallback.2
                @Override // java.lang.Runnable
                public void run() {
                    WrappedOobDataCallback.this.mCallback.onError(errorCode);
                }
            });
        }
    }

    public void generateLocalOobData(int transport, Executor executor, OplusOobDataCallback callback) {
        if (transport != 1 && transport != 2) {
            throw new IllegalArgumentException("Invalid transport '" + transport + "'!");
        }
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Log.w(TAG, "generateLocalOobData(): Adapter isn't enabled!");
            callback.onError(1);
            return;
        }
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            bluetoothAdapterExtImp.generateLocalOobData(transport, new WrappedOobDataCallback(callback, executor), AttributionSource.myAttributionSource());
        } else if (callback != null) {
            callback.onError(1);
        }
    }

    public void sendDataToController(byte[] data) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            bluetoothAdapterExtImp.sendDataToController(data);
        } else {
            Log.w(TAG, "bluetoothAdapterExtImp null in sendDataToController!");
        }
    }

    public OplusBluetoothQoSData getLinkStatus() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            return bluetoothAdapterExtImp.getLinkStatus();
        }
        Log.w(TAG, "bluetoothAdapterExtImp null in getLinkStatus!");
        return null;
    }

    public boolean isCallSwitchSupported(BluetoothDevice device) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp != null) {
            return bluetoothAdapterExtImp.isCallSwitchSupported(device, AttributionSource.myAttributionSource());
        }
        Log.w(TAG, "isCallSwitchSupported null!");
        return false;
    }
}
