package com.android.server.devicestate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface DeviceStateProvider {
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_DEFAULT = 0;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_INITIALIZED = 1;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_POWER_SAVE_DISABLED = 5;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_POWER_SAVE_ENABLED = 4;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_THERMAL_CRITICAL = 3;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_THERMAL_NORMAL = 2;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onStateChanged(int i);

        void onSupportedDeviceStatesChanged(DeviceState[] deviceStateArr, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface SupportedStatesUpdatedReason {
    }

    void notifyKeyguardShowOrSleep(boolean z);

    void registerSensor();

    void setListener(Listener listener);
}
