package cc;

import ac.JavaClassDescriptor;
import cd.ErrorReporter;
import fb.PrimitiveRanges;
import fc.x;
import fc.y;
import gd.AbstractClassTypeConstructor;
import gd.TypeConstructor;
import gd.TypeProjectionImpl;
import gd.TypeUsage;
import gd.Variance;
import gd.c1;
import gd.g0;
import gd.h0;
import gd.o0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.PrimitiveIterators;
import kotlin.collections._Collections;
import kotlin.collections.r;
import kotlin.collections.s;
import kotlin.collections.s0;
import mb.KotlinBuiltIns;
import ob.mappingUtil;
import oc.ClassId;
import oc.FqName;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.Modality;
import pb.NotFoundClasses;
import pb.ScopesHolderForClass;
import pb.SupertypeLoopChecker;
import pb.TypeParameterDescriptor;
import pb.ValueClassRepresentation;
import pb.g1;
import pb.n1;
import pb.u;
import qb.AnnotationDescriptor;
import qd.collections;
import sb.ClassDescriptorBase;
import uc.v;
import yb.FakePureImplementationsProvider;
import yb.JavaDescriptorVisibilities;
import yb.b0;
import yb.j0;
import za.DefaultConstructorMarker;
import za.Lambda;
import zb.JavaResolverCache;
import zc.InnerClassesScopeWrapper;

