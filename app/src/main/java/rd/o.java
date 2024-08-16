package rd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class o<T> implements Sequence<T> {

    /* renamed from: a, reason: collision with root package name */
    private final Sequence<T> f17836a;

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<T, Boolean> f17837b;

    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<T>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final Iterator<T> f17838e;

        /* renamed from: f, reason: collision with root package name */
        private int f17839f = -1;

        /* renamed from: g, reason: collision with root package name */
        private T f17840g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ o<T> f17841h;

        a(o<T> oVar) {
            this.f17841h = oVar;
            this.f17838e = ((o) oVar).f17836a.iterator();
        }

        private final void b() {
            if (this.f17838e.hasNext()) {
                T next = this.f17838e.next();
                if (((Boolean) ((o) this.f17841h).f17837b.invoke(next)).booleanValue()) {
                    this.f17839f = 1;
                    this.f17840g = next;
                    return;
                }
            }
            this.f17839f = 0;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f17839f == -1) {
                b();
            }
            return this.f17839f == 1;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.f17839f == -1) {
                b();
            }
            if (this.f17839f != 0) {
                T t7 = this.f17840g;
                this.f17840g = null;
                this.f17839f = -1;
                return t7;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public o(Sequence<? extends T> sequence, ya.l<? super T, Boolean> lVar) {
        za.k.e(sequence, "sequence");
        za.k.e(lVar, "predicate");
        this.f17836a = sequence;
        this.f17837b = lVar;
    }

    @Override // rd.Sequence
    public Iterator<T> iterator() {
        return new a(this);
    }
}
