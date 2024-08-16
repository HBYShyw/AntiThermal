package ed;

import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.SourceElement;
import sb.ClassConstructorDescriptorImpl;
import za.DefaultConstructorMarker;

/* compiled from: DeserializedMemberDescriptor.kt */
/* loaded from: classes2.dex */
public final class c extends ClassConstructorDescriptorImpl implements b {
    private final jc.d J;
    private final NameResolver K;
    private final TypeTable L;
    private final VersionRequirement M;
    private final f N;

    public /* synthetic */ c(ClassDescriptor classDescriptor, ConstructorDescriptor constructorDescriptor, qb.g gVar, boolean z10, CallableMemberDescriptor.a aVar, jc.d dVar, NameResolver nameResolver, TypeTable typeTable, VersionRequirement versionRequirement, f fVar, SourceElement sourceElement, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(classDescriptor, constructorDescriptor, gVar, z10, aVar, dVar, nameResolver, typeTable, versionRequirement, fVar, (i10 & 1024) != 0 ? null : sourceElement);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.ClassConstructorDescriptorImpl
    /* renamed from: B1, reason: merged with bridge method [inline-methods] */
    public c U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, qb.g gVar, SourceElement sourceElement) {
        za.k.e(declarationDescriptor, "newOwner");
        za.k.e(aVar, "kind");
        za.k.e(gVar, "annotations");
        za.k.e(sourceElement, "source");
        c cVar = new c((ClassDescriptor) declarationDescriptor, (ConstructorDescriptor) functionDescriptor, gVar, this.I, aVar, M(), h0(), b0(), D1(), j0(), sourceElement);
        cVar.h1(Z0());
        return cVar;
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor
    public boolean C0() {
        return false;
    }

    @Override // ed.g
    /* renamed from: C1, reason: merged with bridge method [inline-methods] */
    public jc.d M() {
        return this.J;
    }

    @Override // sb.FunctionDescriptorImpl, pb.MemberDescriptor
    public boolean D() {
        return false;
    }

    public VersionRequirement D1() {
        return this.M;
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor
    public boolean X() {
        return false;
    }

    @Override // ed.g
    public TypeTable b0() {
        return this.L;
    }

    @Override // ed.g
    public NameResolver h0() {
        return this.K;
    }

    @Override // ed.g
    public f j0() {
        return this.N;
    }

    @Override // sb.FunctionDescriptorImpl, pb.FunctionDescriptor
    public boolean y() {
        return false;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c(ClassDescriptor classDescriptor, ConstructorDescriptor constructorDescriptor, qb.g gVar, boolean z10, CallableMemberDescriptor.a aVar, jc.d dVar, NameResolver nameResolver, TypeTable typeTable, VersionRequirement versionRequirement, f fVar, SourceElement sourceElement) {
        super(classDescriptor, constructorDescriptor, gVar, z10, aVar, sourceElement == null ? SourceElement.f16664a : sourceElement);
        za.k.e(classDescriptor, "containingDeclaration");
        za.k.e(gVar, "annotations");
        za.k.e(aVar, "kind");
        za.k.e(dVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(typeTable, "typeTable");
        za.k.e(versionRequirement, "versionRequirementTable");
        this.J = dVar;
        this.K = nameResolver;
        this.L = typeTable;
        this.M = versionRequirement;
        this.N = fVar;
    }
}
