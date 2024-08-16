package com.oplus.flexiblewindow;

import java.lang.reflect.Array;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/* loaded from: classes.dex */
public interface FlexibleExecutor extends Executor {
    @Override // java.util.concurrent.Executor
    void execute(Runnable runnable);

    void executeDelayed(Runnable runnable, long j);

    boolean hasCallback(Runnable runnable);

    void removeCallbacks(Runnable runnable);

    default void executeBlocking(final Runnable runnable, int waitTimeout, TimeUnit waitTimeUnit) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleExecutor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FlexibleExecutor.lambda$executeBlocking$0(runnable, latch);
            }
        });
        latch.await(waitTimeout, waitTimeUnit);
    }

    static /* synthetic */ void lambda$executeBlocking$0(Runnable runnable, CountDownLatch latch) {
        runnable.run();
        latch.countDown();
    }

    default void executeBlocking(Runnable runnable) throws InterruptedException {
        executeBlocking(runnable, 2, TimeUnit.SECONDS);
    }

    default <T> T executeBlockingForResult(final Supplier<T> supplier, Class cls) {
        final Object[] objArr = (Object[]) Array.newInstance((Class<?>) cls, 1);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleExecutor$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FlexibleExecutor.lambda$executeBlockingForResult$1(objArr, supplier, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
            return (T) objArr[0];
        } catch (InterruptedException e) {
            return null;
        }
    }

    static /* synthetic */ void lambda$executeBlockingForResult$1(Object[] result, Supplier runnable, CountDownLatch latch) {
        result[0] = runnable.get();
        latch.countDown();
    }
}
