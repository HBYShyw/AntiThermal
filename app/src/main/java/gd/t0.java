package gd;

import mb.KotlinBuiltIns;

/* compiled from: StarProjectionImpl.kt */
/* loaded from: classes2.dex */
public final class t0 extends TypeProjectionBase {

    /* renamed from: a, reason: collision with root package name */
    private final g0 f11888a;

    public t0(KotlinBuiltIns kotlinBuiltIns) {
        za.k.e(kotlinBuiltIns, "kotlinBuiltIns");
        o0 I = kotlinBuiltIns.I();
        za.k.d(I, "kotlinBuiltIns.nullableAnyType");
        this.f11888a = I;
    }

    @Override // gd.TypeProjection
    public Variance a() {
        return Variance.OUT_VARIANCE;
    }

    @Override // gd.TypeProjection
    public boolean b() {
        return true;
    }

    @Override // gd.TypeProjection
    public g0 getType() {
        return this.f11888a;
    }

    @Override // gd.TypeProjection
    public TypeProjection u(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this;
    }
}
