package gd;

import fb._Ranges;
import fd.LockBasedStorageManager;
import gd.h1;
import id.ErrorType;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import kotlin.collections.MapsJVM;
import kotlin.collections.SetsJVM;
import kotlin.collections._Collections;
import ma.NoWhenBranchMatchedException;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: TypeParameterUpperBoundEraser.kt */
/* renamed from: gd.j1, reason: use source file name */
/* loaded from: classes2.dex */
public final class TypeParameterUpperBoundEraser {

    /* renamed from: f, reason: collision with root package name */
    public static final a f11838f = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final ErasureProjectionComputer f11839a;

    /* renamed from: b, reason: collision with root package name */
    private final TypeParameterErasureOptions f11840b;

    /* renamed from: c, reason: collision with root package name */
    private final LockBasedStorageManager f11841c;

    /* renamed from: d, reason: collision with root package name */
    private final ma.h f11842d;

    /* renamed from: e, reason: collision with root package name */
    private final fd.g<b, g0> f11843e;

    /* compiled from: TypeParameterUpperBoundEraser.kt */
    /* renamed from: gd.j1$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x00b3 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:52:0x014e A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:91:0x01f0 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final g0 a(g0 g0Var, TypeSubstitutor typeSubstitutor, Set<? extends TypeParameterDescriptor> set, boolean z10) {
            v1 v1Var;
            int u7;
            Object W;
            boolean z11;
            g0 type;
            int u10;
            Object W2;
            boolean z12;
            g0 type2;
            int u11;
            Object W3;
            boolean z13;
            g0 type3;
            za.k.e(g0Var, "<this>");
            za.k.e(typeSubstitutor, "substitutor");
            v1 Z0 = g0Var.Z0();
            if (Z0 instanceof a0) {
                a0 a0Var = (a0) Z0;
                o0 e12 = a0Var.e1();
                if (!e12.W0().getParameters().isEmpty() && e12.W0().v() != null) {
                    List<TypeParameterDescriptor> parameters = e12.W0().getParameters();
                    za.k.d(parameters, "constructor.parameters");
                    u11 = kotlin.collections.s.u(parameters, 10);
                    ArrayList arrayList = new ArrayList(u11);
                    for (TypeParameterDescriptor typeParameterDescriptor : parameters) {
                        W3 = _Collections.W(g0Var.U0(), typeParameterDescriptor.j());
                        TypeProjection typeProjection = (TypeProjection) W3;
                        if (z10) {
                            if (typeProjection != null && (type3 = typeProjection.getType()) != null) {
                                za.k.d(type3, "type");
                                if (!ld.a.e(type3)) {
                                    z13 = true;
                                    if (z13) {
                                        arrayList.add(typeProjection);
                                    }
                                }
                            }
                            z13 = false;
                            if (z13) {
                            }
                        }
                        boolean z14 = set != null && set.contains(typeParameterDescriptor);
                        if (typeProjection != null && !z14) {
                            n1 j10 = typeSubstitutor.j();
                            g0 type4 = typeProjection.getType();
                            za.k.d(type4, "argument.type");
                            if (j10.e(type4) != null) {
                                arrayList.add(typeProjection);
                            }
                        }
                        typeProjection = new u0(typeParameterDescriptor);
                        arrayList.add(typeProjection);
                    }
                    e12 = o1.f(e12, arrayList, null, 2, null);
                }
                o0 f12 = a0Var.f1();
                if (!f12.W0().getParameters().isEmpty() && f12.W0().v() != null) {
                    List<TypeParameterDescriptor> parameters2 = f12.W0().getParameters();
                    za.k.d(parameters2, "constructor.parameters");
                    u10 = kotlin.collections.s.u(parameters2, 10);
                    ArrayList arrayList2 = new ArrayList(u10);
                    for (TypeParameterDescriptor typeParameterDescriptor2 : parameters2) {
                        W2 = _Collections.W(g0Var.U0(), typeParameterDescriptor2.j());
                        TypeProjection typeProjection2 = (TypeProjection) W2;
                        if (z10) {
                            if (typeProjection2 != null && (type2 = typeProjection2.getType()) != null) {
                                za.k.d(type2, "type");
                                if (!ld.a.e(type2)) {
                                    z12 = true;
                                    if (z12) {
                                        arrayList2.add(typeProjection2);
                                    }
                                }
                            }
                            z12 = false;
                            if (z12) {
                            }
                        }
                        boolean z15 = set != null && set.contains(typeParameterDescriptor2);
                        if (typeProjection2 != null && !z15) {
                            n1 j11 = typeSubstitutor.j();
                            g0 type5 = typeProjection2.getType();
                            za.k.d(type5, "argument.type");
                            if (j11.e(type5) != null) {
                                arrayList2.add(typeProjection2);
                            }
                        }
                        typeProjection2 = new u0(typeParameterDescriptor2);
                        arrayList2.add(typeProjection2);
                    }
                    f12 = o1.f(f12, arrayList2, null, 2, null);
                }
                v1Var = h0.d(e12, f12);
            } else if (Z0 instanceof o0) {
                o0 o0Var = (o0) Z0;
                if (o0Var.W0().getParameters().isEmpty() || o0Var.W0().v() == null) {
                    v1Var = o0Var;
                } else {
                    List<TypeParameterDescriptor> parameters3 = o0Var.W0().getParameters();
                    za.k.d(parameters3, "constructor.parameters");
                    u7 = kotlin.collections.s.u(parameters3, 10);
                    ArrayList arrayList3 = new ArrayList(u7);
                    for (TypeParameterDescriptor typeParameterDescriptor3 : parameters3) {
                        W = _Collections.W(g0Var.U0(), typeParameterDescriptor3.j());
                        TypeProjection typeProjection3 = (TypeProjection) W;
                        if (z10) {
                            if (typeProjection3 != null && (type = typeProjection3.getType()) != null) {
                                za.k.d(type, "type");
                                if (!ld.a.e(type)) {
                                    z11 = true;
                                    if (z11) {
                                        arrayList3.add(typeProjection3);
                                    }
                                }
                            }
                            z11 = false;
                            if (z11) {
                            }
                        }
                        boolean z16 = set != null && set.contains(typeParameterDescriptor3);
                        if (typeProjection3 != null && !z16) {
                            n1 j12 = typeSubstitutor.j();
                            g0 type6 = typeProjection3.getType();
                            za.k.d(type6, "argument.type");
                            if (j12.e(type6) != null) {
                                arrayList3.add(typeProjection3);
                            }
                        }
                        typeProjection3 = new u0(typeParameterDescriptor3);
                        arrayList3.add(typeProjection3);
                    }
                    v1Var = o1.f(o0Var, arrayList3, null, 2, null);
                }
            } else {
                throw new NoWhenBranchMatchedException();
            }
            g0 n10 = typeSubstitutor.n(u1.b(v1Var, Z0), Variance.OUT_VARIANCE);
            za.k.d(n10, "substitutor.safeSubstitu…s, Variance.OUT_VARIANCE)");
            return n10;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TypeParameterUpperBoundEraser.kt */
    /* renamed from: gd.j1$b */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final TypeParameterDescriptor f11844a;

