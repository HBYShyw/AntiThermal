package za;

/* compiled from: PackageReference.kt */
/* renamed from: za.q, reason: use source file name */
/* loaded from: classes2.dex */
public final class PackageReference implements ClassBasedDeclarationContainer {

    /* renamed from: e, reason: collision with root package name */
    private final Class<?> f20371e;

    /* renamed from: f, reason: collision with root package name */
    private final String f20372f;

    public PackageReference(Class<?> cls, String str) {
        k.e(cls, "jClass");
        k.e(str, "moduleName");
        this.f20371e = cls;
        this.f20372f = str;
    }

    @Override // za.ClassBasedDeclarationContainer
    public Class<?> e() {
        return this.f20371e;
    }

    public boolean equals(Object obj) {
        return (obj instanceof PackageReference) && k.a(e(), ((PackageReference) obj).e());
    }

    public int hashCode() {
        return e().hashCode();
    }

    public String toString() {
        return e().toString() + " (Kotlin reflection is not available)";
    }
}
