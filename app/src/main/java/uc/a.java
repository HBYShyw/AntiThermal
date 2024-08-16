package uc;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gd.g0;
import pb.ModuleDescriptor;
import qb.AnnotationDescriptor;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class a extends g<AnnotationDescriptor> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(AnnotationDescriptor annotationDescriptor) {
        super(annotationDescriptor);
        za.k.e(annotationDescriptor, ThermalBaseConfig.Item.ATTR_VALUE);
    }

    @Override // uc.g
    public g0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        return b().getType();
    }
}
