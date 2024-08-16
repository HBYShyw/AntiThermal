package na;

import java.util.Collection;
import java.util.Iterator;
import kotlin.collections.AbstractMutableSet;
import za.k;

/* compiled from: MapBuilder.kt */
/* loaded from: classes2.dex */
public final class f<E> extends AbstractMutableSet<E> {

    /* renamed from: e, reason: collision with root package name */
    private final d<E, ?> f15946e;

    public f(d<E, ?> dVar) {
        k.e(dVar, "backing");
        this.f15946e = dVar;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E e10) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> collection) {
        k.e(collection, "elements");
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.f15946e.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.f15946e.containsKey(obj);
    }

    @Override // kotlin.collections.AbstractMutableSet
    public int d() {
        return this.f15946e.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.f15946e.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return this.f15946e.E();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return this.f15946e.L(obj) >= 0;
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean removeAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        this.f15946e.m();
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean retainAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        this.f15946e.m();
        return super.retainAll(collection);
    }
}
