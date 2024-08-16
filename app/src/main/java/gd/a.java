package gd;

/* compiled from: SpecialTypes.kt */
/* loaded from: classes2.dex */
public final class a extends r {

    /* renamed from: f, reason: collision with root package name */
    private final o0 f11739f;

    /* renamed from: g, reason: collision with root package name */
    private final o0 f11740g;

    public a(o0 o0Var, o0 o0Var2) {
        za.k.e(o0Var, "delegate");
        za.k.e(o0Var2, "abbreviation");
        this.f11739f = o0Var;
        this.f11740g = o0Var2;
    }

    public final o0 e0() {
        return f1();
    }

    @Override // gd.v1
    /* renamed from: e1 */
    public o0 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return new a(f1().c1(c1Var), this.f11740g);
    }

    @Override // gd.r
    protected o0 f1() {
        return this.f11739f;
    }

    public final o0 i1() {
        return this.f11740g;
    }

    @Override // gd.o0
    /* renamed from: j1, reason: merged with bridge method [inline-methods] */
    public a a1(boolean z10) {
        return new a(f1().a1(z10), this.f11740g.a1(z10));
    }

    @Override // gd.r
    /* renamed from: k1, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public a g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        g0 a10 = gVar.a(f1());
        za.k.c(a10, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        g0 a11 = gVar.a(this.f11740g);
        za.k.c(a11, "null cannot be cast to non-null type org.jetbrains.kotlin.types.SimpleType");
        return new a((o0) a10, (o0) a11);
    }

    @Override // gd.r
    /* renamed from: l1, reason: merged with bridge method [inline-methods] */
    public a h1(o0 o0Var) {
        za.k.e(o0Var, "delegate");
        return new a(o0Var, this.f11740g);
    }
}
