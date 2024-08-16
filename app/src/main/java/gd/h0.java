package gd;

import gd.TypeAliasExpansionReportStrategy;
import id.ErrorScopeKind;
import id.ErrorUtils;
import java.util.List;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import uc.IntegerLiteralTypeConstructor;
import za.Lambda;

/* compiled from: KotlinTypeFactory.kt */
/* loaded from: classes2.dex */
public final class h0 {

    /* renamed from: a, reason: collision with root package name */
    public static final h0 f11813a = new h0();

    /* renamed from: b, reason: collision with root package name */
    private static final ya.l<hd.g, o0> f11814b = a.f11815e;

    /* compiled from: KotlinTypeFactory.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l {

        /* renamed from: e, reason: collision with root package name */
        public static final a f11815e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Void invoke(hd.g gVar) {
            za.k.e(gVar, "<anonymous parameter 0>");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: KotlinTypeFactory.kt */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final o0 f11816a;

        /* renamed from: b, reason: collision with root package name */
        private final TypeConstructor f11817b;

        public b(o0 o0Var, TypeConstructor typeConstructor) {
            this.f11816a = o0Var;
            this.f11817b = typeConstructor;
        }

        public final o0 a() {
            return this.f11816a;
        }

        public final TypeConstructor b() {
            return this.f11817b;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KotlinTypeFactory.kt */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<hd.g, o0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ TypeConstructor f11818e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ List<TypeProjection> f11819f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ c1 f11820g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ boolean f11821h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        c(TypeConstructor typeConstructor, List<? extends TypeProjection> list, c1 c1Var, boolean z10) {
            super(1);
            this.f11818e = typeConstructor;
            this.f11819f = list;
            this.f11820g = c1Var;
            this.f11821h = z10;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final o0 invoke(hd.g gVar) {
            za.k.e(gVar, "refiner");
            b f10 = h0.f11813a.f(this.f11818e, gVar, this.f11819f);
            if (f10 == null) {
                return null;
            }
            o0 a10 = f10.a();
            if (a10 != null) {
                return a10;
            }
            c1 c1Var = this.f11820g;
            TypeConstructor b10 = f10.b();
            za.k.b(b10);
            return h0.i(c1Var, b10, this.f11819f, this.f11821h, gVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KotlinTypeFactory.kt */
    /* loaded from: classes2.dex */
    public static final class d extends Lambda implements ya.l<hd.g, o0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ TypeConstructor f11822e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ List<TypeProjection> f11823f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ c1 f11824g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ boolean f11825h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ zc.h f11826i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        d(TypeConstructor typeConstructor, List<? extends TypeProjection> list, c1 c1Var, boolean z10, zc.h hVar) {
            super(1);
            this.f11822e = typeConstructor;
            this.f11823f = list;
            this.f11824g = c1Var;
            this.f11825h = z10;
            this.f11826i = hVar;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final o0 invoke(hd.g gVar) {
            za.k.e(gVar, "kotlinTypeRefiner");
            b f10 = h0.f11813a.f(this.f11822e, gVar, this.f11823f);
            if (f10 == null) {
                return null;
            }
            o0 a10 = f10.a();
            if (a10 != null) {
                return a10;
            }
            c1 c1Var = this.f11824g;
            TypeConstructor b10 = f10.b();
            za.k.b(b10);
            return h0.k(c1Var, b10, this.f11823f, this.f11825h, this.f11826i);
        }
    }

    private h0() {
    }

    public static final o0 b(TypeAliasDescriptor typeAliasDescriptor, List<? extends TypeProjection> list) {
        za.k.e(typeAliasDescriptor, "<this>");
        za.k.e(list, "arguments");
        return new TypeAliasExpander(TypeAliasExpansionReportStrategy.a.f11919a, false).i(TypeAliasExpansion.f11914e.a(null, typeAliasDescriptor, list), c1.f11749f.h());
    }

    private final zc.h c(TypeConstructor typeConstructor, List<? extends TypeProjection> list, hd.g gVar) {
        ClassifierDescriptor v7 = typeConstructor.v();
        if (v7 instanceof TypeParameterDescriptor) {
            return ((TypeParameterDescriptor) v7).x().u();
        }
        if (v7 instanceof ClassDescriptor) {
            if (gVar == null) {
                gVar = wc.c.o(wc.c.p(v7));
            }
            if (list.isEmpty()) {
                return sb.u.b((ClassDescriptor) v7, gVar);
            }
            return sb.u.a((ClassDescriptor) v7, h1.f11827c.b(typeConstructor, list), gVar);
        }
        if (v7 instanceof TypeAliasDescriptor) {
            ErrorScopeKind errorScopeKind = ErrorScopeKind.SCOPE_FOR_ABBREVIATION_TYPE;
            String name = ((TypeAliasDescriptor) v7).getName().toString();
            za.k.d(name, "descriptor.name.toString()");
            return ErrorUtils.a(errorScopeKind, true, name);
        }
        if (typeConstructor instanceof IntersectionTypeConstructor) {
            return ((IntersectionTypeConstructor) typeConstructor).c();
        }
        throw new IllegalStateException("Unsupported classifier: " + v7 + " for constructor: " + typeConstructor);
    }

    public static final v1 d(o0 o0Var, o0 o0Var2) {
        za.k.e(o0Var, "lowerBound");
        za.k.e(o0Var2, "upperBound");
        return za.k.a(o0Var, o0Var2) ? o0Var : new b0(o0Var, o0Var2);
    }

    public static final o0 e(c1 c1Var, IntegerLiteralTypeConstructor integerLiteralTypeConstructor, boolean z10) {
        List j10;
        za.k.e(c1Var, "attributes");
        za.k.e(integerLiteralTypeConstructor, "constructor");
        j10 = kotlin.collections.r.j();
        return k(c1Var, integerLiteralTypeConstructor, j10, z10, ErrorUtils.a(ErrorScopeKind.INTEGER_LITERAL_TYPE_SCOPE, true, "unknown integer literal type"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final b f(TypeConstructor typeConstructor, hd.g gVar, List<? extends TypeProjection> list) {
        ClassifierDescriptor f10;
        ClassifierDescriptor v7 = typeConstructor.v();
        if (v7 == null || (f10 = gVar.f(v7)) == null) {
            return null;
        }
        if (f10 instanceof TypeAliasDescriptor) {
            return new b(b((TypeAliasDescriptor) f10, list), null);
        }
        TypeConstructor u7 = f10.n().u(gVar);
        za.k.d(u7, "descriptor.typeConstruct…refine(kotlinTypeRefiner)");
        return new b(null, u7);
    }

    public static final o0 g(c1 c1Var, ClassDescriptor classDescriptor, List<? extends TypeProjection> list) {
        za.k.e(c1Var, "attributes");
        za.k.e(classDescriptor, "descriptor");
        za.k.e(list, "arguments");
        TypeConstructor n10 = classDescriptor.n();
        za.k.d(n10, "descriptor.typeConstructor");
        return j(c1Var, n10, list, false, null, 16, null);
    }

    public static final o0 h(c1 c1Var, TypeConstructor typeConstructor, List<? extends TypeProjection> list, boolean z10) {
        za.k.e(c1Var, "attributes");
        za.k.e(typeConstructor, "constructor");
        za.k.e(list, "arguments");
        return j(c1Var, typeConstructor, list, z10, null, 16, null);
    }

    public static final o0 i(c1 c1Var, TypeConstructor typeConstructor, List<? extends TypeProjection> list, boolean z10, hd.g gVar) {
        za.k.e(c1Var, "attributes");
        za.k.e(typeConstructor, "constructor");
        za.k.e(list, "arguments");
        if (c1Var.isEmpty() && list.isEmpty() && !z10 && typeConstructor.v() != null) {
            ClassifierDescriptor v7 = typeConstructor.v();
            za.k.b(v7);
            o0 x10 = v7.x();
            za.k.d(x10, "constructor.declarationDescriptor!!.defaultType");
            return x10;
        }
        return l(c1Var, typeConstructor, list, z10, f11813a.c(typeConstructor, list, gVar), new c(typeConstructor, list, c1Var, z10));
    }

    public static /* synthetic */ o0 j(c1 c1Var, TypeConstructor typeConstructor, List list, boolean z10, hd.g gVar, int i10, Object obj) {
        if ((i10 & 16) != 0) {
            gVar = null;
        }
        return i(c1Var, typeConstructor, list, z10, gVar);
    }

    public static final o0 k(c1 c1Var, TypeConstructor typeConstructor, List<? extends TypeProjection> list, boolean z10, zc.h hVar) {
        za.k.e(c1Var, "attributes");
        za.k.e(typeConstructor, "constructor");
        za.k.e(list, "arguments");
        za.k.e(hVar, "memberScope");
        p0 p0Var = new p0(typeConstructor, list, z10, hVar, new d(typeConstructor, list, c1Var, z10, hVar));
        return c1Var.isEmpty() ? p0Var : new q0(p0Var, c1Var);
    }

    public static final o0 l(c1 c1Var, TypeConstructor typeConstructor, List<? extends TypeProjection> list, boolean z10, zc.h hVar, ya.l<? super hd.g, ? extends o0> lVar) {
        za.k.e(c1Var, "attributes");
        za.k.e(typeConstructor, "constructor");
        za.k.e(list, "arguments");
        za.k.e(hVar, "memberScope");
        za.k.e(lVar, "refinedTypeFactory");
        p0 p0Var = new p0(typeConstructor, list, z10, hVar, lVar);
        return c1Var.isEmpty() ? p0Var : new q0(p0Var, c1Var);
    }
}
