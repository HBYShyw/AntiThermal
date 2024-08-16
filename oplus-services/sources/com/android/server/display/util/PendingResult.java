package com.android.server.display.util;

import android.util.Slog;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PendingResult<R> {
    private static final int COUNT_DOWN_THREAD_AMOUNT = 1;
    private static final String TAG = "PendingResult";
    private CountDownLatch mLatch = new CountDownLatch(1);
    private volatile R mResult;

    public PendingResult(R r) {
        this.mResult = r;
    }

    public R await(long j, TimeUnit timeUnit) {
        try {
            this.mLatch.await(j, timeUnit);
        } catch (InterruptedException e) {
            Slog.i(TAG, e.toString());
        }
        return this.mResult;
    }

    public R getResult() {
        return this.mResult;
    }

    public boolean isCounting() {
        return this.mLatch.getCount() == 1;
    }

    public void setResult(R r) {
        this.mResult = r;
        this.mLatch.countDown();
    }

    public void cancel() {
        this.mLatch.countDown();
    }
}
