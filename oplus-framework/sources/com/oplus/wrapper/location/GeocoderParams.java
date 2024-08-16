package com.oplus.wrapper.location;

import java.util.Locale;

/* loaded from: classes.dex */
public class GeocoderParams {
    private final android.location.GeocoderParams mGeocoderParams;

    public GeocoderParams(android.location.GeocoderParams geocoderParams) {
        this.mGeocoderParams = geocoderParams;
    }

    public android.location.GeocoderParams get() {
        return this.mGeocoderParams;
    }

    public String getClientPackage() {
        return this.mGeocoderParams.getClientPackage();
    }

    public Locale getLocale() {
        return this.mGeocoderParams.getLocale();
    }
}
