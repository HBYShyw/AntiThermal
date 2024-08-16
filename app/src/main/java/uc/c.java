package uc;

import gd.o0;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class c extends g<Boolean> {
    public c(boolean z10) {
        super(Boolean.valueOf(z10));
    }

    @Override // uc.g
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        o0 n10 = moduleDescriptor.t().n();
        za.k.d(n10, "module.builtIns.booleanType");
        return n10;
    }
}
