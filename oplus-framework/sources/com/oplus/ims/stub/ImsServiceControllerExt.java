package com.oplus.ims.stub;

import android.content.Context;
import android.util.Log;
import com.android.internal.telephony.util.TelephonyUtils;
import com.oplus.ims.IImsExt;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/* loaded from: classes.dex */
public class ImsServiceControllerExt {
    private static final String TAG = "ImsServiceControllerExt";
    protected Context mContext;
    private Executor mExecutor;
    protected final Object mLock = new Object();
    private ImsExtStub mImsExtStub = new ImsExtStub();

    public ImsServiceControllerExt(Context context, Executor executor) {
        this.mContext = context;
        this.mExecutor = executor;
    }

    private void executeMethodAsync(final Runnable r, String errorLogName) {
        try {
            CompletableFuture.runAsync(new Runnable() { // from class: com.oplus.ims.stub.ImsServiceControllerExt$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyUtils.runWithCleanCallingIdentity(r);
                }
            }, this.mExecutor).join();
        } catch (CancellationException | CompletionException e) {
            Log.w(TAG, "MmTelFeature Binder - " + errorLogName + " exception: " + e.getMessage());
        }
    }

    private <T> T executeMethodAsyncForResult(final Supplier<T> r, String errorLogName) {
        CompletableFuture<T> future = CompletableFuture.supplyAsync(new Supplier() { // from class: com.oplus.ims.stub.ImsServiceControllerExt$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                Object runWithCleanCallingIdentity;
                runWithCleanCallingIdentity = TelephonyUtils.runWithCleanCallingIdentity(r);
                return runWithCleanCallingIdentity;
            }
        }, this.mExecutor);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w(TAG, "MmTelFeature Binder - " + errorLogName + " exception: " + e.getMessage());
            return null;
        }
    }

    private void enforceOplusPhoneState(String message) {
        this.mContext.enforceCallingOrSelfPermission("com.oplus.permission.safe.PHONE", message);
    }

    public IImsExt getIImsExt() {
        return this.mImsExtStub;
    }

    /* loaded from: classes.dex */
    private class ImsExtStub extends IImsExt.Stub {
        private ImsExtStub() {
        }
    }
}
