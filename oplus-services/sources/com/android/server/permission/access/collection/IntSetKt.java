package com.android.server.permission.access.collection;

import org.jetbrains.annotations.NotNull;

/* compiled from: IntSet.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class IntSetKt {
    @NotNull
    public static final IntSet IntSet(@NotNull int[] iArr) {
        IntSet intSet = new IntSet();
        plusAssign(intSet, iArr);
        return intSet;
    }

    public static final void plusAssign(@NotNull IntSet intSet, @NotNull IntSet intSet2) {
        int size = intSet2.getSize();
        for (int i = 0; i < size; i++) {
            intSet.add(intSet2.elementAt(i));
        }
    }

    public static final void plusAssign(@NotNull IntSet intSet, @NotNull int[] iArr) {
        for (int i : iArr) {
            intSet.add(i);
        }
    }
}
