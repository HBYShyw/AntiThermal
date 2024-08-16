package com.oplus.wrapper.preference;

import android.preference.PreferenceScreen;

/* loaded from: classes.dex */
public class Preference {
    private final android.preference.Preference mPreference;

    public Preference(android.preference.Preference preference) {
        this.mPreference = preference;
    }

    public void performClick(PreferenceScreen preferenceScreen) {
        this.mPreference.performClick(preferenceScreen);
    }
}
