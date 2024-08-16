package gd;

import java.util.List;

/* compiled from: SpecialTypes.kt */
/* loaded from: classes2.dex */
public abstract class r extends o0 {
    @Override // gd.g0
    public List<TypeProjection> U0() {
        return f1().U0();
    }

    @Override // gd.g0
    public c1 V0() {
        return f1().V0();
    }

    @Override // gd.g0
    public TypeConstructor W0() {
        return f1().W0();
    }

    @Override // gd.g0
    public boolean X0() {
        return f1().X0();
    }

    protected abstract o0 f1();

    @Override // gd.v1
    public o0 g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        g0 a10 = gVar.a(f1());
        za.k.c(a10, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        return h1((o0) a10);
    }

    public abstract r h1(o0 o0Var);

    @Override // gd.g0
    public zc.h u() {
        return f1().u();
    }
}
