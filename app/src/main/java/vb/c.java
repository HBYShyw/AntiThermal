package vb;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* compiled from: ReflectJavaMember.kt */
/* loaded from: classes2.dex */
final class c {

    /* renamed from: a, reason: collision with root package name */
    public static final c f19218a = new c();

    /* renamed from: b, reason: collision with root package name */
    private static a f19219b;

    /* compiled from: ReflectJavaMember.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final Method f19220a;

        /* renamed from: b, reason: collision with root package name */
        private final Method f19221b;

        public a(Method method, Method method2) {
            this.f19220a = method;
            this.f19221b = method2;
        }

        public final Method a() {
            return this.f19221b;
        }

        public final Method b() {
            return this.f19220a;
        }
    }

    private c() {
    }

    public final a a(Member member) {
        za.k.e(member, "member");
        Class<?> cls = member.getClass();
        try {
            return new a(cls.getMethod("getParameters", new Class[0]), reflectClassUtil.f(cls).loadClass("java.lang.reflect.Parameter").getMethod("getName", new Class[0]));
        } catch (NoSuchMethodException unused) {
            return new a(null, null);
        }
    }

    public final List<String> b(Member member) {
        Method a10;
        za.k.e(member, "member");
        a aVar = f19219b;
        if (aVar == null) {
            synchronized (this) {
                c cVar = f19218a;
                a aVar2 = f19219b;
                if (aVar2 == null) {
                    aVar = cVar.a(member);
                    f19219b = aVar;
                } else {
                    aVar = aVar2;
                }
            }
        }
        Method b10 = aVar.b();
        if (b10 == null || (a10 = aVar.a()) == null) {
            return null;
        }
        Object invoke = b10.invoke(member, new Object[0]);
        za.k.c(invoke, "null cannot be cast to non-null type kotlin.Array<*>");
        Object[] objArr = (Object[]) invoke;
        ArrayList arrayList = new ArrayList(objArr.length);
        for (Object obj : objArr) {
            Object invoke2 = a10.invoke(obj, new Object[0]);
            za.k.c(invoke2, "null cannot be cast to non-null type kotlin.String");
            arrayList.add((String) invoke2);
        }
        return arrayList;
    }
}
