package pb;

import gd.TypeConstructor;
import gd.TypeProjection;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import rd.Sequence;
import rd._Sequences;
import za.Lambda;

/* compiled from: typeParameterUtils.kt */
/* loaded from: classes2.dex */
public final class g1 {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: typeParameterUtils.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<DeclarationDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f16691e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(DeclarationDescriptor declarationDescriptor) {
            za.k.e(declarationDescriptor, "it");
            return Boolean.valueOf(declarationDescriptor instanceof CallableDescriptor);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: typeParameterUtils.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l<DeclarationDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f16692e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(DeclarationDescriptor declarationDescriptor) {
            za.k.e(declarationDescriptor, "it");
            return Boolean.valueOf(!(declarationDescriptor instanceof ConstructorDescriptor));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: typeParameterUtils.kt */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<DeclarationDescriptor, Sequence<? extends TypeParameterDescriptor>> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f16693e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Sequence<TypeParameterDescriptor> invoke(DeclarationDescriptor declarationDescriptor) {
            Sequence<TypeParameterDescriptor> K;
            za.k.e(declarationDescriptor, "it");
            List<TypeParameterDescriptor> m10 = ((CallableDescriptor) declarationDescriptor).m();
            za.k.d(m10, "it as CallableDescriptor).typeParameters");
            K = _Collections.K(m10);
            return K;
        }
    }

    public static final s0 a(gd.g0 g0Var) {
        za.k.e(g0Var, "<this>");
        ClassifierDescriptor v7 = g0Var.W0().v();
        return b(g0Var, v7 instanceof ClassifierDescriptorWithTypeParameters ? (ClassifierDescriptorWithTypeParameters) v7 : null, 0);
    }

    private static final s0 b(gd.g0 g0Var, ClassifierDescriptorWithTypeParameters classifierDescriptorWithTypeParameters, int i10) {
        if (classifierDescriptorWithTypeParameters == null || ErrorUtils.m(classifierDescriptorWithTypeParameters)) {
            return null;
        }
        int size = classifierDescriptorWithTypeParameters.B().size() + i10;
        if (!classifierDescriptorWithTypeParameters.r()) {
            if (size != g0Var.U0().size()) {
                sc.e.E(classifierDescriptorWithTypeParameters);
            }
            return new s0(classifierDescriptorWithTypeParameters, g0Var.U0().subList(i10, g0Var.U0().size()), null);
        }
        List<TypeProjection> subList = g0Var.U0().subList(i10, size);
        DeclarationDescriptor b10 = classifierDescriptorWithTypeParameters.b();
        return new s0(classifierDescriptorWithTypeParameters, subList, b(g0Var, b10 instanceof ClassifierDescriptorWithTypeParameters ? (ClassifierDescriptorWithTypeParameters) b10 : null, size));
    }

    private static final pb.c c(TypeParameterDescriptor typeParameterDescriptor, DeclarationDescriptor declarationDescriptor, int i10) {
        return new pb.c(typeParameterDescriptor, declarationDescriptor, i10);
    }

    public static final List<TypeParameterDescriptor> d(ClassifierDescriptorWithTypeParameters classifierDescriptorWithTypeParameters) {
        Sequence A;
        Sequence m10;
        Sequence q10;
        List C;
        List<TypeParameterDescriptor> list;
        DeclarationDescriptor declarationDescriptor;
        List<TypeParameterDescriptor> m02;
        int u7;
        List<TypeParameterDescriptor> m03;
        TypeConstructor n10;
        za.k.e(classifierDescriptorWithTypeParameters, "<this>");
        List<TypeParameterDescriptor> B = classifierDescriptorWithTypeParameters.B();
        za.k.d(B, "declaredTypeParameters");
        if (!classifierDescriptorWithTypeParameters.r() && !(classifierDescriptorWithTypeParameters.b() instanceof CallableDescriptor)) {
            return B;
        }
        A = _Sequences.A(wc.c.q(classifierDescriptorWithTypeParameters), a.f16691e);
        m10 = _Sequences.m(A, b.f16692e);
        q10 = _Sequences.q(m10, c.f16693e);
        C = _Sequences.C(q10);
        Iterator<DeclarationDescriptor> it = wc.c.q(classifierDescriptorWithTypeParameters).iterator();
        while (true) {
            list = null;
            if (!it.hasNext()) {
                declarationDescriptor = null;
                break;
            }
            declarationDescriptor = it.next();
            if (declarationDescriptor instanceof ClassDescriptor) {
                break;
            }
        }
        ClassDescriptor classDescriptor = (ClassDescriptor) declarationDescriptor;
        if (classDescriptor != null && (n10 = classDescriptor.n()) != null) {
            list = n10.getParameters();
        }
        if (list == null) {
            list = kotlin.collections.r.j();
        }
        if (C.isEmpty() && list.isEmpty()) {
            List<TypeParameterDescriptor> B2 = classifierDescriptorWithTypeParameters.B();
            za.k.d(B2, "declaredTypeParameters");
            return B2;
        }
        m02 = _Collections.m0(C, list);
        u7 = kotlin.collections.s.u(m02, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (TypeParameterDescriptor typeParameterDescriptor : m02) {
            za.k.d(typeParameterDescriptor, "it");
            arrayList.add(c(typeParameterDescriptor, classifierDescriptorWithTypeParameters, B.size()));
        }
        m03 = _Collections.m0(B, arrayList);
        return m03;
    }
}
