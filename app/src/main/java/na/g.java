package na;

import java.util.Collection;
import java.util.Iterator;
import kotlin.collections.AbstractMutableCollection;
import za.k;

/* compiled from: MapBuilder.kt */
/* loaded from: classes2.dex */
public final class g<V> extends AbstractMutableCollection<V> {

    /* renamed from: e, reason: collision with root package name */
    private final d<?, V> f15947e;

    public g(d<?, V> dVar) {
        k.e(dVar, "backing");
        this.f15947e = dVar;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean add(V v7) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends V> collection) {
        k.e(collection, "elements");
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.f15947e.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return this.f15947e.containsValue(obj);
    }

    @Override // kotlin.collections.AbstractMutableCollection
    public int d() {
        return this.f15947e.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.f15947e.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<V> iterator() {
        return this.f15947e.P();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean remove(Object obj) {
        return this.f15947e.N(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        this.f15947e.m();
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        this.f15947e.m();
        return super.retainAll(collection);
    }
}
