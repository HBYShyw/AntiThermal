package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import za.DefaultConstructorMarker;

/* compiled from: AbstractList.kt */
/* renamed from: kotlin.collections.c, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {

    /* renamed from: e, reason: collision with root package name */
    public static final a f14311e = new a(null);

    /* compiled from: AbstractList.kt */
    /* renamed from: kotlin.collections.c$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void a(int i10, int i11, int i12) {
            if (i10 < 0 || i11 > i12) {
                throw new IndexOutOfBoundsException("startIndex: " + i10 + ", endIndex: " + i11 + ", size: " + i12);
            }
            if (i10 <= i11) {
                return;
            }
            throw new IllegalArgumentException("startIndex: " + i10 + " > endIndex: " + i11);
        }

        public final void b(int i10, int i11) {
            if (i10 < 0 || i10 >= i11) {
                throw new IndexOutOfBoundsException("index: " + i10 + ", size: " + i11);
            }
        }

        public final void c(int i10, int i11) {
            if (i10 < 0 || i10 > i11) {
                throw new IndexOutOfBoundsException("index: " + i10 + ", size: " + i11);
            }
        }

        public final void d(int i10, int i11, int i12) {
            if (i10 < 0 || i11 > i12) {
                throw new IndexOutOfBoundsException("fromIndex: " + i10 + ", toIndex: " + i11 + ", size: " + i12);
            }
            if (i10 <= i11) {
                return;
            }
            throw new IllegalArgumentException("fromIndex: " + i10 + " > toIndex: " + i11);
        }

        public final boolean e(Collection<?> collection, Collection<?> collection2) {
            za.k.e(collection, "c");
            za.k.e(collection2, "other");
            if (collection.size() != collection2.size()) {
                return false;
            }
            Iterator<?> it = collection2.iterator();
            Iterator<?> it2 = collection.iterator();
            while (it2.hasNext()) {
                if (!za.k.a(it2.next(), it.next())) {
                    return false;
                }
            }
            return true;
        }

        public final int f(Collection<?> collection) {
            za.k.e(collection, "c");
            Iterator<?> it = collection.iterator();
            int i10 = 1;
            while (it.hasNext()) {
                Object next = it.next();
                i10 = (i10 * 31) + (next != null ? next.hashCode() : 0);
            }
            return i10;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractList.kt */
    /* renamed from: kotlin.collections.c$b */
    /* loaded from: classes2.dex */
    public class b implements Iterator<E>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private int f14312e;

        public b() {
        }

        protected final int b() {
            return this.f14312e;
        }

        protected final void d(int i10) {
            this.f14312e = i10;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f14312e < AbstractList.this.size();
        }

        @Override // java.util.Iterator
        public E next() {
            if (hasNext()) {
                AbstractList<E> abstractList = AbstractList.this;
                int i10 = this.f14312e;
                this.f14312e = i10 + 1;
                return abstractList.get(i10);
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    /* compiled from: AbstractList.kt */
    /* renamed from: kotlin.collections.c$c */
    /* loaded from: classes2.dex */
    private class c extends AbstractList<E>.b implements ListIterator<E> {
        public c(int i10) {
            super();
            AbstractList.f14311e.c(i10, AbstractList.this.size());
            d(i10);
        }

        @Override // java.util.ListIterator
        public void add(E e10) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return b() > 0;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return b();
        }

        @Override // java.util.ListIterator
        public E previous() {
            if (hasPrevious()) {
                AbstractList<E> abstractList = AbstractList.this;
                d(b() - 1);
                return abstractList.get(b());
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return b() - 1;
        }

        @Override // java.util.ListIterator
        public void set(E e10) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    /* compiled from: AbstractList.kt */
    /* renamed from: kotlin.collections.c$d */
    /* loaded from: classes2.dex */
    private static final class d<E> extends AbstractList<E> implements RandomAccess {

        /* renamed from: f, reason: collision with root package name */
        private final AbstractList<E> f14315f;

        /* renamed from: g, reason: collision with root package name */
        private final int f14316g;

        /* renamed from: h, reason: collision with root package name */
        private int f14317h;

        /* JADX WARN: Multi-variable type inference failed */
        public d(AbstractList<? extends E> abstractList, int i10, int i11) {
            za.k.e(abstractList, "list");
            this.f14315f = abstractList;
            this.f14316g = i10;
            AbstractList.f14311e.d(i10, i11, abstractList.size());
            this.f14317h = i11 - i10;
        }

        @Override // kotlin.collections.AbstractCollection
        public int d() {
            return this.f14317h;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public E get(int i10) {
            AbstractList.f14311e.b(i10, this.f14317h);
            return this.f14315f.get(this.f14316g + i10);
        }
    }

    @Override // java.util.List
    public void add(int i10, E e10) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public boolean addAll(int i10, Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof List) {
            return f14311e.e(this, (Collection) obj);
        }
        return false;
    }

    @Override // java.util.List
    public abstract E get(int i10);

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        return f14311e.f(this);
    }

    @Override // java.util.List
    public int indexOf(E e10) {
        Iterator<E> it = iterator();
        int i10 = 0;
        while (it.hasNext()) {
            if (za.k.a(it.next(), e10)) {
                return i10;
            }
            i10++;
        }
        return -1;
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new b();
    }

    @Override // java.util.List
    public int lastIndexOf(E e10) {
        ListIterator<E> listIterator = listIterator(size());
        while (listIterator.hasPrevious()) {
            if (za.k.a(listIterator.previous(), e10)) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        return new c(0);
    }

    @Override // java.util.List
    public E remove(int i10) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public E set(int i10, E e10) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public List<E> subList(int i10, int i11) {
        return new d(this, i10, i11);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int i10) {
        return new c(i10);
    }
}
