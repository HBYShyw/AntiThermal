package td;

import kotlin.Metadata;
import ma.Unit;
import ma.p;

/* compiled from: DispatchedTask.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\u001a \u0010\u0005\u001a\u00020\u0004\"\u0004\b\u0000\u0010\u0000*\b\u0012\u0004\u0012\u00028\u00000\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0000\u001a.\u0010\n\u001a\u00020\u0004\"\u0004\b\u0000\u0010\u0000*\b\u0012\u0004\u0012\u00028\u00000\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\u00062\u0006\u0010\t\u001a\u00020\bH\u0000\u001a\u0010\u0010\u000b\u001a\u00020\u0004*\u0006\u0012\u0002\b\u00030\u0001H\u0002\"\u0018\u0010\u000e\u001a\u00020\b*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r\"\u0018\u0010\u0010\u001a\u00020\b*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\r¨\u0006\u0011"}, d2 = {"T", "Ltd/r0;", "", "mode", "Lma/f0;", "a", "Lqa/d;", "delegate", "", "undispatched", "d", "e", "b", "(I)Z", "isCancellableMode", "c", "isReusableMode", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class s0 {
    public static final <T> void a(r0<? super T> r0Var, int i10) {
        qa.d<? super T> c10 = r0Var.c();
        boolean z10 = i10 == 4;
        if (!z10 && (c10 instanceof kotlinx.coroutines.internal.e) && b(i10) == b(r0Var.f18787g)) {
            CoroutineDispatcher coroutineDispatcher = ((kotlinx.coroutines.internal.e) c10).f14351h;
            qa.g f18758i = c10.getF18758i();
            if (coroutineDispatcher.u0(f18758i)) {
                coroutineDispatcher.t0(f18758i, r0Var);
                return;
            } else {
                e(r0Var);
                return;
            }
        }
        d(r0Var, c10, z10);
    }

    public static final boolean b(int i10) {
        return i10 == 1 || i10 == 2;
    }

    public static final boolean c(int i10) {
        return i10 == 2;
    }

    public static final <T> void d(r0<? super T> r0Var, qa.d<? super T> dVar, boolean z10) {
        Object g6;
        boolean E0;
        Object k10 = r0Var.k();
        Throwable e10 = r0Var.e(k10);
        if (e10 != null) {
            p.a aVar = ma.p.f15184e;
            g6 = ma.q.a(e10);
        } else {
            p.a aVar2 = ma.p.f15184e;
            g6 = r0Var.g(k10);
        }
        Object a10 = ma.p.a(g6);
        if (z10) {
            kotlinx.coroutines.internal.e eVar = (kotlinx.coroutines.internal.e) dVar;
            qa.d<T> dVar2 = eVar.f14352i;
            Object obj = eVar.countOrElement;
            qa.g f18758i = dVar2.getF18758i();
            Object c10 = kotlinx.coroutines.internal.e0.c(f18758i, obj);
            e2<?> g10 = c10 != kotlinx.coroutines.internal.e0.f14355a ? b0.g(dVar2, f18758i, c10) : null;
            try {
                eVar.f14352i.resumeWith(a10);
                Unit unit = Unit.f15173a;
                if (g10 != null) {
                    if (!E0) {
                        return;
                    }
                }
                return;
            } finally {
                if (g10 == null || g10.E0()) {
                    kotlinx.coroutines.internal.e0.a(f18758i, c10);
                }
            }
        }
        dVar.resumeWith(a10);
    }

    private static final void e(r0<?> r0Var) {
        w0 a10 = c2.f18727a.a();
        if (a10.C0()) {
            a10.y0(r0Var);
            return;
        }
        a10.A0(true);
        try {
            d(r0Var, r0Var.c(), true);
            do {
            } while (a10.E0());
        } finally {
            try {
            } finally {
            }
        }
    }
}
