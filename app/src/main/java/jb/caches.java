package jb;

import gb.KClass;
import gb.KDeclarationContainer;
import gb.KType;
import gb.KTypeProjection;
import hb.KClassifiers;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import za.Lambda;

/* compiled from: caches.kt */
/* renamed from: jb.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class caches {

    /* renamed from: a, reason: collision with root package name */
    private static final jb.a<KClassImpl<? extends Object>> f13156a = jb.b.a(d.f13164e);

    /* renamed from: b, reason: collision with root package name */
    private static final jb.a<KPackageImpl> f13157b = jb.b.a(e.f13165e);

    /* renamed from: c, reason: collision with root package name */
    private static final jb.a<KType> f13158c = jb.b.a(a.f13161e);

    /* renamed from: d, reason: collision with root package name */
    private static final jb.a<KType> f13159d = jb.b.a(c.f13163e);

    /* renamed from: e, reason: collision with root package name */
    private static final jb.a<ConcurrentHashMap<ma.o<List<KTypeProjection>, Boolean>, KType>> f13160e = jb.b.a(b.f13162e);

    /* compiled from: caches.kt */
    /* renamed from: jb.c$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<Class<?>, KType> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f13161e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final KType invoke(Class<?> cls) {
            List j10;
            List j11;
            za.k.e(cls, "it");
            KClassImpl a10 = caches.a(cls);
            j10 = kotlin.collections.r.j();
            j11 = kotlin.collections.r.j();
            return KClassifiers.b(a10, j10, false, j11);
        }
    }

    /* compiled from: caches.kt */
    /* renamed from: jb.c$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<Class<?>, ConcurrentHashMap<ma.o<? extends List<? extends KTypeProjection>, ? extends Boolean>, KType>> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f13162e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ConcurrentHashMap<ma.o<List<KTypeProjection>, Boolean>, KType> invoke(Class<?> cls) {
            za.k.e(cls, "it");
            return new ConcurrentHashMap<>();
        }
    }

    /* compiled from: caches.kt */
    /* renamed from: jb.c$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.l<Class<?>, KType> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f13163e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final KType invoke(Class<?> cls) {
            List j10;
            List j11;
            za.k.e(cls, "it");
            KClassImpl a10 = caches.a(cls);
            j10 = kotlin.collections.r.j();
            j11 = kotlin.collections.r.j();
            return KClassifiers.b(a10, j10, true, j11);
        }
    }

    /* compiled from: caches.kt */
    /* renamed from: jb.c$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.l<Class<?>, KClassImpl<? extends Object>> {

        /* renamed from: e, reason: collision with root package name */
        public static final d f13164e = new d();

        d() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final KClassImpl<? extends Object> invoke(Class<?> cls) {
            za.k.e(cls, "it");
            return new KClassImpl<>(cls);
        }
    }

    /* compiled from: caches.kt */
    /* renamed from: jb.c$e */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.l<Class<?>, KPackageImpl> {

        /* renamed from: e, reason: collision with root package name */
        public static final e f13165e = new e();

        e() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final KPackageImpl invoke(Class<?> cls) {
            za.k.e(cls, "it");
            return new KPackageImpl(cls);
        }
    }

    public static final <T> KClassImpl<T> a(Class<T> cls) {
        za.k.e(cls, "jClass");
        KClass a10 = f13156a.a(cls);
        za.k.c(a10, "null cannot be cast to non-null type kotlin.reflect.jvm.internal.KClassImpl<T of kotlin.reflect.jvm.internal.CachesKt.getOrCreateKotlinClass>");
        return (KClassImpl) a10;
    }

    public static final <T> KDeclarationContainer b(Class<T> cls) {
        za.k.e(cls, "jClass");
        return f13157b.a(cls);
    }
}
