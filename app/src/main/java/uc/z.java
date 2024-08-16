package uc;

import gd.g0;
import gd.o0;
import id.ErrorTypeKind;
import id.ErrorUtils;
import mb.StandardNames;
import pb.ClassDescriptor;
import pb.ModuleDescriptor;
import pb.findClassInModule;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class z extends a0<Short> {
    public z(short s7) {
        super(Short.valueOf(s7));
    }

    @Override // uc.g
    public g0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        ClassDescriptor a10 = findClassInModule.a(moduleDescriptor, StandardNames.a.f15340z0);
        o0 x10 = a10 != null ? a10.x() : null;
        return x10 == null ? ErrorUtils.d(ErrorTypeKind.B0, "UShort") : x10;
    }

    @Override // uc.g
    public String toString() {
        return b().intValue() + ".toUShort()";
    }
}
