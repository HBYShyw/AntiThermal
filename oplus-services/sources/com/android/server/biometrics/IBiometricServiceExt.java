package com.android.server.biometrics;

import android.os.Handler;
import com.android.server.biometrics.log.BiometricContext;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBiometricServiceExt {
    default Handler createHandlerWithNewLooper() {
        return null;
    }

    default void resetLockoutTimeBound(BiometricContext biometricContext, int i, int i2) {
    }
}
