package com.android.server.bluetooth;

import android.provider.DeviceConfig;
import android.util.Log;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothDeviceConfigListener {
    private static final String TAG = "BluetoothDeviceConfigListener";
    private final BluetoothDeviceConfigChangeTracker mConfigChangeTracker;
    private final DeviceConfig.OnPropertiesChangedListener mDeviceConfigChangedListener;
    private final boolean mLogDebug;
    private final BluetoothManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothDeviceConfigListener(BluetoothManagerService bluetoothManagerService, boolean z) {
        DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.bluetooth.BluetoothDeviceConfigListener.1
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (BluetoothDeviceConfigListener.this.mConfigChangeTracker.shouldRestartWhenPropertiesUpdated(properties)) {
                    Log.d(BluetoothDeviceConfigListener.TAG, "Properties changed, enqueuing restart");
                    BluetoothDeviceConfigListener.this.mService.onInitFlagsChanged();
                } else {
                    Log.d(BluetoothDeviceConfigListener.TAG, "All properties unchanged, skipping restart");
                }
            }
        };
        this.mDeviceConfigChangedListener = onPropertiesChangedListener;
        this.mService = bluetoothManagerService;
        this.mLogDebug = z;
        this.mConfigChangeTracker = new BluetoothDeviceConfigChangeTracker(DeviceConfig.getProperties("bluetooth", new String[0]));
        DeviceConfig.addOnPropertiesChangedListener("bluetooth", new Executor() { // from class: com.android.server.bluetooth.BluetoothDeviceConfigListener$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                runnable.run();
            }
        }, onPropertiesChangedListener);
    }
}
