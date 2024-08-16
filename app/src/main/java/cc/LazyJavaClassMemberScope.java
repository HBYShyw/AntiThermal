package cc;

import ac.JavaClassConstructorDescriptor;
import ac.JavaForKotlinOverridePropertyDescriptor;
import ac.JavaMethodDescriptor;
import ac.JavaPropertyDescriptor;
import cc.LazyJavaScope;
import cd.ErrorReporter;
import com.oplus.backup.sdk.common.utils.Constants;
import fb._Ranges;
import fc.q;
import fc.x;
import fc.y;
import gb.KDeclarationContainer;
import gd.TypeUsage;
import gd.g0;
import gd.s1;
import hc.w;
import hd.KotlinTypeChecker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.MapsJVM;
import kotlin.collections.MutableCollections;
import kotlin.collections._Collections;
import kotlin.collections._Sets;
import kotlin.collections.r;
import kotlin.collections.s;
import ma.o;
import mb.KotlinBuiltIns;
import mb.StandardNames;
import oc.ClassId;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;
import pb.CallableDescriptor;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.ConstructorDescriptor;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.PropertyDescriptor;
import pb.PropertyGetterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.u;
import qd.SmartSet;
import qd.collections;
import sb.ClassConstructorDescriptorImpl;
import sb.EnumEntrySyntheticClassDescriptor;
import sb.PropertyGetterDescriptorImpl;
import sb.PropertySetterDescriptorImpl;
import sb.SimpleFunctionDescriptorImpl;
import sb.ValueParameterDescriptorImpl;
import sc.DescriptorFactory;
import sc.OverridingUtil;
import yb.ClassicBuiltinSpecialProperties;
import yb.JavaClassFinder;
import yb.JavaDescriptorVisibilities;
import yb.JavaIncompatibilityRulesOverridabilityCondition;
import yb.JvmAbi;
import yb.SpecialGenericSignatures;
import yb.b0;
import yb.h0;
import yb.j0;
import yb.propertiesConventionUtil;
import za.DefaultConstructorMarker;
import za.FunctionReference;
import za.Lambda;
import za.Reflection;
import zb.DescriptorResolverUtils;
import zb.SignaturePropagator;

