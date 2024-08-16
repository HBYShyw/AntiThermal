package uc;

import gd.o0;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class l extends g<Float> {
    public l(float f10) {
        super(Float.valueOf(f10));
    }

    @Override // uc.g
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        o0 B = moduleDescriptor.t().B();
        za.k.d(B, "module.builtIns.floatType");
        return B;
    }

    @Override // uc.g
    public String toString() {
        return b().floatValue() + ".toFloat()";
    }
}
