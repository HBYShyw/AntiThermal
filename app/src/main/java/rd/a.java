package rd;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

/* compiled from: SequencesJVM.kt */
/* loaded from: classes2.dex */
public final class a<T> implements Sequence<T> {

    /* renamed from: a, reason: collision with root package name */
    private final AtomicReference<Sequence<T>> f17804a;

    public a(Sequence<? extends T> sequence) {
        za.k.e(sequence, "sequence");
        this.f17804a = new AtomicReference<>(sequence);
    }

    @Override // rd.Sequence
    public Iterator<T> iterator() {
        Sequence<T> andSet = this.f17804a.getAndSet(null);
        if (andSet != null) {
            return andSet.iterator();
        }
        throw new IllegalStateException("This sequence can be consumed only once.");
    }
}
