package com.android.server.timezonedetector;

import android.util.ArraySet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class OrdinalGenerator<T> {
    private final Function<T, T> mCanonicalizationFunction;
    private final ArraySet<T> mKnownIds = new ArraySet<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public OrdinalGenerator(Function<T, T> function) {
        Objects.requireNonNull(function);
        this.mCanonicalizationFunction = function;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int ordinal(T t) {
        T apply = this.mCanonicalizationFunction.apply(t);
        int indexOf = this.mKnownIds.indexOf(apply);
        if (indexOf >= 0) {
            return indexOf;
        }
        int size = this.mKnownIds.size();
        this.mKnownIds.add(apply);
        return size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] ordinals(List<T> list) {
        int size = list.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = ordinal(list.get(i));
        }
        return iArr;
    }
}
