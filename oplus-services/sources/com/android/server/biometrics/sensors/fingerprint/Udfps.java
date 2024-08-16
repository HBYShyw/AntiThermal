package com.android.server.biometrics.sensors.fingerprint;

import android.hardware.biometrics.fingerprint.PointerContext;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface Udfps {
    boolean isPointerDown();

    void onPointerDown(PointerContext pointerContext);

    void onPointerUp(PointerContext pointerContext);

    void onUiReady();
}
