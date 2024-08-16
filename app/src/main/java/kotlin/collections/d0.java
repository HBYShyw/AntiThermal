package kotlin.collections;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import za.CollectionToArray;

/* compiled from: Sets.kt */
/* loaded from: classes2.dex */
public final class d0 implements Set, Serializable, ab.a {

    /* renamed from: e, reason: collision with root package name */
    public static final d0 f14319e = new d0();
    private static final long serialVersionUID = 3406603774387020532L;

    private d0() {
    }

    private final Object readResolve() {
        return f14319e;
    }

    @Override // java.util.Set, java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Set, java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof Void) {
            return d((Void) obj);
        }
        return false;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection collection) {
        za.k.e(collection, "elements");
        return collection.isEmpty();
    }

    public boolean d(Void r12) {
        za.k.e(r12, "element");
        return false;
    }

    public int e() {
        return 0;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean equals(Object obj) {
        return (obj instanceof Set) && ((Set) obj).isEmpty();
    }

    @Override // java.util.Set, java.util.Collection
    public int hashCode() {
        return 0;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        return true;
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return a0.f14306e;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Set, java.util.Collection
    public final /* bridge */ int size() {
        return e();
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        return CollectionToArray.a(this);
    }

    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        za.k.e(tArr, ThermalWindowConfigInfo.TAG_ARRAY);
        return (T[]) CollectionToArray.b(this, tArr);
    }

    public String toString() {
        return "[]";
    }
}
