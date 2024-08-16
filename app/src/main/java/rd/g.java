package rd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class g<T> implements Sequence<T> {

    /* renamed from: a, reason: collision with root package name */
    private final ya.a<T> f17823a;

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<T, T> f17824b;

    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<T>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private T f17825e;

        /* renamed from: f, reason: collision with root package name */
        private int f17826f = -2;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ g<T> f17827g;

        a(g<T> gVar) {
            this.f17827g = gVar;
        }

        private final void b() {
            T t7;
            if (this.f17826f == -2) {
                t7 = (T) ((g) this.f17827g).f17823a.invoke();
            } else {
                ya.l lVar = ((g) this.f17827g).f17824b;
                T t10 = this.f17825e;
                za.k.b(t10);
                t7 = (T) lVar.invoke(t10);
            }
            this.f17825e = t7;
            this.f17826f = t7 == null ? 0 : 1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f17826f < 0) {
                b();
            }
            return this.f17826f == 1;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.f17826f < 0) {
                b();
            }
            if (this.f17826f != 0) {
                T t7 = this.f17825e;
                za.k.c(t7, "null cannot be cast to non-null type T of kotlin.sequences.GeneratorSequence");
                this.f17826f = -1;
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
    public g(ya.a<? extends T> aVar, ya.l<? super T, ? extends T> lVar) {
        za.k.e(aVar, "getInitialValue");
        za.k.e(lVar, "getNextValue");
        this.f17823a = aVar;
        this.f17824b = lVar;
    }

    @Override // rd.Sequence
    public Iterator<T> iterator() {
        return new a(this);
    }
}
