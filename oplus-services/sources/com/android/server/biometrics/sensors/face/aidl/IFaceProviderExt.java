package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.hardware.biometrics.face.SensorProps;
import android.hardware.biometrics.face.V1_0.IBiometricsFace;
import android.hardware.face.IFaceCommandCallback;
import android.os.Handler;
import android.os.IBinder;
import android.os.NativeHandle;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFaceProviderExt {
    default void authPreOperation(IBinder iBinder, String str) {
    }

    default Handler createHandlerWithNewLooper() {
        return null;
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

    default void handleOnFaceCmd(int i, ArrayList<Byte> arrayList, int i2) {
    }

    default void init(FaceProvider faceProvider, Context context, SensorProps[] sensorPropsArr, String str, Handler handler) {
    }

    default boolean isBackgroundAuthAllow(String str) {
        return false;
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

    default void resetFaceLockout(byte[] bArr) {
    }

    default void scheduleAuthenticate() {
    }

    default int scheduleSendFaceCmd(int i, int i2, byte[] bArr) {
        return -1;
    }

    default void setOplusCallback(IBiometricsFace iBiometricsFace) {
    }

    default void setPreviewSurface(NativeHandle nativeHandle) {
    }

    default int unregsiterFaceCmdCallback(IFaceCommandCallback iFaceCommandCallback) {
        return -1;
    }
}
