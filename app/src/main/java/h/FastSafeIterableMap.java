package h;

import h.SafeIterableMap;
import java.util.HashMap;
import java.util.Map;

/* compiled from: FastSafeIterableMap.java */
/* renamed from: h.a, reason: use source file name */
/* loaded from: classes.dex */
public class FastSafeIterableMap<K, V> extends SafeIterableMap<K, V> {

    /* renamed from: i, reason: collision with root package name */
    private HashMap<K, SafeIterableMap.c<K, V>> f11948i = new HashMap<>();

    public boolean contains(K k10) {
        return this.f11948i.containsKey(k10);
    }

    @Override // h.SafeIterableMap
    protected SafeIterableMap.c<K, V> e(K k10) {
        return this.f11948i.get(k10);
    }

    @Override // h.SafeIterableMap
    public V i(K k10, V v7) {
        SafeIterableMap.c<K, V> e10 = e(k10);
        if (e10 != null) {
            return e10.f11954f;
        }
        this.f11948i.put(k10, h(k10, v7));
        return null;
    }

    @Override // h.SafeIterableMap
    public V k(K k10) {
        V v7 = (V) super.k(k10);
        this.f11948i.remove(k10);
        return v7;
    }

    public Map.Entry<K, V> l(K k10) {
        if (contains(k10)) {
            return this.f11948i.get(k10).f11956h;
        }
        return null;
    }
}
