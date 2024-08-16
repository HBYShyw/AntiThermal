package com.android.server.biometrics.sensors.face;

import android.hardware.face.Face;
import android.os.IBinder;
import java.io.PrintWriter;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFaceServiceExt {
    default void authPreOperation(IBinder iBinder, String str) {
    }

    default void dumpInternal(ServiceProvider serviceProvider, PrintWriter printWriter, String[] strArr) {
    }

    default List<Face> getEnrolledFacesExcludePalms(List<Face> list) {
        return list;
    }

    default void init() {
    }

    default boolean isBiometricDisabled() {
        return false;
    }

    default void onSystemReady() {
    }

    default void scheduleEnroll() {
    }
}
