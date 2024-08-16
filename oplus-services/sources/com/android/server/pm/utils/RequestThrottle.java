package com.android.server.pm.utils;

import android.os.Handler;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RequestThrottle {
    private static final int DEFAULT_BACKOFF_BASE = 2;
    private static final int DEFAULT_DELAY_MS = 1000;
    private static final int DEFAULT_RETRY_MAX_ATTEMPTS = 5;
    private final int mBackoffBase;
    private final Supplier<Boolean> mBlock;
    private final AtomicInteger mCurrentRetry;
    private final int mFirstDelay;
    private final Handler mHandler;
    private final AtomicInteger mLastCommitted;
    private final AtomicInteger mLastRequest;
    private final int mMaxAttempts;
    private final Runnable mRunnable;

    public RequestThrottle(Handler handler, Supplier<Boolean> supplier) {
        this(handler, 5, 1000, 2, supplier);
    }

    public RequestThrottle(Handler handler, int i, int i2, int i3, Supplier<Boolean> supplier) {
        this.mLastRequest = new AtomicInteger(0);
        this.mLastCommitted = new AtomicInteger(-1);
        this.mCurrentRetry = new AtomicInteger(0);
        this.mHandler = handler;
        this.mBlock = supplier;
        this.mMaxAttempts = i;
        this.mFirstDelay = i2;
        this.mBackoffBase = i3;
        this.mRunnable = new Runnable() { // from class: com.android.server.pm.utils.RequestThrottle$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RequestThrottle.this.runInternal();
            }
        };
    }

    public void schedule() {
        this.mLastRequest.incrementAndGet();
        this.mHandler.post(this.mRunnable);
    }

    public boolean runNow() {
        this.mLastRequest.incrementAndGet();
        return runInternal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean runInternal() {
        int i = this.mLastRequest.get();
        if (i == this.mLastCommitted.get()) {
            return true;
        }
        if (this.mBlock.get().booleanValue()) {
            this.mCurrentRetry.set(0);
            this.mLastCommitted.set(i);
            return true;
        }
        int andIncrement = this.mCurrentRetry.getAndIncrement();
        if (andIncrement < this.mMaxAttempts) {
            this.mHandler.postDelayed(this.mRunnable, (long) (this.mFirstDelay * Math.pow(this.mBackoffBase, andIncrement)));
        } else {
            this.mCurrentRetry.set(0);
        }
        return false;
    }
}
