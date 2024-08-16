package hc;

import hc.MemberSignature;
import lc.NameResolver;
import lc.ProtoBufUtil;
import lc.TypeTable;
import mc.JvmProtoBuf;
import nc.JvmMemberSignature;
import nc.JvmProtoBufUtil;
import qc.i;

/* compiled from: AbstractBinaryClassAnnotationLoader.kt */
/* loaded from: classes2.dex */
public final class c {
    public static final MemberSignature a(jc.n nVar, NameResolver nameResolver, TypeTable typeTable, boolean z10, boolean z11, boolean z12) {
        za.k.e(nVar, "proto");
        za.k.e(nameResolver, "nameResolver");
        za.k.e(typeTable, "typeTable");
        i.f<jc.n, JvmProtoBuf.d> fVar = JvmProtoBuf.f15367d;
        za.k.d(fVar, "propertySignature");
        JvmProtoBuf.d dVar = (JvmProtoBuf.d) ProtoBufUtil.a(nVar, fVar);
        if (dVar == null) {
            return null;
        }
        if (z10) {
            JvmMemberSignature.a c10 = JvmProtoBufUtil.f16006a.c(nVar, nameResolver, typeTable, z12);
            if (c10 == null) {
                return null;
            }
            return MemberSignature.f12206b.b(c10);
        }
        if (!z11 || !dVar.D()) {
            return null;
        }
        MemberSignature.a aVar = MemberSignature.f12206b;
        JvmProtoBuf.c y4 = dVar.y();
        za.k.d(y4, "signature.syntheticMethod");
        return aVar.c(nameResolver, y4);
    }

    public static /* synthetic */ MemberSignature b(jc.n nVar, NameResolver nameResolver, TypeTable typeTable, boolean z10, boolean z11, boolean z12, int i10, Object obj) {
        boolean z13 = (i10 & 8) != 0 ? false : z10;
        boolean z14 = (i10 & 16) != 0 ? false : z11;
        if ((i10 & 32) != 0) {
            z12 = true;
        }
        return a(nVar, nameResolver, typeTable, z13, z14, z12);
    }
}
