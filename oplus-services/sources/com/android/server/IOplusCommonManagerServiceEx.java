package com.android.server;

import android.common.IOplusCommonFeature;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusCommonManagerServiceEx extends IOplusCommonFeature {
    default void onStart() {
    }

    default boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        return false;
    }

    default void systemReady() {
    }
}
