package hd;

import gd.a0;
import gd.d0;
import gd.h0;
import gd.i0;
import gd.o0;
import gd.v1;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import ma.NoWhenBranchMatchedException;

/* compiled from: IntersectionType.kt */
/* loaded from: classes2.dex */
public final class d {
    public static final v1 a(List<? extends v1> list) {
        Object q02;
        int u7;
        int u10;
        o0 e12;
        za.k.e(list, "types");
        int size = list.size();
        if (size == 0) {
            throw new IllegalStateException("Expected some types".toString());
        }
        if (size != 1) {
            u7 = kotlin.collections.s.u(list, 10);
            ArrayList arrayList = new ArrayList(u7);
            boolean z10 = false;
            boolean z11 = false;
            for (v1 v1Var : list) {
                z10 = z10 || i0.a(v1Var);
                if (v1Var instanceof o0) {
                    e12 = (o0) v1Var;
                } else if (v1Var instanceof a0) {
                    if (gd.w.a(v1Var)) {
                        return v1Var;
                    }
                    e12 = ((a0) v1Var).e1();
                    z11 = true;
                } else {
                    throw new NoWhenBranchMatchedException();
                }
                arrayList.add(e12);
            }
            if (z10) {
                return ErrorUtils.d(ErrorTypeKind.f12830z0, list.toString());
            }
            if (!z11) {
                return w.f12245a.c(arrayList);
            }
            u10 = kotlin.collections.s.u(list, 10);
            ArrayList arrayList2 = new ArrayList(u10);
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                arrayList2.add(d0.d((v1) it.next()));
            }
            w wVar = w.f12245a;
            return h0.d(wVar.c(arrayList), wVar.c(arrayList2));
        }
        q02 = _Collections.q0(list);
        return (v1) q02;
    }
}
