package rd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class e<T> implements Sequence<T> {

    /* renamed from: a, reason: collision with root package name */
    private final Sequence<T> f17810a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f17811b;

    /* renamed from: c, reason: collision with root package name */
    private final ya.l<T, Boolean> f17812c;

    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<T>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final Iterator<T> f17813e;

        /* renamed from: f, reason: collision with root package name */
        private int f17814f = -1;

        /* renamed from: g, reason: collision with root package name */
        private T f17815g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ e<T> f17816h;

        a(e<T> eVar) {
            this.f17816h = eVar;
            this.f17813e = ((e) eVar).f17810a.iterator();
        }

        private final void b() {
            while (this.f17813e.hasNext()) {
                T next = this.f17813e.next();
                if (((Boolean) ((e) this.f17816h).f17812c.invoke(next)).booleanValue() == ((e) this.f17816h).f17811b) {
                    this.f17815g = next;
                    this.f17814f = 1;
                    return;
                }
            }
            this.f17814f = 0;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f17814f == -1) {
                b();
            }
            return this.f17814f == 1;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.f17814f == -1) {
                b();
            }
            if (this.f17814f != 0) {
                T t7 = this.f17815g;
                this.f17815g = null;
                this.f17814f = -1;
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
    public e(Sequence<? extends T> sequence, boolean z10, ya.l<? super T, Boolean> lVar) {
        za.k.e(sequence, "sequence");
        za.k.e(lVar, "predicate");
        this.f17810a = sequence;
        this.f17811b = z10;
        this.f17812c = lVar;
    }

    @Override // rd.Sequence
    public Iterator<T> iterator() {
        return new a(this);
    }
}
