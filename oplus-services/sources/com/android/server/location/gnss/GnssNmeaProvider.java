package com.android.server.location.gnss;

import android.location.IGnssNmeaListener;
import android.location.util.identity.CallerIdentity;
import android.util.Log;
import com.android.internal.listeners.ListenerExecutor;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.gnss.GnssNmeaProvider;
import com.android.server.location.gnss.hal.GnssNative;
import com.android.server.location.injector.AppOpsHelper;
import com.android.server.location.injector.Injector;
import com.android.server.location.interfaces.IOplusLBSMainClass;
import java.util.Collection;
import java.util.function.Function;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssNmeaProvider extends GnssListenerMultiplexer<Void, IGnssNmeaListener, Void> implements GnssNative.BaseCallbacks, GnssNative.NmeaCallbacks {
    private final AppOpsHelper mAppOpsHelper;
    private final GnssNative mGnssNative;
    private final byte[] mNmeaBuffer;
    private IOplusLBSMainClass mOplusLbsClass;
    private boolean mPreciseLocationSupported;

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean registerWithService(Object obj, Collection collection) {
        return registerWithService((Void) obj, (Collection<GnssListenerMultiplexer<Void, IGnssNmeaListener, Void>.GnssListenerRegistration>) collection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GnssNmeaProvider(Injector injector, GnssNative gnssNative) {
        super(injector);
        this.mNmeaBuffer = new byte[120];
        this.mOplusLbsClass = null;
        this.mPreciseLocationSupported = false;
        this.mAppOpsHelper = injector.getAppOpsHelper();
        this.mGnssNative = gnssNative;
        gnssNative.addBaseCallbacks(this);
        gnssNative.addNmeaCallbacks(this);
        IOplusLBSMainClass iOplusLBSMainClass = (IOplusLBSMainClass) OplusLbsFactory.getInstance().getFeature(IOplusLBSMainClass.DEFAULT, null);
        this.mOplusLbsClass = iOplusLBSMainClass;
        if (iOplusLBSMainClass != null) {
            this.mPreciseLocationSupported = iOplusLBSMainClass.isPreciseLocationSupported();
        }
    }

    @Override // com.android.server.location.gnss.GnssListenerMultiplexer
    public void addListener(CallerIdentity callerIdentity, IGnssNmeaListener iGnssNmeaListener) {
        super.addListener(callerIdentity, (CallerIdentity) iGnssNmeaListener);
    }

    protected boolean registerWithService(Void r1, Collection<GnssListenerMultiplexer<Void, IGnssNmeaListener, Void>.GnssListenerRegistration> collection) {
        if (this.mGnssNative.startNmeaMessageCollection()) {
            if (!GnssManagerService.D) {
                return true;
            }
            Log.d(GnssManagerService.TAG, "starting gnss nmea messages collection");
            return true;
        }
        Log.e(GnssManagerService.TAG, "error starting gnss nmea messages collection");
        return false;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
        if (this.mGnssNative.stopNmeaMessageCollection()) {
            if (GnssManagerService.D) {
                Log.d(GnssManagerService.TAG, "stopping gnss nmea messages collection");
                return;
            }
            return;
        }
        Log.e(GnssManagerService.TAG, "error stopping gnss nmea messages collection");
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        resetService();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.NmeaCallbacks
    public void onReportNmea(long j) {
        deliverToListeners(new AnonymousClass1(j));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.location.gnss.GnssNmeaProvider$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 implements Function<GnssListenerMultiplexer<Void, IGnssNmeaListener, Void>.GnssListenerRegistration, ListenerExecutor.ListenerOperation<IGnssNmeaListener>> {
        private String mNmea;
        final /* synthetic */ long val$timestamp;

        AnonymousClass1(long j) {
            this.val$timestamp = j;
        }

        @Override // java.util.function.Function
        public ListenerExecutor.ListenerOperation<IGnssNmeaListener> apply(GnssListenerMultiplexer<Void, IGnssNmeaListener, Void>.GnssListenerRegistration gnssListenerRegistration) {
            if (!GnssNmeaProvider.this.mAppOpsHelper.noteOpNoThrow(1, gnssListenerRegistration.getIdentity())) {
                return null;
            }
            if (this.mNmea == null) {
                this.mNmea = new String(GnssNmeaProvider.this.mNmeaBuffer, 0, GnssNmeaProvider.this.mGnssNative.readNmea(GnssNmeaProvider.this.mNmeaBuffer, GnssNmeaProvider.this.mNmeaBuffer.length));
                if (GnssNmeaProvider.this.mPreciseLocationSupported && GnssNmeaProvider.this.mOplusLbsClass != null) {
                    this.mNmea = GnssNmeaProvider.this.mOplusLbsClass.reduceAccuracyOfNmeaSentences(this.mNmea);
                }
            }
            final long j = this.val$timestamp;
            return new ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssNmeaProvider$1$$ExternalSyntheticLambda0
                public final void operate(Object obj) {
                    GnssNmeaProvider.AnonymousClass1.this.lambda$apply$0(j, (IGnssNmeaListener) obj);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$apply$0(long j, IGnssNmeaListener iGnssNmeaListener) throws Exception {
            iGnssNmeaListener.onNmeaReceived(j, this.mNmea);
        }
    }
}
