package pb;

import gd.TypeSubstitutor;
import java.util.List;

/* compiled from: ConstructorDescriptor.java */
/* renamed from: pb.l, reason: use source file name */
/* loaded from: classes2.dex */
public interface ConstructorDescriptor extends FunctionDescriptor {
    boolean J();

    ClassDescriptor K();

    @Override // pb.FunctionDescriptor, pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    ClassifierDescriptorWithTypeParameters b();

    @Override // pb.FunctionDescriptor, pb.Substitutable
    ConstructorDescriptor c(TypeSubstitutor typeSubstitutor);

    @Override // pb.CallableDescriptor
    gd.g0 f();

    @Override // pb.CallableDescriptor
    List<TypeParameterDescriptor> m();
}
