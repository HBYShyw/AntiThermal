package com.android.server.display;

import android.os.IBinder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IVirtualDisplayAdapterExt {
    default DisplayDevice getMediaProjectionStoppedDevice(IBinder iBinder, DisplayDevice displayDevice) {
        return displayDevice;
    }

    default void setMediaProjectionStoppedDevice(IBinder iBinder, DisplayDevice displayDevice) {
    }
}
