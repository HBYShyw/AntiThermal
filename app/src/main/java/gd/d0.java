package gd;

import ma.NoWhenBranchMatchedException;

/* compiled from: flexibleTypes.kt */
/* loaded from: classes2.dex */
public final class d0 {
    public static final a0 a(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        v1 Z0 = g0Var.Z0();
        za.k.c(Z0, "null cannot be cast to non-null type org.jetbrains.kotlin.types.FlexibleType");
        return (a0) Z0;
    }

    public static final boolean b(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        return g0Var.Z0() instanceof a0;
    }

    public static final o0 c(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        v1 Z0 = g0Var.Z0();
        if (Z0 instanceof a0) {
            return ((a0) Z0).e1();
        }
        if (Z0 instanceof o0) {
            return (o0) Z0;
        }
        throw new NoWhenBranchMatchedException();
    }

    public static final o0 d(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        v1 Z0 = g0Var.Z0();
        if (Z0 instanceof a0) {
            return ((a0) Z0).f1();
        }
        if (Z0 instanceof o0) {
            return (o0) Z0;
        }
        throw new NoWhenBranchMatchedException();
    }
}
