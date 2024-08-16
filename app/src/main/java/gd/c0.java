package gd;

/* compiled from: TypeWithEnhancement.kt */
/* loaded from: classes2.dex */
public final class c0 extends a0 implements t1 {

    /* renamed from: h, reason: collision with root package name */
    private final a0 f11747h;

    /* renamed from: i, reason: collision with root package name */
    private final g0 f11748i;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c0(a0 a0Var, g0 g0Var) {
        super(a0Var.e1(), a0Var.f1());
        za.k.e(a0Var, "origin");
        za.k.e(g0Var, "enhancement");
        this.f11747h = a0Var;
        this.f11748i = g0Var;
    }

    @Override // gd.t1
    public g0 Q() {
        return this.f11748i;
    }

    @Override // gd.v1
    public v1 a1(boolean z10) {
        return u1.d(O0().a1(z10), Q().Z0().a1(z10));
    }

    @Override // gd.v1
    public v1 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return u1.d(O0().c1(c1Var), Q());
    }

    @Override // gd.a0
    public o0 d1() {
        return O0().d1();
    }

    @Override // gd.a0
    public String g1(rc.c cVar, rc.f fVar) {
        za.k.e(cVar, "renderer");
        za.k.e(fVar, "options");
        if (fVar.f()) {
            return cVar.w(Q());
        }
        return O0().g1(cVar, fVar);
    }

    @Override // gd.t1
    /* renamed from: h1, reason: merged with bridge method [inline-methods] */
    public a0 O0() {
        return this.f11747h;
    }

    @Override // gd.v1
    /* renamed from: i1, reason: merged with bridge method [inline-methods] */
    public c0 g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        g0 a10 = gVar.a(O0());
        za.k.c(a10, "null cannot be cast to non-null type org.jetbrains.kotlin.types.FlexibleType");
        return new c0((a0) a10, gVar.a(Q()));
    }

    @Override // gd.a0
    public String toString() {
        return "[@EnhancedForWarnings(" + Q() + ")] " + O0();
    }
}
