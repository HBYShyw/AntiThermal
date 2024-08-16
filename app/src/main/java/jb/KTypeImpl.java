package jb;

import gb.KClassifier;
import gb.KTypeProjection;
import gd.TypeProjection;
import gd.Variance;
import gd.s1;
import ib.KTypesJvm;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;
import jb.ReflectProperties;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import ma.NoWhenBranchMatchedException;
import ma.Standard;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import vb.reflectClassUtil;
import xa.JvmClassMapping;
import za.DefaultConstructorMarker;
import za.KTypeBase;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: KTypeImpl.kt */
/* renamed from: jb.d0, reason: use source file name */
/* loaded from: classes2.dex */
public final class KTypeImpl implements KTypeBase {

    /* renamed from: i, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f13169i = {Reflection.g(new PropertyReference1Impl(Reflection.b(KTypeImpl.class), "classifier", "getClassifier()Lkotlin/reflect/KClassifier;")), Reflection.g(new PropertyReference1Impl(Reflection.b(KTypeImpl.class), "arguments", "getArguments()Ljava/util/List;"))};

    /* renamed from: e, reason: collision with root package name */
    private final gd.g0 f13170e;

    /* renamed from: f, reason: collision with root package name */
    private final ReflectProperties.a<Type> f13171f;

    /* renamed from: g, reason: collision with root package name */
    private final ReflectProperties.a f13172g;

    /* renamed from: h, reason: collision with root package name */
    private final ReflectProperties.a f13173h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KTypeImpl.kt */
    /* renamed from: jb.d0$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.a<List<? extends KTypeProjection>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ ya.a<Type> f13175f;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: KTypeImpl.kt */
        /* renamed from: jb.d0$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0055a extends Lambda implements ya.a<Type> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KTypeImpl f13176e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ int f13177f;

            /* renamed from: g, reason: collision with root package name */
            final /* synthetic */ ma.h<List<Type>> f13178g;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            C0055a(KTypeImpl kTypeImpl, int i10, ma.h<? extends List<? extends Type>> hVar) {
                super(0);
                this.f13176e = kTypeImpl;
                this.f13177f = i10;
                this.f13178g = hVar;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Type invoke() {
                Object B;
                Object A;
                Type o10 = this.f13176e.o();
                if (o10 instanceof Class) {
                    Class cls = (Class) o10;
                    Class componentType = cls.isArray() ? cls.getComponentType() : Object.class;
                    za.k.d(componentType, "{\n                      …                        }");
                    return componentType;
                }
                if (o10 instanceof GenericArrayType) {
                    if (this.f13177f == 0) {
                        Type genericComponentType = ((GenericArrayType) o10).getGenericComponentType();
                        za.k.d(genericComponentType, "{\n                      …                        }");
                        return genericComponentType;
                    }
                    throw new KotlinReflectionInternalError("Array type has been queried for a non-0th argument: " + this.f13176e);
                }
                if (o10 instanceof ParameterizedType) {
                    Type type = (Type) a.c(this.f13178g).get(this.f13177f);
                    if (type instanceof WildcardType) {
                        WildcardType wildcardType = (WildcardType) type;
                        Type[] lowerBounds = wildcardType.getLowerBounds();
                        za.k.d(lowerBounds, "argument.lowerBounds");
                        B = _Arrays.B(lowerBounds);
                        Type type2 = (Type) B;
                        if (type2 == null) {
                            Type[] upperBounds = wildcardType.getUpperBounds();
                            za.k.d(upperBounds, "argument.upperBounds");
                            A = _Arrays.A(upperBounds);
                            type = (Type) A;
                        } else {
                            type = type2;
                        }
                    }
                    za.k.d(type, "{\n                      …                        }");
                    return type;
                }
                throw new KotlinReflectionInternalError("Non-generic type has been queried for arguments: " + this.f13176e);
            }
        }

