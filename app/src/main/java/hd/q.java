package hd;

import gd.f1;
import hd.b;
import java.util.Collection;
import java.util.List;
import mb.PrimitiveType;
import oc.FqName;
import oc.FqNameUnsafe;
import za.Reflection;

/* compiled from: NewKotlinTypeChecker.kt */
/* loaded from: classes2.dex */
public final class q implements b {

    /* renamed from: a, reason: collision with root package name */
    public static final q f12241a = new q();

    private q() {
    }

    @Override // gd.TypeSystemCommonBackendContext
    public kd.i A(kd.i iVar) {
        return b.a.y(this, iVar);
    }

    @Override // kd.p
    public boolean A0(kd.n nVar) {
        return b.a.N(this, nVar);
    }

    @Override // kd.p
    public boolean B(kd.k kVar) {
        return b.a.O(this, kVar);
    }

    @Override // kd.p
    public List<kd.o> B0(kd.n nVar) {
        return b.a.r(this, nVar);
    }

    @Override // kd.p
    public boolean C(kd.d dVar) {
        return b.a.U(this, dVar);
    }

    @Override // kd.p
    public kd.m C0(kd.k kVar, int i10) {
        za.k.e(kVar, "<this>");
        boolean z10 = false;
        if (i10 >= 0 && i10 < c0(kVar)) {
            z10 = true;
        }
        if (z10) {
            return S(kVar, i10);
        }
        return null;
    }

    @Override // gd.TypeSystemCommonBackendContext
    public boolean D(kd.n nVar) {
        return b.a.b0(this, nVar);
    }

    @Override // kd.p
    public kd.m D0(kd.i iVar) {
        return b.a.j(this, iVar);
    }

    @Override // kd.p
    public boolean E(kd.o oVar, kd.n nVar) {
        return b.a.D(this, oVar, nVar);
    }

    @Override // kd.p
    public kd.j E0(kd.g gVar) {
        return b.a.h(this, gVar);
    }

    @Override // kd.p
    public kd.n F(kd.i iVar) {
        za.k.e(iVar, "<this>");
        kd.k c10 = c(iVar);
        if (c10 == null) {
            c10 = v0(iVar);
        }
        return b(c10);
    }

    @Override // kd.p
    public boolean F0(kd.k kVar) {
        return b.a.Y(this, kVar);
    }

    @Override // kd.p
    public kd.i G(List<? extends kd.i> list) {
        return b.a.F(this, list);
    }

    public f1 G0(boolean z10, boolean z11) {
        return b.a.f0(this, z10, z11);
    }

    @Override // kd.p
    public boolean H(kd.i iVar) {
        return b.a.P(this, iVar);
    }

    @Override // kd.p
    public kd.l I(kd.k kVar) {
        return b.a.c(this, kVar);
    }

    @Override // kd.p
    public kd.k J(kd.e eVar) {
        return b.a.g0(this, eVar);
    }

    @Override // kd.p
    public kd.f K(kd.g gVar) {
        return b.a.f(this, gVar);
    }

    @Override // gd.TypeSystemCommonBackendContext
    public boolean L(kd.n nVar) {
        return b.a.L(this, nVar);
    }

    @Override // hd.b
    public kd.i M(kd.k kVar, kd.k kVar2) {
        return b.a.m(this, kVar, kVar2);
    }

    @Override // kd.p
    public kd.m N(kd.l lVar, int i10) {
        za.k.e(lVar, "<this>");
        if (lVar instanceof kd.k) {
            return S((kd.i) lVar, i10);
        }
        if (lVar instanceof kd.a) {
            kd.m mVar = ((kd.a) lVar).get(i10);
            za.k.d(mVar, "get(index)");
            return mVar;
        }
        throw new IllegalStateException(("unknown type argument list type: " + lVar + ", " + Reflection.b(lVar.getClass())).toString());
    }

    @Override // kd.p
    public kd.k O(kd.k kVar) {
        kd.k J;
        za.k.e(kVar, "<this>");
        kd.e u7 = u(kVar);
        return (u7 == null || (J = J(u7)) == null) ? kVar : J;
    }

    @Override // kd.p
    public boolean P(kd.n nVar) {
        return b.a.G(this, nVar);
    }

    @Override // kd.p
    public Collection<kd.i> Q(kd.n nVar) {
        return b.a.l0(this, nVar);
    }

    @Override // kd.p
    public kd.u R(kd.m mVar) {
        return b.a.A(this, mVar);
    }

