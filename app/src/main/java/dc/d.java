package dc;

import bc.k;
import fc.a0;
import fc.c0;
import fc.i;
import fc.j;
import fc.v;
import fc.x;
import fc.y;
import gd.TypeConstructor;
import gd.TypeParameterUpperBoundEraser;
import gd.TypeProjection;
import gd.TypeProjectionImpl;
import gd.TypeUsage;
import gd.Variance;
import gd.c1;
import gd.d1;
import gd.g0;
import gd.h0;
import gd.j0;
import gd.o0;
import gd.s1;
import id.ErrorType;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.IndexedValue;
import kotlin.collections._Collections;
import kotlin.collections.s;
import mb.PrimitiveType;
import ob.JavaToKotlinClassMapper;
import oc.ClassId;
import oc.FqName;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.NotFoundClasses;
import pb.TypeParameterDescriptor;
import qb.AnnotationDescriptor;
import qb.g;
import za.Lambda;

/* compiled from: JavaTypeResolver.kt */
/* loaded from: classes2.dex */
public final class d {

    /* renamed from: a, reason: collision with root package name */
    private final bc.g f10905a;

    /* renamed from: b, reason: collision with root package name */
    private final k f10906b;

    /* renamed from: c, reason: collision with root package name */
    private final RawProjectionComputer f10907c;

    /* renamed from: d, reason: collision with root package name */
    private final TypeParameterUpperBoundEraser f10908d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: JavaTypeResolver.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.a<g0> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ TypeParameterDescriptor f10910f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ dc.a f10911g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ TypeConstructor f10912h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ j f10913i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(TypeParameterDescriptor typeParameterDescriptor, dc.a aVar, TypeConstructor typeConstructor, j jVar) {
            super(0);
            this.f10910f = typeParameterDescriptor;
            this.f10911g = aVar;
            this.f10912h = typeConstructor;
            this.f10913i = jVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke() {
            TypeParameterUpperBoundEraser typeParameterUpperBoundEraser = d.this.f10908d;
            TypeParameterDescriptor typeParameterDescriptor = this.f10910f;
            dc.a aVar = this.f10911g;
            ClassifierDescriptor v7 = this.f10912h.v();
            return typeParameterUpperBoundEraser.c(typeParameterDescriptor, aVar.k(v7 != null ? v7.x() : null).j(this.f10913i.E()));
        }
    }

    public d(bc.g gVar, k kVar) {
        za.k.e(gVar, "c");
        za.k.e(kVar, "typeParameterResolver");
        this.f10905a = gVar;
        this.f10906b = kVar;
        RawProjectionComputer rawProjectionComputer = new RawProjectionComputer();
        this.f10907c = rawProjectionComputer;
        this.f10908d = new TypeParameterUpperBoundEraser(rawProjectionComputer, null, 2, 0 == true ? 1 : 0);
    }

