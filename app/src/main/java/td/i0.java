package td;

import kotlin.Metadata;
import kotlinx.coroutines.internal.CoroutineScope;
import yd.Undispatched;

/* compiled from: CoroutineScope.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u001aJ\u0010\u0006\u001a\u00028\u0000\"\u0004\b\u0000\u0010\u00002\"\u0010\u0005\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0001H\u0086@ø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0004\b\u0006\u0010\u0007\u001a\u000e\u0010\n\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000b"}, d2 = {"R", "Lkotlin/Function2;", "Ltd/h0;", "Lqa/d;", "", "block", "b", "(Lya/p;Lqa/d;)Ljava/lang/Object;", "Lqa/g;", "context", "a", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class i0 {
    public static final h0 a(qa.g gVar) {
        CompletableJob b10;
        if (gVar.c(i1.f18746d) == null) {
            b10 = n1.b(null, 1, null);
            gVar = gVar.o0(b10);
        }
        return new CoroutineScope(gVar);
    }

    public static final <R> Object b(ya.p<? super h0, ? super qa.d<? super R>, ? extends Object> pVar, qa.d<? super R> dVar) {
        Object c10;
        kotlinx.coroutines.internal.y yVar = new kotlinx.coroutines.internal.y(dVar.getF18758i(), dVar);
        Object b10 = Undispatched.b(yVar, yVar, pVar);
        c10 = ra.d.c();
        if (b10 == c10) {
            sa.h.c(dVar);
        }
        return b10;
    }
}
