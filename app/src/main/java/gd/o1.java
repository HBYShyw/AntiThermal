package gd;

import java.util.List;
import ma.NoWhenBranchMatchedException;

/* compiled from: TypeSubstitution.kt */
/* loaded from: classes2.dex */
public final class o1 {
    public static final o0 a(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        v1 Z0 = g0Var.Z0();
        o0 o0Var = Z0 instanceof o0 ? (o0) Z0 : null;
        if (o0Var != null) {
            return o0Var;
        }
        throw new IllegalStateException(("This is should be simple type: " + g0Var).toString());
    }

    public static final g0 b(g0 g0Var, List<? extends TypeProjection> list, qb.g gVar) {
        za.k.e(g0Var, "<this>");
        za.k.e(list, "newArguments");
        za.k.e(gVar, "newAnnotations");
        return e(g0Var, list, gVar, null, 4, null);
    }

    public static final g0 c(g0 g0Var, List<? extends TypeProjection> list, qb.g gVar, List<? extends TypeProjection> list2) {
        za.k.e(g0Var, "<this>");
        za.k.e(list, "newArguments");
        za.k.e(gVar, "newAnnotations");
        za.k.e(list2, "newArgumentsForUpperBound");
        if ((list.isEmpty() || list == g0Var.U0()) && gVar == g0Var.i()) {
            return g0Var;
        }
        c1 V0 = g0Var.V0();
        if ((gVar instanceof qb.l) && gVar.isEmpty()) {
            gVar = qb.g.f17195b.b();
        }
        c1 a10 = d1.a(V0, gVar);
        v1 Z0 = g0Var.Z0();
        if (Z0 instanceof a0) {
            a0 a0Var = (a0) Z0;
            return h0.d(d(a0Var.e1(), list, a10), d(a0Var.f1(), list2, a10));
        }
        if (Z0 instanceof o0) {
            return d((o0) Z0, list, a10);
        }
        throw new NoWhenBranchMatchedException();
    }

    public static final o0 d(o0 o0Var, List<? extends TypeProjection> list, c1 c1Var) {
        za.k.e(o0Var, "<this>");
        za.k.e(list, "newArguments");
        za.k.e(c1Var, "newAttributes");
        if (list.isEmpty() && c1Var == o0Var.V0()) {
            return o0Var;
        }
        if (list.isEmpty()) {
            return o0Var.c1(c1Var);
        }
        return h0.j(c1Var, o0Var.W0(), list, o0Var.X0(), null, 16, null);
    }

    public static /* synthetic */ g0 e(g0 g0Var, List list, qb.g gVar, List list2, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            list = g0Var.U0();
        }
        if ((i10 & 2) != 0) {
            gVar = g0Var.i();
        }
        if ((i10 & 4) != 0) {
            list2 = list;
        }
        return c(g0Var, list, gVar, list2);
    }

    public static /* synthetic */ o0 f(o0 o0Var, List list, c1 c1Var, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            list = o0Var.U0();
        }
        if ((i10 & 2) != 0) {
            c1Var = o0Var.V0();
        }
        return d(o0Var, list, c1Var);
    }
}
