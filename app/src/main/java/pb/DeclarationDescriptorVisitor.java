package pb;

/* compiled from: DeclarationDescriptorVisitor.java */
/* renamed from: pb.o, reason: use source file name */
/* loaded from: classes2.dex */
public interface DeclarationDescriptorVisitor<R, D> {
    R a(PropertySetterDescriptor propertySetterDescriptor, D d10);

    R b(TypeParameterDescriptor typeParameterDescriptor, D d10);

    R c(ClassDescriptor classDescriptor, D d10);

    R d(PackageFragmentDescriptor packageFragmentDescriptor, D d10);

    R e(ValueParameterDescriptor valueParameterDescriptor, D d10);

    R f(PropertyDescriptor propertyDescriptor, D d10);

    R g(PackageViewDescriptor packageViewDescriptor, D d10);

    R h(FunctionDescriptor functionDescriptor, D d10);

    R i(TypeAliasDescriptor typeAliasDescriptor, D d10);

    R j(ModuleDescriptor moduleDescriptor, D d10);

    R k(PropertyGetterDescriptor propertyGetterDescriptor, D d10);

    R l(ConstructorDescriptor constructorDescriptor, D d10);

    R m(ReceiverParameterDescriptor receiverParameterDescriptor, D d10);
}
