package na;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.collections.AbstractMutableSet;
import za.k;

/* compiled from: SetBuilder.kt */
/* renamed from: na.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class SetBuilder<E> extends AbstractMutableSet<E> implements Serializable {

    /* renamed from: e, reason: collision with root package name */
    private final d<E, ?> f15953e;

    public SetBuilder(d<E, ?> dVar) {
        k.e(dVar, "backing");
        this.f15953e = dVar;
    }

    private final Object writeReplace() {
        if (this.f15953e.D()) {
            return new h(this, 1);
        }
        throw new NotSerializableException("The set cannot be serialized while it is being built.");
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E e10) {
        return this.f15953e.i(e10) >= 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> collection) {
        k.e(collection, "elements");
        this.f15953e.m();
        return super.addAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.f15953e.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.f15953e.containsKey(obj);
    }

    @Override // kotlin.collections.AbstractMutableSet
    public int d() {
        return this.f15953e.size();
    }

    public final Set<E> e() {
        this.f15953e.l();
        return this;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.f15953e.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return this.f15953e.E();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return this.f15953e.L(obj) >= 0;
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean removeAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        this.f15953e.m();
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean retainAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        this.f15953e.m();
        return super.retainAll(collection);
    }

    public SetBuilder() {
        this(new d());
    }

    public SetBuilder(int i10) {
        this(new d(i10));
    }
}
