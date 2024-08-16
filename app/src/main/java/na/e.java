package na;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import za.k;

/* compiled from: MapBuilder.kt */
/* loaded from: classes2.dex */
public final class e<K, V> extends a<Map.Entry<K, V>, K, V> {

    /* renamed from: e, reason: collision with root package name */
    private final d<K, V> f15945e;

    public e(d<K, V> dVar) {
        k.e(dVar, "backing");
        this.f15945e = dVar;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
        k.e(collection, "elements");
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.f15945e.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean containsAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        return this.f15945e.o(collection);
    }

    @Override // kotlin.collections.AbstractMutableSet
    public int d() {
        return this.f15945e.size();
    }

    @Override // na.a
    public boolean f(Map.Entry<? extends K, ? extends V> entry) {
        k.e(entry, "element");
        return this.f15945e.p(entry);
    }

    @Override // na.a
    public boolean g(Map.Entry entry) {
        k.e(entry, "element");
        return this.f15945e.J(entry);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public boolean add(Map.Entry<K, V> entry) {
        k.e(entry, "element");
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.f15945e.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.f15945e.t();
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean removeAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        this.f15945e.m();
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean retainAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        this.f15945e.m();
        return super.retainAll(collection);
    }
}
