package gc;

import gd.TypeSystemCommonBackendContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.collections._Collections;
import kotlin.collections._Sets;
import oc.FqName;
import yb.b0;

/* compiled from: typeEnhancementUtils.kt */
/* renamed from: gc.s, reason: use source file name */
/* loaded from: classes2.dex */
public final class typeEnhancementUtils {
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00d2, code lost:
    
        if (r6 != false) goto L49;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final e a(e eVar, Collection<e> collection, boolean z10, boolean z11, boolean z12) {
        Set D0;
        h hVar;
        Set D02;
        boolean z13;
        boolean z14;
        Set D03;
        za.k.e(eVar, "<this>");
        za.k.e(collection, "superQualifiers");
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = collection.iterator();
        while (it.hasNext()) {
            h b10 = b((e) it.next());
            if (b10 != null) {
                arrayList.add(b10);
            }
        }
        D0 = _Collections.D0(arrayList);
        h d10 = d(D0, b(eVar), z10);
        if (d10 == null) {
            ArrayList arrayList2 = new ArrayList();
            Iterator<T> it2 = collection.iterator();
            while (it2.hasNext()) {
                h d11 = ((e) it2.next()).d();
                if (d11 != null) {
                    arrayList2.add(d11);
                }
            }
            D03 = _Collections.D0(arrayList2);
            hVar = d(D03, eVar.d(), z10);
        } else {
            hVar = d10;
        }
        ArrayList arrayList3 = new ArrayList();
        Iterator<T> it3 = collection.iterator();
        while (it3.hasNext()) {
            f c10 = ((e) it3.next()).c();
            if (c10 != null) {
                arrayList3.add(c10);
            }
        }
        D02 = _Collections.D0(arrayList3);
        f fVar = (f) e(D02, f.MUTABLE, f.READ_ONLY, eVar.c(), z10);
        h hVar2 = null;
        if (hVar != null) {
            if (!(z12 || (z11 && hVar == h.NULLABLE))) {
                hVar2 = hVar;
            }
        }
        if (hVar2 == h.NOT_NULL) {
            if (!eVar.b()) {
                if (!collection.isEmpty()) {
                    Iterator<T> it4 = collection.iterator();
                    while (it4.hasNext()) {
                        if (((e) it4.next()).b()) {
                            z14 = true;
                            break;
                        }
                    }
                }
                z14 = false;
            }
            z13 = true;
            return new e(hVar2, fVar, z13, hVar2 == null && d10 != hVar);
        }
        z13 = false;
        return new e(hVar2, fVar, z13, hVar2 == null && d10 != hVar);
    }

    private static final h b(e eVar) {
        if (eVar.e()) {
            return null;
        }
        return eVar.d();
    }

    public static final boolean c(TypeSystemCommonBackendContext typeSystemCommonBackendContext, kd.i iVar) {
        za.k.e(typeSystemCommonBackendContext, "<this>");
        za.k.e(iVar, "type");
        FqName fqName = b0.f20038u;
        za.k.d(fqName, "ENHANCED_NULLABILITY_ANNOTATION");
        return typeSystemCommonBackendContext.W(iVar, fqName);
    }

    private static final h d(Set<? extends h> set, h hVar, boolean z10) {
        h hVar2 = h.FORCE_FLEXIBILITY;
        return hVar == hVar2 ? hVar2 : (h) e(set, h.NOT_NULL, h.NULLABLE, hVar, z10);
    }

    private static final <T> T e(Set<? extends T> set, T t7, T t10, T t11, boolean z10) {
        Set l10;
        Set<? extends T> D0;
        Object r02;
        if (z10) {
            T t12 = set.contains(t7) ? t7 : set.contains(t10) ? t10 : null;
            if (za.k.a(t12, t7) && za.k.a(t11, t10)) {
                return null;
            }
            return t11 == null ? t12 : t11;
        }
        if (t11 != null) {
            l10 = _Sets.l(set, t11);
            D0 = _Collections.D0(l10);
            if (D0 != null) {
                set = D0;
            }
        }
        r02 = _Collections.r0(set);
        return (T) r02;
    }
}
