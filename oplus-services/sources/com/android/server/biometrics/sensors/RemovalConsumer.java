package com.android.server.biometrics.sensors;

import android.hardware.biometrics.BiometricAuthenticator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface RemovalConsumer {
    void onRemoved(BiometricAuthenticator.Identifier identifier, int i);
}
