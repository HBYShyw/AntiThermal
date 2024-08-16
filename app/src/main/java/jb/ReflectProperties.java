package jb;

import java.lang.ref.SoftReference;

/* compiled from: ReflectProperties.java */
/* renamed from: jb.i0, reason: use source file name */
/* loaded from: classes2.dex */
public class ReflectProperties {

    /* compiled from: ReflectProperties.java */
    /* renamed from: jb.i0$a */
    /* loaded from: classes2.dex */
    public static class a<T> extends c<T> implements ya.a<T> {

        /* renamed from: f, reason: collision with root package name */
        private final ya.a<T> f13203f;

        /* renamed from: g, reason: collision with root package name */
        private volatile SoftReference<Object> f13204g;

        public a(T t7, ya.a<T> aVar) {
            if (aVar == null) {
                d(0);
            }
            this.f13204g = null;
            this.f13203f = aVar;
            if (t7 != null) {
                this.f13204g = new SoftReference<>(a(t7));
            }
        }

        private static /* synthetic */ void d(int i10) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "initializer", "kotlin/reflect/jvm/internal/ReflectProperties$LazySoftVal", "<init>"));
        }

        @Override // jb.ReflectProperties.c, ya.a
        public T invoke() {
            Object obj;
            SoftReference<Object> softReference = this.f13204g;
            if (softReference != null && (obj = softReference.get()) != null) {
                return c(obj);
            }
            T invoke = this.f13203f.invoke();
            this.f13204g = new SoftReference<>(a(invoke));
            return invoke;
        }
    }

    /* compiled from: ReflectProperties.java */
    /* renamed from: jb.i0$b */
    /* loaded from: classes2.dex */
    public static class b<T> extends c<T> {

        /* renamed from: f, reason: collision with root package name */
        private final ya.a<T> f13205f;

        /* renamed from: g, reason: collision with root package name */
        private volatile Object f13206g;

        public b(ya.a<T> aVar) {
            if (aVar == null) {
                d(0);
            }
            this.f13206g = null;
            this.f13205f = aVar;
        }

        private static /* synthetic */ void d(int i10) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "initializer", "kotlin/reflect/jvm/internal/ReflectProperties$LazyVal", "<init>"));
        }

        @Override // jb.ReflectProperties.c, ya.a
        public T invoke() {
            Object obj = this.f13206g;
            if (obj != null) {
                return c(obj);
            }
            T invoke = this.f13205f.invoke();
            this.f13206g = a(invoke);
            return invoke;
        }
    }

    /* compiled from: ReflectProperties.java */
    /* renamed from: jb.i0$c */
    /* loaded from: classes2.dex */
    public static abstract class c<T> {

        /* renamed from: e, reason: collision with root package name */
        private static final Object f13207e = new a();

        /* compiled from: ReflectProperties.java */
        /* renamed from: jb.i0$c$a */
        /* loaded from: classes2.dex */
        static class a {
            a() {
            }
        }

        protected Object a(T t7) {
            return t7 == null ? f13207e : t7;
        }

        public final T b(Object obj, Object obj2) {
            return invoke();
        }

        /* JADX WARN: Multi-variable type inference failed */
        protected T c(Object obj) {
            if (obj == f13207e) {
                return null;
            }
            return obj;
        }

        public abstract T invoke();
    }

    private static /* synthetic */ void a(int i10) {
        Object[] objArr = new Object[3];
        objArr[0] = "initializer";
        objArr[1] = "kotlin/reflect/jvm/internal/ReflectProperties";
        if (i10 == 1 || i10 == 2) {
            objArr[2] = "lazySoft";
        } else {
            objArr[2] = "lazy";
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
    }

    public static <T> b<T> b(ya.a<T> aVar) {
        if (aVar == null) {
            a(0);
        }
        return new b<>(aVar);
    }

    public static <T> a<T> c(T t7, ya.a<T> aVar) {
        if (aVar == null) {
            a(1);
        }
        return new a<>(t7, aVar);
    }

    public static <T> a<T> d(ya.a<T> aVar) {
        if (aVar == null) {
            a(2);
        }
        return c(null, aVar);
    }
}
