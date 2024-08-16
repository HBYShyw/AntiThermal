package ub;

import com.oplus.backup.sdk.common.utils.Constants;
import hc.KotlinJvmBinaryClass;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import kotlin.collections._Arrays;
import mb.PrimitiveType;
import mb.StandardNames;
import ob.JavaToKotlinClassMap;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import oc.SpecialNames;
import uc.ClassLiteralValue;
import vb.reflectClassUtil;
import xa.JvmClassMapping;
import xc.JvmPrimitiveType;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ReflectKotlinClass.kt */
/* loaded from: classes2.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    public static final c f18972a = new c();

    private c() {
    }

    private final ClassLiteralValue a(Class<?> cls) {
        int i10 = 0;
        while (cls.isArray()) {
            i10++;
            cls = cls.getComponentType();
            za.k.d(cls, "currentClass.componentType");
        }
        if (cls.isPrimitive()) {
            if (za.k.a(cls, Void.TYPE)) {
                ClassId m10 = ClassId.m(StandardNames.a.f15299f.l());
                za.k.d(m10, "topLevel(StandardNames.FqNames.unit.toSafe())");
                return new ClassLiteralValue(m10, i10);
            }
            PrimitiveType f10 = JvmPrimitiveType.b(cls.getName()).f();
            za.k.d(f10, "get(currentClass.name).primitiveType");
            if (i10 > 0) {
                ClassId m11 = ClassId.m(f10.b());
                za.k.d(m11, "topLevel(primitiveType.arrayTypeFqName)");
                return new ClassLiteralValue(m11, i10 - 1);
            }
            ClassId m12 = ClassId.m(f10.d());
            za.k.d(m12, "topLevel(primitiveType.typeFqName)");
            return new ClassLiteralValue(m12, i10);
        }
        ClassId a10 = reflectClassUtil.a(cls);
        JavaToKotlinClassMap javaToKotlinClassMap = JavaToKotlinClassMap.f16339a;
        FqName b10 = a10.b();
        za.k.d(b10, "javaClassId.asSingleFqName()");
        ClassId m13 = javaToKotlinClassMap.m(b10);
        if (m13 != null) {
            a10 = m13;
        }
        return new ClassLiteralValue(a10, i10);
    }

    private final void c(Class<?> cls, KotlinJvmBinaryClass.d dVar) {
        Constructor<?>[] constructorArr;
        int i10;
        Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
        za.k.d(declaredConstructors, "klass.declaredConstructors");
        int length = declaredConstructors.length;
        int i11 = 0;
        while (i11 < length) {
            Constructor<?> constructor = declaredConstructors[i11];
            Name name = SpecialNames.f16455j;
            m mVar = m.f18986a;
            za.k.d(constructor, "constructor");
            KotlinJvmBinaryClass.e a10 = dVar.a(name, mVar.a(constructor));
            if (a10 == null) {
                constructorArr = declaredConstructors;
                i10 = length;
            } else {
                Annotation[] declaredAnnotations = constructor.getDeclaredAnnotations();
                za.k.d(declaredAnnotations, "constructor.declaredAnnotations");
                for (Annotation annotation : declaredAnnotations) {
                    za.k.d(annotation, "annotation");
                    f(a10, annotation);
                }
                Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
                za.k.d(parameterAnnotations, "parameterAnnotations");
                if (!(parameterAnnotations.length == 0)) {
                    int length2 = constructor.getParameterTypes().length - parameterAnnotations.length;
                    int length3 = parameterAnnotations.length;
                    for (int i12 = 0; i12 < length3; i12++) {
                        Annotation[] annotationArr = parameterAnnotations[i12];
                        za.k.d(annotationArr, "annotations");
                        int length4 = annotationArr.length;
                        int i13 = 0;
                        while (i13 < length4) {
                            Annotation annotation2 = annotationArr[i13];
                            Class<?> b10 = JvmClassMapping.b(JvmClassMapping.a(annotation2));
                            Constructor<?>[] constructorArr2 = declaredConstructors;
                            int i14 = length;
                            ClassId a11 = reflectClassUtil.a(b10);
                            int i15 = length2;
                            za.k.d(annotation2, "annotation");
                            KotlinJvmBinaryClass.a c10 = a10.c(i12 + length2, a11, new ReflectAnnotationSource(annotation2));
                            if (c10 != null) {
                                f18972a.h(c10, annotation2, b10);
                            }
                            i13++;
                            declaredConstructors = constructorArr2;
                            length = i14;
                            length2 = i15;
                        }
                    }
                }
                constructorArr = declaredConstructors;
                i10 = length;
                a10.a();
            }
            i11++;
            declaredConstructors = constructorArr;
            length = i10;
        }
    }

    private final void d(Class<?> cls, KotlinJvmBinaryClass.d dVar) {
        Field[] declaredFields = cls.getDeclaredFields();
        za.k.d(declaredFields, "klass.declaredFields");
        for (Field field : declaredFields) {
            Name f10 = Name.f(field.getName());
            za.k.d(f10, "identifier(field.name)");
            m mVar = m.f18986a;
            za.k.d(field, "field");
            KotlinJvmBinaryClass.c b10 = dVar.b(f10, mVar.b(field), null);
            if (b10 != null) {
                Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                za.k.d(declaredAnnotations, "field.declaredAnnotations");
                for (Annotation annotation : declaredAnnotations) {
                    za.k.d(annotation, "annotation");
                    f(b10, annotation);
                }
                b10.a();
            }
        }
    }

    private final void e(Class<?> cls, KotlinJvmBinaryClass.d dVar) {
        Method[] methodArr;
        Method[] declaredMethods = cls.getDeclaredMethods();
        za.k.d(declaredMethods, "klass.declaredMethods");
        int length = declaredMethods.length;
        int i10 = 0;
        while (i10 < length) {
            Method method = declaredMethods[i10];
            Name f10 = Name.f(method.getName());
            za.k.d(f10, "identifier(method.name)");
            m mVar = m.f18986a;
            za.k.d(method, Constants.MessagerConstants.METHOD_KEY);
            KotlinJvmBinaryClass.e a10 = dVar.a(f10, mVar.c(method));
            if (a10 == null) {
                methodArr = declaredMethods;
            } else {
                Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                za.k.d(declaredAnnotations, "method.declaredAnnotations");
                for (Annotation annotation : declaredAnnotations) {
                    za.k.d(annotation, "annotation");
                    f(a10, annotation);
                }
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                za.k.d(parameterAnnotations, "method.parameterAnnotations");
                int length2 = parameterAnnotations.length;
                for (int i11 = 0; i11 < length2; i11++) {
                    Annotation[] annotationArr = parameterAnnotations[i11];
                    za.k.d(annotationArr, "annotations");
                    int length3 = annotationArr.length;
                    int i12 = 0;
                    while (i12 < length3) {
                        Annotation annotation2 = annotationArr[i12];
                        Class<?> b10 = JvmClassMapping.b(JvmClassMapping.a(annotation2));
                        ClassId a11 = reflectClassUtil.a(b10);
                        Method[] methodArr2 = declaredMethods;
                        za.k.d(annotation2, "annotation");
                        KotlinJvmBinaryClass.a c10 = a10.c(i11, a11, new ReflectAnnotationSource(annotation2));
                        if (c10 != null) {
                            f18972a.h(c10, annotation2, b10);
                        }
                        i12++;
                        declaredMethods = methodArr2;
                    }
                }
                methodArr = declaredMethods;
                a10.a();
            }
            i10++;
            declaredMethods = methodArr;
        }
    }

    private final void f(KotlinJvmBinaryClass.c cVar, Annotation annotation) {
        Class<?> b10 = JvmClassMapping.b(JvmClassMapping.a(annotation));
        KotlinJvmBinaryClass.a b11 = cVar.b(reflectClassUtil.a(b10), new ReflectAnnotationSource(annotation));
        if (b11 != null) {
            f18972a.h(b11, annotation, b10);
        }
    }

    private final void g(KotlinJvmBinaryClass.a aVar, Name name, Object obj) {
        Set set;
        Object T;
        Class<?> cls = obj.getClass();
        if (za.k.a(cls, Class.class)) {
            za.k.c(obj, "null cannot be cast to non-null type java.lang.Class<*>");
            aVar.f(name, a((Class) obj));
            return;
        }
        set = i.f18979a;
        if (set.contains(cls)) {
            aVar.c(name, obj);
            return;
        }
        if (reflectClassUtil.h(cls)) {
            if (!cls.isEnum()) {
                cls = cls.getEnclosingClass();
            }
            za.k.d(cls, "if (clazz.isEnum) clazz else clazz.enclosingClass");
            ClassId a10 = reflectClassUtil.a(cls);
            za.k.c(obj, "null cannot be cast to non-null type kotlin.Enum<*>");
            Name f10 = Name.f(((Enum) obj).name());
            za.k.d(f10, "identifier((value as Enum<*>).name)");
            aVar.e(name, a10, f10);
            return;
        }
        if (Annotation.class.isAssignableFrom(cls)) {
            Class<?>[] interfaces = cls.getInterfaces();
            za.k.d(interfaces, "clazz.interfaces");
            T = _Arrays.T(interfaces);
            Class<?> cls2 = (Class) T;
            za.k.d(cls2, "annotationClass");
            KotlinJvmBinaryClass.a b10 = aVar.b(name, reflectClassUtil.a(cls2));
            if (b10 == null) {
                return;
            }
            za.k.c(obj, "null cannot be cast to non-null type kotlin.Annotation");
            h(b10, (Annotation) obj, cls2);
            return;
        }
        if (cls.isArray()) {
            KotlinJvmBinaryClass.b d10 = aVar.d(name);
            if (d10 == null) {
                return;
            }
            Class<?> componentType = cls.getComponentType();
            int i10 = 0;
            if (componentType.isEnum()) {
                za.k.d(componentType, "componentType");
                ClassId a11 = reflectClassUtil.a(componentType);
                za.k.c(obj, "null cannot be cast to non-null type kotlin.Array<*>");
                Object[] objArr = (Object[]) obj;
                int length = objArr.length;
                while (i10 < length) {
                    Object obj2 = objArr[i10];
                    za.k.c(obj2, "null cannot be cast to non-null type kotlin.Enum<*>");
                    Name f11 = Name.f(((Enum) obj2).name());
                    za.k.d(f11, "identifier((element as Enum<*>).name)");
                    d10.d(a11, f11);
                    i10++;
                }
            } else if (za.k.a(componentType, Class.class)) {
                za.k.c(obj, "null cannot be cast to non-null type kotlin.Array<*>");
                Object[] objArr2 = (Object[]) obj;
                int length2 = objArr2.length;
                while (i10 < length2) {
                    Object obj3 = objArr2[i10];
                    za.k.c(obj3, "null cannot be cast to non-null type java.lang.Class<*>");
                    d10.e(a((Class) obj3));
                    i10++;
                }
            } else if (Annotation.class.isAssignableFrom(componentType)) {
                za.k.c(obj, "null cannot be cast to non-null type kotlin.Array<*>");
                Object[] objArr3 = (Object[]) obj;
                int length3 = objArr3.length;
                while (i10 < length3) {
                    Object obj4 = objArr3[i10];
                    za.k.d(componentType, "componentType");
                    KotlinJvmBinaryClass.a c10 = d10.c(reflectClassUtil.a(componentType));
                    if (c10 != null) {
                        za.k.c(obj4, "null cannot be cast to non-null type kotlin.Annotation");
                        h(c10, (Annotation) obj4, componentType);
                    }
                    i10++;
                }
            } else {
                za.k.c(obj, "null cannot be cast to non-null type kotlin.Array<*>");
                Object[] objArr4 = (Object[]) obj;
                int length4 = objArr4.length;
                while (i10 < length4) {
                    d10.b(objArr4[i10]);
                    i10++;
                }
            }
            d10.a();
            return;
        }
        throw new UnsupportedOperationException("Unsupported annotation argument value (" + cls + "): " + obj);
    }

    private final void h(KotlinJvmBinaryClass.a aVar, Annotation annotation, Class<?> cls) {
        Method[] declaredMethods = cls.getDeclaredMethods();
        za.k.d(declaredMethods, "annotationType.declaredMethods");
        for (Method method : declaredMethods) {
            try {
                Object invoke = method.invoke(annotation, new Object[0]);
                za.k.b(invoke);
                Name f10 = Name.f(method.getName());
                za.k.d(f10, "identifier(method.name)");
                g(aVar, f10, invoke);
            } catch (IllegalAccessException unused) {
            }
        }
        aVar.a();
    }

    public final void b(Class<?> cls, KotlinJvmBinaryClass.c cVar) {
        za.k.e(cls, "klass");
        za.k.e(cVar, "visitor");
        Annotation[] declaredAnnotations = cls.getDeclaredAnnotations();
        za.k.d(declaredAnnotations, "klass.declaredAnnotations");
        for (Annotation annotation : declaredAnnotations) {
            za.k.d(annotation, "annotation");
            f(cVar, annotation);
        }
        cVar.a();
    }

    public final void i(Class<?> cls, KotlinJvmBinaryClass.d dVar) {
        za.k.e(cls, "klass");
        za.k.e(dVar, "memberVisitor");
        e(cls, dVar);
        c(cls, dVar);
        d(cls, dVar);
    }
}
