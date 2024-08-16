package qc;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/* compiled from: UnmodifiableLazyStringList.java */
/* loaded from: classes2.dex */
public class x extends AbstractList<String> implements RandomAccess, o {

    /* renamed from: e, reason: collision with root package name */
    private final o f17369e;

    /* compiled from: UnmodifiableLazyStringList.java */
    /* loaded from: classes2.dex */
    class a implements ListIterator<String> {

        /* renamed from: e, reason: collision with root package name */
        ListIterator<String> f17370e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f17371f;

        a(int i10) {
            this.f17371f = i10;
            this.f17370e = x.this.f17369e.listIterator(i10);
        }

        @Override // java.util.ListIterator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public void add(String str) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public String next() {
            return this.f17370e.next();
        }

        @Override // java.util.ListIterator
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public String previous() {
            return this.f17370e.previous();
        }

        @Override // java.util.ListIterator
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void set(String str) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.f17370e.hasNext();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.f17370e.hasPrevious();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.f17370e.nextIndex();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.f17370e.previousIndex();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* compiled from: UnmodifiableLazyStringList.java */
    /* loaded from: classes2.dex */
    class b implements Iterator<String> {

        /* renamed from: e, reason: collision with root package name */
        Iterator<String> f17373e;

        b() {
            this.f17373e = x.this.f17369e.iterator();
        }

        @Override // java.util.Iterator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public String next() {
            return this.f17373e.next();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17373e.hasNext();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public x(o oVar) {
        this.f17369e = oVar;
    }

    @Override // qc.o
    public void b(d dVar) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractList, java.util.List
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public String get(int i10) {
        return this.f17369e.get(i10);
    }

    @Override // qc.o
    public d getByteString(int i10) {
        return this.f17369e.getByteString(i10);
    }

    @Override // qc.o
    public List<?> getUnderlyingElements() {
        return this.f17369e.getUnderlyingElements();
    }

    @Override // qc.o
    public o getUnmodifiableView() {
        return this;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<String> iterator() {
        return new b();
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<String> listIterator(int i10) {
        return new a(i10);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.f17369e.size();
    }
}
