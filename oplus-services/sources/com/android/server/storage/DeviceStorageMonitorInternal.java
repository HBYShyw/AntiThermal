package com.android.server.storage;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface DeviceStorageMonitorInternal {
    void checkMemory();

    long getMemoryLowThreshold();

    boolean isMemoryLow();
}
