package qa;

import ma.Unit;
import ma.p;
import ra.IntrinsicsJvm;
import ya.p;
import za.k;

/* compiled from: Continuation.kt */
/* loaded from: classes2.dex */
public final class f {
    public static final <R, T> void a(p<? super R, ? super d<? super T>, ? extends Object> pVar, R r10, d<? super T> dVar) {
        d a10;
        d b10;
        k.e(pVar, "<this>");
        k.e(dVar, "completion");
        a10 = IntrinsicsJvm.a(pVar, r10, dVar);
        b10 = IntrinsicsJvm.b(a10);
        p.a aVar = ma.p.f15184e;
        b10.resumeWith(ma.p.a(Unit.f15173a));
    }
}
