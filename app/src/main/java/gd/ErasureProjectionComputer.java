package gd;

import pb.TypeParameterDescriptor;

/* compiled from: ErasureProjectionComputer.kt */
/* renamed from: gd.x, reason: use source file name */
/* loaded from: classes2.dex */
public class ErasureProjectionComputer {
    public static /* synthetic */ TypeProjection b(ErasureProjectionComputer erasureProjectionComputer, TypeParameterDescriptor typeParameterDescriptor, ErasureTypeAttributes erasureTypeAttributes, TypeParameterUpperBoundEraser typeParameterUpperBoundEraser, g0 g0Var, int i10, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: computeProjection");
        }
        if ((i10 & 8) != 0) {
            g0Var = typeParameterUpperBoundEraser.c(typeParameterDescriptor, erasureTypeAttributes);
        }
        return erasureProjectionComputer.a(typeParameterDescriptor, erasureTypeAttributes, typeParameterUpperBoundEraser, g0Var);
    }

    public TypeProjection a(TypeParameterDescriptor typeParameterDescriptor, ErasureTypeAttributes erasureTypeAttributes, TypeParameterUpperBoundEraser typeParameterUpperBoundEraser, g0 g0Var) {
        za.k.e(typeParameterDescriptor, "parameter");
        za.k.e(erasureTypeAttributes, "typeAttr");
        za.k.e(typeParameterUpperBoundEraser, "typeParameterUpperBoundEraser");
        za.k.e(g0Var, "erasedUpperBound");
        return new TypeProjectionImpl(Variance.OUT_VARIANCE, g0Var);
    }
}
