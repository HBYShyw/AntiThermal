package com.android.server.location.provider;

import android.location.LocationResult;
import android.location.provider.ProviderRequest;
import android.os.Bundle;
import com.android.internal.util.Preconditions;
import com.android.server.location.provider.AbstractLocationProvider;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.function.UnaryOperator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DelegateLocationProvider extends AbstractLocationProvider implements AbstractLocationProvider.Listener {
    protected final AbstractLocationProvider mDelegate;
    private final Object mInitializationLock;
    private boolean mInitialized;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ AbstractLocationProvider.State lambda$onStateChanged$1(AbstractLocationProvider.State state, AbstractLocationProvider.State state2) {
        return state;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DelegateLocationProvider(Executor executor, AbstractLocationProvider abstractLocationProvider) {
        super(executor, null, null, Collections.emptySet());
        this.mInitializationLock = new Object();
        this.mInitialized = false;
        this.mDelegate = abstractLocationProvider;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initializeDelegate() {
        synchronized (this.mInitializationLock) {
            Preconditions.checkState(!this.mInitialized);
            setState(new UnaryOperator() { // from class: com.android.server.location.provider.DelegateLocationProvider$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    AbstractLocationProvider.State lambda$initializeDelegate$0;
                    lambda$initializeDelegate$0 = DelegateLocationProvider.this.lambda$initializeDelegate$0((AbstractLocationProvider.State) obj);
                    return lambda$initializeDelegate$0;
                }
            });
            this.mInitialized = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ AbstractLocationProvider.State lambda$initializeDelegate$0(AbstractLocationProvider.State state) {
        return this.mDelegate.getController().setListener(this);
    }

    protected final void waitForInitialization() {
        synchronized (this.mInitializationLock) {
            Preconditions.checkState(this.mInitialized);
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
    public void onStateChanged(AbstractLocationProvider.State state, final AbstractLocationProvider.State state2) {
        waitForInitialization();
        setState(new UnaryOperator() { // from class: com.android.server.location.provider.DelegateLocationProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                AbstractLocationProvider.State lambda$onStateChanged$1;
                lambda$onStateChanged$1 = DelegateLocationProvider.lambda$onStateChanged$1(AbstractLocationProvider.State.this, (AbstractLocationProvider.State) obj);
                return lambda$onStateChanged$1;
            }
        });
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
    public void onReportLocation(LocationResult locationResult) {
        waitForInitialization();
        reportLocation(locationResult);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void onStart() {
        Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().start();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void onStop() {
        Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().stop();
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onSetRequest(ProviderRequest providerRequest) {
        Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().setRequest(providerRequest);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onFlush(Runnable runnable) {
        Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().flush(runnable);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onExtraCommand(int i, int i2, String str, Bundle bundle) {
        Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().sendExtraCommand(i, i2, str, bundle);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Preconditions.checkState(this.mInitialized);
        this.mDelegate.dump(fileDescriptor, printWriter, strArr);
    }
}
