package com.android.server.location.injector;

import android.os.Binder;
import com.android.internal.util.Preconditions;
import com.android.server.DeviceIdleInternal;
import com.android.server.LocalServices;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SystemDeviceStationaryHelper extends DeviceStationaryHelper {
    private DeviceIdleInternal mDeviceIdle;

    public void onSystemReady() {
        DeviceIdleInternal deviceIdleInternal = (DeviceIdleInternal) LocalServices.getService(DeviceIdleInternal.class);
        Objects.requireNonNull(deviceIdleInternal);
        this.mDeviceIdle = deviceIdleInternal;
    }

    @Override // com.android.server.location.injector.DeviceStationaryHelper
    public void addListener(DeviceIdleInternal.StationaryListener stationaryListener) {
        Preconditions.checkState(this.mDeviceIdle != null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mDeviceIdle.registerStationaryListener(stationaryListener);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.DeviceStationaryHelper
    public void removeListener(DeviceIdleInternal.StationaryListener stationaryListener) {
        Preconditions.checkState(this.mDeviceIdle != null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mDeviceIdle.unregisterStationaryListener(stationaryListener);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
