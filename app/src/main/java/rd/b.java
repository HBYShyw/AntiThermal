package rd;

import java.util.Iterator;

/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class b<T> implements Sequence<T>, c<T> {

    /* renamed from: a, reason: collision with root package name */
    private final Sequence<T> f17805a;

    /* renamed from: b, reason: collision with root package name */
    private final int f17806b;

    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<T>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final Iterator<T> f17807e;

        /* renamed from: f, reason: collision with root package name */
        private int f17808f;

        a(b<T> bVar) {
            this.f17807e = ((b) bVar).f17805a.iterator();
            this.f17808f = ((b) bVar).f17806b;
        }

        private final void b() {
            while (this.f17808f > 0 && this.f17807e.hasNext()) {
                this.f17807e.next();
                this.f17808f--;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            b();
            return this.f17807e.hasNext();
        }

        @Override // java.util.Iterator
        public T next() {
            b();
            return this.f17807e.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public b(Sequence<? extends T> sequence, int i10) {
        za.k.e(sequence, "sequence");
        this.f17805a = sequence;
        this.f17806b = i10;
        if (i10 >= 0) {
            return;
        }
        throw new IllegalArgumentException(("count must be non-negative, but was " + i10 + '.').toString());
    }

    @Override // rd.c
    public Sequence<T> a(int i10) {
        int i11 = this.f17806b + i10;
        return i11 < 0 ? new b(this, i10) : new b(this.f17805a, i11);
    }

    @Override // rd.Sequence
    public Iterator<T> iterator() {
        return new a(this);
    }
}
