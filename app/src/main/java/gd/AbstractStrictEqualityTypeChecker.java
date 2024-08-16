package gd;

/* compiled from: AbstractStrictEqualityTypeChecker.kt */
/* renamed from: gd.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class AbstractStrictEqualityTypeChecker {

    /* renamed from: a, reason: collision with root package name */
    public static final AbstractStrictEqualityTypeChecker f11751a = new AbstractStrictEqualityTypeChecker();

    private AbstractStrictEqualityTypeChecker() {
    }

    private final boolean a(kd.p pVar, kd.k kVar, kd.k kVar2) {
        if (pVar.c0(kVar) == pVar.c0(kVar2) && pVar.B(kVar) == pVar.B(kVar2)) {
            if ((pVar.u(kVar) == null) == (pVar.u(kVar2) == null) && pVar.y(pVar.b(kVar), pVar.b(kVar2))) {
                if (pVar.s(kVar, kVar2)) {
                    return true;
                }
                int c02 = pVar.c0(kVar);
                for (int i10 = 0; i10 < c02; i10++) {
                    kd.m S = pVar.S(kVar, i10);
                    kd.m S2 = pVar.S(kVar2, i10);
                    if (pVar.d0(S) != pVar.d0(S2)) {
                        return false;
                    }
                    if (!pVar.d0(S) && (pVar.R(S) != pVar.R(S2) || !c(pVar, pVar.X(S), pVar.X(S2)))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private final boolean c(kd.p pVar, kd.i iVar, kd.i iVar2) {
        if (iVar == iVar2) {
            return true;
        }
        kd.k c10 = pVar.c(iVar);
        kd.k c11 = pVar.c(iVar2);
        if (c10 != null && c11 != null) {
            return a(pVar, c10, c11);
        }
        kd.g z10 = pVar.z(iVar);
        kd.g z11 = pVar.z(iVar2);
        if (z10 == null || z11 == null) {
            return false;
        }
        return a(pVar, pVar.d(z10), pVar.d(z11)) && a(pVar, pVar.a(z10), pVar.a(z11));
    }

    public final boolean b(kd.p pVar, kd.i iVar, kd.i iVar2) {
        za.k.e(pVar, "context");
        za.k.e(iVar, "a");
        za.k.e(iVar2, "b");
        return c(pVar, iVar, iVar2);
    }
}
