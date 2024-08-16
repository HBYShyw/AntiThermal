package com.android.server.timezonedetector;

import android.R;
import android.app.ActivityManagerInternal;
import android.app.time.TimeZoneConfiguration;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import com.android.internal.annotations.GuardedBy;
import com.android.server.LocalServices;
import com.android.server.timedetector.ServerFlags;
import com.android.server.timezonedetector.ConfigurationInternal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ServiceConfigAccessorImpl implements ServiceConfigAccessor {

    @GuardedBy({"SLOCK"})
    private static ServiceConfigAccessor sInstance;

    @GuardedBy({"this"})
    private final List<StateChangeListener> mConfigurationInternalListeners = new ArrayList();
    private final Context mContext;
    private final ContentResolver mCr;
    private final LocationManager mLocationManager;

    @GuardedBy({"this"})
    private boolean mRecordStateChangesForTests;
    private final ServerFlags mServerFlags;

    @GuardedBy({"this"})
    private String mTestPrimaryLocationTimeZoneProviderMode;

    @GuardedBy({"this"})
    private String mTestPrimaryLocationTimeZoneProviderPackageName;

    @GuardedBy({"this"})
    private String mTestSecondaryLocationTimeZoneProviderMode;

    @GuardedBy({"this"})
    private String mTestSecondaryLocationTimeZoneProviderPackageName;
    private final UserManager mUserManager;
    private static final Set<String> CONFIGURATION_INTERNAL_SERVER_FLAGS_KEYS_TO_WATCH = Set.of(ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_FEATURE_SUPPORTED, ServerFlags.KEY_PRIMARY_LTZP_MODE_OVERRIDE, ServerFlags.KEY_SECONDARY_LTZP_MODE_OVERRIDE, ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_RUN_IN_BACKGROUND_ENABLED, ServerFlags.KEY_ENHANCED_METRICS_COLLECTION_ENABLED, ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_DEFAULT, ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_OVERRIDE, ServerFlags.KEY_TIME_ZONE_DETECTOR_AUTO_DETECTION_ENABLED_DEFAULT, ServerFlags.KEY_TIME_ZONE_DETECTOR_TELEPHONY_FALLBACK_SUPPORTED);
    private static final Set<String> LOCATION_TIME_ZONE_MANAGER_SERVER_FLAGS_KEYS_TO_WATCH = Set.of(ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_FEATURE_SUPPORTED, ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_RUN_IN_BACKGROUND_ENABLED, ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_DEFAULT, ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_OVERRIDE, ServerFlags.KEY_PRIMARY_LTZP_MODE_OVERRIDE, ServerFlags.KEY_SECONDARY_LTZP_MODE_OVERRIDE, ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_MILLIS, ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_FUZZ_MILLIS, ServerFlags.KEY_LTZP_EVENT_FILTERING_AGE_THRESHOLD_MILLIS, ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_UNCERTAINTY_DELAY_MILLIS);
    private static final Duration DEFAULT_LTZP_INITIALIZATION_TIMEOUT = Duration.ofMinutes(5);
    private static final Duration DEFAULT_LTZP_INITIALIZATION_TIMEOUT_FUZZ = Duration.ofMinutes(1);
    private static final Duration DEFAULT_LTZP_UNCERTAINTY_DELAY = Duration.ofMinutes(5);
    private static final Duration DEFAULT_LTZP_EVENT_FILTER_AGE_THRESHOLD = Duration.ofMinutes(1);
    private static final Object SLOCK = new Object();

    private ServiceConfigAccessorImpl(Context context) {
        Objects.requireNonNull(context);
        this.mContext = context;
        this.mCr = context.getContentResolver();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mLocationManager = (LocationManager) context.getSystemService(LocationManager.class);
        ServerFlags serverFlags = ServerFlags.getInstance(context);
        this.mServerFlags = serverFlags;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.location.MODE_CHANGED");
        context.registerReceiverForAllUsers(new BroadcastReceiver() { // from class: com.android.server.timezonedetector.ServiceConfigAccessorImpl.1
            AnonymousClass1() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        }, intentFilter, null, null);
        ContentResolver contentResolver = context.getContentResolver();
        AnonymousClass2 anonymousClass2 = new ContentObserver(context.getMainThreadHandler()) { // from class: com.android.server.timezonedetector.ServiceConfigAccessorImpl.2
            AnonymousClass2(Handler handler) {
                super(handler);
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        };
        contentResolver.registerContentObserver(Settings.Global.getUriFor("auto_time_zone"), true, anonymousClass2);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("auto_time_zone_explicit"), true, anonymousClass2);
        contentResolver.registerContentObserver(Settings.Secure.getUriFor("location_time_zone_detection_enabled"), true, anonymousClass2, -1);
        serverFlags.addListener(new StateChangeListener() { // from class: com.android.server.timezonedetector.ServiceConfigAccessorImpl$$ExternalSyntheticLambda0
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        }, CONFIGURATION_INTERNAL_SERVER_FLAGS_KEYS_TO_WATCH);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.timezonedetector.ServiceConfigAccessorImpl$1 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context2, Intent intent) {
            ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.timezonedetector.ServiceConfigAccessorImpl$2 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass2 extends ContentObserver {
        AnonymousClass2(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
        }
    }

    public static ServiceConfigAccessor getInstance(Context context) {
        ServiceConfigAccessor serviceConfigAccessor;
        synchronized (SLOCK) {
            if (sInstance == null) {
                sInstance = new ServiceConfigAccessorImpl(context);
            }
            serviceConfigAccessor = sInstance;
        }
        return serviceConfigAccessor;
    }

    public void handleConfigurationInternalChangeOnMainThread() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.mConfigurationInternalListeners);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((StateChangeListener) it.next()).onChange();
        }
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void addConfigurationInternalChangeListener(StateChangeListener stateChangeListener) {
        List<StateChangeListener> list = this.mConfigurationInternalListeners;
        Objects.requireNonNull(stateChangeListener);
        list.add(stateChangeListener);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void removeConfigurationInternalChangeListener(StateChangeListener stateChangeListener) {
        List<StateChangeListener> list = this.mConfigurationInternalListeners;
        Objects.requireNonNull(stateChangeListener);
        list.remove(stateChangeListener);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized ConfigurationInternal getCurrentUserConfigurationInternal() {
        return getConfigurationInternal(((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getCurrentUserId());
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized boolean updateConfiguration(int i, TimeZoneConfiguration timeZoneConfiguration, boolean z) {
        Objects.requireNonNull(timeZoneConfiguration);
        ConfigurationInternal configurationInternal = getConfigurationInternal(i);
        TimeZoneConfiguration tryApplyConfigChanges = configurationInternal.asCapabilities(z).tryApplyConfigChanges(configurationInternal.asConfiguration(), timeZoneConfiguration);
        if (tryApplyConfigChanges == null) {
            return false;
        }
        storeConfiguration(i, timeZoneConfiguration, tryApplyConfigChanges);
        return true;
    }

    @GuardedBy({"this"})
    private void storeConfiguration(int i, TimeZoneConfiguration timeZoneConfiguration, TimeZoneConfiguration timeZoneConfiguration2) {
        Objects.requireNonNull(timeZoneConfiguration2);
        if (isAutoDetectionFeatureSupported()) {
            if (timeZoneConfiguration.hasIsAutoDetectionEnabled()) {
                Settings.Global.putInt(this.mCr, "auto_time_zone_explicit", 1);
            }
            setAutoDetectionEnabledIfRequired(timeZoneConfiguration2.isAutoDetectionEnabled());
            if (getGeoDetectionSettingEnabledOverride().isEmpty() && isGeoTimeZoneDetectionFeatureSupported() && isTelephonyTimeZoneDetectionFeatureSupported()) {
                setGeoDetectionEnabledSettingIfRequired(i, timeZoneConfiguration2.isGeoDetectionEnabled());
            }
        }
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized ConfigurationInternal getConfigurationInternal(int i) {
        return new ConfigurationInternal.Builder().setUserId(i).setTelephonyDetectionFeatureSupported(isTelephonyTimeZoneDetectionFeatureSupported()).setGeoDetectionFeatureSupported(isGeoTimeZoneDetectionFeatureSupported()).setTelephonyFallbackSupported(isTelephonyFallbackSupported()).setGeoDetectionRunInBackgroundEnabled(getGeoDetectionRunInBackgroundEnabled()).setEnhancedMetricsCollectionEnabled(isEnhancedMetricsCollectionEnabled()).setAutoDetectionEnabledSetting(getAutoDetectionEnabledSetting()).setUserConfigAllowed(isUserConfigAllowed(i)).setLocationEnabledSetting(getLocationEnabledSetting(i)).setGeoDetectionEnabledSetting(getGeoDetectionEnabledSetting(i)).build();
    }

    private void setAutoDetectionEnabledIfRequired(boolean z) {
        if (getAutoDetectionEnabledSetting() != z) {
            Settings.Global.putInt(this.mCr, "auto_time_zone", z ? 1 : 0);
        }
    }

    private boolean getLocationEnabledSetting(int i) {
        return this.mLocationManager.isLocationEnabledForUser(UserHandle.of(i));
    }

    private boolean isUserConfigAllowed(int i) {
        return !this.mUserManager.hasUserRestriction("no_config_date_time", UserHandle.of(i));
    }

    private boolean getAutoDetectionEnabledSetting() {
        boolean z = Settings.Global.getInt(this.mCr, "auto_time_zone", 1) > 0;
        Optional<Boolean> optionalBoolean = this.mServerFlags.getOptionalBoolean(ServerFlags.KEY_TIME_ZONE_DETECTOR_AUTO_DETECTION_ENABLED_DEFAULT);
        if (!optionalBoolean.isPresent() || Settings.Global.getInt(this.mCr, "auto_time_zone_explicit", 0) != 0) {
            return z;
        }
        boolean booleanValue = optionalBoolean.get().booleanValue();
        if (booleanValue != z) {
            Settings.Global.putInt(this.mCr, "auto_time_zone", booleanValue ? 1 : 0);
        }
        return booleanValue;
    }

    private boolean getGeoDetectionEnabledSetting(int i) {
        Optional<Boolean> geoDetectionSettingEnabledOverride = getGeoDetectionSettingEnabledOverride();
        if (geoDetectionSettingEnabledOverride.isPresent()) {
            return geoDetectionSettingEnabledOverride.get().booleanValue();
        }
        return Settings.Secure.getIntForUser(this.mCr, "location_time_zone_detection_enabled", isGeoDetectionEnabledForUsersByDefault() ? 1 : 0, i) != 0;
    }

    private void setGeoDetectionEnabledSettingIfRequired(int i, boolean z) {
        if (getGeoDetectionEnabledSetting(i) != z) {
            Settings.Secure.putIntForUser(this.mCr, "location_time_zone_detection_enabled", z ? 1 : 0, i);
        }
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public void addLocationTimeZoneManagerConfigListener(StateChangeListener stateChangeListener) {
        this.mServerFlags.addListener(stateChangeListener, LOCATION_TIME_ZONE_MANAGER_SERVER_FLAGS_KEYS_TO_WATCH);
    }

    private boolean isAutoDetectionFeatureSupported() {
        return isTelephonyTimeZoneDetectionFeatureSupported() || isGeoTimeZoneDetectionFeatureSupported();
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public boolean isTelephonyTimeZoneDetectionFeatureSupported() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony");
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public boolean isGeoTimeZoneDetectionFeatureSupportedInConfig() {
        return this.mContext.getResources().getBoolean(17891663);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public boolean isGeoTimeZoneDetectionFeatureSupported() {
        return isGeoTimeZoneDetectionFeatureSupportedInConfig() && isGeoTimeZoneDetectionFeatureSupportedInternal() && atLeastOneProviderIsEnabled();
    }

    private boolean atLeastOneProviderIsEnabled() {
        return (Objects.equals(getPrimaryLocationTimeZoneProviderMode(), ServiceConfigAccessor.PROVIDER_MODE_DISABLED) && Objects.equals(getSecondaryLocationTimeZoneProviderMode(), ServiceConfigAccessor.PROVIDER_MODE_DISABLED)) ? false : true;
    }

    private boolean isGeoTimeZoneDetectionFeatureSupportedInternal() {
        return this.mServerFlags.getBoolean(ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_FEATURE_SUPPORTED, true);
    }

    private boolean getGeoDetectionRunInBackgroundEnabled() {
        return this.mServerFlags.getBoolean(ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_RUN_IN_BACKGROUND_ENABLED, false);
    }

    private boolean isEnhancedMetricsCollectionEnabled() {
        return this.mServerFlags.getBoolean(ServerFlags.KEY_ENHANCED_METRICS_COLLECTION_ENABLED, false);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized String getPrimaryLocationTimeZoneProviderPackageName() {
        if (this.mTestPrimaryLocationTimeZoneProviderMode != null) {
            return this.mTestPrimaryLocationTimeZoneProviderPackageName;
        }
        return this.mContext.getResources().getString(R.string.date_time_done);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void setTestPrimaryLocationTimeZoneProviderPackageName(String str) {
        this.mTestPrimaryLocationTimeZoneProviderPackageName = str;
        this.mTestPrimaryLocationTimeZoneProviderMode = str == null ? ServiceConfigAccessor.PROVIDER_MODE_DISABLED : ServiceConfigAccessor.PROVIDER_MODE_ENABLED;
        this.mContext.getMainThreadHandler().post(new ServiceConfigAccessorImpl$$ExternalSyntheticLambda1(this));
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized boolean isTestPrimaryLocationTimeZoneProvider() {
        return this.mTestPrimaryLocationTimeZoneProviderMode != null;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized String getSecondaryLocationTimeZoneProviderPackageName() {
        if (this.mTestSecondaryLocationTimeZoneProviderMode != null) {
            return this.mTestSecondaryLocationTimeZoneProviderPackageName;
        }
        return this.mContext.getResources().getString(R.string.demo_starting_message);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void setTestSecondaryLocationTimeZoneProviderPackageName(String str) {
        this.mTestSecondaryLocationTimeZoneProviderPackageName = str;
        this.mTestSecondaryLocationTimeZoneProviderMode = str == null ? ServiceConfigAccessor.PROVIDER_MODE_DISABLED : ServiceConfigAccessor.PROVIDER_MODE_ENABLED;
        this.mContext.getMainThreadHandler().post(new ServiceConfigAccessorImpl$$ExternalSyntheticLambda1(this));
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized boolean isTestSecondaryLocationTimeZoneProvider() {
        return this.mTestSecondaryLocationTimeZoneProviderMode != null;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void setRecordStateChangesForTests(boolean z) {
        this.mRecordStateChangesForTests = z;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized boolean getRecordStateChangesForTests() {
        return this.mRecordStateChangesForTests;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized String getPrimaryLocationTimeZoneProviderMode() {
        String str = this.mTestPrimaryLocationTimeZoneProviderMode;
        if (str != null) {
            return str;
        }
        return this.mServerFlags.getOptionalString(ServerFlags.KEY_PRIMARY_LTZP_MODE_OVERRIDE).orElse(getPrimaryLocationTimeZoneProviderModeFromConfig());
    }

    private synchronized String getPrimaryLocationTimeZoneProviderModeFromConfig() {
        return getConfigBoolean(17891675) ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized String getSecondaryLocationTimeZoneProviderMode() {
        String str = this.mTestSecondaryLocationTimeZoneProviderMode;
        if (str != null) {
            return str;
        }
        return this.mServerFlags.getOptionalString(ServerFlags.KEY_SECONDARY_LTZP_MODE_OVERRIDE).orElse(getSecondaryLocationTimeZoneProviderModeFromConfig());
    }

    private synchronized String getSecondaryLocationTimeZoneProviderModeFromConfig() {
        return getConfigBoolean(17891678) ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public boolean isGeoDetectionEnabledForUsersByDefault() {
        return this.mServerFlags.getBoolean(ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_DEFAULT, false);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public Optional<Boolean> getGeoDetectionSettingEnabledOverride() {
        return this.mServerFlags.getOptionalBoolean(ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_OVERRIDE);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public Duration getLocationTimeZoneProviderInitializationTimeout() {
        return this.mServerFlags.getDurationFromMillis(ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_MILLIS, DEFAULT_LTZP_INITIALIZATION_TIMEOUT);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public Duration getLocationTimeZoneProviderInitializationTimeoutFuzz() {
        return this.mServerFlags.getDurationFromMillis(ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_FUZZ_MILLIS, DEFAULT_LTZP_INITIALIZATION_TIMEOUT_FUZZ);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public Duration getLocationTimeZoneUncertaintyDelay() {
        return this.mServerFlags.getDurationFromMillis(ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_UNCERTAINTY_DELAY_MILLIS, DEFAULT_LTZP_UNCERTAINTY_DELAY);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public Duration getLocationTimeZoneProviderEventFilteringAgeThreshold() {
        return this.mServerFlags.getDurationFromMillis(ServerFlags.KEY_LTZP_EVENT_FILTERING_AGE_THRESHOLD_MILLIS, DEFAULT_LTZP_EVENT_FILTER_AGE_THRESHOLD);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void resetVolatileTestConfig() {
        this.mTestPrimaryLocationTimeZoneProviderPackageName = null;
        this.mTestPrimaryLocationTimeZoneProviderMode = null;
        this.mTestSecondaryLocationTimeZoneProviderPackageName = null;
        this.mTestSecondaryLocationTimeZoneProviderMode = null;
        this.mRecordStateChangesForTests = false;
        this.mContext.getMainThreadHandler().post(new ServiceConfigAccessorImpl$$ExternalSyntheticLambda1(this));
    }

    private boolean isTelephonyFallbackSupported() {
        return this.mServerFlags.getBoolean(ServerFlags.KEY_TIME_ZONE_DETECTOR_TELEPHONY_FALLBACK_SUPPORTED, getConfigBoolean(17891849));
    }

    private boolean getConfigBoolean(int i) {
        return this.mContext.getResources().getBoolean(i);
    }
}
