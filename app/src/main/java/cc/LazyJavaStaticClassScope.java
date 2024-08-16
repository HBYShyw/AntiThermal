package cc;

import ac.JavaClassDescriptor;
import fc.q;
import gd.g0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.MutableCollections;
import kotlin.collections._Collections;
import kotlin.collections.r;
import kotlin.collections.s;
import kotlin.collections.s0;
import ma.Unit;
import mb.StandardNames;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import qd.DFS;
import qd.collections;
import rd.Sequence;
import rd._Sequences;
import sc.DescriptorFactory;
import za.Lambda;
import zb.DescriptorResolverUtils;

/* compiled from: LazyJavaStaticClassScope.kt */
/* renamed from: cc.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class LazyJavaStaticClassScope extends LazyJavaStaticScope {

    /* renamed from: n, reason: collision with root package name */
    private final fc.g f5171n;

    /* renamed from: o, reason: collision with root package name */
    private final JavaClassDescriptor f5172o;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LazyJavaStaticClassScope.kt */
    /* renamed from: cc.l$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<q, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f5173e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(q qVar) {
            za.k.e(qVar, "it");
            return Boolean.valueOf(qVar.o());
        }
    }

    /* compiled from: LazyJavaStaticClassScope.kt */
    /* renamed from: cc.l$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<zc.h, Collection<? extends PropertyDescriptor>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Name f5174e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(Name name) {
            super(1);
            this.f5174e = name;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<? extends PropertyDescriptor> invoke(zc.h hVar) {
            za.k.e(hVar, "it");
            return hVar.a(this.f5174e, xb.d.WHEN_GET_SUPER_MEMBERS);
        }
    }

    /* compiled from: LazyJavaStaticClassScope.kt */
    /* renamed from: cc.l$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.l<zc.h, Collection<? extends Name>> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f5175e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<Name> invoke(zc.h hVar) {
            za.k.e(hVar, "it");
            return hVar.d();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LazyJavaStaticClassScope.kt */
    /* renamed from: cc.l$d */
    /* loaded from: classes2.dex */
    public static final class d extends Lambda implements ya.l<g0, ClassDescriptor> {

        /* renamed from: e, reason: collision with root package name */
        public static final d f5176e = new d();

        d() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ClassDescriptor invoke(g0 g0Var) {
            ClassifierDescriptor v7 = g0Var.W0().v();
            if (v7 instanceof ClassDescriptor) {
                return (ClassDescriptor) v7;
            }
            return null;
        }
    }

    /* compiled from: LazyJavaStaticClassScope.kt */
    /* renamed from: cc.l$e */
    /* loaded from: classes2.dex */
    public static final class e extends DFS.b<ClassDescriptor, Unit> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ClassDescriptor f5177a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ Set<R> f5178b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ ya.l<zc.h, Collection<R>> f5179c;

        /* JADX WARN: Multi-variable type inference failed */
        e(ClassDescriptor classDescriptor, Set<R> set, ya.l<? super zc.h, ? extends Collection<? extends R>> lVar) {
            this.f5177a = classDescriptor;
            this.f5178b = set;
            this.f5179c = lVar;
        }

        @Override // qd.DFS.d
        public /* bridge */ /* synthetic */ Object a() {
            e();
            return Unit.f15173a;
        }

        @Override // qd.DFS.d
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public boolean c(ClassDescriptor classDescriptor) {
            za.k.e(classDescriptor, "current");
            if (classDescriptor == this.f5177a) {
                return true;
            }
            zc.h a02 = classDescriptor.a0();
            za.k.d(a02, "current.staticScope");
            if (!(a02 instanceof LazyJavaStaticScope)) {
                return true;
            }
            this.f5178b.addAll((Collection) this.f5179c.invoke(a02));
            return false;
        }

        public void e() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyJavaStaticClassScope(bc.g gVar, fc.g gVar2, JavaClassDescriptor javaClassDescriptor) {
        super(gVar);
        za.k.e(gVar, "c");
        za.k.e(gVar2, "jClass");
        za.k.e(javaClassDescriptor, "ownerDescriptor");
        this.f5171n = gVar2;
        this.f5172o = javaClassDescriptor;
    }

    private final <R> Set<R> O(ClassDescriptor classDescriptor, Set<R> set, ya.l<? super zc.h, ? extends Collection<? extends R>> lVar) {
        List e10;
        e10 = CollectionsJVM.e(classDescriptor);
        DFS.b(e10, k.f5170a, new e(classDescriptor, set, lVar));
        return set;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Iterable P(ClassDescriptor classDescriptor) {
        Sequence K;
        Sequence x10;
        Iterable i10;
        Collection<g0> q10 = classDescriptor.n().q();
        za.k.d(q10, "it.typeConstructor.supertypes");
        K = _Collections.K(q10);
        x10 = _Sequences.x(K, d.f5176e);
        i10 = _Sequences.i(x10);
        return i10;
    }

    private final PropertyDescriptor R(PropertyDescriptor propertyDescriptor) {
        int u7;
        List M;
        Object q02;
        if (propertyDescriptor.getKind().a()) {
            return propertyDescriptor;
        }
        Collection<? extends PropertyDescriptor> e10 = propertyDescriptor.e();
        za.k.d(e10, "this.overriddenDescriptors");
        u7 = s.u(e10, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (PropertyDescriptor propertyDescriptor2 : e10) {
            za.k.d(propertyDescriptor2, "it");
            arrayList.add(R(propertyDescriptor2));
        }
        M = _Collections.M(arrayList);
        q02 = _Collections.q0(M);
        return (PropertyDescriptor) q02;
    }

    private final Set<SimpleFunctionDescriptor> S(Name name, ClassDescriptor classDescriptor) {
        Set<SimpleFunctionDescriptor> D0;
        Set<SimpleFunctionDescriptor> e10;
        LazyJavaStaticClassScope b10 = ac.h.b(classDescriptor);
        if (b10 == null) {
            e10 = s0.e();
            return e10;
        }
        D0 = _Collections.D0(b10.c(name, xb.d.WHEN_GET_SUPER_MEMBERS));
        return D0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cc.LazyJavaScope
    /* renamed from: N, reason: merged with bridge method [inline-methods] */
    public cc.a p() {
        return new cc.a(this.f5171n, a.f5173e);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cc.LazyJavaScope
    /* renamed from: Q, reason: merged with bridge method [inline-methods] */
    public JavaClassDescriptor C() {
        return this.f5172o;
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return null;
    }

    @Override // cc.LazyJavaScope
    protected Set<Name> l(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        Set<Name> e10;
        za.k.e(dVar, "kindFilter");
        e10 = s0.e();
        return e10;
    }

    @Override // cc.LazyJavaScope
    protected Set<Name> n(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        Set<Name> C0;
        List m10;
        za.k.e(dVar, "kindFilter");
        C0 = _Collections.C0(y().invoke().a());
        LazyJavaStaticClassScope b10 = ac.h.b(C());
        Set<Name> b11 = b10 != null ? b10.b() : null;
        if (b11 == null) {
            b11 = s0.e();
        }
        C0.addAll(b11);
        if (this.f5171n.I()) {
            m10 = r.m(StandardNames.f15268f, StandardNames.f15266d);
            C0.addAll(m10);
        }
        C0.addAll(w().a().w().f(w(), C()));
        return C0;
    }

    @Override // cc.LazyJavaScope
    protected void o(Collection<SimpleFunctionDescriptor> collection, Name name) {
        za.k.e(collection, "result");
        za.k.e(name, "name");
        w().a().w().c(w(), C(), name, collection);
    }

    @Override // cc.LazyJavaScope
    protected void r(Collection<SimpleFunctionDescriptor> collection, Name name) {
        za.k.e(collection, "result");
        za.k.e(name, "name");
        Collection<? extends SimpleFunctionDescriptor> e10 = DescriptorResolverUtils.e(name, S(name, C()), collection, C(), w().a().c(), w().a().k().a());
        za.k.d(e10, "resolveOverridesForStati…rridingUtil\n            )");
        collection.addAll(e10);
        if (this.f5171n.I()) {
            if (za.k.a(name, StandardNames.f15268f)) {
                SimpleFunctionDescriptor g6 = DescriptorFactory.g(C());
                za.k.d(g6, "createEnumValueOfMethod(ownerDescriptor)");
                collection.add(g6);
            } else if (za.k.a(name, StandardNames.f15266d)) {
                SimpleFunctionDescriptor h10 = DescriptorFactory.h(C());
                za.k.d(h10, "createEnumValuesMethod(ownerDescriptor)");
                collection.add(h10);
            }
        }
    }

    @Override // cc.LazyJavaStaticScope, cc.LazyJavaScope
    protected void s(Name name, Collection<PropertyDescriptor> collection) {
        za.k.e(name, "name");
        za.k.e(collection, "result");
        Set O = O(C(), new LinkedHashSet(), new b(name));
        if (!collection.isEmpty()) {
            Collection<? extends PropertyDescriptor> e10 = DescriptorResolverUtils.e(name, O, collection, C(), w().a().c(), w().a().k().a());
            za.k.d(e10, "resolveOverridesForStati…ingUtil\n                )");
            collection.addAll(e10);
        } else {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Object obj : O) {
                PropertyDescriptor R = R((PropertyDescriptor) obj);
                Object obj2 = linkedHashMap.get(R);
                if (obj2 == null) {
                    obj2 = new ArrayList();
                    linkedHashMap.put(R, obj2);
                }
                ((List) obj2).add(obj);
            }
            ArrayList arrayList = new ArrayList();
            Iterator it = linkedHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Collection e11 = DescriptorResolverUtils.e(name, (Collection) ((Map.Entry) it.next()).getValue(), collection, C(), w().a().c(), w().a().k().a());
                za.k.d(e11, "resolveOverridesForStati…ingUtil\n                )");
                MutableCollections.z(arrayList, e11);
            }
            collection.addAll(arrayList);
        }
        if (this.f5171n.I() && za.k.a(name, StandardNames.f15267e)) {
            collections.a(collection, DescriptorFactory.f(C()));
        }
    }

    @Override // cc.LazyJavaScope
    protected Set<Name> t(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        Set<Name> C0;
        za.k.e(dVar, "kindFilter");
        C0 = _Collections.C0(y().invoke().f());
        O(C(), C0, c.f5175e);
        if (this.f5171n.I()) {
            C0.add(StandardNames.f15267e);
        }
        return C0;
    }
}
