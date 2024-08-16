package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.hardware.biometrics.common.ICancellationSignal;
import android.os.RemoteException;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFaceAuthenticationClientExt {
    default void init(Context context, Supplier<AidlSession> supplier, String str, int i, long j) {
    }

    default ICancellationSignal startHalOperation() throws RemoteException {
        return null;
    }

    default boolean stopHalOperation() throws RemoteException {
        return false;
    }
}
