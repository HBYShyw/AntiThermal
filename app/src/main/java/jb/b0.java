package jb;

import gb.KFunction;
import gb.i;
import gb.l;
import ib.KCallablesJvm;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import jb.ReflectProperties;
import jb.j;
import ma.NoWhenBranchMatchedException;
import ma.Unit;
import mc.JvmProtoBuf;
import nc.JvmMemberSignature;
import nc.JvmProtoBufUtil;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.PropertyGetterDescriptor;
import pb.PropertySetterDescriptor;
import qb.g;
import sc.DescriptorFactory;
import yb.DescriptorsJvmAbiUtil;
import za.CallableReference;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: KPropertyImpl.kt */
/* loaded from: classes2.dex */
public abstract class b0<V> extends KCallableImpl<V> implements gb.l<V> {

    /* renamed from: o, reason: collision with root package name */
    public static final b f13136o = new b(null);

    /* renamed from: p, reason: collision with root package name */
    private static final Object f13137p = new Object();

    /* renamed from: i, reason: collision with root package name */
    private final KDeclarationContainerImpl f13138i;

    /* renamed from: j, reason: collision with root package name */
    private final String f13139j;

    /* renamed from: k, reason: collision with root package name */
    private final String f13140k;

    /* renamed from: l, reason: collision with root package name */
    private final Object f13141l;

    /* renamed from: m, reason: collision with root package name */
    private final ReflectProperties.b<Field> f13142m;

    /* renamed from: n, reason: collision with root package name */
    private final ReflectProperties.a<PropertyDescriptor> f13143n;

    /* compiled from: KPropertyImpl.kt */
    /* loaded from: classes2.dex */
    public static abstract class a<PropertyType, ReturnType> extends KCallableImpl<ReturnType> implements KFunction<ReturnType>, l.a<PropertyType> {
        @Override // jb.KCallableImpl
        public KDeclarationContainerImpl G() {
            return s().G();
        }

        @Override // jb.KCallableImpl
        public kb.e<?> H() {
            return null;
        }

        @Override // jb.KCallableImpl
        public boolean K() {
            return s().K();
        }

        public abstract PropertyAccessorDescriptor L();

        /* renamed from: M */
        public abstract b0<PropertyType> s();
    }

    /* compiled from: KPropertyImpl.kt */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: KPropertyImpl.kt */
    /* loaded from: classes2.dex */
    public static abstract class c<V> extends a<V, V> implements l.b<V> {

