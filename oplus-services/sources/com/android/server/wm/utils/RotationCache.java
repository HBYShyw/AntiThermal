package com.android.server.wm.utils;

import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RotationCache<T, R> {
    private final SparseArray<R> mCache = new SparseArray<>(4);
    private T mCachedFor;
    private final RotationDependentComputation<T, R> mComputation;

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface RotationDependentComputation<T, R> {
        R compute(T t, int i);
    }

    public RotationCache(RotationDependentComputation<T, R> rotationDependentComputation) {
        this.mComputation = rotationDependentComputation;
    }

    public R getOrCompute(T t, int i) {
        if (t != this.mCachedFor) {
            this.mCache.clear();
            this.mCachedFor = t;
        }
        int indexOfKey = this.mCache.indexOfKey(i);
        if (indexOfKey >= 0) {
            return this.mCache.valueAt(indexOfKey);
        }
        R compute = this.mComputation.compute(t, i);
        this.mCache.put(i, compute);
        return compute;
    }
}
