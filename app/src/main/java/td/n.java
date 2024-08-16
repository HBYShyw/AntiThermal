package td;

import kotlin.Metadata;

/* compiled from: CancellableContinuation.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003\"\u0004\b\u0000\u0010\u00002\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001H\u0000\u001a\u0018\u0010\t\u001a\u00020\b*\u0006\u0012\u0002\b\u00030\u00052\u0006\u0010\u0007\u001a\u00020\u0006H\u0000¨\u0006\n"}, d2 = {"T", "Lqa/d;", "delegate", "Ltd/l;", "a", "Ltd/k;", "Lkotlinx/coroutines/internal/n;", "node", "Lma/f0;", "b", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class n {
    public static final <T> l<T> a(qa.d<? super T> dVar) {
        if (!(dVar instanceof kotlinx.coroutines.internal.e)) {
            return new l<>(dVar, 1);
        }
        l<T> n10 = ((kotlinx.coroutines.internal.e) dVar).n();
        if (n10 != null) {
            if (!n10.I()) {
                n10 = null;
            }
            if (n10 != null) {
                return n10;
            }
        }
        return new l<>(dVar, 2);
    }

    public static final void b(k<?> kVar, kotlinx.coroutines.internal.n nVar) {
        kVar.f(new x1(nVar));
    }
}
