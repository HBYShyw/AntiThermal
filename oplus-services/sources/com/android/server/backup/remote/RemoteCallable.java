package com.android.server.backup.remote;

import android.os.RemoteException;

@FunctionalInterface
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface RemoteCallable<T> {
    void call(T t) throws RemoteException;
}
