package gd;

import gd.TypeAliasExpansionReportStrategy;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import pb.ClassifierDescriptor;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import qb.AnnotationDescriptor;
import za.DefaultConstructorMarker;
import zc.h;

/* compiled from: TypeAliasExpander.kt */
/* renamed from: gd.x0, reason: use source file name */
/* loaded from: classes2.dex */
public final class TypeAliasExpander {

    /* renamed from: c, reason: collision with root package name */
    public static final a f11907c = new a(null);

    /* renamed from: d, reason: collision with root package name */
    private static final TypeAliasExpander f11908d = new TypeAliasExpander(TypeAliasExpansionReportStrategy.a.f11919a, false);

    /* renamed from: a, reason: collision with root package name */
    private final TypeAliasExpansionReportStrategy f11909a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f11910b;

    /* compiled from: TypeAliasExpander.kt */
    /* renamed from: gd.x0$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void b(int i10, TypeAliasDescriptor typeAliasDescriptor) {
            if (i10 <= 100) {
                return;
            }
            throw new AssertionError("Too deep recursion while expanding type alias " + typeAliasDescriptor.getName());
        }
    }

    public TypeAliasExpander(TypeAliasExpansionReportStrategy typeAliasExpansionReportStrategy, boolean z10) {
        za.k.e(typeAliasExpansionReportStrategy, "reportStrategy");
        this.f11909a = typeAliasExpansionReportStrategy;
        this.f11910b = z10;
    }

    private final void a(qb.g gVar, qb.g gVar2) {
        HashSet hashSet = new HashSet();
        Iterator<AnnotationDescriptor> it = gVar.iterator();
        while (it.hasNext()) {
            hashSet.add(it.next().d());
        }
        for (AnnotationDescriptor annotationDescriptor : gVar2) {
            if (hashSet.contains(annotationDescriptor.d())) {
                this.f11909a.d(annotationDescriptor);
            }
        }
    }

    private final void b(g0 g0Var, g0 g0Var2) {
        TypeSubstitutor f10 = TypeSubstitutor.f(g0Var2);
        za.k.d(f10, "create(substitutedType)");
        int i10 = 0;
        for (Object obj : g0Var2.U0()) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                kotlin.collections.r.t();
            }
            TypeProjection typeProjection = (TypeProjection) obj;
            if (!typeProjection.b()) {
                g0 type = typeProjection.getType();
                za.k.d(type, "substitutedArgument.type");
                if (!ld.a.d(type)) {
                    TypeProjection typeProjection2 = g0Var.U0().get(i10);
                    TypeParameterDescriptor typeParameterDescriptor = g0Var.W0().getParameters().get(i10);
                    if (this.f11910b) {
                        TypeAliasExpansionReportStrategy typeAliasExpansionReportStrategy = this.f11909a;
                        g0 type2 = typeProjection2.getType();
                        za.k.d(type2, "unsubstitutedArgument.type");
                        g0 type3 = typeProjection.getType();
                        za.k.d(type3, "substitutedArgument.type");
                        za.k.d(typeParameterDescriptor, "typeParameter");
                        typeAliasExpansionReportStrategy.c(f10, type2, type3, typeParameterDescriptor);
                    }
                }
            }
            i10 = i11;
        }
    }

    private final v c(v vVar, c1 c1Var) {
        return vVar.c1(h(vVar, c1Var));
    }

    private final o0 d(o0 o0Var, c1 c1Var) {
        return i0.a(o0Var) ? o0Var : o1.f(o0Var, null, h(o0Var, c1Var), 1, null);
    }

    private final o0 e(o0 o0Var, g0 g0Var) {
        o0 r10 = s1.r(o0Var, g0Var.X0());
        za.k.d(r10, "makeNullableIfNeeded(thi…romType.isMarkedNullable)");
        return r10;
    }

    private final o0 f(o0 o0Var, g0 g0Var) {
        return d(e(o0Var, g0Var), g0Var.V0());
    }

    private final o0 g(TypeAliasExpansion typeAliasExpansion, c1 c1Var, boolean z10) {
        TypeConstructor n10 = typeAliasExpansion.b().n();
        za.k.d(n10, "descriptor.typeConstructor");
        return h0.k(c1Var, n10, typeAliasExpansion.a(), z10, h.b.f20465b);
    }

    private final c1 h(g0 g0Var, c1 c1Var) {
        return i0.a(g0Var) ? g0Var.V0() : c1Var.h(g0Var.V0());
    }

    private final TypeProjection j(TypeProjection typeProjection, TypeAliasExpansion typeAliasExpansion, int i10) {
        TypeProjectionImpl typeProjectionImpl;
        int u7;
        v1 Z0 = typeProjection.getType().Z0();
        if (w.a(Z0)) {
            return typeProjection;
        }
        o0 a10 = o1.a(Z0);
        if (i0.a(a10) || !ld.a.x(a10)) {
            return typeProjection;
        }
        TypeConstructor W0 = a10.W0();
        ClassifierDescriptor v7 = W0.v();
        W0.getParameters().size();
        a10.U0().size();
        if (v7 instanceof TypeParameterDescriptor) {
            return typeProjection;
        }
        if (v7 instanceof TypeAliasDescriptor) {
            TypeAliasDescriptor typeAliasDescriptor = (TypeAliasDescriptor) v7;
            int i11 = 0;
            if (typeAliasExpansion.d(typeAliasDescriptor)) {
                this.f11909a.a(typeAliasDescriptor);
                Variance variance = Variance.INVARIANT;
                ErrorTypeKind errorTypeKind = ErrorTypeKind.f12823w;
                String name = typeAliasDescriptor.getName().toString();
                za.k.d(name, "typeDescriptor.name.toString()");
                return new TypeProjectionImpl(variance, ErrorUtils.d(errorTypeKind, name));
            }
            List<TypeProjection> U0 = a10.U0();
            u7 = kotlin.collections.s.u(U0, 10);
            ArrayList arrayList = new ArrayList(u7);
            for (Object obj : U0) {
                int i12 = i11 + 1;
                if (i11 < 0) {
                    kotlin.collections.r.t();
                }
                arrayList.add(l((TypeProjection) obj, typeAliasExpansion, W0.getParameters().get(i11), i10 + 1));
                i11 = i12;
            }
            o0 k10 = k(TypeAliasExpansion.f11914e.a(typeAliasExpansion, typeAliasDescriptor, arrayList), a10.V0(), a10.X0(), i10 + 1, false);
            o0 m10 = m(a10, typeAliasExpansion, i10);
            if (!w.a(k10)) {
                k10 = s0.j(k10, m10);
            }
            typeProjectionImpl = new TypeProjectionImpl(typeProjection.a(), k10);
        } else {
            o0 m11 = m(a10, typeAliasExpansion, i10);
            b(a10, m11);
            typeProjectionImpl = new TypeProjectionImpl(typeProjection.a(), m11);
        }
        return typeProjectionImpl;
    }

    private final o0 k(TypeAliasExpansion typeAliasExpansion, c1 c1Var, boolean z10, int i10, boolean z11) {
        TypeProjection l10 = l(new TypeProjectionImpl(Variance.INVARIANT, typeAliasExpansion.b().n0()), typeAliasExpansion, null, i10);
        g0 type = l10.getType();
        za.k.d(type, "expandedProjection.type");
        o0 a10 = o1.a(type);
        if (i0.a(a10)) {
            return a10;
        }
        l10.a();
        a(a10.i(), k.a(c1Var));
        o0 r10 = s1.r(d(a10, c1Var), z10);
        za.k.d(r10, "expandedType.combineAttr…fNeeded(it, isNullable) }");
        return z11 ? s0.j(r10, g(typeAliasExpansion, c1Var, z10)) : r10;
    }

    private final TypeProjection l(TypeProjection typeProjection, TypeAliasExpansion typeAliasExpansion, TypeParameterDescriptor typeParameterDescriptor, int i10) {
        Variance variance;
        g0 f10;
        Variance variance2;
        Variance variance3;
        f11907c.b(i10, typeAliasExpansion.b());
        if (typeProjection.b()) {
            za.k.b(typeParameterDescriptor);
            TypeProjection s7 = s1.s(typeParameterDescriptor);
            za.k.d(s7, "makeStarProjection(typeParameterDescriptor!!)");
            return s7;
        }
        g0 type = typeProjection.getType();
        za.k.d(type, "underlyingProjection.type");
        TypeProjection c10 = typeAliasExpansion.c(type.W0());
        if (c10 == null) {
            return j(typeProjection, typeAliasExpansion, i10);
        }
        if (c10.b()) {
            za.k.b(typeParameterDescriptor);
            TypeProjection s10 = s1.s(typeParameterDescriptor);
            za.k.d(s10, "makeStarProjection(typeParameterDescriptor!!)");
            return s10;
        }
        v1 Z0 = c10.getType().Z0();
        Variance a10 = c10.a();
        za.k.d(a10, "argument.projectionKind");
        Variance a11 = typeProjection.a();
        za.k.d(a11, "underlyingProjection.projectionKind");
        if (a11 != a10 && a11 != (variance3 = Variance.INVARIANT)) {
            if (a10 == variance3) {
                a10 = a11;
            } else {
                this.f11909a.b(typeAliasExpansion.b(), typeParameterDescriptor, Z0);
            }
        }
        if (typeParameterDescriptor == null || (variance = typeParameterDescriptor.s()) == null) {
            variance = Variance.INVARIANT;
        }
        za.k.d(variance, "typeParameterDescriptor?…nce ?: Variance.INVARIANT");
        if (variance != a10 && variance != (variance2 = Variance.INVARIANT)) {
            if (a10 == variance2) {
                a10 = variance2;
            } else {
                this.f11909a.b(typeAliasExpansion.b(), typeParameterDescriptor, Z0);
            }
        }
        a(type.i(), Z0.i());
        if (Z0 instanceof v) {
            f10 = c((v) Z0, type.V0());
        } else {
            f10 = f(o1.a(Z0), type);
        }
        return new TypeProjectionImpl(a10, f10);
    }

    private final o0 m(o0 o0Var, TypeAliasExpansion typeAliasExpansion, int i10) {
        int u7;
        TypeConstructor W0 = o0Var.W0();
        List<TypeProjection> U0 = o0Var.U0();
        u7 = kotlin.collections.s.u(U0, 10);
        ArrayList arrayList = new ArrayList(u7);
        int i11 = 0;
        for (Object obj : U0) {
            int i12 = i11 + 1;
            if (i11 < 0) {
                kotlin.collections.r.t();
            }
            TypeProjection typeProjection = (TypeProjection) obj;
            TypeProjection l10 = l(typeProjection, typeAliasExpansion, W0.getParameters().get(i11), i10 + 1);
            if (!l10.b()) {
                l10 = new TypeProjectionImpl(l10.a(), s1.q(l10.getType(), typeProjection.getType().X0()));
            }
            arrayList.add(l10);
            i11 = i12;
        }
        return o1.f(o0Var, arrayList, null, 2, null);
    }

    public final o0 i(TypeAliasExpansion typeAliasExpansion, c1 c1Var) {
        za.k.e(typeAliasExpansion, "typeAliasExpansion");
        za.k.e(c1Var, "attributes");
        return k(typeAliasExpansion, c1Var, false, 0, true);
    }
}
