package com.android.server.pm;

import android.content.Context;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageManagerNativeExt {
    default void init(PackageManagerService packageManagerService, Context context) {
    }

    default boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        return false;
    }
}
