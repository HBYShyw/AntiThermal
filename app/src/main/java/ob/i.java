package ob;

import cc.LazyJavaClassDescriptor;
import cc.LazyJavaClassMemberScope;
import cd.NameResolverUtil;
import ed.DeserializedClassDescriptor;
import fd.StorageManager;
import fd.m;
import gd.TypeSubstitutor;
import gd.g0;
import gd.j0;
import gd.o0;
import hc.SignatureBuildingComponents;
import hc.methodSignatureBuildingUtils;
import hc.w;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import kotlin.collections.r;
import kotlin.collections.s;
import kotlin.collections.s0;
import mb.KotlinBuiltIns;
import ob.JvmBuiltIns;
import oc.ClassId;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.ModalityUtils;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import pb.SimpleFunctionDescriptor;
import pb.SourceElement;
import pb.ValueParameterDescriptor;
import pb.findClassInModule;
import qb.AnnotationDescriptor;
import qb.annotationUtil;
import qb.g;
import qd.DFS;
import qd.SmartSet;
import rb.AdditionalClassPartsProvider;
import sb.ClassDescriptorImpl;
import sb.PackageFragmentDescriptorImpl;
import sc.OverridingUtil;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import za.y;
import zb.JavaResolverCache;
import zc.h;

/* compiled from: JvmBuiltInsCustomizer.kt */
/* loaded from: classes2.dex */
public final class i implements AdditionalClassPartsProvider, rb.c {

    /* renamed from: h, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f16389h = {Reflection.g(new PropertyReference1Impl(Reflection.b(i.class), "settings", "getSettings()Lorg/jetbrains/kotlin/builtins/jvm/JvmBuiltIns$Settings;")), Reflection.g(new PropertyReference1Impl(Reflection.b(i.class), "cloneableType", "getCloneableType()Lorg/jetbrains/kotlin/types/SimpleType;")), Reflection.g(new PropertyReference1Impl(Reflection.b(i.class), "notConsideredDeprecation", "getNotConsideredDeprecation()Lorg/jetbrains/kotlin/descriptors/annotations/Annotations;"))};

    /* renamed from: a, reason: collision with root package name */
    private final ModuleDescriptor f16390a;

    /* renamed from: b, reason: collision with root package name */
    private final JavaToKotlinClassMapper f16391b;

    /* renamed from: c, reason: collision with root package name */
    private final fd.i f16392c;

    /* renamed from: d, reason: collision with root package name */
    private final g0 f16393d;

    /* renamed from: e, reason: collision with root package name */
    private final fd.i f16394e;

    /* renamed from: f, reason: collision with root package name */
    private final fd.a<FqName, ClassDescriptor> f16395f;

    /* renamed from: g, reason: collision with root package name */
    private final fd.i f16396g;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    public enum a {
        HIDDEN,
        VISIBLE,
        NOT_CONSIDERED,
        DROP
    }

    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f16402a;

