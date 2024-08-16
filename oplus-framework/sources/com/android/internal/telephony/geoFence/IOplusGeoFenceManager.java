package com.android.internal.telephony.geoFence;

import android.content.Context;
import android.location.Location;
import android.os.Message;
import android.telephony.Rlog;

/* loaded from: classes.dex */
public interface IOplusGeoFenceManager {
    public static final String TAG = IOplusGeoFenceManager.class.getSimpleName();
    public static final IOplusGeoFenceManager DEFAULT = new IOplusGeoFenceManager() { // from class: com.android.internal.telephony.geoFence.IOplusGeoFenceManager.1
    };

    default void initialOnce(Context context) {
        Rlog.d(TAG, "OplusGeoFenceManager Not Support");
    }

    default void addOplusGeoFenceToLs(String gfname, int radius, Location location, Message onComplete) {
    }

    default void removeOplusGeoFenceFromLs(String gfname) {
    }

    default void registerGfEventCallBack(IOplusGeoFenceCallback cb) {
    }

    default void unregisterGfEventCallBack(IOplusGeoFenceCallback cb) {
    }
}
