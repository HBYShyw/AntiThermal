package kotlin.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Maps.kt */
/* loaded from: classes2.dex */
public class m0 extends MapsJVM {
    public static <K, V> Map<K, V> i() {
        c0 c0Var = c0.f14318e;
        za.k.c(c0Var, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.emptyMap, V of kotlin.collections.MapsKt__MapsKt.emptyMap>");
        return c0Var;
    }

    public static <K, V> V j(Map<K, ? extends V> map, K k10) {
        za.k.e(map, "<this>");
        return (V) k0.a(map, k10);
    }

    public static <K, V> HashMap<K, V> k(ma.o<? extends K, ? extends V>... oVarArr) {
        za.k.e(oVarArr, "pairs");
        HashMap<K, V> hashMap = new HashMap<>(j0.e(oVarArr.length));
        p(hashMap, oVarArr);
        return hashMap;
    }

    public static <K, V> Map<K, V> l(ma.o<? extends K, ? extends V>... oVarArr) {
        za.k.e(oVarArr, "pairs");
        return oVarArr.length > 0 ? t(oVarArr, new LinkedHashMap(j0.e(oVarArr.length))) : j0.i();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V> Map<K, V> m(Map<K, ? extends V> map) {
        za.k.e(map, "<this>");
        int size = map.size();
        if (size != 0) {
            return size != 1 ? map : MapsJVM.g(map);
        }
        return j0.i();
    }

    public static <K, V> Map<K, V> n(Map<? extends K, ? extends V> map, Map<? extends K, ? extends V> map2) {
        za.k.e(map, "<this>");
        za.k.e(map2, "map");
        LinkedHashMap linkedHashMap = new LinkedHashMap(map);
        linkedHashMap.putAll(map2);
        return linkedHashMap;
    }

    public static <K, V> void o(Map<? super K, ? super V> map, Iterable<? extends ma.o<? extends K, ? extends V>> iterable) {
        za.k.e(map, "<this>");
        za.k.e(iterable, "pairs");
        for (ma.o<? extends K, ? extends V> oVar : iterable) {
            map.put(oVar.a(), oVar.b());
        }
    }

    public static final <K, V> void p(Map<? super K, ? super V> map, ma.o<? extends K, ? extends V>[] oVarArr) {
        za.k.e(map, "<this>");
        za.k.e(oVarArr, "pairs");
        for (ma.o<? extends K, ? extends V> oVar : oVarArr) {
            map.put(oVar.a(), oVar.b());
        }
    }

    public static <K, V> Map<K, V> q(Iterable<? extends ma.o<? extends K, ? extends V>> iterable) {
        za.k.e(iterable, "<this>");
        if (iterable instanceof Collection) {
            Collection collection = (Collection) iterable;
            int size = collection.size();
            if (size == 0) {
                return j0.i();
            }
            if (size != 1) {
                return r(iterable, new LinkedHashMap(j0.e(collection.size())));
            }
            return j0.f(iterable instanceof List ? (ma.o<? extends K, ? extends V>) ((List) iterable).get(0) : iterable.iterator().next());
        }
        return m(r(iterable, new LinkedHashMap()));
    }

    public static final <K, V, M extends Map<? super K, ? super V>> M r(Iterable<? extends ma.o<? extends K, ? extends V>> iterable, M m10) {
        za.k.e(iterable, "<this>");
        za.k.e(m10, "destination");
        j0.o(m10, iterable);
        return m10;
    }

    public static <K, V> Map<K, V> s(Map<? extends K, ? extends V> map) {
        za.k.e(map, "<this>");
        int size = map.size();
        if (size == 0) {
            return j0.i();
        }
        if (size != 1) {
            return j0.u(map);
        }
        return MapsJVM.g(map);
    }

    public static final <K, V, M extends Map<? super K, ? super V>> M t(ma.o<? extends K, ? extends V>[] oVarArr, M m10) {
        za.k.e(oVarArr, "<this>");
        za.k.e(m10, "destination");
        p(m10, oVarArr);
        return m10;
    }

    public static <K, V> Map<K, V> u(Map<? extends K, ? extends V> map) {
        za.k.e(map, "<this>");
        return new LinkedHashMap(map);
    }
}
