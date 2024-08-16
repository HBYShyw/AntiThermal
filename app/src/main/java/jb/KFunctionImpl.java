package jb;

import gb.KFunction;
import gb.KParameter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jb.FunctionWithAllInvokes;
import jb.ReflectProperties;
import jb.i;
import kb.CallerImpl;
import kb.a;
import ma.NoWhenBranchMatchedException;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import xc.inlineClassManglingRules;
import za.CallableReference;
import za.DefaultConstructorMarker;
import za.FunctionBase;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: KFunctionImpl.kt */
/* renamed from: jb.p, reason: use source file name */
/* loaded from: classes2.dex */
public final class KFunctionImpl extends KCallableImpl<Object> implements FunctionBase<Object>, KFunction<Object>, FunctionWithAllInvokes {

    /* renamed from: o, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f13307o = {Reflection.g(new PropertyReference1Impl(Reflection.b(KFunctionImpl.class), "descriptor", "getDescriptor()Lorg/jetbrains/kotlin/descriptors/FunctionDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(KFunctionImpl.class), "caller", "getCaller()Lkotlin/reflect/jvm/internal/calls/Caller;")), Reflection.g(new PropertyReference1Impl(Reflection.b(KFunctionImpl.class), "defaultCaller", "getDefaultCaller()Lkotlin/reflect/jvm/internal/calls/Caller;"))};

    /* renamed from: i, reason: collision with root package name */
    private final KDeclarationContainerImpl f13308i;

    /* renamed from: j, reason: collision with root package name */
    private final String f13309j;

    /* renamed from: k, reason: collision with root package name */
    private final Object f13310k;

    /* renamed from: l, reason: collision with root package name */
    private final ReflectProperties.a f13311l;

    /* renamed from: m, reason: collision with root package name */
    private final ReflectProperties.b f13312m;

    /* renamed from: n, reason: collision with root package name */
    private final ReflectProperties.b f13313n;

