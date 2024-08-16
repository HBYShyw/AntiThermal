package com.android.server.utils;

import android.util.ArraySet;
import android.util.SparseSetArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WatchedSparseSetArray<T> extends WatchableImpl implements Snappable {
    private final SparseSetArray mStorage;

    private void onChanged() {
        dispatchChange(this);
    }

    public WatchedSparseSetArray() {
        this.mStorage = new SparseSetArray();
    }

    public WatchedSparseSetArray(WatchedSparseSetArray<T> watchedSparseSetArray) {
        this.mStorage = new SparseSetArray(watchedSparseSetArray.untrackedStorage());
    }

    public WatchedSparseSetArray(SparseSetArray<T> sparseSetArray) {
        this.mStorage = sparseSetArray;
    }

    public SparseSetArray<T> untrackedStorage() {
        return this.mStorage;
    }

    public boolean add(int i, T t) {
        boolean add = this.mStorage.add(i, t);
        onChanged();
        return add;
    }

    public void addAll(int i, ArraySet<T> arraySet) {
        this.mStorage.addAll(i, arraySet);
        onChanged();
    }

    public void clear() {
        this.mStorage.clear();
        onChanged();
    }

    public boolean contains(int i, T t) {
        return this.mStorage.contains(i, t);
    }

    public ArraySet<T> get(int i) {
        return this.mStorage.get(i);
    }

    public boolean remove(int i, T t) {
        if (!this.mStorage.remove(i, t)) {
            return false;
        }
        onChanged();
        return true;
    }

    public void remove(int i) {
        this.mStorage.remove(i);
        onChanged();
    }

    public int size() {
        return this.mStorage.size();
    }

    public int keyAt(int i) {
        return this.mStorage.keyAt(i);
    }

    public int sizeAt(int i) {
        return this.mStorage.sizeAt(i);
    }

    public T valueAt(int i, int i2) {
        return (T) this.mStorage.valueAt(i, i2);
    }

    public void copyFrom(SparseSetArray<T> sparseSetArray) {
        clear();
        int size = sparseSetArray.size();
        for (int i = 0; i < size; i++) {
            int keyAt = sparseSetArray.keyAt(i);
            this.mStorage.addAll(keyAt, sparseSetArray.get(keyAt));
        }
        onChanged();
    }

    @Override // com.android.server.utils.Snappable
    public Object snapshot() {
        WatchedSparseSetArray watchedSparseSetArray = new WatchedSparseSetArray(this);
        watchedSparseSetArray.seal();
        return watchedSparseSetArray;
    }

    public void snapshot(WatchedSparseSetArray<T> watchedSparseSetArray) {
        snapshot(this, watchedSparseSetArray);
    }

    public static void snapshot(WatchedSparseSetArray watchedSparseSetArray, WatchedSparseSetArray watchedSparseSetArray2) {
        if (watchedSparseSetArray.size() != 0) {
            throw new IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedSparseSetArray2.size();
        for (int i = 0; i < size; i++) {
            ArraySet<T> arraySet = watchedSparseSetArray2.get(i);
            int size2 = arraySet.size();
            for (int i2 = 0; i2 < size2; i2++) {
                watchedSparseSetArray.mStorage.add(watchedSparseSetArray2.keyAt(i), arraySet.valueAt(i2));
            }
        }
        watchedSparseSetArray.seal();
    }
}
