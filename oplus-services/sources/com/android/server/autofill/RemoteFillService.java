package com.android.server.autofill;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.Handler;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.RemoteException;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.IAutoFillService;
import android.service.autofill.IFillCallback;
import android.service.autofill.ISaveCallback;
import android.service.autofill.SaveRequest;
import android.util.Slog;
import com.android.internal.infra.AbstractRemoteService;
import com.android.internal.infra.AndroidFuture;
import com.android.internal.infra.ServiceConnector;
import com.android.internal.os.IResultReceiver;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class RemoteFillService extends ServiceConnector.Impl<IAutoFillService> {
    private static final String TAG = "RemoteFillService";
    private static final long TIMEOUT_IDLE_BIND_MILLIS = 5000;
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 5000;
    private final FillServiceCallbacks mCallbacks;
    private final ComponentName mComponentName;
    private final Object mLock;
    private CompletableFuture<FillResponse> mPendingFillRequest;
    private int mPendingFillRequestId;
    private IRemoteFillServiceExt mRemoteFillServiceExt;
    private IRemoteFillServiceWrapper mWrapper;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface FillServiceCallbacks extends AbstractRemoteService.VultureCallback<RemoteFillService> {
        void onFillRequestFailure(int i, CharSequence charSequence);

        void onFillRequestSuccess(int i, FillResponse fillResponse, String str, int i2);

        void onFillRequestTimeout(int i);

        void onSaveRequestFailure(CharSequence charSequence, String str);

        void onSaveRequestSuccess(String str, IntentSender intentSender);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteFillService(Context context, ComponentName componentName, int i, FillServiceCallbacks fillServiceCallbacks, boolean z) {
        super(context, new Intent("android.service.autofill.AutofillService").setComponent(componentName), (z ? AudioDevice.OUT_SPEAKER_SAFE : 0) | AudioDevice.OUT_FM, i, new Function() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return IAutoFillService.Stub.asInterface((IBinder) obj);
            }
        });
        this.mRemoteFillServiceExt = (IRemoteFillServiceExt) ExtLoader.type(IRemoteFillServiceExt.class).create();
        this.mLock = new Object();
        this.mPendingFillRequestId = Integer.MIN_VALUE;
        this.mWrapper = new RemoteFillServiceWrapper();
        this.mCallbacks = fillServiceCallbacks;
        this.mComponentName = componentName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(IAutoFillService iAutoFillService, boolean z) {
        try {
            iAutoFillService.onConnectedStateChanged(z);
        } catch (Exception e) {
            Slog.w(TAG, "Exception calling onConnectedStateChanged(" + z + "): " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchCancellationSignal(ICancellationSignal iCancellationSignal) {
        if (iCancellationSignal == null) {
            return;
        }
        try {
            iCancellationSignal.cancel();
        } catch (RemoteException e) {
            Slog.e(TAG, "Error requesting a cancellation", e);
        }
    }

    protected long getAutoDisconnectTimeoutMs() {
        return this.mRemoteFillServiceExt.getOplusTimeoutIdleBindMillis(this.mComponentName, 5000L);
    }

    public void addLast(ServiceConnector.Job<IAutoFillService, ?> job) {
        cancelPendingJobs();
        super.addLast(job);
    }

    public int cancelCurrentRequest() {
        int i;
        synchronized (this.mLock) {
            CompletableFuture<FillResponse> completableFuture = this.mPendingFillRequest;
            i = (completableFuture == null || !completableFuture.cancel(false)) ? Integer.MIN_VALUE : this.mPendingFillRequestId;
        }
        return i;
    }

    public void onFillRequest(final FillRequest fillRequest) {
        if (Helper.sVerbose) {
            Slog.v(TAG, "onFillRequest:" + fillRequest);
        }
        final AtomicReference atomicReference = new AtomicReference();
        final AtomicReference atomicReference2 = new AtomicReference();
        AndroidFuture orTimeout = postAsync(new ServiceConnector.Job() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda4
            public final Object run(Object obj) {
                CompletableFuture lambda$onFillRequest$0;
                lambda$onFillRequest$0 = RemoteFillService.this.lambda$onFillRequest$0(fillRequest, atomicReference2, atomicReference, (IAutoFillService) obj);
                return lambda$onFillRequest$0;
            }
        }).orTimeout(this.mRemoteFillServiceExt.getOplusTimeoutMillis(this.mComponentName, 5000L), TimeUnit.MILLISECONDS);
        atomicReference2.set(orTimeout);
        synchronized (this.mLock) {
            this.mPendingFillRequest = orTimeout;
            this.mPendingFillRequestId = fillRequest.getId();
        }
        orTimeout.whenComplete(new BiConsumer() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                RemoteFillService.this.lambda$onFillRequest$2(fillRequest, atomicReference, (FillResponse) obj, (Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletableFuture lambda$onFillRequest$0(FillRequest fillRequest, final AtomicReference atomicReference, final AtomicReference atomicReference2, IAutoFillService iAutoFillService) throws Exception {
        if (Helper.sVerbose) {
            Slog.v(TAG, "calling onFillRequest() for id=" + fillRequest.getId());
        }
        final CompletableFuture completableFuture = new CompletableFuture();
        iAutoFillService.onFillRequest(fillRequest, new IFillCallback.Stub() { // from class: com.android.server.autofill.RemoteFillService.1
            public void onCancellable(ICancellationSignal iCancellationSignal) {
                CompletableFuture completableFuture2 = (CompletableFuture) atomicReference.get();
                if (completableFuture2 != null && completableFuture2.isCancelled()) {
                    RemoteFillService.this.dispatchCancellationSignal(iCancellationSignal);
                } else {
                    atomicReference2.set(iCancellationSignal);
                }
            }

            public void onSuccess(FillResponse fillResponse) {
                completableFuture.complete(fillResponse);
            }

            public void onFailure(int i, CharSequence charSequence) {
                completableFuture.completeExceptionally(new RuntimeException(charSequence == null ? "" : String.valueOf(charSequence)));
            }
        });
        return completableFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFillRequest$2(final FillRequest fillRequest, final AtomicReference atomicReference, final FillResponse fillResponse, final Throwable th) {
        Handler.getMain().post(new Runnable() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                RemoteFillService.this.lambda$onFillRequest$1(th, fillRequest, fillResponse, atomicReference);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFillRequest$1(Throwable th, FillRequest fillRequest, FillResponse fillResponse, AtomicReference atomicReference) {
        synchronized (this.mLock) {
            this.mPendingFillRequest = null;
            this.mPendingFillRequestId = Integer.MIN_VALUE;
        }
        if (th == null) {
            this.mCallbacks.onFillRequestSuccess(fillRequest.getId(), fillResponse, this.mComponentName.getPackageName(), fillRequest.getFlags());
            return;
        }
        Slog.e(TAG, "Error calling on fill request", th);
        if (th instanceof TimeoutException) {
            dispatchCancellationSignal((ICancellationSignal) atomicReference.get());
            this.mCallbacks.onFillRequestTimeout(fillRequest.getId());
        } else if (th instanceof CancellationException) {
            dispatchCancellationSignal((ICancellationSignal) atomicReference.get());
        } else {
            this.mCallbacks.onFillRequestFailure(fillRequest.getId(), th.getMessage());
        }
    }

    public void onSaveRequest(final SaveRequest saveRequest) {
        postAsync(new ServiceConnector.Job() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda0
            public final Object run(Object obj) {
                CompletableFuture lambda$onSaveRequest$3;
                lambda$onSaveRequest$3 = RemoteFillService.this.lambda$onSaveRequest$3(saveRequest, (IAutoFillService) obj);
                return lambda$onSaveRequest$3;
            }
        }).orTimeout(this.mRemoteFillServiceExt.getOplusTimeoutMillis(this.mComponentName, 5000L), TimeUnit.MILLISECONDS).whenComplete(new BiConsumer() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                RemoteFillService.this.lambda$onSaveRequest$5((IntentSender) obj, (Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ CompletableFuture lambda$onSaveRequest$3(SaveRequest saveRequest, IAutoFillService iAutoFillService) throws Exception {
        if (Helper.sVerbose) {
            Slog.v(TAG, "calling onSaveRequest()");
        }
        final CompletableFuture completableFuture = new CompletableFuture();
        iAutoFillService.onSaveRequest(saveRequest, new ISaveCallback.Stub() { // from class: com.android.server.autofill.RemoteFillService.2
            public void onSuccess(IntentSender intentSender) {
                completableFuture.complete(intentSender);
            }

            public void onFailure(CharSequence charSequence) {
                completableFuture.completeExceptionally(new RuntimeException(String.valueOf(charSequence)));
            }
        });
        return completableFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSaveRequest$5(final IntentSender intentSender, final Throwable th) {
        Handler.getMain().post(new Runnable() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                RemoteFillService.this.lambda$onSaveRequest$4(th, intentSender);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSaveRequest$4(Throwable th, IntentSender intentSender) {
        if (th == null) {
            this.mCallbacks.onSaveRequestSuccess(this.mComponentName.getPackageName(), intentSender);
        } else {
            this.mCallbacks.onSaveRequestFailure(this.mComponentName.getPackageName(), th.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSavedPasswordCountRequest(final IResultReceiver iResultReceiver) {
        run(new ServiceConnector.VoidJob() { // from class: com.android.server.autofill.RemoteFillService$$ExternalSyntheticLambda3
            public final void runNoResult(Object obj) {
                ((IAutoFillService) obj).onSavedPasswordCountRequest(iResultReceiver);
            }
        });
    }

    protected long getRequestTimeoutMs() {
        return this.mRemoteFillServiceExt.getOplusRequestTimeoutMillis(this.mComponentName, super.getRequestTimeoutMs());
    }

    public void destroy() {
        unbind();
    }

    public IRemoteFillServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class RemoteFillServiceWrapper implements IRemoteFillServiceWrapper {
        private RemoteFillServiceWrapper() {
        }

        @Override // com.android.server.autofill.IRemoteFillServiceWrapper
        public IRemoteFillServiceExt getRemoteFillServiceExt() {
            return RemoteFillService.this.mRemoteFillServiceExt;
        }

        @Override // com.android.server.autofill.IRemoteFillServiceWrapper
        public void delayCancelRequest(List<FillContext> list) {
            synchronized (RemoteFillService.this.mLock) {
                RemoteFillService.this.mRemoteFillServiceExt.delayCancelRequest(list, RemoteFillService.this.mPendingFillRequestId, RemoteFillService.this.mPendingFillRequest);
            }
        }
    }
}
