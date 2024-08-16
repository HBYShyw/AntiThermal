package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.os.Parcel;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ISensorExt {
    default void init(Context context, FaceProvider faceProvider) {
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

    default boolean onTransactFromHal(int i, Parcel parcel, Parcel parcel2, int i2) {
        return false;
    }
}