    private final boolean b(j jVar, ClassDescriptor classDescriptor) {
        Object g02;
        Object g03;
        Variance s7;
        g02 = _Collections.g0(jVar.L());
        if (!a0.a((x) g02)) {
            return false;
        }
        List<TypeParameterDescriptor> parameters = JavaToKotlinClassMapper.f16359a.b(classDescriptor).n().getParameters();
        za.k.d(parameters, "JavaToKotlinClassMapper.…ypeConstructor.parameters");
        g03 = _Collections.g0(parameters);
        TypeParameterDescriptor typeParameterDescriptor = (TypeParameterDescriptor) g03;
        return (typeParameterDescriptor == null || (s7 = typeParameterDescriptor.s()) == null || s7 == Variance.OUT_VARIANCE) ? false : true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0020, code lost:
    
        if ((!r0.isEmpty()) != false) goto L10;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0034  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final List<TypeProjection> c(j jVar, dc.a aVar, TypeConstructor typeConstructor) {
        boolean z10;
        Iterable<IndexedValue> F0;
        int u7;
        List<TypeProjection> z02;
        int u10;
        List<TypeProjection> z03;
        if (!jVar.E()) {
            if (jVar.L().isEmpty()) {
                za.k.d(typeConstructor.getParameters(), "constructor.parameters");
            }
            z10 = false;
            List<TypeParameterDescriptor> parameters = typeConstructor.getParameters();
            za.k.d(parameters, "constructor.parameters");
            if (!z10) {
                return d(jVar, parameters, typeConstructor, aVar);
            }
            if (parameters.size() != jVar.L().size()) {
                u10 = s.u(parameters, 10);
                ArrayList arrayList = new ArrayList(u10);
                for (TypeParameterDescriptor typeParameterDescriptor : parameters) {
                    ErrorTypeKind errorTypeKind = ErrorTypeKind.f12785a0;
                    String b10 = typeParameterDescriptor.getName().b();
                    za.k.d(b10, "p.name.asString()");
                    arrayList.add(new TypeProjectionImpl(ErrorUtils.d(errorTypeKind, b10)));
                }
                z03 = _Collections.z0(arrayList);
                return z03;
            }
            F0 = _Collections.F0(jVar.L());
            u7 = s.u(F0, 10);
            ArrayList arrayList2 = new ArrayList(u7);
            for (IndexedValue indexedValue : F0) {
                int a10 = indexedValue.a();
                x xVar = (x) indexedValue.b();
                parameters.size();
                TypeParameterDescriptor typeParameterDescriptor2 = parameters.get(a10);
                dc.a b11 = b.b(TypeUsage.COMMON, false, false, null, 7, null);
                za.k.d(typeParameterDescriptor2, "parameter");
                arrayList2.add(p(xVar, b11, typeParameterDescriptor2));
            }
            z02 = _Collections.z0(arrayList2);
            return z02;
        }
        z10 = true;
        List<TypeParameterDescriptor> parameters2 = typeConstructor.getParameters();
        za.k.d(parameters2, "constructor.parameters");
        if (!z10) {
        }
    }

    private final List<TypeProjection> d(j jVar, List<? extends TypeParameterDescriptor> list, TypeConstructor typeConstructor, dc.a aVar) {
        int u7;
        TypeProjection a10;
        u7 = s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (TypeParameterDescriptor typeParameterDescriptor : list) {
            if (ld.a.l(typeParameterDescriptor, null, aVar.c())) {
                a10 = s1.t(typeParameterDescriptor, aVar);
            } else {
                a10 = this.f10907c.a(typeParameterDescriptor, aVar.j(jVar.E()), this.f10908d, new j0(this.f10905a.e(), new a(typeParameterDescriptor, aVar, typeConstructor, jVar)));
            }
            arrayList.add(a10);
        }
        return arrayList;
    }

    private final o0 e(j jVar, dc.a aVar, o0 o0Var) {
        c1 b10;
        if (o0Var == null || (b10 = o0Var.V0()) == null) {
            b10 = d1.b(new bc.d(this.f10905a, jVar, false, 4, null));
        }
        c1 c1Var = b10;
        TypeConstructor f10 = f(jVar, aVar);
        if (f10 == null) {
            return null;
        }
        boolean i10 = i(aVar);
        if (za.k.a(o0Var != null ? o0Var.W0() : null, f10) && !jVar.E() && i10) {
            return o0Var.a1(true);
        }
        return h0.j(c1Var, f10, c(jVar, aVar, f10), i10, null, 16, null);
    }

    private final TypeConstructor f(j jVar, dc.a aVar) {
        TypeConstructor n10;
        i c10 = jVar.c();
        if (c10 == null) {
            return g(jVar);
        }
        if (c10 instanceof fc.g) {
            fc.g gVar = (fc.g) c10;
            FqName d10 = gVar.d();
            if (d10 != null) {
                ClassDescriptor j10 = j(jVar, aVar, d10);
                if (j10 == null) {
                    j10 = this.f10905a.a().n().a(gVar);
                }
                return (j10 == null || (n10 = j10.n()) == null) ? g(jVar) : n10;
            }
            throw new AssertionError("Class type should have a FQ name: " + c10);
        }
        if (c10 instanceof y) {
            TypeParameterDescriptor a10 = this.f10906b.a((y) c10);
            if (a10 != null) {
                return a10.n();
            }
            return null;
        }
        throw new IllegalStateException("Unknown classifier kind: " + c10);
    }

    private final TypeConstructor g(j jVar) {
        List<Integer> e10;
        ClassId m10 = ClassId.m(new FqName(jVar.F()));
        za.k.d(m10, "topLevel(FqName(javaType.classifierQualifiedName))");
        NotFoundClasses q10 = this.f10905a.a().b().d().q();
        e10 = CollectionsJVM.e(0);
        TypeConstructor n10 = q10.d(m10, e10).n();
        za.k.d(n10, "c.components.deserialize…istOf(0)).typeConstructor");
        return n10;
    }

    private final boolean h(Variance variance, TypeParameterDescriptor typeParameterDescriptor) {
        return (typeParameterDescriptor.s() == Variance.INVARIANT || variance == typeParameterDescriptor.s()) ? false : true;
    }

    private final boolean i(dc.a aVar) {
        return (aVar.g() == JavaTypeFlexibility.FLEXIBLE_LOWER_BOUND || aVar.h() || aVar.b() == TypeUsage.SUPERTYPE) ? false : true;
    }

    private final ClassDescriptor j(j jVar, dc.a aVar, FqName fqName) {
        FqName fqName2;
        if (aVar.h()) {
            fqName2 = e.f10914a;
            if (za.k.a(fqName, fqName2)) {
                return this.f10905a.a().p().c();
            }
        }
        JavaToKotlinClassMapper javaToKotlinClassMapper = JavaToKotlinClassMapper.f16359a;
        ClassDescriptor f10 = JavaToKotlinClassMapper.f(javaToKotlinClassMapper, fqName, this.f10905a.d().t(), null, 4, null);
        if (f10 == null) {
            return null;
        }
        return (javaToKotlinClassMapper.d(f10) && (aVar.g() == JavaTypeFlexibility.FLEXIBLE_LOWER_BOUND || aVar.b() == TypeUsage.SUPERTYPE || b(jVar, f10))) ? javaToKotlinClassMapper.b(f10) : f10;
    }

    public static /* synthetic */ g0 l(d dVar, fc.f fVar, dc.a aVar, boolean z10, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            z10 = false;
        }
        return dVar.k(fVar, aVar, z10);
    }

