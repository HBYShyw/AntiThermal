package id;

import gd.TypeSubstitutor;
import gd.g0;
import java.util.Collection;
import java.util.List;
import kotlin.collections.r;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.DescriptorVisibilities;
import pb.Modality;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.PropertyGetterDescriptor;
import pb.PropertySetterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.u;
import pb.w;
import sb.PropertyDescriptorImpl;

/* compiled from: ErrorPropertyDescriptor.kt */
/* renamed from: id.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class ErrorPropertyDescriptor implements PropertyDescriptor {

    /* renamed from: e, reason: collision with root package name */
    private final /* synthetic */ PropertyDescriptorImpl f12760e;

    public ErrorPropertyDescriptor() {
        List<? extends TypeParameterDescriptor> j10;
        List<ReceiverParameterDescriptor> j11;
        ErrorUtils errorUtils = ErrorUtils.f12833a;
        PropertyDescriptorImpl X0 = PropertyDescriptorImpl.X0(errorUtils.h(), qb.g.f17195b.b(), Modality.OPEN, DescriptorVisibilities.f16733e, true, Name.i(ErrorEntity.ERROR_PROPERTY.b()), CallableMemberDescriptor.a.DECLARATION, SourceElement.f16664a, false, false, false, false, false, false);
        g0 k10 = errorUtils.k();
        j10 = r.j();
        j11 = r.j();
        X0.k1(k10, j10, null, null, j11);
        this.f12760e = X0;
    }

    @Override // pb.PropertyDescriptor
    public List<PropertyAccessorDescriptor> C() {
        return this.f12760e.C();
    }

    @Override // pb.MemberDescriptor
    public boolean D() {
        return this.f12760e.D();
    }

    @Override // pb.CallableMemberDescriptor
    public void D0(Collection<? extends CallableMemberDescriptor> collection) {
        za.k.e(collection, "overriddenDescriptors");
        this.f12760e.D0(collection);
    }

    @Override // pb.CallableDescriptor
    public <V> V E(CallableDescriptor.a<V> aVar) {
        return (V) this.f12760e.E(aVar);
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        return (R) this.f12760e.H0(declarationDescriptorVisitor, d10);
    }

    @Override // pb.VariableDescriptor
    public boolean I() {
        return this.f12760e.I();
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return this.f12760e.N0();
    }

    @Override // pb.CallableDescriptor
    public boolean O() {
        return this.f12760e.O();
    }

    @Override // pb.CallableMemberDescriptor
    /* renamed from: T */
    public CallableMemberDescriptor T0(DeclarationDescriptor declarationDescriptor, Modality modality, u uVar, CallableMemberDescriptor.a aVar, boolean z10) {
        return this.f12760e.T0(declarationDescriptor, modality, uVar, aVar, z10);
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        return this.f12760e.U();
    }

    @Override // pb.VariableDescriptorWithAccessors
    public boolean V() {
        return this.f12760e.V();
    }

    @Override // pb.DeclarationDescriptor
    /* renamed from: a */
    public PropertyDescriptor T0() {
        return this.f12760e.T0();
    }

    @Override // pb.DeclarationDescriptorNonRoot, pb.DeclarationDescriptor
    public DeclarationDescriptor b() {
        return this.f12760e.b();
    }

    @Override // pb.Substitutable
    public PropertyDescriptor c(TypeSubstitutor typeSubstitutor) {
        za.k.e(typeSubstitutor, "substitutor");
        return this.f12760e.c(typeSubstitutor);
    }

    @Override // pb.PropertyDescriptor, pb.CallableMemberDescriptor, pb.CallableDescriptor
    public Collection<? extends PropertyDescriptor> e() {
        return this.f12760e.e();
    }

    @Override // pb.CallableDescriptor
    public g0 f() {
        return this.f12760e.f();
    }

    @Override // pb.VariableDescriptor
    public uc.g<?> f0() {
        return this.f12760e.f0();
    }

    @Override // pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public u g() {
        return this.f12760e.g();
    }

    @Override // pb.CallableMemberDescriptor
    public CallableMemberDescriptor.a getKind() {
        return this.f12760e.getKind();
    }

    @Override // pb.Named
    public Name getName() {
        return this.f12760e.getName();
    }

    @Override // pb.ValueDescriptor
    public g0 getType() {
        return this.f12760e.getType();
    }

    @Override // pb.PropertyDescriptor
    public PropertyGetterDescriptor h() {
        return this.f12760e.h();
    }

    @Override // qb.a
    public qb.g i() {
        qb.g i10 = this.f12760e.i();
        za.k.d(i10, "<get-annotations>(...)");
        return i10;
    }

    @Override // pb.PropertyDescriptor
    public PropertySetterDescriptor k() {
        return this.f12760e.k();
    }

    @Override // pb.CallableDescriptor
    public List<ValueParameterDescriptor> l() {
        return this.f12760e.l();
    }

    @Override // pb.CallableDescriptor
    public List<TypeParameterDescriptor> m() {
        return this.f12760e.m();
    }

    @Override // pb.CallableDescriptor
    public ReceiverParameterDescriptor m0() {
        return this.f12760e.m0();
    }

    @Override // pb.MemberDescriptor
    public Modality o() {
        return this.f12760e.o();
    }

    @Override // pb.VariableDescriptor
    public boolean p0() {
        return this.f12760e.p0();
    }

    @Override // pb.CallableDescriptor
    public ReceiverParameterDescriptor r0() {
        return this.f12760e.r0();
    }

    @Override // pb.PropertyDescriptor
    public w s0() {
        return this.f12760e.s0();
    }

    @Override // pb.PropertyDescriptor
    public w v0() {
        return this.f12760e.v0();
    }

    @Override // pb.CallableDescriptor
    public List<ReceiverParameterDescriptor> w0() {
        return this.f12760e.w0();
    }

    @Override // pb.VariableDescriptor
    public boolean x0() {
        return this.f12760e.x0();
    }

    @Override // pb.DeclarationDescriptorWithSource
    public SourceElement z() {
        return this.f12760e.z();
    }
}
