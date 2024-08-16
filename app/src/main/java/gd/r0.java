package gd;

/* compiled from: TypeWithEnhancement.kt */
/* loaded from: classes2.dex */
public final class r0 extends r implements t1 {

    /* renamed from: f, reason: collision with root package name */
    private final o0 f11877f;

    /* renamed from: g, reason: collision with root package name */
    private final g0 f11878g;

    public r0(o0 o0Var, g0 g0Var) {
        za.k.e(o0Var, "delegate");
        za.k.e(g0Var, "enhancement");
        this.f11877f = o0Var;
        this.f11878g = g0Var;
    }

    @Override // gd.t1
    public g0 Q() {
        return this.f11878g;
    }

    @Override // gd.v1
    /* renamed from: d1 */
    public o0 a1(boolean z10) {
        v1 d10 = u1.d(O0().a1(z10), Q().Z0().a1(z10));
        za.k.c(d10, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        return (o0) d10;
    }

    @Override // gd.v1
    /* renamed from: e1 */
    public o0 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        v1 d10 = u1.d(O0().c1(c1Var), Q());
        za.k.c(d10, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        return (o0) d10;
    }

    @Override // gd.r
    protected o0 f1() {
        return this.f11877f;
    }

    @Override // gd.t1
    /* renamed from: i1, reason: merged with bridge method [inline-methods] */
    public o0 O0() {
        return f1();
    }

    @Override // gd.r
    /* renamed from: j1, reason: merged with bridge method [inline-methods] */
    public r0 g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        g0 a10 = gVar.a(f1());
        za.k.c(a10, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        return new r0((o0) a10, gVar.a(Q()));
    }

    @Override // gd.r
    /* renamed from: k1, reason: merged with bridge method [inline-methods] */
    public r0 h1(o0 o0Var) {
        za.k.e(o0Var, "delegate");
        return new r0(o0Var, Q());
    }

    @Override // gd.o0
    public String toString() {
        return "[@EnhancedForWarnings(" + Q() + ")] " + O0();
    }
}
