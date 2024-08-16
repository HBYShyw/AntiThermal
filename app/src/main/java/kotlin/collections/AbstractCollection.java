package kotlin.collections;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.util.Collection;
import java.util.Iterator;
import za.CollectionToArray;
import za.Lambda;

/* compiled from: AbstractCollection.kt */
/* renamed from: kotlin.collections.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractCollection<E> implements Collection<E>, ab.a {

    /* compiled from: AbstractCollection.kt */
    /* renamed from: kotlin.collections.a$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<E, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ AbstractCollection<E> f14305e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        a(AbstractCollection<? extends E> abstractCollection) {
            super(1);
            this.f14305e = abstractCollection;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(E e10) {
            return e10 == this.f14305e ? "(this Collection)" : String.valueOf(e10);
        }
    }

    @Override // java.util.Collection
    public boolean add(E e10) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean contains(E e10) {
        if (isEmpty()) {
            return false;
        }
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (za.k.a(it.next(), e10)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
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

    public abstract int d();

    @Override // java.util.Collection
    public boolean isEmpty() {
        return size() == 0;
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
    public Object[] toArray() {
        return CollectionToArray.a(this);
    }

    public String toString() {
        String c02;
        c02 = _Collections.c0(this, ", ", "[", "]", 0, null, new a(this), 24, null);
        return c02;
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        za.k.e(tArr, ThermalWindowConfigInfo.TAG_ARRAY);
        return (T[]) CollectionToArray.b(this, tArr);
    }
}
