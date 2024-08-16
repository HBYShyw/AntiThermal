package com.android.server.location.listeners;

import android.app.PendingIntent;
import android.util.Log;
import com.android.internal.listeners.ListenerExecutor;
import com.android.internal.util.ConcurrentUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class PendingIntentListenerRegistration<TKey, TListener> extends RemovableListenerRegistration<TKey, TListener> implements PendingIntent.CancelListener {
    protected abstract PendingIntent getPendingIntentFromKey(TKey tkey);

    /* JADX INFO: Access modifiers changed from: protected */
    public PendingIntentListenerRegistration(TListener tlistener) {
        super(ConcurrentUtils.DIRECT_EXECUTOR, tlistener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.RemovableListenerRegistration
    public void onRegister() {
        super.onRegister();
        if (getPendingIntentFromKey(getKey()).addCancelListener(ConcurrentUtils.DIRECT_EXECUTOR, this)) {
            return;
        }
        remove();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
    public void onUnregister() {
        getPendingIntentFromKey(getKey()).removeCancelListener(this);
        super.onUnregister();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerRegistration
    public void onOperationFailure(ListenerExecutor.ListenerOperation<TListener> listenerOperation, Exception exc) {
        if (exc instanceof PendingIntent.CanceledException) {
            Log.w(getTag(), "registration " + this + " removed", exc);
            remove();
            return;
        }
        super.onOperationFailure(listenerOperation, exc);
    }

    public void onCanceled(PendingIntent pendingIntent) {
        if (Log.isLoggable(getTag(), 3)) {
            Log.d(getTag(), "pending intent registration " + this + " canceled");
        }
        remove();
    }
}
