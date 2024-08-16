package jb;

import cd.DeserializedArrayValue;
import cd.MemberDeserializer;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gb.KCallable;
import gb.KType;
import gd.TypeProjection;
import hc.KotlinJvmBinaryClass;
import hc.KotlinJvmBinarySourceElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.MutableCollections;
import kotlin.collections._ArraysJvm;
import kotlin.collections._Collections;
import lc.BinaryVersion;
import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import ma.NoWhenBranchMatchedException;
import mb.KotlinBuiltIns;
import mb.PrimitiveType;
import ob.JavaToKotlinClassMap;
import oc.ClassId;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;
import pb.CallableDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.ModuleDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SourceElement;
import qb.AnnotationDescriptor;
import sc.inlineClassesUtils;
import sd.StringsJVM;
import ub.ReflectAnnotationSource;
import ub.RuntimeModuleData;
import ub.RuntimeSourceElementFactory;
import uc.q;
import vb.ReflectJavaAnnotation;
import vb.ReflectJavaElement;
import vb.reflectClassUtil;
import xa.JvmClassMapping;
import za.FunctionReference;
import za.PropertyReference;
import za.RepeatableContainer;

/* compiled from: util.kt */
/* loaded from: classes2.dex */
public final class o0 {

    /* renamed from: a, reason: collision with root package name */
    private static final FqName f13305a = new FqName("kotlin.jvm.JvmStatic");

