package mb;

import com.oplus.deepthinker.sdk.app.geofence.Geofence;
import fd.StorageManager;
import gd.TypeConstructor;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.d1;
import gd.g0;
import gd.h0;
import gd.o0;
import gd.s1;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mb.StandardNames;
import nb.BuiltInFictitiousFunctionClassFactory;
import oc.ClassId;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;
import pb.PackageViewDescriptor;
import pb.PropertyDescriptor;
import pb.PropertyGetterDescriptor;
import pb.PropertySetterDescriptor;
import pb.findClassInModule;
import pb.s;
import rb.AdditionalClassPartsProvider;
import rb.ClassDescriptorFactory;
import rb.c;
import sb.x;

/* compiled from: KotlinBuiltIns.java */
/* renamed from: mb.h, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class KotlinBuiltIns {

    /* renamed from: g, reason: collision with root package name */
    public static final Name f15217g = Name.i("<built-ins module>");

    /* renamed from: a, reason: collision with root package name */
    private x f15218a;

    /* renamed from: b, reason: collision with root package name */
    private fd.i<x> f15219b;

    /* renamed from: c, reason: collision with root package name */
    private final fd.i<e> f15220c;

    /* renamed from: d, reason: collision with root package name */
    private final fd.i<Collection<PackageViewDescriptor>> f15221d;

    /* renamed from: e, reason: collision with root package name */
    private final fd.g<Name, ClassDescriptor> f15222e;

    /* renamed from: f, reason: collision with root package name */
    private final StorageManager f15223f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KotlinBuiltIns.java */
    /* renamed from: mb.h$a */
    /* loaded from: classes2.dex */
    public class a implements ya.a<Collection<PackageViewDescriptor>> {
        a() {
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Collection<PackageViewDescriptor> invoke() {
            return Arrays.asList(KotlinBuiltIns.this.r().H(StandardNames.f15283u), KotlinBuiltIns.this.r().H(StandardNames.f15285w), KotlinBuiltIns.this.r().H(StandardNames.f15286x), KotlinBuiltIns.this.r().H(StandardNames.f15284v));
        }
    }

    /* compiled from: KotlinBuiltIns.java */
    /* renamed from: mb.h$b */
    /* loaded from: classes2.dex */
    class b implements ya.a<e> {
        b() {
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public e invoke() {
            EnumMap enumMap = new EnumMap(PrimitiveType.class);
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            for (PrimitiveType primitiveType : PrimitiveType.values()) {
                o0 q10 = KotlinBuiltIns.this.q(primitiveType.e().b());
                o0 q11 = KotlinBuiltIns.this.q(primitiveType.c().b());
                enumMap.put((EnumMap) primitiveType, (PrimitiveType) q11);
                hashMap.put(q10, q11);
                hashMap2.put(q11, q10);
            }
            return new e(enumMap, hashMap, hashMap2, null);
        }
    }

    /* compiled from: KotlinBuiltIns.java */
    /* renamed from: mb.h$c */
    /* loaded from: classes2.dex */
    class c implements ya.l<Name, ClassDescriptor> {
        c() {
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public ClassDescriptor invoke(Name name) {
            ClassifierDescriptor e10 = KotlinBuiltIns.this.s().e(name, xb.d.FROM_BUILTINS);
            if (e10 != null) {
                if (e10 instanceof ClassDescriptor) {
                    return (ClassDescriptor) e10;
                }
                throw new AssertionError("Must be a class descriptor " + name + ", but was " + e10);
            }
            throw new AssertionError("Built-in class " + StandardNames.f15283u.c(name) + " is not found");
        }
    }

    /* compiled from: KotlinBuiltIns.java */
    /* renamed from: mb.h$d */
    /* loaded from: classes2.dex */
    class d implements ya.a<Void> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ x f15227e;

        d(x xVar) {
            this.f15227e = xVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void invoke() {
            if (KotlinBuiltIns.this.f15218a == null) {
                KotlinBuiltIns.this.f15218a = this.f15227e;
                return null;
            }
            throw new AssertionError("Built-ins module is already set: " + KotlinBuiltIns.this.f15218a + " (attempting to reset to " + this.f15227e + ")");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: KotlinBuiltIns.java */
    /* renamed from: mb.h$e */
    /* loaded from: classes2.dex */
    public static class e {

        /* renamed from: a, reason: collision with root package name */
        public final Map<PrimitiveType, o0> f15229a;

        /* renamed from: b, reason: collision with root package name */
        public final Map<g0, o0> f15230b;

        /* renamed from: c, reason: collision with root package name */
        public final Map<o0, o0> f15231c;

        /* synthetic */ e(Map map, Map map2, Map map3, a aVar) {
            this(map, map2, map3);
        }

        private static /* synthetic */ void a(int i10) {
            Object[] objArr = new Object[3];
            if (i10 == 1) {
                objArr[0] = "primitiveKotlinTypeToKotlinArrayType";
            } else if (i10 != 2) {
                objArr[0] = "primitiveTypeToArrayKotlinType";
            } else {
                objArr[0] = "kotlinArrayTypeToPrimitiveKotlinType";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/builtins/KotlinBuiltIns$Primitives";
            objArr[2] = "<init>";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        private e(Map<PrimitiveType, o0> map, Map<g0, o0> map2, Map<o0, o0> map3) {
            if (map == null) {
                a(0);
            }
            if (map2 == null) {
                a(1);
            }
            if (map3 == null) {
                a(2);
            }
            this.f15229a = map;
            this.f15230b = map2;
            this.f15231c = map3;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public KotlinBuiltIns(StorageManager storageManager) {
        if (storageManager == null) {
            a(0);
        }
        this.f15223f = storageManager;
        this.f15221d = storageManager.g(new a());
        this.f15220c = storageManager.g(new b());
        this.f15222e = storageManager.d(new c());
    }

    private static g0 A(g0 g0Var, ModuleDescriptor moduleDescriptor) {
        ClassId k10;
        ClassId a10;
        ClassDescriptor a11;
        if (g0Var == null) {
            a(71);
        }
        if (moduleDescriptor == null) {
            a(72);
        }
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (v7 == null) {
            return null;
        }
        o oVar = o.f15357a;
        if (!oVar.b(v7.getName()) || (k10 = wc.c.k(v7)) == null || (a10 = oVar.a(k10)) == null || (a11 = findClassInModule.a(moduleDescriptor, a10)) == null) {
            return null;
        }
        return a11.x();
    }

    public static boolean A0(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(10);
        }
        while (declarationDescriptor != null) {
            if (declarationDescriptor instanceof PackageFragmentDescriptor) {
                return ((PackageFragmentDescriptor) declarationDescriptor).d().i(StandardNames.f15282t);
            }
            declarationDescriptor = declarationDescriptor.b();
        }
        return false;
    }

    public static boolean B0(g0 g0Var) {
        if (g0Var == null) {
            a(142);
        }
        return l0(g0Var, StandardNames.a.f15299f);
    }

    public static boolean C0(g0 g0Var) {
        if (g0Var == null) {
            a(132);
        }
        return w0(g0Var) || z0(g0Var) || x0(g0Var) || y0(g0Var);
    }

    public static PrimitiveType N(g0 g0Var) {
        if (g0Var == null) {
            a(92);
        }
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (v7 == null) {
            return null;
        }
        return P(v7);
    }

    public static PrimitiveType P(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(77);
        }
        if (StandardNames.a.H0.contains(declarationDescriptor.getName())) {
            return StandardNames.a.J0.get(sc.e.m(declarationDescriptor));
        }
        return null;
    }

    private ClassDescriptor Q(PrimitiveType primitiveType) {
        if (primitiveType == null) {
            a(16);
        }
        return p(primitiveType.e().b());
    }

    public static PrimitiveType S(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(76);
        }
        if (StandardNames.a.G0.contains(declarationDescriptor.getName())) {
            return StandardNames.a.I0.get(sc.e.m(declarationDescriptor));
        }
        return null;
    }

    private static /* synthetic */ void a(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 13:
            case 15:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 74:
            case 81:
            case 84:
            case 86:
            case 87:
                str = "@NotNull method %s.%s must not return null";
                break;
            case 9:
            case 10:
            case 12:
            case 14:
            case 16:
            case 17:
            case 46:
            case 53:
            case 67:
            case 71:
            case 72:
            case 73:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 82:
            case 83:
            case 85:
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 13:
            case 15:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 74:
            case 81:
            case 84:
            case 86:
            case 87:
                i11 = 2;
                break;
            case 9:
            case 10:
            case 12:
            case 14:
            case 16:
            case 17:
            case 46:
            case 53:
            case 67:
            case 71:
            case 72:
            case 73:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 82:
            case 83:
            case 85:
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
            case 72:
                objArr[0] = "module";
                break;
            case 2:
                objArr[0] = "computation";
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 13:
            case 15:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 74:
            case 81:
            case 84:
            case 86:
            case 87:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/builtins/KotlinBuiltIns";
                break;
            case 9:
            case 10:
            case 76:
            case 77:
            case 89:
            case 96:
            case 103:
            case 107:
            case 108:
            case 143:
            case 146:
            case 147:
            case 149:
            case 157:
            case 158:
            case 159:
            case 160:
                objArr[0] = "descriptor";
                break;
            case 12:
            case 98:
            case 100:
            case 102:
            case 104:
            case 106:
            case 135:
                objArr[0] = "fqName";
                break;
            case 14:
                objArr[0] = "simpleName";
                break;
            case 16:
            case 17:
            case 53:
            case 88:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 97:
            case 99:
            case 105:
            case 109:
            case 110:
            case 111:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 144:
            case 145:
            case 148:
            case Geofence.MIN_RADIUS /* 150 */:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 162:
                objArr[0] = "type";
                break;
            case 46:
                objArr[0] = "classSimpleName";
                break;
            case 67:
                objArr[0] = "arrayType";
                break;
            case 71:
                objArr[0] = "notNullArrayType";
                break;
            case 73:
                objArr[0] = "primitiveType";
                break;
            case 75:
                objArr[0] = "kotlinType";
                break;
            case 78:
            case 82:
                objArr[0] = "projectionType";
                break;
            case 79:
            case 83:
            case 85:
                objArr[0] = "argument";
                break;
            case 80:
                objArr[0] = "annotations";
                break;
            case 101:
                objArr[0] = "typeConstructor";
                break;
            case 112:
                objArr[0] = "classDescriptor";
                break;
            case 161:
                objArr[0] = "declarationDescriptor";
                break;
            default:
                objArr[0] = "storageManager";
                break;
        }
        switch (i10) {
            case 3:
                objArr[1] = "getAdditionalClassPartsProvider";
                break;
            case 4:
                objArr[1] = "getPlatformDependentDeclarationFilter";
                break;
            case 5:
                objArr[1] = "getClassDescriptorFactories";
                break;
            case 6:
                objArr[1] = "getStorageManager";
                break;
            case 7:
                objArr[1] = "getBuiltInsModule";
                break;
            case 8:
                objArr[1] = "getBuiltInPackagesImportedByDefault";
                break;
            case 9:
            case 10:
            case 12:
            case 14:
            case 16:
            case 17:
            case 46:
            case 53:
            case 67:
            case 71:
            case 72:
            case 73:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 82:
            case 83:
            case 85:
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/builtins/KotlinBuiltIns";
                break;
            case 11:
                objArr[1] = "getBuiltInsPackageScope";
                break;
            case 13:
                objArr[1] = "getBuiltInClassByFqName";
                break;
            case 15:
                objArr[1] = "getBuiltInClassByName";
                break;
            case 18:
                objArr[1] = "getSuspendFunction";
                break;
            case 19:
                objArr[1] = "getKFunction";
                break;
            case 20:
                objArr[1] = "getKSuspendFunction";
                break;
            case 21:
                objArr[1] = "getKClass";
                break;
            case 22:
                objArr[1] = "getKCallable";
                break;
            case 23:
                objArr[1] = "getKProperty";
                break;
            case 24:
                objArr[1] = "getKProperty0";
                break;
            case 25:
                objArr[1] = "getKProperty1";
                break;
            case 26:
                objArr[1] = "getKProperty2";
                break;
            case 27:
                objArr[1] = "getKMutableProperty0";
                break;
            case 28:
                objArr[1] = "getKMutableProperty1";
                break;
            case 29:
                objArr[1] = "getKMutableProperty2";
                break;
            case 30:
                objArr[1] = "getIterator";
                break;
            case 31:
                objArr[1] = "getIterable";
                break;
            case 32:
                objArr[1] = "getMutableIterable";
                break;
            case 33:
                objArr[1] = "getMutableIterator";
                break;
            case 34:
                objArr[1] = "getCollection";
                break;
            case 35:
                objArr[1] = "getMutableCollection";
                break;
            case 36:
                objArr[1] = "getList";
                break;
            case 37:
                objArr[1] = "getMutableList";
                break;
            case 38:
                objArr[1] = "getSet";
                break;
            case 39:
                objArr[1] = "getMutableSet";
                break;
            case 40:
                objArr[1] = "getMap";
                break;
            case 41:
                objArr[1] = "getMutableMap";
                break;
            case 42:
                objArr[1] = "getMapEntry";
                break;
            case 43:
                objArr[1] = "getMutableMapEntry";
                break;
            case 44:
                objArr[1] = "getListIterator";
                break;
            case 45:
                objArr[1] = "getMutableListIterator";
                break;
            case 47:
                objArr[1] = "getBuiltInTypeByClassName";
                break;
            case 48:
                objArr[1] = "getNothingType";
                break;
            case 49:
                objArr[1] = "getNullableNothingType";
                break;
            case 50:
                objArr[1] = "getAnyType";
                break;
            case 51:
                objArr[1] = "getNullableAnyType";
                break;
            case 52:
                objArr[1] = "getDefaultBound";
                break;
            case 54:
                objArr[1] = "getPrimitiveKotlinType";
                break;
            case 55:
                objArr[1] = "getNumberType";
                break;
            case 56:
                objArr[1] = "getByteType";
                break;
            case 57:
                objArr[1] = "getShortType";
                break;
            case 58:
                objArr[1] = "getIntType";
                break;
            case 59:
                objArr[1] = "getLongType";
                break;
            case 60:
                objArr[1] = "getFloatType";
                break;
            case 61:
                objArr[1] = "getDoubleType";
                break;
            case 62:
                objArr[1] = "getCharType";
                break;
            case 63:
                objArr[1] = "getBooleanType";
                break;
            case 64:
                objArr[1] = "getUnitType";
                break;
            case 65:
                objArr[1] = "getStringType";
                break;
            case 66:
                objArr[1] = "getIterableType";
                break;
            case 68:
            case 69:
            case 70:
                objArr[1] = "getArrayElementType";
                break;
            case 74:
                objArr[1] = "getPrimitiveArrayKotlinType";
                break;
            case 81:
            case 84:
                objArr[1] = "getArrayType";
                break;
            case 86:
                objArr[1] = "getEnumType";
                break;
            case 87:
                objArr[1] = "getAnnotationType";
                break;
        }
        switch (i10) {
            case 1:
                objArr[2] = "setBuiltInsModule";
                break;
            case 2:
                objArr[2] = "setPostponedBuiltinsModuleComputation";
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 13:
            case 15:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 74:
            case 81:
            case 84:
            case 86:
            case 87:
                break;
            case 9:
                objArr[2] = "isBuiltIn";
                break;
            case 10:
                objArr[2] = "isUnderKotlinPackage";
                break;
            case 12:
                objArr[2] = "getBuiltInClassByFqName";
                break;
            case 14:
                objArr[2] = "getBuiltInClassByName";
                break;
            case 16:
                objArr[2] = "getPrimitiveClassDescriptor";
                break;
            case 17:
                objArr[2] = "getPrimitiveArrayClassDescriptor";
                break;
            case 46:
                objArr[2] = "getBuiltInTypeByClassName";
                break;
            case 53:
                objArr[2] = "getPrimitiveKotlinType";
                break;
            case 67:
                objArr[2] = "getArrayElementType";
                break;
            case 71:
            case 72:
                objArr[2] = "getElementTypeForUnsignedArray";
                break;
            case 73:
                objArr[2] = "getPrimitiveArrayKotlinType";
                break;
            case 75:
                objArr[2] = "getPrimitiveArrayKotlinTypeByPrimitiveKotlinType";
                break;
            case 76:
            case 93:
                objArr[2] = "getPrimitiveType";
                break;
            case 77:
                objArr[2] = "getPrimitiveArrayType";
                break;
            case 78:
            case 79:
            case 80:
            case 82:
            case 83:
                objArr[2] = "getArrayType";
                break;
            case 85:
                objArr[2] = "getEnumType";
                break;
            case 88:
                objArr[2] = "isArray";
                break;
            case 89:
            case 90:
                objArr[2] = "isArrayOrPrimitiveArray";
                break;
            case 91:
                objArr[2] = "isPrimitiveArray";
                break;
            case 92:
                objArr[2] = "getPrimitiveArrayElementType";
                break;
            case 94:
                objArr[2] = "isPrimitiveType";
                break;
            case 95:
                objArr[2] = "isPrimitiveTypeOrNullablePrimitiveType";
                break;
            case 96:
                objArr[2] = "isPrimitiveClass";
                break;
            case 97:
            case 98:
            case 99:
            case 100:
                objArr[2] = "isConstructedFromGivenClass";
                break;
            case 101:
            case 102:
                objArr[2] = "isTypeConstructorForGivenClass";
                break;
            case 103:
            case 104:
                objArr[2] = "classFqNameEquals";
                break;
            case 105:
            case 106:
                objArr[2] = "isNotNullConstructedFromGivenClass";
                break;
            case 107:
                objArr[2] = "isSpecialClassWithNoSupertypes";
                break;
            case 108:
            case 109:
                objArr[2] = "isAny";
                break;
            case 110:
            case 112:
                objArr[2] = "isBoolean";
                break;
            case 111:
                objArr[2] = "isBooleanOrNullableBoolean";
                break;
            case 113:
                objArr[2] = "isNumber";
                break;
            case 114:
                objArr[2] = "isChar";
                break;
            case 115:
                objArr[2] = "isCharOrNullableChar";
                break;
            case 116:
                objArr[2] = "isInt";
                break;
            case 117:
                objArr[2] = "isByte";
                break;
            case 118:
                objArr[2] = "isLong";
                break;
            case 119:
                objArr[2] = "isLongOrNullableLong";
                break;
            case 120:
                objArr[2] = "isShort";
                break;
            case 121:
                objArr[2] = "isFloat";
                break;
            case 122:
                objArr[2] = "isFloatOrNullableFloat";
                break;
            case 123:
                objArr[2] = "isDouble";
                break;
            case 124:
                objArr[2] = "isUByte";
                break;
            case 125:
                objArr[2] = "isUShort";
                break;
            case 126:
                objArr[2] = "isUInt";
                break;
            case 127:
                objArr[2] = "isULong";
                break;
            case 128:
                objArr[2] = "isUByteArray";
                break;
            case 129:
                objArr[2] = "isUShortArray";
                break;
            case 130:
                objArr[2] = "isUIntArray";
                break;
            case 131:
                objArr[2] = "isULongArray";
                break;
            case 132:
                objArr[2] = "isUnsignedArrayType";
                break;
            case 133:
                objArr[2] = "isDoubleOrNullableDouble";
                break;
            case 134:
            case 135:
                objArr[2] = "isConstructedFromGivenClassAndNotNullable";
                break;
            case 136:
                objArr[2] = "isNothing";
                break;
            case 137:
                objArr[2] = "isNullableNothing";
                break;
            case 138:
                objArr[2] = "isNothingOrNullableNothing";
                break;
            case 139:
                objArr[2] = "isAnyOrNullableAny";
                break;
            case 140:
                objArr[2] = "isNullableAny";
                break;
            case 141:
                objArr[2] = "isDefaultBound";
                break;
            case 142:
                objArr[2] = "isUnit";
                break;
            case 143:
                objArr[2] = "mayReturnNonUnitValue";
                break;
            case 144:
                objArr[2] = "isUnitOrNullableUnit";
                break;
            case 145:
                objArr[2] = "isBooleanOrSubtype";
                break;
            case 146:
                objArr[2] = "isMemberOfAny";
                break;
            case 147:
            case 148:
                objArr[2] = "isEnum";
                break;
            case 149:
            case Geofence.MIN_RADIUS /* 150 */:
                objArr[2] = "isComparable";
                break;
            case 151:
                objArr[2] = "isCollectionOrNullableCollection";
                break;
            case 152:
                objArr[2] = "isListOrNullableList";
                break;
            case 153:
                objArr[2] = "isSetOrNullableSet";
                break;
            case 154:
                objArr[2] = "isMapOrNullableMap";
                break;
            case 155:
                objArr[2] = "isIterableOrNullableIterable";
                break;
            case 156:
                objArr[2] = "isThrowableOrNullableThrowable";
                break;
            case 157:
                objArr[2] = "isThrowable";
                break;
            case 158:
                objArr[2] = "isKClass";
                break;
            case 159:
                objArr[2] = "isNonPrimitiveArray";
                break;
            case 160:
                objArr[2] = "isCloneable";
                break;
            case 161:
                objArr[2] = "isDeprecated";
                break;
            case 162:
                objArr[2] = "isNotNullOrNullableFunctionSupertype";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 13:
            case 15:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 74:
            case 81:
            case 84:
            case 86:
            case 87:
                throw new IllegalStateException(format);
            case 9:
            case 10:
            case 12:
            case 14:
            case 16:
            case 17:
            case 46:
            case 53:
            case 67:
            case 71:
            case 72:
            case 73:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 82:
            case 83:
            case 85:
            default:
                throw new IllegalArgumentException(format);
        }
    }

    public static boolean a0(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(108);
        }
        return e(classDescriptor, StandardNames.a.f15291b);
    }

    public static boolean b0(g0 g0Var) {
        if (g0Var == null) {
            a(139);
        }
        return g0(g0Var, StandardNames.a.f15291b);
    }

    public static boolean c0(g0 g0Var) {
        if (g0Var == null) {
            a(88);
        }
        return g0(g0Var, StandardNames.a.f15305i);
    }

    public static boolean d0(g0 g0Var) {
        if (g0Var == null) {
            a(90);
        }
        return c0(g0Var) || p0(g0Var);
    }

    private static boolean e(ClassifierDescriptor classifierDescriptor, FqNameUnsafe fqNameUnsafe) {
        if (classifierDescriptor == null) {
            a(103);
        }
        if (fqNameUnsafe == null) {
            a(104);
        }
        return classifierDescriptor.getName().equals(fqNameUnsafe.i()) && fqNameUnsafe.equals(sc.e.m(classifierDescriptor));
    }

    public static boolean e0(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(89);
        }
        return e(classDescriptor, StandardNames.a.f15305i) || P(classDescriptor) != null;
    }

    public static boolean f0(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(9);
        }
        return sc.e.r(declarationDescriptor, BuiltInsPackageFragment.class, false) != null;
    }

    private static boolean g0(g0 g0Var, FqNameUnsafe fqNameUnsafe) {
        if (g0Var == null) {
            a(97);
        }
        if (fqNameUnsafe == null) {
            a(98);
        }
        return v0(g0Var.W0(), fqNameUnsafe);
    }

    private static boolean h0(g0 g0Var, FqNameUnsafe fqNameUnsafe) {
        if (g0Var == null) {
            a(134);
        }
        if (fqNameUnsafe == null) {
            a(135);
        }
        return g0(g0Var, fqNameUnsafe) && !g0Var.X0();
    }

    public static boolean i0(g0 g0Var) {
        if (g0Var == null) {
            a(141);
        }
        return o0(g0Var);
    }

    public static boolean j0(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(161);
        }
        if (declarationDescriptor.a().i().a(StandardNames.a.f15337y)) {
            return true;
        }
        if (!(declarationDescriptor instanceof PropertyDescriptor)) {
            return false;
        }
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor) declarationDescriptor;
        boolean p02 = propertyDescriptor.p0();
        PropertyGetterDescriptor h10 = propertyDescriptor.h();
        PropertySetterDescriptor k10 = propertyDescriptor.k();
        if (h10 != null && j0(h10)) {
            if (!p02) {
                return true;
            }
            if (k10 != null && j0(k10)) {
                return true;
            }
        }
        return false;
    }

    public static boolean k0(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(158);
        }
        return e(classDescriptor, StandardNames.a.f15306i0);
    }

    private static boolean l0(g0 g0Var, FqNameUnsafe fqNameUnsafe) {
        if (g0Var == null) {
            a(105);
        }
        if (fqNameUnsafe == null) {
            a(106);
        }
        return !g0Var.X0() && g0(g0Var, fqNameUnsafe);
    }

    public static boolean m0(g0 g0Var) {
        if (g0Var == null) {
            a(136);
        }
        return n0(g0Var) && !s1.l(g0Var);
    }

    public static boolean n0(g0 g0Var) {
        if (g0Var == null) {
            a(138);
        }
        return g0(g0Var, StandardNames.a.f15293c);
    }

    public static boolean o0(g0 g0Var) {
        if (g0Var == null) {
            a(140);
        }
        return b0(g0Var) && g0Var.X0();
    }

    private ClassDescriptor p(String str) {
        if (str == null) {
            a(14);
        }
        ClassDescriptor invoke = this.f15222e.invoke(Name.f(str));
        if (invoke == null) {
            a(15);
        }
        return invoke;
    }

    public static boolean p0(g0 g0Var) {
        if (g0Var == null) {
            a(91);
        }
        ClassifierDescriptor v7 = g0Var.W0().v();
        return (v7 == null || P(v7) == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public o0 q(String str) {
        if (str == null) {
            a(46);
        }
        o0 x10 = p(str).x();
        if (x10 == null) {
            a(47);
        }
        return x10;
    }

    public static boolean q0(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(96);
        }
        return S(classDescriptor) != null;
    }

    public static boolean r0(g0 g0Var) {
        if (g0Var == null) {
            a(94);
        }
        return !g0Var.X0() && s0(g0Var);
    }

    public static boolean s0(g0 g0Var) {
        if (g0Var == null) {
            a(95);
        }
        ClassifierDescriptor v7 = g0Var.W0().v();
        return (v7 instanceof ClassDescriptor) && q0((ClassDescriptor) v7);
    }

    public static boolean t0(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(107);
        }
        return e(classDescriptor, StandardNames.a.f15291b) || e(classDescriptor, StandardNames.a.f15293c);
    }

    public static boolean u0(g0 g0Var) {
        return g0Var != null && l0(g0Var, StandardNames.a.f15303h);
    }

    public static boolean v0(TypeConstructor typeConstructor, FqNameUnsafe fqNameUnsafe) {
        if (typeConstructor == null) {
            a(101);
        }
        if (fqNameUnsafe == null) {
            a(102);
        }
        ClassifierDescriptor v7 = typeConstructor.v();
        return (v7 instanceof ClassDescriptor) && e(v7, fqNameUnsafe);
    }

    public static boolean w0(g0 g0Var) {
        if (g0Var == null) {
            a(128);
        }
        return h0(g0Var, StandardNames.a.C0.j());
    }

    public static boolean x0(g0 g0Var) {
        if (g0Var == null) {
            a(130);
        }
        return h0(g0Var, StandardNames.a.E0.j());
    }

    public static boolean y0(g0 g0Var) {
        if (g0Var == null) {
            a(131);
        }
        return h0(g0Var, StandardNames.a.F0.j());
    }

    public static boolean z0(g0 g0Var) {
        if (g0Var == null) {
            a(129);
        }
        return h0(g0Var, StandardNames.a.D0.j());
    }

    public o0 B() {
        o0 R = R(PrimitiveType.FLOAT);
        if (R == null) {
            a(60);
        }
        return R;
    }

    public ClassDescriptor C(int i10) {
        return p(StandardNames.b(i10));
    }

    public o0 D() {
        o0 R = R(PrimitiveType.INT);
        if (R == null) {
            a(58);
        }
        return R;
    }

    public void D0(x xVar) {
        if (xVar == null) {
            a(1);
        }
        this.f15223f.i(new d(xVar));
    }

    public ClassDescriptor E() {
        ClassDescriptor o10 = o(StandardNames.a.f15306i0.l());
        if (o10 == null) {
            a(21);
        }
        return o10;
    }

    public o0 F() {
        o0 R = R(PrimitiveType.LONG);
        if (R == null) {
            a(59);
        }
        return R;
    }

    public ClassDescriptor G() {
        return p("Nothing");
    }

    public o0 H() {
        o0 x10 = G().x();
        if (x10 == null) {
            a(48);
        }
        return x10;
    }

    public o0 I() {
        o0 a12 = i().a1(true);
        if (a12 == null) {
            a(51);
        }
        return a12;
    }

    public o0 J() {
        o0 a12 = H().a1(true);
        if (a12 == null) {
            a(49);
        }
        return a12;
    }

    public ClassDescriptor K() {
        return p("Number");
    }

    public o0 L() {
        o0 x10 = K().x();
        if (x10 == null) {
            a(55);
        }
        return x10;
    }

    protected rb.c M() {
        c.b bVar = c.b.f17690a;
        if (bVar == null) {
            a(4);
        }
        return bVar;
    }

    public o0 O(PrimitiveType primitiveType) {
        if (primitiveType == null) {
            a(73);
        }
        o0 o0Var = this.f15220c.invoke().f15229a.get(primitiveType);
        if (o0Var == null) {
            a(74);
        }
        return o0Var;
    }

    public o0 R(PrimitiveType primitiveType) {
        if (primitiveType == null) {
            a(53);
        }
        o0 x10 = Q(primitiveType).x();
        if (x10 == null) {
            a(54);
        }
        return x10;
    }

    public o0 T() {
        o0 R = R(PrimitiveType.SHORT);
        if (R == null) {
            a(57);
        }
        return R;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public StorageManager U() {
        StorageManager storageManager = this.f15223f;
        if (storageManager == null) {
            a(6);
        }
        return storageManager;
    }

    public ClassDescriptor V() {
        return p("String");
    }

    public o0 W() {
        o0 x10 = V().x();
        if (x10 == null) {
            a(65);
        }
        return x10;
    }

    public ClassDescriptor X(int i10) {
        ClassDescriptor o10 = o(StandardNames.f15275m.c(Name.f(StandardNames.d(i10))));
        if (o10 == null) {
            a(18);
        }
        return o10;
    }

    public ClassDescriptor Y() {
        return p("Unit");
    }

    public o0 Z() {
        o0 x10 = Y().x();
        if (x10 == null) {
            a(64);
        }
        return x10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void f(boolean z10) {
        x xVar = new x(f15217g, this.f15223f, this, null);
        this.f15218a = xVar;
        xVar.b1(BuiltInsLoader.f15209a.a().a(this.f15223f, this.f15218a, v(), M(), g(), z10));
        x xVar2 = this.f15218a;
        xVar2.h1(xVar2);
    }

    protected AdditionalClassPartsProvider g() {
        AdditionalClassPartsProvider.a aVar = AdditionalClassPartsProvider.a.f17688a;
        if (aVar == null) {
            a(3);
        }
        return aVar;
    }

    public ClassDescriptor h() {
        return p("Any");
    }

    public o0 i() {
        o0 x10 = h().x();
        if (x10 == null) {
            a(50);
        }
        return x10;
    }

    public ClassDescriptor j() {
        return p("Array");
    }

    public g0 k(g0 g0Var) {
        g0 A;
        if (g0Var == null) {
            a(67);
        }
        if (c0(g0Var)) {
            if (g0Var.U0().size() == 1) {
                g0 type = g0Var.U0().get(0).getType();
                if (type == null) {
                    a(68);
                }
                return type;
            }
            throw new IllegalStateException();
        }
        g0 n10 = s1.n(g0Var);
        o0 o0Var = this.f15220c.invoke().f15231c.get(n10);
        if (o0Var != null) {
            return o0Var;
        }
        ModuleDescriptor h10 = sc.e.h(n10);
        if (h10 != null && (A = A(n10, h10)) != null) {
            return A;
        }
        throw new IllegalStateException("not array: " + g0Var);
    }

    public o0 l(Variance variance, g0 g0Var) {
        if (variance == null) {
            a(82);
        }
        if (g0Var == null) {
            a(83);
        }
        o0 m10 = m(variance, g0Var, qb.g.f17195b.b());
        if (m10 == null) {
            a(84);
        }
        return m10;
    }

    public o0 m(Variance variance, g0 g0Var, qb.g gVar) {
        if (variance == null) {
            a(78);
        }
        if (g0Var == null) {
            a(79);
        }
        if (gVar == null) {
            a(80);
        }
        o0 g6 = h0.g(d1.b(gVar), j(), Collections.singletonList(new TypeProjectionImpl(variance, g0Var)));
        if (g6 == null) {
            a(81);
        }
        return g6;
    }

    public o0 n() {
        o0 R = R(PrimitiveType.BOOLEAN);
        if (R == null) {
            a(63);
        }
        return R;
    }

    public ClassDescriptor o(FqName fqName) {
        if (fqName == null) {
            a(12);
        }
        ClassDescriptor c10 = s.c(r(), fqName, xb.d.FROM_BUILTINS);
        if (c10 == null) {
            a(13);
        }
        return c10;
    }

    public x r() {
        if (this.f15218a == null) {
            this.f15218a = this.f15219b.invoke();
        }
        x xVar = this.f15218a;
        if (xVar == null) {
            a(7);
        }
        return xVar;
    }

    public zc.h s() {
        zc.h u7 = r().H(StandardNames.f15283u).u();
        if (u7 == null) {
            a(11);
        }
        return u7;
    }

    public o0 t() {
        o0 R = R(PrimitiveType.BYTE);
        if (R == null) {
            a(56);
        }
        return R;
    }

    public o0 u() {
        o0 R = R(PrimitiveType.CHAR);
        if (R == null) {
            a(62);
        }
        return R;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Iterable<ClassDescriptorFactory> v() {
        List singletonList = Collections.singletonList(new BuiltInFictitiousFunctionClassFactory(this.f15223f, r()));
        if (singletonList == null) {
            a(5);
        }
        return singletonList;
    }

    public ClassDescriptor w() {
        ClassDescriptor o10 = o(StandardNames.a.U);
        if (o10 == null) {
            a(34);
        }
        return o10;
    }

    public ClassDescriptor x() {
        return p("Comparable");
    }

    public o0 y() {
        o0 I = I();
        if (I == null) {
            a(52);
        }
        return I;
    }

    public o0 z() {
        o0 R = R(PrimitiveType.DOUBLE);
        if (R == null) {
            a(61);
        }
        return R;
    }
}
