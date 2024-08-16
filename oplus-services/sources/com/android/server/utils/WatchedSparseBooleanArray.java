package com.android.server.utils;

import android.util.SparseBooleanArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WatchedSparseBooleanArray extends WatchableImpl implements Snappable {
    private final SparseBooleanArray mStorage;

    private void onChanged() {
        dispatchChange(this);
    }

    public WatchedSparseBooleanArray() {
        this.mStorage = new SparseBooleanArray();
    }

    public WatchedSparseBooleanArray(int i) {
        this.mStorage = new SparseBooleanArray(i);
    }

    public WatchedSparseBooleanArray(SparseBooleanArray sparseBooleanArray) {
        this.mStorage = sparseBooleanArray.clone();
    }

    public WatchedSparseBooleanArray(WatchedSparseBooleanArray watchedSparseBooleanArray) {
        this.mStorage = watchedSparseBooleanArray.mStorage.clone();
    }

    public void copyFrom(SparseBooleanArray sparseBooleanArray) {
        clear();
        int size = sparseBooleanArray.size();
        for (int i = 0; i < size; i++) {
            put(sparseBooleanArray.keyAt(i), sparseBooleanArray.valueAt(i));
        }
    }

    public void copyTo(SparseBooleanArray sparseBooleanArray) {
        sparseBooleanArray.clear();
        int size = size();
        for (int i = 0; i < size; i++) {
            sparseBooleanArray.put(keyAt(i), valueAt(i));
        }
    }

    public SparseBooleanArray untrackedStorage() {
        return this.mStorage;
    }

    public boolean get(int i) {
        return this.mStorage.get(i);
    }

    public boolean get(int i, boolean z) {
        return this.mStorage.get(i, z);
    }

    public void delete(int i) {
        this.mStorage.delete(i);
        onChanged();
    }

    public void removeAt(int i) {
        this.mStorage.removeAt(i);
        onChanged();
    }

    public void put(int i, boolean z) {
        this.mStorage.put(i, z);
        onChanged();
    }

    public int size() {
        return this.mStorage.size();
    }

    public int keyAt(int i) {
        return this.mStorage.keyAt(i);
    }

    public boolean valueAt(int i) {
        return this.mStorage.valueAt(i);
    }

    public void setValueAt(int i, boolean z) {
        if (this.mStorage.valueAt(i) != z) {
            this.mStorage.setValueAt(i, z);
            onChanged();
        }
    }

    public void setKeyAt(int i, int i2) {
        if (this.mStorage.keyAt(i) != i2) {
            this.mStorage.setKeyAt(i, i2);
            onChanged();
        }
    }

    public int indexOfKey(int i) {
        return this.mStorage.indexOfKey(i);
    }

    public int indexOfValue(boolean z) {
        return this.mStorage.indexOfValue(z);
    }

    public void clear() {
        this.mStorage.clear();
        onChanged();
    }

    public void append(int i, boolean z) {
        this.mStorage.append(i, z);
        onChanged();
    }

    public int hashCode() {
        return this.mStorage.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof WatchedSparseBooleanArray) {
            return this.mStorage.equals(((WatchedSparseBooleanArray) obj).mStorage);
        }
        return false;
    }

    public String toString() {
        return this.mStorage.toString();
    }

    @Override // com.android.server.utils.Snappable
    public WatchedSparseBooleanArray snapshot() {
        WatchedSparseBooleanArray watchedSparseBooleanArray = new WatchedSparseBooleanArray(this);
        watchedSparseBooleanArray.seal();
        return watchedSparseBooleanArray;
    }

    public void snapshot(WatchedSparseBooleanArray watchedSparseBooleanArray) {
        snapshot(this, watchedSparseBooleanArray);
    }

    public static void snapshot(WatchedSparseBooleanArray watchedSparseBooleanArray, WatchedSparseBooleanArray watchedSparseBooleanArray2) {
        if (watchedSparseBooleanArray.size() != 0) {
            throw new IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedSparseBooleanArray2.size();
        for (int i = 0; i < size; i++) {
            watchedSparseBooleanArray.mStorage.put(watchedSparseBooleanArray2.keyAt(i), watchedSparseBooleanArray2.valueAt(i));
        }
        watchedSparseBooleanArray.seal();
    }
}
