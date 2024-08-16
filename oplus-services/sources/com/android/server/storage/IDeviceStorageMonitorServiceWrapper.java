package com.android.server.storage;

import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IDeviceStorageMonitorServiceWrapper {
    default long highCheckInterVal() {
        return 0L;
    }

    default AtomicInteger mSeq() {
        return null;
    }

    default int msgChecHigh() {
        return 2;
    }

    default int msgCheckLow() {
        return 1;
    }
}
