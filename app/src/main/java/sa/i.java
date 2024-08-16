package sa;

import java.lang.reflect.Method;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DebugMetadata.kt */
/* loaded from: classes2.dex */
public final class i {

    /* renamed from: a, reason: collision with root package name */
    public static final i f18195a = new i();

    /* renamed from: b, reason: collision with root package name */
    private static final a f18196b = new a(null, null, null);

    /* renamed from: c, reason: collision with root package name */
    private static a f18197c;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DebugMetadata.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        public final Method f18198a;

        /* renamed from: b, reason: collision with root package name */
        public final Method f18199b;

        /* renamed from: c, reason: collision with root package name */
        public final Method f18200c;

        public a(Method method, Method method2, Method method3) {
            this.f18198a = method;
            this.f18199b = method2;
            this.f18200c = method3;
        }
    }

    private i() {
    }

    private final a a(sa.a aVar) {
        try {
            a aVar2 = new a(Class.class.getDeclaredMethod("getModule", new Class[0]), aVar.getClass().getClassLoader().loadClass("java.lang.Module").getDeclaredMethod("getDescriptor", new Class[0]), aVar.getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor").getDeclaredMethod("name", new Class[0]));
            f18197c = aVar2;
            return aVar2;
        } catch (Exception unused) {
            a aVar3 = f18196b;
            f18197c = aVar3;
            return aVar3;
        }
    }

    public final String b(sa.a aVar) {
        za.k.e(aVar, "continuation");
        a aVar2 = f18197c;
        if (aVar2 == null) {
            aVar2 = a(aVar);
        }
        if (aVar2 == f18196b) {
            return null;
        }
        Method method = aVar2.f18198a;
        Object invoke = method != null ? method.invoke(aVar.getClass(), new Object[0]) : null;
        if (invoke == null) {
            return null;
        }
        Method method2 = aVar2.f18199b;
        Object invoke2 = method2 != null ? method2.invoke(invoke, new Object[0]) : null;
        if (invoke2 == null) {
            return null;
        }
        Method method3 = aVar2.f18200c;
        Object invoke3 = method3 != null ? method3.invoke(invoke2, new Object[0]) : null;
        if (invoke3 instanceof String) {
            return (String) invoke3;
        }
        return null;
    }
}
