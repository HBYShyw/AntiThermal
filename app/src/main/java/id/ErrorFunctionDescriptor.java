package id;

import gd.g0;
import gd.n1;
import java.util.Collection;
import java.util.List;
import kotlin.collections.r;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.ReceiverParameterDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.u;
import sb.FunctionDescriptorImpl;
import sb.SimpleFunctionDescriptorImpl;

/* compiled from: ErrorFunctionDescriptor.kt */
/* renamed from: id.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class ErrorFunctionDescriptor extends SimpleFunctionDescriptorImpl {

    /* compiled from: ErrorFunctionDescriptor.kt */
    /* renamed from: id.c$a */
    /* loaded from: classes2.dex */
    public static final class a implements FunctionDescriptor.a<SimpleFunctionDescriptor> {
        a() {
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> a() {
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> b(qb.g gVar) {
            za.k.e(gVar, "additionalAnnotations");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> c(List<? extends ValueParameterDescriptor> list) {
            za.k.e(list, "parameters");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> d(n1 n1Var) {
            za.k.e(n1Var, "substitution");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> e(g0 g0Var) {
            za.k.e(g0Var, "type");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> f(Modality modality) {
            za.k.e(modality, "modality");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> g(ReceiverParameterDescriptor receiverParameterDescriptor) {
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> h() {
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public <V> FunctionDescriptor.a<SimpleFunctionDescriptor> i(CallableDescriptor.a<V> aVar, V v7) {
            za.k.e(aVar, "userDataKey");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> j(ReceiverParameterDescriptor receiverParameterDescriptor) {
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> k() {
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> l(boolean z10) {
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> m(Name name) {
            za.k.e(name, "name");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> n(List<? extends TypeParameterDescriptor> list) {
            za.k.e(list, "parameters");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> o() {
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> p(u uVar) {
            za.k.e(uVar, "visibility");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> q(CallableMemberDescriptor.a aVar) {
            za.k.e(aVar, "kind");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> r(DeclarationDescriptor declarationDescriptor) {
            za.k.e(declarationDescriptor, "owner");
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> s(CallableMemberDescriptor callableMemberDescriptor) {
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        public FunctionDescriptor.a<SimpleFunctionDescriptor> t() {
            return this;
        }

        @Override // pb.FunctionDescriptor.a
        /* renamed from: u, reason: merged with bridge method [inline-methods] */
        public SimpleFunctionDescriptor build() {
            return ErrorFunctionDescriptor.this;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ErrorFunctionDescriptor(ClassDescriptor classDescriptor) {
        super(classDescriptor, null, qb.g.f17195b.b(), Name.i(ErrorEntity.ERROR_FUNCTION.b()), CallableMemberDescriptor.a.DECLARATION, SourceElement.f16664a);
        List<ReceiverParameterDescriptor> j10;
        List<? extends TypeParameterDescriptor> j11;
        List<ValueParameterDescriptor> j12;
        za.k.e(classDescriptor, "containingDeclaration");
        j10 = r.j();
        j11 = r.j();
        j12 = r.j();
        a1(null, null, j10, j11, j12, ErrorUtils.d(ErrorTypeKind.f12807o, new String[0]), Modality.OPEN, DescriptorVisibilities.f16733e);
    }

    @Override // sb.SimpleFunctionDescriptorImpl, sb.FunctionDescriptorImpl, pb.FunctionDescriptor, pb.SimpleFunctionDescriptor
    public FunctionDescriptor.a<SimpleFunctionDescriptor> A() {
        return new a();
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor
    public boolean C0() {
        return false;
    }

    @Override // sb.FunctionDescriptorImpl, pb.CallableMemberDescriptor
    public void D0(Collection<? extends CallableMemberDescriptor> collection) {
        za.k.e(collection, "overriddenDescriptors");
    }

    @Override // sb.FunctionDescriptorImpl, pb.CallableDescriptor
    public <V> V E(CallableDescriptor.a<V> aVar) {
        za.k.e(aVar, "key");
        return null;
    }

    @Override // sb.SimpleFunctionDescriptorImpl, sb.FunctionDescriptorImpl
    protected FunctionDescriptorImpl U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, qb.g gVar, SourceElement sourceElement) {
        za.k.e(declarationDescriptor, "newOwner");
        za.k.e(aVar, "kind");
        za.k.e(gVar, "annotations");
        za.k.e(sourceElement, "source");
        return this;
    }

    @Override // sb.SimpleFunctionDescriptorImpl, sb.FunctionDescriptorImpl
    /* renamed from: t1, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public SimpleFunctionDescriptor T0(DeclarationDescriptor declarationDescriptor, Modality modality, u uVar, CallableMemberDescriptor.a aVar, boolean z10) {
        za.k.e(declarationDescriptor, "newOwner");
        za.k.e(modality, "modality");
        za.k.e(uVar, "visibility");
        za.k.e(aVar, "kind");
        return this;
    }
}
