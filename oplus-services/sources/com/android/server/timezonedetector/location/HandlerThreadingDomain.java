package com.android.server.timezonedetector.location;

import android.os.Handler;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class HandlerThreadingDomain extends ThreadingDomain {
    private final Handler mHandler;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HandlerThreadingDomain(Handler handler) {
        Objects.requireNonNull(handler);
        this.mHandler = handler;
    }

    Handler getHandler() {
        return this.mHandler;
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    Thread getThread() {
        return getHandler().getLooper().getThread();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    public void post(Runnable runnable) {
        getHandler().post(runnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    public <V> V postAndWait(final Callable<V> callable, long j) throws Exception {
        assertNotCurrentThread();
        final AtomicReference atomicReference = new AtomicReference();
        final AtomicReference atomicReference2 = new AtomicReference();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        post(new Runnable() { // from class: com.android.server.timezonedetector.location.HandlerThreadingDomain$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                HandlerThreadingDomain.lambda$postAndWait$0(atomicReference, callable, atomicReference2, countDownLatch);
            }
        });
        try {
            if (!countDownLatch.await(j, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Timed out");
            }
            if (atomicReference2.get() != null) {
                throw ((Exception) atomicReference2.get());
            }
            return (V) atomicReference.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$postAndWait$0(AtomicReference atomicReference, Callable callable, AtomicReference atomicReference2, CountDownLatch countDownLatch) {
        try {
            try {
                atomicReference.set(callable.call());
            } catch (Exception e) {
                atomicReference2.set(e);
            }
        } finally {
            countDownLatch.countDown();
        }
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    void postDelayed(Runnable runnable, long j) {
        getHandler().postDelayed(runnable, j);
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    void postDelayed(Runnable runnable, Object obj, long j) {
        getHandler().postDelayed(runnable, obj, j);
    }

    @Override // com.android.server.timezonedetector.location.ThreadingDomain
    void removeQueuedRunnables(Object obj) {
        getHandler().removeCallbacksAndMessages(obj);
    }
}
