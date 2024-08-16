package kotlin.collections;

import java.util.Map;
import java.util.NoSuchElementException;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MapWithDefault.kt */
/* loaded from: classes2.dex */
public class k0 {
    public static final <K, V> V a(Map<K, ? extends V> map, K k10) {
        za.k.e(map, "<this>");
        if (map instanceof i0) {
            return (V) ((i0) map).c(k10);
        }
        V v7 = map.get(k10);
        if (v7 != null || map.containsKey(k10)) {
            return v7;
        }
        throw new NoSuchElementException("Key " + k10 + " is missing in the map.");
    }
}
