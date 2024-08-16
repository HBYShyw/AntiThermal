package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.hardware.biometrics.fingerprint.ISession;
import com.android.server.biometrics.sensors.fingerprint.aidl.Sensor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AidlSession {
    private final int mHalInterfaceVersion;
    private final Sensor.HalSessionCallback mHalSessionCallback;
    private final ISession mSession;
    private final int mUserId;

    public AidlSession(int i, ISession iSession, int i2, Sensor.HalSessionCallback halSessionCallback) {
        this.mHalInterfaceVersion = i;
        this.mSession = iSession;
        this.mUserId = i2;
        this.mHalSessionCallback = halSessionCallback;
    }

    public ISession getSession() {
        return this.mSession;
    }

    public int getUserId() {
        return this.mUserId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Sensor.HalSessionCallback getHalSessionCallback() {
        return this.mHalSessionCallback;
    }

    public boolean hasContextMethods() {
        return this.mHalInterfaceVersion >= 2;
    }
}
