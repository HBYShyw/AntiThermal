package com.android.server.biometrics.sensors;

import android.hardware.biometrics.BiometricManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
interface AuthSessionListener {
    void authEndedFor(int i, @BiometricManager.Authenticators.Types int i2, int i3, long j, boolean z);

    void authStartedFor(int i, int i2, long j);

    void lockOutTimed(int i, @BiometricManager.Authenticators.Types int i2, int i3, long j, long j2);

    void lockedOutFor(int i, @BiometricManager.Authenticators.Types int i2, int i3, long j);

    void resetLockoutFor(int i, @BiometricManager.Authenticators.Types int i2, long j);
}
