package com.android.server.usb.hal.gadget;

import android.hardware.usb.UsbManager;
import android.os.RemoteException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface UsbGadgetHal {
    public static final int HAL_DATA_ROLE_DEVICE = 2;
    public static final int HAL_DATA_ROLE_HOST = 1;
    public static final int HAL_MODE_DFP = 1;
    public static final int HAL_MODE_UFP = 2;
    public static final int HAL_POWER_ROLE_SINK = 2;
    public static final int HAL_POWER_ROLE_SOURCE = 1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface HalUsbDataRole {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface HalUsbPortMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface HalUsbPowerRole {
    }

    void getCurrentUsbFunctions(long j);

    @UsbManager.UsbHalVersion
    int getGadgetHalVersion() throws RemoteException;

    void getUsbSpeed(long j);

    void reset(long j);

    void setCurrentUsbFunctions(int i, long j, boolean z, int i2, long j2);

    void systemReady();
}
