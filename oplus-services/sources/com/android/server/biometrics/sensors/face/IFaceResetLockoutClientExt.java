package com.android.server.biometrics.sensors.face;

import android.content.Context;
import android.hardware.keymaster.HardwareAuthToken;
import android.os.RemoteException;
import com.android.server.biometrics.sensors.face.aidl.AidlSession;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFaceResetLockoutClientExt {
    default void init(Context context, Supplier<AidlSession> supplier, HardwareAuthToken hardwareAuthToken) {
    }

    default void startHalOperation() throws RemoteException {
    }
}
