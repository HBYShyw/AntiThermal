package uc;

import gd.o0;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class u extends o<Short> {
    public u(short s7) {
        super(Short.valueOf(s7));
    }

    @Override // uc.g
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        o0 T = moduleDescriptor.t().T();
        za.k.d(T, "module.builtIns.shortType");
        return T;
    }

    @Override // uc.g
    public String toString() {
        return b().intValue() + ".toShort()";
    }
}
