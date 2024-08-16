package com.android.server.timezonedetector.location;

import com.android.server.timezonedetector.LocationAlgorithmEvent;
import com.android.server.timezonedetector.location.LocationTimeZoneProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class LocationTimeZoneManagerServiceState {
    private final String mControllerState;
    private final List<String> mControllerStates;
    private final LocationAlgorithmEvent mLastEvent;
    private final List<LocationTimeZoneProvider.ProviderState> mPrimaryProviderStates;
    private final List<LocationTimeZoneProvider.ProviderState> mSecondaryProviderStates;

    LocationTimeZoneManagerServiceState(Builder builder) {
        this.mControllerState = builder.mControllerState;
        this.mLastEvent = builder.mLastEvent;
        List<String> list = builder.mControllerStates;
        Objects.requireNonNull(list);
        this.mControllerStates = list;
        List<LocationTimeZoneProvider.ProviderState> list2 = builder.mPrimaryProviderStates;
        Objects.requireNonNull(list2);
        this.mPrimaryProviderStates = list2;
        List<LocationTimeZoneProvider.ProviderState> list3 = builder.mSecondaryProviderStates;
        Objects.requireNonNull(list3);
        this.mSecondaryProviderStates = list3;
    }

    public String getControllerState() {
        return this.mControllerState;
    }

    public LocationAlgorithmEvent getLastEvent() {
        return this.mLastEvent;
    }

    public List<String> getControllerStates() {
        return this.mControllerStates;
    }

    public List<LocationTimeZoneProvider.ProviderState> getPrimaryProviderStates() {
        return Collections.unmodifiableList(this.mPrimaryProviderStates);
    }

    public List<LocationTimeZoneProvider.ProviderState> getSecondaryProviderStates() {
        return Collections.unmodifiableList(this.mSecondaryProviderStates);
    }

    public String toString() {
        return "LocationTimeZoneManagerServiceState{mControllerState=" + this.mControllerState + ", mLastEvent=" + this.mLastEvent + ", mControllerStates=" + this.mControllerStates + ", mPrimaryProviderStates=" + this.mPrimaryProviderStates + ", mSecondaryProviderStates=" + this.mSecondaryProviderStates + '}';
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Builder {
        private String mControllerState;
        private List<String> mControllerStates;
        private LocationAlgorithmEvent mLastEvent;
        private List<LocationTimeZoneProvider.ProviderState> mPrimaryProviderStates;
        private List<LocationTimeZoneProvider.ProviderState> mSecondaryProviderStates;

        public Builder setControllerState(String str) {
            this.mControllerState = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setLastEvent(LocationAlgorithmEvent locationAlgorithmEvent) {
            Objects.requireNonNull(locationAlgorithmEvent);
            this.mLastEvent = locationAlgorithmEvent;
            return this;
        }

        public Builder setStateChanges(List<String> list) {
            this.mControllerStates = new ArrayList(list);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setPrimaryProviderStateChanges(List<LocationTimeZoneProvider.ProviderState> list) {
            this.mPrimaryProviderStates = new ArrayList(list);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setSecondaryProviderStateChanges(List<LocationTimeZoneProvider.ProviderState> list) {
            this.mSecondaryProviderStates = new ArrayList(list);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public LocationTimeZoneManagerServiceState build() {
            return new LocationTimeZoneManagerServiceState(this);
        }
    }
}
