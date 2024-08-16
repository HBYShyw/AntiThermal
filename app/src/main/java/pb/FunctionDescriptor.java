package pb;

import gd.TypeSubstitutor;
import java.util.Collection;
import java.util.List;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;

/* compiled from: FunctionDescriptor.java */
/* renamed from: pb.y, reason: use source file name */
/* loaded from: classes2.dex */
public interface FunctionDescriptor extends CallableMemberDescriptor {

    /* compiled from: FunctionDescriptor.java */
    /* renamed from: pb.y$a */
    /* loaded from: classes2.dex */
    public interface a<D extends FunctionDescriptor> {
        a<D> a();

        a<D> b(qb.g gVar);

        D build();

        a<D> c(List<ValueParameterDescriptor> list);

        a<D> d(gd.n1 n1Var);

        a<D> e(gd.g0 g0Var);

        a<D> f(Modality modality);

        a<D> g(ReceiverParameterDescriptor receiverParameterDescriptor);

        a<D> h();

        <V> a<D> i(CallableDescriptor.a<V> aVar, V v7);

        a<D> j(ReceiverParameterDescriptor receiverParameterDescriptor);

        a<D> k();

        a<D> l(boolean z10);

        a<D> m(Name name);

        a<D> n(List<TypeParameterDescriptor> list);

        a<D> o();

        a<D> p(u uVar);

        a<D> q(CallableMemberDescriptor.a aVar);

        a<D> r(DeclarationDescriptor declarationDescriptor);

        a<D> s(CallableMemberDescriptor callableMemberDescriptor);

        a<D> t();
    }

    a<? extends FunctionDescriptor> A();

    boolean B0();

    boolean C0();

    boolean L0();

    boolean Q0();

    boolean X();

    boolean Y();

    @Override // pb.CallableMemberDescriptor, pb.CallableDescriptor, pb.DeclarationDescriptor
    FunctionDescriptor a();

    @Override // pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    DeclarationDescriptor b();

    FunctionDescriptor c(TypeSubstitutor typeSubstitutor);

    @Override // pb.CallableMemberDescriptor, pb.CallableDescriptor
    Collection<? extends FunctionDescriptor> e();

    FunctionDescriptor l0();

    boolean y();
}
