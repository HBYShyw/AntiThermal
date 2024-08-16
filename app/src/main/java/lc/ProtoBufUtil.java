package lc;

import java.util.List;
import qc.i;
import za.k;

/* compiled from: ProtoBufUtil.kt */
/* renamed from: lc.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class ProtoBufUtil {
    /* JADX WARN: Multi-variable type inference failed */
    public static final <M extends i.d<M>, T> T a(i.d<M> dVar, i.f<M, T> fVar) {
        k.e(dVar, "<this>");
        k.e(fVar, "extension");
        if (dVar.s(fVar)) {
            return (T) dVar.p(fVar);
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <M extends i.d<M>, T> T b(i.d<M> dVar, i.f<M, List<T>> fVar, int i10) {
        k.e(dVar, "<this>");
        k.e(fVar, "extension");
        if (i10 < dVar.r(fVar)) {
            return (T) dVar.q(fVar, i10);
        }
        return null;
    }
}