    private final g0 m(j jVar, dc.a aVar) {
        boolean z10 = (aVar.h() || aVar.b() == TypeUsage.SUPERTYPE) ? false : true;
        boolean E = jVar.E();
        if (!E && !z10) {
            o0 e10 = e(jVar, aVar, null);
            return e10 != null ? e10 : n(jVar);
        }
        o0 e11 = e(jVar, aVar.l(JavaTypeFlexibility.FLEXIBLE_LOWER_BOUND), null);
        if (e11 == null) {
            return n(jVar);
        }
        o0 e12 = e(jVar, aVar.l(JavaTypeFlexibility.FLEXIBLE_UPPER_BOUND), e11);
        if (e12 == null) {
            return n(jVar);
        }
        if (E) {
            return new h(e11, e12);
        }
        return h0.d(e11, e12);
    }

    private static final ErrorType n(j jVar) {
        return ErrorUtils.d(ErrorTypeKind.f12797j, jVar.x());
    }

    private final TypeProjection p(x xVar, dc.a aVar, TypeParameterDescriptor typeParameterDescriptor) {
        TypeProjection t7;
        List<? extends AnnotationDescriptor> l02;
        if (xVar instanceof c0) {
            c0 c0Var = (c0) xVar;
            x J = c0Var.J();
            Variance variance = c0Var.P() ? Variance.OUT_VARIANCE : Variance.IN_VARIANCE;
            if (J != null && !h(variance, typeParameterDescriptor)) {
                AnnotationDescriptor a10 = yb.j0.a(this.f10905a, c0Var);
                g0 o10 = o(J, b.b(TypeUsage.COMMON, false, false, null, 7, null));
                if (a10 != null) {
                    g.a aVar2 = qb.g.f17195b;
                    l02 = _Collections.l0(o10.i(), a10);
                    o10 = ld.a.v(o10, aVar2.a(l02));
                }
                t7 = ld.a.f(o10, variance, typeParameterDescriptor);
            } else {
                t7 = s1.t(typeParameterDescriptor, aVar);
            }
            za.k.d(t7, "{\n                val bo…          }\n            }");
            return t7;
        }
        return new TypeProjectionImpl(Variance.INVARIANT, o(xVar, aVar));
    }

