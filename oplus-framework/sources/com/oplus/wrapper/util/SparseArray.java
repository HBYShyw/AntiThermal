package com.oplus.wrapper.util;

/* loaded from: classes.dex */
public class SparseArray<E> {
    private final android.util.SparseArray mSparseArray;

    public SparseArray(android.util.SparseArray sparseArray) {
        this.mSparseArray = sparseArray;
    }

    public E removeReturnOld(int i) {
        return (E) this.mSparseArray.removeReturnOld(i);
    }
}
