package com.oplus.wrapper.location;

import android.location.LocationRequest;
import android.os.WorkSource;

/* loaded from: classes.dex */
public class LocationRequest {
    private final android.location.LocationRequest mLocationRequest;

    public LocationRequest(android.location.LocationRequest locationRequest) {
        this.mLocationRequest = locationRequest;
    }

    public boolean isLocationSettingsIgnored() {
        return this.mLocationRequest.isLocationSettingsIgnored();
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private LocationRequest.Builder mBuilder;

        public Builder(long intervalMillis) {
            this.mBuilder = new LocationRequest.Builder(intervalMillis);
        }

        public Builder setHiddenFromAppOps(boolean hiddenFromAppOps) {
            this.mBuilder.setHiddenFromAppOps(hiddenFromAppOps);
            return this;
        }

        public Builder setLocationSettingsIgnored(boolean locationSettingsIgnored) {
            this.mBuilder.setLocationSettingsIgnored(locationSettingsIgnored);
            return this;
        }

        public Builder setLowPower(boolean lowPower) {
            this.mBuilder.setLowPower(lowPower);
            return this;
        }

        public Builder setWorkSource(WorkSource workSource) {
            this.mBuilder.setWorkSource(workSource);
            return this;
        }

        public Builder setQuality(int quality) {
            this.mBuilder.setQuality(quality);
            return this;
        }

        public android.location.LocationRequest build() {
            return this.mBuilder.build();
        }
    }
}
