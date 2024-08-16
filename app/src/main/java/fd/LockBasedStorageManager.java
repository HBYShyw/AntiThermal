package fd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import ma.Unit;
import qd.WrappedValues;
import qd.exceptionUtils;
import sd.v;

/* compiled from: LockBasedStorageManager.java */
/* renamed from: fd.f, reason: use source file name */
/* loaded from: classes2.dex */
public class LockBasedStorageManager implements StorageManager {

    /* renamed from: d, reason: collision with root package name */
    private static final String f11423d;

    /* renamed from: e, reason: collision with root package name */
    public static final StorageManager f11424e;

    /* renamed from: a, reason: collision with root package name */
    protected final fd.k f11425a;

    /* renamed from: b, reason: collision with root package name */
    private final f f11426b;

    /* renamed from: c, reason: collision with root package name */
    private final String f11427c;

    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$a */
    /* loaded from: classes2.dex */
    static class a extends LockBasedStorageManager {
        a(String str, f fVar, fd.k kVar) {
            super(str, fVar, kVar, null);
        }

        private static /* synthetic */ void j(int i10) {
            String str = i10 != 1 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
            Object[] objArr = new Object[i10 != 1 ? 3 : 2];
            if (i10 != 1) {
                objArr[0] = "source";
            } else {
                objArr[0] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$1";
            }
            if (i10 != 1) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$1";
            } else {
                objArr[1] = "recursionDetectedDefault";
            }
            if (i10 != 1) {
                objArr[2] = "recursionDetectedDefault";
            }
            String format = String.format(str, objArr);
            if (i10 == 1) {
                throw new IllegalStateException(format);
            }
        }

