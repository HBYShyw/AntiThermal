package hc;

import cc.LazyJavaPackageFragment;
import pb.SourceElement;
import pb.b1;

/* compiled from: KotlinJvmBinaryPackageSourceElement.kt */
/* renamed from: hc.s, reason: use source file name */
/* loaded from: classes2.dex */
public final class KotlinJvmBinaryPackageSourceElement implements SourceElement {

    /* renamed from: b, reason: collision with root package name */
    private final LazyJavaPackageFragment f12201b;

    public KotlinJvmBinaryPackageSourceElement(LazyJavaPackageFragment lazyJavaPackageFragment) {
        za.k.e(lazyJavaPackageFragment, "packageFragment");
        this.f12201b = lazyJavaPackageFragment;
    }

    @Override // pb.SourceElement
    public b1 a() {
        b1 b1Var = b1.f16671a;
        za.k.d(b1Var, "NO_SOURCE_FILE");
        return b1Var;
    }

    public String toString() {
        return this.f12201b + ": " + this.f12201b.V0().keySet();
    }
}
