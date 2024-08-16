package td;

import kotlin.Metadata;
import ma.Unit;
import ma.p;

/* compiled from: CompletionState.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a<\u0010\u0007\u001a\u0004\u0018\u00010\u0006\"\u0004\b\u0000\u0010\u0000*\b\u0012\u0004\u0012\u00028\u00000\u00012\u0016\b\u0002\u0010\u0005\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0002H\u0000ø\u0001\u0000¢\u0006\u0004\b\u0007\u0010\b\u001a0\u0010\u000b\u001a\u0004\u0018\u00010\u0006\"\u0004\b\u0000\u0010\u0000*\b\u0012\u0004\u0012\u00028\u00000\u00012\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\tH\u0000ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\f\u001a6\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001\"\u0004\b\u0000\u0010\u00002\b\u0010\r\u001a\u0004\u0018\u00010\u00062\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u000eH\u0000ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0011\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012"}, d2 = {"T", "Lma/p;", "Lkotlin/Function1;", "", "Lma/f0;", "onCancellation", "", "c", "(Ljava/lang/Object;Lya/l;)Ljava/lang/Object;", "Ltd/k;", "caller", "b", "(Ljava/lang/Object;Ltd/k;)Ljava/lang/Object;", "state", "Lqa/d;", "uCont", "a", "(Ljava/lang/Object;Lqa/d;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class z {
    public static final <T> Object a(Object obj, qa.d<? super T> dVar) {
        if (obj instanceof v) {
            p.a aVar = ma.p.f15184e;
            return ma.p.a(ma.q.a(((v) obj).f18800a));
        }
        p.a aVar2 = ma.p.f15184e;
        return ma.p.a(obj);
    }

    public static final <T> Object b(Object obj, k<?> kVar) {
        Throwable b10 = ma.p.b(obj);
        return b10 == null ? obj : new v(b10, false, 2, null);
    }

    public static final <T> Object c(Object obj, ya.l<? super Throwable, Unit> lVar) {
        Throwable b10 = ma.p.b(obj);
        if (b10 == null) {
            return lVar != null ? new CompletedWithCancellation(obj, lVar) : obj;
        }
        return new v(b10, false, 2, null);
    }

    public static /* synthetic */ Object d(Object obj, ya.l lVar, int i10, Object obj2) {
        if ((i10 & 1) != 0) {
            lVar = null;
        }
        return c(obj, lVar);
    }
}
