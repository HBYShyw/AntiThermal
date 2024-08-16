package com.android.server.location;

import com.android.server.location.provider.AbstractLocationProvider;
import com.android.server.location.provider.LocationProviderManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ILocationManagerServiceWrapper {
    default void addLocationProviderManager(LocationProviderManager locationProviderManager, AbstractLocationProvider abstractLocationProvider) {
    }

    default LocationProviderManager creatLocationProviderManager(String str) {
        return null;
    }

    default LocationProviderManager getLocationProviderManager(String str) {
        return null;
    }

    default void removeLocationProviderManager(LocationProviderManager locationProviderManager) {
    }
}
