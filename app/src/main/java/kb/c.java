package kb;

import com.oplus.backup.sdk.common.utils.Constants;
import gb.KClass;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jb.KotlinReflectionInternalError;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import kotlin.collections.s;
import xa.JvmClassMapping;
import ya.l;
import za.Lambda;
import za.Reflection;

/* compiled from: AnnotationConstructorCaller.kt */
/* loaded from: classes2.dex */
public final class c {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AnnotationConstructorCaller.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.a<Integer> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Map<String, Object> f14251e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(Map<String, ? extends Object> map) {
            super(0);
            this.f14251e = map;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Integer invoke() {
            int hashCode;
            Iterator<T> it = this.f14251e.entrySet().iterator();
            int i10 = 0;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String str = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof boolean[]) {
                    hashCode = Arrays.hashCode((boolean[]) value);
                } else if (value instanceof char[]) {
                    hashCode = Arrays.hashCode((char[]) value);
                } else if (value instanceof byte[]) {
                    hashCode = Arrays.hashCode((byte[]) value);
                } else if (value instanceof short[]) {
                    hashCode = Arrays.hashCode((short[]) value);
                } else if (value instanceof int[]) {
                    hashCode = Arrays.hashCode((int[]) value);
                } else if (value instanceof float[]) {
                    hashCode = Arrays.hashCode((float[]) value);
                } else if (value instanceof long[]) {
                    hashCode = Arrays.hashCode((long[]) value);
                } else if (value instanceof double[]) {
                    hashCode = Arrays.hashCode((double[]) value);
                } else {
                    hashCode = value instanceof Object[] ? Arrays.hashCode((Object[]) value) : value.hashCode();
                }
                i10 += hashCode ^ (str.hashCode() * 127);
            }
            return Integer.valueOf(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AnnotationConstructorCaller.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.a<String> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Class<T> f14252e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Map<String, Object> f14253f;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: AnnotationConstructorCaller.kt */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements l<Map.Entry<? extends String, ? extends Object>, CharSequence> {

            /* renamed from: e, reason: collision with root package name */
            public static final a f14254e = new a();

            a() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final CharSequence invoke(Map.Entry<String, ? extends Object> entry) {
                String obj;
                za.k.e(entry, "entry");
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof boolean[]) {
                    obj = Arrays.toString((boolean[]) value);
                    za.k.d(obj, "toString(this)");
                } else if (value instanceof char[]) {
                    obj = Arrays.toString((char[]) value);
                    za.k.d(obj, "toString(this)");
                } else if (value instanceof byte[]) {
                    obj = Arrays.toString((byte[]) value);
                    za.k.d(obj, "toString(this)");
                } else if (value instanceof short[]) {
                    obj = Arrays.toString((short[]) value);
                    za.k.d(obj, "toString(this)");
                } else if (value instanceof int[]) {
                    obj = Arrays.toString((int[]) value);
                    za.k.d(obj, "toString(this)");
                } else if (value instanceof float[]) {
                    obj = Arrays.toString((float[]) value);
                    za.k.d(obj, "toString(this)");
                } else if (value instanceof long[]) {
                    obj = Arrays.toString((long[]) value);
                    za.k.d(obj, "toString(this)");
                } else if (value instanceof double[]) {
                    obj = Arrays.toString((double[]) value);
                    za.k.d(obj, "toString(this)");
                } else if (value instanceof Object[]) {
                    obj = Arrays.toString((Object[]) value);
                    za.k.d(obj, "toString(this)");
                } else {
                    obj = value.toString();
                }
                return key + '=' + obj;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(Class<T> cls, Map<String, ? extends Object> map) {
            super(0);
            this.f14252e = cls;
            this.f14253f = map;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String invoke() {
            Class<T> cls = this.f14252e;
            Map<String, Object> map = this.f14253f;
            StringBuilder sb2 = new StringBuilder();
            sb2.append('@');
            sb2.append(cls.getCanonicalName());
            _Collections.a0(map.entrySet(), sb2, ", ", "(", ")", 0, null, a.f14254e, 48, null);
            String sb3 = sb2.toString();
            za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
            return sb3;
        }
    }

    public static final <T> T d(Class<T> cls, Map<String, ? extends Object> map, List<Method> list) {
        ma.h b10;
        ma.h b11;
        za.k.e(cls, "annotationClass");
        za.k.e(map, "values");
        za.k.e(list, "methods");
        b10 = ma.j.b(new a(map));
        b11 = ma.j.b(new b(cls, map));
        T t7 = (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new kb.b(cls, map, b11, b10, list));
        za.k.c(t7, "null cannot be cast to non-null type T of kotlin.reflect.jvm.internal.calls.AnnotationConstructorCallerKt.createAnnotationInstance");
        return t7;
    }

    public static /* synthetic */ Object e(Class cls, Map map, List list, int i10, Object obj) {
        int u7;
        if ((i10 & 4) != 0) {
            Set keySet = map.keySet();
            u7 = s.u(keySet, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator it = keySet.iterator();
            while (it.hasNext()) {
                arrayList.add(cls.getDeclaredMethod((String) it.next(), new Class[0]));
            }
            list = arrayList;
        }
        return d(cls, map, list);
    }

    private static final <T> boolean f(Class<T> cls, List<Method> list, Map<String, ? extends Object> map, Object obj) {
        boolean a10;
        boolean z10;
        KClass a11;
        Class cls2 = null;
        Annotation annotation = obj instanceof Annotation ? (Annotation) obj : null;
        if (annotation != null && (a11 = JvmClassMapping.a(annotation)) != null) {
            cls2 = JvmClassMapping.b(a11);
        }
        if (za.k.a(cls2, cls)) {
            if (!(list instanceof Collection) || !list.isEmpty()) {
                for (Method method : list) {
                    Object obj2 = map.get(method.getName());
                    Object invoke = method.invoke(obj, new Object[0]);
                    if (obj2 instanceof boolean[]) {
                        za.k.c(invoke, "null cannot be cast to non-null type kotlin.BooleanArray");
                        a10 = Arrays.equals((boolean[]) obj2, (boolean[]) invoke);
                    } else if (obj2 instanceof char[]) {
                        za.k.c(invoke, "null cannot be cast to non-null type kotlin.CharArray");
                        a10 = Arrays.equals((char[]) obj2, (char[]) invoke);
                    } else if (obj2 instanceof byte[]) {
                        za.k.c(invoke, "null cannot be cast to non-null type kotlin.ByteArray");
                        a10 = Arrays.equals((byte[]) obj2, (byte[]) invoke);
                    } else if (obj2 instanceof short[]) {
                        za.k.c(invoke, "null cannot be cast to non-null type kotlin.ShortArray");
                        a10 = Arrays.equals((short[]) obj2, (short[]) invoke);
                    } else if (obj2 instanceof int[]) {
                        za.k.c(invoke, "null cannot be cast to non-null type kotlin.IntArray");
                        a10 = Arrays.equals((int[]) obj2, (int[]) invoke);
                    } else if (obj2 instanceof float[]) {
                        za.k.c(invoke, "null cannot be cast to non-null type kotlin.FloatArray");
                        a10 = Arrays.equals((float[]) obj2, (float[]) invoke);
                    } else if (obj2 instanceof long[]) {
                        za.k.c(invoke, "null cannot be cast to non-null type kotlin.LongArray");
                        a10 = Arrays.equals((long[]) obj2, (long[]) invoke);
                    } else if (obj2 instanceof double[]) {
                        za.k.c(invoke, "null cannot be cast to non-null type kotlin.DoubleArray");
                        a10 = Arrays.equals((double[]) obj2, (double[]) invoke);
                    } else if (obj2 instanceof Object[]) {
                        za.k.c(invoke, "null cannot be cast to non-null type kotlin.Array<*>");
                        a10 = Arrays.equals((Object[]) obj2, (Object[]) invoke);
                    } else {
                        a10 = za.k.a(obj2, invoke);
                    }
                    if (!a10) {
                        z10 = false;
                        break;
                    }
                }
            }
            z10 = true;
            if (z10) {
                return true;
            }
        }
        return false;
    }

    private static final int g(ma.h<Integer> hVar) {
        return hVar.getValue().intValue();
    }

    private static final String h(ma.h<String> hVar) {
        return hVar.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object i(Class cls, Map map, ma.h hVar, ma.h hVar2, List list, Object obj, Method method, Object[] objArr) {
        List f02;
        Object T;
        za.k.e(cls, "$annotationClass");
        za.k.e(map, "$values");
        za.k.e(hVar, "$toString$delegate");
        za.k.e(hVar2, "$hashCode$delegate");
        za.k.e(list, "$methods");
        String name = method.getName();
        if (name != null) {
            int hashCode = name.hashCode();
            if (hashCode != -1776922004) {
                if (hashCode != 147696667) {
                    if (hashCode == 1444986633 && name.equals("annotationType")) {
                        return cls;
                    }
                } else if (name.equals("hashCode")) {
                    return Integer.valueOf(g(hVar2));
                }
            } else if (name.equals("toString")) {
                return h(hVar);
            }
        }
        if (za.k.a(name, "equals")) {
            if (objArr != null && objArr.length == 1) {
                za.k.d(objArr, Constants.MessagerConstants.ARGS_KEY);
                T = _Arrays.T(objArr);
                return Boolean.valueOf(f(cls, list, map, T));
            }
        }
        if (map.containsKey(name)) {
            return map.get(name);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Method is not supported: ");
        sb2.append(method);
        sb2.append(" (args: ");
        if (objArr == null) {
            objArr = new Object[0];
        }
        f02 = _Arrays.f0(objArr);
        sb2.append(f02);
        sb2.append(')');
        throw new KotlinReflectionInternalError(sb2.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Void j(int i10, String str, Class<?> cls) {
        KClass b10;
        String u7;
        if (za.k.a(cls, Class.class)) {
            b10 = Reflection.b(KClass.class);
        } else {
            b10 = (cls.isArray() && za.k.a(cls.getComponentType(), Class.class)) ? Reflection.b(KClass[].class) : JvmClassMapping.e(cls);
        }
        if (za.k.a(b10.u(), Reflection.b(Object[].class).u())) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(b10.u());
            sb2.append('<');
            Class<?> componentType = JvmClassMapping.b(b10).getComponentType();
            za.k.d(componentType, "kotlinClass.java.componentType");
            sb2.append(JvmClassMapping.e(componentType).u());
            sb2.append('>');
            u7 = sb2.toString();
        } else {
            u7 = b10.u();
        }
        throw new IllegalArgumentException("Argument #" + i10 + ' ' + str + " is not of the required type " + u7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object k(Object obj, Class<?> cls) {
        if (obj instanceof Class) {
            return null;
        }
        if (obj instanceof KClass) {
            obj = JvmClassMapping.b((KClass) obj);
        } else if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            if (objArr instanceof Class[]) {
                return null;
            }
            if (objArr instanceof KClass[]) {
                za.k.c(obj, "null cannot be cast to non-null type kotlin.Array<kotlin.reflect.KClass<*>>");
                KClass[] kClassArr = (KClass[]) obj;
                ArrayList arrayList = new ArrayList(kClassArr.length);
                for (KClass kClass : kClassArr) {
                    arrayList.add(JvmClassMapping.b(kClass));
                }
                obj = arrayList.toArray(new Class[0]);
            } else {
                obj = objArr;
            }
        }
        if (cls.isInstance(obj)) {
            return obj;
        }
        return null;
    }
}
