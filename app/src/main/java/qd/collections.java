package qd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import kotlin.collections.r;
import za.k;

/* compiled from: collections.kt */
/* renamed from: qd.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class collections {
    public static final <T> void a(Collection<T> collection, T t7) {
        k.e(collection, "<this>");
        if (t7 != null) {
            collection.add(t7);
        }
    }

    private static final int b(int i10) {
        if (i10 < 3) {
            return 3;
        }
        return i10 + (i10 / 3) + 1;
    }

    public static final <T> List<T> c(ArrayList<T> arrayList) {
        List<T> j10;
        Object T;
        List<T> e10;
        k.e(arrayList, "<this>");
        int size = arrayList.size();
        if (size == 0) {
            j10 = r.j();
            return j10;
        }
        if (size != 1) {
            arrayList.trimToSize();
            return arrayList;
        }
        T = _Collections.T(arrayList);
        e10 = CollectionsJVM.e(T);
        return e10;
    }

    public static final <K> Map<K, Integer> d(Iterable<? extends K> iterable) {
        k.e(iterable, "<this>");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator<? extends K> it = iterable.iterator();
        int i10 = 0;
        while (it.hasNext()) {
            linkedHashMap.put(it.next(), Integer.valueOf(i10));
            i10++;
        }
        return linkedHashMap;
    }

    public static final <K, V> HashMap<K, V> e(int i10) {
        return new HashMap<>(b(i10));
    }

    public static final <E> HashSet<E> f(int i10) {
        return new HashSet<>(b(i10));
    }

    public static final <E> LinkedHashSet<E> g(int i10) {
        return new LinkedHashSet<>(b(i10));
    }
}
