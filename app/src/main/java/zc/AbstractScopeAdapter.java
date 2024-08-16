package zc;

import java.util.Collection;
import java.util.Set;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;

/* compiled from: AbstractScopeAdapter.kt */
/* renamed from: zc.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractScopeAdapter implements h {
    @Override // zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return i().a(name, bVar);
    }

    @Override // zc.h
    public Set<Name> b() {
        return i().b();
    }

    @Override // zc.h
    public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return i().c(name, bVar);
    }

    @Override // zc.h
    public Set<Name> d() {
        return i().d();
    }

    @Override // zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return i().e(name, bVar);
    }

    @Override // zc.h
    public Set<Name> f() {
        return i().f();
    }

    @Override // zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(d dVar, ya.l<? super Name, Boolean> lVar) {
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        return i().g(dVar, lVar);
    }

    public final h h() {
        if (i() instanceof AbstractScopeAdapter) {
            h i10 = i();
            za.k.c(i10, "null cannot be cast to non-null type org.jetbrains.kotlin.resolve.scopes.AbstractScopeAdapter");
            return ((AbstractScopeAdapter) i10).h();
        }
        return i();
    }

    protected abstract h i();
}
