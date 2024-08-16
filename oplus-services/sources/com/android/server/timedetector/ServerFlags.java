package com.android.server.timedetector;

import android.content.Context;
import android.provider.DeviceConfig;
import android.util.ArrayMap;
import com.android.internal.annotations.GuardedBy;
import com.android.server.timezonedetector.StateChangeListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ServerFlags {
    public static final String KEY_ENHANCED_METRICS_COLLECTION_ENABLED = "enhanced_metrics_collection_enabled";
    public static final String KEY_LOCATION_TIME_ZONE_DETECTION_FEATURE_SUPPORTED = "location_time_zone_detection_feature_supported";
    public static final String KEY_LOCATION_TIME_ZONE_DETECTION_RUN_IN_BACKGROUND_ENABLED = "location_time_zone_detection_run_in_background_enabled";
    public static final String KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_DEFAULT = "location_time_zone_detection_setting_enabled_default";
    public static final String KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_OVERRIDE = "location_time_zone_detection_setting_enabled_override";
    public static final String KEY_LOCATION_TIME_ZONE_DETECTION_UNCERTAINTY_DELAY_MILLIS = "location_time_zone_detection_uncertainty_delay_millis";
    public static final String KEY_LTZP_EVENT_FILTERING_AGE_THRESHOLD_MILLIS = "ltzp_event_filtering_age_threshold_millis";
    public static final String KEY_LTZP_INITIALIZATION_TIMEOUT_FUZZ_MILLIS = "ltzp_init_timeout_fuzz_millis";
    public static final String KEY_LTZP_INITIALIZATION_TIMEOUT_MILLIS = "ltzp_init_timeout_millis";
    public static final String KEY_PRIMARY_LTZP_MODE_OVERRIDE = "primary_location_time_zone_provider_mode_override";
    public static final String KEY_SECONDARY_LTZP_MODE_OVERRIDE = "secondary_location_time_zone_provider_mode_override";
    public static final String KEY_TIME_DETECTOR_LOWER_BOUND_MILLIS_OVERRIDE = "time_detector_lower_bound_millis_override";
    public static final String KEY_TIME_DETECTOR_ORIGIN_PRIORITIES_OVERRIDE = "time_detector_origin_priorities_override";
    public static final String KEY_TIME_ZONE_DETECTOR_AUTO_DETECTION_ENABLED_DEFAULT = "time_zone_detector_auto_detection_enabled_default";
    public static final String KEY_TIME_ZONE_DETECTOR_TELEPHONY_FALLBACK_SUPPORTED = "time_zone_detector_telephony_fallback_supported";

    @GuardedBy({"SLOCK"})
    private static ServerFlags sInstance;

    @GuardedBy({"mListeners"})
    private final ArrayMap<StateChangeListener, HashSet<String>> mListeners = new ArrayMap<>();
    private static final Optional<Boolean> OPTIONAL_TRUE = Optional.of(Boolean.TRUE);
    private static final Optional<Boolean> OPTIONAL_FALSE = Optional.of(Boolean.FALSE);
    private static final Object SLOCK = new Object();

    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface DeviceConfigKey {
    }

    private ServerFlags(Context context) {
        DeviceConfig.addOnPropertiesChangedListener("system_time", context.getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.timedetector.ServerFlags$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                ServerFlags.this.handlePropertiesChanged(properties);
            }
        });
    }

    public static ServerFlags getInstance(Context context) {
        ServerFlags serverFlags;
        synchronized (SLOCK) {
            if (sInstance == null) {
                sInstance = new ServerFlags(context);
            }
            serverFlags = sInstance;
        }
        return serverFlags;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePropertiesChanged(DeviceConfig.Properties properties) {
        ArrayList arrayList;
        synchronized (this.mListeners) {
            arrayList = new ArrayList(this.mListeners.size());
            for (Map.Entry<StateChangeListener, HashSet<String>> entry : this.mListeners.entrySet()) {
                if (containsAny(entry.getValue(), properties.getKeyset())) {
                    arrayList.add(entry.getKey());
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((StateChangeListener) it.next()).onChange();
        }
    }

    private static boolean containsAny(Set<String> set, Iterable<String> iterable) {
        Iterator<String> it = iterable.iterator();
        while (it.hasNext()) {
            if (set.contains(it.next())) {
                return true;
            }
        }
        return false;
    }

    public void addListener(StateChangeListener stateChangeListener, Set<String> set) {
        Objects.requireNonNull(stateChangeListener);
        Objects.requireNonNull(set);
        HashSet<String> hashSet = new HashSet<>(set);
        synchronized (this.mListeners) {
            this.mListeners.put(stateChangeListener, hashSet);
        }
    }

    public Optional<String> getOptionalString(String str) {
        return Optional.ofNullable(DeviceConfig.getProperty("system_time", str));
    }

    public Optional<String[]> getOptionalStringArray(String str) {
        Optional<String> optionalString = getOptionalString(str);
        if (!optionalString.isPresent()) {
            return Optional.empty();
        }
        String str2 = optionalString.get();
        if ("_[]_".equals(str2)) {
            return Optional.of(new String[0]);
        }
        return Optional.of(str2.split(","));
    }

    public Optional<Instant> getOptionalInstant(String str) {
        String property = DeviceConfig.getProperty("system_time", str);
        if (property == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Instant.ofEpochMilli(Long.parseLong(property)));
        } catch (NumberFormatException | DateTimeException unused) {
            return Optional.empty();
        }
    }

    public Optional<Boolean> getOptionalBoolean(String str) {
        return parseOptionalBoolean(DeviceConfig.getProperty("system_time", str));
    }

    private static Optional<Boolean> parseOptionalBoolean(String str) {
        if (str == null) {
            return Optional.empty();
        }
        return Boolean.parseBoolean(str) ? OPTIONAL_TRUE : OPTIONAL_FALSE;
    }

    public boolean getBoolean(String str, boolean z) {
        return DeviceConfig.getBoolean("system_time", str, z);
    }

    public Duration getDurationFromMillis(String str, Duration duration) {
        long j = DeviceConfig.getLong("system_time", str, -1L);
        return j < 0 ? duration : Duration.ofMillis(j);
    }
}
