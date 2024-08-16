package com.android.server.biometrics.sensors;

import android.hardware.biometrics.IBiometricService;
import android.hardware.biometrics.SensorPropertiesInternal;
import android.os.Handler;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.ServiceThread;
import com.android.server.biometrics.sensors.BiometricServiceProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class BiometricServiceRegistry<T extends BiometricServiceProvider<P>, P extends SensorPropertiesInternal, C extends IInterface> {
    private static final String TAG = "BiometricServiceRegistry";
    private volatile List<P> mAllProps;
    private final Supplier<IBiometricService> mBiometricServiceSupplier;
    private final RemoteCallbackList<C> mRegisteredCallbacks = new RemoteCallbackList<>();
    private volatile List<T> mServiceProviders;

    protected abstract void invokeRegisteredCallback(C c, List<P> list) throws RemoteException;

    protected abstract void registerService(IBiometricService iBiometricService, P p);

    public BiometricServiceRegistry(Supplier<IBiometricService> supplier) {
        this.mBiometricServiceSupplier = supplier;
    }

    public void registerAll(final Supplier<List<T>> supplier) {
        ServiceThread serviceThread = new ServiceThread(TAG, 10, true);
        serviceThread.start();
        new Handler(serviceThread.getLooper()).post(new Runnable() { // from class: com.android.server.biometrics.sensors.BiometricServiceRegistry$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BiometricServiceRegistry.this.lambda$registerAll$0(supplier);
            }
        });
        serviceThread.quitSafely();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @VisibleForTesting
    /* renamed from: registerAllInBackground, reason: merged with bridge method [inline-methods] */
    public void lambda$registerAll$0(Supplier<List<T>> supplier) {
        List<T> list = supplier.get();
        if (list == null) {
            list = new ArrayList<>();
        }
        IBiometricService iBiometricService = this.mBiometricServiceSupplier.get();
        if (iBiometricService == null) {
            throw new IllegalStateException("biometric service cannot be null");
        }
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            List sensorProperties = it.next().getSensorProperties();
            Iterator it2 = sensorProperties.iterator();
            while (it2.hasNext()) {
                registerService(iBiometricService, (SensorPropertiesInternal) it2.next());
            }
            arrayList.addAll(sensorProperties);
        }
        finishRegistration(list, arrayList);
    }

    private synchronized void finishRegistration(List<T> list, List<P> list2) {
        this.mServiceProviders = Collections.unmodifiableList(list);
        this.mAllProps = Collections.unmodifiableList(list2);
        broadcastAllAuthenticatorsRegistered();
    }

    public synchronized void addAllRegisteredCallback(C c) {
        if (c == null) {
            Slog.e(TAG, "addAllRegisteredCallback, callback is null");
            return;
        }
        boolean register = this.mRegisteredCallbacks.register(c);
        boolean z = this.mServiceProviders != null;
        if (register && z) {
            broadcastAllAuthenticatorsRegistered();
        } else if (!register) {
            Slog.e(TAG, "addAllRegisteredCallback failed to register callback");
        }
    }

    private synchronized void broadcastAllAuthenticatorsRegistered() {
        RemoteCallbackList<C> remoteCallbackList;
        int beginBroadcast = this.mRegisteredCallbacks.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            C broadcastItem = this.mRegisteredCallbacks.getBroadcastItem(i);
            try {
                try {
                    invokeRegisteredCallback(broadcastItem, this.mAllProps);
                    remoteCallbackList = this.mRegisteredCallbacks;
                } catch (Throwable th) {
                    this.mRegisteredCallbacks.unregister(broadcastItem);
                    throw th;
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception in broadcastAllAuthenticatorsRegistered", e);
                remoteCallbackList = this.mRegisteredCallbacks;
            }
            remoteCallbackList.unregister(broadcastItem);
        }
        this.mRegisteredCallbacks.finishBroadcast();
    }

    public List<T> getProviders() {
        return this.mServiceProviders != null ? this.mServiceProviders : Collections.emptyList();
    }

    public T getProviderForSensor(int i) {
        if (this.mServiceProviders == null) {
            return null;
        }
        for (T t : this.mServiceProviders) {
            if (t.containsSensor(i)) {
                return t;
            }
        }
        return null;
    }

    public Pair<Integer, T> getSingleProvider() {
        String str;
        if (this.mAllProps == null || this.mAllProps.isEmpty()) {
            Slog.e(TAG, "No sensors found");
            return null;
        }
        try {
            if (this.mAllProps.size() > 1) {
                Slog.e(TAG, "getSingleProvider() called but multiple sensors present: " + this.mAllProps.size());
            }
            int i = ((SensorPropertiesInternal) this.mAllProps.get(0)).sensorId;
            T providerForSensor = getProviderForSensor(i);
            if (providerForSensor != null) {
                return new Pair<>(Integer.valueOf(i), providerForSensor);
            }
            Slog.e(TAG, "Single sensor: " + i + ", but provider not found");
            return null;
        } catch (NullPointerException e) {
            if (this.mAllProps == null) {
                str = "mAllProps: null";
            } else {
                str = "mAllProps.size(): " + this.mAllProps.size();
            }
            Slog.e(TAG, "This shouldn't happen. " + str, e);
            throw e;
        }
    }

    public List<P> getAllProperties() {
        return this.mAllProps != null ? this.mAllProps : Collections.emptyList();
    }
}
