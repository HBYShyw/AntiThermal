package ed;

import fd.StorageManager;
import gd.TypeSubstitutor;
import gd.Variance;
import gd.g0;
import gd.o0;
import gd.o1;
import java.util.Collection;
import java.util.List;
import jc.r;
import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.SourceElement;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import pb.g1;
import pb.u;
import sb.AbstractTypeAliasDescriptor;
import sb.i0;

/* compiled from: DeserializedMemberDescriptor.kt */
/* loaded from: classes2.dex */
public final class l extends AbstractTypeAliasDescriptor implements g {

    /* renamed from: l, reason: collision with root package name */
    private final StorageManager f11139l;

    /* renamed from: m, reason: collision with root package name */
    private final r f11140m;

    /* renamed from: n, reason: collision with root package name */
    private final NameResolver f11141n;

    /* renamed from: o, reason: collision with root package name */
    private final TypeTable f11142o;

    /* renamed from: p, reason: collision with root package name */
    private final VersionRequirement f11143p;

    /* renamed from: q, reason: collision with root package name */
    private final f f11144q;

    /* renamed from: r, reason: collision with root package name */
    private Collection<? extends i0> f11145r;

    /* renamed from: s, reason: collision with root package name */
    private o0 f11146s;

    /* renamed from: t, reason: collision with root package name */
    private o0 f11147t;

    /* renamed from: u, reason: collision with root package name */
    private List<? extends TypeParameterDescriptor> f11148u;

    /* renamed from: v, reason: collision with root package name */
    private o0 f11149v;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public l(StorageManager storageManager, DeclarationDescriptor declarationDescriptor, qb.g gVar, Name name, u uVar, r rVar, NameResolver nameResolver, TypeTable typeTable, VersionRequirement versionRequirement, f fVar) {
        super(declarationDescriptor, gVar, name, r4, uVar);
        za.k.e(storageManager, "storageManager");
        za.k.e(declarationDescriptor, "containingDeclaration");
        za.k.e(gVar, "annotations");
        za.k.e(name, "name");
        za.k.e(uVar, "visibility");
        za.k.e(rVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(typeTable, "typeTable");
        za.k.e(versionRequirement, "versionRequirementTable");
        SourceElement sourceElement = SourceElement.f16664a;
        za.k.d(sourceElement, "NO_SOURCE");
        this.f11139l = storageManager;
        this.f11140m = rVar;
        this.f11141n = nameResolver;
        this.f11142o = typeTable;
        this.f11143p = versionRequirement;
        this.f11144q = fVar;
    }

    @Override // sb.AbstractTypeAliasDescriptor
    protected List<TypeParameterDescriptor> V0() {
        List list = this.f11148u;
        if (list != null) {
            return list;
        }
        za.k.s("typeConstructorParameters");
        return null;
    }

    public r X0() {
        return this.f11140m;
    }

    public VersionRequirement Y0() {
        return this.f11143p;
    }

    public final void Z0(List<? extends TypeParameterDescriptor> list, o0 o0Var, o0 o0Var2) {
        za.k.e(list, "declaredTypeParameters");
        za.k.e(o0Var, "underlyingType");
        za.k.e(o0Var2, "expandedType");
        W0(list);
        this.f11146s = o0Var;
        this.f11147t = o0Var2;
        this.f11148u = g1.d(this);
        this.f11149v = O0();
        this.f11145r = U0();
    }

    @Override // pb.Substitutable
    /* renamed from: a1, reason: merged with bridge method [inline-methods] */
    public TypeAliasDescriptor c(TypeSubstitutor typeSubstitutor) {
        za.k.e(typeSubstitutor, "substitutor");
        if (typeSubstitutor.k()) {
            return this;
        }
        StorageManager o02 = o0();
        DeclarationDescriptor b10 = b();
        za.k.d(b10, "containingDeclaration");
        qb.g i10 = i();
        za.k.d(i10, "annotations");
        Name name = getName();
        za.k.d(name, "name");
        l lVar = new l(o02, b10, i10, name, g(), X0(), h0(), b0(), Y0(), j0());
        List<TypeParameterDescriptor> B = B();
        o0 n02 = n0();
        Variance variance = Variance.INVARIANT;
        g0 n10 = typeSubstitutor.n(n02, variance);
        za.k.d(n10, "substitutor.safeSubstitu…Type, Variance.INVARIANT)");
        o0 a10 = o1.a(n10);
        g0 n11 = typeSubstitutor.n(e0(), variance);
        za.k.d(n11, "substitutor.safeSubstitu…Type, Variance.INVARIANT)");
        lVar.Z0(B, a10, o1.a(n11));
        return lVar;
    }

    @Override // ed.g
    public TypeTable b0() {
        return this.f11142o;
    }

    @Override // pb.TypeAliasDescriptor
    public o0 e0() {
        o0 o0Var = this.f11147t;
        if (o0Var != null) {
            return o0Var;
        }
        za.k.s("expandedType");
        return null;
    }

    @Override // ed.g
    public NameResolver h0() {
        return this.f11141n;
    }

    @Override // ed.g
    public f j0() {
        return this.f11144q;
    }

    @Override // pb.TypeAliasDescriptor
    public o0 n0() {
        o0 o0Var = this.f11146s;
        if (o0Var != null) {
            return o0Var;
        }
        za.k.s("underlyingType");
        return null;
    }

    @Override // sb.AbstractTypeAliasDescriptor
    protected StorageManager o0() {
        return this.f11139l;
    }

    @Override // pb.TypeAliasDescriptor
    public ClassDescriptor w() {
        if (gd.i0.a(e0())) {
            return null;
        }
        ClassifierDescriptor v7 = e0().W0().v();
        if (v7 instanceof ClassDescriptor) {
            return (ClassDescriptor) v7;
        }
        return null;
    }

    @Override // pb.ClassifierDescriptor
    public o0 x() {
        o0 o0Var = this.f11149v;
        if (o0Var != null) {
            return o0Var;
        }
        za.k.s("defaultTypeImpl");
        return null;
    }
}
