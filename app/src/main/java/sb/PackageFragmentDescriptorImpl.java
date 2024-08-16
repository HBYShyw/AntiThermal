package sb;

import oc.FqName;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;
import pb.SourceElement;

/* compiled from: PackageFragmentDescriptorImpl.kt */
/* renamed from: sb.z, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class PackageFragmentDescriptorImpl extends DeclarationDescriptorNonRootImpl implements PackageFragmentDescriptor {

    /* renamed from: i, reason: collision with root package name */
    private final FqName f18415i;

    /* renamed from: j, reason: collision with root package name */
    private final String f18416j;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PackageFragmentDescriptorImpl(ModuleDescriptor moduleDescriptor, FqName fqName) {
        super(moduleDescriptor, qb.g.f17195b.b(), fqName.h(), SourceElement.f16664a);
        za.k.e(moduleDescriptor, "module");
        za.k.e(fqName, "fqName");
        this.f18415i = fqName;
        this.f18416j = "package " + fqName + " of " + moduleDescriptor;
    }

    @Override // pb.DeclarationDescriptor
    public <R, D> R H0(DeclarationDescriptorVisitor<R, D> declarationDescriptorVisitor, D d10) {
        za.k.e(declarationDescriptorVisitor, "visitor");
        return declarationDescriptorVisitor.d(this, d10);
    }

    @Override // pb.PackageFragmentDescriptor
    public final FqName d() {
        return this.f18415i;
    }

    @Override // sb.DeclarationDescriptorImpl
    public String toString() {
        return this.f18416j;
    }

    @Override // sb.DeclarationDescriptorNonRootImpl, pb.DeclarationDescriptorWithSource
    public SourceElement z() {
        SourceElement sourceElement = SourceElement.f16664a;
        za.k.d(sourceElement, "NO_SOURCE");
        return sourceElement;
    }

    @Override // sb.DeclarationDescriptorNonRootImpl, pb.DeclarationDescriptor
    public ModuleDescriptor b() {
        DeclarationDescriptor b10 = super.b();
        za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ModuleDescriptor");
        return (ModuleDescriptor) b10;
    }
}
