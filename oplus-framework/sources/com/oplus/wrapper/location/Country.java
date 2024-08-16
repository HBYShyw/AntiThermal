package com.oplus.wrapper.location;

/* loaded from: classes.dex */
public class Country {
    private final android.location.Country mCountry;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Country(android.location.Country country) {
        this.mCountry = country;
    }

    public String getCountryIso() {
        return this.mCountry.getCountryIso();
    }
}
