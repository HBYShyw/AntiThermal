package gd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* compiled from: SpecialTypes.kt */
/* loaded from: classes2.dex */
public final class s0 {
    public static final a a(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        v1 Z0 = g0Var.Z0();
        if (Z0 instanceof a) {
            return (a) Z0;
        }
        return null;
    }

    public static final o0 b(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        a a10 = a(g0Var);
        if (a10 != null) {
            return a10.i1();
        }
        return null;
    }

    public static final boolean c(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        return g0Var.Z0() instanceof p;
    }

    private static final IntersectionTypeConstructor d(IntersectionTypeConstructor intersectionTypeConstructor) {
        int u7;
        g0 g0Var;
        Collection<g0> q10 = intersectionTypeConstructor.q();
        u7 = kotlin.collections.s.u(q10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = q10.iterator();
        boolean z10 = false;
        while (true) {
            g0Var = null;
            if (!it.hasNext()) {
                break;
            }
            g0 g0Var2 = (g0) it.next();
            if (s1.l(g0Var2)) {
                g0Var2 = f(g0Var2.Z0(), false, 1, null);
                z10 = true;
            }
            arrayList.add(g0Var2);
        }
        if (!z10) {
            return null;
        }
        g0 e10 = intersectionTypeConstructor.e();
        if (e10 != null) {
            if (s1.l(e10)) {
                e10 = f(e10.Z0(), false, 1, null);
            }
            g0Var = e10;
        }
        return new IntersectionTypeConstructor(arrayList).i(g0Var);
    }

    public static final v1 e(v1 v1Var, boolean z10) {
        za.k.e(v1Var, "<this>");
        p b10 = p.f11860h.b(v1Var, z10);
        if (b10 != null) {
            return b10;
        }
        o0 g6 = g(v1Var);
        return g6 != null ? g6 : v1Var.a1(false);
    }

    public static /* synthetic */ v1 f(v1 v1Var, boolean z10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            z10 = false;
        }
        return e(v1Var, z10);
    }

    private static final o0 g(g0 g0Var) {
        IntersectionTypeConstructor d10;
        TypeConstructor W0 = g0Var.W0();
        IntersectionTypeConstructor intersectionTypeConstructor = W0 instanceof IntersectionTypeConstructor ? (IntersectionTypeConstructor) W0 : null;
        if (intersectionTypeConstructor == null || (d10 = d(intersectionTypeConstructor)) == null) {
            return null;
        }
        return d10.d();
    }

    public static final o0 h(o0 o0Var, boolean z10) {
        za.k.e(o0Var, "<this>");
        p b10 = p.f11860h.b(o0Var, z10);
        if (b10 != null) {
            return b10;
        }
        o0 g6 = g(o0Var);
        return g6 == null ? o0Var.a1(false) : g6;
    }

    public static /* synthetic */ o0 i(o0 o0Var, boolean z10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            z10 = false;
        }
        return h(o0Var, z10);
    }

    public static final o0 j(o0 o0Var, o0 o0Var2) {
        za.k.e(o0Var, "<this>");
        za.k.e(o0Var2, "abbreviatedType");
        return i0.a(o0Var) ? o0Var : new a(o0Var, o0Var2);
    }

    public static final hd.i k(hd.i iVar) {
        za.k.e(iVar, "<this>");
        return new hd.i(iVar.f1(), iVar.W0(), iVar.h1(), iVar.V0(), iVar.X0(), true);
    }
}
