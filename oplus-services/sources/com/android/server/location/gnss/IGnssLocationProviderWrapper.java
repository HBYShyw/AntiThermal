package com.android.server.location.gnss;

import android.location.LocationResult;
import android.os.WorkSource;
import com.android.server.location.gnss.hal.GnssNative;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IGnssLocationProviderWrapper {
    default GnssNative getGnssNative() {
        return null;
    }

    default void reportLocation(LocationResult locationResult) {
    }

    default void startNavigating() {
    }

    default void stopNavigating() {
    }

    default void subscriptionOrCarrierConfigChanged() {
    }

    default void updateClientUids(WorkSource workSource) {
    }
}
