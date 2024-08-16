package jb;

import ma.p;

/* compiled from: CacheByClass.kt */
/* loaded from: classes2.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private static final boolean f13135a;

    static {
        Object a10;
        try {
            p.a aVar = ma.p.f15184e;
            a10 = ma.p.a(Class.forName("java.lang.ClassValue"));
        } catch (Throwable th) {
            p.a aVar2 = ma.p.f15184e;
            a10 = ma.p.a(ma.q.a(th));
        }
        if (ma.p.d(a10)) {
            p.a aVar3 = ma.p.f15184e;
            a10 = Boolean.TRUE;
        }
        Object a11 = ma.p.a(a10);
        Boolean bool = Boolean.FALSE;
        if (ma.p.c(a11)) {
            a11 = bool;
        }
        f13135a = ((Boolean) a11).booleanValue();
    }

    public static final <V> a<V> a(ya.l<? super Class<?>, ? extends V> lVar) {
        za.k.e(lVar, "compute");
        return f13135a ? new d(lVar) : new e(lVar);
    }
}
