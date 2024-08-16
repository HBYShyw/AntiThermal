package com.android.server.timedetector;

import android.app.time.TimeCapabilitiesAndConfig;
import android.app.time.TimeConfiguration;
import android.app.timedetector.ManualTimeSuggestion;
import android.content.Context;
import android.os.Handler;
import com.android.server.timezonedetector.CurrentUserIdentityInjector;
import com.android.server.timezonedetector.StateChangeListener;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TimeDetectorInternalImpl implements TimeDetectorInternal {
    private final Context mContext;
    private final CurrentUserIdentityInjector mCurrentUserIdentityInjector;
    private final Handler mHandler;
    private final ServiceConfigAccessor mServiceConfigAccessor;
    private final TimeDetectorStrategy mTimeDetectorStrategy;

    public TimeDetectorInternalImpl(Context context, Handler handler, CurrentUserIdentityInjector currentUserIdentityInjector, ServiceConfigAccessor serviceConfigAccessor, TimeDetectorStrategy timeDetectorStrategy) {
        Objects.requireNonNull(context);
        this.mContext = context;
        Objects.requireNonNull(handler);
        this.mHandler = handler;
        Objects.requireNonNull(currentUserIdentityInjector);
        this.mCurrentUserIdentityInjector = currentUserIdentityInjector;
        Objects.requireNonNull(serviceConfigAccessor);
        this.mServiceConfigAccessor = serviceConfigAccessor;
        Objects.requireNonNull(timeDetectorStrategy);
        this.mTimeDetectorStrategy = timeDetectorStrategy;
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public TimeCapabilitiesAndConfig getCapabilitiesAndConfigForDpm() {
        return this.mServiceConfigAccessor.getConfigurationInternal(this.mCurrentUserIdentityInjector.getCurrentUserId()).createCapabilitiesAndConfig(true);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public boolean updateConfigurationForDpm(TimeConfiguration timeConfiguration) {
        Objects.requireNonNull(timeConfiguration);
        return this.mServiceConfigAccessor.updateConfiguration(this.mCurrentUserIdentityInjector.getCurrentUserId(), timeConfiguration, true);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public boolean setManualTimeForDpm(ManualTimeSuggestion manualTimeSuggestion) {
        Objects.requireNonNull(manualTimeSuggestion);
        return this.mTimeDetectorStrategy.suggestManualTime(this.mCurrentUserIdentityInjector.getCurrentUserId(), manualTimeSuggestion, false);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public void suggestNetworkTime(final NetworkTimeSuggestion networkTimeSuggestion) {
        Objects.requireNonNull(networkTimeSuggestion);
        this.mHandler.post(new Runnable() { // from class: com.android.server.timedetector.TimeDetectorInternalImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                TimeDetectorInternalImpl.this.lambda$suggestNetworkTime$0(networkTimeSuggestion);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$suggestNetworkTime$0(NetworkTimeSuggestion networkTimeSuggestion) {
        this.mTimeDetectorStrategy.suggestNetworkTime(networkTimeSuggestion);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public void addNetworkTimeUpdateListener(StateChangeListener stateChangeListener) {
        Objects.requireNonNull(stateChangeListener);
        this.mTimeDetectorStrategy.addNetworkTimeUpdateListener(stateChangeListener);
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public NetworkTimeSuggestion getLatestNetworkSuggestion() {
        return this.mTimeDetectorStrategy.getLatestNetworkSuggestion();
    }

    @Override // com.android.server.timedetector.TimeDetectorInternal
    public void suggestGnssTime(final GnssTimeSuggestion gnssTimeSuggestion) {
        Objects.requireNonNull(gnssTimeSuggestion);
        this.mHandler.post(new Runnable() { // from class: com.android.server.timedetector.TimeDetectorInternalImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TimeDetectorInternalImpl.this.lambda$suggestGnssTime$1(gnssTimeSuggestion);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$suggestGnssTime$1(GnssTimeSuggestion gnssTimeSuggestion) {
        this.mTimeDetectorStrategy.suggestGnssTime(gnssTimeSuggestion);
    }
}
