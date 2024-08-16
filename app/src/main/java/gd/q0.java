package gd;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: KotlinTypeFactory.kt */
/* loaded from: classes2.dex */
public final class q0 extends s {

    /* renamed from: g, reason: collision with root package name */
    private final c1 f11876g;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public q0(o0 o0Var, c1 c1Var) {
        super(o0Var);
        za.k.e(o0Var, "delegate");
        za.k.e(c1Var, "attributes");
        this.f11876g = c1Var;
    }

    @Override // gd.r, gd.g0
    public c1 V0() {
        return this.f11876g;
    }

    @Override // gd.r
    /* renamed from: i1, reason: merged with bridge method [inline-methods] */
    public q0 h1(o0 o0Var) {
        za.k.e(o0Var, "delegate");
        return new q0(o0Var, V0());
    }
}
