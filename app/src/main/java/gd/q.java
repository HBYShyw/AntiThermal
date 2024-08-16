package gd;

/* compiled from: TypeSubstitution.kt */
/* loaded from: classes2.dex */
public class q extends n1 {

    /* renamed from: c, reason: collision with root package name */
    private final n1 f11875c;

    public q(n1 n1Var) {
        za.k.e(n1Var, "substitution");
        this.f11875c = n1Var;
    }

    @Override // gd.n1
    public boolean a() {
        return this.f11875c.a();
    }

    @Override // gd.n1
    public qb.g d(qb.g gVar) {
        za.k.e(gVar, "annotations");
        return this.f11875c.d(gVar);
    }

    @Override // gd.n1
    public TypeProjection e(g0 g0Var) {
        za.k.e(g0Var, "key");
        return this.f11875c.e(g0Var);
    }

    @Override // gd.n1
    public boolean f() {
        return this.f11875c.f();
    }

    @Override // gd.n1
    public g0 g(g0 g0Var, Variance variance) {
        za.k.e(g0Var, "topLevelType");
        za.k.e(variance, "position");
        return this.f11875c.g(g0Var, variance);
    }
}