    @Override // kd.p
    public kd.m S(kd.i iVar, int i10) {
        return b.a.n(this, iVar, i10);
    }

    @Override // kd.p
    public kd.o T(kd.n nVar) {
        return b.a.x(this, nVar);
    }

    @Override // kd.p
    public boolean U(kd.n nVar) {
        return b.a.J(this, nVar);
    }

    @Override // kd.p
    public boolean V(kd.n nVar) {
        return b.a.I(this, nVar);
    }

    @Override // gd.TypeSystemCommonBackendContext
    public boolean W(kd.i iVar, FqName fqName) {
        return b.a.C(this, iVar, fqName);
    }

    @Override // kd.p
    public kd.i X(kd.m mVar) {
        return b.a.v(this, mVar);
    }

    @Override // kd.p
    public kd.o Y(kd.t tVar) {
        return b.a.w(this, tVar);
    }

    @Override // kd.p
    public boolean Z(kd.k kVar) {
        za.k.e(kVar, "<this>");
        return e0(b(kVar));
    }

    @Override // hd.b, kd.p
    public kd.k a(kd.g gVar) {
        return b.a.o0(this, gVar);
    }

    @Override // kd.p
    public kd.k a0(kd.k kVar, kd.b bVar) {
        return b.a.k(this, kVar, bVar);
    }

    @Override // hd.b, kd.p
    public kd.n b(kd.k kVar) {
        return b.a.n0(this, kVar);
    }

    @Override // kd.p
    public boolean b0(kd.i iVar) {
        za.k.e(iVar, "<this>");
        return B(v0(iVar)) != B(s0(iVar));
    }

    @Override // hd.b, kd.p
    public kd.k c(kd.i iVar) {
        return b.a.i(this, iVar);
    }

    @Override // kd.p
    public int c0(kd.i iVar) {
        return b.a.b(this, iVar);
    }

    @Override // hd.b, kd.p
    public kd.k d(kd.g gVar) {
        return b.a.c0(this, gVar);
    }

    @Override // kd.p
    public boolean d0(kd.m mVar) {
        return b.a.X(this, mVar);
    }

    @Override // hd.b, kd.p
    public kd.k e(kd.k kVar, boolean z10) {
        return b.a.q0(this, kVar, z10);
    }

    @Override // kd.p
    public boolean e0(kd.n nVar) {
        return b.a.H(this, nVar);
    }

    @Override // hd.b, kd.p
    public boolean f(kd.k kVar) {
        return b.a.V(this, kVar);
    }

    @Override // kd.p
    public kd.c f0(kd.d dVar) {
        return b.a.m0(this, dVar);
    }

    @Override // hd.b, kd.p
    public kd.d g(kd.k kVar) {
        return b.a.d(this, kVar);
    }

    @Override // gd.TypeSystemCommonBackendContext
    public FqNameUnsafe g0(kd.n nVar) {
        return b.a.p(this, nVar);
    }

    @Override // kd.p
    public List<kd.m> h(kd.i iVar) {
        return b.a.o(this, iVar);
    }

    @Override // kd.p
    public boolean h0(kd.i iVar) {
        return b.a.R(this, iVar);
    }

    @Override // gd.TypeSystemCommonBackendContext
    public PrimitiveType i(kd.n nVar) {
        return b.a.t(this, nVar);
    }

    @Override // kd.p
    public boolean i0(kd.i iVar) {
        za.k.e(iVar, "<this>");
        kd.k c10 = c(iVar);
        return (c10 != null ? u(c10) : null) != null;
    }

    @Override // kd.p
    public boolean j(kd.i iVar) {
        za.k.e(iVar, "<this>");
        return y0(F(iVar)) && !h0(iVar);
    }

    @Override // kd.p
    public boolean j0(kd.d dVar) {
        return b.a.S(this, dVar);
    }

    @Override // kd.p
    public kd.i k(kd.i iVar, boolean z10) {
        return b.a.p0(this, iVar, z10);
    }

    @Override // kd.p
    public kd.o k0(kd.n nVar, int i10) {
        return b.a.q(this, nVar, i10);
    }

    @Override // kd.p
    public List<kd.i> l(kd.o oVar) {
        return b.a.z(this, oVar);
    }

    @Override // kd.p
    public boolean l0(kd.k kVar) {
        return b.a.Z(this, kVar);
    }

    @Override // kd.p
    public kd.i m(kd.i iVar) {
        return b.a.e0(this, iVar);
    }

    @Override // kd.p
    public kd.b m0(kd.d dVar) {
        return b.a.l(this, dVar);
    }

