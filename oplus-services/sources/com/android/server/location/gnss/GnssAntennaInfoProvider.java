package com.android.server.location.gnss;

import android.location.GnssAntennaInfo;
import android.location.IGnssAntennaInfoListener;
import android.location.util.identity.CallerIdentity;
import android.os.Binder;
import android.os.IBinder;
import com.android.internal.listeners.ListenerExecutor;
import com.android.internal.util.ConcurrentUtils;
import com.android.server.FgThread;
import com.android.server.location.gnss.hal.GnssNative;
import com.android.server.location.listeners.BinderListenerRegistration;
import com.android.server.location.listeners.ListenerMultiplexer;
import com.android.server.location.listeners.ListenerRegistration;
import java.util.Collection;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssAntennaInfoProvider extends ListenerMultiplexer<IBinder, IGnssAntennaInfoListener, ListenerRegistration<IGnssAntennaInfoListener>, Void> implements GnssNative.BaseCallbacks, GnssNative.AntennaInfoCallbacks {
    private volatile List<GnssAntennaInfo> mAntennaInfos;
    private final GnssNative mGnssNative;

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected boolean isActive(ListenerRegistration<IGnssAntennaInfoListener> listenerRegistration) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public Void mergeRegistrations(Collection<ListenerRegistration<IGnssAntennaInfoListener>> collection) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public boolean registerWithService(Void r1, Collection<ListenerRegistration<IGnssAntennaInfoListener>> collection) {
        return true;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    protected class AntennaInfoListenerRegistration extends BinderListenerRegistration<IBinder, IGnssAntennaInfoListener> {
        private final CallerIdentity mIdentity;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.BinderListenerRegistration
        public IBinder getBinderFromKey(IBinder iBinder) {
            return iBinder;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.ListenerRegistration
        public String getTag() {
            return GnssManagerService.TAG;
        }

        protected AntennaInfoListenerRegistration(CallerIdentity callerIdentity, IGnssAntennaInfoListener iGnssAntennaInfoListener) {
            super(callerIdentity.isMyProcess() ? FgThread.getExecutor() : ConcurrentUtils.DIRECT_EXECUTOR, iGnssAntennaInfoListener);
            this.mIdentity = callerIdentity;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.RemovableListenerRegistration
        public GnssAntennaInfoProvider getOwner() {
            return GnssAntennaInfoProvider.this;
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public String toString() {
            return this.mIdentity.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GnssAntennaInfoProvider(GnssNative gnssNative) {
        this.mGnssNative = gnssNative;
        gnssNative.addBaseCallbacks(this);
        gnssNative.addAntennaInfoCallbacks(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<GnssAntennaInfo> getAntennaInfos() {
        return this.mAntennaInfos;
    }

    public boolean isSupported() {
        return this.mGnssNative.isAntennaInfoSupported();
    }

    public void addListener(CallerIdentity callerIdentity, IGnssAntennaInfoListener iGnssAntennaInfoListener) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            putRegistration(iGnssAntennaInfoListener.asBinder(), new AntennaInfoListenerRegistration(callerIdentity, iGnssAntennaInfoListener));
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void removeListener(IGnssAntennaInfoListener iGnssAntennaInfoListener) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            removeRegistration((GnssAntennaInfoProvider) iGnssAntennaInfoListener.asBinder());
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalStarted() {
        this.mGnssNative.startAntennaInfoListening();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        this.mGnssNative.startAntennaInfoListening();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.AntennaInfoCallbacks
    public void onReportAntennaInfo(final List<GnssAntennaInfo> list) {
        if (list.equals(this.mAntennaInfos)) {
            return;
        }
        this.mAntennaInfos = list;
        deliverToListeners(new ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.gnss.GnssAntennaInfoProvider$$ExternalSyntheticLambda0
            public final void operate(Object obj) {
                ((IGnssAntennaInfoListener) obj).onGnssAntennaInfoChanged(list);
            }
        });
    }
}
