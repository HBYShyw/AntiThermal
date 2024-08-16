package rd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class f<T, R, E> implements Sequence<E> {

    /* renamed from: a, reason: collision with root package name */
    private final Sequence<T> f17817a;

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<T, R> f17818b;

    /* renamed from: c, reason: collision with root package name */
    private final ya.l<R, Iterator<E>> f17819c;

    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Iterator<E>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final Iterator<T> f17820e;

        /* renamed from: f, reason: collision with root package name */
        private Iterator<? extends E> f17821f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ f<T, R, E> f17822g;

        a(f<T, R, E> fVar) {
            this.f17822g = fVar;
            this.f17820e = ((f) fVar).f17817a.iterator();
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x0045, code lost:
        
            return true;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private final boolean b() {
            Iterator<? extends E> it = this.f17821f;
            if ((it == null || it.hasNext()) ? false : true) {
                this.f17821f = null;
            }
            while (true) {
                if (this.f17821f != null) {
                    break;
                }
                if (!this.f17820e.hasNext()) {
                    return false;
                }
                Iterator<? extends E> it2 = (Iterator) ((f) this.f17822g).f17819c.invoke(((f) this.f17822g).f17818b.invoke(this.f17820e.next()));
                if (it2.hasNext()) {
                    this.f17821f = it2;
                    break;
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return b();
        }

        @Override // java.util.Iterator
        public E next() {
            if (b()) {
                Iterator<? extends E> it = this.f17821f;
                za.k.b(it);
                return it.next();
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public f(Sequence<? extends T> sequence, ya.l<? super T, ? extends R> lVar, ya.l<? super R, ? extends Iterator<? extends E>> lVar2) {
        za.k.e(sequence, "sequence");
        za.k.e(lVar, "transformer");
        za.k.e(lVar2, "iterator");
        this.f17817a = sequence;
        this.f17818b = lVar;
        this.f17819c = lVar2;
    }

    @Override // rd.Sequence
    public Iterator<E> iterator() {
        return new a(this);
    }
}
