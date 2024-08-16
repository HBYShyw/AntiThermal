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
public final class w extends a0<Byte> {
    public w(byte b10) {
        super(Byte.valueOf(b10));
    }

    @Override // uc.g
    public g0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        ClassDescriptor a10 = findClassInModule.a(moduleDescriptor, StandardNames.a.f15338y0);
        o0 x10 = a10 != null ? a10.x() : null;
        return x10 == null ? ErrorUtils.d(ErrorTypeKind.B0, "UByte") : x10;
    }

    @Override // uc.g
    public String toString() {
        return b().intValue() + ".toUByte()";
    }
}
