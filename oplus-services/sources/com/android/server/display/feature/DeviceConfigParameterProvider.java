package com.android.server.display.feature;

import android.provider.DeviceConfig;
import android.provider.DeviceConfigInterface;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DeviceConfigParameterProvider {
    private static final String TAG = "DisplayFeatureProvider";
    private final DeviceConfigInterface mDeviceConfig;

    public DeviceConfigParameterProvider(DeviceConfigInterface deviceConfigInterface) {
        this.mDeviceConfig = deviceConfigInterface;
    }

    public boolean isDisableScreenWakeLocksWhileCachedFeatureEnabled() {
        return this.mDeviceConfig.getBoolean("display_manager", "disable_screen_wake_locks_while_cached", true);
    }

    public void addOnPropertiesChangedListener(Executor executor, DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
        this.mDeviceConfig.addOnPropertiesChangedListener("display_manager", executor, onPropertiesChangedListener);
    }
}
