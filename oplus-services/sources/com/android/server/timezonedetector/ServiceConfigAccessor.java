package com.android.server.timezonedetector;

import android.app.time.TimeZoneConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;
import java.util.Optional;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ServiceConfigAccessor {
    public static final String PROVIDER_MODE_DISABLED = "disabled";
    public static final String PROVIDER_MODE_ENABLED = "enabled";

    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface ProviderMode {
    }

    void addConfigurationInternalChangeListener(StateChangeListener stateChangeListener);

    void addLocationTimeZoneManagerConfigListener(StateChangeListener stateChangeListener);

    ConfigurationInternal getConfigurationInternal(int i);

    ConfigurationInternal getCurrentUserConfigurationInternal();

    Optional<Boolean> getGeoDetectionSettingEnabledOverride();

    Duration getLocationTimeZoneProviderEventFilteringAgeThreshold();

    Duration getLocationTimeZoneProviderInitializationTimeout();

    Duration getLocationTimeZoneProviderInitializationTimeoutFuzz();

    Duration getLocationTimeZoneUncertaintyDelay();

    String getPrimaryLocationTimeZoneProviderMode();

    String getPrimaryLocationTimeZoneProviderPackageName();

    boolean getRecordStateChangesForTests();

    String getSecondaryLocationTimeZoneProviderMode();

    String getSecondaryLocationTimeZoneProviderPackageName();

    boolean isGeoDetectionEnabledForUsersByDefault();

    boolean isGeoTimeZoneDetectionFeatureSupported();

    boolean isGeoTimeZoneDetectionFeatureSupportedInConfig();

    boolean isTelephonyTimeZoneDetectionFeatureSupported();

    boolean isTestPrimaryLocationTimeZoneProvider();

    boolean isTestSecondaryLocationTimeZoneProvider();

    void removeConfigurationInternalChangeListener(StateChangeListener stateChangeListener);

    void resetVolatileTestConfig();

    void setRecordStateChangesForTests(boolean z);

    void setTestPrimaryLocationTimeZoneProviderPackageName(String str);

    void setTestSecondaryLocationTimeZoneProviderPackageName(String str);

    boolean updateConfiguration(int i, TimeZoneConfiguration timeZoneConfiguration, boolean z);
}
