package com.android.server.timezonedetector;

import android.app.time.TimeZoneCapabilitiesAndConfig;
import android.app.time.TimeZoneConfiguration;
import android.app.timezonedetector.ManualTimeZoneSuggestion;
import android.content.Context;
import android.os.Handler;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class TimeZoneDetectorInternalImpl implements TimeZoneDetectorInternal {
    private final Context mContext;
    private final CurrentUserIdentityInjector mCurrentUserIdentityInjector;
    private final Handler mHandler;
    private final TimeZoneDetectorStrategy mTimeZoneDetectorStrategy;

    public TimeZoneDetectorInternalImpl(Context context, Handler handler, CurrentUserIdentityInjector currentUserIdentityInjector, TimeZoneDetectorStrategy timeZoneDetectorStrategy) {
        Objects.requireNonNull(context);
        this.mContext = context;
        Objects.requireNonNull(handler);
        this.mHandler = handler;
        Objects.requireNonNull(currentUserIdentityInjector);
        this.mCurrentUserIdentityInjector = currentUserIdentityInjector;
        Objects.requireNonNull(timeZoneDetectorStrategy);
        this.mTimeZoneDetectorStrategy = timeZoneDetectorStrategy;
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public TimeZoneCapabilitiesAndConfig getCapabilitiesAndConfigForDpm() {
        return this.mTimeZoneDetectorStrategy.getCapabilitiesAndConfig(this.mCurrentUserIdentityInjector.getCurrentUserId(), true);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public boolean updateConfigurationForDpm(TimeZoneConfiguration timeZoneConfiguration) {
        Objects.requireNonNull(timeZoneConfiguration);
        return this.mTimeZoneDetectorStrategy.updateConfiguration(this.mCurrentUserIdentityInjector.getCurrentUserId(), timeZoneConfiguration, true);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public boolean setManualTimeZoneForDpm(ManualTimeZoneSuggestion manualTimeZoneSuggestion) {
        Objects.requireNonNull(manualTimeZoneSuggestion);
        return this.mTimeZoneDetectorStrategy.suggestManualTimeZone(this.mCurrentUserIdentityInjector.getCurrentUserId(), manualTimeZoneSuggestion, true);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public void handleLocationAlgorithmEvent(final LocationAlgorithmEvent locationAlgorithmEvent) {
        Objects.requireNonNull(locationAlgorithmEvent);
        this.mHandler.post(new Runnable() { // from class: com.android.server.timezonedetector.TimeZoneDetectorInternalImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TimeZoneDetectorInternalImpl.this.lambda$handleLocationAlgorithmEvent$0(locationAlgorithmEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLocationAlgorithmEvent$0(LocationAlgorithmEvent locationAlgorithmEvent) {
        this.mTimeZoneDetectorStrategy.handleLocationAlgorithmEvent(locationAlgorithmEvent);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorInternal
    public MetricsTimeZoneDetectorState generateMetricsState() {
        return this.mTimeZoneDetectorStrategy.generateMetricsState();
    }
}
