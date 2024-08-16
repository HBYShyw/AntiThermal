package gd;

import id.ErrorType;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import qd.SmartSet;

/* compiled from: TypeUtils.java */
/* loaded from: classes2.dex */
public class s1 {

    /* renamed from: a, reason: collision with root package name */
    public static final o0 f11883a = ErrorUtils.d(ErrorTypeKind.I, new String[0]);

    /* renamed from: b, reason: collision with root package name */
    public static final o0 f11884b = ErrorUtils.d(ErrorTypeKind.B, new String[0]);

    /* renamed from: c, reason: collision with root package name */
    public static final o0 f11885c = new a("NO_EXPECTED_TYPE");

    /* renamed from: d, reason: collision with root package name */
    public static final o0 f11886d = new a("UNIT_EXPECTED_TYPE");

    /* compiled from: TypeUtils.java */
    /* loaded from: classes2.dex */
    public static class a extends r {

        /* renamed from: f, reason: collision with root package name */
        private final String f11887f;

        public a(String str) {
            this.f11887f = str;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x0036  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0044  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x003e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private static /* synthetic */ void i1(int i10) {
            String format;
            String str = (i10 == 1 || i10 == 4) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            Object[] objArr = new Object[(i10 == 1 || i10 == 4) ? 2 : 3];
            if (i10 != 1) {
                if (i10 == 2) {
                    objArr[0] = "delegate";
                } else if (i10 == 3) {
                    objArr[0] = "kotlinTypeRefiner";
                } else if (i10 != 4) {
                    objArr[0] = "newAttributes";
                }
                if (i10 != 1) {
                    objArr[1] = "toString";
                } else if (i10 != 4) {
                    objArr[1] = "kotlin/reflect/jvm/internal/impl/types/TypeUtils$SpecialType";
                } else {
                    objArr[1] = "refine";
                }
                if (i10 != 1) {
                    if (i10 == 2) {
                        objArr[2] = "replaceDelegate";
                    } else if (i10 == 3) {
                        objArr[2] = "refine";
                    } else if (i10 != 4) {
                        objArr[2] = "replaceAttributes";
                    }
                }
                format = String.format(str, objArr);
                if (i10 == 1 && i10 != 4) {
                    throw new IllegalArgumentException(format);
                }
                throw new IllegalStateException(format);
            }
            objArr[0] = "kotlin/reflect/jvm/internal/impl/types/TypeUtils$SpecialType";
            if (i10 != 1) {
            }
            if (i10 != 1) {
            }
            format = String.format(str, objArr);
            if (i10 == 1) {
            }
            throw new IllegalStateException(format);
        }

        @Override // gd.v1
        /* renamed from: d1, reason: merged with bridge method [inline-methods] */
        public o0 a1(boolean z10) {
            throw new IllegalStateException(this.f11887f);
        }

        @Override // gd.v1
        /* renamed from: e1, reason: merged with bridge method [inline-methods] */
        public o0 c1(c1 c1Var) {
            if (c1Var == null) {
                i1(0);
            }
            throw new IllegalStateException(this.f11887f);
        }

        @Override // gd.r
        protected o0 f1() {
            throw new IllegalStateException(this.f11887f);
        }

        @Override // gd.r
        public r h1(o0 o0Var) {
            if (o0Var == null) {
                i1(2);
            }
            throw new IllegalStateException(this.f11887f);
        }

        @Override // gd.r
        /* renamed from: j1, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
        public a g1(hd.g gVar) {
            if (gVar == null) {
                i1(3);
            }
            return this;
        }

        @Override // gd.o0
        public String toString() {
            String str = this.f11887f;
            if (str == null) {
                i1(1);
            }
            return str;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:107:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x00c3  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x00d2  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x012e  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x013a  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0152  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0169  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0173  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x017d  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0182  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0187  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0191  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0194  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0199  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x019e  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01ac  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01b2  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01c1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01da  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void a(int i10) {
        String str;
        int i11;
        if (i10 != 4 && i10 != 9 && i10 != 11 && i10 != 15 && i10 != 17 && i10 != 19 && i10 != 26 && i10 != 35 && i10 != 48 && i10 != 53 && i10 != 6 && i10 != 7) {
            switch (i10) {
                case 56:
                case 57:
                case 58:
                case 59:
                    break;
                default:
                    str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                    break;
            }
            if (i10 != 4 && i10 != 9 && i10 != 11 && i10 != 15 && i10 != 17 && i10 != 19 && i10 != 26 && i10 != 35 && i10 != 48 && i10 != 53 && i10 != 6 && i10 != 7) {
                switch (i10) {
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                        break;
                    default:
                        i11 = 3;
                        break;
                }
                Object[] objArr = new Object[i11];
                switch (i10) {
                    case 4:
                    case 6:
                    case 7:
                    case 9:
                    case 11:
                    case 15:
                    case 17:
                    case 19:
                    case 26:
                    case 35:
                    case 48:
                    case 53:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                        objArr[0] = "kotlin/reflect/jvm/internal/impl/types/TypeUtils";
                        break;
                    case 5:
                    case 8:
                    case 10:
                    case 18:
                    case 23:
                    case 25:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 38:
                    case 40:
                    default:
                        objArr[0] = "type";
                        break;
                    case 12:
                        objArr[0] = "typeConstructor";
                        break;
                    case 13:
                        objArr[0] = "unsubstitutedMemberScope";
                        break;
                    case 14:
                        objArr[0] = "refinedTypeFactory";
                        break;
                    case 16:
                        objArr[0] = "parameters";
                        break;
                    case 20:
                        objArr[0] = "subType";
                        break;
                    case 21:
                        objArr[0] = "superType";
                        break;
                    case 22:
                        objArr[0] = "substitutor";
                        break;
                    case 24:
                        objArr[0] = "result";
                        break;
                    case 31:
                    case 33:
                        objArr[0] = "clazz";
                        break;
                    case 32:
                        objArr[0] = "typeArguments";
                        break;
                    case 34:
                        objArr[0] = "projections";
                        break;
                    case 36:
                        objArr[0] = "a";
                        break;
                    case 37:
                        objArr[0] = "b";
                        break;
                    case 39:
                        objArr[0] = "typeParameters";
                        break;
                    case 41:
                        objArr[0] = "typeParameterConstructors";
                        break;
                    case 42:
                        objArr[0] = "specialType";
                        break;
                    case 43:
                    case 44:
                        objArr[0] = "isSpecialType";
                        break;
                    case 45:
                    case 46:
                        objArr[0] = "parameterDescriptor";
                        break;
                    case 47:
                    case 51:
                        objArr[0] = "numberValueTypeConstructor";
                        break;
                    case 49:
                    case 50:
                        objArr[0] = "supertypes";
                        break;
                    case 52:
                    case 55:
                        objArr[0] = "expectedType";
                        break;
                    case 54:
                        objArr[0] = "literalTypeConstructor";
                        break;
                }
                if (i10 == 4) {
                    if (i10 != 9) {
                        if (i10 == 11 || i10 == 15) {
                            objArr[1] = "makeUnsubstitutedType";
                        } else if (i10 == 17) {
                            objArr[1] = "getDefaultTypeProjections";
                        } else if (i10 == 19) {
                            objArr[1] = "getImmediateSupertypes";
                        } else if (i10 == 26) {
                            objArr[1] = "getAllSupertypes";
                        } else if (i10 == 35) {
                            objArr[1] = "substituteProjectionsForParameters";
                        } else if (i10 != 48) {
                            if (i10 != 53) {
                                if (i10 != 6 && i10 != 7) {
                                    switch (i10) {
                                        case 56:
                                        case 57:
                                        case 58:
                                        case 59:
                                            break;
                                        default:
                                            objArr[1] = "kotlin/reflect/jvm/internal/impl/types/TypeUtils";
                                            break;
                                    }
                                }
                            }
                            objArr[1] = "getPrimitiveNumberType";
                        } else {
                            objArr[1] = "getDefaultPrimitiveNumberType";
                        }
                    }
                    objArr[1] = "makeNullableIfNeeded";
                } else {
                    objArr[1] = "makeNullableAsSpecified";
                }
                switch (i10) {
                    case 1:
                        objArr[2] = "makeNullable";
                        break;
                    case 2:
                        objArr[2] = "makeNotNullable";
                        break;
                    case 3:
                        objArr[2] = "makeNullableAsSpecified";
                        break;
                    case 4:
                    case 6:
                    case 7:
                    case 9:
                    case 11:
                    case 15:
                    case 17:
                    case 19:
                    case 26:
                    case 35:
                    case 48:
                    case 53:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                        break;
                    case 5:
                    case 8:
                        objArr[2] = "makeNullableIfNeeded";
                        break;
                    case 10:
                        objArr[2] = "canHaveSubtypes";
                        break;
                    case 12:
                    case 13:
                    case 14:
                        objArr[2] = "makeUnsubstitutedType";
                        break;
                    case 16:
                        objArr[2] = "getDefaultTypeProjections";
                        break;
                    case 18:
                        objArr[2] = "getImmediateSupertypes";
                        break;
                    case 20:
                    case 21:
                    case 22:
                        objArr[2] = "createSubstitutedSupertype";
                        break;
                    case 23:
                    case 24:
                        objArr[2] = "collectAllSupertypes";
                        break;
                    case 25:
                        objArr[2] = "getAllSupertypes";
                        break;
                    case 27:
                        objArr[2] = "isNullableType";
                        break;
                    case 28:
                        objArr[2] = "acceptsNullable";
                        break;
                    case 29:
                        objArr[2] = "hasNullableSuperType";
                        break;
                    case 30:
                        objArr[2] = "getClassDescriptor";
                        break;
                    case 31:
                    case 32:
                        objArr[2] = "substituteParameters";
                        break;
                    case 33:
                    case 34:
                        objArr[2] = "substituteProjectionsForParameters";
                        break;
                    case 36:
                    case 37:
                        objArr[2] = "equalTypes";
                        break;
                    case 38:
                    case 39:
                        objArr[2] = "dependsOnTypeParameters";
                        break;
                    case 40:
                    case 41:
                        objArr[2] = "dependsOnTypeConstructors";
                        break;
                    case 42:
                    case 43:
                    case 44:
                        objArr[2] = "contains";
                        break;
                    case 45:
                    case 46:
                        objArr[2] = "makeStarProjection";
                        break;
                    case 47:
                    case 49:
                        objArr[2] = "getDefaultPrimitiveNumberType";
                        break;
                    case 50:
                        objArr[2] = "findByFqName";
                        break;
                    case 51:
                    case 52:
                    case 54:
                    case 55:
                        objArr[2] = "getPrimitiveNumberType";
                        break;
                    case 60:
                        objArr[2] = "isTypeParameter";
                        break;
                    case 61:
                        objArr[2] = "isReifiedTypeParameter";
                        break;
                    case 62:
                        objArr[2] = "isNonReifiedTypeParameter";
                        break;
                    case 63:
                        objArr[2] = "getTypeParameterDescriptorOrNull";
                        break;
                    default:
                        objArr[2] = "noExpectedType";
                        break;
                }
                String format = String.format(str, objArr);
                if (i10 != 4 && i10 != 9 && i10 != 11 && i10 != 15 && i10 != 17 && i10 != 19 && i10 != 26 && i10 != 35 && i10 != 48 && i10 != 53 && i10 != 6 && i10 != 7) {
                    switch (i10) {
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                            break;
                        default:
                            throw new IllegalArgumentException(format);
                    }
                }
                throw new IllegalStateException(format);
            }
            i11 = 2;
            Object[] objArr2 = new Object[i11];
            switch (i10) {
            }
            if (i10 == 4) {
            }
            switch (i10) {
            }
            String format2 = String.format(str, objArr2);
            if (i10 != 4) {
                switch (i10) {
                }
            }
            throw new IllegalStateException(format2);
        }
        str = "@NotNull method %s.%s must not return null";
        if (i10 != 4) {
            switch (i10) {
            }
            Object[] objArr22 = new Object[i11];
            switch (i10) {
            }
            if (i10 == 4) {
            }
            switch (i10) {
            }
            String format22 = String.format(str, objArr22);
            if (i10 != 4) {
            }
            throw new IllegalStateException(format22);
        }
        i11 = 2;
        Object[] objArr222 = new Object[i11];
        switch (i10) {
        }
        if (i10 == 4) {
        }
        switch (i10) {
        }
        String format222 = String.format(str, objArr222);
        if (i10 != 4) {
        }
        throw new IllegalStateException(format222);
    }

    public static boolean b(g0 g0Var) {
        if (g0Var == null) {
            a(28);
        }
        if (g0Var.X0()) {
            return true;
        }
        return d0.b(g0Var) && b(d0.a(g0Var).f1());
    }

    public static boolean c(g0 g0Var, ya.l<v1, Boolean> lVar) {
        if (lVar == null) {
            a(43);
        }
        return d(g0Var, lVar, null);
    }

    private static boolean d(g0 g0Var, ya.l<v1, Boolean> lVar, SmartSet<g0> smartSet) {
        if (lVar == null) {
            a(44);
        }
        if (g0Var == null) {
            return false;
        }
        v1 Z0 = g0Var.Z0();
        if (w(g0Var)) {
            return lVar.invoke(Z0).booleanValue();
        }
        if (smartSet != null && smartSet.contains(g0Var)) {
            return false;
        }
        if (lVar.invoke(Z0).booleanValue()) {
            return true;
        }
        if (smartSet == null) {
            smartSet = SmartSet.c();
        }
        smartSet.add(g0Var);
        a0 a0Var = Z0 instanceof a0 ? (a0) Z0 : null;
        if (a0Var != null && (d(a0Var.e1(), lVar, smartSet) || d(a0Var.f1(), lVar, smartSet))) {
            return true;
        }
        if ((Z0 instanceof p) && d(((p) Z0).i1(), lVar, smartSet)) {
            return true;
        }
        TypeConstructor W0 = g0Var.W0();
        if (W0 instanceof IntersectionTypeConstructor) {
            Iterator<g0> it = ((IntersectionTypeConstructor) W0).q().iterator();
            while (it.hasNext()) {
                if (d(it.next(), lVar, smartSet)) {
                    return true;
                }
            }
            return false;
        }
        for (TypeProjection typeProjection : g0Var.U0()) {
            if (!typeProjection.b() && d(typeProjection.getType(), lVar, smartSet)) {
                return true;
            }
        }
        return false;
    }

    public static g0 e(g0 g0Var, g0 g0Var2, TypeSubstitutor typeSubstitutor) {
        if (g0Var == null) {
            a(20);
        }
        if (g0Var2 == null) {
            a(21);
        }
        if (typeSubstitutor == null) {
            a(22);
        }
        g0 p10 = typeSubstitutor.p(g0Var2, Variance.INVARIANT);
        if (p10 != null) {
            return q(p10, g0Var.X0());
        }
        return null;
    }

    public static ClassDescriptor f(g0 g0Var) {
        if (g0Var == null) {
            a(30);
        }
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (v7 instanceof ClassDescriptor) {
            return (ClassDescriptor) v7;
        }
        return null;
    }

    public static List<TypeProjection> g(List<TypeParameterDescriptor> list) {
        List<TypeProjection> z02;
        if (list == null) {
            a(16);
        }
        ArrayList arrayList = new ArrayList(list.size());
        Iterator<TypeParameterDescriptor> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(new TypeProjectionImpl(it.next().x()));
        }
        z02 = _Collections.z0(arrayList);
        if (z02 == null) {
            a(17);
        }
        return z02;
    }

    public static List<g0> h(g0 g0Var) {
        if (g0Var == null) {
            a(18);
        }
        TypeSubstitutor f10 = TypeSubstitutor.f(g0Var);
        Collection<g0> q10 = g0Var.W0().q();
        ArrayList arrayList = new ArrayList(q10.size());
        Iterator<g0> it = q10.iterator();
        while (it.hasNext()) {
            g0 e10 = e(g0Var, it.next(), f10);
            if (e10 != null) {
                arrayList.add(e10);
            }
        }
        return arrayList;
    }

    public static TypeParameterDescriptor i(g0 g0Var) {
        if (g0Var == null) {
            a(63);
        }
        if (g0Var.W0().v() instanceof TypeParameterDescriptor) {
            return (TypeParameterDescriptor) g0Var.W0().v();
        }
        return null;
    }

    public static boolean j(g0 g0Var) {
        if (g0Var == null) {
            a(29);
        }
        if (g0Var.W0().v() instanceof ClassDescriptor) {
            return false;
        }
        Iterator<g0> it = h(g0Var).iterator();
        while (it.hasNext()) {
            if (l(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static boolean k(g0 g0Var) {
        return g0Var != null && g0Var.W0() == f11883a.W0();
    }

    public static boolean l(g0 g0Var) {
        if (g0Var == null) {
            a(27);
        }
        if (g0Var.X0()) {
            return true;
        }
        if (d0.b(g0Var) && l(d0.a(g0Var).f1())) {
            return true;
        }
        if (s0.c(g0Var)) {
            return false;
        }
        if (m(g0Var)) {
            return j(g0Var);
        }
        if (g0Var instanceof e) {
            TypeParameterDescriptor a10 = ((e) g0Var).f1().a();
            return a10 == null || j(a10.x());
        }
        TypeConstructor W0 = g0Var.W0();
        if (W0 instanceof IntersectionTypeConstructor) {
            Iterator<g0> it = W0.q().iterator();
            while (it.hasNext()) {
                if (l(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean m(g0 g0Var) {
        if (g0Var == null) {
            a(60);
        }
        return i(g0Var) != null || (g0Var.W0() instanceof hd.n);
    }

    public static g0 n(g0 g0Var) {
        if (g0Var == null) {
            a(2);
        }
        return p(g0Var, false);
    }

    public static g0 o(g0 g0Var) {
        if (g0Var == null) {
            a(1);
        }
        return p(g0Var, true);
    }

    public static g0 p(g0 g0Var, boolean z10) {
        if (g0Var == null) {
            a(3);
        }
        v1 a12 = g0Var.Z0().a1(z10);
        if (a12 == null) {
            a(4);
        }
        return a12;
    }

    public static g0 q(g0 g0Var, boolean z10) {
        if (g0Var == null) {
            a(8);
        }
        if (z10) {
            return o(g0Var);
        }
        if (g0Var == null) {
            a(9);
        }
        return g0Var;
    }

    public static o0 r(o0 o0Var, boolean z10) {
        if (o0Var == null) {
            a(5);
        }
        if (!z10) {
            if (o0Var == null) {
                a(7);
            }
            return o0Var;
        }
        o0 a12 = o0Var.a1(true);
        if (a12 == null) {
            a(6);
        }
        return a12;
    }

    public static TypeProjection s(TypeParameterDescriptor typeParameterDescriptor) {
        if (typeParameterDescriptor == null) {
            a(45);
        }
        return new u0(typeParameterDescriptor);
    }

    public static TypeProjection t(TypeParameterDescriptor typeParameterDescriptor, ErasureTypeAttributes erasureTypeAttributes) {
        if (typeParameterDescriptor == null) {
            a(46);
        }
        if (erasureTypeAttributes.b() == TypeUsage.SUPERTYPE) {
            return new TypeProjectionImpl(v0.b(typeParameterDescriptor));
        }
        return new u0(typeParameterDescriptor);
    }

    public static o0 u(TypeConstructor typeConstructor, zc.h hVar, ya.l<hd.g, o0> lVar) {
        if (typeConstructor == null) {
            a(12);
        }
        if (hVar == null) {
            a(13);
        }
        if (lVar == null) {
            a(14);
        }
        o0 l10 = h0.l(c1.f11749f.h(), typeConstructor, g(typeConstructor.getParameters()), false, hVar, lVar);
        if (l10 == null) {
            a(15);
        }
        return l10;
    }

    public static o0 v(ClassifierDescriptor classifierDescriptor, zc.h hVar, ya.l<hd.g, o0> lVar) {
        if (ErrorUtils.m(classifierDescriptor)) {
            ErrorType d10 = ErrorUtils.d(ErrorTypeKind.H, classifierDescriptor.toString());
            if (d10 == null) {
                a(11);
            }
            return d10;
        }
        return u(classifierDescriptor.n(), hVar, lVar);
    }

    public static boolean w(g0 g0Var) {
        if (g0Var == null) {
            a(0);
        }
        return g0Var == f11885c || g0Var == f11886d;
    }
}
