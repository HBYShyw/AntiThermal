package bc;

import pb.ClassDescriptor;
import xc.JavaDescriptorResolver;

/* compiled from: ModuleClassResolver.kt */
/* loaded from: classes2.dex */
public final class j implements i {

    /* renamed from: a, reason: collision with root package name */
    public JavaDescriptorResolver f4705a;

    @Override // bc.i
    public ClassDescriptor a(fc.g gVar) {
        za.k.e(gVar, "javaClass");
        return b().b(gVar);
    }

    public final JavaDescriptorResolver b() {
        JavaDescriptorResolver javaDescriptorResolver = this.f4705a;
        if (javaDescriptorResolver != null) {
            return javaDescriptorResolver;
        }
        za.k.s("resolver");
        return null;
    }

    public final void c(JavaDescriptorResolver javaDescriptorResolver) {
        za.k.e(javaDescriptorResolver, "<set-?>");
        this.f4705a = javaDescriptorResolver;
    }
}
