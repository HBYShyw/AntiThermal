package com.android.internal.telephony.geoFence;

import android.location.Location;

/* loaded from: classes.dex */
public interface IOplusGeoFenceCallback {
    void onOplusFenceTransitionEvent(String str, int i, Location location);
}
