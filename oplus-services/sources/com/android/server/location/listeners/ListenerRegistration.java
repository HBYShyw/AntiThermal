package com.android.server.location.listeners;

import com.android.internal.listeners.ListenerExecutor;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ListenerRegistration<TListener> implements ListenerExecutor {
    private boolean mActive;
    private final Executor mExecutor;
    private volatile TListener mListener;

    public final boolean equals(Object obj) {
        return this == obj;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getTag() {
        return "ListenerRegistration";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onActive() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onInactive() {
    }

    protected void onListenerUnregister() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onRegister(Object obj) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onUnregister() {
    }

    public String toString() {
        return "[]";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ListenerRegistration(Executor executor, TListener tlistener) {
        Objects.requireNonNull(executor);
        this.mExecutor = executor;
        this.mActive = false;
        Objects.requireNonNull(tlistener);
        this.mListener = tlistener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Executor getExecutor() {
        return this.mExecutor;
    }

    public final boolean isActive() {
        return this.mActive;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean setActive(boolean z) {
        if (z == this.mActive) {
            return false;
        }
        this.mActive = z;
        return true;
    }

    public final boolean isRegistered() {
        return this.mListener != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void unregisterInternal() {
        this.mListener = null;
        onListenerUnregister();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onOperationFailure(ListenerExecutor.ListenerOperation<TListener> listenerOperation, Exception exc) {
        throw new AssertionError(exc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object lambda$executeOperation$0() {
        return this.mListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void executeOperation(ListenerExecutor.ListenerOperation<TListener> listenerOperation) {
        executeSafely(this.mExecutor, new Supplier() { // from class: com.android.server.location.listeners.ListenerRegistration$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                Object lambda$executeOperation$0;
                lambda$executeOperation$0 = ListenerRegistration.this.lambda$executeOperation$0();
                return lambda$executeOperation$0;
            }
        }, listenerOperation, new ListenerExecutor.FailureCallback() { // from class: com.android.server.location.listeners.ListenerRegistration$$ExternalSyntheticLambda1
            public final void onFailure(ListenerExecutor.ListenerOperation listenerOperation2, Exception exc) {
                ListenerRegistration.this.onOperationFailure(listenerOperation2, exc);
            }
        });
    }

    public final int hashCode() {
        return super.hashCode();
    }
}
