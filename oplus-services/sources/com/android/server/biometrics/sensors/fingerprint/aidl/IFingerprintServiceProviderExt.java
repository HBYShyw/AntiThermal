package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.content.Context;
import android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint;
import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.ISidefpsController;
import android.hardware.fingerprint.IUdfpsOverlay;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.sensors.SensorList;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFingerprintServiceProviderExt {
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

    default Handler createHandlerWithNewLooper() {
        return null;
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

    default void init(Context context, String str, Handler handler, SensorList<Sensor> sensorList, BiometricContext biometricContext, FingerprintProvider fingerprintProvider) {
    }

    default boolean isClientCanAuth(IBinder iBinder, String str) {
        return false;
    }

    default boolean isIconShow() {
        return false;
    }

    default void notifyHalReady() {
    }

    default boolean onAcquired(int i, int i2) {
        return false;
    }

    default boolean onAuthenticated(int i, int i2, int i3, ArrayList<Byte> arrayList) {
        return false;
    }

    default boolean onError(int i, int i2) {
        return false;
    }

    default void onLockoutPermanent() {
    }

    default void onLockoutTimed() {
    }

    default void onSystemReady() {
    }

    default boolean onTransactFromHal(int i, Parcel parcel, Parcel parcel2, int i2) {
        return false;
    }

    default int pauseEnroll(int i) {
        return -1;
    }

    default void resetFingerprintLockout(byte[] bArr, int i) {
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
