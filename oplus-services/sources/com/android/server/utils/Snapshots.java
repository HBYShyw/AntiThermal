package com.android.server.utils;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseSetArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Snapshots {
    public static <T> T maybeSnapshot(T t) {
        return t instanceof Snappable ? (T) ((Snappable) t).snapshot() : t;
    }

    public static <E> void copy(SparseArray<E> sparseArray, SparseArray<E> sparseArray2) {
        if (sparseArray.size() != 0) {
            throw new IllegalArgumentException("copy destination is not empty");
        }
        int size = sparseArray2.size();
        for (int i = 0; i < size; i++) {
            sparseArray.put(sparseArray2.keyAt(i), sparseArray2.valueAt(i));
        }
    }

    public static <E> void copy(SparseSetArray<E> sparseSetArray, SparseSetArray<E> sparseSetArray2) {
        if (sparseSetArray.size() != 0) {
            throw new IllegalArgumentException("copy destination is not empty");
        }
        int size = sparseSetArray2.size();
        for (int i = 0; i < size; i++) {
            int sizeAt = sparseSetArray2.sizeAt(i);
            for (int i2 = 0; i2 < sizeAt; i2++) {
                sparseSetArray.add(sparseSetArray2.keyAt(i), sparseSetArray2.valueAt(i, i2));
            }
        }
    }

    public static void snapshot(SparseIntArray sparseIntArray, SparseIntArray sparseIntArray2) {
        if (sparseIntArray.size() != 0) {
            throw new IllegalArgumentException("snapshot destination is not empty");
        }
        int size = sparseIntArray2.size();
        for (int i = 0; i < size; i++) {
            sparseIntArray.put(sparseIntArray2.keyAt(i), sparseIntArray2.valueAt(i));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E extends Snappable<E>> void snapshot(SparseArray<E> sparseArray, SparseArray<E> sparseArray2) {
        if (sparseArray.size() != 0) {
            throw new IllegalArgumentException("snapshot destination is not empty");
        }
        int size = sparseArray2.size();
        for (int i = 0; i < size; i++) {
            sparseArray.put(sparseArray2.keyAt(i), (Snappable) sparseArray2.valueAt(i).snapshot());
        }
    }

    public static <E extends Snappable<E>> void snapshot(SparseSetArray<E> sparseSetArray, SparseSetArray<E> sparseSetArray2) {
        if (sparseSetArray.size() != 0) {
            throw new IllegalArgumentException("snapshot destination is not empty");
        }
        int size = sparseSetArray2.size();
        for (int i = 0; i < size; i++) {
            int sizeAt = sparseSetArray2.sizeAt(i);
            for (int i2 = 0; i2 < sizeAt; i2++) {
                sparseSetArray.add(sparseSetArray2.keyAt(i), (Snappable) ((Snappable) sparseSetArray2.valueAt(i, i2)).snapshot());
            }
        }
    }
}
