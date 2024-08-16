package vb;

import java.lang.reflect.Method;

/* compiled from: ReflectJavaClass.kt */
/* loaded from: classes2.dex */
final class b {

    /* renamed from: a, reason: collision with root package name */
    public static final b f19208a = new b();

    /* renamed from: b, reason: collision with root package name */
    private static a f19209b;

    /* compiled from: ReflectJavaClass.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final Method f19210a;

        /* renamed from: b, reason: collision with root package name */
        private final Method f19211b;

        /* renamed from: c, reason: collision with root package name */
        private final Method f19212c;

        /* renamed from: d, reason: collision with root package name */
        private final Method f19213d;

        public a(Method method, Method method2, Method method3, Method method4) {
            this.f19210a = method;
            this.f19211b = method2;
            this.f19212c = method3;
            this.f19213d = method4;
        }

        public final Method a() {
            return this.f19211b;
        }

        public final Method b() {
            return this.f19213d;
        }

        public final Method c() {
            return this.f19212c;
        }

        public final Method d() {
            return this.f19210a;
        }
    }

    private b() {
    }

    private final a a() {
        try {
            return new a(Class.class.getMethod("isSealed", new Class[0]), Class.class.getMethod("getPermittedSubclasses", new Class[0]), Class.class.getMethod("isRecord", new Class[0]), Class.class.getMethod("getRecordComponents", new Class[0]));
        } catch (NoSuchMethodException unused) {
            return new a(null, null, null, null);
        }
    }

    private final a b() {
        a aVar = f19209b;
        if (aVar != null) {
            return aVar;
        }
        a a10 = a();
        f19209b = a10;
        return a10;
    }

    public final Class<?>[] c(Class<?> cls) {
        za.k.e(cls, "clazz");
        Method a10 = b().a();
        if (a10 == null) {
            return null;
        }
        Object invoke = a10.invoke(cls, new Object[0]);
        za.k.c(invoke, "null cannot be cast to non-null type kotlin.Array<java.lang.Class<*>>");
        return (Class[]) invoke;
    }

    public final Object[] d(Class<?> cls) {
        za.k.e(cls, "clazz");
        Method b10 = b().b();
        if (b10 == null) {
            return null;
        }
        return (Object[]) b10.invoke(cls, new Object[0]);
    }

    public final Boolean e(Class<?> cls) {
        za.k.e(cls, "clazz");
        Method c10 = b().c();
        if (c10 == null) {
            return null;
        }
        Object invoke = c10.invoke(cls, new Object[0]);
        za.k.c(invoke, "null cannot be cast to non-null type kotlin.Boolean");
        return (Boolean) invoke;
    }

    public final Boolean f(Class<?> cls) {
        za.k.e(cls, "clazz");
        Method d10 = b().d();
        if (d10 == null) {
            return null;
        }
        Object invoke = d10.invoke(cls, new Object[0]);
        za.k.c(invoke, "null cannot be cast to non-null type kotlin.Boolean");
        return (Boolean) invoke;
    }
}
