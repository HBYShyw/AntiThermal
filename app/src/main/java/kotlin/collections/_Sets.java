package kotlin.collections;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _Sets.kt */
/* renamed from: kotlin.collections.t0, reason: use source file name */
/* loaded from: classes2.dex */
public class _Sets extends s0 {
    public static <T> Set<T> i(Set<? extends T> set, Iterable<? extends T> iterable) {
        za.k.e(set, "<this>");
        za.k.e(iterable, "elements");
        Collection<?> B = MutableCollections.B(iterable);
        if (B.isEmpty()) {
            return p.D0(set);
        }
        if (B instanceof Set) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            for (T t7 : set) {
                if (!B.contains(t7)) {
                    linkedHashSet.add(t7);
                }
            }
            return linkedHashSet;
        }
        LinkedHashSet linkedHashSet2 = new LinkedHashSet(set);
        linkedHashSet2.removeAll(B);
        return linkedHashSet2;
    }

    public static <T> Set<T> j(Set<? extends T> set, T t7) {
        za.k.e(set, "<this>");
        LinkedHashSet linkedHashSet = new LinkedHashSet(j0.e(set.size()));
        boolean z10 = false;
        for (T t10 : set) {
            boolean z11 = true;
            if (!z10 && za.k.a(t10, t7)) {
                z10 = true;
                z11 = false;
            }
            if (z11) {
                linkedHashSet.add(t10);
            }
        }
        return linkedHashSet;
    }

    public static <T> Set<T> k(Set<? extends T> set, Iterable<? extends T> iterable) {
        int size;
        za.k.e(set, "<this>");
        za.k.e(iterable, "elements");
        Integer v7 = s.v(iterable);
        if (v7 != null) {
            size = set.size() + v7.intValue();
        } else {
            size = set.size() * 2;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet(j0.e(size));
        linkedHashSet.addAll(set);
        p.z(linkedHashSet, iterable);
        return linkedHashSet;
    }

    public static <T> Set<T> l(Set<? extends T> set, T t7) {
        za.k.e(set, "<this>");
        LinkedHashSet linkedHashSet = new LinkedHashSet(j0.e(set.size() + 1));
        linkedHashSet.addAll(set);
        linkedHashSet.add(t7);
        return linkedHashSet;
    }
}
