package com.android.server.biometrics.sensors.fingerprint;

import android.content.Context;
import android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint;
import android.hardware.fingerprint.FingerprintManager;
import android.os.IHwInterface;
import android.os.RemoteException;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UdfpsHelper {
    private static final String TAG = "UdfpsHelper";

    public static void onFingerDown(IBiometricsFingerprint iBiometricsFingerprint, int i, int i2, float f, float f2) {
        android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint castFrom = android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.castFrom((IHwInterface) iBiometricsFingerprint);
        if (castFrom == null) {
            Slog.v(TAG, "onFingerDown | failed to cast the HIDL to V2_3");
            return;
        }
        try {
            castFrom.onFingerDown(i, i2, f, f2);
        } catch (RemoteException e) {
            Slog.e(TAG, "onFingerDown | RemoteException: ", e);
        }
    }

    public static void onFingerUp(IBiometricsFingerprint iBiometricsFingerprint) {
        android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint castFrom = android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.castFrom((IHwInterface) iBiometricsFingerprint);
        if (castFrom == null) {
            Slog.v(TAG, "onFingerUp | failed to cast the HIDL to V2_3");
            return;
        }
        try {
            castFrom.onFingerUp();
        } catch (RemoteException e) {
            Slog.e(TAG, "onFingerUp | RemoteException: ", e);
        }
    }

    public static boolean isValidAcquisitionMessage(Context context, int i, int i2) {
        return FingerprintManager.getAcquiredString(context, i, i2) != null;
    }
}
