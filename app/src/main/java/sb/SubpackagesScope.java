package sb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections.s0;
import oc.FqName;
import oc.Name;
import pb.DeclarationDescriptor;
import pb.ModuleDescriptor;
import pb.PackageViewDescriptor;
import qd.collections;
import zc.MemberScopeImpl;
import zc.c;

/* compiled from: SubpackagesScope.kt */
/* renamed from: sb.h0, reason: use source file name */
/* loaded from: classes2.dex */
public class SubpackagesScope extends MemberScopeImpl {

    /* renamed from: b, reason: collision with root package name */
    private final ModuleDescriptor f18284b;

    /* renamed from: c, reason: collision with root package name */
    private final FqName f18285c;

    public SubpackagesScope(ModuleDescriptor moduleDescriptor, FqName fqName) {
        za.k.e(moduleDescriptor, "moduleDescriptor");
        za.k.e(fqName, "fqName");
        this.f18284b = moduleDescriptor;
        this.f18285c = fqName;
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> f() {
        Set<Name> e10;
        e10 = s0.e();
        return e10;
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        List j10;
        List j11;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        if (!dVar.a(zc.d.f20424c.f())) {
            j11 = kotlin.collections.r.j();
            return j11;
        }
        if (this.f18285c.d() && dVar.l().contains(c.b.f20423a)) {
            j10 = kotlin.collections.r.j();
            return j10;
        }
        Collection<FqName> v7 = this.f18284b.v(this.f18285c, lVar);
        ArrayList arrayList = new ArrayList(v7.size());
        Iterator<FqName> it = v7.iterator();
        while (it.hasNext()) {
            Name g6 = it.next().g();
            za.k.d(g6, "subFqName.shortName()");
            if (lVar.invoke(g6).booleanValue()) {
                collections.a(arrayList, h(g6));
            }
        }
        return arrayList;
    }

    protected final PackageViewDescriptor h(Name name) {
        za.k.e(name, "name");
        if (name.g()) {
            return null;
        }
        ModuleDescriptor moduleDescriptor = this.f18284b;
        FqName c10 = this.f18285c.c(name);
        za.k.d(c10, "fqName.child(name)");
        PackageViewDescriptor H = moduleDescriptor.H(c10);
        if (H.isEmpty()) {
            return null;
        }
        return H;
    }

    public String toString() {
        return "subpackages of " + this.f18285c + " from " + this.f18284b;
    }
}
