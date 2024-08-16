package com.android.server.location.provider;

import android.content.Context;
import android.location.LocationResult;
import android.location.provider.ProviderRequest;
import android.os.Binder;
import com.android.internal.util.Preconditions;
import com.android.server.location.injector.Injector;
import com.android.server.location.provider.LocationProviderManager;
import java.util.Collection;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PassiveLocationProviderManager extends LocationProviderManager {
    @Override // com.android.server.location.provider.LocationProviderManager
    protected long calculateRequestDelayMillis(long j, Collection<LocationProviderManager.Registration> collection) {
        return 0L;
    }

    public PassiveLocationProviderManager(Context context, Injector injector) {
        super(context, injector, "passive", null);
    }

    @Override // com.android.server.location.provider.LocationProviderManager
    public void setRealProvider(AbstractLocationProvider abstractLocationProvider) {
        Preconditions.checkArgument(abstractLocationProvider instanceof PassiveLocationProvider);
        super.setRealProvider(abstractLocationProvider);
    }

    @Override // com.android.server.location.provider.LocationProviderManager
    public void setMockProvider(MockLocationProvider mockLocationProvider) {
        if (mockLocationProvider != null) {
            throw new IllegalArgumentException("Cannot mock the passive provider");
        }
    }

    public void updateLocation(LocationResult locationResult) {
        synchronized (this.mMultiplexerLock) {
            PassiveLocationProvider passiveLocationProvider = (PassiveLocationProvider) this.mProvider.getProvider();
            Preconditions.checkState(passiveLocationProvider != null);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                passiveLocationProvider.updateLocation(locationResult);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.location.provider.LocationProviderManager, com.android.server.location.listeners.ListenerMultiplexer
    public ProviderRequest mergeRegistrations(Collection<LocationProviderManager.Registration> collection) {
        return new ProviderRequest.Builder().setIntervalMillis(0L).build();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.provider.LocationProviderManager, com.android.server.location.listeners.ListenerMultiplexer
    public String getServiceState() {
        return this.mProvider.getCurrentRequest().isActive() ? "registered" : "unregistered";
    }
}
