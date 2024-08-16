package jb;

import java.util.concurrent.ConcurrentHashMap;

/* compiled from: CacheByClass.kt */
/* loaded from: classes2.dex */
final class e<V> extends a<V> {

    /* renamed from: a, reason: collision with root package name */
    private final ya.l<Class<?>, V> f13182a;

    /* renamed from: b, reason: collision with root package name */
    private final ConcurrentHashMap<Class<?>, V> f13183b;

    /* JADX WARN: Multi-variable type inference failed */
    public e(ya.l<? super Class<?>, ? extends V> lVar) {
        za.k.e(lVar, "compute");
        this.f13182a = lVar;
        this.f13183b = new ConcurrentHashMap<>();
    }

    @Override // jb.a
    public V a(Class<?> cls) {
        za.k.e(cls, "key");
        ConcurrentHashMap<Class<?>, V> concurrentHashMap = this.f13183b;
        V v7 = (V) concurrentHashMap.get(cls);
        if (v7 != null) {
            return v7;
        }
        V invoke = this.f13182a.invoke(cls);
        V v10 = (V) concurrentHashMap.putIfAbsent(cls, invoke);
        return v10 == null ? invoke : v10;
    }
}
