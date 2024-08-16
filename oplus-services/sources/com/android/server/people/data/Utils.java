package com.android.server.people.data;

import android.content.Context;
import android.location.Country;
import android.location.CountryDetector;
import java.util.Locale;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class Utils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getCurrentCountryIso(Context context) {
        Country detectCountry;
        CountryDetector countryDetector = (CountryDetector) context.getSystemService("country_detector");
        String countryIso = (countryDetector == null || (detectCountry = countryDetector.detectCountry()) == null) ? null : detectCountry.getCountryIso();
        return countryIso == null ? Locale.getDefault().getCountry() : countryIso;
    }

    private Utils() {
    }
}
