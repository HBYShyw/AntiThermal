package com.android.server.utils;

import android.util.SparseIntArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WatchedSparseIntArray extends WatchableImpl implements Snappable {
    private final SparseIntArray mStorage;

    private void onChanged() {
        dispatchChange(this);
    }

    public WatchedSparseIntArray() {
        this.mStorage = new SparseIntArray();
    }

    public WatchedSparseIntArray(int i) {
        this.mStorage = new SparseIntArray(i);
    }

    public WatchedSparseIntArray(SparseIntArray sparseIntArray) {
        this.mStorage = sparseIntArray.clone();
    }

    public WatchedSparseIntArray(WatchedSparseIntArray watchedSparseIntArray) {
        this.mStorage = watchedSparseIntArray.mStorage.clone();
    }

    public void copyFrom(SparseIntArray sparseIntArray) {
        clear();
        int size = sparseIntArray.size();
        for (int i = 0; i < size; i++) {
            put(sparseIntArray.keyAt(i), sparseIntArray.valueAt(i));
        }
    }

    public void copyTo(SparseIntArray sparseIntArray) {
        sparseIntArray.clear();
        int size = size();
        for (int i = 0; i < size; i++) {
            sparseIntArray.put(keyAt(i), valueAt(i));
        }
    }

    public SparseIntArray untrackedStorage() {
        return this.mStorage;
    }

    public int get(int i) {
        return this.mStorage.get(i);
    }

    public int get(int i, int i2) {
        return this.mStorage.get(i, i2);
    }

    public void delete(int i) {
        int indexOfKey = this.mStorage.indexOfKey(i);
        if (indexOfKey >= 0) {
            this.mStorage.removeAt(indexOfKey);
            onChanged();
        }
    }

    public void removeAt(int i) {
        this.mStorage.removeAt(i);
        onChanged();
    }

    public void put(int i, int i2) {
        this.mStorage.put(i, i2);
        onChanged();
    }

    public int size() {
        return this.mStorage.size();
    }

    public int keyAt(int i) {
        return this.mStorage.keyAt(i);
    }

    public int valueAt(int i) {
        return this.mStorage.valueAt(i);
    }

    public void setValueAt(int i, int i2) {
        if (this.mStorage.valueAt(i) != i2) {
            this.mStorage.setValueAt(i, i2);
            onChanged();
        }
    }

    public int indexOfKey(int i) {
        return this.mStorage.indexOfKey(i);
    }

    public int indexOfValue(int i) {
        return this.mStorage.indexOfValue(i);
    }

    public void clear() {
        int size = size();
        this.mStorage.clear();
        if (size > 0) {
            onChanged();
        }
    }

    public void append(int i, int i2) {
        this.mStorage.append(i, i2);
        onChanged();
    }

    public int[] copyKeys() {
        return this.mStorage.copyKeys();
    }

    public int hashCode() {
        return this.mStorage.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof WatchedSparseIntArray) {
            return this.mStorage.equals(((WatchedSparseIntArray) obj).mStorage);
        }
        return false;
    }

    public String toString() {
        return this.mStorage.toString();
    }

    @Override // com.android.server.utils.Snappable
    public WatchedSparseIntArray snapshot() {
        WatchedSparseIntArray watchedSparseIntArray = new WatchedSparseIntArray(this);
        watchedSparseIntArray.seal();
        return watchedSparseIntArray;
    }

    public void snapshot(WatchedSparseIntArray watchedSparseIntArray) {
        snapshot(this, watchedSparseIntArray);
    }

    public static void snapshot(WatchedSparseIntArray watchedSparseIntArray, WatchedSparseIntArray watchedSparseIntArray2) {
        if (watchedSparseIntArray.size() != 0) {
            throw new IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedSparseIntArray2.size();
        for (int i = 0; i < size; i++) {
            watchedSparseIntArray.mStorage.put(watchedSparseIntArray2.keyAt(i), watchedSparseIntArray2.valueAt(i));
        }
        watchedSparseIntArray.seal();
    }
}
