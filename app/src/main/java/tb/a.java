package tb;

import pb.Visibilities;
import pb.n1;
import za.k;

/* compiled from: JavaVisibilities.kt */
/* loaded from: classes2.dex */
public final class a extends n1 {

    /* renamed from: c, reason: collision with root package name */
    public static final a f18704c = new a();

    private a() {
        super("package", false);
    }

    @Override // pb.n1
    public Integer a(n1 n1Var) {
        k.e(n1Var, "visibility");
        if (this == n1Var) {
            return 0;
        }
        return Visibilities.f16708a.b(n1Var) ? 1 : -1;
    }

    @Override // pb.n1
    public String b() {
        return "public/*package*/";
    }

    @Override // pb.n1
    public n1 d() {
        return Visibilities.g.f16717c;
    }
}