    /* compiled from: KFunctionImpl.kt */
    /* renamed from: jb.p$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<kb.e<? extends Executable>> {
        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final kb.e<Executable> invoke() {
            int u7;
            Object b10;
            kb.e T;
            int u10;
            i g6 = l0.f13288a.g(KFunctionImpl.this.L());
            if (g6 instanceof i.d) {
                if (KFunctionImpl.this.J()) {
                    Class<?> e10 = KFunctionImpl.this.G().e();
                    List<KParameter> parameters = KFunctionImpl.this.getParameters();
                    u10 = kotlin.collections.s.u(parameters, 10);
                    ArrayList arrayList = new ArrayList(u10);
                    Iterator<T> it = parameters.iterator();
                    while (it.hasNext()) {
                        String name = ((KParameter) it.next()).getName();
                        za.k.b(name);
                        arrayList.add(name);
                    }
                    return new kb.a(e10, arrayList, a.EnumC0070a.POSITIONAL_CALL, a.b.KOTLIN, null, 16, null);
                }
                b10 = KFunctionImpl.this.G().B(((i.d) g6).b());
            } else if (g6 instanceof i.e) {
                i.e eVar = (i.e) g6;
                b10 = KFunctionImpl.this.G().F(eVar.c(), eVar.b());
            } else if (g6 instanceof i.c) {
                b10 = ((i.c) g6).b();
            } else {
                if (!(g6 instanceof i.b)) {
                    if (!(g6 instanceof i.a)) {
                        throw new NoWhenBranchMatchedException();
                    }
                    List<Method> b11 = ((i.a) g6).b();
                    Class<?> e11 = KFunctionImpl.this.G().e();
                    u7 = kotlin.collections.s.u(b11, 10);
                    ArrayList arrayList2 = new ArrayList(u7);
                    Iterator<T> it2 = b11.iterator();
                    while (it2.hasNext()) {
                        arrayList2.add(((Method) it2.next()).getName());
                    }
                    return new kb.a(e11, arrayList2, a.EnumC0070a.POSITIONAL_CALL, a.b.JAVA, b11);
                }
                b10 = ((i.b) g6).b();
            }
            if (b10 instanceof Constructor) {
                KFunctionImpl kFunctionImpl = KFunctionImpl.this;
                T = kFunctionImpl.Q((Constructor) b10, kFunctionImpl.L(), false);
            } else if (b10 instanceof Method) {
                Method method = (Method) b10;
                if (!Modifier.isStatic(method.getModifiers())) {
                    T = KFunctionImpl.this.R(method);
                } else if (KFunctionImpl.this.L().i().j(o0.j()) != null) {
                    T = KFunctionImpl.this.S(method);
                } else {
                    T = KFunctionImpl.this.T(method);
                }
            } else {
                throw new KotlinReflectionInternalError("Could not compute caller for function: " + KFunctionImpl.this.L() + " (member = " + b10 + ')');
            }
            return kb.i.c(T, KFunctionImpl.this.L(), false, 2, null);
        }
    }

    /* compiled from: KFunctionImpl.kt */
    /* renamed from: jb.p$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<kb.e<? extends Executable>> {
        b() {
            super(0);
        }

        /* JADX WARN: Type inference failed for: r5v4, types: [java.lang.reflect.Member, java.lang.Object] */
        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final kb.e<Executable> invoke() {
            GenericDeclaration genericDeclaration;
            int u7;
            int u10;
            kb.e eVar;
            i g6 = l0.f13288a.g(KFunctionImpl.this.L());
            if (g6 instanceof i.e) {
                KDeclarationContainerImpl G = KFunctionImpl.this.G();
                i.e eVar2 = (i.e) g6;
                String c10 = eVar2.c();
                String b10 = eVar2.b();
                za.k.b(KFunctionImpl.this.F().b());
                genericDeclaration = G.D(c10, b10, !Modifier.isStatic(r5.getModifiers()));
            } else if (g6 instanceof i.d) {
                if (KFunctionImpl.this.J()) {
                    Class<?> e10 = KFunctionImpl.this.G().e();
                    List<KParameter> parameters = KFunctionImpl.this.getParameters();
                    u10 = kotlin.collections.s.u(parameters, 10);
                    ArrayList arrayList = new ArrayList(u10);
                    Iterator<T> it = parameters.iterator();
                    while (it.hasNext()) {
                        String name = ((KParameter) it.next()).getName();
                        za.k.b(name);
                        arrayList.add(name);
                    }
                    return new kb.a(e10, arrayList, a.EnumC0070a.CALL_BY_NAME, a.b.KOTLIN, null, 16, null);
                }
                genericDeclaration = KFunctionImpl.this.G().C(((i.d) g6).b());
            } else {
                if (g6 instanceof i.a) {
                    List<Method> b11 = ((i.a) g6).b();
                    Class<?> e11 = KFunctionImpl.this.G().e();
                    u7 = kotlin.collections.s.u(b11, 10);
                    ArrayList arrayList2 = new ArrayList(u7);
                    Iterator<T> it2 = b11.iterator();
                    while (it2.hasNext()) {
                        arrayList2.add(((Method) it2.next()).getName());
                    }
                    return new kb.a(e11, arrayList2, a.EnumC0070a.CALL_BY_NAME, a.b.JAVA, b11);
                }
                genericDeclaration = null;
            }
            if (genericDeclaration instanceof Constructor) {
                KFunctionImpl kFunctionImpl = KFunctionImpl.this;
                eVar = kFunctionImpl.Q((Constructor) genericDeclaration, kFunctionImpl.L(), true);
            } else if (genericDeclaration instanceof Method) {
                if (KFunctionImpl.this.L().i().j(o0.j()) != null) {
                    DeclarationDescriptor b12 = KFunctionImpl.this.L().b();
                    za.k.c(b12, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                    if (!((ClassDescriptor) b12).F()) {
                        eVar = KFunctionImpl.this.S((Method) genericDeclaration);
                    }
                }
                eVar = KFunctionImpl.this.T((Method) genericDeclaration);
            } else {
                eVar = null;
            }
            if (eVar != null) {
                return kb.i.b(eVar, KFunctionImpl.this.L(), true);
            }
            return null;
        }
    }

