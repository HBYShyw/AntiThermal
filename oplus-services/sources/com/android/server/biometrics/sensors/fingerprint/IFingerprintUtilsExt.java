package com.android.server.biometrics.sensors.fingerprint;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFingerprintUtilsExt {
    default int hookTargetUserId(int i) {
        return i;
    }

    default int setFingerprintFlags(Context context, int i, int i2, int i3) {
        return -1;
    }
}