        /* renamed from: b, reason: collision with root package name */
        private final ErasureTypeAttributes f11845b;

        public b(TypeParameterDescriptor typeParameterDescriptor, ErasureTypeAttributes erasureTypeAttributes) {
            za.k.e(typeParameterDescriptor, "typeParameter");
            za.k.e(erasureTypeAttributes, "typeAttr");
            this.f11844a = typeParameterDescriptor;
            this.f11845b = erasureTypeAttributes;
        }

        public final ErasureTypeAttributes a() {
            return this.f11845b;
        }

        public final TypeParameterDescriptor b() {
            return this.f11844a;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof b)) {
                return false;
            }
            b bVar = (b) obj;
            return za.k.a(bVar.f11844a, this.f11844a) && za.k.a(bVar.f11845b, this.f11845b);
        }

        public int hashCode() {
            int hashCode = this.f11844a.hashCode();
            return hashCode + (hashCode * 31) + this.f11845b.hashCode();
        }

        public String toString() {
            return "DataToEraseUpperBound(typeParameter=" + this.f11844a + ", typeAttr=" + this.f11845b + ')';
        }
    }

    /* compiled from: TypeParameterUpperBoundEraser.kt */
    /* renamed from: gd.j1$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<ErrorType> {
        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ErrorType invoke() {
            return ErrorUtils.d(ErrorTypeKind.A0, TypeParameterUpperBoundEraser.this.toString());
        }
    }

    /* compiled from: TypeParameterUpperBoundEraser.kt */
    /* renamed from: gd.j1$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.l<b, g0> {
        d() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(b bVar) {
            return TypeParameterUpperBoundEraser.this.d(bVar.b(), bVar.a());
        }
    }

    public TypeParameterUpperBoundEraser(ErasureProjectionComputer erasureProjectionComputer, TypeParameterErasureOptions typeParameterErasureOptions) {
        ma.h b10;
        za.k.e(erasureProjectionComputer, "projectionComputer");
        za.k.e(typeParameterErasureOptions, "options");
        this.f11839a = erasureProjectionComputer;
        this.f11840b = typeParameterErasureOptions;
        LockBasedStorageManager lockBasedStorageManager = new LockBasedStorageManager("Type parameter upper bound erasure results");
        this.f11841c = lockBasedStorageManager;
        b10 = ma.j.b(new c());
        this.f11842d = b10;
        fd.g<b, g0> d10 = lockBasedStorageManager.d(new d());
        za.k.d(d10, "storage.createMemoizedFu…ameter, typeAttr) }\n    }");
        this.f11843e = d10;
    }

    private final g0 b(ErasureTypeAttributes erasureTypeAttributes) {
        g0 w10;
        o0 a10 = erasureTypeAttributes.a();
        return (a10 == null || (w10 = ld.a.w(a10)) == null) ? e() : w10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final g0 d(TypeParameterDescriptor typeParameterDescriptor, ErasureTypeAttributes erasureTypeAttributes) {
        int u7;
        int e10;
        int c10;
        List z02;
        int u10;
        Object p02;
        TypeProjection a10;
        Set<TypeParameterDescriptor> c11 = erasureTypeAttributes.c();
        if (c11 != null && c11.contains(typeParameterDescriptor.a())) {
            return b(erasureTypeAttributes);
        }
        o0 x10 = typeParameterDescriptor.x();
        za.k.d(x10, "typeParameter.defaultType");
        Set<TypeParameterDescriptor> g6 = ld.a.g(x10, c11);
        u7 = kotlin.collections.s.u(g6, 10);
        e10 = MapsJVM.e(u7);
        c10 = _Ranges.c(e10, 16);
        LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
        for (TypeParameterDescriptor typeParameterDescriptor2 : g6) {
            if (c11 != null && c11.contains(typeParameterDescriptor2)) {
                a10 = s1.t(typeParameterDescriptor2, erasureTypeAttributes);
                za.k.d(a10, "makeStarProjection(it, typeAttr)");
            } else {
                a10 = this.f11839a.a(typeParameterDescriptor2, erasureTypeAttributes, this, c(typeParameterDescriptor2, erasureTypeAttributes.d(typeParameterDescriptor)));
            }
            ma.o a11 = ma.u.a(typeParameterDescriptor2.n(), a10);
            linkedHashMap.put(a11.c(), a11.d());
        }
        TypeSubstitutor g10 = TypeSubstitutor.g(h1.a.e(h1.f11827c, linkedHashMap, false, 2, null));
        za.k.d(g10, "create(TypeConstructorSu…ap(erasedTypeParameters))");
        List<g0> upperBounds = typeParameterDescriptor.getUpperBounds();
        za.k.d(upperBounds, "typeParameter.upperBounds");
        Set<g0> f10 = f(g10, upperBounds, erasureTypeAttributes);
        if (!f10.isEmpty()) {
            if (!this.f11840b.a()) {
                if (f10.size() == 1) {
                    p02 = _Collections.p0(f10);
                    return (g0) p02;
                }
                throw new IllegalArgumentException("Should only be one computed upper bound if no need to intersect all bounds".toString());
            }
            z02 = _Collections.z0(f10);
            u10 = kotlin.collections.s.u(z02, 10);
            ArrayList arrayList = new ArrayList(u10);
            Iterator it = z02.iterator();
            while (it.hasNext()) {
                arrayList.add(((g0) it.next()).Z0());
            }
            return hd.d.a(arrayList);
        }
        return b(erasureTypeAttributes);
    }

    private final ErrorType e() {
        return (ErrorType) this.f11842d.getValue();
    }

    private final Set<g0> f(TypeSubstitutor typeSubstitutor, List<? extends g0> list, ErasureTypeAttributes erasureTypeAttributes) {
        Set b10;
        Set<g0> a10;
        b10 = SetsJVM.b();
        for (g0 g0Var : list) {
            ClassifierDescriptor v7 = g0Var.W0().v();
            if (v7 instanceof ClassDescriptor) {
                b10.add(f11838f.a(g0Var, typeSubstitutor, erasureTypeAttributes.c(), this.f11840b.b()));
            } else if (v7 instanceof TypeParameterDescriptor) {
                Set<TypeParameterDescriptor> c10 = erasureTypeAttributes.c();
                if (c10 != null && c10.contains(v7)) {
                    b10.add(b(erasureTypeAttributes));
                } else {
                    List<g0> upperBounds = ((TypeParameterDescriptor) v7).getUpperBounds();
                    za.k.d(upperBounds, "declaration.upperBounds");
                    b10.addAll(f(typeSubstitutor, upperBounds, erasureTypeAttributes));
                }
            }
            if (!this.f11840b.a()) {
                break;
            }
        }
        a10 = SetsJVM.a(b10);
        return a10;
    }

    public final g0 c(TypeParameterDescriptor typeParameterDescriptor, ErasureTypeAttributes erasureTypeAttributes) {
        za.k.e(typeParameterDescriptor, "typeParameter");
        za.k.e(erasureTypeAttributes, "typeAttr");
        g0 invoke = this.f11843e.invoke(new b(typeParameterDescriptor, erasureTypeAttributes));
        za.k.d(invoke, "getErasedUpperBound(Data…typeParameter, typeAttr))");
        return invoke;
    }

    public /* synthetic */ TypeParameterUpperBoundEraser(ErasureProjectionComputer erasureProjectionComputer, TypeParameterErasureOptions typeParameterErasureOptions, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(erasureProjectionComputer, (i10 & 2) != 0 ? new TypeParameterErasureOptions(false, false) : typeParameterErasureOptions);
    }
}
