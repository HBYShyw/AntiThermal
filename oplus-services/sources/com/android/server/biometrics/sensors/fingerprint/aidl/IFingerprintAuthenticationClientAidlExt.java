package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.content.Context;
import android.hardware.biometrics.common.ICancellationSignal;
import android.os.RemoteException;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFingerprintAuthenticationClientAidlExt {
    default void init(Context context, Supplier<AidlSession> supplier, int i, long j, String str) {
    }

    default void setIsNearState(boolean z) {
    }

    default ICancellationSignal startHalOperation() throws RemoteException {
        return null;
    }
}
