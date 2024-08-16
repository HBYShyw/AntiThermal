package com.oplus.wrapper.location;

import android.os.UserHandle;

/* loaded from: classes.dex */
public class LocationManager {
    private final android.location.LocationManager mLocationManager;

    public LocationManager(android.location.LocationManager locationManager) {
        this.mLocationManager = locationManager;
    }

    public boolean isLocationEnabledForUser(UserHandle userHandle) {
        return this.mLocationManager.isLocationEnabledForUser(userHandle);
    }

    public void setLocationEnabledForUser(boolean enabled, UserHandle userHandle) {
        this.mLocationManager.setLocationEnabledForUser(enabled, userHandle);
    }

    public void injectGnssMeasurementCorrections(GnssMeasurementCorrections measurementCorrections) {
        this.mLocationManager.injectGnssMeasurementCorrections(measurementCorrections.get());
    }
}
