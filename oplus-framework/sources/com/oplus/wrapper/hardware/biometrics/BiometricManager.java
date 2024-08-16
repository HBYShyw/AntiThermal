package com.oplus.wrapper.hardware.biometrics;

/* loaded from: classes.dex */
public class BiometricManager {
    private final android.hardware.biometrics.BiometricManager mBiometricManager;

    public BiometricManager(android.hardware.biometrics.BiometricManager biometricManager) {
        this.mBiometricManager = biometricManager;
    }

    public int canAuthenticate(int userId, int authenticators) {
        return this.mBiometricManager.canAuthenticate(userId, authenticators);
    }
}
