package cc;

import ac.JavaMethodDescriptor;
import ac.JavaPropertyDescriptor;
import com.oplus.backup.sdk.common.utils.Constants;
import fc.b0;
import fc.r;
import fc.x;
import fc.y;
import fd.StorageManager;
import gd.TypeUsage;
import gd.g0;
import gd.s1;
import hc.w;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.IndexedValue;
import kotlin.collections.MapsJVM;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import kotlin.collections.s;
import ma.o;
import mb.KotlinBuiltIns;
import oc.Name;
import pb.CallableDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.u;
import qd.collections;
import sb.PropertyDescriptorImpl;
import sb.ValueParameterDescriptorImpl;
import sc.DescriptorFactory;
import sc.overridingUtils;
import yb.j0;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import zc.MemberScopeImpl;
import zc.c;

/* compiled from: LazyJavaScope.kt */
/* renamed from: cc.j, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class LazyJavaScope extends MemberScopeImpl {

    /* renamed from: m, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f5134m = {Reflection.g(new PropertyReference1Impl(Reflection.b(LazyJavaScope.class), "functionNamesLazy", "getFunctionNamesLazy()Ljava/util/Set;")), Reflection.g(new PropertyReference1Impl(Reflection.b(LazyJavaScope.class), "propertyNamesLazy", "getPropertyNamesLazy()Ljava/util/Set;")), Reflection.g(new PropertyReference1Impl(Reflection.b(LazyJavaScope.class), "classNamesLazy", "getClassNamesLazy()Ljava/util/Set;"))};

    /* renamed from: b, reason: collision with root package name */
    private final bc.g f5135b;

    /* renamed from: c, reason: collision with root package name */
    private final LazyJavaScope f5136c;

    /* renamed from: d, reason: collision with root package name */
    private final fd.i<Collection<DeclarationDescriptor>> f5137d;

    /* renamed from: e, reason: collision with root package name */
    private final fd.i<cc.b> f5138e;

    /* renamed from: f, reason: collision with root package name */
    private final fd.g<Name, Collection<SimpleFunctionDescriptor>> f5139f;

    /* renamed from: g, reason: collision with root package name */
    private final fd.h<Name, PropertyDescriptor> f5140g;

    /* renamed from: h, reason: collision with root package name */
    private final fd.g<Name, Collection<SimpleFunctionDescriptor>> f5141h;

    /* renamed from: i, reason: collision with root package name */
    private final fd.i f5142i;

    /* renamed from: j, reason: collision with root package name */
    private final fd.i f5143j;

    /* renamed from: k, reason: collision with root package name */
    private final fd.i f5144k;

    /* renamed from: l, reason: collision with root package name */
    private final fd.g<Name, List<PropertyDescriptor>> f5145l;

    /* JADX INFO: Access modifiers changed from: protected */
    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final g0 f5146a;

        /* renamed from: b, reason: collision with root package name */
        private final g0 f5147b;

        /* renamed from: c, reason: collision with root package name */
        private final List<ValueParameterDescriptor> f5148c;

        /* renamed from: d, reason: collision with root package name */
        private final List<TypeParameterDescriptor> f5149d;

        /* renamed from: e, reason: collision with root package name */
        private final boolean f5150e;

        /* renamed from: f, reason: collision with root package name */
        private final List<String> f5151f;

        /* JADX WARN: Multi-variable type inference failed */
        public a(g0 g0Var, g0 g0Var2, List<? extends ValueParameterDescriptor> list, List<? extends TypeParameterDescriptor> list2, boolean z10, List<String> list3) {
            za.k.e(g0Var, "returnType");
            za.k.e(list, "valueParameters");
            za.k.e(list2, "typeParameters");
            za.k.e(list3, "errors");
            this.f5146a = g0Var;
            this.f5147b = g0Var2;
            this.f5148c = list;
            this.f5149d = list2;
            this.f5150e = z10;
            this.f5151f = list3;
        }

        public final List<String> a() {
            return this.f5151f;
        }

        public final boolean b() {
            return this.f5150e;
        }

        public final g0 c() {
            return this.f5147b;
        }

        public final g0 d() {
            return this.f5146a;
        }

        public final List<TypeParameterDescriptor> e() {
            return this.f5149d;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return za.k.a(this.f5146a, aVar.f5146a) && za.k.a(this.f5147b, aVar.f5147b) && za.k.a(this.f5148c, aVar.f5148c) && za.k.a(this.f5149d, aVar.f5149d) && this.f5150e == aVar.f5150e && za.k.a(this.f5151f, aVar.f5151f);
        }

        public final List<ValueParameterDescriptor> f() {
            return this.f5148c;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int hashCode() {
            int hashCode = this.f5146a.hashCode() * 31;
            g0 g0Var = this.f5147b;
            int hashCode2 = (((((hashCode + (g0Var == null ? 0 : g0Var.hashCode())) * 31) + this.f5148c.hashCode()) * 31) + this.f5149d.hashCode()) * 31;
            boolean z10 = this.f5150e;
            int i10 = z10;
            if (z10 != 0) {
                i10 = 1;
            }
            return ((hashCode2 + i10) * 31) + this.f5151f.hashCode();
        }

        public String toString() {
            return "MethodSignatureData(returnType=" + this.f5146a + ", receiverType=" + this.f5147b + ", valueParameters=" + this.f5148c + ", typeParameters=" + this.f5149d + ", hasStableParameterNames=" + this.f5150e + ", errors=" + this.f5151f + ')';
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$b */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final List<ValueParameterDescriptor> f5152a;

        /* renamed from: b, reason: collision with root package name */
        private final boolean f5153b;

        /* JADX WARN: Multi-variable type inference failed */
        public b(List<? extends ValueParameterDescriptor> list, boolean z10) {
            za.k.e(list, "descriptors");
            this.f5152a = list;
            this.f5153b = z10;
        }

        public final List<ValueParameterDescriptor> a() {
            return this.f5152a;
        }

        public final boolean b() {
            return this.f5153b;
        }
    }

    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<Collection<? extends DeclarationDescriptor>> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<DeclarationDescriptor> invoke() {
            return LazyJavaScope.this.m(zc.d.f20436o, zc.h.f20461a.a());
        }
    }

    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.a<Set<? extends Name>> {
        d() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Set<Name> invoke() {
            return LazyJavaScope.this.l(zc.d.f20441t, null);
        }
    }

    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$e */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.l<Name, PropertyDescriptor> {
        e() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final PropertyDescriptor invoke(Name name) {
            za.k.e(name, "name");
            if (LazyJavaScope.this.B() != null) {
                return (PropertyDescriptor) LazyJavaScope.this.B().f5140g.invoke(name);
            }
            fc.n b10 = LazyJavaScope.this.y().invoke().b(name);
            if (b10 == null || b10.M()) {
                return null;
            }
            return LazyJavaScope.this.J(b10);
        }
    }

    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$f */
    /* loaded from: classes2.dex */
    static final class f extends Lambda implements ya.l<Name, Collection<? extends SimpleFunctionDescriptor>> {
        f() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<SimpleFunctionDescriptor> invoke(Name name) {
            za.k.e(name, "name");
            if (LazyJavaScope.this.B() != null) {
                return (Collection) LazyJavaScope.this.B().f5139f.invoke(name);
            }
            ArrayList arrayList = new ArrayList();
            for (r rVar : LazyJavaScope.this.y().invoke().d(name)) {
                JavaMethodDescriptor I = LazyJavaScope.this.I(rVar);
                if (LazyJavaScope.this.G(I)) {
                    LazyJavaScope.this.w().a().h().b(rVar, I);
                    arrayList.add(I);
                }
            }
            LazyJavaScope.this.o(arrayList, name);
            return arrayList;
        }
    }

    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$g */
    /* loaded from: classes2.dex */
    static final class g extends Lambda implements ya.a<cc.b> {
        g() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final cc.b invoke() {
            return LazyJavaScope.this.p();
        }
    }

    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$h */
    /* loaded from: classes2.dex */
    static final class h extends Lambda implements ya.a<Set<? extends Name>> {
        h() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Set<Name> invoke() {
            return LazyJavaScope.this.n(zc.d.f20443v, null);
        }
    }

    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$i */
    /* loaded from: classes2.dex */
    static final class i extends Lambda implements ya.l<Name, Collection<? extends SimpleFunctionDescriptor>> {
        i() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<SimpleFunctionDescriptor> invoke(Name name) {
            List z02;
            za.k.e(name, "name");
            LinkedHashSet linkedHashSet = new LinkedHashSet((Collection) LazyJavaScope.this.f5139f.invoke(name));
            LazyJavaScope.this.L(linkedHashSet);
            LazyJavaScope.this.r(linkedHashSet, name);
            z02 = _Collections.z0(LazyJavaScope.this.w().a().r().g(LazyJavaScope.this.w(), linkedHashSet));
            return z02;
        }
    }

    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$j */
    /* loaded from: classes2.dex */
    static final class j extends Lambda implements ya.l<Name, List<? extends PropertyDescriptor>> {
        j() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<PropertyDescriptor> invoke(Name name) {
            List<PropertyDescriptor> z02;
            List<PropertyDescriptor> z03;
            za.k.e(name, "name");
            ArrayList arrayList = new ArrayList();
            collections.a(arrayList, LazyJavaScope.this.f5140g.invoke(name));
            LazyJavaScope.this.s(name, arrayList);
            if (sc.e.t(LazyJavaScope.this.C())) {
                z03 = _Collections.z0(arrayList);
                return z03;
            }
            z02 = _Collections.z0(LazyJavaScope.this.w().a().r().g(LazyJavaScope.this.w(), arrayList));
            return z02;
        }
    }

    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$k */
    /* loaded from: classes2.dex */
    static final class k extends Lambda implements ya.a<Set<? extends Name>> {
        k() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Set<Name> invoke() {
            return LazyJavaScope.this.t(zc.d.f20444w, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$l */
    /* loaded from: classes2.dex */
    public static final class l extends Lambda implements ya.a<fd.j<? extends uc.g<?>>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ fc.n f5164f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ PropertyDescriptorImpl f5165g;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: LazyJavaScope.kt */
        /* renamed from: cc.j$l$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.a<uc.g<?>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ LazyJavaScope f5166e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ fc.n f5167f;

            /* renamed from: g, reason: collision with root package name */
            final /* synthetic */ PropertyDescriptorImpl f5168g;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(LazyJavaScope lazyJavaScope, fc.n nVar, PropertyDescriptorImpl propertyDescriptorImpl) {
                super(0);
                this.f5166e = lazyJavaScope;
                this.f5167f = nVar;
                this.f5168g = propertyDescriptorImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final uc.g<?> invoke() {
                return this.f5166e.w().a().g().a(this.f5167f, this.f5168g);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        l(fc.n nVar, PropertyDescriptorImpl propertyDescriptorImpl) {
            super(0);
            this.f5164f = nVar;
            this.f5165g = propertyDescriptorImpl;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final fd.j<uc.g<?>> invoke() {
            return LazyJavaScope.this.w().e().f(new a(LazyJavaScope.this, this.f5164f, this.f5165g));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LazyJavaScope.kt */
    /* renamed from: cc.j$m */
    /* loaded from: classes2.dex */
    public static final class m extends Lambda implements ya.l<SimpleFunctionDescriptor, CallableDescriptor> {

        /* renamed from: e, reason: collision with root package name */
        public static final m f5169e = new m();

        m() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CallableDescriptor invoke(SimpleFunctionDescriptor simpleFunctionDescriptor) {
            za.k.e(simpleFunctionDescriptor, "$this$selectMostSpecificInEachOverridableGroup");
            return simpleFunctionDescriptor;
        }
    }

    public /* synthetic */ LazyJavaScope(bc.g gVar, LazyJavaScope lazyJavaScope, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(gVar, (i10 & 2) != 0 ? null : lazyJavaScope);
    }

    private final Set<Name> A() {
        return (Set) fd.m.a(this.f5142i, this, f5134m[0]);
    }

    private final Set<Name> D() {
        return (Set) fd.m.a(this.f5143j, this, f5134m[1]);
    }

    private final g0 E(fc.n nVar) {
        g0 o10 = this.f5135b.g().o(nVar.getType(), dc.b.b(TypeUsage.COMMON, false, false, null, 7, null));
        if (!((KotlinBuiltIns.r0(o10) || KotlinBuiltIns.u0(o10)) && F(nVar) && nVar.U())) {
            return o10;
        }
        g0 n10 = s1.n(o10);
        za.k.d(n10, "makeNotNullable(propertyType)");
        return n10;
    }

    private final boolean F(fc.n nVar) {
        return nVar.w() && nVar.o();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final PropertyDescriptor J(fc.n nVar) {
        List<? extends TypeParameterDescriptor> j10;
        List<ReceiverParameterDescriptor> j11;
        PropertyDescriptorImpl u7 = u(nVar);
        u7.e1(null, null, null, null);
        g0 E = E(nVar);
        j10 = kotlin.collections.r.j();
        ReceiverParameterDescriptor z10 = z();
        j11 = kotlin.collections.r.j();
        u7.k1(E, j10, z10, null, j11);
        if (sc.e.K(u7, u7.getType())) {
            u7.U0(new l(nVar, u7));
        }
        this.f5135b.a().h().e(nVar, u7);
        return u7;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void L(Set<SimpleFunctionDescriptor> set) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : set) {
            String c10 = w.c((SimpleFunctionDescriptor) obj, false, false, 2, null);
            Object obj2 = linkedHashMap.get(c10);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(c10, obj2);
            }
            ((List) obj2).add(obj);
        }
        for (List list : linkedHashMap.values()) {
            if (list.size() != 1) {
                Collection<? extends SimpleFunctionDescriptor> a10 = overridingUtils.a(list, m.f5169e);
                set.removeAll(list);
                set.addAll(a10);
            }
        }
    }

    private final PropertyDescriptorImpl u(fc.n nVar) {
        JavaPropertyDescriptor o12 = JavaPropertyDescriptor.o1(C(), bc.e.a(this.f5135b, nVar), Modality.FINAL, j0.d(nVar.g()), !nVar.w(), nVar.getName(), this.f5135b.a().t().a(nVar), F(nVar));
        za.k.d(o12, "create(\n            owne…d.isFinalStatic\n        )");
        return o12;
    }

    private final Set<Name> x() {
        return (Set) fd.m.a(this.f5144k, this, f5134m[2]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final LazyJavaScope B() {
        return this.f5136c;
    }

    protected abstract DeclarationDescriptor C();

    protected boolean G(JavaMethodDescriptor javaMethodDescriptor) {
        za.k.e(javaMethodDescriptor, "<this>");
        return true;
    }

    protected abstract a H(r rVar, List<? extends TypeParameterDescriptor> list, g0 g0Var, List<? extends ValueParameterDescriptor> list2);

    /* JADX INFO: Access modifiers changed from: protected */
    public final JavaMethodDescriptor I(r rVar) {
        int u7;
        List<ReceiverParameterDescriptor> j10;
        Map<? extends CallableDescriptor.a<?>, ?> i10;
        Object T;
        za.k.e(rVar, Constants.MessagerConstants.METHOD_KEY);
        JavaMethodDescriptor y12 = JavaMethodDescriptor.y1(C(), bc.e.a(this.f5135b, rVar), rVar.getName(), this.f5135b.a().t().a(rVar), this.f5138e.invoke().c(rVar.getName()) != null && rVar.l().isEmpty());
        za.k.d(y12, "createJavaMethod(\n      …eters.isEmpty()\n        )");
        bc.g f10 = bc.a.f(this.f5135b, y12, rVar, 0, 4, null);
        List<y> m10 = rVar.m();
        u7 = s.u(m10, 10);
        List<? extends TypeParameterDescriptor> arrayList = new ArrayList<>(u7);
        Iterator<T> it = m10.iterator();
        while (it.hasNext()) {
            TypeParameterDescriptor a10 = f10.f().a((y) it.next());
            za.k.b(a10);
            arrayList.add(a10);
        }
        b K = K(f10, y12, rVar.l());
        a H = H(rVar, arrayList, q(rVar, f10), K.a());
        g0 c10 = H.c();
        ReceiverParameterDescriptor i11 = c10 != null ? DescriptorFactory.i(y12, c10, qb.g.f17195b.b()) : null;
        ReceiverParameterDescriptor z10 = z();
        j10 = kotlin.collections.r.j();
        List<TypeParameterDescriptor> e10 = H.e();
        List<ValueParameterDescriptor> f11 = H.f();
        g0 d10 = H.d();
        Modality a11 = Modality.f16676e.a(false, rVar.n(), !rVar.w());
        u d11 = j0.d(rVar.g());
        if (H.c() != null) {
            CallableDescriptor.a<ValueParameterDescriptor> aVar = JavaMethodDescriptor.K;
            T = _Collections.T(K.a());
            i10 = MapsJVM.f(ma.u.a(aVar, T));
        } else {
            i10 = m0.i();
        }
        y12.x1(i11, z10, j10, e10, f11, d10, a11, d11, i10);
        y12.B1(H.b(), K.b());
        if (!H.a().isEmpty()) {
            f10.a().s().a(y12, H.a());
        }
        return y12;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final b K(bc.g gVar, FunctionDescriptor functionDescriptor, List<? extends b0> list) {
        Iterable<IndexedValue> F0;
        int u7;
        List z02;
        o a10;
        Name name;
        bc.g gVar2 = gVar;
        za.k.e(gVar2, "c");
        za.k.e(functionDescriptor, "function");
        za.k.e(list, "jValueParameters");
        F0 = _Collections.F0(list);
        u7 = s.u(F0, 10);
        ArrayList arrayList = new ArrayList(u7);
        boolean z10 = false;
        for (IndexedValue indexedValue : F0) {
            int a11 = indexedValue.a();
            b0 b0Var = (b0) indexedValue.b();
            qb.g a12 = bc.e.a(gVar2, b0Var);
            dc.a b10 = dc.b.b(TypeUsage.COMMON, false, false, null, 7, null);
            if (b0Var.a()) {
                x type = b0Var.getType();
                fc.f fVar = type instanceof fc.f ? (fc.f) type : null;
                if (fVar != null) {
                    g0 k10 = gVar.g().k(fVar, b10, true);
                    a10 = ma.u.a(k10, gVar.d().t().k(k10));
                } else {
                    throw new AssertionError("Vararg parameter should be an array: " + b0Var);
                }
            } else {
                a10 = ma.u.a(gVar.g().o(b0Var.getType(), b10), null);
            }
            g0 g0Var = (g0) a10.a();
            g0 g0Var2 = (g0) a10.b();
            if (za.k.a(functionDescriptor.getName().b(), "equals") && list.size() == 1 && za.k.a(gVar.d().t().I(), g0Var)) {
                name = Name.f("other");
            } else {
                name = b0Var.getName();
                if (name == null) {
                    z10 = true;
                }
                if (name == null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append('p');
                    sb2.append(a11);
                    name = Name.f(sb2.toString());
                    za.k.d(name, "identifier(\"p$index\")");
                }
            }
            Name name2 = name;
            za.k.d(name2, "if (function.name.asStri…(\"p$index\")\n            }");
            ArrayList arrayList2 = arrayList;
            arrayList2.add(new ValueParameterDescriptorImpl(functionDescriptor, null, a11, a12, name2, g0Var, false, false, false, g0Var2, gVar.a().t().a(b0Var)));
            arrayList = arrayList2;
            z10 = z10;
            gVar2 = gVar;
        }
        z02 = _Collections.z0(arrayList);
        return new b(z02, z10);
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Collection<PropertyDescriptor> a(Name name, xb.b bVar) {
        List j10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        if (d().contains(name)) {
            return this.f5145l.invoke(name);
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> b() {
        return A();
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Collection<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        List j10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        if (b().contains(name)) {
            return this.f5141h.invoke(name);
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> d() {
        return D();
    }

    @Override // zc.MemberScopeImpl, zc.h
    public Set<Name> f() {
        return x();
    }

    @Override // zc.MemberScopeImpl, zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        return this.f5137d.invoke();
    }

    protected abstract Set<Name> l(zc.d dVar, ya.l<? super Name, Boolean> lVar);

    protected final List<DeclarationDescriptor> m(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        List<DeclarationDescriptor> z02;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        xb.d dVar2 = xb.d.WHEN_GET_ALL_DESCRIPTORS;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        if (dVar.a(zc.d.f20424c.c())) {
            for (Name name : l(dVar, lVar)) {
                if (lVar.invoke(name).booleanValue()) {
                    collections.a(linkedHashSet, e(name, dVar2));
                }
            }
        }
        if (dVar.a(zc.d.f20424c.d()) && !dVar.l().contains(c.a.f20421a)) {
            for (Name name2 : n(dVar, lVar)) {
                if (lVar.invoke(name2).booleanValue()) {
                    linkedHashSet.addAll(c(name2, dVar2));
                }
            }
        }
        if (dVar.a(zc.d.f20424c.i()) && !dVar.l().contains(c.a.f20421a)) {
            for (Name name3 : t(dVar, lVar)) {
                if (lVar.invoke(name3).booleanValue()) {
                    linkedHashSet.addAll(a(name3, dVar2));
                }
            }
        }
        z02 = _Collections.z0(linkedHashSet);
        return z02;
    }

    protected abstract Set<Name> n(zc.d dVar, ya.l<? super Name, Boolean> lVar);

    protected void o(Collection<SimpleFunctionDescriptor> collection, Name name) {
        za.k.e(collection, "result");
        za.k.e(name, "name");
    }

    protected abstract cc.b p();

    /* JADX INFO: Access modifiers changed from: protected */
    public final g0 q(r rVar, bc.g gVar) {
        za.k.e(rVar, Constants.MessagerConstants.METHOD_KEY);
        za.k.e(gVar, "c");
        return gVar.g().o(rVar.f(), dc.b.b(TypeUsage.COMMON, rVar.V().y(), false, null, 6, null));
    }

    protected abstract void r(Collection<SimpleFunctionDescriptor> collection, Name name);

    protected abstract void s(Name name, Collection<PropertyDescriptor> collection);

    protected abstract Set<Name> t(zc.d dVar, ya.l<? super Name, Boolean> lVar);

    public String toString() {
        return "Lazy scope for " + C();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final fd.i<Collection<DeclarationDescriptor>> v() {
        return this.f5137d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final bc.g w() {
        return this.f5135b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final fd.i<cc.b> y() {
        return this.f5138e;
    }

    protected abstract ReceiverParameterDescriptor z();

    public LazyJavaScope(bc.g gVar, LazyJavaScope lazyJavaScope) {
        List j10;
        za.k.e(gVar, "c");
        this.f5135b = gVar;
        this.f5136c = lazyJavaScope;
        StorageManager e10 = gVar.e();
        c cVar = new c();
        j10 = kotlin.collections.r.j();
        this.f5137d = e10.a(cVar, j10);
        this.f5138e = gVar.e().g(new g());
        this.f5139f = gVar.e().d(new f());
        this.f5140g = gVar.e().b(new e());
        this.f5141h = gVar.e().d(new i());
        this.f5142i = gVar.e().g(new h());
        this.f5143j = gVar.e().g(new k());
        this.f5144k = gVar.e().g(new d());
        this.f5145l = gVar.e().d(new j());
    }
}
