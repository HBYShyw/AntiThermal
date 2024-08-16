package uc;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gd.g0;
import java.util.List;
import mb.KotlinBuiltIns;
import pb.ModuleDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public class b extends g<List<? extends g<?>>> {

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<ModuleDescriptor, g0> f18987b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public b(List<? extends g<?>> list, ya.l<? super ModuleDescriptor, ? extends g0> lVar) {
        super(list);
        za.k.e(list, ThermalBaseConfig.Item.ATTR_VALUE);
        za.k.e(lVar, "computeType");
        this.f18987b = lVar;
    }

    @Override // uc.g
    public g0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        g0 invoke = this.f18987b.invoke(moduleDescriptor);
        if (!KotlinBuiltIns.c0(invoke) && !KotlinBuiltIns.p0(invoke)) {
            KotlinBuiltIns.C0(invoke);
        }
        return invoke;
    }
}
