package gd;

import ma.NoWhenBranchMatchedException;

/* compiled from: TypeWithEnhancement.kt */
/* loaded from: classes2.dex */
public final class u1 {
    /* JADX WARN: Multi-variable type inference failed */
    public static final g0 a(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        if (g0Var instanceof t1) {
            return ((t1) g0Var).Q();
        }
        return null;
    }

    public static final v1 b(v1 v1Var, g0 g0Var) {
        za.k.e(v1Var, "<this>");
        za.k.e(g0Var, "origin");
        return d(v1Var, a(g0Var));
    }

    public static final v1 c(v1 v1Var, g0 g0Var, ya.l<? super g0, ? extends g0> lVar) {
        za.k.e(v1Var, "<this>");
        za.k.e(g0Var, "origin");
        za.k.e(lVar, "transform");
        g0 a10 = a(g0Var);
        return d(v1Var, a10 != null ? lVar.invoke(a10) : null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final v1 d(v1 v1Var, g0 g0Var) {
        za.k.e(v1Var, "<this>");
        if (v1Var instanceof t1) {
            return d(((t1) v1Var).O0(), g0Var);
        }
        if (g0Var == null || za.k.a(g0Var, v1Var)) {
            return v1Var;
        }
        if (v1Var instanceof o0) {
            return new r0((o0) v1Var, g0Var);
        }
        if (v1Var instanceof a0) {
            return new c0((a0) v1Var, g0Var);
        }
        throw new NoWhenBranchMatchedException();
    }
}