    /* compiled from: util.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f13306a;

        static {
            int[] iArr = new int[PrimitiveType.values().length];
            try {
                iArr[PrimitiveType.BOOLEAN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[PrimitiveType.CHAR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[PrimitiveType.BYTE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[PrimitiveType.SHORT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[PrimitiveType.INT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[PrimitiveType.FLOAT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[PrimitiveType.LONG.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr[PrimitiveType.DOUBLE.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            f13306a = iArr;
        }
    }

    /* JADX WARN: Incorrect type for immutable var: ssa=boolean[], code=short[], for r7v12, types: [boolean[]] */
    /* JADX WARN: Incorrect type for immutable var: ssa=byte[], code=short[], for r7v14, types: [byte[]] */
    /* JADX WARN: Incorrect type for immutable var: ssa=char[], code=short[], for r7v13, types: [char[]] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v10, types: [java.lang.Class[]] */
    /* JADX WARN: Type inference failed for: r7v11, types: [java.lang.String[]] */
    /* JADX WARN: Type inference failed for: r7v16, types: [int[]] */
    /* JADX WARN: Type inference failed for: r7v17, types: [float[]] */
    /* JADX WARN: Type inference failed for: r7v18, types: [long[]] */
    /* JADX WARN: Type inference failed for: r7v20, types: [double[]] */
    /* JADX WARN: Type inference failed for: r7v9, types: [java.lang.Object[]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static final Object a(uc.b bVar, ClassLoader classLoader) {
        gd.g0 c10;
        int u7;
        Object q02;
        Class n10;
        short[] sArr;
        DeserializedArrayValue deserializedArrayValue = bVar instanceof DeserializedArrayValue ? (DeserializedArrayValue) bVar : null;
        if (deserializedArrayValue == null || (c10 = deserializedArrayValue.c()) == null) {
            return null;
        }
        List<? extends uc.g<?>> b10 = bVar.b();
        u7 = kotlin.collections.s.u(b10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = b10.iterator();
        while (it.hasNext()) {
            arrayList.add(q((uc.g) it.next(), classLoader));
        }
        PrimitiveType N = KotlinBuiltIns.N(c10);
        int i10 = 0;
        switch (N == null ? -1 : a.f13306a[N.ordinal()]) {
            case -1:
                if (KotlinBuiltIns.c0(c10)) {
                    q02 = _Collections.q0(c10.U0());
                    gd.g0 type = ((TypeProjection) q02).getType();
                    za.k.d(type, "type.arguments.single().type");
                    ClassifierDescriptor v7 = type.W0().v();
                    ClassDescriptor classDescriptor = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
                    if (classDescriptor != null) {
                        if (KotlinBuiltIns.u0(type)) {
                            int size = bVar.b().size();
                            sArr = new String[size];
                            while (i10 < size) {
                                Object obj = arrayList.get(i10);
                                za.k.c(obj, "null cannot be cast to non-null type kotlin.String");
                                sArr[i10] = (String) obj;
                                i10++;
                            }
                        } else if (KotlinBuiltIns.k0(classDescriptor)) {
                            int size2 = bVar.b().size();
                            sArr = new Class[size2];
                            while (i10 < size2) {
                                Object obj2 = arrayList.get(i10);
                                za.k.c(obj2, "null cannot be cast to non-null type java.lang.Class<*>");
                                sArr[i10] = (Class) obj2;
                                i10++;
                            }
                        } else {
                            ClassId k10 = wc.c.k(classDescriptor);
                            if (k10 == null || (n10 = n(classLoader, k10, 0, 4, null)) == null) {
                                return null;
                            }
                            Object newInstance = Array.newInstance((Class<?>) n10, bVar.b().size());
                            za.k.c(newInstance, "null cannot be cast to non-null type kotlin.Array<in kotlin.Any?>");
                            sArr = (Object[]) newInstance;
                            int size3 = arrayList.size();
                            while (i10 < size3) {
                                sArr[i10] = arrayList.get(i10);
                                i10++;
                            }
                        }
                        return sArr;
                    }
                    throw new IllegalStateException(("Not a class type: " + type).toString());
                }
                throw new IllegalStateException(("Not an array type: " + c10).toString());
            case 0:
            default:
                throw new NoWhenBranchMatchedException();
            case 1:
                int size4 = bVar.b().size();
                sArr = new boolean[size4];
                while (i10 < size4) {
                    Object obj3 = arrayList.get(i10);
                    za.k.c(obj3, "null cannot be cast to non-null type kotlin.Boolean");
                    sArr[i10] = ((Boolean) obj3).booleanValue();
                    i10++;
                }
                return sArr;
            case 2:
                int size5 = bVar.b().size();
                sArr = new char[size5];
                while (i10 < size5) {
                    Object obj4 = arrayList.get(i10);
                    za.k.c(obj4, "null cannot be cast to non-null type kotlin.Char");
                    sArr[i10] = ((Character) obj4).charValue();
                    i10++;
                }
                return sArr;
            case 3:
                int size6 = bVar.b().size();
                sArr = new byte[size6];
                while (i10 < size6) {
                    Object obj5 = arrayList.get(i10);
                    za.k.c(obj5, "null cannot be cast to non-null type kotlin.Byte");
                    sArr[i10] = ((Byte) obj5).byteValue();
                    i10++;
                }
                return sArr;
            case 4:
                int size7 = bVar.b().size();
                sArr = new short[size7];
                while (i10 < size7) {
                    Object obj6 = arrayList.get(i10);
                    za.k.c(obj6, "null cannot be cast to non-null type kotlin.Short");
                    sArr[i10] = ((Short) obj6).shortValue();
                    i10++;
                }
                return sArr;
            case 5:
                int size8 = bVar.b().size();
                sArr = new int[size8];
                while (i10 < size8) {
                    Object obj7 = arrayList.get(i10);
                    za.k.c(obj7, "null cannot be cast to non-null type kotlin.Int");
                    sArr[i10] = ((Integer) obj7).intValue();
                    i10++;
                }
                return sArr;
            case 6:
                int size9 = bVar.b().size();
                sArr = new float[size9];
                while (i10 < size9) {
                    Object obj8 = arrayList.get(i10);
                    za.k.c(obj8, "null cannot be cast to non-null type kotlin.Float");
                    sArr[i10] = ((Float) obj8).floatValue();
                    i10++;
                }
                return sArr;
            case 7:
                int size10 = bVar.b().size();
                sArr = new long[size10];
                while (i10 < size10) {
                    Object obj9 = arrayList.get(i10);
                    za.k.c(obj9, "null cannot be cast to non-null type kotlin.Long");
                    sArr[i10] = ((Long) obj9).longValue();
                    i10++;
                }
                return sArr;
            case 8:
                int size11 = bVar.b().size();
                sArr = new double[size11];
                while (i10 < size11) {
                    Object obj10 = arrayList.get(i10);
                    za.k.c(obj10, "null cannot be cast to non-null type kotlin.Double");
                    sArr[i10] = ((Double) obj10).doubleValue();
                    i10++;
                }
                return sArr;
        }
    }

    public static final KCallableImpl<?> b(Object obj) {
        KCallableImpl<?> kCallableImpl = obj instanceof KCallableImpl ? (KCallableImpl) obj : null;
        if (kCallableImpl != null) {
            return kCallableImpl;
        }
        KFunctionImpl c10 = c(obj);
        return c10 != null ? c10 : d(obj);
    }

    public static final KFunctionImpl c(Object obj) {
        KFunctionImpl kFunctionImpl = obj instanceof KFunctionImpl ? (KFunctionImpl) obj : null;
        if (kFunctionImpl != null) {
            return kFunctionImpl;
        }
        FunctionReference functionReference = obj instanceof FunctionReference ? (FunctionReference) obj : null;
        KCallable s7 = functionReference != null ? functionReference.s() : null;
        if (s7 instanceof KFunctionImpl) {
            return (KFunctionImpl) s7;
        }
        return null;
    }

    public static final b0<?> d(Object obj) {
        b0<?> b0Var = obj instanceof b0 ? (b0) obj : null;
        if (b0Var != null) {
            return b0Var;
        }
        PropertyReference propertyReference = obj instanceof PropertyReference ? (PropertyReference) obj : null;
        KCallable s7 = propertyReference != null ? propertyReference.s() : null;
        if (s7 instanceof b0) {
            return (b0) s7;
        }
        return null;
    }

    public static final List<Annotation> e(qb.a aVar) {
        za.k.e(aVar, "<this>");
        qb.g i10 = aVar.i();
        ArrayList arrayList = new ArrayList();
        for (AnnotationDescriptor annotationDescriptor : i10) {
            SourceElement z10 = annotationDescriptor.z();
            Annotation annotation = null;
            if (z10 instanceof ReflectAnnotationSource) {
                annotation = ((ReflectAnnotationSource) z10).d();
            } else if (z10 instanceof RuntimeSourceElementFactory.a) {
                ReflectJavaElement b10 = ((RuntimeSourceElementFactory.a) z10).b();
                ReflectJavaAnnotation reflectJavaAnnotation = b10 instanceof ReflectJavaAnnotation ? (ReflectJavaAnnotation) b10 : null;
                if (reflectJavaAnnotation != null) {
                    annotation = reflectJavaAnnotation.W();
                }
            } else {
                annotation = o(annotationDescriptor);
            }
            if (annotation != null) {
                arrayList.add(annotation);
            }
        }
        return r(arrayList);
    }

    public static final Class<?> f(Class<?> cls) {
        za.k.e(cls, "<this>");
        return Array.newInstance(cls, 0).getClass();
    }

    public static final Object g(Type type) {
        za.k.e(type, "type");
        if (!(type instanceof Class) || !((Class) type).isPrimitive()) {
            return null;
        }
        if (za.k.a(type, Boolean.TYPE)) {
            return Boolean.FALSE;
        }
        if (za.k.a(type, Character.TYPE)) {
            return (char) 0;
        }
        if (za.k.a(type, Byte.TYPE)) {
            return (byte) 0;
        }
        if (za.k.a(type, Short.TYPE)) {
            return (short) 0;
        }
        if (za.k.a(type, Integer.TYPE)) {
            return 0;
        }
        if (za.k.a(type, Float.TYPE)) {
            return Float.valueOf(0.0f);
        }
        if (za.k.a(type, Long.TYPE)) {
            return 0L;
        }
        if (za.k.a(type, Double.TYPE)) {
            return Double.valueOf(UserProfileInfo.Constant.NA_LAT_LON);
        }
        if (za.k.a(type, Void.TYPE)) {
            throw new IllegalStateException("Parameter with void type is illegal");
        }
        throw new UnsupportedOperationException("Unknown primitive: " + type);
    }

    public static final <M extends qc.q, D extends CallableDescriptor> D h(Class<?> cls, M m10, NameResolver nameResolver, TypeTable typeTable, BinaryVersion binaryVersion, ya.p<? super MemberDeserializer, ? super M, ? extends D> pVar) {
        List<jc.s> h02;
        za.k.e(cls, "moduleAnchor");
        za.k.e(m10, "proto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(typeTable, "typeTable");
        za.k.e(binaryVersion, "metadataVersion");
        za.k.e(pVar, "createDescriptor");
        RuntimeModuleData a10 = h0.a(cls);
        if (m10 instanceof jc.i) {
            h02 = ((jc.i) m10).g0();
        } else {
            if (!(m10 instanceof jc.n)) {
                throw new IllegalStateException(("Unsupported message: " + m10).toString());
            }
            h02 = ((jc.n) m10).h0();
        }
        List<jc.s> list = h02;
        cd.k a11 = a10.a();
        ModuleDescriptor b10 = a10.b();
        VersionRequirement b11 = VersionRequirement.f14699b.b();
        za.k.d(list, "typeParameters");
        return pVar.invoke(new MemberDeserializer(new cd.m(a11, nameResolver, b10, typeTable, b11, binaryVersion, null, null, list)), m10);
    }

    public static final ReceiverParameterDescriptor i(CallableDescriptor callableDescriptor) {
        za.k.e(callableDescriptor, "<this>");
        if (callableDescriptor.m0() == null) {
            return null;
        }
        DeclarationDescriptor b10 = callableDescriptor.b();
        za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
        return ((ClassDescriptor) b10).S0();
    }

    public static final FqName j() {
        return f13305a;
    }

    public static final boolean k(KType kType) {
        gd.g0 m10;
        za.k.e(kType, "<this>");
        KTypeImpl kTypeImpl = kType instanceof KTypeImpl ? (KTypeImpl) kType : null;
        return (kTypeImpl == null || (m10 = kTypeImpl.m()) == null || !inlineClassesUtils.c(m10)) ? false : true;
    }

    private static final Class<?> l(ClassLoader classLoader, String str, String str2, int i10) {
        String y4;
        String v7;
        if (za.k.a(str, "kotlin")) {
            switch (str2.hashCode()) {
                case -901856463:
                    if (str2.equals("BooleanArray")) {
                        return boolean[].class;
                    }
                    break;
                case -763279523:
                    if (str2.equals("ShortArray")) {
                        return short[].class;
                    }
                    break;
                case -755911549:
                    if (str2.equals("CharArray")) {
                        return char[].class;
                    }
                    break;
                case -74930671:
                    if (str2.equals("ByteArray")) {
                        return byte[].class;
                    }
                    break;
                case 22374632:
                    if (str2.equals("DoubleArray")) {
                        return double[].class;
                    }
                    break;
                case 63537721:
                    if (str2.equals("Array")) {
                        return Object[].class;
                    }
                    break;
                case 601811914:
                    if (str2.equals("IntArray")) {
                        return int[].class;
                    }
                    break;
                case 948852093:
                    if (str2.equals("FloatArray")) {
                        return float[].class;
                    }
                    break;
                case 2104330525:
                    if (str2.equals("LongArray")) {
                        return long[].class;
                    }
                    break;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append('.');
        y4 = StringsJVM.y(str2, '.', '$', false, 4, null);
        sb2.append(y4);
        String sb3 = sb2.toString();
        if (i10 > 0) {
            StringBuilder sb4 = new StringBuilder();
            v7 = StringsJVM.v("[", i10);
            sb4.append(v7);
            sb4.append('L');
            sb4.append(sb3);
            sb4.append(';');
            sb3 = sb4.toString();
        }
        return ub.e.a(classLoader, sb3);
    }

    private static final Class<?> m(ClassLoader classLoader, ClassId classId, int i10) {
        JavaToKotlinClassMap javaToKotlinClassMap = JavaToKotlinClassMap.f16339a;
        FqNameUnsafe j10 = classId.b().j();
        za.k.d(j10, "kotlinClassId.asSingleFqName().toUnsafe()");
        ClassId n10 = javaToKotlinClassMap.n(j10);
        if (n10 != null) {
            classId = n10;
        }
        String b10 = classId.h().b();
        za.k.d(b10, "javaClassId.packageFqName.asString()");
        String b11 = classId.i().b();
        za.k.d(b11, "javaClassId.relativeClassName.asString()");
        return l(classLoader, b10, b11, i10);
    }

    static /* synthetic */ Class n(ClassLoader classLoader, ClassId classId, int i10, int i11, Object obj) {
        if ((i11 & 4) != 0) {
            i10 = 0;
        }
        return m(classLoader, classId, i10);
    }

