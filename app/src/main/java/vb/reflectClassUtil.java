package vb;

import gb.KClass;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.collections._Arrays;
import kotlin.collections.m0;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import rd.Sequence;
import rd._Sequences;
import sd.StringsJVM;
import xa.JvmClassMapping;
import za.Lambda;
import za.Reflection;

/* compiled from: reflectClassUtil.kt */
/* renamed from: vb.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class reflectClassUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final List<KClass<? extends Object>> f19225a;

    /* renamed from: b, reason: collision with root package name */
    private static final Map<Class<? extends Object>, Class<? extends Object>> f19226b;

    /* renamed from: c, reason: collision with root package name */
    private static final Map<Class<? extends Object>, Class<? extends Object>> f19227c;

    /* renamed from: d, reason: collision with root package name */
    private static final Map<Class<? extends ma.c<?>>, Integer> f19228d;

    /* compiled from: reflectClassUtil.kt */
    /* renamed from: vb.d$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<ParameterizedType, ParameterizedType> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f19229e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ParameterizedType invoke(ParameterizedType parameterizedType) {
            za.k.e(parameterizedType, "it");
            Type ownerType = parameterizedType.getOwnerType();
            if (ownerType instanceof ParameterizedType) {
                return (ParameterizedType) ownerType;
            }
            return null;
        }
    }

    /* compiled from: reflectClassUtil.kt */
    /* renamed from: vb.d$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<ParameterizedType, Sequence<? extends Type>> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f19230e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Sequence<Type> invoke(ParameterizedType parameterizedType) {
            Sequence<Type> r10;
            za.k.e(parameterizedType, "it");
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            za.k.d(actualTypeArguments, "it.actualTypeArguments");
            r10 = _Arrays.r(actualTypeArguments);
            return r10;
        }
    }

    static {
        List<KClass<? extends Object>> m10;
        int u7;
        Map<Class<? extends Object>, Class<? extends Object>> q10;
        int u10;
        Map<Class<? extends Object>, Class<? extends Object>> q11;
        List m11;
        int u11;
        Map<Class<? extends ma.c<?>>, Integer> q12;
        int i10 = 0;
        m10 = kotlin.collections.r.m(Reflection.b(Boolean.TYPE), Reflection.b(Byte.TYPE), Reflection.b(Character.TYPE), Reflection.b(Double.TYPE), Reflection.b(Float.TYPE), Reflection.b(Integer.TYPE), Reflection.b(Long.TYPE), Reflection.b(Short.TYPE));
        f19225a = m10;
        u7 = kotlin.collections.s.u(m10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = m10.iterator();
        while (it.hasNext()) {
            KClass kClass = (KClass) it.next();
            arrayList.add(ma.u.a(JvmClassMapping.c(kClass), JvmClassMapping.d(kClass)));
        }
        q10 = m0.q(arrayList);
        f19226b = q10;
        List<KClass<? extends Object>> list = f19225a;
        u10 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList2 = new ArrayList(u10);
        Iterator<T> it2 = list.iterator();
        while (it2.hasNext()) {
            KClass kClass2 = (KClass) it2.next();
            arrayList2.add(ma.u.a(JvmClassMapping.d(kClass2), JvmClassMapping.c(kClass2)));
        }
        q11 = m0.q(arrayList2);
        f19227c = q11;
        m11 = kotlin.collections.r.m(ya.a.class, ya.l.class, ya.p.class, ya.q.class, ya.r.class, ya.s.class, ya.t.class, ya.u.class, ya.v.class, ya.w.class, ya.b.class, ya.c.class, ya.d.class, ya.e.class, ya.f.class, ya.g.class, ya.h.class, ya.i.class, ya.j.class, ya.k.class, ya.m.class, ya.n.class, ya.o.class);
        u11 = kotlin.collections.s.u(m11, 10);
        ArrayList arrayList3 = new ArrayList(u11);
        for (Object obj : m11) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                kotlin.collections.r.t();
            }
            arrayList3.add(ma.u.a((Class) obj, Integer.valueOf(i10)));
            i10 = i11;
        }
        q12 = m0.q(arrayList3);
        f19228d = q12;
    }

    public static final ClassId a(Class<?> cls) {
        ClassId m10;
        ClassId a10;
        za.k.e(cls, "<this>");
        if (!cls.isPrimitive()) {
            if (!cls.isArray()) {
                if (cls.getEnclosingMethod() == null && cls.getEnclosingConstructor() == null) {
                    String simpleName = cls.getSimpleName();
                    za.k.d(simpleName, "simpleName");
                    if (!(simpleName.length() == 0)) {
                        Class<?> declaringClass = cls.getDeclaringClass();
                        if (declaringClass == null || (a10 = a(declaringClass)) == null || (m10 = a10.d(Name.f(cls.getSimpleName()))) == null) {
                            m10 = ClassId.m(new FqName(cls.getName()));
                        }
                        za.k.d(m10, "declaringClass?.classId?…Id.topLevel(FqName(name))");
                        return m10;
                    }
                }
                FqName fqName = new FqName(cls.getName());
                return new ClassId(fqName.e(), FqName.k(fqName.g()), true);
            }
            throw new IllegalArgumentException("Can't compute ClassId for array type: " + cls);
        }
        throw new IllegalArgumentException("Can't compute ClassId for primitive type: " + cls);
    }

    public static final String b(Class<?> cls) {
        String y4;
        String y10;
        za.k.e(cls, "<this>");
        if (cls.isPrimitive()) {
            String name = cls.getName();
            switch (name.hashCode()) {
                case -1325958191:
                    if (name.equals("double")) {
                        return "D";
                    }
                    break;
                case 104431:
                    if (name.equals("int")) {
                        return "I";
                    }
                    break;
                case 3039496:
                    if (name.equals("byte")) {
                        return "B";
                    }
                    break;
                case 3052374:
                    if (name.equals("char")) {
                        return "C";
                    }
                    break;
                case 3327612:
                    if (name.equals("long")) {
                        return "J";
                    }
                    break;
                case 3625364:
                    if (name.equals("void")) {
                        return "V";
                    }
                    break;
                case 64711720:
                    if (name.equals("boolean")) {
                        return "Z";
                    }
                    break;
                case 97526364:
                    if (name.equals("float")) {
                        return "F";
                    }
                    break;
                case 109413500:
                    if (name.equals("short")) {
                        return "S";
                    }
                    break;
            }
            throw new IllegalArgumentException("Unsupported primitive type: " + cls);
        }
        if (cls.isArray()) {
            String name2 = cls.getName();
            za.k.d(name2, "name");
            y10 = StringsJVM.y(name2, '.', '/', false, 4, null);
            return y10;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append('L');
        String name3 = cls.getName();
        za.k.d(name3, "name");
        y4 = StringsJVM.y(name3, '.', '/', false, 4, null);
        sb2.append(y4);
        sb2.append(';');
        return sb2.toString();
    }

    public static final Integer c(Class<?> cls) {
        za.k.e(cls, "<this>");
        return f19228d.get(cls);
    }

    public static final List<Type> d(Type type) {
        Sequence f10;
        Sequence q10;
        List<Type> C;
        List<Type> f02;
        List<Type> j10;
        za.k.e(type, "<this>");
        if (!(type instanceof ParameterizedType)) {
            j10 = kotlin.collections.r.j();
            return j10;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        if (parameterizedType.getOwnerType() == null) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            za.k.d(actualTypeArguments, "actualTypeArguments");
            f02 = _Arrays.f0(actualTypeArguments);
            return f02;
        }
        f10 = rd.l.f(type, a.f19229e);
        q10 = _Sequences.q(f10, b.f19230e);
        C = _Sequences.C(q10);
        return C;
    }

    public static final Class<?> e(Class<?> cls) {
        za.k.e(cls, "<this>");
        return f19226b.get(cls);
    }

    public static final ClassLoader f(Class<?> cls) {
        za.k.e(cls, "<this>");
        ClassLoader classLoader = cls.getClassLoader();
        if (classLoader != null) {
            return classLoader;
        }
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        za.k.d(systemClassLoader, "getSystemClassLoader()");
        return systemClassLoader;
    }

    public static final Class<?> g(Class<?> cls) {
        za.k.e(cls, "<this>");
        return f19227c.get(cls);
    }

    public static final boolean h(Class<?> cls) {
        za.k.e(cls, "<this>");
        return Enum.class.isAssignableFrom(cls);
    }
}
