package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Iterables.kt */
/* loaded from: classes2.dex */
public class s extends r {
    public static <T> int u(Iterable<? extends T> iterable, int i10) {
        za.k.e(iterable, "<this>");
        return iterable instanceof Collection ? ((Collection) iterable).size() : i10;
    }

    public static final <T> Integer v(Iterable<? extends T> iterable) {
        za.k.e(iterable, "<this>");
        if (iterable instanceof Collection) {
            return Integer.valueOf(((Collection) iterable).size());
        }
        return null;
    }

    public static <T> List<T> w(Iterable<? extends Iterable<? extends T>> iterable) {
        za.k.e(iterable, "<this>");
        ArrayList arrayList = new ArrayList();
        Iterator<? extends Iterable<? extends T>> it = iterable.iterator();
        while (it.hasNext()) {
            p.z(arrayList, it.next());
        }
        return arrayList;
    }
}
