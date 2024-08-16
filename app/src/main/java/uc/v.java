package uc;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gd.o0;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class v extends g<String> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public v(String str) {
        super(str);
        za.k.e(str, ThermalBaseConfig.Item.ATTR_VALUE);
    }

    @Override // uc.g
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        o0 W = moduleDescriptor.t().W();
        za.k.d(W, "module.builtIns.stringType");
        return W;
    }

    @Override // uc.g
    public String toString() {
        return '\"' + b() + '\"';
    }
}
