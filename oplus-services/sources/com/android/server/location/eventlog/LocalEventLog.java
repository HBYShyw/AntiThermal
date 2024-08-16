package com.android.server.location.eventlog;

import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LocalEventLog<T> {
    private static final int IS_FILLER_MASK = Integer.MIN_VALUE;
    private static final int TIME_DELTA_MASK = Integer.MAX_VALUE;

    @GuardedBy({"this"})
    int[] mEntries;

    @GuardedBy({"this"})
    long mLastLogTime;

    @GuardedBy({"this"})
    int mLogEndIndex;

    @GuardedBy({"this"})
    T[] mLogEvents;

    @GuardedBy({"this"})
    int mLogSize;

    @GuardedBy({"this"})
    long mModificationCount;

    @GuardedBy({"this"})
    long mStartTime;
    private final LocalEventLog<T>.LocalEventLogWrapper mWrapper = new LocalEventLogWrapper();
    private static final int IS_FILLER_OFFSET = countTrailingZeros(Integer.MIN_VALUE);
    private static final int TIME_DELTA_OFFSET = countTrailingZeros(Integer.MAX_VALUE);

    @VisibleForTesting
    static final int MAX_TIME_DELTA = (1 << Integer.bitCount(Integer.MAX_VALUE)) - 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface LogConsumer<T> {
        void acceptLog(long j, T t);
    }

    private static int countTrailingZeros(int i) {
        int i2 = 0;
        while (i != 0 && (i & 1) == 0) {
            i2++;
            i >>>= 1;
        }
        return i2;
    }

    static boolean isFiller(int i) {
        return (i & Integer.MIN_VALUE) != 0;
    }

    private static int createEntry(boolean z, int i) {
        Preconditions.checkArgument(i >= 0 && i <= MAX_TIME_DELTA);
        return (((z ? 1 : 0) << IS_FILLER_OFFSET) & Integer.MIN_VALUE) | ((i << TIME_DELTA_OFFSET) & Integer.MAX_VALUE);
    }

    static int getTimeDelta(int i) {
        return (i & Integer.MAX_VALUE) >>> TIME_DELTA_OFFSET;
    }

    public LocalEventLog(int i, Class<T> cls) {
        Preconditions.checkArgument(i > 0);
        this.mEntries = new int[i];
        this.mLogEvents = (T[]) ((Object[]) Array.newInstance((Class<?>) cls, i));
        this.mLogSize = 0;
        this.mLogEndIndex = 0;
        this.mStartTime = -1L;
        this.mLastLogTime = -1L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void addLog(long j, T t) {
        Preconditions.checkArgument(t != null);
        long j2 = 0;
        if (!isEmpty()) {
            long j3 = j - this.mLastLogTime;
            if (j3 >= 0 && j3 / MAX_TIME_DELTA < this.mEntries.length - 1) {
                j2 = j3;
                while (true) {
                    int i = MAX_TIME_DELTA;
                    if (j2 < i) {
                        break;
                    }
                    addLogEventInternal(true, i, null);
                    j2 -= i;
                }
            }
            clear();
        }
        if (isEmpty()) {
            this.mStartTime = j;
            this.mLastLogTime = j;
            this.mModificationCount++;
        }
        addLogEventInternal(false, (int) j2, t);
    }

    @GuardedBy({"this"})
    private void addLogEventInternal(boolean z, int i, T t) {
        boolean z2 = false;
        Preconditions.checkArgument(z || t != null);
        if (this.mStartTime != -1 && this.mLastLogTime != -1) {
            z2 = true;
        }
        Preconditions.checkState(z2);
        int i2 = this.mLogSize;
        if (i2 == this.mEntries.length) {
            this.mStartTime += getTimeDelta(r2[startIndex()]);
            this.mModificationCount++;
        } else {
            this.mLogSize = i2 + 1;
        }
        this.mEntries[this.mLogEndIndex] = createEntry(z, i);
        T[] tArr = this.mLogEvents;
        int i3 = this.mLogEndIndex;
        tArr[i3] = t;
        this.mLogEndIndex = incrementIndex(i3);
        this.mLastLogTime += i;
    }

    public synchronized void clear() {
        Arrays.fill(this.mLogEvents, (Object) null);
        this.mLogEndIndex = 0;
        this.mLogSize = 0;
        this.mModificationCount++;
        this.mStartTime = -1L;
        this.mLastLogTime = -1L;
    }

    @GuardedBy({"this"})
    private boolean isEmpty() {
        return this.mLogSize == 0;
    }

    public synchronized void iterate(LogConsumer<? super T> logConsumer) {
        LogIterator logIterator = new LogIterator();
        while (logIterator.hasNext()) {
            logIterator.next();
            logConsumer.acceptLog(logIterator.getTime(), (Object) logIterator.getLog());
        }
    }

    @SafeVarargs
    public static <T> void iterate(LogConsumer<? super T> logConsumer, LocalEventLog<T>... localEventLogArr) {
        ArrayList arrayList = new ArrayList(localEventLogArr.length);
        for (LocalEventLog<T> localEventLog : localEventLogArr) {
            Objects.requireNonNull(localEventLog);
            LogIterator logIterator = new LogIterator();
            if (logIterator.hasNext()) {
                arrayList.add(logIterator);
                logIterator.next();
            }
        }
        while (true) {
            Iterator it = arrayList.iterator();
            LogIterator logIterator2 = null;
            while (it.hasNext()) {
                LogIterator logIterator3 = (LogIterator) it.next();
                if (logIterator3 != null && (logIterator2 == null || logIterator3.getTime() < logIterator2.getTime())) {
                    logIterator2 = logIterator3;
                }
            }
            if (logIterator2 == null) {
                return;
            }
            logConsumer.acceptLog(logIterator2.getTime(), (Object) logIterator2.getLog());
            if (logIterator2.hasNext()) {
                logIterator2.next();
            } else {
                arrayList.remove(logIterator2);
            }
        }
    }

    @GuardedBy({"this"})
    int startIndex() {
        return wrapIndex(this.mLogEndIndex - this.mLogSize);
    }

    @GuardedBy({"this"})
    int incrementIndex(int i) {
        if (i == -1) {
            return startIndex();
        }
        if (i >= 0) {
            return wrapIndex(i + 1);
        }
        throw new IllegalArgumentException();
    }

    @GuardedBy({"this"})
    int wrapIndex(int i) {
        int[] iArr = this.mEntries;
        return ((i % iArr.length) + iArr.length) % iArr.length;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    protected final class LogIterator {
        private int mCount;
        private T mCurrentLogEvent;
        private long mCurrentTime;
        private int mIndex;
        private long mLogTime;
        private final long mModificationCount;

        public LogIterator() {
            synchronized (LocalEventLog.this) {
                this.mModificationCount = LocalEventLog.this.mModificationCount;
                this.mLogTime = LocalEventLog.this.mStartTime;
                this.mIndex = -1;
                this.mCount = -1;
                increment();
            }
        }

        public boolean hasNext() {
            boolean z;
            synchronized (LocalEventLog.this) {
                checkModifications();
                z = this.mCount < LocalEventLog.this.mLogSize;
            }
            return z;
        }

        public void next() {
            synchronized (LocalEventLog.this) {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                this.mCurrentTime = this.mLogTime + LocalEventLog.getTimeDelta(LocalEventLog.this.mEntries[this.mIndex]);
                T t = LocalEventLog.this.mLogEvents[this.mIndex];
                Objects.requireNonNull(t);
                this.mCurrentLogEvent = t;
                increment();
            }
        }

        public long getTime() {
            return this.mCurrentTime;
        }

        public T getLog() {
            return this.mCurrentLogEvent;
        }

        @GuardedBy({"LocalEventLog.this"})
        private void increment() {
            LocalEventLog localEventLog;
            long timeDelta = this.mIndex == -1 ? 0L : LocalEventLog.getTimeDelta(LocalEventLog.this.mEntries[r0]);
            do {
                this.mLogTime += timeDelta;
                int incrementIndex = LocalEventLog.this.incrementIndex(this.mIndex);
                this.mIndex = incrementIndex;
                int i = this.mCount + 1;
                this.mCount = i;
                LocalEventLog localEventLog2 = LocalEventLog.this;
                if (i < localEventLog2.mLogSize) {
                    timeDelta = LocalEventLog.getTimeDelta(localEventLog2.mEntries[incrementIndex]);
                }
                int i2 = this.mCount;
                localEventLog = LocalEventLog.this;
                if (i2 >= localEventLog.mLogSize) {
                    return;
                }
            } while (LocalEventLog.isFiller(localEventLog.mEntries[this.mIndex]));
        }

        @GuardedBy({"LocalEventLog.this"})
        private void checkModifications() {
            if (this.mModificationCount != LocalEventLog.this.mModificationCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public ILocalEventLogWrapper getLocalWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LocalEventLogWrapper implements ILocalEventLogWrapper {
        public LocalEventLogWrapper() {
        }

        @Override // com.android.server.location.eventlog.ILocalEventLogWrapper
        public void updateEventsLogSize(int i) {
            int max = Math.max(LocalEventLog.this.mEntries.length, i);
            int[] copyOf = Arrays.copyOf(LocalEventLog.this.mEntries, max);
            LocalEventLog localEventLog = LocalEventLog.this;
            localEventLog.mEntries = copyOf;
            LocalEventLog.this.mLogEvents = (T[]) Arrays.copyOf(localEventLog.mLogEvents, max);
        }
    }
}
