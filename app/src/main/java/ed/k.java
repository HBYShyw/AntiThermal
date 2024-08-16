package ed;

import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.SourceElement;
import sb.FunctionDescriptorImpl;
import sb.SimpleFunctionDescriptorImpl;
import za.DefaultConstructorMarker;

/* compiled from: DeserializedMemberDescriptor.kt */
/* loaded from: classes2.dex */
public final class k extends SimpleFunctionDescriptorImpl implements b {
    private final jc.i I;
    private final NameResolver J;
    private final TypeTable K;
    private final VersionRequirement L;
    private final f M;

    public /* synthetic */ k(DeclarationDescriptor declarationDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor, qb.g gVar, Name name, CallableMemberDescriptor.a aVar, jc.i iVar, NameResolver nameResolver, TypeTable typeTable, VersionRequirement versionRequirement, f fVar, SourceElement sourceElement, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(declarationDescriptor, simpleFunctionDescriptor, gVar, name, aVar, iVar, nameResolver, typeTable, versionRequirement, fVar, (i10 & 1024) != 0 ? null : sourceElement);
    }

    @Override // sb.SimpleFunctionDescriptorImpl, sb.FunctionDescriptorImpl
    protected FunctionDescriptorImpl U0(DeclarationDescriptor declarationDescriptor, FunctionDescriptor functionDescriptor, CallableMemberDescriptor.a aVar, Name name, qb.g gVar, SourceElement sourceElement) {
        Name name2;
        za.k.e(declarationDescriptor, "newOwner");
        za.k.e(aVar, "kind");
        za.k.e(gVar, "annotations");
        za.k.e(sourceElement, "source");
        SimpleFunctionDescriptor simpleFunctionDescriptor = (SimpleFunctionDescriptor) functionDescriptor;
        if (name == null) {
            Name name3 = getName();
            za.k.d(name3, "name");
            name2 = name3;
        } else {
            name2 = name;
        }
        k kVar = new k(declarationDescriptor, simpleFunctionDescriptor, gVar, name2, aVar, M(), h0(), b0(), z1(), j0(), sourceElement);
        kVar.h1(Z0());
        return kVar;
    }

    @Override // ed.g
    public TypeTable b0() {
        return this.K;
    }

    @Override // ed.g
    public NameResolver h0() {
        return this.J;
    }

    @Override // ed.g
    public f j0() {
        return this.M;
    }

    @Override // ed.g
    /* renamed from: y1, reason: merged with bridge method [inline-methods] */
    public jc.i M() {
        return this.I;
    }

    public VersionRequirement z1() {
        return this.L;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public k(DeclarationDescriptor declarationDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor, qb.g gVar, Name name, CallableMemberDescriptor.a aVar, jc.i iVar, NameResolver nameResolver, TypeTable typeTable, VersionRequirement versionRequirement, f fVar, SourceElement sourceElement) {
        super(declarationDescriptor, simpleFunctionDescriptor, gVar, name, aVar, sourceElement == null ? SourceElement.f16664a : sourceElement);
        za.k.e(declarationDescriptor, "containingDeclaration");
        za.k.e(gVar, "annotations");
        za.k.e(name, "name");
        za.k.e(aVar, "kind");
        za.k.e(iVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(typeTable, "typeTable");
        za.k.e(versionRequirement, "versionRequirementTable");
        this.I = iVar;
        this.J = nameResolver;
        this.K = typeTable;
        this.L = versionRequirement;
        this.M = fVar;
    }
}
