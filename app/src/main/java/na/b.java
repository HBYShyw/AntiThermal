package na;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import kotlin.collections.AbstractList;
import kotlin.collections.AbstractMutableList;
import kotlin.collections.ArrayDeque;
import kotlin.collections._ArraysJvm;
import za.k;

/* compiled from: ListBuilder.kt */
/* loaded from: classes2.dex */
public final class b<E> extends AbstractMutableList<E> implements RandomAccess, Serializable {

    /* renamed from: e, reason: collision with root package name */
    private E[] f15918e;

    /* renamed from: f, reason: collision with root package name */
    private int f15919f;

    /* renamed from: g, reason: collision with root package name */
    private int f15920g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f15921h;

    /* renamed from: i, reason: collision with root package name */
    private final b<E> f15922i;

    /* renamed from: j, reason: collision with root package name */
    private final b<E> f15923j;

    /* compiled from: ListBuilder.kt */
    /* loaded from: classes2.dex */
    private static final class a<E> implements ListIterator<E>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final b<E> f15924e;

        /* renamed from: f, reason: collision with root package name */
        private int f15925f;

        /* renamed from: g, reason: collision with root package name */
        private int f15926g;

        public a(b<E> bVar, int i10) {
            k.e(bVar, "list");
            this.f15924e = bVar;
            this.f15925f = i10;
            this.f15926g = -1;
        }

