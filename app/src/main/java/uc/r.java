package uc;

import gd.o0;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class r extends o<Long> {
    public r(long j10) {
        super(Long.valueOf(j10));
    }

    @Override // uc.g
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        o0 F = moduleDescriptor.t().F();
        za.k.d(F, "module.builtIns.longType");
        return F;
    }

    @Override // uc.g
    public String toString() {
        return b().longValue() + ".toLong()";
    }
}
