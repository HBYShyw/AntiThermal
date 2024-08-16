package com.oplus.wrapper.location;

import android.content.Context;
import android.os.Looper;

/* loaded from: classes.dex */
public class CountryDetector {
    private final android.location.CountryDetector mCountryDetector;

    public CountryDetector(Context context) {
        this.mCountryDetector = (android.location.CountryDetector) context.getSystemService("country_detector");
    }

    public Country detectCountry() {
        android.location.Country country = this.mCountryDetector.detectCountry();
        if (country == null) {
            return null;
        }
        return new Country(country);
    }

    public void addCountryListener(final CountryListener listener, Looper looper) {
        android.location.CountryListener countryListener = null;
        if (listener != null) {
            countryListener = new android.location.CountryListener() { // from class: com.oplus.wrapper.location.CountryDetector.1
                public void onCountryDetected(android.location.Country country) {
                    if (country == null) {
                        return;
                    }
                    listener.onCountryDetected(new Country(country));
                }
            };
        }
        this.mCountryDetector.addCountryListener(countryListener, looper);
    }
}
