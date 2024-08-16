package com.android.server.biometrics.sensors.fingerprint;

import android.util.Pair;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFingerprintServiceWrapper {
    default boolean canUseFingerprintWrapper(String str, String str2, boolean z, int i, int i2, int i3) {
        return true;
    }

    default ServiceProvider getProviderForSensorWrapper(int i) {
        return null;
    }

    default Pair<Integer, ServiceProvider> getSingleProviderWrapper() {
        return null;
    }

    default IFingerprintServiceExt getExtImpl() {
        return new IFingerprintServiceExt() { // from class: com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper.1
        };
    }
}