        /* renamed from: k, reason: collision with root package name */
        static final /* synthetic */ gb.l<Object>[] f13144k = {Reflection.g(new PropertyReference1Impl(Reflection.b(c.class), "descriptor", "getDescriptor()Lorg/jetbrains/kotlin/descriptors/PropertyGetterDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(c.class), "caller", "getCaller()Lkotlin/reflect/jvm/internal/calls/Caller;"))};

        /* renamed from: i, reason: collision with root package name */
        private final ReflectProperties.a f13145i = ReflectProperties.d(new b(this));

        /* renamed from: j, reason: collision with root package name */
        private final ReflectProperties.b f13146j = ReflectProperties.b(new a(this));

        /* compiled from: KPropertyImpl.kt */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.a<kb.e<?>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ c<V> f13147e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            a(c<? extends V> cVar) {
                super(0);
                this.f13147e = cVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final kb.e<?> invoke() {
                return c0.a(this.f13147e, true);
            }
        }

        /* compiled from: KPropertyImpl.kt */
        /* loaded from: classes2.dex */
        static final class b extends Lambda implements ya.a<PropertyGetterDescriptor> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ c<V> f13148e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            b(c<? extends V> cVar) {
                super(0);
                this.f13148e = cVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final PropertyGetterDescriptor invoke() {
                PropertyGetterDescriptor h10 = this.f13148e.s().L().h();
                return h10 == null ? DescriptorFactory.d(this.f13148e.s().L(), qb.g.f17195b.b()) : h10;
            }
        }

        @Override // jb.KCallableImpl
        public kb.e<?> F() {
            T b10 = this.f13146j.b(this, f13144k[1]);
            za.k.d(b10, "<get-caller>(...)");
            return (kb.e) b10;
        }

        @Override // jb.b0.a
        /* renamed from: N, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
        public PropertyGetterDescriptor L() {
            T b10 = this.f13145i.b(this, f13144k[0]);
            za.k.d(b10, "<get-descriptor>(...)");
            return (PropertyGetterDescriptor) b10;
        }

        public boolean equals(Object obj) {
            return (obj instanceof c) && za.k.a(s(), ((c) obj).s());
        }

        @Override // gb.KCallable
        public String getName() {
            return "<get-" + s().getName() + '>';
        }

        public int hashCode() {
            return s().hashCode();
        }

        public String toString() {
            return "getter of " + s();
        }
    }

    /* compiled from: KPropertyImpl.kt */
    /* loaded from: classes2.dex */
    public static abstract class d<V> extends a<V, Unit> implements i.a<V> {

        /* renamed from: k, reason: collision with root package name */
        static final /* synthetic */ gb.l<Object>[] f13149k = {Reflection.g(new PropertyReference1Impl(Reflection.b(d.class), "descriptor", "getDescriptor()Lorg/jetbrains/kotlin/descriptors/PropertySetterDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(d.class), "caller", "getCaller()Lkotlin/reflect/jvm/internal/calls/Caller;"))};

        /* renamed from: i, reason: collision with root package name */
        private final ReflectProperties.a f13150i = ReflectProperties.d(new b(this));

        /* renamed from: j, reason: collision with root package name */
        private final ReflectProperties.b f13151j = ReflectProperties.b(new a(this));

        /* compiled from: KPropertyImpl.kt */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.a<kb.e<?>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ d<V> f13152e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(d<V> dVar) {
                super(0);
                this.f13152e = dVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final kb.e<?> invoke() {
                return c0.a(this.f13152e, false);
            }
        }

        /* compiled from: KPropertyImpl.kt */
        /* loaded from: classes2.dex */
        static final class b extends Lambda implements ya.a<PropertySetterDescriptor> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ d<V> f13153e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            b(d<V> dVar) {
                super(0);
                this.f13153e = dVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final PropertySetterDescriptor invoke() {
                PropertySetterDescriptor k10 = this.f13153e.s().L().k();
                if (k10 != null) {
                    return k10;
                }
                PropertyDescriptor L = this.f13153e.s().L();
                g.a aVar = qb.g.f17195b;
                return DescriptorFactory.e(L, aVar.b(), aVar.b());
            }
        }

        @Override // jb.KCallableImpl
        public kb.e<?> F() {
            T b10 = this.f13151j.b(this, f13149k[1]);
            za.k.d(b10, "<get-caller>(...)");
            return (kb.e) b10;
        }

        @Override // jb.b0.a
        /* renamed from: N, reason: merged with bridge method [inline-methods] */
        public PropertySetterDescriptor L() {
            T b10 = this.f13150i.b(this, f13149k[0]);
            za.k.d(b10, "<get-descriptor>(...)");
            return (PropertySetterDescriptor) b10;
        }

        public boolean equals(Object obj) {
            return (obj instanceof d) && za.k.a(s(), ((d) obj).s());
        }

        @Override // gb.KCallable
        public String getName() {
            return "<set-" + s().getName() + '>';
        }

        public int hashCode() {
            return s().hashCode();
        }

        public String toString() {
            return "setter of " + s();
        }
    }

    /* compiled from: KPropertyImpl.kt */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.a<PropertyDescriptor> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ b0<V> f13154e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        e(b0<? extends V> b0Var) {
            super(0);
            this.f13154e = b0Var;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final PropertyDescriptor invoke() {
            return this.f13154e.G().G(this.f13154e.getName(), this.f13154e.R());
        }
    }

    /* compiled from: KPropertyImpl.kt */
    /* loaded from: classes2.dex */
    static final class f extends Lambda implements ya.a<Field> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ b0<V> f13155e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        f(b0<? extends V> b0Var) {
            super(0);
            this.f13155e = b0Var;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Field invoke() {
            Class<?> enclosingClass;
            j f10 = l0.f13288a.f(this.f13155e.L());
            if (f10 instanceof j.c) {
                j.c cVar = (j.c) f10;
                PropertyDescriptor b10 = cVar.b();
                JvmMemberSignature.a d10 = JvmProtoBufUtil.d(JvmProtoBufUtil.f16006a, cVar.e(), cVar.d(), cVar.g(), false, 8, null);
                if (d10 == null) {
                    return null;
                }
                b0<V> b0Var = this.f13155e;
                if (!DescriptorsJvmAbiUtil.e(b10) && !JvmProtoBufUtil.f(cVar.e())) {
                    DeclarationDescriptor b11 = b10.b();
                    enclosingClass = b11 instanceof ClassDescriptor ? o0.p((ClassDescriptor) b11) : b0Var.G().e();
                } else {
                    enclosingClass = b0Var.G().e().getEnclosingClass();
                }
                if (enclosingClass == null) {
                    return null;
                }
                try {
                    return enclosingClass.getDeclaredField(d10.c());
                } catch (NoSuchFieldException unused) {
                    return null;
                }
            }
            if (f10 instanceof j.a) {
                return ((j.a) f10).b();
            }
            if ((f10 instanceof j.b) || (f10 instanceof j.d)) {
                return null;
            }
            throw new NoWhenBranchMatchedException();
        }
    }

    private b0(KDeclarationContainerImpl kDeclarationContainerImpl, String str, String str2, PropertyDescriptor propertyDescriptor, Object obj) {
        this.f13138i = kDeclarationContainerImpl;
        this.f13139j = str;
        this.f13140k = str2;
        this.f13141l = obj;
        ReflectProperties.b<Field> b10 = ReflectProperties.b(new f(this));
        za.k.d(b10, "lazy {\n        when (val…y -> null\n        }\n    }");
        this.f13142m = b10;
        ReflectProperties.a<PropertyDescriptor> c10 = ReflectProperties.c(propertyDescriptor, new e(this));
        za.k.d(c10, "lazySoft(descriptorIniti…or(name, signature)\n    }");
        this.f13143n = c10;
    }

    @Override // jb.KCallableImpl
    public kb.e<?> F() {
        return h().F();
    }

    @Override // jb.KCallableImpl
    public KDeclarationContainerImpl G() {
        return this.f13138i;
    }

    @Override // jb.KCallableImpl
    public kb.e<?> H() {
        return h().H();
    }

    @Override // jb.KCallableImpl
    public boolean K() {
        return !za.k.a(this.f13141l, CallableReference.f20349k);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Member L() {
        if (!L().V()) {
            return null;
        }
        j f10 = l0.f13288a.f(L());
        if (f10 instanceof j.c) {
            j.c cVar = (j.c) f10;
            if (cVar.f().z()) {
                JvmProtoBuf.c u7 = cVar.f().u();
                if (!u7.u() || !u7.t()) {
                    return null;
                }
                return G().F(cVar.d().getString(u7.s()), cVar.d().getString(u7.r()));
            }
        }
        return Q();
    }

    public final Object M() {
        return kb.i.a(this.f13141l, L());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public final Object N(Member member, Object obj, Object obj2) {
        try {
            Object obj3 = f13137p;
            if ((obj != obj3 && obj2 != obj3) || L().r0() != null) {
                Object M = K() ? M() : obj;
                if (!(M != obj3)) {
                    M = null;
                }
                if (!K()) {
                    obj = obj2;
                }
                if (!(obj != obj3)) {
                    obj = null;
                }
                AccessibleObject accessibleObject = member instanceof AccessibleObject ? (AccessibleObject) member : null;
                if (accessibleObject != null) {
                    accessibleObject.setAccessible(KCallablesJvm.a(this));
                }
                if (member == 0) {
                    return null;
                }
                if (member instanceof Field) {
                    return ((Field) member).get(M);
                }
                if (!(member instanceof Method)) {
                    throw new AssertionError("delegate field/method " + member + " neither field nor method");
                }
                int length = ((Method) member).getParameterTypes().length;
                if (length == 0) {
                    return ((Method) member).invoke(null, new Object[0]);
                }
                if (length == 1) {
                    Method method = (Method) member;
                    Object[] objArr = new Object[1];
                    if (M == null) {
                        Class<?> cls = ((Method) member).getParameterTypes()[0];
                        za.k.d(cls, "fieldOrMethod.parameterTypes[0]");
                        M = o0.g(cls);
                    }
                    objArr[0] = M;
                    return method.invoke(null, objArr);
                }
                if (length == 2) {
                    Method method2 = (Method) member;
                    Object[] objArr2 = new Object[2];
                    objArr2[0] = M;
                    if (obj == null) {
                        Class<?> cls2 = ((Method) member).getParameterTypes()[1];
                        za.k.d(cls2, "fieldOrMethod.parameterTypes[1]");
                        obj = o0.g(cls2);
                    }
                    objArr2[1] = obj;
                    return method2.invoke(null, objArr2);
                }
                throw new AssertionError("delegate method " + member + " should take 0, 1, or 2 parameters");
            }
            throw new RuntimeException('\'' + this + "' is not an extension property and thus getExtensionDelegate() is not going to work, use getDelegate() instead");
        } catch (IllegalAccessException e10) {
            throw new hb.b(e10);
        }
    }

    @Override // jb.KCallableImpl
    /* renamed from: O, reason: merged with bridge method [inline-methods] */
    public PropertyDescriptor L() {
        PropertyDescriptor invoke = this.f13143n.invoke();
        za.k.d(invoke, "_descriptor()");
        return invoke;
    }

    /* renamed from: P */
    public abstract c<V> h();

    public final Field Q() {
        return this.f13142m.invoke();
    }

    public final String R() {
        return this.f13140k;
    }

    public boolean equals(Object obj) {
        b0<?> d10 = o0.d(obj);
        return d10 != null && za.k.a(G(), d10.G()) && za.k.a(getName(), d10.getName()) && za.k.a(this.f13140k, d10.f13140k) && za.k.a(this.f13141l, d10.f13141l);
    }

    @Override // gb.KCallable
    public String getName() {
        return this.f13139j;
    }

    public int hashCode() {
        return (((G().hashCode() * 31) + getName().hashCode()) * 31) + this.f13140k.hashCode();
    }

    public String toString() {
        return ReflectionObjectRenderer.f13232a.g(L());
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public b0(KDeclarationContainerImpl kDeclarationContainerImpl, String str, String str2, Object obj) {
        this(kDeclarationContainerImpl, str, str2, null, obj);
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(str, "name");
        za.k.e(str2, "signature");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public b0(KDeclarationContainerImpl kDeclarationContainerImpl, PropertyDescriptor propertyDescriptor) {
        this(kDeclarationContainerImpl, r3, l0.f13288a.f(propertyDescriptor).a(), propertyDescriptor, CallableReference.f20349k);
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(propertyDescriptor, "descriptor");
        String b10 = propertyDescriptor.getName().b();
        za.k.d(b10, "descriptor.name.asString()");
    }
}
