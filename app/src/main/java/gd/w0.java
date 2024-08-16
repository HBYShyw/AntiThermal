package gd;

/* compiled from: StubTypes.kt */
/* loaded from: classes2.dex */
public final class w0 extends e {

    /* renamed from: j, reason: collision with root package name */
    private final TypeConstructor f11897j;

    /* renamed from: k, reason: collision with root package name */
    private final zc.h f11898k;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w0(hd.n nVar, boolean z10, TypeConstructor typeConstructor) {
        super(nVar, z10);
        za.k.e(nVar, "originalTypeVariable");
        za.k.e(typeConstructor, "constructor");
        this.f11897j = typeConstructor;
        this.f11898k = nVar.t().i().u();
    }

    @Override // gd.g0
    public TypeConstructor W0() {
        return this.f11897j;
    }

    @Override // gd.e
    public e g1(boolean z10) {
        return new w0(f1(), z10, W0());
    }

    @Override // gd.o0
    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Stub (BI): ");
        sb2.append(f1());
        sb2.append(X0() ? "?" : "");
        return sb2.toString();
    }

    @Override // gd.e, gd.g0
    public zc.h u() {
        return this.f11898k;
    }
}
