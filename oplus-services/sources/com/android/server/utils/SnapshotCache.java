package com.android.server.utils;

import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class SnapshotCache<T> extends Watcher {
    private static final boolean ENABLED = true;
    private static final WeakHashMap<SnapshotCache, Void> sCaches = new WeakHashMap<>();
    private volatile boolean mSealed;
    private volatile T mSnapshot;
    protected final T mSource;
    private final Statistics mStatistics;

    public abstract T createSnapshot();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Statistics {
        final String mName;
        private final AtomicInteger mReused = new AtomicInteger(0);
        private final AtomicInteger mRebuilt = new AtomicInteger(0);

        Statistics(String str) {
            this.mName = str;
        }
    }

    public SnapshotCache(T t, Watchable watchable, String str) {
        this.mSnapshot = null;
        this.mSealed = false;
        this.mSource = t;
        watchable.registerObserver(this);
        if (str != null) {
            this.mStatistics = new Statistics(str);
            sCaches.put(this, null);
        } else {
            this.mStatistics = null;
        }
    }

    public SnapshotCache(T t, Watchable watchable) {
        this(t, watchable, null);
    }

    public SnapshotCache() {
        this.mSnapshot = null;
        this.mSealed = false;
        this.mSource = null;
        this.mSealed = true;
        this.mStatistics = null;
    }

    @Override // com.android.server.utils.Watcher
    public final void onChange(Watchable watchable) {
        if (this.mSealed) {
            throw new IllegalStateException("attempt to change a sealed object");
        }
        this.mSnapshot = null;
    }

    public final void seal() {
        this.mSealed = true;
    }

    public final T snapshot() {
        T t = this.mSnapshot;
        if (t == null) {
            t = createSnapshot();
            this.mSnapshot = t;
            Statistics statistics = this.mStatistics;
            if (statistics != null) {
                statistics.mRebuilt.incrementAndGet();
            }
        } else {
            Statistics statistics2 = this.mStatistics;
            if (statistics2 != null) {
                statistics2.mReused.incrementAndGet();
            }
        }
        return t;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Sealed<T> extends SnapshotCache<T> {
        @Override // com.android.server.utils.SnapshotCache
        public T createSnapshot() {
            throw new UnsupportedOperationException("cannot snapshot a sealed snaphot");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Auto<T extends Snappable<T>> extends SnapshotCache<T> {
        public Auto(T t, Watchable watchable, String str) {
            super(t, watchable, str);
        }

        public Auto(T t, Watchable watchable) {
            this(t, watchable, null);
        }

        @Override // com.android.server.utils.SnapshotCache
        public T createSnapshot() {
            return (T) ((Snappable) this.mSource).snapshot();
        }
    }
}
