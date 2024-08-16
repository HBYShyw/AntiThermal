package com.android.server.timezonedetector.location;

import com.android.internal.util.Preconditions;
import com.android.server.timezonedetector.location.ThreadingDomain;
import java.util.concurrent.Callable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class ThreadingDomain {
    private final Object mLockObject = new Object();

    abstract Thread getThread();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void post(Runnable runnable);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract <V> V postAndWait(Callable<V> callable, long j) throws Exception;

    abstract void postDelayed(Runnable runnable, long j);

    abstract void postDelayed(Runnable runnable, Object obj, long j);

    abstract void removeQueuedRunnables(Object obj);

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getLockObject() {
        return this.mLockObject;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assertCurrentThread() {
        Preconditions.checkState(Thread.currentThread() == getThread());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assertNotCurrentThread() {
        Preconditions.checkState(Thread.currentThread() != getThread());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void postAndWait(final Runnable runnable, long j) {
        try {
            postAndWait(new Callable() { // from class: com.android.server.timezonedetector.location.ThreadingDomain$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    Object lambda$postAndWait$0;
                    lambda$postAndWait$0 = ThreadingDomain.lambda$postAndWait$0(runnable);
                    return lambda$postAndWait$0;
                }
            }, j);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object lambda$postAndWait$0(Runnable runnable) throws Exception {
        runnable.run();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SingleRunnableQueue createSingleRunnableQueue() {
        return new SingleRunnableQueue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class SingleRunnableQueue {
        private long mDelayMillis;
        private boolean mIsQueued;

        SingleRunnableQueue() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void runDelayed(final Runnable runnable, long j) {
            cancel();
            this.mIsQueued = true;
            this.mDelayMillis = j;
            ThreadingDomain.this.postDelayed(new Runnable() { // from class: com.android.server.timezonedetector.location.ThreadingDomain$SingleRunnableQueue$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ThreadingDomain.SingleRunnableQueue.this.lambda$runDelayed$0(runnable);
                }
            }, this, j);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$runDelayed$0(Runnable runnable) {
            this.mIsQueued = false;
            this.mDelayMillis = -2L;
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean hasQueued() {
            ThreadingDomain.this.assertCurrentThread();
            return this.mIsQueued;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getQueuedDelayMillis() {
            ThreadingDomain.this.assertCurrentThread();
            if (!this.mIsQueued) {
                throw new IllegalStateException("No item queued");
            }
            return this.mDelayMillis;
        }

        public void cancel() {
            ThreadingDomain.this.assertCurrentThread();
            if (this.mIsQueued) {
                ThreadingDomain.this.removeQueuedRunnables(this);
            }
            this.mIsQueued = false;
            this.mDelayMillis = -1L;
        }
    }
}
