package kotlin.collections;

import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ArraysJVM.kt */
/* renamed from: kotlin.collections.k, reason: use source file name */
/* loaded from: classes2.dex */
public class ArraysJVM {
    public static <T> int a(T[] tArr) {
        return Arrays.deepHashCode(tArr);
    }

    public static final void b(int i10, int i11) {
        if (i10 <= i11) {
            return;
        }
        throw new IndexOutOfBoundsException("toIndex (" + i10 + ") is greater than size (" + i11 + ").");
    }
}