        @Override // fd.LockBasedStorageManager
        protected <K, V> o<V> p(String str, K k10) {
            if (str == null) {
                j(0);
            }
            o<V> a10 = o.a();
            if (a10 == null) {
                j(1);
            }
            return a10;
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$b */
    /* loaded from: classes2.dex */
    class b<T> extends j<T> {

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ Object f11428h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(LockBasedStorageManager lockBasedStorageManager, ya.a aVar, Object obj) {
            super(lockBasedStorageManager, aVar);
            this.f11428h = obj;
        }

        private static /* synthetic */ void a(int i10) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$4", "recursionDetected"));
        }

        @Override // fd.LockBasedStorageManager.h
        protected o<T> c(boolean z10) {
            o<T> d10 = o.d(this.f11428h);
            if (d10 == null) {
                a(0);
            }
            return d10;
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$c */
    /* loaded from: classes2.dex */
    class c<T> extends k<T> {

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ ya.l f11430i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ ya.l f11431j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(LockBasedStorageManager lockBasedStorageManager, ya.a aVar, ya.l lVar, ya.l lVar2) {
            super(lockBasedStorageManager, aVar);
            this.f11430i = lVar;
            this.f11431j = lVar2;
        }

        private static /* synthetic */ void a(int i10) {
            String str = i10 != 2 ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            Object[] objArr = new Object[i10 != 2 ? 2 : 3];
            if (i10 != 2) {
                objArr[0] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$5";
            } else {
                objArr[0] = ThermalBaseConfig.Item.ATTR_VALUE;
            }
            if (i10 != 2) {
                objArr[1] = "recursionDetected";
            } else {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$5";
            }
            if (i10 == 2) {
                objArr[2] = "doPostCompute";
            }
            String format = String.format(str, objArr);
            if (i10 == 2) {
                throw new IllegalArgumentException(format);
            }
        }

        @Override // fd.LockBasedStorageManager.h
        protected o<T> c(boolean z10) {
            ya.l lVar = this.f11430i;
            if (lVar == null) {
                o<T> c10 = super.c(z10);
                if (c10 == null) {
                    a(0);
                }
                return c10;
            }
            o<T> d10 = o.d(lVar.invoke(Boolean.valueOf(z10)));
            if (d10 == null) {
                a(1);
            }
            return d10;
        }

        @Override // fd.LockBasedStorageManager.i
        protected void d(T t7) {
            if (t7 == null) {
                a(2);
            }
            this.f11431j.invoke(t7);
        }
    }

    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$d */
    /* loaded from: classes2.dex */
    private static class d<K, V> extends e<K, V> implements fd.a<K, V> {
        /* synthetic */ d(LockBasedStorageManager lockBasedStorageManager, ConcurrentMap concurrentMap, a aVar) {
            this(lockBasedStorageManager, concurrentMap);
        }

        private static /* synthetic */ void b(int i10) {
            String str = i10 != 3 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
            Object[] objArr = new Object[i10 != 3 ? 3 : 2];
            if (i10 == 1) {
                objArr[0] = "map";
            } else if (i10 == 2) {
                objArr[0] = "computation";
            } else if (i10 != 3) {
                objArr[0] = "storageManager";
            } else {
                objArr[0] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$CacheWithNotNullValuesBasedOnMemoizedFunction";
            }
            if (i10 != 3) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$CacheWithNotNullValuesBasedOnMemoizedFunction";
            } else {
                objArr[1] = "computeIfAbsent";
            }
            if (i10 == 2) {
                objArr[2] = "computeIfAbsent";
            } else if (i10 != 3) {
                objArr[2] = "<init>";
            }
            String format = String.format(str, objArr);
            if (i10 == 3) {
                throw new IllegalStateException(format);
            }
        }

        @Override // fd.LockBasedStorageManager.e, fd.a
        public V a(K k10, ya.a<? extends V> aVar) {
            if (aVar == null) {
                b(2);
            }
            V v7 = (V) super.a(k10, aVar);
            if (v7 == null) {
                b(3);
            }
            return v7;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private d(LockBasedStorageManager lockBasedStorageManager, ConcurrentMap<g<K, V>, Object> concurrentMap) {
            super(lockBasedStorageManager, concurrentMap, null);
            if (lockBasedStorageManager == null) {
                b(0);
            }
            if (concurrentMap == null) {
                b(1);
            }
        }
    }

    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$e */
    /* loaded from: classes2.dex */
    private static class e<K, V> extends l<g<K, V>, V> implements fd.b<K, V> {

        /* compiled from: LockBasedStorageManager.java */
        /* renamed from: fd.f$e$a */
        /* loaded from: classes2.dex */
        class a implements ya.l<g<K, V>, V> {
            a() {
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public V invoke(g<K, V> gVar) {
                return (V) ((g) gVar).f11435b.invoke();
            }
        }

        /* synthetic */ e(LockBasedStorageManager lockBasedStorageManager, ConcurrentMap concurrentMap, a aVar) {
            this(lockBasedStorageManager, concurrentMap);
        }

        private static /* synthetic */ void b(int i10) {
            Object[] objArr = new Object[3];
            if (i10 == 1) {
                objArr[0] = "map";
            } else if (i10 != 2) {
                objArr[0] = "storageManager";
            } else {
                objArr[0] = "computation";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$CacheWithNullableValuesBasedOnMemoizedFunction";
            if (i10 != 2) {
                objArr[2] = "<init>";
            } else {
                objArr[2] = "computeIfAbsent";
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        public V a(K k10, ya.a<? extends V> aVar) {
            if (aVar == null) {
                b(2);
            }
            return invoke(new g(k10, aVar));
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private e(LockBasedStorageManager lockBasedStorageManager, ConcurrentMap<g<K, V>, Object> concurrentMap) {
            super(lockBasedStorageManager, concurrentMap, new a());
            if (lockBasedStorageManager == null) {
                b(0);
            }
            if (concurrentMap == null) {
                b(1);
            }
        }
    }

    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$f */
    /* loaded from: classes2.dex */
    public interface f {

        /* renamed from: a, reason: collision with root package name */
        public static final f f11433a = new a();

        /* compiled from: LockBasedStorageManager.java */
        /* renamed from: fd.f$f$a */
        /* loaded from: classes2.dex */
        static class a implements f {
            a() {
            }

            private static /* synthetic */ void b(int i10) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "throwable", "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$ExceptionHandlingStrategy$1", "handleException"));
            }

            @Override // fd.LockBasedStorageManager.f
            public RuntimeException a(Throwable th) {
                if (th == null) {
                    b(0);
                }
                throw exceptionUtils.b(th);
            }
        }

        RuntimeException a(Throwable th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$g */
    /* loaded from: classes2.dex */
    public static class g<K, V> {

        /* renamed from: a, reason: collision with root package name */
        private final K f11434a;

        /* renamed from: b, reason: collision with root package name */
        private final ya.a<? extends V> f11435b;

        public g(K k10, ya.a<? extends V> aVar) {
            this.f11434a = k10;
            this.f11435b = aVar;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.f11434a.equals(((g) obj).f11434a);
        }

        public int hashCode() {
            return this.f11434a.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$h */
    /* loaded from: classes2.dex */
    public static class h<T> implements fd.j<T> {

        /* renamed from: e, reason: collision with root package name */
        private final LockBasedStorageManager f11436e;

        /* renamed from: f, reason: collision with root package name */
        private final ya.a<? extends T> f11437f;

        /* renamed from: g, reason: collision with root package name */
        private volatile Object f11438g;

        public h(LockBasedStorageManager lockBasedStorageManager, ya.a<? extends T> aVar) {
            if (lockBasedStorageManager == null) {
                a(0);
            }
            if (aVar == null) {
                a(1);
            }
            this.f11438g = n.NOT_COMPUTED;
            this.f11436e = lockBasedStorageManager;
            this.f11437f = aVar;
        }

        private static /* synthetic */ void a(int i10) {
            String str = (i10 == 2 || i10 == 3) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            Object[] objArr = new Object[(i10 == 2 || i10 == 3) ? 2 : 3];
            if (i10 == 1) {
                objArr[0] = "computable";
            } else if (i10 == 2 || i10 == 3) {
                objArr[0] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$LockBasedLazyValue";
            } else {
                objArr[0] = "storageManager";
            }
            if (i10 == 2) {
                objArr[1] = "recursionDetected";
            } else if (i10 != 3) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$LockBasedLazyValue";
            } else {
                objArr[1] = "renderDebugInformation";
            }
            if (i10 != 2 && i10 != 3) {
                objArr[2] = "<init>";
            }
            String format = String.format(str, objArr);
            if (i10 != 2 && i10 != 3) {
                throw new IllegalArgumentException(format);
            }
            throw new IllegalStateException(format);
        }

        protected void b(T t7) {
        }

        protected o<T> c(boolean z10) {
            o<T> p10 = this.f11436e.p("in a lazy value", null);
            if (p10 == null) {
                a(2);
            }
            return p10;
        }

        public boolean e() {
            return (this.f11438g == n.NOT_COMPUTED || this.f11438g == n.COMPUTING) ? false : true;
        }

        @Override // ya.a
        public T invoke() {
            T invoke;
            Object obj = this.f11438g;
            if (!(obj instanceof n)) {
                return (T) WrappedValues.f(obj);
            }
            this.f11436e.f11425a.b();
            try {
                Object obj2 = this.f11438g;
                if (obj2 instanceof n) {
                    n nVar = n.COMPUTING;
                    if (obj2 == nVar) {
                        this.f11438g = n.RECURSION_WAS_DETECTED;
                        o<T> c10 = c(true);
                        if (!c10.c()) {
                            invoke = c10.b();
                        }
                    }
                    if (obj2 == n.RECURSION_WAS_DETECTED) {
                        o<T> c11 = c(false);
                        if (!c11.c()) {
                            invoke = c11.b();
                        }
                    }
                    this.f11438g = nVar;
                    try {
                        invoke = this.f11437f.invoke();
                        b(invoke);
                        this.f11438g = invoke;
                    } catch (Throwable th) {
                        if (!exceptionUtils.a(th)) {
                            if (this.f11438g == n.COMPUTING) {
                                this.f11438g = WrappedValues.c(th);
                            }
                            throw this.f11436e.f11426b.a(th);
                        }
                        this.f11438g = n.NOT_COMPUTED;
                        throw th;
                    }
                } else {
                    invoke = (T) WrappedValues.f(obj2);
                }
                return invoke;
            } finally {
                this.f11436e.f11425a.a();
            }
        }
    }

    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$i */
    /* loaded from: classes2.dex */
    private static abstract class i<T> extends h<T> {

        /* renamed from: h, reason: collision with root package name */
        private volatile SingleThreadValue<T> f11439h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public i(LockBasedStorageManager lockBasedStorageManager, ya.a<? extends T> aVar) {
            super(lockBasedStorageManager, aVar);
            if (lockBasedStorageManager == null) {
                a(0);
            }
            if (aVar == null) {
                a(1);
            }
            this.f11439h = null;
        }

        private static /* synthetic */ void a(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "storageManager";
            } else {
                objArr[0] = "computable";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$LockBasedLazyValueWithPostCompute";
            objArr[2] = "<init>";
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // fd.LockBasedStorageManager.h
        protected final void b(T t7) {
            this.f11439h = new SingleThreadValue<>(t7);
            try {
                d(t7);
            } finally {
                this.f11439h = null;
            }
        }

        protected abstract void d(T t7);

        @Override // fd.LockBasedStorageManager.h, ya.a
        public T invoke() {
            SingleThreadValue<T> singleThreadValue = this.f11439h;
            if (singleThreadValue != null && singleThreadValue.b()) {
                return singleThreadValue.a();
            }
            return (T) super.invoke();
        }
    }

    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$j */
    /* loaded from: classes2.dex */
    private static class j<T> extends h<T> implements fd.i<T> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public j(LockBasedStorageManager lockBasedStorageManager, ya.a<? extends T> aVar) {
            super(lockBasedStorageManager, aVar);
            if (lockBasedStorageManager == null) {
                a(0);
            }
            if (aVar == null) {
                a(1);
            }
        }

        private static /* synthetic */ void a(int i10) {
            String str = i10 != 2 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
            Object[] objArr = new Object[i10 != 2 ? 3 : 2];
            if (i10 == 1) {
                objArr[0] = "computable";
            } else if (i10 != 2) {
                objArr[0] = "storageManager";
            } else {
                objArr[0] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$LockBasedNotNullLazyValue";
            }
            if (i10 != 2) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$LockBasedNotNullLazyValue";
            } else {
                objArr[1] = "invoke";
            }
            if (i10 != 2) {
                objArr[2] = "<init>";
            }
            String format = String.format(str, objArr);
            if (i10 == 2) {
                throw new IllegalStateException(format);
            }
        }

        @Override // fd.LockBasedStorageManager.h, ya.a
        public T invoke() {
            T t7 = (T) super.invoke();
            if (t7 == null) {
                a(2);
            }
            return t7;
        }
    }

    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$k */
    /* loaded from: classes2.dex */
    private static abstract class k<T> extends i<T> implements fd.i<T> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public k(LockBasedStorageManager lockBasedStorageManager, ya.a<? extends T> aVar) {
            super(lockBasedStorageManager, aVar);
            if (lockBasedStorageManager == null) {
                a(0);
            }
            if (aVar == null) {
                a(1);
            }
        }

        private static /* synthetic */ void a(int i10) {
            String str = i10 != 2 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
            Object[] objArr = new Object[i10 != 2 ? 3 : 2];
            if (i10 == 1) {
                objArr[0] = "computable";
            } else if (i10 != 2) {
                objArr[0] = "storageManager";
            } else {
                objArr[0] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$LockBasedNotNullLazyValueWithPostCompute";
            }
            if (i10 != 2) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$LockBasedNotNullLazyValueWithPostCompute";
            } else {
                objArr[1] = "invoke";
            }
            if (i10 != 2) {
                objArr[2] = "<init>";
            }
            String format = String.format(str, objArr);
            if (i10 == 2) {
                throw new IllegalStateException(format);
            }
        }

        @Override // fd.LockBasedStorageManager.i, fd.LockBasedStorageManager.h, ya.a
        public T invoke() {
            T t7 = (T) super.invoke();
            if (t7 == null) {
                a(2);
            }
            return t7;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$l */
    /* loaded from: classes2.dex */
    public static class l<K, V> implements fd.h<K, V> {

        /* renamed from: e, reason: collision with root package name */
        private final LockBasedStorageManager f11440e;

        /* renamed from: f, reason: collision with root package name */
        private final ConcurrentMap<K, Object> f11441f;

        /* renamed from: g, reason: collision with root package name */
        private final ya.l<? super K, ? extends V> f11442g;

        public l(LockBasedStorageManager lockBasedStorageManager, ConcurrentMap<K, Object> concurrentMap, ya.l<? super K, ? extends V> lVar) {
            if (lockBasedStorageManager == null) {
                b(0);
            }
            if (concurrentMap == null) {
                b(1);
            }
            if (lVar == null) {
                b(2);
            }
            this.f11440e = lockBasedStorageManager;
            this.f11441f = concurrentMap;
            this.f11442g = lVar;
        }

        private static /* synthetic */ void b(int i10) {
            String str = (i10 == 3 || i10 == 4) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            Object[] objArr = new Object[(i10 == 3 || i10 == 4) ? 2 : 3];
            if (i10 == 1) {
                objArr[0] = "map";
            } else if (i10 == 2) {
                objArr[0] = "compute";
            } else if (i10 == 3 || i10 == 4) {
                objArr[0] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$MapBasedMemoizedFunction";
            } else {
                objArr[0] = "storageManager";
            }
            if (i10 == 3) {
                objArr[1] = "recursionDetected";
            } else if (i10 != 4) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$MapBasedMemoizedFunction";
            } else {
                objArr[1] = "raceCondition";
            }
            if (i10 != 3 && i10 != 4) {
                objArr[2] = "<init>";
            }
            String format = String.format(str, objArr);
            if (i10 != 3 && i10 != 4) {
                throw new IllegalArgumentException(format);
            }
            throw new IllegalStateException(format);
        }

        private AssertionError c(K k10, Object obj) {
            AssertionError assertionError = (AssertionError) LockBasedStorageManager.q(new AssertionError("Race condition detected on input " + k10 + ". Old value is " + obj + " under " + this.f11440e));
            if (assertionError == null) {
                b(4);
            }
            return assertionError;
        }

        protected o<V> d(K k10, boolean z10) {
            o<V> p10 = this.f11440e.p("", k10);
            if (p10 == null) {
                b(3);
            }
            return p10;
        }

        @Override // ya.l
        public V invoke(K k10) {
            V v7;
            Object obj = this.f11441f.get(k10);
            if (obj != null && obj != n.COMPUTING) {
                return (V) WrappedValues.d(obj);
            }
            this.f11440e.f11425a.b();
            try {
                Object obj2 = this.f11441f.get(k10);
                n nVar = n.COMPUTING;
                if (obj2 == nVar) {
                    obj2 = n.RECURSION_WAS_DETECTED;
                    o<V> d10 = d(k10, true);
                    if (!d10.c()) {
                        v7 = d10.b();
                        return v7;
                    }
                }
                if (obj2 == n.RECURSION_WAS_DETECTED) {
                    o<V> d11 = d(k10, false);
                    if (!d11.c()) {
                        v7 = d11.b();
                        return v7;
                    }
                }
                if (obj2 != null) {
                    v7 = (V) WrappedValues.d(obj2);
                    return v7;
                }
                AssertionError assertionError = null;
                try {
                    this.f11441f.put(k10, nVar);
                    V invoke = this.f11442g.invoke(k10);
                    Object put = this.f11441f.put(k10, WrappedValues.b(invoke));
                    if (put == nVar) {
                        return invoke;
                    }
                    assertionError = c(k10, put);
                    throw assertionError;
                } catch (Throwable th) {
                    if (exceptionUtils.a(th)) {
                        this.f11441f.remove(k10);
                        throw th;
                    }
                    if (th != assertionError) {
                        Object put2 = this.f11441f.put(k10, WrappedValues.c(th));
                        if (put2 != n.COMPUTING) {
                            throw c(k10, put2);
                        }
                        throw this.f11440e.f11426b.a(th);
                    }
                    throw this.f11440e.f11426b.a(th);
                }
            } finally {
                this.f11440e.f11425a.a();
            }
        }

        @Override // fd.h
        public boolean m(K k10) {
            Object obj = this.f11441f.get(k10);
            return (obj == null || obj == n.COMPUTING) ? false : true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$m */
    /* loaded from: classes2.dex */
    public static class m<K, V> extends l<K, V> implements fd.g<K, V> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public m(LockBasedStorageManager lockBasedStorageManager, ConcurrentMap<K, Object> concurrentMap, ya.l<? super K, ? extends V> lVar) {
            super(lockBasedStorageManager, concurrentMap, lVar);
            if (lockBasedStorageManager == null) {
                b(0);
            }
            if (concurrentMap == null) {
                b(1);
            }
            if (lVar == null) {
                b(2);
            }
        }

        private static /* synthetic */ void b(int i10) {
            String str = i10 != 3 ? "Argument for @NotNull parameter '%s' of %s.%s must not be null" : "@NotNull method %s.%s must not return null";
            Object[] objArr = new Object[i10 != 3 ? 3 : 2];
            if (i10 == 1) {
                objArr[0] = "map";
            } else if (i10 == 2) {
                objArr[0] = "compute";
            } else if (i10 != 3) {
                objArr[0] = "storageManager";
            } else {
                objArr[0] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$MapBasedMemoizedFunctionToNotNull";
            }
            if (i10 != 3) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager$MapBasedMemoizedFunctionToNotNull";
            } else {
                objArr[1] = "invoke";
            }
            if (i10 != 3) {
                objArr[2] = "<init>";
            }
            String format = String.format(str, objArr);
            if (i10 == 3) {
                throw new IllegalStateException(format);
            }
        }

        @Override // fd.LockBasedStorageManager.l, ya.l
        public V invoke(K k10) {
            V v7 = (V) super.invoke(k10);
            if (v7 == null) {
                b(3);
            }
            return v7;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$n */
    /* loaded from: classes2.dex */
    public enum n {
        NOT_COMPUTED,
        COMPUTING,
        RECURSION_WAS_DETECTED
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LockBasedStorageManager.java */
    /* renamed from: fd.f$o */
    /* loaded from: classes2.dex */
    public static class o<T> {

        /* renamed from: a, reason: collision with root package name */
        private final T f11447a;

        /* renamed from: b, reason: collision with root package name */
        private final boolean f11448b;

        private o(T t7, boolean z10) {
            this.f11447a = t7;
            this.f11448b = z10;
        }

        public static <T> o<T> a() {
            return new o<>(null, true);
        }

        public static <T> o<T> d(T t7) {
            return new o<>(t7, false);
        }

        public T b() {
            return this.f11447a;
        }

        public boolean c() {
            return this.f11448b;
        }

        public String toString() {
            return c() ? "FALL_THROUGH" : String.valueOf(this.f11447a);
        }
    }

    static {
        String I0;
        I0 = v.I0(LockBasedStorageManager.class.getCanonicalName(), ".", "");
        f11423d = I0;
        f11424e = new a("NO_LOCKS", f.f11433a, fd.e.f11422b);
    }

    /* synthetic */ LockBasedStorageManager(String str, f fVar, fd.k kVar, a aVar) {
        this(str, fVar, kVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void j(int i10) {
        String format;
        String str = (i10 == 10 || i10 == 13 || i10 == 20 || i10 == 37) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 10 || i10 == 13 || i10 == 20 || i10 == 37) ? 2 : 3];
        if (i10 != 1 && i10 != 3 && i10 != 5) {
            if (i10 != 6) {
                switch (i10) {
                    case 8:
                        break;
                    case 9:
                    case 11:
                    case 14:
                    case 16:
                    case 19:
                    case 21:
                        objArr[0] = "compute";
                        break;
                    case 10:
                    case 13:
                    case 20:
                    case 37:
                        objArr[0] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager";
                        break;
                    case 12:
                    case 17:
                    case 25:
                    case 27:
                        objArr[0] = "onRecursiveCall";
                        break;
                    case 15:
                    case 18:
                    case 22:
                        objArr[0] = "map";
                        break;
                    case 23:
                    case 24:
                    case 26:
                    case 28:
                    case 30:
                    case 31:
                    case 32:
                    case 34:
                        objArr[0] = "computable";
                        break;
                    case 29:
                    case 33:
                        objArr[0] = "postCompute";
                        break;
                    case 35:
                        objArr[0] = "source";
                        break;
                    case 36:
                        objArr[0] = "throwable";
                        break;
                    default:
                        objArr[0] = "debugText";
                        break;
                }
            } else {
                objArr[0] = "lock";
            }
            if (i10 != 10 || i10 == 13) {
                objArr[1] = "createMemoizedFunction";
            } else if (i10 == 20) {
                objArr[1] = "createMemoizedFunctionWithNullableValues";
            } else if (i10 != 37) {
                objArr[1] = "kotlin/reflect/jvm/internal/impl/storage/LockBasedStorageManager";
            } else {
                objArr[1] = "sanitizeStackTrace";
            }
            switch (i10) {
                case 4:
                case 5:
                case 6:
                    objArr[2] = "<init>";
                    break;
                case 7:
                case 8:
                    objArr[2] = "replaceExceptionHandling";
                    break;
                case 9:
                case 11:
                case 12:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                    objArr[2] = "createMemoizedFunction";
                    break;
                case 10:
                case 13:
                case 20:
                case 37:
                    break;
                case 19:
                case 21:
                case 22:
                    objArr[2] = "createMemoizedFunctionWithNullableValues";
                    break;
                case 23:
                case 24:
                case 25:
                    objArr[2] = "createLazyValue";
                    break;
                case 26:
                case 27:
                    objArr[2] = "createRecursionTolerantLazyValue";
                    break;
                case 28:
                case 29:
                    objArr[2] = "createLazyValueWithPostCompute";
                    break;
                case 30:
                    objArr[2] = "createNullableLazyValue";
                    break;
                case 31:
                    objArr[2] = "createRecursionTolerantNullableLazyValue";
                    break;
                case 32:
                case 33:
                    objArr[2] = "createNullableLazyValueWithPostCompute";
                    break;
                case 34:
                    objArr[2] = "compute";
                    break;
                case 35:
                    objArr[2] = "recursionDetectedDefault";
                    break;
                case 36:
                    objArr[2] = "sanitizeStackTrace";
                    break;
                default:
                    objArr[2] = "createWithExceptionHandling";
                    break;
            }
            format = String.format(str, objArr);
            if (i10 == 10 && i10 != 13 && i10 != 20 && i10 != 37) {
                throw new IllegalArgumentException(format);
            }
            throw new IllegalStateException(format);
        }
        objArr[0] = "exceptionHandlingStrategy";
        if (i10 != 10) {
        }
        objArr[1] = "createMemoizedFunction";
        switch (i10) {
        }
        format = String.format(str, objArr);
        if (i10 == 10) {
        }
        throw new IllegalStateException(format);
    }

    private static <K> ConcurrentMap<K, Object> m() {
        return new ConcurrentHashMap(3, 1.0f, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends Throwable> T q(T t7) {
        if (t7 == null) {
            j(36);
        }
        StackTraceElement[] stackTrace = t7.getStackTrace();
        int length = stackTrace.length;
        int i10 = -1;
        int i11 = 0;
        while (true) {
            if (i11 >= length) {
                break;
            }
            if (!stackTrace[i11].getClassName().startsWith(f11423d)) {
                i10 = i11;
                break;
            }
            i11++;
        }
        List subList = Arrays.asList(stackTrace).subList(i10, length);
        t7.setStackTrace((StackTraceElement[]) subList.toArray(new StackTraceElement[subList.size()]));
        return t7;
    }

    @Override // fd.StorageManager
    public <T> fd.i<T> a(ya.a<? extends T> aVar, T t7) {
        if (aVar == null) {
            j(26);
        }
        if (t7 == null) {
            j(27);
        }
        return new b(this, aVar, t7);
    }

    @Override // fd.StorageManager
    public <K, V> fd.h<K, V> b(ya.l<? super K, ? extends V> lVar) {
        if (lVar == null) {
            j(19);
        }
        fd.h<K, V> o10 = o(lVar, m());
        if (o10 == null) {
            j(20);
        }
        return o10;
    }

    @Override // fd.StorageManager
    public <K, V> fd.b<K, V> c() {
        return new e(this, m(), null);
    }

    @Override // fd.StorageManager
    public <K, V> fd.g<K, V> d(ya.l<? super K, ? extends V> lVar) {
        if (lVar == null) {
            j(9);
        }
        fd.g<K, V> n10 = n(lVar, m());
        if (n10 == null) {
            j(10);
        }
        return n10;
    }

    @Override // fd.StorageManager
    public <K, V> fd.a<K, V> e() {
        return new d(this, m(), null);
    }

    @Override // fd.StorageManager
    public <T> fd.j<T> f(ya.a<? extends T> aVar) {
        if (aVar == null) {
            j(30);
        }
        return new h(this, aVar);
    }

    @Override // fd.StorageManager
    public <T> fd.i<T> g(ya.a<? extends T> aVar) {
        if (aVar == null) {
            j(23);
        }
        return new j(this, aVar);
    }

    @Override // fd.StorageManager
    public <T> fd.i<T> h(ya.a<? extends T> aVar, ya.l<? super Boolean, ? extends T> lVar, ya.l<? super T, Unit> lVar2) {
        if (aVar == null) {
            j(28);
        }
        if (lVar2 == null) {
            j(29);
        }
        return new c(this, aVar, lVar, lVar2);
    }

    @Override // fd.StorageManager
    public <T> T i(ya.a<? extends T> aVar) {
        if (aVar == null) {
            j(34);
        }
        this.f11425a.b();
        try {
            return aVar.invoke();
        } finally {
        }
    }

    public <K, V> fd.g<K, V> n(ya.l<? super K, ? extends V> lVar, ConcurrentMap<K, Object> concurrentMap) {
        if (lVar == null) {
            j(14);
        }
        if (concurrentMap == null) {
            j(15);
        }
        return new m(this, concurrentMap, lVar);
    }

    public <K, V> fd.h<K, V> o(ya.l<? super K, ? extends V> lVar, ConcurrentMap<K, Object> concurrentMap) {
        if (lVar == null) {
            j(21);
        }
        if (concurrentMap == null) {
            j(22);
        }
        return new l(this, concurrentMap, lVar);
    }

    protected <K, V> o<V> p(String str, K k10) {
        String str2;
        if (str == null) {
            j(35);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Recursion detected ");
        sb2.append(str);
        if (k10 == null) {
            str2 = "";
        } else {
            str2 = "on input: " + k10;
        }
        sb2.append(str2);
        sb2.append(" under ");
        sb2.append(this);
        throw ((AssertionError) q(new AssertionError(sb2.toString())));
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " (" + this.f11427c + ")";
    }

    private LockBasedStorageManager(String str, f fVar, fd.k kVar) {
        if (str == null) {
            j(4);
        }
        if (fVar == null) {
            j(5);
        }
        if (kVar == null) {
            j(6);
        }
        this.f11425a = kVar;
        this.f11426b = fVar;
        this.f11427c = str;
    }

    public LockBasedStorageManager(String str) {
        this(str, (Runnable) null, (ya.l<InterruptedException, Unit>) null);
    }

    public LockBasedStorageManager(String str, Runnable runnable, ya.l<InterruptedException, Unit> lVar) {
        this(str, f.f11433a, fd.k.f11449a.a(runnable, lVar));
    }
}
