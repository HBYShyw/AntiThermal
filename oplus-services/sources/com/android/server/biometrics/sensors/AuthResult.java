package com.android.server.biometrics.sensors;

import android.hardware.biometrics.BiometricManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class AuthResult {
    static final int AUTHENTICATED = 2;
    static final int FAILED = 0;
    static final int LOCKED_OUT = 1;
    private final int mBiometricStrength;
    private final int mStatus;

    AuthResult(int i, @BiometricManager.Authenticators.Types int i2) {
        this.mStatus = i;
        this.mBiometricStrength = i2;
    }

    int getStatus() {
        return this.mStatus;
    }

    int getBiometricStrength() {
        return this.mBiometricStrength;
    }
}
