package com.android.server.display;

import android.os.Handler;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusLogicDisplayMapperWrapper {
    default void dispatchDelayedDeviceState(int i, boolean z) {
    }

    default int getDeviceState() {
        return 0;
    }

    default int getPendingDeviceState() {
        return 0;
    }

    default void setPendingDeviceState(int i) {
    }

    default Handler getHandler() {
        return new Handler();
    }
}
