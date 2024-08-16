package yb;

import cc.JavaDescriptorUtil;
import pb.CallableDescriptor;
import pb.ClassDescriptor;
import pb.PropertyDescriptor;
import sc.ExternalOverridabilityCondition;

/* compiled from: FieldOverridabilityCondition.kt */
/* renamed from: yb.n, reason: use source file name */
/* loaded from: classes2.dex */
public final class FieldOverridabilityCondition implements ExternalOverridabilityCondition {
    @Override // sc.ExternalOverridabilityCondition
    public ExternalOverridabilityCondition.a a() {
        return ExternalOverridabilityCondition.a.BOTH;
    }

    @Override // sc.ExternalOverridabilityCondition
    public ExternalOverridabilityCondition.b b(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, ClassDescriptor classDescriptor) {
        za.k.e(callableDescriptor, "superDescriptor");
        za.k.e(callableDescriptor2, "subDescriptor");
        if ((callableDescriptor2 instanceof PropertyDescriptor) && (callableDescriptor instanceof PropertyDescriptor)) {
            PropertyDescriptor propertyDescriptor = (PropertyDescriptor) callableDescriptor2;
            PropertyDescriptor propertyDescriptor2 = (PropertyDescriptor) callableDescriptor;
            if (!za.k.a(propertyDescriptor.getName(), propertyDescriptor2.getName())) {
                return ExternalOverridabilityCondition.b.UNKNOWN;
            }
            if (JavaDescriptorUtil.a(propertyDescriptor) && JavaDescriptorUtil.a(propertyDescriptor2)) {
                return ExternalOverridabilityCondition.b.OVERRIDABLE;
            }
            if (!JavaDescriptorUtil.a(propertyDescriptor) && !JavaDescriptorUtil.a(propertyDescriptor2)) {
                return ExternalOverridabilityCondition.b.UNKNOWN;
            }
            return ExternalOverridabilityCondition.b.INCOMPATIBLE;
        }
        return ExternalOverridabilityCondition.b.UNKNOWN;
    }
}
