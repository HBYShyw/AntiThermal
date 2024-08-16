package rd;

import java.util.Iterator;

/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class p<T, R> implements Sequence<R> {

    /* renamed from: a, reason: collision with root package name */
    private final Sequence<T> f17842a;

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<T, R> f17843b;

    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<R>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final Iterator<T> f17844e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ p<T, R> f17845f;

        a(p<T, R> pVar) {
            this.f17845f = pVar;
            this.f17844e = ((p) pVar).f17842a.iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17844e.hasNext();
        }

        @Override // java.util.Iterator
        public R next() {
            return (R) ((p) this.f17845f).f17843b.invoke(this.f17844e.next());
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public p(Sequence<? extends T> sequence, ya.l<? super T, ? extends R> lVar) {
        za.k.e(sequence, "sequence");
        za.k.e(lVar, "transformer");
        this.f17842a = sequence;
        this.f17843b = lVar;
    }

    public final <E> Sequence<E> d(ya.l<? super R, ? extends Iterator<? extends E>> lVar) {
        za.k.e(lVar, "iterator");
        return new f(this.f17842a, this.f17843b, lVar);
    }

    @Override // rd.Sequence
    public Iterator<R> iterator() {
        return new a(this);
    }
}
