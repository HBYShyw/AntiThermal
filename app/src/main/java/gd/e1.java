package gd;

/* compiled from: TypeCapabilities.kt */
/* loaded from: classes2.dex */
public final class e1 {
    public static final n a(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        qb.a Z0 = g0Var.Z0();
        n nVar = Z0 instanceof n ? (n) Z0 : null;
        if (nVar == null || !nVar.J0()) {
            return null;
        }
        return nVar;
    }

    public static final boolean b(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        qb.a Z0 = g0Var.Z0();
        n nVar = Z0 instanceof n ? (n) Z0 : null;
        if (nVar != null) {
            return nVar.J0();
        }
        return false;
    }
}
