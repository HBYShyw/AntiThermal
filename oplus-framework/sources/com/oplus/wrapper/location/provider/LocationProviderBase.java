package com.oplus.wrapper.location.provider;

import android.content.Context;
import android.location.Location;
import android.location.provider.LocationProviderBase;
import android.location.provider.ProviderProperties;
import android.os.Bundle;
import android.os.IBinder;

/* loaded from: classes.dex */
public abstract class LocationProviderBase {
    private final LocationProviderBaseInner mInnerProvider;

    /* loaded from: classes.dex */
    public interface OnFlushCompleteCallback {
        void onFlushComplete();
    }

    public abstract void onFlush(OnFlushCompleteCallback onFlushCompleteCallback);

    public abstract void onSendExtraCommand(String str, Bundle bundle);

    public abstract void onSetRequest(ProviderRequest providerRequest);

    public LocationProviderBase(Context context, String tag, ProviderProperties properties) {
        this.mInnerProvider = new LocationProviderBaseInner(context, tag, properties);
    }

    public final IBinder getBinder() {
        return this.mInnerProvider.getBinder();
    }

    public void reportLocation(Location location) {
        this.mInnerProvider.reportLocation(location);
    }

    public void setAllowed(boolean allowed) {
        this.mInnerProvider.setAllowed(allowed);
    }

    /* loaded from: classes.dex */
    private class LocationProviderBaseInner extends android.location.provider.LocationProviderBase {
        public LocationProviderBaseInner(Context context, String tag, ProviderProperties properties) {
            super(context, tag, properties);
        }

        public void onSetRequest(android.location.provider.ProviderRequest request) {
            LocationProviderBase.this.onSetRequest(new ProviderRequest(request));
        }

        public void onFlush(LocationProviderBase.OnFlushCompleteCallback callback) {
            OnFlushCompleteCallbackImpl onFlushCompleteCallback = new OnFlushCompleteCallbackImpl(callback);
            LocationProviderBase.this.onFlush(onFlushCompleteCallback);
        }

        public void onSendExtraCommand(String command, Bundle extras) {
            LocationProviderBase.this.onSendExtraCommand(command, extras);
        }
    }

    /* loaded from: classes.dex */
    private static class OnFlushCompleteCallbackImpl implements OnFlushCompleteCallback {
        private final LocationProviderBase.OnFlushCompleteCallback mOnFlushCompleteCallback;

        public OnFlushCompleteCallbackImpl(LocationProviderBase.OnFlushCompleteCallback onFlushCompleteCallback) {
            this.mOnFlushCompleteCallback = onFlushCompleteCallback;
        }

        @Override // com.oplus.wrapper.location.provider.LocationProviderBase.OnFlushCompleteCallback
        public void onFlushComplete() {
            this.mOnFlushCompleteCallback.onFlushComplete();
        }
    }
}
