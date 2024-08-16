package kotlin.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CollectionsJVM.kt */
/* renamed from: kotlin.collections.q, reason: use source file name */
/* loaded from: classes2.dex */
public class CollectionsJVM {
    public static <E> List<E> a(List<E> list) {
        za.k.e(list, "builder");
        return ((na.b) list).l();
    }

    public static final <T> Object[] b(T[] tArr, boolean z10) {
        za.k.e(tArr, "<this>");
        if (z10 && za.k.a(tArr.getClass(), Object[].class)) {
            return tArr;
        }
        Object[] copyOf = Arrays.copyOf(tArr, tArr.length, Object[].class);
        za.k.d(copyOf, "copyOf(this, this.size, Array<Any?>::class.java)");
        return copyOf;
    }

    public static <E> List<E> c() {
        return new na.b();
    }

    public static <E> List<E> d(int i10) {
        return new na.b(i10);
    }

    public static <T> List<T> e(T t7) {
        List<T> singletonList = Collections.singletonList(t7);
        za.k.d(singletonList, "singletonList(element)");
        return singletonList;
    }
}
