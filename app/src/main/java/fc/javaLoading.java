package fc;

import kotlin.collections._Collections;
import oc.FqName;

/* compiled from: javaLoading.kt */
/* renamed from: fc.p, reason: use source file name */
/* loaded from: classes2.dex */
public final class javaLoading {
    private static final boolean a(r rVar) {
        Object s02;
        FqName d10;
        s02 = _Collections.s0(rVar.l());
        b0 b0Var = (b0) s02;
        x type = b0Var != null ? b0Var.getType() : null;
        j jVar = type instanceof j ? (j) type : null;
        if (jVar == null) {
            return false;
        }
        i c10 = jVar.c();
        return (c10 instanceof g) && (d10 = ((g) c10).d()) != null && za.k.a(d10.b(), "java.lang.Object");
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:?, code lost:
    
        return r3.l().isEmpty();
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0039, code lost:
    
        if (r0.equals("toString") != false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0022, code lost:
    
        if (r0.equals("hashCode") == false) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static final boolean b(r rVar) {
        String b10 = rVar.getName().b();
        int hashCode = b10.hashCode();
        if (hashCode != -1776922004) {
            if (hashCode != -1295482945) {
                if (hashCode == 147696667) {
                }
            } else if (b10.equals("equals")) {
                return a(rVar);
            }
            return false;
        }
    }

    public static final boolean c(q qVar) {
        za.k.e(qVar, "<this>");
        return qVar.V().N() && (qVar instanceof r) && b((r) qVar);
    }
}
