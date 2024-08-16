package com.android.server.servicewatcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.content.PackageMonitor;
import com.android.internal.util.Preconditions;
import com.android.server.servicewatcher.ServiceWatcher;
import com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo;
import com.android.server.servicewatcher.ServiceWatcherImpl;
import java.io.PrintWriter;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ServiceWatcherImpl<TBoundServiceInfo extends ServiceWatcher.BoundServiceInfo> implements ServiceWatcher, ServiceWatcher.ServiceChangedListener {
    static final long RETRY_DELAY_MS = 15000;
    final Context mContext;
    final Handler mHandler;
    private final PackageMonitor mPackageMonitor = new PackageMonitor() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl.1
        public boolean onPackageChanged(String str, int i, String[] strArr) {
            return true;
        }

        public void onSomePackagesChanged() {
            ServiceWatcherImpl.this.onServiceChanged(false);
        }
    };

    @GuardedBy({"this"})
    private boolean mRegistered = false;

    @GuardedBy({"this"})
    private ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection mServiceConnection = new MyServiceConnection(null);
    final ServiceWatcher.ServiceListener<? super TBoundServiceInfo> mServiceListener;
    final ServiceWatcher.ServiceSupplier<TBoundServiceInfo> mServiceSupplier;
    final String mTag;
    static final String TAG = "ServiceWatcher";
    static final boolean D = Log.isLoggable(TAG, 3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public ServiceWatcherImpl(Context context, Handler handler, String str, ServiceWatcher.ServiceSupplier<TBoundServiceInfo> serviceSupplier, ServiceWatcher.ServiceListener<? super TBoundServiceInfo> serviceListener) {
        this.mContext = context;
        this.mHandler = handler;
        this.mTag = str;
        this.mServiceSupplier = serviceSupplier;
        this.mServiceListener = serviceListener;
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public boolean checkServiceResolves() {
        return this.mServiceSupplier.hasMatchingService();
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public synchronized void register() {
        Preconditions.checkState(!this.mRegistered);
        this.mRegistered = true;
        this.mPackageMonitor.getWrapper().getExtImpl().register(this.mContext, UserHandle.ALL, true, this.mHandler, new int[]{2, 19});
        this.mServiceSupplier.register(this);
        onServiceChanged(false);
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public synchronized void unregister() {
        Preconditions.checkState(this.mRegistered);
        this.mServiceSupplier.unregister();
        this.mPackageMonitor.unregister();
        this.mRegistered = false;
        onServiceChanged(false);
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceChangedListener
    public synchronized void onServiceChanged() {
        onServiceChanged(false);
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public synchronized void runOnBinder(final ServiceWatcher.BinderOperation binderOperation) {
        final ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection myServiceConnection = this.mServiceConnection;
        this.mHandler.post(new Runnable() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ServiceWatcherImpl.MyServiceConnection.this.runOnBinder(binderOperation);
            }
        });
    }

    synchronized void onServiceChanged(boolean z) {
        TBoundServiceInfo serviceInfo = this.mRegistered ? this.mServiceSupplier.getServiceInfo() : null;
        if ((z | (!this.mServiceConnection.isConnected())) || !Objects.equals(this.mServiceConnection.getBoundServiceInfo(), serviceInfo)) {
            Log.i(TAG, "[" + this.mTag + "] chose new implementation " + serviceInfo);
            final ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection myServiceConnection = this.mServiceConnection;
            final ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection myServiceConnection2 = new MyServiceConnection(serviceInfo);
            this.mServiceConnection = myServiceConnection2;
            this.mHandler.post(new Runnable() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ServiceWatcherImpl.lambda$onServiceChanged$1(ServiceWatcherImpl.MyServiceConnection.this, myServiceConnection2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onServiceChanged$1(MyServiceConnection myServiceConnection, MyServiceConnection myServiceConnection2) {
        myServiceConnection.unbind();
        myServiceConnection2.bind();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.server.servicewatcher.ServiceWatcher$BoundServiceInfo] */
    public String toString() {
        ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection myServiceConnection;
        synchronized (this) {
            myServiceConnection = this.mServiceConnection;
        }
        return myServiceConnection.getBoundServiceInfo().toString();
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public void dump(PrintWriter printWriter) {
        ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection myServiceConnection;
        synchronized (this) {
            myServiceConnection = this.mServiceConnection;
        }
        printWriter.println("target service=" + myServiceConnection.getBoundServiceInfo());
        printWriter.println("connected=" + myServiceConnection.isConnected());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class MyServiceConnection implements ServiceConnection {
        private volatile IBinder mBinder;
        private final TBoundServiceInfo mBoundServiceInfo;
        private Runnable mRebinder;

        MyServiceConnection(TBoundServiceInfo tboundserviceinfo) {
            this.mBoundServiceInfo = tboundserviceinfo;
        }

        TBoundServiceInfo getBoundServiceInfo() {
            return this.mBoundServiceInfo;
        }

        boolean isConnected() {
            return this.mBinder != null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void bind() {
            Preconditions.checkState(Looper.myLooper() == ServiceWatcherImpl.this.mHandler.getLooper());
            if (this.mBoundServiceInfo == null) {
                return;
            }
            if (ServiceWatcherImpl.D) {
                Log.d(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] binding to " + this.mBoundServiceInfo);
            }
            this.mRebinder = null;
            Intent component = new Intent(this.mBoundServiceInfo.getAction()).setComponent(this.mBoundServiceInfo.getComponentName());
            try {
                ServiceWatcherImpl serviceWatcherImpl = ServiceWatcherImpl.this;
                if (serviceWatcherImpl.mContext.bindServiceAsUser(component, this, 1073741829, serviceWatcherImpl.mHandler, UserHandle.of(this.mBoundServiceInfo.getUserId()))) {
                    return;
                }
                Log.e(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] unexpected bind failure - retrying later");
                Runnable runnable = new Runnable() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl$MyServiceConnection$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ServiceWatcherImpl.MyServiceConnection.this.bind();
                    }
                };
                this.mRebinder = runnable;
                ServiceWatcherImpl.this.mHandler.postDelayed(runnable, ServiceWatcherImpl.RETRY_DELAY_MS);
            } catch (SecurityException e) {
                Log.e(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] " + this.mBoundServiceInfo + " bind failed", e);
            }
        }

        void unbind() {
            Preconditions.checkState(Looper.myLooper() == ServiceWatcherImpl.this.mHandler.getLooper());
            if (this.mBoundServiceInfo == null) {
                return;
            }
            if (ServiceWatcherImpl.D) {
                Log.d(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] unbinding from " + this.mBoundServiceInfo);
            }
            Runnable runnable = this.mRebinder;
            if (runnable != null) {
                ServiceWatcherImpl.this.mHandler.removeCallbacks(runnable);
                this.mRebinder = null;
            } else {
                ServiceWatcherImpl.this.mContext.unbindService(this);
            }
            onServiceDisconnected(this.mBoundServiceInfo.getComponentName());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void runOnBinder(ServiceWatcher.BinderOperation binderOperation) {
            Preconditions.checkState(Looper.myLooper() == ServiceWatcherImpl.this.mHandler.getLooper());
            if (this.mBinder == null) {
                binderOperation.onError(new DeadObjectException());
                return;
            }
            try {
                binderOperation.run(this.mBinder);
            } catch (RemoteException | RuntimeException e) {
                Log.e(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] error running operation on " + this.mBoundServiceInfo, e);
                binderOperation.onError(e);
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Preconditions.checkState(Looper.myLooper() == ServiceWatcherImpl.this.mHandler.getLooper());
            Preconditions.checkState(this.mBinder == null);
            Log.i(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] connected to " + componentName.toShortString());
            this.mBinder = iBinder;
            ServiceWatcher.ServiceListener<? super TBoundServiceInfo> serviceListener = ServiceWatcherImpl.this.mServiceListener;
            if (serviceListener != null) {
                try {
                    serviceListener.onBind(iBinder, this.mBoundServiceInfo);
                } catch (RemoteException | RuntimeException e) {
                    Log.e(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] error running operation on " + this.mBoundServiceInfo, e);
                }
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
            Preconditions.checkState(Looper.myLooper() == ServiceWatcherImpl.this.mHandler.getLooper());
            if (this.mBinder == null) {
                return;
            }
            Log.i(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] disconnected from " + this.mBoundServiceInfo);
            this.mBinder = null;
            ServiceWatcher.ServiceListener<? super TBoundServiceInfo> serviceListener = ServiceWatcherImpl.this.mServiceListener;
            if (serviceListener != null) {
                serviceListener.onUnbind();
            }
        }

        @Override // android.content.ServiceConnection
        public final void onBindingDied(ComponentName componentName) {
            Preconditions.checkState(Looper.myLooper() == ServiceWatcherImpl.this.mHandler.getLooper());
            Log.w(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] " + this.mBoundServiceInfo + " died");
            ServiceWatcherImpl.this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl$MyServiceConnection$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ServiceWatcherImpl.MyServiceConnection.this.lambda$onBindingDied$0();
                }
            }, 500L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindingDied$0() {
            ServiceWatcherImpl.this.onServiceChanged(true);
        }

        @Override // android.content.ServiceConnection
        public final void onNullBinding(ComponentName componentName) {
            Log.e(ServiceWatcherImpl.TAG, "[" + ServiceWatcherImpl.this.mTag + "] " + this.mBoundServiceInfo + " has null binding");
        }
    }
}
