package kotlin.collections;

import java.util.Iterator;

/* compiled from: Iterators.kt */
/* loaded from: classes2.dex */
public final class g0<T> implements Iterator<IndexedValue<? extends T>>, ab.a {

    /* renamed from: e, reason: collision with root package name */
    private final Iterator<T> f14323e;

    /* renamed from: f, reason: collision with root package name */
    private int f14324f;

    /* JADX WARN: Multi-variable type inference failed */
    public g0(Iterator<? extends T> it) {
        za.k.e(it, "iterator");
        this.f14323e = it;
    }

    @Override // java.util.Iterator
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public final IndexedValue<T> next() {
        int i10 = this.f14324f;
        this.f14324f = i10 + 1;
        if (i10 < 0) {
            r.t();
        }
        return new IndexedValue<>(i10, this.f14323e.next());
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f14323e.hasNext();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
