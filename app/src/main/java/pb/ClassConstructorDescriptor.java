package pb;

import gd.TypeSubstitutor;

/* compiled from: ClassConstructorDescriptor.kt */
/* renamed from: pb.d, reason: use source file name */
/* loaded from: classes2.dex */
public interface ClassConstructorDescriptor extends ConstructorDescriptor {
    @Override // pb.FunctionDescriptor, pb.CallableMemberDescriptor, pb.CallableDescriptor, pb.DeclarationDescriptor
    ClassConstructorDescriptor a();

    @Override // pb.ConstructorDescriptor, pb.FunctionDescriptor, pb.Substitutable
    ClassConstructorDescriptor c(TypeSubstitutor typeSubstitutor);
}
