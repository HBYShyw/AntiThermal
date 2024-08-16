package pb;

/* compiled from: Visibility.kt */
/* loaded from: classes2.dex */
public abstract class n1 {

    /* renamed from: a, reason: collision with root package name */
    private final String f16723a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f16724b;

    /* JADX INFO: Access modifiers changed from: protected */
    public n1(String str, boolean z10) {
        za.k.e(str, "name");
        this.f16723a = str;
        this.f16724b = z10;
    }

    public Integer a(n1 n1Var) {
        za.k.e(n1Var, "visibility");
        return Visibilities.f16708a.a(this, n1Var);
    }

    public String b() {
        return this.f16723a;
    }

    public final boolean c() {
        return this.f16724b;
    }

    public n1 d() {
        return this;
    }

    public final String toString() {
        return b();
    }
}
