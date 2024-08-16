package pb;

import gd.TypeSubstitutor;
import java.util.Collection;
import java.util.List;

/* compiled from: PropertyDescriptor.java */
/* renamed from: pb.u0, reason: use source file name */
/* loaded from: classes2.dex */
public interface PropertyDescriptor extends CallableMemberDescriptor, VariableDescriptorWithAccessors {
    List<PropertyAccessorDescriptor> C();

    @Override // pb.CallableMemberDescriptor, pb.CallableDescriptor, pb.DeclarationDescriptor
    PropertyDescriptor a();

    PropertyDescriptor c(TypeSubstitutor typeSubstitutor);

    @Override // pb.CallableMemberDescriptor, pb.CallableDescriptor
    Collection<? extends PropertyDescriptor> e();

    PropertyGetterDescriptor h();

    PropertySetterDescriptor k();

    w s0();

    w v0();
}
