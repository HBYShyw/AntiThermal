package kotlin.collections;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.util.Collection;
import java.util.Iterator;
import za.CollectionToArray;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Collections.kt */
/* loaded from: classes2.dex */
public final class h<T> implements Collection<T>, ab.a {

    /* renamed from: e, reason: collision with root package name */
    private final T[] f14325e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f14326f;

    public h(T[] tArr, boolean z10) {
        za.k.e(tArr, "values");
        this.f14325e = tArr;
        this.f14326f = z10;
    }

    @Override // java.util.Collection
    public boolean add(T t7) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean contains(Object obj) {
        boolean v7;
        v7 = _Arrays.v(this.f14325e, obj);
        return v7;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<? extends Object> collection) {
        za.k.e(collection, "elements");
        if (collection.isEmpty()) {
            return true;
        }
        Iterator<T> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    public int d() {
        return this.f14325e.length;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return this.f14325e.length == 0;
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        return za.b.a(this.f14325e);
    }

    @Override // java.util.Collection
    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public final /* bridge */ int size() {
        return d();
    }

    @Override // java.util.Collection
    public final Object[] toArray() {
        return CollectionsJVM.b(this.f14325e, this.f14326f);
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        za.k.e(tArr, ThermalWindowConfigInfo.TAG_ARRAY);
        return (T[]) CollectionToArray.b(this, tArr);
    }
}