        @Override // java.util.ListIterator
        public void add(E e10) {
            b<E> bVar = this.f15924e;
            int i10 = this.f15925f;
            this.f15925f = i10 + 1;
            bVar.add(i10, e10);
            this.f15926g = -1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.f15925f < ((b) this.f15924e).f15920g;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.f15925f > 0;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            if (this.f15925f < ((b) this.f15924e).f15920g) {
                int i10 = this.f15925f;
                this.f15925f = i10 + 1;
                this.f15926g = i10;
                return (E) ((b) this.f15924e).f15918e[((b) this.f15924e).f15919f + this.f15926g];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.f15925f;
        }

        @Override // java.util.ListIterator
        public E previous() {
            int i10 = this.f15925f;
            if (i10 > 0) {
                int i11 = i10 - 1;
                this.f15925f = i11;
                this.f15926g = i11;
                return (E) ((b) this.f15924e).f15918e[((b) this.f15924e).f15919f + this.f15926g];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.f15925f - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            int i10 = this.f15926g;
            if (i10 != -1) {
                this.f15924e.remove(i10);
                this.f15925f = this.f15926g;
                this.f15926g = -1;
                return;
            }
            throw new IllegalStateException("Call next() or previous() before removing element from the iterator.".toString());
        }

        @Override // java.util.ListIterator
        public void set(E e10) {
            int i10 = this.f15926g;
            if (i10 != -1) {
                this.f15924e.set(i10, e10);
                return;
            }
            throw new IllegalStateException("Call next() or previous() before replacing element from the iterator.".toString());
        }
    }

    private b(E[] eArr, int i10, int i11, boolean z10, b<E> bVar, b<E> bVar2) {
        this.f15918e = eArr;
        this.f15919f = i10;
        this.f15920g = i11;
        this.f15921h = z10;
        this.f15922i = bVar;
        this.f15923j = bVar2;
    }

    private final void i(int i10, Collection<? extends E> collection, int i11) {
        b<E> bVar = this.f15922i;
        if (bVar != null) {
            bVar.i(i10, collection, i11);
            this.f15918e = this.f15922i.f15918e;
            this.f15920g += i11;
        } else {
            q(i10, i11);
            Iterator<? extends E> it = collection.iterator();
            for (int i12 = 0; i12 < i11; i12++) {
                this.f15918e[i10 + i12] = it.next();
            }
        }
    }

    private final void k(int i10, E e10) {
        b<E> bVar = this.f15922i;
        if (bVar != null) {
            bVar.k(i10, e10);
            this.f15918e = this.f15922i.f15918e;
            this.f15920g++;
        } else {
            q(i10, 1);
            this.f15918e[i10] = e10;
        }
    }

    private final void m() {
        if (r()) {
            throw new UnsupportedOperationException();
        }
    }

    private final boolean n(List<?> list) {
        boolean h10;
        h10 = c.h(this.f15918e, this.f15919f, this.f15920g, list);
        return h10;
    }

    private final void o(int i10) {
        if (this.f15922i != null) {
            throw new IllegalStateException();
        }
        if (i10 >= 0) {
            E[] eArr = this.f15918e;
            if (i10 > eArr.length) {
                this.f15918e = (E[]) c.e(this.f15918e, ArrayDeque.f14327e.a(eArr.length, i10));
                return;
            }
            return;
        }
        throw new OutOfMemoryError();
    }

    private final void p(int i10) {
        o(this.f15920g + i10);
    }

    private final void q(int i10, int i11) {
        p(i11);
        E[] eArr = this.f15918e;
        _ArraysJvm.g(eArr, eArr, i10 + i11, i10, this.f15919f + this.f15920g);
        this.f15920g += i11;
    }

    private final boolean r() {
        b<E> bVar;
        return this.f15921h || ((bVar = this.f15923j) != null && bVar.f15921h);
    }

    private final E s(int i10) {
        b<E> bVar = this.f15922i;
        if (bVar != null) {
            this.f15920g--;
            return bVar.s(i10);
        }
        E[] eArr = this.f15918e;
        E e10 = eArr[i10];
        _ArraysJvm.g(eArr, eArr, i10, i10 + 1, this.f15919f + this.f15920g);
        c.f(this.f15918e, (this.f15919f + this.f15920g) - 1);
        this.f15920g--;
        return e10;
    }

    private final void t(int i10, int i11) {
        b<E> bVar = this.f15922i;
        if (bVar != null) {
            bVar.t(i10, i11);
        } else {
            E[] eArr = this.f15918e;
            _ArraysJvm.g(eArr, eArr, i10, i10 + i11, this.f15920g);
            E[] eArr2 = this.f15918e;
            int i12 = this.f15920g;
            c.g(eArr2, i12 - i11, i12);
        }
        this.f15920g -= i11;
    }

    private final int u(int i10, int i11, Collection<? extends E> collection, boolean z10) {
        b<E> bVar = this.f15922i;
        if (bVar != null) {
            int u7 = bVar.u(i10, i11, collection, z10);
            this.f15920g -= u7;
            return u7;
        }
        int i12 = 0;
        int i13 = 0;
        while (i12 < i11) {
            int i14 = i10 + i12;
            if (collection.contains(this.f15918e[i14]) == z10) {
                E[] eArr = this.f15918e;
                i12++;
                eArr[i13 + i10] = eArr[i14];
                i13++;
            } else {
                i12++;
            }
        }
        int i15 = i11 - i13;
        E[] eArr2 = this.f15918e;
        _ArraysJvm.g(eArr2, eArr2, i10 + i13, i11 + i10, this.f15920g);
        E[] eArr3 = this.f15918e;
        int i16 = this.f15920g;
        c.g(eArr3, i16 - i15, i16);
        this.f15920g -= i15;
        return i15;
    }

    private final Object writeReplace() {
        if (r()) {
            return new h(this, 0);
        }
        throw new NotSerializableException("The list cannot be serialized while it is being built.");
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e10) {
        m();
        k(this.f15919f + this.f15920g, e10);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends E> collection) {
        k.e(collection, "elements");
        m();
        int size = collection.size();
        i(this.f15919f + this.f15920g, collection, size);
        return size > 0;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        m();
        t(this.f15919f, this.f15920g);
    }

    @Override // kotlin.collections.AbstractMutableList
    public int d() {
        return this.f15920g;
    }

    @Override // kotlin.collections.AbstractMutableList
    public E e(int i10) {
        m();
        AbstractList.f14311e.b(i10, this.f15920g);
        return s(this.f15919f + i10);
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof List) && n((List) obj));
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i10) {
        AbstractList.f14311e.b(i10, this.f15920g);
        return this.f15918e[this.f15919f + i10];
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        int i10;
        i10 = c.i(this.f15918e, this.f15919f, this.f15920g);
        return i10;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object obj) {
        for (int i10 = 0; i10 < this.f15920g; i10++) {
            if (k.a(this.f15918e[this.f15919f + i10], obj)) {
                return i10;
            }
        }
        return -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        return this.f15920g == 0;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new a(this, 0);
    }

    public final List<E> l() {
        if (this.f15922i == null) {
            m();
            this.f15921h = true;
            return this;
        }
        throw new IllegalStateException();
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object obj) {
        for (int i10 = this.f15920g - 1; i10 >= 0; i10--) {
            if (k.a(this.f15918e[this.f15919f + i10], obj)) {
                return i10;
            }
        }
        return -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator() {
        return new a(this, 0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object obj) {
        m();
        int indexOf = indexOf(obj);
        if (indexOf >= 0) {
            remove(indexOf);
        }
        return indexOf >= 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean removeAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        m();
        return u(this.f15919f, this.f15920g, collection, false) > 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean retainAll(Collection<? extends Object> collection) {
        k.e(collection, "elements");
        m();
        return u(this.f15919f, this.f15920g, collection, true) > 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int i10, E e10) {
        m();
        AbstractList.f14311e.b(i10, this.f15920g);
        E[] eArr = this.f15918e;
        int i11 = this.f15919f;
        E e11 = eArr[i11 + i10];
        eArr[i11 + i10] = e10;
        return e11;
    }

    @Override // java.util.AbstractList, java.util.List
    public List<E> subList(int i10, int i11) {
        AbstractList.f14311e.d(i10, i11, this.f15920g);
        E[] eArr = this.f15918e;
        int i12 = this.f15919f + i10;
        int i13 = i11 - i10;
        boolean z10 = this.f15921h;
        b<E> bVar = this.f15923j;
        return new b(eArr, i12, i13, z10, this, bVar == null ? this : bVar);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] tArr) {
        k.e(tArr, "destination");
        int length = tArr.length;
        int i10 = this.f15920g;
        if (length < i10) {
            E[] eArr = this.f15918e;
            int i11 = this.f15919f;
            T[] tArr2 = (T[]) Arrays.copyOfRange(eArr, i11, i10 + i11, tArr.getClass());
            k.d(tArr2, "copyOfRange(array, offse…h, destination.javaClass)");
            return tArr2;
        }
        E[] eArr2 = this.f15918e;
        int i12 = this.f15919f;
        _ArraysJvm.g(eArr2, tArr, 0, i12, i10 + i12);
        int length2 = tArr.length;
        int i13 = this.f15920g;
        if (length2 > i13) {
            tArr[i13] = null;
        }
        return tArr;
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        String j10;
        j10 = c.j(this.f15918e, this.f15919f, this.f15920g);
        return j10;
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(int i10) {
        AbstractList.f14311e.c(i10, this.f15920g);
        return new a(this, i10);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i10, E e10) {
        m();
        AbstractList.f14311e.c(i10, this.f15920g);
        k(this.f15919f + i10, e10);
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int i10, Collection<? extends E> collection) {
        k.e(collection, "elements");
        m();
        AbstractList.f14311e.c(i10, this.f15920g);
        int size = collection.size();
        i(this.f15919f + i10, collection, size);
        return size > 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] k10;
        E[] eArr = this.f15918e;
        int i10 = this.f15919f;
        k10 = _ArraysJvm.k(eArr, i10, this.f15920g + i10);
        return k10;
    }

    public b() {
        this(10);
    }

    public b(int i10) {
        this(c.d(i10), 0, 0, false, null, null);
    }
}
