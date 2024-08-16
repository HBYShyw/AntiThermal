package com.android.server.location.gnss;

import android.location.GnssMeasurementRequest;
import android.location.GnssMeasurementsEvent;
import android.location.IGnssMeasurementsListener;
import android.location.util.identity.CallerIdentity;
import android.os.IBinder;
import android.util.Log;
import com.android.internal.listeners.ListenerExecutor;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.gnss.GnssConfiguration;
import com.android.server.location.gnss.GnssListenerMultiplexer;
import com.android.server.location.gnss.hal.GnssNative;
import com.android.server.location.injector.AppOpsHelper;
import com.android.server.location.injector.Injector;
import com.android.server.location.injector.LocationUsageLogger;
import com.android.server.location.injector.SettingsHelper;
import com.android.server.location.interfaces.IGnssMeasurementsProviderExt;
import com.android.server.location.interfaces.IOplusLBSMainClass;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class GnssMeasurementsProvider extends GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest> implements SettingsHelper.GlobalSettingChangedListener, GnssNative.BaseCallbacks, GnssNative.MeasurementCallbacks {
    private final AppOpsHelper mAppOpsHelper;
    private IGnssMeasurementsProviderWrapper mGnssMeasurementsProviderWrapper;
    private final GnssNative mGnssNative;
    private final LocationUsageLogger mLogger;

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer, com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ Object mergeRegistrations(Collection collection) {
        return mergeRegistrations((Collection<GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration>) collection);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean registerWithService(Object obj, Collection collection) {
        return registerWithService((GnssMeasurementRequest) obj, (Collection<GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration>) collection);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean reregisterWithService(Object obj, Object obj2, Collection collection) {
        return reregisterWithService((GnssMeasurementRequest) obj, (GnssMeasurementRequest) obj2, (Collection<GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration>) collection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class GnssMeasurementListenerRegistration extends GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration {
        protected GnssMeasurementListenerRegistration(GnssMeasurementRequest gnssMeasurementRequest, CallerIdentity callerIdentity, IGnssMeasurementsListener iGnssMeasurementsListener) {
            super(gnssMeasurementRequest, callerIdentity, iGnssMeasurementsListener);
        }

        @Override // com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration, com.android.server.location.listeners.BinderListenerRegistration, com.android.server.location.listeners.RemovableListenerRegistration
        protected void onRegister() {
            super.onRegister();
            executeOperation(new ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssMeasurementsProvider$GnssMeasurementListenerRegistration$$ExternalSyntheticLambda0
                public final void operate(Object obj) {
                    ((IGnssMeasurementsListener) obj).onStatusChanged(1);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.ListenerRegistration
        public void onActive() {
            GnssMeasurementsProvider.this.mAppOpsHelper.startOpNoThrow(42, getIdentity());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.ListenerRegistration
        public void onInactive() {
            GnssMeasurementsProvider.this.mAppOpsHelper.finishOp(42, getIdentity());
        }
    }

    public GnssMeasurementsProvider(Injector injector, GnssNative gnssNative) {
        super(injector);
        this.mGnssMeasurementsProviderWrapper = new GnssMeasurementsProviderWrapper();
        this.mAppOpsHelper = injector.getAppOpsHelper();
        this.mLogger = injector.getLocationUsageLogger();
        this.mGnssNative = gnssNative;
        gnssNative.addBaseCallbacks(this);
        gnssNative.addMeasurementCallbacks(this);
        ((IOplusLBSMainClass) OplusLbsFactory.getInstance().getFeature(IOplusLBSMainClass.DEFAULT, null)).onGnssMeasurementsProviderInit(this);
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public boolean isSupported() {
        return this.mGnssNative.isMeasurementSupported();
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public void addListener(GnssMeasurementRequest gnssMeasurementRequest, CallerIdentity callerIdentity, IGnssMeasurementsListener iGnssMeasurementsListener) {
        super.addListener((GnssMeasurementsProvider) gnssMeasurementRequest, callerIdentity, (CallerIdentity) iGnssMeasurementsListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration createRegistration(GnssMeasurementRequest gnssMeasurementRequest, CallerIdentity callerIdentity, IGnssMeasurementsListener iGnssMeasurementsListener) {
        return new GnssMeasurementListenerRegistration(gnssMeasurementRequest, callerIdentity, iGnssMeasurementsListener);
    }

    protected boolean registerWithService(GnssMeasurementRequest gnssMeasurementRequest, Collection<GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration> collection) {
        if (gnssMeasurementRequest.getIntervalMillis() == Integer.MAX_VALUE) {
            return true;
        }
        if (this.mGnssNative.startMeasurementCollection(gnssMeasurementRequest.isFullTracking(), gnssMeasurementRequest.isCorrelationVectorOutputsEnabled(), gnssMeasurementRequest.getIntervalMillis())) {
            if (GnssManagerService.D) {
                Log.d(GnssManagerService.TAG, "starting gnss measurements (" + gnssMeasurementRequest + ")");
            }
            return true;
        }
        Log.e(GnssManagerService.TAG, "error starting gnss measurements");
        return false;
    }

    protected boolean reregisterWithService(GnssMeasurementRequest gnssMeasurementRequest, GnssMeasurementRequest gnssMeasurementRequest2, Collection<GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration> collection) {
        if (gnssMeasurementRequest2.getIntervalMillis() == Integer.MAX_VALUE) {
            unregisterWithService();
            return true;
        }
        GnssConfiguration.HalInterfaceVersion halInterfaceVersion = this.mGnssNative.getConfiguration().getHalInterfaceVersion();
        if (!(halInterfaceVersion.mMajor == 3 && halInterfaceVersion.mMinor >= 3)) {
            unregisterWithService();
        }
        return registerWithService(gnssMeasurementRequest2, collection);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
        if (this.mGnssNative.stopMeasurementCollection()) {
            if (GnssManagerService.D) {
                Log.d(GnssManagerService.TAG, "stopping gnss measurements");
                return;
            }
            return;
        }
        Log.e(GnssManagerService.TAG, "error stopping gnss measurements");
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onActive() {
        this.mSettingsHelper.addOnGnssMeasurementsFullTrackingEnabledChangedListener(this);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onInactive() {
        this.mSettingsHelper.removeOnGnssMeasurementsFullTrackingEnabledChangedListener(this);
    }

    @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
    public void onSettingChanged() {
        updateService();
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer, com.android.server.location.listeners.ListenerMultiplexer
    protected GnssMeasurementRequest mergeRegistrations(Collection<GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration> collection) {
        boolean isGnssMeasurementsFullTrackingEnabled = this.mSettingsHelper.isGnssMeasurementsFullTrackingEnabled();
        Iterator<GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration> it = collection.iterator();
        boolean z = false;
        int i = Integer.MAX_VALUE;
        while (it.hasNext()) {
            GnssMeasurementRequest request = it.next().getRequest();
            if (request.getIntervalMillis() != Integer.MAX_VALUE) {
                if (request.isFullTracking()) {
                    isGnssMeasurementsFullTrackingEnabled = true;
                }
                if (request.isCorrelationVectorOutputsEnabled()) {
                    z = true;
                }
                i = Math.min(i, request.getIntervalMillis());
            }
        }
        return new GnssMeasurementRequest.Builder().setFullTracking(isGnssMeasurementsFullTrackingEnabled).setCorrelationVectorOutputsEnabled(z).setIntervalMillis(i).build();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationAdded(IBinder iBinder, GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration gnssListenerRegistration) {
        ((IGnssMeasurementsProviderExt) OplusLbsFactory.getInstance().getFeature(IGnssMeasurementsProviderExt.DEFAULT, new Object[0])).onRegistrationAdded(gnssListenerRegistration.getIdentity(), gnssListenerRegistration.getRequest());
        this.mLogger.logLocationApiUsage(0, 2, gnssListenerRegistration.getIdentity().getPackageName(), gnssListenerRegistration.getIdentity().getAttributionTag(), null, null, true, false, null, gnssListenerRegistration.isForeground());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationRemoved(IBinder iBinder, GnssListenerMultiplexer<GnssMeasurementRequest, IGnssMeasurementsListener, GnssMeasurementRequest>.GnssListenerRegistration gnssListenerRegistration) {
        ((IGnssMeasurementsProviderExt) OplusLbsFactory.getInstance().getFeature(IGnssMeasurementsProviderExt.DEFAULT, new Object[0])).onRegistrationRemoved(gnssListenerRegistration.getIdentity());
        this.mLogger.logLocationApiUsage(1, 2, gnssListenerRegistration.getIdentity().getPackageName(), gnssListenerRegistration.getIdentity().getAttributionTag(), null, null, true, false, null, gnssListenerRegistration.isForeground());
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        resetService();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.MeasurementCallbacks
    public void onReportMeasurements(final GnssMeasurementsEvent gnssMeasurementsEvent) {
        deliverToListeners(new Function() { // from class: com.android.server.location.gnss.GnssMeasurementsProvider$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ListenerExecutor.ListenerOperation lambda$onReportMeasurements$1;
                lambda$onReportMeasurements$1 = GnssMeasurementsProvider.this.lambda$onReportMeasurements$1(gnssMeasurementsEvent, (GnssListenerMultiplexer.GnssListenerRegistration) obj);
                return lambda$onReportMeasurements$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ListenerExecutor.ListenerOperation lambda$onReportMeasurements$1(final GnssMeasurementsEvent gnssMeasurementsEvent, GnssListenerMultiplexer.GnssListenerRegistration gnssListenerRegistration) {
        if (this.mAppOpsHelper.noteOpNoThrow(1, gnssListenerRegistration.getIdentity())) {
            return new ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssMeasurementsProvider$$ExternalSyntheticLambda0
                public final void operate(Object obj) {
                    ((IGnssMeasurementsListener) obj).onGnssMeasurementsReceived(gnssMeasurementsEvent);
                }
            };
        }
        return null;
    }

    public IGnssMeasurementsProviderWrapper getGnssMeasurementsProviderWrapper() {
        return this.mGnssMeasurementsProviderWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class GnssMeasurementsProviderWrapper implements IGnssMeasurementsProviderWrapper {
        private GnssMeasurementsProviderWrapper() {
        }

        @Override // com.android.server.location.gnss.IGnssMeasurementsProviderWrapper
        public void stopMeasurementCollection() {
            GnssMeasurementsProvider.this.unregisterWithService();
        }

        @Override // com.android.server.location.gnss.IGnssMeasurementsProviderWrapper
        public void restart() {
            GnssMeasurementsProvider.this.resetService();
        }
    }
}