/* compiled from: LazyJavaClassDescriptor.kt */
/* renamed from: cc.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class LazyJavaClassDescriptor extends ClassDescriptorBase implements JavaClassDescriptor {
    public static final a C = new a(null);
    private static final Set<String> D;
    private final qb.g A;
    private final fd.i<List<TypeParameterDescriptor>> B;

    /* renamed from: m, reason: collision with root package name */
    private final bc.g f5067m;

    /* renamed from: n, reason: collision with root package name */
    private final fc.g f5068n;

    /* renamed from: o, reason: collision with root package name */
    private final ClassDescriptor f5069o;

    /* renamed from: p, reason: collision with root package name */
    private final bc.g f5070p;

    /* renamed from: q, reason: collision with root package name */
    private final ma.h f5071q;

    /* renamed from: r, reason: collision with root package name */
    private final ClassKind f5072r;

    /* renamed from: s, reason: collision with root package name */
    private final Modality f5073s;

    /* renamed from: t, reason: collision with root package name */
    private final n1 f5074t;

    /* renamed from: u, reason: collision with root package name */
    private final boolean f5075u;

    /* renamed from: v, reason: collision with root package name */
    private final b f5076v;

    /* renamed from: w, reason: collision with root package name */
    private final LazyJavaClassMemberScope f5077w;

    /* renamed from: x, reason: collision with root package name */
    private final ScopesHolderForClass<LazyJavaClassMemberScope> f5078x;

    /* renamed from: y, reason: collision with root package name */
    private final InnerClassesScopeWrapper f5079y;

    /* renamed from: z, reason: collision with root package name */
    private final LazyJavaStaticClassScope f5080z;

    /* compiled from: LazyJavaClassDescriptor.kt */
    /* renamed from: cc.f$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LazyJavaClassDescriptor.kt */
    /* renamed from: cc.f$b */
    /* loaded from: classes2.dex */
    public final class b extends AbstractClassTypeConstructor {

        /* renamed from: d, reason: collision with root package name */
        private final fd.i<List<TypeParameterDescriptor>> f5081d;

        /* compiled from: LazyJavaClassDescriptor.kt */
        /* renamed from: cc.f$b$a */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.a<List<? extends TypeParameterDescriptor>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ LazyJavaClassDescriptor f5083e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(LazyJavaClassDescriptor lazyJavaClassDescriptor) {
                super(0);
                this.f5083e = lazyJavaClassDescriptor;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<TypeParameterDescriptor> invoke() {
                return g1.d(this.f5083e);
            }
        }

        public b() {
            super(LazyJavaClassDescriptor.this.f5070p.e());
            this.f5081d = LazyJavaClassDescriptor.this.f5070p.e().g(new a(LazyJavaClassDescriptor.this));
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x0019, code lost:
        
            if ((!r0.d() && r0.i(mb.StandardNames.f15282t)) != false) goto L13;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private final g0 x() {
            FqName fqName;
            Object q02;
            int u7;
            ArrayList arrayList;
            int u10;
            FqName y4 = y();
            if (y4 != null) {
            }
            y4 = null;
            if (y4 == null) {
                fqName = FakePureImplementationsProvider.f20121a.b(wc.c.l(LazyJavaClassDescriptor.this));
                if (fqName == null) {
                    return null;
                }
            } else {
                fqName = y4;
            }
            ClassDescriptor v7 = wc.c.v(LazyJavaClassDescriptor.this.f5070p.d(), fqName, xb.d.FROM_JAVA_LOADER);
            if (v7 == null) {
                return null;
            }
            int size = v7.n().getParameters().size();
            List<TypeParameterDescriptor> parameters = LazyJavaClassDescriptor.this.n().getParameters();
            za.k.d(parameters, "getTypeConstructor().parameters");
            int size2 = parameters.size();
            if (size2 == size) {
                u10 = s.u(parameters, 10);
                arrayList = new ArrayList(u10);
                Iterator<T> it = parameters.iterator();
                while (it.hasNext()) {
                    arrayList.add(new TypeProjectionImpl(Variance.INVARIANT, ((TypeParameterDescriptor) it.next()).x()));
                }
            } else {
                if (size2 != 1 || size <= 1 || y4 != null) {
                    return null;
                }
                Variance variance = Variance.INVARIANT;
                q02 = _Collections.q0(parameters);
                TypeProjectionImpl typeProjectionImpl = new TypeProjectionImpl(variance, ((TypeParameterDescriptor) q02).x());
                PrimitiveRanges primitiveRanges = new PrimitiveRanges(1, size);
                u7 = s.u(primitiveRanges, 10);
                ArrayList arrayList2 = new ArrayList(u7);
                Iterator<Integer> it2 = primitiveRanges.iterator();
                while (it2.hasNext()) {
                    ((PrimitiveIterators) it2).b();
                    arrayList2.add(typeProjectionImpl);
                }
                arrayList = arrayList2;
            }
            return h0.g(c1.f11749f.h(), v7, arrayList);
        }

        private final FqName y() {
            Object r02;
            String b10;
            qb.g i10 = LazyJavaClassDescriptor.this.i();
            FqName fqName = b0.f20034q;
            za.k.d(fqName, "PURELY_IMPLEMENTS_ANNOTATION");
            AnnotationDescriptor j10 = i10.j(fqName);
            if (j10 == null) {
                return null;
            }
            r02 = _Collections.r0(j10.a().values());
            v vVar = r02 instanceof v ? (v) r02 : null;
            if (vVar == null || (b10 = vVar.b()) == null || !oc.e.e(b10)) {
                return null;
            }
            return new FqName(b10);
        }

        @Override // gd.TypeConstructor
        public List<TypeParameterDescriptor> getParameters() {
            return this.f5081d.invoke();
        }

        @Override // gd.AbstractTypeConstructor
        protected Collection<g0> h() {
            List e10;
            List z02;
            int u7;
            Collection<fc.j> q10 = LazyJavaClassDescriptor.this.Y0().q();
            ArrayList arrayList = new ArrayList(q10.size());
            ArrayList<x> arrayList2 = new ArrayList(0);
            g0 x10 = x();
            Iterator<fc.j> it = q10.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                fc.j next = it.next();
                g0 h10 = LazyJavaClassDescriptor.this.f5070p.a().r().h(LazyJavaClassDescriptor.this.f5070p.g().o(next, dc.b.b(TypeUsage.SUPERTYPE, false, false, null, 7, null)), LazyJavaClassDescriptor.this.f5070p);
                if (h10.W0().v() instanceof NotFoundClasses.b) {
                    arrayList2.add(next);
                }
                if (!za.k.a(h10.W0(), x10 != null ? x10.W0() : null) && !KotlinBuiltIns.b0(h10)) {
                    arrayList.add(h10);
                }
            }
            ClassDescriptor classDescriptor = LazyJavaClassDescriptor.this.f5069o;
            collections.a(arrayList, classDescriptor != null ? mappingUtil.a(classDescriptor, LazyJavaClassDescriptor.this).c().p(classDescriptor.x(), Variance.INVARIANT) : null);
            collections.a(arrayList, x10);
            if (!arrayList2.isEmpty()) {
                ErrorReporter c10 = LazyJavaClassDescriptor.this.f5070p.a().c();
                ClassDescriptor v7 = v();
                u7 = s.u(arrayList2, 10);
                ArrayList arrayList3 = new ArrayList(u7);
                for (x xVar : arrayList2) {
                    za.k.c(xVar, "null cannot be cast to non-null type org.jetbrains.kotlin.load.java.structure.JavaClassifierType");
                    arrayList3.add(((fc.j) xVar).x());
                }
                c10.a(v7, arrayList3);
            }
            if (!arrayList.isEmpty()) {
                z02 = _Collections.z0(arrayList);
                return z02;
            }
            e10 = CollectionsJVM.e(LazyJavaClassDescriptor.this.f5070p.d().t().i());
            return e10;
        }

        @Override // gd.AbstractTypeConstructor
        protected SupertypeLoopChecker l() {
            return LazyJavaClassDescriptor.this.f5070p.a().v();
        }

        @Override // gd.ClassifierBasedTypeConstructor, gd.TypeConstructor
        /* renamed from: s, reason: merged with bridge method [inline-methods] */
        public ClassDescriptor v() {
            return LazyJavaClassDescriptor.this;
        }

        public String toString() {
            String b10 = LazyJavaClassDescriptor.this.getName().b();
            za.k.d(b10, "name.asString()");
            return b10;
        }

        @Override // gd.TypeConstructor
        public boolean w() {
            return true;
        }
    }

    /* compiled from: LazyJavaClassDescriptor.kt */
    /* renamed from: cc.f$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<List<? extends TypeParameterDescriptor>> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<TypeParameterDescriptor> invoke() {
            int u7;
            List<y> m10 = LazyJavaClassDescriptor.this.Y0().m();
            LazyJavaClassDescriptor lazyJavaClassDescriptor = LazyJavaClassDescriptor.this;
            u7 = s.u(m10, 10);
            ArrayList arrayList = new ArrayList(u7);
            for (y yVar : m10) {
                TypeParameterDescriptor a10 = lazyJavaClassDescriptor.f5070p.f().a(yVar);
                if (a10 != null) {
                    arrayList.add(a10);
                } else {
                    throw new AssertionError("Parameter " + yVar + " surely belongs to class " + lazyJavaClassDescriptor.Y0() + ", so it must be resolved");
                }
            }
            return arrayList;
        }
    }

    /* compiled from: Comparisons.kt */
    /* renamed from: cc.f$d */
    /* loaded from: classes2.dex */
    public static final class d<T> implements Comparator {
        @Override // java.util.Comparator
        public final int compare(T t7, T t10) {
            int a10;
            a10 = pa.b.a(wc.c.l((ClassDescriptor) t7).b(), wc.c.l((ClassDescriptor) t10).b());
            return a10;
        }
    }

    /* compiled from: LazyJavaClassDescriptor.kt */
    /* renamed from: cc.f$e */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.a<List<? extends fc.a>> {
        e() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<fc.a> invoke() {
            ClassId k10 = wc.c.k(LazyJavaClassDescriptor.this);
            if (k10 != null) {
                return LazyJavaClassDescriptor.this.a1().a().f().a(k10);
            }
            return null;
        }
    }

    /* compiled from: LazyJavaClassDescriptor.kt */
    /* renamed from: cc.f$f */
    /* loaded from: classes2.dex */
    static final class f extends Lambda implements ya.l<hd.g, LazyJavaClassMemberScope> {
        f() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final LazyJavaClassMemberScope invoke(hd.g gVar) {
            za.k.e(gVar, "it");
            bc.g gVar2 = LazyJavaClassDescriptor.this.f5070p;
            LazyJavaClassDescriptor lazyJavaClassDescriptor = LazyJavaClassDescriptor.this;
            return new LazyJavaClassMemberScope(gVar2, lazyJavaClassDescriptor, lazyJavaClassDescriptor.Y0(), LazyJavaClassDescriptor.this.f5069o != null, LazyJavaClassDescriptor.this.f5077w);
        }
    }

    static {
        Set<String> h10;
        h10 = s0.h("equals", "hashCode", "getClass", "wait", "notify", "notifyAll", "toString");
        D = h10;
    }

    public /* synthetic */ LazyJavaClassDescriptor(bc.g gVar, DeclarationDescriptor declarationDescriptor, fc.g gVar2, ClassDescriptor classDescriptor, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(gVar, declarationDescriptor, gVar2, (i10 & 8) != 0 ? null : classDescriptor);
    }

    @Override // pb.ClassDescriptor, pb.ClassifierDescriptorWithTypeParameters
    public List<TypeParameterDescriptor> B() {
        return this.B.invoke();
    }

    @Override // pb.ClassDescriptor
    public boolean F() {
        return false;
    }

    @Override // sb.AbstractClassDescriptor, pb.ClassDescriptor
    public zc.h F0() {
        return this.f5079y;
    }

    @Override // pb.ClassDescriptor
    public ValueClassRepresentation<o0> G0() {
        return null;
    }

    @Override // pb.ClassDescriptor
    public boolean L() {
        return false;
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public boolean R0() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public Collection<ClassDescriptor> S() {
        List j10;
        List u02;
        if (this.f5073s == Modality.SEALED) {
            dc.a b10 = dc.b.b(TypeUsage.COMMON, false, false, null, 7, null);
            Collection<fc.j> T = this.f5068n.T();
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = T.iterator();
            while (it.hasNext()) {
                ClassifierDescriptor v7 = this.f5070p.g().o((fc.j) it.next(), b10).W0().v();
                ClassDescriptor classDescriptor = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
                if (classDescriptor != null) {
                    arrayList.add(classDescriptor);
                }
            }
            u02 = _Collections.u0(arrayList, new d());
            return u02;
        }
        j10 = r.j();
        return j10;
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        return false;
    }

    public final LazyJavaClassDescriptor W0(JavaResolverCache javaResolverCache, ClassDescriptor classDescriptor) {
        za.k.e(javaResolverCache, "javaResolverCache");
        bc.g gVar = this.f5070p;
        bc.g i10 = bc.a.i(gVar, gVar.a().x(javaResolverCache));
        DeclarationDescriptor b10 = b();
        za.k.d(b10, "containingDeclaration");
        return new LazyJavaClassDescriptor(i10, b10, this.f5068n, classDescriptor);
    }

    @Override // pb.ClassDescriptor
    /* renamed from: X0, reason: merged with bridge method [inline-methods] */
    public List<ClassConstructorDescriptor> p() {
        return this.f5077w.x0().invoke();
    }

    public final fc.g Y0() {
        return this.f5068n;
    }

    @Override // pb.ClassDescriptor
    public ClassConstructorDescriptor Z() {
        return null;
    }

    public final List<fc.a> Z0() {
        return (List) this.f5071q.getValue();
    }

    @Override // pb.ClassDescriptor
    public zc.h a0() {
        return this.f5080z;
    }

    public final bc.g a1() {
        return this.f5067m;
    }

    @Override // sb.AbstractClassDescriptor, pb.ClassDescriptor
    /* renamed from: b1, reason: merged with bridge method [inline-methods] */
    public LazyJavaClassMemberScope M0() {
        zc.h M0 = super.M0();
        za.k.c(M0, "null cannot be cast to non-null type org.jetbrains.kotlin.load.java.lazy.descriptors.LazyJavaClassMemberScope");
        return (LazyJavaClassMemberScope) M0;
    }

    @Override // pb.ClassDescriptor
    public ClassDescriptor c0() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sb.t
    /* renamed from: c1, reason: merged with bridge method [inline-methods] */
    public LazyJavaClassMemberScope Q(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this.f5078x.c(gVar);
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public u g() {
        if (za.k.a(this.f5074t, DescriptorVisibilities.f16729a) && this.f5068n.u() == null) {
            u uVar = JavaDescriptorVisibilities.f20131a;
            za.k.d(uVar, "{\n            JavaDescri…KAGE_VISIBILITY\n        }");
            return uVar;
        }
        return j0.d(this.f5074t);
    }

    @Override // pb.ClassDescriptor
    public ClassKind getKind() {
        return this.f5072r;
    }

    @Override // qb.a
    public qb.g i() {
        return this.A;
    }

    @Override // pb.ClassifierDescriptor
    public TypeConstructor n() {
        return this.f5076v;
    }

    @Override // pb.ClassDescriptor, pb.MemberDescriptor
    public Modality o() {
        return this.f5073s;
    }

    @Override // pb.ClassDescriptor
    public boolean q() {
        return false;
    }

    @Override // pb.ClassifierDescriptorWithTypeParameters
    public boolean r() {
        return this.f5075u;
    }

    public String toString() {
        return "Lazy Java class " + wc.c.m(this);
    }

    @Override // pb.ClassDescriptor
    public boolean y() {
        return false;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyJavaClassDescriptor(bc.g gVar, DeclarationDescriptor declarationDescriptor, fc.g gVar2, ClassDescriptor classDescriptor) {
        super(gVar.e(), declarationDescriptor, gVar2.getName(), gVar.a().t().a(gVar2), false);
        ma.h b10;
        ClassKind classKind;
        Modality modality;
        za.k.e(gVar, "outerContext");
        za.k.e(declarationDescriptor, "containingDeclaration");
        za.k.e(gVar2, "jClass");
        this.f5067m = gVar;
        this.f5068n = gVar2;
        this.f5069o = classDescriptor;
        bc.g d10 = bc.a.d(gVar, this, gVar2, 0, 4, null);
        this.f5070p = d10;
        d10.a().h().c(gVar2, this);
        gVar2.O();
        b10 = ma.j.b(new e());
        this.f5071q = b10;
        if (gVar2.y()) {
            classKind = ClassKind.ANNOTATION_CLASS;
        } else if (gVar2.N()) {
            classKind = ClassKind.INTERFACE;
        } else {
            classKind = gVar2.I() ? ClassKind.ENUM_CLASS : ClassKind.CLASS;
        }
        this.f5072r = classKind;
        if (!gVar2.y() && !gVar2.I()) {
            modality = Modality.f16676e.a(gVar2.t(), gVar2.t() || gVar2.n() || gVar2.N(), !gVar2.w());
        } else {
            modality = Modality.FINAL;
        }
        this.f5073s = modality;
        this.f5074t = gVar2.g();
        this.f5075u = (gVar2.u() == null || gVar2.o()) ? false : true;
        this.f5076v = new b();
        LazyJavaClassMemberScope lazyJavaClassMemberScope = new LazyJavaClassMemberScope(d10, this, gVar2, classDescriptor != null, null, 16, null);
        this.f5077w = lazyJavaClassMemberScope;
        this.f5078x = ScopesHolderForClass.f16749e.a(this, d10.e(), d10.a().k().c(), new f());
        this.f5079y = new InnerClassesScopeWrapper(lazyJavaClassMemberScope);
        this.f5080z = new LazyJavaStaticClassScope(d10, gVar2, this);
        this.A = bc.e.a(d10, gVar2);
        this.B = d10.e().g(new c());
    }
}
