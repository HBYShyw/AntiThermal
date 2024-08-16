package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.content.Context;
import android.hardware.fingerprint.Fingerprint;
import android.os.Parcel;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFingerprintSensorExt {
    default void handleOnEnrollment(Fingerprint fingerprint, int i) {
    }

    default void init(Context context, FingerprintProvider fingerprintProvider) {
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

    default boolean onTransactFromHal(int i, Parcel parcel, Parcel parcel2, int i2) {
        return false;
    }
}
