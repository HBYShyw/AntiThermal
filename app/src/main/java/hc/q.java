package hc;

import hc.p;
import oc.ClassId;

/* compiled from: KotlinClassFinder.kt */
/* loaded from: classes2.dex */
public final class q {
    public static final KotlinJvmBinaryClass a(p pVar, fc.g gVar) {
        za.k.e(pVar, "<this>");
        za.k.e(gVar, "javaClass");
        p.a c10 = pVar.c(gVar);
        if (c10 != null) {
            return c10.a();
        }
        return null;
    }

    public static final KotlinJvmBinaryClass b(p pVar, ClassId classId) {
        za.k.e(pVar, "<this>");
        za.k.e(classId, "classId");
        p.a b10 = pVar.b(classId);
        if (b10 != null) {
            return b10.a();
        }
        return null;
    }
}
