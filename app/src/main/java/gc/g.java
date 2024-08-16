package gc;

import gd.a0;
import gd.c1;
import gd.g0;
import gd.h0;
import gd.l0;
import gd.o0;
import gd.s1;
import gd.u1;
import gd.v1;

/* compiled from: typeEnhancement.kt */
/* loaded from: classes2.dex */
public final class g extends gd.r implements l0 {

    /* renamed from: f, reason: collision with root package name */
    private final o0 f11666f;

    public g(o0 o0Var) {
        za.k.e(o0Var, "delegate");
        this.f11666f = o0Var;
    }

    private final o0 i1(o0 o0Var) {
        o0 a12 = o0Var.a1(false);
        return !ld.a.r(o0Var) ? a12 : new g(a12);
    }

    @Override // gd.n
    public boolean J0() {
        return true;
    }

    @Override // gd.n
    public g0 P(g0 g0Var) {
        za.k.e(g0Var, "replacement");
        v1 Z0 = g0Var.Z0();
        if (!ld.a.r(Z0) && !s1.l(Z0)) {
            return Z0;
        }
        if (Z0 instanceof o0) {
            return i1((o0) Z0);
        }
        if (Z0 instanceof a0) {
            a0 a0Var = (a0) Z0;
            return u1.d(h0.d(i1(a0Var.e1()), i1(a0Var.f1())), u1.a(Z0));
        }
        throw new IllegalStateException(("Incorrect type: " + Z0).toString());
    }

    @Override // gd.r, gd.g0
    public boolean X0() {
        return false;
    }

    @Override // gd.v1
    /* renamed from: d1 */
    public o0 a1(boolean z10) {
        return z10 ? f1().a1(true) : this;
    }

    @Override // gd.r
    protected o0 f1() {
        return this.f11666f;
    }

    @Override // gd.o0
    /* renamed from: j1, reason: merged with bridge method [inline-methods] */
    public g c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return new g(f1().c1(c1Var));
    }

    @Override // gd.r
    /* renamed from: k1, reason: merged with bridge method [inline-methods] */
    public g h1(o0 o0Var) {
        za.k.e(o0Var, "delegate");
        return new g(o0Var);
    }
}