    private static final Annotation o(AnnotationDescriptor annotationDescriptor) {
        Map q10;
        ClassDescriptor i10 = wc.c.i(annotationDescriptor);
        Class<?> p10 = i10 != null ? p(i10) : null;
        if (!(p10 instanceof Class)) {
            p10 = null;
        }
        if (p10 == null) {
            return null;
        }
        Set<Map.Entry<Name, uc.g<?>>> entrySet = annotationDescriptor.a().entrySet();
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Name name = (Name) entry.getKey();
            uc.g gVar = (uc.g) entry.getValue();
            ClassLoader classLoader = p10.getClassLoader();
            za.k.d(classLoader, "annotationClass.classLoader");
            Object q11 = q(gVar, classLoader);
            ma.o a10 = q11 != null ? ma.u.a(name.b(), q11) : null;
            if (a10 != null) {
                arrayList.add(a10);
            }
        }
        q10 = kotlin.collections.m0.q(arrayList);
        return (Annotation) kb.c.e(p10, q10, null, 4, null);
    }

    public static final Class<?> p(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "<this>");
        SourceElement z10 = classDescriptor.z();
        za.k.d(z10, "source");
        if (z10 instanceof KotlinJvmBinarySourceElement) {
            KotlinJvmBinaryClass d10 = ((KotlinJvmBinarySourceElement) z10).d();
            za.k.c(d10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.runtime.components.ReflectKotlinClass");
            return ((ub.f) d10).f();
        }
        if (z10 instanceof RuntimeSourceElementFactory.a) {
            ReflectJavaElement b10 = ((RuntimeSourceElementFactory.a) z10).b();
            za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.runtime.structure.ReflectJavaClass");
            return ((vb.l) b10).D();
        }
        ClassId k10 = wc.c.k(classDescriptor);
        if (k10 == null) {
            return null;
        }
        return m(reflectClassUtil.f(classDescriptor.getClass()), k10, 0);
    }

    private static final Object q(uc.g<?> gVar, ClassLoader classLoader) {
        if (gVar instanceof uc.a) {
            return o(((uc.a) gVar).b());
        }
        if (gVar instanceof uc.b) {
            return a((uc.b) gVar, classLoader);
        }
        if (gVar instanceof uc.j) {
            ma.o<? extends ClassId, ? extends Name> b10 = ((uc.j) gVar).b();
            ClassId a10 = b10.a();
            Name b11 = b10.b();
            Class n10 = n(classLoader, a10, 0, 4, null);
            if (n10 != null) {
                return n0.a(n10, b11.b());
            }
            return null;
        }
        if (gVar instanceof uc.q) {
            q.b b12 = ((uc.q) gVar).b();
            if (b12 instanceof q.b.C0110b) {
                q.b.C0110b c0110b = (q.b.C0110b) b12;
                return m(classLoader, c0110b.b(), c0110b.a());
            }
            if (!(b12 instanceof q.b.a)) {
                throw new NoWhenBranchMatchedException();
            }
            ClassifierDescriptor v7 = ((q.b.a) b12).a().W0().v();
            ClassDescriptor classDescriptor = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
            if (classDescriptor != null) {
                return p(classDescriptor);
            }
            return null;
        }
        if (gVar instanceof uc.k ? true : gVar instanceof uc.s) {
            return null;
        }
        return gVar.b();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final List<Annotation> r(List<? extends Annotation> list) {
        boolean z10;
        List e10;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (za.k.a(JvmClassMapping.b(JvmClassMapping.a((Annotation) it.next())).getSimpleName(), "Container")) {
                    z10 = true;
                    break;
                }
            }
        }
        z10 = false;
        if (!z10) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (Annotation annotation : list) {
            Class b10 = JvmClassMapping.b(JvmClassMapping.a(annotation));
            if (za.k.a(b10.getSimpleName(), "Container") && b10.getAnnotation(RepeatableContainer.class) != null) {
                Object invoke = b10.getDeclaredMethod(ThermalBaseConfig.Item.ATTR_VALUE, new Class[0]).invoke(annotation, new Object[0]);
                za.k.c(invoke, "null cannot be cast to non-null type kotlin.Array<out kotlin.Annotation>");
                e10 = _ArraysJvm.e((Annotation[]) invoke);
            } else {
                e10 = CollectionsJVM.e(annotation);
            }
            MutableCollections.z(arrayList, e10);
        }
        return arrayList;
    }
}
