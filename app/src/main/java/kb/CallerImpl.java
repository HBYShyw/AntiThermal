package kb;

import com.oplus.backup.sdk.common.utils.Constants;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import kb.e;
import kotlin.collections._Arrays;
import kotlin.collections._ArraysJvm;
import kotlin.collections.r;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.SpreadBuilder;

/* compiled from: CallerImpl.kt */
/* renamed from: kb.f, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class CallerImpl<M extends Member> implements kb.e<M> {

    /* renamed from: e, reason: collision with root package name */
    public static final d f14255e = new d(null);

    /* renamed from: a, reason: collision with root package name */
    private final M f14256a;

    /* renamed from: b, reason: collision with root package name */
    private final Type f14257b;

    /* renamed from: c, reason: collision with root package name */
    private final Class<?> f14258c;

    /* renamed from: d, reason: collision with root package name */
    private final List<Type> f14259d;

    /* compiled from: CallerImpl.kt */
    /* renamed from: kb.f$a */
    /* loaded from: classes2.dex */
    public static final class a extends CallerImpl<Constructor<?>> implements kb.d {

        /* renamed from: f, reason: collision with root package name */
        private final Object f14260f;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public a(Constructor<?> constructor, Object obj) {
            super(constructor, r3, null, (Type[]) r0, null);
            Object[] k10;
            Object[] objArr;
            za.k.e(constructor, "constructor");
            Class<?> declaringClass = constructor.getDeclaringClass();
            za.k.d(declaringClass, "constructor.declaringClass");
            Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            za.k.d(genericParameterTypes, "constructor.genericParameterTypes");
            if (genericParameterTypes.length <= 2) {
                objArr = new Type[0];
            } else {
                k10 = _ArraysJvm.k(genericParameterTypes, 1, genericParameterTypes.length - 1);
                objArr = k10;
            }
            this.f14260f = obj;
        }

        @Override // kb.e
        public Object d(Object[] objArr) {
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            c(objArr);
            Constructor<?> b10 = b();
            SpreadBuilder spreadBuilder = new SpreadBuilder(3);
            spreadBuilder.a(this.f14260f);
            spreadBuilder.b(objArr);
            spreadBuilder.a(null);
            return b10.newInstance(spreadBuilder.d(new Object[spreadBuilder.c()]));
        }
    }

    /* compiled from: CallerImpl.kt */
    /* renamed from: kb.f$b */
    /* loaded from: classes2.dex */
    public static final class b extends CallerImpl<Constructor<?>> {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public b(Constructor<?> constructor) {
            super(constructor, r3, null, (Type[]) r0, null);
            Object[] k10;
            Object[] objArr;
            za.k.e(constructor, "constructor");
            Class<?> declaringClass = constructor.getDeclaringClass();
            za.k.d(declaringClass, "constructor.declaringClass");
            Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            za.k.d(genericParameterTypes, "constructor.genericParameterTypes");
            if (genericParameterTypes.length <= 1) {
                objArr = new Type[0];
            } else {
                k10 = _ArraysJvm.k(genericParameterTypes, 0, genericParameterTypes.length - 1);
                objArr = k10;
            }
        }

        @Override // kb.e
        public Object d(Object[] objArr) {
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            c(objArr);
            Constructor<?> b10 = b();
            SpreadBuilder spreadBuilder = new SpreadBuilder(2);
            spreadBuilder.b(objArr);
            spreadBuilder.a(null);
            return b10.newInstance(spreadBuilder.d(new Object[spreadBuilder.c()]));
        }
    }

    /* compiled from: CallerImpl.kt */
    /* renamed from: kb.f$c */
    /* loaded from: classes2.dex */
    public static final class c extends CallerImpl<Constructor<?>> implements kb.d {

        /* renamed from: f, reason: collision with root package name */
        private final Object f14261f;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public c(Constructor<?> constructor, Object obj) {
            super(constructor, r3, null, r5, null);
            za.k.e(constructor, "constructor");
            Class<?> declaringClass = constructor.getDeclaringClass();
            za.k.d(declaringClass, "constructor.declaringClass");
            Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            za.k.d(genericParameterTypes, "constructor.genericParameterTypes");
            this.f14261f = obj;
        }

        @Override // kb.e
        public Object d(Object[] objArr) {
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            c(objArr);
            Constructor<?> b10 = b();
            SpreadBuilder spreadBuilder = new SpreadBuilder(2);
            spreadBuilder.a(this.f14261f);
            spreadBuilder.b(objArr);
            return b10.newInstance(spreadBuilder.d(new Object[spreadBuilder.c()]));
        }
    }

    /* compiled from: CallerImpl.kt */
    /* renamed from: kb.f$d */
    /* loaded from: classes2.dex */
    public static final class d {
        private d() {
        }

        public /* synthetic */ d(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: CallerImpl.kt */
    /* renamed from: kb.f$e */
    /* loaded from: classes2.dex */
    public static final class e extends CallerImpl<Constructor<?>> {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public e(Constructor<?> constructor) {
            super(constructor, r3, r4, r5, null);
            za.k.e(constructor, "constructor");
            Class<?> declaringClass = constructor.getDeclaringClass();
            za.k.d(declaringClass, "constructor.declaringClass");
            Class<?> declaringClass2 = constructor.getDeclaringClass();
            Class<?> declaringClass3 = declaringClass2.getDeclaringClass();
            Class<?> cls = (declaringClass3 == null || Modifier.isStatic(declaringClass2.getModifiers())) ? null : declaringClass3;
            Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            za.k.d(genericParameterTypes, "constructor.genericParameterTypes");
        }

        @Override // kb.e
        public Object d(Object[] objArr) {
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            c(objArr);
            return b().newInstance(Arrays.copyOf(objArr, objArr.length));
        }
    }

    /* compiled from: CallerImpl.kt */
    /* renamed from: kb.f$f */
    /* loaded from: classes2.dex */
    public static abstract class f extends CallerImpl<Field> {

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$f$a */
        /* loaded from: classes2.dex */
        public static final class a extends f implements kb.d {

            /* renamed from: f, reason: collision with root package name */
            private final Object f14262f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public a(Field field, Object obj) {
                super(field, false, null);
                za.k.e(field, "field");
                this.f14262f = obj;
            }

            @Override // kb.CallerImpl.f, kb.e
            public Object d(Object[] objArr) {
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                c(objArr);
                return b().get(this.f14262f);
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$f$b */
        /* loaded from: classes2.dex */
        public static final class b extends f implements kb.d {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public b(Field field) {
                super(field, false, null);
                za.k.e(field, "field");
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$f$c */
        /* loaded from: classes2.dex */
        public static final class c extends f {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public c(Field field) {
                super(field, true, null);
                za.k.e(field, "field");
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$f$d */
        /* loaded from: classes2.dex */
        public static final class d extends f {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public d(Field field) {
                super(field, true, null);
                za.k.e(field, "field");
            }

            @Override // kb.CallerImpl
            public void c(Object[] objArr) {
                Object B;
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                super.c(objArr);
                B = _Arrays.B(objArr);
                e(B);
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$f$e */
        /* loaded from: classes2.dex */
        public static final class e extends f {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public e(Field field) {
                super(field, false, null);
                za.k.e(field, "field");
            }
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private f(Field field, boolean z10) {
            super(field, r2, z10 ? field.getDeclaringClass() : null, new Type[0], null);
            Type genericType = field.getGenericType();
            za.k.d(genericType, "field.genericType");
        }

        public /* synthetic */ f(Field field, boolean z10, DefaultConstructorMarker defaultConstructorMarker) {
            this(field, z10);
        }

        @Override // kb.e
        public Object d(Object[] objArr) {
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            c(objArr);
            return b().get(g() != null ? _Arrays.A(objArr) : null);
        }
    }

    /* compiled from: CallerImpl.kt */
    /* renamed from: kb.f$g */
    /* loaded from: classes2.dex */
    public static abstract class g extends CallerImpl<Field> {

        /* renamed from: f, reason: collision with root package name */
        private final boolean f14263f;

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$g$a */
        /* loaded from: classes2.dex */
        public static final class a extends g implements kb.d {

            /* renamed from: g, reason: collision with root package name */
            private final Object f14264g;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public a(Field field, boolean z10, Object obj) {
                super(field, z10, false, null);
                za.k.e(field, "field");
                this.f14264g = obj;
            }

            @Override // kb.CallerImpl.g, kb.e
            public Object d(Object[] objArr) {
                Object A;
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                c(objArr);
                Field b10 = b();
                Object obj = this.f14264g;
                A = _Arrays.A(objArr);
                b10.set(obj, A);
                return Unit.f15173a;
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$g$b */
        /* loaded from: classes2.dex */
        public static final class b extends g implements kb.d {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public b(Field field, boolean z10) {
                super(field, z10, false, null);
                za.k.e(field, "field");
            }

            @Override // kb.CallerImpl.g, kb.e
            public Object d(Object[] objArr) {
                Object P;
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                c(objArr);
                Field b10 = b();
                P = _Arrays.P(objArr);
                b10.set(null, P);
                return Unit.f15173a;
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$g$c */
        /* loaded from: classes2.dex */
        public static final class c extends g {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public c(Field field, boolean z10) {
                super(field, z10, true, null);
                za.k.e(field, "field");
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$g$d */
        /* loaded from: classes2.dex */
        public static final class d extends g {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public d(Field field, boolean z10) {
                super(field, z10, true, null);
                za.k.e(field, "field");
            }

            @Override // kb.CallerImpl.g, kb.CallerImpl
            public void c(Object[] objArr) {
                Object B;
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                super.c(objArr);
                B = _Arrays.B(objArr);
                e(B);
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$g$e */
        /* loaded from: classes2.dex */
        public static final class e extends g {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public e(Field field, boolean z10) {
                super(field, z10, false, null);
                za.k.e(field, "field");
            }
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private g(Field field, boolean z10, boolean z11) {
            super(field, r2, r9, new Type[]{r0}, null);
            Class cls = Void.TYPE;
            za.k.d(cls, "TYPE");
            Class<?> declaringClass = z11 ? field.getDeclaringClass() : null;
            Type genericType = field.getGenericType();
            za.k.d(genericType, "field.genericType");
            this.f14263f = z10;
        }

        public /* synthetic */ g(Field field, boolean z10, boolean z11, DefaultConstructorMarker defaultConstructorMarker) {
            this(field, z10, z11);
        }

        @Override // kb.CallerImpl
        public void c(Object[] objArr) {
            Object P;
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            super.c(objArr);
            if (this.f14263f) {
                P = _Arrays.P(objArr);
                if (P == null) {
                    throw new IllegalArgumentException("null is not allowed as a value for this property.");
                }
            }
        }

        @Override // kb.e
        public Object d(Object[] objArr) {
            Object P;
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            c(objArr);
            Field b10 = b();
            Object A = g() != null ? _Arrays.A(objArr) : null;
            P = _Arrays.P(objArr);
            b10.set(A, P);
            return Unit.f15173a;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0025, code lost:
    
        if (r1 == null) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private CallerImpl(M m10, Type type, Class<?> cls, Type[] typeArr) {
        List<Type> f02;
        this.f14256a = m10;
        this.f14257b = type;
        this.f14258c = cls;
        if (cls != null) {
            SpreadBuilder spreadBuilder = new SpreadBuilder(2);
            spreadBuilder.a(cls);
            spreadBuilder.b(typeArr);
            f02 = r.m(spreadBuilder.d(new Type[spreadBuilder.c()]));
        }
        f02 = _Arrays.f0(typeArr);
        this.f14259d = f02;
    }

    public /* synthetic */ CallerImpl(Member member, Type type, Class cls, Type[] typeArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(member, type, cls, typeArr);
    }

    @Override // kb.e
    public List<Type> a() {
        return this.f14259d;
    }

    @Override // kb.e
    public final M b() {
        return this.f14256a;
    }

    public void c(Object[] objArr) {
        e.a.a(this, objArr);
    }

    protected final void e(Object obj) {
        if (obj == null || !this.f14256a.getDeclaringClass().isInstance(obj)) {
            throw new IllegalArgumentException("An object member requires the object instance passed as the first argument.");
        }
    }

    @Override // kb.e
    public final Type f() {
        return this.f14257b;
    }

    public final Class<?> g() {
        return this.f14258c;
    }

    /* compiled from: CallerImpl.kt */
    /* renamed from: kb.f$h */
    /* loaded from: classes2.dex */
    public static abstract class h extends CallerImpl<Method> {

        /* renamed from: f, reason: collision with root package name */
        private final boolean f14265f;

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$h$a */
        /* loaded from: classes2.dex */
        public static final class a extends h implements kb.d {

            /* renamed from: g, reason: collision with root package name */
            private final Object f14266g;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public a(Method method, Object obj) {
                super(method, false, null, 4, null);
                za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
                this.f14266g = obj;
            }

            @Override // kb.e
            public Object d(Object[] objArr) {
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                c(objArr);
                return h(this.f14266g, objArr);
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$h$b */
        /* loaded from: classes2.dex */
        public static final class b extends h implements kb.d {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public b(Method method) {
                super(method, false, null, 4, null);
                za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
            }

            @Override // kb.e
            public Object d(Object[] objArr) {
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                c(objArr);
                return h(null, objArr);
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$h$c */
        /* loaded from: classes2.dex */
        public static final class c extends h implements kb.d {

            /* renamed from: g, reason: collision with root package name */
            private final Object f14267g;

            /* JADX WARN: Illegal instructions before constructor call */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public c(Method method, Object obj) {
                super(method, false, (Type[]) (r0.length <= 1 ? new Type[0] : _ArraysJvm.k(r0, 1, r0.length)), null);
                za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
                Type[] genericParameterTypes = method.getGenericParameterTypes();
                za.k.d(genericParameterTypes, "method.genericParameterTypes");
                this.f14267g = obj;
            }

            @Override // kb.e
            public Object d(Object[] objArr) {
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                c(objArr);
                SpreadBuilder spreadBuilder = new SpreadBuilder(2);
                spreadBuilder.a(this.f14267g);
                spreadBuilder.b(objArr);
                return h(null, spreadBuilder.d(new Object[spreadBuilder.c()]));
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$h$d */
        /* loaded from: classes2.dex */
        public static final class d extends h {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public d(Method method) {
                super(method, false, null, 6, null);
                za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
            }

            @Override // kb.e
            public Object d(Object[] objArr) {
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                c(objArr);
                return h(objArr[0], objArr.length <= 1 ? new Object[0] : _ArraysJvm.k(objArr, 1, objArr.length));
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$h$e */
        /* loaded from: classes2.dex */
        public static final class e extends h {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public e(Method method) {
                super(method, true, null, 4, null);
                za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
            }

            @Override // kb.e
            public Object d(Object[] objArr) {
                Object B;
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                c(objArr);
                B = _Arrays.B(objArr);
                e(B);
                return h(null, objArr.length <= 1 ? new Object[0] : _ArraysJvm.k(objArr, 1, objArr.length));
            }
        }

        /* compiled from: CallerImpl.kt */
        /* renamed from: kb.f$h$f */
        /* loaded from: classes2.dex */
        public static final class f extends h {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public f(Method method) {
                super(method, false, null, 6, null);
                za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
            }

            @Override // kb.e
            public Object d(Object[] objArr) {
                za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
                c(objArr);
                return h(null, objArr);
            }
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ h(Method method, boolean z10, Type[] typeArr, int i10, DefaultConstructorMarker defaultConstructorMarker) {
            this(method, z10, typeArr, null);
            z10 = (i10 & 2) != 0 ? !Modifier.isStatic(method.getModifiers()) : z10;
            if ((i10 & 4) != 0) {
                typeArr = method.getGenericParameterTypes();
                za.k.d(typeArr, "method.genericParameterTypes");
            }
        }

        public /* synthetic */ h(Method method, boolean z10, Type[] typeArr, DefaultConstructorMarker defaultConstructorMarker) {
            this(method, z10, typeArr);
        }

        protected final Object h(Object obj, Object[] objArr) {
            za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
            return this.f14265f ? Unit.f15173a : b().invoke(obj, Arrays.copyOf(objArr, objArr.length));
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private h(Method method, boolean z10, Type[] typeArr) {
            super(method, r2, z10 ? method.getDeclaringClass() : null, typeArr, null);
            Type genericReturnType = method.getGenericReturnType();
            za.k.d(genericReturnType, "method.genericReturnType");
            this.f14265f = za.k.a(f(), Void.TYPE);
        }
    }
}
