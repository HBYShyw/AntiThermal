package dc;

import gd.ErasureProjectionComputer;
import gd.ErasureTypeAttributes;
import gd.TypeParameterUpperBoundEraser;
import gd.TypeProjection;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.g0;
import gd.s1;
import ma.NoWhenBranchMatchedException;
import pb.TypeParameterDescriptor;
import za.k;

/* compiled from: RawProjectionComputer.kt */
/* renamed from: dc.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class RawProjectionComputer extends ErasureProjectionComputer {

    /* compiled from: RawProjectionComputer.kt */
    /* renamed from: dc.f$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f10915a;

        static {
            int[] iArr = new int[JavaTypeFlexibility.values().length];
            try {
                iArr[JavaTypeFlexibility.FLEXIBLE_LOWER_BOUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[JavaTypeFlexibility.FLEXIBLE_UPPER_BOUND.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[JavaTypeFlexibility.INFLEXIBLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f10915a = iArr;
        }
    }

    @Override // gd.ErasureProjectionComputer
    public TypeProjection a(TypeParameterDescriptor typeParameterDescriptor, ErasureTypeAttributes erasureTypeAttributes, TypeParameterUpperBoundEraser typeParameterUpperBoundEraser, g0 g0Var) {
        TypeProjection t7;
        k.e(typeParameterDescriptor, "parameter");
        k.e(erasureTypeAttributes, "typeAttr");
        k.e(typeParameterUpperBoundEraser, "typeParameterUpperBoundEraser");
        k.e(g0Var, "erasedUpperBound");
        if (!(erasureTypeAttributes instanceof dc.a)) {
            return super.a(typeParameterDescriptor, erasureTypeAttributes, typeParameterUpperBoundEraser, g0Var);
        }
        dc.a aVar = (dc.a) erasureTypeAttributes;
        if (!aVar.i()) {
            aVar = aVar.l(JavaTypeFlexibility.INFLEXIBLE);
        }
        int i10 = a.f10915a[aVar.g().ordinal()];
        if (i10 == 1) {
            return new TypeProjectionImpl(Variance.INVARIANT, g0Var);
        }
        if (i10 != 2 && i10 != 3) {
            throw new NoWhenBranchMatchedException();
        }
        if (!typeParameterDescriptor.s().b()) {
            t7 = new TypeProjectionImpl(Variance.INVARIANT, wc.c.j(typeParameterDescriptor).H());
        } else {
            k.d(g0Var.W0().getParameters(), "erasedUpperBound.constructor.parameters");
            if (!r1.isEmpty()) {
                t7 = new TypeProjectionImpl(Variance.OUT_VARIANCE, g0Var);
            } else {
                t7 = s1.t(typeParameterDescriptor, aVar);
            }
        }
        k.d(t7, "{\n                if (!p…          }\n            }");
        return t7;
    }
}
