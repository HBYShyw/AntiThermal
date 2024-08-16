package jb;

import ac.JavaCallableMemberDescriptor;
import com.oplus.backup.sdk.common.utils.Constants;
import gb.KCallable;
import gb.KParameter;
import gb.KType;
import ib.KTypesJvm;
import ib.ReflectJvmMapping;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jb.ReflectProperties;
import kotlin.collections.MutableCollectionsJVM;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import pb.CallableMemberDescriptor;
import pb.FunctionDescriptor;
import pb.ParameterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import xa.JvmClassMapping;
import za.Lambda;

/* compiled from: KCallableImpl.kt */
/* renamed from: jb.k, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class KCallableImpl<R> implements KCallable<R>, KTypeParameterOwnerImpl {

    /* renamed from: e, reason: collision with root package name */
    private final ReflectProperties.a<List<Annotation>> f13219e;

    /* renamed from: f, reason: collision with root package name */
    private final ReflectProperties.a<ArrayList<KParameter>> f13220f;

    /* renamed from: g, reason: collision with root package name */
    private final ReflectProperties.a<KTypeImpl> f13221g;

    /* renamed from: h, reason: collision with root package name */
    private final ReflectProperties.a<List<KTypeParameterImpl>> f13222h;

    /* compiled from: KCallableImpl.kt */
    /* renamed from: jb.k$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.a<List<? extends Annotation>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ KCallableImpl<R> f13223e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        a(KCallableImpl<? extends R> kCallableImpl) {
            super(0);
            this.f13223e = kCallableImpl;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<Annotation> invoke() {
            return o0.e(this.f13223e.L());
        }
    }

    /* compiled from: KCallableImpl.kt */
    /* renamed from: jb.k$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<ArrayList<KParameter>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ KCallableImpl<R> f13224e;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: KCallableImpl.kt */
        /* renamed from: jb.k$b$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.a<ParameterDescriptor> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ ReceiverParameterDescriptor f13225e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(ReceiverParameterDescriptor receiverParameterDescriptor) {
                super(0);
                this.f13225e = receiverParameterDescriptor;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final ParameterDescriptor invoke() {
                return this.f13225e;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: KCallableImpl.kt */
        /* renamed from: jb.k$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0057b extends Lambda implements ya.a<ParameterDescriptor> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ ReceiverParameterDescriptor f13226e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0057b(ReceiverParameterDescriptor receiverParameterDescriptor) {
                super(0);
                this.f13226e = receiverParameterDescriptor;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final ParameterDescriptor invoke() {
                return this.f13226e;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: KCallableImpl.kt */
        /* renamed from: jb.k$b$c */
        /* loaded from: classes2.dex */
        public static final class c extends Lambda implements ya.a<ParameterDescriptor> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ CallableMemberDescriptor f13227e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ int f13228f;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            c(CallableMemberDescriptor callableMemberDescriptor, int i10) {
                super(0);
                this.f13227e = callableMemberDescriptor;
                this.f13228f = i10;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final ParameterDescriptor invoke() {
                ValueParameterDescriptor valueParameterDescriptor = this.f13227e.l().get(this.f13228f);
                za.k.d(valueParameterDescriptor, "descriptor.valueParameters[i]");
                return valueParameterDescriptor;
            }
        }

        /* compiled from: Comparisons.kt */
        /* renamed from: jb.k$b$d */
        /* loaded from: classes2.dex */
        public static final class d<T> implements Comparator {
            @Override // java.util.Comparator
            public final int compare(T t7, T t10) {
                int a10;
                a10 = pa.b.a(((KParameter) t7).getName(), ((KParameter) t10).getName());
                return a10;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        b(KCallableImpl<? extends R> kCallableImpl) {
            super(0);
            this.f13224e = kCallableImpl;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ArrayList<KParameter> invoke() {
            int i10;
            CallableMemberDescriptor L = this.f13224e.L();
            ArrayList<KParameter> arrayList = new ArrayList<>();
            int i11 = 0;
            if (this.f13224e.K()) {
                i10 = 0;
            } else {
                ReceiverParameterDescriptor i12 = o0.i(L);
                if (i12 != null) {
                    arrayList.add(new KParameterImpl(this.f13224e, 0, KParameter.a.INSTANCE, new a(i12)));
                    i10 = 1;
                } else {
                    i10 = 0;
                }
                ReceiverParameterDescriptor r02 = L.r0();
                if (r02 != null) {
                    arrayList.add(new KParameterImpl(this.f13224e, i10, KParameter.a.EXTENSION_RECEIVER, new C0057b(r02)));
                    i10++;
                }
            }
            int size = L.l().size();
            while (i11 < size) {
                arrayList.add(new KParameterImpl(this.f13224e, i10, KParameter.a.VALUE, new c(L, i11)));
                i11++;
                i10++;
            }
            if (this.f13224e.J() && (L instanceof JavaCallableMemberDescriptor) && arrayList.size() > 1) {
                MutableCollectionsJVM.y(arrayList, new d());
            }
            arrayList.trimToSize();
            return arrayList;
        }
    }

    /* compiled from: KCallableImpl.kt */
    /* renamed from: jb.k$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<KTypeImpl> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ KCallableImpl<R> f13229e;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: KCallableImpl.kt */
        /* renamed from: jb.k$c$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.a<Type> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KCallableImpl<R> f13230e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            a(KCallableImpl<? extends R> kCallableImpl) {
                super(0);
                this.f13230e = kCallableImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Type invoke() {
                Type E = this.f13230e.E();
                return E == null ? this.f13230e.F().f() : E;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        c(KCallableImpl<? extends R> kCallableImpl) {
            super(0);
            this.f13229e = kCallableImpl;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final KTypeImpl invoke() {
            gd.g0 f10 = this.f13229e.L().f();
            za.k.b(f10);
            return new KTypeImpl(f10, new a(this.f13229e));
        }
    }

    /* compiled from: KCallableImpl.kt */
    /* renamed from: jb.k$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.a<List<? extends KTypeParameterImpl>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ KCallableImpl<R> f13231e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        d(KCallableImpl<? extends R> kCallableImpl) {
            super(0);
            this.f13231e = kCallableImpl;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<KTypeParameterImpl> invoke() {
            int u7;
            List<TypeParameterDescriptor> m10 = this.f13231e.L().m();
            za.k.d(m10, "descriptor.typeParameters");
            KCallableImpl<R> kCallableImpl = this.f13231e;
            u7 = kotlin.collections.s.u(m10, 10);
            ArrayList arrayList = new ArrayList(u7);
            for (TypeParameterDescriptor typeParameterDescriptor : m10) {
                za.k.d(typeParameterDescriptor, "descriptor");
                arrayList.add(new KTypeParameterImpl(kCallableImpl, typeParameterDescriptor));
            }
            return arrayList;
        }
    }

    public KCallableImpl() {
        ReflectProperties.a<List<Annotation>> d10 = ReflectProperties.d(new a(this));
        za.k.d(d10, "lazySoft { descriptor.computeAnnotations() }");
        this.f13219e = d10;
        ReflectProperties.a<ArrayList<KParameter>> d11 = ReflectProperties.d(new b(this));
        za.k.d(d11, "lazySoft {\n        val d…ze()\n        result\n    }");
        this.f13220f = d11;
        ReflectProperties.a<KTypeImpl> d12 = ReflectProperties.d(new c(this));
        za.k.d(d12, "lazySoft {\n        KType…eturnType\n        }\n    }");
        this.f13221g = d12;
        ReflectProperties.a<List<KTypeParameterImpl>> d13 = ReflectProperties.d(new d(this));
        za.k.d(d13, "lazySoft {\n        descr…this, descriptor) }\n    }");
        this.f13222h = d13;
    }

    private final R B(Map<KParameter, ? extends Object> map) {
        int u7;
        Object D;
        List<KParameter> parameters = getParameters();
        u7 = kotlin.collections.s.u(parameters, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (KParameter kParameter : parameters) {
            if (map.containsKey(kParameter)) {
                D = map.get(kParameter);
                if (D == null) {
                    throw new IllegalArgumentException("Annotation argument value cannot be null (" + kParameter + ')');
                }
            } else if (kParameter.z()) {
                D = null;
            } else {
                if (!kParameter.a()) {
                    throw new IllegalArgumentException("No argument provided for a required parameter: " + kParameter);
                }
                D = D(kParameter.getType());
            }
            arrayList.add(D);
        }
        kb.e<?> H = H();
        if (H != null) {
            try {
                return (R) H.d(arrayList.toArray(new Object[0]));
            } catch (IllegalAccessException e10) {
                throw new hb.a(e10);
            }
        }
        throw new KotlinReflectionInternalError("This callable does not support a default call: " + L());
    }

    private final Object D(KType kType) {
        Class b10 = JvmClassMapping.b(KTypesJvm.b(kType));
        if (b10.isArray()) {
            Object newInstance = Array.newInstance(b10.getComponentType(), 0);
            za.k.d(newInstance, "type.jvmErasure.java.run…\"\n            )\n        }");
            return newInstance;
        }
        throw new KotlinReflectionInternalError("Cannot instantiate the default empty array of type " + b10.getSimpleName() + ", because it is not an array type");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Type E() {
        Object g02;
        Object T;
        Type[] lowerBounds;
        Object A;
        CallableMemberDescriptor L = L();
        FunctionDescriptor functionDescriptor = L instanceof FunctionDescriptor ? (FunctionDescriptor) L : null;
        if (!(functionDescriptor != null && functionDescriptor.C0())) {
            return null;
        }
        g02 = _Collections.g0(F().a());
        ParameterizedType parameterizedType = g02 instanceof ParameterizedType ? (ParameterizedType) g02 : null;
        if (!za.k.a(parameterizedType != null ? parameterizedType.getRawType() : null, qa.d.class)) {
            return null;
        }
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        za.k.d(actualTypeArguments, "continuationType.actualTypeArguments");
        T = _Arrays.T(actualTypeArguments);
        WildcardType wildcardType = T instanceof WildcardType ? (WildcardType) T : null;
        if (wildcardType == null || (lowerBounds = wildcardType.getLowerBounds()) == null) {
            return null;
        }
        A = _Arrays.A(lowerBounds);
        return (Type) A;
    }

    public final R C(Map<KParameter, ? extends Object> map, qa.d<?> dVar) {
        za.k.e(map, Constants.MessagerConstants.ARGS_KEY);
        List<KParameter> parameters = getParameters();
        ArrayList arrayList = new ArrayList(parameters.size());
        ArrayList arrayList2 = new ArrayList(1);
        Iterator<KParameter> it = parameters.iterator();
        int i10 = 0;
        boolean z10 = false;
        int i11 = 0;
        while (true) {
            if (!it.hasNext()) {
                if (dVar != null) {
                    arrayList.add(dVar);
                }
                if (!z10) {
                    Object[] array = arrayList.toArray(new Object[0]);
                    return d(Arrays.copyOf(array, array.length));
                }
                arrayList2.add(Integer.valueOf(i11));
                kb.e<?> H = H();
                if (H != null) {
                    arrayList.addAll(arrayList2);
                    arrayList.add(null);
                    try {
                        return (R) H.d(arrayList.toArray(new Object[0]));
                    } catch (IllegalAccessException e10) {
                        throw new hb.a(e10);
                    }
                }
                throw new KotlinReflectionInternalError("This callable does not support a default call: " + L());
            }
            KParameter next = it.next();
            if (i10 != 0 && i10 % 32 == 0) {
                arrayList2.add(Integer.valueOf(i11));
                i11 = 0;
            }
            if (map.containsKey(next)) {
                arrayList.add(map.get(next));
            } else if (next.z()) {
                arrayList.add(o0.k(next.getType()) ? null : o0.g(ReflectJvmMapping.f(next.getType())));
                i11 = (1 << (i10 % 32)) | i11;
                z10 = true;
            } else if (next.a()) {
                arrayList.add(D(next.getType()));
            } else {
                throw new IllegalArgumentException("No argument provided for a required parameter: " + next);
            }
            if (next.getKind() == KParameter.a.VALUE) {
                i10++;
            }
        }
    }

    public abstract kb.e<?> F();

    public abstract KDeclarationContainerImpl G();

    public abstract kb.e<?> H();

    /* renamed from: I */
    public abstract CallableMemberDescriptor L();

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean J() {
        return za.k.a(getName(), "<init>") && G().e().isAnnotation();
    }

    public abstract boolean K();

    @Override // gb.KCallable
    public R d(Object... objArr) {
        za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
        try {
            return (R) F().d(objArr);
        } catch (IllegalAccessException e10) {
            throw new hb.a(e10);
        }
    }

    @Override // gb.KCallable
    public KType f() {
        KTypeImpl invoke = this.f13221g.invoke();
        za.k.d(invoke, "_returnType()");
        return invoke;
    }

    @Override // gb.KCallable
    public List<KParameter> getParameters() {
        ArrayList<KParameter> invoke = this.f13220f.invoke();
        za.k.d(invoke, "_parameters()");
        return invoke;
    }

    @Override // gb.KAnnotatedElement
    public List<Annotation> i() {
        List<Annotation> invoke = this.f13219e.invoke();
        za.k.d(invoke, "_annotations()");
        return invoke;
    }

    @Override // gb.KCallable
    public R p(Map<KParameter, ? extends Object> map) {
        za.k.e(map, Constants.MessagerConstants.ARGS_KEY);
        return J() ? B(map) : C(map, null);
    }
}
