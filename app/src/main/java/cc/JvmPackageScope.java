package cc;

import fc.u;
import hc.KotlinJvmBinaryClass;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.collections.MutableCollections;
import kotlin.collections._Arrays;
import kotlin.collections.s0;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.ClassifierDescriptorWithTypeParameters;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import pd.scopeUtils;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: JvmPackageScope.kt */
/* renamed from: cc.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmPackageScope implements zc.h {

    /* renamed from: f, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f5049f = {Reflection.g(new PropertyReference1Impl(Reflection.b(JvmPackageScope.class), "kotlinScopes", "getKotlinScopes()[Lorg/jetbrains/kotlin/resolve/scopes/MemberScope;"))};

    /* renamed from: b, reason: collision with root package name */
    private final bc.g f5050b;

    /* renamed from: c, reason: collision with root package name */
    private final LazyJavaPackageFragment f5051c;

    /* renamed from: d, reason: collision with root package name */
    private final LazyJavaPackageScope f5052d;

    /* renamed from: e, reason: collision with root package name */
    private final fd.i f5053e;

    /* compiled from: JvmPackageScope.kt */
    /* renamed from: cc.d$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<zc.h[]> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final zc.h[] invoke() {
            Collection<KotlinJvmBinaryClass> values = JvmPackageScope.this.f5051c.V0().values();
            JvmPackageScope jvmPackageScope = JvmPackageScope.this;
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = values.iterator();
            while (it.hasNext()) {
                zc.h b10 = jvmPackageScope.f5050b.a().b().b(jvmPackageScope.f5051c, (KotlinJvmBinaryClass) it.next());
                if (b10 != null) {
                    arrayList.add(b10);
                }
            }
            return (zc.h[]) scopeUtils.b(arrayList).toArray(new zc.h[0]);
        }
    }

    public JvmPackageScope(bc.g gVar, u uVar, LazyJavaPackageFragment lazyJavaPackageFragment) {
        za.k.e(gVar, "c");
        za.k.e(uVar, "jPackage");
        za.k.e(lazyJavaPackageFragment, "packageFragment");
        this.f5050b = gVar;
        this.f5051c = lazyJavaPackageFragment;
        this.f5052d = new LazyJavaPackageScope(gVar, uVar, lazyJavaPackageFragment);
        this.f5053e = gVar.e().g(new a());
    }

    private final zc.h[] k() {
        return (zc.h[]) fd.m.a(this.f5053e, this, f5049f[0]);
    }

    @Override // zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        Set e10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        l(name, bVar);
        LazyJavaPackageScope lazyJavaPackageScope = this.f5052d;
        zc.h[] k10 = k();
        Collection<? extends PropertyDescriptor> a10 = lazyJavaPackageScope.a(name, bVar);
        int length = k10.length;
        int i10 = 0;
        Collection collection = a10;
        while (i10 < length) {
            Collection a11 = scopeUtils.a(collection, k10[i10].a(name, bVar));
            i10++;
            collection = a11;
        }
        if (collection != null) {
            return collection;
        }
        e10 = s0.e();
        return e10;
    }

    @Override // zc.h
    public Set<Name> b() {
        zc.h[] k10 = k();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (zc.h hVar : k10) {
            MutableCollections.z(linkedHashSet, hVar.b());
        }
        linkedHashSet.addAll(this.f5052d.b());
        return linkedHashSet;
    }

    @Override // zc.h
    public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        Set e10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        l(name, bVar);
        LazyJavaPackageScope lazyJavaPackageScope = this.f5052d;
        zc.h[] k10 = k();
        Collection<? extends SimpleFunctionDescriptor> c10 = lazyJavaPackageScope.c(name, bVar);
        int length = k10.length;
        int i10 = 0;
        Collection collection = c10;
        while (i10 < length) {
            Collection a10 = scopeUtils.a(collection, k10[i10].c(name, bVar));
            i10++;
            collection = a10;
        }
        if (collection != null) {
            return collection;
        }
        e10 = s0.e();
        return e10;
    }

    @Override // zc.h
    public Set<Name> d() {
        zc.h[] k10 = k();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (zc.h hVar : k10) {
            MutableCollections.z(linkedHashSet, hVar.d());
        }
        linkedHashSet.addAll(this.f5052d.d());
        return linkedHashSet;
    }

    @Override // zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        l(name, bVar);
        ClassDescriptor e10 = this.f5052d.e(name, bVar);
        if (e10 != null) {
            return e10;
        }
        ClassifierDescriptor classifierDescriptor = null;
        for (zc.h hVar : k()) {
            ClassifierDescriptor e11 = hVar.e(name, bVar);
            if (e11 != null) {
                if (!(e11 instanceof ClassifierDescriptorWithTypeParameters) || !((ClassifierDescriptorWithTypeParameters) e11).U()) {
                    return e11;
                }
                if (classifierDescriptor == null) {
                    classifierDescriptor = e11;
                }
            }
        }
        return classifierDescriptor;
    }

    @Override // zc.h
    public Set<Name> f() {
        Iterable q10;
        q10 = _Arrays.q(k());
        Set<Name> a10 = zc.j.a(q10);
        if (a10 == null) {
            return null;
        }
        a10.addAll(this.f5052d.f());
        return a10;
    }

    @Override // zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        Set e10;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        LazyJavaPackageScope lazyJavaPackageScope = this.f5052d;
        zc.h[] k10 = k();
        Collection<DeclarationDescriptor> g6 = lazyJavaPackageScope.g(dVar, lVar);
        for (zc.h hVar : k10) {
            g6 = scopeUtils.a(g6, hVar.g(dVar, lVar));
        }
        if (g6 != null) {
            return g6;
        }
        e10 = s0.e();
        return e10;
    }

    public final LazyJavaPackageScope j() {
        return this.f5052d;
    }

    public void l(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        wb.a.b(this.f5050b.a().l(), bVar, this.f5051c, name);
    }

    public String toString() {
        return "scope for " + this.f5051c;
    }
}
