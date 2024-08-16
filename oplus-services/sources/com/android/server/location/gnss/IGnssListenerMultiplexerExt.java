package com.android.server.location.gnss;

import android.os.IBinder;
import android.os.IInterface;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IGnssListenerMultiplexerExt {
    default boolean addProxyBinder(IBinder iBinder, IInterface iInterface, int i, int i2) {
        return false;
    }

    default boolean removeProxyBinder(IBinder iBinder, IInterface iInterface) {
        return false;
    }
}
