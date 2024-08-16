package gd;

import java.util.HashSet;

/* compiled from: expandedTypeUtils.kt */
/* renamed from: gd.z, reason: use source file name */
/* loaded from: classes2.dex */
public final class expandedTypeUtils {
    public static final kd.i a(TypeSystemCommonBackendContext typeSystemCommonBackendContext, kd.i iVar) {
        za.k.e(typeSystemCommonBackendContext, "<this>");
        za.k.e(iVar, "inlineClassType");
        return b(typeSystemCommonBackendContext, iVar, new HashSet());
    }

    private static final kd.i b(TypeSystemCommonBackendContext typeSystemCommonBackendContext, kd.i iVar, HashSet<kd.n> hashSet) {
        kd.i b10;
        kd.i r10;
        kd.n F = typeSystemCommonBackendContext.F(iVar);
        if (!hashSet.add(F)) {
            return null;
        }
        kd.o T = typeSystemCommonBackendContext.T(F);
        if (T != null) {
            kd.i p02 = typeSystemCommonBackendContext.p0(T);
            b10 = b(typeSystemCommonBackendContext, p02, hashSet);
            if (b10 == null) {
                return null;
            }
            boolean z10 = typeSystemCommonBackendContext.L(typeSystemCommonBackendContext.F(p02)) || ((p02 instanceof kd.k) && typeSystemCommonBackendContext.p((kd.k) p02));
            if ((b10 instanceof kd.k) && typeSystemCommonBackendContext.p((kd.k) b10) && typeSystemCommonBackendContext.h0(iVar) && z10) {
                r10 = typeSystemCommonBackendContext.r(p02);
            } else if (!typeSystemCommonBackendContext.h0(b10) && typeSystemCommonBackendContext.w(iVar)) {
                r10 = typeSystemCommonBackendContext.r(b10);
            }
            return r10;
        }
        if (!typeSystemCommonBackendContext.L(F)) {
            return iVar;
        }
        kd.i A = typeSystemCommonBackendContext.A(iVar);
        if (A == null || (b10 = b(typeSystemCommonBackendContext, A, hashSet)) == null) {
            return null;
        }
        if (typeSystemCommonBackendContext.h0(iVar)) {
            return typeSystemCommonBackendContext.h0(b10) ? iVar : ((b10 instanceof kd.k) && typeSystemCommonBackendContext.p((kd.k) b10)) ? iVar : typeSystemCommonBackendContext.r(b10);
        }
        return b10;
    }
}
