package uc;

import gd.o0;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class m extends o<Integer> {
    public m(int i10) {
        super(Integer.valueOf(i10));
    }

    @Override // uc.g
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        o0 D = moduleDescriptor.t().D();
        za.k.d(D, "module.builtIns.intType");
        return D;
    }
}
