package vb;

import java.lang.reflect.Method;

/* compiled from: ReflectJavaRecordComponent.kt */
/* loaded from: classes2.dex */
final class a {

    /* renamed from: a, reason: collision with root package name */
    public static final a f19203a = new a();

    /* renamed from: b, reason: collision with root package name */
    private static C0111a f19204b;

    /* compiled from: ReflectJavaRecordComponent.kt */
    /* renamed from: vb.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0111a {

        /* renamed from: a, reason: collision with root package name */
        private final Method f19205a;

        /* renamed from: b, reason: collision with root package name */
        private final Method f19206b;

        public C0111a(Method method, Method method2) {
            this.f19205a = method;
            this.f19206b = method2;
        }

        public final Method a() {
            return this.f19206b;
        }

        public final Method b() {
            return this.f19205a;
        }
    }

    private a() {
    }

    private final C0111a a(Object obj) {
        Class<?> cls = obj.getClass();
        try {
            return new C0111a(cls.getMethod("getType", new Class[0]), cls.getMethod("getAccessor", new Class[0]));
        } catch (NoSuchMethodException unused) {
            return new C0111a(null, null);
        }
    }

    private final C0111a b(Object obj) {
        C0111a c0111a = f19204b;
        if (c0111a != null) {
            return c0111a;
        }
        C0111a a10 = a(obj);
        f19204b = a10;
        return a10;
    }

    public final Method c(Object obj) {
        za.k.e(obj, "recordComponent");
        Method a10 = b(obj).a();
        if (a10 == null) {
            return null;
        }
        Object invoke = a10.invoke(obj, new Object[0]);
        za.k.c(invoke, "null cannot be cast to non-null type java.lang.reflect.Method");
        return (Method) invoke;
    }

    public final Class<?> d(Object obj) {
        za.k.e(obj, "recordComponent");
        Method b10 = b(obj).b();
        if (b10 == null) {
            return null;
        }
        Object invoke = b10.invoke(obj, new Object[0]);
        za.k.c(invoke, "null cannot be cast to non-null type java.lang.Class<*>");
        return (Class) invoke;
    }
}
