package com.android.server.biometrics.sensors.face;

import android.hardware.face.IFaceCommandCallback;
import android.os.IBinder;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IServiceProviderWrapper {
    default void authPreOperation(IBinder iBinder, String str) {
    }

    default void dumpInternal(PrintWriter printWriter, String[] strArr) {
    }

    default int getFaceProcessMemory() {
        return -1;
    }

    default int getFailedAttempts() {
        return -1;
    }

    default long getLockoutAttemptDeadline(int i) {
        return -1L;
    }

    default boolean onAcquired(int i, int i2) {
        return false;
    }

    default void onAuthenticated(boolean z) {
    }

    default void onError(int i, int i2) {
    }

    default void onLockoutPermanent() {
    }

    default void onLockoutTimed() {
    }

    default void onSystemReady() {
    }

    default int regsiterFaceCmdCallback(IFaceCommandCallback iFaceCommandCallback) {
        return -1;
    }

    default void resetFaceDaemon() {
    }

    default int scheduleSendFaceCmd(int i, int i2, byte[] bArr) {
        return -1;
    }

    default int unregsiterFaceCmdCallback(IFaceCommandCallback iFaceCommandCallback) {
        return -1;
    }
}
