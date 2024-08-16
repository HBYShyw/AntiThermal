package ld;

import gd.TypeConstructor;
import gd.TypeProjection;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.a0;
import gd.d1;
import gd.e;
import gd.g0;
import gd.h0;
import gd.o0;
import gd.o1;
import gd.p;
import gd.s1;
import gd.u0;
import gd.u1;
import gd.v1;
import gd.w0;
import hd.KotlinTypeChecker;
import id.ErrorType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections.IndexedValue;
import kotlin.collections._Collections;
import kotlin.collections.s;
import ma.NoWhenBranchMatchedException;
import mb.KotlinBuiltIns;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.ClassifierDescriptorWithTypeParameters;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import qb.g;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: TypeUtils.kt */
/* loaded from: classes2.dex */
public final class a {

    /* compiled from: TypeUtils.kt */
    /* renamed from: ld.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    static final class C0073a extends Lambda implements l<v1, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final C0073a f14702e = new C0073a();

        C0073a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(v1 v1Var) {
            k.e(v1Var, "it");
            ClassifierDescriptor v7 = v1Var.W0().v();
            return Boolean.valueOf(v7 != null ? a.q(v7) : false);
        }
    }

    /* compiled from: TypeUtils.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements l<v1, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f14703e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(v1 v1Var) {
            return Boolean.valueOf(s1.m(v1Var));
        }
    }

    /* compiled from: TypeUtils.kt */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements l<v1, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f14704e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(v1 v1Var) {
            k.e(v1Var, "it");
            ClassifierDescriptor v7 = v1Var.W0().v();
            boolean z10 = false;
            if (v7 != null && ((v7 instanceof TypeAliasDescriptor) || (v7 instanceof TypeParameterDescriptor))) {
                z10 = true;
            }
            return Boolean.valueOf(z10);
        }
    }

    public static final TypeProjection a(g0 g0Var) {
        k.e(g0Var, "<this>");
        return new TypeProjectionImpl(g0Var);
    }

    public static final boolean b(g0 g0Var, l<? super v1, Boolean> lVar) {
        k.e(g0Var, "<this>");
        k.e(lVar, "predicate");
        return s1.c(g0Var, lVar);
    }

    private static final boolean c(g0 g0Var, TypeConstructor typeConstructor, Set<? extends TypeParameterDescriptor> set) {
        Iterable<IndexedValue> F0;
        TypeParameterDescriptor typeParameterDescriptor;
        boolean z10;
        Object W;
        if (k.a(g0Var.W0(), typeConstructor)) {
            return true;
        }
        ClassifierDescriptor v7 = g0Var.W0().v();
        ClassifierDescriptorWithTypeParameters classifierDescriptorWithTypeParameters = v7 instanceof ClassifierDescriptorWithTypeParameters ? (ClassifierDescriptorWithTypeParameters) v7 : null;
        List<TypeParameterDescriptor> B = classifierDescriptorWithTypeParameters != null ? classifierDescriptorWithTypeParameters.B() : null;
        F0 = _Collections.F0(g0Var.U0());
        if (!(F0 instanceof Collection) || !((Collection) F0).isEmpty()) {
            for (IndexedValue indexedValue : F0) {
                int a10 = indexedValue.a();
                TypeProjection typeProjection = (TypeProjection) indexedValue.b();
                if (B != null) {
                    W = _Collections.W(B, a10);
                    typeParameterDescriptor = (TypeParameterDescriptor) W;
                } else {
                    typeParameterDescriptor = null;
                }
                if (((typeParameterDescriptor == null || set == null || !set.contains(typeParameterDescriptor)) ? false : true) || typeProjection.b()) {
                    z10 = false;
                } else {
                    g0 type = typeProjection.getType();
                    k.d(type, "argument.type");
                    z10 = c(type, typeConstructor, set);
                }
                if (z10) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final boolean d(g0 g0Var) {
        k.e(g0Var, "<this>");
        return b(g0Var, C0073a.f14702e);
    }

    public static final boolean e(g0 g0Var) {
        k.e(g0Var, "<this>");
        return s1.c(g0Var, b.f14703e);
    }

    public static final TypeProjection f(g0 g0Var, Variance variance, TypeParameterDescriptor typeParameterDescriptor) {
        k.e(g0Var, "type");
        k.e(variance, "projectionKind");
        if ((typeParameterDescriptor != null ? typeParameterDescriptor.s() : null) == variance) {
            variance = Variance.INVARIANT;
        }
        return new TypeProjectionImpl(variance, g0Var);
    }

    public static final Set<TypeParameterDescriptor> g(g0 g0Var, Set<? extends TypeParameterDescriptor> set) {
        k.e(g0Var, "<this>");
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        h(g0Var, g0Var, linkedHashSet, set);
        return linkedHashSet;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final void h(g0 g0Var, g0 g0Var2, Set<TypeParameterDescriptor> set, Set<? extends TypeParameterDescriptor> set2) {
        TypeParameterDescriptor typeParameterDescriptor;
        boolean L;
        Object W;
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (v7 instanceof TypeParameterDescriptor) {
            if (!k.a(g0Var.W0(), g0Var2.W0())) {
                set.add(v7);
                return;
            }
            for (g0 g0Var3 : ((TypeParameterDescriptor) v7).getUpperBounds()) {
                k.d(g0Var3, "upperBound");
                h(g0Var3, g0Var2, set, set2);
            }
            return;
        }
        ClassifierDescriptor v10 = g0Var.W0().v();
        ClassifierDescriptorWithTypeParameters classifierDescriptorWithTypeParameters = v10 instanceof ClassifierDescriptorWithTypeParameters ? (ClassifierDescriptorWithTypeParameters) v10 : null;
        List<TypeParameterDescriptor> B = classifierDescriptorWithTypeParameters != null ? classifierDescriptorWithTypeParameters.B() : null;
        int i10 = 0;
        for (TypeProjection typeProjection : g0Var.U0()) {
            int i11 = i10 + 1;
            if (B != null) {
                W = _Collections.W(B, i10);
                typeParameterDescriptor = (TypeParameterDescriptor) W;
            } else {
                typeParameterDescriptor = null;
            }
            if (!((typeParameterDescriptor == null || set2 == null || !set2.contains(typeParameterDescriptor)) ? false : true) && !typeProjection.b()) {
                L = _Collections.L(set, typeProjection.getType().W0().v());
                if (!L && !k.a(typeProjection.getType().W0(), g0Var2.W0())) {
                    g0 type = typeProjection.getType();
                    k.d(type, "argument.type");
                    h(type, g0Var2, set, set2);
                }
            }
            i10 = i11;
        }
    }

    public static final KotlinBuiltIns i(g0 g0Var) {
        k.e(g0Var, "<this>");
        KotlinBuiltIns t7 = g0Var.W0().t();
        k.d(t7, "constructor.builtIns");
        return t7;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0050, code lost:
    
        r3 = r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final g0 j(TypeParameterDescriptor typeParameterDescriptor) {
        Object obj;
        Object T;
        k.e(typeParameterDescriptor, "<this>");
        List<g0> upperBounds = typeParameterDescriptor.getUpperBounds();
        k.d(upperBounds, "upperBounds");
        upperBounds.isEmpty();
        List<g0> upperBounds2 = typeParameterDescriptor.getUpperBounds();
        k.d(upperBounds2, "upperBounds");
        Iterator<T> it = upperBounds2.iterator();
        while (true) {
            obj = null;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            ClassifierDescriptor v7 = ((g0) next).W0().v();
            ClassDescriptor classDescriptor = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
            boolean z10 = false;
            if (classDescriptor != null && classDescriptor.getKind() != ClassKind.INTERFACE && classDescriptor.getKind() != ClassKind.ANNOTATION_CLASS) {
                z10 = true;
            }
        }
        g0 g0Var = (g0) obj;
        if (g0Var != null) {
            return g0Var;
        }
        List<g0> upperBounds3 = typeParameterDescriptor.getUpperBounds();
        k.d(upperBounds3, "upperBounds");
        T = _Collections.T(upperBounds3);
        k.d(T, "upperBounds.first()");
        return (g0) T;
    }

    public static final boolean k(TypeParameterDescriptor typeParameterDescriptor) {
        k.e(typeParameterDescriptor, "typeParameter");
        return m(typeParameterDescriptor, null, null, 6, null);
    }

    public static final boolean l(TypeParameterDescriptor typeParameterDescriptor, TypeConstructor typeConstructor, Set<? extends TypeParameterDescriptor> set) {
        k.e(typeParameterDescriptor, "typeParameter");
        List<g0> upperBounds = typeParameterDescriptor.getUpperBounds();
        k.d(upperBounds, "typeParameter.upperBounds");
        if (!(upperBounds instanceof Collection) || !upperBounds.isEmpty()) {
            for (g0 g0Var : upperBounds) {
                k.d(g0Var, "upperBound");
                if (c(g0Var, typeParameterDescriptor.x().W0(), set) && (typeConstructor == null || k.a(g0Var.W0(), typeConstructor))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static /* synthetic */ boolean m(TypeParameterDescriptor typeParameterDescriptor, TypeConstructor typeConstructor, Set set, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            typeConstructor = null;
        }
        if ((i10 & 4) != 0) {
            set = null;
        }
        return l(typeParameterDescriptor, typeConstructor, set);
    }

    public static final boolean n(g0 g0Var) {
        k.e(g0Var, "<this>");
        if (!(g0Var instanceof e)) {
            if (!((g0Var instanceof p) && (((p) g0Var).i1() instanceof e))) {
                return false;
            }
        }
        return true;
    }

    public static final boolean o(g0 g0Var) {
        k.e(g0Var, "<this>");
        if (!(g0Var instanceof w0)) {
            if (!((g0Var instanceof p) && (((p) g0Var).i1() instanceof w0))) {
                return false;
            }
        }
        return true;
    }

    public static final boolean p(g0 g0Var, g0 g0Var2) {
        k.e(g0Var, "<this>");
        k.e(g0Var2, "superType");
        return KotlinTypeChecker.f12213a.b(g0Var, g0Var2);
    }

    public static final boolean q(ClassifierDescriptor classifierDescriptor) {
        k.e(classifierDescriptor, "<this>");
        return (classifierDescriptor instanceof TypeParameterDescriptor) && (((TypeParameterDescriptor) classifierDescriptor).b() instanceof TypeAliasDescriptor);
    }

    public static final boolean r(g0 g0Var) {
        k.e(g0Var, "<this>");
        return s1.m(g0Var);
    }

    public static final boolean s(g0 g0Var) {
        k.e(g0Var, "type");
        return (g0Var instanceof ErrorType) && ((ErrorType) g0Var).g1().c();
    }

    public static final g0 t(g0 g0Var) {
        k.e(g0Var, "<this>");
        g0 n10 = s1.n(g0Var);
        k.d(n10, "makeNotNullable(this)");
        return n10;
    }

    public static final g0 u(g0 g0Var) {
        k.e(g0Var, "<this>");
        g0 o10 = s1.o(g0Var);
        k.d(o10, "makeNullable(this)");
        return o10;
    }

    public static final g0 v(g0 g0Var, g gVar) {
        k.e(g0Var, "<this>");
        k.e(gVar, "newAnnotations");
        return (g0Var.i().isEmpty() && gVar.isEmpty()) ? g0Var : g0Var.Z0().c1(d1.a(g0Var.V0(), gVar));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [gd.v1] */
    public static final g0 w(g0 g0Var) {
        int u7;
        o0 o0Var;
        int u10;
        int u11;
        k.e(g0Var, "<this>");
        v1 Z0 = g0Var.Z0();
        if (Z0 instanceof a0) {
            a0 a0Var = (a0) Z0;
            o0 e12 = a0Var.e1();
            if (!e12.W0().getParameters().isEmpty() && e12.W0().v() != null) {
                List<TypeParameterDescriptor> parameters = e12.W0().getParameters();
                k.d(parameters, "constructor.parameters");
                u11 = s.u(parameters, 10);
                ArrayList arrayList = new ArrayList(u11);
                Iterator it = parameters.iterator();
                while (it.hasNext()) {
                    arrayList.add(new u0((TypeParameterDescriptor) it.next()));
                }
                e12 = o1.f(e12, arrayList, null, 2, null);
            }
            o0 f12 = a0Var.f1();
            if (!f12.W0().getParameters().isEmpty() && f12.W0().v() != null) {
                List<TypeParameterDescriptor> parameters2 = f12.W0().getParameters();
                k.d(parameters2, "constructor.parameters");
                u10 = s.u(parameters2, 10);
                ArrayList arrayList2 = new ArrayList(u10);
                Iterator it2 = parameters2.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(new u0((TypeParameterDescriptor) it2.next()));
                }
                f12 = o1.f(f12, arrayList2, null, 2, null);
            }
            o0Var = h0.d(e12, f12);
        } else if (Z0 instanceof o0) {
            o0 o0Var2 = (o0) Z0;
            boolean isEmpty = o0Var2.W0().getParameters().isEmpty();
            o0Var = o0Var2;
            if (!isEmpty) {
                ClassifierDescriptor v7 = o0Var2.W0().v();
                o0Var = o0Var2;
                if (v7 != null) {
                    List<TypeParameterDescriptor> parameters3 = o0Var2.W0().getParameters();
                    k.d(parameters3, "constructor.parameters");
                    u7 = s.u(parameters3, 10);
                    ArrayList arrayList3 = new ArrayList(u7);
                    Iterator it3 = parameters3.iterator();
                    while (it3.hasNext()) {
                        arrayList3.add(new u0((TypeParameterDescriptor) it3.next()));
                    }
                    o0Var = o1.f(o0Var2, arrayList3, null, 2, null);
                }
            }
        } else {
            throw new NoWhenBranchMatchedException();
        }
        return u1.b(o0Var, Z0);
    }

    public static final boolean x(g0 g0Var) {
        k.e(g0Var, "<this>");
        return b(g0Var, c.f14704e);
    }
}
