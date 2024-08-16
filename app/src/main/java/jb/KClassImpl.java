package jb;

import cd.MemberDeserializer;
import ed.DeserializedClassDescriptor;
import gb.KClass;
import gb.KDeclarationContainer;
import gb.KFunction;
import ic.KotlinClassHeader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import jb.KDeclarationContainerImpl;
import jb.ReflectProperties;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import lc.ProtoBufUtil;
import ma.NoWhenBranchMatchedException;
import mb.CompanionObjectMapping;
import mb.CompanionObjectMappingUtils;
import mb.KotlinBuiltIns;
import mc.JvmProtoBuf;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.Modality;
import pb.PropertyDescriptor;
import pb.TypeParameterDescriptor;
import pb.findClassInModule;
import qc.i;
import qd.collections;
import sd.StringsJVM;
import ub.RuntimeModuleData;
import vb.reflectClassUtil;
import xa.JvmClassMapping;
import za.FunctionReference;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import za.TypeIntrinsics;
import zc.ResolutionScope;

/* compiled from: KClassImpl.kt */
/* renamed from: jb.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class KClassImpl<T> extends KDeclarationContainerImpl implements KClass<T>, KClassifierImpl, KTypeParameterOwnerImpl {

    /* renamed from: h, reason: collision with root package name */
    private final Class<T> f13237h;

    /* renamed from: i, reason: collision with root package name */
    private final ReflectProperties.b<KClassImpl<T>.a> f13238i;

    /* compiled from: KClassImpl.kt */
    /* renamed from: jb.l$a */
    /* loaded from: classes2.dex */
    public final class a extends KDeclarationContainerImpl.b {

        /* renamed from: w, reason: collision with root package name */
        static final /* synthetic */ gb.l<Object>[] f13239w = {Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "descriptor", "getDescriptor()Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "annotations", "getAnnotations()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "simpleName", "getSimpleName()Ljava/lang/String;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "qualifiedName", "getQualifiedName()Ljava/lang/String;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "constructors", "getConstructors()Ljava/util/Collection;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "nestedClasses", "getNestedClasses()Ljava/util/Collection;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "objectInstance", "getObjectInstance()Ljava/lang/Object;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "typeParameters", "getTypeParameters()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "supertypes", "getSupertypes()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "sealedSubclasses", "getSealedSubclasses()Ljava/util/List;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "declaredNonStaticMembers", "getDeclaredNonStaticMembers()Ljava/util/Collection;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "declaredStaticMembers", "getDeclaredStaticMembers()Ljava/util/Collection;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "inheritedNonStaticMembers", "getInheritedNonStaticMembers()Ljava/util/Collection;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "inheritedStaticMembers", "getInheritedStaticMembers()Ljava/util/Collection;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "allNonStaticMembers", "getAllNonStaticMembers()Ljava/util/Collection;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "allStaticMembers", "getAllStaticMembers()Ljava/util/Collection;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "declaredMembers", "getDeclaredMembers()Ljava/util/Collection;")), Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "allMembers", "getAllMembers()Ljava/util/Collection;"))};

        /* renamed from: d, reason: collision with root package name */
        private final ReflectProperties.a f13240d;

        /* renamed from: e, reason: collision with root package name */
        private final ReflectProperties.a f13241e;

        /* renamed from: f, reason: collision with root package name */
        private final ReflectProperties.a f13242f;

        /* renamed from: g, reason: collision with root package name */
        private final ReflectProperties.a f13243g;

        /* renamed from: h, reason: collision with root package name */
        private final ReflectProperties.a f13244h;

        /* renamed from: i, reason: collision with root package name */
        private final ReflectProperties.a f13245i;

        /* renamed from: j, reason: collision with root package name */
        private final ReflectProperties.b f13246j;

        /* renamed from: k, reason: collision with root package name */
        private final ReflectProperties.a f13247k;

        /* renamed from: l, reason: collision with root package name */
        private final ReflectProperties.a f13248l;

        /* renamed from: m, reason: collision with root package name */
        private final ReflectProperties.a f13249m;

        /* renamed from: n, reason: collision with root package name */
        private final ReflectProperties.a f13250n;

        /* renamed from: o, reason: collision with root package name */
        private final ReflectProperties.a f13251o;

        /* renamed from: p, reason: collision with root package name */
        private final ReflectProperties.a f13252p;

        /* renamed from: q, reason: collision with root package name */
        private final ReflectProperties.a f13253q;

        /* renamed from: r, reason: collision with root package name */
        private final ReflectProperties.a f13254r;

        /* renamed from: s, reason: collision with root package name */
        private final ReflectProperties.a f13255s;

        /* renamed from: t, reason: collision with root package name */
        private final ReflectProperties.a f13256t;

        /* renamed from: u, reason: collision with root package name */
        private final ReflectProperties.a f13257u;

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0058a extends Lambda implements ya.a<List<? extends KCallableImpl<?>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13259e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0058a(KClassImpl<T>.a aVar) {
                super(0);
                this.f13259e = aVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<KCallableImpl<?>> invoke() {
                List<KCallableImpl<?>> m02;
                m02 = _Collections.m0(this.f13259e.g(), this.f13259e.h());
                return m02;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$b */
        /* loaded from: classes2.dex */
        static final class b extends Lambda implements ya.a<List<? extends KCallableImpl<?>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13260e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            b(KClassImpl<T>.a aVar) {
                super(0);
                this.f13260e = aVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<KCallableImpl<?>> invoke() {
                List<KCallableImpl<?>> m02;
                m02 = _Collections.m0(this.f13260e.k(), this.f13260e.n());
                return m02;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$c */
        /* loaded from: classes2.dex */
        static final class c extends Lambda implements ya.a<List<? extends KCallableImpl<?>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13261e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            c(KClassImpl<T>.a aVar) {
                super(0);
                this.f13261e = aVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<KCallableImpl<?>> invoke() {
                List<KCallableImpl<?>> m02;
                m02 = _Collections.m0(this.f13261e.l(), this.f13261e.o());
                return m02;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$d */
        /* loaded from: classes2.dex */
        static final class d extends Lambda implements ya.a<List<? extends Annotation>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13262e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            d(KClassImpl<T>.a aVar) {
                super(0);
                this.f13262e = aVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<Annotation> invoke() {
                return o0.e(this.f13262e.m());
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$e */
        /* loaded from: classes2.dex */
        static final class e extends Lambda implements ya.a<List<? extends KFunction<? extends T>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13263e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            e(KClassImpl<T> kClassImpl) {
                super(0);
                this.f13263e = kClassImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<KFunction<T>> invoke() {
                int u7;
                Collection<ConstructorDescriptor> I = this.f13263e.I();
                KClassImpl<T> kClassImpl = this.f13263e;
                u7 = kotlin.collections.s.u(I, 10);
                ArrayList arrayList = new ArrayList(u7);
                Iterator<T> it = I.iterator();
                while (it.hasNext()) {
                    arrayList.add(new KFunctionImpl(kClassImpl, (ConstructorDescriptor) it.next()));
                }
                return arrayList;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$f */
        /* loaded from: classes2.dex */
        static final class f extends Lambda implements ya.a<List<? extends KCallableImpl<?>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13264e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            f(KClassImpl<T>.a aVar) {
                super(0);
                this.f13264e = aVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<KCallableImpl<?>> invoke() {
                List<KCallableImpl<?>> m02;
                m02 = _Collections.m0(this.f13264e.k(), this.f13264e.l());
                return m02;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$g */
        /* loaded from: classes2.dex */
        static final class g extends Lambda implements ya.a<Collection<? extends KCallableImpl<?>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13265e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            g(KClassImpl<T> kClassImpl) {
                super(0);
                this.f13265e = kClassImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Collection<KCallableImpl<?>> invoke() {
                KClassImpl<T> kClassImpl = this.f13265e;
                return kClassImpl.L(kClassImpl.a0(), KDeclarationContainerImpl.c.DECLARED);
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$h */
        /* loaded from: classes2.dex */
        static final class h extends Lambda implements ya.a<Collection<? extends KCallableImpl<?>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13266e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            h(KClassImpl<T> kClassImpl) {
                super(0);
                this.f13266e = kClassImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Collection<KCallableImpl<?>> invoke() {
                KClassImpl<T> kClassImpl = this.f13266e;
                return kClassImpl.L(kClassImpl.b0(), KDeclarationContainerImpl.c.DECLARED);
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$i */
        /* loaded from: classes2.dex */
        static final class i extends Lambda implements ya.a<ClassDescriptor> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13267e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            i(KClassImpl<T> kClassImpl) {
                super(0);
                this.f13267e = kClassImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final ClassDescriptor invoke() {
                ClassId W = this.f13267e.W();
                RuntimeModuleData a10 = this.f13267e.Y().invoke().a();
                ClassDescriptor b10 = W.k() ? a10.a().b(W) : findClassInModule.a(a10.b(), W);
                if (b10 != null) {
                    return b10;
                }
                this.f13267e.c0();
                throw null;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$j */
        /* loaded from: classes2.dex */
        static final class j extends Lambda implements ya.a<Collection<? extends KCallableImpl<?>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13268e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            j(KClassImpl<T> kClassImpl) {
                super(0);
                this.f13268e = kClassImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Collection<KCallableImpl<?>> invoke() {
                KClassImpl<T> kClassImpl = this.f13268e;
                return kClassImpl.L(kClassImpl.a0(), KDeclarationContainerImpl.c.INHERITED);
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$k */
        /* loaded from: classes2.dex */
        static final class k extends Lambda implements ya.a<Collection<? extends KCallableImpl<?>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13269e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            k(KClassImpl<T> kClassImpl) {
                super(0);
                this.f13269e = kClassImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Collection<KCallableImpl<?>> invoke() {
                KClassImpl<T> kClassImpl = this.f13269e;
                return kClassImpl.L(kClassImpl.b0(), KDeclarationContainerImpl.c.INHERITED);
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$l */
        /* loaded from: classes2.dex */
        static final class l extends Lambda implements ya.a<List<? extends KClassImpl<? extends Object>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13270e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            l(KClassImpl<T>.a aVar) {
                super(0);
                this.f13270e = aVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<KClassImpl<? extends Object>> invoke() {
                zc.h F0 = this.f13270e.m().F0();
                za.k.d(F0, "descriptor.unsubstitutedInnerClassesScope");
                Collection a10 = ResolutionScope.a.a(F0, null, null, 3, null);
                ArrayList<DeclarationDescriptor> arrayList = new ArrayList();
                for (T t7 : a10) {
                    if (!sc.e.B((DeclarationDescriptor) t7)) {
                        arrayList.add(t7);
                    }
                }
                ArrayList arrayList2 = new ArrayList();
                for (DeclarationDescriptor declarationDescriptor : arrayList) {
                    ClassDescriptor classDescriptor = declarationDescriptor instanceof ClassDescriptor ? (ClassDescriptor) declarationDescriptor : null;
                    Class<?> p10 = classDescriptor != null ? o0.p(classDescriptor) : null;
                    KClassImpl kClassImpl = p10 != null ? new KClassImpl(p10) : null;
                    if (kClassImpl != null) {
                        arrayList2.add(kClassImpl);
                    }
                }
                return arrayList2;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$m */
        /* loaded from: classes2.dex */
        static final class m extends Lambda implements ya.a<T> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13271e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13272f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            m(KClassImpl<T>.a aVar, KClassImpl<T> kClassImpl) {
                super(0);
                this.f13271e = aVar;
                this.f13272f = kClassImpl;
            }

            @Override // ya.a
            public final T invoke() {
                Field declaredField;
                ClassDescriptor m10 = this.f13271e.m();
                if (m10.getKind() != ClassKind.OBJECT) {
                    return null;
                }
                if (m10.F() && !CompanionObjectMappingUtils.a(CompanionObjectMapping.f15213a, m10)) {
                    declaredField = this.f13272f.e().getEnclosingClass().getDeclaredField(m10.getName().b());
                } else {
                    declaredField = this.f13272f.e().getDeclaredField("INSTANCE");
                }
                T t7 = (T) declaredField.get(null);
                za.k.c(t7, "null cannot be cast to non-null type T of kotlin.reflect.jvm.internal.KClassImpl");
                return t7;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$n */
        /* loaded from: classes2.dex */
        static final class n extends Lambda implements ya.a<String> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13273e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            n(KClassImpl<T> kClassImpl) {
                super(0);
                this.f13273e = kClassImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final String invoke() {
                if (this.f13273e.e().isAnonymousClass()) {
                    return null;
                }
                ClassId W = this.f13273e.W();
                if (W.k()) {
                    return null;
                }
                return W.b().b();
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$o */
        /* loaded from: classes2.dex */
        static final class o extends Lambda implements ya.a<List<? extends KClassImpl<? extends T>>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13274e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            o(KClassImpl<T>.a aVar) {
                super(0);
                this.f13274e = aVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<KClassImpl<? extends T>> invoke() {
                Collection<ClassDescriptor> S = this.f13274e.m().S();
                za.k.d(S, "descriptor.sealedSubclasses");
                ArrayList arrayList = new ArrayList();
                for (ClassDescriptor classDescriptor : S) {
                    za.k.c(classDescriptor, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                    Class<?> p10 = o0.p(classDescriptor);
                    KClassImpl kClassImpl = p10 != null ? new KClassImpl(p10) : null;
                    if (kClassImpl != null) {
                        arrayList.add(kClassImpl);
                    }
                }
                return arrayList;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$p */
        /* loaded from: classes2.dex */
        static final class p extends Lambda implements ya.a<String> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13275e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13276f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            p(KClassImpl<T> kClassImpl, KClassImpl<T>.a aVar) {
                super(0);
                this.f13275e = kClassImpl;
                this.f13276f = aVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final String invoke() {
                if (this.f13275e.e().isAnonymousClass()) {
                    return null;
                }
                ClassId W = this.f13275e.W();
                if (W.k()) {
                    return this.f13276f.f(this.f13275e.e());
                }
                String b10 = W.j().b();
                za.k.d(b10, "classId.shortClassName.asString()");
                return b10;
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$q */
        /* loaded from: classes2.dex */
        static final class q extends Lambda implements ya.a<List<? extends KTypeImpl>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13277e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13278f;

            /* JADX INFO: Access modifiers changed from: package-private */
            /* compiled from: KClassImpl.kt */
            /* renamed from: jb.l$a$q$a, reason: collision with other inner class name */
            /* loaded from: classes2.dex */
            public static final class C0059a extends Lambda implements ya.a<Type> {

                /* renamed from: e, reason: collision with root package name */
                final /* synthetic */ gd.g0 f13279e;

                /* renamed from: f, reason: collision with root package name */
                final /* synthetic */ KClassImpl<T>.a f13280f;

                /* renamed from: g, reason: collision with root package name */
                final /* synthetic */ KClassImpl<T> f13281g;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                C0059a(gd.g0 g0Var, KClassImpl<T>.a aVar, KClassImpl<T> kClassImpl) {
                    super(0);
                    this.f13279e = g0Var;
                    this.f13280f = aVar;
                    this.f13281g = kClassImpl;
                }

                @Override // ya.a
                /* renamed from: a, reason: merged with bridge method [inline-methods] */
                public final Type invoke() {
                    int J;
                    ClassifierDescriptor v7 = this.f13279e.W0().v();
                    if (v7 instanceof ClassDescriptor) {
                        Class<?> p10 = o0.p((ClassDescriptor) v7);
                        if (p10 != null) {
                            if (za.k.a(this.f13281g.e().getSuperclass(), p10)) {
                                Type genericSuperclass = this.f13281g.e().getGenericSuperclass();
                                za.k.d(genericSuperclass, "{\n                      …ass\n                    }");
                                return genericSuperclass;
                            }
                            Class<?>[] interfaces = this.f13281g.e().getInterfaces();
                            za.k.d(interfaces, "jClass.interfaces");
                            J = _Arrays.J(interfaces, p10);
                            if (J >= 0) {
                                Type type = this.f13281g.e().getGenericInterfaces()[J];
                                za.k.d(type, "{\n                      …ex]\n                    }");
                                return type;
                            }
                            throw new KotlinReflectionInternalError("No superclass of " + this.f13280f + " in Java reflection for " + v7);
                        }
                        throw new KotlinReflectionInternalError("Unsupported superclass of " + this.f13280f + ": " + v7);
                    }
                    throw new KotlinReflectionInternalError("Supertype not a class: " + v7);
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* compiled from: KClassImpl.kt */
            /* renamed from: jb.l$a$q$b */
            /* loaded from: classes2.dex */
            public static final class b extends Lambda implements ya.a<Type> {

                /* renamed from: e, reason: collision with root package name */
                public static final b f13282e = new b();

                b() {
                    super(0);
                }

                @Override // ya.a
                /* renamed from: a, reason: merged with bridge method [inline-methods] */
                public final Type invoke() {
                    return Object.class;
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            q(KClassImpl<T>.a aVar, KClassImpl<T> kClassImpl) {
                super(0);
                this.f13277e = aVar;
                this.f13278f = kClassImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<KTypeImpl> invoke() {
                Collection<gd.g0> q10 = this.f13277e.m().n().q();
                za.k.d(q10, "descriptor.typeConstructor.supertypes");
                ArrayList arrayList = new ArrayList(q10.size());
                KClassImpl<T>.a aVar = this.f13277e;
                KClassImpl<T> kClassImpl = this.f13278f;
                for (gd.g0 g0Var : q10) {
                    za.k.d(g0Var, "kotlinType");
                    arrayList.add(new KTypeImpl(g0Var, new C0059a(g0Var, aVar, kClassImpl)));
                }
                if (!KotlinBuiltIns.t0(this.f13277e.m())) {
                    boolean z10 = false;
                    if (!arrayList.isEmpty()) {
                        Iterator<T> it = arrayList.iterator();
                        while (it.hasNext()) {
                            ClassKind kind = sc.e.e(((KTypeImpl) it.next()).m()).getKind();
                            za.k.d(kind, "getClassDescriptorForType(it.type).kind");
                            if (!(kind == ClassKind.INTERFACE || kind == ClassKind.ANNOTATION_CLASS)) {
                                break;
                            }
                        }
                    }
                    z10 = true;
                    if (z10) {
                        gd.o0 i10 = wc.c.j(this.f13277e.m()).i();
                        za.k.d(i10, "descriptor.builtIns.anyType");
                        arrayList.add(new KTypeImpl(i10, b.f13282e));
                    }
                }
                return collections.c(arrayList);
            }
        }

        /* compiled from: KClassImpl.kt */
        /* renamed from: jb.l$a$r */
        /* loaded from: classes2.dex */
        static final class r extends Lambda implements ya.a<List<? extends KTypeParameterImpl>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T>.a f13283e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ KClassImpl<T> f13284f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            r(KClassImpl<T>.a aVar, KClassImpl<T> kClassImpl) {
                super(0);
                this.f13283e = aVar;
                this.f13284f = kClassImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<KTypeParameterImpl> invoke() {
                int u7;
                List<TypeParameterDescriptor> B = this.f13283e.m().B();
                za.k.d(B, "descriptor.declaredTypeParameters");
                KClassImpl<T> kClassImpl = this.f13284f;
                u7 = kotlin.collections.s.u(B, 10);
                ArrayList arrayList = new ArrayList(u7);
                for (TypeParameterDescriptor typeParameterDescriptor : B) {
                    za.k.d(typeParameterDescriptor, "descriptor");
                    arrayList.add(new KTypeParameterImpl(kClassImpl, typeParameterDescriptor));
                }
                return arrayList;
            }
        }

        public a() {
            super();
            this.f13240d = ReflectProperties.d(new i(KClassImpl.this));
            this.f13241e = ReflectProperties.d(new d(this));
            this.f13242f = ReflectProperties.d(new p(KClassImpl.this, this));
            this.f13243g = ReflectProperties.d(new n(KClassImpl.this));
            this.f13244h = ReflectProperties.d(new e(KClassImpl.this));
            this.f13245i = ReflectProperties.d(new l(this));
            this.f13246j = ReflectProperties.b(new m(this, KClassImpl.this));
            this.f13247k = ReflectProperties.d(new r(this, KClassImpl.this));
            this.f13248l = ReflectProperties.d(new q(this, KClassImpl.this));
            this.f13249m = ReflectProperties.d(new o(this));
            this.f13250n = ReflectProperties.d(new g(KClassImpl.this));
            this.f13251o = ReflectProperties.d(new h(KClassImpl.this));
            this.f13252p = ReflectProperties.d(new j(KClassImpl.this));
            this.f13253q = ReflectProperties.d(new k(KClassImpl.this));
            this.f13254r = ReflectProperties.d(new b(this));
            this.f13255s = ReflectProperties.d(new c(this));
            this.f13256t = ReflectProperties.d(new f(this));
            this.f13257u = ReflectProperties.d(new C0058a(this));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String f(Class<?> cls) {
            String A0;
            String B0;
            String B02;
            String simpleName = cls.getSimpleName();
            Method enclosingMethod = cls.getEnclosingMethod();
            if (enclosingMethod != null) {
                za.k.d(simpleName, "name");
                B02 = sd.v.B0(simpleName, enclosingMethod.getName() + '$', null, 2, null);
                return B02;
            }
            Constructor<?> enclosingConstructor = cls.getEnclosingConstructor();
            if (enclosingConstructor != null) {
                za.k.d(simpleName, "name");
                B0 = sd.v.B0(simpleName, enclosingConstructor.getName() + '$', null, 2, null);
                return B0;
            }
            za.k.d(simpleName, "name");
            A0 = sd.v.A0(simpleName, '$', null, 2, null);
            return A0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Collection<KCallableImpl<?>> l() {
            T b10 = this.f13251o.b(this, f13239w[11]);
            za.k.d(b10, "<get-declaredStaticMembers>(...)");
            return (Collection) b10;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Collection<KCallableImpl<?>> n() {
            T b10 = this.f13252p.b(this, f13239w[12]);
            za.k.d(b10, "<get-inheritedNonStaticMembers>(...)");
            return (Collection) b10;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Collection<KCallableImpl<?>> o() {
            T b10 = this.f13253q.b(this, f13239w[13]);
            za.k.d(b10, "<get-inheritedStaticMembers>(...)");
            return (Collection) b10;
        }

        public final Collection<KCallableImpl<?>> g() {
            T b10 = this.f13254r.b(this, f13239w[14]);
            za.k.d(b10, "<get-allNonStaticMembers>(...)");
            return (Collection) b10;
        }

        public final Collection<KCallableImpl<?>> h() {
            T b10 = this.f13255s.b(this, f13239w[15]);
            za.k.d(b10, "<get-allStaticMembers>(...)");
            return (Collection) b10;
        }

        public final List<Annotation> i() {
            T b10 = this.f13241e.b(this, f13239w[1]);
            za.k.d(b10, "<get-annotations>(...)");
            return (List) b10;
        }

        public final Collection<KFunction<T>> j() {
            T b10 = this.f13244h.b(this, f13239w[4]);
            za.k.d(b10, "<get-constructors>(...)");
            return (Collection) b10;
        }

        public final Collection<KCallableImpl<?>> k() {
            T b10 = this.f13250n.b(this, f13239w[10]);
            za.k.d(b10, "<get-declaredNonStaticMembers>(...)");
            return (Collection) b10;
        }

        public final ClassDescriptor m() {
            T b10 = this.f13240d.b(this, f13239w[0]);
            za.k.d(b10, "<get-descriptor>(...)");
            return (ClassDescriptor) b10;
        }

        public final T p() {
            return this.f13246j.b(this, f13239w[6]);
        }

        public final String q() {
            return (String) this.f13243g.b(this, f13239w[3]);
        }

        public final String r() {
            return (String) this.f13242f.b(this, f13239w[2]);
        }
    }

    /* compiled from: KClassImpl.kt */
    /* renamed from: jb.l$b */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f13285a;

        static {
            int[] iArr = new int[KotlinClassHeader.a.values().length];
            try {
                iArr[KotlinClassHeader.a.FILE_FACADE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[KotlinClassHeader.a.MULTIFILE_CLASS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[KotlinClassHeader.a.MULTIFILE_CLASS_PART.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[KotlinClassHeader.a.SYNTHETIC_CLASS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[KotlinClassHeader.a.UNKNOWN.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[KotlinClassHeader.a.CLASS.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            f13285a = iArr;
        }
    }

    /* compiled from: KClassImpl.kt */
    /* renamed from: jb.l$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<KClassImpl<T>.a> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ KClassImpl<T> f13286e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(KClassImpl<T> kClassImpl) {
            super(0);
            this.f13286e = kClassImpl;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final KClassImpl<T>.a invoke() {
            return new a();
        }
    }

    /* compiled from: KClassImpl.kt */
    /* renamed from: jb.l$d */
    /* loaded from: classes2.dex */
    /* synthetic */ class d extends FunctionReference implements ya.p<MemberDeserializer, jc.n, PropertyDescriptor> {

        /* renamed from: n, reason: collision with root package name */
        public static final d f13287n = new d();

        d() {
            super(2);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(MemberDeserializer.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "loadProperty(Lorg/jetbrains/kotlin/metadata/ProtoBuf$Property;)Lorg/jetbrains/kotlin/descriptors/PropertyDescriptor;";
        }

        @Override // ya.p
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final PropertyDescriptor invoke(MemberDeserializer memberDeserializer, jc.n nVar) {
            za.k.e(memberDeserializer, "p0");
            za.k.e(nVar, "p1");
            return memberDeserializer.l(nVar);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "loadProperty";
        }
    }

    public KClassImpl(Class<T> cls) {
        za.k.e(cls, "jClass");
        this.f13237h = cls;
        ReflectProperties.b<KClassImpl<T>.a> b10 = ReflectProperties.b(new c(this));
        za.k.d(b10, "lazy { Data() }");
        this.f13238i = b10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ClassId W() {
        return l0.f13288a.c(e());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Void c0() {
        KotlinClassHeader b10;
        ub.f a10 = ub.f.f18974c.a(e());
        KotlinClassHeader.a c10 = (a10 == null || (b10 = a10.b()) == null) ? null : b10.c();
        switch (c10 == null ? -1 : b.f13285a[c10.ordinal()]) {
            case -1:
            case 6:
                throw new KotlinReflectionInternalError("Unresolved class: " + e());
            case 0:
            default:
                throw new NoWhenBranchMatchedException();
            case 1:
            case 2:
            case 3:
                throw new UnsupportedOperationException("Packages and file facades are not yet supported in Kotlin reflection. Meanwhile please use Java reflection to inspect this class: " + e());
            case 4:
                throw new UnsupportedOperationException("This class is an internal synthetic class generated by the Kotlin compiler, such as an anonymous class for a lambda, a SAM wrapper, a callable reference, etc. It's not a Kotlin class or interface, so the reflection library has no idea what declarations it has. Please use Java reflection to inspect this class: " + e());
            case 5:
                throw new KotlinReflectionInternalError("Unknown class: " + e() + " (kind = " + c10 + ')');
        }
    }

    @Override // jb.KDeclarationContainerImpl
    public Collection<ConstructorDescriptor> I() {
        List j10;
        ClassDescriptor descriptor = getDescriptor();
        if (descriptor.getKind() != ClassKind.INTERFACE && descriptor.getKind() != ClassKind.OBJECT) {
            Collection<ClassConstructorDescriptor> p10 = descriptor.p();
            za.k.d(p10, "descriptor.constructors");
            return p10;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // jb.KDeclarationContainerImpl
    public Collection<FunctionDescriptor> J(Name name) {
        List m02;
        za.k.e(name, "name");
        zc.h a02 = a0();
        xb.d dVar = xb.d.FROM_REFLECTION;
        m02 = _Collections.m0(a02.c(name, dVar), b0().c(name, dVar));
        return m02;
    }

    @Override // jb.KDeclarationContainerImpl
    public PropertyDescriptor K(int i10) {
        Class<?> declaringClass;
        if (za.k.a(e().getSimpleName(), "DefaultImpls") && (declaringClass = e().getDeclaringClass()) != null && declaringClass.isInterface()) {
            KClass e10 = JvmClassMapping.e(declaringClass);
            za.k.c(e10, "null cannot be cast to non-null type kotlin.reflect.jvm.internal.KClassImpl<*>");
            return ((KClassImpl) e10).K(i10);
        }
        ClassDescriptor descriptor = getDescriptor();
        DeserializedClassDescriptor deserializedClassDescriptor = descriptor instanceof DeserializedClassDescriptor ? (DeserializedClassDescriptor) descriptor : null;
        if (deserializedClassDescriptor == null) {
            return null;
        }
        jc.c k12 = deserializedClassDescriptor.k1();
        i.f<jc.c, List<jc.n>> fVar = JvmProtoBuf.f15373j;
        za.k.d(fVar, "classLocalVariable");
        jc.n nVar = (jc.n) ProtoBufUtil.b(k12, fVar, i10);
        if (nVar != null) {
            return (PropertyDescriptor) o0.h(e(), nVar, deserializedClassDescriptor.j1().g(), deserializedClassDescriptor.j1().j(), deserializedClassDescriptor.m1(), d.f13287n);
        }
        return null;
    }

    @Override // jb.KDeclarationContainerImpl
    public Collection<PropertyDescriptor> N(Name name) {
        List m02;
        za.k.e(name, "name");
        zc.h a02 = a0();
        xb.d dVar = xb.d.FROM_REFLECTION;
        m02 = _Collections.m0(a02.a(name, dVar), b0().a(name, dVar));
        return m02;
    }

    public Collection<KFunction<T>> X() {
        return this.f13238i.invoke().j();
    }

    public final ReflectProperties.b<KClassImpl<T>.a> Y() {
        return this.f13238i;
    }

    @Override // jb.KClassifierImpl
    /* renamed from: Z, reason: merged with bridge method [inline-methods] */
    public ClassDescriptor getDescriptor() {
        return this.f13238i.invoke().m();
    }

    public final zc.h a0() {
        return getDescriptor().x().u();
    }

    public final zc.h b0() {
        zc.h a02 = getDescriptor().a0();
        za.k.d(a02, "descriptor.staticScope");
        return a02;
    }

    @Override // za.ClassBasedDeclarationContainer
    public Class<T> e() {
        return this.f13237h;
    }

    public boolean equals(Object obj) {
        return (obj instanceof KClassImpl) && za.k.a(JvmClassMapping.c(this), JvmClassMapping.c((KClass) obj));
    }

    public int hashCode() {
        return JvmClassMapping.c(this).hashCode();
    }

    @Override // gb.KAnnotatedElement
    public List<Annotation> i() {
        return this.f13238i.invoke().i();
    }

    @Override // gb.KClass
    public boolean n() {
        return getDescriptor().o() == Modality.ABSTRACT;
    }

    @Override // gb.KClass
    public boolean q() {
        return getDescriptor().q();
    }

    @Override // gb.KClass
    public boolean r() {
        return getDescriptor().r();
    }

    @Override // gb.KClass
    public boolean t() {
        return getDescriptor().o() == Modality.SEALED;
    }

    public String toString() {
        String str;
        String y4;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("class ");
        ClassId W = W();
        FqName h10 = W.h();
        za.k.d(h10, "classId.packageFqName");
        if (h10.d()) {
            str = "";
        } else {
            str = h10.b() + '.';
        }
        String b10 = W.i().b();
        za.k.d(b10, "classId.relativeClassName.asString()");
        y4 = StringsJVM.y(b10, '.', '$', false, 4, null);
        sb2.append(str + y4);
        return sb2.toString();
    }

    @Override // gb.KClass
    public String u() {
        return this.f13238i.invoke().q();
    }

    @Override // gb.KClass
    public String v() {
        return this.f13238i.invoke().r();
    }

    @Override // gb.KClass
    public T w() {
        return this.f13238i.invoke().p();
    }

    @Override // gb.KClass
    public boolean y(Object obj) {
        Integer c10 = reflectClassUtil.c(e());
        if (c10 != null) {
            return TypeIntrinsics.i(obj, c10.intValue());
        }
        Class g6 = reflectClassUtil.g(e());
        if (g6 == null) {
            g6 = e();
        }
        return g6.isInstance(obj);
    }
}
