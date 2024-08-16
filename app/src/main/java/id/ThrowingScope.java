package id;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;

/* compiled from: ThrowingScope.kt */
/* renamed from: id.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class ThrowingScope extends ErrorScope {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ThrowingScope(ErrorScopeKind errorScopeKind, String... strArr) {
        super(errorScopeKind, (String[]) Arrays.copyOf(strArr, strArr.length));
        za.k.e(errorScopeKind, "kind");
        za.k.e(strArr, "formatParams");
    }

    @Override // id.ErrorScope, zc.h
    public Set<Name> b() {
        throw new IllegalStateException();
    }

    @Override // id.ErrorScope, zc.h
    public Set<Name> d() {
        throw new IllegalStateException();
    }

    @Override // id.ErrorScope, zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        throw new IllegalStateException(j() + ", required name: " + name);
    }

    @Override // id.ErrorScope, zc.h
    public Set<Name> f() {
        throw new IllegalStateException();
    }

    @Override // id.ErrorScope, zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        throw new IllegalStateException(j());
    }

    @Override // id.ErrorScope, zc.h
    /* renamed from: h */
    public Set<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        throw new IllegalStateException(j() + ", required name: " + name);
    }

    @Override // id.ErrorScope, zc.h
    /* renamed from: i */
    public Set<PropertyDescriptor> a(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        throw new IllegalStateException(j() + ", required name: " + name);
    }

    @Override // id.ErrorScope
    public String toString() {
        return "ThrowingScope{" + j() + '}';
    }
}
