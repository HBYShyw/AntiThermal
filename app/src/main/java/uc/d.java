package uc;

import gd.o0;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class d extends o<Byte> {
    public d(byte b10) {
        super(Byte.valueOf(b10));
    }

    @Override // uc.g
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        o0 t7 = moduleDescriptor.t().t();
        za.k.d(t7, "module.builtIns.byteType");
        return t7;
    }

    @Override // uc.g
    public String toString() {
        return b().intValue() + ".toByte()";
    }
}
