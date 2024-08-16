package kotlin.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MapsJVM.kt */
/* renamed from: kotlin.collections.l0, reason: use source file name */
/* loaded from: classes2.dex */
public class MapsJVM extends k0 {
    public static <K, V> Map<K, V> b(Map<K, V> map) {
        za.k.e(map, "builder");
        return ((na.d) map).l();
    }

    public static <K, V> Map<K, V> c() {
        return new na.d();
    }

    public static <K, V> Map<K, V> d(int i10) {
        return new na.d(i10);
    }

    public static int e(int i10) {
        if (i10 < 0) {
            return i10;
        }
        if (i10 < 3) {
            return i10 + 1;
        }
        if (i10 < 1073741824) {
            return (int) ((i10 / 0.75f) + 1.0f);
        }
        return Integer.MAX_VALUE;
    }

    public static <K, V> Map<K, V> f(ma.o<? extends K, ? extends V> oVar) {
        za.k.e(oVar, "pair");
        Map<K, V> singletonMap = Collections.singletonMap(oVar.c(), oVar.d());
        za.k.d(singletonMap, "singletonMap(pair.first, pair.second)");
        return singletonMap;
    }

    public static final <K, V> Map<K, V> g(Map<? extends K, ? extends V> map) {
        za.k.e(map, "<this>");
        Map.Entry<? extends K, ? extends V> next = map.entrySet().iterator().next();
        Map<K, V> singletonMap = Collections.singletonMap(next.getKey(), next.getValue());
        za.k.d(singletonMap, "with(entries.iterator().…ingletonMap(key, value) }");
        return singletonMap;
    }

    public static <K, V> SortedMap<K, V> h(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        za.k.e(map, "<this>");
        za.k.e(comparator, "comparator");
        TreeMap treeMap = new TreeMap(comparator);
        treeMap.putAll(map);
        return treeMap;
    }
}
