package com.oplus.deepthinker.sdk.app.api;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.ServiceStateObserver;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import ma.Unit;
import ma.h;
import ma.i;
import ya.l;
import za.k;

/* compiled from: TransactionHandle.kt */
@Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\b\u0016\u0018\u0000 4*\u0004\b\u0000\u0010\u00012\u00020\u0002:\u000245B\u0007¢\u0006\u0004\b2\u00103J+\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00028\u00002\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00050\u0004H\u0002¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\t\u001a\u00020\u0005H\u0002J\u0015\u0010\n\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00028\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u000e\u0010\u000e\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\fJ\b\u0010\u000f\u001a\u00020\u0005H\u0016J#\u0010\u0012\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00050\u0004H\u0000¢\u0006\u0004\b\u0010\u0010\u0011J\u0006\u0010\u0014\u001a\u00020\u0013R.\u0010\u0003\u001a\u0004\u0018\u00018\u00002\b\u0010\u0015\u001a\u0004\u0018\u00018\u00008\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u000bR$\u0010\u001b\u001a\u0004\u0018\u00010\u001a8\u0004@\u0004X\u0084\u000e¢\u0006\u0012\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R$\u0010\"\u001a\u0004\u0018\u00010!8\u0004@\u0004X\u0084\u000e¢\u0006\u0012\n\u0004\b\"\u0010#\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u001a\u0010)\u001a\b\u0012\u0004\u0012\u00020\f0(8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b)\u0010*R(\u0010+\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00050\u00040(8\u0002@\u0002X\u0083\u000e¢\u0006\u0006\n\u0004\b+\u0010*R\u001b\u00101\u001a\u00020,8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b-\u0010.\u001a\u0004\b/\u00100¨\u00066"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/api/TransactionHandle;", "T", "", "remote", "Lkotlin/Function1;", "Lma/f0;", "request", "doTransact", "(Ljava/lang/Object;Lya/l;)V", "onRemoteDisconnected", "onRemoteConnected", "(Ljava/lang/Object;)V", "Lcom/oplus/deepthinker/sdk/app/ServiceStateObserver;", "serviceStateListener", "setServiceStateListener", "release", "transact$com_oplus_deepthinker_sdk_release", "(Lya/l;)V", "transact", "", "doConnect", ThermalBaseConfig.Item.ATTR_VALUE, "Ljava/lang/Object;", "getRemote", "()Ljava/lang/Object;", "setRemote", "Ljava/util/concurrent/Executor;", "targetExecutor", "Ljava/util/concurrent/Executor;", "getTargetExecutor", "()Ljava/util/concurrent/Executor;", "setTargetExecutor", "(Ljava/util/concurrent/Executor;)V", "Lcom/oplus/deepthinker/sdk/app/api/TransactionHandle$IRemoteCallback;", "requestRemoteInvoker", "Lcom/oplus/deepthinker/sdk/app/api/TransactionHandle$IRemoteCallback;", "getRequestRemoteInvoker", "()Lcom/oplus/deepthinker/sdk/app/api/TransactionHandle$IRemoteCallback;", "setRequestRemoteInvoker", "(Lcom/oplus/deepthinker/sdk/app/api/TransactionHandle$IRemoteCallback;)V", "", "stateListenerList", "Ljava/util/List;", "pendingRequest", "Landroid/os/IBinder$DeathRecipient;", "deathRecipient$delegate", "Lma/h;", "getDeathRecipient", "()Landroid/os/IBinder$DeathRecipient;", "deathRecipient", "<init>", "()V", "Companion", "IRemoteCallback", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public class TransactionHandle<T> {
    private static final String TAG = "TransactionHandle";
    private volatile T remote;
    private ya.a<? extends T> remoteGetter;
    private IRemoteCallback requestRemoteInvoker;
    private Executor targetExecutor;
    private final List<ServiceStateObserver> stateListenerList = new ArrayList();

    /* renamed from: deathRecipient$delegate, reason: from kotlin metadata */
    private final h deathRecipient = i.b(new TransactionHandle$deathRecipient$2(this));
    private List<l<T, Unit>> pendingRequest = new ArrayList();

    /* compiled from: TransactionHandle.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H&J\b\u0010\u0004\u001a\u00020\u0002H&¨\u0006\u0005"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/api/TransactionHandle$IRemoteCallback;", "", "Lma/f0;", "onConnectRemote", "onRemoteDisconnected", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public interface IRemoteCallback {
        void onConnectRemote();

        void onRemoteDisconnected();
    }

    private final void doTransact(final T remote, final l<? super T, Unit> request) {
        Unit unit;
        Executor executor = this.targetExecutor;
        if (executor == null) {
            unit = null;
        } else {
            executor.execute(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.api.b
                @Override // java.lang.Runnable
                public final void run() {
                    TransactionHandle.m8doTransact$lambda4(l.this, remote);
                }
            });
            unit = Unit.f15173a;
        }
        if (unit == null) {
            request.invoke(remote);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: doTransact$lambda-4, reason: not valid java name */
    public static final void m8doTransact$lambda4(l lVar, Object obj) {
        k.e(lVar, "$request");
        lVar.invoke(obj);
    }

    private final IBinder.DeathRecipient getDeathRecipient() {
        return (IBinder.DeathRecipient) this.deathRecipient.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onRemoteDisconnected() {
        Iterator<T> it = this.stateListenerList.iterator();
        while (it.hasNext()) {
            ((ServiceStateObserver) it.next()).onServiceDied();
        }
        IRemoteCallback iRemoteCallback = this.requestRemoteInvoker;
        if (iRemoteCallback == null) {
            return;
        }
        iRemoteCallback.onRemoteDisconnected();
    }

    public final boolean doConnect() {
        if (this.remote == null) {
            IRemoteCallback iRemoteCallback = this.requestRemoteInvoker;
            if (iRemoteCallback == null) {
                SDKLog.w(TAG, "requestRemoteInvoker is null!");
                return false;
            }
            iRemoteCallback.onConnectRemote();
        }
        return true;
    }

    public final T getRemote() {
        return this.remote;
    }

    protected final IRemoteCallback getRequestRemoteInvoker() {
        return this.requestRemoteInvoker;
    }

    protected final Executor getTargetExecutor() {
        return this.targetExecutor;
    }

    public final void onRemoteConnected(T remote) {
        setRemote(remote);
        while (this.pendingRequest.size() > 0) {
            synchronized (this) {
                Iterator<T> it = this.pendingRequest.iterator();
                while (it.hasNext()) {
                    transact$com_oplus_deepthinker_sdk_release((l) it.next());
                }
                this.pendingRequest.clear();
                Unit unit = Unit.f15173a;
            }
        }
    }

    public void release() {
        IBinder asBinder;
        try {
            T t7 = this.remote;
            IInterface iInterface = t7 instanceof IInterface ? (IInterface) t7 : null;
            if (iInterface != null && (asBinder = iInterface.asBinder()) != null) {
                asBinder.unlinkToDeath(getDeathRecipient(), 0);
            }
        } catch (NoSuchElementException unused) {
        }
    }

    public final void setRemote(T t7) {
        IBinder asBinder;
        IBinder asBinder2;
        synchronized (this) {
            try {
                T t10 = this.remote;
                IInterface iInterface = t10 instanceof IInterface ? (IInterface) t10 : null;
                if (iInterface != null && (asBinder2 = iInterface.asBinder()) != null) {
                    asBinder2.unlinkToDeath(getDeathRecipient(), 0);
                }
            } catch (NoSuchElementException unused) {
            }
            this.remote = t7;
            try {
                T t11 = this.remote;
                IInterface iInterface2 = t11 instanceof IInterface ? (IInterface) t11 : null;
                if (iInterface2 != null && (asBinder = iInterface2.asBinder()) != null) {
                    asBinder.linkToDeath(getDeathRecipient(), 0);
                    Unit unit = Unit.f15173a;
                }
            } catch (RemoteException unused2) {
                Unit unit2 = Unit.f15173a;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setRequestRemoteInvoker(IRemoteCallback iRemoteCallback) {
        this.requestRemoteInvoker = iRemoteCallback;
    }

    public final void setServiceStateListener(ServiceStateObserver serviceStateObserver) {
        k.e(serviceStateObserver, "serviceStateListener");
        this.stateListenerList.add(serviceStateObserver);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setTargetExecutor(Executor executor) {
        this.targetExecutor = executor;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void transact$com_oplus_deepthinker_sdk_release(l<? super T, Unit> request) {
        IRemoteCallback iRemoteCallback;
        k.e(request, "request");
        T t7 = this.remote;
        if (t7 == null) {
            ya.a<? extends T> aVar = this.remoteGetter;
            t7 = aVar == null ? null : aVar.invoke();
        }
        boolean z10 = false;
        if (t7 != null) {
            if (t7 instanceof IBinder) {
                if (((IBinder) t7).isBinderAlive()) {
                    doTransact(t7, request);
                }
            } else {
                doTransact(t7, request);
            }
            if (z10) {
                return;
            }
            if (this.requestRemoteInvoker != null) {
                synchronized (this) {
                    this.pendingRequest.add(request);
                }
                if (this.remote != null || (iRemoteCallback = this.requestRemoteInvoker) == null) {
                    return;
                }
                iRemoteCallback.onConnectRemote();
                return;
            }
            SDKLog.w(TAG, "Remote is not alive!");
            return;
        }
        z10 = true;
        if (z10) {
        }
    }
}
