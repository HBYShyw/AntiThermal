package kotlin.collections;

import java.util.Iterator;

/* compiled from: Iterables.kt */
/* loaded from: classes2.dex */
public final class f0<T> implements Iterable<IndexedValue<? extends T>>, ab.a {

    /* renamed from: e, reason: collision with root package name */
    private final ya.a<Iterator<T>> f14322e;

    /* JADX WARN: Multi-variable type inference failed */
    public f0(ya.a<? extends Iterator<? extends T>> aVar) {
        za.k.e(aVar, "iteratorFactory");
        this.f14322e = aVar;
    }

    @Override // java.lang.Iterable
    public Iterator<IndexedValue<T>> iterator() {
        return new g0(this.f14322e.invoke());
    }
}
