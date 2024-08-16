package uc;

import gd.o0;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class i extends g<Double> {
    public i(double d10) {
        super(Double.valueOf(d10));
    }

    @Override // uc.g
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        o0 z10 = moduleDescriptor.t().z();
        za.k.d(z10, "module.builtIns.doubleType");
        return z10;
    }

    @Override // uc.g
    public String toString() {
        return b().doubleValue() + ".toDouble()";
    }
}
