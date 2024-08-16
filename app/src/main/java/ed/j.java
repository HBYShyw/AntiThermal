package ed;

import lc.Flags;
import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.DeclarationDescriptor;
import pb.Modality;
import pb.PropertyDescriptor;
import pb.SourceElement;
import pb.u;
import sb.PropertyDescriptorImpl;

/* compiled from: DeserializedMemberDescriptor.kt */
/* loaded from: classes2.dex */
public final class j extends PropertyDescriptorImpl implements b {
    private final jc.n G;
    private final NameResolver H;
    private final TypeTable I;
    private final VersionRequirement J;
    private final f K;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public j(DeclarationDescriptor declarationDescriptor, PropertyDescriptor propertyDescriptor, qb.g gVar, Modality modality, u uVar, boolean z10, Name name, CallableMemberDescriptor.a aVar, boolean z11, boolean z12, boolean z13, boolean z14, boolean z15, jc.n nVar, NameResolver nameResolver, TypeTable typeTable, VersionRequirement versionRequirement, f fVar) {
        super(declarationDescriptor, propertyDescriptor, gVar, modality, uVar, z10, name, aVar, SourceElement.f16664a, z11, z12, z15, false, z13, z14);
        za.k.e(declarationDescriptor, "containingDeclaration");
        za.k.e(gVar, "annotations");
        za.k.e(modality, "modality");
        za.k.e(uVar, "visibility");
        za.k.e(name, "name");
        za.k.e(aVar, "kind");
        za.k.e(nVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(typeTable, "typeTable");
        za.k.e(versionRequirement, "versionRequirementTable");
        this.G = nVar;
        this.H = nameResolver;
        this.I = typeTable;
        this.J = versionRequirement;
        this.K = fVar;
    }

    @Override // sb.PropertyDescriptorImpl, pb.MemberDescriptor
    public boolean D() {
        Boolean d10 = Flags.D.d(M().V());
        za.k.d(d10, "IS_EXTERNAL_PROPERTY.get(proto.flags)");
        return d10.booleanValue();
    }

    @Override // sb.PropertyDescriptorImpl
    protected PropertyDescriptorImpl Y0(DeclarationDescriptor declarationDescriptor, Modality modality, u uVar, PropertyDescriptor propertyDescriptor, CallableMemberDescriptor.a aVar, Name name, SourceElement sourceElement) {
        za.k.e(declarationDescriptor, "newOwner");
        za.k.e(modality, "newModality");
        za.k.e(uVar, "newVisibility");
        za.k.e(aVar, "kind");
        za.k.e(name, "newName");
        za.k.e(sourceElement, "source");
        return new j(declarationDescriptor, propertyDescriptor, i(), modality, uVar, p0(), name, aVar, x0(), I(), D(), V(), U(), M(), h0(), b0(), p1(), j0());
    }

    @Override // ed.g
    public TypeTable b0() {
        return this.I;
    }

    @Override // ed.g
    public NameResolver h0() {
        return this.H;
    }

    @Override // ed.g
    public f j0() {
        return this.K;
    }

    @Override // ed.g
    /* renamed from: o1, reason: merged with bridge method [inline-methods] */
    public jc.n M() {
        return this.G;
    }

    public VersionRequirement p1() {
        return this.J;
    }
}