    @Override // kd.p
    public kd.m n(kd.c cVar) {
        return b.a.j0(this, cVar);
    }

    @Override // kd.p
    public int n0(kd.n nVar) {
        return b.a.h0(this, nVar);
    }

    @Override // gd.TypeSystemCommonBackendContext
    public PrimitiveType o(kd.n nVar) {
        return b.a.s(this, nVar);
    }

    @Override // kd.p
    public boolean o0(kd.k kVar) {
        za.k.e(kVar, "<this>");
        return z0(b(kVar));
    }

    @Override // kd.p
    public boolean p(kd.k kVar) {
        return b.a.T(this, kVar);
    }

    @Override // gd.TypeSystemCommonBackendContext
    public kd.i p0(kd.o oVar) {
        return b.a.u(this, oVar);
    }

    @Override // kd.p
    public List<kd.k> q(kd.k kVar, kd.n nVar) {
        za.k.e(kVar, "<this>");
        za.k.e(nVar, "constructor");
        return null;
    }

    @Override // kd.p
    public boolean q0(kd.i iVar) {
        za.k.e(iVar, "<this>");
        kd.k c10 = c(iVar);
        return (c10 != null ? g(c10) : null) != null;
    }

    @Override // gd.TypeSystemCommonBackendContext
    public kd.i r(kd.i iVar) {
        kd.k e10;
        za.k.e(iVar, "<this>");
        kd.k c10 = c(iVar);
        return (c10 == null || (e10 = e(c10, true)) == null) ? iVar : e10;
    }

    @Override // kd.p
    public kd.i r0(kd.d dVar) {
        return b.a.d0(this, dVar);
    }

    @Override // kd.s
    public boolean s(kd.k kVar, kd.k kVar2) {
        return b.a.E(this, kVar, kVar2);
    }

    @Override // kd.p
    public kd.k s0(kd.i iVar) {
        kd.k a10;
        za.k.e(iVar, "<this>");
        kd.g z10 = z(iVar);
        if (z10 != null && (a10 = a(z10)) != null) {
            return a10;
        }
        kd.k c10 = c(iVar);
        za.k.b(c10);
        return c10;
    }

    @Override // kd.p
    public int t(kd.l lVar) {
        za.k.e(lVar, "<this>");
        if (lVar instanceof kd.k) {
            return c0((kd.i) lVar);
        }
        if (lVar instanceof kd.a) {
            return ((kd.a) lVar).size();
        }
        throw new IllegalStateException(("unknown type argument list type: " + lVar + ", " + Reflection.b(lVar.getClass())).toString());
    }

    @Override // kd.p
    public f1.c t0(kd.k kVar) {
        return b.a.k0(this, kVar);
    }

    @Override // kd.p
    public kd.e u(kd.k kVar) {
        return b.a.e(this, kVar);
    }

    @Override // kd.p
    public boolean u0(kd.i iVar) {
        return b.a.a0(this, iVar);
    }

    @Override // kd.p
    public boolean v(kd.i iVar) {
        return b.a.K(this, iVar);
    }

    @Override // kd.p
    public kd.k v0(kd.i iVar) {
        kd.k d10;
        za.k.e(iVar, "<this>");
        kd.g z10 = z(iVar);
        if (z10 != null && (d10 = d(z10)) != null) {
            return d10;
        }
        kd.k c10 = c(iVar);
        za.k.b(c10);
        return c10;
    }

    @Override // kd.p
    public boolean w(kd.i iVar) {
        za.k.e(iVar, "<this>");
        return (iVar instanceof kd.k) && B((kd.k) iVar);
    }

    @Override // kd.p
    public boolean w0(kd.i iVar) {
        za.k.e(iVar, "<this>");
        kd.g z10 = z(iVar);
        return (z10 != null ? K(z10) : null) != null;
    }

    @Override // kd.p
    public kd.u x(kd.o oVar) {
        return b.a.B(this, oVar);
    }

    @Override // kd.p
    public Collection<kd.i> x0(kd.k kVar) {
        return b.a.i0(this, kVar);
    }

    @Override // kd.p
    public boolean y(kd.n nVar, kd.n nVar2) {
        return b.a.a(this, nVar, nVar2);
    }

    @Override // kd.p
    public boolean y0(kd.n nVar) {
        return b.a.Q(this, nVar);
    }

    @Override // kd.p
    public kd.g z(kd.i iVar) {
        return b.a.g(this, iVar);
    }

    @Override // kd.p
    public boolean z0(kd.n nVar) {
        return b.a.M(this, nVar);
    }
}
