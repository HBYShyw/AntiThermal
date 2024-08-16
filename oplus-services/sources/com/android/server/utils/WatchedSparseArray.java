package com.android.server.utils;

import android.util.SparseArray;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WatchedSparseArray<E> extends WatchableImpl implements Snappable {
    private final Watcher mObserver;
    private final SparseArray<E> mStorage;
    private volatile boolean mWatching;

    private void onChanged() {
        dispatchChange(this);
    }

    private void registerChild(Object obj) {
        if (this.mWatching && (obj instanceof Watchable)) {
            ((Watchable) obj).registerObserver(this.mObserver);
        }
    }

    private void unregisterChild(Object obj) {
        if (this.mWatching && (obj instanceof Watchable)) {
            ((Watchable) obj).unregisterObserver(this.mObserver);
        }
    }

    private void unregisterChildIf(Object obj) {
        if (this.mWatching && (obj instanceof Watchable) && this.mStorage.indexOfValue(obj) == -1) {
            ((Watchable) obj).unregisterObserver(this.mObserver);
        }
    }

    @Override // com.android.server.utils.WatchableImpl, com.android.server.utils.Watchable
    public void registerObserver(Watcher watcher) {
        super.registerObserver(watcher);
        if (registeredObserverCount() == 1) {
            this.mWatching = true;
            int size = this.mStorage.size();
            for (int i = 0; i < size; i++) {
                registerChild(this.mStorage.valueAt(i));
            }
        }
    }

    @Override // com.android.server.utils.WatchableImpl, com.android.server.utils.Watchable
    public void unregisterObserver(Watcher watcher) {
        super.unregisterObserver(watcher);
        if (registeredObserverCount() == 0) {
            int size = this.mStorage.size();
            for (int i = 0; i < size; i++) {
                unregisterChild(this.mStorage.valueAt(i));
            }
            this.mWatching = false;
        }
    }

    public WatchedSparseArray() {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedSparseArray.this.dispatchChange(watchable);
            }
        };
        this.mStorage = new SparseArray<>();
    }

    public WatchedSparseArray(int i) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedSparseArray.this.dispatchChange(watchable);
            }
        };
        this.mStorage = new SparseArray<>(i);
    }

    public WatchedSparseArray(SparseArray<E> sparseArray) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedSparseArray.this.dispatchChange(watchable);
            }
        };
        this.mStorage = sparseArray.clone();
    }

    public WatchedSparseArray(WatchedSparseArray<E> watchedSparseArray) {
        this.mWatching = false;
        this.mObserver = new Watcher() { // from class: com.android.server.utils.WatchedSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                WatchedSparseArray.this.dispatchChange(watchable);
            }
        };
        this.mStorage = watchedSparseArray.mStorage.clone();
    }

    public void copyFrom(SparseArray<E> sparseArray) {
        clear();
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            put(sparseArray.keyAt(i), sparseArray.valueAt(i));
        }
    }

    public void copyTo(SparseArray<E> sparseArray) {
        sparseArray.clear();
        int size = size();
        for (int i = 0; i < size; i++) {
            sparseArray.put(keyAt(i), valueAt(i));
        }
    }

    public SparseArray<E> untrackedStorage() {
        return this.mStorage;
    }

    public boolean contains(int i) {
        return this.mStorage.contains(i);
    }

    public E get(int i) {
        return this.mStorage.get(i);
    }

    public E get(int i, E e) {
        return this.mStorage.get(i, e);
    }

    public void delete(int i) {
        E e = this.mStorage.get(i);
        this.mStorage.delete(i);
        unregisterChildIf(e);
        onChanged();
    }

    public E removeReturnOld(int i) {
        E e = (E) this.mStorage.removeReturnOld(i);
        unregisterChildIf(e);
        return e;
    }

    public void remove(int i) {
        delete(i);
    }

    public void removeAt(int i) {
        E valueAt = this.mStorage.valueAt(i);
        this.mStorage.removeAt(i);
        unregisterChildIf(valueAt);
        onChanged();
    }

    public void removeAtRange(int i, int i2) {
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            try {
                arrayList.add(this.mStorage.valueAt(i4 + i));
            } catch (Exception unused) {
            }
        }
        try {
            this.mStorage.removeAtRange(i, i2);
            onChanged();
        } finally {
            while (i3 < i2) {
                unregisterChildIf(arrayList.get(i3));
                i3++;
            }
        }
    }

    public void put(int i, E e) {
        E e2 = this.mStorage.get(i);
        this.mStorage.put(i, e);
        unregisterChildIf(e2);
        registerChild(e);
        onChanged();
    }

    public int size() {
        return this.mStorage.size();
    }

    public int keyAt(int i) {
        return this.mStorage.keyAt(i);
    }

    public E valueAt(int i) {
        return this.mStorage.valueAt(i);
    }

    public void setValueAt(int i, E e) {
        E valueAt = this.mStorage.valueAt(i);
        this.mStorage.setValueAt(i, e);
        if (e != valueAt) {
            unregisterChildIf(valueAt);
            registerChild(e);
            onChanged();
        }
    }

    public int indexOfKey(int i) {
        return this.mStorage.indexOfKey(i);
    }

    public int indexOfValue(E e) {
        return this.mStorage.indexOfValue(e);
    }

    public int indexOfValueByValue(E e) {
        return this.mStorage.indexOfValueByValue(e);
    }

    public void clear() {
        if (this.mWatching) {
            int size = this.mStorage.size();
            for (int i = 0; i < size; i++) {
                unregisterChild(this.mStorage.valueAt(i));
            }
        }
        this.mStorage.clear();
        onChanged();
    }

    public void append(int i, E e) {
        this.mStorage.append(i, e);
        registerChild(e);
        onChanged();
    }

    public int hashCode() {
        return this.mStorage.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof WatchedSparseArray) {
            return this.mStorage.equals(((WatchedSparseArray) obj).mStorage);
        }
        return false;
    }

    public String toString() {
        return this.mStorage.toString();
    }

    @Override // com.android.server.utils.Snappable
    public WatchedSparseArray<E> snapshot() {
        WatchedSparseArray<E> watchedSparseArray = new WatchedSparseArray<>(size());
        snapshot(watchedSparseArray, this);
        return watchedSparseArray;
    }

    public void snapshot(WatchedSparseArray<E> watchedSparseArray) {
        snapshot(this, watchedSparseArray);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> void snapshot(WatchedSparseArray<E> watchedSparseArray, WatchedSparseArray<E> watchedSparseArray2) {
        if (watchedSparseArray.size() != 0) {
            throw new IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedSparseArray2.size();
        for (int i = 0; i < size; i++) {
            Object maybeSnapshot = Snapshots.maybeSnapshot(watchedSparseArray2.valueAt(i));
            ((WatchedSparseArray) watchedSparseArray).mStorage.put(watchedSparseArray2.keyAt(i), maybeSnapshot);
        }
        watchedSparseArray.seal();
    }
}
