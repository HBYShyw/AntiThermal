package com.oplus.wrapper.location.provider;

import android.os.WorkSource;

/* loaded from: classes.dex */
public class ProviderRequest {
    private android.location.provider.ProviderRequest mProviderRequest;

    public ProviderRequest(android.location.provider.ProviderRequest providerRequest) {
        this.mProviderRequest = providerRequest;
    }

    public static ProviderRequest getEmptyRequest() {
        return new ProviderRequest(android.location.provider.ProviderRequest.EMPTY_REQUEST);
    }

    public long getIntervalMillis() {
        return this.mProviderRequest.getIntervalMillis();
    }

    public long getMaxUpdateDelayMillis() {
        return this.mProviderRequest.getMaxUpdateDelayMillis();
    }

    public int getQuality() {
        return this.mProviderRequest.getQuality();
    }

    public WorkSource getWorkSource() {
        return this.mProviderRequest.getWorkSource();
    }

    public boolean isActive() {
        return this.mProviderRequest.isActive();
    }

    public boolean isLocationSettingsIgnored() {
        return this.mProviderRequest.isLocationSettingsIgnored();
    }

    public boolean isLowPower() {
        return this.mProviderRequest.isLowPower();
    }
}
