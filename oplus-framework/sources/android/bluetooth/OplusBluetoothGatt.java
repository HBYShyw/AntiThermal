package android.bluetooth;

import android.bluetooth.BluetoothAdapterExtImpl;
import android.bluetooth.IOplusBluetoothGatt;
import android.bluetooth.le.IOplusRssiDetectCallback;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusBluetoothGatt {
    public static final String DESCRIPTOR = "android.bluetooth.IBluetoothGatt";
    public static final int OPLUS_CALL_TRANSACTION_INDEX = 10000;
    public static final int OPLUS_GATT_FAST_MODE = 1;
    public static final int OPLUS_GATT_NORMAL_MODE = 2;
    private static final String TAG = "OplusBluetoothGatt";
    public static final int TRANSACTION_CLIENT_SET_CONNECT_MODE = 10001;
    public static final int TRANSACTION_REG_RSSI_DETECT_CALLBACK = 10002;
    public static final int TRANSACTION_UNREG_RSSI_DETECT_CALLBACK = 10003;
    private static final Map<OplusBluetoothRssiDetectCallback, IOplusRssiDetectCallback> mRssiDetectCallbackList = new HashMap();

    private static IOplusBluetoothGatt getOplusBluetoothGatt(IBluetoothGatt bluetoothGatt) {
        if (bluetoothGatt != null) {
            try {
                IBinder b = bluetoothGatt.asBinder().getExtension();
                return IOplusBluetoothGatt.Stub.asInterface(b);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
                return null;
            }
        }
        return null;
    }

    static boolean clientSetConnectMode(IBluetoothGatt bluetoothGatt, String address, int mode) throws RemoteException {
        if (bluetoothGatt == null) {
            Log.w(TAG, "oplusClientSetConnectMode bluetoothGatt is null!");
            return false;
        }
        if (address == null) {
            Log.w(TAG, "oplusClientSetConnectMode address is null!");
            return false;
        }
        IOplusBluetoothGatt iOplusBluetoothGatt = getOplusBluetoothGatt(bluetoothGatt);
        if (iOplusBluetoothGatt == null) {
            Log.w(TAG, "iOplusBluetoothGatt is null!");
            return false;
        }
        try {
            iOplusBluetoothGatt.clientSetConnectMode(address, mode);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean oplusClientSetFastConnectMode(IBluetoothGatt bluetoothGatt, String address, int connectMode) throws RemoteException {
        return clientSetConnectMode(bluetoothGatt, address, connectMode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean registerBluetoothRssiDetectCallback(IBluetoothGatt bluetoothGatt, BluetoothAdapterExtImpl.BleRssiDetectCallback callback, String packageName) throws RemoteException {
        if (bluetoothGatt == null || callback == null || packageName == null) {
            Log.w(TAG, "bluetoothGatt,callback or packageName is null!");
            return false;
        }
        if (callback.getCallback() != null) {
            Map<OplusBluetoothRssiDetectCallback, IOplusRssiDetectCallback> map = mRssiDetectCallbackList;
            if (!map.containsKey(callback.getCallback())) {
                IOplusBluetoothGatt iOplusBluetoothGatt = getOplusBluetoothGatt(bluetoothGatt);
                if (iOplusBluetoothGatt == null) {
                    Log.w(TAG, "iOplusBluetoothGatt is null!");
                    return false;
                }
                map.put(callback.getCallback(), callback);
                try {
                    return iOplusBluetoothGatt.registerBluetoothRssiDetectCallback(packageName, callback);
                } catch (RemoteException e) {
                    Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
                    return false;
                }
            }
        }
        Log.w(TAG, "iCallback or packageName is null or callback has registered!");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean unregisterBluetoothRssiDetectCallback(IBluetoothGatt bluetoothGatt, BluetoothAdapterExtImpl.BleRssiDetectCallback callback, String packageName) throws RemoteException {
        if (callback == null) {
            Log.w(TAG, "callback is null!");
            return false;
        }
        mRssiDetectCallbackList.remove(callback.getCallback());
        if (bluetoothGatt == null) {
            Log.w(TAG, "bluetoothGatt is null!");
            return false;
        }
        IOplusBluetoothGatt iOplusBluetoothGatt = getOplusBluetoothGatt(bluetoothGatt);
        if (iOplusBluetoothGatt == null) {
            Log.w(TAG, "iOplusBluetoothGatt is null!");
            return false;
        }
        try {
            return iOplusBluetoothGatt.unregisterBluetoothRssiDetectCallback(packageName, callback);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            return false;
        }
    }
}
