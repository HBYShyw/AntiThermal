package pb;

import ma.Unit;

/* compiled from: InvalidModuleException.kt */
/* loaded from: classes2.dex */
public final class b0 {

    /* renamed from: a, reason: collision with root package name */
    private static final ModuleCapability<c0> f16670a = new ModuleCapability<>("InvalidModuleNotifier");

    public static final void a(ModuleDescriptor moduleDescriptor) {
        Unit unit;
        za.k.e(moduleDescriptor, "<this>");
        c0 c0Var = (c0) moduleDescriptor.k0(f16670a);
        if (c0Var != null) {
            c0Var.a(moduleDescriptor);
            unit = Unit.f15173a;
        } else {
            unit = null;
        }
        if (unit != null) {
            return;
        }
        throw new a0("Accessing invalid module descriptor " + moduleDescriptor);
    }
}