    public final g0 k(fc.f fVar, dc.a aVar, boolean z10) {
        List<? extends AnnotationDescriptor> k02;
        za.k.e(fVar, "arrayType");
        za.k.e(aVar, "attr");
        x r10 = fVar.r();
        v vVar = r10 instanceof v ? (v) r10 : null;
        PrimitiveType type = vVar != null ? vVar.getType() : null;
        bc.d dVar = new bc.d(this.f10905a, fVar, true);
        if (type != null) {
            o0 O = this.f10905a.d().t().O(type);
            za.k.d(O, "c.module.builtIns.getPri…KotlinType(primitiveType)");
            g.a aVar2 = qb.g.f17195b;
            k02 = _Collections.k0(dVar, O.i());
            ld.a.v(O, aVar2.a(k02));
            return aVar.h() ? O : h0.d(O, O.a1(true));
        }
        g0 o10 = o(r10, b.b(TypeUsage.COMMON, aVar.h(), false, null, 6, null));
        if (aVar.h()) {
            o0 m10 = this.f10905a.d().t().m(z10 ? Variance.OUT_VARIANCE : Variance.INVARIANT, o10, dVar);
            za.k.d(m10, "c.module.builtIns.getArr…mponentType, annotations)");
            return m10;
        }
        o0 m11 = this.f10905a.d().t().m(Variance.INVARIANT, o10, dVar);
        za.k.d(m11, "c.module.builtIns.getArr…mponentType, annotations)");
        return h0.d(m11, this.f10905a.d().t().m(Variance.OUT_VARIANCE, o10, dVar).a1(true));
    }

    public final g0 o(x xVar, dc.a aVar) {
        g0 o10;
        o0 Z;
        za.k.e(aVar, "attr");
        if (xVar instanceof v) {
            PrimitiveType type = ((v) xVar).getType();
            if (type != null) {
                Z = this.f10905a.d().t().R(type);
            } else {
                Z = this.f10905a.d().t().Z();
            }
            za.k.d(Z, "{\n                val pr…ns.unitType\n            }");
            return Z;
        }
        if (xVar instanceof j) {
            return m((j) xVar, aVar);
        }
        if (xVar instanceof fc.f) {
            return l(this, (fc.f) xVar, aVar, false, 4, null);
        }
        if (xVar instanceof c0) {
            x J = ((c0) xVar).J();
            if (J != null && (o10 = o(J, aVar)) != null) {
                return o10;
            }
            o0 y4 = this.f10905a.d().t().y();
            za.k.d(y4, "c.module.builtIns.defaultBound");
            return y4;
        }
        if (xVar == null) {
            o0 y10 = this.f10905a.d().t().y();
            za.k.d(y10, "c.module.builtIns.defaultBound");
            return y10;
        }
        throw new UnsupportedOperationException("Unsupported type: " + xVar);
    }
}
