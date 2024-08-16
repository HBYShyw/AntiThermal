package com.android.server.content;

import android.os.IBinder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IContentServiceExt {
    default boolean addProxyBinder(IBinder iBinder, int i, int i2) {
        return true;
    }

    default int checkUserHandle(String str, int i) {
        return i;
    }

    default boolean removeProxyBinder(IBinder iBinder, int i) {
        return true;
    }
}
