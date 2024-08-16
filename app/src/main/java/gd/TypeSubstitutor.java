package gd;

import id.ErrorType;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.List;
import mb.KotlinBuiltIns;
import mb.StandardNames;
import oc.FqName;
import pb.TypeParameterDescriptor;
import qd.exceptionUtils;

/* compiled from: TypeSubstitutor.java */
/* renamed from: gd.p1, reason: use source file name */
/* loaded from: classes2.dex */
public class TypeSubstitutor {

    /* renamed from: b, reason: collision with root package name */
    public static final TypeSubstitutor f11868b = g(n1.f11857b);

    /* renamed from: a, reason: collision with root package name */
    private final n1 f11869a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TypeSubstitutor.java */
    /* renamed from: gd.p1$a */
    /* loaded from: classes2.dex */
    public static class a implements ya.l<FqName, Boolean> {
        a() {
        }

        private static /* synthetic */ void a(int i10) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "name", "kotlin/reflect/jvm/internal/impl/types/TypeSubstitutor$1", "invoke"));
        }

        @Override // ya.l
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public Boolean invoke(FqName fqName) {
            if (fqName == null) {
                a(0);
            }
            return Boolean.valueOf(!fqName.equals(StandardNames.a.Q));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TypeSubstitutor.java */
    /* renamed from: gd.p1$b */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f11870a;

        static {
            int[] iArr = new int[d.values().length];
            f11870a = iArr;
            try {
                iArr[d.OUT_IN_IN_POSITION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f11870a[d.IN_IN_OUT_POSITION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f11870a[d.NO_CONFLICT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TypeSubstitutor.java */
    /* renamed from: gd.p1$c */
    /* loaded from: classes2.dex */
    public static final class c extends Exception {
        public c(String str) {
            super(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TypeSubstitutor.java */
    /* renamed from: gd.p1$d */
    /* loaded from: classes2.dex */
    public enum d {
        NO_CONFLICT,
        IN_IN_OUT_POSITION,
        OUT_IN_IN_POSITION
    }

    protected TypeSubstitutor(n1 n1Var) {
        if (n1Var == null) {
            a(7);
        }
        this.f11869a = n1Var;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:53:0x0104. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:54:0x0107. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:55:0x010a. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00fc A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0116 A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0058  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x003b A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0021 A[FALL_THROUGH] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void a(int i10) {
        String str;
        int i11;
        if (i10 != 1 && i10 != 2 && i10 != 8 && i10 != 34 && i10 != 37) {
            switch (i10) {
                default:
                    switch (i10) {
                        default:
                            switch (i10) {
                                default:
                                    switch (i10) {
                                        case 40:
                                        case 41:
                                        case 42:
                                            break;
                                        default:
                                            str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                                            break;
                                    }
                                case 29:
                                case 30:
                                case 31:
                                case 32:
                                    str = "@NotNull method %s.%s must not return null";
                                    break;
                            }
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                            break;
                    }
                case 11:
                case 12:
                case 13:
                    break;
            }
            if (i10 != 1 && i10 != 2 && i10 != 8 && i10 != 34 && i10 != 37) {
                switch (i10) {
                    default:
                        switch (i10) {
                            default:
                                switch (i10) {
                                    default:
                                        switch (i10) {
                                            case 40:
                                            case 41:
                                            case 42:
                                                break;
                                            default:
                                                i11 = 3;
                                                break;
                                        }
                                    case 29:
                                    case 30:
                                    case 31:
                                    case 32:
                                        i11 = 2;
                                        break;
                                }
                            case 19:
                            case 20:
                            case 21:
                            case 22:
                            case 23:
                            case 24:
                            case 25:
                                break;
                        }
                    case 11:
                    case 12:
                    case 13:
                        break;
                }
                Object[] objArr = new Object[i11];
                switch (i10) {
                    case 1:
                    case 2:
                    case 8:
                    case 11:
                    case 12:
                    case 13:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 34:
                    case 37:
                    case 40:
                    case 41:
                    case 42:
                        objArr[0] = "kotlin/reflect/jvm/internal/impl/types/TypeSubstitutor";
                        break;
                    case 3:
                        objArr[0] = "first";
                        break;
                    case 4:
                        objArr[0] = "second";
                        break;
                    case 5:
                        objArr[0] = "substitutionContext";
                        break;
                    case 6:
                        objArr[0] = "context";
                        break;
                    case 7:
                    default:
                        objArr[0] = "substitution";
                        break;
                    case 9:
                    case 14:
                        objArr[0] = "type";
                        break;
                    case 10:
                    case 15:
                        objArr[0] = "howThisTypeIsUsed";
                        break;
                    case 16:
                    case 17:
                    case 36:
                        objArr[0] = "typeProjection";
                        break;
                    case 18:
                    case 28:
                        objArr[0] = "originalProjection";
                        break;
                    case 26:
                        objArr[0] = "originalType";
                        break;
                    case 27:
                        objArr[0] = "substituted";
                        break;
                    case 33:
                        objArr[0] = "annotations";
                        break;
                    case 35:
                    case 38:
                        objArr[0] = "typeParameterVariance";
                        break;
                    case 39:
                        objArr[0] = "projectionKind";
                        break;
                }
                if (i10 != 1) {
                    objArr[1] = "replaceWithNonApproximatingSubstitution";
                } else if (i10 == 2) {
                    objArr[1] = "replaceWithContravariantApproximatingSubstitution";
                } else if (i10 == 8) {
                    objArr[1] = "getSubstitution";
                } else if (i10 != 34) {
                    if (i10 != 37) {
                        switch (i10) {
                            case 11:
                            case 12:
                            case 13:
                                objArr[1] = "safeSubstitute";
                                break;
                            default:
                                switch (i10) {
                                    case 19:
                                    case 20:
                                    case 21:
                                    case 22:
                                    case 23:
                                    case 24:
                                    case 25:
                                        objArr[1] = "unsafeSubstitute";
                                        break;
                                    default:
                                        switch (i10) {
                                            case 29:
                                            case 30:
                                            case 31:
                                            case 32:
                                                objArr[1] = "projectedTypeForConflictedTypeWithUnsafeVariance";
                                                break;
                                            default:
                                                switch (i10) {
                                                    case 40:
                                                    case 41:
                                                    case 42:
                                                        break;
                                                    default:
                                                        objArr[1] = "kotlin/reflect/jvm/internal/impl/types/TypeSubstitutor";
                                                        break;
                                                }
                                        }
                                }
                        }
                    }
                    objArr[1] = "combine";
                } else {
                    objArr[1] = "filterOutUnsafeVariance";
                }
                switch (i10) {
                    case 1:
                    case 2:
                    case 8:
                    case 11:
                    case 12:
                    case 13:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 34:
                    case 37:
                    case 40:
                    case 41:
                    case 42:
                        break;
                    case 3:
                    case 4:
                        objArr[2] = "createChainedSubstitutor";
                        break;
                    case 5:
                    case 6:
                    default:
                        objArr[2] = "create";
                        break;
                    case 7:
                        objArr[2] = "<init>";
                        break;
                    case 9:
                    case 10:
                        objArr[2] = "safeSubstitute";
                        break;
                    case 14:
                    case 15:
                    case 16:
                        objArr[2] = "substitute";
                        break;
                    case 17:
                        objArr[2] = "substituteWithoutApproximation";
                        break;
                    case 18:
                        objArr[2] = "unsafeSubstitute";
                        break;
                    case 26:
                    case 27:
                    case 28:
                        objArr[2] = "projectedTypeForConflictedTypeWithUnsafeVariance";
                        break;
                    case 33:
                        objArr[2] = "filterOutUnsafeVariance";
                        break;
                    case 35:
                    case 36:
                    case 38:
                    case 39:
                        objArr[2] = "combine";
                        break;
                }
                String format = String.format(str, objArr);
                if (i10 != 1 && i10 != 2 && i10 != 8 && i10 != 34 && i10 != 37) {
                    switch (i10) {
                        default:
                            switch (i10) {
                                default:
                                    switch (i10) {
                                        default:
                                            switch (i10) {
                                                case 40:
                                                case 41:
                                                case 42:
                                                    break;
                                                default:
                                                    throw new IllegalArgumentException(format);
                                            }
                                        case 29:
                                        case 30:
                                        case 31:
                                        case 32:
                                            throw new IllegalStateException(format);
                                    }
                                case 19:
                                case 20:
                                case 21:
                                case 22:
                                case 23:
                                case 24:
                                case 25:
                                    break;
                            }
                        case 11:
                        case 12:
                        case 13:
                            break;
                    }
                }
                throw new IllegalStateException(format);
            }
            i11 = 2;
            Object[] objArr2 = new Object[i11];
            switch (i10) {
            }
            if (i10 != 1) {
            }
            switch (i10) {
            }
            String format2 = String.format(str, objArr2);
            if (i10 != 1) {
                switch (i10) {
                }
            }
            throw new IllegalStateException(format2);
        }
        str = "@NotNull method %s.%s must not return null";
        if (i10 != 1) {
            switch (i10) {
            }
            Object[] objArr22 = new Object[i11];
            switch (i10) {
            }
            if (i10 != 1) {
            }
            switch (i10) {
            }
            String format22 = String.format(str, objArr22);
            if (i10 != 1) {
            }
            throw new IllegalStateException(format22);
        }
        i11 = 2;
        Object[] objArr222 = new Object[i11];
        switch (i10) {
        }
        if (i10 != 1) {
        }
        switch (i10) {
        }
        String format222 = String.format(str, objArr222);
        if (i10 != 1) {
        }
        throw new IllegalStateException(format222);
    }

    private static void b(int i10, TypeProjection typeProjection, n1 n1Var) {
        if (i10 <= 100) {
            return;
        }
        throw new IllegalStateException("Recursion too deep. Most likely infinite loop while substituting " + o(typeProjection) + "; substitution: " + o(n1Var));
    }

    public static Variance c(Variance variance, TypeProjection typeProjection) {
        if (variance == null) {
            a(35);
        }
        if (typeProjection == null) {
            a(36);
        }
        if (!typeProjection.b()) {
            return d(variance, typeProjection.a());
        }
        Variance variance2 = Variance.OUT_VARIANCE;
        if (variance2 == null) {
            a(37);
        }
        return variance2;
    }

    public static Variance d(Variance variance, Variance variance2) {
        if (variance == null) {
            a(38);
        }
        if (variance2 == null) {
            a(39);
        }
        Variance variance3 = Variance.INVARIANT;
        if (variance == variance3) {
            if (variance2 == null) {
                a(40);
            }
            return variance2;
        }
        if (variance2 == variance3) {
            if (variance == null) {
                a(41);
            }
            return variance;
        }
        if (variance == variance2) {
            if (variance2 == null) {
                a(42);
            }
            return variance2;
        }
        throw new AssertionError("Variance conflict: type parameter variance '" + variance + "' and projection kind '" + variance2 + "' cannot be combined");
    }

    private static d e(Variance variance, Variance variance2) {
        Variance variance3 = Variance.IN_VARIANCE;
        if (variance == variance3 && variance2 == Variance.OUT_VARIANCE) {
            return d.OUT_IN_IN_POSITION;
        }
        if (variance == Variance.OUT_VARIANCE && variance2 == variance3) {
            return d.IN_IN_OUT_POSITION;
        }
        return d.NO_CONFLICT;
    }

    public static TypeSubstitutor f(g0 g0Var) {
        if (g0Var == null) {
            a(6);
        }
        return g(h1.i(g0Var.W0(), g0Var.U0()));
    }

    public static TypeSubstitutor g(n1 n1Var) {
        if (n1Var == null) {
            a(0);
        }
        return new TypeSubstitutor(n1Var);
    }

    public static TypeSubstitutor h(n1 n1Var, n1 n1Var2) {
        if (n1Var == null) {
            a(3);
        }
        if (n1Var2 == null) {
            a(4);
        }
        return g(DisjointKeysUnionTypeSubstitution.i(n1Var, n1Var2));
    }

    private static qb.g i(qb.g gVar) {
        if (gVar == null) {
            a(33);
        }
        return !gVar.a(StandardNames.a.Q) ? gVar : new qb.l(gVar, new a());
    }

    private static TypeProjection l(g0 g0Var, TypeProjection typeProjection, TypeParameterDescriptor typeParameterDescriptor, TypeProjection typeProjection2) {
        if (g0Var == null) {
            a(26);
        }
        if (typeProjection == null) {
            a(27);
        }
        if (typeProjection2 == null) {
            a(28);
        }
        if (!g0Var.i().a(StandardNames.a.Q)) {
            if (typeProjection == null) {
                a(29);
            }
            return typeProjection;
        }
        TypeConstructor W0 = typeProjection.getType().W0();
        if (!(W0 instanceof hd.j)) {
            return typeProjection;
        }
        TypeProjection b10 = ((hd.j) W0).b();
        Variance a10 = b10.a();
        d e10 = e(typeProjection2.a(), a10);
        d dVar = d.OUT_IN_IN_POSITION;
        if (e10 == dVar) {
            return new TypeProjectionImpl(b10.getType());
        }
        return (typeParameterDescriptor != null && e(typeParameterDescriptor.s(), a10) == dVar) ? new TypeProjectionImpl(b10.getType()) : typeProjection;
    }

    private static String o(Object obj) {
        try {
            return obj.toString();
        } catch (Throwable th) {
            if (!exceptionUtils.a(th)) {
                return "[Exception while computing toString(): " + th + "]";
            }
            throw th;
        }
    }

    private TypeProjection r(TypeProjection typeProjection, int i10) {
        g0 type = typeProjection.getType();
        Variance a10 = typeProjection.a();
        if (type.W0().v() instanceof TypeParameterDescriptor) {
            return typeProjection;
        }
        o0 b10 = s0.b(type);
        g0 p10 = b10 != null ? m().p(b10, Variance.INVARIANT) : null;
        g0 b11 = o1.b(type, s(type.W0().getParameters(), type.U0(), i10), this.f11869a.d(type.i()));
        if ((b11 instanceof o0) && (p10 instanceof o0)) {
            b11 = s0.j((o0) b11, (o0) p10);
        }
        return new TypeProjectionImpl(a10, b11);
    }

    private List<TypeProjection> s(List<TypeParameterDescriptor> list, List<TypeProjection> list2, int i10) {
        ArrayList arrayList = new ArrayList(list.size());
        boolean z10 = false;
        for (int i11 = 0; i11 < list.size(); i11++) {
            TypeParameterDescriptor typeParameterDescriptor = list.get(i11);
            TypeProjection typeProjection = list2.get(i11);
            TypeProjection u7 = u(typeProjection, typeParameterDescriptor, i10 + 1);
            int i12 = b.f11870a[e(typeParameterDescriptor.s(), u7.a()).ordinal()];
            if (i12 == 1 || i12 == 2) {
                u7 = s1.s(typeParameterDescriptor);
            } else if (i12 == 3) {
                Variance s7 = typeParameterDescriptor.s();
                Variance variance = Variance.INVARIANT;
                if (s7 != variance && !u7.b()) {
                    u7 = new TypeProjectionImpl(variance, u7.getType());
                }
            }
            if (u7 != typeProjection) {
                z10 = true;
            }
            arrayList.add(u7);
        }
        return !z10 ? list2 : arrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private TypeProjection u(TypeProjection typeProjection, TypeParameterDescriptor typeParameterDescriptor, int i10) {
        g0 q10;
        if (typeProjection == null) {
            a(18);
        }
        b(i10, typeProjection, this.f11869a);
        if (typeProjection.b()) {
            return typeProjection;
        }
        g0 type = typeProjection.getType();
        if (type instanceof t1) {
            t1 t1Var = (t1) type;
            v1 O0 = t1Var.O0();
            g0 Q = t1Var.Q();
            TypeProjection u7 = u(new TypeProjectionImpl(typeProjection.a(), O0), typeParameterDescriptor, i10 + 1);
            return u7.b() ? u7 : new TypeProjectionImpl(u7.a(), u1.d(u7.getType().Z0(), p(Q, typeProjection.a())));
        }
        if (!w.a(type) && !(type.Z0() instanceof n0)) {
            TypeProjection e10 = this.f11869a.e(type);
            TypeProjection l10 = e10 != null ? l(type, e10, typeParameterDescriptor, typeProjection) : null;
            Variance a10 = typeProjection.a();
            if (l10 == null && d0.b(type) && !e1.b(type)) {
                a0 a11 = d0.a(type);
                int i11 = i10 + 1;
                TypeProjection u10 = u(new TypeProjectionImpl(a10, a11.e1()), typeParameterDescriptor, i11);
                TypeProjection u11 = u(new TypeProjectionImpl(a10, a11.f1()), typeParameterDescriptor, i11);
                return (u10.getType() == a11.e1() && u11.getType() == a11.f1()) ? typeProjection : new TypeProjectionImpl(u10.a(), h0.d(o1.a(u10.getType()), o1.a(u11.getType())));
            }
            if (!KotlinBuiltIns.m0(type) && !i0.a(type)) {
                if (l10 != null) {
                    d e11 = e(a10, l10.a());
                    if (!tc.d.d(type)) {
                        int i12 = b.f11870a[e11.ordinal()];
                        if (i12 == 1) {
                            throw new c("Out-projection in in-position");
                        }
                        if (i12 == 2) {
                            return new TypeProjectionImpl(Variance.OUT_VARIANCE, type.W0().t().I());
                        }
                    }
                    n a12 = e1.a(type);
                    if (l10.b()) {
                        return l10;
                    }
                    if (a12 != null) {
                        q10 = a12.P(l10.getType());
                    } else {
                        q10 = s1.q(l10.getType(), type.X0());
                    }
                    if (!type.i().isEmpty()) {
                        q10 = ld.a.v(q10, new qb.k(q10.i(), i(this.f11869a.d(type.i()))));
                    }
                    if (e11 == d.NO_CONFLICT) {
                        a10 = d(a10, l10.a());
                    }
                    return new TypeProjectionImpl(a10, q10);
                }
                TypeProjection r10 = r(typeProjection, i10);
                if (r10 == null) {
                    a(25);
                }
                return r10;
            }
        }
        return typeProjection;
    }

    public n1 j() {
        n1 n1Var = this.f11869a;
        if (n1Var == null) {
            a(8);
        }
        return n1Var;
    }

    public boolean k() {
        return this.f11869a.f();
    }

    public TypeSubstitutor m() {
        n1 n1Var = this.f11869a;
        return ((n1Var instanceof e0) && n1Var.b()) ? new TypeSubstitutor(new e0(((e0) this.f11869a).j(), ((e0) this.f11869a).i(), false)) : this;
    }

    public g0 n(g0 g0Var, Variance variance) {
        if (g0Var == null) {
            a(9);
        }
        if (variance == null) {
            a(10);
        }
        if (k()) {
            if (g0Var == null) {
                a(11);
            }
            return g0Var;
        }
        try {
            g0 type = u(new TypeProjectionImpl(variance, g0Var), null, 0).getType();
            if (type == null) {
                a(12);
            }
            return type;
        } catch (c e10) {
            ErrorType d10 = ErrorUtils.d(ErrorTypeKind.H, e10.getMessage());
            if (d10 == null) {
                a(13);
            }
            return d10;
        }
    }

    public g0 p(g0 g0Var, Variance variance) {
        if (g0Var == null) {
            a(14);
        }
        if (variance == null) {
            a(15);
        }
        TypeProjection q10 = q(new TypeProjectionImpl(variance, j().g(g0Var, variance)));
        if (q10 == null) {
            return null;
        }
        return q10.getType();
    }

    public TypeProjection q(TypeProjection typeProjection) {
        if (typeProjection == null) {
            a(16);
        }
        TypeProjection t7 = t(typeProjection);
        return (this.f11869a.a() || this.f11869a.b()) ? md.b.c(t7, this.f11869a.b()) : t7;
    }

    public TypeProjection t(TypeProjection typeProjection) {
        if (typeProjection == null) {
            a(17);
        }
        if (k()) {
            return typeProjection;
        }
        try {
            return u(typeProjection, null, 0);
        } catch (c unused) {
            return null;
        }
    }
}
