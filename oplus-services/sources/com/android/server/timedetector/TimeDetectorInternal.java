package com.android.server.timedetector;

import android.app.time.TimeCapabilitiesAndConfig;
import android.app.time.TimeConfiguration;
import android.app.timedetector.ManualTimeSuggestion;
import com.android.server.timezonedetector.StateChangeListener;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface TimeDetectorInternal {
    void addNetworkTimeUpdateListener(StateChangeListener stateChangeListener);

    TimeCapabilitiesAndConfig getCapabilitiesAndConfigForDpm();

    NetworkTimeSuggestion getLatestNetworkSuggestion();

    boolean setManualTimeForDpm(ManualTimeSuggestion manualTimeSuggestion);

    void suggestGnssTime(GnssTimeSuggestion gnssTimeSuggestion);

    void suggestNetworkTime(NetworkTimeSuggestion networkTimeSuggestion);

    boolean updateConfigurationForDpm(TimeConfiguration timeConfiguration);
}
