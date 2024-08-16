package gb;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import ma.NoWhenBranchMatchedException;
import rd.Sequence;
import rd._Sequences;
import sd.StringsJVM;
import xa.JvmClassMapping;
import za.FunctionReferenceImpl;
import za.KTypeBase;

/* compiled from: TypesJVM.kt */
/* loaded from: classes2.dex */
public final class u {

    /* compiled from: TypesJVM.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f11635a;

        static {
            int[] iArr = new int[KVariance.values().length];
            try {
                iArr[KVariance.IN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[KVariance.INVARIANT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[KVariance.OUT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f11635a = iArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TypesJVM.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b extends FunctionReferenceImpl implements ya.l<Class<?>, Class<?>> {

        /* renamed from: n, reason: collision with root package name */
        public static final b f11636n = new b();

        b() {
            super(1, Class.class, "getComponentType", "getComponentType()Ljava/lang/Class;", 0);
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final Class<?> invoke(Class<?> cls) {
            za.k.e(cls, "p0");
            return cls.getComponentType();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Type c(KType kType, boolean z10) {
        Object s02;
        KClassifier c10 = kType.c();
        if (c10 instanceof KTypeParameter) {
            return new t((KTypeParameter) c10);
        }
        if (c10 instanceof KClass) {
            KClass kClass = (KClass) c10;
            Class c11 = z10 ? JvmClassMapping.c(kClass) : JvmClassMapping.b(kClass);
            List<KTypeProjection> b10 = kType.b();
            if (b10.isEmpty()) {
                return c11;
            }
            if (c11.isArray()) {
                if (c11.getComponentType().isPrimitive()) {
                    return c11;
                }
                s02 = _Collections.s0(b10);
                KTypeProjection kTypeProjection = (KTypeProjection) s02;
                if (kTypeProjection != null) {
                    KVariance a10 = kTypeProjection.a();
                    KType b11 = kTypeProjection.b();
                    int i10 = a10 == null ? -1 : a.f11635a[a10.ordinal()];
                    if (i10 == -1 || i10 == 1) {
                        return c11;
                    }
                    if (i10 != 2 && i10 != 3) {
                        throw new NoWhenBranchMatchedException();
                    }
                    za.k.b(b11);
                    Type d10 = d(b11, false, 1, null);
                    return d10 instanceof Class ? c11 : new gb.a(d10);
                }
                throw new IllegalArgumentException("kotlin.Array must have exactly one type argument: " + kType);
            }
            return e(c11, b10);
        }
        throw new UnsupportedOperationException("Unsupported type classifier: " + kType);
    }

    static /* synthetic */ Type d(KType kType, boolean z10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            z10 = false;
        }
        return c(kType, z10);
    }

    private static final Type e(Class<?> cls, List<KTypeProjection> list) {
        int u7;
        int u10;
        int u11;
        Class<?> declaringClass = cls.getDeclaringClass();
        if (declaringClass == null) {
            u11 = kotlin.collections.s.u(list, 10);
            ArrayList arrayList = new ArrayList(u11);
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(g((KTypeProjection) it.next()));
            }
            return new s(cls, null, arrayList);
        }
        if (Modifier.isStatic(cls.getModifiers())) {
            u10 = kotlin.collections.s.u(list, 10);
            ArrayList arrayList2 = new ArrayList(u10);
            Iterator<T> it2 = list.iterator();
            while (it2.hasNext()) {
                arrayList2.add(g((KTypeProjection) it2.next()));
            }
            return new s(cls, declaringClass, arrayList2);
        }
        int length = cls.getTypeParameters().length;
        Type e10 = e(declaringClass, list.subList(length, list.size()));
        List<KTypeProjection> subList = list.subList(0, length);
        u7 = kotlin.collections.s.u(subList, 10);
        ArrayList arrayList3 = new ArrayList(u7);
        Iterator<T> it3 = subList.iterator();
        while (it3.hasNext()) {
            arrayList3.add(g((KTypeProjection) it3.next()));
        }
        return new s(cls, e10, arrayList3);
    }

    public static final Type f(KType kType) {
        Type o10;
        za.k.e(kType, "<this>");
        return (!(kType instanceof KTypeBase) || (o10 = ((KTypeBase) kType).o()) == null) ? d(kType, false, 1, null) : o10;
    }

    private static final Type g(KTypeProjection kTypeProjection) {
        KVariance d10 = kTypeProjection.d();
        if (d10 == null) {
            return v.f11637g.a();
        }
        KType c10 = kTypeProjection.c();
        za.k.b(c10);
        int i10 = a.f11635a[d10.ordinal()];
        if (i10 == 1) {
            return new v(null, c(c10, true));
        }
        if (i10 == 2) {
            return c(c10, true);
        }
        if (i10 == 3) {
            return new v(c(c10, true), null);
        }
        throw new NoWhenBranchMatchedException();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String h(Type type) {
        String name;
        Sequence f10;
        Object v7;
        int k10;
        String v10;
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray()) {
                f10 = rd.l.f(type, b.f11636n);
                StringBuilder sb2 = new StringBuilder();
                v7 = _Sequences.v(f10);
                sb2.append(((Class) v7).getName());
                k10 = _Sequences.k(f10);
                v10 = StringsJVM.v("[]", k10);
                sb2.append(v10);
                name = sb2.toString();
            } else {
                name = cls.getName();
            }
            za.k.d(name, "{\n        if (type.isArr…   } else type.name\n    }");
            return name;
        }
        return type.toString();
    }
}
