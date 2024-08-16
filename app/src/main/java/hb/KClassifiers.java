package hb;

import gb.KClassifier;
import gb.KType;
import gb.KTypeProjection;
import gb.KVariance;
import gd.TypeConstructor;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.c1;
import gd.g0;
import gd.h0;
import gd.o0;
import gd.u0;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import jb.KClassifierImpl;
import jb.KTypeImpl;
import jb.KotlinReflectionInternalError;
import kd.m;
import kotlin.collections.r;
import kotlin.collections.s;
import ma.NoWhenBranchMatchedException;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;
import za.k;

/* compiled from: KClassifiers.kt */
/* renamed from: hb.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class KClassifiers {

    /* compiled from: KClassifiers.kt */
    /* renamed from: hb.d$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f12092a;

        static {
            int[] iArr = new int[KVariance.values().length];
            try {
                iArr[KVariance.INVARIANT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[KVariance.IN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[KVariance.OUT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f12092a = iArr;
        }
    }

    private static final o0 a(c1 c1Var, TypeConstructor typeConstructor, List<KTypeProjection> list, boolean z10) {
        int u7;
        m u0Var;
        List<TypeParameterDescriptor> parameters = typeConstructor.getParameters();
        k.d(parameters, "typeConstructor.parameters");
        u7 = s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        int i10 = 0;
        for (Object obj : list) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                r.t();
            }
            KTypeProjection kTypeProjection = (KTypeProjection) obj;
            KTypeImpl kTypeImpl = (KTypeImpl) kTypeProjection.c();
            g0 m10 = kTypeImpl != null ? kTypeImpl.m() : null;
            KVariance d10 = kTypeProjection.d();
            int i12 = d10 == null ? -1 : a.f12092a[d10.ordinal()];
            if (i12 == -1) {
                TypeParameterDescriptor typeParameterDescriptor = parameters.get(i10);
                k.d(typeParameterDescriptor, "parameters[index]");
                u0Var = new u0(typeParameterDescriptor);
            } else if (i12 == 1) {
                Variance variance = Variance.INVARIANT;
                k.b(m10);
                u0Var = new TypeProjectionImpl(variance, m10);
            } else if (i12 == 2) {
                Variance variance2 = Variance.IN_VARIANCE;
                k.b(m10);
                u0Var = new TypeProjectionImpl(variance2, m10);
            } else if (i12 == 3) {
                Variance variance3 = Variance.OUT_VARIANCE;
                k.b(m10);
                u0Var = new TypeProjectionImpl(variance3, m10);
            } else {
                throw new NoWhenBranchMatchedException();
            }
            arrayList.add(u0Var);
            i10 = i11;
        }
        return h0.j(c1Var, typeConstructor, arrayList, z10, null, 16, null);
    }

    public static final KType b(KClassifier kClassifier, List<KTypeProjection> list, boolean z10, List<? extends Annotation> list2) {
        ClassifierDescriptor descriptor;
        k.e(kClassifier, "<this>");
        k.e(list, "arguments");
        k.e(list2, "annotations");
        KClassifierImpl kClassifierImpl = kClassifier instanceof KClassifierImpl ? (KClassifierImpl) kClassifier : null;
        if (kClassifierImpl != null && (descriptor = kClassifierImpl.getDescriptor()) != null) {
            TypeConstructor n10 = descriptor.n();
            k.d(n10, "descriptor.typeConstructor");
            List<TypeParameterDescriptor> parameters = n10.getParameters();
            k.d(parameters, "typeConstructor.parameters");
            if (parameters.size() == list.size()) {
                return new KTypeImpl(a(list2.isEmpty() ? c1.f11749f.h() : c1.f11749f.h(), n10, list, z10), null, 2, null);
            }
            throw new IllegalArgumentException("Class declares " + parameters.size() + " type parameters, but " + list.size() + " were provided.");
        }
        throw new KotlinReflectionInternalError("Cannot create type for an unsupported classifier: " + kClassifier + " (" + kClassifier.getClass() + ')');
    }
}
