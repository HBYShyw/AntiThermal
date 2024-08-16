package com.android.server.wm;

import android.os.Process;
import android.os.SystemClock;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.wm.PersisterQueue;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class PersisterQueue {
    private static final boolean DEBUG = false;
    static final WriteQueueItem EMPTY_ITEM = new WriteQueueItem() { // from class: com.android.server.wm.PersisterQueue$$ExternalSyntheticLambda1
        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public final void process() {
            PersisterQueue.lambda$static$0();
        }
    };
    private static final long FLUSH_QUEUE = -1;
    private static final long INTER_WRITE_DELAY_MS = 500;
    private static final int MAX_WRITE_QUEUE_LENGTH = 6;
    private static final long PRE_TASK_DELAY_MS = 3000;
    private static final String TAG = "PersisterQueue";
    private final long mInterWriteDelayMs;
    private final LazyTaskWriterThread mLazyTaskWriterThread;
    private final ArrayList<Listener> mListeners;
    private long mNextWriteTime;
    private final long mPreTaskDelayMs;
    private final ArrayList<WriteQueueItem> mWriteQueue;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    interface Listener {
        void onPreProcessItem(boolean z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface WriteQueueItem<T extends WriteQueueItem<T>> {
        default boolean matches(T t) {
            return false;
        }

        void process();

        default void updateFrom(T t) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$0() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PersisterQueue() {
        this(INTER_WRITE_DELAY_MS, PRE_TASK_DELAY_MS);
    }

    @VisibleForTesting
    PersisterQueue(long j, long j2) {
        this.mWriteQueue = new ArrayList<>();
        this.mListeners = new ArrayList<>();
        this.mNextWriteTime = 0L;
        if (j < 0 || j2 < 0) {
            throw new IllegalArgumentException("Both inter-write delay and pre-task delay need tobe non-negative. inter-write delay: " + j + "ms pre-task delay: " + j2);
        }
        this.mInterWriteDelayMs = j;
        this.mPreTaskDelayMs = j2;
        this.mLazyTaskWriterThread = new LazyTaskWriterThread("LazyTaskWriterThread");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void startPersisting() {
        if (!this.mLazyTaskWriterThread.isAlive()) {
            this.mLazyTaskWriterThread.start();
        }
    }

    @VisibleForTesting
    void stopPersisting() throws InterruptedException {
        if (this.mLazyTaskWriterThread.isAlive()) {
            synchronized (this) {
                this.mLazyTaskWriterThread.interrupt();
            }
            this.mLazyTaskWriterThread.join();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addItem(WriteQueueItem writeQueueItem, boolean z) {
        this.mWriteQueue.add(writeQueueItem);
        if (!z && this.mWriteQueue.size() <= 6) {
            if (this.mNextWriteTime == 0) {
                this.mNextWriteTime = SystemClock.uptimeMillis() + this.mPreTaskDelayMs;
            }
            notify();
        }
        this.mNextWriteTime = FLUSH_QUEUE;
        notify();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized <T extends WriteQueueItem> T findLastItem(Predicate<T> predicate, Class<T> cls) {
        for (int size = this.mWriteQueue.size() - 1; size >= 0; size--) {
            WriteQueueItem writeQueueItem = this.mWriteQueue.get(size);
            if (cls.isInstance(writeQueueItem)) {
                T cast = cls.cast(writeQueueItem);
                if (predicate.test(cast)) {
                    return cast;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized <T extends WriteQueueItem> void updateLastOrAddItem(final T t, boolean z) {
        Objects.requireNonNull(t);
        WriteQueueItem findLastItem = findLastItem(new Predicate() { // from class: com.android.server.wm.PersisterQueue$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return PersisterQueue.WriteQueueItem.this.matches((PersisterQueue.WriteQueueItem) obj);
            }
        }, t.getClass());
        if (findLastItem == null) {
            addItem(t, z);
        } else {
            findLastItem.updateFrom(t);
        }
        yieldIfQueueTooDeep();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized <T extends WriteQueueItem> void removeItems(Predicate<T> predicate, Class<T> cls) {
        for (int size = this.mWriteQueue.size() - 1; size >= 0; size--) {
            WriteQueueItem writeQueueItem = this.mWriteQueue.get(size);
            if (cls.isInstance(writeQueueItem) && predicate.test(cls.cast(writeQueueItem))) {
                this.mWriteQueue.remove(size);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void flush() {
        this.mNextWriteTime = FLUSH_QUEUE;
        notifyAll();
        do {
            try {
                wait();
            } catch (InterruptedException unused) {
            }
        } while (this.mNextWriteTime == FLUSH_QUEUE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void yieldIfQueueTooDeep() {
        boolean z;
        synchronized (this) {
            z = this.mNextWriteTime == FLUSH_QUEUE;
        }
        if (z) {
            Thread.yield();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }

    @VisibleForTesting
    boolean removeListener(Listener listener) {
        return this.mListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNextItem() throws InterruptedException {
        WriteQueueItem remove;
        synchronized (this) {
            if (this.mNextWriteTime != FLUSH_QUEUE) {
                this.mNextWriteTime = SystemClock.uptimeMillis() + this.mInterWriteDelayMs;
            }
            while (this.mWriteQueue.isEmpty()) {
                if (this.mNextWriteTime != 0) {
                    this.mNextWriteTime = 0L;
                    notify();
                }
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                wait();
            }
            remove = this.mWriteQueue.remove(0);
            long uptimeMillis = SystemClock.uptimeMillis();
            while (true) {
                long j = this.mNextWriteTime;
                if (uptimeMillis < j) {
                    wait(j - uptimeMillis);
                    uptimeMillis = SystemClock.uptimeMillis();
                }
            }
        }
        remove.process();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class LazyTaskWriterThread extends Thread {
        private LazyTaskWriterThread(String str) {
            super(str);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            boolean isEmpty;
            Process.setThreadPriority(10);
            while (true) {
                try {
                    synchronized (PersisterQueue.this) {
                        isEmpty = PersisterQueue.this.mWriteQueue.isEmpty();
                    }
                    for (int size = PersisterQueue.this.mListeners.size() - 1; size >= 0; size--) {
                        ((Listener) PersisterQueue.this.mListeners.get(size)).onPreProcessItem(isEmpty);
                    }
                    PersisterQueue.this.processNextItem();
                } catch (InterruptedException unused) {
                    Slog.e(PersisterQueue.TAG, "Persister thread is exiting. Should never happen in prod, butit's OK in tests.");
                    return;
                }
            }
        }
    }
}
