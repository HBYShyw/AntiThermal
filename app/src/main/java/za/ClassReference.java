package za;

import gb.KClass;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.MapsJVM;
import kotlin.collections.m0;
import ma.ExceptionsH;
import xa.JvmClassMapping;
import xa.KotlinReflectionNotSupportedError;

/* compiled from: ClassReference.kt */
/* renamed from: za.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class ClassReference implements KClass<Object>, ClassBasedDeclarationContainer {

    /* renamed from: f, reason: collision with root package name */
    public static final a f20359f = new a(null);

    /* renamed from: g, reason: collision with root package name */
    private static final Map<Class<? extends ma.c<?>>, Integer> f20360g;

    /* renamed from: h, reason: collision with root package name */
    private static final HashMap<String, String> f20361h;

    /* renamed from: i, reason: collision with root package name */
    private static final HashMap<String, String> f20362i;

    /* renamed from: j, reason: collision with root package name */
    private static final HashMap<String, String> f20363j;

    /* renamed from: k, reason: collision with root package name */
    private static final Map<String, String> f20364k;

    /* renamed from: e, reason: collision with root package name */
    private final Class<?> f20365e;

    /* compiled from: ClassReference.kt */
    /* renamed from: za.e$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(Class<?> cls) {
            String str;
            k.e(cls, "jClass");
            String str2 = null;
            if (cls.isAnonymousClass() || cls.isLocalClass()) {
                return null;
            }
            if (cls.isArray()) {
                Class<?> componentType = cls.getComponentType();
                if (componentType.isPrimitive() && (str = (String) ClassReference.f20363j.get(componentType.getName())) != null) {
                    str2 = str + "Array";
                }
                return str2 == null ? "kotlin.Array" : str2;
            }
            String str3 = (String) ClassReference.f20363j.get(cls.getName());
            return str3 == null ? cls.getCanonicalName() : str3;
        }

        public final String b(Class<?> cls) {
            String str;
            String A0;
            String B0;
            String B02;
            k.e(cls, "jClass");
            String str2 = null;
            if (!cls.isAnonymousClass()) {
                if (cls.isLocalClass()) {
                    String simpleName = cls.getSimpleName();
                    Method enclosingMethod = cls.getEnclosingMethod();
                    if (enclosingMethod != null) {
                        k.d(simpleName, "name");
                        B02 = sd.v.B0(simpleName, enclosingMethod.getName() + '$', null, 2, null);
                        if (B02 != null) {
                            return B02;
                        }
                    }
                    Constructor<?> enclosingConstructor = cls.getEnclosingConstructor();
                    if (enclosingConstructor == null) {
                        k.d(simpleName, "name");
                        A0 = sd.v.A0(simpleName, '$', null, 2, null);
                        return A0;
                    }
                    k.d(simpleName, "name");
                    B0 = sd.v.B0(simpleName, enclosingConstructor.getName() + '$', null, 2, null);
                    return B0;
                }
                if (cls.isArray()) {
                    Class<?> componentType = cls.getComponentType();
                    if (componentType.isPrimitive() && (str = (String) ClassReference.f20364k.get(componentType.getName())) != null) {
                        str2 = str + "Array";
                    }
                    if (str2 == null) {
                        return "Array";
                    }
                } else {
                    String str3 = (String) ClassReference.f20364k.get(cls.getName());
                    return str3 == null ? cls.getSimpleName() : str3;
                }
            }
            return str2;
        }

        public final boolean c(Object obj, Class<?> cls) {
            k.e(cls, "jClass");
            Map map = ClassReference.f20360g;
            k.c(map, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.get, V of kotlin.collections.MapsKt__MapsKt.get>");
            Integer num = (Integer) map.get(cls);
            if (num != null) {
                return TypeIntrinsics.i(obj, num.intValue());
            }
            if (cls.isPrimitive()) {
                cls = JvmClassMapping.c(JvmClassMapping.e(cls));
            }
            return cls.isInstance(obj);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static {
        List m10;
        int u7;
        Map<Class<? extends ma.c<?>>, Integer> q10;
        int e10;
        String D0;
        String D02;
        int i10 = 0;
        m10 = kotlin.collections.r.m(ya.a.class, ya.l.class, ya.p.class, ya.q.class, ya.r.class, ya.s.class, ya.t.class, ya.u.class, ya.v.class, ya.w.class, ya.b.class, ya.c.class, ya.d.class, ya.e.class, ya.f.class, ya.g.class, ya.h.class, ya.i.class, ya.j.class, ya.k.class, ya.m.class, ya.n.class, ya.o.class);
        u7 = kotlin.collections.s.u(m10, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (Object obj : m10) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                kotlin.collections.r.t();
            }
            arrayList.add(ma.u.a((Class) obj, Integer.valueOf(i10)));
            i10 = i11;
        }
        q10 = m0.q(arrayList);
        f20360g = q10;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("boolean", "kotlin.Boolean");
        hashMap.put("char", "kotlin.Char");
        hashMap.put("byte", "kotlin.Byte");
        hashMap.put("short", "kotlin.Short");
        hashMap.put("int", "kotlin.Int");
        hashMap.put("float", "kotlin.Float");
        hashMap.put("long", "kotlin.Long");
        hashMap.put("double", "kotlin.Double");
        f20361h = hashMap;
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("java.lang.Boolean", "kotlin.Boolean");
        hashMap2.put("java.lang.Character", "kotlin.Char");
        hashMap2.put("java.lang.Byte", "kotlin.Byte");
        hashMap2.put("java.lang.Short", "kotlin.Short");
        hashMap2.put("java.lang.Integer", "kotlin.Int");
        hashMap2.put("java.lang.Float", "kotlin.Float");
        hashMap2.put("java.lang.Long", "kotlin.Long");
        hashMap2.put("java.lang.Double", "kotlin.Double");
        f20362i = hashMap2;
        HashMap<String, String> hashMap3 = new HashMap<>();
        hashMap3.put("java.lang.Object", "kotlin.Any");
        hashMap3.put("java.lang.String", "kotlin.String");
        hashMap3.put("java.lang.CharSequence", "kotlin.CharSequence");
        hashMap3.put("java.lang.Throwable", "kotlin.Throwable");
        hashMap3.put("java.lang.Cloneable", "kotlin.Cloneable");
        hashMap3.put("java.lang.Number", "kotlin.Number");
        hashMap3.put("java.lang.Comparable", "kotlin.Comparable");
        hashMap3.put("java.lang.Enum", "kotlin.Enum");
        hashMap3.put("java.lang.annotation.Annotation", "kotlin.Annotation");
        hashMap3.put("java.lang.Iterable", "kotlin.collections.Iterable");
        hashMap3.put("java.util.Iterator", "kotlin.collections.Iterator");
        hashMap3.put("java.util.Collection", "kotlin.collections.Collection");
        hashMap3.put("java.util.List", "kotlin.collections.List");
        hashMap3.put("java.util.Set", "kotlin.collections.Set");
        hashMap3.put("java.util.ListIterator", "kotlin.collections.ListIterator");
        hashMap3.put("java.util.Map", "kotlin.collections.Map");
        hashMap3.put("java.util.Map$Entry", "kotlin.collections.Map.Entry");
        hashMap3.put("kotlin.jvm.internal.StringCompanionObject", "kotlin.String.Companion");
        hashMap3.put("kotlin.jvm.internal.EnumCompanionObject", "kotlin.Enum.Companion");
        hashMap3.putAll(hashMap);
        hashMap3.putAll(hashMap2);
        Collection<String> values = hashMap.values();
        k.d(values, "primitiveFqNames.values");
        for (String str : values) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("kotlin.jvm.internal.");
            k.d(str, "kotlinName");
            D02 = sd.v.D0(str, '.', null, 2, null);
            sb2.append(D02);
            sb2.append("CompanionObject");
            ma.o a10 = ma.u.a(sb2.toString(), str + ".Companion");
            hashMap3.put(a10.c(), a10.d());
        }
        for (Map.Entry<Class<? extends ma.c<?>>, Integer> entry : f20360g.entrySet()) {
            hashMap3.put(entry.getKey().getName(), "kotlin.Function" + entry.getValue().intValue());
        }
        f20363j = hashMap3;
        e10 = MapsJVM.e(hashMap3.size());
        LinkedHashMap linkedHashMap = new LinkedHashMap(e10);
        for (Map.Entry entry2 : hashMap3.entrySet()) {
            Object key = entry2.getKey();
            D0 = sd.v.D0((String) entry2.getValue(), '.', null, 2, null);
            linkedHashMap.put(key, D0);
        }
        f20364k = linkedHashMap;
    }

    public ClassReference(Class<?> cls) {
        k.e(cls, "jClass");
        this.f20365e = cls;
    }

    private final Void B() {
        throw new KotlinReflectionNotSupportedError();
    }

    @Override // za.ClassBasedDeclarationContainer
    public Class<?> e() {
        return this.f20365e;
    }

    public boolean equals(Object obj) {
        return (obj instanceof ClassReference) && k.a(JvmClassMapping.c(this), JvmClassMapping.c((KClass) obj));
    }

    public int hashCode() {
        return JvmClassMapping.c(this).hashCode();
    }

    @Override // gb.KAnnotatedElement
    public List<Annotation> i() {
        B();
        throw new ExceptionsH();
    }

    @Override // gb.KClass
    public boolean n() {
        B();
        throw new ExceptionsH();
    }

    @Override // gb.KClass
    public boolean q() {
        B();
        throw new ExceptionsH();
    }

    @Override // gb.KClass
    public boolean r() {
        B();
        throw new ExceptionsH();
    }

    @Override // gb.KClass
    public boolean t() {
        B();
        throw new ExceptionsH();
    }

    public String toString() {
        return e().toString() + " (Kotlin reflection is not available)";
    }

    @Override // gb.KClass
    public String u() {
        return f20359f.a(e());
    }

    @Override // gb.KClass
    public String v() {
        return f20359f.b(e());
    }

    @Override // gb.KClass
    public Object w() {
        B();
        throw new ExceptionsH();
    }

    @Override // gb.KClass
    public boolean y(Object obj) {
        return f20359f.c(obj, e());
    }
}
