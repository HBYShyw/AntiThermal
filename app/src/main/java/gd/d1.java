package gd;

import gd.b1;

/* compiled from: TypeAttributes.kt */
/* loaded from: classes2.dex */
public final class d1 {
    public static final c1 a(c1 c1Var, qb.g gVar) {
        c1 m10;
        za.k.e(c1Var, "<this>");
        za.k.e(gVar, "newAnnotations");
        if (k.a(c1Var) == gVar) {
            return c1Var;
        }
        j b10 = k.b(c1Var);
        if (b10 != null && (m10 = c1Var.m(b10)) != null) {
            c1Var = m10;
        }
        return (gVar.iterator().hasNext() || !gVar.isEmpty()) ? c1Var.l(new j(gVar)) : c1Var;
    }

    public static final c1 b(qb.g gVar) {
        za.k.e(gVar, "<this>");
        return b1.a.a(o.f11859a, gVar, null, null, 6, null);
    }
}
