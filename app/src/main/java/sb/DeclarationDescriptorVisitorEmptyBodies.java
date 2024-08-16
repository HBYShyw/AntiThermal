package sb;

import pb.ClassDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.FunctionDescriptor;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;
import pb.PackageViewDescriptor;
import pb.PropertyGetterDescriptor;
import pb.PropertySetterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.VariableDescriptor;

/* compiled from: DeclarationDescriptorVisitorEmptyBodies.java */
/* renamed from: sb.l, reason: use source file name */
/* loaded from: classes2.dex */
public class DeclarationDescriptorVisitorEmptyBodies<R, D> implements DeclarationDescriptorVisitor<R, D> {
    @Override // pb.DeclarationDescriptorVisitor
    public R a(PropertySetterDescriptor propertySetterDescriptor, D d10) {
        return h(propertySetterDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R b(TypeParameterDescriptor typeParameterDescriptor, D d10) {
        return n(typeParameterDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R c(ClassDescriptor classDescriptor, D d10) {
        return n(classDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R d(PackageFragmentDescriptor packageFragmentDescriptor, D d10) {
        return n(packageFragmentDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R e(ValueParameterDescriptor valueParameterDescriptor, D d10) {
        return o(valueParameterDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R g(PackageViewDescriptor packageViewDescriptor, D d10) {
        return n(packageViewDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R h(FunctionDescriptor functionDescriptor, D d10) {
        throw null;
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R i(TypeAliasDescriptor typeAliasDescriptor, D d10) {
        return n(typeAliasDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R j(ModuleDescriptor moduleDescriptor, D d10) {
        return n(moduleDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R k(PropertyGetterDescriptor propertyGetterDescriptor, D d10) {
        return h(propertyGetterDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R l(ConstructorDescriptor constructorDescriptor, D d10) {
        return h(constructorDescriptor, d10);
    }

    @Override // pb.DeclarationDescriptorVisitor
    public R m(ReceiverParameterDescriptor receiverParameterDescriptor, D d10) {
        return n(receiverParameterDescriptor, d10);
    }

    public R n(DeclarationDescriptor declarationDescriptor, D d10) {
        return null;
    }

    public R o(VariableDescriptor variableDescriptor, D d10) {
        return n(variableDescriptor, d10);
    }
}
