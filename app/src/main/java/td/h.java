package td;

import kotlin.Metadata;
import ma.Unit;
import qa.ContinuationInterceptor;
import yd.Undispatched;

/* compiled from: Builders.common.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u001aL\u0010\u000b\u001a\u00020\n*\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00032\"\u0010\t\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0005ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\f\u001aR\u0010\u000e\u001a\u00028\u0000\"\u0004\b\u0000\u0010\r2\u0006\u0010\u0002\u001a\u00020\u00012\"\u0010\t\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0005H\u0086@ø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0004\b\u000e\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0010"}, d2 = {"Ltd/h0;", "Lqa/g;", "context", "Ltd/j0;", "start", "Lkotlin/Function2;", "Lqa/d;", "Lma/f0;", "", "block", "Ltd/i1;", "a", "(Ltd/h0;Lqa/g;Ltd/j0;Lya/p;)Ltd/i1;", "T", "c", "(Lqa/g;Lya/p;Lqa/d;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 5, mv = {1, 6, 0}, xs = "kotlinx/coroutines/BuildersKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class h {
    public static final i1 a(h0 h0Var, qa.g gVar, CoroutineStart coroutineStart, ya.p<? super h0, ? super qa.d<? super Unit>, ? extends Object> pVar) {
        AbstractCoroutine y1Var;
        qa.g e10 = b0.e(h0Var, gVar);
        if (coroutineStart.c()) {
            y1Var = new r1(e10, pVar);
        } else {
            y1Var = new y1(e10, true);
        }
        y1Var.C0(coroutineStart, y1Var, pVar);
        return y1Var;
    }

    public static /* synthetic */ i1 b(h0 h0Var, qa.g gVar, CoroutineStart coroutineStart, ya.p pVar, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            gVar = qa.h.f17173e;
        }
        if ((i10 & 2) != 0) {
            coroutineStart = CoroutineStart.DEFAULT;
        }
        return g.a(h0Var, gVar, coroutineStart, pVar);
    }

    public static final <T> Object c(qa.g gVar, ya.p<? super h0, ? super qa.d<? super T>, ? extends Object> pVar, qa.d<? super T> dVar) {
        Object E0;
        Object c10;
        qa.g f18758i = dVar.getF18758i();
        qa.g d10 = b0.d(f18758i, gVar);
        m1.e(d10);
        if (d10 == f18758i) {
            kotlinx.coroutines.internal.y yVar = new kotlinx.coroutines.internal.y(d10, dVar);
            E0 = Undispatched.b(yVar, yVar, pVar);
        } else {
            ContinuationInterceptor.b bVar = ContinuationInterceptor.f17170a;
            if (za.k.a(d10.c(bVar), f18758i.c(bVar))) {
                e2 e2Var = new e2(d10, dVar);
                Object c11 = kotlinx.coroutines.internal.e0.c(d10, null);
                try {
                    Object b10 = Undispatched.b(e2Var, e2Var, pVar);
                    kotlinx.coroutines.internal.e0.a(d10, c11);
                    E0 = b10;
                } catch (Throwable th) {
                    kotlinx.coroutines.internal.e0.a(d10, c11);
                    throw th;
                }
            } else {
                q0 q0Var = new q0(d10, dVar);
                yd.a.d(pVar, q0Var, q0Var, null, 4, null);
                E0 = q0Var.E0();
            }
        }
        c10 = ra.d.c();
        if (E0 == c10) {
            sa.h.c(dVar);
        }
        return E0;
    }
}
