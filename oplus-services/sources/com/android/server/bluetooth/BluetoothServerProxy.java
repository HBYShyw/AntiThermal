package com.android.server.bluetooth;

import android.content.ContentResolver;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.bluetooth.BluetoothManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothServerProxy {
    private static final Object INSTANCE_LOCK = new Object();
    private static final String TAG = "BluetoothServerProxy";
    private static BluetoothServerProxy sInstance;

    public BluetoothManagerService.BluetoothHandler createBluetoothHandler(BluetoothManagerService.BluetoothHandler bluetoothHandler) {
        return bluetoothHandler;
    }

    public BluetoothManagerService.BluetoothHandler newBluetoothHandler(BluetoothManagerService.BluetoothHandler bluetoothHandler) {
        return bluetoothHandler;
    }

    private BluetoothServerProxy() {
    }

    public static BluetoothServerProxy getInstance() {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new BluetoothServerProxy();
            }
        }
        return sInstance;
    }

    @VisibleForTesting
    public static void setInstanceForTesting(BluetoothServerProxy bluetoothServerProxy) {
        synchronized (INSTANCE_LOCK) {
            Log.d(TAG, "setInstanceForTesting(), set to " + bluetoothServerProxy);
            sInstance = bluetoothServerProxy;
        }
    }

    public HandlerThread createHandlerThread(String str) {
        return new HandlerThread(str);
    }

    public String settingsSecureGetString(ContentResolver contentResolver, String str) {
        return Settings.Secure.getString(contentResolver, str);
    }

    public boolean handlerSendWhatMessage(BluetoothManagerService.BluetoothHandler bluetoothHandler, int i) {
        return bluetoothHandler.sendMessage(bluetoothHandler.obtainMessage(i));
    }
}
