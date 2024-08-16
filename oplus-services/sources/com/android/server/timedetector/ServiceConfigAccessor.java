package com.android.server.timedetector;

import android.app.time.TimeConfiguration;
import com.android.server.timezonedetector.StateChangeListener;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ServiceConfigAccessor {
    void addConfigurationInternalChangeListener(StateChangeListener stateChangeListener);

    ConfigurationInternal getConfigurationInternal(int i);

    ConfigurationInternal getCurrentUserConfigurationInternal();

    void removeConfigurationInternalChangeListener(StateChangeListener stateChangeListener);

    boolean updateConfiguration(int i, TimeConfiguration timeConfiguration, boolean z);
}
