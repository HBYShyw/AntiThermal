package jb;

import com.oplus.backup.sdk.common.utils.Constants;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import nc.JvmMemberSignature;
import vb.reflectClassUtil;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: RuntimeTypeMapper.kt */
/* loaded from: classes2.dex */
public abstract class i {

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class a extends i {

        /* renamed from: a, reason: collision with root package name */
        private final Class<?> f13193a;

        /* renamed from: b, reason: collision with root package name */
        private final List<Method> f13194b;

        /* compiled from: RuntimeTypeMapper.kt */
        /* renamed from: jb.i$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0056a extends Lambda implements ya.l<Method, CharSequence> {

            /* renamed from: e, reason: collision with root package name */
            public static final C0056a f13195e = new C0056a();

            C0056a() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final CharSequence invoke(Method method) {
                Class<?> returnType = method.getReturnType();
                za.k.d(returnType, "it.returnType");
                return reflectClassUtil.b(returnType);
            }
        }

        /* compiled from: Comparisons.kt */
        /* loaded from: classes2.dex */
        public static final class b<T> implements Comparator {
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t7, T t10) {
                int a10;
                a10 = pa.b.a(((Method) t7).getName(), ((Method) t10).getName());
                return a10;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(Class<?> cls) {
            super(null);
            List<Method> W;
            za.k.e(cls, "jClass");
            this.f13193a = cls;
            Method[] declaredMethods = cls.getDeclaredMethods();
            za.k.d(declaredMethods, "jClass.declaredMethods");
            W = _Arrays.W(declaredMethods, new b());
            this.f13194b = W;
        }

        @Override // jb.i
        public String a() {
            String c02;
            c02 = _Collections.c0(this.f13194b, "", "<init>(", ")V", 0, null, C0056a.f13195e, 24, null);
            return c02;
        }

        public final List<Method> b() {
            return this.f13194b;
        }
    }

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class b extends i {

        /* renamed from: a, reason: collision with root package name */
        private final Constructor<?> f13196a;

        /* compiled from: RuntimeTypeMapper.kt */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.l<Class<?>, CharSequence> {

            /* renamed from: e, reason: collision with root package name */
            public static final a f13197e = new a();

            a() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final CharSequence invoke(Class<?> cls) {
                za.k.d(cls, "it");
                return reflectClassUtil.b(cls);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(Constructor<?> constructor) {
            super(null);
            za.k.e(constructor, "constructor");
            this.f13196a = constructor;
        }

        @Override // jb.i
        public String a() {
            String O;
            Class<?>[] parameterTypes = this.f13196a.getParameterTypes();
            za.k.d(parameterTypes, "constructor.parameterTypes");
            O = _Arrays.O(parameterTypes, "", "<init>(", ")V", 0, null, a.f13197e, 24, null);
            return O;
        }

        public final Constructor<?> b() {
            return this.f13196a;
        }
    }

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class c extends i {

        /* renamed from: a, reason: collision with root package name */
        private final Method f13198a;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public c(Method method) {
            super(null);
            za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
            this.f13198a = method;
        }

        @Override // jb.i
        public String a() {
            return m0.a(this.f13198a);
        }

        public final Method b() {
            return this.f13198a;
        }
    }

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class d extends i {

        /* renamed from: a, reason: collision with root package name */
        private final JvmMemberSignature.b f13199a;

        /* renamed from: b, reason: collision with root package name */
        private final String f13200b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public d(JvmMemberSignature.b bVar) {
            super(null);
            za.k.e(bVar, "signature");
            this.f13199a = bVar;
            this.f13200b = bVar.a();
        }

        @Override // jb.i
        public String a() {
            return this.f13200b;
        }

        public final String b() {
            return this.f13199a.b();
        }
    }

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class e extends i {

        /* renamed from: a, reason: collision with root package name */
        private final JvmMemberSignature.b f13201a;

        /* renamed from: b, reason: collision with root package name */
        private final String f13202b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public e(JvmMemberSignature.b bVar) {
            super(null);
            za.k.e(bVar, "signature");
            this.f13201a = bVar;
            this.f13202b = bVar.a();
        }

        @Override // jb.i
        public String a() {
            return this.f13202b;
        }

        public final String b() {
            return this.f13201a.b();
        }

        public final String c() {
            return this.f13201a.c();
        }
    }

    private i() {
    }

    public /* synthetic */ i(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract String a();
}
