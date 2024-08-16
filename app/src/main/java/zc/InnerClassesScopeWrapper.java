package zc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.collections.r;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.ClassifierDescriptorWithTypeParameters;
import pb.DeclarationDescriptor;
import pb.TypeAliasDescriptor;

/* compiled from: InnerClassesScopeWrapper.kt */
/* renamed from: zc.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class InnerClassesScopeWrapper extends MemberScopeImpl {

    /* renamed from: b, reason: collision with root package name */
    private final h f20458b;

    public InnerClassesScopeWrapper(h hVar) {
        za.k.e(hVar, "workerScope");
        this.f20458b = hVar;
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> b() {
        return this.f20458b.b();
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> d() {
        return this.f20458b.d();
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        ClassifierDescriptor e10 = this.f20458b.e(name, bVar);
        if (e10 == null) {
            return null;
        }
        ClassDescriptor classDescriptor = e10 instanceof ClassDescriptor ? (ClassDescriptor) e10 : null;
        if (classDescriptor != null) {
            return classDescriptor;
        }
        if (e10 instanceof TypeAliasDescriptor) {
            return (TypeAliasDescriptor) e10;
        }
        return null;
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> f() {
        return this.f20458b.f();
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public List<ClassifierDescriptor> g(d dVar, ya.l<? super Name, Boolean> lVar) {
        List<ClassifierDescriptor> j10;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        d n10 = dVar.n(d.f20424c.c());
        if (n10 == null) {
            j10 = r.j();
            return j10;
        }
        Collection<DeclarationDescriptor> g6 = this.f20458b.g(n10, lVar);
        ArrayList arrayList = new ArrayList();
        for (Object obj : g6) {
            if (obj instanceof ClassifierDescriptorWithTypeParameters) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }

    public String toString() {
        return "Classes from " + this.f20458b;
    }
}
