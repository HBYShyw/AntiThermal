package com.android.server.devicestate;

import java.util.Optional;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDeviceStateManagerServiceWrapper {
    default Optional<DeviceState> getBaseState() {
        return null;
    }

    default Optional<DeviceState> getCommittedState() {
        return null;
    }

    default Optional<DeviceState> getStateLocked(int i) {
        return null;
    }
}