        /* compiled from: KTypeImpl.kt */
        /* renamed from: jb.d0$a$b */
        /* loaded from: classes2.dex */
        public /* synthetic */ class b {

            /* renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f13179a;

            static {
                int[] iArr = new int[Variance.values().length];
                try {
                    iArr[Variance.INVARIANT.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[Variance.IN_VARIANCE.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[Variance.OUT_VARIANCE.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                f13179a = iArr;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: KTypeImpl.kt */
        /* renamed from: jb.d0$a$c */
        /* loaded from: classes2.dex */
        public static final class c extends Lambda implements ya.a<List<? extends Type>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KTypeImpl f13180e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            c(KTypeImpl kTypeImpl) {
                super(0);
                this.f13180e = kTypeImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<Type> invoke() {
                Type o10 = this.f13180e.o();
                za.k.b(o10);
                return reflectClassUtil.d(o10);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        a(ya.a<? extends Type> aVar) {
            super(0);
            this.f13175f = aVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final List<Type> c(ma.h<? extends List<? extends Type>> hVar) {
            return (List) hVar.getValue();
        }

        @Override // ya.a
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public final List<KTypeProjection> invoke() {
            ma.h a10;
            int u7;
            KTypeProjection d10;
            List<KTypeProjection> j10;
            List<TypeProjection> U0 = KTypeImpl.this.m().U0();
            if (U0.isEmpty()) {
                j10 = kotlin.collections.r.j();
                return j10;
            }
            a10 = ma.j.a(ma.l.PUBLICATION, new c(KTypeImpl.this));
            ya.a<Type> aVar = this.f13175f;
            KTypeImpl kTypeImpl = KTypeImpl.this;
            u7 = kotlin.collections.s.u(U0, 10);
            ArrayList arrayList = new ArrayList(u7);
            int i10 = 0;
            for (Object obj : U0) {
                int i11 = i10 + 1;
                if (i10 < 0) {
                    kotlin.collections.r.t();
                }
                TypeProjection typeProjection = (TypeProjection) obj;
                if (typeProjection.b()) {
                    d10 = KTypeProjection.f11621c.c();
                } else {
                    gd.g0 type = typeProjection.getType();
                    za.k.d(type, "typeProjection.type");
                    KTypeImpl kTypeImpl2 = new KTypeImpl(type, aVar == null ? null : new C0055a(kTypeImpl, i10, a10));
                    int i12 = b.f13179a[typeProjection.a().ordinal()];
                    if (i12 == 1) {
                        d10 = KTypeProjection.f11621c.d(kTypeImpl2);
                    } else if (i12 != 2) {
                        if (i12 != 3) {
                            throw new NoWhenBranchMatchedException();
                        }
                        d10 = KTypeProjection.f11621c.b(kTypeImpl2);
                    } else {
                        d10 = KTypeProjection.f11621c.a(kTypeImpl2);
                    }
                }
                arrayList.add(d10);
                i10 = i11;
            }
            return arrayList;
        }
    }

    /* compiled from: KTypeImpl.kt */
    /* renamed from: jb.d0$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<KClassifier> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final KClassifier invoke() {
            KTypeImpl kTypeImpl = KTypeImpl.this;
            return kTypeImpl.g(kTypeImpl.m());
        }
    }

    public KTypeImpl(gd.g0 g0Var, ya.a<? extends Type> aVar) {
        za.k.e(g0Var, "type");
        this.f13170e = g0Var;
        ReflectProperties.a<Type> aVar2 = null;
        ReflectProperties.a<Type> aVar3 = aVar instanceof ReflectProperties.a ? (ReflectProperties.a) aVar : null;
        if (aVar3 != null) {
            aVar2 = aVar3;
        } else if (aVar != null) {
            aVar2 = ReflectProperties.d(aVar);
        }
        this.f13171f = aVar2;
        this.f13172g = ReflectProperties.d(new b());
        this.f13173h = ReflectProperties.d(new a(aVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final KClassifier g(gd.g0 g0Var) {
        Object s02;
        gd.g0 type;
        ClassifierDescriptor v7 = g0Var.W0().v();
        if (v7 instanceof ClassDescriptor) {
            Class<?> p10 = o0.p((ClassDescriptor) v7);
            if (p10 == null) {
                return null;
            }
            if (p10.isArray()) {
                s02 = _Collections.s0(g0Var.U0());
                TypeProjection typeProjection = (TypeProjection) s02;
                if (typeProjection != null && (type = typeProjection.getType()) != null) {
                    KClassifier g6 = g(type);
                    if (g6 != null) {
                        return new KClassImpl(o0.f(JvmClassMapping.b(KTypesJvm.a(g6))));
                    }
                    throw new KotlinReflectionInternalError("Cannot determine classifier for array element type: " + this);
                }
                return new KClassImpl(p10);
            }
            if (!s1.l(g0Var)) {
                Class<?> e10 = reflectClassUtil.e(p10);
                if (e10 != null) {
                    p10 = e10;
                }
                return new KClassImpl(p10);
            }
            return new KClassImpl(p10);
        }
        if (v7 instanceof TypeParameterDescriptor) {
            return new KTypeParameterImpl(null, (TypeParameterDescriptor) v7);
        }
        if (!(v7 instanceof TypeAliasDescriptor)) {
            return null;
        }
        throw new Standard("An operation is not implemented: Type alias classifiers are not yet supported");
    }

    @Override // gb.KType
    public List<KTypeProjection> b() {
        T b10 = this.f13173h.b(this, f13169i[1]);
        za.k.d(b10, "<get-arguments>(...)");
        return (List) b10;
    }

    @Override // gb.KType
    public KClassifier c() {
        return (KClassifier) this.f13172g.b(this, f13169i[0]);
    }

    public boolean equals(Object obj) {
        if (obj instanceof KTypeImpl) {
            KTypeImpl kTypeImpl = (KTypeImpl) obj;
            if (za.k.a(this.f13170e, kTypeImpl.f13170e) && za.k.a(c(), kTypeImpl.c()) && za.k.a(b(), kTypeImpl.b())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hashCode = this.f13170e.hashCode() * 31;
        KClassifier c10 = c();
        return ((hashCode + (c10 != null ? c10.hashCode() : 0)) * 31) + b().hashCode();
    }

    @Override // gb.KAnnotatedElement
    public List<Annotation> i() {
        return o0.e(this.f13170e);
    }

    @Override // gb.KType
    public boolean l() {
        return this.f13170e.X0();
    }

    public final gd.g0 m() {
        return this.f13170e;
    }

    @Override // za.KTypeBase
    public Type o() {
        ReflectProperties.a<Type> aVar = this.f13171f;
        if (aVar != null) {
            return aVar.invoke();
        }
        return null;
    }

    public String toString() {
        return ReflectionObjectRenderer.f13232a.h(this.f13170e);
    }

    public /* synthetic */ KTypeImpl(gd.g0 g0Var, ya.a aVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(g0Var, (i10 & 2) != 0 ? null : aVar);
    }
}