/* compiled from: LazyJavaClassMemberScope.kt */
/* renamed from: cc.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class LazyJavaClassMemberScope extends LazyJavaScope {

    /* renamed from: n, reason: collision with root package name */
    private final ClassDescriptor f5087n;

    /* renamed from: o, reason: collision with root package name */
    private final fc.g f5088o;

    /* renamed from: p, reason: collision with root package name */
    private final boolean f5089p;

    /* renamed from: q, reason: collision with root package name */
    private final fd.i<List<ClassConstructorDescriptor>> f5090q;

    /* renamed from: r, reason: collision with root package name */
    private final fd.i<Set<Name>> f5091r;

    /* renamed from: s, reason: collision with root package name */
    private final fd.i<Set<Name>> f5092s;

    /* renamed from: t, reason: collision with root package name */
    private final fd.i<Map<Name, fc.n>> f5093t;

    /* renamed from: u, reason: collision with root package name */
    private final fd.h<Name, ClassDescriptor> f5094u;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<q, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f5095e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(q qVar) {
            za.k.e(qVar, "it");
            return Boolean.valueOf(!qVar.o());
        }
    }

    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$b */
    /* loaded from: classes2.dex */
    /* synthetic */ class b extends FunctionReference implements ya.l<Name, Collection<? extends SimpleFunctionDescriptor>> {
        b(Object obj) {
            super(1, obj);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(LazyJavaClassMemberScope.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "searchMethodsByNameWithoutBuiltinMagic(Lorg/jetbrains/kotlin/name/Name;)Ljava/util/Collection;";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final Collection<SimpleFunctionDescriptor> invoke(Name name) {
            za.k.e(name, "p0");
            return ((LazyJavaClassMemberScope) this.f20351f).J0(name);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "searchMethodsByNameWithoutBuiltinMagic";
        }
    }

    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$c */
    /* loaded from: classes2.dex */
    /* synthetic */ class c extends FunctionReference implements ya.l<Name, Collection<? extends SimpleFunctionDescriptor>> {
        c(Object obj) {
            super(1, obj);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(LazyJavaClassMemberScope.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "searchMethodsInSupertypesWithoutBuiltinMagic(Lorg/jetbrains/kotlin/name/Name;)Ljava/util/Collection;";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final Collection<SimpleFunctionDescriptor> invoke(Name name) {
            za.k.e(name, "p0");
            return ((LazyJavaClassMemberScope) this.f20351f).K0(name);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "searchMethodsInSupertypesWithoutBuiltinMagic";
        }
    }

    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.l<Name, Collection<? extends SimpleFunctionDescriptor>> {
        d() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<SimpleFunctionDescriptor> invoke(Name name) {
            za.k.e(name, "it");
            return LazyJavaClassMemberScope.this.J0(name);
        }
    }

    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$e */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.l<Name, Collection<? extends SimpleFunctionDescriptor>> {
        e() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<SimpleFunctionDescriptor> invoke(Name name) {
            za.k.e(name, "it");
            return LazyJavaClassMemberScope.this.K0(name);
        }
    }

    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$f */
    /* loaded from: classes2.dex */
    static final class f extends Lambda implements ya.a<List<? extends ClassConstructorDescriptor>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ bc.g f5099f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        f(bc.g gVar) {
            super(0);
            this.f5099f = gVar;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v2, types: [java.util.List] */
        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<ClassConstructorDescriptor> invoke() {
            List<ClassConstructorDescriptor> z02;
            ?? n10;
            Collection<fc.k> p10 = LazyJavaClassMemberScope.this.f5088o.p();
            ArrayList arrayList = new ArrayList(p10.size());
            Iterator<fc.k> it = p10.iterator();
            while (it.hasNext()) {
                arrayList.add(LazyJavaClassMemberScope.this.H0(it.next()));
            }
            if (LazyJavaClassMemberScope.this.f5088o.A()) {
                ClassConstructorDescriptor f02 = LazyJavaClassMemberScope.this.f0();
                boolean z10 = false;
                String c10 = w.c(f02, false, false, 2, null);
                if (!arrayList.isEmpty()) {
                    Iterator it2 = arrayList.iterator();
                    while (it2.hasNext()) {
                        if (za.k.a(w.c((ClassConstructorDescriptor) it2.next(), false, false, 2, null), c10)) {
                            break;
                        }
                    }
                }
                z10 = true;
                if (z10) {
                    arrayList.add(f02);
                    this.f5099f.a().h().a(LazyJavaClassMemberScope.this.f5088o, f02);
                }
            }
            bc.g gVar = this.f5099f;
            gVar.a().w().d(gVar, LazyJavaClassMemberScope.this.C(), arrayList);
            gc.l r10 = this.f5099f.a().r();
            bc.g gVar2 = this.f5099f;
            LazyJavaClassMemberScope lazyJavaClassMemberScope = LazyJavaClassMemberScope.this;
            boolean isEmpty = arrayList.isEmpty();
            ArrayList arrayList2 = arrayList;
            if (isEmpty) {
                n10 = r.n(lazyJavaClassMemberScope.e0());
                arrayList2 = n10;
            }
            z02 = _Collections.z0(r10.g(gVar2, arrayList2));
            return z02;
        }
    }

    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$g */
    /* loaded from: classes2.dex */
    static final class g extends Lambda implements ya.a<Map<Name, ? extends fc.n>> {
        g() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Map<Name, fc.n> invoke() {
            int u7;
            int e10;
            int c10;
            Collection<fc.n> fields = LazyJavaClassMemberScope.this.f5088o.getFields();
            ArrayList arrayList = new ArrayList();
            for (Object obj : fields) {
                if (((fc.n) obj).M()) {
                    arrayList.add(obj);
                }
            }
            u7 = s.u(arrayList, 10);
            e10 = MapsJVM.e(u7);
            c10 = _Ranges.c(e10, 16);
            LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
            for (Object obj2 : arrayList) {
                linkedHashMap.put(((fc.n) obj2).getName(), obj2);
            }
            return linkedHashMap;
        }
    }

    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$h */
    /* loaded from: classes2.dex */
    static final class h extends Lambda implements ya.a<Set<? extends Name>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ bc.g f5101e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ LazyJavaClassMemberScope f5102f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        h(bc.g gVar, LazyJavaClassMemberScope lazyJavaClassMemberScope) {
            super(0);
            this.f5101e = gVar;
            this.f5102f = lazyJavaClassMemberScope;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Set<Name> invoke() {
            Set<Name> D0;
            bc.g gVar = this.f5101e;
            D0 = _Collections.D0(gVar.a().w().a(gVar, this.f5102f.C()));
            return D0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$i */
    /* loaded from: classes2.dex */
    public static final class i extends Lambda implements ya.l<Name, Collection<? extends SimpleFunctionDescriptor>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ SimpleFunctionDescriptor f5103e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ LazyJavaClassMemberScope f5104f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        i(SimpleFunctionDescriptor simpleFunctionDescriptor, LazyJavaClassMemberScope lazyJavaClassMemberScope) {
            super(1);
            this.f5103e = simpleFunctionDescriptor;
            this.f5104f = lazyJavaClassMemberScope;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<SimpleFunctionDescriptor> invoke(Name name) {
            List m02;
            List e10;
            za.k.e(name, "accessorName");
            if (za.k.a(this.f5103e.getName(), name)) {
                e10 = CollectionsJVM.e(this.f5103e);
                return e10;
            }
            m02 = _Collections.m0(this.f5104f.J0(name), this.f5104f.K0(name));
            return m02;
        }
    }

    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$j */
    /* loaded from: classes2.dex */
    static final class j extends Lambda implements ya.a<Set<? extends Name>> {
        j() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Set<Name> invoke() {
            Set<Name> D0;
            D0 = _Collections.D0(LazyJavaClassMemberScope.this.f5088o.Q());
            return D0;
        }
    }

    /* compiled from: LazyJavaClassMemberScope.kt */
    /* renamed from: cc.g$k */
    /* loaded from: classes2.dex */
    static final class k extends Lambda implements ya.l<Name, ClassDescriptor> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ bc.g f5107f;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: LazyJavaClassMemberScope.kt */
        /* renamed from: cc.g$k$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.a<Set<? extends Name>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ LazyJavaClassMemberScope f5108e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(LazyJavaClassMemberScope lazyJavaClassMemberScope) {
                super(0);
                this.f5108e = lazyJavaClassMemberScope;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Set<Name> invoke() {
                Set<Name> k10;
                k10 = _Sets.k(this.f5108e.b(), this.f5108e.d());
                return k10;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        k(bc.g gVar) {
            super(1);
            this.f5107f = gVar;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ClassDescriptor invoke(Name name) {
            List<ClassDescriptor> c10;
            List a10;
            Object q02;
            za.k.e(name, "name");
            if (((Set) LazyJavaClassMemberScope.this.f5091r.invoke()).contains(name)) {
                JavaClassFinder d10 = this.f5107f.a().d();
                ClassId k10 = wc.c.k(LazyJavaClassMemberScope.this.C());
                za.k.b(k10);
                ClassId d11 = k10.d(name);
                za.k.d(d11, "ownerDescriptor.classId!…createNestedClassId(name)");
                fc.g a11 = d10.a(new JavaClassFinder.a(d11, null, LazyJavaClassMemberScope.this.f5088o, 2, null));
                if (a11 == null) {
                    return null;
                }
                bc.g gVar = this.f5107f;
                LazyJavaClassDescriptor lazyJavaClassDescriptor = new LazyJavaClassDescriptor(gVar, LazyJavaClassMemberScope.this.C(), a11, null, 8, null);
                gVar.a().e().a(lazyJavaClassDescriptor);
                return lazyJavaClassDescriptor;
            }
            if (((Set) LazyJavaClassMemberScope.this.f5092s.invoke()).contains(name)) {
                bc.g gVar2 = this.f5107f;
                LazyJavaClassMemberScope lazyJavaClassMemberScope = LazyJavaClassMemberScope.this;
                c10 = CollectionsJVM.c();
                gVar2.a().w().b(gVar2, lazyJavaClassMemberScope.C(), name, c10);
                a10 = CollectionsJVM.a(c10);
                int size = a10.size();
                if (size == 0) {
                    return null;
                }
                if (size == 1) {
                    q02 = _Collections.q0(a10);
                    return (ClassDescriptor) q02;
                }
                throw new IllegalStateException(("Multiple classes with same name are generated: " + a10).toString());
            }
            fc.n nVar = (fc.n) ((Map) LazyJavaClassMemberScope.this.f5093t.invoke()).get(name);
            if (nVar == null) {
                return null;
            }
            return EnumEntrySyntheticClassDescriptor.U0(this.f5107f.e(), LazyJavaClassMemberScope.this.C(), name, this.f5107f.e().g(new a(LazyJavaClassMemberScope.this)), bc.e.a(this.f5107f, nVar), this.f5107f.a().t().a(nVar));
        }
    }

    public /* synthetic */ LazyJavaClassMemberScope(bc.g gVar, ClassDescriptor classDescriptor, fc.g gVar2, boolean z10, LazyJavaClassMemberScope lazyJavaClassMemberScope, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(gVar, classDescriptor, gVar2, z10, (i10 & 16) != 0 ? null : lazyJavaClassMemberScope);
    }

    private final Set<PropertyDescriptor> A0(Name name) {
        Set<PropertyDescriptor> D0;
        int u7;
        Collection<g0> c02 = c0();
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = c02.iterator();
        while (it.hasNext()) {
            Collection<? extends PropertyDescriptor> a10 = ((g0) it.next()).u().a(name, xb.d.WHEN_GET_SUPER_MEMBERS);
            u7 = s.u(a10, 10);
            ArrayList arrayList2 = new ArrayList(u7);
            Iterator<T> it2 = a10.iterator();
            while (it2.hasNext()) {
                arrayList2.add((PropertyDescriptor) it2.next());
            }
            MutableCollections.z(arrayList, arrayList2);
        }
        D0 = _Collections.D0(arrayList);
        return D0;
    }

    private final boolean B0(SimpleFunctionDescriptor simpleFunctionDescriptor, FunctionDescriptor functionDescriptor) {
        String c10 = w.c(simpleFunctionDescriptor, false, false, 2, null);
        FunctionDescriptor T0 = functionDescriptor.T0();
        za.k.d(T0, "builtinWithErasedParameters.original");
        return za.k.a(c10, w.c(T0, false, false, 2, null)) && !p0(simpleFunctionDescriptor, functionDescriptor);
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x006d, code lost:
    
        if (yb.JvmAbi.d(r4) == false) goto L24;
     */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0074 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:? A[LOOP:1: B:32:0x003f->B:46:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final boolean C0(SimpleFunctionDescriptor simpleFunctionDescriptor) {
        boolean z10;
        boolean z11;
        boolean z12;
        Name name = simpleFunctionDescriptor.getName();
        za.k.d(name, "function.name");
        List<Name> a10 = propertiesConventionUtil.a(name);
        if (!(a10 instanceof Collection) || !a10.isEmpty()) {
            Iterator<T> it = a10.iterator();
            while (it.hasNext()) {
                Set<PropertyDescriptor> A0 = A0((Name) it.next());
                if (!(A0 instanceof Collection) || !A0.isEmpty()) {
                    for (PropertyDescriptor propertyDescriptor : A0) {
                        if (o0(propertyDescriptor, new i(simpleFunctionDescriptor, this))) {
                            if (!propertyDescriptor.p0()) {
                                String b10 = simpleFunctionDescriptor.getName().b();
                                za.k.d(b10, "function.name.asString()");
                            }
                            z10 = true;
                            if (!z10) {
                                z11 = true;
                                break;
                            }
                        }
                        z10 = false;
                        if (!z10) {
                        }
                    }
                }
                z11 = false;
                if (z11) {
                    z12 = true;
                    break;
                }
            }
        }
        z12 = false;
        if (z12) {
            return false;
        }
        return (q0(simpleFunctionDescriptor) || L0(simpleFunctionDescriptor) || s0(simpleFunctionDescriptor)) ? false : true;
    }

    private final SimpleFunctionDescriptor D0(SimpleFunctionDescriptor simpleFunctionDescriptor, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar, Collection<? extends SimpleFunctionDescriptor> collection) {
        SimpleFunctionDescriptor h02;
        FunctionDescriptor k10 = yb.f.k(simpleFunctionDescriptor);
        if (k10 == null || (h02 = h0(k10, lVar)) == null) {
            return null;
        }
        if (!C0(h02)) {
            h02 = null;
        }
        if (h02 != null) {
            return g0(h02, k10, collection);
        }
        return null;
    }

    private final SimpleFunctionDescriptor E0(SimpleFunctionDescriptor simpleFunctionDescriptor, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar, Name name, Collection<? extends SimpleFunctionDescriptor> collection) {
        SimpleFunctionDescriptor simpleFunctionDescriptor2 = (SimpleFunctionDescriptor) h0.d(simpleFunctionDescriptor);
        if (simpleFunctionDescriptor2 == null) {
            return null;
        }
        String b10 = h0.b(simpleFunctionDescriptor2);
        za.k.b(b10);
        Name f10 = Name.f(b10);
        za.k.d(f10, "identifier(nameInJava)");
        Iterator<? extends SimpleFunctionDescriptor> it = lVar.invoke(f10).iterator();
        while (it.hasNext()) {
            SimpleFunctionDescriptor m02 = m0(it.next(), name);
            if (r0(simpleFunctionDescriptor2, m02)) {
                return g0(m02, simpleFunctionDescriptor2, collection);
            }
        }
        return null;
    }

    private final SimpleFunctionDescriptor F0(SimpleFunctionDescriptor simpleFunctionDescriptor, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        if (!simpleFunctionDescriptor.C0()) {
            return null;
        }
        Name name = simpleFunctionDescriptor.getName();
        za.k.d(name, "descriptor.name");
        Iterator<T> it = lVar.invoke(name).iterator();
        while (it.hasNext()) {
            SimpleFunctionDescriptor n02 = n0((SimpleFunctionDescriptor) it.next());
            if (n02 == null || !p0(n02, simpleFunctionDescriptor)) {
                n02 = null;
            }
            if (n02 != null) {
                return n02;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final JavaClassConstructorDescriptor H0(fc.k kVar) {
        int u7;
        List<TypeParameterDescriptor> m02;
        ClassDescriptor C = C();
        JavaClassConstructorDescriptor C1 = JavaClassConstructorDescriptor.C1(C, bc.e.a(w(), kVar), false, w().a().t().a(kVar));
        za.k.d(C1, "createJavaConstructor(\n …ce(constructor)\n        )");
        bc.g e10 = bc.a.e(w(), C1, kVar, C.B().size());
        LazyJavaScope.b K = K(e10, C1, kVar.l());
        List<TypeParameterDescriptor> B = C.B();
        za.k.d(B, "classDescriptor.declaredTypeParameters");
        List<y> m10 = kVar.m();
        u7 = s.u(m10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = m10.iterator();
        while (it.hasNext()) {
            TypeParameterDescriptor a10 = e10.f().a((y) it.next());
            za.k.b(a10);
            arrayList.add(a10);
        }
        m02 = _Collections.m0(B, arrayList);
        C1.A1(K.a(), j0.d(kVar.g()), m02);
        C1.h1(false);
        C1.i1(K.b());
        C1.p1(C.x());
        e10.a().h().a(kVar, C1);
        return C1;
    }

    private final JavaMethodDescriptor I0(fc.w wVar) {
        List<ReceiverParameterDescriptor> j10;
        List<? extends TypeParameterDescriptor> j11;
        List<ValueParameterDescriptor> j12;
        JavaMethodDescriptor y12 = JavaMethodDescriptor.y1(C(), bc.e.a(w(), wVar), wVar.getName(), w().a().t().a(wVar), true);
        za.k.d(y12, "createJavaMethod(\n      …omponent), true\n        )");
        g0 o10 = w().g().o(wVar.getType(), dc.b.b(TypeUsage.COMMON, false, false, null, 6, null));
        ReceiverParameterDescriptor z10 = z();
        j10 = r.j();
        j11 = r.j();
        j12 = r.j();
        y12.x1(null, z10, j10, j11, j12, o10, Modality.f16676e.a(false, false, true), DescriptorVisibilities.f16733e, null);
        y12.B1(false, false);
        w().a().h().b(wVar, y12);
        return y12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Collection<SimpleFunctionDescriptor> J0(Name name) {
        int u7;
        Collection<fc.r> d10 = y().invoke().d(name);
        u7 = s.u(d10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = d10.iterator();
        while (it.hasNext()) {
            arrayList.add(I((fc.r) it.next()));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Collection<SimpleFunctionDescriptor> K0(Name name) {
        Set<SimpleFunctionDescriptor> y02 = y0(name);
        ArrayList arrayList = new ArrayList();
        for (Object obj : y02) {
            SimpleFunctionDescriptor simpleFunctionDescriptor = (SimpleFunctionDescriptor) obj;
            if (!(h0.a(simpleFunctionDescriptor) || yb.f.k(simpleFunctionDescriptor) != null)) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }

    private final boolean L0(SimpleFunctionDescriptor simpleFunctionDescriptor) {
        yb.f fVar = yb.f.f20072n;
        Name name = simpleFunctionDescriptor.getName();
        za.k.d(name, "name");
        if (!fVar.l(name)) {
            return false;
        }
        Name name2 = simpleFunctionDescriptor.getName();
        za.k.d(name2, "name");
        Set<SimpleFunctionDescriptor> y02 = y0(name2);
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = y02.iterator();
        while (it.hasNext()) {
            FunctionDescriptor k10 = yb.f.k((SimpleFunctionDescriptor) it.next());
            if (k10 != null) {
                arrayList.add(k10);
            }
        }
        if (arrayList.isEmpty()) {
            return false;
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            if (B0(simpleFunctionDescriptor, (FunctionDescriptor) it2.next())) {
                return true;
            }
        }
        return false;
    }

    private final void V(List<ValueParameterDescriptor> list, ConstructorDescriptor constructorDescriptor, int i10, fc.r rVar, g0 g0Var, g0 g0Var2) {
        qb.g b10 = qb.g.f17195b.b();
        Name name = rVar.getName();
        g0 n10 = s1.n(g0Var);
        za.k.d(n10, "makeNotNullable(returnType)");
        list.add(new ValueParameterDescriptorImpl(constructorDescriptor, null, i10, b10, name, n10, rVar.R(), false, false, g0Var2 != null ? s1.n(g0Var2) : null, w().a().t().a(rVar)));
    }

    private final void W(Collection<SimpleFunctionDescriptor> collection, Name name, Collection<? extends SimpleFunctionDescriptor> collection2, boolean z10) {
        List m02;
        int u7;
        Collection<? extends SimpleFunctionDescriptor> d10 = DescriptorResolverUtils.d(name, collection2, collection, C(), w().a().c(), w().a().k().a());
        za.k.d(d10, "resolveOverridesForNonSt….overridingUtil\n        )");
        if (!z10) {
            collection.addAll(d10);
            return;
        }
        m02 = _Collections.m0(collection, d10);
        u7 = s.u(d10, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (SimpleFunctionDescriptor simpleFunctionDescriptor : d10) {
            SimpleFunctionDescriptor simpleFunctionDescriptor2 = (SimpleFunctionDescriptor) h0.e(simpleFunctionDescriptor);
            if (simpleFunctionDescriptor2 == null) {
                za.k.d(simpleFunctionDescriptor, "resolvedOverride");
            } else {
                za.k.d(simpleFunctionDescriptor, "resolvedOverride");
                simpleFunctionDescriptor = g0(simpleFunctionDescriptor, simpleFunctionDescriptor2, m02);
            }
            arrayList.add(simpleFunctionDescriptor);
        }
        collection.addAll(arrayList);
    }

    private final void X(Name name, Collection<? extends SimpleFunctionDescriptor> collection, Collection<? extends SimpleFunctionDescriptor> collection2, Collection<SimpleFunctionDescriptor> collection3, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        for (SimpleFunctionDescriptor simpleFunctionDescriptor : collection2) {
            collections.a(collection3, E0(simpleFunctionDescriptor, lVar, name, collection));
            collections.a(collection3, D0(simpleFunctionDescriptor, lVar, collection));
            collections.a(collection3, F0(simpleFunctionDescriptor, lVar));
        }
    }

    private final void Y(Set<? extends PropertyDescriptor> set, Collection<PropertyDescriptor> collection, Set<PropertyDescriptor> set2, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        for (PropertyDescriptor propertyDescriptor : set) {
            JavaPropertyDescriptor i02 = i0(propertyDescriptor, lVar);
            if (i02 != null) {
                collection.add(i02);
                if (set2 != null) {
                    set2.add(propertyDescriptor);
                    return;
                }
                return;
            }
        }
    }

    private final void Z(Name name, Collection<PropertyDescriptor> collection) {
        Object r02;
        r02 = _Collections.r0(y().invoke().d(name));
        fc.r rVar = (fc.r) r02;
        if (rVar == null) {
            return;
        }
        collection.add(k0(this, rVar, null, Modality.FINAL, 2, null));
    }

    private final Collection<g0> c0() {
        if (!this.f5089p) {
            return w().a().k().c().g(C());
        }
        Collection<g0> q10 = C().n().q();
        za.k.d(q10, "ownerDescriptor.typeConstructor.supertypes");
        return q10;
    }

    private final List<ValueParameterDescriptor> d0(ClassConstructorDescriptorImpl classConstructorDescriptorImpl) {
        Object V;
        o oVar;
        Collection<fc.r> S = this.f5088o.S();
        ArrayList arrayList = new ArrayList(S.size());
        dc.a b10 = dc.b.b(TypeUsage.COMMON, true, false, null, 6, null);
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (Object obj : S) {
            if (za.k.a(((fc.r) obj).getName(), b0.f20020c)) {
                arrayList2.add(obj);
            } else {
                arrayList3.add(obj);
            }
        }
        o oVar2 = new o(arrayList2, arrayList3);
        List list = (List) oVar2.a();
        List<fc.r> list2 = (List) oVar2.b();
        list.size();
        V = _Collections.V(list);
        fc.r rVar = (fc.r) V;
        if (rVar != null) {
            x f10 = rVar.f();
            if (f10 instanceof fc.f) {
                fc.f fVar = (fc.f) f10;
                oVar = new o(w().g().k(fVar, b10, true), w().g().o(fVar.r(), b10));
            } else {
                oVar = new o(w().g().o(f10, b10), null);
            }
            V(arrayList, classConstructorDescriptorImpl, 0, rVar, (g0) oVar.a(), (g0) oVar.b());
        }
        int i10 = 0;
        int i11 = rVar == null ? 0 : 1;
        for (fc.r rVar2 : list2) {
            V(arrayList, classConstructorDescriptorImpl, i10 + i11, rVar2, w().g().o(rVar2.f(), b10), null);
            i10++;
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ClassConstructorDescriptor e0() {
        List<ValueParameterDescriptor> emptyList;
        boolean y4 = this.f5088o.y();
        if ((this.f5088o.N() || !this.f5088o.B()) && !y4) {
            return null;
        }
        ClassDescriptor C = C();
        JavaClassConstructorDescriptor C1 = JavaClassConstructorDescriptor.C1(C, qb.g.f17195b.b(), true, w().a().t().a(this.f5088o));
        za.k.d(C1, "createJavaConstructor(\n ….source(jClass)\n        )");
        if (y4) {
            emptyList = d0(C1);
        } else {
            emptyList = Collections.emptyList();
        }
        C1.i1(false);
        C1.z1(emptyList, w0(C));
        C1.h1(true);
        C1.p1(C.x());
        w().a().h().a(this.f5088o, C1);
        return C1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ClassConstructorDescriptor f0() {
        ClassDescriptor C = C();
        JavaClassConstructorDescriptor C1 = JavaClassConstructorDescriptor.C1(C, qb.g.f17195b.b(), true, w().a().t().a(this.f5088o));
        za.k.d(C1, "createJavaConstructor(\n ….source(jClass)\n        )");
        List<ValueParameterDescriptor> l02 = l0(C1);
        C1.i1(false);
        C1.z1(l02, w0(C));
        C1.h1(false);
        C1.p1(C.x());
        return C1;
    }

    private final SimpleFunctionDescriptor g0(SimpleFunctionDescriptor simpleFunctionDescriptor, CallableDescriptor callableDescriptor, Collection<? extends SimpleFunctionDescriptor> collection) {
        boolean z10 = false;
        if (!(collection instanceof Collection) || !collection.isEmpty()) {
            for (SimpleFunctionDescriptor simpleFunctionDescriptor2 : collection) {
                if (!za.k.a(simpleFunctionDescriptor, simpleFunctionDescriptor2) && simpleFunctionDescriptor2.l0() == null && p0(simpleFunctionDescriptor2, callableDescriptor)) {
                    break;
                }
            }
        }
        z10 = true;
        if (z10) {
            return simpleFunctionDescriptor;
        }
        SimpleFunctionDescriptor build = simpleFunctionDescriptor.A().o().build();
        za.k.b(build);
        return build;
    }

    private final SimpleFunctionDescriptor h0(FunctionDescriptor functionDescriptor, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        Object obj;
        int u7;
        Name name = functionDescriptor.getName();
        za.k.d(name, "overridden.name");
        Iterator<T> it = lVar.invoke(name).iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (B0((SimpleFunctionDescriptor) obj, functionDescriptor)) {
                break;
            }
        }
        SimpleFunctionDescriptor simpleFunctionDescriptor = (SimpleFunctionDescriptor) obj;
        if (simpleFunctionDescriptor == null) {
            return null;
        }
        FunctionDescriptor.a<? extends SimpleFunctionDescriptor> A = simpleFunctionDescriptor.A();
        List<ValueParameterDescriptor> l10 = functionDescriptor.l();
        za.k.d(l10, "overridden.valueParameters");
        u7 = s.u(l10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it2 = l10.iterator();
        while (it2.hasNext()) {
            arrayList.add(((ValueParameterDescriptor) it2.next()).getType());
        }
        List<ValueParameterDescriptor> l11 = simpleFunctionDescriptor.l();
        za.k.d(l11, "override.valueParameters");
        A.c(ac.h.a(arrayList, l11, functionDescriptor));
        A.t();
        A.h();
        A.i(JavaMethodDescriptor.L, Boolean.TRUE);
        return A.build();
    }

    private final JavaPropertyDescriptor i0(PropertyDescriptor propertyDescriptor, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        SimpleFunctionDescriptor simpleFunctionDescriptor;
        List<? extends TypeParameterDescriptor> j10;
        List<ReceiverParameterDescriptor> j11;
        Object V;
        PropertySetterDescriptorImpl propertySetterDescriptorImpl = null;
        if (!o0(propertyDescriptor, lVar)) {
            return null;
        }
        SimpleFunctionDescriptor u02 = u0(propertyDescriptor, lVar);
        za.k.b(u02);
        if (propertyDescriptor.p0()) {
            simpleFunctionDescriptor = v0(propertyDescriptor, lVar);
            za.k.b(simpleFunctionDescriptor);
        } else {
            simpleFunctionDescriptor = null;
        }
        if (simpleFunctionDescriptor != null) {
            simpleFunctionDescriptor.o();
            u02.o();
        }
        JavaForKotlinOverridePropertyDescriptor javaForKotlinOverridePropertyDescriptor = new JavaForKotlinOverridePropertyDescriptor(C(), u02, simpleFunctionDescriptor, propertyDescriptor);
        g0 f10 = u02.f();
        za.k.b(f10);
        j10 = r.j();
        ReceiverParameterDescriptor z10 = z();
        j11 = r.j();
        javaForKotlinOverridePropertyDescriptor.k1(f10, j10, z10, null, j11);
        PropertyGetterDescriptorImpl k10 = DescriptorFactory.k(javaForKotlinOverridePropertyDescriptor, u02.i(), false, false, false, u02.z());
        k10.W0(u02);
        k10.Z0(javaForKotlinOverridePropertyDescriptor.getType());
        za.k.d(k10, "createGetter(\n          …escriptor.type)\n        }");
        if (simpleFunctionDescriptor != null) {
            List<ValueParameterDescriptor> l10 = simpleFunctionDescriptor.l();
            za.k.d(l10, "setterMethod.valueParameters");
            V = _Collections.V(l10);
            ValueParameterDescriptor valueParameterDescriptor = (ValueParameterDescriptor) V;
            if (valueParameterDescriptor != null) {
                propertySetterDescriptorImpl = DescriptorFactory.m(javaForKotlinOverridePropertyDescriptor, simpleFunctionDescriptor.i(), valueParameterDescriptor.i(), false, false, false, simpleFunctionDescriptor.g(), simpleFunctionDescriptor.z());
                propertySetterDescriptorImpl.W0(simpleFunctionDescriptor);
            } else {
                throw new AssertionError("No parameter found for " + simpleFunctionDescriptor);
            }
        }
        javaForKotlinOverridePropertyDescriptor.d1(k10, propertySetterDescriptorImpl);
        return javaForKotlinOverridePropertyDescriptor;
    }

    private final JavaPropertyDescriptor j0(fc.r rVar, g0 g0Var, Modality modality) {
        List<? extends TypeParameterDescriptor> j10;
        List<ReceiverParameterDescriptor> j11;
        JavaPropertyDescriptor o12 = JavaPropertyDescriptor.o1(C(), bc.e.a(w(), rVar), modality, j0.d(rVar.g()), false, rVar.getName(), w().a().t().a(rVar), false);
        za.k.d(o12, "create(\n            owne…inal = */ false\n        )");
        PropertyGetterDescriptorImpl d10 = DescriptorFactory.d(o12, qb.g.f17195b.b());
        za.k.d(d10, "createDefaultGetter(prop…iptor, Annotations.EMPTY)");
        o12.d1(d10, null);
        g0 q10 = g0Var == null ? q(rVar, bc.a.f(w(), o12, rVar, 0, 4, null)) : g0Var;
        j10 = r.j();
        ReceiverParameterDescriptor z10 = z();
        j11 = r.j();
        o12.k1(q10, j10, z10, null, j11);
        d10.Z0(q10);
        return o12;
    }

    static /* synthetic */ JavaPropertyDescriptor k0(LazyJavaClassMemberScope lazyJavaClassMemberScope, fc.r rVar, g0 g0Var, Modality modality, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            g0Var = null;
        }
        return lazyJavaClassMemberScope.j0(rVar, g0Var, modality);
    }

    private final List<ValueParameterDescriptor> l0(ClassConstructorDescriptorImpl classConstructorDescriptorImpl) {
        Collection<fc.w> v7 = this.f5088o.v();
        ArrayList arrayList = new ArrayList(v7.size());
        dc.a b10 = dc.b.b(TypeUsage.COMMON, false, false, null, 6, null);
        Iterator<fc.w> it = v7.iterator();
        int i10 = 0;
        while (true) {
            int i11 = i10;
            if (!it.hasNext()) {
                return arrayList;
            }
            i10 = i11 + 1;
            fc.w next = it.next();
            g0 o10 = w().g().o(next.getType(), b10);
            arrayList.add(new ValueParameterDescriptorImpl(classConstructorDescriptorImpl, null, i11, qb.g.f17195b.b(), next.getName(), o10, false, false, false, next.a() ? w().a().m().t().k(o10) : null, w().a().t().a(next)));
        }
    }

    private final SimpleFunctionDescriptor m0(SimpleFunctionDescriptor simpleFunctionDescriptor, Name name) {
        FunctionDescriptor.a<? extends SimpleFunctionDescriptor> A = simpleFunctionDescriptor.A();
        A.m(name);
        A.t();
        A.h();
        SimpleFunctionDescriptor build = A.build();
        za.k.b(build);
        return build;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final SimpleFunctionDescriptor n0(SimpleFunctionDescriptor simpleFunctionDescriptor) {
        Object g02;
        FqName fqName;
        List<ValueParameterDescriptor> O;
        FqNameUnsafe m10;
        List<ValueParameterDescriptor> l10 = simpleFunctionDescriptor.l();
        za.k.d(l10, "valueParameters");
        g02 = _Collections.g0(l10);
        ValueParameterDescriptor valueParameterDescriptor = (ValueParameterDescriptor) g02;
        if (valueParameterDescriptor != null) {
            ClassifierDescriptor v7 = valueParameterDescriptor.getType().W0().v();
            if (v7 != null && (m10 = wc.c.m(v7)) != null) {
                if (!m10.f()) {
                    m10 = null;
                }
                if (m10 != null) {
                    fqName = m10.l();
                    if (!za.k.a(fqName, StandardNames.f15278p)) {
                        valueParameterDescriptor = null;
                    }
                    if (valueParameterDescriptor != null) {
                        FunctionDescriptor.a<? extends SimpleFunctionDescriptor> A = simpleFunctionDescriptor.A();
                        List<ValueParameterDescriptor> l11 = simpleFunctionDescriptor.l();
                        za.k.d(l11, "valueParameters");
                        O = _Collections.O(l11, 1);
                        SimpleFunctionDescriptor build = A.c(O).e(valueParameterDescriptor.getType().U0().get(0).getType()).build();
                        SimpleFunctionDescriptorImpl simpleFunctionDescriptorImpl = (SimpleFunctionDescriptorImpl) build;
                        if (simpleFunctionDescriptorImpl != null) {
                            simpleFunctionDescriptorImpl.q1(true);
                        }
                        return build;
                    }
                }
            }
            fqName = null;
            if (!za.k.a(fqName, StandardNames.f15278p)) {
            }
            if (valueParameterDescriptor != null) {
            }
        }
        return null;
    }

    private final boolean o0(PropertyDescriptor propertyDescriptor, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        if (JavaDescriptorUtil.a(propertyDescriptor)) {
            return false;
        }
        SimpleFunctionDescriptor u02 = u0(propertyDescriptor, lVar);
        SimpleFunctionDescriptor v02 = v0(propertyDescriptor, lVar);
        if (u02 == null) {
            return false;
        }
        if (propertyDescriptor.p0()) {
            return v02 != null && v02.o() == u02.o();
        }
        return true;
    }

    private final boolean p0(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2) {
        OverridingUtil.i.a c10 = OverridingUtil.f18440f.F(callableDescriptor2, callableDescriptor, true).c();
        za.k.d(c10, "DEFAULT.isOverridableByW…iptor, this, true).result");
        return c10 == OverridingUtil.i.a.OVERRIDABLE && !JavaIncompatibilityRulesOverridabilityCondition.f20135a.a(callableDescriptor2, callableDescriptor);
    }

    private final boolean q0(SimpleFunctionDescriptor simpleFunctionDescriptor) {
        SpecialGenericSignatures.a aVar = SpecialGenericSignatures.f20091a;
        Name name = simpleFunctionDescriptor.getName();
        za.k.d(name, "name");
        Name b10 = aVar.b(name);
        if (b10 == null) {
            return false;
        }
        Set<SimpleFunctionDescriptor> y02 = y0(b10);
        ArrayList arrayList = new ArrayList();
        for (Object obj : y02) {
            if (h0.a((SimpleFunctionDescriptor) obj)) {
                arrayList.add(obj);
            }
        }
        if (arrayList.isEmpty()) {
            return false;
        }
        SimpleFunctionDescriptor m02 = m0(simpleFunctionDescriptor, b10);
        if (arrayList.isEmpty()) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (r0((SimpleFunctionDescriptor) it.next(), m02)) {
                return true;
            }
        }
        return false;
    }

    private final boolean r0(SimpleFunctionDescriptor simpleFunctionDescriptor, FunctionDescriptor functionDescriptor) {
        if (yb.e.f20066n.k(simpleFunctionDescriptor)) {
            functionDescriptor = functionDescriptor.T0();
        }
        za.k.d(functionDescriptor, "if (superDescriptor.isRe…iginal else subDescriptor");
        return p0(functionDescriptor, simpleFunctionDescriptor);
    }

    private final boolean s0(SimpleFunctionDescriptor simpleFunctionDescriptor) {
        SimpleFunctionDescriptor n02 = n0(simpleFunctionDescriptor);
        if (n02 == null) {
            return false;
        }
        Name name = simpleFunctionDescriptor.getName();
        za.k.d(name, "name");
        Set<SimpleFunctionDescriptor> y02 = y0(name);
        if ((y02 instanceof Collection) && y02.isEmpty()) {
            return false;
        }
        for (SimpleFunctionDescriptor simpleFunctionDescriptor2 : y02) {
            if (simpleFunctionDescriptor2.C0() && p0(n02, simpleFunctionDescriptor2)) {
                return true;
            }
        }
        return false;
    }

    private final SimpleFunctionDescriptor t0(PropertyDescriptor propertyDescriptor, String str, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        SimpleFunctionDescriptor simpleFunctionDescriptor;
        Name f10 = Name.f(str);
        za.k.d(f10, "identifier(getterName)");
        Iterator<T> it = lVar.invoke(f10).iterator();
        do {
            simpleFunctionDescriptor = null;
            if (!it.hasNext()) {
                break;
            }
            SimpleFunctionDescriptor simpleFunctionDescriptor2 = (SimpleFunctionDescriptor) it.next();
            if (simpleFunctionDescriptor2.l().size() == 0) {
                KotlinTypeChecker kotlinTypeChecker = KotlinTypeChecker.f12213a;
                g0 f11 = simpleFunctionDescriptor2.f();
                if (f11 == null ? false : kotlinTypeChecker.b(f11, propertyDescriptor.getType())) {
                    simpleFunctionDescriptor = simpleFunctionDescriptor2;
                }
            }
        } while (simpleFunctionDescriptor == null);
        return simpleFunctionDescriptor;
    }

    private final SimpleFunctionDescriptor u0(PropertyDescriptor propertyDescriptor, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        PropertyGetterDescriptor h10 = propertyDescriptor.h();
        PropertyGetterDescriptor propertyGetterDescriptor = h10 != null ? (PropertyGetterDescriptor) h0.d(h10) : null;
        String a10 = propertyGetterDescriptor != null ? ClassicBuiltinSpecialProperties.f20089a.a(propertyGetterDescriptor) : null;
        if (a10 != null && !h0.f(C(), propertyGetterDescriptor)) {
            return t0(propertyDescriptor, a10, lVar);
        }
        String b10 = propertyDescriptor.getName().b();
        za.k.d(b10, "name.asString()");
        return t0(propertyDescriptor, JvmAbi.b(b10), lVar);
    }

    private final SimpleFunctionDescriptor v0(PropertyDescriptor propertyDescriptor, ya.l<? super Name, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        SimpleFunctionDescriptor simpleFunctionDescriptor;
        g0 f10;
        Object q02;
        String b10 = propertyDescriptor.getName().b();
        za.k.d(b10, "name.asString()");
        Name f11 = Name.f(JvmAbi.e(b10));
        za.k.d(f11, "identifier(JvmAbi.setterName(name.asString()))");
        Iterator<T> it = lVar.invoke(f11).iterator();
        do {
            simpleFunctionDescriptor = null;
            if (!it.hasNext()) {
                break;
            }
            SimpleFunctionDescriptor simpleFunctionDescriptor2 = (SimpleFunctionDescriptor) it.next();
            if (simpleFunctionDescriptor2.l().size() == 1 && (f10 = simpleFunctionDescriptor2.f()) != null && KotlinBuiltIns.B0(f10)) {
                KotlinTypeChecker kotlinTypeChecker = KotlinTypeChecker.f12213a;
                List<ValueParameterDescriptor> l10 = simpleFunctionDescriptor2.l();
                za.k.d(l10, "descriptor.valueParameters");
                q02 = _Collections.q0(l10);
                if (kotlinTypeChecker.d(((ValueParameterDescriptor) q02).getType(), propertyDescriptor.getType())) {
                    simpleFunctionDescriptor = simpleFunctionDescriptor2;
                }
            }
        } while (simpleFunctionDescriptor == null);
        return simpleFunctionDescriptor;
    }

    private final u w0(ClassDescriptor classDescriptor) {
        u g6 = classDescriptor.g();
        za.k.d(g6, "classDescriptor.visibility");
        if (!za.k.a(g6, JavaDescriptorVisibilities.f20132b)) {
            return g6;
        }
        u uVar = JavaDescriptorVisibilities.f20133c;
        za.k.d(uVar, "PROTECTED_AND_PACKAGE");
        return uVar;
    }

    private final Set<SimpleFunctionDescriptor> y0(Name name) {
        Collection<g0> c02 = c0();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Iterator<T> it = c02.iterator();
        while (it.hasNext()) {
            MutableCollections.z(linkedHashSet, ((g0) it.next()).u().c(name, xb.d.WHEN_GET_SUPER_MEMBERS));
        }
        return linkedHashSet;
    }

    @Override // cc.LazyJavaScope
    protected boolean G(JavaMethodDescriptor javaMethodDescriptor) {
        za.k.e(javaMethodDescriptor, "<this>");
        if (this.f5088o.y()) {
            return false;
        }
        return C0(javaMethodDescriptor);
    }

    public void G0(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        wb.a.a(w().a().l(), bVar, C(), name);
    }

    @Override // cc.LazyJavaScope
    protected LazyJavaScope.a H(fc.r rVar, List<? extends TypeParameterDescriptor> list, g0 g0Var, List<? extends ValueParameterDescriptor> list2) {
        za.k.e(rVar, Constants.MessagerConstants.METHOD_KEY);
        za.k.e(list, "methodTypeParameters");
        za.k.e(g0Var, "returnType");
        za.k.e(list2, "valueParameters");
        SignaturePropagator.b b10 = w().a().s().b(rVar, C(), g0Var, null, list2, list);
        za.k.d(b10, "c.components.signaturePr…dTypeParameters\n        )");
        g0 d10 = b10.d();
        za.k.d(d10, "propagated.returnType");
        g0 c10 = b10.c();
        List<ValueParameterDescriptor> f10 = b10.f();
        za.k.d(f10, "propagated.valueParameters");
        List<TypeParameterDescriptor> e10 = b10.e();
        za.k.d(e10, "propagated.typeParameters");
        boolean g6 = b10.g();
        List<String> b11 = b10.b();
        za.k.d(b11, "propagated.errors");
        return new LazyJavaScope.a(d10, c10, f10, e10, g6, b11);
    }

    @Override // cc.LazyJavaScope, zc.MemberScopeImpl, zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        G0(name, bVar);
        return super.a(name, bVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cc.LazyJavaScope
    /* renamed from: a0, reason: merged with bridge method [inline-methods] */
    public LinkedHashSet<Name> n(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        za.k.e(dVar, "kindFilter");
        Collection<g0> q10 = C().n().q();
        za.k.d(q10, "ownerDescriptor.typeConstructor.supertypes");
        LinkedHashSet<Name> linkedHashSet = new LinkedHashSet<>();
        Iterator<T> it = q10.iterator();
        while (it.hasNext()) {
            MutableCollections.z(linkedHashSet, ((g0) it.next()).u().b());
        }
        linkedHashSet.addAll(y().invoke().a());
        linkedHashSet.addAll(y().invoke().e());
        linkedHashSet.addAll(l(dVar, lVar));
        linkedHashSet.addAll(w().a().w().e(w(), C()));
        return linkedHashSet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cc.LazyJavaScope
    /* renamed from: b0, reason: merged with bridge method [inline-methods] */
    public cc.a p() {
        return new cc.a(this.f5088o, a.f5095e);
    }

    @Override // cc.LazyJavaScope, zc.MemberScopeImpl, zc.h
    public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        G0(name, bVar);
        return super.c(name, bVar);
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        fd.h<Name, ClassDescriptor> hVar;
        ClassDescriptor invoke;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        G0(name, bVar);
        LazyJavaClassMemberScope lazyJavaClassMemberScope = (LazyJavaClassMemberScope) B();
        return (lazyJavaClassMemberScope == null || (hVar = lazyJavaClassMemberScope.f5094u) == null || (invoke = hVar.invoke(name)) == null) ? this.f5094u.invoke(name) : invoke;
    }

    @Override // cc.LazyJavaScope
    protected Set<Name> l(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        Set<Name> k10;
        za.k.e(dVar, "kindFilter");
        k10 = _Sets.k(this.f5091r.invoke(), this.f5093t.invoke().keySet());
        return k10;
    }

    @Override // cc.LazyJavaScope
    protected void o(Collection<SimpleFunctionDescriptor> collection, Name name) {
        za.k.e(collection, "result");
        za.k.e(name, "name");
        if (this.f5088o.A() && y().invoke().c(name) != null) {
            boolean z10 = true;
            if (!collection.isEmpty()) {
                Iterator<T> it = collection.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    } else if (((SimpleFunctionDescriptor) it.next()).l().isEmpty()) {
                        z10 = false;
                        break;
                    }
                }
            }
            if (z10) {
                fc.w c10 = y().invoke().c(name);
                za.k.b(c10);
                collection.add(I0(c10));
            }
        }
        w().a().w().g(w(), C(), name, collection);
    }

    @Override // cc.LazyJavaScope
    protected void r(Collection<SimpleFunctionDescriptor> collection, Name name) {
        List j10;
        List m02;
        boolean z10;
        za.k.e(collection, "result");
        za.k.e(name, "name");
        Set<SimpleFunctionDescriptor> y02 = y0(name);
        if (!SpecialGenericSignatures.f20091a.k(name) && !yb.f.f20072n.l(name)) {
            if (!(y02 instanceof Collection) || !y02.isEmpty()) {
                Iterator<T> it = y02.iterator();
                while (it.hasNext()) {
                    if (((FunctionDescriptor) it.next()).C0()) {
                        z10 = false;
                        break;
                    }
                }
            }
            z10 = true;
            if (z10) {
                ArrayList arrayList = new ArrayList();
                for (Object obj : y02) {
                    if (C0((SimpleFunctionDescriptor) obj)) {
                        arrayList.add(obj);
                    }
                }
                W(collection, name, arrayList, false);
                return;
            }
        }
        SmartSet a10 = SmartSet.f17432g.a();
        j10 = r.j();
        Collection<? extends SimpleFunctionDescriptor> d10 = DescriptorResolverUtils.d(name, y02, j10, C(), ErrorReporter.f5285a, w().a().k().a());
        za.k.d(d10, "resolveOverridesForNonSt….overridingUtil\n        )");
        X(name, collection, d10, collection, new b(this));
        X(name, collection, d10, a10, new c(this));
        ArrayList arrayList2 = new ArrayList();
        for (Object obj2 : y02) {
            if (C0((SimpleFunctionDescriptor) obj2)) {
                arrayList2.add(obj2);
            }
        }
        m02 = _Collections.m0(arrayList2, a10);
        W(collection, name, m02, true);
    }

    @Override // cc.LazyJavaScope
    protected void s(Name name, Collection<PropertyDescriptor> collection) {
        Set<? extends PropertyDescriptor> i10;
        Set k10;
        za.k.e(name, "name");
        za.k.e(collection, "result");
        if (this.f5088o.y()) {
            Z(name, collection);
        }
        Set<PropertyDescriptor> A0 = A0(name);
        if (A0.isEmpty()) {
            return;
        }
        SmartSet.b bVar = SmartSet.f17432g;
        SmartSet a10 = bVar.a();
        SmartSet a11 = bVar.a();
        Y(A0, collection, a10, new d());
        i10 = _Sets.i(A0, a10);
        Y(i10, a11, null, new e());
        k10 = _Sets.k(A0, a11);
        Collection<? extends PropertyDescriptor> d10 = DescriptorResolverUtils.d(name, k10, collection, C(), w().a().c(), w().a().k().a());
        za.k.d(d10, "resolveOverridesForNonSt…rridingUtil\n            )");
        collection.addAll(d10);
    }

    @Override // cc.LazyJavaScope
    protected Set<Name> t(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        za.k.e(dVar, "kindFilter");
        if (this.f5088o.y()) {
            return b();
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet(y().invoke().f());
        Collection<g0> q10 = C().n().q();
        za.k.d(q10, "ownerDescriptor.typeConstructor.supertypes");
        Iterator<T> it = q10.iterator();
        while (it.hasNext()) {
            MutableCollections.z(linkedHashSet, ((g0) it.next()).u().d());
        }
        return linkedHashSet;
    }

    @Override // cc.LazyJavaScope
    public String toString() {
        return "Lazy Java member scope for " + this.f5088o.d();
    }

    public final fd.i<List<ClassConstructorDescriptor>> x0() {
        return this.f5090q;
    }

    @Override // cc.LazyJavaScope
    protected ReceiverParameterDescriptor z() {
        return sc.e.l(C());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cc.LazyJavaScope
    /* renamed from: z0, reason: merged with bridge method [inline-methods] */
    public ClassDescriptor C() {
        return this.f5087n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LazyJavaClassMemberScope(bc.g gVar, ClassDescriptor classDescriptor, fc.g gVar2, boolean z10, LazyJavaClassMemberScope lazyJavaClassMemberScope) {
        super(gVar, lazyJavaClassMemberScope);
        za.k.e(gVar, "c");
        za.k.e(classDescriptor, "ownerDescriptor");
        za.k.e(gVar2, "jClass");
        this.f5087n = classDescriptor;
        this.f5088o = gVar2;
        this.f5089p = z10;
        this.f5090q = gVar.e().g(new f(gVar));
        this.f5091r = gVar.e().g(new j());
        this.f5092s = gVar.e().g(new h(gVar, this));
        this.f5093t = gVar.e().g(new g());
        this.f5094u = gVar.e().b(new k(gVar));
    }
}
