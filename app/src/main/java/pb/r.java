package pb;

/* compiled from: DescriptorVisibility.kt */
/* loaded from: classes2.dex */
public abstract class r extends u {

    /* renamed from: a, reason: collision with root package name */
    private final n1 f16725a;

    public r(n1 n1Var) {
        za.k.e(n1Var, "delegate");
        this.f16725a = n1Var;
    }

    @Override // pb.u
    public n1 b() {
        return this.f16725a;
    }

    @Override // pb.u
    public String c() {
        return b().b();
    }

    @Override // pb.u
    public u f() {
        u j10 = DescriptorVisibilities.j(b().d());
        za.k.d(j10, "toDescriptorVisibility(delegate.normalize())");
        return j10;
    }
}