    /* compiled from: KFunctionImpl.kt */
    /* renamed from: jb.p$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<FunctionDescriptor> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ String f13317f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(String str) {
            super(0);
            this.f13317f = str;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final FunctionDescriptor invoke() {
            return KFunctionImpl.this.G().E(this.f13317f, KFunctionImpl.this.f13309j);
        }
    }

    /* synthetic */ KFunctionImpl(KDeclarationContainerImpl kDeclarationContainerImpl, String str, String str2, FunctionDescriptor functionDescriptor, Object obj, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(kDeclarationContainerImpl, str, str2, functionDescriptor, (i10 & 16) != 0 ? CallableReference.f20349k : obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CallerImpl<Constructor<?>> Q(Constructor<?> constructor, FunctionDescriptor functionDescriptor, boolean z10) {
        if (!z10 && inlineClassManglingRules.f(functionDescriptor)) {
            if (K()) {
                return new CallerImpl.a(constructor, U());
            }
            return new CallerImpl.b(constructor);
        }
        if (K()) {
            return new CallerImpl.c(constructor, U());
        }
        return new CallerImpl.e(constructor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CallerImpl.h R(Method method) {
        return K() ? new CallerImpl.h.a(method, U()) : new CallerImpl.h.d(method);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CallerImpl.h S(Method method) {
        return K() ? new CallerImpl.h.b(method) : new CallerImpl.h.e(method);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CallerImpl.h T(Method method) {
        return K() ? new CallerImpl.h.c(method, U()) : new CallerImpl.h.f(method);
    }

    private final Object U() {
        return kb.i.a(this.f13310k, L());
    }

    @Override // jb.KCallableImpl
    public kb.e<?> F() {
        T b10 = this.f13312m.b(this, f13307o[1]);
        za.k.d(b10, "<get-caller>(...)");
        return (kb.e) b10;
    }

    @Override // jb.KCallableImpl
    public KDeclarationContainerImpl G() {
        return this.f13308i;
    }

    @Override // jb.KCallableImpl
    public kb.e<?> H() {
        return (kb.e) this.f13313n.b(this, f13307o[2]);
    }

    @Override // jb.KCallableImpl
    public boolean K() {
        return !za.k.a(this.f13310k, CallableReference.f20349k);
    }

    @Override // jb.KCallableImpl
    /* renamed from: V, reason: merged with bridge method [inline-methods] */
    public FunctionDescriptor L() {
        T b10 = this.f13311l.b(this, f13307o[0]);
        za.k.d(b10, "<get-descriptor>(...)");
        return (FunctionDescriptor) b10;
    }

    public boolean equals(Object obj) {
        KFunctionImpl c10 = o0.c(obj);
        return c10 != null && za.k.a(G(), c10.G()) && za.k.a(getName(), c10.getName()) && za.k.a(this.f13309j, c10.f13309j) && za.k.a(this.f13310k, c10.f13310k);
    }

    @Override // ya.q
    public Object g(Object obj, Object obj2, Object obj3) {
        return FunctionWithAllInvokes.a.d(this, obj, obj2, obj3);
    }

    @Override // za.FunctionBase
    public int getArity() {
        return kb.g.a(F());
    }

    @Override // gb.KCallable
    public String getName() {
        String b10 = L().getName().b();
        za.k.d(b10, "descriptor.name.asString()");
        return b10;
    }

    public int hashCode() {
        return (((G().hashCode() * 31) + getName().hashCode()) * 31) + this.f13309j.hashCode();
    }

    @Override // ya.a
    public Object invoke() {
        return FunctionWithAllInvokes.a.a(this);
    }

    public String toString() {
        return ReflectionObjectRenderer.f13232a.d(L());
    }

    @Override // ya.l
    public Object invoke(Object obj) {
        return FunctionWithAllInvokes.a.b(this, obj);
    }

    private KFunctionImpl(KDeclarationContainerImpl kDeclarationContainerImpl, String str, String str2, FunctionDescriptor functionDescriptor, Object obj) {
        this.f13308i = kDeclarationContainerImpl;
        this.f13309j = str2;
        this.f13310k = obj;
        this.f13311l = ReflectProperties.c(functionDescriptor, new c(str));
        this.f13312m = ReflectProperties.b(new a());
        this.f13313n = ReflectProperties.b(new b());
    }

    @Override // ya.p
    public Object invoke(Object obj, Object obj2) {
        return FunctionWithAllInvokes.a.c(this, obj, obj2);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public KFunctionImpl(KDeclarationContainerImpl kDeclarationContainerImpl, String str, String str2, Object obj) {
        this(kDeclarationContainerImpl, str, str2, null, obj);
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(str, "name");
        za.k.e(str2, "signature");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public KFunctionImpl(KDeclarationContainerImpl kDeclarationContainerImpl, FunctionDescriptor functionDescriptor) {
        this(kDeclarationContainerImpl, r3, l0.f13288a.g(functionDescriptor).a(), functionDescriptor, null, 16, null);
        za.k.e(kDeclarationContainerImpl, "container");
        za.k.e(functionDescriptor, "descriptor");
        String b10 = functionDescriptor.getName().b();
        za.k.d(b10, "descriptor.name.asString()");
    }
}
