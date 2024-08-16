package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MutableCollections.kt */
/* renamed from: kotlin.collections.w, reason: use source file name */
/* loaded from: classes2.dex */
public class MutableCollections extends MutableCollectionsJVM {
    public static <T> boolean A(Collection<? super T> collection, T[] tArr) {
        List e10;
        za.k.e(collection, "<this>");
        za.k.e(tArr, "elements");
        e10 = _ArraysJvm.e(tArr);
        return collection.addAll(e10);
    }

    public static final <T> Collection<T> B(Iterable<? extends T> iterable) {
        List z02;
        za.k.e(iterable, "<this>");
        if (iterable instanceof Collection) {
            return (Collection) iterable;
        }
        z02 = _Collections.z0(iterable);
        return z02;
    }

    public static final <T> boolean C(Collection<? super T> collection, Iterable<? extends T> iterable) {
        za.k.e(collection, "<this>");
        za.k.e(iterable, "elements");
        return collection.retainAll(B(iterable));
    }

    public static <T> boolean z(Collection<? super T> collection, Iterable<? extends T> iterable) {
        za.k.e(collection, "<this>");
        za.k.e(iterable, "elements");
        if (iterable instanceof Collection) {
            return collection.addAll((Collection) iterable);
        }
        boolean z10 = false;
        Iterator<? extends T> it = iterable.iterator();
        while (it.hasNext()) {
            if (collection.add(it.next())) {
                z10 = true;
            }
        }
        return z10;
    }
}
