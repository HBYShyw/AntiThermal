package com.android.server.biometrics.sensors.fingerprint;

import android.hardware.fingerprint.IFingerprintServiceReceiver;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFingerprintServiceExt {
    default boolean authPreOperation(IBinder iBinder, String str, int i) {
        return false;
    }

    default int changeUserIdIfNeeded(int i) {
        return i;
    }

    default Handler createHandlerWithNewLooper() {
        return null;
    }

    default void dumpInternal(ServiceProvider serviceProvider, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default boolean enrollPreOperation(IBinder iBinder, String str, int i) {
        return false;
    }

    default boolean isBiometricDisabled() {
        return false;
    }

    default void notifyOperationCanceled(IFingerprintServiceReceiver iFingerprintServiceReceiver) {
    }

    default void onSystemReady() {
    }

    default boolean prepareForAuthPreOperation(IBinder iBinder, String str) {
        return false;
    }

    default void setBinderExtension(Binder binder) {
    }

    default boolean skipAuthWithPrompt(String str) {
        return false;
    }
}
