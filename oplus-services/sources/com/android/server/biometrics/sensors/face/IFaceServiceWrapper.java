package com.android.server.biometrics.sensors.face;

import android.os.IBinder;
import android.util.Pair;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFaceServiceWrapper {
    default ServiceProvider getProviderForSensorWrapper(int i) {
        return null;
    }

    default Pair<Integer, ServiceProvider> getSingleProviderWrapper() {
        return null;
    }

    default void setExtensionWrapper(IBinder iBinder) {
    }

    default IFaceServiceExt getExtImpl() {
        return new IFaceServiceExt() { // from class: com.android.server.biometrics.sensors.face.IFaceServiceWrapper.1
        };
    }
}
