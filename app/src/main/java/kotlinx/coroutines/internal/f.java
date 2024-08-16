package kotlinx.coroutines.internal;

import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import ma.Unit;
import ma.p;
import td.c2;
import td.e2;
import td.i1;
import td.w0;

/* compiled from: DispatchedContinuation.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\u001aH\u0010\b\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0000*\b\u0012\u0004\u0012\u00028\u00000\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0016\b\u0002\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\"\u001a\u0010\u000f\u001a\u00020\n8\u0002X\u0083\u0004¢\u0006\f\n\u0004\b\u000b\u0010\f\u0012\u0004\b\r\u0010\u000e\"\u001a\u0010\u0011\u001a\u00020\n8\u0000X\u0081\u0004¢\u0006\f\n\u0004\b\b\u0010\f\u0012\u0004\b\u0010\u0010\u000e\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012"}, d2 = {"T", "Lqa/d;", "Lma/p;", "result", "Lkotlin/Function1;", "", "Lma/f0;", "onCancellation", "b", "(Lqa/d;Ljava/lang/Object;Lya/l;)V", "Lkotlinx/coroutines/internal/a0;", "a", "Lkotlinx/coroutines/internal/a0;", "getUNDEFINED$annotations", "()V", "UNDEFINED", "getREUSABLE_CLAIMED$annotations", "REUSABLE_CLAIMED", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class f {

    /* renamed from: a */
    private static final Symbol f14362a = new Symbol("UNDEFINED");

    /* renamed from: b */
    public static final Symbol f14363b = new Symbol("REUSABLE_CLAIMED");

    /* JADX WARN: Finally extract failed */
    public static final <T> void b(qa.d<? super T> dVar, Object obj, ya.l<? super Throwable, Unit> lVar) {
        boolean z10;
        if (dVar instanceof e) {
            e eVar = (e) dVar;
            Object c10 = td.z.c(obj, lVar);
            if (eVar.f14351h.u0(eVar.getF18758i())) {
                eVar._state = c10;
                eVar.f18787g = 1;
                eVar.f14351h.t0(eVar.getF18758i(), eVar);
                return;
            }
            w0 a10 = c2.f18727a.a();
            if (a10.C0()) {
                eVar._state = c10;
                eVar.f18787g = 1;
                a10.y0(eVar);
                return;
            }
            a10.A0(true);
            try {
                i1 i1Var = (i1) eVar.getF18758i().c(i1.f18746d);
                if (i1Var == null || i1Var.b()) {
                    z10 = false;
                } else {
                    CancellationException w10 = i1Var.w();
                    eVar.b(c10, w10);
                    p.a aVar = ma.p.f15184e;
                    eVar.resumeWith(ma.p.a(ma.q.a(w10)));
                    z10 = true;
                }
                if (!z10) {
                    qa.d<T> dVar2 = eVar.f14352i;
                    Object obj2 = eVar.countOrElement;
                    qa.g f18758i = dVar2.getF18758i();
                    Object c11 = e0.c(f18758i, obj2);
                    e2<?> g6 = c11 != e0.f14355a ? td.b0.g(dVar2, f18758i, c11) : null;
                    try {
                        eVar.f14352i.resumeWith(obj);
                        Unit unit = Unit.f15173a;
                        if (g6 == null || g6.E0()) {
                            e0.a(f18758i, c11);
                        }
                    } catch (Throwable th) {
                        if (g6 == null || g6.E0()) {
                            e0.a(f18758i, c11);
                        }
                        throw th;
                    }
                }
                do {
                } while (a10.E0());
            } finally {
                try {
                    return;
                } finally {
                }
            }
            return;
        }
        dVar.resumeWith(obj);
    }

    public static /* synthetic */ void c(qa.d dVar, Object obj, ya.l lVar, int i10, Object obj2) {
        if ((i10 & 2) != 0) {
            lVar = null;
        }
        b(dVar, obj, lVar);
    }
}
