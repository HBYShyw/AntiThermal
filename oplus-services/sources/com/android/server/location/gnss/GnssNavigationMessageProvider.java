package com.android.server.location.gnss;

import android.location.GnssNavigationMessage;
import android.location.IGnssNavigationMessageListener;
import android.location.util.identity.CallerIdentity;
import android.util.Log;
import com.android.internal.listeners.ListenerExecutor;
import com.android.server.location.gnss.GnssListenerMultiplexer;
import com.android.server.location.gnss.hal.GnssNative;
import com.android.server.location.injector.AppOpsHelper;
import com.android.server.location.injector.Injector;
import java.util.Collection;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssNavigationMessageProvider extends GnssListenerMultiplexer<Void, IGnssNavigationMessageListener, Void> implements GnssNative.BaseCallbacks, GnssNative.NavigationMessageCallbacks {
    private final AppOpsHelper mAppOpsHelper;
    private final GnssNative mGnssNative;

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean registerWithService(Object obj, Collection collection) {
        return registerWithService((Void) obj, (Collection<GnssListenerMultiplexer<Void, IGnssNavigationMessageListener, Void>.GnssListenerRegistration>) collection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class GnssNavigationMessageListenerRegistration extends GnssListenerMultiplexer<Void, IGnssNavigationMessageListener, Void>.GnssListenerRegistration {
        protected GnssNavigationMessageListenerRegistration(CallerIdentity callerIdentity, IGnssNavigationMessageListener iGnssNavigationMessageListener) {
            super(null, callerIdentity, iGnssNavigationMessageListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration, com.android.server.location.listeners.BinderListenerRegistration, com.android.server.location.listeners.RemovableListenerRegistration
        public void onRegister() {
            super.onRegister();
            executeOperation(new ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssNavigationMessageProvider$GnssNavigationMessageListenerRegistration$$ExternalSyntheticLambda0
                public final void operate(Object obj) {
                    ((IGnssNavigationMessageListener) obj).onStatusChanged(1);
                }
            });
        }
    }

    public GnssNavigationMessageProvider(Injector injector, GnssNative gnssNative) {
        super(injector);
        this.mAppOpsHelper = injector.getAppOpsHelper();
        this.mGnssNative = gnssNative;
        gnssNative.addBaseCallbacks(this);
        gnssNative.addNavigationMessageCallbacks(this);
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public boolean isSupported() {
        return this.mGnssNative.isNavigationMessageCollectionSupported();
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public void addListener(CallerIdentity callerIdentity, IGnssNavigationMessageListener iGnssNavigationMessageListener) {
        super.addListener(callerIdentity, (CallerIdentity) iGnssNavigationMessageListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public GnssListenerMultiplexer<Void, IGnssNavigationMessageListener, Void>.GnssListenerRegistration createRegistration(Void r1, CallerIdentity callerIdentity, IGnssNavigationMessageListener iGnssNavigationMessageListener) {
        return new GnssNavigationMessageListenerRegistration(callerIdentity, iGnssNavigationMessageListener);
    }

    protected boolean registerWithService(Void r1, Collection<GnssListenerMultiplexer<Void, IGnssNavigationMessageListener, Void>.GnssListenerRegistration> collection) {
        if (this.mGnssNative.startNavigationMessageCollection()) {
            if (!GnssManagerService.D) {
                return true;
            }
            Log.d(GnssManagerService.TAG, "starting gnss navigation messages");
            return true;
        }
        Log.e(GnssManagerService.TAG, "error starting gnss navigation messages");
        return false;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
        if (this.mGnssNative.stopNavigationMessageCollection()) {
            if (GnssManagerService.D) {
                Log.d(GnssManagerService.TAG, "stopping gnss navigation messages");
                return;
            }
            return;
        }
        Log.e(GnssManagerService.TAG, "error stopping gnss navigation messages");
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        resetService();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.NavigationMessageCallbacks
    public void onReportNavigationMessage(final GnssNavigationMessage gnssNavigationMessage) {
        deliverToListeners(new Function() { // from class: com.android.server.location.gnss.GnssNavigationMessageProvider$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ListenerExecutor.ListenerOperation lambda$onReportNavigationMessage$1;
                lambda$onReportNavigationMessage$1 = GnssNavigationMessageProvider.this.lambda$onReportNavigationMessage$1(gnssNavigationMessage, (GnssListenerMultiplexer.GnssListenerRegistration) obj);
                return lambda$onReportNavigationMessage$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ListenerExecutor.ListenerOperation lambda$onReportNavigationMessage$1(final GnssNavigationMessage gnssNavigationMessage, GnssListenerMultiplexer.GnssListenerRegistration gnssListenerRegistration) {
        if (this.mAppOpsHelper.noteOpNoThrow(1, gnssListenerRegistration.getIdentity())) {
            return new ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssNavigationMessageProvider$$ExternalSyntheticLambda0
                public final void operate(Object obj) {
                    ((IGnssNavigationMessageListener) obj).onGnssNavigationMessageReceived(gnssNavigationMessage);
                }
            };
        }
        return null;
    }
}
