package com.android.server.timezonedetector;

import android.app.time.TimeZoneCapabilities;
import android.app.time.TimeZoneConfiguration;
import android.os.UserHandle;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ConfigurationInternal {
    public static final int DETECTION_MODE_GEO = 2;
    public static final int DETECTION_MODE_MANUAL = 1;
    public static final int DETECTION_MODE_TELEPHONY = 3;
    public static final int DETECTION_MODE_UNKNOWN = 0;
    private final boolean mAutoDetectionEnabledSetting;
    private final boolean mEnhancedMetricsCollectionEnabled;
    private final boolean mGeoDetectionEnabledSetting;
    private final boolean mGeoDetectionRunInBackgroundEnabled;
    private final boolean mGeoDetectionSupported;
    private final boolean mLocationEnabledSetting;
    private final boolean mTelephonyDetectionSupported;
    private final boolean mTelephonyFallbackSupported;
    private final boolean mUserConfigAllowed;
    private final int mUserId;

    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface DetectionMode {
    }

    private ConfigurationInternal(Builder builder) {
        this.mTelephonyDetectionSupported = builder.mTelephonyDetectionSupported;
        this.mGeoDetectionSupported = builder.mGeoDetectionSupported;
        this.mTelephonyFallbackSupported = builder.mTelephonyFallbackSupported;
        this.mGeoDetectionRunInBackgroundEnabled = builder.mGeoDetectionRunInBackgroundEnabled;
        this.mEnhancedMetricsCollectionEnabled = builder.mEnhancedMetricsCollectionEnabled;
        this.mAutoDetectionEnabledSetting = builder.mAutoDetectionEnabledSetting;
        Integer num = builder.mUserId;
        Objects.requireNonNull(num, "userId must be set");
        this.mUserId = num.intValue();
        this.mUserConfigAllowed = builder.mUserConfigAllowed;
        this.mLocationEnabledSetting = builder.mLocationEnabledSetting;
        this.mGeoDetectionEnabledSetting = builder.mGeoDetectionEnabledSetting;
    }

    public boolean isAutoDetectionSupported() {
        return this.mTelephonyDetectionSupported || this.mGeoDetectionSupported;
    }

    public boolean isTelephonyDetectionSupported() {
        return this.mTelephonyDetectionSupported;
    }

    public boolean isGeoDetectionSupported() {
        return this.mGeoDetectionSupported;
    }

    public boolean isTelephonyFallbackSupported() {
        return this.mTelephonyFallbackSupported;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getGeoDetectionRunInBackgroundEnabledSetting() {
        return this.mGeoDetectionRunInBackgroundEnabled;
    }

    public boolean isEnhancedMetricsCollectionEnabled() {
        return this.mEnhancedMetricsCollectionEnabled;
    }

    public boolean getAutoDetectionEnabledSetting() {
        return this.mAutoDetectionEnabledSetting;
    }

    public boolean getAutoDetectionEnabledBehavior() {
        return isAutoDetectionSupported() && getAutoDetectionEnabledSetting();
    }

    public int getUserId() {
        return this.mUserId;
    }

    public UserHandle getUserHandle() {
        return UserHandle.of(this.mUserId);
    }

    public boolean isUserConfigAllowed() {
        return this.mUserConfigAllowed;
    }

    public boolean getLocationEnabledSetting() {
        return this.mLocationEnabledSetting;
    }

    public boolean getGeoDetectionEnabledSetting() {
        return this.mGeoDetectionEnabledSetting;
    }

    public int getDetectionMode() {
        if (!isAutoDetectionSupported() || !getAutoDetectionEnabledSetting()) {
            return 1;
        }
        if (getGeoDetectionEnabledBehavior()) {
            return 2;
        }
        return isTelephonyDetectionSupported() ? 3 : 0;
    }

    private boolean getGeoDetectionEnabledBehavior() {
        if (!isGeoDetectionSupported() || !getLocationEnabledSetting()) {
            return false;
        }
        if (isTelephonyDetectionSupported()) {
            return getGeoDetectionEnabledSetting();
        }
        return true;
    }

    public boolean isGeoDetectionExecutionEnabled() {
        return getDetectionMode() == 2 || getGeoDetectionRunInBackgroundEnabledBehavior();
    }

    private boolean getGeoDetectionRunInBackgroundEnabledBehavior() {
        return isGeoDetectionSupported() && getLocationEnabledSetting() && getAutoDetectionEnabledSetting() && getGeoDetectionRunInBackgroundEnabledSetting();
    }

    public TimeZoneCapabilities asCapabilities(boolean z) {
        TimeZoneCapabilities.Builder builder = new TimeZoneCapabilities.Builder(UserHandle.of(this.mUserId));
        boolean z2 = isUserConfigAllowed() || z;
        int i = 20;
        int i2 = 10;
        builder.setConfigureAutoDetectionEnabledCapability(!isAutoDetectionSupported() ? 10 : !z2 ? 20 : 40);
        builder.setUseLocationEnabled(this.mLocationEnabledSetting);
        boolean isGeoDetectionSupported = isGeoDetectionSupported();
        boolean isTelephonyDetectionSupported = isTelephonyDetectionSupported();
        if (isGeoDetectionSupported && isTelephonyDetectionSupported) {
            i2 = (this.mAutoDetectionEnabledSetting && getLocationEnabledSetting()) ? 40 : 30;
        }
        builder.setConfigureGeoDetectionEnabledCapability(i2);
        if (z2) {
            i = getAutoDetectionEnabledBehavior() ? 30 : 40;
        }
        builder.setSetManualTimeZoneCapability(i);
        return builder.build();
    }

    public TimeZoneConfiguration asConfiguration() {
        return new TimeZoneConfiguration.Builder().setAutoDetectionEnabled(getAutoDetectionEnabledSetting()).setGeoDetectionEnabled(getGeoDetectionEnabledSetting()).build();
    }

    public ConfigurationInternal merge(TimeZoneConfiguration timeZoneConfiguration) {
        Builder builder = new Builder(this);
        if (timeZoneConfiguration.hasIsAutoDetectionEnabled()) {
            builder.setAutoDetectionEnabledSetting(timeZoneConfiguration.isAutoDetectionEnabled());
        }
        if (timeZoneConfiguration.hasIsGeoDetectionEnabled()) {
            builder.setGeoDetectionEnabledSetting(timeZoneConfiguration.isGeoDetectionEnabled());
        }
        return builder.build();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ConfigurationInternal.class != obj.getClass()) {
            return false;
        }
        ConfigurationInternal configurationInternal = (ConfigurationInternal) obj;
        return this.mUserId == configurationInternal.mUserId && this.mUserConfigAllowed == configurationInternal.mUserConfigAllowed && this.mTelephonyDetectionSupported == configurationInternal.mTelephonyDetectionSupported && this.mGeoDetectionSupported == configurationInternal.mGeoDetectionSupported && this.mTelephonyFallbackSupported == configurationInternal.mTelephonyFallbackSupported && this.mGeoDetectionRunInBackgroundEnabled == configurationInternal.mGeoDetectionRunInBackgroundEnabled && this.mEnhancedMetricsCollectionEnabled == configurationInternal.mEnhancedMetricsCollectionEnabled && this.mAutoDetectionEnabledSetting == configurationInternal.mAutoDetectionEnabledSetting && this.mLocationEnabledSetting == configurationInternal.mLocationEnabledSetting && this.mGeoDetectionEnabledSetting == configurationInternal.mGeoDetectionEnabledSetting;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mUserId), Boolean.valueOf(this.mUserConfigAllowed), Boolean.valueOf(this.mTelephonyDetectionSupported), Boolean.valueOf(this.mGeoDetectionSupported), Boolean.valueOf(this.mTelephonyFallbackSupported), Boolean.valueOf(this.mGeoDetectionRunInBackgroundEnabled), Boolean.valueOf(this.mEnhancedMetricsCollectionEnabled), Boolean.valueOf(this.mAutoDetectionEnabledSetting), Boolean.valueOf(this.mLocationEnabledSetting), Boolean.valueOf(this.mGeoDetectionEnabledSetting));
    }

    public String toString() {
        return "ConfigurationInternal{mUserId=" + this.mUserId + ", mUserConfigAllowed=" + this.mUserConfigAllowed + ", mTelephonyDetectionSupported=" + this.mTelephonyDetectionSupported + ", mGeoDetectionSupported=" + this.mGeoDetectionSupported + ", mTelephonyFallbackSupported=" + this.mTelephonyFallbackSupported + ", mGeoDetectionRunInBackgroundEnabled=" + this.mGeoDetectionRunInBackgroundEnabled + ", mEnhancedMetricsCollectionEnabled=" + this.mEnhancedMetricsCollectionEnabled + ", mAutoDetectionEnabledSetting=" + this.mAutoDetectionEnabledSetting + ", mLocationEnabledSetting=" + this.mLocationEnabledSetting + ", mGeoDetectionEnabledSetting=" + this.mGeoDetectionEnabledSetting + '}';
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Builder {
        private boolean mAutoDetectionEnabledSetting;
        private boolean mEnhancedMetricsCollectionEnabled;
        private boolean mGeoDetectionEnabledSetting;
        private boolean mGeoDetectionRunInBackgroundEnabled;
        private boolean mGeoDetectionSupported;
        private boolean mLocationEnabledSetting;
        private boolean mTelephonyDetectionSupported;
        private boolean mTelephonyFallbackSupported;
        private boolean mUserConfigAllowed;
        private Integer mUserId;

        public Builder() {
        }

        public Builder(ConfigurationInternal configurationInternal) {
            this.mUserId = Integer.valueOf(configurationInternal.mUserId);
            this.mUserConfigAllowed = configurationInternal.mUserConfigAllowed;
            this.mTelephonyDetectionSupported = configurationInternal.mTelephonyDetectionSupported;
            this.mTelephonyFallbackSupported = configurationInternal.mTelephonyFallbackSupported;
            this.mGeoDetectionSupported = configurationInternal.mGeoDetectionSupported;
            this.mGeoDetectionRunInBackgroundEnabled = configurationInternal.mGeoDetectionRunInBackgroundEnabled;
            this.mEnhancedMetricsCollectionEnabled = configurationInternal.mEnhancedMetricsCollectionEnabled;
            this.mAutoDetectionEnabledSetting = configurationInternal.mAutoDetectionEnabledSetting;
            this.mLocationEnabledSetting = configurationInternal.mLocationEnabledSetting;
            this.mGeoDetectionEnabledSetting = configurationInternal.mGeoDetectionEnabledSetting;
        }

        public Builder setUserId(int i) {
            this.mUserId = Integer.valueOf(i);
            return this;
        }

        public Builder setUserConfigAllowed(boolean z) {
            this.mUserConfigAllowed = z;
            return this;
        }

        public Builder setTelephonyDetectionFeatureSupported(boolean z) {
            this.mTelephonyDetectionSupported = z;
            return this;
        }

        public Builder setGeoDetectionFeatureSupported(boolean z) {
            this.mGeoDetectionSupported = z;
            return this;
        }

        public Builder setTelephonyFallbackSupported(boolean z) {
            this.mTelephonyFallbackSupported = z;
            return this;
        }

        public Builder setGeoDetectionRunInBackgroundEnabled(boolean z) {
            this.mGeoDetectionRunInBackgroundEnabled = z;
            return this;
        }

        public Builder setEnhancedMetricsCollectionEnabled(boolean z) {
            this.mEnhancedMetricsCollectionEnabled = z;
            return this;
        }

        public Builder setAutoDetectionEnabledSetting(boolean z) {
            this.mAutoDetectionEnabledSetting = z;
            return this;
        }

        public Builder setLocationEnabledSetting(boolean z) {
            this.mLocationEnabledSetting = z;
            return this;
        }

        public Builder setGeoDetectionEnabledSetting(boolean z) {
            this.mGeoDetectionEnabledSetting = z;
            return this;
        }

        public ConfigurationInternal build() {
            return new ConfigurationInternal(this);
        }
    }
}
