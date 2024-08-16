package com.android.server.timezonedetector;

import android.app.time.TimeZoneCapabilitiesAndConfig;
import android.app.time.TimeZoneConfiguration;
import android.app.timezonedetector.ManualTimeZoneSuggestion;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface TimeZoneDetectorInternal {
    MetricsTimeZoneDetectorState generateMetricsState();

    TimeZoneCapabilitiesAndConfig getCapabilitiesAndConfigForDpm();

    void handleLocationAlgorithmEvent(LocationAlgorithmEvent locationAlgorithmEvent);

    boolean setManualTimeZoneForDpm(ManualTimeZoneSuggestion manualTimeZoneSuggestion);

    boolean updateConfigurationForDpm(TimeZoneConfiguration timeZoneConfiguration);
}
