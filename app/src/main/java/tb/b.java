package tb;

import pb.Visibilities;
import pb.n1;
import za.k;

/* compiled from: JavaVisibilities.kt */
/* loaded from: classes2.dex */
public final class b extends n1 {

    /* renamed from: c, reason: collision with root package name */
    public static final b f18705c = new b();

    private b() {
        super("protected_and_package", true);
    }

    @Override // pb.n1
    public Integer a(n1 n1Var) {
        k.e(n1Var, "visibility");
        if (k.a(this, n1Var)) {
            return 0;
        }
        if (n1Var == Visibilities.b.f16712c) {
            return null;
        }
        return Integer.valueOf(Visibilities.f16708a.b(n1Var) ? 1 : -1);
    }

    @Override // pb.n1
    public String b() {
        return "protected/*protected and package*/";
    }

    @Override // pb.n1
    public n1 d() {
        return Visibilities.g.f16717c;
    }
}
