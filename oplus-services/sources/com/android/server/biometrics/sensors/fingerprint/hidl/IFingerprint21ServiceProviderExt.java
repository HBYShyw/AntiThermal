package com.android.server.biometrics.sensors.fingerprint.hidl;

import android.content.Context;
import android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint;
import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.ISidefpsController;
import android.hardware.fingerprint.IUdfpsOverlay;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.os.Handler;
import android.os.IBinder;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.sensors.BiometricScheduler;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFingerprint21ServiceProviderExt {
    default void authPreOperation(IBinder iBinder, String str) {
    }

    default void cancelFingerprintExtraInfo(IBinder iBinder, String str, long j) {
    }

    default void cancelTouchEventListener(IBinder iBinder, String str, long j) {
    }

    default void closeFingerKeymodeIfOpen() {
    }

    default int continueEnroll(int i) {
        return -1;
    }

    default boolean dispatchOnAcquired(long j, int i, int i2) {
        return false;
    }

    default boolean dispatchOnAuthenticated(long j, int i, int i2, ArrayList<Byte> arrayList) {
        return false;
    }

    default boolean dispatchOnError(long j, int i, int i2) {
        return false;
    }

    default void dumpInternal(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default void enrollPreOperation(IBinder iBinder, String str, int i) {
    }

    default int getEnrollmentTotalTimes(int i) {
        return -1;
    }

    default int getFailedAttempts() {
        return -1;
    }

    default long getLockoutAttemptDeadline(int i) {
        return -1L;
    }

    default void handleCancelAuthentication(int i, IBinder iBinder) {
    }

    default void handleCancelEnrollment(int i, IBinder iBinder) {
    }

    default void handleOnEnrollment(Fingerprint fingerprint, int i) {
    }

    default void handleOnFingerprintCmd(int i, ArrayList<Byte> arrayList, int i2) {
    }

    default boolean handleOnPointerDown() {
        return false;
    }

    default boolean handleOnPointerUp() {
        return false;
    }

    default void handleRemove(int i, int i2, String str, int i3) {
    }

    default boolean handleServiceDied() {
        return false;
    }

    default void hideFingerprintIcon(IBinder iBinder, String str) {
    }

    default void init(Context context, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, LockoutFrameworkImpl lockoutFrameworkImpl, BiometricScheduler biometricScheduler, Handler handler, int i, Supplier<IBiometricsFingerprint> supplier, BiometricContext biometricContext, Fingerprint21 fingerprint21) {
    }

    default boolean isClientCanAuth(IBinder iBinder, String str) {
        return false;
    }

    default boolean isIconShow() {
        return false;
    }

    default void onSystemReady() {
    }

    default int pauseEnroll(int i) {
        return -1;
    }

    default int sendFingerprintCmd(int i, byte[] bArr) {
        return -1;
    }

    default void setFingerKeymode(int i, int i2) {
    }

    default void setOplusCallback(IBiometricsFingerprint iBiometricsFingerprint) {
    }

    default void setScreenOffStateEarlyForkeyguardAuth() {
    }

    default void setSidefpsController(ISidefpsController iSidefpsController) {
    }

    default void setUdfpsOverlay(IUdfpsOverlay iUdfpsOverlay) {
    }

    default void setUdfpsOverlayController(IUdfpsOverlayController iUdfpsOverlayController) {
    }

    default void showFingerprintIcon(IBinder iBinder, String str) {
    }

    default void userSwitchNotice(Context context) {
    }
}
