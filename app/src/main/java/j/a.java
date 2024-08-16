package j;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/* compiled from: ArrayMap.java */
/* loaded from: classes.dex */
public class a<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {

    /* renamed from: l, reason: collision with root package name */
    MapCollections<K, V> f12873l;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ArrayMap.java */
    /* renamed from: j.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0054a extends MapCollections<K, V> {
        C0054a() {
        }

        @Override // j.MapCollections
        protected void a() {
            a.this.clear();
        }

        @Override // j.MapCollections
        protected Object b(int i10, int i11) {
            return a.this.f12922f[(i10 << 1) + i11];
        }

        @Override // j.MapCollections
        protected Map<K, V> c() {
            return a.this;
        }

        @Override // j.MapCollections
        protected int d() {
            return a.this.f12923g;
        }

        @Override // j.MapCollections
        protected int e(Object obj) {
            return a.this.g(obj);
        }

        @Override // j.MapCollections
        protected int f(Object obj) {
            return a.this.i(obj);
        }

        @Override // j.MapCollections
        protected void g(K k10, V v7) {
            a.this.put(k10, v7);
        }

        @Override // j.MapCollections
        protected void h(int i10) {
            a.this.l(i10);
        }

        @Override // j.MapCollections
        protected V i(int i10, V v7) {
            return a.this.m(i10, v7);
        }
    }

    public a() {
    }

    private MapCollections<K, V> o() {
        if (this.f12873l == null) {
            this.f12873l = new C0054a();
        }
        return this.f12873l;
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return o().l();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return o().m();
    }

    public boolean p(Collection<?> collection) {
        return MapCollections.p(this, collection);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        d(this.f12923g + map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return o().n();
    }

    public a(SimpleArrayMap simpleArrayMap) {
        super(simpleArrayMap);
    }
}
