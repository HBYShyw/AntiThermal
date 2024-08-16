package pb;

import java.util.Collection;
import java.util.List;

/* compiled from: ClassDescriptor.java */
/* renamed from: pb.e, reason: use source file name */
/* loaded from: classes2.dex */
public interface ClassDescriptor extends ClassOrPackageFragmentDescriptor, ClassifierDescriptorWithTypeParameters {
    List<TypeParameterDescriptor> B();

    boolean F();

    zc.h F0();

    ValueClassRepresentation<gd.o0> G0();

    zc.h I0(gd.n1 n1Var);

    boolean L();

    zc.h M0();

    List<ReceiverParameterDescriptor> P0();

    boolean R0();

    Collection<ClassDescriptor> S();

    ReceiverParameterDescriptor S0();

    ClassConstructorDescriptor Z();

    @Override // pb.DeclarationDescriptor
    ClassDescriptor a();

    zc.h a0();

    @Override // pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    DeclarationDescriptor b();

    ClassDescriptor c0();

    u g();

    ClassKind getKind();

    Modality o();

    Collection<ClassConstructorDescriptor> p();

    boolean q();

    @Override // pb.ClassifierDescriptor
    gd.o0 x();

    boolean y();
}
