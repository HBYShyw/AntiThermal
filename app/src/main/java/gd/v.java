package gd;

import mb.KotlinBuiltIns;

/* compiled from: dynamicTypes.kt */
/* loaded from: classes2.dex */
public final class v extends a0 implements kd.f {

    /* renamed from: h, reason: collision with root package name */
    private final c1 f11895h;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public v(KotlinBuiltIns kotlinBuiltIns, c1 c1Var) {
        super(r0, r3);
        za.k.e(kotlinBuiltIns, "builtIns");
        za.k.e(c1Var, "attributes");
        o0 H = kotlinBuiltIns.H();
        za.k.d(H, "builtIns.nothingType");
        o0 I = kotlinBuiltIns.I();
        za.k.d(I, "builtIns.nullableAnyType");
        this.f11895h = c1Var;
    }

    @Override // gd.a0, gd.g0
    public c1 V0() {
        return this.f11895h;
    }

    @Override // gd.a0, gd.g0
    public boolean X0() {
        return false;
    }

    @Override // gd.a0
    public o0 d1() {
        return f1();
    }

    @Override // gd.a0
    public String g1(rc.c cVar, rc.f fVar) {
        za.k.e(cVar, "renderer");
        za.k.e(fVar, "options");
        return "dynamic";
    }

    @Override // gd.v1
    /* renamed from: h1, reason: merged with bridge method [inline-methods] */
    public v a1(boolean z10) {
        return this;
    }

    @Override // gd.v1
    /* renamed from: i1, reason: merged with bridge method [inline-methods] */
    public v g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this;
    }

    @Override // gd.v1
    /* renamed from: j1, reason: merged with bridge method [inline-methods] */
    public v c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return new v(ld.a.i(d1()), c1Var);
    }
}
