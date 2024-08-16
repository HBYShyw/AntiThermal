package cc;

import cc.b;
import fc.d0;
import fc.u;
import hc.KotlinJvmBinaryClass;
import hc.p;
import hc.q;
import ic.KotlinClassHeader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections.r;
import kotlin.collections.s0;
import ma.NoWhenBranchMatchedException;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import oc.SpecialNames;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import qd.functions;
import yb.JavaClassFinder;
import za.DefaultConstructorMarker;
import za.Lambda;
import zc.d;

/* compiled from: LazyJavaPackageScope.kt */
/* renamed from: cc.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class LazyJavaPackageScope extends LazyJavaStaticScope {

    /* renamed from: n, reason: collision with root package name */
    private final u f5121n;

    /* renamed from: o, reason: collision with root package name */
    private final LazyJavaPackageFragment f5122o;

    /* renamed from: p, reason: collision with root package name */
    private final fd.j<Set<String>> f5123p;

    /* renamed from: q, reason: collision with root package name */
    private final fd.h<a, ClassDescriptor> f5124q;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LazyJavaPackageScope.kt */
    /* renamed from: cc.i$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final Name f5125a;

        /* renamed from: b, reason: collision with root package name */
        private final fc.g f5126b;

        public a(Name name, fc.g gVar) {
            za.k.e(name, "name");
            this.f5125a = name;
            this.f5126b = gVar;
        }

        public final fc.g a() {
            return this.f5126b;
        }

        public final Name b() {
            return this.f5125a;
        }

        public boolean equals(Object obj) {
            return (obj instanceof a) && za.k.a(this.f5125a, ((a) obj).f5125a);
        }

        public int hashCode() {
            return this.f5125a.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LazyJavaPackageScope.kt */
    /* renamed from: cc.i$b */
    /* loaded from: classes2.dex */
    public static abstract class b {

        /* compiled from: LazyJavaPackageScope.kt */
        /* renamed from: cc.i$b$a */
        /* loaded from: classes2.dex */
        public static final class a extends b {

            /* renamed from: a, reason: collision with root package name */
            private final ClassDescriptor f5127a;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public a(ClassDescriptor classDescriptor) {
                super(null);
                za.k.e(classDescriptor, "descriptor");
                this.f5127a = classDescriptor;
            }

            public final ClassDescriptor a() {
                return this.f5127a;
            }
        }

        /* compiled from: LazyJavaPackageScope.kt */
        /* renamed from: cc.i$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0015b extends b {

            /* renamed from: a, reason: collision with root package name */
            public static final C0015b f5128a = new C0015b();

            private C0015b() {
                super(null);
            }
        }

        /* compiled from: LazyJavaPackageScope.kt */
        /* renamed from: cc.i$b$c */
        /* loaded from: classes2.dex */
        public static final class c extends b {

            /* renamed from: a, reason: collision with root package name */
            public static final c f5129a = new c();

            private c() {
                super(null);
            }
        }

        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: LazyJavaPackageScope.kt */
    /* renamed from: cc.i$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.l<a, ClassDescriptor> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ bc.g f5131f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(bc.g gVar) {
            super(1);
            this.f5131f = gVar;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ClassDescriptor invoke(a aVar) {
            p.a b10;
            byte[] bArr;
            za.k.e(aVar, "request");
            ClassId classId = new ClassId(LazyJavaPackageScope.this.C().d(), aVar.b());
            if (aVar.a() != null) {
                b10 = this.f5131f.a().j().c(aVar.a());
            } else {
                b10 = this.f5131f.a().j().b(classId);
            }
            KotlinJvmBinaryClass a10 = b10 != null ? b10.a() : null;
            ClassId e10 = a10 != null ? a10.e() : null;
            if (e10 != null && (e10.l() || e10.k())) {
                return null;
            }
            b R = LazyJavaPackageScope.this.R(a10);
            if (R instanceof b.a) {
                return ((b.a) R).a();
            }
            if (R instanceof b.c) {
                return null;
            }
            if (R instanceof b.C0015b) {
                fc.g a11 = aVar.a();
                if (a11 == null) {
                    JavaClassFinder d10 = this.f5131f.a().d();
                    if (b10 != null) {
                        if (!(b10 instanceof p.a.C0047a)) {
                            b10 = null;
                        }
                        p.a.C0047a c0047a = (p.a.C0047a) b10;
                        if (c0047a != null) {
                            bArr = c0047a.b();
                            a11 = d10.a(new JavaClassFinder.a(classId, bArr, null, 4, null));
                        }
                    }
                    bArr = null;
                    a11 = d10.a(new JavaClassFinder.a(classId, bArr, null, 4, null));
                }
                fc.g gVar = a11;
                if ((gVar != null ? gVar.O() : null) != d0.BINARY) {
                    FqName d11 = gVar != null ? gVar.d() : null;
                    if (d11 == null || d11.d() || !za.k.a(d11.e(), LazyJavaPackageScope.this.C().d())) {
                        return null;
                    }
                    LazyJavaClassDescriptor lazyJavaClassDescriptor = new LazyJavaClassDescriptor(this.f5131f, LazyJavaPackageScope.this.C(), gVar, null, 8, null);
                    this.f5131f.a().e().a(lazyJavaClassDescriptor);
                    return lazyJavaClassDescriptor;
                }
                throw new IllegalStateException("Couldn't find kotlin binary class for light class created by kotlin binary file\nJavaClass: " + gVar + "\nClassId: " + classId + "\nfindKotlinClass(JavaClass) = " + q.a(this.f5131f.a().j(), gVar) + "\nfindKotlinClass(ClassId) = " + q.b(this.f5131f.a().j(), classId) + '\n');
            }
            throw new NoWhenBranchMatchedException();
        }
    }

    /* compiled from: LazyJavaPackageScope.kt */
    /* renamed from: cc.i$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.a<Set<? extends String>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ bc.g f5132e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ LazyJavaPackageScope f5133f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(bc.g gVar, LazyJavaPackageScope lazyJavaPackageScope) {
            super(0);
            this.f5132e = gVar;
            this.f5133f = lazyJavaPackageScope;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Set<String> invoke() {
            return this.f5132e.a().d().c(this.f5133f.C().d());
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyJavaPackageScope(bc.g gVar, u uVar, LazyJavaPackageFragment lazyJavaPackageFragment) {
        super(gVar);
        za.k.e(gVar, "c");
        za.k.e(uVar, "jPackage");
        za.k.e(lazyJavaPackageFragment, "ownerDescriptor");
        this.f5121n = uVar;
        this.f5122o = lazyJavaPackageFragment;
        this.f5123p = gVar.e().f(new d(gVar, this));
        this.f5124q = gVar.e().b(new c(gVar));
    }

    private final ClassDescriptor N(Name name, fc.g gVar) {
        if (!SpecialNames.f16446a.a(name)) {
            return null;
        }
        Set<String> invoke = this.f5123p.invoke();
        if (gVar != null || invoke == null || invoke.contains(name.b())) {
            return this.f5124q.invoke(new a(name, gVar));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final b R(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        if (kotlinJvmBinaryClass == null) {
            return b.C0015b.f5128a;
        }
        if (kotlinJvmBinaryClass.b().c() == KotlinClassHeader.a.CLASS) {
            ClassDescriptor k10 = w().a().b().k(kotlinJvmBinaryClass);
            return k10 != null ? new b.a(k10) : b.C0015b.f5128a;
        }
        return b.c.f5129a;
    }

    public final ClassDescriptor O(fc.g gVar) {
        za.k.e(gVar, "javaClass");
        return N(gVar.getName(), gVar);
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    /* renamed from: P, reason: merged with bridge method [inline-methods] */
    public ClassDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return N(name, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cc.LazyJavaScope
    /* renamed from: Q, reason: merged with bridge method [inline-methods] */
    public LazyJavaPackageFragment C() {
        return this.f5122o;
    }

    @Override // cc.LazyJavaScope, zc.MemberScopeImpl, zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        List j10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        j10 = r.j();
        return j10;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0060 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0033 A[SYNTHETIC] */
    @Override // cc.LazyJavaScope, zc.MemberScopeImpl, zc.ResolutionScope
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Collection<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        boolean z10;
        List j10;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        d.a aVar = zc.d.f20424c;
        if (!dVar.a(aVar.e() | aVar.c())) {
            j10 = r.j();
            return j10;
        }
        Collection<DeclarationDescriptor> invoke = v().invoke();
        ArrayList arrayList = new ArrayList();
        for (Object obj : invoke) {
            DeclarationDescriptor declarationDescriptor = (DeclarationDescriptor) obj;
            if (declarationDescriptor instanceof ClassDescriptor) {
                Name name = ((ClassDescriptor) declarationDescriptor).getName();
                za.k.d(name, "it.name");
                if (lVar.invoke(name).booleanValue()) {
                    z10 = true;
                    if (!z10) {
                        arrayList.add(obj);
                    }
                }
            }
            z10 = false;
            if (!z10) {
            }
        }
        return arrayList;
    }

    @Override // cc.LazyJavaScope
    protected Set<Name> l(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        Set<Name> e10;
        za.k.e(dVar, "kindFilter");
        if (!dVar.a(zc.d.f20424c.e())) {
            e10 = s0.e();
            return e10;
        }
        Set<String> invoke = this.f5123p.invoke();
        if (invoke != null) {
            HashSet hashSet = new HashSet();
            Iterator<T> it = invoke.iterator();
            while (it.hasNext()) {
                hashSet.add(Name.f((String) it.next()));
            }
            return hashSet;
        }
        u uVar = this.f5121n;
        if (lVar == null) {
            lVar = functions.a();
        }
        Collection<fc.g> s7 = uVar.s(lVar);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (fc.g gVar : s7) {
            Name name = gVar.O() == d0.SOURCE ? null : gVar.getName();
            if (name != null) {
                linkedHashSet.add(name);
            }
        }
        return linkedHashSet;
    }

    @Override // cc.LazyJavaScope
    protected Set<Name> n(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        Set<Name> e10;
        za.k.e(dVar, "kindFilter");
        e10 = s0.e();
        return e10;
    }

    @Override // cc.LazyJavaScope
    protected cc.b p() {
        return b.a.f5048a;
    }

    @Override // cc.LazyJavaScope
    protected void r(Collection<SimpleFunctionDescriptor> collection, Name name) {
        za.k.e(collection, "result");
        za.k.e(name, "name");
    }

    @Override // cc.LazyJavaScope
    protected Set<Name> t(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        Set<Name> e10;
        za.k.e(dVar, "kindFilter");
        e10 = s0.e();
        return e10;
    }
}
