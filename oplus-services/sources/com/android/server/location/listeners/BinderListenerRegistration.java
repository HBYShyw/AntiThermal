package com.android.server.location.listeners;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.listeners.ListenerExecutor;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class BinderListenerRegistration<TKey, TListener> extends RemovableListenerRegistration<TKey, TListener> implements IBinder.DeathRecipient {
    protected abstract IBinder getBinderFromKey(TKey tkey);

    /* JADX INFO: Access modifiers changed from: protected */
    public BinderListenerRegistration(Executor executor, TListener tlistener) {
        super(executor, tlistener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.RemovableListenerRegistration
    public void onRegister() {
        super.onRegister();
        try {
            getBinderFromKey(getKey()).linkToDeath(this, 0);
        } catch (RemoteException unused) {
            remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
    public void onUnregister() {
        try {
            getBinderFromKey(getKey()).unlinkToDeath(this, 0);
        } catch (NoSuchElementException e) {
            Log.w(getTag(), "failed to unregister binder death listener", e);
        }
        super.onUnregister();
    }

    @Override // com.android.server.location.listeners.ListenerRegistration
    public void onOperationFailure(ListenerExecutor.ListenerOperation<TListener> listenerOperation, Exception exc) {
        if (exc instanceof RemoteException) {
            Log.w(getTag(), "registration " + this + " removed", exc);
            remove();
            return;
        }
        super.onOperationFailure(listenerOperation, exc);
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        try {
            if (Log.isLoggable(getTag(), 3)) {
                Log.d(getTag(), "binder registration " + this + " died");
            }
            remove();
        } catch (RuntimeException e) {
            throw new AssertionError(e);
        }
    }
}
