package com.android.server.location.provider.proxy;

import android.content.Context;
import android.location.Location;
import android.location.LocationResult;
import android.location.provider.ILocationProvider;
import android.location.provider.ILocationProviderManager;
import android.location.provider.ProviderProperties;
import android.location.provider.ProviderRequest;
import android.location.util.identity.CallerIdentity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ConcurrentUtils;
import com.android.server.FgThread;
import com.android.server.location.LocationManagerService;
import com.android.server.location.provider.AbstractLocationProvider;
import com.android.server.location.provider.proxy.ProxyLocationProvider;
import com.android.server.servicewatcher.CurrentUserServiceSupplier;
import com.android.server.servicewatcher.ServiceWatcher;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ProxyLocationProvider extends AbstractLocationProvider implements ServiceWatcher.ServiceListener<CurrentUserServiceSupplier.BoundServiceInfo> {
    private static final String EXTRA_LOCATION_TAGS = "android:location_allow_listed_tags";
    private static final String LOCATION_TAGS_SEPARATOR = ";";
    private static final long RESET_DELAY_MS = 1000;

    @GuardedBy({"mLock"})
    CurrentUserServiceSupplier.BoundServiceInfo mBoundServiceInfo;
    final Context mContext;

    @GuardedBy({"mLock"})
    final ArrayList<Runnable> mFlushListeners;
    final Object mLock;
    final String mName;

    @GuardedBy({"mLock"})
    Proxy mProxy;
    private volatile ProviderRequest mRequest;

    @GuardedBy({"mLock"})
    Runnable mResetter;
    final ServiceWatcher mServiceWatcher;

    public static ProxyLocationProvider create(Context context, String str, String str2, int i, int i2) {
        ProxyLocationProvider proxyLocationProvider = new ProxyLocationProvider(context, str, str2, i, i2);
        if (proxyLocationProvider.checkServiceResolves()) {
            return proxyLocationProvider;
        }
        return null;
    }

    private ProxyLocationProvider(Context context, String str, String str2, int i, int i2) {
        super(ConcurrentUtils.DIRECT_EXECUTOR, null, null, Collections.emptySet());
        this.mLock = new Object();
        this.mFlushListeners = new ArrayList<>(0);
        this.mContext = context;
        this.mServiceWatcher = ServiceWatcher.create(context, str, CurrentUserServiceSupplier.createFromConfig(context, str2, i, i2), this);
        this.mName = str;
        this.mProxy = null;
        this.mRequest = ProviderRequest.EMPTY_REQUEST;
    }

    private boolean checkServiceResolves() {
        return this.mServiceWatcher.checkServiceResolves();
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onBind(IBinder iBinder, CurrentUserServiceSupplier.BoundServiceInfo boundServiceInfo) throws RemoteException {
        ILocationProvider asInterface = ILocationProvider.Stub.asInterface(iBinder);
        synchronized (this.mLock) {
            Proxy proxy = new Proxy();
            this.mProxy = proxy;
            this.mBoundServiceInfo = boundServiceInfo;
            asInterface.setLocationProviderManager(proxy);
            ProviderRequest providerRequest = this.mRequest;
            if (!providerRequest.equals(ProviderRequest.EMPTY_REQUEST)) {
                asInterface.setRequest(providerRequest);
            }
        }
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onUnbind() {
        int i;
        Runnable[] runnableArr;
        synchronized (this.mLock) {
            this.mProxy = null;
            this.mBoundServiceInfo = null;
            if (this.mResetter == null) {
                this.mResetter = new AnonymousClass1();
                FgThread.getHandler().postDelayed(this.mResetter, 1000L);
            }
            runnableArr = (Runnable[]) this.mFlushListeners.toArray(new Runnable[0]);
            this.mFlushListeners.clear();
        }
        for (Runnable runnable : runnableArr) {
            runnable.run();
        }
    }

    /* renamed from: com.android.server.location.provider.proxy.ProxyLocationProvider$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (ProxyLocationProvider.this.mLock) {
                ProxyLocationProvider proxyLocationProvider = ProxyLocationProvider.this;
                if (proxyLocationProvider.mResetter == this) {
                    proxyLocationProvider.setState(new UnaryOperator() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            AbstractLocationProvider.State lambda$run$0;
                            lambda$run$0 = ProxyLocationProvider.AnonymousClass1.lambda$run$0((AbstractLocationProvider.State) obj);
                            return lambda$run$0;
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ AbstractLocationProvider.State lambda$run$0(AbstractLocationProvider.State state) {
            return AbstractLocationProvider.State.EMPTY_STATE;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void onStart() {
        this.mServiceWatcher.register();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void onStop() {
        this.mServiceWatcher.unregister();
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onSetRequest(final ProviderRequest providerRequest) {
        this.mRequest = providerRequest;
        this.mServiceWatcher.runOnBinder(new ServiceWatcher.BinderOperation() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider$$ExternalSyntheticLambda0
            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public final void run(IBinder iBinder) {
                ProxyLocationProvider.lambda$onSetRequest$0(providerRequest, iBinder);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onSetRequest$0(ProviderRequest providerRequest, IBinder iBinder) throws RemoteException {
        ILocationProvider.Stub.asInterface(iBinder).setRequest(providerRequest);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onFlush(final Runnable runnable) {
        this.mServiceWatcher.runOnBinder(new ServiceWatcher.BinderOperation() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider.2
            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public void run(IBinder iBinder) throws RemoteException {
                ILocationProvider asInterface = ILocationProvider.Stub.asInterface(iBinder);
                synchronized (ProxyLocationProvider.this.mLock) {
                    ProxyLocationProvider.this.mFlushListeners.add(runnable);
                }
                asInterface.flush();
            }

            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public void onError(Throwable th) {
                synchronized (ProxyLocationProvider.this.mLock) {
                    ProxyLocationProvider.this.mFlushListeners.remove(runnable);
                }
                runnable.run();
            }
        });
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void onExtraCommand(int i, int i2, final String str, final Bundle bundle) {
        this.mServiceWatcher.runOnBinder(new ServiceWatcher.BinderOperation() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider$$ExternalSyntheticLambda1
            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public final void run(IBinder iBinder) {
                ProxyLocationProvider.lambda$onExtraCommand$1(str, bundle, iBinder);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onExtraCommand$1(String str, Bundle bundle, IBinder iBinder) throws RemoteException {
        ILocationProvider.Stub.asInterface(iBinder).sendExtraCommand(str, bundle);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mServiceWatcher.dump(printWriter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class Proxy extends ILocationProviderManager.Stub {
        Proxy() {
        }

        public void onInitialize(final boolean z, final ProviderProperties providerProperties, String str) {
            synchronized (ProxyLocationProvider.this.mLock) {
                ProxyLocationProvider proxyLocationProvider = ProxyLocationProvider.this;
                if (proxyLocationProvider.mProxy != this) {
                    return;
                }
                if (proxyLocationProvider.mResetter != null) {
                    FgThread.getHandler().removeCallbacks(ProxyLocationProvider.this.mResetter);
                    ProxyLocationProvider.this.mResetter = null;
                }
                String[] strArr = new String[0];
                if (ProxyLocationProvider.this.mBoundServiceInfo.getMetadata() != null) {
                    String string = ProxyLocationProvider.this.mBoundServiceInfo.getMetadata().getString(ProxyLocationProvider.EXTRA_LOCATION_TAGS);
                    if (!TextUtils.isEmpty(string)) {
                        strArr = string.split(ProxyLocationProvider.LOCATION_TAGS_SEPARATOR);
                        Log.i(LocationManagerService.TAG, ProxyLocationProvider.this.mName + " provider loaded extra attribution tags: " + Arrays.toString(strArr));
                    }
                }
                final ArraySet arraySet = new ArraySet(strArr);
                final CallerIdentity fromBinderUnsafe = CallerIdentity.fromBinderUnsafe(ProxyLocationProvider.this.mBoundServiceInfo.getComponentName().getPackageName(), str);
                ProxyLocationProvider.this.setState(new UnaryOperator() { // from class: com.android.server.location.provider.proxy.ProxyLocationProvider$Proxy$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        AbstractLocationProvider.State lambda$onInitialize$0;
                        lambda$onInitialize$0 = ProxyLocationProvider.Proxy.lambda$onInitialize$0(z, providerProperties, fromBinderUnsafe, arraySet, (AbstractLocationProvider.State) obj);
                        return lambda$onInitialize$0;
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ AbstractLocationProvider.State lambda$onInitialize$0(boolean z, ProviderProperties providerProperties, CallerIdentity callerIdentity, ArraySet arraySet, AbstractLocationProvider.State state) {
            return AbstractLocationProvider.State.EMPTY_STATE.withAllowed(z).withProperties(providerProperties).withIdentity(callerIdentity).withExtraAttributionTags(arraySet);
        }

        public void onSetProperties(ProviderProperties providerProperties) {
            synchronized (ProxyLocationProvider.this.mLock) {
                ProxyLocationProvider proxyLocationProvider = ProxyLocationProvider.this;
                if (proxyLocationProvider.mProxy != this) {
                    return;
                }
                proxyLocationProvider.setProperties(providerProperties);
            }
        }

        public void onSetAllowed(boolean z) {
            synchronized (ProxyLocationProvider.this.mLock) {
                ProxyLocationProvider proxyLocationProvider = ProxyLocationProvider.this;
                if (proxyLocationProvider.mProxy != this) {
                    return;
                }
                proxyLocationProvider.setAllowed(z);
            }
        }

        public void onReportLocation(Location location) {
            synchronized (ProxyLocationProvider.this.mLock) {
                ProxyLocationProvider proxyLocationProvider = ProxyLocationProvider.this;
                if (proxyLocationProvider.mProxy != this) {
                    return;
                }
                proxyLocationProvider.reportLocation(LocationResult.wrap(new Location[]{location}).validate());
            }
        }

        public void onReportLocations(List<Location> list) {
            synchronized (ProxyLocationProvider.this.mLock) {
                ProxyLocationProvider proxyLocationProvider = ProxyLocationProvider.this;
                if (proxyLocationProvider.mProxy != this) {
                    return;
                }
                proxyLocationProvider.reportLocation(LocationResult.wrap(list).validate());
            }
        }

        public void onFlushComplete() {
            synchronized (ProxyLocationProvider.this.mLock) {
                ProxyLocationProvider proxyLocationProvider = ProxyLocationProvider.this;
                if (proxyLocationProvider.mProxy != this) {
                    return;
                }
                Runnable remove = !proxyLocationProvider.mFlushListeners.isEmpty() ? ProxyLocationProvider.this.mFlushListeners.remove(0) : null;
                if (remove != null) {
                    remove.run();
                }
            }
        }
    }
}
