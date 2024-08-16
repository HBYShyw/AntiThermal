package com.android.server.biometrics.sensors.tool;

import android.os.Looper;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBiometricServiceThreadExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface IStaticExt {
        default Looper getLooperInstance() {
            return null;
        }
    }
}
