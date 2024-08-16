package qd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;

/* compiled from: WrappedValues.java */
/* renamed from: qd.g, reason: use source file name */
/* loaded from: classes2.dex */
public class WrappedValues {

    /* renamed from: a, reason: collision with root package name */
    private static final Object f17438a = new a();

    /* renamed from: b, reason: collision with root package name */
    public static volatile boolean f17439b = false;

    /* compiled from: WrappedValues.java */
    /* renamed from: qd.g$a */
    /* loaded from: classes2.dex */
    static class a {
        a() {
        }

        public String toString() {
            return "NULL_VALUE";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: WrappedValues.java */
    /* renamed from: qd.g$b */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final Throwable f17440a;

        /* synthetic */ b(Throwable th, a aVar) {
            this(th);
        }

        private static /* synthetic */ void a(int i10) {
            String str = i10 != 1 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
            Object[] objArr = new Object[i10 != 1 ? 3 : 2];
            if (i10 != 1) {
                objArr[0] = "throwable";
            } else {
                objArr[0] = "kotlin/reflect/jvm/internal/impl/utils/WrappedValues$ThrowableWrapper";
            }
            if (i10 != 1) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/utils/WrappedValues$ThrowableWrapper";
            } else {
                objArr[1] = "getThrowable";
            }
            if (i10 != 1) {
                objArr[2] = "<init>";
            }
            String format = String.format(str, objArr);
            if (i10 == 1) {
                throw new IllegalStateException(format);
            }
        }

        public Throwable b() {
            Throwable th = this.f17440a;
            if (th == null) {
                a(1);
            }
            return th;
        }

        public String toString() {
            return this.f17440a.toString();
        }

        private b(Throwable th) {
            if (th == null) {
                a(0);
            }
            this.f17440a = th;
        }
    }

    /* compiled from: WrappedValues.java */
    /* renamed from: qd.g$c */
    /* loaded from: classes2.dex */
    public static class c extends RuntimeException {
        public c(Throwable th) {
            super("Rethrow stored exception", th);
        }
    }

    private static /* synthetic */ void a(int i10) {
        String str = (i10 == 1 || i10 == 2) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 1 || i10 == 2) ? 2 : 3];
        if (i10 == 1 || i10 == 2) {
            objArr[0] = "kotlin/reflect/jvm/internal/impl/utils/WrappedValues";
        } else if (i10 != 3) {
            objArr[0] = ThermalBaseConfig.Item.ATTR_VALUE;
        } else {
            objArr[0] = "throwable";
        }
        if (i10 == 1 || i10 == 2) {
            objArr[1] = "escapeNull";
        } else {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/utils/WrappedValues";
        }
        if (i10 != 1 && i10 != 2) {
            if (i10 == 3) {
                objArr[2] = "escapeThrowable";
            } else if (i10 != 4) {
                objArr[2] = "unescapeNull";
            } else {
                objArr[2] = "unescapeExceptionOrNull";
            }
        }
        String format = String.format(str, objArr);
        if (i10 != 1 && i10 != 2) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    public static <V> Object b(V v7) {
        if (v7 == null && (v7 = (V) f17438a) == null) {
            a(1);
        }
        return v7;
    }

    public static Object c(Throwable th) {
        if (th == null) {
            a(3);
        }
        return new b(th, null);
    }

    public static <V> V d(Object obj) {
        if (obj == null) {
            a(4);
        }
        return (V) e(f(obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <V> V e(Object obj) {
        if (obj == 0) {
            a(0);
        }
        if (obj == f17438a) {
            return null;
        }
        return obj;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <V> V f(Object obj) {
        if (!(obj instanceof b)) {
            return obj;
        }
        Throwable b10 = ((b) obj).b();
        if (f17439b && exceptionUtils.a(b10)) {
            throw new c(b10);
        }
        throw exceptionUtils.b(b10);
    }
}
