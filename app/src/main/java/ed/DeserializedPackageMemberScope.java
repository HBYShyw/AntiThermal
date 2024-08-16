package ed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import jc.r;
import jc.t;
import jc.w;
import kotlin.collections.MutableCollections;
import kotlin.collections._Collections;
import kotlin.collections.s0;
import lc.BinaryVersion;
import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.PackageFragmentDescriptor;
import rb.ClassDescriptorFactory;

/* compiled from: DeserializedPackageMemberScope.kt */
/* renamed from: ed.i, reason: use source file name */
/* loaded from: classes2.dex */
public class DeserializedPackageMemberScope extends DeserializedMemberScope {

    /* renamed from: g, reason: collision with root package name */
    private final PackageFragmentDescriptor f11136g;

    /* renamed from: h, reason: collision with root package name */
    private final String f11137h;

    /* renamed from: i, reason: collision with root package name */
    private final FqName f11138i;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public DeserializedPackageMemberScope(PackageFragmentDescriptor packageFragmentDescriptor, jc.l lVar, NameResolver nameResolver, BinaryVersion binaryVersion, f fVar, cd.k kVar, String str, ya.a<? extends Collection<Name>> aVar) {
        super(r2, r3, r4, r7, aVar);
        za.k.e(packageFragmentDescriptor, "packageDescriptor");
        za.k.e(lVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(binaryVersion, "metadataVersion");
        za.k.e(kVar, "components");
        za.k.e(str, "debugName");
        za.k.e(aVar, "classNames");
        t Q = lVar.Q();
        za.k.d(Q, "proto.typeTable");
        TypeTable typeTable = new TypeTable(Q);
        VersionRequirement.a aVar2 = VersionRequirement.f14699b;
        w R = lVar.R();
        za.k.d(R, "proto.versionRequirementTable");
        cd.m a10 = kVar.a(packageFragmentDescriptor, nameResolver, typeTable, aVar2.a(R), binaryVersion, fVar);
        List<jc.i> J = lVar.J();
        za.k.d(J, "proto.functionList");
        List<jc.n> M = lVar.M();
        za.k.d(M, "proto.propertyList");
        List<r> P = lVar.P();
        za.k.d(P, "proto.typeAliasList");
        this.f11136g = packageFragmentDescriptor;
        this.f11137h = str;
        this.f11138i = packageFragmentDescriptor.d();
    }

    @Override // ed.DeserializedMemberScope, zc.MemberScopeImpl, zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        z(name, bVar);
        return super.e(name, bVar);
    }

    @Override // ed.DeserializedMemberScope
    protected void i(Collection<DeclarationDescriptor> collection, ya.l<? super Name, Boolean> lVar) {
        za.k.e(collection, "result");
        za.k.e(lVar, "nameFilter");
    }

    @Override // ed.DeserializedMemberScope
    protected ClassId m(Name name) {
        za.k.e(name, "name");
        return new ClassId(this.f11138i, name);
    }

    @Override // ed.DeserializedMemberScope
    protected Set<Name> s() {
        Set<Name> e10;
        e10 = s0.e();
        return e10;
    }

    @Override // ed.DeserializedMemberScope
    protected Set<Name> t() {
        Set<Name> e10;
        e10 = s0.e();
        return e10;
    }

    public String toString() {
        return this.f11137h;
    }

    @Override // ed.DeserializedMemberScope
    protected Set<Name> u() {
        Set<Name> e10;
        e10 = s0.e();
        return e10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ed.DeserializedMemberScope
    public boolean w(Name name) {
        boolean z10;
        za.k.e(name, "name");
        if (super.w(name)) {
            return true;
        }
        Iterable<ClassDescriptorFactory> k10 = p().c().k();
        if (!(k10 instanceof Collection) || !((Collection) k10).isEmpty()) {
            Iterator<ClassDescriptorFactory> it = k10.iterator();
            while (it.hasNext()) {
                if (it.next().b(this.f11138i, name)) {
                    z10 = true;
                    break;
                }
            }
        }
        z10 = false;
        return z10;
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    /* renamed from: y, reason: merged with bridge method [inline-methods] */
    public List<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        List<DeclarationDescriptor> m02;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        Collection<DeclarationDescriptor> j10 = j(dVar, lVar, xb.d.WHEN_GET_ALL_DESCRIPTORS);
        Iterable<ClassDescriptorFactory> k10 = p().c().k();
        ArrayList arrayList = new ArrayList();
        Iterator<ClassDescriptorFactory> it = k10.iterator();
        while (it.hasNext()) {
            MutableCollections.z(arrayList, it.next().c(this.f11138i));
        }
        m02 = _Collections.m0(j10, arrayList);
        return m02;
    }

    public void z(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        wb.a.b(p().c().o(), bVar, this.f11136g, name);
    }
}
