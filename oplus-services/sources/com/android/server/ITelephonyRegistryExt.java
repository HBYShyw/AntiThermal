package com.android.server;

import android.os.IBinder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ITelephonyRegistryExt {
    default boolean addProxyBinder(IBinder iBinder, int i, int i2) {
        return false;
    }

    default boolean removeProxyBinder(IBinder iBinder, int i) {
        return false;
    }
}
