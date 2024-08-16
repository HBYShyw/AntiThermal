package gd;

/* compiled from: KotlinTypeFactory.kt */
/* loaded from: classes2.dex */
public abstract class s extends r {

    /* renamed from: f, reason: collision with root package name */
    private final o0 f11882f;

    public s(o0 o0Var) {
        za.k.e(o0Var, "delegate");
        this.f11882f = o0Var;
    }

    @Override // gd.v1
    /* renamed from: d1 */
    public o0 a1(boolean z10) {
        return z10 == X0() ? this : f1().a1(z10).c1(V0());
    }

    @Override // gd.v1
    /* renamed from: e1 */
    public o0 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return c1Var != V0() ? new q0(this, c1Var) : this;
    }

    @Override // gd.r
    protected o0 f1() {
        return this.f11882f;
    }
}
