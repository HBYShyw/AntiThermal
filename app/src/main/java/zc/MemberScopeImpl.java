package zc;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections.r;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import qd.functions;

/* compiled from: MemberScopeImpl.kt */
/* renamed from: zc.i, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class MemberScopeImpl implements h {
    @Override // zc.h
    public Collection<? extends PropertyDescriptor> a(Name name, xb.b bVar) {
        List j10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        j10 = r.j();
        return j10;
    }

    @Override // zc.h
    public Set<Name> b() {
        Collection<DeclarationDescriptor> g6 = g(d.f20443v, functions.a());
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Object obj : g6) {
            if (obj instanceof SimpleFunctionDescriptor) {
                Name name = ((SimpleFunctionDescriptor) obj).getName();
                za.k.d(name, "it.name");
                linkedHashSet.add(name);
            }
        }
        return linkedHashSet;
    }

    @Override // zc.h
    public Collection<? extends SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        List j10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        j10 = r.j();
        return j10;
    }

    @Override // zc.h
    public Set<Name> d() {
        Collection<DeclarationDescriptor> g6 = g(d.f20444w, functions.a());
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Object obj : g6) {
            if (obj instanceof SimpleFunctionDescriptor) {
                Name name = ((SimpleFunctionDescriptor) obj).getName();
                za.k.d(name, "it.name");
                linkedHashSet.add(name);
            }
        }
        return linkedHashSet;
    }

    @Override // zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return null;
    }

    @Override // zc.h
    public Set<Name> f() {
        return null;
    }

    @Override // zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(d dVar, ya.l<? super Name, Boolean> lVar) {
        List j10;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        j10 = r.j();
        return j10;
    }
}
