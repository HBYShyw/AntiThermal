package gd;

import java.util.List;

/* compiled from: KotlinType.kt */
/* loaded from: classes2.dex */
public abstract class a0 extends v1 implements kd.g {

    /* renamed from: f, reason: collision with root package name */
    private final o0 f11741f;

    /* renamed from: g, reason: collision with root package name */
    private final o0 f11742g;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a0(o0 o0Var, o0 o0Var2) {
        super(null);
        za.k.e(o0Var, "lowerBound");
        za.k.e(o0Var2, "upperBound");
        this.f11741f = o0Var;
        this.f11742g = o0Var2;
    }

    @Override // gd.g0
    public List<TypeProjection> U0() {
        return d1().U0();
    }

    @Override // gd.g0
    public c1 V0() {
        return d1().V0();
    }

    @Override // gd.g0
    public TypeConstructor W0() {
        return d1().W0();
    }

    @Override // gd.g0
    public boolean X0() {
        return d1().X0();
    }

    public abstract o0 d1();

    public final o0 e1() {
        return this.f11741f;
    }

    public final o0 f1() {
        return this.f11742g;
    }

    public abstract String g1(rc.c cVar, rc.f fVar);

    public String toString() {
        return rc.c.f17711j.w(this);
    }

    @Override // gd.g0
    public zc.h u() {
        return d1().u();
    }
}
