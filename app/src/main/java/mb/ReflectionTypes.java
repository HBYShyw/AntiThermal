package mb;

import gd.c1;
import gd.g0;
import gd.h0;
import gd.u0;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import mb.StandardNames;
import oc.ClassId;
import oc.Name;
import od.capitalizeDecapitalize;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import pb.TypeParameterDescriptor;
import pb.findClassInModule;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: ReflectionTypes.kt */
/* renamed from: mb.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectionTypes {

    /* renamed from: a, reason: collision with root package name */
    private final NotFoundClasses f15251a;

    /* renamed from: b, reason: collision with root package name */
    private final ma.h f15252b;

    /* renamed from: c, reason: collision with root package name */
    private final a f15253c;

    /* renamed from: d, reason: collision with root package name */
    private final a f15254d;

    /* renamed from: e, reason: collision with root package name */
    private final a f15255e;

    /* renamed from: f, reason: collision with root package name */
    private final a f15256f;

    /* renamed from: g, reason: collision with root package name */
    private final a f15257g;

    /* renamed from: h, reason: collision with root package name */
    private final a f15258h;

    /* renamed from: i, reason: collision with root package name */
    private final a f15259i;

    /* renamed from: j, reason: collision with root package name */
    private final a f15260j;

    /* renamed from: l, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f15250l = {Reflection.g(new PropertyReference1Impl(Reflection.b(ReflectionTypes.class), "kClass", "getKClass()Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(ReflectionTypes.class), "kProperty", "getKProperty()Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(ReflectionTypes.class), "kProperty0", "getKProperty0()Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(ReflectionTypes.class), "kProperty1", "getKProperty1()Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(ReflectionTypes.class), "kProperty2", "getKProperty2()Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(ReflectionTypes.class), "kMutableProperty0", "getKMutableProperty0()Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(ReflectionTypes.class), "kMutableProperty1", "getKMutableProperty1()Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(ReflectionTypes.class), "kMutableProperty2", "getKMutableProperty2()Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;"))};

    /* renamed from: k, reason: collision with root package name */
    public static final b f15249k = new b(null);

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ReflectionTypes.kt */
    /* renamed from: mb.j$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final int f15261a;

        public a(int i10) {
            this.f15261a = i10;
        }

        public final ClassDescriptor a(ReflectionTypes reflectionTypes, gb.l<?> lVar) {
            za.k.e(reflectionTypes, "types");
            za.k.e(lVar, "property");
            return reflectionTypes.b(capitalizeDecapitalize.a(lVar.getName()), this.f15261a);
        }
    }

    /* compiled from: ReflectionTypes.kt */
    /* renamed from: mb.j$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final g0 a(ModuleDescriptor moduleDescriptor) {
            Object q02;
            List e10;
            za.k.e(moduleDescriptor, "module");
            ClassDescriptor a10 = findClassInModule.a(moduleDescriptor, StandardNames.a.f15326s0);
            if (a10 == null) {
                return null;
            }
            c1 h10 = c1.f11749f.h();
            List<TypeParameterDescriptor> parameters = a10.n().getParameters();
            za.k.d(parameters, "kPropertyClass.typeConstructor.parameters");
            q02 = _Collections.q0(parameters);
            za.k.d(q02, "kPropertyClass.typeConstructor.parameters.single()");
            e10 = CollectionsJVM.e(new u0((TypeParameterDescriptor) q02));
            return h0.g(h10, a10, e10);
        }
    }

    /* compiled from: ReflectionTypes.kt */
    /* renamed from: mb.j$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<zc.h> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ModuleDescriptor f15262e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(ModuleDescriptor moduleDescriptor) {
            super(0);
            this.f15262e = moduleDescriptor;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final zc.h invoke() {
            return this.f15262e.H(StandardNames.f15280r).u();
        }
    }

    public ReflectionTypes(ModuleDescriptor moduleDescriptor, NotFoundClasses notFoundClasses) {
        ma.h a10;
        za.k.e(moduleDescriptor, "module");
        za.k.e(notFoundClasses, "notFoundClasses");
        this.f15251a = notFoundClasses;
        a10 = ma.j.a(ma.l.PUBLICATION, new c(moduleDescriptor));
        this.f15252b = a10;
        this.f15253c = new a(1);
        this.f15254d = new a(1);
        this.f15255e = new a(1);
        this.f15256f = new a(2);
        this.f15257g = new a(3);
        this.f15258h = new a(1);
        this.f15259i = new a(2);
        this.f15260j = new a(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ClassDescriptor b(String str, int i10) {
        List<Integer> e10;
        Name f10 = Name.f(str);
        za.k.d(f10, "identifier(className)");
        ClassifierDescriptor e11 = d().e(f10, xb.d.FROM_REFLECTION);
        ClassDescriptor classDescriptor = e11 instanceof ClassDescriptor ? (ClassDescriptor) e11 : null;
        if (classDescriptor != null) {
            return classDescriptor;
        }
        NotFoundClasses notFoundClasses = this.f15251a;
        ClassId classId = new ClassId(StandardNames.f15280r, f10);
        e10 = CollectionsJVM.e(Integer.valueOf(i10));
        return notFoundClasses.d(classId, e10);
    }

    private final zc.h d() {
        return (zc.h) this.f15252b.getValue();
    }

    public final ClassDescriptor c() {
        return this.f15253c.a(this, f15250l[0]);
    }
}
