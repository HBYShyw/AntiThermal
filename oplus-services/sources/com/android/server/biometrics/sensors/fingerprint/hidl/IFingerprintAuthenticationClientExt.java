package com.android.server.biometrics.sensors.fingerprint.hidl;

import android.content.Context;
import android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint;
import android.os.RemoteException;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFingerprintAuthenticationClientExt {
    default void init(Context context, Supplier<IBiometricsFingerprint> supplier, int i, long j, String str) {
    }

    default boolean isMistakeTouchMode() {
        return false;
    }

    default boolean onHandleFailedAttempt(LockoutFrameworkImpl lockoutFrameworkImpl, int i) {
        return false;
    }

    default void setIsNearState(boolean z) {
    }

    default boolean startHalOperation() throws RemoteException {
        return false;
    }
}
