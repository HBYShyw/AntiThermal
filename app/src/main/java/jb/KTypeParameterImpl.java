package jb;

import gb.KClass;
import gb.KType;
import gb.KTypeParameter;
import gb.KVariance;
import gd.Variance;
import hc.JvmPackagePartSource;
import hc.KotlinJvmBinaryClass;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jb.ReflectProperties;
import ma.NoWhenBranchMatchedException;
import ma.Unit;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.TypeParameterDescriptor;
import xa.JvmClassMapping;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import za.TypeParameterReference;

/* compiled from: KTypeParameterImpl.kt */
/* renamed from: jb.e0, reason: use source file name */
/* loaded from: classes2.dex */
public final class KTypeParameterImpl implements KTypeParameter, KClassifierImpl {

    /* renamed from: h, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f13184h = {Reflection.g(new PropertyReference1Impl(Reflection.b(KTypeParameterImpl.class), "upperBounds", "getUpperBounds()Ljava/util/List;"))};

    /* renamed from: e, reason: collision with root package name */
    private final TypeParameterDescriptor f13185e;

    /* renamed from: f, reason: collision with root package name */
    private final ReflectProperties.a f13186f;

    /* renamed from: g, reason: collision with root package name */
    private final KTypeParameterOwnerImpl f13187g;

    /* compiled from: KTypeParameterImpl.kt */
    /* renamed from: jb.e0$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f13188a;

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
            f13188a = iArr;
        }
    }

    /* compiled from: KTypeParameterImpl.kt */
    /* renamed from: jb.e0$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<List<? extends KTypeImpl>> {
        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<KTypeImpl> invoke() {
            int u7;
            List<gd.g0> upperBounds = KTypeParameterImpl.this.getDescriptor().getUpperBounds();
            za.k.d(upperBounds, "descriptor.upperBounds");
            u7 = kotlin.collections.s.u(upperBounds, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = upperBounds.iterator();
            while (it.hasNext()) {
                arrayList.add(new KTypeImpl((gd.g0) it.next(), null, 2, null));
            }
            return arrayList;
        }
    }

    public KTypeParameterImpl(KTypeParameterOwnerImpl kTypeParameterOwnerImpl, TypeParameterDescriptor typeParameterDescriptor) {
        KClassImpl<?> kClassImpl;
        Object H0;
        za.k.e(typeParameterDescriptor, "descriptor");
        this.f13185e = typeParameterDescriptor;
        this.f13186f = ReflectProperties.d(new b());
        if (kTypeParameterOwnerImpl == null) {
            DeclarationDescriptor b10 = getDescriptor().b();
            za.k.d(b10, "descriptor.containingDeclaration");
            if (b10 instanceof ClassDescriptor) {
                H0 = c((ClassDescriptor) b10);
            } else if (b10 instanceof CallableMemberDescriptor) {
                DeclarationDescriptor b11 = ((CallableMemberDescriptor) b10).b();
                za.k.d(b11, "declaration.containingDeclaration");
                if (b11 instanceof ClassDescriptor) {
                    kClassImpl = c((ClassDescriptor) b11);
                } else {
                    ed.g gVar = b10 instanceof ed.g ? (ed.g) b10 : null;
                    if (gVar != null) {
                        KClass e10 = JvmClassMapping.e(a(gVar));
                        za.k.c(e10, "null cannot be cast to non-null type kotlin.reflect.jvm.internal.KClassImpl<*>");
                        kClassImpl = (KClassImpl) e10;
                    } else {
                        throw new KotlinReflectionInternalError("Non-class callable descriptor must be deserialized: " + b10);
                    }
                }
                H0 = b10.H0(new f(kClassImpl), Unit.f15173a);
            } else {
                throw new KotlinReflectionInternalError("Unknown type parameter container: " + b10);
            }
            za.k.d(H0, "when (val declaration = … $declaration\")\n        }");
            kTypeParameterOwnerImpl = (KTypeParameterOwnerImpl) H0;
        }
        this.f13187g = kTypeParameterOwnerImpl;
    }

    private final Class<?> a(ed.g gVar) {
        Class<?> f10;
        ed.f j02 = gVar.j0();
        if (!(j02 instanceof JvmPackagePartSource)) {
            j02 = null;
        }
        JvmPackagePartSource jvmPackagePartSource = (JvmPackagePartSource) j02;
        KotlinJvmBinaryClass g6 = jvmPackagePartSource != null ? jvmPackagePartSource.g() : null;
        ub.f fVar = (ub.f) (g6 instanceof ub.f ? g6 : null);
        if (fVar != null && (f10 = fVar.f()) != null) {
            return f10;
        }
        throw new KotlinReflectionInternalError("Container of deserialized member is not resolved: " + gVar);
    }

    private final KClassImpl<?> c(ClassDescriptor classDescriptor) {
        Class<?> p10 = o0.p(classDescriptor);
        KClassImpl<?> kClassImpl = (KClassImpl) (p10 != null ? JvmClassMapping.e(p10) : null);
        if (kClassImpl != null) {
            return kClassImpl;
        }
        throw new KotlinReflectionInternalError("Type parameter container is not resolved: " + classDescriptor.b());
    }

    @Override // jb.KClassifierImpl
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public TypeParameterDescriptor getDescriptor() {
        return this.f13185e;
    }

    public boolean equals(Object obj) {
        if (obj instanceof KTypeParameterImpl) {
            KTypeParameterImpl kTypeParameterImpl = (KTypeParameterImpl) obj;
            if (za.k.a(this.f13187g, kTypeParameterImpl.f13187g) && za.k.a(getName(), kTypeParameterImpl.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override // gb.KTypeParameter
    public String getName() {
        String b10 = getDescriptor().getName().b();
        za.k.d(b10, "descriptor.name.asString()");
        return b10;
    }

    @Override // gb.KTypeParameter
    public List<KType> getUpperBounds() {
        T b10 = this.f13186f.b(this, f13184h[0]);
        za.k.d(b10, "<get-upperBounds>(...)");
        return (List) b10;
    }

    public int hashCode() {
        return (this.f13187g.hashCode() * 31) + getName().hashCode();
    }

    @Override // gb.KTypeParameter
    public KVariance s() {
        int i10 = a.f13188a[getDescriptor().s().ordinal()];
        if (i10 == 1) {
            return KVariance.INVARIANT;
        }
        if (i10 == 2) {
            return KVariance.IN;
        }
        if (i10 == 3) {
            return KVariance.OUT;
        }
        throw new NoWhenBranchMatchedException();
    }

    public String toString() {
        return TypeParameterReference.f20367e.a(this);
    }
}