        static {
            int[] iArr = new int[a.values().length];
            try {
                iArr[a.HIDDEN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[a.NOT_CONSIDERED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[a.DROP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[a.VISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f16402a = iArr;
        }
    }

    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<o0> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ StorageManager f16404f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(StorageManager storageManager) {
            super(0);
            this.f16404f = storageManager;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final o0 invoke() {
            return findClassInModule.c(i.this.u().a(), JvmBuiltInClassDescriptorFactory.f16360d.a(), new NotFoundClasses(this.f16404f, i.this.u().a())).x();
        }
    }

    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    public static final class d extends PackageFragmentDescriptorImpl {
        d(ModuleDescriptor moduleDescriptor, FqName fqName) {
            super(moduleDescriptor, fqName);
        }

        @Override // pb.PackageFragmentDescriptor
        /* renamed from: O0, reason: merged with bridge method [inline-methods] */
        public h.b u() {
            return h.b.f20465b;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    public static final class e extends Lambda implements ya.a<g0> {
        e() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke() {
            o0 i10 = i.this.f16390a.t().i();
            za.k.d(i10, "moduleDescriptor.builtIns.anyType");
            return i10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    public static final class f extends Lambda implements ya.a<ClassDescriptor> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ LazyJavaClassDescriptor f16406e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ ClassDescriptor f16407f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        f(LazyJavaClassDescriptor lazyJavaClassDescriptor, ClassDescriptor classDescriptor) {
            super(0);
            this.f16406e = lazyJavaClassDescriptor;
            this.f16407f = classDescriptor;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ClassDescriptor invoke() {
            LazyJavaClassDescriptor lazyJavaClassDescriptor = this.f16406e;
            JavaResolverCache javaResolverCache = JavaResolverCache.f20404a;
            za.k.d(javaResolverCache, "EMPTY");
            return lazyJavaClassDescriptor.W0(javaResolverCache, this.f16407f);
        }
    }

    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    static final class g extends Lambda implements ya.l<zc.h, Collection<? extends SimpleFunctionDescriptor>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Name f16408e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        g(Name name) {
            super(1);
            this.f16408e = name;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Collection<SimpleFunctionDescriptor> invoke(zc.h hVar) {
            za.k.e(hVar, "it");
            return hVar.c(this.f16408e, xb.d.FROM_BUILTINS);
        }
    }

    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    public static final class h extends DFS.b<ClassDescriptor, a> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f16409a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ y<a> f16410b;

        h(String str, y<a> yVar) {
            this.f16409a = str;
            this.f16410b = yVar;
        }

        /* JADX WARN: Type inference failed for: r0v4, types: [T, ob.i$a] */
        /* JADX WARN: Type inference failed for: r0v5, types: [T, ob.i$a] */
        /* JADX WARN: Type inference failed for: r0v6, types: [T, ob.i$a] */
        @Override // qd.DFS.d
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public boolean c(ClassDescriptor classDescriptor) {
            za.k.e(classDescriptor, "javaClassDescriptor");
            String a10 = methodSignatureBuildingUtils.a(SignatureBuildingComponents.f12209a, classDescriptor, this.f16409a);
            JvmBuiltInsSignatures jvmBuiltInsSignatures = JvmBuiltInsSignatures.f16414a;
            if (jvmBuiltInsSignatures.e().contains(a10)) {
                this.f16410b.f20376e = a.HIDDEN;
            } else if (jvmBuiltInsSignatures.h().contains(a10)) {
                this.f16410b.f20376e = a.VISIBLE;
            } else if (jvmBuiltInsSignatures.c().contains(a10)) {
                this.f16410b.f20376e = a.DROP;
            }
            return this.f16410b.f20376e == null;
        }

        @Override // qd.DFS.d
        /* renamed from: e, reason: merged with bridge method [inline-methods] */
        public a a() {
            a aVar = this.f16410b.f20376e;
            return aVar == null ? a.NOT_CONSIDERED : aVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* renamed from: ob.i$i, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0086i extends Lambda implements ya.l<CallableMemberDescriptor, Boolean> {
        C0086i() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            boolean z10;
            if (callableMemberDescriptor.getKind() == CallableMemberDescriptor.a.DECLARATION) {
                JavaToKotlinClassMapper javaToKotlinClassMapper = i.this.f16391b;
                DeclarationDescriptor b10 = callableMemberDescriptor.b();
                za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                if (javaToKotlinClassMapper.c((ClassDescriptor) b10)) {
                    z10 = true;
                    return Boolean.valueOf(z10);
                }
            }
            z10 = false;
            return Boolean.valueOf(z10);
        }
    }

    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    static final class j extends Lambda implements ya.a<qb.g> {
        j() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final qb.g invoke() {
            List<? extends AnnotationDescriptor> e10;
            AnnotationDescriptor b10 = annotationUtil.b(i.this.f16390a.t(), "This member is not fully supported by Kotlin compiler, so it may be absent or have different signature in next major version", null, null, 6, null);
            g.a aVar = qb.g.f17195b;
            e10 = CollectionsJVM.e(b10);
            return aVar.a(e10);
        }
    }

    public i(ModuleDescriptor moduleDescriptor, StorageManager storageManager, ya.a<JvmBuiltIns.b> aVar) {
        za.k.e(moduleDescriptor, "moduleDescriptor");
        za.k.e(storageManager, "storageManager");
        za.k.e(aVar, "settingsComputation");
        this.f16390a = moduleDescriptor;
        this.f16391b = JavaToKotlinClassMapper.f16359a;
        this.f16392c = storageManager.g(aVar);
        this.f16393d = l(storageManager);
        this.f16394e = storageManager.g(new c(storageManager));
        this.f16395f = storageManager.e();
        this.f16396g = storageManager.g(new j());
    }

    private final SimpleFunctionDescriptor k(DeserializedClassDescriptor deserializedClassDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor) {
        FunctionDescriptor.a<? extends SimpleFunctionDescriptor> A = simpleFunctionDescriptor.A();
        A.r(deserializedClassDescriptor);
        A.p(DescriptorVisibilities.f16733e);
        A.e(deserializedClassDescriptor.x());
        A.g(deserializedClassDescriptor.S0());
        SimpleFunctionDescriptor build = A.build();
        za.k.b(build);
        return build;
    }

    private final g0 l(StorageManager storageManager) {
        List e10;
        Set<ClassConstructorDescriptor> e11;
        d dVar = new d(this.f16390a, new FqName("java.io"));
        e10 = CollectionsJVM.e(new j0(storageManager, new e()));
        ClassDescriptorImpl classDescriptorImpl = new ClassDescriptorImpl(dVar, Name.f("Serializable"), Modality.ABSTRACT, ClassKind.INTERFACE, e10, SourceElement.f16664a, false, storageManager);
        h.b bVar = h.b.f20465b;
        e11 = s0.e();
        classDescriptorImpl.T0(bVar, e11, null);
        o0 x10 = classDescriptorImpl.x();
        za.k.d(x10, "mockSerializableClass.defaultType");
        return x10;
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x00eb, code lost:
    
        if (v(r3, r10) != false) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final Collection<SimpleFunctionDescriptor> m(ClassDescriptor classDescriptor, ya.l<? super zc.h, ? extends Collection<? extends SimpleFunctionDescriptor>> lVar) {
        Object f02;
        int u7;
        boolean z10;
        List j10;
        List j11;
        LazyJavaClassDescriptor q10 = q(classDescriptor);
        if (q10 == null) {
            j11 = r.j();
            return j11;
        }
        Collection<ClassDescriptor> g6 = this.f16391b.g(wc.c.l(q10), ob.b.f16337h.a());
        f02 = _Collections.f0(g6);
        ClassDescriptor classDescriptor2 = (ClassDescriptor) f02;
        if (classDescriptor2 == null) {
            j10 = r.j();
            return j10;
        }
        SmartSet.b bVar = SmartSet.f17432g;
        u7 = s.u(g6, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = g6.iterator();
        while (it.hasNext()) {
            arrayList.add(wc.c.l((ClassDescriptor) it.next()));
        }
        SmartSet b10 = bVar.b(arrayList);
        boolean c10 = this.f16391b.c(classDescriptor);
        zc.h M0 = this.f16395f.a(wc.c.l(q10), new f(q10, classDescriptor2)).M0();
        za.k.d(M0, "fakeJavaClassDescriptor.unsubstitutedMemberScope");
        Collection<? extends SimpleFunctionDescriptor> invoke = lVar.invoke(M0);
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : invoke) {
            SimpleFunctionDescriptor simpleFunctionDescriptor = (SimpleFunctionDescriptor) obj;
            boolean z11 = true;
            if (simpleFunctionDescriptor.getKind() == CallableMemberDescriptor.a.DECLARATION && simpleFunctionDescriptor.g().d() && !KotlinBuiltIns.j0(simpleFunctionDescriptor)) {
                Collection<? extends FunctionDescriptor> e10 = simpleFunctionDescriptor.e();
                za.k.d(e10, "analogueMember.overriddenDescriptors");
                if (!(e10 instanceof Collection) || !e10.isEmpty()) {
                    Iterator<T> it2 = e10.iterator();
                    while (it2.hasNext()) {
                        DeclarationDescriptor b11 = ((FunctionDescriptor) it2.next()).b();
                        za.k.d(b11, "it.containingDeclaration");
                        if (b10.contains(wc.c.l(b11))) {
                            z10 = true;
                            break;
                        }
                    }
                }
                z10 = false;
                if (!z10) {
                }
            }
            z11 = false;
            if (z11) {
                arrayList2.add(obj);
            }
        }
        return arrayList2;
    }

    private final o0 n() {
        return (o0) m.a(this.f16394e, this, f16389h[1]);
    }

    private static final boolean o(ConstructorDescriptor constructorDescriptor, TypeSubstitutor typeSubstitutor, ConstructorDescriptor constructorDescriptor2) {
        return OverridingUtil.x(constructorDescriptor, constructorDescriptor2.c(typeSubstitutor)) == OverridingUtil.i.a.OVERRIDABLE;
    }

    private final LazyJavaClassDescriptor q(ClassDescriptor classDescriptor) {
        ClassId n10;
        FqName b10;
        if (KotlinBuiltIns.a0(classDescriptor) || !KotlinBuiltIns.A0(classDescriptor)) {
            return null;
        }
        FqNameUnsafe m10 = wc.c.m(classDescriptor);
        if (!m10.f() || (n10 = JavaToKotlinClassMap.f16339a.n(m10)) == null || (b10 = n10.b()) == null) {
            return null;
        }
        ClassDescriptor c10 = pb.s.c(u().a(), b10, xb.d.FROM_BUILTINS);
        if (c10 instanceof LazyJavaClassDescriptor) {
            return (LazyJavaClassDescriptor) c10;
        }
        return null;
    }

    private final a r(FunctionDescriptor functionDescriptor) {
        List e10;
        DeclarationDescriptor b10 = functionDescriptor.b();
        za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
        String c10 = w.c(functionDescriptor, false, false, 3, null);
        y yVar = new y();
        e10 = CollectionsJVM.e((ClassDescriptor) b10);
        Object b11 = DFS.b(e10, new ob.h(this), new h(c10, yVar));
        za.k.d(b11, "jvmDescriptor = computeJ…CONSIDERED\n            })");
        return (a) b11;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Iterable s(i iVar, ClassDescriptor classDescriptor) {
        za.k.e(iVar, "this$0");
        Collection<g0> q10 = classDescriptor.n().q();
        za.k.d(q10, "it.typeConstructor.supertypes");
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = q10.iterator();
        while (it.hasNext()) {
            ClassifierDescriptor v7 = ((g0) it.next()).W0().v();
            ClassifierDescriptor a10 = v7 != null ? v7.a() : null;
            ClassDescriptor classDescriptor2 = a10 instanceof ClassDescriptor ? (ClassDescriptor) a10 : null;
            LazyJavaClassDescriptor q11 = classDescriptor2 != null ? iVar.q(classDescriptor2) : null;
            if (q11 != null) {
                arrayList.add(q11);
            }
        }
        return arrayList;
    }

    private final qb.g t() {
        return (qb.g) m.a(this.f16396g, this, f16389h[2]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final JvmBuiltIns.b u() {
        return (JvmBuiltIns.b) m.a(this.f16392c, this, f16389h[0]);
    }

    private final boolean v(SimpleFunctionDescriptor simpleFunctionDescriptor, boolean z10) {
        List e10;
        DeclarationDescriptor b10 = simpleFunctionDescriptor.b();
        za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
        String c10 = w.c(simpleFunctionDescriptor, false, false, 3, null);
        if (z10 ^ JvmBuiltInsSignatures.f16414a.f().contains(methodSignatureBuildingUtils.a(SignatureBuildingComponents.f12209a, (ClassDescriptor) b10, c10))) {
            return true;
        }
        e10 = CollectionsJVM.e(simpleFunctionDescriptor);
        Boolean e11 = DFS.e(e10, ob.g.f16387a, new C0086i());
        za.k.d(e11, "private fun SimpleFuncti…scriptor)\n        }\n    }");
        return e11.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Iterable w(CallableMemberDescriptor callableMemberDescriptor) {
        return callableMemberDescriptor.a().e();
    }

    private final boolean x(ConstructorDescriptor constructorDescriptor, ClassDescriptor classDescriptor) {
        Object q02;
        if (constructorDescriptor.l().size() == 1) {
            List<ValueParameterDescriptor> l10 = constructorDescriptor.l();
            za.k.d(l10, "valueParameters");
            q02 = _Collections.q0(l10);
            ClassifierDescriptor v7 = ((ValueParameterDescriptor) q02).getType().W0().v();
            if (za.k.a(v7 != null ? wc.c.m(v7) : null, wc.c.m(classDescriptor))) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x00fc, code lost:
    
        if (r2 != 3) goto L42;
     */
    @Override // rb.AdditionalClassPartsProvider
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Collection<SimpleFunctionDescriptor> b(Name name, ClassDescriptor classDescriptor) {
        List j10;
        Object p02;
        List e10;
        List j11;
        za.k.e(name, "name");
        za.k.e(classDescriptor, "classDescriptor");
        boolean z10 = true;
        if (za.k.a(name, CloneableClassScope.f16335e.a()) && (classDescriptor instanceof DeserializedClassDescriptor) && KotlinBuiltIns.e0(classDescriptor)) {
            DeserializedClassDescriptor deserializedClassDescriptor = (DeserializedClassDescriptor) classDescriptor;
            List<jc.i> C0 = deserializedClassDescriptor.k1().C0();
            za.k.d(C0, "classDescriptor.classProto.functionList");
            if (!(C0 instanceof Collection) || !C0.isEmpty()) {
                Iterator<T> it = C0.iterator();
                while (it.hasNext()) {
                    if (za.k.a(NameResolverUtil.b(deserializedClassDescriptor.j1().g(), ((jc.i) it.next()).Y()), CloneableClassScope.f16335e.a())) {
                        break;
                    }
                }
            }
            z10 = false;
            if (z10) {
                j11 = r.j();
                return j11;
            }
            p02 = _Collections.p0(n().u().c(name, xb.d.FROM_BUILTINS));
            e10 = CollectionsJVM.e(k(deserializedClassDescriptor, (SimpleFunctionDescriptor) p02));
            return e10;
        }
        if (!u().b()) {
            j10 = r.j();
            return j10;
        }
        Collection<SimpleFunctionDescriptor> m10 = m(classDescriptor, new g(name));
        ArrayList arrayList = new ArrayList();
        for (SimpleFunctionDescriptor simpleFunctionDescriptor : m10) {
            DeclarationDescriptor b10 = simpleFunctionDescriptor.b();
            za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
            FunctionDescriptor c10 = simpleFunctionDescriptor.c(mappingUtil.a((ClassDescriptor) b10, classDescriptor).c());
            za.k.c(c10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor");
            FunctionDescriptor.a<? extends SimpleFunctionDescriptor> A = ((SimpleFunctionDescriptor) c10).A();
            A.r(classDescriptor);
            A.g(classDescriptor.S0());
            A.h();
            int i10 = b.f16402a[r(simpleFunctionDescriptor).ordinal()];
            SimpleFunctionDescriptor simpleFunctionDescriptor2 = null;
            if (i10 == 1) {
                if (!ModalityUtils.a(classDescriptor)) {
                    A.k();
                    SimpleFunctionDescriptor build = A.build();
                    za.k.b(build);
                    simpleFunctionDescriptor2 = build;
                }
            } else {
                if (i10 == 2) {
                    A.b(t());
                }
                SimpleFunctionDescriptor build2 = A.build();
                za.k.b(build2);
                simpleFunctionDescriptor2 = build2;
            }
            if (simpleFunctionDescriptor2 != null) {
                arrayList.add(simpleFunctionDescriptor2);
            }
        }
        return arrayList;
    }

    @Override // rb.AdditionalClassPartsProvider
    public Collection<g0> c(ClassDescriptor classDescriptor) {
        List j10;
        List e10;
        List m10;
        za.k.e(classDescriptor, "classDescriptor");
        FqNameUnsafe m11 = wc.c.m(classDescriptor);
        JvmBuiltInsSignatures jvmBuiltInsSignatures = JvmBuiltInsSignatures.f16414a;
        if (jvmBuiltInsSignatures.i(m11)) {
            o0 n10 = n();
            za.k.d(n10, "cloneableType");
            m10 = r.m(n10, this.f16393d);
            return m10;
        }
        if (jvmBuiltInsSignatures.j(m11)) {
            e10 = CollectionsJVM.e(this.f16393d);
            return e10;
        }
        j10 = r.j();
        return j10;
    }

    @Override // rb.c
    public boolean d(ClassDescriptor classDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor) {
        za.k.e(classDescriptor, "classDescriptor");
        za.k.e(simpleFunctionDescriptor, "functionDescriptor");
        LazyJavaClassDescriptor q10 = q(classDescriptor);
        if (q10 == null || !simpleFunctionDescriptor.i().a(rb.d.a())) {
            return true;
        }
        if (!u().b()) {
            return false;
        }
        String c10 = w.c(simpleFunctionDescriptor, false, false, 3, null);
        LazyJavaClassMemberScope M0 = q10.M0();
        Name name = simpleFunctionDescriptor.getName();
        za.k.d(name, "functionDescriptor.name");
        Collection<SimpleFunctionDescriptor> c11 = M0.c(name, xb.d.FROM_BUILTINS);
        if (!(c11 instanceof Collection) || !c11.isEmpty()) {
            Iterator<T> it = c11.iterator();
            while (it.hasNext()) {
                if (za.k.a(w.c((SimpleFunctionDescriptor) it.next(), false, false, 3, null), c10)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // rb.AdditionalClassPartsProvider
    public Collection<ClassConstructorDescriptor> e(ClassDescriptor classDescriptor) {
        List j10;
        int u7;
        boolean z10;
        List j11;
        List j12;
        za.k.e(classDescriptor, "classDescriptor");
        if (classDescriptor.getKind() == ClassKind.CLASS && u().b()) {
            LazyJavaClassDescriptor q10 = q(classDescriptor);
            if (q10 == null) {
                j12 = r.j();
                return j12;
            }
            ClassDescriptor f10 = JavaToKotlinClassMapper.f(this.f16391b, wc.c.l(q10), ob.b.f16337h.a(), null, 4, null);
            if (f10 == null) {
                j11 = r.j();
                return j11;
            }
            TypeSubstitutor c10 = mappingUtil.a(f10, q10).c();
            List<ClassConstructorDescriptor> p10 = q10.p();
            ArrayList<ClassConstructorDescriptor> arrayList = new ArrayList();
            Iterator<T> it = p10.iterator();
            while (true) {
                boolean z11 = false;
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                ClassConstructorDescriptor classConstructorDescriptor = (ClassConstructorDescriptor) next;
                if (classConstructorDescriptor.g().d()) {
                    Collection<ClassConstructorDescriptor> p11 = f10.p();
                    za.k.d(p11, "defaultKotlinVersion.constructors");
                    if (!(p11 instanceof Collection) || !p11.isEmpty()) {
                        for (ClassConstructorDescriptor classConstructorDescriptor2 : p11) {
                            za.k.d(classConstructorDescriptor2, "it");
                            if (o(classConstructorDescriptor2, c10, classConstructorDescriptor)) {
                                z10 = false;
                                break;
                            }
                        }
                    }
                    z10 = true;
                    if (z10 && !x(classConstructorDescriptor, classDescriptor) && !KotlinBuiltIns.j0(classConstructorDescriptor) && !JvmBuiltInsSignatures.f16414a.d().contains(methodSignatureBuildingUtils.a(SignatureBuildingComponents.f12209a, q10, w.c(classConstructorDescriptor, false, false, 3, null)))) {
                        z11 = true;
                    }
                }
                if (z11) {
                    arrayList.add(next);
                }
            }
            u7 = s.u(arrayList, 10);
            ArrayList arrayList2 = new ArrayList(u7);
            for (ClassConstructorDescriptor classConstructorDescriptor3 : arrayList) {
                FunctionDescriptor.a<? extends FunctionDescriptor> A = classConstructorDescriptor3.A();
                A.r(classDescriptor);
                A.e(classDescriptor.x());
                A.h();
                A.d(c10.j());
                if (!JvmBuiltInsSignatures.f16414a.g().contains(methodSignatureBuildingUtils.a(SignatureBuildingComponents.f12209a, q10, w.c(classConstructorDescriptor3, false, false, 3, null)))) {
                    A.b(t());
                }
                FunctionDescriptor build = A.build();
                za.k.c(build, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor");
                arrayList2.add((ClassConstructorDescriptor) build);
            }
            return arrayList2;
        }
        j10 = r.j();
        return j10;
    }

    @Override // rb.AdditionalClassPartsProvider
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public Set<Name> a(ClassDescriptor classDescriptor) {
        Set<Name> e10;
        LazyJavaClassMemberScope M0;
        Set<Name> b10;
        Set<Name> e11;
        za.k.e(classDescriptor, "classDescriptor");
        if (!u().b()) {
            e11 = s0.e();
            return e11;
        }
        LazyJavaClassDescriptor q10 = q(classDescriptor);
        if (q10 != null && (M0 = q10.M0()) != null && (b10 = M0.b()) != null) {
            return b10;
        }
        e10 = s0.e();
        return e10;
    }
}
