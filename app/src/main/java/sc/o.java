package sc;

import pb.ModuleCapability;
import pb.ModuleDescriptor;

/* compiled from: ResolutionAnchorProvider.kt */
/* loaded from: classes2.dex */
public final class o {

    /* renamed from: a, reason: collision with root package name */
    private static final ModuleCapability<n> f18467a = new ModuleCapability<>("ResolutionAnchorProvider");

    public static final ModuleDescriptor a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "<this>");
        n nVar = (n) moduleDescriptor.k0(f18467a);
        if (nVar != null) {
            return nVar.a(moduleDescriptor);
        }
        return null;
    }
}
