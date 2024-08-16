package zc;

import gd.TypeSubstitutor;
import gd.n1;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.Substitutable;
import qd.collections;
import za.Lambda;
import zc.ResolutionScope;

/* compiled from: SubstitutingScope.kt */
/* renamed from: zc.m, reason: use source file name */
/* loaded from: classes2.dex */
public final class SubstitutingScope implements h {

    /* renamed from: b, reason: collision with root package name */
    private final h f20472b;

    /* renamed from: c, reason: collision with root package name */
    private final ma.h f20473c;

    /* renamed from: d, reason: collision with root package name */
    private final TypeSubstitutor f20474d;

    /* renamed from: e, reason: collision with root package name */
    private Map<DeclarationDescriptor, DeclarationDescriptor> f20475e;

    /* renamed from: f, reason: collision with root package name */
    private final ma.h f20476f;

    /* compiled from: SubstitutingScope.kt */
    /* renamed from: zc.m$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<Collection<? extends DeclarationDescriptor>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<DeclarationDescriptor> invoke() {
            SubstitutingScope substitutingScope = SubstitutingScope.this;
            return substitutingScope.k(ResolutionScope.a.a(substitutingScope.f20472b, null, null, 3, null));
        }
    }

    /* compiled from: SubstitutingScope.kt */
    /* renamed from: zc.m$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<TypeSubstitutor> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ TypeSubstitutor f20478e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(TypeSubstitutor typeSubstitutor) {
            super(0);
            this.f20478e = typeSubstitutor;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final TypeSubstitutor invoke() {
            return this.f20478e.j().c();
        }
    }

    public SubstitutingScope(h hVar, TypeSubstitutor typeSubstitutor) {
        ma.h b10;
        ma.h b11;
        za.k.e(hVar, "workerScope");
        za.k.e(typeSubstitutor, "givenSubstitutor");
        this.f20472b = hVar;
        b10 = ma.j.b(new b(typeSubstitutor));
        this.f20473c = b10;
        n1 j10 = typeSubstitutor.j();
        za.k.d(j10, "givenSubstitutor.substitution");
        this.f20474d = tc.d.f(j10, false, 1, null).c();
        b11 = ma.j.b(new a());
        this.f20476f = b11;
    }

    private final Collection<DeclarationDescriptor> j() {
        return (Collection) this.f20476f.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public final <D extends DeclarationDescriptor> Collection<D> k(Collection<? extends D> collection) {
        if (this.f20474d.k() || collection.isEmpty()) {
            return collection;
        }
        LinkedHashSet g6 = collections.g(collection.size());
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            g6.add(l((DeclarationDescriptor) it.next()));
        }
        return g6;
    }

    private final <D extends DeclarationDescriptor> D l(D d10) {
        if (this.f20474d.k()) {
            return d10;
        }
        if (this.f20475e == null) {
            this.f20475e = new HashMap();
        }
        Map<DeclarationDescriptor, DeclarationDescriptor> map = this.f20475e;
        za.k.b(map);
        DeclarationDescriptor declarationDescriptor = map.get(d10);
        if (declarationDescriptor == null) {
            if (!(d10 instanceof Substitutable)) {
                throw new IllegalStateException(("Unknown descriptor in scope: " + d10).toString());
            }
            declarationDescriptor = ((Substitutable) d10).c(this.f20474d);
            if (declarationDescriptor != null) {
                map.put(d10, declarationDescriptor);
            } else {
                throw new AssertionError("We expect that no conflict should happen while substitution is guaranteed to generate invariant projection, but " + d10 + " substitution fails");
            }
        }
        D d11 = (D) declarationDescriptor;
        za.k.c(d11, "null cannot be cast to non-null type D of org.jetbrains.kotlin.resolve.scopes.SubstitutingScope.substitute");
        return d11;
    }

    @Override // zc.h
    public Collection<? extends PropertyDescriptor> a(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return k(this.f20472b.a(name, bVar));
    }

    @Override // zc.h
    public Set<Name> b() {
        return this.f20472b.b();
    }

    @Override // zc.h
    public Collection<? extends SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return k(this.f20472b.c(name, bVar));
    }

    @Override // zc.h
    public Set<Name> d() {
        return this.f20472b.d();
    }

    @Override // zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        ClassifierDescriptor e10 = this.f20472b.e(name, bVar);
        if (e10 != null) {
            return (ClassifierDescriptor) l(e10);
        }
        return null;
    }

    @Override // zc.h
    public Set<Name> f() {
        return this.f20472b.f();
    }

    @Override // zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(d dVar, ya.l<? super Name, Boolean> lVar) {
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        return j();
    }
}
