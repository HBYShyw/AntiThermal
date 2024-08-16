package cd;

import fd.StorageManager;
import java.util.List;
import lc.BinaryVersion;
import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import lc.versionSpecificBehavior;
import pb.DeclarationDescriptor;

/* compiled from: context.kt */
/* loaded from: classes2.dex */
public final class m {

    /* renamed from: a, reason: collision with root package name */
    private final k f5264a;

    /* renamed from: b, reason: collision with root package name */
    private final NameResolver f5265b;

    /* renamed from: c, reason: collision with root package name */
    private final DeclarationDescriptor f5266c;

    /* renamed from: d, reason: collision with root package name */
    private final TypeTable f5267d;

    /* renamed from: e, reason: collision with root package name */
    private final VersionRequirement f5268e;

    /* renamed from: f, reason: collision with root package name */
    private final BinaryVersion f5269f;

    /* renamed from: g, reason: collision with root package name */
    private final ed.f f5270g;

    /* renamed from: h, reason: collision with root package name */
    private final d0 f5271h;

    /* renamed from: i, reason: collision with root package name */
    private final MemberDeserializer f5272i;

    public m(k kVar, NameResolver nameResolver, DeclarationDescriptor declarationDescriptor, TypeTable typeTable, VersionRequirement versionRequirement, BinaryVersion binaryVersion, ed.f fVar, d0 d0Var, List<jc.s> list) {
        String c10;
        za.k.e(kVar, "components");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(declarationDescriptor, "containingDeclaration");
        za.k.e(typeTable, "typeTable");
        za.k.e(versionRequirement, "versionRequirementTable");
        za.k.e(binaryVersion, "metadataVersion");
        za.k.e(list, "typeParameters");
        this.f5264a = kVar;
        this.f5265b = nameResolver;
        this.f5266c = declarationDescriptor;
        this.f5267d = typeTable;
        this.f5268e = versionRequirement;
        this.f5269f = binaryVersion;
        this.f5270g = fVar;
        this.f5271h = new d0(this, d0Var, list, "Deserializer for \"" + declarationDescriptor.getName() + '\"', (fVar == null || (c10 = fVar.c()) == null) ? "[container not found]" : c10);
        this.f5272i = new MemberDeserializer(this);
    }

    public static /* synthetic */ m b(m mVar, DeclarationDescriptor declarationDescriptor, List list, NameResolver nameResolver, TypeTable typeTable, VersionRequirement versionRequirement, BinaryVersion binaryVersion, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            nameResolver = mVar.f5265b;
        }
        NameResolver nameResolver2 = nameResolver;
        if ((i10 & 8) != 0) {
            typeTable = mVar.f5267d;
        }
        TypeTable typeTable2 = typeTable;
        if ((i10 & 16) != 0) {
            versionRequirement = mVar.f5268e;
        }
        VersionRequirement versionRequirement2 = versionRequirement;
        if ((i10 & 32) != 0) {
            binaryVersion = mVar.f5269f;
        }
        return mVar.a(declarationDescriptor, list, nameResolver2, typeTable2, versionRequirement2, binaryVersion);
    }

    public final m a(DeclarationDescriptor declarationDescriptor, List<jc.s> list, NameResolver nameResolver, TypeTable typeTable, VersionRequirement versionRequirement, BinaryVersion binaryVersion) {
        za.k.e(declarationDescriptor, "descriptor");
        za.k.e(list, "typeParameterProtos");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(typeTable, "typeTable");
        VersionRequirement versionRequirement2 = versionRequirement;
        za.k.e(versionRequirement2, "versionRequirementTable");
        za.k.e(binaryVersion, "metadataVersion");
        k kVar = this.f5264a;
        if (!versionSpecificBehavior.b(binaryVersion)) {
            versionRequirement2 = this.f5268e;
        }
        return new m(kVar, nameResolver, declarationDescriptor, typeTable, versionRequirement2, binaryVersion, this.f5270g, this.f5271h, list);
    }

    public final k c() {
        return this.f5264a;
    }

    public final ed.f d() {
        return this.f5270g;
    }

    public final DeclarationDescriptor e() {
        return this.f5266c;
    }

    public final MemberDeserializer f() {
        return this.f5272i;
    }

    public final NameResolver g() {
        return this.f5265b;
    }

    public final StorageManager h() {
        return this.f5264a.u();
    }

    public final d0 i() {
        return this.f5271h;
    }

    public final TypeTable j() {
        return this.f5267d;
    }

    public final VersionRequirement k() {
        return this.f5268e;
    }
}
